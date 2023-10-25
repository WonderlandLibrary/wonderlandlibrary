package net.minecraft.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public enum EnumFacing implements IStringSerializable {
  DOWN("DOWN", 0, 0, 1, -1, "down", AxisDirection.NEGATIVE, Axis.Y, new Vec3i(0, -1, 0)),
  UP("UP", 1, 1, 0, -1, "up", AxisDirection.POSITIVE, Axis.Y, new Vec3i(0, 1, 0)),
  NORTH("NORTH", 2, 2, 3, 2, "north", AxisDirection.NEGATIVE, Axis.Z, new Vec3i(0, 0, -1)),
  SOUTH("SOUTH", 3, 3, 2, 0, "south", AxisDirection.POSITIVE, Axis.Z, new Vec3i(0, 0, 1)),
  WEST("WEST", 4, 4, 5, 1, "west", AxisDirection.NEGATIVE, Axis.X, new Vec3i(-1, 0, 0)),
  EAST("EAST", 5, 5, 4, 3, "east", AxisDirection.POSITIVE, Axis.X, new Vec3i(1, 0, 0));
  
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
  
  private static final EnumFacing[] $VALUES;
  
  private static final String __OBFID = "CL_00001201";
  
  static {
    VALUES = new EnumFacing[6];
    HORIZONTALS = new EnumFacing[4];
    NAME_LOOKUP = Maps.newHashMap();
    $VALUES = new EnumFacing[] { DOWN, UP, NORTH, SOUTH, WEST, EAST };
    for (EnumFacing enumfacing : values()) {
      VALUES[enumfacing.index] = enumfacing;
      if (enumfacing.getAxis().isHorizontal())
        HORIZONTALS[enumfacing.horizontalIndex] = enumfacing; 
      NAME_LOOKUP.put(enumfacing.getName2().toLowerCase(), enumfacing);
    } 
  }
  
  EnumFacing(String p_i17_3_, int p_i17_4_, int p_i17_5_, int p_i17_6_, int p_i17_7_, String p_i17_8_, AxisDirection p_i17_9_, Axis p_i17_10_, Vec3i p_i17_11_) {
    this.index = p_i17_5_;
    this.horizontalIndex = p_i17_7_;
    this.opposite = p_i17_6_;
    this.name = p_i17_8_;
    this.axis = p_i17_10_;
    this.axisDirection = p_i17_9_;
    this.directionVec = p_i17_11_;
  }
  
  public int getIndex() {
    return this.index;
  }
  
  public int getHorizontalIndex() {
    return this.horizontalIndex;
  }
  
  public AxisDirection getAxisDirection() {
    return this.axisDirection;
  }
  
  public EnumFacing getOpposite() {
    return VALUES[this.opposite];
  }
  
  public EnumFacing rotateAround(Axis axis) {
    switch (EnumFacing$1.field_179515_a[axis.ordinal()]) {
      case 1:
        if (this != WEST && this != EAST)
          return rotateX(); 
        return this;
      case 2:
        if (this != UP && this != DOWN)
          return rotateY(); 
        return this;
      case 3:
        if (this != NORTH && this != SOUTH)
          return rotateZ(); 
        return this;
    } 
    throw new IllegalStateException("Unable to get CW facing for axis " + axis);
  }
  
  public EnumFacing rotateY() {
    switch (EnumFacing$1.field_179513_b[ordinal()]) {
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
  
  private EnumFacing rotateX() {
    switch (EnumFacing$1.field_179513_b[ordinal()]) {
      case 1:
        return DOWN;
      default:
        throw new IllegalStateException("Unable to get X-rotated facing of " + this);
      case 3:
        return UP;
      case 5:
        return NORTH;
      case 6:
        break;
    } 
    return SOUTH;
  }
  
  private EnumFacing rotateZ() {
    switch (EnumFacing$1.field_179513_b[ordinal()]) {
      case 2:
        return DOWN;
      default:
        throw new IllegalStateException("Unable to get Z-rotated facing of " + this);
      case 4:
        return UP;
      case 5:
        return EAST;
      case 6:
        break;
    } 
    return WEST;
  }
  
  public EnumFacing rotateYCCW() {
    switch (EnumFacing$1.field_179513_b[ordinal()]) {
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
  
  public int getFrontOffsetX() {
    return (this.axis == Axis.X) ? this.axisDirection.getOffset() : 0;
  }
  
  public int getFrontOffsetY() {
    return (this.axis == Axis.Y) ? this.axisDirection.getOffset() : 0;
  }
  
  public int getFrontOffsetZ() {
    return (this.axis == Axis.Z) ? this.axisDirection.getOffset() : 0;
  }
  
  public String getName2() {
    return this.name;
  }
  
  public Axis getAxis() {
    return this.axis;
  }
  
  public static EnumFacing byName(String name) {
    return (name == null) ? null : (EnumFacing)NAME_LOOKUP.get(name.toLowerCase());
  }
  
  public static EnumFacing getFront(int index) {
    return VALUES[MathHelper.abs_int(index % VALUES.length)];
  }
  
  public static EnumFacing getHorizontal(int p_176731_0_) {
    return HORIZONTALS[MathHelper.abs_int(p_176731_0_ % HORIZONTALS.length)];
  }
  
  public static EnumFacing fromAngle(double angle) {
    return getHorizontal(MathHelper.floor_double(angle / 90.0D + 0.5D) & 0x3);
  }
  
  public static EnumFacing random(Random rand) {
    return values()[rand.nextInt((values()).length)];
  }
  
  public static EnumFacing getFacingFromVector(float p_176737_0_, float p_176737_1_, float p_176737_2_) {
    EnumFacing enumfacing = NORTH;
    float f = Float.MIN_VALUE;
    for (EnumFacing enumfacing1 : values()) {
      float f1 = p_176737_0_ * enumfacing1.directionVec.getX() + p_176737_1_ * enumfacing1.directionVec.getY() + p_176737_2_ * enumfacing1.directionVec.getZ();
      if (f1 > f) {
        f = f1;
        enumfacing = enumfacing1;
      } 
    } 
    return enumfacing;
  }
  
  public String toString() {
    return this.name;
  }
  
  public String getName() {
    return this.name;
  }
  
  public static EnumFacing func_181076_a(AxisDirection p_181076_0_, Axis p_181076_1_) {
    for (EnumFacing enumfacing : values()) {
      if (enumfacing.getAxisDirection() == p_181076_0_ && enumfacing.getAxis() == p_181076_1_)
        return enumfacing; 
    } 
    throw new IllegalArgumentException("No such direction: " + p_181076_0_ + " " + p_181076_1_);
  }
  
  public Vec3i getDirectionVec() {
    return this.directionVec;
  }
  
  static final class EnumFacing$1 {
    static final int[] field_179515_a;
    
    static final int[] field_179513_b = new int[(EnumFacing.values()).length];
    
    static final int[] field_179514_c = new int[(EnumFacing.Plane.values()).length];
    
    private static final String __OBFID = "CL_00002322";
    
    static {
      try {
        field_179513_b[EnumFacing.NORTH.ordinal()] = 1;
      } catch (NoSuchFieldError noSuchFieldError) {}
      try {
        field_179513_b[EnumFacing.EAST.ordinal()] = 2;
      } catch (NoSuchFieldError noSuchFieldError) {}
      try {
        field_179513_b[EnumFacing.SOUTH.ordinal()] = 3;
      } catch (NoSuchFieldError noSuchFieldError) {}
      try {
        field_179513_b[EnumFacing.WEST.ordinal()] = 4;
      } catch (NoSuchFieldError noSuchFieldError) {}
      try {
        field_179513_b[EnumFacing.UP.ordinal()] = 5;
      } catch (NoSuchFieldError noSuchFieldError) {}
      try {
        field_179513_b[EnumFacing.DOWN.ordinal()] = 6;
      } catch (NoSuchFieldError noSuchFieldError) {}
      field_179515_a = new int[(EnumFacing.Axis.values()).length];
      try {
        field_179515_a[EnumFacing.Axis.X.ordinal()] = 1;
      } catch (NoSuchFieldError noSuchFieldError) {}
      try {
        field_179515_a[EnumFacing.Axis.Y.ordinal()] = 2;
      } catch (NoSuchFieldError noSuchFieldError) {}
      try {
        field_179515_a[EnumFacing.Axis.Z.ordinal()] = 3;
      } catch (NoSuchFieldError noSuchFieldError) {}
    }
  }
  
  public enum Axis implements Predicate, IStringSerializable {
    X("X", 0, "x", EnumFacing.Plane.HORIZONTAL),
    Y("Y", 1, "y", EnumFacing.Plane.VERTICAL),
    Z("Z", 2, "z", EnumFacing.Plane.HORIZONTAL);
    
    private static final Map NAME_LOOKUP = Maps.newHashMap();
    
    private final String name;
    
    private final EnumFacing.Plane plane;
    
    private static final Axis[] $VALUES = new Axis[] { X, Y, Z };
    
    private static final String __OBFID = "CL_00002321";
    
    static {
      for (Axis enumfacing$axis : values())
        NAME_LOOKUP.put(enumfacing$axis.getName2().toLowerCase(), enumfacing$axis); 
    }
    
    Axis(String p_i14_3_, int p_i14_4_, String p_i14_5_, EnumFacing.Plane p_i14_6_) {
      this.name = p_i14_5_;
      this.plane = p_i14_6_;
    }
    
    public static Axis byName(String name) {
      return (name == null) ? null : (Axis)NAME_LOOKUP.get(name.toLowerCase());
    }
    
    public String getName2() {
      return this.name;
    }
    
    public boolean isVertical() {
      return (this.plane == EnumFacing.Plane.VERTICAL);
    }
    
    public boolean isHorizontal() {
      return (this.plane == EnumFacing.Plane.HORIZONTAL);
    }
    
    public String toString() {
      return this.name;
    }
    
    public boolean apply(EnumFacing p_apply_1_) {
      return (p_apply_1_ != null && p_apply_1_.getAxis() == this);
    }
    
    public EnumFacing.Plane getPlane() {
      return this.plane;
    }
    
    public String getName() {
      return this.name;
    }
    
    public boolean apply(Object p_apply_1_) {
      return apply((EnumFacing)p_apply_1_);
    }
  }
  
  public enum AxisDirection {
    POSITIVE("POSITIVE", 0, 1, "Towards positive"),
    NEGATIVE("NEGATIVE", 1, -1, "Towards negative");
    
    private final int offset;
    
    private final String description;
    
    private static final AxisDirection[] $VALUES = new AxisDirection[] { POSITIVE, NEGATIVE };
    
    private static final String __OBFID = "CL_00002320";
    
    static {
    
    }
    
    AxisDirection(String p_i15_3_, int p_i15_4_, int p_i15_5_, String p_i15_6_) {
      this.offset = p_i15_5_;
      this.description = p_i15_6_;
    }
    
    public int getOffset() {
      return this.offset;
    }
    
    public String toString() {
      return this.description;
    }
  }
  
  public enum Plane implements Predicate, Iterable {
    HORIZONTAL("HORIZONTAL", 0),
    VERTICAL("VERTICAL", 1);
    
    private static final Plane[] $VALUES = new Plane[] { HORIZONTAL, VERTICAL };
    
    private static final String __OBFID = "CL_00002319";
    
    static {
    
    }
    
    public EnumFacing[] facings() {
      switch (EnumFacing.EnumFacing$1.field_179514_c[ordinal()]) {
        case 1:
          return new EnumFacing[] { EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST };
        case 2:
          return new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN };
      } 
      throw new Error("Someone's been tampering with the universe!");
    }
    
    public EnumFacing random(Random rand) {
      EnumFacing[] aenumfacing = facings();
      return aenumfacing[rand.nextInt(aenumfacing.length)];
    }
    
    public boolean apply(EnumFacing p_apply_1_) {
      return (p_apply_1_ != null && p_apply_1_.getAxis().getPlane() == this);
    }
    
    public Iterator iterator() {
      return (Iterator)Iterators.forArray((Object[])facings());
    }
    
    public boolean apply(Object p_apply_1_) {
      return apply((EnumFacing)p_apply_1_);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraf\\util\EnumFacing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */