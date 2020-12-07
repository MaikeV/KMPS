package main

import (
	"fmt"
	"os"
	"runtime"
	"runtime/trace"
	"time"
)

func f(myName string) {
	for i := 0; i < 10; i++ {
		fmt.Println(myName, ": ", i)
	}
}

func main() {
	runtime.GOMAXPROCS(1)
	fmt.Printf("runtime.GOMAXPROCS(0) returned %d CPUs\n", runtime.GOMAXPROCS(0))
	//fmt.Printf("runtime.NumCPU() returned %d CPUs\n", runtime.NumCPU())

	trace.Start(os.Stderr)
	defer trace.Stop()

	go f("goroutine1")
	go f("goroutine2")
	go f("goroutine3")

	time.Sleep(10 * time.Second)
}

//go run sched1.go 2> trace.out
//go tool trace trace.out