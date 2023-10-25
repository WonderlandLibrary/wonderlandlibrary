package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple3d implements Serializable, Cloneable {
  static final long serialVersionUID = 5542096614926168415L;
  
  public double x;
  
  public double y;
  
  public double z;
  
  public Tuple3d(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public Tuple3d(double[] t) {
    this.x = t[0];
    this.y = t[1];
    this.z = t[2];
  }
  
  public Tuple3d(Tuple3d t1) {
    this.x = t1.x;
    this.y = t1.y;
    this.z = t1.z;
  }
  
  public Tuple3d(Tuple3f t1) {
    this.x = t1.x;
    this.y = t1.y;
    this.z = t1.z;
  }
  
  public Tuple3d() {
    this.x = 0.0D;
    this.y = 0.0D;
    this.z = 0.0D;
  }
  
  public final void set(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public final void set(double[] t) {
    this.x = t[0];
    this.y = t[1];
    this.z = t[2];
  }
  
  public final void set(Tuple3d t1) {
    this.x = t1.x;
    this.y = t1.y;
    this.z = t1.z;
  }
  
  public final void set(Tuple3f t1) {
    this.x = t1.x;
    this.y = t1.y;
    this.z = t1.z;
  }
  
  public final void get(double[] t) {
    t[0] = this.x;
    t[1] = this.y;
    t[2] = this.z;
  }
  
  public final void get(Tuple3d t) {
    t.x = this.x;
    t.y = this.y;
    t.z = this.z;
  }
  
  public final void add(Tuple3d t1, Tuple3d t2) {
    t1.x += t2.x;
    t1.y += t2.y;
    t1.z += t2.z;
  }
  
  public final void add(Tuple3d t1) {
    this.x += t1.x;
    this.y += t1.y;
    this.z += t1.z;
  }
  
  public final void sub(Tuple3d t1, Tuple3d t2) {
    t1.x -= t2.x;
    t1.y -= t2.y;
    t1.z -= t2.z;
  }
  
  public final void sub(Tuple3d t1) {
    this.x -= t1.x;
    this.y -= t1.y;
    this.z -= t1.z;
  }
  
  public final void negate(Tuple3d t1) {
    this.x = -t1.x;
    this.y = -t1.y;
    this.z = -t1.z;
  }
  
  public final void negate() {
    this.x = -this.x;
    this.y = -this.y;
    this.z = -this.z;
  }
  
  public final void scale(double s, Tuple3d t1) {
    this.x = s * t1.x;
    this.y = s * t1.y;
    this.z = s * t1.z;
  }
  
  public final void scale(double s) {
    this.x *= s;
    this.y *= s;
    this.z *= s;
  }
  
  public final void scaleAdd(double s, Tuple3d t1, Tuple3d t2) {
    this.x = s * t1.x + t2.x;
    this.y = s * t1.y + t2.y;
    this.z = s * t1.z + t2.z;
  }
  
  public final void scaleAdd(double s, Tuple3f t1) {
    scaleAdd(s, new Point3d(t1));
  }
  
  public final void scaleAdd(double s, Tuple3d t1) {
    this.x = s * this.x + t1.x;
    this.y = s * this.y + t1.y;
    this.z = s * this.z + t1.z;
  }
  
  public String toString() {
    return "(" + this.x + ", " + this.y + ", " + this.z + ")";
  }
  
  public int hashCode() {
    long bits = 1L;
    bits = 31L * bits + VecMathUtil.doubleToLongBits(this.x);
    bits = 31L * bits + VecMathUtil.doubleToLongBits(this.y);
    bits = 31L * bits + VecMathUtil.doubleToLongBits(this.z);
    return (int)(bits ^ bits >> 32L);
  }
  
  public boolean equals(Tuple3d t1) {
    try {
      return (this.x == t1.x && this.y == t1.y && this.z == t1.z);
    } catch (NullPointerException e2) {
      return false;
    } 
  }
  
  public boolean equals(Object t1) {
    try {
      Tuple3d t2 = (Tuple3d)t1;
      return (this.x == t2.x && this.y == t2.y && this.z == t2.z);
    } catch (ClassCastException e1) {
      return false;
    } catch (NullPointerException e2) {
      return false;
    } 
  }
  
  public boolean epsilonEquals(Tuple3d t1, double epsilon) {
    double diff = this.x - t1.x;
    if (Double.isNaN(diff))
      return false; 
    if (((diff < 0.0D) ? -diff : diff) > epsilon)
      return false; 
    diff = this.y - t1.y;
    if (Double.isNaN(diff))
      return false; 
    if (((diff < 0.0D) ? -diff : diff) > epsilon)
      return false; 
    diff = this.z - t1.z;
    if (Double.isNaN(diff))
      return false; 
    if (((diff < 0.0D) ? -diff : diff) > epsilon)
      return false; 
    return true;
  }
  
  public final void clamp(float min, float max, Tuple3d t) {
    clamp(min, max, t);
  }
  
  public final void clamp(double min, double max, Tuple3d t) {
    if (t.x > max) {
      this.x = max;
    } else if (t.x < min) {
      this.x = min;
    } else {
      this.x = t.x;
    } 
    if (t.y > max) {
      this.y = max;
    } else if (t.y < min) {
      this.y = min;
    } else {
      this.y = t.y;
    } 
    if (t.z > max) {
      this.z = max;
    } else if (t.z < min) {
      this.z = min;
    } else {
      this.z = t.z;
    } 
  }
  
  public final void clampMin(float min, Tuple3d t) {
    clampMin(min, t);
  }
  
  public final void clampMin(double min, Tuple3d t) {
    if (t.x < min) {
      this.x = min;
    } else {
      this.x = t.x;
    } 
    if (t.y < min) {
      this.y = min;
    } else {
      this.y = t.y;
    } 
    if (t.z < min) {
      this.z = min;
    } else {
      this.z = t.z;
    } 
  }
  
  public final void clampMax(float max, Tuple3d t) {
    clampMax(max, t);
  }
  
  public final void clampMax(double max, Tuple3d t) {
    if (t.x > max) {
      this.x = max;
    } else {
      this.x = t.x;
    } 
    if (t.y > max) {
      this.y = max;
    } else {
      this.y = t.y;
    } 
    if (t.z > max) {
      this.z = max;
    } else {
      this.z = t.z;
    } 
  }
  
  public final void absolute(Tuple3d t) {
    this.x = Math.abs(t.x);
    this.y = Math.abs(t.y);
    this.z = Math.abs(t.z);
  }
  
  public final void clamp(float min, float max) {
    clamp(min, max);
  }
  
  public final void clamp(double min, double max) {
    if (this.x > max) {
      this.x = max;
    } else if (this.x < min) {
      this.x = min;
    } 
    if (this.y > max) {
      this.y = max;
    } else if (this.y < min) {
      this.y = min;
    } 
    if (this.z > max) {
      this.z = max;
    } else if (this.z < min) {
      this.z = min;
    } 
  }
  
  public final void clampMin(float min) {
    clampMin(min);
  }
  
  public final void clampMin(double min) {
    if (this.x < min)
      this.x = min; 
    if (this.y < min)
      this.y = min; 
    if (this.z < min)
      this.z = min; 
  }
  
  public final void clampMax(float max) {
    clampMax(max);
  }
  
  public final void clampMax(double max) {
    if (this.x > max)
      this.x = max; 
    if (this.y > max)
      this.y = max; 
    if (this.z > max)
      this.z = max; 
  }
  
  public final void absolute() {
    this.x = Math.abs(this.x);
    this.y = Math.abs(this.y);
    this.z = Math.abs(this.z);
  }
  
  public final void interpolate(Tuple3d t1, Tuple3d t2, float alpha) {
    interpolate(t1, t2, alpha);
  }
  
  public final void interpolate(Tuple3d t1, Tuple3d t2, double alpha) {
    this.x = (1.0D - alpha) * t1.x + alpha * t2.x;
    this.y = (1.0D - alpha) * t1.y + alpha * t2.y;
    this.z = (1.0D - alpha) * t1.z + alpha * t2.z;
  }
  
  public final void interpolate(Tuple3d t1, float alpha) {
    interpolate(t1, alpha);
  }
  
  public final void interpolate(Tuple3d t1, double alpha) {
    this.x = (1.0D - alpha) * this.x + alpha * t1.x;
    this.y = (1.0D - alpha) * this.y + alpha * t1.y;
    this.z = (1.0D - alpha) * this.z + alpha * t1.z;
  }
  
  public Object clone() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException e) {
      throw new InternalError();
    } 
  }
  
  public final double getX() {
    return this.x;
  }
  
  public final void setX(double x) {
    this.x = x;
  }
  
  public final double getY() {
    return this.y;
  }
  
  public final void setY(double y) {
    this.y = y;
  }
  
  public final double getZ() {
    return this.z;
  }
  
  public final void setZ(double z) {
    this.z = z;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\javax\vecmath\Tuple3d.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */