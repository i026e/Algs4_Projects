import java.lang.Math;
public class BoggleSolver
{
    private myTST dict;
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary)
    {
        this.dict = new myTST();
        for (String word : dictionary)
        {
            if (word.length() > 2)
                this.dict.put(word);
        }
    }

    
    private String getLetter(BoggleBoard board, int row, int col)
    {
        char ch = board.getLetter(row, col);
        if (ch == 'Q') return "QU";
        else return String.valueOf(ch);
    }
    private void DFS(BoggleBoard board, int row, int col, boolean[][] visited, String wordSoFar,Bag<String> wordsDiscovered)
    {
        // double check
        

        if (wordSoFar.length() > 2 && !this.dict.exists(wordSoFar))
            return;
        if (wordSoFar.length() > 2 && this.dict.contains(wordSoFar)) 
            wordsDiscovered.add(wordSoFar);
            
        
        
        //mark current cell as visited
        visited[row][col] = true;
        //StdOut.println(wordSoFar);       
        
        
        
        
            
        for (int i = Math.max(0,row-1); i <= row+1 && i < board.rows(); i++)
        {
            for (int j = Math.max(0,col-1); j <= col+1 && j < board.cols(); j++)
            {
                if (!visited[i][j])
                {
                     DFS(board, i, j, visited, wordSoFar + getLetter(board, i, j), wordsDiscovered);                    
                }
            }
        }
                
        //unmark current cell
        visited[row][col] = false;
    }
    
    
    // Returns all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
    {
        int rows = board.rows();
        int cols = board.cols();
        boolean[][] visited = new boolean[rows][cols];
//        for (int i = 0; i < rows; i++)
//        {
//            for (int j = 0; j < cols; j++)
//            {
//                visited[i][j] = false;
//            }
//        }
        //StdOut.println(this.dict.get(this.dict.get(this.dict.getRoot(),"AB",0),"AB",1)!=null);
        Bag<String> wordsDiscovered = new Bag<String>();
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                DFS(board, i, j, visited, getLetter(board, i, j), wordsDiscovered);
            }
        }
        TST<Boolean> uniqueWords = new TST<Boolean>();
        for (String word:wordsDiscovered)
            uniqueWords.put(word, true);
        return uniqueWords.keys();
    }

    
    // Returns the score of the given (not necessarily valid) word.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
    {
        int l = word.length();
        if (l <= 2 || !this.dict.contains(word)) return 0;
        else if (l <= 4) return 1;
        else if (l == 5) return 2;
        else if (l == 6) return 3;
        else if (l == 7) return 5;
        else return 11;
    }
    
    public static void main(String[] args)
    {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
    
    private class Node {
            private char c;                 // character
            private Node left, mid, right;  // left, middle, and right subtries
            private boolean val;              // value associated with string
        }
    
    
    private class myTST {
        private int N;       // size
        private Node root;   // root of TST
        
        
        
        // return number of key-value pairs
        public int size() {
            return N;
        }
        
        /**************************************************************
          * Is string key in the symbol table?
          **************************************************************/
        public boolean contains(String key) {
            return get(key) != false;
        }
        
        public boolean get(String key) {
            if (key == null) throw new NullPointerException();
            if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
            Node x = get(root, key, 0);
            if (x == null) return false;
            return x.val;
        }
        
        // return subtrie corresponding to given key
        public Node get(Node x, String key, int d) {
            if (key == null) throw new NullPointerException();
            if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
            if (x == null) return null;
            char c = key.charAt(d);
            if      (c < x.c)              return get(x.left,  key, d);
            else if (c > x.c)              return get(x.right, key, d);
            else if (d < key.length() - 1) return get(x.mid,   key, d+1);
            else                           return x;
        }
        
        
        /**************************************************************
          * Insert string s into the symbol table.
          **************************************************************/
        public void put(String s) {
            if (!contains(s)) N++;
            root = put(root, s, true, 0);
        }
        
        private Node put(Node x, String s, boolean val, int d) {
            char c = s.charAt(d);
            if (x == null) {
                x = new Node();
                x.c = c;
            }
            if      (c < x.c)             x.left  = put(x.left,  s, val, d);
            else if (c > x.c)             x.right = put(x.right, s, val, d);
            else if (d < s.length() - 1)  x.mid   = put(x.mid,   s, val, d+1);
            else                          x.val   = val;
            return x;
        }
                
         
        public boolean exists(String prefix) {           
            Node x = get(root, prefix, 0);
            
            return (x != null);// && (x.mid != null || x.right != null || x.left != null));
        }
        public Node getRoot()
        {
            return root;
        }
              
        
        
    }
    
    
    
    
}