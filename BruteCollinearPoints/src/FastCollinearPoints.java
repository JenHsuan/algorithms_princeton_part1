import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
  private class Node 
  {
    LineSegment value;
    Node next;
  }

  private final Point[] points;
  private Node first, last;
  private int size = 0;

  public FastCollinearPoints(Point[] points) {
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

    for (int p = 0; p < num; p++) {
      Point[] clonedPoints = new Point[num - p - 1];
      for (int i = p; i < num - 1; i++) {
        clonedPoints[i - p] = this.points[i + 1];
      }

      Arrays.sort(clonedPoints, this.points[p].slopeOrder());
      
      int cnt = 0;
      Point min = null;
      Point max = null;
      for (int j = 0; j < clonedPoints.length - 1; j++) {
        if (this.points[p].slopeTo(clonedPoints[j]) == this.points[p].slopeTo(clonedPoints[j + 1])) {
          
          if (min == null) {
            if (this.points[p].compareTo(clonedPoints[j]) > 0) {
              min = clonedPoints[j];
              max = this.points[p];
            } else {
              min = this.points[p];
              max = clonedPoints[j];
            }
          }

          if (min.compareTo(clonedPoints[j + 1]) > 0) {
            min = clonedPoints[j + 1];
          }

          if (max.compareTo(clonedPoints[j + 1]) < 0) {
            max = clonedPoints[j + 1];
          }

          cnt++;
          if (j == (clonedPoints.length - 2)) {
            if ((cnt > 1) && min.compareTo(this.points[p]) == 0) {
              this.addLine(min, max);
            }
            cnt = 0;
            min = null;
            max = null;
          }
        } else {
          if ((cnt > 1) && min.compareTo(this.points[p]) == 0) {
            this.addLine(min, max);
          }
          cnt = 0;
          min = null;
          max = null;
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
      this.last.next = null;
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

    FastCollinearPoints collinear = new FastCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
        StdOut.println(segment);
        segment.draw();
    }
    StdDraw.show();
  }
}
