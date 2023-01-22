import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdIn;
public class Percolation {
  private Status[] sites;
  private int length = 0;
  private WeightedQuickUnionUF weightedQuickUnionUF;
  private int virtualTopSiteId = 0;
  private int virtualBottomSiteId = 0;

  private enum Status
  {
      OPEN, FULL, BLOCKED;
  }

  public Percolation(int n) {
    if (n <= 0) {
      throw new java.lang.IllegalArgumentException("n should be larger than zero");
    } 

    this.length = n;
    this.virtualTopSiteId = this.length * this.length;
    this.virtualBottomSiteId = this.length * this.length + 1;
    this.sites = new Status[this.length * this.length + 2];

    for (int i = 0; i < this.length * this.length + 2; i++) {
      this.sites[i] = Status.BLOCKED;
    }   

    this.weightedQuickUnionUF = new WeightedQuickUnionUF(this.length * this.length + 2);
    this.sites[this.virtualBottomSiteId] = Status.FULL;
    this.sites[this.virtualTopSiteId] = Status.FULL;
  }

  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Please give a number"); 
        return;
    }

    int length = Integer.parseInt(args[0]) + 1;
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
    System.out.println(String.format("percentage: %1$s", number/(length * length)));   
  }

  public void open(int row, int col) {
    if (row < 1 || row > this.length + 1 || col < 1 || col > this.length + 1) {
      throw new java.lang.IllegalArgumentException("out of boundary");
    }

    if (this.isOpen(row, col)) {
      return;
    }

    int index = this.flattenIndex(row, col);
    this.sites[index] = Status.OPEN;

    if (row == 1) {
      this.weightedQuickUnionUF.union(this.virtualTopSiteId, index);
    } else if (row == this.length) {
      this.weightedQuickUnionUF.union(this.virtualBottomSiteId, index);
    }

    int bottom = this.flattenIndex(row + 1, col);
    int top = this.flattenIndex(row - 1, col);
    int right = this.flattenIndex(row, col + 1);
    int left = this.flattenIndex(row, col - 1);
    
    if (row - 1 > 0 && this.isOpen(row - 1, col)) {
      this.weightedQuickUnionUF.union(index, top);
    }

    if (col - 1 > 0 && this.isOpen(row, col - 1)) {
      this.weightedQuickUnionUF.union(index, left);
    }

    if (row + 1 <= this.length && this.isOpen(row + 1, col)) {
      this.weightedQuickUnionUF.union(index, bottom);
    }
    
    if (col + 1 <= this.length && this.isOpen(row, col + 1)) {
      this.weightedQuickUnionUF.union(index, right);
    }
    
  }

  public boolean isOpen(int row, int col) {
    if (row < 1 || row > this.length || col < 1 || col > this.length) {
      throw new java.lang.IllegalArgumentException("out of boundary");
    }
    
    return this.isSpecificState(row, col, Status.OPEN) || this.isSpecificState(row, col, Status.FULL);
  }

  public boolean isFull(int row, int col) {
    if (row < 1 || row > this.length || col < 1 || col > this.length) {
      throw new java.lang.IllegalArgumentException("out of boundary");
    }
    int index = this.flattenIndex(row, col);
    return this.sites[index] == Status.OPEN && this.weightedQuickUnionUF.find(index) == this.weightedQuickUnionUF.find(this.virtualTopSiteId);
  }

  public int numberOfOpenSites() {
    int cnt = 0;
    for (int i = 0; i < this.length * this.length; i++) {
      if (this.sites[i] == Status.OPEN || this.sites[i] == Status.FULL) {
        cnt++;
      }
    }   
    return cnt;
  }

  public boolean percolates() {
    return this.weightedQuickUnionUF.find(this.virtualTopSiteId) == this.weightedQuickUnionUF.find(this.virtualBottomSiteId);
  }

  private boolean isSpecificState(int row, int col, Status state) {
    return this.sites[this.flattenIndex(row, col)] == state;
  }

  private int flattenIndex(int row, int col) {
    return this.length * (row - 1) + (col - 1);
  }
}
