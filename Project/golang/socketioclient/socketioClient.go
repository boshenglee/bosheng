// author : BO

package main

import (
	"fmt"
	"log"
	"time"

	socketio_client "github.com/zhouhui8915/go-socket.io-client"
)

func main() {

	opts := &socketio_client.Options{
		Transport: "websocket",
	}
	uri := "http://localhost:8000/station/"

	client, err := socketio_client.NewClient(uri, opts)
	if err != nil {
		log.Printf("NewClient error:%v\n", err)
		return
	}

	client.On("error", func() {
		log.Printf("on error\n")
	})
	client.On("connection", func() {
		log.Printf("on connect\n")
	})
	client.On("heartbeat", func(msg string) {
		client.Emit("message", "hi")
		log.Printf("heartbeat: " + msg)
	})
	client.On("message", func(msg string) {
		log.Printf("on message:%v\n", msg)
	})
	client.On("disconnection", func() {
		log.Printf("on disconnect\n")
	})

	go heartbeat(client)

	// reader := bufio.NewReader(os.Stdin)

}

func heartbeat(client *socketio_client.Client) {
	for {
		// data, _, _ := reader.ReadLine()
		command := "ping"
		client.Emit("heartbeat", command)
		fmt.Printf("send heartbeat:%v\n", command)

		time.Sleep(5 * time.Second)
	}
}
