package me.tojatta.api.utilities.vector.impl;

import me.tojatta.api.utilities.vector.Vector;

public class Vector2<T extends Number> extends Vector<Number> {
  public Vector2(T x, T y) {
    super((Number)x, (Number)y, Integer.valueOf(0));
  }
  
  public Vector3<T> toVector3() {
    return new Vector3<>((T)getX(), (T)getY(), (T)getZ());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\tojatta\ap\\utilities\vector\impl\Vector2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */