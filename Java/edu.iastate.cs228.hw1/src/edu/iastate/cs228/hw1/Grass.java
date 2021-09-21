package edu.iastate.cs228.hw1;

/**
 *  
 * @author Bo Sheng Lee
 *
 */

/**
 * Grass remains if more than rabbits in the neighborhood; otherwise, it is eaten. 
 *
 */
public class Grass extends Living 
{
	/**
	 * Constructor
	 * @param p: plain
	 * @param r: row position
	 * @param c: column position
	 */
	public Grass (Plain p, int r, int c) 
	{
		plain = p;
		row = r;
		column = c;
	}

	/**
	 * A grass occupies the square.
	 * @return State.GRASS
	 */
	public State who()
	{
		return State.GRASS;
	}

	/**
	 * Grass can be eaten out by too many rabbits. Rabbits may also multiply fast enough to take over Grass.
	 * @param  pNew  plain of the next cycle
	 * @return Living life form occupying the square in the next cycle.
	 */
	public Living next(Plain pNew)
	{
		int[] population = new int[NUM_LIFE_FORMS];
		this.census(population);	//call census function to manipulate the population array
		if(population[4]>=(3*population[3]))
			return new Empty(pNew,row,column);
		else if(population[4]>=3)
			return new Rabbit(pNew,row,column,0);
		else
			return new Grass(pNew,row,column);
	}
}
