import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
  private static final double CONFIDENCE_95 = 1.96;
  private double[] trialResults;
  private int trials = 0;

  public PercolationStats(int n, int trials) {
    if (n < 0 || trials < 0) {
      throw new java.lang.IllegalArgumentException("out of boundary");
    }

    this.trials = trials;
    this.trialResults = new double[this.trials];
    for (int i = 0; i < this.trials; i++) {
      this.trialResults[i] = this.getTrialResult(n);
    }
  }

  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("Please give a number"); 
      return;
    }

    int n = Integer.parseInt(args[0]); 
    int trials = Integer.parseInt(args[1]);

    if (n < 0 || trials < 0) {
      throw new java.lang.IllegalArgumentException("out of boundary");
    }

    PercolationStats percolationStats = new PercolationStats(n, trials);
    System.out.println(String.format("mean = %1$s", percolationStats.mean()));
    System.out.println(String.format("stddev = %1$s", percolationStats.stddev()));
    System.out.println(String.format("95%% confidence interval = [%1$s, %2$s]", percolationStats.confidenceLo(), percolationStats.confidenceHi()));
  }

  private double getTrialResult(int n) {
    if (n < 0) {
      throw new java.lang.IllegalArgumentException("out of boundary");
    }

    int length = n;
    Percolation percolation = new Percolation(length);
    while (!percolation.percolates()) {
      int index = StdRandom.uniformInt(1, length * length);
      int row = index / length +  1;
      int col = index - (row - 1) * length; 
      if (index % length == 0) {
        row -= 1;
        col = length;
      }

      percolation.open(row, col);
    }
    double number = percolation.numberOfOpenSites();
    return number/(length * length);
  }
  
  public double mean() {
    return StdStats.mean(this.trialResults);
  }

  public double stddev() {
    return Math.sqrt(StdStats.stddev(this.trialResults));
  }

  public double confidenceLo() {
    return this.mean() - CONFIDENCE_95 * this.stddev() / Math.sqrt(this.trials);
  }

  public double confidenceHi() {
    return this.mean() + CONFIDENCE_95 * this.stddev() / Math.sqrt(this.trials);
  }
}
