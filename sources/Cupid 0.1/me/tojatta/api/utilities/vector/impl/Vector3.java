package me.tojatta.api.utilities.vector.impl;

import me.tojatta.api.utilities.vector.Vector;

public class Vector3<T extends Number> extends Vector<Number> {
  public Vector3(T x, T y, T z) {
    super((Number)x, (Number)y, (Number)z);
  }
  
  public Vector2<T> toVector2() {
    return new Vector2<>((T)getX(), (T)getY());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\tojatta\ap\\utilities\vector\impl\Vector3.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */