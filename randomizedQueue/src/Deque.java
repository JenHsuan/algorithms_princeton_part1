import java.util.Iterator;

public class Deque<Item> implements Iterable<Item>
{
  private Node first, last;
  private class Node 
  {
    Item item;
    Node next;
  }

  public Deque()
  {
  }

  public boolean isEmpty()
  {
    return first == null || last == null;
  }

  public int size()
  {
    int cnt = 0;
    Node cur = first;
    
    while (cur != null)
    {
      cnt++;
      cur = cur.next;
    }
    return cnt;
  }

  public void addFirst(Item item)
  {
    if (item == null)
    {            
      throw new IllegalArgumentException("The item cannot be null!");
    }

    Node oldfirst = first;
    first = new Node();
    first.item = item;
    first.next = oldfirst;
    
    if (isEmpty()) {
      last = first;
    }
  }

  public void addLast(Item item)
  {
    if (item == null)
    {            
      throw new IllegalArgumentException("The item cannot be null!");
    }

    Node oldlast = last;
    last = new Node();
    last.item = item;
    last.next = null;

    if (isEmpty()) {
      first = last;
    } else {
      oldlast.next = last;
    }
  }

  public Item removeFirst()
  {
    if (this.isEmpty())
    {            
      throw new java.util.NoSuchElementException("The deque is empty!");
    }

    Item item = first.item;
    first = first.next;
    return item;
  }

  public Item removeLast()
  {
    if (this.isEmpty())
    {            
      throw new java.util.NoSuchElementException("The deque is empty!");
    }

    Node cur = first;
    Node deletedNode = last;

    if (first == last) 
    {
      first = null;
      last = null;
      return deletedNode.item;
    }

    while (cur != null && cur.next != null && cur.next.next != null)
    {
      cur = cur.next;
    }

    if (cur == null)
    {            
      throw new java.util.NoSuchElementException("The deque is empty!");
    }

    last = cur;
    last.next = null;
    
    return deletedNode.item;
  }

  public Iterator<Item> iterator()
  {
    return new ListIterator();
  }

  private class ListIterator implements Iterator<Item>
  {
    private Node current = first;

    public boolean hasNext() { 
      return current != null; 
    }
    public void remove() 
    { 
      throw new UnsupportedOperationException("Unsupported");
    }

    public Item next() 
    {
      if (current == null)
      {            
        throw new java.util.NoSuchElementException("No more items!");
      }

      Item item = current.item;
      current = current.next;
      return item;
    }
  }

  public static void main(String[] args)
  {
    Deque<Integer> deque = new Deque<Integer>();
    System.out.println(String.format("is empty? %1$s", deque.isEmpty()));
    System.out.println(String.format("size? %1$s", deque.size()));
    System.out.println("Add three items");
    deque.addFirst(1);
    deque.addLast(2);
    deque.addLast(3);
    Iterator<Integer> iter = deque.iterator();
    int i = 1;
    System.out.println("List items:");
    while (iter.hasNext())
    {
      System.out.println(String.format("#%1$s: %2$s", i++, iter.next()));  
    }
    System.out.println(String.format("size? %1$s", deque.size()));


    System.out.println("Remove the first item");
    deque.removeFirst();
    Iterator<Integer> iter2 = deque.iterator();
    i = 1;
    System.out.println("List items:");
    while (iter2.hasNext())
    {
      System.out.println(String.format("#%1$s: %2$s", i++, iter2.next()));  
    }

    System.out.println("Remove the last item");
    deque.removeLast();
    Iterator<Integer> iter3 = deque.iterator();
    i = 1;
    System.out.println("List items:");
    while (iter3.hasNext())
    {
      System.out.println(String.format("#%1$s: %2$s", i++, iter3.next()));  
    }
    System.out.println(String.format("is empty? %1$s", deque.isEmpty()));
    System.out.println(String.format("size? %1$s", deque.size()));
  }
}
