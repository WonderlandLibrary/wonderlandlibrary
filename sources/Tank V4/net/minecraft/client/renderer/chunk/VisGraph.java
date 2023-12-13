package net.minecraft.client.renderer.chunk;

import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.EnumSet;
import java.util.Set;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import optifine.IntegerCache;

public class VisGraph {
   private final BitSet field_178612_d = new BitSet(4096);
   private int field_178611_f = 4096;
   private static final int field_178615_c = (int)Math.pow(16.0D, 2.0D);
   private static final int field_178614_b = (int)Math.pow(16.0D, 1.0D);
   private static final int[] field_178613_e = new int[1352];
   private static final String __OBFID = "CL_00002450";
   private static final int field_178616_a = (int)Math.pow(16.0D, 0.0D);

   public SetVisibility computeVisibility() {
      SetVisibility var1 = new SetVisibility();
      if (4096 - this.field_178611_f < 256) {
         var1.setAllVisible(true);
      } else if (this.field_178611_f == 0) {
         var1.setAllVisible(false);
      } else {
         int[] var5;
         int var4 = (var5 = field_178613_e).length;

         for(int var3 = 0; var3 < var4; ++var3) {
            int var2 = var5[var3];
            if (!this.field_178612_d.get(var2)) {
               var1.setManyVisible(this.func_178604_a(var2));
            }
         }
      }

      return var1;
   }

   static {
      boolean var0 = false;
      boolean var1 = true;
      int var2 = 0;

      for(int var3 = 0; var3 < 16; ++var3) {
         for(int var4 = 0; var4 < 16; ++var4) {
            for(int var5 = 0; var5 < 16; ++var5) {
               if (var3 == 0 || var3 == 15 || var4 == 0 || var4 == 15 || var5 == 0 || var5 == 15) {
                  field_178613_e[var2++] = getIndex(var3, var4, var5);
               }
            }
         }
      }

   }

   private static int getIndex(int var0, int var1, int var2) {
      return var0 << 0 | var1 << 8 | var2 << 4;
   }

   public void func_178606_a(BlockPos var1) {
      this.field_178612_d.set(getIndex(var1), true);
      --this.field_178611_f;
   }

   private void func_178610_a(int var1, Set var2) {
      int var3 = var1 >> 0 & 15;
      if (var3 == 0) {
         var2.add(EnumFacing.WEST);
      } else if (var3 == 15) {
         var2.add(EnumFacing.EAST);
      }

      int var4 = var1 >> 8 & 15;
      if (var4 == 0) {
         var2.add(EnumFacing.DOWN);
      } else if (var4 == 15) {
         var2.add(EnumFacing.UP);
      }

      int var5 = var1 >> 4 & 15;
      if (var5 == 0) {
         var2.add(EnumFacing.NORTH);
      } else if (var5 == 15) {
         var2.add(EnumFacing.SOUTH);
      }

   }

   private Set func_178604_a(int var1) {
      EnumSet var2 = EnumSet.noneOf(EnumFacing.class);
      ArrayDeque var3 = new ArrayDeque(384);
      var3.add(IntegerCache.valueOf(var1));
      this.field_178612_d.set(var1, true);

      while(!var3.isEmpty()) {
         int var4 = (Integer)var3.poll();
         this.func_178610_a(var4, var2);
         EnumFacing[] var8;
         int var7 = (var8 = EnumFacing.VALUES).length;

         for(int var6 = 0; var6 < var7; ++var6) {
            EnumFacing var5 = var8[var6];
            int var9 = this.func_178603_a(var4, var5);
            if (var9 >= 0 && !this.field_178612_d.get(var9)) {
               this.field_178612_d.set(var9, true);
               var3.add(IntegerCache.valueOf(var9));
            }
         }
      }

      return var2;
   }

   public Set func_178609_b(BlockPos var1) {
      return this.func_178604_a(getIndex(var1));
   }

   private int func_178603_a(int var1, EnumFacing var2) {
      switch(var2) {
      case DOWN:
         if ((var1 >> 8 & 15) == 0) {
            return -1;
         }

         return var1 - field_178615_c;
      case UP:
         if ((var1 >> 8 & 15) == 15) {
            return -1;
         }

         return var1 + field_178615_c;
      case NORTH:
         if ((var1 >> 4 & 15) == 0) {
            return -1;
         }

         return var1 - field_178614_b;
      case SOUTH:
         if ((var1 >> 4 & 15) == 15) {
            return -1;
         }

         return var1 + field_178614_b;
      case WEST:
         if ((var1 >> 0 & 15) == 0) {
            return -1;
         }

         return var1 - field_178616_a;
      case EAST:
         if ((var1 >> 0 & 15) == 15) {
            return -1;
         }

         return var1 + field_178616_a;
      default:
         return -1;
      }
   }

   private static int getIndex(BlockPos var0) {
      return getIndex(var0.getX() & 15, var0.getY() & 15, var0.getZ() & 15);
   }

   static final class VisGraph$1 {
      private static final String __OBFID = "CL_00002449";
      static final int[] field_178617_a = new int[EnumFacing.values().length];

      static {
         try {
            field_178617_a[EnumFacing.DOWN.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
         }

         try {
            field_178617_a[EnumFacing.UP.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
         }

         try {
            field_178617_a[EnumFacing.NORTH.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
         }

         try {
            field_178617_a[EnumFacing.SOUTH.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
         }

         try {
            field_178617_a[EnumFacing.WEST.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
         }

         try {
            field_178617_a[EnumFacing.EAST.ordinal()] = 6;
         } catch (NoSuchFieldError var1) {
         }

      }
   }
}
