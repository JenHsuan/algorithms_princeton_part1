import java.util.TreeSet;
import java.util.Iterator;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
  private TreeSet<Point2D> set;

  public PointSET()
  {
    this.set = new TreeSet<Point2D>();
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
    this.set.add(p);
  }

  public boolean contains(Point2D p)
  {
    if (p == null)
    {
      throw new IllegalArgumentException("The parameter can't be null");
    }
    return this.set.contains(p);
  } 

  public void draw()
  {
    Iterator<Point2D> iter = set.iterator();
    while (iter.hasNext())
    {
      Point2D pt = iter.next();
      pt.draw();
    }
  }

  public Iterable<Point2D> range(RectHV rect)
  {
    if (rect == null)
    {
      throw new IllegalArgumentException("The parameter can't be null");
    }
    PointSET self = this;
    return new Iterable<Point2D>() 
    {
      public Iterator<Point2D> iterator()
      {
        return new PointIterator(self.set, rect);
      }
    };
  }

  public Point2D nearest(Point2D p)
  {
    if (p == null)
    {
      throw new IllegalArgumentException("The parameter can't be null");
    }
    if (this.set.size() == 0) {
      return null;
    }

    Point2D nearestNeighbor = this.set.first();
    double minDistance = Double.POSITIVE_INFINITY;

    for (Point2D pt: this.set)
    {
      if (p.distanceSquaredTo(pt) < minDistance)
      {
        minDistance = p.distanceSquaredTo(pt);
        nearestNeighbor = pt;
      }
    }

    return nearestNeighbor;
  }

  private class PointIterator implements Iterator<Point2D>
  {
    private class Node 
    {
      Point2D item;
      Node next;
    }

    private Node current = null;
    public PointIterator(TreeSet<Point2D> set, RectHV rect)
    {
      Iterator<Point2D> iter = set.iterator();
      while (iter.hasNext())
      {
        Point2D pt = iter.next();
        if (rect.contains(pt))
        {
          Node newNode = new Node();
          newNode.item = pt;
          newNode.next = current;
          current = newNode;
        } 
      }
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

  public static void main(String[] args)
  {
  }
}
