package edu.iastate.cs228.hw2;

/**
 *  
 * @author Bo Sheng Lee
 *
 */

/**
 * 
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input. 
 *
 */

import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Random; 


public class CompareSorters 
{
	/**
	 * Repeatedly take integer sequences either randomly generated or read from files. 
	 * Use them as coordinates to construct points.  Scan these points with respect to their 
	 * median coordinate point four times, each time using a different sorting algorithm.  
	 * 
	 * @param args
	 **/
	public static void main(String[] args) throws FileNotFoundException {

		Scanner sc = new Scanner(System.in);
		Random generator = new Random();
		int trial = 1;
		int key;
		printMenu();
		do {
			System.out.print("Trial " + trial + ": ");
			key = sc.nextInt();
			RotationalPointScanner[] scanners = new RotationalPointScanner[4];
			if (key == 1) {
				System.out.print("Enter number of random points: ");
				int numRandomPoints = sc.nextInt();
				Point[] randomPoints = generateRandomPoints(numRandomPoints, generator);
				scanners[0] = new RotationalPointScanner(randomPoints,Algorithm.SelectionSort);
				scanners[1] = new RotationalPointScanner(randomPoints,Algorithm.InsertionSort);
				scanners[2] = new RotationalPointScanner(randomPoints,Algorithm.MergeSort);
				scanners[3] = new RotationalPointScanner(randomPoints,Algorithm.QuickSort);
				System.out.println("algorithm size time (ns)");
				System.out.println("-------------------------");
				for(int i=0;i<4;i++){
					scanners[i].scan();
					System.out.println(scanners[i].stats());
					scanners[i].draw();
					//scanners[i].writePointsToFile();
				}
				System.out.println("-------------------------");
			}
			else if(key == 2){
				System.out.println("Points from a file");
				System.out.print("File Name: ");
				String fileName = sc.next();
				scanners[0] = new RotationalPointScanner(fileName, Algorithm.SelectionSort);
				scanners[1] = new RotationalPointScanner(fileName, Algorithm.InsertionSort);
				scanners[2] = new RotationalPointScanner(fileName, Algorithm.MergeSort);
				scanners[3] = new RotationalPointScanner(fileName, Algorithm.QuickSort);
				System.out.println("algorithm size time (ns)");
				System.out.println("-------------------------");
				for (int i=0;i<4;i++){
					scanners[i].scan();
					System.out.println(scanners[i].stats());
					scanners[i].draw();
					//scanners[i].writePointsToFile();
				}
				System.out.println("-------------------------");
			}
			else {
				System.exit(0);
			}
			trial++;
		}while(true);
	}

	/**
	 *
	 * @param numPts  	number of points
	 * @param rand      Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	private static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException
	{
		if(numPts<1) {throw new IllegalArgumentException();}
		Point[] randomPoints = new Point[numPts];
		for(int i = 0; i<numPts; i++){
			randomPoints[i] = new Point(rand.nextInt(101)-50,rand.nextInt(101)-50);
		}
		return randomPoints;
	}

	/**
	 * print the menu
	 */
	public static void printMenu(){
		System.out.println("Performances of Four Sorting Algorithms in Point Scanning");
		System.out.println("keys: 1 (random integers) 2 (file input) 3 (exit)");
	}
}
