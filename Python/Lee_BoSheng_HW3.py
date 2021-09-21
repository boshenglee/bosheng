#def tax(x):
#        if (x>1000):
#                return x*0.05
#        if (x>500):
#                return x*0.02
#        if (x<500):
#                return x*0.01

#Q3
def tax(x):
        if (x>1000):
                return x*0.05
        elif (x>500):
                return x*0.02
        else:
                return x*0.01

#Q4
def tax2(x):
        if(x>50):
                if (x>=1000):
                        return (x-50)*0.05
                elif(x>=500):
                        return (x-50)*0.02
                else:
                        return (x-50)*0.01

        else:
                return 0

#Q5
def tax3(x):
	t = 0
	if(x>100):
		t += min(x-100,400)*0.01
	if (x>500):
		t += min(x-500,500)*0.02
	if (x>1000):
		t += (x-1000)*0.05

	return t

#Q6
def median(a,b,c):
  if(a==b):
    return a
  elif(b==c):
    return b
  elif(c==a):
    return c
  elif(a>b):
    if(b>c):
      return b
    if(c>a):
      return a
    if(c>b):
      return c
  elif(a>c):
    if(c>b):
      return c
    if(b>a):
      return a
  else:
    if(b>c):
      return c
    if(c>b):
      return b

#Q7
def caesar_encoder(x):

	new_word = ""
	for i in x:
		if(i=="a"):
			encrypt="c"
		elif(i=="b"):
			encrypt="d"
		elif(i=="c"):
                        encrypt="e"
		elif(i=="d"):
                        encrypt="f"
		elif(i=="e"):
                        encrypt="g"
		elif(i=="f"):
                        encrypt="h"
		elif(i=="g"):
                        encrypt="i"
		elif(i=="h"):
                        encrypt="j"
		elif(i=="i"):
                        encrypt="k"
		elif(i=="j"):
                        encrypt="l"
		elif(i=="k"):
                        encrypt="m"
		elif(i=="l"):
                        encrypt="n"
		elif(i=="m"):
                        encrypt="o"
		elif(i=="n"):
                        encrypt="p"
		elif(i=="o"):
                        encrypt="q"
		elif(i=="p"):
                        encrypt="r"
		elif(i=="q"):
                        encrypt="s"
		elif(i=="r"):
                        encrypt="t"
		elif(i=="s"):
                        encrypt="u"
		elif(i=="t"):
                        encrypt="v"
		elif(i=="u"):
                        encrypt="w"
		elif(i=="v"):
                        encrypt="x"
		elif(i=="w"):
                        encrypt="y"
		elif(i=="x"):
                        encrypt="z"
		elif(i=="y"):
                        encrypt="a"
		elif(i=="z"):
                        encrypt="b"
		else:
			encrypt=i
		
		new_word += encrypt
	return new_word

def caesar_decoder(x):

        new_word = ""
        for i in x:
                if(i=="a"):
                        decrypt="y"
                elif(i=="b"):
                        decrypt="z"
                elif(i=="c"):
                        decrypt="a"
                elif(i=="d"):
                        decrypt="b"
                elif(i=="e"):
                        decrypt="c"
                elif(i=="f"):
                        decrypt="d"
                elif(i=="g"):
                        decrypt="e"
                elif(i=="h"):
                        decrypt="f"
                elif(i=="i"):
                        decrypt="g"
                elif(i=="j"):
                        decrypt="h"
                elif(i=="k"):
                        decrypt="i"
                elif(i=="l"):
                        decrypt="j"
                elif(i=="m"):
                        decrypt="k"
                elif(i=="n"):
                        decrypt="l"
                elif(i=="o"):
                        decrypt="m"
                elif(i=="p"):
                        decrypt="n"
                elif(i=="q"):
                        decrypt="o"
                elif(i=="r"):
                        decrypt="p"
                elif(i=="s"):
                        decrypt="q"
                elif(i=="t"):
                        decrypt="r"
                elif(i=="u"):
                        decrypt="s"
                elif(i=="v"):
                        decrypt="t"
                elif(i=="w"):
                        decrypt="u"
                elif(i=="x"):
                        decrypt="v"
                elif(i=="y"):
                        decrypt="w"
                elif(i=="z"):
                        decrypt="x"
                else:
                        decrypt=i

                new_word += decrypt
        return new_word

#def scramble(a):
#	return "".join(map(caesar_encoder,a))
#def restore(a):
#	return "".join(map(caesar_decoder,a))
#msg = input("What's your command your majesty? ")
#print("Caesar's enemies read: "+ scramble(msg))
#print("Caesar's generals read: "+ restore(scramble(msg)))

#Q8
def is_solvable(a,b,c):
	x = (b**2)-4*a*c
	if ((a==0) or (x <0)):
		return False

	elif (x>0):
		return True

	else:
		return True

def solve_eq(a,b,c):

	if(is_solvable(a,b,c)):
		smaller_root = (-b-((b**2)-4*a*c)**0.5)/(2*a)
		bigger_root = (-b+((b**2)-4*a*c)**0.5)/(2*a)
		return smaller_root,bigger_root

	else:
		return None,None

