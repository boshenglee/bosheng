package edu.iastate.cs228.hw1;

/**
 *  
 * @author Bo Sheng Lee
 *
 */

import java.io.*;
import java.util.Scanner;
import java.util.Random;

/**
 * 
 * The plain is represented as a square grid of size width x width. 
 *
 */
public class Plain
{
	private int width; // grid size: width X width
	public Living[][] grid; // an Living array

	/**
	 * Default constructor reads from a file
	 * @param inputFileName the file name to be input
	 * @throws FileNotFoundException throws exception
	 */
	public Plain(String inputFileName) throws FileNotFoundException
	{
		Scanner sc = new Scanner(new File(inputFileName));
		width = sc.nextLine().split("\\s+").length;
		sc = new Scanner(new File(inputFileName));
		grid = new Living[width][width];
		for(int i=0; i<width; i++){
			for(int j=0; j<width; j++) {
				String tempo = sc.next();
				if (tempo.charAt(0) == 'B'){
					if(tempo.charAt(1) == '0')
						grid[i][j] = new Badger(this, i, j, 0);
					else if(tempo.charAt(1) == '1')
						grid[i][j] = new Badger(this, i, j, 1);
					else if(tempo.charAt(1) == '2')
						grid[i][j] = new Badger(this, i, j, 2);
					else if(tempo.charAt(1) == '3')
						grid[i][j] = new Badger(this, i, j, 3);
					else
						grid[i][j] = new Badger(this, i, j, 4);
				}
				else if (tempo.charAt(0) == 'R'){
					if(tempo.charAt(1) == '0')
						grid[i][j] = new Rabbit(this, i, j, 0);
					else if(tempo.charAt(1) == '1')
						grid[i][j] = new Rabbit(this, i, j, 1);
					else if(tempo.charAt(1) == '2')
						grid[i][j] = new Rabbit(this, i, j, 2);
					else
						grid[i][j] = new Badger(this, i, j, 3);
				}
				else if(tempo.charAt(0) == 'G'){
					grid[i][j] = new Grass(this, i, j);
				}
				else if (tempo.charAt(0) == 'F'){
					if(tempo.charAt(1) == '0')
						grid[i][j] = new Fox(this, i, j, 0);
					else if(tempo.charAt(1) == '1')
						grid[i][j] = new Fox(this, i, j, 1);
					else if(tempo.charAt(1) == '2')
						grid[i][j] = new Fox(this, i, j, 2);
					else if(tempo.charAt(1) == '3')
						grid[i][j] = new Fox(this, i, j, 3);
					else if(tempo.charAt(1) == '4')
						grid[i][j] = new Fox(this, i, j, 4);
					else if(tempo.charAt(1) == '5')
						grid[i][j] = new Fox(this, i, j, 5);
					else
						grid[i][j] = new Fox(this, i, j, 6);
				}
				else
					grid[i][j] = new Empty(this, i, j);
			}
		}
	}
	
	/**
	 * Constructor that builds a w x w grid without initializing it. 
	 * @param w the grid
	 */
	public Plain(int w)
	{
		width = w;
		grid = new Living[width][width];
	}

	/**
	 * Getter for width
	 * @return width
	 */
	public int getWidth()
	{
		return width;
	}
	
	/**
	 * Initialize the plain by randomly assigning to every square of the grid  
	 * one of BADGER, FOX, RABBIT, GRASS, or EMPTY.  
	 * 
	 * Every animal starts at age 0.
	 */
	public void randomInit()
	{
		Random generator = new Random();
		for(int i=0; i<width; i++) {
			for (int j = 0; j < width; j++) {
				int rdn = generator.nextInt(5);
				if (rdn == 0)
					grid[i][j] = new Badger(this, i, j, 0);
				else if (rdn == 1)
					grid[i][j] = new Empty(this, i, j);
				else if (rdn == 2)
					grid[i][j] = new Fox(this, i, j, 0);
				else if (rdn == 3)
					grid[i][j] = new Grass(this, i, j);
				else
					grid[i][j] = new Rabbit(this, i, j, 0);
			}
		}
	}
	
	
	/**
	 * Output the plain grid. For each square, output the first letter of the living form
	 * occupying the square. If the living form is an animal, then output the age of the animal 
	 * followed by a blank space; otherwise, output two blanks.
	 * @return format want to be printed
	 */
	public String toString()
	{
		String finalDisplay="";
		for(int i=0; i<width; i++) {
			for (int j = 0; j < width; j++) {
				if (grid[i][j].who() == State.BADGER) {
					Badger b = (Badger)grid[i][j];
					finalDisplay = finalDisplay+"B"+b.myAge()+" ";
				}
				else if (grid[i][j].who() == State.EMPTY) {
					finalDisplay = finalDisplay+"E  ";
				}//got to plus age
				else if (grid[i][j].who() == State.FOX) {
					Fox f = (Fox)grid[i][j];
					finalDisplay = finalDisplay+"F"+f.myAge()+" ";
				}
				else if (grid[i][j].who() == State.GRASS) {
					finalDisplay = finalDisplay+"G  ";
				} else {
					Rabbit r = (Rabbit) grid[i][j];
					finalDisplay = finalDisplay+"R"+r.myAge()+" ";
				}
			}
			finalDisplay = finalDisplay+"\n";
		}
		return finalDisplay;
	}
	

	/**
	 * Write the plain grid to an output file.  Also useful for saving a randomly 
	 * generated plain for debugging purpose.
	 * @param outputFileName
	 * @throws FileNotFoundException
	 */
	public void write(String outputFileName) throws IOException {

		File outPut = new File(outputFileName);
		FileWriter fw = new FileWriter(outPut,true);
		PrintWriter pw = new PrintWriter(fw);
		pw.println(this.toString());
		pw.close();
	}
}
