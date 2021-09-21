package edu.iastate.cs228.hw1;

/**
 *  
 * @author Bo Sheng Lee
 *
 */

/*
 * A rabbit eats grass and lives no more than three years.
 */
public class Rabbit extends Animal 
{	
	/**
	 * Creates a Rabbit object.
	 * @param p: plain  
	 * @param r: row position 
	 * @param c: column position
	 * @param a: age 
	 */
	public Rabbit (Plain p, int r, int c, int a) 
	{
		plain = p;
		row = r;
		column =c;
		age =a;
	}

	/**
	 * Rabbit occupies the square.
	 * @return State.RABBIT
	 */
	public State who()
	{
		return State.RABBIT;
	}
	
	/**
	 * A rabbit dies of old age or hunger. It may also be eaten by a badger or a fox.  
	 * @param pNew     plain of the next cycle 
	 * @return Living  new life form occupying the same square
	 */
	public Living next(Plain pNew)
	{
		int[] population = new int[NUM_LIFE_FORMS];
		this.census(population);
		if(this.myAge()==RABBIT_MAX_AGE)
			return new Empty(pNew,row,column);
		else if(population[3]==0)
			return new Empty(pNew,row,column);
		else if((population[2]+population[0])>=population[4]&&population[2]>population[0])
			return new Fox(pNew,row,column,0);
		else if(population[0]>population[4])
			return new Badger(pNew,row,column,0);
		else
			return new Rabbit(pNew,row,column,this.myAge()+1);
	}
}
