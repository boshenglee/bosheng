// author : BO

package main

import (
	"fmt"
	"log"
	"net/http"

	socketio "github.com/googollee/go-socket.io"
)

func main() {
	server := socketio.NewServer(nil)

	// server.OnEvent("/station", "connect", func(s socketio.Conn) {
	// 	fmt.Println("connected:", s.ID())
	// })

	server.OnConnect("/", func(s socketio.Conn) error {
		fmt.Println("connected:", s.ID())
		return nil
	})

	server.OnError("/", func(s socketio.Conn, e error) {
		fmt.Println("meet error:", e)
	})

	server.OnDisconnect("/", func(s socketio.Conn, reason string) {
		fmt.Println("closed: ", reason)
	})

	server.OnEvent("/station", "register_robot", func(s socketio.Conn, data string) {
		s.SetContext(data)
		fmt.Println("harlo robot")

	})

	go server.Serve()
	defer server.Close()

	http.Handle("/", server)
	log.Println("Serving at localhost:8000...")
	log.Fatal(http.ListenAndServe(":8000", nil))
}
