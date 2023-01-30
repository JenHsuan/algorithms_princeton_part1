import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
  private class Node 
  {
    LineSegment value;
    Node next;
  }

  private final Point[] points;
  private Node first, last;
  private int size = 0;

  public BruteCollinearPoints(Point[] points)
  {
    if (points == null) {
      throw new IllegalArgumentException("The arguement can't be null.");
    }

    this.points = points;
    Arrays.sort(this.points);

    int num = this.points.length;
    for (int i = 0; i < num - 1; i++) {
     if (this.points[i].compareTo(this.points[i + 1]) == 0) {
      throw new IllegalArgumentException("The point can't be repeated.");
     }
    }

    for (int p = 0; p < num - 3; p++) {
      for (int q = p + 1; q < num - 2; q++) {
        for (int r = q + 1; r < num - 1; r++) {
          for (int s = r + 1; s < num; s++) {
            double slope1 = this.points[p].slopeTo(this.points[q]);
            double slope2 = this.points[q].slopeTo(this.points[r]);
            double slope3 = this.points[r].slopeTo(this.points[s]);
            if (slope1 == slope2 && slope2 == slope3) {
              this.addLine(this.points[p], this.points[s]);
            }
          }
        }
      }
    }
    
  }

  public LineSegment[] segments()
  {
    LineSegment[] lineSegments = new LineSegment[this.size];
    Node cur = this.first;
    for (int i = 0; i < this.size; i++) {
      lineSegments[i] = cur.value;
      cur = cur.next;
    }
    return lineSegments;
  }

  public int numberOfSegments()
  {
    return this.size;
  }

  private void addLine(Point p, Point q) {
    if (this.first == null) {
      this.first = new Node();
      this.first.value = new LineSegment(p, q);
      this.first.next = null;
      this.last = this.first;
      this.last.next = null;
    } else {
      Node newNode = new Node();
      newNode.value = new LineSegment(p, q);
      this.last.next = newNode;
      this.last = this.last.next;
    }
    this.size++;
  }

  public static void main(String[] args) {
    In in = new In(args[0]);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
      int x = in.readInt();
      int y = in.readInt();
      points[i] = new Point(x, y);
    }    

    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
        p.draw();
    }
    StdDraw.show();

    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
        StdOut.println(segment);
        segment.draw();
    }
    StdDraw.show();
  }
}
