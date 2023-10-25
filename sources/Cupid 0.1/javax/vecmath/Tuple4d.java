package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple4d implements Serializable, Cloneable {
  static final long serialVersionUID = -4748953690425311052L;
  
  public double x;
  
  public double y;
  
  public double z;
  
  public double w;
  
  public Tuple4d(double x, double y, double z, double w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }
  
  public Tuple4d(double[] t) {
    this.x = t[0];
    this.y = t[1];
    this.z = t[2];
    this.w = t[3];
  }
  
  public Tuple4d(Tuple4d t1) {
    this.x = t1.x;
    this.y = t1.y;
    this.z = t1.z;
    this.w = t1.w;
  }
  
  public Tuple4d(Tuple4f t1) {
    this.x = t1.x;
    this.y = t1.y;
    this.z = t1.z;
    this.w = t1.w;
  }
  
  public Tuple4d() {
    this.x = 0.0D;
    this.y = 0.0D;
    this.z = 0.0D;
    this.w = 0.0D;
  }
  
  public final void set(double x, double y, double z, double w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }
  
  public final void set(double[] t) {
    this.x = t[0];
    this.y = t[1];
    this.z = t[2];
    this.w = t[3];
  }
  
  public final void set(Tuple4d t1) {
    this.x = t1.x;
    this.y = t1.y;
    this.z = t1.z;
    this.w = t1.w;
  }
  
  public final void set(Tuple4f t1) {
    this.x = t1.x;
    this.y = t1.y;
    this.z = t1.z;
    this.w = t1.w;
  }
  
  public final void get(double[] t) {
    t[0] = this.x;
    t[1] = this.y;
    t[2] = this.z;
    t[3] = this.w;
  }
  
  public final void get(Tuple4d t) {
    t.x = this.x;
    t.y = this.y;
    t.z = this.z;
    t.w = this.w;
  }
  
  public final void add(Tuple4d t1, Tuple4d t2) {
    t1.x += t2.x;
    t1.y += t2.y;
    t1.z += t2.z;
    t1.w += t2.w;
  }
  
  public final void add(Tuple4d t1) {
    this.x += t1.x;
    this.y += t1.y;
    this.z += t1.z;
    this.w += t1.w;
  }
  
  public final void sub(Tuple4d t1, Tuple4d t2) {
    t1.x -= t2.x;
    t1.y -= t2.y;
    t1.z -= t2.z;
    t1.w -= t2.w;
  }
  
  public final void sub(Tuple4d t1) {
    this.x -= t1.x;
    this.y -= t1.y;
    this.z -= t1.z;
    this.w -= t1.w;
  }
  
  public final void negate(Tuple4d t1) {
    this.x = -t1.x;
    this.y = -t1.y;
    this.z = -t1.z;
    this.w = -t1.w;
  }
  
  public final void negate() {
    this.x = -this.x;
    this.y = -this.y;
    this.z = -this.z;
    this.w = -this.w;
  }
  
  public final void scale(double s, Tuple4d t1) {
    this.x = s * t1.x;
    this.y = s * t1.y;
    this.z = s * t1.z;
    this.w = s * t1.w;
  }
  
  public final void scale(double s) {
    this.x *= s;
    this.y *= s;
    this.z *= s;
    this.w *= s;
  }
  
  public final void scaleAdd(double s, Tuple4d t1, Tuple4d t2) {
    this.x = s * t1.x + t2.x;
    this.y = s * t1.y + t2.y;
    this.z = s * t1.z + t2.z;
    this.w = s * t1.w + t2.w;
  }
  
  public final void scaleAdd(float s, Tuple4d t1) {
    scaleAdd(s, t1);
  }
  
  public final void scaleAdd(double s, Tuple4d t1) {
    this.x = s * this.x + t1.x;
    this.y = s * this.y + t1.y;
    this.z = s * this.z + t1.z;
    this.w = s * this.w + t1.w;
  }
  
  public String toString() {
    return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + ")";
  }
  
  public boolean equals(Tuple4d t1) {
    try {
      return (this.x == t1.x && this.y == t1.y && this.z == t1.z && this.w == t1.w);
    } catch (NullPointerException e2) {
      return false;
    } 
  }
  
  public boolean equals(Object t1) {
    try {
      Tuple4d t2 = (Tuple4d)t1;
      return (this.x == t2.x && this.y == t2.y && this.z == t2.z && this.w == t2.w);
    } catch (NullPointerException e2) {
      return false;
    } catch (ClassCastException e1) {
      return false;
    } 
  }
  
  public boolean epsilonEquals(Tuple4d t1, double epsilon) {
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
    diff = this.w - t1.w;
    if (Double.isNaN(diff))
      return false; 
    if (((diff < 0.0D) ? -diff : diff) > epsilon)
      return false; 
    return true;
  }
  
  public int hashCode() {
    long bits = 1L;
    bits = 31L * bits + VecMathUtil.doubleToLongBits(this.x);
    bits = 31L * bits + VecMathUtil.doubleToLongBits(this.y);
    bits = 31L * bits + VecMathUtil.doubleToLongBits(this.z);
    bits = 31L * bits + VecMathUtil.doubleToLongBits(this.w);
    return (int)(bits ^ bits >> 32L);
  }
  
  public final void clamp(float min, float max, Tuple4d t) {
    clamp(min, max, t);
  }
  
  public final void clamp(double min, double max, Tuple4d t) {
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
    if (t.w > max) {
      this.w = max;
    } else if (t.w < min) {
      this.w = min;
    } else {
      this.w = t.w;
    } 
  }
  
  public final void clampMin(float min, Tuple4d t) {
    clampMin(min, t);
  }
  
  public final void clampMin(double min, Tuple4d t) {
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
    if (t.w < min) {
      this.w = min;
    } else {
      this.w = t.w;
    } 
  }
  
  public final void clampMax(float max, Tuple4d t) {
    clampMax(max, t);
  }
  
  public final void clampMax(double max, Tuple4d t) {
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
    if (t.w > max) {
      this.w = max;
    } else {
      this.w = t.z;
    } 
  }
  
  public final void absolute(Tuple4d t) {
    this.x = Math.abs(t.x);
    this.y = Math.abs(t.y);
    this.z = Math.abs(t.z);
    this.w = Math.abs(t.w);
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
    if (this.w > max) {
      this.w = max;
    } else if (this.w < min) {
      this.w = min;
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
    if (this.w < min)
      this.w = min; 
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
    if (this.w > max)
      this.w = max; 
  }
  
  public final void absolute() {
    this.x = Math.abs(this.x);
    this.y = Math.abs(this.y);
    this.z = Math.abs(this.z);
    this.w = Math.abs(this.w);
  }
  
  public void interpolate(Tuple4d t1, Tuple4d t2, float alpha) {
    interpolate(t1, t2, alpha);
  }
  
  public void interpolate(Tuple4d t1, Tuple4d t2, double alpha) {
    this.x = (1.0D - alpha) * t1.x + alpha * t2.x;
    this.y = (1.0D - alpha) * t1.y + alpha * t2.y;
    this.z = (1.0D - alpha) * t1.z + alpha * t2.z;
    this.w = (1.0D - alpha) * t1.w + alpha * t2.w;
  }
  
  public void interpolate(Tuple4d t1, float alpha) {
    interpolate(t1, alpha);
  }
  
  public void interpolate(Tuple4d t1, double alpha) {
    this.x = (1.0D - alpha) * this.x + alpha * t1.x;
    this.y = (1.0D - alpha) * this.y + alpha * t1.y;
    this.z = (1.0D - alpha) * this.z + alpha * t1.z;
    this.w = (1.0D - alpha) * this.w + alpha * t1.w;
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
  
  public final double getW() {
    return this.w;
  }
  
  public final void setW(double w) {
    this.w = w;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\javax\vecmath\Tuple4d.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */