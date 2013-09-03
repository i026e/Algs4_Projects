import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> { //pronounced "deck"
    private int N;
    private Node first;
    private Node last;
    
   /*
    * construct an empty deque
    */
    public Deque() 
    {
        this.N = 0;
        this.first = null;
        this.last = null;
    }
 
    /*
    * is the deque empty?
    */
    public boolean isEmpty()
    {
        return (this.N == 0);
    }
    
   /*
    * return the number of items on the deque
    */
    public int size() 
    {
        return this.N;
    }
   
    /*
    * insert the item at the front
    */
    public void addFirst(Item item) 
    {
        throwExceptionIfNull(item);
        if (N == 0) { addInEmptyList(item); }
        else
        {
            Node node = new Node(item, null, first);
            first.prev = node;
            first = node;
            N++;
        }
    }
   
    /*
    * insert the item at the end
    */
    public void addLast(Item item) 
    {
        throwExceptionIfNull(item);
        if (N == 0) { addInEmptyList(item); }
        else
        {
            Node node = new Node(item, last, null);
            last.next = node;
            last = node;
            N++;
        }
       
    }
  
    /*
    * dangerous, will destroy existing list
    */
    private void addInEmptyList(Item item)
    {
        Node node = new Node(item, null, null);
        last = node;
        first = node;
        N = 1;
    }
      
    /*
    * delete and return the item at the front
    */
    public Item removeFirst()
    {
        throwExceptionIfEmpty();
        
        Node node = first;
        
        N--;
        if (N == 0) removeAllNodes(); //no more nodes
        else
        {
            first = node.next;
            first.prev = null;
        }
        
        
        return node.item;
    }
    
   /*
    * delete and return the item at the end
    */
    public Item removeLast() 
    {
        throwExceptionIfEmpty();
        Node node = last;
                
        N--;
        if (N == 0) removeAllNodes(); //no more nodes
        else
        {
            last = node.prev;
            last.next = null;
        }
       
        
        return node.item;
    }
    
    private void removeAllNodes()
    {
        last = null;
        first = null;
    }
    
    
   /*
    * return an iterator over items in order from front to end
    */
    public Iterator<Item> iterator() 
    {
        return new ListIterator();
    }
    
    /*
    * 
    */
    private void throwExceptionIfNull(Item item)
    {
        if (item == null) throw new java.lang.NullPointerException();
    }
    
    /*
    * 
    */
    private void throwExceptionIfEmpty()
    {
        if (this.isEmpty()) throw new java.util.NoSuchElementException();
    }
    
    /*
    * 
    */
    private class Node
    {
        private Item item;
        private Node next;
        private Node prev;
        
        public Node(Item item, Node prev, Node next)
        {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }
    
    private class ListIterator implements Iterator<Item>
    {
        private Node current;
        
        public ListIterator()
        {
            current = first;
        }
        public boolean hasNext()
        { return (current != null); }
        
        public void remove()
        { throw new java.lang.UnsupportedOperationException(); }
        
        public Item next()
        {
            if (!hasNext())
            {
                throw new java.util.NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
  

}