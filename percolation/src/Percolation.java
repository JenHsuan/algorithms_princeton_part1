import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

  private enum Status
  {
      OPEN, FULL, BLOCKED;
  }

  //test client
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Please give a number"); 
        return;
    }

    int length = Integer.parseInt(args[0]) + 1;
    Percolation percolation = new Percolation(length);
    while (!percolation.percolates()) {
      int index = StdRandom.uniformInt(1, length * length + 1);
      int row = index / length + 1;
      int col = index - (row - 1) * length + 1; 
      percolation.open(row, col);
    }
    double number = percolation.numberOfOpenSites();
    System.out.println(String.format("percentage: %1$s", number/(length * length)));
  }

  private Status sites[];
  private int length = 0;
  private WeightedQuickUnionUF weightedQuickUnionUF;
  private int virtualTopSiteId = 0;
  private int virtualBottomSiteId = 0;

  // creates n-by-n grid, with all sites initially blocked
  public Percolation(int n) {
    this.length = n + 1;
    this.virtualTopSiteId = this.length * this.length;
    this.virtualBottomSiteId= this.length * this.length + 1;
    this.sites = new Status[this.length * this.length + 2];

    for (int i = 1; i < this.length * this.length; i++) {
      this.sites[i] = Status.BLOCKED;
    }   

    this.weightedQuickUnionUF = new WeightedQuickUnionUF(this.length * this.length + 2);
    this.sites[this.virtualTopSiteId] = Status.FULL;
    this.sites[this.virtualBottomSiteId] = Status.FULL;
  }

  // opens the site (row, col) if it is not open already
  public void open(int row, int col) {
    this.sites[this.length * row + col] = Status.OPEN;

    if (row == 1) {
      //Union for the first row
      this.weightedQuickUnionUF.union(row * this.length + col, this.virtualTopSiteId);
      this.sites[this.length * row + col] = Status.FULL;  
    } else if (row == this.length - 1) {
      //Union for the last row          
      this.weightedQuickUnionUF.union(row * this.length + col, this.virtualBottomSiteId);
      this.sites[this.length * row + col] = Status.FULL;  
    }

    if ((row + 1) * this.length + col < this.length * this.length && this.isFull(row + 1, col)) {
      this.weightedQuickUnionUF.union(row * this.length + col, (row + 1) * this.length + col);
      this.sites[this.length * row + col] = Status.FULL;  
    } else if ((row -  1) * this.length + col >= 1 && this.isFull(row - 1, col)) {
      this.weightedQuickUnionUF.union(row * this.length + col, (row - 1) * this.length + col);
      this.sites[this.length * row + col] = Status.FULL;  
    } else if (row * this.length + col +  1 < this.length * this.length && this.isFull(row, col + 1)) {
      this.weightedQuickUnionUF.union(row * this.length + col, row * this.length + col + 1);
      this.sites[this.length * row + col] = Status.FULL;  
    } else if (row * this.length + col -  1 >= 1 && this.isFull(row, col - 1)) {
      this.weightedQuickUnionUF.union(row * this.length + col, row * this.length + col - 1);
      this.sites[this.length * row + col] = Status.FULL;  
    }
  }

  // is the site (row, col) open?
  public boolean isOpen(int row, int col) {
    return this.isSpecificState(row, col, Status.OPEN);
  }

  // is the site (row, col) full?
  public boolean isFull(int row, int col) {
    return this.isSpecificState(row, col, Status.FULL);
  }

  // returns the number of open sites
  public int numberOfOpenSites() {
    int cnt = 0;
    for (int i = 1; i < this.length * this.length; i++) {
      if (this.sites[i] == Status.OPEN || this.sites[i] == Status.FULL) {
        cnt++;
      }
    }   
    return cnt;
  }

  // does the system percolate?
  public boolean percolates() {
    return this.weightedQuickUnionUF.find(this.virtualTopSiteId) == this.weightedQuickUnionUF.find(this.virtualBottomSiteId);
  }

  private boolean isSpecificState(int row, int col, Status state) {
    return this.sites[this.length * row + col] == state;
  }
}
