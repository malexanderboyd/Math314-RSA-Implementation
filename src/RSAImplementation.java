import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.plaf.synth.SynthSeparatorUI;

/**
 * Created by Boyd on 4/26/2017.
 */
public class RSAImplementation {
    // Set to default example values, overwritten on start
    static int N;
    static int E;
    static boolean debugMode = true;
    
   static ArrayList<String> finalMessage = new ArrayList<>(); //holds the characters for the final decrypted message and prints them out

    public static void main(String[] args) throws FileNotFoundException {
    	//--Reading message-------------------------------------------------
    	Scanner m = new Scanner(new File("src/message.txt"));
        ArrayList<Integer> modMessage = new ArrayList<>();
        ArrayList<Integer> newMessage = new ArrayList<>();
        
        while(m.hasNext()){
        	if(m.hasNextInt()){
        		newMessage.add(m.nextInt());
        	}else{
        		m.next();
        	}
        }
        System.out.println(newMessage);
        m.close();
       //---------------------------------------------------------------------
        
        Scanner in = new Scanner(System.in);

        System.out.println("Please enter the N value of public key.");
        N = in.nextInt();
        System.out.println("Please enter the E value of the public key.");
        E = in.nextInt();
        in.close();
       
    /*
        Given Example Information:

            n = 18923
            e = 1261

            1. Factor N to find prime factors

            2. Compute d as the inverse of e mod (p-1) (q-1) using the extended Euclidean algorithm

            3. Decrypt the cipher text

     */

        // Find P & Q is our first steps in solving for d.
        int p = -1;
        int q = -1;

        //debugPrint("Given N = 18923, and E = 1261");

        debugPrint("Factoring N to find prime factors...");
        ArrayList<Integer> primeFactors = factor(N);


        // Java 8 Streams: Thanks Intellij - http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/
        // primeFactors.forEach(System.out::println);

        if(primeFactors.get(0) != null)
        p = primeFactors.get(0);
        if(primeFactors.get(1) != null)
        q = primeFactors.get(1);

        int toitent = (p-1) * (q-1);

        if(p != -1 && q != -1) // We have found p & q.
        {
            debugPrint("Factoring Complete:");
            debugPrint("P: " + p);
            debugPrint("Q: " + q);
            debugPrint("Eulers Toitent: " + toitent);
        }

        debugPrint("Finding D...");
        int d = multiplicativeInverse(E, toitent);
        if(d == -1) // error!
        {
            System.out.println("Error! Something went wrong finding D, gcd(N,e) != 1?");
        }


        System.out.println("D:" + d);

        
        //read message array, and apply repeated squaring
       for(int i : newMessage){
            debugPrint("Current I: " + i);
        	i =  ModularExponentiation(i, d, N);
            debugPrint("Answer I: " + i);
            modMessage.add(i);

        }


        System.out.println("End of mod Exponentiation");

        //print out final message as one long string
        System.out.println("FINAL TO TEXT MESSAGE:" );
        for(int i : modMessage){
        	// When numToText gets called each block is added to the "finalMessage" array 
        	// then reversed, printed, and the list is cleared to apply the process over for each block
        	numToText(i); 
        	Collections.reverse(finalMessage);
        	for(String f : finalMessage){
        		System.out.print(f);}
        	finalMessage.clear();
        	//System.out.println("----------------------");
        }
        
        
        
        System.out.println("Final Message:" +finalMessage);
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

    public static int multiplicativeInverse(int e, int toitent) {
        // this should use the extended Euclidean algorithm
        // e * d = 1 (mod (p-1)(q-1))

        // In our example, 1261 * D = 1 (mod toitent)
        int ogToitent = toitent;
        int workingT = toitent;
        int workingE = e;
        int remainder = workingT % workingE;
        int workingM = (int) Math.floor(workingT/workingE);


        debugPrint("Euclidean Algorithm Begin");
        debugPrint(String.valueOf(workingT) + "= " + String.valueOf(workingM) + "(" + String.valueOf(workingE) + ")  + " + remainder);

        long s = 0, t = 1, old_s = 1, old_t = 0, old_r = workingT, quotient, r = workingE;

        long temp;
        while(remainder != 1)
        {
            // Shift toitent and remainder to correct positions
            workingT = workingE;
            workingE = remainder;
            remainder = workingT % workingE;
            workingM = (int) Math.floor(workingT/workingE);

            //Extended Euclidean Algorithm
            // gcd(a,b) == xa + yb
            // 1 = gcd(40, 7) == 40a + 7b = 1
            // 1 - 7b = 40a
            // 1 - 7b / 40 = a
            // 7 * d = 1 (mod 40)
            // d = e^-1 (mod 40)


            debugPrint(String.valueOf(workingT) + "= " + String.valueOf(workingM) + "(" + String.valueOf(workingE) + ")  + " + remainder);
        }

        if(remainder == 1)
        {
            // we know that the gcd(e, toitent) is 1, we use brute force to determine d.
            int d = 0;
            boolean found = false;
            while(!found)
            {
                d++;
                if((e * d) % ogToitent == 1)
                {
                    found = true;
                }
            }

            return d;
        }




        debugPrint("Euclidean Algorithm End");
        return -1;
    }
    


    public static int ModularExponentiation(int c, int d, int n) {
        // this should use the repeated squaring method
    	if(d == 0) return 1;
    	long t = ModularExponentiation(c, d/2, n);
    	long m = (t*t) % n;
    	if(d % 2 == 1)
    		m = (m * c) % n;
    	return (int) m;
    }
    
    private static String getCharForNumber(int i) {
        return i > -1 && i < 26 ? String.valueOf((char)(i + 97)) : null;
    }

    public static int numToText(int m)
    {
        // this should convert numbers back to characters
    	double t = m/26.0; //initially divide number
    	if(t == Math.floor(t)){ //check if number is whole
    		m = (int) t; //m is now the whole number
    		if(t > 26.0){
    			t = t /26.0; 
    			t = Math.floor(t) * 26.0;
    			int text = (int) (m-t);
    			//System.out.println("Num Text:" +text);
    			//System.out.println("Char: " +getCharForNumber(text));
    			finalMessage.add(getCharForNumber(text));
    			
    			m = (int) t; //assign m to t for next value
    			return numToText(m);
    		} else{ //lest than 26 so you return
    			//System.out.println("Char: " +getCharForNumber((int) t));
    			finalMessage.add(getCharForNumber((int) t));
    			return (int) t;
    		}
    	} else{ //Not whole
    		t = Math.floor(t) * 26.0;
        	int text = (int) (m - t);
        	//System.out.println("Num Text:" +text);
        	//System.out.println("Char: " +getCharForNumber(text));
        	finalMessage.add(getCharForNumber(text));
        	
    	}
    	m = (int) t;
    	 return numToText(m);
    	
    }

}
