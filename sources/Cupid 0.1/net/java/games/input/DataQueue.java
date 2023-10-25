package net.java.games.input;

final class DataQueue {
  private final Object[] elements;
  
  private int position;
  
  private int limit;
  
  static final boolean $assertionsDisabled;
  
  public DataQueue(int size, Class element_type) {
    this.elements = new Object[size];
    for (int i = 0; i < this.elements.length; i++) {
      try {
        this.elements[i] = element_type.newInstance();
      } catch (InstantiationException e) {
        throw new RuntimeException(e);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      } 
    } 
    clear();
  }
  
  public final void clear() {
    this.position = 0;
    this.limit = this.elements.length;
  }
  
  public final int position() {
    return this.position;
  }
  
  public final int limit() {
    return this.limit;
  }
  
  public final Object get(int index) {
    assert index < this.limit;
    return this.elements[index];
  }
  
  public final Object get() {
    if (!hasRemaining())
      return null; 
    return get(this.position++);
  }
  
  public final void compact() {
    int index = 0;
    while (hasRemaining()) {
      swap(this.position, index);
      this.position++;
      index++;
    } 
    this.position = index;
    this.limit = this.elements.length;
  }
  
  private final void swap(int index1, int index2) {
    Object temp = this.elements[index1];
    this.elements[index1] = this.elements[index2];
    this.elements[index2] = temp;
  }
  
  public final void flip() {
    this.limit = this.position;
    this.position = 0;
  }
  
  public final boolean hasRemaining() {
    return (remaining() > 0);
  }
  
  public final int remaining() {
    return this.limit - this.position;
  }
  
  public final void position(int position) {
    this.position = position;
  }
  
  public final Object[] getElements() {
    return this.elements;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\DataQueue.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */