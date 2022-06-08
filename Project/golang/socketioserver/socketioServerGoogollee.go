// // author : BO

// package main

// import (
// 	"fmt"
// 	"log"
// 	"net/http"

// 	socketio "github.com/googollee/go-socket.io"
// )

// func main() {
// 	server := socketio.NewServer(nil)

// 	server.OnConnect("/station", func(s socketio.Conn) error {
// 		fmt.Println("connected:", s.ID())
// 		return nil
// 	})

// 	server.OnError("/", func(s socketio.Conn, e error) {
// 		fmt.Println("meet error:", e)
// 	})

// 	server.OnDisconnect("/", func(s socketio.Conn, reason string) {
// 		fmt.Println("closed: ", reason)
// 	})

// 	server.OnEvent("/", "register_robot", func(s socketio.Conn, data map[string]interface{}) {
// 		fmt.Println(data["id"])
// 		returnData := map[string]interface{}{
// 			"intValue":    1234,
// 			"boolValue":   true,
// 			"stringValue": "hello!",
// 		}
// 		s.Emit("fail_register_rbt", returnData)
// 	})

// 	server.OnEvent("/", "create_order", func(s socketio.Conn, data map[string]interface{}) string {
// 		fmt.Println(data["id"])

// 		return "ACK"
// 	})

// 	server.OnEvent("/", "heartbeat", func(s socketio.Conn, data string) string {
// 		fmt.Println("heartbeat: " + data)

// 		return "pong"
// 	})

// 	go server.Serve()
// 	defer server.Close()

// 	http.Handle("/socket.io/", server)
// 	log.Println("Serving at localhost:8000...")
// 	log.Fatal(http.ListenAndServe(":8000", nil))
// }
