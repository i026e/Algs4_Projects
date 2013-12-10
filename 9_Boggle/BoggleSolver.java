public class BoggleSolver
{
    private static final int SHIFT = -(int) 'A';
    private static final int R = 26; 
    
    private MyTrieST dict;
    // Initializes the data structure using the given 
    //array of strings as the dictionary.
    // (You can assume each word in the dictionary 
    // contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary)
    {
        this.dict = new MyTrieST();
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
    private void DFS(BoggleBoard board, int row, int col, boolean[][] visited, 
                     String wordSoFar, Bag<String> wordsDiscovered, Node n)
    {
        // double check
        //Node n = this.dict.getNode(wordSoFar);
        if (n == null) return;
        if (n.val)
                wordsDiscovered.add(wordSoFar);
        
        //mark current cell as visited
        visited[row][col] = true;
        //StdOut.println(wordSoFar); 
            
        for (int i = Math.max(0, row-1); i <= row+1 && i < board.rows(); i++)
        {
            for (int j = Math.max(0, col-1); j <= col+1 && j < board.cols(); j++)
            {
                if (!visited[i][j])
                {
                    char c = board.getLetter(i, j);                    
                    Node x = n.next[c+SHIFT];
                    if (x != null)
                    {
                        
                        if (c == 'Q')
                        {
                            DFS(board, i, j, visited, wordSoFar + "QU", 
                                wordsDiscovered, x.next['U' + SHIFT]);
                            
                        }
                        else
                            DFS(board, i, j, visited, wordSoFar + c, 
                                wordsDiscovered, x);
                    }
                                         
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

        
        Bag<String> wordsDiscovered = new Bag<String>();
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                DFS(board, i, j, visited, getLetter(board, i, j), 
                    wordsDiscovered, this.dict.getNode(getLetter(board, i, j)));
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
            //StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
    
    
    
    /*************************************************************************
 *  Compilation:  javac TrieST.java
 *  Execution:    java TrieST < words.txt
 *  Dependencies: StdIn.java
 *
 *  A string symbol table for ASCII strings, implemented using a 256-way trie.
 *
 *  % java TrieST < shellsST.txt 
 *  by 4
 *  sea 6
 *  sells 1
 *  she 0
 *  shells 3
 *  shore 7
 *  the 5
 *
 *************************************************************************/
    private static class Node {
        
        private boolean val;
        private Node[] next = new Node[R];
    }
    
    
    
    private class MyTrieST {        
        private Node root = new Node();
        
        
        
        /****************************************************
          * Is the key in the symbol table?
          ****************************************************/
        public boolean contains(String key) {
            return get(key);
        }
        
        public boolean get(String key) {
            Node x = get(root, key, 0);
            if (x == null) return false;
            return x.val;
        }
        
        public Node getNode(String key) {
            return get(root, key, 0);
        }
        private Node get(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            char c = key.charAt(d);
            return get(x.next[c+SHIFT], key, d+1);
        }
        
        /****************************************************
          * Insert key-value pair into the symbol table.
          ****************************************************/
        public void put(String key) {
            root = put(root, key, true, 0);
        }
        
        
        private Node put(Node x, String key, boolean val, int d) {
            if (x == null) x = new Node();
            if (d == key.length()) {
                x.val = val;
                return x;
            }
            char c = key.charAt(d);
            x.next[c+SHIFT] = put(x.next[c+SHIFT], key, val, d+1);
            return x;
        }
       
        
    }

}