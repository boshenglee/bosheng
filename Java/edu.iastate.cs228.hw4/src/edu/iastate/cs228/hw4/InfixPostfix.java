package edu.iastate.cs228.hw4;

/**
 *  
 * @author Bo Sheng Lee
 *
 */

/**
 * 
 * This class evaluates input infix and postfix expressions. 
 *
 */

import com.sun.media.jfxmediaimpl.HostUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class InfixPostfix 
{

	/**
	 * Repeatedly evaluates input infix and postfix expressions.  See the project description
	 * for the input description. It constructs a HashMap object for each expression and passes it 
	 * to the created InfixExpression or PostfixExpression object. 
	 *  
	 * @param args
	 **/
	public static void main(String[] args) throws ExpressionFormatException, UnassignedVariableException, FileNotFoundException {
		// TODO
		System.out.println("\n" +
				"Evaluation of Infix and Postfix Expressions\n" +
				"keys: 1 (standard input) 2 (file input) 3 (exit)\n" +
				"(Enter \"I\" before an infix expression, \"P\" before a postfix expression)");
		int trial;
		int count = 1;
		Scanner sc = new Scanner(System.in);

		do{
			Expression e;
			HashMap<Character, Integer> variable = new HashMap<Character, Integer>();

			System.out.print("\nTrial "+count+++": ");
			trial = Integer.parseInt(sc.nextLine());


			if (trial==1)
			{
				System.out.print("Expression: ");
				String expression = sc.nextLine();

				if(expression.charAt(0)=='I')
				{
					expression = expression.substring(1,expression.length());
					e = new InfixExpression(expression);
					System.out.println("Infix form: "+e.toString());
					System.out.println("Postfix form: "+((InfixExpression)e).postfixString());
				}else {
					expression = expression.substring(1,expression.length());
					e = new PostfixExpression(expression);
					System.out.println("Postfix form: "+e.toString());
				}
				if(containVariable(expression))
				{
					updateVariable(expression,variable);
					e.setVarTable(variable);
				}
				System.out.println("Expression value: "+e.evaluate());
			}
			else if (trial==2)
			{
				System.out.println("Input from a file");
				System.out.print("Enter file name: ");
				String fileName = sc.nextLine();

				try {
					File file = new File(fileName);
					Scanner fileScanner = new Scanner(file);
					while(fileScanner.hasNextLine()) {
						String firstLine = fileScanner.nextLine();

						if (firstLine.charAt(0) == 'I') {
							String expression = firstLine.substring(1, firstLine.length());
							e = new InfixExpression(expression);
							System.out.println("\nInfix form: "+e.toString());
							System.out.println("postfix form: "+((InfixExpression)e).postfixString());

							if (containVariable(firstLine)) {
								while (fileScanner.hasNextLine()) {
									String token = fileScanner.nextLine();
									if (token.equals(""))
										break;
									token = token.replaceAll("\\s+","");
									variable.put(token.charAt(0), Integer.parseInt(token.substring(2, token.length())));
								}
								e.setVarTable(variable);
								for (Character key : variable.keySet()) {
									System.out.println(key + " = " + variable.get(key));
								}
							} else {
								if(fileScanner.hasNextLine())
									fileScanner.nextLine();
							}
						} else {
							String expression = firstLine.substring(1, firstLine.length());
							e = new PostfixExpression(expression);
							System.out.println("\nPostfix form: "+e.toString());

							if (containVariable(firstLine)){
								while (fileScanner.hasNextLine()) {
									String token = fileScanner.nextLine();
									if (token.equals(""))
										break;
									token = token.replaceAll("\\s+","");
									variable.put(token.charAt(0), Integer.parseInt(token.substring(2, token.length())));
								}
								e.setVarTable(variable);
								for (Character key : variable.keySet()) {
									System.out.println(key + " = " + variable.get(key));
								}
							} else {
								if(fileScanner.hasNextLine())
									fileScanner.nextLine();
							}
						}
						System.out.println("Expression value: "+e.evaluate());
					}
				}
				catch (FileNotFoundException ex) {
					throw new FileNotFoundException();
				}
			}else{
				trial =3;
			}

		}while(trial != 3);
	}

	/**
	 * method to check whether the expression contain variable
	 * @param s
	 * @return boolean value
	 */
	public static boolean containVariable(String s){
		for(int i=0; i<s.length();i++){
			if(Expression.isVariable(s.charAt(i)))
				return true;
		}
		return false;
	}

	/**
	 * update the value of variable
	 * @param s
	 * @param v
	 */
	public static  void updateVariable(String s,HashMap<Character,Integer> v){

		Scanner sc = new Scanner(System.in);
		System.out.println("where");
		for (int i = 0; i < s.length(); i++) {
			if (Expression.isVariable(s.charAt(i))) {
				System.out.print(s.charAt(i) + " = ");
				int temp = Integer.parseInt(sc.nextLine());
				v.put(s.charAt(i), temp);
			}
		}
	}
	
	// helper methods if needed
}
