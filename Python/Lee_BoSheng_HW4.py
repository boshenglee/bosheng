def MaxPool(x):
    max = x[0]
    for i in x:
        if (i>=max):
            max = i
    return max
    
#print(MaxPool([0,0,0,0]))
#print(MaxPool([7,1,-3]))
#print(MaxPool([-5,0,9,-1,30]))

def SilverMedal(x):
    gold = x[0]
    silver = x[0]
    for i in x:
        if(i>gold):
            silver = gold
            gold = i
        else:
            if(i>=silver):
                silver = i
    if(silver == gold):
        silver = -10000
        for i in x:
            if (i!=gold):
                if(i>=silver):
                    silver = i
    return silver

#print(SilverMedal([5,5.5,3,5,9,7]))
#print(SilverMedal([5,9,3,5,9,7]))
#print(SilverMedal([1,2,2,2,2]))
#print(SilverMedal([7,-1,-3]))
#print(SilverMedal([9,7,8]))

 
def BelongTo(x,y):
    check = False
    for i in x:
        if(i==y):
            check = True
    return check

#print(BelongTo([1,2,3],3))
#print(BelongTo([5,9,3,5,9,7],8))
#print(BelongTo([5,9,-3,5,9,7],-3))

def gcd(x,y):
    gcd = 1
    for i in range(1,x+1):
        if(x%i==0 and y%i==0):
            gcd = i
    return gcd

#print(gcd(20,24))
#print(gcd(24,20))
#print(gcd(3,7))
#print(gcd(10,5))

def PrintMe(x):
    word = ""
    for i in range(x-1):
        word+=str(x)+" "
    word += str(x)
    return word

#print(PrintMe(3))
#print(PrintMe(4))
#print(PrintMe(10))

def Triangle(x):
    list = []
    for i in range (1,x+1):
        list.append(PrintMe(i))
    return list

#print(Triangle(3))
#print(Triangle(4))
#print(Triangle(5))

def TypeWriter(x):
    list = []
    for i in range(0,len(x)):
        list.append(x[0:i+1])
    return list

#print(TypeWriter("Hello"))
#print(TypeWriter("I S U"))
#print(TypeWriter("C-S"))

def sift(x,y):
    list = []
    
    if (len(x)!=0):
        for i in x:
            if (i>y):
                list.append(i)
        return list
    else:
        return list
        

#print(sift([1,5,7,4,3],4))
#print(sift([1,5,7,4,3],3))
#print(sift([1,-5,7,-1,3],-3))
#print(sift([0,0,0,0],-1))
#print(sift([],4))

def Backward(x):
    word = ""
    for i in range (len(x)-1,-1,-1):
        word += x[i]
    return word
    
#print(Backward("Happy Birthday"))


def abs(x):
    if (x<0):
        return x*-1
    else:
        return x

def BestApprox(x,y):
    diff = 0
    smallestDiff = 1000000
    num = 0
    for i in x:
        diff = abs(y-i)
        if(diff<=smallestDiff):
            smallestDiff = diff
            num = i
    return num

#print(BestApprox([6.1,6.1,6.2,3,4],6.5))
#print(BestApprox([4,7,8,1,2,3],4))
#print(BestApprox([4,7,8,1,2,3],3.5))
#print(BestApprox([3.2,7,3.8,1.2,3],3.5))
#print(BestApprox([-1,-2,-3,-4],-2.7))

def SmallerApprox(x,y):
    diff = 0
    smallestDiff = 1000000
    num = 0
    for i in x:
        diff = abs(y-i)
        if (diff == smallestDiff):
            if (num>i):
                num = i
                
        if(diff<smallestDiff):
            smallestDiff = diff
            num = i
    return num

#print(SmallerApprox([4,7,8,1,2,3],3.5))
#print(SmallerApprox([3.2,7,3.8,1,2,3],3.5))

def BFF(x):
    diff = 0
    smallestDiff = 1000000
    num1 =0
    num2 =0
    if(len(x)<=2):
        if(len(x)==1):
            return x[0],x[0]
        else:
            return x[0],x[1]
    else:
        for i in range(0,len(x)):
            for j in range(i+1,len(x)):
                diff = abs(x[i]-x[j])
                if(diff<smallestDiff):
                    smallestDiff = diff
                    num1 = x[i]
                    num2 = x[j]
        return num1,num2
#print(BFF([9,3,20,9,7]))
#print(BFF([5]))
#print(BFF([5,5]))
