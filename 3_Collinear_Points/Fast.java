import java.util.Arrays;

public class Fast {
   private static final int MIN_CHAIN = 3;
   
   public static void main(String[] args)
   {
       // input file
       In in = new In(args[0]); 
       // # of points
       int N = in.readInt();
       
       StdDraw.setXscale(0, 32768);
       StdDraw.setYscale(0, 32768);
       
       Point[] points = new Point[N];
       Point[] aux = new Point[N];
       
       //read file
       for (int i = 0; i < N; i++)
       {
           if (in.hasNextLine())
           {
               int x = in.readInt();
               int y = in.readInt();
               Point p = new Point(x, y);
               points[i] = p;
               aux[i] = p;
               p.draw();
               
           }           
       }
       
       if (N < MIN_CHAIN) return;
       
       Arrays.sort(points);
              
       for (int i = 0; i < N; i++)
       {
           Point p = points[i];
           Arrays.sort(aux, p.SLOPE_ORDER);
           
           //zeroth element in aux shoud be point itself
           int start = 1;          
           
           do
           {
               int count = 1;
               double startSlope = p.slopeTo(aux[start]);
               int stop = start+1;
               while (stop < N && p.slopeTo(aux[stop]) == startSlope) 
               {
                   count++; 
                   stop++; 
               }
               
               if (count >= MIN_CHAIN)
               {
                   Arrays.sort(aux, start, stop);
                   if (p.compareTo(aux[start]) < 0)
                       echoChain(aux, start, stop);
               }
               start = stop;
               
               
           }
           while (start < N);
           
       }
       
   }
   
   private static void echoChain(Point[] points, int startInd, int endInd)
   {       
       StdOut.print(points[0]);       
       for (int i = startInd; i < endInd; i++)
          StdOut.print(" -> "+points[i]);

       StdOut.println();
       points[0].drawTo(points[endInd-1]);
   }
}