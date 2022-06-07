// author : BO

package main

import (
	"fmt"
	"net"
	"strconv"
	"strings"
	"sync"
)

const (
	HOST = "localhost"
	PORT = "2525"
	TYPE = "tcp"
)

var wg sync.WaitGroup

func main() {
	l, err := net.Listen(TYPE, HOST+":"+PORT)
	if err != nil {
		fmt.Println(err)
		return
	}
	defer l.Close()

	wg.Add(1)

	for {
		c, err := l.Accept()
		if err != nil {
			fmt.Println(err)
			return
		}

		go handle(c)

		wg.Wait()

		fmt.Println("Done")
	}
}

func handle(c net.Conn) {
	defer wg.Done()

	for {
		buffer := make([]byte, 1024)
		data, err := c.Read(buffer)
		if err != nil {
			fmt.Println("Error reading:", err.Error())
		}
		var stringData string = string(buffer[:data])

		if strings.Contains(stringData, "disconnect") {
			fmt.Println("Disconnecting...")
			c.Close()
			break
		} else {
			fmt.Println("Received: ", stringData)
			split := strings.Split(stringData, ",")
			stationNum, err := strconv.Atoi(split[1])
			if err != nil {
				fmt.Println("Error converting")
				return
			}
			fmt.Printf("Station Number: %d\n", stationNum)
			switch split[2] {
			case "M":
				fmt.Println("This is a move command")
				sendMessage("ST,1,D,Disconnect", c)
			case "J":
				fmt.Println("This is a job done command")
			case "R":
				fmt.Println("This is a request command")
			case "P":
				fmt.Println("This is a pairing command")
				sendMessage("ST,1,P,Success", c)
			default:
				fmt.Println("Invalid command")
			}

		}
	}
}

func sendMessage(msg string, c net.Conn) {
	_, err := c.Write([]byte(msg))
	if err != nil {
		fmt.Println("Error sending message")
		return
	}
	fmt.Println("Send: " + msg)
}

func cubaCuba(name string, age int) int {

	fmt.Printf("Hi, %s I am %d years old\n", name, age)

	return age
}
