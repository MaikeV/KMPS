package main

import (
	"fmt"
	"fyne.io/fyne/widget"
	"strconv"
	"time"
	"fyne.io/fyne/app"
)

func calc() int {
	time.Sleep(10 * time.Second)

	fmt.Println()
	return 42
}

func main() {
	myApp := app.New()
	myWindow := myApp.NewWindow("Hello")

	hello := widget.NewLabel("Hello Fyne!")
	myWindow.SetContent(widget.NewVBox(
		hello,
		widget.NewButton("Calculate", func() {
			fmt.Println("Button wurde geklickt")
			hello.Text = strconv.Itoa(calc())
		})))

	myWindow.ShowAndRun()
}
