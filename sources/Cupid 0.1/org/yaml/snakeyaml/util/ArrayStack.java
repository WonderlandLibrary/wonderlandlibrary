package org.yaml.snakeyaml.util;

import java.util.ArrayList;

public class ArrayStack<T> {
  private ArrayList<T> stack;
  
  public ArrayStack(int initSize) {
    this.stack = new ArrayList<>(initSize);
  }
  
  public void push(T obj) {
    this.stack.add(obj);
  }
  
  public T pop() {
    return this.stack.remove(this.stack.size() - 1);
  }
  
  public boolean isEmpty() {
    return this.stack.isEmpty();
  }
  
  public void clear() {
    this.stack.clear();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyam\\util\ArrayStack.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */