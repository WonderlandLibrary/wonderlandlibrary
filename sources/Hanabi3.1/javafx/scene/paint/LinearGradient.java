package javafx.scene.paint;

import com.sun.javafx.scene.paint.GradientUtils;
import com.sun.javafx.tk.Toolkit;
import java.util.Iterator;
import java.util.List;
import javafx.beans.NamedArg;

public final class LinearGradient extends Paint {
   private double startX;
   private double startY;
   private double endX;
   private double endY;
   private boolean proportional;
   private CycleMethod cycleMethod;
   private List stops;
   private final boolean opaque;
   private Object platformPaint;
   private int hash;

   public final double getStartX() {
      return this.startX;
   }

   public final double getStartY() {
      return this.startY;
   }

   public final double getEndX() {
      return this.endX;
   }

   public final double getEndY() {
      return this.endY;
   }

   public final boolean isProportional() {
      return this.proportional;
   }

   public final CycleMethod getCycleMethod() {
      return this.cycleMethod;
   }

   public final List getStops() {
      return this.stops;
   }

   public final boolean isOpaque() {
      return this.opaque;
   }

   public LinearGradient(@NamedArg("startX") double var1, @NamedArg("startY") double var3, @NamedArg(value = "endX",defaultValue = "1") double var5, @NamedArg(value = "endY",defaultValue = "1") double var7, @NamedArg(value = "proportional",defaultValue = "true") boolean var9, @NamedArg("cycleMethod") CycleMethod var10, @NamedArg("stops") Stop... var11) {
      this.startX = var1;
      this.startY = var3;
      this.endX = var5;
      this.endY = var7;
      this.proportional = var9;
      this.cycleMethod = var10 == null ? CycleMethod.NO_CYCLE : var10;
      this.stops = Stop.normalize(var11);
      this.opaque = this.determineOpacity();
   }

   public LinearGradient(@NamedArg("startX") double var1, @NamedArg("startY") double var3, @NamedArg(value = "endX",defaultValue = "1") double var5, @NamedArg(value = "endY",defaultValue = "1") double var7, @NamedArg(value = "proportional",defaultValue = "true") boolean var9, @NamedArg("cycleMethod") CycleMethod var10, @NamedArg("stops") List var11) {
      this.startX = var1;
      this.startY = var3;
      this.endX = var5;
      this.endY = var7;
      this.proportional = var9;
      this.cycleMethod = var10 == null ? CycleMethod.NO_CYCLE : var10;
      this.stops = Stop.normalize(var11);
      this.opaque = this.determineOpacity();
   }

   private boolean determineOpacity() {
      int var1 = this.stops.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         if (!((Stop)this.stops.get(var2)).getColor().isOpaque()) {
            return false;
         }
      }

      return true;
   }

   Object acc_getPlatformPaint() {
      if (this.platformPaint == null) {
         this.platformPaint = Toolkit.getToolkit().getPaint((Paint)this);
      }

      return this.platformPaint;
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (var1 == this) {
         return true;
      } else if (var1 instanceof LinearGradient) {
         LinearGradient var2 = (LinearGradient)var1;
         if (this.startX == var2.startX && this.startY == var2.startY && this.endX == var2.endX && this.endY == var2.endY && this.proportional == var2.proportional && this.cycleMethod == var2.cycleMethod) {
            return this.stops.equals(var2.stops);
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      if (this.hash == 0) {
         long var1 = 17L;
         var1 = 37L * var1 + Double.doubleToLongBits(this.startX);
         var1 = 37L * var1 + Double.doubleToLongBits(this.startY);
         var1 = 37L * var1 + Double.doubleToLongBits(this.endX);
         var1 = 37L * var1 + Double.doubleToLongBits(this.endY);
         var1 = 37L * var1 + (this.proportional ? 1231L : 1237L);
         var1 = 37L * var1 + (long)this.cycleMethod.hashCode();

         Stop var4;
         for(Iterator var3 = this.stops.iterator(); var3.hasNext(); var1 = 37L * var1 + (long)var4.hashCode()) {
            var4 = (Stop)var3.next();
         }

         this.hash = (int)(var1 ^ var1 >> 32);
      }

      return this.hash;
   }

   public String toString() {
      StringBuilder var1 = (new StringBuilder("linear-gradient(from ")).append(GradientUtils.lengthToString(this.startX, this.proportional)).append(" ").append(GradientUtils.lengthToString(this.startY, this.proportional)).append(" to ").append(GradientUtils.lengthToString(this.endX, this.proportional)).append(" ").append(GradientUtils.lengthToString(this.endY, this.proportional)).append(", ");
      switch (this.cycleMethod) {
         case REFLECT:
            var1.append("reflect").append(", ");
            break;
         case REPEAT:
            var1.append("repeat").append(", ");
      }

      Iterator var2 = this.stops.iterator();

      while(var2.hasNext()) {
         Stop var3 = (Stop)var2.next();
         var1.append(var3).append(", ");
      }

      var1.delete(var1.length() - 2, var1.length());
      var1.append(")");
      return var1.toString();
   }

   public static LinearGradient valueOf(String var0) {
      if (var0 == null) {
         throw new NullPointerException("gradient must be specified");
      } else {
         String var1 = "linear-gradient(";
         String var2 = ")";
         if (var0.startsWith(var1)) {
            if (!var0.endsWith(var2)) {
               throw new IllegalArgumentException("Invalid gradient specification, must end with \"" + var2 + '"');
            }

            var0 = var0.substring(var1.length(), var0.length() - var2.length());
         }

         GradientUtils.Parser var3 = new GradientUtils.Parser(var0);
         if (var3.getSize() < 2) {
            throw new IllegalArgumentException("Invalid gradient specification");
         } else {
            GradientUtils.Point var4 = GradientUtils.Point.MIN;
            GradientUtils.Point var5 = GradientUtils.Point.MIN;
            GradientUtils.Point var6 = GradientUtils.Point.MIN;
            GradientUtils.Point var7 = GradientUtils.Point.MIN;
            String[] var8 = var3.splitCurrentToken();
            if ("from".equals(var8[0])) {
               GradientUtils.Parser.checkNumberOfArguments(var8, 5);
               var4 = var3.parsePoint(var8[1]);
               var5 = var3.parsePoint(var8[2]);
               if (!"to".equals(var8[3])) {
                  throw new IllegalArgumentException("Invalid gradient specification, \"to\" expected");
               }

               var6 = var3.parsePoint(var8[4]);
               var7 = var3.parsePoint(var8[5]);
               var3.shift();
            } else if ("to".equals(var8[0])) {
               int var9 = 0;
               int var10 = 0;

               for(int var11 = 1; var11 < 3 && var11 < var8.length; ++var11) {
                  if ("left".equals(var8[var11])) {
                     var4 = GradientUtils.Point.MAX;
                     var6 = GradientUtils.Point.MIN;
                     ++var9;
                  } else if ("right".equals(var8[var11])) {
                     var4 = GradientUtils.Point.MIN;
                     var6 = GradientUtils.Point.MAX;
                     ++var9;
                  } else if ("top".equals(var8[var11])) {
                     var5 = GradientUtils.Point.MAX;
                     var7 = GradientUtils.Point.MIN;
                     ++var10;
                  } else {
                     if (!"bottom".equals(var8[var11])) {
                        throw new IllegalArgumentException("Invalid gradient specification, unknown value after 'to'");
                     }

                     var5 = GradientUtils.Point.MIN;
                     var7 = GradientUtils.Point.MAX;
                     ++var10;
                  }
               }

               if (var10 > 1) {
                  throw new IllegalArgumentException("Invalid gradient specification, vertical direction set twice after 'to'");
               }

               if (var9 > 1) {
                  throw new IllegalArgumentException("Invalid gradient specification, horizontal direction set twice after 'to'");
               }

               var3.shift();
            } else {
               var5 = GradientUtils.Point.MIN;
               var7 = GradientUtils.Point.MAX;
            }

            CycleMethod var17 = CycleMethod.NO_CYCLE;
            String var18 = var3.getCurrentToken();
            if ("repeat".equals(var18)) {
               var17 = CycleMethod.REPEAT;
               var3.shift();
            } else if ("reflect".equals(var18)) {
               var17 = CycleMethod.REFLECT;
               var3.shift();
            }

            double var19 = 0.0;
            if (!var4.proportional) {
               double var13 = var6.value - var4.value;
               double var15 = var7.value - var5.value;
               var19 = Math.sqrt(var13 * var13 + var15 * var15);
            }

            Stop[] var20 = var3.parseStops(var4.proportional, var19);
            return new LinearGradient(var4.value, var5.value, var6.value, var7.value, var4.proportional, var17, var20);
         }
      }
   }
}
