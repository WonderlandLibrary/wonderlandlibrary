package javax.vecmath;

import java.io.Serializable;

public class Point3f extends Tuple3f implements Serializable {
  static final long serialVersionUID = -8689337816398030143L;
  
  public Point3f(float x, float y, float z) {
    super(x, y, z);
  }
  
  public Point3f(float[] p) {
    super(p);
  }
  
  public Point3f(Point3f p1) {
    super(p1);
  }
  
  public Point3f(Point3d p1) {
    super(p1);
  }
  
  public Point3f(Tuple3f t1) {
    super(t1);
  }
  
  public Point3f(Tuple3d t1) {
    super(t1);
  }
  
  public Point3f() {}
  
  public final float distanceSquared(Point3f p1) {
    float dx = this.x - p1.x;
    float dy = this.y - p1.y;
    float dz = this.z - p1.z;
    return dx * dx + dy * dy + dz * dz;
  }
  
  public final float distance(Point3f p1) {
    float dx = this.x - p1.x;
    float dy = this.y - p1.y;
    float dz = this.z - p1.z;
    return (float)Math.sqrt((dx * dx + dy * dy + dz * dz));
  }
  
  public final float distanceL1(Point3f p1) {
    return Math.abs(this.x - p1.x) + Math.abs(this.y - p1.y) + Math.abs(this.z - p1.z);
  }
  
  public final float distanceLinf(Point3f p1) {
    float tmp = Math.max(Math.abs(this.x - p1.x), Math.abs(this.y - p1.y));
    return Math.max(tmp, Math.abs(this.z - p1.z));
  }
  
  public final void project(Point4f p1) {
    float oneOw = 1.0F / p1.w;
    this.x = p1.x * oneOw;
    this.y = p1.y * oneOw;
    this.z = p1.z * oneOw;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\javax\vecmath\Point3f.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */