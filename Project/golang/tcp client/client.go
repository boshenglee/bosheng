// author: BO

package main

import (
	"fmt"
	"net"
	"strconv"
	"strings"
)

const (
	HOST = "localhost"
	PORT = "2525"
	TYPE = "tcp"
)

func main() {

	var connect bool = true

	c, err := net.Dial(TYPE, HOST+":"+PORT)
	if err != nil {
		fmt.Println(err)
		return
	} else {
		connect = true
	}
	sendMessage("ST,1,P,Pairing", c)
	for {
		buffer := make([]byte, 1024)
		data, err := c.Read(buffer)
		if err != nil {
			fmt.Println("Error reading:", err.Error())
		}
		var stringData string = string(buffer[:data])
		fmt.Println("Received: ", stringData)
		split := strings.Split(stringData, ",")
		stationNum, err := strconv.Atoi(split[1])

		fmt.Printf("Station Number: %d\n", stationNum)
		switch split[2] {
		case "D":
			fmt.Println("This is a disconnect command")
			sendMessage("disconnect", c)
			connect = false
			break
		case "J":
			fmt.Println("This is a job done command")
		case "R":
			fmt.Println("This is a request command")
		case "P":
			if split[3] == "Success" {
				fmt.Println("Pair successfully")
				sendMessage("ST,1,M", c)
			}
		default:
			fmt.Println("Invalid command")
		}

		if !connect {
			break
		}
	}
	c.Close()

}

func sendMessage(msg string, c net.Conn) {
	_, err := c.Write([]byte(msg))
	if err != nil {
		fmt.Println("Error sending message")
		return
	}
	fmt.Println("Send: " + msg)
}
