package com.sun.javafx.scene.text;

import com.sun.javafx.geom.RectBounds;

public interface TextLine {
   GlyphList[] getRuns();

   RectBounds getBounds();

   float getLeftSideBearing();

   float getRightSideBearing();

   int getStart();

   int getLength();
}
