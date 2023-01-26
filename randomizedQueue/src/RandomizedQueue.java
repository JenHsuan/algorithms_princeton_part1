import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item>
{
  private Node first, last;
  private class Node 
  {
    Item item;
    Node next;
  }
  
  public RandomizedQueue()
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

  public void enqueue(Item item)
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

  public Item dequeue()
  {
    if (this.isEmpty())
    {            
      throw new java.util.NoSuchElementException("The queue is empty!");
    }

    int size = this.size();
    int index = StdRandom.uniformInt(size);
    int cnt = 0;
    Node cur = first;
    Node deletedNode = cur;

    if (index == 0) {
      return this.removeFirst();
    } else if (index == (size - 1)) {
      return this.removeLast();
    }

    while (cur != null && cur.next != null)
    {
      if ((cnt + 1) == index) {
        deletedNode = cur.next;
        cur.next = cur.next.next;
        break;
      }
      cnt++;
      cur = cur.next;
    }

    return deletedNode.item;
  }

  public Item sample() 
  {
    if (this.isEmpty())
    {            
      throw new java.util.NoSuchElementException("The queue is empty!");
    }

    int size = this.size();
    int index = StdRandom.uniformInt(size);
    int cnt = 0;
    Node cur = first;
    Node deletedNode = cur;
    
    if (index == 0) {
      return first.item;
    } else if (index == (size - 1)) {
      return last.item;
    }

    while (cur != null && cur.next != null)
    {
      if ((cnt + 1) == index) {
        deletedNode = cur.next;
        break;
      }
      cnt++;
      cur = cur.next;
    }

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
  
  private Item removeFirst()
  {
    if (this.isEmpty())
    {            
      throw new java.util.NoSuchElementException("The deque is empty!");
    }

    Item item = first.item;
    first = first.next;
    return item;
  }

  private Item removeLast()
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


  public static void main(String[] args) {
    RandomizedQueue<Integer> deque = new RandomizedQueue<Integer>();
    System.out.println(String.format("is empty? %1$s", deque.isEmpty()));
    System.out.println(String.format("size? %1$s", deque.size()));
    System.out.println("Add three items");
    deque.enqueue(1);
    deque.enqueue(2);
    deque.enqueue(3);
    Iterator<Integer> iter = deque.iterator();
    int i = 1;
    System.out.println("List items:");
    while (iter.hasNext())
    {
      System.out.println(String.format("#%1$s: %2$s", i++, iter.next()));  
    }
    System.out.println(String.format("size? %1$s", deque.size()));


    System.out.println("Remove the random item");
    deque.dequeue();
    Iterator<Integer> iter2 = deque.iterator();
    i = 1;
    System.out.println("List items:");
    while (iter2.hasNext())
    {
      System.out.println(String.format("#%1$s: %2$s", i++, iter2.next()));  
    }

    System.out.println("Remove the random item");
    deque.dequeue();
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
