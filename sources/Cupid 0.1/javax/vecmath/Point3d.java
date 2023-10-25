package javax.vecmath;

import java.io.Serializable;

public class Point3d extends Tuple3d implements Serializable {
  static final long serialVersionUID = 5718062286069042927L;
  
  public Point3d(double x, double y, double z) {
    super(x, y, z);
  }
  
  public Point3d(double[] p) {
    super(p);
  }
  
  public Point3d(Point3d p1) {
    super(p1);
  }
  
  public Point3d(Point3f p1) {
    super(p1);
  }
  
  public Point3d(Tuple3f t1) {
    super(t1);
  }
  
  public Point3d(Tuple3d t1) {
    super(t1);
  }
  
  public Point3d() {}
  
  public final double distanceSquared(Point3d p1) {
    double dx = this.x - p1.x;
    double dy = this.y - p1.y;
    double dz = this.z - p1.z;
    return dx * dx + dy * dy + dz * dz;
  }
  
  public final double distance(Point3d p1) {
    double dx = this.x - p1.x;
    double dy = this.y - p1.y;
    double dz = this.z - p1.z;
    return Math.sqrt(dx * dx + dy * dy + dz * dz);
  }
  
  public final double distanceL1(Point3d p1) {
    return Math.abs(this.x - p1.x) + Math.abs(this.y - p1.y) + Math.abs(this.z - p1.z);
  }
  
  public final double distanceLinf(Point3d p1) {
    double tmp = Math.max(Math.abs(this.x - p1.x), Math.abs(this.y - p1.y));
    return Math.max(tmp, Math.abs(this.z - p1.z));
  }
  
  public final void project(Point4d p1) {
    double oneOw = 1.0D / p1.w;
    this.x = p1.x * oneOw;
    this.y = p1.y * oneOw;
    this.z = p1.z * oneOw;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\javax\vecmath\Point3d.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */