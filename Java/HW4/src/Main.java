import java.io.IOException;

public class Main {

    public static void main(String[] args) {
	// write your code here
        RobotPath rPath = new RobotPath();
        try {
            rPath.readInput("Grid.txt");

            System.out.println("\n planShortest:\n");
            rPath.planShortest();
            rPath.output();


            System.out.println("\n quickPlan:\n");
            rPath.quickPlan();
            rPath.output();

        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}
