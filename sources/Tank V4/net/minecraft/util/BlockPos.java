package net.minecraft.util;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import net.minecraft.entity.Entity;

public class BlockPos extends Vec3i {
   private static final int X_SHIFT;
   private static final int NUM_Z_BITS;
   private static final long Y_MASK;
   private static final int NUM_X_BITS = 1 + MathHelper.calculateLogBaseTwo(MathHelper.roundUpToPowerOfTwo(30000000));
   private static final long Z_MASK;
   public static final BlockPos ORIGIN = new BlockPos(0, 0, 0);
   private static final int Y_SHIFT;
   private static final long X_MASK;
   private static final int NUM_Y_BITS;

   public BlockPos offset(EnumFacing var1, int var2) {
      return var2 == 0 ? this : new BlockPos(this.getX() + var1.getFrontOffsetX() * var2, this.getY() + var1.getFrontOffsetY() * var2, this.getZ() + var1.getFrontOffsetZ() * var2);
   }

   public BlockPos(Vec3 var1) {
      this(var1.xCoord, var1.yCoord, var1.zCoord);
   }

   public BlockPos west(int var1) {
      return this.offset(EnumFacing.WEST, var1);
   }

   public BlockPos add(double var1, double var3, double var5) {
      return var1 == 0.0D && var3 == 0.0D && var5 == 0.0D ? this : new BlockPos((double)this.getX() + var1, (double)this.getY() + var3, (double)this.getZ() + var5);
   }

   public BlockPos(double var1, double var3, double var5) {
      super(var1, var3, var5);
   }

   public BlockPos north(int var1) {
      return this.offset(EnumFacing.NORTH, var1);
   }

   public BlockPos down(int var1) {
      return this.offset(EnumFacing.DOWN, var1);
   }

   public BlockPos(Entity var1) {
      this(var1.posX, var1.posY, var1.posZ);
   }

   public Vec3i crossProduct(Vec3i var1) {
      return this.crossProduct(var1);
   }

   public BlockPos add(int var1, int var2, int var3) {
      return var1 == 0 && var2 == 0 && var3 == 0 ? this : new BlockPos(this.getX() + var1, this.getY() + var2, this.getZ() + var3);
   }

   public BlockPos east() {
      return this.east(1);
   }

   public BlockPos south() {
      return this.south(1);
   }

   public BlockPos subtract(Vec3i var1) {
      return var1.getX() == 0 && var1.getY() == 0 && var1.getZ() == 0 ? this : new BlockPos(this.getX() - var1.getX(), this.getY() - var1.getY(), this.getZ() - var1.getZ());
   }

   public BlockPos(int var1, int var2, int var3) {
      super(var1, var2, var3);
   }

   public BlockPos offset(EnumFacing var1) {
      return this.offset(var1, 1);
   }

   public static Iterable getAllInBoxMutable(BlockPos var0, BlockPos var1) {
      BlockPos var2 = new BlockPos(Math.min(var0.getX(), var1.getX()), Math.min(var0.getY(), var1.getY()), Math.min(var0.getZ(), var1.getZ()));
      BlockPos var3 = new BlockPos(Math.max(var0.getX(), var1.getX()), Math.max(var0.getY(), var1.getY()), Math.max(var0.getZ(), var1.getZ()));
      return new Iterable(var2, var3) {
         private final BlockPos val$blockpos1;
         private final BlockPos val$blockpos;

         public Iterator iterator() {
            return new AbstractIterator(this, this.val$blockpos, this.val$blockpos1) {
               private final BlockPos val$blockpos1;
               final <undefinedtype> this$1;
               private BlockPos.MutableBlockPos theBlockPos;
               private final BlockPos val$blockpos;

               {
                  this.this$1 = var1;
                  this.val$blockpos = var2;
                  this.val$blockpos1 = var3;
                  this.theBlockPos = null;
               }

               protected Object computeNext() {
                  return this.computeNext();
               }

               protected BlockPos.MutableBlockPos computeNext() {
                  if (this.theBlockPos == null) {
                     this.theBlockPos = new BlockPos.MutableBlockPos(this.val$blockpos.getX(), this.val$blockpos.getY(), this.val$blockpos.getZ());
                     return this.theBlockPos;
                  } else if (this.theBlockPos.equals(this.val$blockpos1)) {
                     return (BlockPos.MutableBlockPos)this.endOfData();
                  } else {
                     int var1 = this.theBlockPos.getX();
                     int var2 = this.theBlockPos.getY();
                     int var3 = this.theBlockPos.getZ();
                     if (var1 < this.val$blockpos1.getX()) {
                        ++var1;
                     } else if (var2 < this.val$blockpos1.getY()) {
                        var1 = this.val$blockpos.getX();
                        ++var2;
                     } else if (var3 < this.val$blockpos1.getZ()) {
                        var1 = this.val$blockpos.getX();
                        var2 = this.val$blockpos.getY();
                        ++var3;
                     }

                     BlockPos.MutableBlockPos.access$0(this.theBlockPos, var1);
                     BlockPos.MutableBlockPos.access$1(this.theBlockPos, var2);
                     BlockPos.MutableBlockPos.access$2(this.theBlockPos, var3);
                     return this.theBlockPos;
                  }
               }
            };
         }

         {
            this.val$blockpos = var1;
            this.val$blockpos1 = var2;
         }
      };
   }

   public BlockPos up() {
      return this.up(1);
   }

   public BlockPos south(int var1) {
      return this.offset(EnumFacing.SOUTH, var1);
   }

   public static BlockPos fromLong(long var0) {
      int var2 = (int)(var0 << 64 - X_SHIFT - NUM_X_BITS >> 64 - NUM_X_BITS);
      int var3 = (int)(var0 << 64 - Y_SHIFT - NUM_Y_BITS >> 64 - NUM_Y_BITS);
      int var4 = (int)(var0 << 64 - NUM_Z_BITS >> 64 - NUM_Z_BITS);
      return new BlockPos(var2, var3, var4);
   }

   public long toLong() {
      return ((long)this.getX() & X_MASK) << X_SHIFT | ((long)this.getY() & Y_MASK) << Y_SHIFT | ((long)this.getZ() & Z_MASK) << 0;
   }

   public static Iterable getAllInBox(BlockPos var0, BlockPos var1) {
      BlockPos var2 = new BlockPos(Math.min(var0.getX(), var1.getX()), Math.min(var0.getY(), var1.getY()), Math.min(var0.getZ(), var1.getZ()));
      BlockPos var3 = new BlockPos(Math.max(var0.getX(), var1.getX()), Math.max(var0.getY(), var1.getY()), Math.max(var0.getZ(), var1.getZ()));
      return new Iterable(var2, var3) {
         private final BlockPos val$blockpos1;
         private final BlockPos val$blockpos;

         {
            this.val$blockpos = var1;
            this.val$blockpos1 = var2;
         }

         public Iterator iterator() {
            return new AbstractIterator(this, this.val$blockpos, this.val$blockpos1) {
               private final BlockPos val$blockpos1;
               private BlockPos lastReturned;
               final <undefinedtype> this$1;
               private final BlockPos val$blockpos;

               {
                  this.this$1 = var1;
                  this.val$blockpos = var2;
                  this.val$blockpos1 = var3;
                  this.lastReturned = null;
               }

               protected Object computeNext() {
                  return this.computeNext();
               }

               protected BlockPos computeNext() {
                  if (this.lastReturned == null) {
                     this.lastReturned = this.val$blockpos;
                     return this.lastReturned;
                  } else if (this.lastReturned.equals(this.val$blockpos1)) {
                     return (BlockPos)this.endOfData();
                  } else {
                     int var1 = this.lastReturned.getX();
                     int var2 = this.lastReturned.getY();
                     int var3 = this.lastReturned.getZ();
                     if (var1 < this.val$blockpos1.getX()) {
                        ++var1;
                     } else if (var2 < this.val$blockpos1.getY()) {
                        var1 = this.val$blockpos.getX();
                        ++var2;
                     } else if (var3 < this.val$blockpos1.getZ()) {
                        var1 = this.val$blockpos.getX();
                        var2 = this.val$blockpos.getY();
                        ++var3;
                     }

                     this.lastReturned = new BlockPos(var1, var2, var3);
                     return this.lastReturned;
                  }
               }
            };
         }
      };
   }

   public BlockPos north() {
      return this.north(1);
   }

   public BlockPos down() {
      return this.down(1);
   }

   public BlockPos east(int var1) {
      return this.offset(EnumFacing.EAST, var1);
   }

   public BlockPos(Vec3i var1) {
      this(var1.getX(), var1.getY(), var1.getZ());
   }

   public BlockPos west() {
      return this.west(1);
   }

   public BlockPos add(Vec3i var1) {
      return var1.getX() == 0 && var1.getY() == 0 && var1.getZ() == 0 ? this : new BlockPos(this.getX() + var1.getX(), this.getY() + var1.getY(), this.getZ() + var1.getZ());
   }

   public BlockPos crossProduct(Vec3i var1) {
      return new BlockPos(this.getY() * var1.getZ() - this.getZ() * var1.getY(), this.getZ() * var1.getX() - this.getX() * var1.getZ(), this.getX() * var1.getY() - this.getY() * var1.getX());
   }

   public BlockPos up(int var1) {
      return this.offset(EnumFacing.UP, var1);
   }

   static {
      NUM_Z_BITS = NUM_X_BITS;
      NUM_Y_BITS = 64 - NUM_X_BITS - NUM_Z_BITS;
      Y_SHIFT = 0 + NUM_Z_BITS;
      X_SHIFT = Y_SHIFT + NUM_Y_BITS;
      X_MASK = (1L << NUM_X_BITS) - 1L;
      Y_MASK = (1L << NUM_Y_BITS) - 1L;
      Z_MASK = (1L << NUM_Z_BITS) - 1L;
   }

   public static final class MutableBlockPos extends BlockPos {
      private int z;
      private int x;
      private int y;

      public BlockPos.MutableBlockPos func_181079_c(int var1, int var2, int var3) {
         this.x = var1;
         this.y = var2;
         this.z = var3;
         return this;
      }

      static void access$2(BlockPos.MutableBlockPos var0, int var1) {
         var0.z = var1;
      }

      public MutableBlockPos(int var1, int var2, int var3) {
         super(0, 0, 0);
         this.x = var1;
         this.y = var2;
         this.z = var3;
      }

      static void access$0(BlockPos.MutableBlockPos var0, int var1) {
         var0.x = var1;
      }

      public int getZ() {
         return this.z;
      }

      static void access$1(BlockPos.MutableBlockPos var0, int var1) {
         var0.y = var1;
      }

      public int getY() {
         return this.y;
      }

      public int getX() {
         return this.x;
      }

      public MutableBlockPos() {
         this(0, 0, 0);
      }
   }
}
