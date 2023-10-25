package de.gerrygames.viarewind.utils.math;

import java.util.Objects;

public class Vector3d {
  double x;
  
  double y;
  
  double z;
  
  public Vector3d(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public Vector3d() {}
  
  public void setX(double x) {
    this.x = x;
  }
  
  public void setY(double y) {
    this.y = y;
  }
  
  public void setZ(double z) {
    this.z = z;
  }
  
  public double getX() {
    return this.x;
  }
  
  public double getY() {
    return this.y;
  }
  
  public double getZ() {
    return this.z;
  }
  
  public void set(Vector3d vec) {
    this.x = vec.x;
    this.y = vec.y;
    this.z = vec.z;
  }
  
  public Vector3d set(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
    return this;
  }
  
  public Vector3d multiply(double a) {
    this.x *= a;
    this.y *= a;
    this.z *= a;
    return this;
  }
  
  public Vector3d add(Vector3d vec) {
    this.x += vec.x;
    this.y += vec.y;
    this.z += vec.z;
    return this;
  }
  
  public Vector3d substract(Vector3d vec) {
    this.x -= vec.x;
    this.y -= vec.y;
    this.z -= vec.z;
    return this;
  }
  
  public double length() {
    return Math.sqrt(lengthSquared());
  }
  
  public double lengthSquared() {
    return this.x * this.x + this.y * this.y + this.z * this.z;
  }
  
  public Vector3d normalize() {
    double length = length();
    multiply(1.0D / length);
    return this;
  }
  
  public Vector3d clone() {
    return new Vector3d(this.x, this.y, this.z);
  }
  
  public boolean equals(Object o) {
    if (this == o)
      return true; 
    if (o == null || getClass() != o.getClass())
      return false; 
    Vector3d vector3d = (Vector3d)o;
    return (Double.compare(vector3d.x, this.x) == 0 && Double.compare(vector3d.y, this.y) == 0 && Double.compare(vector3d.z, this.z) == 0);
  }
  
  public int hashCode() {
    return Objects.hash(new Object[] { Double.valueOf(this.x), Double.valueOf(this.y), Double.valueOf(this.z) });
  }
  
  public String toString() {
    return "Vector3d{x=" + this.x + ", y=" + this.y + ", z=" + this.z + '}';
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewin\\utils\math\Vector3d.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */