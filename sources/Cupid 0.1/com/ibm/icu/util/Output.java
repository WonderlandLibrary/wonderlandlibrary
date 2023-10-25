package com.ibm.icu.util;

public class Output<T> {
  public T value;
  
  public String toString() {
    return (this.value == null) ? "null" : this.value.toString();
  }
  
  public Output() {}
  
  public Output(T value) {
    this.value = value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\Output.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */