package main

import (
	"fmt"
	"time"
)

func main() {
	ch := make(chan int)

	go func() {
		fmt.Println("Waiting for value from channel")
		val := <-ch
		fmt.Println("Value:", val)
	}()

	time.Sleep(5 * time.Second)
}

//Goroutine blockiert waehrend sie auf Input vom Channel wartet
//Da die Funktion allerdings endet, bevor ein Signal vom Channel kommt,
//bleibt die Goroutine blockiert und das fmt.Println, welches den value
//ausgeben soll wird niemals ausgefuehrt