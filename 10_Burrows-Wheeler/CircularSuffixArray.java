public class CircularSuffixArray {
    private int[] indecies;    
    private String str;
    private int length;
    
    public CircularSuffixArray(String s)  // circular suffix array of s
    {
        str = s;
        length = s.length();
        indecies = new int[length];
        
        Suffix[] suffixes = new Suffix[length];
        for (int i = 0; i < length; i++)
        {
            suffixes[i] = new Suffix(i);
        }
        java.util.Arrays.sort(suffixes);
                
        
        for (int i = 0; i < length; i++)
        {
            indecies[i] = suffixes[i].startAt;            
        }
    }
    
    private class Suffix implements Comparable
    {
        private int startAt;
        public Suffix(int start)
        {
            this.startAt = start;
        }
        public char charAt(int position)
        {
            return str.charAt((position + startAt) % length);
        }
        public int compareTo(Object another) 
        {
            Suffix that = (Suffix) another;
            for (int i = 0; i < length; i++)
            {
                char a = this.charAt(i);
                char b = that.charAt(i);
                if (a > b) 
                    return 1;
                else if (a < b) 
                    return -1;
            }
            return 0;
            
        }
        
    }

    
    public int length()                   // length of s
    { return length; }
    public int index(int i)               // returns index of ith sorted suffix
    { return indecies[i]; }
}