public class Test
{
        public static void main(String[] args)
    {
        testRQ();
        StdOut.println();
        testDeque();
    }
    
    public static void testRQ()
    {
        RandomizedQueue<String> s = new RandomizedQueue<String>();
        s.enqueue("XXXX");
        //StdOut.println(s.removeLast());
        StdOut.println(s.sample());
        
        s.enqueue("AAAA");
        s.enqueue("BBBB");
        s.enqueue("CCCC");
        s.enqueue("DDDD");
        s.enqueue("EEEE");
        s.enqueue("FFFF");
        s.enqueue("GGGG");
        s.enqueue("HHHH");
        
        StdOut.println(s.size());
        
        for (String a : s)
        {
            StdOut.println(a);
        }
        //StdOut.println(s.dequeue());
    }
    
    public static void testDeque()
    {
        Deque<String> s = new Deque<String>();
        s.addFirst("AAAA");
        //StdOut.println(s.removeLast());
        StdOut.println(s.removeFirst());
        
        s.addLast("BBBB");
        s.addFirst("AAAA");
        s.addLast("CCCC");
        StdOut.println(s.size());
        
        for (String a : s)
        {
            StdOut.println(a);
        }
        //StdOut.println(s.removeLast());
    }
}