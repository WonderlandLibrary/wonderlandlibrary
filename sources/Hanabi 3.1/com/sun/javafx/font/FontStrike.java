package com.sun.javafx.font;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.text.GlyphList;

public interface FontStrike {
   FontResource getFontResource();

   float getSize();

   BaseTransform getTransform();

   boolean drawAsShapes();

   int getQuantizedPosition(Point2D var1);

   Metrics getMetrics();

   Glyph getGlyph(char var1);

   Glyph getGlyph(int var1);

   void clearDesc();

   int getAAMode();

   float getCharAdvance(char var1);

   Shape getOutline(GlyphList var1, BaseTransform var2);
}
