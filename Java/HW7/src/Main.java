import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

//         test minHeap
//        MinHeap mH = new MinHeap();
//        mH.add(1,5.0);
//        mH.add(11,6);
//        mH.add(2,5);
//        mH.add(3,4);
//        mH.add(10,3);
//        mH.add(9,5);
//
//        ArrayList<Integer> elements = mH.getHeap();
//        for(int i=0;i<elements.size();i++){
//            System.out.printf("%5s",elements.get(i));
//        }
//        System.out.println("\n");
//        mH.updateKey(8,3,342);
//        mH.extractMin();
//        elements = mH.getHeap();
//        for(int i=0;i<elements.size();i++){
//            System.out.printf("%5s",elements.get(i));
//        }

        // write your code here
        PathFinder pf = new PathFinder();
//      read input
        pf.readInput("testBig.txt");
        // M-Path distance
//        System.out.println("M-Path Distance: " + pf.dist2Dest(0, 87, 2, 1));
        /* produce the output:
        Distance of shortest paths: 38.2842712474619
        */
        // A variant
//        System.out.println("M-Path Distance: " + pf.dist2Dest(0, 3, 2, 2));
        /* produce the output:
        Distance of shortest paths: 38.2842712474619
        */
//        System.out.println("M-Path Distance: " + pf.dist2Dest(0, 9, 2, 2));
        /* produce the output:
        Distance of shortest paths: -1.0
        */
        // Number of M-Paths
//        System.out.println("Number of shortest paths: " + pf.noOfMPaths2Dest(0, 51, 2));
        /* produce the output:®
        Number of shortest paths: 2
        */
        // low-M-Path
//        ArrayList<Integer> path = pf.path2Dest(0,  51, 2, 1);
//        if (path == null)
//            System.out.println("No path to destination");
//        else {
//            for (int i=0; i<path.size(); i++)
//                System.out.printf("%5s", path.get(i));
//            System.out.println(); }
//        /* produce the output: 0523
//         */
        // M-Path distance to all vertices
//        double[] distances = pf.dist2All(0,2);
//        for (int i=0; i<distances.length; i++) System.out.println("distance to " + i + " is " + distances[i]);
//        System.out.println();

        /* produce the output: (NOTE DISTANCE TO 9 IS -1 AS 9 IS UNREACHABLE)
         distance to 0 is 0.0
         distance to 1 is 10.0
         distance to 2 is 28.284271247461902
         distance to 3 is 38.2842712474619
         distance to 4 is 24.14213562373095
         distance to 5 is 14.142135623730951
         distance to 6 is 19.14213562373095
         distance to 7 is 84.8528137423857
         distance to 8 is 89.8528137423857
         distance to 9 is -1.0
         */

        /*
         * R-Tree
         * */
        // cost mR-Tree
//        System.out.println("Cost of mR-Tree: " + pf.mRtreeCostFromSource(0, 2));
        /* produce the output:
        Cost of mR-Tree: 134.14213562373095
        */
        // mR-tree
//        int[] parents = pf.mRtreeFromSource(0, 2);
//        for (int i=0; i<parents.length; i++) System.out.println("parent of " + i + " is " + parents[i]);
        /* produce the output: NOTE PARENT OF 9 IS -1 AS 9 IS UNREACHABLE
        parent of 0 is 0
        parent of 1 is 0
        parent of 2 is 4
        parent of 3 is 2
        parent of 4 is 5
        parent of 5 is 0
        parent of 6 is 5
        parent of 7 is 4
        parent of 8 is 7
        parent of 9 is -1
        */

    }
}