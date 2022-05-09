import os
import json
import enum
import socket
from sys import is_finalizing
import eventlet
import threading
from tokenize import String
from threading import Thread
from datetime import datetime
from dotenv import load_dotenv
from pkg_resources import require
from flask_migrate import Migrate
from sqlalchemy.orm import backref
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.sql.schema import ForeignKey
from flask_restful import Api, Resource, reqparse
from simple_chalk import redBright, yellowBright, blueBright
from sqlalchemy import Integer, func, DateTime, Enum, Boolean
from flask_socketio import SocketIO, send, emit, join_room, leave_room
from flask import Flask,request, flash, url_for, redirect, render_template, jsonify


GRID_ROW = 8
GRID_COL = 8
CS_STATION_X = 7
CS_STATION_Y = 7
NUM_LIMIT_OF_ROBOTS = 3
TCP_PORT = 3535
TCP_HOST = 'localhost'
HEADER = 64
FORMAT = 'utf-8'

app = Flask(__name__)
api = Api(app)
app.secret_key = os.getenv("SECRET_KEY")
socketio = SocketIO(app,async_mode='eventlet')

# tcp server settings
tcp_server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_address = (TCP_HOST, TCP_PORT)
tcp_server.bind(server_address)

#configuration
app.config['SQLALCHEMY_DATABASE_URI'] = 'postgresql://postgres:password@localhost:5432/little_project'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False


#cretae database object
db = SQLAlchemy(app)
migrate = Migrate(app,db)

#set up grid
grid = [[True for i in range(GRID_COL)] for j in range(GRID_ROW)]
# make gird[7][7] the charging station
grid[GRID_ROW-1][GRID_COL-1] = False 

# enum type for job
class JobType(enum.Enum):
    NORMAL = 'NORMAL'
    CHARGING = 'CHARGING'
    
    def __str__(self):
        return self.value

#initialize db
class Robot(db.Model):
   __tablename__ = 'robot'
   id = db.Column(db.Integer(), primary_key = True)
   serial_number = db.Column(db.Integer())
   location_x = db.Column(db.Integer())
   location_y = db.Column(db.Integer())
   availability = db.Column(db.Boolean, default = True)
   sid = db.Column(db.String(30))
   axis = db.Column(db.String(1))
   job = db.relationship('Job',backref='robot')
   movement = db.relationship('Movement',backref='robot')
   charge = db.relationship('Charge',backref='robot')
   __table_args__ = (db.UniqueConstraint('serial_number'), )


   def __init__(self, location_x,location_y,serial_number, availability,axis,sid):
    self.sid = sid
    self.location_x = location_x
    self.location_y = location_y
    self.serial_number = serial_number
    self.availability = availability
    self.axis = axis

class Job(db.Model):
    __tablename__ = 'job'
    id = db.Column(db.Integer(), primary_key = True)
    type = db.Column(Enum(JobType))
    assign_to = db.Column(db.Integer())
    destination_x = db.Column(db.Integer())
    destination_y = db.Column(db.Integer())
    status = db.Column(db.String(30), default = "Waiting")
    start_date = db.Column(db.DateTime)
    end_date = db.Column(db.DateTime)
    last_update = db.Column(db.DateTime, default = datetime.now())
    robot_id = db.Column(db.Integer(),db.ForeignKey('robot.id'))
    movement = db.relationship('Movement',backref='job')
    charge = db.relationship('Charge',backref='job')

    def __init__(self, type, assign_to, destination_x,destination_y, status, start_date, end_date, last_update, robot_id):
        self.type = type
        self.assign_to = assign_to
        self.destination_x = destination_x
        self.destination_y = destination_y
        self.status = status
        self.start_date = start_date
        self.end_date = end_date
        self.last_update = last_update
        self.robot_id = robot_id
        
      
    def obj_to_dict(self):
        return{
            "id": self.id,
            "type": self.type,
            "assign_to": self.assign_to,
            "destination_x": self.destination_x,
            "destination_y": self.destination_y,
            "status": self.status,
            "start_date":self.start_date,
            "end_date":self.end_date,
            "last_update":self.last_update,
            "robot_id": self.robot_id,
            
        }

class Movement(db.Model):
    __tablename__= 'movement'
    id = db.Column(db.Integer(), primary_key = True)
    direction = db.Column(db.String(10))
    job_id = db.Column(db.Integer(),db.ForeignKey('job.id'))
    robot_id = db.Column(db.Integer(),db.ForeignKey('robot.id'))

    def __init__(self,direction, job_id, robot_id):
      self.direction = direction
      self.job_id = job_id
      self.robot_id = robot_id
      
class Charge(db.Model):
    __tablename__='charge'
    id = db.Column(db.Integer(), primary_key = True)
    job_id = db.Column(db.Integer(),db.ForeignKey('job.id'))
    robot_id = db.Column(db.Integer(),db.ForeignKey('robot.id'))
    start_date = db.Column(db.DateTime)
    end_date = db.Column(db.DateTime)
    
    def __init__(self,job_id,robot_id,start_date,end_date):
        self.job_id = job_id
        self.robot_id = robot_id
        self.start_date = start_date
        self.end_date = end_date
    
#resource class for api
class NewJobs(Resource):
    def put(self):
        data = request.data
        data_args = reqparse.RequestParser()
        data_args.add_argument("type", choices=list(map(lambda x: x.value, JobType._member_map_.values())), help='type of the job is required',required = True)
        data_args.add_argument("assign_to", type = int, required = False)
        data_args.add_argument("destination_x", type = int, required = False)
        data_args.add_argument("destination_y", type = int, required = False)
        data_args.add_argument("status",required = False)
        data_args.add_argument('start_date',required = False)
        data_args.add_argument('end_date', required=False)
        data_args.add_argument('last_update', required= False)
        data_args.add_argument("robot_id", type = int, required = False)
        data = data_args.parse_args()
        print("[sys]Job arrived")
        if data['type'] == 'NORMAL':
            if data['destination_x'] is None or data['destination_y'] is None:
                print('No destination for normal job')
                print(redBright("[sys]Job rejected"))
                return 'No destination for normal job', 404
            if data["destination_x"] > 7 or data["destination_y"] > 7 or (data["destination_x"] == 7 and data["destination_y"]== 7):
                print("[sys]Invalid destination of job")
                print(redBright("[sys]Job rejected"))
                return "Invalid destination of job", 404
            if not grid[data["destination_x"]][data["destination_y"]]:
                print("[sys]A robot is already at the destination")
                print(redBright("[sys]Job rejected"))
                return "A robot is already at the destination", 404
                  
        if data['type']=='CHARGING':
            if not data['assign_to']:
                print('[sys]Charging job but no assignee')
                print(redBright("[sys]Job rejected"))
                return "Invalid charging job", 404
            data["destination_x"] = CS_STATION_X
            data["destination_y"] = CS_STATION_Y
        try:
            jb = Job(data['type'],data['assign_to'],data["destination_x"],data["destination_y"],data["status"],data['start_date'],data['end_date'],datetime.now(),data["robot_id"])
            db.session.add(jb)
            db.session.commit()
            socketio.start_background_task(target=assign_job(jb))
        except Exception as e:
            jb.status = 'Error'
            db.session.commit()
            return 404
        else:
            return 200
        
# ASK!!!!!!!!
# 
# 
#  Query joining table cannot get robot info
class ChargeStatus(Resource):
    def get(self):
        data = request.json
        return_data = []
        
        if not data is None:
            if 'robot_id' in data:
                rbt_charge = db.session.query(Charge).filter(Charge.robot_id == data['robot_id'])
                num_of_charge = rbt_charge.count()
                total_charge_time = 0.0
                for charge in rbt_charge.all():
                    charge_duration = charge.end_date - charge.start_date
                    total_charge_time += charge_duration.seconds
                return_data.append({"Robot ID":charge.robot.id})
                return_data.append({"Robot Serial Number":charge.robot.serial_number})
                return_data.append({"Total Number of Charge":num_of_charge}) 
                return_data.append({"Total Charge Time": total_charge_time})
                return_data.append({"Average Charge Time":total_charge_time/num_of_charge})
                
                return return_data
                
        rbt_charge = Charge.query
        total_num_of_charge = rbt_charge.count()
        total_charge_time = 0.0
        for charge in rbt_charge.all():
            charge_duration = charge.end_date - charge.start_date
            total_charge_time += charge_duration.seconds
        return_data.append({"Robot ID":charge.robot.id})
        return_data.append({"Robot ID":charge.robot.serial_number})
        return_data.append({"Total Number of Charge":total_num_of_charge})
        return_data.append({"Total Charge Time": total_charge_time})
        return_data.append({"Avergae Charge Time":total_charge_time/total_num_of_charge})
            
        return return_data
     
#start assign job after receiving job
def assign_job(jb):
    try:
        correct_rbt = get_correct_rbt(jb)
        jb.robot_id = correct_rbt.id
        print("[sys]Robot ID %d has picked up the job" %(jb.robot_id))
        jb_data = {
            "id": jb.id,
            "destination_x": jb.destination_x,
            "destination_y": jb.destination_y
        }
        socketio.emit('job_arrived',jb_data, to=correct_rbt.sid, callback=job_arrived_callback)
        db.session.commit()
    except Exception as e:
        print(redBright('Job Assign Error: '), e)
        raise e

def get_correct_rbt(jb):
    if jb.assign_to is not None:
        assigned_rbt = Robot.query.filter_by(id = jb.assign_to).first()
        if assigned_rbt is None:
            print(redBright(f'[sys]No robot with ID {jb.assign_to}'))
            raise Exception("Assigned robot error")
            return assigned_rbt
        else:
            if assigned_rbt.availability == False:
                print(redBright('[sys]Robot ID %d is currently not available '%(jb.assign_to)))
                raise Exception('Assigned robot error')
                return None
            else:
                return assigned_rbt
    else:
        available_robot = Robot.query.filter_by(availability = True)
        closest_robot = available_robot.first()
        if closest_robot is None:
            print(redBright("[sys]No available robot for Job ID %d" %(jb.id)))
            raise Exception('Assigned robot error')
        else:
            # find closest robot
            min_distance = 10000
            for rbt in available_robot:
                distance = abs(jb.destination_x - rbt.location_x) + abs(jb.destination_y - rbt.location_y)
                if(distance<min_distance):
                    min_distance = distance
                    closest_robot = rbt
            return closest_robot  
    
def set_unavailable(jb_id):
    try:
        jb = Job.query.filter_by(id=jb_id).first()
        rbt =Robot.query.filter_by(id=jb.robot_id).first()
        rbt.availability = False
        socketio.emit('set_unavailable',{'robot_id':rbt.id, 'job_id':jb.id}, to=rbt.sid,callback=set_unavailable_callback)
        db.session.commit()
    except Exception as e:
          print(redBright('Set Unavailable Error: '), e)
          
def set_available(jb_id):
    try:
        jb = Job.query.filter_by(id=jb_id).first()
        rbt =Robot.query.filter_by(id=jb.robot_id).first()
        rbt.availability = True
        socketio.emit('set_available',{'robot_id':rbt.id, 'job_id':jb.id}, to=rbt.sid,callback=set_available_callback)
        db.session.commit()
    except Exception as e:
          print(redBright('Set Available Error: '), e)
    
def start_job(jb_id):
    try:
        jb = Job.query.filter_by(id=jb_id).first()
        jb.start_date = datetime.now()
        jb.last_update = datetime.now()
        jb.status="Processing"
        db.session.commit()
        if jb.type ==JobType.CHARGING:
            # need to get CS permission
            send_message("INF:REQ CS")
            reply_msg = receive_message()
            print(blueBright(f'[tcp]Receive Message: {reply_msg}'))
            split_reply_msg = reply_msg.split(":")
            if split_reply_msg[0] == 'REP':
                if  split_reply_msg[1] == 'YES':
                    print(blueBright('[tcp]Charging Station is available'))
                    print(f'[sys]{jb.type} Job with ID {jb_id} is now processing')
                    move_rbt_to_destination(jb_id)
                else:
                    print(blueBright('[tcp]Charging Station is not available'))
                    print('[sys]Job rejected')
                    set_available(jb_id)
                    jb.last_update = datetime.now()
                    jb.status ='Rejected'
                    db.session.commit()
            else:   
                print(redBright('[tcp]Invalid response from charging station'))
                set_available(jb_id)
        else:
            print(f'[sys]{jb.type} Job with ID {jb_id} is now processing')
            move_rbt_to_destination(jb_id)
    except Exception as e:
          print(redBright('Start Job Error: '), e)
    
    
def move_rbt_to_destination(jb_id):
    try:
        jb = Job.query.filter_by(id=jb_id).first()
        rbt =Robot.query.filter_by(id=jb.robot_id).first()
        planned_x_move = abs(rbt.location_x - jb.destination_x)
        planned_y_move = abs(rbt.location_y - jb.destination_y)    
        
        # check if moving away from charging station, need to make cs available
        if rbt.location_x == CS_STATION_X and rbt.location_y == CS_STATION_Y:
            if not(planned_x_move ==0 and planned_y_move == 0):
                send_message('INF:LEAVE CS')
                reply_msg = receive_message()
                split_reply_msg = reply_msg.split(":")
                if split_reply_msg[0] == 'REP':
                    if  split_reply_msg[1] == 'ACK':
                        print(blueBright('[tcp]CS Station available now'))
        
        if planned_x_move != 0:
            if rbt.axis != 'X':
                # need to rotate to x axis
                socketio.emit('rotate_axis',{'job_id':jb_id,
                                            'robot_id':rbt.id,
                                            'to_axis':'X'},
                            to=rbt.sid, callback=rotate_axis_callback)
                eventlet.sleep(5)
            if rbt.location_x < jb.destination_x:
                x_increment_value = 1
                mvmt = Movement("right",jb.id,rbt.id)
            else:
                x_increment_value = -1
                mvmt = Movement("left",jb.id,rbt.id)
            grid[rbt.location_x][rbt.location_y] = True
            grid[rbt.location_x+x_increment_value][rbt.location_y] = False
            rbt.location_x = rbt.location_x + x_increment_value
            db.session.add(mvmt)
            db.session.commit()
            socketio.emit('task_assign',{'robot_id':rbt.id,
                                        'job_id':jb_id,
                                        'direction':mvmt.direction,
                                        }, 
                        to=rbt.sid, callback=task_assign_callback)
        elif planned_y_move!=0:
            if rbt.axis != 'Y':
                # need to rotate to y axis
                socketio.emit('rotate_axis',{'job_id':jb_id,
                                            'robot_id':rbt.id,
                                            'to_axis':'Y'},
                            to=rbt.sid, callback=rotate_axis_callback)
                eventlet.sleep(5)
            if rbt.location_y < jb.destination_y:
                y_increment_value = 1
                mvmt = Movement("down",jb.id,rbt.id)
            else:
                y_increment_value = -1
                mvmt = Movement("up",jb.id,rbt.id)
            grid[rbt.location_x][rbt.location_y] = True
            grid[rbt.location_x][rbt.location_y+y_increment_value] = False
            rbt.location_y = rbt.location_y + y_increment_value
            db.session.add(mvmt)
            db.session.commit()
            socketio.emit('task_assign',{'robot_id':rbt.id,
                                        'job_id':jb_id,
                                        'direction':mvmt.direction,
                                        }, 
                        to=rbt.sid, callback=task_assign_callback)
        else:
            socketio.emit('reach_destination',{'job_id':jb_id,'robot_id':rbt.id}, to=rbt.sid, callback=reach_destination_callback)
    except Exception as e:
          print(redBright('Move To Destination Error: '), e)

# -- call back function -- 
def job_arrived_callback(data):
    try:
        print(yellowBright('[res]'+data['status']+', Job received by robot successfully'))
        set_unavailable(data['job_id'])
    except Exception as e:
          print(redBright('Job Arrived Call Back Error: '), e)
    
def set_unavailable_callback(data):
    try:
        print(yellowBright('[res]%s, Robot ID %d is now unavailable' %(data['status'],data['robot_id'])))
        start_job(data['job_id'])
    except Exception as e:
          print(redBright('Set Unavailable Call Back Error: '), e)
          
def set_available_callback(data):
    try:
        print(yellowBright('[res]%s, Robot ID %d is now available' %(data['status'],data['robot_id'])))
    except Exception as e:
          print(redBright('Set Available Call Back Error: '), e)
                    
def rotate_axis_callback(data):
    try:
        print(yellowBright('[res]%s, Axis rotated'%(data['status'])))
        rbt =Robot.query.filter_by(id=data['robot_id']).first()
        rbt.axis = data['to_axis']
        db.session.commit()
    except Exception as e:
          print(redBright('Rotate Axis Call Back Error: '), e)
    
def task_assign_callback(data):
    try:
        print(yellowBright('[res]%s, Instruction recevied by the robot, direction: %s' %(data['status'],data['direction'])))
        print("[sys]Robot ID %d moved %s" %(data['robot_id'],data['direction']))
        move_rbt_to_destination(data['job_id'])        
    except Exception as e:
          print(redBright('Task Assign Call Back Error: '), e)
          
def reach_destination_callback(data):
    try:
        jb = Job.query.filter_by(id=data['job_id']).first()
        rbt =Robot.query.filter_by(id=jb.robot_id).first()
        print('[sys]Robot ID %d has reached the destination' %(rbt.id))
        if jb.type == JobType.NORMAL:
            socketio.emit('job_done',{'job_id':jb.id,'robot_id':rbt.id}, to=rbt.sid, callback=job_done_callback)
        else:  
            send_message('INF:RBT ARR')
            reply_msg = receive_message()
            print(blueBright(f'[tcp]Receive Message: {reply_msg}'))
            split_reply_msg = reply_msg.split(":")
            if split_reply_msg[0] == 'REP':
                if  split_reply_msg[1] == 'STRT CHRG':
                    socketio.emit('start_charging',{'job_id':jb.id,
                                                    'robot_id':rbt.id,
                                                    'robot_sid':rbt.sid
                                                    },to=rbt.sid, callback=start_charging_callback)
                else:
                    print(redBright('[tcp]Invalid response from charging station'))
            else:   
                print(redBright('[tcp]Invalid response from charging station'))
        
      
    except Exception as e:
          print(redBright('Reach Destination Call Back Error: '), e)
          
def start_charging_callback(data):
    print('[sys]Robot ID %d is now charging'%(data['robot_id']))
    chrg = Charge(data['job_id'],data['robot_id'],datetime.now(),None)
    db.session.add(chrg)
    db.session.commit()
    # waiting for charging complete message.....
    receive_msg = receive_message()
    print(blueBright(f'[tcp]Receive Message: {receive_msg}'))
    split_reply_msg = receive_msg.split(":")
    if split_reply_msg[0] == 'INF':
        if  split_reply_msg[1] == 'DONE CHRG':
            socketio.emit('done_charging',{'job_id':data['job_id'],
                                           'robot_id':data['robot_id'],
                                           'robot_sid':data['robot_sid'],
                                           'charge_id': chrg.id
                                           },to=data['robot_sid'], callback=done_charging_callback)
                            
        else:
            print(redBright('[tcp]Invalid response from charging station'))
    else:   
        print(redBright('[tcp]Invalid response from charging station'))
    
def done_charging_callback(data):
    print('[sys]Robot ID %d has finished charging'%(data['robot_id']))
    chrg= Charge.query.filter_by(id=data['charge_id']).first()
    chrg.end_date = datetime.now()
    db.session.commit()
    socketio.emit('job_done',{'job_id':data['job_id'],
                              'robot_id':data['robot_id']
                              }, to=data['robot_sid'], callback=job_done_callback)

    
def job_done_callback(data):
    try:
        jb = Job.query.filter_by(id=data['job_id']).first()
        rbt =Robot.query.filter_by(id=jb.robot_id).first()
        jb.end_date = datetime.now()
        jb.last_update = datetime.now()
        jb.status = "Complete"
        rbt.availability = True
        print("[sys]Robot ID %d has completed Job ID %d" %(rbt.id,jb.id))
        db.session.commit()
    except Exception as e:
        print(redBright('Job Done Call Back Error: '), e)

# start up tcp server for charging station
def start_up_charging_server():
    tcp_server.listen()
    print(blueBright('[tcp]TCP server start listening on %s...' %(TCP_PORT)))
    while True:
        global conn
        global addr
        conn, addr = tcp_server.accept()
        handle_cs_client()
       
    
# funciton to handle client connetion
def handle_cs_client():
    print(blueBright(f'[tcp]New charging station client {addr} connected' ))
    connected = True
            
def send_message(msg):
    encode_msg = msg.encode(FORMAT)
    encode_msg_len = len(encode_msg)
    send_len = str(encode_msg_len).encode(FORMAT)
    # padding to HEADER size
    send_len += b' ' * (HEADER-len(send_len))
    conn.send(send_len)
    conn.send(encode_msg)

def receive_message():
    msg_length = conn.recv(HEADER).decode(FORMAT)
    msg_length = int(msg_length)
    msg = conn.recv(msg_length).decode(FORMAT)
    return msg

# -- socket io --
@socketio.on('connect')
def connected():
    print('[sys]New robot is trying to connect')

@socketio.on('disconnect')
def disconnected():
    disconnected_rbt = Robot.query.filter_by(sid = request.sid).first()
    if disconnected_rbt:
        print('[sys]Robot ID %d has disconnected' %(disconnected_rbt.id))
        # make it unavailbale after disconnect
        disconnected_rbt.availability = False
        grid[disconnected_rbt.location_x][disconnected_rbt.location_y] = True
        disconnected_rbt_task = Job.query.filter_by(robot_id = disconnected_rbt.id,status='Processing')
        if disconnected_rbt_task.first():
            for job in disconnected_rbt_task:
                job.status = 'Error'
            print(redBright('[sys]Job unfinished because robot disconnected')) 
        db.session.commit()
    else:
        print(redBright("Something is worng"))
    
@socketio.on('register_robot')
def register_robot(rbt_data):
    try:
        connecting_rbt = Robot.query.filter_by(serial_number = rbt_data['serial_number']).first()
        if connecting_rbt:
            connecting_rbt.availability = True
            connecting_rbt.location_x = rbt_data["location_x"]
            connecting_rbt.location_y = rbt_data["location_y"]
            connecting_rbt.sid = rbt_data["sid"]
            connecting_rbt.axis = rbt_data["axis"]
            db.session.commit()
            grid[rbt_data["location_x"]][rbt_data["location_y"]] = False
            print("[sys]Robot ID %d has reconnected" %(connecting_rbt.id))
            return True
        else:
            room =rbt_data["sid"]
            join_room(room)
            location_taken = db.session.query(Robot).filter_by(location_x = rbt_data["location_x"], location_y = rbt_data["location_y"]).first()
            num_of_rbt = db.session.query(Robot).count()
            if num_of_rbt<NUM_LIMIT_OF_ROBOTS and not location_taken:
                rbt = Robot(rbt_data["location_x"],rbt_data["location_y"],rbt_data["serial_number"], rbt_data["availability"],rbt_data['axis'],rbt_data["sid"])
                db.session.add(rbt)
                db.session.commit()
                #set to false means there is a robot on grid location
                grid[rbt_data["location_x"]][rbt_data["location_y"]] = False
                print("[sys]Robot ID %d has connected" %(rbt.id))
                return True
            else:
                if location_taken:
                    error_message ="[sys]There is already a robot in the location"
                else:
                    error_message ="[sys]Number of robot has reached the limit"
                print(redBright(error_message))
                socketio.emit('fail_register_rbt', error_message, to=room)
                return False
    except Exception as e:
          print(redBright('Register Robot Error: '), e)

# -- Rest API request --
def dict_helper(objlist):
    result = [item.obj_to_dict() for item in objlist]
    return result

class CompletedJobs(Resource):
    def get(self):
        completed_job = (db.session.query(Job).filter(Job.status =="Complete").all())
        return json.dumps(dict_helper(completed_job))
    
# -- api resource --
api.add_resource(NewJobs,"/newjob")    
api.add_resource(CompletedJobs,"/completedjobs")
api.add_resource(ChargeStatus,"/chargestatus")

@app.route("/view-robot")
def view_robot():
    return render_template("view_robot.html",content=Robot.query.all())

@app.route("/view-job")
def view_job():
    return render_template("view_job.html",content=Job.query.all())


if __name__ == '__main__':
    # db.drop_all()
    db.create_all()
    t = threading.Thread(target=start_up_charging_server)
    t.start()
    socketio.run(app)