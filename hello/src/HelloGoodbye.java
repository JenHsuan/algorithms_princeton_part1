public class HelloGoodbye {
  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("Please give two names"); 
      return;
    }

    System.out.println(String.format("Hello %1$s and %2$s.", args[0], args[1])); 
    System.out.println(String.format("Goodbye %2$s and %1$s.", args[0], args[1])); 
  }
}
