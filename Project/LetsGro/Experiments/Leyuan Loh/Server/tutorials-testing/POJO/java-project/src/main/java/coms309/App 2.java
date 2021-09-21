package coms309;
import java.util.Scanner;
/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
       Scanner in = new Scanner(System.in);

       App a = new App();
       System.out.println("Enter a String: ");
       String s = in.next();
       System.out.println("What do you want to do? capitalize or reverse?");
       String opType = in.next();

       StringOps sops = new StringOps();
       System.out.println(a.doSomeOperations(s, opType, sops));

    }

    String doSomeOperations(String s, String opType, StringOps sops) {
      if (opType.equals("capitalize")) {
        return sops.capitalize(s);
      }
      else if (opType.equals("reverse")) {
        return sops.reverse(s);
      }
      else return null;
    }


}
