package com.sun.javafx.scene.text;

import com.sun.javafx.geom.RectBounds;

public interface TextSpan {
   String getText();

   Object getFont();

   RectBounds getBounds();
}
