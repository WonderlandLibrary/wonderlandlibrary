package javafx.scene.paint;

import com.sun.javafx.scene.paint.GradientUtils;
import com.sun.javafx.tk.Toolkit;
import java.util.Iterator;
import java.util.List;
import javafx.beans.NamedArg;

public final class RadialGradient extends Paint {
   private double focusAngle;
   private double focusDistance;
   private double centerX;
   private double centerY;
   private double radius;
   private boolean proportional;
   private CycleMethod cycleMethod;
   private List stops;
   private final boolean opaque;
   private Object platformPaint;
   private int hash;

   public final double getFocusAngle() {
      return this.focusAngle;
   }

   public final double getFocusDistance() {
      return this.focusDistance;
   }

   public final double getCenterX() {
      return this.centerX;
   }

   public final double getCenterY() {
      return this.centerY;
   }

   public final double getRadius() {
      return this.radius;
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

   public RadialGradient(@NamedArg("focusAngle") double var1, @NamedArg("focusDistance") double var3, @NamedArg("centerX") double var5, @NamedArg("centerY") double var7, @NamedArg(value = "radius",defaultValue = "1") double var9, @NamedArg(value = "proportional",defaultValue = "true") boolean var11, @NamedArg("cycleMethod") CycleMethod var12, @NamedArg("stops") Stop... var13) {
      this.focusAngle = var1;
      this.focusDistance = var3;
      this.centerX = var5;
      this.centerY = var7;
      this.radius = var9;
      this.proportional = var11;
      this.cycleMethod = var12 == null ? CycleMethod.NO_CYCLE : var12;
      this.stops = Stop.normalize(var13);
      this.opaque = this.determineOpacity();
   }

   public RadialGradient(@NamedArg("focusAngle") double var1, @NamedArg("focusDistance") double var3, @NamedArg("centerX") double var5, @NamedArg("centerY") double var7, @NamedArg(value = "radius",defaultValue = "1") double var9, @NamedArg(value = "proportional",defaultValue = "true") boolean var11, @NamedArg("cycleMethod") CycleMethod var12, @NamedArg("stops") List var13) {
      this.focusAngle = var1;
      this.focusDistance = var3;
      this.centerX = var5;
      this.centerY = var7;
      this.radius = var9;
      this.proportional = var11;
      this.cycleMethod = var12 == null ? CycleMethod.NO_CYCLE : var12;
      this.stops = Stop.normalize(var13);
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
      if (var1 == this) {
         return true;
      } else if (var1 instanceof RadialGradient) {
         RadialGradient var2 = (RadialGradient)var1;
         if (this.focusAngle == var2.focusAngle && this.focusDistance == var2.focusDistance && this.centerX == var2.centerX && this.centerY == var2.centerY && this.radius == var2.radius && this.proportional == var2.proportional && this.cycleMethod == var2.cycleMethod) {
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
         var1 = 37L * var1 + Double.doubleToLongBits(this.focusAngle);
         var1 = 37L * var1 + Double.doubleToLongBits(this.focusDistance);
         var1 = 37L * var1 + Double.doubleToLongBits(this.centerX);
         var1 = 37L * var1 + Double.doubleToLongBits(this.centerY);
         var1 = 37L * var1 + Double.doubleToLongBits(this.radius);
         var1 = 37L * var1 + (long)(this.proportional ? 1231 : 1237);
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
      StringBuilder var1 = (new StringBuilder("radial-gradient(focus-angle ")).append(this.focusAngle).append("deg, focus-distance ").append(this.focusDistance * 100.0).append("% , center ").append(GradientUtils.lengthToString(this.centerX, this.proportional)).append(" ").append(GradientUtils.lengthToString(this.centerY, this.proportional)).append(", radius ").append(GradientUtils.lengthToString(this.radius, this.proportional)).append(", ");
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

   public static RadialGradient valueOf(String var0) {
      if (var0 == null) {
         throw new NullPointerException("gradient must be specified");
      } else {
         String var1 = "radial-gradient(";
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
            double var4 = 0.0;
            double var6 = 0.0;
            String[] var11 = var3.splitCurrentToken();
            if ("focus-angle".equals(var11[0])) {
               GradientUtils.Parser.checkNumberOfArguments(var11, 1);
               var4 = GradientUtils.Parser.parseAngle(var11[1]);
               var3.shift();
            }

            var11 = var3.splitCurrentToken();
            if ("focus-distance".equals(var11[0])) {
               GradientUtils.Parser.checkNumberOfArguments(var11, 1);
               var6 = GradientUtils.Parser.parsePercentage(var11[1]);
               var3.shift();
            }

            var11 = var3.splitCurrentToken();
            GradientUtils.Point var8;
            GradientUtils.Point var9;
            if ("center".equals(var11[0])) {
               GradientUtils.Parser.checkNumberOfArguments(var11, 2);
               var8 = var3.parsePoint(var11[1]);
               var9 = var3.parsePoint(var11[2]);
               var3.shift();
            } else {
               var8 = GradientUtils.Point.MIN;
               var9 = GradientUtils.Point.MIN;
            }

            var11 = var3.splitCurrentToken();
            if ("radius".equals(var11[0])) {
               GradientUtils.Parser.checkNumberOfArguments(var11, 1);
               GradientUtils.Point var10 = var3.parsePoint(var11[1]);
               var3.shift();
               CycleMethod var12 = CycleMethod.NO_CYCLE;
               String var13 = var3.getCurrentToken();
               if ("repeat".equals(var13)) {
                  var12 = CycleMethod.REPEAT;
                  var3.shift();
               } else if ("reflect".equals(var13)) {
                  var12 = CycleMethod.REFLECT;
                  var3.shift();
               }

               Stop[] var14 = var3.parseStops(var10.proportional, var10.value);
               return new RadialGradient(var4, var6, var8.value, var9.value, var10.value, var10.proportional, var12, var14);
            } else {
               throw new IllegalArgumentException("Invalid gradient specification: radius must be specified");
            }
         }
      }
   }
}
