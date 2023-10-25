package me.rhys.base.util.vec;

public class Vec4f {
  public float x;
  
  public float y;
  
  public float z;
  
  public float w;
  
  public Vec4f(float x, float y, float z, float w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }
  
  public Vec4f() {
    this(0.0F, 0.0F, 0.0F, 0.0F);
  }
  
  public Vec4f sub(Vec4f vec) {
    this.x -= vec.x;
    this.y -= vec.y;
    this.z -= vec.z;
    this.w -= vec.w;
    return this;
  }
  
  public Vec4f add(Vec4f vec) {
    this.x += vec.x;
    this.y += vec.y;
    this.z += vec.z;
    this.w += vec.w;
    return this;
  }
  
  public Vec4f mul(Vec4f vec) {
    this.x *= vec.x;
    this.y *= vec.y;
    this.z *= vec.z;
    this.w *= vec.w;
    return this;
  }
  
  public Vec4f mul(float scale) {
    return mul(new Vec4f(scale, scale, scale, scale));
  }
  
  public Vec4f clone() {
    return new Vec4f(this.x, this.y, this.z, this.w);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\util\vec\Vec4f.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */