public class Test {
    public static void main(String[] args) {  
        
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        Board twin = initial.twin();
        Iterable<Board> neigb = initial.neighbors();

        StdOut.println("hamming: "+initial.hamming());       
        StdOut.println("manhattan: "+initial.manhattan());
        StdOut.println("Init: "+initial);
        
        
        
        
        for (Board b : neigb)
        {
            StdOut.println("hamming: "+b.hamming());       
            StdOut.println("manhattan: "+b.manhattan());
            StdOut.println(b);
            
        }
        StdOut.println("Twin: "+twin);
    }

}