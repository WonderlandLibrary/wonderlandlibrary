package net.minecraft.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public enum EnumFacing implements IStringSerializable {
   private final int horizontalIndex;
   SOUTH("SOUTH", 3, 3, 2, 0, "south", EnumFacing.AxisDirection.POSITIVE, EnumFacing.Axis.Z, new Vec3i(0, 0, 1));

   private final EnumFacing.Axis axis;
   public static final EnumFacing[] VALUES = new EnumFacing[6];
   private final Vec3i directionVec;
   DOWN("DOWN", 0, 0, 1, -1, "down", EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.Y, new Vec3i(0, -1, 0));

   private final EnumFacing.AxisDirection axisDirection;
   WEST("WEST", 4, 4, 5, 1, "west", EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.X, new Vec3i(-1, 0, 0)),
   EAST("EAST", 5, 5, 4, 3, "east", EnumFacing.AxisDirection.POSITIVE, EnumFacing.Axis.X, new Vec3i(1, 0, 0)),
   NORTH("NORTH", 2, 2, 3, 2, "north", EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.Z, new Vec3i(0, 0, -1));

   private static final EnumFacing[] $VALUES = new EnumFacing[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};
   private static final String __OBFID = "CL_00001201";
   private static final EnumFacing[] HORIZONTALS = new EnumFacing[4];
   private final String name;
   private static final EnumFacing[] ENUM$VALUES = new EnumFacing[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};
   private final int opposite;
   private final int index;
   UP("UP", 1, 1, 0, -1, "up", EnumFacing.AxisDirection.POSITIVE, EnumFacing.Axis.Y, new Vec3i(0, 1, 0));

   private static final Map NAME_LOOKUP = Maps.newHashMap();

   public static EnumFacing random(Random var0) {
      return values()[var0.nextInt(values().length)];
   }

   public EnumFacing getOpposite() {
      return VALUES[this.opposite];
   }

   public static EnumFacing byName(String var0) {
      return var0 == null ? null : (EnumFacing)NAME_LOOKUP.get(var0.toLowerCase());
   }

   public EnumFacing rotateAround(EnumFacing.Axis var1) {
      switch(var1) {
      case X:
         if (this != WEST && this != EAST) {
            return this.rotateX();
         }

         return this;
      case Y:
         if (this != UP && this != DOWN) {
            return this.rotateY();
         }

         return this;
      case Z:
         if (this != NORTH && this != SOUTH) {
            return this.rotateZ();
         }

         return this;
      default:
         throw new IllegalStateException("Unable to get CW facing for axis " + var1);
      }
   }

   public EnumFacing rotateYCCW() {
      switch(this) {
      case NORTH:
         return WEST;
      case EAST:
         return NORTH;
      case SOUTH:
         return EAST;
      case WEST:
         return SOUTH;
      default:
         throw new IllegalStateException("Unable to get CCW facing of " + this);
      }
   }

   public EnumFacing.AxisDirection getAxisDirection() {
      return this.axisDirection;
   }

   public EnumFacing rotateY() {
      switch(this) {
      case NORTH:
         return EAST;
      case EAST:
         return SOUTH;
      case SOUTH:
         return WEST;
      case WEST:
         return NORTH;
      default:
         throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
      }
   }

   static {
      EnumFacing[] var3;
      int var2 = (var3 = values()).length;

      for(int var1 = 0; var1 < var2; ++var1) {
         EnumFacing var0 = var3[var1];
         VALUES[var0.index] = var0;
         if (var0.getAxis().isHorizontal()) {
            HORIZONTALS[var0.horizontalIndex] = var0;
         }

         NAME_LOOKUP.put(var0.getName2().toLowerCase(), var0);
      }

   }

   public static EnumFacing getFront(int var0) {
      return VALUES[MathHelper.abs_int(var0 % VALUES.length)];
   }

   public String getName2() {
      return this.name;
   }

   public EnumFacing.Axis getAxis() {
      return this.axis;
   }

   public static EnumFacing fromAngle(double var0) {
      return getHorizontal(MathHelper.floor_double(var0 / 90.0D + 0.5D) & 3);
   }

   public String getName() {
      return this.name;
   }

   private EnumFacing rotateX() {
      switch(this) {
      case NORTH:
         return DOWN;
      case EAST:
      case WEST:
      default:
         throw new IllegalStateException("Unable to get X-rotated facing of " + this);
      case SOUTH:
         return UP;
      case UP:
         return NORTH;
      case DOWN:
         return SOUTH;
      }
   }

   public int getFrontOffsetZ() {
      return this.axis == EnumFacing.Axis.Z ? this.axisDirection.getOffset() : 0;
   }

   public static EnumFacing getFacingFromVector(float var0, float var1, float var2) {
      EnumFacing var3 = NORTH;
      float var4 = Float.MIN_VALUE;
      EnumFacing[] var8;
      int var7 = (var8 = values()).length;

      for(int var6 = 0; var6 < var7; ++var6) {
         EnumFacing var5 = var8[var6];
         float var9 = var0 * (float)var5.directionVec.getX() + var1 * (float)var5.directionVec.getY() + var2 * (float)var5.directionVec.getZ();
         if (var9 > var4) {
            var4 = var9;
            var3 = var5;
         }
      }

      return var3;
   }

   private EnumFacing rotateZ() {
      switch(this) {
      case EAST:
         return DOWN;
      case SOUTH:
      default:
         throw new IllegalStateException("Unable to get Z-rotated facing of " + this);
      case WEST:
         return UP;
      case UP:
         return EAST;
      case DOWN:
         return WEST;
      }
   }

   public int getFrontOffsetY() {
      return this.axis == EnumFacing.Axis.Y ? this.axisDirection.getOffset() : 0;
   }

   public int getFrontOffsetX() {
      return this.axis == EnumFacing.Axis.X ? this.axisDirection.getOffset() : 0;
   }

   public int getIndex() {
      return this.index;
   }

   public Vec3i getDirectionVec() {
      return this.directionVec;
   }

   public String toString() {
      return this.name;
   }

   public int getHorizontalIndex() {
      return this.horizontalIndex;
   }

   private EnumFacing(String var3, int var4, int var5, int var6, int var7, String var8, EnumFacing.AxisDirection var9, EnumFacing.Axis var10, Vec3i var11) {
      this.index = var5;
      this.horizontalIndex = var7;
      this.opposite = var6;
      this.name = var8;
      this.axis = var10;
      this.axisDirection = var9;
      this.directionVec = var11;
   }

   public static EnumFacing func_181076_a(EnumFacing.AxisDirection var0, EnumFacing.Axis var1) {
      EnumFacing[] var5;
      int var4 = (var5 = values()).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         EnumFacing var2 = var5[var3];
         if (var2.getAxisDirection() == var0 && var2.getAxis() == var1) {
            return var2;
         }
      }

      throw new IllegalArgumentException("No such direction: " + var0 + " " + var1);
   }

   public static EnumFacing getHorizontal(int var0) {
      return HORIZONTALS[MathHelper.abs_int(var0 % HORIZONTALS.length)];
   }

   static final class EnumFacing$1 {
      static final int[] field_179513_b;
      static final int[] field_179514_c = new int[EnumFacing.Plane.values().length];
      private static final String __OBFID = "CL_00002322";
      static final int[] field_179515_a;

      static {
         try {
            field_179514_c[EnumFacing.Plane.HORIZONTAL.ordinal()] = 1;
         } catch (NoSuchFieldError var12) {
         }

         try {
            field_179514_c[EnumFacing.Plane.VERTICAL.ordinal()] = 2;
         } catch (NoSuchFieldError var11) {
         }

         field_179513_b = new int[EnumFacing.values().length];

         try {
            field_179513_b[EnumFacing.NORTH.ordinal()] = 1;
         } catch (NoSuchFieldError var10) {
         }

         try {
            field_179513_b[EnumFacing.EAST.ordinal()] = 2;
         } catch (NoSuchFieldError var9) {
         }

         try {
            field_179513_b[EnumFacing.SOUTH.ordinal()] = 3;
         } catch (NoSuchFieldError var8) {
         }

         try {
            field_179513_b[EnumFacing.WEST.ordinal()] = 4;
         } catch (NoSuchFieldError var7) {
         }

         try {
            field_179513_b[EnumFacing.UP.ordinal()] = 5;
         } catch (NoSuchFieldError var6) {
         }

         try {
            field_179513_b[EnumFacing.DOWN.ordinal()] = 6;
         } catch (NoSuchFieldError var5) {
         }

         field_179515_a = new int[EnumFacing.Axis.values().length];

         try {
            field_179515_a[EnumFacing.Axis.X.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
         }

         try {
            field_179515_a[EnumFacing.Axis.Y.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
         }

         try {
            field_179515_a[EnumFacing.Axis.Z.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
         }

      }
   }

   public static enum AxisDirection {
      POSITIVE("POSITIVE", 0, 1, "Towards positive");

      private final int offset;
      private static final EnumFacing.AxisDirection[] $VALUES = new EnumFacing.AxisDirection[]{POSITIVE, NEGATIVE};
      private static final String __OBFID = "CL_00002320";
      private static final EnumFacing.AxisDirection[] ENUM$VALUES = new EnumFacing.AxisDirection[]{POSITIVE, NEGATIVE};
      NEGATIVE("NEGATIVE", 1, -1, "Towards negative");

      private final String description;

      public String toString() {
         return this.description;
      }

      private AxisDirection(String var3, int var4, int var5, String var6) {
         this.offset = var5;
         this.description = var6;
      }

      public int getOffset() {
         return this.offset;
      }
   }

   public static enum Plane implements Predicate, Iterable {
      private static final String __OBFID = "CL_00002319";
      private static final EnumFacing.Plane[] ENUM$VALUES = new EnumFacing.Plane[]{HORIZONTAL, VERTICAL};
      HORIZONTAL("HORIZONTAL", 0),
      VERTICAL("VERTICAL", 1);

      private static final EnumFacing.Plane[] $VALUES = new EnumFacing.Plane[]{HORIZONTAL, VERTICAL};

      public EnumFacing random(Random var1) {
         EnumFacing[] var2 = this.facings();
         return var2[var1.nextInt(var2.length)];
      }

      public Iterator iterator() {
         return Iterators.forArray(this.facings());
      }

      private Plane(String var3, int var4) {
      }

      public boolean apply(Object var1) {
         return this.apply((EnumFacing)var1);
      }

      public EnumFacing[] facings() {
         switch(EnumFacing.EnumFacing$1.field_179514_c[this.ordinal()]) {
         case 1:
            return new EnumFacing[]{EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST};
         case 2:
            return new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN};
         default:
            throw new Error("Someone's been tampering with the universe!");
         }
      }

      public boolean apply(EnumFacing var1) {
         return var1 != null && var1.getAxis().getPlane() == this;
      }
   }

   public static enum Axis implements IStringSerializable, Predicate {
      private final EnumFacing.Plane plane;
      private static final EnumFacing.Axis[] $VALUES = new EnumFacing.Axis[]{X, Y, Z};
      private static final EnumFacing.Axis[] ENUM$VALUES = new EnumFacing.Axis[]{X, Y, Z};
      private static final String __OBFID = "CL_00002321";
      Y("Y", 1, "y", EnumFacing.Plane.VERTICAL),
      Z("Z", 2, "z", EnumFacing.Plane.HORIZONTAL);

      private static final Map NAME_LOOKUP = Maps.newHashMap();
      private final String name;
      X("X", 0, "x", EnumFacing.Plane.HORIZONTAL);

      public boolean apply(Object var1) {
         return this.apply((EnumFacing)var1);
      }

      public static EnumFacing.Axis byName(String var0) {
         return var0 == null ? null : (EnumFacing.Axis)NAME_LOOKUP.get(var0.toLowerCase());
      }

      public String getName2() {
         return this.name;
      }

      static {
         EnumFacing.Axis[] var3;
         int var2 = (var3 = values()).length;

         for(int var1 = 0; var1 < var2; ++var1) {
            EnumFacing.Axis var0 = var3[var1];
            NAME_LOOKUP.put(var0.getName2().toLowerCase(), var0);
         }

      }

      private Axis(String var3, int var4, String var5, EnumFacing.Plane var6) {
         this.name = var5;
         this.plane = var6;
      }

      public String getName() {
         return this.name;
      }

      public boolean isHorizontal() {
         return this.plane == EnumFacing.Plane.HORIZONTAL;
      }

      public boolean isVertical() {
         return this.plane == EnumFacing.Plane.VERTICAL;
      }

      public boolean apply(EnumFacing var1) {
         return var1 != null && var1.getAxis() == this;
      }

      public String toString() {
         return this.name;
      }

      public EnumFacing.Plane getPlane() {
         return this.plane;
      }
   }
}
