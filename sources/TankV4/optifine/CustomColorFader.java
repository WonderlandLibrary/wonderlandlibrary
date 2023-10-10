package optifine;

import net.minecraft.util.Vec3;

public class CustomColorFader {
   private Vec3 color = null;
   private long timeUpdate = System.currentTimeMillis();

   public Vec3 getColor(double var1, double var3, double var5) {
      if (this.color == null) {
         this.color = new Vec3(var1, var3, var5);
         return this.color;
      } else {
         long var7 = System.currentTimeMillis();
         long var9 = var7 - this.timeUpdate;
         if (var9 == 0L) {
            return this.color;
         } else {
            this.timeUpdate = var7;
            if (Math.abs(var1 - this.color.xCoord) < 0.004D && Math.abs(var3 - this.color.yCoord) < 0.004D && Math.abs(var5 - this.color.zCoord) < 0.004D) {
               return this.color;
            } else {
               double var11 = (double)var9 * 0.001D;
               var11 = Config.limit(var11, 0.0D, 1.0D);
               double var13 = var1 - this.color.xCoord;
               double var15 = var3 - this.color.yCoord;
               double var17 = var5 - this.color.zCoord;
               double var19 = this.color.xCoord + var13 * var11;
               double var21 = this.color.yCoord + var15 * var11;
               double var23 = this.color.zCoord + var17 * var11;
               this.color = new Vec3(var19, var21, var23);
               return this.color;
            }
         }
      }
   }
}
