package edu.iastate.cs228.hw1;

/**
 * @author Bo Sheng Lee
 *
 */

/** 
 * Empty squares are competed by various forms of life.
 */
public class Empty extends Living 
{
	/**
	 * Constructor
	 * @param p: plain
	 * @param r: row position
	 * @param c: column position
	 */
	public Empty (Plain p, int r, int c) 
	{
		plain = p;
		row = r;
		column = c;
	}

	/**
	 * A empty occupies the square.
	 * @return State.Empty
	 */
	public State who()
	{
		return State.EMPTY;
	}
	
	/**
	 * An empty square will be occupied by a neighboring Badger, Fox, Rabbit, or Grass, or remain empty. 
	 * @param pNew     plain of the next life cycle.
	 * @return Living  life form in the next cycle.   
	 */
	public Living next(Plain pNew)
	{
		int[] population = new int[NUM_LIFE_FORMS];
		this.census(population);	//call census function to manipulate the population array
		if(population[4]>1)
			return new Rabbit(pNew,row,column,0);
		else if(population[2]>1)
			return new Fox(pNew,row,column,0);
		else if(population[0]>1)
			return new Badger(pNew,row,column,0);
		else if(population[3]>=1)
			return new Grass(pNew,row,column);
		else
			return new Empty(pNew,row,column);
	}
}
