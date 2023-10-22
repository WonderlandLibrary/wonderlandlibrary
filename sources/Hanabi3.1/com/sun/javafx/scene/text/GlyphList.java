package com.sun.javafx.scene.text;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;

public interface GlyphList {
   int getGlyphCount();

   int getGlyphCode(int var1);

   float getPosX(int var1);

   float getPosY(int var1);

   float getWidth();

   float getHeight();

   RectBounds getLineBounds();

   Point2D getLocation();

   int getCharOffset(int var1);

   boolean isComplex();

   TextSpan getTextSpan();
}
