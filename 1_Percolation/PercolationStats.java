public class PercolationStats {
    private static final int START_IND = 1;
    
    private int N;
    private int T;
    private double[] results; 
    
    
    
    /**
     * perform T independent computational experiments on an N-by-N grid
     */
   public PercolationStats(int N, int T) 
   {
       if (N <= 0 || T <= 0)
       {
           throw new java.lang.IllegalArgumentException("negative or zero number");
       }
       this.N = N;
       this.T = T;
       this.results = new double[T];
       this.performMultipleExperiments();      
       
   }
   
   
   /**
     * 
     */ 
   private void performMultipleExperiments()
   {
       double gridArea = (double) this.N*this.N;
       for (int i = 0; i < this.T; i++)
       {
           results[i] = performSingleExp() / gridArea;
       }
   }
   
   /**
     * @return number of opened sites for which system starts percolate
   */
   private int performSingleExp()
   {
      
       Percolation perc = new Percolation(this.N);
       
       int count = 0;
       
       // repeat until starts percolate
       while (!perc.percolates())
       {
           int i;
           int j;
           //try to find closed site 
           do 
           {
               i = StdRandom.uniform(this.N) + START_IND;
               j = StdRandom.uniform(this.N) + START_IND;
               //StdOut.println(i + " " + j);
           }
           while (perc.isOpen(i, j));
           
           perc.open(i, j);
           count++;          
               
       }
       return count;
   }
   
   /**
     * sample mean of percolation threshold
     */
   public double mean()                      
   {
       return StdStats.mean(this.results);
   }
   /**
     * sample standard deviation of percolation threshold
     */
   public double stddev()                   
   {
       return StdStats.stddev(this.results);
   }
   /**
     * returns lower bound of the 95% confidence interval
     */
   public double confidenceLo()             
   {
       return this.mean() - (1.96*this.stddev())/Math.sqrt(T);
   }
   
   /**
     * returns upper bound of the 95% confidence interval
     */
   public double confidenceHi()              
   {
       return this.mean() + (1.96*this.stddev())/Math.sqrt(T);
   }
   
   /**
     * test client
     */
   public static void main(String[] args)  
   {
       //  N-by-N grid
       int N = Integer.parseInt(args[0]);        
       // T independent computational experiments
       int T = Integer.parseInt(args[1]);     
       
       //Stopwatch sw = new Stopwatch();
       PercolationStats perStats = new PercolationStats(N, T);
       
       //double elapsedTime = sw.elapsedTime(); 
       //StdOut.println("Time to run simulation: " + elapsedTime);
                      
       StdOut.printf("%-24s= %f%n", "mean", perStats.mean());
       StdOut.printf("%-24s= %f%n", "stddev", perStats.stddev());
       StdOut.printf("%-24s= %f, %f%n", "95% confidence interval",
                     perStats.confidenceLo(), perStats.confidenceHi());
       
       //StdOut.println("Time to calculate statistics: " 
       // + (sw.elapsedTime()-elapsedTime));
       
       
   }
}