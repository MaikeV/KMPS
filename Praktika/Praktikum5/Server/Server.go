package main

import (
	"bufio"
	"fmt"
	"github.com/gorilla/websocket"
	"log"
	"net/http"
	"os"
	"strings"
	"sync"
)

type Ticket struct {
	Id int `json:"id"`
	Owner Client `json:"owner"`
}

type Client struct {
	Name string `json:"name"`
	conn *websocket.Conn
}

type Message struct {
	Mt string `json:"mt"`
	Client Client `json:"client"`
	Tickets []Ticket `json:"tickets"`
}

var mutex = &sync.Mutex{}
var tickets []Ticket
var clients []Client

var upgrader = websocket.Upgrader{}

func createNewTicket(conn *websocket.Conn) {
	emptyOwner := Client{Name: ""}
	ticket := Ticket{Id: len(tickets) + 1, Owner: emptyOwner}

	tickets = append(tickets, ticket)

	printTickets(conn)

	message := &Message{Mt: "tickets", Tickets: tickets}

	err := publish(message)

	if err != nil {
		log.Println("failed to send updated ticket list", err.Error())
	}
}

func printTickets(conn *websocket.Conn) {
	if len(tickets) > 0 {
		fmt.Println("\nTickets:")
		for _, ticket := range tickets {
			owner := ""

			if ticket.Owner.Name == "" {
				owner = "unassigned"
			} else {
				owner = ticket.Owner.Name
			}

			fmt.Printf("ticket%d -> %s \n", ticket.Id, owner)
		}
	} else {
		fmt.Println("No tickets")
	}
	go handleUserAction(conn)
}

func handleUserAction(conn *websocket.Conn) {
	fmt.Println("n: new Ticket, q: Quit")

	reader := bufio.NewReader(os.Stdin)
	action, _, err := reader.ReadRune()

	if err != nil {
		log.Println("failed to read user input", err.Error())
		return
	}

	switch action {
	case 'n':
		createNewTicket(conn)
		break
	case 'q':
		quit(conn)
		break
	default:
		break
	}
}

func handler(rw http.ResponseWriter, r *http.Request) {
	c, err := upgrader.Upgrade(rw, r, nil)

	if err != nil {
		log.Println("failed to upgrade connection to WebSocket protocol",err.Error())
		return
	}

	defer c.Close()

	printTickets(c)

	for {
		message := &Message{}

		err := c.ReadJSON(message)

		if err != nil {
			log.Println("failed to read message from client", err.Error())
			return
		}

		go func() {
			if message.Mt == "client" {
				cl := message.Client
				cl.conn = c

				isNewClient := true

				for i, client := range clients {
					if strings.EqualFold(client.Name, cl.Name) {
						clients[i] = cl
						isNewClient = false
						break
					}
				}

				if isNewClient {
					clients = append(clients, cl)
				}

				fmt.Printf("\nNew Client: %s\n", cl.Name)

				msg := &Message{Mt: "tickets", Tickets: tickets}

				err = publish(msg)

				if err != nil {
					log.Println("failed to send ticket list to new client:", err.Error())
				}
			} else if message.Mt == "tickets" {
				tickets = message.Tickets

				msg := &Message{Mt: "tickets", Tickets: tickets}

				err = publish(msg)

				if err != nil {
					log.Println("failed to send updated ticket list to clients:", err.Error())
				}
			}
			printTickets(c)
		}()
	}
}

func publish(msg *Message) error {
	for _, client := range clients {
		mutex.Lock()
		err := client.conn.WriteJSON(msg)
		mutex.Unlock()

		if err != nil {
			log.Println("failed to publish message:", err.Error())
			return err
		}
	}

	return nil
}

func quit(conn *websocket.Conn)  {
	err := conn.Close()

	if err != nil {
		log.Println("failed to close WebSocket", err.Error())
		return
	}

	os.Exit(0)
}

func main() {
	fmt.Println("Server started")

	http.HandleFunc("/", handler)
	err := http.ListenAndServe("localhost:8080", nil)

	if err != nil {
		log.Fatal("failed to start server", err.Error())
	}
}
