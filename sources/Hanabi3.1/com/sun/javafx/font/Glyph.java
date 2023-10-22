package com.sun.javafx.font;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;

public interface Glyph {
   int getGlyphCode();

   RectBounds getBBox();

   float getAdvance();

   Shape getShape();

   byte[] getPixelData();

   byte[] getPixelData(int var1);

   float getPixelXAdvance();

   float getPixelYAdvance();

   boolean isLCDGlyph();

   int getWidth();

   int getHeight();

   int getOriginX();

   int getOriginY();
}
