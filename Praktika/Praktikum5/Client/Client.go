package main

import (
	"bufio"
	"fmt"
	"log"
	"net/url"
	"os"
	"src/github.com/gorilla/websocket"
	"strings"
	"sync"
)

type Ticket struct {
	Id int `json:"id"`
	Owner Client `json:"owner"`
}

type Client struct {
	Name    string   `json:"name"`
}

type Message struct {
	Mt string `json:"mt"`
	Client Client `json:"client"`
	Tickets []Ticket `json:"tickets"`
}

var mutex = &sync.Mutex{}
var client Client
var tickets []Ticket

func assign(id int, conn *websocket.Conn) {
	for i,ticket := range tickets {
		if ticket.Id == id {
			mutex.Lock()
			tickets[i].Owner = client
			mutex.Unlock()
			break
		}
	}

	printInfo(conn)
	go update(conn)
}

func update(conn *websocket.Conn) {
	message := Message{Mt: "tickets", Tickets: tickets}

	err := conn.WriteJSON(message)

	if err != nil {
		log.Println("failed to send updated ticket list to server", err.Error())
		return
	}
}

func printInfo(conn *websocket.Conn) {
	fmt.Println("Tickets:")

	if len(tickets) > 0 {
		owner := ""
		for i, ticket := range tickets {
			if ticket.Owner.Name == "" {
				owner = "Not assigned"
			} else {
				owner = ticket.Owner.Name
			}

			fmt.Printf("%d: ticket%d -> %s \n", i + 1, ticket.Id, owner)
		}
	} else {
		fmt.Println("No tickets")
	}

	go handleUserAction(conn)
}

func handleUserAction(conn *websocket.Conn) {
	fmt.Println("number: self assign, q: quit")

	reader := bufio.NewReader(os.Stdin)

	action, _, err := reader.ReadRune()

	if err != nil {
		log.Println("failed to read user input", err.Error())
		return
	}

	fmt.Println(action)

	switch {
	case action > '0' && action < '9':
		id := int(action - '0')
		assign(id, conn)
		break
	case action == 'q':
		quit(conn)
		break
	default:
		break
	}
}

func quit(conn *websocket.Conn)  {
	err := conn.Close()

	if err != nil {
		log.Println("failed to close connection to server", err.Error())
		return
	}

	os.Exit(0)
}

func register(conn *websocket.Conn) {
	message := Message{Mt: "client", Client: client}

	err := conn.WriteJSON(message)

	if err != nil {
		log.Println("failed to register", err.Error())
		return
	}
}

func main() {
	u := url.URL{Scheme: "ws", Host: "localhost:8080", Path: "/"}

	c, _, err := websocket.DefaultDialer.Dial(u.String(), nil)

	if err != nil {
		log.Fatal("failed to build WebSocket connection", err.Error())
		return
	}

	defer c.Close()

	fmt.Println("Client started")
	fmt.Println("Please enter Client ID: ")

	reader := bufio.NewReader(os.Stdin)
	clientID, _ := reader.ReadString('\n')
	clientID = strings.Replace(clientID, "\r\n", "", -1)
	clientID = strings.Replace(clientID, "\n", "", -1)

	client.Name = clientID

	fmt.Println("Client:", client.Name)

	register(c)
	printInfo(c)

	for {
		message := &Message{}

		err = c.ReadJSON(message)

		if err != nil {
			log.Println("failed to read message", err.Error())
			return
		}

		if message.Mt == "tickets" {
			tickets = message.Tickets
		}

		printInfo(c)
	}
}
