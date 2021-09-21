package edu.iastate.cs228.hw2;

/**
 * 
 * @author 
 *
 */

import com.sun.scenario.effect.Merge;
import java.io.*;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;


/**
 * 
 * This class sorts all the points in an array by polar angle with respect to a reference point whose x and y 
 * coordinates are respectively the medians of the x and y coordinates of the original points. 
 * 
 * It records the employed sorting algorithm as well as the sorting time for comparison. 
 *
 */
public class RotationalPointScanner  
{
	private Point[] points; 
	
	private Point medianCoordinatePoint;  // point whose x and y coordinates are respectively the medians of 
	                                      // the x coordinates and y coordinates of those points in the array points[].
	private Algorithm sortingAlgorithm;
	
	protected String outputFileName;   // "select.txt", "insert.txt", "merge.txt", or "quick.txt"
	
	protected long scanTime; 	       // execution time in nanoseconds. 
	
	/**
	 * This constructor accepts an array of points and one of the four sorting algorithms as input. Copy 
	 * the points into the array points[]. Set outputFileName. 
	 * 
	 * @param  pts  input array of points 
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	public RotationalPointScanner(Point[] pts, Algorithm algo) throws IllegalArgumentException
	{
		if(pts==null||pts.length==0) {throw new IllegalArgumentException();}
		points = new Point[pts.length];
		for(int i=0; i<pts.length;i++){
			points[i] = new Point(pts[i]);
		}
		if(algo == Algorithm.SelectionSort)
			outputFileName = "select.txt";
		else if(algo == Algorithm.InsertionSort)
			outputFileName = "insertion.txt";
		else if(algo == Algorithm.MergeSort)
			outputFileName = "merge.txt";
		else
			outputFileName = "quick.txt";
		sortingAlgorithm = algo;
	}

	
	/**
	 * This constructor reads points from a file. Set outputFileName. 
	 * 
	 * @param  inputFileName
	 * @throws FileNotFoundException 
	 * @throws InputMismatchException   if the input file contains an odd number of integers
	 */
	protected RotationalPointScanner(String inputFileName, Algorithm algo) throws FileNotFoundException, InputMismatchException
	{
		try {
			Scanner sc = new Scanner(new File(inputFileName));
			int size = 0;
			while (sc.hasNextInt()) {
				int runner = sc.nextInt();
				size++;
			}
			if (size % 2 != 0) {
				throw new InputMismatchException();
			}

			points = new Point[size / 2];
			int counter = 0;
			int x = 0;
			int y = 0;
			sc = new Scanner(new File(inputFileName));
			while (sc.hasNextInt()) {
				x = sc.nextInt();
				if (sc.hasNextInt()) {
					y = sc.nextInt();
				}
				points[counter++] = new Point(x, y);
			}
		}catch(FileNotFoundException e) {
			throw new FileNotFoundException();
		}
		if(algo == Algorithm.SelectionSort)
			outputFileName = "select.txt";
		else if(algo == Algorithm.InsertionSort)
			outputFileName = "insertion.txt";
		else if(algo == Algorithm.MergeSort)
			outputFileName = "merge.txt";
		else
			outputFileName = "quick.txt";
		sortingAlgorithm = algo;
	}

	
	/**
	 * Carry out three rounds of sorting using the algorithm designated by sortingAlgorithm as follows:  
	 *    
	 *     a) Sort points[] by the x-coordinate to get the median x-coordinate. 
	 *     b) Sort points[] again by the y-coordinate to get the median y-coordinate.
	 *     c) Construct medianCoordinatePoint using the obtained median x- and y-coordinates. 
	 *     d) Sort points[] again by the polar angle with respect to medianCoordinatePoint.
	 *  
	 * Based on the value of sortingAlgorithm, create an object of SelectionSorter, InsertionSorter, MergeSorter,
	 * or QuickSorter to carry out sorting. Copy the sorting result back onto the array points[] by calling 
	 * the method getPoints() in AbstractSorter. 
	 *
	 *
	 */
	public void scan()
	{
		AbstractSorter aSorter;
		int counter=0;
		int xReference=0;
		int yReference=0;
		if (sortingAlgorithm == Algorithm.SelectionSort) {
			aSorter = new SelectionSorter(points);
			while(counter<3) {
				if(counter==2)
				{
					medianCoordinatePoint = new Point(xReference,yReference);
					aSorter.setReferencePoint(medianCoordinatePoint);
				}
				aSorter.setComparator(counter);
				long start =System.nanoTime();
				aSorter.sort();
				long end = System.nanoTime();
				long roundTime = end-start;
				scanTime += roundTime;
				if(counter==0){xReference=aSorter.getMedian().getX();}
				if(counter==1){yReference=aSorter.getMedian().getY();}
				counter++;
			}aSorter.getPoints(points);
		} else if (sortingAlgorithm == Algorithm.InsertionSort) {
			aSorter = new InsertionSorter(points);
			while(counter<3) {
				if(counter==2)
				{
					medianCoordinatePoint = new Point(xReference,yReference);
					aSorter.setReferencePoint(medianCoordinatePoint);
				}
				aSorter.setComparator(counter);
				long start =System.nanoTime();
				aSorter.sort();
				long end = System.nanoTime();
				long roundTime = end-start;
				scanTime += roundTime;
				if(counter==0){xReference=aSorter.getMedian().getX();}
				if(counter==1){yReference=aSorter.getMedian().getY();}
				counter++;
			}aSorter.getPoints(points);
		} else if (sortingAlgorithm == Algorithm.MergeSort) {
			aSorter = new MergeSorter(points);
			while(counter<3) {
				if(counter==2)
				{
					medianCoordinatePoint = new Point(xReference,yReference);
					aSorter.setReferencePoint(medianCoordinatePoint);
				}
				aSorter.setComparator(counter);
				long start =System.nanoTime();
				aSorter.sort();
				long end = System.nanoTime();
				long roundTime = end-start;
				scanTime += roundTime;
				if(counter==0){xReference=aSorter.getMedian().getX();}
				if(counter==1){yReference=aSorter.getMedian().getY();}
				counter++;
			}aSorter.getPoints(points);
		} else {
			aSorter = new QuickSorter(points);
			while(counter<3) {
				if(counter==2)
				{
					medianCoordinatePoint = new Point(xReference,yReference);
					aSorter.setReferencePoint(medianCoordinatePoint);
				}
				aSorter.setComparator(counter);
				long start =System.nanoTime();
				aSorter.sort();
				long end = System.nanoTime();
				long roundTime = end-start;
				scanTime += roundTime;
				if(counter==0){xReference=aSorter.getMedian().getX();}
				if(counter==1){yReference=aSorter.getMedian().getY();}
				counter++;
			}aSorter.getPoints(points);
		}
	}
	
	
	/**
	 * Outputs performance statistics in the format: 
	 * 
	 * <sorting algorithm> <size>  <time>
	 */
	public String stats()
	{
		String finalDisplay ="";
		finalDisplay += sortingAlgorithm+" "+points.length+" "+scanTime;
		return finalDisplay;

	}

	/**
	 * Write points[] after a call to scan().  When printed, the points will appear 
	 * in order of polar angle with respect to medianCoordinatePoint with every point occupying a separate 
	 * line.  The x and y coordinates of the point are displayed on the same line with exactly one blank space 
	 * in between. 
	 */
	@Override
	public String toString()
	{
		String finalDisplay="";
		for(int i=0; i<points.length;i++){
			finalDisplay += points[i].getX()+" "+points[i].getY()+"\n";
		}
		return finalDisplay;
	}

	/**
	 *  
	 * This method, called after scanning, writes point data into a file by outputFileName. The format 
	 * of data in the file is the same as printed out from toString().  The file can help you verify 
	 * the full correctness of a sorting result and debug the underlying algorithm. 
	 * 
	 * @throws FileNotFoundException
	 */
	public void writePointsToFile() throws FileNotFoundException
	{
		try {
			File file = new File(outputFileName);
			PrintWriter writer = new PrintWriter(file);
			writer.println(toString());
			writer.close();
		}catch(FileNotFoundException e){
			throw new FileNotFoundException();
		}
	}

	
	/**
	 * This method is called after each scan for visually check whether the result is correct.  You  
	 * just need to generate a list of points and a list of segments, depending on the value of 
	 * sortByAngle, as detailed in Section 4.1. Then create a Plot object to call the method myFrame().  
	 */
	public void draw()
	{
		int counterSeg = 0;
		int numSegs = getSize();// number of segments to draw
		Segment[] segments = new Segment[numSegs];
		for(int i=0;i<points.length-1;i++){  //connect evert distinct consecutive point
			if(!points[i].equals(points[i+1])) {
				segments[counterSeg++] = new Segment(points[i], points[i + 1]);
			}
		}
		if(!points[0].equals(points[points.length-1])){  //connect first point and last point if different
			segments[counterSeg++] = new Segment(points[points.length-1],points[0]);
		}
		segments[counterSeg++] = new Segment(medianCoordinatePoint,points[0]); //connect medianPoint to first point
		for(int i=0;i<points.length;i++){    //connect median point to every point in array without repeating
			if(!points[i].equals(segments[counterSeg-1].getQ())){
				segments[counterSeg++] = new Segment(medianCoordinatePoint, points[i]);
			}
		}

		String sort = null; 
		
		switch(sortingAlgorithm)
		{
		case SelectionSort: 
			sort = "Selection Sort"; 
			break; 
		case InsertionSort: 
			sort = "Insertion Sort"; 
			break; 
		case MergeSort: 
			sort = "Mergesort"; 
			break; 
		case QuickSort: 
			sort = "Quicksort"; 
			break; 
		default: 
			break; 		
		}
		
		// The following statement creates a window to display the sorting result.
		Plot.myFrame(points, segments, sort);
		
	}

	/**
	 * To calculate the size of segment array
	 * @return	size
	 */
	private int getSize(){
		int size = points.length*2;
		for(int i=0;i<points.length-1;i++){
			if(points[i].equals(points[i+1])) { //if the point is repeated
				size -= 2;
			}
		}
		if(points[0].equals(points[points.length-1])){ //if the last point is equal to first point.
			size -= 2;
		}
		return size;
	}
}
