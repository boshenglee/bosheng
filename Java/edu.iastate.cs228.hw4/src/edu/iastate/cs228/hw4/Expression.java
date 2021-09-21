package edu.iastate.cs228.hw4;

/**
 *
 * @author Bo Sheng Lee
 *
 */

import java.util.HashMap;

public abstract class Expression 
{
	protected String postfixExpression; 		
	protected HashMap<Character, Integer> varTable; // hash map to store variables in the 

	
	protected Expression() { }
	
	
	/**
	 * Initialization with a provided hash map. 
	 * 
	 * @param varTbl
	 */
	protected Expression(String st, HashMap<Character, Integer> varTbl)
	{
		postfixExpression = st.replaceAll("\\s{2,}", " ").trim();;
		setVarTable(varTbl);
	}
	
	
	/**
	 * Initialization with a default hash map.
	 * 
	 * @param st
	 */
	protected Expression(String st) 
	{
		postfixExpression = st;
		varTable = new HashMap<Character, Integer>();
	}

	
	/**
	 * Setter for instance variable varTable.
	 * @param varTbl
	 */
	public void setVarTable(HashMap<Character, Integer> varTbl) 
	{
		varTable.putAll(varTbl);
	}
	
	
	/**
	 * Evaluates the infix or postfix expression. 
	 * 
	 * @return value of the expression 
	 * @throws ExpressionFormatException, UnassignedVariableException
	 */
	public abstract int evaluate() throws ExpressionFormatException, UnassignedVariableException;  

	
	
	// --------------------------------------------------------
	// Helper methods for InfixExpression and PostfixExpression 
	// --------------------------------------------------------

	/** 
	 * Checks if a string represents an integer.  You may call the static method 
	 * Integer.parseInt(). 
	 * 
	 * @param s
	 * @return
	 */
	protected static boolean isInt(String s) 
	{
		try{
			Integer.parseInt(s);
		}catch(NumberFormatException e) {
			return false;
		}
		return true;
	}

	
	/**
	 * Checks if a char represents an operator, i.e., one of '~', '+', '-', '*', '/', '%', '^', '(', ')'. 
	 * 
	 * @param c
	 * @return
	 */
	protected static boolean isOperator(char c) 
	{
		return c=='~'||c=='+'||c=='-'||c=='*'||c=='/'||c=='%'||c=='^'||c=='('||c==')';
	}

	
	/** 
	 * Checks if a char is a variable, i.e., a lower case English letter. 
	 * 
	 * @param c
	 * @return
	 */
	protected static boolean isVariable(char c) 
	{
		return Character.isLowerCase(c);
	}
	
	
	/**
	 * Removes extra blank spaces in a string. 
	 * @param s
	 * @return
	 */
	protected static String removeExtraSpaces(String s) 
	{
		String finalDisplay="";
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)=='('){
				finalDisplay +='(';
				i++;
			}
			else if(s.charAt(i)==')'){
				finalDisplay = finalDisplay.substring(0,finalDisplay.length()-1)+')';
			}
			else{
				finalDisplay+=s.charAt(i);
			}
		}
		finalDisplay = finalDisplay.replaceAll("\\t"," ").trim();
		return finalDisplay.replaceAll("\\s{2,}", " ").trim();
	}

}
