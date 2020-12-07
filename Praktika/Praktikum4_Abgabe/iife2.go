package main

import (
	"fmt"
	"time"
)

func main() {
	for i := 0; i < 10; i++ {
		go func(j int) {
			fmt.Println("Hello from Goroutine", j, &j)
			time.Sleep(2 * time.Second)
			fmt.Println("Goroutine ends", j, &j)
		}(i)
	}
	time.Sleep(10 * time.Second)

	fmt.Println("Done")
}