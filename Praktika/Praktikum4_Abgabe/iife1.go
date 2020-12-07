package main

import "fmt"
import "time"

func main() {
	for i := 0; i < 10; i++ {
		go func() {
			fmt.Println("Hello from Goroutine", i, &i)
			time.Sleep(2 * time.Second)
			fmt.Println("Goroutine ends", i, &i)
		}()
	}
	time.Sleep(10 * time.Second)
	
	fmt.Println("Done")
}

//In der Ausgabe sind zuerst alle "Hello from Goroutine"s zu sehen und
//dann erst die "Goroutines ends"s
//Der Effekt tritt auf, weil die Schleife weiter laeuft, waehrend parallel
//dazu die goroutines laufen
//Ausserdem wird als Index immer 10 ausgegeben, da Schleife weiterlaeuft,
//waehrend die Groutinen "darin" laufen und der Zaehler seinen Endwert erreicht,
//bevor die Ausgaben der Goroutine stattfinden