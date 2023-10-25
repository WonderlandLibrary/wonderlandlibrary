package javax.vecmath;

import java.io.Serializable;

public class Vector3f extends Tuple3f implements Serializable {
  static final long serialVersionUID = -7031930069184524614L;
  
  public Vector3f(float x, float y, float z) {
    super(x, y, z);
  }
  
  public Vector3f(float[] v) {
    super(v);
  }
  
  public Vector3f(Vector3f v1) {
    super(v1);
  }
  
  public Vector3f(Vector3d v1) {
    super(v1);
  }
  
  public Vector3f(Tuple3f t1) {
    super(t1);
  }
  
  public Vector3f(Tuple3d t1) {
    super(t1);
  }
  
  public Vector3f() {}
  
  public final float lengthSquared() {
    return this.x * this.x + this.y * this.y + this.z * this.z;
  }
  
  public final float length() {
    return (float)Math.sqrt((this.x * this.x + this.y * this.y + this.z * this.z));
  }
  
  public final void cross(Vector3f v1, Vector3f v2) {
    float x = v1.y * v2.z - v1.z * v2.y;
    float y = v2.x * v1.z - v2.z * v1.x;
    this.z = v1.x * v2.y - v1.y * v2.x;
    this.x = x;
    this.y = y;
  }
  
  public final float dot(Vector3f v1) {
    return this.x * v1.x + this.y * v1.y + this.z * v1.z;
  }
  
  public final void normalize(Vector3f v1) {
    float norm = (float)(1.0D / Math.sqrt((v1.x * v1.x + v1.y * v1.y + v1.z * v1.z)));
    v1.x *= norm;
    v1.y *= norm;
    v1.z *= norm;
  }
  
  public final void normalize() {
    float norm = (float)(1.0D / Math.sqrt((this.x * this.x + this.y * this.y + this.z * this.z)));
    this.x *= norm;
    this.y *= norm;
    this.z *= norm;
  }
  
  public final float angle(Vector3f v1) {
    double vDot = (dot(v1) / length() * v1.length());
    if (vDot < -1.0D)
      vDot = -1.0D; 
    if (vDot > 1.0D)
      vDot = 1.0D; 
    return (float)Math.acos(vDot);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\javax\vecmath\Vector3f.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */