from time import sleep
import socketio
import eventlet
import random
import sys
import threading
from simple_chalk import redBright, yellowBright, greenBright


sio = socketio.Client(engineio_logger=False, logger=False, ssl_verify=False)
  
# event
@sio.event
def connect():
    print('sid: ', sio.get_sid())
    
@sio.event
def disconnect():
    print('Disocnnected from tc-solver')
    
@sio.event
def connect_error(self):
    print('Lost connect to server...')
    
@sio.event
def status(data):
    print(yellowBright(data["text"]))
        
# work flow function
def connect_robot():
    try:
        url = f"http://localhost:8000/socket.io/"
        sio.connect(url, transports="websocket")
    except socketio.exceptions.ConnectionError as e:
        print('Error connecting to server. Retrying in 5 seconds')
        eventlet.sleep(5)
        connect_robot()
    
def heartbeat():
      while True:
          if sio.connected:
            sio.emit('heartbeat',{"text":"ping"}, callback=heartbeat_callback)
            eventlet.sleep(5)
            

# callback
def get_solution_path_callback(data):
    print("Receive solution from tc-solver")
    for d in data["body"] :
        print("skycar ID: "+d)
        for i in data["body"][d]:
            print(i)
    sio.disconnect()

    
def heartbeat_callback(data):
    print("heartbeat: "+data['text'])

    
if __name__ == '__main__':
    serial_number = int(sys.argv[1])
    connect_robot()
        
    t1 = threading.Thread(target=heartbeat, args=())
    t1.start()
    
    # Enter to skip
    tunggu = input("Enter to request path")
    
    print("Requiring solution from tc-server...")
    sio.emit('get_solution_path',
             {"body" : 
                 {"7": {
                        "job": 167,
                        "start": [24,1,0,"x",0],
                        "pickup": [35,21,8],
                        "dropoff": [26,11,2],
                        "PU_rank": 1,
                        "DO_rank": 1
                        }
                  }
                 }, callback=get_solution_path_callback)

    # sio.emit('create_order',
    #          {"body":
    #              {'id':1}
    #              })

    
