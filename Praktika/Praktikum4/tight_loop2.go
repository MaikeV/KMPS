package main

import (
	"fmt"
	"runtime"
	"time"
)

func main() {
	var x int
	procs := runtime.GOMAXPROCS(0) - 1
	fmt.Println("Anzahl Prozessoren:", procs)

	for i := 0; i < procs; i++ {
		go func() {
			for { x++ }
		}()
	}

	time.Sleep(time.Second)
	fmt.Println("x =", x)
}