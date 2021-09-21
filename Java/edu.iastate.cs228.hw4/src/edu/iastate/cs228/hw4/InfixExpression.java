package edu.iastate.cs228.hw4;

/**
 *  
 * @author
 *
 */

import java.util.HashMap;
import java.util.Scanner;

/**
 * 
 * This class represents an infix expression. It implements infix to postfix conversion using 
 * one stack, and evaluates the converted postfix expression.    
 *
 */

public class InfixExpression extends Expression 
{
	private String infixExpression;   	// the infix expression to convert		
	private boolean postfixReady = false;   // postfix already generated if true
	private int rankTotal = 0;		// Keeps track of the cumulative rank of the infix expression.
	
	private PureStack<Operator> operatorStack; 	  // stack of operators 
	
	
	/**
	 * Constructor stores the input infix string, and initializes the operand stack and 
	 * the hash map.
	 * 
	 * @param st  input infix string. 
	 * @param varTbl  hash map storing all variables in the infix expression and their values. 
	 */
	public InfixExpression (String st, HashMap<Character, Integer> varTbl)
	{
		infixExpression = st.replaceAll("\\s{2,}", " ").trim();
		postfixExpression ="";
		operatorStack = new ArrayBasedStack<>();
		varTable = new HashMap<Character, Integer>();
		setVarTable(varTbl);
	}
	

	/**
	 * Constructor supplies a default hash map. 
	 * 
	 * @param s
	 */
	public InfixExpression (String s)
	{
		infixExpression = s.replaceAll("\\s{2,}", " ").trim();
		operatorStack = new ArrayBasedStack<>();
		varTable = new HashMap<Character, Integer>();
		postfixExpression = "";

	}
	

	/**
	 * Outputs the infix expression according to the format in the project description.
	 */
	@Override
	public String toString()
	{
		return Expression.removeExtraSpaces(infixExpression);
	}
	
	
	/** 
	 * @return equivalent postfix expression, or  
	 * 
	 *         a null string if a call to postfix() inside the body (when postfixReady 
	 * 		   == false) throws an exception.
	 */
	public String postfixString() 
	{
		try {
			postfix();
			return Expression.removeExtraSpaces(postfixExpression);
		}catch (ExpressionFormatException e) {
			postfixExpression = "";
			operatorStack = new ArrayBasedStack<>();
			return null;
		}

	}


	/**
	 * Resets the infix expression. 
	 * 
	 * @param st
	 */
	public void resetInfix (String st)
	{
		infixExpression = st;
	}


	/**
	 * Converts infix expression to an equivalent postfix string stored at postfixExpression.
	 * If postfixReady == false, the method scans the infixExpression, and does the following
	 * (for algorithm details refer to the relevant PowerPoint slides): 
	 * 
	 *     1. Skips a whitespace character.
	 *     2. Writes a scanned operand to postfixExpression. 
	 *     3. When an operator is scanned, generates an operator object.  In case the operator is 
	 *        determined to be a unary minus, store the char '~' in the generated operator object.
	 *     4. If the scanned operator has a higher input precedence than the stack precedence of 
	 *        the top operator on the operatorStack, push it onto the stack.   
	 *     5. Otherwise, first calls outputHigherOrEqual() before pushing the scanned operator 
	 *        onto the stack. No push if the scanned operator is ). 
     *     6. Keeps track of the cumulative rank of the infix expression. 
     *     
     *  During the conversion, catches errors in the infixExpression by throwing 
     *  ExpressionFormatException with one of the following messages:
     *  
     *      -- "Operator expected" if the cumulative rank goes above 1;
     *      -- "Operand expected" if the rank goes below 0; 
     *      -- "Missing '('" if scanning a �)� results in popping the stack empty with no '(';
     *      -- "Missing ')'" if a '(' is left unmatched on the stack at the end of the scan; 
     *      -- "Invalid character" if a scanned char is neither a digit nor an operator; 
     *   
     *  If an error is not one of the above types, throw the exception with a message you define.
     *      
     *  Sets postfixReady to true.  
	 */
	public void postfix() throws ExpressionFormatException
	{
		if(postfixReady==false){
			String[] splited = infixExpression.split(" ");
			for(int i=0; i<splited.length;i++) {
				String token = splited[i];
				char c = token.charAt(0);

				if(Expression.isInt(token)) {
					postfixExpression += token + " ";
					rankTotal += 1;
				}
				else if(Expression.isVariable(c)){
					postfixExpression += token+" ";
					rankTotal += 1;
				}

				else if (Expression.isOperator(c)){
					Operator op ;
					if(checkUnaryMinus(i,c,splited)) {
						op = new Operator('~');
					}
					else {
						op = new Operator(c);
						if(op.operator=='+'||op.operator=='-'||op.operator=='*'||op.operator=='/'||op.operator=='%'||op.operator=='^') {
							rankTotal -= 1;
							if (i == splited.length - 1)
								throw new ExpressionFormatException("Invalid expression end with operator '"+c+"'");
						}
					}
					if(!operatorStack.isEmpty()){
						if(op.compareTo(operatorStack.peek())==1){
							operatorStack.push(op);
						}
						else{
							outputHigherOrEqual(op);
							if(op.operator!=')')
								operatorStack.push(op);
						}
					}
					else{
						operatorStack.push(op);
					}
				}
				else{
					throw new ExpressionFormatException("Invalid Character");
				}
				if(rankTotal>1)
					throw new ExpressionFormatException("Operator Expexted");
				if(rankTotal<0)
					throw new ExpressionFormatException("Operand Expexted");
			}
			while(!operatorStack.isEmpty()){
				if(operatorStack.peek().operator=='('){
					throw new ExpressionFormatException("Missing '('");
				}
				postfixExpression += operatorStack.pop().operator + " ";
			}
			postfixReady =true;
		}
	}
	
	
	/**
	 * This function first calls postfix() to convert infixExpression into postfixExpression. Then 
	 * it creates a PostfixExpression object and calls its evaluate() method (which may throw  
	 * an exception).  It also passes any exception thrown by the evaluate() method of the 
	 * PostfixExpression object upward the chain. 
	 * 
	 * @return value of the infix expression 
	 * @throws ExpressionFormatException,UnassignedVariableException
	 */
	public int evaluate() throws ExpressionFormatException, UnassignedVariableException {
		postfix();
		PostfixExpression p;
		p  = new PostfixExpression(postfixExpression,varTable);
		return p.evaluate();
    }


	/**
	 * Pops the operator stack and output as long as the operator on the top of the stack has a 
	 * stack precedence greater than or equal to the input precedence of the current operator op.  
	 * Writes the popped operators to the string postfixExpression.  
	 * 
	 * If op is a ')', and the top of the stack is a '(', also pops '(' from the stack but does 
	 * not write it to postfixExpression. 
	 * 
	 * @param op  current operator
	 */
	private void outputHigherOrEqual(Operator op) throws ExpressionFormatException {
		boolean checkMissing = true;
		while(operatorStack.peek().compareTo(op)>=0){
			postfixExpression += operatorStack.pop().operator+ " ";
			if(operatorStack.isEmpty()){
				break;
			}
			if(op.operator == ')' && operatorStack.peek().operator=='('){
				checkMissing = false;
				operatorStack.pop();
			}
			if(operatorStack.isEmpty()){
				break;
			}
		}
		if(op.operator==')'&&checkMissing){
			throw new ExpressionFormatException("Missing '('");
		}
	}

	/**
	 * check whether a '-' in expression is binary or unary minus
	 * @param i
	 * @param ch
	 * @param s
	 * @return bolean value
	 */
	public boolean checkUnaryMinus(int i,char ch,String[] s){
		if(i==0 && ch =='-')
			return true;
		if(i==0 && ch!= '-')
			return false;
		else if(Expression.isOperator(s[i-1].charAt(0))&&s[i-1].charAt(0)!='('&&s[i-1].charAt(0)!=')' && ch == '-')
			return true;
		else if(infixExpression.charAt(i-1)=='(' && ch =='-')
			return true;
		else
			return false;
	}
	
	// other helper methods if needed
}
