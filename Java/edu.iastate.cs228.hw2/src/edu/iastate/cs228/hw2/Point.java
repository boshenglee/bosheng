 package edu.iastate.cs228.hw2;

 import java.util.Comparator;

 /**
 *  
 * @author Bo Sheng Lee
 *
 */

 /**
  * this class implement Comparable and have x & y variable and some Point method
  */
 public class Point implements Comparable<Point>
{
	private int x; 
	private int y;
	
	public static boolean xORy;  // compare x coordinates if xORy == true and y coordinates otherwise 
	                             // To set its value, use Point.xORy = true or false. 
	
	public Point()  // default constructor
	{
		x=0;
		y=0;
	}
	
	public Point(int x, int y)
	{
		this.x = x;  
		this.y = y;   
	}
	
	public Point(Point p) { // copy constructor
		x = p.getX();
		y = p.getY();
	}

	public int getX() { return x; }
	public int getY() { return y; }
	
	/** 
	 * Set the value of the static instance variable xORy. 
	 * @param xORy
	 */
	public static void setXorY(boolean xORy)
	{
		Point.xORy =xORy;
	}

	/**
	 * method to check the equality of two Point
	 * @param obj
	 * @return the value of boolean
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null || obj.getClass() != this.getClass())
		{
			return false;
		}
    
		Point other = (Point) obj;
		return x == other.x && y == other.y;   
	}

	/**
	 * Compare this point with a second point q depending on the value of the static variable xORy 
	 * @param 	q 
	 * @return  -1  if (xORy == true && (this.x < q.x || (this.x == q.x && this.y < q.y))) 
	 *                || (xORy == false && (this.y < q.y || (this.y == q.y && this.x < q.x)))
	 * 		    0   if this.x == q.x && this.y == q.y)  
	 * 			1	otherwise 
	 */
	public int compareTo(Point q)
	{
		if(xORy==true&&(this.x<q.x||(this.x==q.x&&this.y<q.y))) { return -1; }
		else if(xORy==false&&this.y<q.y||(this.y==q.y&&this.x<q.x)) {return -1;}
		else if(this.x==q.x&&this.y==q.y){return 0;}
		else {return 1;}
	}

	/**
	 * Static method to set comparator to compare x
	 * @return p1.compareTo(p2)
	 */
	static Comparator<Point> xComparator = new Comparator<Point>() {
		@Override
		public int compare(Point p1, Point p2) {
			Point.setXorY(true);
			return p1.compareTo(p2); }
	};

	/**
	 * Static method to set comparator to compare y
	 * @return p1.compareTo(p2)
	 */
	static Comparator<Point> yComparator = new Comparator<Point>() {
		@Override
		public int compare(Point p1, Point p2) {
			Point.setXorY(false);
			return p1.compareTo(p2); }
	};
	
	/**
	 * Output a point in the standard form (x, y). 
	 */
	@Override
    public String toString() 
	{
		String finalResult = "";
		finalResult = finalResult + "(" + x +","+y+")"+" ";
		return finalResult;
	}
}
