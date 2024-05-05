package net.minecraft.util;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

public class Vec3i
  implements Comparable
{
  public static final Vec3i NULL_VECTOR = new Vec3i(0, 0, 0);
  private final int x;
  private final int y;
  private final int z;
  private static final String __OBFID = "CL_00002315";
  
  public Vec3i(int p_i46007_1_, int p_i46007_2_, int p_i46007_3_)
  {
    this.x = p_i46007_1_;
    this.y = p_i46007_2_;
    this.z = p_i46007_3_;
  }
  
  public Vec3i(double p_i46008_1_, double p_i46008_3_, double p_i46008_5_)
  {
    this(MathHelper.floor_double(p_i46008_1_), MathHelper.floor_double(p_i46008_3_), MathHelper.floor_double(p_i46008_5_));
  }
  
  public boolean equals(Object p_equals_1_)
  {
    if (this == p_equals_1_) {
      return true;
    }
    if (!(p_equals_1_ instanceof Vec3i)) {
      return false;
    }
    Vec3i var2 = (Vec3i)p_equals_1_;
    return getX() == var2.getX();
  }
  
  public int hashCode()
  {
    return (getY() + getZ() * 31) * 31 + getX();
  }
  
  public int compareTo(Vec3i p_177953_1_)
  {
    return getY() == p_177953_1_.getY() ? getZ() - p_177953_1_.getZ() : getZ() == p_177953_1_.getZ() ? getX() - p_177953_1_.getX() : getY() - p_177953_1_.getY();
  }
  
  public int getX()
  {
    return this.x;
  }
  
  public int getY()
  {
    return this.y;
  }
  
  public int getZ()
  {
    return this.z;
  }
  
  public Vec3i crossProduct(Vec3i vec)
  {
    return new Vec3i(getY() * vec.getZ() - getZ() * vec.getY(), getZ() * vec.getX() - getX() * vec.getZ(), getX() * vec.getY() - getY() * vec.getX());
  }
  
  public double distanceSq(double toX, double toY, double toZ)
  {
    double var7 = getX() - toX;
    double var9 = getY() - toY;
    double var11 = getZ() - toZ;
    return var7 * var7 + var9 * var9 + var11 * var11;
  }
  
  public double distanceSqToCenter(double p_177957_1_, double p_177957_3_, double p_177957_5_)
  {
    double var7 = getX() + 0.5D - p_177957_1_;
    double var9 = getY() + 0.5D - p_177957_3_;
    double var11 = getZ() + 0.5D - p_177957_5_;
    return var7 * var7 + var9 * var9 + var11 * var11;
  }
  
  public double distanceSq(Vec3i to)
  {
    return distanceSq(to.getX(), to.getY(), to.getZ());
  }
  
  public String toString()
  {
    return Objects.toStringHelper(this).add("x", getX()).add("y", getY()).add("z", getZ()).toString();
  }
  
  public int compareTo(Object p_compareTo_1_)
  {
    return compareTo((Vec3i)p_compareTo_1_);
  }
}
