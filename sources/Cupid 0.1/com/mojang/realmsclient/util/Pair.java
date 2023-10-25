package com.mojang.realmsclient.util;

public class Pair<A, B> {
  private final A first;
  
  private final B second;
  
  protected Pair(A first, B second) {
    this.first = first;
    this.second = second;
  }
  
  public static <A, B> Pair<A, B> of(A a, B b) {
    return new Pair<A, B>(a, b);
  }
  
  public A first() {
    return this.first;
  }
  
  public B second() {
    return this.second;
  }
  
  public String mkString(String separator) {
    return String.format("%s%s%s", new Object[] { this.first, separator, this.second });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\realmsclien\\util\Pair.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */