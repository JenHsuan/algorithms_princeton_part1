import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {
  private final int x;
  private final int y;
  
  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public void draw() {
    StdDraw.point(x, y);
  }

  public void drawTo(Point that) {
    StdDraw.line(this.x, this.y, that.x, that.y);
  }

  public double slopeTo(Point that) {
    if ((x == that.x) && (y == that.y)) {
      return Double.NEGATIVE_INFINITY;
    } else if (x == that.x) {
      return Double.POSITIVE_INFINITY;
    } else if (y == that.y) {
      return 0;
    } 

    return Double.valueOf(that.y - y)/Double.valueOf(that.x - x);
  }
  
  public int compareTo(Point that) {
    if (y < that.y) {
      return -1;
    } else if (y > that.y) {
      return 1;
    } else if (x < that.x) {
      return -1;
    } else if (x > that.x) {
      return 1;
    } else {
      return 0;
    }
  }

  public Comparator<Point> slopeOrder() {
    return new Slope(this);
  }

  private class Slope implements Comparator<Point> 
  {
    private Point p;

    public Slope(Point p) {
      this.p = p;
    }

    @Override
    public int compare(Point p1, Point p2) {
      double slope1 = p.slopeTo(p1);
      double slope2 = p.slopeTo(p2);
      
      if (slope1 > slope2) {
        return 1;
      } else if (slope1 < slope2) {
        return -1;
      }

      return 0;
    }

  }

  public String toString() {
    return "(" + x + ", " + y + ")";
  }

  public static void main(String[] args) {
  }
}
