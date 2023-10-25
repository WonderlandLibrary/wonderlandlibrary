package me.tojatta.api.utilities.vector;

public class Vector<T extends Number> {
  private T x;
  
  private T y;
  
  private T z;
  
  public Vector(T x, T y, T z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public Vector setX(T x) {
    this.x = x;
    return this;
  }
  
  public Vector setY(T y) {
    this.y = y;
    return this;
  }
  
  public Vector setZ(T z) {
    this.z = z;
    return this;
  }
  
  public T getX() {
    return this.x;
  }
  
  public T getY() {
    return this.y;
  }
  
  public T getZ() {
    return this.z;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\tojatta\ap\\utilities\vector\Vector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */