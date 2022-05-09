from time import sleep
import socketio
import eventlet
import random
import sys
from simple_chalk import redBright, yellowBright, greenBright


sio = socketio.Client()
  
  
@sio.event
def connect():
    print('This robot sid is', sio.get_sid())
    register_robot(sio.get_sid())
    
@sio.event
def connect_error(self):
    print('Lost connect to server...')
  
@sio.event
def fail_register_rbt(message):
    print(redBright(message))
    disconnect_robot()

@sio.event
def job_arrived(data):
    callback_data = {
            'status': '200',
            'job_id': data["id"],
        }
    print("Receive Job ID %d, to destination (%d , %d)" %(data["id"],data["destination_x"],data["destination_y"]))
    return callback_data
    
@sio.event
def set_unavailable(data):
    callback_data ={
        'status': '200',
        'robot_id': data['robot_id'],
        'job_id': data['job_id']
    }
    print("This robot is now unavailable")
    return callback_data

@sio.event
def set_available(data):
    callback_data ={
        'status': '200',
        'robot_id': data['robot_id'],
        'job_id': data['job_id']
    }
    print("This robot is now available")
    return callback_data

@sio.event
def task_assign(data):
    callback_data = {
            'status': '200',
            'robot_id':data['robot_id'],
            'job_id':data['job_id'],
            'direction': data['direction'],
        }
    print("Instruction Received: Move "+data['direction'])
    eventlet.sleep(5)
    return callback_data

@sio.event
def rotate_axis(data):
    callback_data = {
            'status': '200',
            'job_id':data['job_id'],
            'robot_id':data['robot_id'],
            'to_axis':data['to_axis']
        }
    print("Rotating axis...")
    eventlet.sleep(5)
    return callback_data

@sio.event
def reach_destination(data):
    callback_data ={
        'status': '200',
        'job_id':data['job_id'],
        'robot_id':data['robot_id']
    }
    print('Reach destination!')
    return callback_data

@sio.event
def start_charging(data):
    callback_data ={
        'status': '200',
        'job_id':data['job_id'],
        'robot_id':data['robot_id'],
        'robot_sid':data['robot_sid']
    }
    print('Start charging...')
    return callback_data

@sio.event
def done_charging(data):
    callback_data ={
        'status': '200',
        'job_id':data['job_id'],
        'robot_id':data['robot_id'],
        'robot_sid':data['robot_sid'],
        'charge_id':data['charge_id']
    }
    print('Finish Charging!')
    return callback_data

@sio.event
def job_done(data):
    callback_data ={
        'status': '200',
        'job_id':data['job_id'],
        'robot_id':data['robot_id']
    }
    print(greenBright('This robot is now available'))
    return callback_data


def connect_robot():
    try:
        url = f'http://127.0.0.1:5000'
        sio.connect(url)
    except socketio.exceptions.ConnectionError as e:
        print('Error connecting to visualisation dashboard. Retrying in 5 seconds')
        eventlet.sleep(5)
        connect_robot()
    
def register_robot(sid):
    try:
        coor_x = random.randint(0,7)
        coor_y = random.randint(0,7)
        sio.emit('register_robot',
                    {'sid':sid,
                    'location_x':coor_x,
                    'location_y':coor_y,
                    'availability': True, 
                    'serial_number':serial_number,
                    'axis': 'X'
                    }
                )
    except Exception as e:
         print(redBright('Job Register Error: '), e)
    
def disconnect_robot():
    print("Disconnected")
    sio.disconnect()
    
if __name__ == '__main__':
    serial_number = int(sys.argv[1])
    connect_robot()

    
