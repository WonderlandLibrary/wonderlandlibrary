package javax.vecmath;

import java.io.Serializable;

public class Vector3d extends Tuple3d implements Serializable {
  static final long serialVersionUID = 3761969948420550442L;
  
  public Vector3d(double x, double y, double z) {
    super(x, y, z);
  }
  
  public Vector3d(double[] v) {
    super(v);
  }
  
  public Vector3d(Vector3d v1) {
    super(v1);
  }
  
  public Vector3d(Vector3f v1) {
    super(v1);
  }
  
  public Vector3d(Tuple3f t1) {
    super(t1);
  }
  
  public Vector3d(Tuple3d t1) {
    super(t1);
  }
  
  public Vector3d() {}
  
  public final void cross(Vector3d v1, Vector3d v2) {
    double x = v1.y * v2.z - v1.z * v2.y;
    double y = v2.x * v1.z - v2.z * v1.x;
    this.z = v1.x * v2.y - v1.y * v2.x;
    this.x = x;
    this.y = y;
  }
  
  public final void normalize(Vector3d v1) {
    double norm = 1.0D / Math.sqrt(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z);
    v1.x *= norm;
    v1.y *= norm;
    v1.z *= norm;
  }
  
  public final void normalize() {
    double norm = 1.0D / Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    this.x *= norm;
    this.y *= norm;
    this.z *= norm;
  }
  
  public final double dot(Vector3d v1) {
    return this.x * v1.x + this.y * v1.y + this.z * v1.z;
  }
  
  public final double lengthSquared() {
    return this.x * this.x + this.y * this.y + this.z * this.z;
  }
  
  public final double length() {
    return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
  }
  
  public final double angle(Vector3d v1) {
    double vDot = dot(v1) / length() * v1.length();
    if (vDot < -1.0D)
      vDot = -1.0D; 
    if (vDot > 1.0D)
      vDot = 1.0D; 
    return Math.acos(vDot);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\javax\vecmath\Vector3d.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */