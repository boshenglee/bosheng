package edu.iastate.cs228.hw1;

/**
 *  
 * @author Bo Sheng Lee
 *
 */

/*
 * This class is to be extended by the Badger, Fox, and Rabbit classes.
 * Implements MyAge class
 */
public abstract class Animal extends Living implements MyAge
{
	protected int age;   // age of the animal 

	@Override
	/**
	 * 
	 * @return age of the animal 
	 */
	public int myAge()
	{
		return age;
	}
}
