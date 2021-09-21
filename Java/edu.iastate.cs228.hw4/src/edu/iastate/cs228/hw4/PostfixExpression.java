package edu.iastate.cs228.hw4;

/**
 *  
 * @author Bo Sheng Lee
 *
 */

/**
 * 
 * This class evaluates a postfix expression using one stack.    
 *
 */

import sun.tools.jstat.ExpressionExecuter;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class PostfixExpression extends Expression 
{
	private int leftOperand;            // left operand for the current evaluation step             
	private int rightOperand;           // right operand (or the only operand in the case of 
	                                    // a unary minus) for the current evaluation step
	public String postfixExpression;

	private PureStack<Integer> operandStack;  // stack of operands
	

	/**
	 * Constructor stores the input postfix string and initializes the operand stack.
	 * 
	 * @param st      input postfix string. 
	 * @param varTbl  hash map that stores variables from the postfix string and their values.
	 */
	public PostfixExpression (String st, HashMap<Character, Integer> varTbl)
	{
		postfixExpression = st.replaceAll("\\s{2,}", " ").trim();
		operandStack = new ArrayBasedStack<>();
		varTable = new HashMap<Character, Integer>();
		setVarTable(varTbl);
	}
	
	
	/**
	 * Constructor supplies a default hash map. 
	 * 
	 * @param s
	 */
	public PostfixExpression (String s)
	{
		postfixExpression = s.replaceAll("\\s{2,}", " ").trim();
		operandStack = new ArrayBasedStack<>();
		varTable = new HashMap<Character, Integer>();
	}

	
	/**
	 * Outputs the postfix expression according to the format in the project description.
	 */
	@Override 
	public String toString()
	{
		return Expression.removeExtraSpaces(postfixExpression);
	}
	

	/**
	 * Resets the postfix expression. 
	 * @param st
	 */
	public void resetPostfix (String st)
	{
		postfixExpression = st; 
	}


	/**
     * Scan the postfixExpression and carry out the following:  
     * 
     *    1. Whenever an integer is encountered, push it onto operandStack.
     *    2. Whenever a binary (unary) operator is encountered, invoke it on the two (one) elements popped from  
     *       operandStack,  and push the result back onto the stack.  
     *    3. On encountering a character that is not a digit, an operator, or a blank space, stop 
     *       the evaluation. 
     *       
     * @return value of the postfix expression 
     * @throws UnassignedVariableException,ExpressionFormatException with one of the messages below:
     *  
     *           -- "Invalid character" if encountering a character that is not a digit, an operator
     *              or a whitespace (blank, tab); 
     *           --	"Too many operands" if operandStack is non-empty at the end of evaluation; 
     *           -- "Too many operators" if getOperands() throws NoSuchElementException; 
     *           -- "Divide by zero" if division or modulo is the current operation and rightOperand == 0;
     *           -- "0^0" if the current operation is "^" and leftOperand == 0 and rightOperand == 0;
     *           -- self-defined message if the error is not one of the above.
     *           
     *         UnassignedVariableException if the operand as a variable does not have a value stored
     *            in the hash map.  In this case, the exception is thrown with the message
     *            
     *           -- "Variable <name> was not assigned a value", where <name> is the name of the variable.  
     *           
     */
	public int evaluate() throws ExpressionFormatException, UnassignedVariableException {

		String[] splited = postfixExpression.split(" ");
		for(int i =0; i<splited.length;i++){
			if(Expression.isInt(splited[i])||Expression.isVariable(splited[i].charAt(0))){
				if(Expression.isInt(splited[i])) {
					operandStack.push(Integer.parseInt(splited[i]));
				}else{
					if(varTable.get(splited[i].charAt(0))==null){
						throw new UnassignedVariableException("Variable "+splited[i].charAt(0)+" was not assigned a value");
					}
					operandStack.push(varTable.get(splited[i].charAt(0)));
				}
			}
			else if(Expression.isOperator(splited[i].charAt(0))){
				try {
					getOperands(splited[i].charAt(0));
				}catch(NoSuchElementException e){
					throw new ExpressionFormatException("Too many operators");
				}
				int temp = compute(splited[i].charAt(0));
				operandStack.push(temp);
			}
			else{
				throw new ExpressionFormatException("Invalid Character");
			}
		}
		if(operandStack.size()>1){
			throw new ExpressionFormatException("Too many operand");
		}
		return operandStack.pop();
    }
	

	/**
	 * For unary operator, pops the right operand from operandStack, and assign it to rightOperand. The stack must have at least
	 * one entry. Otherwise, throws NoSuchElementException.
	 * For binary operator, pops the right and left operands from operandStack, and assign them to rightOperand and leftOperand, respectively. The stack must have at least
	 * two entries. Otherwise, throws NoSuchElementException.
	 * @param op
	 * 			char operator for checking if it is binary or unary operator.
	 */
	private void getOperands(char op) throws NoSuchElementException 
	{
		if(op=='~'){
			if(operandStack.size()<1)
				throw new NoSuchElementException();
			else{
				rightOperand=operandStack.pop();
			}
		}
		else{
			if(operandStack.size()<2)
				throw new NoSuchElementException();
			else{
				rightOperand=operandStack.pop();
				leftOperand=operandStack.pop();
			}
		}
	}


	/**
	 * Computes "leftOperand op rightOprand" or "op rightOprand" if a unary operator. 
	 * 
	 * @param op operator that acts on leftOperand and rightOperand. 
	 * @return
	 */
	private int compute(char op) throws ExpressionFormatException {

		switch(op){
			case'+':
				return leftOperand+rightOperand;
			case'-':
				return leftOperand-rightOperand;
			case'~':
				return rightOperand*-1;
			case'*':
				return leftOperand*rightOperand;
			case'/':
				if(rightOperand==0)
					throw new ExpressionFormatException("Divide by zero");
				return leftOperand/rightOperand;
			case'%':
				if(rightOperand==0)
					throw new ExpressionFormatException("Divide by zero");
				return leftOperand%rightOperand;
			case'^':
				if(rightOperand==0&&leftOperand==0)
					throw new ExpressionFormatException("0^0");
				return (int) Math.pow(leftOperand,rightOperand);
			default:
				return 0;
		}// TO MODIFY
	}
}
