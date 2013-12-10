import java.awt.Color;

public class SeamCarver {
    private static final double BOARDER_NRG = 3*255*255;
    private static final double INFINITY = Double.MAX_VALUE; 
    private Picture picture;
    
    public SeamCarver(Picture picture)
    {        
        this.picture = new Picture(picture);
        //StdOut.printf("width:%d, height:%d\n", width() , height());
    }    
    // current picture 
    public Picture picture()                       
    {
        return this.picture;
    }
    // width of current picture
    public int width()                         
    {
        return this.picture.width();
    }
    // height of current picture
    public int height()                       
    {
        return this.picture.height();
    }    
    // energy of pixel at column x and row y
    public  double energy(int x, int y)
    {   
        //StdOut.printf("x:%d, y:%d", x, y);
        int w = width() -1, h = height()-1;
        //check boundaries
        if (x < 0 || x > w || y < 0 || y > h)
            throw new java.lang.IndexOutOfBoundsException("Energy");
        // if pixel on boarder:
        else if (x == 0 || x == w ||  y == 0 || y == h)
            return this.BOARDER_NRG;
        else return innerEnergy(x, y);
    }
    // energy of pixel at column x and row y not on boarder
    private double innerEnergy(int x, int y)
    {
        Color left = this.picture.get(x-1, y);
        Color right = this.picture.get(x+1, y);
        Color up = this.picture.get(x, y-1);
        Color down = this.picture.get(x, y+1);
        return gradient(left, right) + gradient(up, down);
    }
    private double gradient(Color either, Color other)
    {
        double red = either.getRed() - other.getRed();
        double green = either.getGreen() - other.getGreen();
        double blue = either.getBlue() - other.getBlue();
        return red*red + green*green + blue*blue;
    }
    
    private double[][] energiesArray()
    {
        double[][] enrgs = new double[height()][width()];
        // enrgs[row][column]
        for (int i = 0; i < height(); i++)
        {
            for (int j = 0; j < width(); j++)
            {
                enrgs[i][j] = energy(j, i);
            }
        }
        return enrgs;
    }
    
    // pass through an array and mark the shorthest distance from top to entry
    private static void vertTopologicalSort(double[][] energies)
    {
        //energies[row][column]
        int h = energies.length, w = energies[0].length;
        for (int row = 1; row < h; row++) //row
        {
            for (int col = 0; col < w; col++)
            {
                double upCenter = energies[row-1][col];
                double min = (col == 0) ?
                    upCenter : Math.min(upCenter, energies[row-1][col-1]);
                min = (col == (w -1)) ? min : Math.min(min, energies[row-1][col+1]);
                
                energies[row][col] += min;
            }
        }
            
    }
    private static double[][] transpose(double[][] energies)
    {
        int h = energies.length, w = energies[0].length;
        double[][] trEnergies = new double[w][h];
        for (int row = 0; row < h; row++)
        {
            for (int col = 0; col < w; col++)
            {
                trEnergies[col][row] = energies[row][col];
            }
        }
        return trEnergies;
    }
    
    private static int[] findMinVertPath(double[][] energies)
    {
        int h = energies.length, w = energies[0].length;
        int[] minPath = new int[h];        
        
        vertTopologicalSort(energies);
        
        // find the lowest element in last row        
        minPath[h-1] = 0;
        for (int i = 0; i < w; i++)
        {
            if (energies[h-1][i] < energies[h-1][minPath[h-1]])
                minPath[h-1] = i;
        }
        // trace path back to first row
        // assuming we need the cheapest upper neighboring entry
        for (int row = h - 2; row >= 0; row--)
        {
            int col = minPath[row+1];
            // three neighboring, priority to center
            minPath[row] = col;
            if (col > 0 && energies[row][col-1] < energies[row][minPath[row]])
                minPath[row] = col-1;            
            if (col < (w - 2) && energies[row][col+1] < energies[row][minPath[row]])
                minPath[row] = col+1;
        }
        //for (int i : minPath)
            //StdOut.println(i);
        return minPath;
    }
    // sequence of indices for horizontal seam
    public   int[] findHorizontalSeam()           
    {
        double[][] transNrgs = transpose(energiesArray());
        return findMinVertPath(transNrgs);
    }
    // sequence of indices for vertical seam
    public   int[] findVerticalSeam()           
    {
        double[][] nrgs = energiesArray();
        return findMinVertPath(nrgs);
    }
    // remove horizontal seam from picture
    public    void removeHorizontalSeam(int[] a)  
    {
        if (height() <= 1 || !isValidSeam(a, width(), height()-1))
            throw new java.lang.IllegalArgumentException();
        Picture pic = new Picture(width(), height()-1);
        for (int w = 0; w < width(); w++)
        {
            for (int h = 0; h < a[w]; h++)
                pic.set(w, h, this.picture.get(w, h));
            
            for (int h = a[w]+1; h < height(); h++)
                pic.set(w, h-1, this.picture.get(w, h));
            
        }
        this.picture = pic;
    }
    // remove vertical seam from picture
    public    void removeVerticalSeam(int[] a)    
    {
        if (width() <= 1 || !isValidSeam(a, height(), width()))
            throw new java.lang.IllegalArgumentException();
        Picture pic = new Picture(width()-1, height());
        
        for (int h = 0; h < height(); h++)
        {           
            for (int w = 0; w < a[h]; w++)
                pic.set(w, h, this.picture.get(w, h));  
            
            for (int w = a[h]+1; w < width(); w++)            
                pic.set(w-1, h, this.picture.get(w, h));  
            
        }
        this.picture = pic;
    }
    //return false if two consecutive entries differ by more than 1
    private boolean isValidSeam(int[] a, int len, int range)
    {
        if (a.length != len || a[0] < 0 || a[0] > range)
            return false;
        for (int i = 1; i < len; i++)
        {
            if (a[i] < Math.max(0, a[i-1] -1) || a[i] > Math.min(range, a[i-1] +1))
                return false;
        }
        return true;
    }
}