import java.util.Iterator;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
public class KdTree {
  private TwoDimensionTree set;

  public KdTree()
  {
    this.set = new TwoDimensionTree();
  } 

  public boolean isEmpty()
  {
    return this.set.isEmpty();
  } 

  public int size()
  {
    return this.set.size();
  } 

  public void insert(Point2D p) 
  {
    if (p == null)
    {
      throw new IllegalArgumentException("The parameter can't be null");
    }
    this.set.put(p);
  }

  public boolean contains(Point2D p)
  {
    if (p == null)
    {
      throw new IllegalArgumentException("The parameter can't be null");
    }
    return this.set.get(p);
  } 

  public void draw()
  {
    this.set.inOrderDraw();
  }

  public Iterable<Point2D> range(RectHV rect)
  {
    if (rect == null)
    {
      throw new IllegalArgumentException("The parameter can't be null");
    }
    return this.set.range(rect);
  }

  public Point2D nearest(Point2D p)
  {
    if (p == null)
    {
      throw new IllegalArgumentException("The parameter can't be null");
    }
    return this.set.nearest(p);
  }
  
  private class Node 
  {
    Point2D item;
    Node next;
  }

  private class TwoDimensionTree {
    private class TreeNode 
    {
      private int count;
      private double[] points = new double[2];
      private TreeNode left, right;

      public TreeNode(double[] points, int count)
      {
        for (int i = 0; i < 2; i++)
        {
          this.points[i] = points[i];
        }
        this.count = count;
      }
    }

    private TreeNode root;
    private Node current;

    public boolean get(Point2D point) 
    {
      return this.get(this.root, point, 0);
    }

    public void put(Point2D point)
    {
      this.root = this.put(this.root, point, 0);
    }

    public Iterable<Point2D> range(RectHV rect)
    {
      this.current = null;
      this.range(this.root, rect, 0);
      return new Iterable<Point2D>() 
      {
        public Iterator<Point2D> iterator()
        {
          return new PointIterator(current);
        }
      };
    }

    public Point2D nearest(Point2D point)
    {
      if (point == null)
      {
        throw new IllegalArgumentException("The parameter can't be null");
      }

      if (this.root == null) {
        return null;
      }

      return this.nearest(this.root, point, new Point2D(this.root.points[0], this.root.points[1]), 0);
    }

    public boolean isEmpty()
    {
      return this.root == null;
    }

    public int size()
    {  
      return size(this.root);  
    }

    public void inOrderDraw()
    {
      this.inOrderDraw(this.root);
    }

    private void inOrderDraw(TreeNode x)
    {
      if (x != null)
      {
        this.inOrderDraw(x.left);
        Point2D currentPoint = new Point2D(x.points[0], x.points[1]);
        currentPoint.draw();
        this.inOrderDraw(x.right);
      }
    }

    private boolean get(TreeNode x, Point2D point, int depth) 
    {
      if (x != null)
      {
        if (point.x() == x.points[0] && point.y() == x.points[1])
        {
          return true;
        }

        double[] points = { point.x(), point.y() };
        int currentDimension = depth % 2;
        int cmp = points[currentDimension] < x.points[currentDimension] ? -1 : 1;
        if (cmp < 0) {
          return this.get(x.left, point, depth + 1);
        } else if (cmp > 0) {
          return this.get(x.right, point, depth + 1);
        }
      }

      return false;
    }

    private TreeNode put(TreeNode x, Point2D point, int depth)
    {
      double[] points = { point.x(), point.y() };
      if (x == null)
      {
        return new TreeNode(points, 1);
      }
      
      if (this.get(point))
      {
        return x;
      }

      int currentDimension = depth % 2;
      int cmp = points[currentDimension] < x.points[currentDimension] ? -1 : 1;

      if (cmp < 0) {
        x.left = this.put(x.left, point, depth + 1);
      } else {
        x.right = this.put(x.right, point, depth + 1);
      } 

      x.count = 1 + size(x.left) + size(x.right);

      return x;
    }

    private void range(TreeNode node, RectHV rect, int depth)
    {
      if (node != null)
      {
        Point2D point = new Point2D(node.points[0], node.points[1]);
        if (rect.contains(point)) 
        {
          Node tmp = new Node();
          tmp.item = point;
          tmp.next = current;
          this.current = tmp;
        }

        this.range(node.left, rect, depth + 1);
        this.range(node.right, rect, depth + 1);
      }
    }

    private class PointIterator implements Iterator<Point2D>
    {
      private Node current = null;
      public PointIterator(Node node)
      {
        this.current = node;
        
      }

      public boolean hasNext() { 
        return current != null; 
      }

      public void remove() 
      { 
        throw new UnsupportedOperationException("Unsupported");
      }
  
      public Point2D next() 
      {
        if (this.current == null || this.current.item == null)
        {
          throw new java.util.NoSuchElementException("no such element");
        }
  
        Point2D item = current.item;
        current = current.next;
        return item;
      }
    }

    private Point2D nearest(TreeNode x, Point2D point, Point2D nearestPoint, int depth)
    {
      if (x != null)
      {
        double[] points = { x.points[0], x.points[1] };
        int currentDimension = depth % 2;
        int cmp = points[currentDimension] < x.points[currentDimension] ? -1 : 1;
        
        double lastDis = this.distance(nearestPoint.x(), nearestPoint.y(), point.x(), point.y());
        double dis = this.distance(x.points[0], x.points[1], point.x(), point.y());
        
        if (dis < lastDis) 
        {
          nearestPoint = new Point2D(points[0], points[1]);
        }

        Point2D tmp = this.nearest(x.left, point, nearestPoint, depth + 1);
        nearestPoint = tmp != null ? tmp : nearestPoint;

        if (cmp > 0) {
          tmp = this.nearest(x.right, point, nearestPoint, depth + 1);
          nearestPoint = tmp != null ? tmp : nearestPoint;
        }

        return nearestPoint;
      }
      return null;
    }

    private double distance(double x1, double y1, double x2, double y2)
    {
      return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    private int size(TreeNode x)
    {
      if (x == null)
      {
        return 0;
      }
      return x.count;
    }
  }
  public static void main(String[] args)
  {
     // initialize the data structures from file
     String filename = args[0];
     In in = new In(filename);
     PointSET brute = new PointSET();
     KdTree kdtree = new KdTree();
     while (!in.isEmpty()) {
         double x = in.readDouble();
         double y = in.readDouble();
         Point2D p = new Point2D(x, y);
         kdtree.insert(p);
         brute.insert(p);
     }

     double x0 = 0.0, y0 = 0.0;      // initial endpoint of rectangle
     double x1 = 0.0, y1 = 0.0;      // current location of mouse
     boolean isDragging = false;     // is the user dragging a rectangle

     // draw the points
     StdDraw.clear();
     StdDraw.setPenColor(StdDraw.BLACK);
     StdDraw.setPenRadius(0.01);
     brute.draw();
     StdDraw.show();

     // process range search queries
     StdDraw.enableDoubleBuffering();
     while (true) {

         // user starts to drag a rectangle
         if (StdDraw.isMousePressed() && !isDragging) {
             x0 = x1 = StdDraw.mouseX();
             y0 = y1 = StdDraw.mouseY();
             isDragging = true;
         }

         // user is dragging a rectangle
         else if (StdDraw.isMousePressed() && isDragging) {
             x1 = StdDraw.mouseX();
             y1 = StdDraw.mouseY();
         }

         // user stops dragging rectangle
         else if (!StdDraw.isMousePressed() && isDragging) {
             isDragging = false;
         }

         // draw the points
         StdDraw.clear();
         StdDraw.setPenColor(StdDraw.BLACK);
         StdDraw.setPenRadius(0.01);
         brute.draw();

         // draw the rectangle
         RectHV rect = new RectHV(Math.min(x0, x1), Math.min(y0, y1),
                                  Math.max(x0, x1), Math.max(y0, y1));
         StdDraw.setPenColor(StdDraw.BLACK);
         StdDraw.setPenRadius();
         rect.draw();

         // draw the range search results for brute-force data structure in red
         StdDraw.setPenRadius(0.03);
         StdDraw.setPenColor(StdDraw.RED);
         for (Point2D p : brute.range(rect))
             p.draw();

         // draw the range search results for kd-tree in blue
         StdDraw.setPenRadius(0.02);
         StdDraw.setPenColor(StdDraw.BLUE);
         for (Point2D p : kdtree.range(rect))
             p.draw();

         StdDraw.show();
         StdDraw.pause(20);
     }
  }
}
