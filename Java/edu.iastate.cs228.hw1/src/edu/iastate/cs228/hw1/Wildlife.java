package edu.iastate.cs228.hw1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 *  
 * @author Bo Sheng Lee
 *
 */

/**
 * 
 * The Wildlife class performs a simulation of a grid plain with
 * squares inhabited by badgers, foxes, rabbits, grass, or none. 
 *
 */
public class Wildlife 
{
	/**
	 * Update the new plain from the old plain in one cycle. 
	 * @param pOld  old plain
	 * @param pNew  new plain 
	 */
	public static void updatePlain(Plain pOld, Plain pNew)
	{
		for (int i = 0; i < pOld.grid.length; i++) {
			for (int j = 0; j < pOld.grid[i].length; j++) {
				pNew.grid[i][j] = pOld.grid[i][j].next(pNew); //create a new object with .next method and add into a grid from new Plain
			}
		}
	}

	/**
	 * print menu for the user
	 */
	public static void printMenu(){
		System.out.println("Simulation of Wildlife of the Plain");
		System.out.println("keys: 1 (random plain) 2 (file input) 3 (exit)");
	}
	
	/**
	 * Repeatedly generates plains either randomly or from reading files. 
	 * Over each plain, carries out an input number of cycles of evolution. 
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws IOException {

		printMenu();
		Scanner sc = new Scanner(System.in);
		int userSelection;
		int trial = 1;
		do { //do while loop for user choose to continue trial
			System.out.print("Trial "+trial+": ");
			userSelection = sc.nextInt();

			if (userSelection == 1) { //random plain
				System.out.println("Random Plain");
				System.out.print("Enter grid width: ");
				Plain even = new Plain(sc.nextInt());
				even.randomInit();
				int cycle = 0;
				do{ //do while loop to make sure user enter a positive cycle
					System.out.print("Enter the number of cycles: ");
					cycle = sc.nextInt();
				}while(cycle<=0);
				System.out.println("\nInitial plain:\n");
				System.out.println(even.toString());
				int count = 0;
				Plain odd = new Plain(even.getWidth());
				while (count < cycle) {
					if(count%2==0)
						updatePlain(even, odd);
					if(count%2!=0)
						updatePlain(odd,even);
					count++;
				}
				System.out.println("Final plain: \n");
				if(count%2==0){
					System.out.println(even.toString());
					//even.write("output.txt");
				}
				if(count%2!=0) {
					System.out.println(odd.toString());
					//odd.write("output.txt");
				}
			} else if (userSelection == 2) { // file input
				System.out.println("Plain input from a file");
				System.out.print("File name: ");
				Plain even = new Plain(sc.next());
				int cycle =0;
				do{ //do while loop to make sure user input a positive cycle
					System.out.print("Enter the number of cycles: ");
					cycle = sc.nextInt();
				}while(cycle<=0);
				System.out.println("\nInitial plain:\n");
				System.out.println(even.toString());
				int count = 0;
				Plain odd = new Plain(even.getWidth());
				while (count < cycle) {
					if(count%2==0)
						updatePlain(even, odd);
					if(count%2!=0)
						updatePlain(odd,even);
					count++;
				}
				System.out.println("Final plain: \n");
				if(count%2==0) {
					System.out.println(even.toString());
					//ev.write("output.txt");
				}
				if(count%2!=0) {
					System.out.println(odd.toString());
					//odd.write("output.txt");
				}
			} else {
				userSelection = 3;
			}
			trial++;
		}while(userSelection == 1 || userSelection ==2);
	}
}
