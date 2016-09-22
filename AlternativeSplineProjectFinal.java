import java.util.Scanner;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.lang.Math;

/*Description of Program
 * 
 * This program asks the user for input for interpolating points (which must be in ascending order by x-coordinate, 
 * and must be inputted in a certain format). Press enter after entering each point.
 * Then the program asks the user whether he or she wants a clamped or natural spline. 
 * If the user wants clamped, the program will ask for the derivatives at the endpoints
 * The program will then output the equations for spline pieces for each specified interval
 * The program will then ask the user to input a point between the two endpoint x-coordinates to evaluate
 * Finally, the program will output the value of the spline at that point, and ask for another point until the user signals stop.
 * 
 * Enjoy!
 * 
 * */

//NOTE: THE d ArrayList eventually becomes the solution that is printed
public class AlternativeSplineProjectFinal
{

  String pointinput;
  ArrayList<Double> tempx = new ArrayList<Double>(); //all double values of x coordinates
  ArrayList<Double> tempy = new ArrayList<Double>(); //all double values of y coordinates
  ArrayList<Double> a = new ArrayList<Double>(); //all values h0 to hn-2
  ArrayList<Double> b = new ArrayList<Double>(); //all 2(h + h) values and 0's at ends
  ArrayList<Double> c = new ArrayList<Double>(); //all values h1 to hn-1
  ArrayList<Double> d = new ArrayList<Double>(); //all the other random stuff from y values 
  ArrayList<Double> h = new ArrayList<Double>();//all h values
  //for answer
  ArrayList<Double> aco = new ArrayList<Double>();
  ArrayList<Double> bco = new ArrayList<Double>();
  ArrayList<Double> cco = new ArrayList<Double>();
  ArrayList<Double> dco = new ArrayList<Double>();
  
  
  //NOTE: THE d ArrayList eventually becomes the solution that is printed
  public void createArrayListsOpen(ArrayList<String> points)
   {  

     for(int i = 0; i < points.size()-1; i++)
     {
       int j = points.get(i).indexOf(",");
       int k = points.get(i).indexOf("(");
       Double don = Double.parseDouble(points.get(i).substring(k+1, j));
       tempx.add(don);
     }
     for(int i = 0; i < points.size()-1; i++)
            {
              int j = points.get(i).indexOf(",");
              int k = points.get(i).indexOf(")"); 
              Double doy = Double.parseDouble(points.get(i).substring(j+1, k));
              tempy.add(doy);
            } 
     
     for(int i = 1; i < tempx.size(); i++)
     {
       h.add(tempx.get(i) - tempx.get(i-1));
     }
     
     
     //creating a  
    for(int i = 0; i < h.size()-1; i++)
    {
     a.add(h.get(i));

    }
    a.add(0.0);    
     //creating b
     
     b.add(1.0);
     for(int i = 0; i < h.size()-1; i++)
     {
       b.add(2*(h.get(i)+h.get(i+1)));
     }
     b.add(1.0);
       
     //creating c
     
     c.add(0.0);
    for(int i = 1; i < h.size(); i++)
    {
     c.add(h.get(i));

    }
     //creating d
     d.add(0.0);
     
     

     for(int i = 2; i < tempy.size(); i++)
            {
             d.add(3/c.get(i-1)*(tempy.get(i)-tempy.get(i-1)) - 3/a.get(i-2)*(tempy.get(i-1) - tempy.get(i-2)));
            }
     d.add(0.0); 
     



   }
   public void createArrayListsClamped(ArrayList<String> points, ArrayList<Double> derivatives)
     {
       createArrayListsOpen(points);
       a.set(a.size()-1, c.get(c.size()-1));
       c = a;
       b.set(0, a.get(0));
       b.set(b.size()-1, c.get(c.size()-1));
       d.set(0, 3/(a.get(0)*(tempy.get(1) - tempy.get(0))-3*derivatives.get(0)));
       d.set(d.size()-1, 3*derivatives.get(1) - 
             3/(a.get(a.size()-1)*(tempy.get(tempy.size()-1) - tempy.get(tempy.size()-2))));
       //after creating matrix, calc ans
       tridiagonal(a,b,c,d);
       for(int i=0; i<d.size();i++)
         {
         System.out.println("gd");
           System.out.println(d.get(i));
         }
     }
   public void tridiagonal(ArrayList<Double> a1, ArrayList<Double> b1, ArrayList<Double> c1, ArrayList<Double> d1 )
   {
     //credit to wikipedia for inspiratoin
   /*Forward sweep*/
    //*a /= *b;
   int len = b1.size();
   for(int i=1;i<len-1;i++)
   {
     b1.set(i, b1.get(i) - (a1.get(i-1)*c1.get(i-1)));
     a1.set(i, a1.get(i)/b1.get(i)); 
   }
   b1.set(len-1, b1.get(len-1) - (a1.get(len-2)*c1.get(len-2))); 
   for(int j=1;j<len;j++)
   {
     d1.set(j, d1.get(j) - (d1.get(j-1)*a1.get(j-1)));
     d1.set(len-1, d1.get(len-1)/b1.get(len-1));
   }
    /*Backward sweep*/
   for(int k=len-2;k>=0;k--)
   {
     d1.set(k, d1.get(k) - (d1.get(k+1)*c1.get(k)));
     d1.set(k, d1.get(k)/b1.get(k));
   }
   }
   
   public void findOtherValues()
   {
     cco = d; //assign answers of tridiagonal to c's
     System.out.println(d.size());

     
     for(int j = 0; j < cco.size()-1; j++)
     {
       dco.add(-0.33333333333333*1.0/h.get(j)*cco.get(j) + 0.33333333333333*1.0/h.get(j)*cco.get(j+1));
     }
     
     for(int j = 0; j < cco.size()-1; j++)
     {
       bco.add((tempy.get(j+1)-tempy.get(j))/h.get(j)-cco.get(j)*h.get(j)-dco.get(j)*Math.pow(h.get(j),2));
     }
     aco = tempy;
     aco.remove(aco.size()-1);


     for(int i = 0; i < cco.size()-1; i++)
     {
       System.out.println(Double.toString(aco.get(i)) + " + " + Double.toString(bco.get(i)) + "(x-" + Double.toString(tempx.get(i)) + ") + "
                            + Double.toString(cco.get(i)) + "(x-" + Double.toString(tempx.get(i)) + ")^2 + "
                            + Double.toString(dco.get(i)) + "(x-" + Double.toString(tempx.get(i)) + ")^3" + 
                          " | |  From: " + Double.toString(tempx.get(i)) + "  to  " + Double.toString(tempx.get(i+1)));
     }
     
   }
   
   public void evaluatePoint(String s)
   {
     int index = 0;
     Double de = Double.parseDouble(s);
     for(int i = 0; i < tempx.size()-1; i++)
     {
       if (de > tempx.get(i) && de <= tempx.get(i+1))
       {
         index = i;
       }
     }
     Double ans = aco.get(index) + bco.get(index)*(de-tempx.get(index)) + cco.get(index)*Math.pow(de-tempx.get(index), 2.0) + 
                  dco.get(index)*Math.pow(de-tempx.get(index), 3.0);
     System.out.println("Spline Evaluated at " + Double.toString(de) + " is " + Double.toString(ans));
   }
   public static void main(String [] args)
   {
     AlternativeSplineProjectFinal as = new AlternativeSplineProjectFinal();
     String input; 
     String clamp; 
     Double endpt;
     ArrayList<String> inputArray = new ArrayList<String>();
     ArrayList<Double> clampDerivatives = new ArrayList<Double>();
     Scanner in = new Scanner(System.in); 
     System.out.println("Please enter your points in increasing order based on the x-coordinate.");
     System.out.println("Enter a point in the format: \"(x,y)\", such (3,-15). If you do not wish to enter any more points please enter \"none\"");
     input = in.nextLine();
     inputArray.add(input);
     while(input.equals("none")==false)
     {
       System.out.println("Enter a point in the format: \"(x,y)\", such (3,-15). If you do not wish to enter any more points please enter \"none\"");
       input = in.nextLine(); 
       inputArray.add(input);
     }
     if(input.equals("none")==true)
     {
       System.out.println("Would you like a clamped-boundary? Enter \"yes\" or \"no\" to select.");
       clamp = in.nextLine();
       if(clamp.equals("yes"))
       {
         System.out.println("Please enter the derivative at the first endpoint x0 in the format");
         endpt = Double.parseDouble(in.nextLine()); 
         clampDerivatives.add(endpt);
         System.out.println("Please enter the derivative at the second endpoint xn in the format");
         endpt = Double.parseDouble(in.nextLine()); 
         clampDerivatives.add(endpt);
         as.createArrayListsClamped(inputArray, clampDerivatives);
       }
       else
       {
         as.createArrayListsOpen(inputArray);
         as.tridiagonal(as.a, as.b, as.c, as.d);//calculate solution
         
         for(int i=0; i<as.d.size();i++)
         {
           System.out.println(as.d.get(i)); 
         }
       }
     }
     as.findOtherValues();
     

     //find point
     System.out.println("Enter the x-coordinate of a point between the endpoints to be evaluated. If you want to stop, type \"none\".");
     as.pointinput = in.nextLine();
     while(as.pointinput.equals("none")==false)
     {
       as.evaluatePoint(as.pointinput);
       System.out.println("Enter the x-coordinate of a point between the endpoints to be evaluated. If you want to stop, type \"none\".");
       as.pointinput = in.nextLine();
     }
     
     } 
   }
