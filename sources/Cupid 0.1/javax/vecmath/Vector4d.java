package javax.vecmath;

import java.io.Serializable;

public class Vector4d extends Tuple4d implements Serializable {
  static final long serialVersionUID = 3938123424117448700L;
  
  public Vector4d(double x, double y, double z, double w) {
    super(x, y, z, w);
  }
  
  public Vector4d(double[] v) {
    super(v);
  }
  
  public Vector4d(Vector4d v1) {
    super(v1);
  }
  
  public Vector4d(Vector4f v1) {
    super(v1);
  }
  
  public Vector4d(Tuple4f t1) {
    super(t1);
  }
  
  public Vector4d(Tuple4d t1) {
    super(t1);
  }
  
  public Vector4d(Tuple3d t1) {
    super(t1.x, t1.y, t1.z, 0.0D);
  }
  
  public Vector4d() {}
  
  public final void set(Tuple3d t1) {
    this.x = t1.x;
    this.y = t1.y;
    this.z = t1.z;
    this.w = 0.0D;
  }
  
  public final double length() {
    return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
  }
  
  public final double lengthSquared() {
    return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
  }
  
  public final double dot(Vector4d v1) {
    return this.x * v1.x + this.y * v1.y + this.z * v1.z + this.w * v1.w;
  }
  
  public final void normalize(Vector4d v1) {
    double norm = 1.0D / Math.sqrt(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z + v1.w * v1.w);
    v1.x *= norm;
    v1.y *= norm;
    v1.z *= norm;
    v1.w *= norm;
  }
  
  public final void normalize() {
    double norm = 1.0D / Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
    this.x *= norm;
    this.y *= norm;
    this.z *= norm;
    this.w *= norm;
  }
  
  public final double angle(Vector4d v1) {
    double vDot = dot(v1) / length() * v1.length();
    if (vDot < -1.0D)
      vDot = -1.0D; 
    if (vDot > 1.0D)
      vDot = 1.0D; 
    return Math.acos(vDot);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\javax\vecmath\Vector4d.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */