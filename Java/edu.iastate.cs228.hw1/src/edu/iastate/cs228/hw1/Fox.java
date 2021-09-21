package edu.iastate.cs228.hw1;

/**
 *  
 * @author Bo Sheng Lee
 *
 */

/**
 * A fox eats rabbits and competes against a badger. 
 */
public class Fox extends Animal 
{
	/**
	 * Constructor 
	 * @param p: plain
	 * @param r: row position 
	 * @param c: column position
	 * @param a: age 
	 */
	public Fox (Plain p, int r, int c, int a) 
	{
		plain = p;
		row = r;
		column =  c;
		age = a;
	}
		
	/**
	 * A fox occupies the square.
	 * @return State.FOX
	 */
	public State who()
	{
		return State.FOX;
	}
	
	/**
	 * A fox dies of old age or hunger, or from attack by numerically superior badgers. 
	 * @param pNew     plain of the next cycle
	 * @return Living  life form occupying the square in the next cycle. 
	 */
	public Living next(Plain pNew)
	{
		int[] population = new int[NUM_LIFE_FORMS];
		this.census(population); 	//call census function to manipulate the population array
		if(this.myAge()==FOX_MAX_AGE)
			return new Empty(pNew,row,column);
		else if(population[0]>population[2])
			return new Badger(pNew,row,column,0);
		else if((population[0]+population[2])>population[4])
			return new Empty(pNew,row,column);
		else
			return new Fox(pNew,row,column,this.myAge()+1);
	}
}
