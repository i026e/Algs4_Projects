public class KdTree {
    
    private Node root;
    private int size;
    /*
     * construct an empty set of points
     */
   public KdTree()
   {
       this.root = null;
       this.size = 0;
       
   }
    /*
     * is the set empty?
     */
   public boolean isEmpty()
   {
       return this.root == null;
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
           root = new Node(p, new RectCoord(), true);
           this.size++;
       }
       else recInsert(p, root, new RectCoord());
              
   }
   
   private void recInsert(Point2D p, Node parent, RectCoord rc)
   {
       if (parent == null) return; // something wrong!
       if (p.equals(parent.point)) return; // point already exists
       
       if (!parent.lessThanPoint(p)) // go left
       {
           if (parent.useX) rc.xMax = parent.point.x();
           else rc.yMax = parent.point.y();
           
           if (parent.lbTree != null) recInsert(p, parent.lbTree, rc);
           else 
           { 
               parent.lbTree = new Node(p, rc, !parent.useX);
               this.size++;
           }
       }
          
       else //go right
       {
           if (parent.useX) rc.xMin = parent.point.x();
           else rc.yMin = parent.point.y();
           
           if (parent.rtTree != null) recInsert(p, parent.rtTree, rc);
           else 
           {
               parent.rtTree = new Node(p, rc, !parent.useX);
               this.size++;
           }
       }
       
       
   }
   

    /*
     * does the set contain the point p?
     */
   public boolean contains(Point2D p)
   {
       return recContains(p, root);
   }
   private boolean recContains(Point2D p, Node n)
   {
       if (n == null) return false;
       else if (p.equals(n.point)) return true;
       
       else if (!n.lessThanPoint(p))
           return recContains(p, n.lbTree);
       else return recContains(p, n.rtTree);
   }

    /*
     * draw all of the points to standard draw
     */
   public void draw()
   { draw(root); }
   private void draw(Node n)
   {
       if (n == null) return;
       
       draw(n.lbTree);
       draw(n.rtTree);
       n.point.draw();
                
   }
    /*
     * all points in the set that are inside the rectangle
     */
   public Iterable<Point2D> range(RectHV rect)
   {
       Queue<Point2D> q = new Queue<Point2D>();
       recRange(q, rect, root);
       return q;
   }
   
   private void recRange(Queue<Point2D> q, RectHV rect, Node n)
   {
       if (n == null) return;
       
       
       if (rect.intersects(n.rect))
       {
           if (rect.contains(n.point)) q.enqueue(n.point);
           
           recRange(q, rect, n.lbTree);
           recRange(q, rect, n.rtTree);
       }
   }
    /*
     * a nearest neighbor in the set to p; null if set is empty
     */
   public Point2D nearest(Point2D p)
   {      
       double minDist = Double.MAX_VALUE;
       Point2D neighb = null;
       
       Stack<Node> s = new Stack<Node>();
       s.push(root);
       while (!s.isEmpty())
       {
           Node n = s.pop();
           if (n == null) continue;
           if (minDist < n.rect.distanceSquaredTo(p)) continue;
           
           if (p.distanceSquaredTo(n.point) < minDist)
           {
               neighb = n.point;
               minDist = p.distanceSquaredTo(n.point);
           }
           
           if (n.lbTree != null && n.lbTree.rect.contains(p))
           {
               s.push(n.rtTree);
               s.push(n.lbTree);
           }
           else
           {
               s.push(n.lbTree);
               s.push(n.rtTree);
           }
               
           
                       
       }
       return neighb;
       
   }
   

   
    /*
     * 
     */
   private static class Node {
       // the point
       private Point2D point; 
       // the axis-aligned rectangle corresponding to this node
       private RectHV rect;    
       // the left/bottom, right/top subtrees 
       private Node lbTree, rtTree; 
       // use x coordinate to compare points
       private boolean useX;
       
       
       public Node(Point2D point, RectCoord rc, boolean useXtoCompare)
       {
           this.point = point;
           this.rect = new RectHV(rc.xMin, rc.yMin, rc.xMax, rc.yMax);
           this.useX = useXtoCompare;
               
           this.lbTree = null;
           this.rtTree = null;
           
           
       }
       // compare point of current node with other point 
       public boolean lessThanPoint(Point2D anotherPoint)
       {
           if (useX)           
               return this.point.x() < anotherPoint.x();
           else return this.point.y() < anotherPoint.y();               
               
       }
   
   }
   private static class RectCoord {
       private static final double X_MAX = 1.0;
       private static final double Y_MAX = 1.0;
       
       private double xMin, yMin, xMax, yMax;
       
       public RectCoord()
       {
           xMin = 0;
           yMin = 0;
           xMax = X_MAX;
           yMax = Y_MAX;
       }
           
   }
}