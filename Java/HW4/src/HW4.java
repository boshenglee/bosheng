import java.io.IOException;
import java.io.PrintStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.lang.System;
import java.text.NumberFormat;
import java.text.DecimalFormat;


public class HW4 {

    public static void testshort(RobotPath rPath, String f) throws IOException {
	PrintStream out;

	rPath.readInput(f);                                                             
	out = new PrintStream(new FileOutputStream("std-short.txt"), true); 
	System.setOut(out);  
	rPath.planShortest();                                                                                                                                             
	rPath.output();   
    }


    public static void testquick(RobotPath rPath, String f) throws IOException {
	PrintStream out;

	rPath.readInput(f);                                                             
	out = new PrintStream(new FileOutputStream("std-quick.txt"), true); 
	System.setOut(out);  
	rPath.quickPlan();                                                                                                                                             
	rPath.output();   
    }


    public static void testtime(RobotPath rPath, String f) throws IOException {
	PrintStream out;

	rPath.readInput(f);                                                             
	NumberFormat formatter = new DecimalFormat("#0.00000");
	    out = new PrintStream(new OutputStream(){
		    public void write(int b) {
			// NO-OP
		    }
		});
	System.setOut(out);
	long start = System.currentTimeMillis();
	rPath.planShortest();
	long end1 = System.currentTimeMillis();
	/* Not needed - use timeouts */
	out = new PrintStream(new FileOutputStream("std-time.txt"), true);
	System.setOut(out);
	
	System.out.println("planShortest Time: " + 
			   formatter.format((end1 - start) / 1000d) + 
			   " seconds");
    }





    public static void main(String[] args) throws IOException {

	RobotPath rPath = new RobotPath();

	if (args[0].equals("s"))
	    testshort(rPath, args[1]);
	if (args[0].equals("q"))
	    testquick(rPath, args[1]);

	if (args[0].equals("t"))
	    testtime(rPath, args[1]);
    }
}


