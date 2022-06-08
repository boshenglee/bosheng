package main

import (
	"log"
	"time"

	socketio "github.com/ambelovsky/gosf"
)

func init() {

	socketio.OnConnect(func(client *socketio.Client, request *socketio.Request) {
		log.Println("Client connected.")
		client.Join("tb")
	})

	socketio.OnDisconnect(func(client *socketio.Client, request *socketio.Request) {
		log.Println("Client disconnected.")
	})

	// Listen on an endpoint
	socketio.Listen("get_solution_path", getSolutionPath)
	socketio.Listen("heartbeat", heartbeat)
}

func heartbeat(client *socketio.Client, request *socketio.Request) *socketio.Message {
	if request.Message.Text == "ping" {
		log.Println("heartbeat | ping")
		return socketio.NewSuccessMessage("pong")
	} else {
		return socketio.NewFailureMessage("wrong protocol")
	}
}

func getSolutionPath(client *socketio.Client, request *socketio.Request) *socketio.Message {

	//access info received
	for t, i := range request.Message.Body {
		log.Println("Skycar ID: " + t)
		var info map[string]interface{} = i.(map[string]interface{})
		log.Println(info["job"])
		log.Println(info["start"])
		log.Println(info["pickup"])
		log.Println(info["dropoff"])
		log.Println(info["PU_rank"])
		log.Println(info["DO_rank"])
	}

	// START CALCULATING
	log.Println("Calculating path...")
	socketio.Broadcast("tc", "status", create_message("Calculating Path..."))
	time.Sleep(5 * time.Second)
	log.Println("Done calculation")
	socketio.Broadcast("tc", "status", create_message("Calculation Done"))
	// CALCULATE FINISH

	//mock data
	data1 := []interface{}{24, 1, 0, "x", 0}
	data2 := []interface{}{25, 1, 0, "x", 1}
	data3 := []interface{}{26, 1, 0, "x", 2}
	totalData := [][]interface{}{data1, data2, data3}

	callback_data := map[string]interface{}{
		"7": totalData,
		"8": totalData,
	}
	return create_message(callback_data)
}

//create standard message according to the arguments type
func create_message(any interface{}) *socketio.Message {
	message := new(socketio.Message)
	message.Success = true

	switch any.(type) {
	case string:
		message.Text = any.(string)
	case map[string]interface{}:
		message.Body = any.(map[string]interface{})
	}

	return message
}

func main() {
	// Start the server using a basic configuration
	socketio.Startup(map[string]interface{}{"port": 8000})
}
