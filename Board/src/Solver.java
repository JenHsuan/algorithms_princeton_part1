import java.util.Arrays;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
  private Board[] path = null;
  private boolean solvedByMajorRoot = false;

  private class SearchNode implements Comparable<SearchNode> 
  {
    private Board board;
    private int priority = 0;
    private int move = 0;
    private SearchNode pre = null;

    public SearchNode(Board board)
    {
      this.board = board;
      this.move = 0;
      this.priority = board.manhattan() + move;
    }

    public SearchNode(Board board, SearchNode pre)
    {
      this.board = board;
      this.pre = pre;
      this.move = pre.move + 1;
      this.priority = board.manhattan() + move;
    }

    public int compareTo(SearchNode node)
    {
      return this.priority - node.priority == 0 ? (this.board.manhattan() - node.board.manhattan()) : (this.priority - node.priority);
    }
  }

  public Solver(Board initial)
  {
    if (initial == null) 
    {
      throw new IllegalArgumentException("Initial can't be null");
    }


    MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>();
    minPQ.insert(new SearchNode(initial));
    minPQ.insert(new SearchNode(initial.twin()));
    
    while (!minPQ.isEmpty())
    {
      SearchNode deletedNode = minPQ.delMin();

      if (deletedNode.board.isGoal())
      {
        SearchNode cur = deletedNode;
        this.path = new Board[cur.move + 1];
        for (int i = deletedNode.move; i >= 0; i--)
        {
          this.path[i] = cur.board;
          cur = cur.pre;
        }

        if (this.path[0].equals(initial))
        {
          this.solvedByMajorRoot = true;
        }
        break;
      }

      for (Board neighbor: deletedNode.board.neighbors())
      {
        if (deletedNode.move < 1 || !neighbor.equals(deletedNode.pre.board))
        {
          minPQ.insert(new SearchNode(neighbor, deletedNode));
        }
      }
    }
  }
  
  public boolean isSolvable()
  {
    return this.solvedByMajorRoot;
  }

  public int moves()
  {
    return this.isSolvable() ? this.path.length - 1 : -1;
  }

  public Iterable<Board> solution()
  {
    return this.isSolvable() ? Arrays.asList(this.path) : null;
  }

  public static void main(String[] args)
  {
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            tiles[i][j] = in.readInt();
    Board initial = new Board(tiles);

    Solver solver = new Solver(initial);

    if (!solver.isSolvable())
        StdOut.println("No solution possible");
    else {
        StdOut.println("Minimum number of moves = " + solver.moves());
        for (Board board : solver.solution())
            StdOut.println(board);
    }
  }
}
