import java.util.Hashtable;
import java.util.Collections;
import java.util.ArrayList;

public class WordNet 
{
    //private Digraph digraph;
    private Hashtable<String, Bag<Integer>> words;
    private ArrayList<String> synsets;
    private SAP sap;
    //private int numSynsets
    
    public WordNet(String synsets, String hypernyms)
    {
        this.words = new Hashtable<String, Bag<Integer>>();
        this.synsets = new ArrayList<String>();
        
        int numSynsets = parseSynsets(synsets);        
        Digraph digraph = parseHypernyms(hypernyms, numSynsets);
        
        this.sap = new SAP(digraph); 
        
    }
     
    private Digraph parseHypernyms(String hypernyms, int numSynsets)
    {
        Digraph digraph = new Digraph(numSynsets);
        In hyper = new In(hypernyms);
        while (!hyper.isEmpty())
        {
            String[] s = hyper.readLine().split(",");
            //if (s.length < 2) 
                //throw new java.lang.IllegalArgumentException();
            int from = Integer.parseInt(s[0]);
            for (int i = 1; i < s.length; i++)
            {
                int to = Integer.parseInt(s[i]);
                digraph.addEdge(from, to);
            }
        }
        hyper.close();
        
        // check if graph has a cycle
        DirectedCycle dc = new DirectedCycle(digraph);
        if (dc.hasCycle())
            throw new java.lang.IllegalArgumentException("Cycle detected");
        // check if one root
        int roots = 0;
        for (int v = 0; v < numSynsets; v++)
        {
            
            if (!digraph.adj(v).iterator().hasNext())
                roots++;
        }
        if (roots > 1)
            throw new java.lang.IllegalArgumentException("Multiple roots: "+roots);
        return digraph;
    }
    
    private int parseSynsets(String synsetsFN)
    {
        int count = 0;        
        
        In syns = new In(synsetsFN);
        
        while (!syns.isEmpty())
        {
            String[] s = syns.readLine().split(",");
            if (s.length < 2) 
                throw new java.lang.IllegalArgumentException();
            int id = Integer.parseInt(s[0]);
            
            this.synsets.add(id, s[1]);
            
            String[] wordsArray = s[1].split(" ");
            
            
            for (String w: wordsArray)
            {
                Bag bag;
                if (this.words.containsKey(w)) bag = this.words.get(w);
                else bag = new Bag<Integer>();
                bag.add(id);
                this.words.put(w, bag);
            }
            count++;
            
        }
        
        syns.close();
        return count;
    }
// the set of nouns (no duplicates), returned as an Iterable
    public Iterable<String> nouns()
    {
        return Collections.list(this.words.keys());
    }
    
// is the word a WordNet noun?
    public boolean isNoun(String word)
    {
        return this.words.containsKey(word);
    }
    
// distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB)
    {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new java.lang.IllegalArgumentException();
        Bag<Integer> v = this.words.get(nounA);
        Bag<Integer> w = this.words.get(nounB);
        return this.sap.length(v, w);
    }
    
// a synset (second field of synsets.txt) that is 
// the common ancestor of nounA and nounB
// in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB)
    {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new java.lang.IllegalArgumentException();
        Bag<Integer> v = this.words.get(nounA);
        Bag<Integer> w = this.words.get(nounB);
        int root = this.sap.ancestor(v, w);
        return this.synsets.get(root);
    }
    
// for unit testing of this class
    public static void main(String[] args)
    {
        String synsets = args[0];
        String hypernyms = args[1];
        WordNet d = new WordNet(synsets, hypernyms);
        while (!StdIn.isEmpty()) 
        {
            String v = StdIn.readString();            
            String w = StdIn.readString();
            String ancestor   = d.sap(v, w);
            int distance = d.distance(v, w);
            StdOut.printf("distance = %d, ancestor = %s\n", distance, ancestor);
        }
    }
    
}        