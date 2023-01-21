import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class PercolationStats {
  // test client (see below)
  public static void main(String[] args) {
    PercolationStats percolationStats = new PercolationStats(50, 50);
    System.out.println(String.format("mean: %1$s.", percolationStats.mean()));
    System.out.println(String.format("stddev: %1$s.", percolationStats.stddev()));
    System.out.println(String.format("confidenceHi: %1$s.", percolationStats.confidenceHi()));
    System.out.println(String.format("confidenceLo: %1$s.", percolationStats.confidenceLo()));
  }

  private double trialResults[];
  private int trials = 0;
  private double avg = 0;

  // perform independent trials on an n-by-n grid
  public PercolationStats(int n, int trials) {
    this.trials = trials;
    this.trialResults = new double[this.trials];
    for (int i = 0; i < this.trials; i++) {
      this.trialResults[i] = this.getTrialResult(n);
      this.avg += this.trialResults[i];
    }
  }

  private double getTrialResult(int n) {
    int length = n + 1;
    Percolation percolation = new Percolation(length);
    while (!percolation.percolates()) {
      int index = StdRandom.uniformInt(1, length * length + 1);
      int row = index / length + 1;
      int col = index - (row - 1) * length + 1;  
      percolation.open(row, col);
    }
    double number = percolation.numberOfOpenSites();
    return number/((length - 1) * (length - 1));
  }
  
  // sample mean of percolation threshold
  public double mean() {
    return this.avg / this.trials;
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    double res = 0;
    for (int i = 0; i < this.trials; i++) {
      res += Math.pow(this.trialResults[i] - this.mean(), 2);
    }
    return res / (this.trials - 1);
  }

  // low endpoint of 95% confidence interval
  public double confidenceLo() {
    return this.mean() - 1.96 * Math.sqrt(this.stddev()) / Math.sqrt(this.trials);
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    return this.mean() + 1.96 * Math.sqrt(this.stddev()) / Math.sqrt(this.trials);
  }

 
}
