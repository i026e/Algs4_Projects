import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private static final int DEFAULT_ARR_LEN = 4;
    private static final int RESIZE_COEFF = 2;
    private static final int SHRINK_AFTER_COEF = 4;
    
    private Item[] array;
    private int head;
    private int tail;
    private int length;
    
   /*
    * construct an empty randomized queue
    */
    public RandomizedQueue()        
    {
        this.length = DEFAULT_ARR_LEN;
        this.head = 0;
        this.tail = 0;
        this.array = (Item[]) new Object[DEFAULT_ARR_LEN];
    }
   
    /*
     * is the queue empty?
     */
    public boolean isEmpty()
    {
        return (head <= tail);
    }
   
    /*
     * return the number of items on the queue
     */
    public int size()
    {
        return (head - tail);
    }
   
    /*
     * add the item
     */
    public void enqueue(Item item)
    {
        if (item == null) { throw new java.lang.NullPointerException(); }
        array[head++] = item;
        
        if (head >= length) { resize((size()+1)*RESIZE_COEFF); }   
                                    //1 to ensure that new size > 0
    }
   
    /*
     * delete and return a random item
     */
    public Item dequeue()
    {
        if (isEmpty()) { throw new java.util.NoSuchElementException(); }
        int randPosition = StdRandom.uniform(tail, head);
        Item item = array[randPosition];
        
        array[randPosition] = array[tail]; 
        array[tail++] = null;
        if (size()*SHRINK_AFTER_COEF < length) { resize((length)/RESIZE_COEFF); } 
        
        return item;
    }
   
    /*
     * return (but do not delete) a random item
     */
    public Item sample()
    {
        if (isEmpty()) { throw new java.util.NoSuchElementException(); }
        int randPosition = StdRandom.uniform(tail, head);
        Item item = array[randPosition];
        
        return item;
        
    }
       
    /*
     * resize the underlying array holding the elements
     */
    private void resize(int newSize)
    {
        Item[] newArray = (Item[]) new Object[newSize];
        for (int i = tail; i < head; i++)
        {
            newArray[i-tail] = this.array[i];
        }
        this.head -= tail;
        this.tail = 0;
        this.length = newSize;
        this.array = newArray;
        //StdOut.println("resize");
    }
  
    
    /*
     * return an independent iterator over items in random order
     */
    public Iterator<Item> iterator() 
    {
        return new ArrayIterator();
    }
    
    private class ArrayIterator implements Iterator<Item>
    {
        private int[] indecies;
        private int index;
        
        public ArrayIterator()
        {
            indecies = new int[size()];
            for (int i = tail; i < head; i++)
            {
                indecies[i-tail] = i;
            }  
            StdRandom.shuffle(indecies);
            index = 0;
                
        }
        public boolean hasNext()
        { return (index < indecies.length); }
        
        public void remove()
        { throw new java.lang.UnsupportedOperationException(); }
        
        public Item next()
        {
            if (!hasNext()) { throw new java.util.NoSuchElementException(); }
            
            Item item = array[indecies[index++]];

            return item;
        }
    }
    
    

}