package com.sun.javafx.webkit.prism;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.paint.Stop;
import com.sun.webkit.graphics.WCGradient;
import com.sun.webkit.graphics.WCPoint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

final class WCRadialGradient extends WCGradient {
   static final Comparator COMPARATOR = (var0, var1) -> {
      float var2 = var0.getOffset();
      float var3 = var1.getOffset();
      if (var2 < var3) {
         return -1;
      } else {
         return var2 > var3 ? 1 : 0;
      }
   };
   private final boolean reverse;
   private final WCPoint p1;
   private final WCPoint p2;
   private final float r1over;
   private final float r1;
   private final float r2;
   private final List stops = new ArrayList();

   WCRadialGradient(WCPoint var1, float var2, WCPoint var3, float var4) {
      this.reverse = var2 < var4;
      this.p1 = this.reverse ? var3 : var1;
      this.p2 = this.reverse ? var1 : var3;
      this.r1 = this.reverse ? var4 : var2;
      this.r2 = this.reverse ? var2 : var4;
      this.r1over = this.r1 > 0.0F ? 1.0F / this.r1 : 0.0F;
   }

   protected void addStop(Color var1, float var2) {
      if (this.reverse) {
         var2 = 1.0F - var2;
      }

      var2 = 1.0F - var2 + var2 * this.r2 * this.r1over;
      this.stops.add(new Stop(var1, var2));
   }

   public RadialGradient getPlatformGradient() {
      Collections.sort(this.stops, COMPARATOR);
      float var1 = this.p2.getX() - this.p1.getX();
      float var2 = this.p2.getY() - this.p1.getY();
      return new RadialGradient(this.p1.getX(), this.p1.getY(), (float)(Math.atan2((double)var2, (double)var1) * 180.0 / Math.PI), (float)Math.sqrt((double)(var1 * var1 + var2 * var2)) * this.r1over, this.r1, BaseTransform.IDENTITY_TRANSFORM, this.isProportional(), this.getSpreadMethod() - 1, this.stops);
   }

   public String toString() {
      return toString(this, this.p1, this.p2, this.r1, this.stops);
   }

   static String toString(WCGradient var0, WCPoint var1, WCPoint var2, Float var3, List var4) {
      StringBuilder var5 = new StringBuilder(var0.getClass().getSimpleName());
      switch (var0.getSpreadMethod()) {
         case 1:
            var5.append("[spreadMethod=PAD");
            break;
         case 2:
            var5.append("[spreadMethod=REFLECT");
            break;
         case 3:
            var5.append("[spreadMethod=REPEAT");
      }

      var5.append(", proportional=").append(var0.isProportional());
      if (var3 != null) {
         var5.append(", radius=").append(var3);
      }

      var5.append(", x1=").append(var1.getX());
      var5.append(", y1=").append(var1.getY());
      var5.append(", x2=").append(var2.getX());
      var5.append(", y2=").append(var2.getY());
      var5.append(", stops=");

      for(int var6 = 0; var6 < var4.size(); ++var6) {
         var5.append(var6 == 0 ? "[" : ", ");
         var5.append(((Stop)var4.get(var6)).getOffset()).append(":").append(((Stop)var4.get(var6)).getColor());
      }

      return var5.append("]]").toString();
   }
}
