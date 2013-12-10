public class SAP 
{
// constructor takes a digraph (not necessarily a DAG)
    private Digraph digraph;
    
    public SAP(Digraph G)
    {
        this.digraph = new Digraph(G);
                
    }
        
// length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w)
    {
        if (!isValidVertex(v) || !isValidVertex(w))
            throw new java.lang.IndexOutOfBoundsException();
        int[] result = findAncestorAndDist(v, w);
        return result[1];
    }
    
    //private String vertexRepresent(int
    
    private boolean isValidVertex(int v)
    {
        return (v >= 0 && v < this.digraph.V());
    }
// a common ancestor of v and w that participates in a shortest ancestral path;
//-1 if no such path
    public int ancestor(int v, int w)
    {
        if (!isValidVertex(v) || !isValidVertex(w))
            throw new java.lang.IndexOutOfBoundsException();
        
        int[] result = findAncestorAndDist(v, w);
        
        return result[0];
    }
    
    private int[] findAncestorAndDist(Iterable<Integer> v, Iterable<Integer> w)
    {
        BreadthFirstDirectedPaths bpdsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bpdsW = new BreadthFirstDirectedPaths(digraph, w);
        
        int root = -1;
        int distance = Integer.MAX_VALUE;
        
        for (int i = 0; i < this.digraph.V(); i++)
        {
            if (bpdsV.hasPathTo(i) && bpdsW.hasPathTo(i) && bpdsV.distTo(i) 
                    + bpdsW.distTo(i) < distance)
            {
                root = i;
                distance = bpdsV.distTo(i) + bpdsW.distTo(i);
                
            }
        }
        if (distance == Integer.MAX_VALUE)
            distance = -1;
        int[] result = {root, distance};
        return result;
    }
    private int[] findAncestorAndDist(int v, int w)
    {
        BreadthFirstDirectedPaths bpdsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bpdsW = new BreadthFirstDirectedPaths(digraph, w);
        
        int root = -1;
        int distance = Integer.MAX_VALUE;
        
        for (int i = 0; i < this.digraph.V(); i++)
        {
            
            if (bpdsV.hasPathTo(i) && bpdsW.hasPathTo(i) && bpdsV.distTo(i) 
                    + bpdsW.distTo(i) < distance)
            {
                root = i;
                distance = bpdsV.distTo(i) + bpdsW.distTo(i);
                
            }
        }
        if (distance == Integer.MAX_VALUE)
            distance = -1;
        int[] result = {root, distance};
        return result;
    }
    
// length of shortest ancestral path between any vertex in v and any vertex in w;
//-1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w)
    {
        int[] result = findAncestorAndDist(v, w);
        return result[1];
    }
        
// a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
    {
        for (int i :v)
        {
            if (!isValidVertex(i))
                throw new java.lang.IndexOutOfBoundsException();
        }
        for (int i :w)
        {
            if (!isValidVertex(i))
                throw new java.lang.IndexOutOfBoundsException();
        }    
        
        int[] result = findAncestorAndDist(v, w);
        
        return result[0];
    }
        
// for unit testing of this class (such as the one below)
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();            
            int w = StdIn.readInt();
            
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
    
}