from tokenize import String
from flask import Flask,request, flash, url_for, redirect, render_template, jsonify
from flask_restful import Api, Resource, reqparse
from flask_sqlalchemy import SQLAlchemy
from flask_migrate import Migrate
from pkg_resources import require
from sqlalchemy import Integer, func
from sqlalchemy.orm import backref
from sqlalchemy.sql.schema import ForeignKey
import logging
import json
import random
from dotenv import load_dotenv
import os


GRID_ROW = 8
GRID_COL = 8
NUM_LIMIT_OF_ROBOTS = 3

app = Flask(__name__)
api = Api(app)
app.secret_key = os.getenv("SECRET_KEY")

log = logging.getLogger('werkzeug')
log.setLevel(logging.ERROR)

#configuration
app.config['SQLALCHEMY_DATABASE_URI'] = 'postgresql://postgres:password@localhost:5432/little_project'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False


#cretae database object
db = SQLAlchemy(app)
migrate = Migrate(app,db)

#set up grid
grid = [[True for i in range(GRID_COL)] for j in range(GRID_ROW)]

#initialize db
class Robot(db.Model):
   __tablename__ = 'robot'
   id = db.Column(db.Integer(), primary_key = True)
   location_x = db.Column(db.Integer())
   location_y = db.Column(db.Integer())
   availability = db.Column(db.Boolean, default = True)
   model = db.Column(db.Integer())
   job = db.relationship('Job',backref='robot')
   movement = db.relationship('Movement',backref='robot')

   def __init__(self, location_x,location_y,model, availability):
    self.location_x = location_x
    self.location_y = location_y
    self.model = model
    self.availability = availability

class Job(db.Model):
    __tablename__ = 'job'
    id = db.Column(db.Integer(), primary_key = True)
    destination_x = db.Column(db.Integer())
    destination_y = db.Column(db.Integer())
    status = db.Column(db.String(30), default = "Waiting")
    robot_id = db.Column(db.Integer(),db.ForeignKey('robot.id'))
    movement = db.relationship('Movement',backref='job')

    def __init__(self, destination_x,destination_y, status, robot_id):
      self.destination_x = destination_x
      self.destination_y = destination_y
      self.robot_id = robot_id
      self.status = status
      
    def obj_to_dict(self):
        return{
            "id": self.id,
            "destination_x": self.destination_x,
            "destination_y": self.destination_y,
            "robot_id": self.robot_id,
            "status": self.status
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


#resource class for api
class NewJobs(Resource):
    def put(self):
        data = request.data
        data_args = reqparse.RequestParser()
        data_args.add_argument("destination_x", type = int, help="x destination of this job is required", required = True)
        data_args.add_argument("destination_y", type = int, help="y destination of this job is required", required = True)
        data_args.add_argument("status",required = False)
        data_args.add_argument("robot_id", type = int, required = False)
        data = data_args.parse_args()
        print("-----Job recevied")
        if data["destination_x"] > 7 or data["destination_y"] > 7:
            print("-----Invalid destination of job")
            print("-----Job rejected")
            return "Invalid destination of job", 404
        elif not grid[data["destination_x"]][data["destination_y"]]:
            print("-----A robot is already at the destination")
            print("Job rejected")
            return "A robot is already at the destination", 404
        else:
            jb = Job(data["destination_x"],data["destination_y"],data["status"],data["robot_id"])
            db.session.add(jb)
            db.session.commit()
            print("-----Destination (%d , %d)" %(data["destination_x"],data["destination_y"]))
            assign_job(jb)
            set_unavailable(jb)
            start_job(jb)
            return jb.id
       
api.add_resource(NewJobs,"/newjob")


def dict_helper(objlist):
    result = [item.obj_to_dict() for item in objlist]
    return result

class CompletedJobs(Resource):
    def get(self):
        completed_job = (db.session.query(Job).filter(Job.status =="Complete").all())
        return json.dumps(dict_helper(completed_job))
    
api.add_resource(CompletedJobs,"/completedjobs")



# generate one robot 
def generate_robot():
    coor_x = random.randint(0,7)
    coor_y = random.randint(0,7)
    location_taken = db.session.query(Robot).filter_by(location_x = coor_x, location_y = coor_y).first()
    num_of_rbt = db.session.query(Robot).count()
    if num_of_rbt<NUM_LIMIT_OF_ROBOTS and not location_taken:
        rbt = Robot(coor_x,coor_y,1, True)
        db.session.add(rbt)
        db.session.commit()
        #set to false means there is a robot on grid location
        grid[coor_x][coor_y] = False
        print("-----Robot added")
        return True
    else:
        print("-----Number of robot has reached the limit")
        return False

# generate robot to max limit
def generate_all_robot():
    while generate_robot():
        continue
    
    return
    
#start assign job after receiving job
#let the nearest robot ot pick up the job
def assign_job(jb):
    available_robot = db.session.query(Robot).filter_by(availability = True)
    min_distance = 10000
    closest_robot = available_robot.first()
    for rbt in available_robot:
        distance = abs(jb.destination_x - rbt.location_x) + abs(jb.destination_y - rbt.location_y)
        if(distance<min_distance):
            min_distance = distance
            closest_robot = rbt
    update_jb = Job.query.filter_by(id=jb.id).first()
    jb.robot_id = closest_robot.id
    print("-----Robot ID %d has picked up the job" %(jb.robot_id))
    db.session.commit()
    return closest_robot.id

def set_unavailable(jb):
    rbt =Robot.query.filter_by(id=jb.robot_id).first()
    rbt.availability = False
    print("-----Robot ID %d is now unavailable" %(jb.robot_id))
    db.session.commit()
    
def start_job(jb):
    rbt =Robot.query.filter_by(id=jb.robot_id).first()
    jb.status="Processing"
    print("-----Job ID %d is now processing" %(jb.id))
    db.session.commit()
    move_rbt_to_destination(jb,rbt)
    
    
    
def move_rbt_to_destination(jb, rbt):
    #first move in direction x
    while rbt.location_x != jb.destination_x:
         if rbt.location_x < jb.destination_x:
             x_increment_value = 1
             mvmt = Movement("right",jb.id,rbt.id)
         else:
             x_increment_value = -1
             mvmt = Movement("left",jb.id,rbt.id)
         print("-----Robot ID %d is moving from (%d, %d) to (%d, %d)" %(rbt.id,rbt.location_x,rbt.location_y,rbt.location_x+x_increment_value,rbt.location_y))
         grid[rbt.location_x][rbt.location_y] = True
         grid[rbt.location_x+x_increment_value][rbt.location_y] = False
         rbt.location_x = rbt.location_x + x_increment_value
         db.session.add(mvmt)
         db.session.commit()
         
    #then move in direction y
    while rbt.location_y != jb.destination_y:
         if rbt.location_y < jb.destination_y:
             y_increment_value = 1
             mvmt = Movement("down",jb.id,rbt.id)
         else:
             y_increment_value = -1
             mvmt = Movement("up",jb.id,rbt.id)
         print("-----Robot ID %d is moving from (%d, %d) to (%d, %d)" %(rbt.id,rbt.location_x,rbt.location_y,rbt.location_x,rbt.location_y+y_increment_value))
         grid[rbt.location_x][rbt.location_y] = True
         grid[rbt.location_x][rbt.location_y+y_increment_value] = False
         rbt.location_y = rbt.location_y + y_increment_value
         db.session.add(mvmt)
         db.session.commit()
         
    if(rbt.location_x == jb.destination_x and rbt.location_y == jb.destination_y):
        jb.status = "Complete"
        rbt.availability = True
        print("-----Robot ID %d has reached its destination" %(rbt.id))
        print("-----Job ID %d is now completed" %(jb.id))
        print("-----Robot ID %d is now available" %(rbt.id))
        db.session.commit()

# some app route
@app.route("/")
def home():
    db.drop_all()
    db.create_all()
    generate_all_robot()
    return render_template("index.html")

@app.route("/view-robot")
def view_robot():
    return render_template("view_robot.html",content=Robot.query.all())

@app.route("/view-job")
def view_job():
    return render_template("view_job.html",content=Job.query.all())


if __name__ == '__main__':
    db.drop_all()
    db.create_all()
    app.run(debug = True)
    
    