package com.sun.javafx.font;

import com.sun.javafx.geom.transform.BaseTransform;

public interface PGFont {
   String getFullName();

   String getFamilyName();

   String getStyleName();

   String getName();

   float getSize();

   FontResource getFontResource();

   FontStrike getStrike(BaseTransform var1);

   FontStrike getStrike(BaseTransform var1, int var2);

   int getFeatures();
}
