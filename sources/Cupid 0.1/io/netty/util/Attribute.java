package io.netty.util;

public interface Attribute<T> {
  AttributeKey<T> key();
  
  T get();
  
  void set(T paramT);
  
  T getAndSet(T paramT);
  
  T setIfAbsent(T paramT);
  
  T getAndRemove();
  
  boolean compareAndSet(T paramT1, T paramT2);
  
  void remove();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\Attribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */