public class Outcast 
{
    private WordNet wordnet;
// constructor takes a WordNet object
    public Outcast(WordNet wordnet)
    {
        this.wordnet = wordnet;
    }
        
// given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns)
    {        
        String s = nouns[0];
        int maxDist = 0;
        for (int i = 0; i < nouns.length; i++)
        {
            int dist = 0;
            for (int j = 0; j < nouns.length; j++)
            {
                dist += this.wordnet.distance(nouns[i], nouns[j]);
            }
            if (dist > maxDist)
            {
                maxDist = dist;
                s = nouns[i];
            }
        }
        return s;
    }
        
// for unit testing of this class (such as the one below)
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            String[] nouns = In.readStrings(args[t]);
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
    
}