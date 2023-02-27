import java.util.Iterator;

public class Board {
  private int[][] currentTiles;
  private int[][] goal;

  public Board(int[][] tiles)
  {
    this.currentTiles = new int[tiles.length][tiles.length];
    for (int i = 0; i < tiles.length; i++) {
      this.currentTiles[i] = tiles[i].clone();
    }

    this.goal = new int[this.dimension()][this.dimension()];
    for (int i = 0; i < this.dimension(); i++)
    {
      for (int j = 0; j < this.dimension(); j++)
      {
        this.goal[i][j] = i * this.dimension() + j + 1;
      }
    }

    this.goal[this.dimension() - 1][this.dimension() - 1] = 0;
  }

  public String toString()
  {
    String result = Integer.toString(this.dimension());
    String rowString = " ";
    for (int [] row: this.currentTiles)
    {
      rowString = " ";
      for (int data: row)
      {
        rowString = rowString.concat("  ").concat(Integer.toString(data));
      }
      result = result.concat("\n").concat(rowString);
    }
    return result;
  }

  public int dimension()
  {
    return this.currentTiles.length;
  }

  public int hamming()
  {
    int cnt = 0;
    for (int i = 0; i < this.dimension(); i++)
    {
      for (int j = 0; j < this.dimension(); j++)
      {
        if (this.currentTiles[i][j] != 0 && this.currentTiles[i][j] != this.goal[i][j])
        {
          cnt++;
        }
      }
    }
    return cnt;
  }

  public int manhattan()
  {
    int cnt = 0;
    for (int [] row: this.currentTiles)
    {
      for (int data: row)
      {
        if (data != 0)
        {
          int[] currentIndex = getIndex(data, this.currentTiles);
          int[] goalIndex = getIndex(data, this.goal);
          cnt += (Math.abs(currentIndex[0] - goalIndex[0]) + Math.abs(currentIndex[1] - goalIndex[1]));  
        }
      }
    }
    return cnt;
  }

  public boolean isGoal()
  {
    for (int i = 0; i < this.dimension(); i++)
    {
      for (int j = 0; j < this.dimension(); j++)
      {
        if (this.currentTiles[i][j] != this.goal[i][j])
        {
          return false;
        } 
      }
    }
    return true;
  }

  public boolean equals(Object y)
  {
    if (y == null) 
    {
      return false;
    }

    Board board = ((Board) y);
    for (int i = 0; i < this.dimension(); i++)
    {
      for (int j = 0; j < this.dimension(); j++)
      {
        if (this.currentTiles[i][j] != board.currentTiles[i][j])
        {
          return false;
        } 
      }
    }

    return true;
  }

  public Iterable<Board> neighbors()
  {
    Board self = this;
    return new Iterable<Board>() 
    {
      public Iterator<Board> iterator()
      {
        return new BoardIterator(self);
      }
    };
  }

  public Board twin()
  {
    int row = 0, col = 0;
    if (this.currentTiles[col][row] == 0 || this.currentTiles[col + 1][row] == 0) {
      row++;
    }
    return new Board(swap(this.currentTiles, col, row, col, row + 1));
  }
  
  private static int[] getIndex(int val, int[][] tiles)
  {
      int size = tiles.length;
      int[] res = new int[2];
      for (int i = 0; i < size; i++)
      {
        for (int j = 0; j < size; j++)
        {
          if (tiles[i][j] == val)
          {
            res[0] = (i * size + j) / size;
            res[1] = (i * size + j) % size;
            return res;
          } 
        }
      }

      res[0] = 0;
      res[1] = 0;
      return res;
  }

  private static int[][] swap(int[][] tiles, int col, int row, int newCol, int newRow) 
  {
    int size = tiles.length;
    int [][] newTiles = new int[size][size];
    for (int i = 0; i < size; i++)
    {
      for (int j = 0; j < size; j++)
      {
        newTiles[i][j] = tiles[i][j]; 
      }
    }

    int tmp = newTiles[col][row];
    newTiles[col][row] = newTiles[newCol][newRow];
    newTiles[newCol][newRow] = tmp;

    return newTiles;
  }

  private class BoardIterator implements Iterator<Board>
  {

    private Node current = null;
    private Board board = null;

    public BoardIterator(Board board)
    {
      this.board = board;
      int size = this.board.dimension();
      Node tmp = new Node();
      Node newNode = tmp;
      int[] zeroIndex = this.zeroIndex();
      int col = zeroIndex[0];
      int row = zeroIndex[1];
      if ((col - 1) >= 0)
      {
        Node node = new Node();
        node.item = new Board(Board.swap(this.board.currentTiles, col, row, col - 1, row));
        tmp.next = node;
        tmp = tmp.next;
      }
      if ((col + 1) < size)
      {
        Node node = new Node();
        node.item = new Board(Board.swap(this.board.currentTiles, col, row, col + 1, row));
        tmp.next = node;
        tmp = tmp.next;
      }
      if ((row - 1) >= 0)
      {
        Node node = new Node();
        node.item = new Board(Board.swap(this.board.currentTiles, col, row, col, row - 1));
        tmp.next = node;
        tmp = tmp.next;
      }
      if ((row+ 1) < size)
      {
        Node node = new Node();
        node.item = new Board(Board.swap(this.board.currentTiles, col, row, col, row + 1));
        tmp.next = node;
        tmp = tmp.next;
      }

      this.current = newNode.next;
    }

    private class Node 
    {
      Board item;
      Node next;
    }

    private int[] zeroIndex()
    {
      return Board.getIndex(0, this.board.currentTiles);
    }

    public boolean hasNext() { 
      return current != null; 
    }
    public void remove() 
    { 
      throw new UnsupportedOperationException("Unsupported");
    }

    public Board next() 
    {
      if (this.current == null || this.current.item == null)
      {
        throw new java.util.NoSuchElementException("no such element");
      }

      Board item = current.item;
      current = current.next;
      return item;
    }
  }

  public static void main(String[] args) {
    int[][] tiles = new int[3][3];
    tiles[0][0] = 0;
    tiles[0][1] = 1;
    tiles[0][2] = 3;
    tiles[1][0] = 4;
    tiles[1][1] = 2;
    tiles[1][2] = 5;
    tiles[2][0] = 7;
    tiles[2][1] = 8;
    tiles[2][2] = 6;

    Board board = new Board(tiles);
    System.out.println(String.format("board: %s", board.toString()));    
    System.out.println(String.format("Demension: %d", board.dimension()));   
    System.out.println(String.format("is goal %b", board.isGoal()));
    System.out.println(String.format("Haming: %d", board.hamming()));
    System.out.println(String.format("Manhattan: %d", board.manhattan()));

    int[][] tiles2 = new int[4][4];
    for (int i = 0; i < tiles2.length; i++)
    {
      for (int j = 0; j < tiles2.length; j++)
      {
        tiles2[i][j] = i * tiles2.length + j;
      }   
    }

    Board board2 = new Board(tiles2);   
    System.out.println(String.format("is equal: %b", board.equals(board2)));
    System.out.println(String.format("twin: %s", board.twin()));

    int i = 0;
    Iterator<Board> iter = board.neighbors().iterator();
    while (iter.hasNext())
    {
      System.out.println(String.format("#%1$s: %2$s", i++, iter.next().toString()));  
    }
  }
}
