package edu.iastate.cs228.hw2;

/**
 *  
 * @author	Bo Sheng Lee
 *
 */

import java.util.Comparator;
import java.io.FileNotFoundException;
import java.lang.IllegalArgumentException; 
import java.util.InputMismatchException;

/**
 * 
 * This abstract class is extended by SelectionSort, InsertionSort, MergeSort, and QuickSort.
 * It stores the input (later the sorted) sequence. 
 *
 */
public abstract class AbstractSorter
{
	
	protected Point[] points;    // array of points operated on by a sorting algorithm. 
	                             // stores ordered points after a call to sort(). 
	
	protected String algorithm = null; // "selection sort", "insertion sort", "mergesort", or
	                                   // "quicksort". Initialized by a subclass constructor.
		 
	protected Comparator<Point> pointComparator = null;  
	
	private Point referencePoint = null; 	      // common reference point for computing the polar angle 

	/**
	 * This constructor accepts an array of points as input. Copy the points into the array points[]. 
	 * 
	 * @param  pts  input array of points 
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	protected AbstractSorter(Point[] pts) throws IllegalArgumentException
	{
		if(pts==null||pts.length==0) {throw new IllegalArgumentException();}
		points = new Point[pts.length];
		for(int i=0; i<pts.length;i++){
			points[i] = new Point(pts[i]);
		}
	}

	/**
	 * 
	 * @param p
	 * @throws IllegalArgumentException  if p == null
	 */
	public void setReferencePoint(Point p) throws IllegalArgumentException 
	{
		if(p==null) {throw new IllegalArgumentException();}
		referencePoint = p;
	}
	

	/**
	 * @param order  0   by x-coordinate 
	 * 				 1   by y-coordinate
	 * 			     2   by polar angle w.r.t referencePoint 
	 * 
	 * @throws IllegalArgumentException if order is less than 0 or greater than 2
	 *         IllegalStateException if order == 2 and referencePoint == null; 
	 */
	public void setComparator(int order) throws IllegalArgumentException, IllegalStateException
	{
		if(order<0||order>2)
			throw new IllegalArgumentException();
		if(order==2&&referencePoint==null)
			throw new IllegalStateException();

		if(order==0){
			pointComparator = Point.xComparator;
		}
		else if(order==1){
			pointComparator = Point.yComparator;
		}
		else{
			if(referencePoint==null){
				setReferencePoint(referencePoint);
			}
			pointComparator = new PolarAngleComparator(referencePoint);

		}
	}

	/**
	 * Use the created pointComparator to conduct sorting.
	 */
	protected abstract void sort();
	
	
	/**
	 * Obtain the point in the array points[] that has median index 
	 * 
	 * @return	median point 
	 */
	public Point getMedian() {return points[(points.length-1)/2];}
	
	
	/**
	 * Copys the array points[] onto the array pts[]. 
	 * 
	 * @param pts
	 */
	public void getPoints(Point[] pts)
	{
		for(int i=0;i<pts.length;i++){
			pts[i] = new Point(points[i]);
		}
	}

	/**
	 * Swaps the two elements indexed at i and j respectively in the array points[]. 
	 * 
	 * @param i
	 * @param j
	 */
	protected void swap(int i, int j)
	{
		Point temp = points[i];
		points[i]  = points[j];
		points[j] = temp;
	}	
}
