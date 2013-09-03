public class Subset {
   
    public static void main(String[] args)
    {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        
        while (!StdIn.isEmpty())
        {
            queue.enqueue(StdIn.readString());
        }
          
        for (int i = 0; i < k; i++)
        {
            if (!queue.isEmpty())
            {
                StdOut.println(queue.dequeue());
            }
        }
    }
}