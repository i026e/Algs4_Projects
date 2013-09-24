
public class PointSET {
    private Node first;
    private int size;
    /*
     * construct an empty set of points
     */
   public PointSET()
   {
       this.first = null;
       this.size = 0;
   }
   
    /*
     * is the set empty?
     */
   public boolean isEmpty()
   {
       return (this.first == null);
   }
   
    /*
     * number of points in the set
     */
   public int size()
   {       
       return this.size;       
   }
   
  
   
    /*
     * add the point p to the set (if it is not already in the set)
     */
   public void insert(Point2D p)
   {
       if (isEmpty()) 
       {
           first = new Node(p);
           this.size++;
       }
       else insert(p, first);
           
   }
   private void insert(Point2D p, Node n)
   {
       if (n == null) return;
       int cmp = p.compareTo(n.point);
       if (cmp == 0) return;
       else if (cmp < 0)
       {
           if (n.left != null) insert(p, n.left);
           else 
           {
               n.left = new Node(p);
               this.size++;
           }
       }
       else
       {
           if (n.right != null) insert(p, n.right);
           else 
           {
               n.right = new Node(p);
               this.size++;
           }
       }
   }
    /*
     * does the set contain the point p?
     */
   public boolean contains(Point2D p)
   {
       return contains(p, first);
   }
   
   private boolean contains(Point2D p, Node n)
   {
       if (n == null) return false;
       
       int cmp = p.compareTo(n.point);
       
       if (cmp == 0) return true;
       else if (cmp < 0) return contains(p, n.left);
       else return contains(p, n.right);
   }
    /*
     * draw all of the points to standard draw
     */
   public void draw()
   {
       draw(first);
   }
   private void draw(Node n)
   {
       if (n == null) return;
       draw(n.left);
       draw(n.right);
       n.point.draw();
   }
    /*
     * all points in the set that are inside the rectangle
     */
   public Iterable<Point2D> range(RectHV rect)
   {
       Queue<Point2D> q = new Queue<Point2D>();       
       recRange(rect, q, first);
       
       return q;
   }
   private void recRange(RectHV rect, Queue<Point2D> q, Node n)
   {
       if (n == null) return;
       if (rect.contains(n.point)) q.enqueue(n.point);
       recRange(rect, q, n.left);
       recRange(rect, q, n.right);
   }
    /*
     * a nearest neighbor in the set to p; null if set is empty
     */
   public Point2D nearest(Point2D p)
   {
       Point2D neighb = null;
       double minDist = Double.MAX_VALUE;
       
       
       Stack<Node> s = new Stack<Node>();
       s.push(first);
       while (!s.isEmpty())
       {
           Node n = s.pop();
           if (n != null)
           {
               s.push(n.left);
               s.push(n.right);
               if (p.distanceSquaredTo(n.point) < minDist)
               {
                   minDist = p.distanceSquaredTo(n.point);
                   neighb = n.point;
               }
           }  
       }
       return neighb;
   }
      
   
   private static class Node {
        private Point2D point;
        private Node left;
        private Node right;
        public Node(Point2D point)
        {
            this.point = point;
        }
    }
}