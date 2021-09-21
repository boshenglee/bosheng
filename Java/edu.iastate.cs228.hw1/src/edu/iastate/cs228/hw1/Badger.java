package edu.iastate.cs228.hw1;

/**
 *  
 * @author Bo Sheng Lee
 *
 */

/**
 * A badger eats a rabbit and competes against a fox. 
 */
public class Badger extends Animal
{
	/**
	 * Constructor 
	 * @param p: plain
	 * @param r: row position 
	 * @param c: column position
	 * @param a: age 
	 */
	public Badger (Plain p, int r, int c, int a) 
	{
		plain = p;
		row = r;
		column = c;
		age = a;
	}
	
	/**
	 * A badger occupies the square.
	 * @return State.BADGER
	 */
	public State who()
	{
		return State.BADGER;
	}
	
	/**
	 * A badger dies of old age or hunger, or from isolation and attack by a group of foxes. 
	 * @param pNew     plain of the next cycle
	 * @return Living  life form occupying the square in the next cycle. 
	 */
	public Living next(Plain pNew)
	{
		int[] population = new int[NUM_LIFE_FORMS];
		this.census(population);	//call census function to manipulate the population array
		if(this.myAge()==BADGER_MAX_AGE)
			return new Empty(pNew,row,column);
		else if(population[0]==1 && population[2]>1)
			return new Fox(pNew,row,column,0);
		else if((population[0]+population[2])>population[4])
			return new Empty(pNew,row,column);
		else
			return new Badger(pNew,row,column,this.myAge()+1);
	}
}
