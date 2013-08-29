//import algs4.WeightedQuickUnionUF;

public class Percolation {
    // convention
    private static final int SHIFT = 1; 
    
    // instance variables
    private boolean[] grid;   
    
    private WeightedQuickUnionUF uf;
    // to avoid backwash
    private WeightedQuickUnionUF dirtyHack;
    //For testing
    //private QuickFindUF uf;
    
    private final int N;    
    // virtual sites
    private final int TOP;
    private final int BOTTOM;
    
    
       
    
   
    
    /**
     * create N-by-N grid, with all sites blocked
     */
    public Percolation(int N)              // 
    {
        this.N = N;
        this.TOP = 0;
        this.BOTTOM = N*N + 1;
            
        this.grid = new boolean[N*N+2];

        this.uf = new WeightedQuickUnionUF(N*N + 2); // +2 virtual sides
        this.dirtyHack = new WeightedQuickUnionUF(N*N + 1);
        
        //For testing
        //this.uf = new QuickFindUF(N*N + 2);
        
        
    }
    
    /**
     * 
     */
    private void exceptionOnBounds(int i, int j) 
    {
      if (i < SHIFT || i > this.N)
      { 
          throw new  java.lang.IndexOutOfBoundsException(
                                "row index i out of bounds"); 
      }
      if (j < SHIFT || j > this.N)
      { 
          throw new  java.lang.IndexOutOfBoundsException(
                                "column index j out of bounds"); 
      }
    }
    
    
    
    /**
     * Convert conventional indexes of row and colum  from 1 .. N 
     * to 0 .. (N-1) n and transform to index in array
     * @return index
     */ 
    private int getIndexOfCite(int i, int j)
    {
        //return SHIFT + (i-SHIFT)*N + (j-SHIFT);
        return (i-SHIFT)*N + j;
    }
    

    
    /**
     * @param i index of first site
     * @param j index of second site
     */ 
    private void addConnection(int indI, int indJ)
    {
        this.uf.union(indI, indJ);
        if (indI < BOTTOM && indJ < BOTTOM)
        {
            this.dirtyHack.union(indI, indJ);
        }
    }
    
    /**
     * open site (row i, column j) if it is not already
     */    
    public void open(int i, int j)
    {        
        if (!isOpen(i, j))
        {
                        
            int self = getIndexOfCite(i, j);
            
            grid[self] = true;
            
            //top             
            if (i == SHIFT) 
            { addConnection(this.TOP, self); } //virtual connection
            else if (isOpen(i-1, j)) 
            { 
                addConnection(getIndexOfCite(i-1, j), self); 
            }
            
            //bottom 
            if (i == this.N) 
            { addConnection(this.BOTTOM, self); } //virtual connection
            else if (isOpen(i+1, j)) 
            { 
                addConnection(getIndexOfCite(i+1, j), self); 
            }
            
            //left
            if (j > SHIFT && isOpen(i, j-1)) 
            { 
                addConnection(getIndexOfCite(i, j-1), self); 
            }
            
            //right
            if (j < this.N && isOpen(i, j+1)) 
            { 
                addConnection(getIndexOfCite(i, j+1), self); 
            } 
            
            
        }
        
    }
    
    /**
     * is site (row i, column j) open?
     * @param indexes from 1..N
     */
    public boolean isOpen(int i, int j)    
    {        
        exceptionOnBounds(i, j);
        return grid[getIndexOfCite(i, j)];
    }
    
       
    /**
     * is site (row i, column j) full?
     */
    public boolean isFull(int i, int j)   
    {
        exceptionOnBounds(i, j);        
        return this.dirtyHack.connected(this.TOP, getIndexOfCite(i, j));
    }
    
    /**
     * does the system percolate?
     */
    public boolean percolates()    
    {
        return this.uf.connected(this.TOP, this.BOTTOM);
    }
}