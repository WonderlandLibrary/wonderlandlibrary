package me.rhys.base.util.vec;

import javax.vecmath.Vector2f;

public class Vec2f extends Vector2f {
  public float x;
  
  public float y;
  
  public Vec2f(float x, float y) {
    this.x = x;
    this.y = y;
  }
  
  public Vec2f() {
    this(0.0F, 0.0F);
  }
  
  public Vec2f add(double x, double y) {
    this.x = (float)(this.x + x);
    this.y = (float)(this.y + y);
    return this;
  }
  
  public Vec2f add(float x, float y) {
    this.x += x;
    this.y += y;
    return this;
  }
  
  public Vec2f add(int x, int y) {
    this.x += x;
    this.y += y;
    return this;
  }
  
  public Vec2f add(Vec2f vec) {
    this.x += vec.x;
    this.y += vec.y;
    return this;
  }
  
  public Vec2f sub(float x, float y) {
    this.x -= x;
    this.y -= y;
    return this;
  }
  
  public Vec2f div(float x, float y) {
    this.x /= x;
    this.y /= y;
    return this;
  }
  
  public Vec2f mul(float x, float y) {
    this.x *= x;
    this.y *= y;
    return this;
  }
  
  public Vec2f clone() {
    return new Vec2f(this.x, this.y);
  }
  
  public Vec2f subtract(Vec2f vec) {
    this.x -= vec.x;
    this.y -= vec.y;
    return this;
  }
  
  public Vec2f multiply(float number) {
    this.x *= this.x;
    this.y *= this.y;
    return this;
  }
  
  public Vec2f multiply(Vec2f vec) {
    this.x *= vec.x;
    this.y *= vec.y;
    return this;
  }
  
  public Vec2f multiply(float x, float y) {
    this.x *= x;
    this.y *= y;
    return this;
  }
  
  public Vec2f set(Vec2f vec) {
    this.x = vec.x;
    this.y = vec.y;
    return this;
  }
  
  public void setVecX(float f) {
    this.x = f;
  }
  
  public void setVecY(float f) {
    this.y = f;
  }
  
  public float getVecX() {
    return this.x;
  }
  
  public float getVecY() {
    return this.y;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\util\vec\Vec2f.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */