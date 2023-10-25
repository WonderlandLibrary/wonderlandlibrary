package javax.vecmath;

import java.io.Serializable;

public class AxisAngle4d implements Serializable, Cloneable {
  static final long serialVersionUID = 3644296204459140589L;
  
  public double x;
  
  public double y;
  
  public double z;
  
  public double angle;
  
  static final double EPS = 1.0E-12D;
  
  public AxisAngle4d(double x, double y, double z, double angle) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.angle = angle;
  }
  
  public AxisAngle4d(double[] a) {
    this.x = a[0];
    this.y = a[1];
    this.z = a[2];
    this.angle = a[3];
  }
  
  public AxisAngle4d(AxisAngle4d a1) {
    this.x = a1.x;
    this.y = a1.y;
    this.z = a1.z;
    this.angle = a1.angle;
  }
  
  public AxisAngle4d(AxisAngle4f a1) {
    this.x = a1.x;
    this.y = a1.y;
    this.z = a1.z;
    this.angle = a1.angle;
  }
  
  public AxisAngle4d(Vector3d axis, double angle) {
    this.x = axis.x;
    this.y = axis.y;
    this.z = axis.z;
    this.angle = angle;
  }
  
  public AxisAngle4d() {
    this.x = 0.0D;
    this.y = 0.0D;
    this.z = 1.0D;
    this.angle = 0.0D;
  }
  
  public final void set(double x, double y, double z, double angle) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.angle = angle;
  }
  
  public final void set(double[] a) {
    this.x = a[0];
    this.y = a[1];
    this.z = a[2];
    this.angle = a[3];
  }
  
  public final void set(AxisAngle4d a1) {
    this.x = a1.x;
    this.y = a1.y;
    this.z = a1.z;
    this.angle = a1.angle;
  }
  
  public final void set(AxisAngle4f a1) {
    this.x = a1.x;
    this.y = a1.y;
    this.z = a1.z;
    this.angle = a1.angle;
  }
  
  public final void set(Vector3d axis, double angle) {
    this.x = axis.x;
    this.y = axis.y;
    this.z = axis.z;
    this.angle = angle;
  }
  
  public final void get(double[] a) {
    a[0] = this.x;
    a[1] = this.y;
    a[2] = this.z;
    a[3] = this.angle;
  }
  
  public final void set(Matrix4f m1) {
    Matrix3d m3d = new Matrix3d();
    m1.get(m3d);
    this.x = (float)(m3d.m21 - m3d.m12);
    this.y = (float)(m3d.m02 - m3d.m20);
    this.z = (float)(m3d.m10 - m3d.m01);
    double mag = this.x * this.x + this.y * this.y + this.z * this.z;
    if (mag > 1.0E-12D) {
      mag = Math.sqrt(mag);
      double sin = 0.5D * mag;
      double cos = 0.5D * (m3d.m00 + m3d.m11 + m3d.m22 - 1.0D);
      this.angle = (float)Math.atan2(sin, cos);
      double invMag = 1.0D / mag;
      this.x *= invMag;
      this.y *= invMag;
      this.z *= invMag;
    } else {
      this.x = 0.0D;
      this.y = 1.0D;
      this.z = 0.0D;
      this.angle = 0.0D;
    } 
  }
  
  public final void set(Matrix4d m1) {
    Matrix3d m3d = new Matrix3d();
    m1.get(m3d);
    this.x = (float)(m3d.m21 - m3d.m12);
    this.y = (float)(m3d.m02 - m3d.m20);
    this.z = (float)(m3d.m10 - m3d.m01);
    double mag = this.x * this.x + this.y * this.y + this.z * this.z;
    if (mag > 1.0E-12D) {
      mag = Math.sqrt(mag);
      double sin = 0.5D * mag;
      double cos = 0.5D * (m3d.m00 + m3d.m11 + m3d.m22 - 1.0D);
      this.angle = (float)Math.atan2(sin, cos);
      double invMag = 1.0D / mag;
      this.x *= invMag;
      this.y *= invMag;
      this.z *= invMag;
    } else {
      this.x = 0.0D;
      this.y = 1.0D;
      this.z = 0.0D;
      this.angle = 0.0D;
    } 
  }
  
  public final void set(Matrix3f m1) {
    this.x = (m1.m21 - m1.m12);
    this.y = (m1.m02 - m1.m20);
    this.z = (m1.m10 - m1.m01);
    double mag = this.x * this.x + this.y * this.y + this.z * this.z;
    if (mag > 1.0E-12D) {
      mag = Math.sqrt(mag);
      double sin = 0.5D * mag;
      double cos = 0.5D * ((m1.m00 + m1.m11 + m1.m22) - 1.0D);
      this.angle = (float)Math.atan2(sin, cos);
      double invMag = 1.0D / mag;
      this.x *= invMag;
      this.y *= invMag;
      this.z *= invMag;
    } else {
      this.x = 0.0D;
      this.y = 1.0D;
      this.z = 0.0D;
      this.angle = 0.0D;
    } 
  }
  
  public final void set(Matrix3d m1) {
    this.x = (float)(m1.m21 - m1.m12);
    this.y = (float)(m1.m02 - m1.m20);
    this.z = (float)(m1.m10 - m1.m01);
    double mag = this.x * this.x + this.y * this.y + this.z * this.z;
    if (mag > 1.0E-12D) {
      mag = Math.sqrt(mag);
      double sin = 0.5D * mag;
      double cos = 0.5D * (m1.m00 + m1.m11 + m1.m22 - 1.0D);
      this.angle = (float)Math.atan2(sin, cos);
      double invMag = 1.0D / mag;
      this.x *= invMag;
      this.y *= invMag;
      this.z *= invMag;
    } else {
      this.x = 0.0D;
      this.y = 1.0D;
      this.z = 0.0D;
      this.angle = 0.0D;
    } 
  }
  
  public final void set(Quat4f q1) {
    double mag = (q1.x * q1.x + q1.y * q1.y + q1.z * q1.z);
    if (mag > 1.0E-12D) {
      mag = Math.sqrt(mag);
      double invMag = 1.0D / mag;
      this.x = q1.x * invMag;
      this.y = q1.y * invMag;
      this.z = q1.z * invMag;
      this.angle = 2.0D * Math.atan2(mag, q1.w);
    } else {
      this.x = 0.0D;
      this.y = 1.0D;
      this.z = 0.0D;
      this.angle = 0.0D;
    } 
  }
  
  public final void set(Quat4d q1) {
    double mag = q1.x * q1.x + q1.y * q1.y + q1.z * q1.z;
    if (mag > 1.0E-12D) {
      mag = Math.sqrt(mag);
      double invMag = 1.0D / mag;
      this.x = q1.x * invMag;
      this.y = q1.y * invMag;
      this.z = q1.z * invMag;
      this.angle = 2.0D * Math.atan2(mag, q1.w);
    } else {
      this.x = 0.0D;
      this.y = 1.0D;
      this.z = 0.0D;
      this.angle = 0.0D;
    } 
  }
  
  public String toString() {
    return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.angle + ")";
  }
  
  public boolean equals(AxisAngle4d a1) {
    try {
      return (this.x == a1.x && this.y == a1.y && this.z == a1.z && this.angle == a1.angle);
    } catch (NullPointerException e2) {
      return false;
    } 
  }
  
  public boolean equals(Object o1) {
    try {
      AxisAngle4d a2 = (AxisAngle4d)o1;
      return (this.x == a2.x && this.y == a2.y && this.z == a2.z && this.angle == a2.angle);
    } catch (NullPointerException e2) {
      return false;
    } catch (ClassCastException e1) {
      return false;
    } 
  }
  
  public boolean epsilonEquals(AxisAngle4d a1, double epsilon) {
    double diff = this.x - a1.x;
    if (((diff < 0.0D) ? -diff : diff) > epsilon)
      return false; 
    diff = this.y - a1.y;
    if (((diff < 0.0D) ? -diff : diff) > epsilon)
      return false; 
    diff = this.z - a1.z;
    if (((diff < 0.0D) ? -diff : diff) > epsilon)
      return false; 
    diff = this.angle - a1.angle;
    if (((diff < 0.0D) ? -diff : diff) > epsilon)
      return false; 
    return true;
  }
  
  public int hashCode() {
    long bits = 1L;
    bits = 31L * bits + VecMathUtil.doubleToLongBits(this.x);
    bits = 31L * bits + VecMathUtil.doubleToLongBits(this.y);
    bits = 31L * bits + VecMathUtil.doubleToLongBits(this.z);
    bits = 31L * bits + VecMathUtil.doubleToLongBits(this.angle);
    return (int)(bits ^ bits >> 32L);
  }
  
  public Object clone() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException e) {
      throw new InternalError();
    } 
  }
  
  public final double getAngle() {
    return this.angle;
  }
  
  public final void setAngle(double angle) {
    this.angle = angle;
  }
  
  public double getX() {
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
  
  public double getZ() {
    return this.z;
  }
  
  public final void setZ(double z) {
    this.z = z;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\javax\vecmath\AxisAngle4d.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */