package com.sun.javafx.webkit.prism;

import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.prism.paint.Paint;
import com.sun.webkit.graphics.WCStroke;

final class WCStrokeImpl extends WCStroke {
   private BasicStroke stroke;

   public WCStrokeImpl() {
   }

   public WCStrokeImpl(float var1, int var2, int var3, float var4, float[] var5, float var6) {
      this.setThickness(var1);
      this.setLineCap(var2);
      this.setLineJoin(var3);
      this.setMiterLimit(var4);
      this.setDashSizes(var5);
      this.setDashOffset(var6);
   }

   protected void invalidate() {
      this.stroke = null;
   }

   public BasicStroke getPlatformStroke() {
      if (this.stroke == null) {
         int var1 = this.getStyle();
         if (var1 != 0) {
            float var2 = this.getThickness();
            float[] var3 = this.getDashSizes();
            if (var3 == null) {
               switch (var1) {
                  case 2:
                     var3 = new float[]{var2, var2};
                     break;
                  case 3:
                     var3 = new float[]{3.0F * var2, 3.0F * var2};
               }
            }

            this.stroke = new BasicStroke(var2, this.getLineCap(), this.getLineJoin(), this.getMiterLimit(), var3, this.getDashOffset());
         }
      }

      return this.stroke;
   }

   boolean isApplicable() {
      return this.getPaint() != null && this.getPlatformStroke() != null;
   }

   boolean apply(Graphics var1) {
      if (this.isApplicable()) {
         Paint var2 = (Paint)this.getPaint();
         BasicStroke var3 = this.getPlatformStroke();
         var1.setPaint(var2);
         var1.setStroke(var3);
         return true;
      } else {
         return false;
      }
   }
}
