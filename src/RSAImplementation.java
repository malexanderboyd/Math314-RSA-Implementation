import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Boyd on 4/26/2017.
 */
public class RSAImplementation {

    // Set to default example values, overwritten on start
     int N = 18923;
     int E = 1261;
    static boolean debugMode = true;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);


        System.out.println("Please enter the N value of public key.");
        N = in.nextInt();
        System.out.println("Please enter the E value of the public key.");
        E = in.nextInt();
    /*
        Given Example Information:

            n = 18923
            e = 1261

            1. Factor N to find prime factors

            2. Compute d as the inverse of e mod (p-1) (q-1) using the extended Euclidean algorithm

            3. Decrypt the cipher text

     */
        System

        // Find P & Q is our first steps in solving for d.
        int p = -1;
        int q = -1;

        debugPrint("Given N = 18923, and E = 1261");

        debugPrint("Factoring N to find prime factors...");
        ArrayList<Integer> primeFactors = factor(N);


        // Java 8 Streams: Thanks Intellij - http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/
        // primeFactors.forEach(System.out::println);

        if(primeFactors.get(0) != null)
        p = primeFactors.get(0);
        if(primeFactors.get(1) != null)
        q = primeFactors.get(1);

        if(p != -1 && q != -1) // We have found p & q.
        {
            debugPrint("Factoring Complete:");
            debugPrint("P: " + p);
            debugPrint("Q: " + q);
        }

        debugPrint("Finding D...");
        int d = multiplicativeInverse(E, p, q);


    }


    public static void debugPrint(String s)
    {
        if(debugMode)
            System.out.println(s);
    }

    public static ArrayList<Integer> factor(int n) {
        ArrayList<Integer> returnList = new ArrayList<>();
            for(int i = 3; i <= n; i++)
            {
                if(n % i == 0) //  I is a factor of n
                {
                    if(isPrime(i)) // check if factor is prime
                    {
                        returnList.add(i);
                    }
                }
            }
        return returnList;
    }

    public static boolean isPrime(int num)
    {
       if(num < 2)
           return false;

        for(int i = 2; i < Math.sqrt(num); i++)
        {
            if(num % i == 0)
            {
                return false;
            }
        }
        return true;
    }

    public static int multiplicativeInverse(int e, int p, int q) {
        // this should use the extended Euclidean algorithm
        // e * d = 1 (mod (p-1)(q-1))
        int phi = (p-1) * (q-1);
        return -1;
    }

    public static void ModularExponentiation() {
        // this should use the repeated squaring method
    }

    public static void numToText()
    {
        // this should convert numbers back to characters
    }

}
