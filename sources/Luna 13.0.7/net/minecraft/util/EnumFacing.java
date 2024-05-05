package net.minecraft.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public enum EnumFacing
  implements IStringSerializable
{
  private final int index;
  private final int opposite;
  private final int horizontalIndex;
  private final String name;
  private final Axis axis;
  private final AxisDirection axisDirection;
  private final Vec3i directionVec;
  public static final EnumFacing[] VALUES;
  private static final EnumFacing[] HORIZONTALS;
  private static final Map NAME_LOOKUP;
  private static final String __OBFID = "CL_00001201";
  private static final EnumFacing[] $VALUES;
  
  private EnumFacing(String p_i46016_1_, int p_i46016_2_, int indexIn, int oppositeIn, int horizontalIndexIn, String nameIn, AxisDirection axisDirectionIn, Axis axisIn, Vec3i directionVecIn)
  {
    this.index = indexIn;
    this.horizontalIndex = horizontalIndexIn;
    this.opposite = oppositeIn;
    this.name = nameIn;
    this.axis = axisIn;
    this.axisDirection = axisDirectionIn;
    this.directionVec = directionVecIn;
  }
  
  public int getIndex()
  {
    return this.index;
  }
  
  public int getHorizontalIndex()
  {
    return this.horizontalIndex;
  }
  
  public AxisDirection getAxisDirection()
  {
    return this.axisDirection;
  }
  
  public EnumFacing getOpposite()
  {
    return VALUES[this.opposite];
  }
  
  public EnumFacing rotateAround(Axis axis)
  {
    switch (SwitchPlane.AXIS_LOOKUP[axis.ordinal()])
    {
    case 1: 
      if ((this != WEST) && (this != EAST)) {
        return rotateX();
      }
      return this;
    case 2: 
      if ((this != UP) && (this != DOWN)) {
        return rotateY();
      }
      return this;
    case 3: 
      if ((this != NORTH) && (this != SOUTH)) {
        return rotateZ();
      }
      return this;
    }
    throw new IllegalStateException("Unable to get CW facing for axis " + axis);
  }
  
  public EnumFacing rotateY()
  {
    switch (SwitchPlane.FACING_LOOKUP[ordinal()])
    {
    case 1: 
      return EAST;
    case 2: 
      return SOUTH;
    case 3: 
      return WEST;
    case 4: 
      return NORTH;
    }
    throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
  }
  
  private EnumFacing rotateX()
  {
    switch (SwitchPlane.FACING_LOOKUP[ordinal()])
    {
    case 1: 
      return DOWN;
    case 2: 
    case 4: 
    default: 
      throw new IllegalStateException("Unable to get X-rotated facing of " + this);
    case 3: 
      return UP;
    case 5: 
      return NORTH;
    }
    return SOUTH;
  }
  
  private EnumFacing rotateZ()
  {
    switch (SwitchPlane.FACING_LOOKUP[ordinal()])
    {
    case 2: 
      return DOWN;
    case 3: 
    default: 
      throw new IllegalStateException("Unable to get Z-rotated facing of " + this);
    case 4: 
      return UP;
    case 5: 
      return EAST;
    }
    return WEST;
  }
  
  public EnumFacing rotateYCCW()
  {
    switch (SwitchPlane.FACING_LOOKUP[ordinal()])
    {
    case 1: 
      return WEST;
    case 2: 
      return NORTH;
    case 3: 
      return EAST;
    case 4: 
      return SOUTH;
    }
    throw new IllegalStateException("Unable to get CCW facing of " + this);
  }
  
  public int getFrontOffsetX()
  {
    return this.axis == Axis.X ? this.axisDirection.getOffset() : 0;
  }
  
  public int getFrontOffsetY()
  {
    return this.axis == Axis.Y ? this.axisDirection.getOffset() : 0;
  }
  
  public int getFrontOffsetZ()
  {
    return this.axis == Axis.Z ? this.axisDirection.getOffset() : 0;
  }
  
  public String getName2()
  {
    return this.name;
  }
  
  public Axis getAxis()
  {
    return this.axis;
  }
  
  public static EnumFacing byName(String name)
  {
    return name == null ? null : (EnumFacing)NAME_LOOKUP.get(name.toLowerCase());
  }
  
  public static EnumFacing getFront(int index)
  {
    return VALUES[MathHelper.abs_int(index % VALUES.length)];
  }
  
  public static EnumFacing getHorizontal(int p_176731_0_)
  {
    return HORIZONTALS[MathHelper.abs_int(p_176731_0_ % HORIZONTALS.length)];
  }
  
  public static EnumFacing fromAngle(double angle)
  {
    return getHorizontal(MathHelper.floor_double(angle / 90.0D + 0.5D) & 0x3);
  }
  
  public static EnumFacing random(Random rand)
  {
    return values()[rand.nextInt(values().length)];
  }
  
  public static EnumFacing func_176737_a(float p_176737_0_, float p_176737_1_, float p_176737_2_)
  {
    EnumFacing var3 = NORTH;
    float var4 = Float.MIN_VALUE;
    EnumFacing[] var5 = values();
    int var6 = var5.length;
    for (int var7 = 0; var7 < var6; var7++)
    {
      EnumFacing var8 = var5[var7];
      float var9 = p_176737_0_ * var8.directionVec.getX() + p_176737_1_ * var8.directionVec.getY() + p_176737_2_ * var8.directionVec.getZ();
      if (var9 > var4)
      {
        var4 = var9;
        var3 = var8;
      }
    }
    return var3;
  }
  
  public String toString()
  {
    return this.name;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public Vec3i getDirectionVec()
  {
    return this.directionVec;
  }
  
  static
  {
    VALUES = new EnumFacing[6];
    
    HORIZONTALS = new EnumFacing[4];
    NAME_LOOKUP = Maps.newHashMap();
    
    $VALUES = new EnumFacing[] { DOWN, UP, NORTH, SOUTH, WEST, EAST };
    
    EnumFacing[] var0 = values();
    int var1 = var0.length;
    for (int var2 = 0; var2 < var1; var2++)
    {
      EnumFacing var3 = var0[var2];
      VALUES[var3.index] = var3;
      if (var3.getAxis().isHorizontal()) {
        HORIZONTALS[var3.horizontalIndex] = var3;
      }
      NAME_LOOKUP.put(var3.getName2().toLowerCase(), var3);
    }
  }
  
  public static enum Axis
    implements Predicate, IStringSerializable
  {
    private static final Map NAME_LOOKUP;
    private final String name;
    private final EnumFacing.Plane plane;
    private static final Axis[] $VALUES;
    private static final String __OBFID = "CL_00002321";
    private static final Axis[] $VALUES$;
    
    private Axis(String p_i46390_1_, int p_i46390_2_, String p_i46015_1_, int p_i46015_2_, String name, EnumFacing.Plane plane)
    {
      this.name = name;
      this.plane = plane;
    }
    
    public static Axis byName(String name)
    {
      return name == null ? null : (Axis)NAME_LOOKUP.get(name.toLowerCase());
    }
    
    public String getName2()
    {
      return this.name;
    }
    
    public boolean isVertical()
    {
      return this.plane == EnumFacing.Plane.VERTICAL;
    }
    
    public boolean isHorizontal()
    {
      return this.plane == EnumFacing.Plane.HORIZONTAL;
    }
    
    public String toString()
    {
      return this.name;
    }
    
    public boolean apply(EnumFacing facing)
    {
      return (facing != null) && (facing.getAxis() == this);
    }
    
    public EnumFacing.Plane getPlane()
    {
      return this.plane;
    }
    
    public String getName()
    {
      return this.name;
    }
    
    public boolean apply(Object p_apply_1_)
    {
      return apply((EnumFacing)p_apply_1_);
    }
    
    static
    {
      NAME_LOOKUP = Maps.newHashMap();
      
      $VALUES = new Axis[] { X, Y, Z };
      
      $VALUES$ = new Axis[] { X, Y, Z };
      
      Axis[] var0 = values();
      int var1 = var0.length;
      for (int var2 = 0; var2 < var1; var2++)
      {
        Axis var3 = var0[var2];
        NAME_LOOKUP.put(var3.getName2().toLowerCase(), var3);
      }
    }
  }
  
  public static enum AxisDirection
  {
    private final int offset;
    private final String description;
    private static final AxisDirection[] $VALUES = { POSITIVE, NEGATIVE };
    private static final String __OBFID = "CL_00002320";
    
    private AxisDirection(String p_i46391_1_, int p_i46391_2_, String p_i46014_1_, int p_i46014_2_, int offset, String description)
    {
      this.offset = offset;
      this.description = description;
    }
    
    public int getOffset()
    {
      return this.offset;
    }
    
    public String toString()
    {
      return this.description;
    }
  }
  
  public static enum Plane
    implements Predicate, Iterable
  {
    private static final Plane[] $VALUES = { HORIZONTAL, VERTICAL };
    private static final String __OBFID = "CL_00002319";
    
    private Plane(String p_i46392_1_, int p_i46392_2_, String p_i46013_1_, int p_i46013_2_) {}
    
    public EnumFacing[] facings()
    {
      switch (EnumFacing.SwitchPlane.PLANE_LOOKUP[ordinal()])
      {
      case 1: 
        return new EnumFacing[] { EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST };
      case 2: 
        return new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN };
      }
      throw new Error("Someone's been tampering with the universe!");
    }
    
    public EnumFacing random(Random rand)
    {
      EnumFacing[] var2 = facings();
      return var2[rand.nextInt(var2.length)];
    }
    
    public boolean apply(EnumFacing facing)
    {
      return (facing != null) && (facing.getAxis().getPlane() == this);
    }
    
    public Iterator iterator()
    {
      return Iterators.forArray(facings());
    }
    
    public boolean apply(Object p_apply_1_)
    {
      return apply((EnumFacing)p_apply_1_);
    }
  }
  
  static final class SwitchPlane
  {
    static final int[] AXIS_LOOKUP;
    static final int[] FACING_LOOKUP;
    static final int[] PLANE_LOOKUP = new int[EnumFacing.Plane.values().length];
    private static final String __OBFID = "CL_00002322";
    
    SwitchPlane() {}
    
    static
    {
      try
      {
        PLANE_LOOKUP[EnumFacing.Plane.HORIZONTAL.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        PLANE_LOOKUP[EnumFacing.Plane.VERTICAL.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
      FACING_LOOKUP = new int[EnumFacing.values().length];
      try
      {
        FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError3) {}
      try
      {
        FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError4) {}
      try
      {
        FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError5) {}
      try
      {
        FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError6) {}
      try
      {
        FACING_LOOKUP[EnumFacing.UP.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError7) {}
      try
      {
        FACING_LOOKUP[EnumFacing.DOWN.ordinal()] = 6;
      }
      catch (NoSuchFieldError localNoSuchFieldError8) {}
      AXIS_LOOKUP = new int[EnumFacing.Axis.values().length];
      try
      {
        AXIS_LOOKUP[EnumFacing.Axis.X.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError9) {}
      try
      {
        AXIS_LOOKUP[EnumFacing.Axis.Y.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError10) {}
      try
      {
        AXIS_LOOKUP[EnumFacing.Axis.Z.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError11) {}
    }
  }
}
