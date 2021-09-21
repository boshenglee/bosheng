package edu.iastate.cs228.hw2;

import java.io.FileNotFoundException;
import java.lang.NumberFormatException; 
import java.lang.IllegalArgumentException; 
import java.util.InputMismatchException;


/**
 *  
 * @author Bo Sheng Lee
 *
 */

/**
 * 
 * This class implements selection sort.   
 *
 */

public class SelectionSorter extends AbstractSorter
{
	/**
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *  
	 * @param pts  
	 */
	public SelectionSorter(Point[] pts)  
	{
		super(pts);
		algorithm = "selection sort";
	}

	
	/** 
	 * Apply selection sort on the array points[] of the parent class AbstractSorter.
	 * Code reference to lecture note COMS 228
	 * 
	 */
	@Override 
	public void sort() {
		int n = points.length;
		Point min;
		int jmin;
		for (int i = 0; i <= n - 1; i++) {
			min = points[i];
			jmin = i;
			for (int j = i + 1; j <= n - 1; j++) {
				if (pointComparator.compare(points[j], min) < 0) {
					min = points[j];
					jmin = j;
				}
			}
			swap(i, jmin);
		}
	}
}
