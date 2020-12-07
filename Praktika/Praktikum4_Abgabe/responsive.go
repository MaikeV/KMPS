package main

import (
	"fmt"
	"fyne.io/fyne/app"
	"fyne.io/fyne/widget"
	"strconv"
	"time"
)

func calcAsync(c chan int)  {
	time.Sleep(10 * time.Second)

	c<-42
}

func main() {
	myApp := app.New()
	myWindow := myApp.NewWindow("Hello")
	val := make(chan int)

	hello := widget.NewLabel("Hello Fyne!")
	myWindow.SetContent(widget.NewVBox(
		hello,
		widget.NewButton("Calculate", func() {
			fmt.Println("Button wurde geklickt")
			go calcAsync(val)
			go func() {
				hello.Text = strconv.Itoa(<-val)
			}()
		})))

	myWindow.ShowAndRun()
}