public class Solver {

    private State fin;
    
    
    private class State implements Comparable<State>
    {
        private Board board;
        private State prev;
        private int cost;
        private int steps;
        
        public State(Board board, State prev)
        {
            this.board = board;
            this.prev = prev;            
            if (prev == null) this.steps = 0;
            else this.steps = prev.steps + 1;
            this.cost = board.manhattan() + this.steps;
        }
        
        public int compareTo(State that)
        {
            if (this.cost == that.cost) return 0;
            else if (this.cost < that.cost) return -1;
            else return 1;
        }
        
        public boolean isFinal()
        {
            return this.board.isGoal();
        }
        
    }
    
    /*
     * find a solution to the initial board (using the A* algorithm)
     */
    public Solver(Board initial)            // 
    {
        this.fin = findSolution(initial);
    }
    
    private State findSolution(Board initial)
    {
        MinPQ<State> pqInit = new MinPQ<State>();
        MinPQ<State> pqTwin = new MinPQ<State>();
        
        //this.pqInit.insert();
        //this.pqTwin.insert();
        
        State init = new State(initial, null);
        State twin = new State(initial.twin(), null);
        
        while (!init.isFinal() && !twin.isFinal())
        {
                       
            Iterable<Board> initNeighbs = init.board.neighbors();
            Iterable<Board> twinNeighbs = twin.board.neighbors();
            
            for (Board b: initNeighbs)
            {
                if (init.prev == null || !b.equals(init.prev.board))
                {
                    pqInit.insert(new State(b, init));
                }
            }
            for (Board b: twinNeighbs)
            {
                if (twin.prev == null || !b.equals(twin.prev.board))
                {
                    pqTwin.insert(new State(b, twin));
                }
            }
            
            init = pqInit.delMin();
            twin = pqTwin.delMin();
        }
        if (init.isFinal()) return init;
        else return null;
        
    }
    /*
     * is the initial board solvable?
     */
    public boolean isSolvable()             // 
    {
        return (this.fin != null);
    }
    /*
     * min number of moves to solve initial board; -1 if no solution
     */
    public int moves()                      // 
    {
        if (!isSolvable()) return -1;
        else return fin.steps;
    }
    /*
     * sequence of boards in a shortest solution; null if no solution
     */
    public Iterable<Board> solution()       // 
    {
        if (!isSolvable()) return null;
        
        Queue<Board> q = new Queue<Board>();
        restorePath(q, this.fin);
        return q;
    }
    
    /*
     * recursively restore path to solve puzzle
     */
    private void restorePath(Queue<Board> q, State s)
    {
        if (s == null) return;
        
        // recursive call for previous state
        restorePath(q, s.prev); 
        
        q.enqueue(s.board);
    }
    
    /*
     * solve a slider puzzle
     */
    public static void main(String[] args) {  
        
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }


}
