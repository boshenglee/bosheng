#Q1
def stairway(n):
	stairs = 0
	while(n>=stairs):
		stairs += 1
		n -= stairs
	return stairs

#print(stairway(6))
#print(stairway(10))
#print(stairway(11))

#Q2
def h(x,C):
    return (x**3-C)/(3*x*x)
def cycbrt(C,e):
    x=10
    while(h(x,C)>e):
        x -= h(x,C)
    return x

#print(cycbrt(8,0.1))

#Q3
def mypower(base,exponent):
    answer = 1
    counter = 0
    while(counter<exponent):
        answer *=base
        counter +=1
    return answer
#print(mypower(2,10))
#print(mypower(3,2))
#print(mypower(9,3))

#Q4
def multiple(n):
    list = []
    for i in range (1,n+1):
        list.append(n*i)
    return list
#print(multiple(1))
#print(multiple(2))
#print(multiple(4))

#Q5
def nbyn(n):
    list = []
    for i in range (1, n+1):
        list.append(multiple(i))
    return list
#print(nbyn(1))
#print(nbyn(2))
#print(nbyn(4))

#Q6
def alldivisor(n):
    list = []
    for i in range (1, n+1):
        if(n%i ==0):
            list.append(i)
    return list
#print(alldivisor(16))
#print(alldivisor(75))
#print(alldivisor(100))
#print(alldivisor(1))

#Q7
def isperfect(n):
    list = alldivisor(n)
    if ((sum(list)-n)==n):
        return True
    else:
        return False
#print(isperfect(6))
#print(isperfect(28))
#print(isperfect(496))
#print(isperfect(7))
#print(isperfect(10))
#print(isperfect(20))

#Q8
def triplecut(x,y):
    cut = 0
    while(x>y):
        x /= 3
        cut += 1
    return cut
#print(triplecut(72,8))
#print(triplecut(72,7))
#print(triplecut(72,72))
#print(triplecut(72,70))

#Q9
def common_in_range(num1,num2):
    list = []
    while(num1<=num2):
        number = 2
        while(number<num1):
            if(num1%number ==0):
                list.append(num1)
                break
            number += 1
        num1 += 1
    return list
#print(common_in_range(1,4))
#print(common_in_range(1,2))
#print(common_in_range(5,20))
#print(common_in_range(100,110))

#Q10
def digit2char(a):
    if a==1:
        return '1'
    elif a==2:
        return '2'
    elif a==3:
        return '3'
    elif a==4:
        return '4'
    elif a==5:
        return '5'
    elif a==6:
        return '6'
    elif a==7:
        return '7'
    elif a==8:
        return '8'
    elif a==9:
        return '9'
    elif a==0:
        return '0'

def integer2list(a):
    x = []
    while a > 0 :
        b = a % 10
        a = a // 10
        x.append(b)
    return x[::-1]

def float2list(a):
    while a % 1 != 0:
        a = a*10
    return integer2list(a)
    
def approxfloat(L):

    zerocount, ninecount = 0, 0
    for i in range(len(L)):
        if L[-i] == 0 :
            zerocount += 1
        elif L[-i] == 9:
            ninecount += 1
        else:
            if i>zerocount>ninecount and zerocount>1:
                if L[-(i-1)] == 9:
                    L = L[:-(i-2)]
                else:
                    L = L[:-(i-1)]
                break
            elif i>ninecount>zerocount and ninecount>1:
                L[-i]+=1
                L = L[:-(i-1)]
                break
                
    return L
    
def float2str(a):
    num = 1
    index = 0
    while(a//num!=0):
        index += 1
        num *= 10
    
    L = float2list(a)
    L = approxfloat(L)
    S = ""
    for l in L:
        S += digit2char(l)
    result = ""
    counter = 0
    for i in S:
        if(counter== index):
            if(index ==0):
                result += "0"
            result += "."
        result += i
        counter += 1
    return result

#print(float2str(637.155))
#print(float2str(1.155))
#print(float2str(63715523424.4))

#Q11
def user_moves(XO):

    y = input("What row (0-index): ")
    x = input("What column (0-index): ")
    
    y = int(y)
    x = int(x)
    
    while(XO[y][x]!=0):
        print("\nIt is filled,choose again!")
        y = input("What row (0-index): ")
        x = input("What column (0-index): ")
        y = int(y)
        x = int(x)
    
    XO[y][x] = 1
    return XO
        
import random
def computer_moves(XO,counterY,counterX):
    
    randomY = random.randrange(3)
    randomX = random.randrange(3)
    
    while(XO[randomY][randomX]!=0):
        randomY = random.randrange(3)
        randomX = random.randrange(3)
        
    XO[randomY][randomX] = 2
    return XO


#def cyrandom(counter):

#    randomlist= [0,1,2]
#    randomnum = randomlist[counter]
#    counter += 1

#    if(counter>2):
#        counter = 0
    
#    return randomnum,counter

def print_grid(XO):
    
    for row in range(0,3):
        grid = ""
        for column in range(0,3):
            if(XO[row][column]==1):
                grid+= "X"
            elif(XO[row][column]==2):
                grid+="O"
            else:
                grid+= "_"
        print(grid)

    return None
    
def judge(XO):

    #1: tie
    #2: user (playing X) wins
    #3: computer (playing O) wins
    #4: else (the game should keep going )

    if(XO[0][0]==XO[0][1]==XO[0][2]==1):
        return 2
    elif(XO[1][0]==XO[1][1]==XO[1][2]==1):
        return 2
    elif(XO[2][0]==XO[2][1]==XO[2][2]==1):
        return 2
    elif(XO[0][0]==XO[1][0]==XO[2][0]==1):
        return 2
    elif(XO[0][1]==XO[1][1]==XO[2][1]==1):
        return 2
    elif(XO[0][2]==XO[1][2]==XO[2][2]==1):
        return 2
    elif(XO[0][0]==XO[1][1]==XO[2][2]==1):
        return 2
    elif(XO[0][2]==XO[1][1]==XO[2][0]==1):
        return 2
    elif(XO[0][0]==XO[0][1]==XO[0][2]==2):
        return 3
    elif(XO[1][0]==XO[1][1]==XO[1][2]==2):
        return 3
    elif(XO[2][0]==XO[2][1]==XO[2][2]==2):
        return 3
    elif(XO[0][0]==XO[1][0]==XO[2][0]==2):
        return 3
    elif(XO[0][1]==XO[1][1]==XO[2][1]==2):
        return 3
    elif(XO[0][2]==XO[1][2]==XO[2][2]==2):
        return 3
    elif(XO[0][0]==XO[1][1]==XO[2][2]==2):
        return 3
    elif(XO[0][2]==XO[1][1]==XO[2][0]==2):
        return 3
    elif(draw_game(XO)==True):
        return 1
    else:
        return 4
        
def draw_game(XO):
    for y in range(3):
        for x in range(3):
            if(XO[y][x]==0):
                return False
    return True
                
        
def ko(result):
    if(result==1):
        print("\nIt's a draw!")
    elif(result==2):
        print("\nGood job, you win!")
    else:
        print("\nSorry, you were defeated by a bot.")

def game():
    
    XO = [[0,0,0],[0,0,0],[0,0,0]]
    counterX = 0
    counterY = 2
    while(True):
        user_moves(XO)
        result = judge(XO)
        print("\nYour move")
        print_grid(XO)
        if(result<4):
            ko(result)
            return 0
        computer_moves(XO,counterY,counterX)
        result = judge(XO)
        print("\nComputer's move")
        print_grid(XO)
        if(result<4):
            ko(result)
            return 0
        
#if __name__ == "__main__":
#game()


#Q12
def uno(x,y):
    temp = 0
    x1 = 0
    x2 = 0
    numofx = 0
    if (x>y):
        temp = y
        y = x
        x = temp
    while(y>=10 and y>=x):
        x1 = y//10
        if(x1 == 1):
            numofx += 1
        x2 = y%10
        if(x2 ==1):
            numofx += 1
        y -=1
    while(y>=x):
        x1 = y//1
        if(x1 == 1):
            numofx += 1
        y -=1
    return numofx

#print(uno(1,18))
#print(uno(2,18))
#print(uno(19,2))
#print(uno(99,0))

        
        
        

        
