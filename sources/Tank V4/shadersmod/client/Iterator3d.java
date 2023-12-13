package shadersmod.client;

import java.util.Iterator;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import optifine.BlockPosM;

public class Iterator3d implements Iterator {
   private IteratorAxis iteratorAxis;
   private BlockPosM blockPos = new BlockPosM(0, 0, 0);
   private int axis = 0;
   private int kX;
   private int kY;
   private int kZ;
   private static final int AXIS_X = 0;
   private static final int AXIS_Y = 1;
   private static final int AXIS_Z = 2;

   public Iterator3d(BlockPos var1, BlockPos var2, int var3, int var4) {
      boolean var5 = var1.getX() > var2.getX();
      boolean var6 = var1.getY() > var2.getY();
      boolean var7 = var1.getZ() > var2.getZ();
      var1 = this.reverseCoord(var1, var5, var6, var7);
      var2 = this.reverseCoord(var2, var5, var6, var7);
      this.kX = var5 ? -1 : 1;
      this.kY = var6 ? -1 : 1;
      this.kZ = var7 ? -1 : 1;
      Vec3 var8 = new Vec3((double)(var2.getX() - var1.getX()), (double)(var2.getY() - var1.getY()), (double)(var2.getZ() - var1.getZ()));
      Vec3 var9 = var8.normalize();
      Vec3 var10 = new Vec3(1.0D, 0.0D, 0.0D);
      double var11 = var9.dotProduct(var10);
      double var13 = Math.abs(var11);
      Vec3 var15 = new Vec3(0.0D, 1.0D, 0.0D);
      double var16 = var9.dotProduct(var15);
      double var18 = Math.abs(var16);
      Vec3 var20 = new Vec3(0.0D, 0.0D, 1.0D);
      double var21 = var9.dotProduct(var20);
      double var23 = Math.abs(var21);
      BlockPos var25;
      BlockPos var26;
      int var27;
      double var28;
      double var30;
      if (var23 >= var18 && var23 >= var13) {
         this.axis = 2;
         var25 = new BlockPos(var1.getZ(), var1.getY() - var3, var1.getX() - var4);
         var26 = new BlockPos(var2.getZ(), var1.getY() + var3 + 1, var1.getX() + var4 + 1);
         var27 = var2.getZ() - var1.getZ();
         var28 = (double)(var2.getY() - var1.getY()) / (1.0D * (double)var27);
         var30 = (double)(var2.getX() - var1.getX()) / (1.0D * (double)var27);
         this.iteratorAxis = new IteratorAxis(var25, var26, var28, var30);
      } else if (var18 >= var13 && var18 >= var23) {
         this.axis = 1;
         var25 = new BlockPos(var1.getY(), var1.getX() - var3, var1.getZ() - var4);
         var26 = new BlockPos(var2.getY(), var1.getX() + var3 + 1, var1.getZ() + var4 + 1);
         var27 = var2.getY() - var1.getY();
         var28 = (double)(var2.getX() - var1.getX()) / (1.0D * (double)var27);
         var30 = (double)(var2.getZ() - var1.getZ()) / (1.0D * (double)var27);
         this.iteratorAxis = new IteratorAxis(var25, var26, var28, var30);
      } else {
         this.axis = 0;
         var25 = new BlockPos(var1.getX(), var1.getY() - var3, var1.getZ() - var4);
         var26 = new BlockPos(var2.getX(), var1.getY() + var3 + 1, var1.getZ() + var4 + 1);
         var27 = var2.getX() - var1.getX();
         var28 = (double)(var2.getY() - var1.getY()) / (1.0D * (double)var27);
         var30 = (double)(var2.getZ() - var1.getZ()) / (1.0D * (double)var27);
         this.iteratorAxis = new IteratorAxis(var25, var26, var28, var30);
      }

   }

   private BlockPos reverseCoord(BlockPos var1, boolean var2, boolean var3, boolean var4) {
      if (var2) {
         var1 = new BlockPos(-var1.getX(), var1.getY(), var1.getZ());
      }

      if (var3) {
         var1 = new BlockPos(var1.getX(), -var1.getY(), var1.getZ());
      }

      if (var4) {
         var1 = new BlockPos(var1.getX(), var1.getY(), -var1.getZ());
      }

      return var1;
   }

   public boolean hasNext() {
      return this.iteratorAxis.hasNext();
   }

   public BlockPos next() {
      BlockPos var1 = this.iteratorAxis.next();
      switch(this.axis) {
      case 0:
         this.blockPos.setXyz(var1.getX() * this.kX, var1.getY() * this.kY, var1.getZ() * this.kZ);
         return this.blockPos;
      case 1:
         this.blockPos.setXyz(var1.getY() * this.kX, var1.getX() * this.kY, var1.getZ() * this.kZ);
         return this.blockPos;
      case 2:
         this.blockPos.setXyz(var1.getZ() * this.kX, var1.getY() * this.kY, var1.getX() * this.kZ);
         return this.blockPos;
      default:
         this.blockPos.setXyz(var1.getX() * this.kX, var1.getY() * this.kY, var1.getZ() * this.kZ);
         return this.blockPos;
      }
   }

   public void remove() {
      throw new RuntimeException("Not supported");
   }

   public static void main(String[] var0) {
      BlockPos var1 = new BlockPos(10, 20, 30);
      BlockPos var2 = new BlockPos(30, 40, 20);
      Iterator3d var3 = new Iterator3d(var1, var2, 1, 1);

      while(var3.hasNext()) {
         BlockPos var4 = var3.next();
         System.out.println("" + var4);
      }

   }

   public Object next() {
      return this.next();
   }
}
