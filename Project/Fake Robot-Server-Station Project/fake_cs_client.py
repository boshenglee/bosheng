from base64 import encode
from posixpath import split
import socket
import time
import random
from simple_chalk import redBright, yellowBright, blueBright, greenBright

TCP_PORT = 3535
TCP_HOST = 'localhost'
HEADER = 64
FORMAT = 'utf-8'

# message format
# INF:REQ CS: (request for cs status)
# INF:RBT ARR (inform robot arrive)
# INF:DONE CHRG (inform finish charging)
# REP:STRT CHRG (reply with start cahrging after receive robot arrive)
# REP:YES (ack)
# REP:NO  (ack)

# tcp client settings
tcp_client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_address = (TCP_HOST, TCP_PORT)
tcp_client.connect(server_address)
print('Charging station has connected to server')
connected = True

#cs settings
cs_available = True

def send_message(msg):
    encode_msg = msg.encode(FORMAT)
    encode_msg_len = len(encode_msg)
    send_len = str(encode_msg_len).encode(FORMAT)
    # padding to HEADER size
    send_len += b' ' * (HEADER-len(send_len))
    tcp_client.send(send_len)
    tcp_client.send(encode_msg)

while connected:
    print(greenBright('Listening...'))
    msg_length = tcp_client.recv(HEADER).decode(FORMAT)
    msg_length = int(msg_length)
    msg = tcp_client.recv(msg_length).decode(FORMAT)
    print(f'Receive message: {msg}')
    # strat reading message
    split_msg = msg.split(':')
    if split_msg[0] == 'INF':
        if split_msg[1] == 'REQ CS':
            if cs_available:
                reply_message = 'REP:YES'
                print(f'Reply message  : {reply_message}')
                send_message(reply_message) 
                cs_available = False
                print('Charging Station is now unavailbale')
            else:
                reply_message = 'REP:NO'
                print(f'Reply message  : {reply_message}')
                send_message(reply_message) 
                
        elif split_msg[1] =='LEAVE CS':
            reply_message = 'REP:ACK'
            print(f'Reply message  : {reply_message}')
            send_message(reply_message) 
            cs_available = True
            print('Charing Station is now available')
        # notify cs station rbt arrive 
        elif split_msg[1]=='RBT ARR':
            reply_message = 'REP:STRT CHRG'
            print(f'Reply message  : {reply_message}')
            send_message(reply_message)
            # charging wait time
            charge_time = random.randint(8,15)
            time.sleep(charge_time)
            reply_message = 'INF:DONE CHRG'
            print(f'Send message   : {reply_message}')
            send_message(reply_message)
    
    