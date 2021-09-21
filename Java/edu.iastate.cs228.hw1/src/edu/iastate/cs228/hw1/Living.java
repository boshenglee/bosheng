package edu.iastate.cs228.hw1;

/**
 *  
 * @author Bo Sheng Lee
 *
 */

/**
 * 
 * Living refers to the life form occupying a square in a plain grid. It is a 
 * superclass of Empty, Grass, and Animal, the latter of which is in turn a superclass
 * of Badger, Fox, and Rabbit. Living has two abstract methods awaiting implementation. 
 *
 */
public abstract class Living 
{
	protected Plain plain; // the plain in which the life form resides
	protected int row;     // location of the square on which 
	protected int column;  // the life form resides
	
	// constants to be used as indices. 
	protected static final int BADGER = 0; 
	protected static final int EMPTY = 1; 
	protected static final int FOX = 2; 
	protected static final int GRASS = 3; 
	protected static final int RABBIT = 4; 
	
	public static final int NUM_LIFE_FORMS = 5; 
	
	// life expectancies 
	public static final int BADGER_MAX_AGE = 4; 
	public static final int FOX_MAX_AGE = 6; 
	public static final int RABBIT_MAX_AGE = 3; 
	
	
	/**
	 * Censuses all life forms in the 3 X 3 neighborhood in a plain. 
	 * @param population  counts of all life forms
	 */
	protected void census(int[] population)
	{
		for (int i = this.row-1;i<=this.row +1;i++) {
			for (int j = this.column-1;j<=this.column+1;j++) {
				if(i>=0&&j>=0&&i<this.plain.getWidth()&&j<this.plain.getWidth()) {
					if (this.plain.grid[i][j].who() == State.BADGER)
						population[BADGER]+= 1;
					else if (this.plain.grid[i][j].who() == State.EMPTY)
						population[EMPTY]+= 1;
					else if (this.plain.grid[i][j].who() == State.FOX)
						population[FOX]+= 1;
					else if (this.plain.grid[i][j].who() == State.GRASS)
						population[GRASS]+= 1;
					else
						population[RABBIT]+= 1;
				}
			}
		}
	}

	/**
	 * Gets the identity of the life form on the square.
	 * @return State
	 */
	public abstract State who();
	
	/**
	 * Determines the life form on the square in the next cycle.
	 * @param  pNew  plain of the next cycle
	 * @return Living
	 */
	public abstract Living next(Plain pNew);
}
