package javax.vecmath;

import java.io.Serializable;

public class Quat4f extends Tuple4f implements Serializable {
  static final long serialVersionUID = 2675933778405442383L;
  
  static final double EPS = 1.0E-6D;
  
  static final double EPS2 = 1.0E-30D;
  
  static final double PIO2 = 1.57079632679D;
  
  public Quat4f(float x, float y, float z, float w) {
    float mag = (float)(1.0D / Math.sqrt((x * x + y * y + z * z + w * w)));
    this.x = x * mag;
    this.y = y * mag;
    this.z = z * mag;
    this.w = w * mag;
  }
  
  public Quat4f(float[] q) {
    float mag = (float)(1.0D / Math.sqrt((q[0] * q[0] + q[1] * q[1] + q[2] * q[2] + q[3] * q[3])));
    this.x = q[0] * mag;
    this.y = q[1] * mag;
    this.z = q[2] * mag;
    this.w = q[3] * mag;
  }
  
  public Quat4f(Quat4f q1) {
    super(q1);
  }
  
  public Quat4f(Quat4d q1) {
    super(q1);
  }
  
  public Quat4f(Tuple4f t1) {
    float mag = (float)(1.0D / Math.sqrt((t1.x * t1.x + t1.y * t1.y + t1.z * t1.z + t1.w * t1.w)));
    this.x = t1.x * mag;
    this.y = t1.y * mag;
    this.z = t1.z * mag;
    this.w = t1.w * mag;
  }
  
  public Quat4f(Tuple4d t1) {
    double mag = 1.0D / Math.sqrt(t1.x * t1.x + t1.y * t1.y + t1.z * t1.z + t1.w * t1.w);
    this.x = (float)(t1.x * mag);
    this.y = (float)(t1.y * mag);
    this.z = (float)(t1.z * mag);
    this.w = (float)(t1.w * mag);
  }
  
  public Quat4f() {}
  
  public final void conjugate(Quat4f q1) {
    this.x = -q1.x;
    this.y = -q1.y;
    this.z = -q1.z;
    this.w = q1.w;
  }
  
  public final void conjugate() {
    this.x = -this.x;
    this.y = -this.y;
    this.z = -this.z;
  }
  
  public final void mul(Quat4f q1, Quat4f q2) {
    if (this != q1 && this != q2) {
      this.w = q1.w * q2.w - q1.x * q2.x - q1.y * q2.y - q1.z * q2.z;
      this.x = q1.w * q2.x + q2.w * q1.x + q1.y * q2.z - q1.z * q2.y;
      this.y = q1.w * q2.y + q2.w * q1.y - q1.x * q2.z + q1.z * q2.x;
      this.z = q1.w * q2.z + q2.w * q1.z + q1.x * q2.y - q1.y * q2.x;
    } else {
      float w = q1.w * q2.w - q1.x * q2.x - q1.y * q2.y - q1.z * q2.z;
      float x = q1.w * q2.x + q2.w * q1.x + q1.y * q2.z - q1.z * q2.y;
      float y = q1.w * q2.y + q2.w * q1.y - q1.x * q2.z + q1.z * q2.x;
      this.z = q1.w * q2.z + q2.w * q1.z + q1.x * q2.y - q1.y * q2.x;
      this.w = w;
      this.x = x;
      this.y = y;
    } 
  }
  
  public final void mul(Quat4f q1) {
    float w = this.w * q1.w - this.x * q1.x - this.y * q1.y - this.z * q1.z;
    float x = this.w * q1.x + q1.w * this.x + this.y * q1.z - this.z * q1.y;
    float y = this.w * q1.y + q1.w * this.y - this.x * q1.z + this.z * q1.x;
    this.z = this.w * q1.z + q1.w * this.z + this.x * q1.y - this.y * q1.x;
    this.w = w;
    this.x = x;
    this.y = y;
  }
  
  public final void mulInverse(Quat4f q1, Quat4f q2) {
    Quat4f tempQuat = new Quat4f(q2);
    tempQuat.inverse();
    mul(q1, tempQuat);
  }
  
  public final void mulInverse(Quat4f q1) {
    Quat4f tempQuat = new Quat4f(q1);
    tempQuat.inverse();
    mul(tempQuat);
  }
  
  public final void inverse(Quat4f q1) {
    float norm = 1.0F / (q1.w * q1.w + q1.x * q1.x + q1.y * q1.y + q1.z * q1.z);
    this.w = norm * q1.w;
    this.x = -norm * q1.x;
    this.y = -norm * q1.y;
    this.z = -norm * q1.z;
  }
  
  public final void inverse() {
    float norm = 1.0F / (this.w * this.w + this.x * this.x + this.y * this.y + this.z * this.z);
    this.w *= norm;
    this.x *= -norm;
    this.y *= -norm;
    this.z *= -norm;
  }
  
  public final void normalize(Quat4f q1) {
    float norm = q1.x * q1.x + q1.y * q1.y + q1.z * q1.z + q1.w * q1.w;
    if (norm > 0.0F) {
      norm = 1.0F / (float)Math.sqrt(norm);
      this.x = norm * q1.x;
      this.y = norm * q1.y;
      this.z = norm * q1.z;
      this.w = norm * q1.w;
    } else {
      this.x = 0.0F;
      this.y = 0.0F;
      this.z = 0.0F;
      this.w = 0.0F;
    } 
  }
  
  public final void normalize() {
    float norm = this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
    if (norm > 0.0F) {
      norm = 1.0F / (float)Math.sqrt(norm);
      this.x *= norm;
      this.y *= norm;
      this.z *= norm;
      this.w *= norm;
    } else {
      this.x = 0.0F;
      this.y = 0.0F;
      this.z = 0.0F;
      this.w = 0.0F;
    } 
  }
  
  public final void set(Matrix4f m1) {
    float ww = 0.25F * (m1.m00 + m1.m11 + m1.m22 + m1.m33);
    if (ww >= 0.0F) {
      if (ww >= 1.0E-30D) {
        this.w = (float)Math.sqrt(ww);
        ww = 0.25F / this.w;
        this.x = (m1.m21 - m1.m12) * ww;
        this.y = (m1.m02 - m1.m20) * ww;
        this.z = (m1.m10 - m1.m01) * ww;
        return;
      } 
    } else {
      this.w = 0.0F;
      this.x = 0.0F;
      this.y = 0.0F;
      this.z = 1.0F;
      return;
    } 
    this.w = 0.0F;
    ww = -0.5F * (m1.m11 + m1.m22);
    if (ww >= 0.0F) {
      if (ww >= 1.0E-30D) {
        this.x = (float)Math.sqrt(ww);
        ww = 1.0F / 2.0F * this.x;
        this.y = m1.m10 * ww;
        this.z = m1.m20 * ww;
        return;
      } 
    } else {
      this.x = 0.0F;
      this.y = 0.0F;
      this.z = 1.0F;
      return;
    } 
    this.x = 0.0F;
    ww = 0.5F * (1.0F - m1.m22);
    if (ww >= 1.0E-30D) {
      this.y = (float)Math.sqrt(ww);
      this.z = m1.m21 / 2.0F * this.y;
      return;
    } 
    this.y = 0.0F;
    this.z = 1.0F;
  }
  
  public final void set(Matrix4d m1) {
    double ww = 0.25D * (m1.m00 + m1.m11 + m1.m22 + m1.m33);
    if (ww >= 0.0D) {
      if (ww >= 1.0E-30D) {
        this.w = (float)Math.sqrt(ww);
        ww = 0.25D / this.w;
        this.x = (float)((m1.m21 - m1.m12) * ww);
        this.y = (float)((m1.m02 - m1.m20) * ww);
        this.z = (float)((m1.m10 - m1.m01) * ww);
        return;
      } 
    } else {
      this.w = 0.0F;
      this.x = 0.0F;
      this.y = 0.0F;
      this.z = 1.0F;
      return;
    } 
    this.w = 0.0F;
    ww = -0.5D * (m1.m11 + m1.m22);
    if (ww >= 0.0D) {
      if (ww >= 1.0E-30D) {
        this.x = (float)Math.sqrt(ww);
        ww = 0.5D / this.x;
        this.y = (float)(m1.m10 * ww);
        this.z = (float)(m1.m20 * ww);
        return;
      } 
    } else {
      this.x = 0.0F;
      this.y = 0.0F;
      this.z = 1.0F;
      return;
    } 
    this.x = 0.0F;
    ww = 0.5D * (1.0D - m1.m22);
    if (ww >= 1.0E-30D) {
      this.y = (float)Math.sqrt(ww);
      this.z = (float)(m1.m21 / 2.0D * this.y);
      return;
    } 
    this.y = 0.0F;
    this.z = 1.0F;
  }
  
  public final void set(Matrix3f m1) {
    float ww = 0.25F * (m1.m00 + m1.m11 + m1.m22 + 1.0F);
    if (ww >= 0.0F) {
      if (ww >= 1.0E-30D) {
        this.w = (float)Math.sqrt(ww);
        ww = 0.25F / this.w;
        this.x = (m1.m21 - m1.m12) * ww;
        this.y = (m1.m02 - m1.m20) * ww;
        this.z = (m1.m10 - m1.m01) * ww;
        return;
      } 
    } else {
      this.w = 0.0F;
      this.x = 0.0F;
      this.y = 0.0F;
      this.z = 1.0F;
      return;
    } 
    this.w = 0.0F;
    ww = -0.5F * (m1.m11 + m1.m22);
    if (ww >= 0.0F) {
      if (ww >= 1.0E-30D) {
        this.x = (float)Math.sqrt(ww);
        ww = 0.5F / this.x;
        this.y = m1.m10 * ww;
        this.z = m1.m20 * ww;
        return;
      } 
    } else {
      this.x = 0.0F;
      this.y = 0.0F;
      this.z = 1.0F;
      return;
    } 
    this.x = 0.0F;
    ww = 0.5F * (1.0F - m1.m22);
    if (ww >= 1.0E-30D) {
      this.y = (float)Math.sqrt(ww);
      this.z = m1.m21 / 2.0F * this.y;
      return;
    } 
    this.y = 0.0F;
    this.z = 1.0F;
  }
  
  public final void set(Matrix3d m1) {
    double ww = 0.25D * (m1.m00 + m1.m11 + m1.m22 + 1.0D);
    if (ww >= 0.0D) {
      if (ww >= 1.0E-30D) {
        this.w = (float)Math.sqrt(ww);
        ww = 0.25D / this.w;
        this.x = (float)((m1.m21 - m1.m12) * ww);
        this.y = (float)((m1.m02 - m1.m20) * ww);
        this.z = (float)((m1.m10 - m1.m01) * ww);
        return;
      } 
    } else {
      this.w = 0.0F;
      this.x = 0.0F;
      this.y = 0.0F;
      this.z = 1.0F;
      return;
    } 
    this.w = 0.0F;
    ww = -0.5D * (m1.m11 + m1.m22);
    if (ww >= 0.0D) {
      if (ww >= 1.0E-30D) {
        this.x = (float)Math.sqrt(ww);
        ww = 0.5D / this.x;
        this.y = (float)(m1.m10 * ww);
        this.z = (float)(m1.m20 * ww);
        return;
      } 
    } else {
      this.x = 0.0F;
      this.y = 0.0F;
      this.z = 1.0F;
      return;
    } 
    this.x = 0.0F;
    ww = 0.5D * (1.0D - m1.m22);
    if (ww >= 1.0E-30D) {
      this.y = (float)Math.sqrt(ww);
      this.z = (float)(m1.m21 / 2.0D * this.y);
      return;
    } 
    this.y = 0.0F;
    this.z = 1.0F;
  }
  
  public final void set(AxisAngle4f a) {
    float amag = (float)Math.sqrt((a.x * a.x + a.y * a.y + a.z * a.z));
    if (amag < 1.0E-6D) {
      this.w = 0.0F;
      this.x = 0.0F;
      this.y = 0.0F;
      this.z = 0.0F;
    } else {
      amag = 1.0F / amag;
      float mag = (float)Math.sin(a.angle / 2.0D);
      this.w = (float)Math.cos(a.angle / 2.0D);
      this.x = a.x * amag * mag;
      this.y = a.y * amag * mag;
      this.z = a.z * amag * mag;
    } 
  }
  
  public final void set(AxisAngle4d a) {
    float amag = (float)(1.0D / Math.sqrt(a.x * a.x + a.y * a.y + a.z * a.z));
    if (amag < 1.0E-6D) {
      this.w = 0.0F;
      this.x = 0.0F;
      this.y = 0.0F;
      this.z = 0.0F;
    } else {
      amag = 1.0F / amag;
      float mag = (float)Math.sin(a.angle / 2.0D);
      this.w = (float)Math.cos(a.angle / 2.0D);
      this.x = (float)a.x * amag * mag;
      this.y = (float)a.y * amag * mag;
      this.z = (float)a.z * amag * mag;
    } 
  }
  
  public final void interpolate(Quat4f q1, float alpha) {
    double s1, s2, dot = (this.x * q1.x + this.y * q1.y + this.z * q1.z + this.w * q1.w);
    if (dot < 0.0D) {
      q1.x = -q1.x;
      q1.y = -q1.y;
      q1.z = -q1.z;
      q1.w = -q1.w;
      dot = -dot;
    } 
    if (1.0D - dot > 1.0E-6D) {
      double om = Math.acos(dot);
      double sinom = Math.sin(om);
      s1 = Math.sin((1.0D - alpha) * om) / sinom;
      s2 = Math.sin(alpha * om) / sinom;
    } else {
      s1 = 1.0D - alpha;
      s2 = alpha;
    } 
    this.w = (float)(s1 * this.w + s2 * q1.w);
    this.x = (float)(s1 * this.x + s2 * q1.x);
    this.y = (float)(s1 * this.y + s2 * q1.y);
    this.z = (float)(s1 * this.z + s2 * q1.z);
  }
  
  public final void interpolate(Quat4f q1, Quat4f q2, float alpha) {
    double s1, s2, dot = (q2.x * q1.x + q2.y * q1.y + q2.z * q1.z + q2.w * q1.w);
    if (dot < 0.0D) {
      q1.x = -q1.x;
      q1.y = -q1.y;
      q1.z = -q1.z;
      q1.w = -q1.w;
      dot = -dot;
    } 
    if (1.0D - dot > 1.0E-6D) {
      double om = Math.acos(dot);
      double sinom = Math.sin(om);
      s1 = Math.sin((1.0D - alpha) * om) / sinom;
      s2 = Math.sin(alpha * om) / sinom;
    } else {
      s1 = 1.0D - alpha;
      s2 = alpha;
    } 
    this.w = (float)(s1 * q1.w + s2 * q2.w);
    this.x = (float)(s1 * q1.x + s2 * q2.x);
    this.y = (float)(s1 * q1.y + s2 * q2.y);
    this.z = (float)(s1 * q1.z + s2 * q2.z);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\javax\vecmath\Quat4f.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */