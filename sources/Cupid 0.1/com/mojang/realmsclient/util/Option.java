package com.mojang.realmsclient.util;

public abstract class Option<A> {
  public abstract A get();
  
  public static <A> Some<A> some(A a) {
    return new Some<A>(a);
  }
  
  public static <A> None<A> none() {
    return new None<A>();
  }
  
  public static final class Some<A> extends Option<A> {
    private final A a;
    
    public Some(A a) {
      this.a = a;
    }
    
    public A get() {
      return this.a;
    }
    
    public static <A> Option<A> of(A value) {
      return new Some<A>(value);
    }
  }
  
  public static final class None<A> extends Option<A> {
    public A get() {
      throw new RuntimeException("None has no value");
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\realmsclien\\util\Option.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */