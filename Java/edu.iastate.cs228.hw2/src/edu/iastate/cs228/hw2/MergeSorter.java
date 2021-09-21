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
 * This class implements the mergesort algorithm.   
 *
 */

public class MergeSorter extends AbstractSorter
{
	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *  
	 * @param pts   input array of integers
	 */
	public MergeSorter(Point[] pts) 
	{
		super(pts);
		algorithm = "merge sort";
	}

	/**
	 * Perform mergesort on the array points[] of the parent class AbstractSorter.
	 *  Code reference to lecture note COMS 228
	 * 
	 */
	@Override 
	public void sort() {mergeSortRec(points);}
	
	/**
	 * This is a recursive method that carries out mergesort on an array pts[] of points. One 
	 * way is to make copies of the two halves of pts[], recursively call mergeSort on them, 
	 * and merge the two sorted subarrays into pts[].   
	 * 
	 * @param pts	point array 
	 */
	private void mergeSortRec(Point[] pts)
	{
		int n = pts.length;
		if(n<=1) {return;}

		int mid = n/2;
		Point[] left = new Point[mid];
		Point[] right = new Point[n-mid];

		for(int i = 0; i < mid; i++) {
			left[i] = pts[i];
		}
		for(int i = mid; i < n; i++) {
			right[i - mid] = pts[i];
		}
		mergeSortRec(left);
		mergeSortRec(right);
		merge(left,right,pts);
	}

	/**
	 * merge the left and right array
	 * @param left
	 * @param right
	 * @param pts
	 */
	private void merge(Point[] left,Point[] right, Point[] pts){
		int p = left.length;
		int q = right.length;
		int i=0;
		int j=0;
		int k=0;
		while(i < p && j < q){
			if (pointComparator.compare(left[i], right[j]) <= 0){
				pts[k++] = new Point(left[i++]);
			} else {
				pts[k++] = new Point(right[j++]);
			}
		}
		while(i<p){
			pts[k++] = new Point(left[i++]);
		}
		while(j<q) {
			pts[k++] = new Point(right[j++]);
		}
	}
}
