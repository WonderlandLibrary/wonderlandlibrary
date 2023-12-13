package shadersmod.client;

import java.util.Iterator;
import java.util.NoSuchElementException;
import net.minecraft.util.BlockPos;
import optifine.BlockPosM;

public class IteratorAxis implements Iterator {
   private double yDelta;
   private double zDelta;
   private int xStart;
   private int xEnd;
   private double yStart;
   private double yEnd;
   private double zStart;
   private double zEnd;
   private int xNext;
   private double yNext;
   private double zNext;
   private BlockPosM pos = new BlockPosM(0, 0, 0);
   private boolean hasNext = false;

   public IteratorAxis(BlockPos var1, BlockPos var2, double var3, double var5) {
      this.yDelta = var3;
      this.zDelta = var5;
      this.xStart = var1.getX();
      this.xEnd = var2.getX();
      this.yStart = (double)var1.getY();
      this.yEnd = (double)var2.getY() - 0.5D;
      this.zStart = (double)var1.getZ();
      this.zEnd = (double)var2.getZ() - 0.5D;
      this.xNext = this.xStart;
      this.yNext = this.yStart;
      this.zNext = this.zStart;
      this.hasNext = this.xNext < this.xEnd && this.yNext < this.yEnd && this.zNext < this.zEnd;
   }

   public boolean hasNext() {
      return this.hasNext;
   }

   public BlockPos next() {
      if (!this.hasNext) {
         throw new NoSuchElementException();
      } else {
         this.pos.setXyz((double)this.xNext, this.yNext, this.zNext);
         this.nextPos();
         this.hasNext = this.xNext < this.xEnd && this.yNext < this.yEnd && this.zNext < this.zEnd;
         return this.pos;
      }
   }

   private void nextPos() {
      ++this.zNext;
      if (this.zNext >= this.zEnd) {
         this.zNext = this.zStart;
         ++this.yNext;
         if (this.yNext >= this.yEnd) {
            this.yNext = this.yStart;
            this.yStart += this.yDelta;
            this.yEnd += this.yDelta;
            this.yNext = this.yStart;
            this.zStart += this.zDelta;
            this.zEnd += this.zDelta;
            this.zNext = this.zStart;
            ++this.xNext;
            if (this.xNext >= this.xEnd) {
            }
         }
      }

   }

   public void remove() {
      throw new RuntimeException("Not implemented");
   }

   public static void main(String[] var0) throws Exception {
      BlockPos var1 = new BlockPos(-2, 10, 20);
      BlockPos var2 = new BlockPos(2, 12, 22);
      double var3 = -0.5D;
      double var5 = 0.5D;
      IteratorAxis var7 = new IteratorAxis(var1, var2, var3, var5);
      System.out.println("Start: " + var1 + ", end: " + var2 + ", yDelta: " + var3 + ", zDelta: " + var5);

      while(var7.hasNext()) {
         BlockPos var8 = var7.next();
         System.out.println("" + var8);
      }

   }

   public Object next() {
      return this.next();
   }
}
