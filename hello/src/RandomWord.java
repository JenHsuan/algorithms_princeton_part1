import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
  public static void main(String[] args) throws Exception {
    double cnt = 1;
    String champion = StdIn.readString();
    while (!StdIn.isEmpty()) {
      String candidate = StdIn.readString();
      cnt++;
      if (StdRandom.bernoulli(1/cnt)) {
        //swap
        String tmp = candidate;
        candidate = champion;
        champion = tmp;
      }
    }
    StdOut.println(champion);
  }
}
