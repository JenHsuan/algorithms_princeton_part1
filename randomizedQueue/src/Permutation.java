import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

public class Permutation
{
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Please give a number"); 
      return;
    }
    double cnt = 1;
    int num = Integer.parseInt(args[0]);
    Deque<String> deque = new Deque<String>();

    while (!StdIn.isEmpty()) {
      String candidate = StdIn.readString();
      cnt++;
      if (StdRandom.bernoulli(1/cnt)) {
        deque.addFirst(candidate);
      } else {
        deque.addLast(candidate);
      }
    }

    cnt = 0;
    Iterator<String> iter = deque.iterator();
    while (iter.hasNext() && cnt < num) {
      cnt++;
      System.out.println(iter.next());
    }

  }
}
