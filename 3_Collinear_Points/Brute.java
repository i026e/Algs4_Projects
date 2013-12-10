import java.util.Arrays;

public class Brute {
   public static void main(String[] args)
   {
       // input file
       In in = new In(args[0]); 
       // # of points
       int N = in.readInt();
       
       StdDraw.setXscale(0, 32768);
       StdDraw.setYscale(0, 32768);
       
       Point[] points = new Point[N];
       
       //read file
       for (int i = 0; i < N; i++)
       {
           if (in.hasNextLine())
           {
               int x = in.readInt();
               int y = in.readInt();
               Point p = new Point(x, y);
               points[i] = p;
               p.draw();
               
           }           
       }
       
       Arrays.sort(points);
       
       //iterate
       for (int p = 0; p < N; p++)
       {
           Point i = points[p];
           
           for (int q = p+1; q < N; q++)
           {
               double slopePQ = i.slopeTo(points[q]);
               
               for (int r = q+1; r < N; r++)
               {
                   double slopePR = i.slopeTo(points[r]);
                   if (slopePQ == slopePR)
                   {
                       for (int s = r+1; s < N; s++)
                       {
                           if (i.slopeTo(points[s]) == slopePQ)
                           {
                               Point[] chain = {i, points[q], points[r], 
                                   points[s]};
                               echoChain(chain);
                               
                           }
                       }
                   }
               }
           }
       }
   }
   
   private static void echoChain(Point[] points)
   {
       int len = points.length;
       
       for (int i = 0; i < len; i++)
       {
           if (i == 0) StdOut.print(points[i]);
           else StdOut.print(" -> "+points[i]);
       }
       StdOut.println();
       points[0].drawTo(points[len-1]);
   }
}