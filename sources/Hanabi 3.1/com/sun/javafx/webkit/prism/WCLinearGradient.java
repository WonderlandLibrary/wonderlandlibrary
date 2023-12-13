package com.sun.javafx.webkit.prism;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.Stop;
import com.sun.webkit.graphics.WCGradient;
import com.sun.webkit.graphics.WCPoint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class WCLinearGradient extends WCGradient {
   private final WCPoint p1;
   private final WCPoint p2;
   private final List stops = new ArrayList();

   WCLinearGradient(WCPoint var1, WCPoint var2) {
      this.p1 = var1;
      this.p2 = var2;
   }

   protected void addStop(Color var1, float var2) {
      this.stops.add(new Stop(var1, var2));
   }

   public LinearGradient getPlatformGradient() {
      Collections.sort(this.stops, WCRadialGradient.COMPARATOR);
      return new LinearGradient(this.p1.getX(), this.p1.getY(), this.p2.getX(), this.p2.getY(), BaseTransform.IDENTITY_TRANSFORM, this.isProportional(), this.getSpreadMethod() - 1, this.stops);
   }

   public String toString() {
      return WCRadialGradient.toString(this, this.p1, this.p2, (Float)null, this.stops);
   }
}
