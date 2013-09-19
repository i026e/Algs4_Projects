
public class Board {
    private static final int EMPTY_SYMBOL = 0;
    private static final int MIN_SIZE = 2;
    private static final int MAX_SIZE = 128;
    private int N;
    private int[][] grid;
    private int emptyRow, emptyCol;
    
    /*
     * construct a board from an N-by-N array of blocks
     * (where blocks[i][j] = block in row i, column j)
     */
    public Board(int[][] blocks)          
    {
        
        checkBoard(blocks);  
        this.N = blocks.length;
        this.grid = new int[N][N];
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                grid[i][j] = blocks[i][j];                
                if (EMPTY_SYMBOL == blocks[i][j]) 
                {
                    emptyRow = i;
                    emptyCol = j;
                }
                
            }
        }
    }
    
    /*
     * construct a board from an N*N array of char blocks
     * unsafe
     */
    private Board(int[][] blocks, int emptyRow, int emptyCol)          
    {
        checkBoardAndEmpty(blocks, emptyRow, emptyCol);
        this.grid = blocks;
        this.N = blocks.length;
        this.emptyRow = emptyRow;
        this.emptyCol = emptyCol;
    }
    
    /*
     * @return board dimension N
     */
    public int dimension()                 // 
    {
        return this.N;
    }
    
    /*
     * Correct block that should be at position i, j
     */
    private int correctElement(int row, int column)
    {
        if (row == N-1 && column == N-1)
            return EMPTY_SYMBOL;
        else return N*row + column + 1;
    }
    
    /*
     * number of blocks out of place
     */
    public int hamming()                   // 
    {
        int count = 0;
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if (grid[i][j] != correctElement(i, j) 
                        && grid[i][j] != EMPTY_SYMBOL)
                {
                    count++;
                }
            }
        }
        return count;
    }
    /*
     * sum of Manhattan distances between blocks and goal
     */
    public int manhattan()                 // 
    {
        int count = 0;
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                count += getManhattanDist(i, j);
            }
        }
        return count;
    }
    
    private int getManhattanDist(int row, int column)
    {
        int element = grid[row][column];
        if (element == EMPTY_SYMBOL || element == correctElement(row, column)) 
            return 0;

        int corrColumn = (element-1) % N;
        int corrRow = (element-1) / N;
        
        return Math.abs(corrRow - row) + Math.abs(corrColumn - column);
    }
    
    /*
     * is this board the goal board?
     */    
    public boolean isGoal()                // 
    {
        //if last element is not empty
        if (emptyRow != N-1 && emptyCol != N-1) return false; 
        else return (hamming() == 0);
    }
    
    /*
     * @param i, j indecies to swap
     * @return copy of board with swaped i and j elements
     */
    private int[][] getSwapedGrid(int rowA, int colA, int rowB, int colB)
    {
        int[][] copy = new int[N][N];
        for (int i = 0; i < N; i++)       
            for (int j = 0; j < N; j++)            
                copy[i][j] = grid[i][j];
            
        copy[rowA][colA] = grid[rowB][colB];
        copy[rowB][colB] = grid[rowA][colA];
        return copy;
        
    }
    /*
     * a board obtained by exchanging two adjacent blocks in the same row
     */
    public Board twin()                    // 
    {
        int row = 0;
        while (row == emptyRow) row++;
        if (row >= N) 
        {
            throw new java.util.NoSuchElementException();
        }

        return new Board(getSwapedGrid(row, 0, row, 1), emptyRow, emptyCol);
    }
    
    /*
     * does this board equal y?
     */
    public boolean equals(Object y)        // 
    {
        if (y == null) return false;        
        if (y.getClass() != this.getClass()) return false;
        if (y == this) return true;
        
        Board that = (Board) y;
        
        if (that.grid.length != N || that.grid[0].length != N)
            return false;
        
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (this.grid[i][j] != that.grid[i][j])
                    return false;
           
        return true;
    }
    
    /*
     * all neighboring boards
     */
    public Iterable<Board> neighbors()     // 
    {
        Queue<Board> q = new Queue<Board>();
        findNeighbors(q);
        return q;
    }
    
    /*
     * shift empty block
     */
    private void findNeighbors(Queue<Board> q)
    {
        //up
        if (emptyRow > 0)
        {
            int[][] newGrid = getSwapedGrid(emptyRow, emptyCol, 
                                            emptyRow-1, emptyCol);
            q.enqueue(new Board(newGrid, emptyRow-1, emptyCol));
        }
        //down
        if (emptyRow < N-1)
        {
            int[][] newGrid = getSwapedGrid(emptyRow, emptyCol,
                                            emptyRow+1, emptyCol);
            q.enqueue(new Board(newGrid, emptyRow+1, emptyCol));
        }
        
        //left
        if (emptyCol > 0)
        {
            int[][] newGrid = getSwapedGrid(emptyRow, emptyCol,
                                            emptyRow, emptyCol-1);
            q.enqueue(new Board(newGrid, emptyRow, emptyCol-1));
        }
        //right
        if (emptyCol < N-1)
        {
            int[][] newGrid = getSwapedGrid(emptyRow, emptyCol,
                                            emptyRow, emptyCol+1);
            q.enqueue(new Board(newGrid, emptyRow, emptyCol+1));
        }
            
    }
    
    /*
     * string representation of the board (in the output format specified below)
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", grid[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    /*
     * Throw exception if something wrong with board
     * @param board
     */
    private void checkBoard(int[][] blocks)
    {
        if (blocks == null)
        {
            throw new java.util.NoSuchElementException("No board");
        }
        if (blocks.length < MIN_SIZE || blocks.length > MAX_SIZE 
                ||  blocks.length != blocks[0].length)
        {
            throw new java.util.NoSuchElementException();
        }
        
        int emptyCount = 0;
        int L = blocks.length;
        for (int i = 0; i < L; i++)
            for (int j = 0; j < L; j++)
            {
                int element = blocks[i][j];
                if (element == EMPTY_SYMBOL) emptyCount++;
                else if (element < 1 || element >= L*L)                
                    throw new java.util.NoSuchElementException();
            }
        
        if (emptyCount != 1)       
            throw new java.util.NoSuchElementException();
        
    }
    
    /*
     * Light version of check
     * 
     */
    private void checkBoardAndEmpty(int[][] blocks, int emptyR, int emptyC)
    {
        if (blocks == null)
        {
            throw new java.util.NoSuchElementException("No board");
        }
        if (blocks.length < MIN_SIZE || blocks.length > MAX_SIZE 
                ||  blocks.length != blocks[0].length)
        {
            throw new java.util.NoSuchElementException();
        }
        if (blocks[emptyR][emptyC] != EMPTY_SYMBOL)
        {
            throw new java.util.NoSuchElementException();
        }
    }
}