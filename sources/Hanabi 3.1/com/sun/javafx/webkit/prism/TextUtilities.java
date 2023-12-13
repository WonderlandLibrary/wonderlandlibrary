package com.sun.javafx.webkit.prism;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.scene.text.TextSpan;
import com.sun.javafx.text.PrismTextLayoutFactory;
import com.sun.javafx.text.TextRun;

final class TextUtilities {
   static TextLayout createLayout(String var0, Object var1) {
      TextLayout var2 = PrismTextLayoutFactory.getFactory().createLayout();
      var2.setContent(var0, var1);
      return var2;
   }

   static BaseBounds getLayoutBounds(String var0, Object var1) {
      return createLayout(var0, var1).getBounds();
   }

   static float getLayoutWidth(String var0, Object var1) {
      return getLayoutBounds(var0, var1).getWidth();
   }

   static TextRun createGlyphList(int[] var0, float[] var1, float var2, float var3) {
      TextRun var4 = new TextRun(0, var0.length, 0, true, 0, (TextSpan)null, 0, false) {
         public RectBounds getLineBounds() {
            return new RectBounds();
         }
      };
      var4.shape(var0.length, var0, var1);
      var4.setLocation(var2, var3);
      return var4;
   }
}
