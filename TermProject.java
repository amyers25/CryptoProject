package termproject;

/*
Alex Myers
Justin Jones
 */

import java.io.*;
import java.util.*;
import java.lang.*;
import java.io.FileNotFoundException;
import java.math.*;

public class TermProject {
    public static void main(String[] args) throws IOException{
        int alpha = 5, beta = 18074, p = 31847, a, j = 0;
        int [] rT = new int [144]; 
        int [] s = new int[72];
        int [] sA = new int[72];
        int [] m =  new int[72];
        
        rT = arrayIn();//(r,t)
        a = disLog(beta, alpha, p);//a
        s = multInvOut(rT, p); //r^-1
        sA = modExpOut(s, a, p); //s^a
                
        for(int i = 0; i < s.length; i++){
            m[i] = (rT[i+1] * sA[i])%p;//m = t(s^a)(mod p?)
            System.out.print(num(m[i]));//plaintext
        }
    }
    
    //Read Ciphertext in from file
    public static int[] arrayIn()throws IOException{
            int [] stuff = new int[144];
            int count =0;
            Scanner input = new Scanner(new File("C:\\Users\\Alex\\Desktop\\Stuff\\Schoolwork\\Fall 2016\\MATH 314\\input.txt"));
            while(input.hasNext()){
                if(input.hasNextInt()){
                    stuff[count] = input.nextInt();
                    count++;
                }
                else
                    input.next();
            }
            return stuff;
        }
     
    // Compute discreet logarithms for a                                        //Math checks out.
    public static int disLog(int beta, int alpha, int p){
            int a = 0;
            double y = 0;
            while (beta != y){
                //Beta = Alpha^a (mod p)
                y = Math.pow(a,alpha)%p;
                a++;
            }
            System.out.print(a);
            return a;
        }
        
    //Adapt multiple inverse function for use with array
    public static int[] multInvOut(int [] alpha, int p){
        int y = 0, x = 0;
        int[] s = new int[alpha.length/2];
        while (y < s.length){
            s[y] = multInverse(p, alpha[x]);
            y++;
            x+=2;
        }
        return s;
    }
                
    //function for multiplicative inverse using extended Euclidean algorithm    //Math checks out.
    public static int multInverse(int p, int r){    //z = little a
        
        int [] f = new int [20]; //f
        int [] g = new int [20]; //g
        int [] h = new int [20]; //h
        int [] j = new int [20]; //j
        
        int remainder = -999;
        int i = 2;
        int k = 0;
        int gcd=-999;
        double temp=0;
        
        f[0] = 0;
        f[1] = 1;
        g[0] = 1;
        g[1] = 0;
    
       
        
        //Using the Euclidean algorithm to find the GCD of p, r
        System.out.println("Using the Euclidean algorithm to find the GCD of p, r");
        h[0] = (int)p/r;
        j[0] = (int)p-(h[0]*r);
        System.out.printf("%d = %d x %d + %d\n", p, h[0], r, j[0]);
        h[1] = r/j[0];
        j[1] = r - (h[1]*j[0]);
        System.out.printf("%d = %d x %d + %d\n", r, h[1], j[0], j[1]);
        k=2;
        while(remainder != 0)
        {
            h[k] = j[k-2]/j[k-1];
            j[k] = j[k-2] - (h[k]*j[k-1]);
            System.out.printf("%d = %d x %d + %d\n", j[k-2], h[k], j[k-1], j[k]);
            if (j[k] == 0){
                System.out.printf("the GCD of (%d, %d) using Euclidean algorithm is: %d\n", p, r, j[k-1]);
                gcd = j[k-1];
                remainder = 0;
            }
            k++;
        }
        //Using Extended Algorithm to find r^-1
        System.out.println("Using Extended Algorithm to find r^-1");
        System.out.printf("Xo = %d, x1 = %d\n", f[0], f[1]);
        int w = 2;
        int ex=0, why=0;
        while (h[w-1] != 0){
            f[w] = -(h[w-2]*f[w-1]) + f[w-2];
           System.out.printf("X%d = -(%d*%d) + %d = %d\n", w, h[w-2], f[w-1], f[w-2], f[w]);
            ex = f[w];
            w++;
        }
        w = 2;
        while (h[w-1] != 0){
            g[w] = -(h[w-2]*g[w-1]) + g[w-2];
           System.out.printf("Y%d = -(%d*%d) + %d = %d\n", w, h[w-2], g[w-1], g[w-2], g[w]);
            why = g[w];
            w++;
        }
        
        System.out.printf("aXn + bYn = gcd(a,b)\n");
        int sum = (r*ex+p*why)%p;
        System.out.printf("(%d*%d) + (%d*%d) = %d\n", r, ex, p, why, sum);  
        
        int ex2 = ex;
        if(ex2<0){
           ex2 = Math.floorMod(ex2, p);
           ex = ex2;
        }
        System.out.printf("%d^-1 = %d\n\n",r, ex);
        return (int) ex;
    
    }
    
    //Adapt modular exponentiation for use with array
    public static int[] modExpOut (int [] s, int a, int p){
        int [] sA = new int [72];
        for (int i = 0; i < s.length; i++){
            sA[i]= powermod(s[i],a,p);
        }
        return sA;
    }
      
    //calculate modular exponentiation                                          //This function seems to work. Fuck.
    public static int powermod(int s, int a, int p) {
        if (s < 1 || a < 0 || p < 1)
            return -1;
            
    int result = 1;
    while (a > 0) {
       if ((a % 2) == 1) {
           result = (result * s) % p;
       }
       s = (s * s) % p;
       a = Math.floorDiv(a, 2);
    }
    return result;
    }

    //function for turning numerical values into plaintext                      //This function seems to work. Fuck.
    public static String num (int m){
        int p1 = 0, p2 = 0, p3 = 0, letter;
        int [] letterArr = new int [3];
        String numText = "",num = "";
        
        if (m >= 703){ 
            p1 = m/676;
            p2 = (m-(p1*676))/26;
            p3 = (m-(p1*676)-(p2*26));
        }
        else if(m > 676 && m < 702){
            p1 = m/676;
            p2 = (m-(p1*676));
        }
        else if(m < 676 && m > 26){
            p1 = m/26;
            p2 = m-(26*p1);
        }
              
        p1=p1%26;
        p2=p2%26;
        p3=p3%26;
        letterArr[0]=p1;
        letterArr[1]=p2;
        letterArr[2]=p3;
        
        for(int i=0; i<letterArr.length; i++){
            letter=letterArr[i];
            switch(letter){
            case 0:  numText = "a";
                     break;
            case 1:  numText = "b";
                     break;
            case 2:  numText = "c";
                     break;
            case 3:  numText = "d";
                     break;
            case 4:  numText = "e";
                     break;
            case 5:  numText = "f";
                     break;
            case 6:  numText = "g";
                     break;
            case 7:  numText = "h";
                     break;
            case 8:  numText = "i";
                     break;
            case 9:  numText = "j";
                     break;
            case 10: numText = "k";
                     break;
            case 11: numText = "l";
                     break;
            case 12: numText = "m";
                     break;
            case 13: numText = "n";
                     break;
            case 14: numText = "o";
                     break;
            case 15: numText = "p";
                     break;
            case 16: numText = "q";
                     break;
            case 17: numText = "r";
                     break;
            case 18: numText = "s";
                     break;
            case 19: numText = "t";
                     break;
            case 20: numText = "u";
                     break;
            case 21: numText = "v";
                     break;
            case 22: numText = "w";
                     break;
            case 23: numText = "x";
                     break;
            case 24: numText = "y";
                     break;
            case 25: numText = "z";
                     break;
            }
            num = num + numText;
        }
        return num;
    }
}