package com.sun.javafx.font;

import com.sun.javafx.geom.transform.BaseTransform;
import java.util.Map;

public interface FontResource {
   int AA_GREYSCALE = 0;
   int AA_LCD = 1;
   int KERN = 1;
   int CLIG = 2;
   int DLIG = 4;
   int HLIG = 8;
   int LIGA = 16;
   int RLIG = 32;
   int LIGATURES = 62;
   int SMCP = 64;
   int FRAC = 128;
   int AFRC = 256;
   int ZERO = 512;
   int SWSH = 1024;
   int CSWH = 2048;
   int SALT = 4096;
   int NALT = 8192;
   int RUBY = 16384;
   int SS01 = 32768;
   int SS02 = 65536;
   int SS03 = 131072;
   int SS04 = 262144;
   int SS05 = 524288;
   int SS06 = 1048576;
   int SS07 = 2097152;

   String getFullName();

   String getPSName();

   String getFamilyName();

   String getFileName();

   String getStyleName();

   String getLocaleFullName();

   String getLocaleFamilyName();

   String getLocaleStyleName();

   int getFeatures();

   boolean isBold();

   boolean isItalic();

   float getAdvance(int var1, float var2);

   float[] getGlyphBoundingBox(int var1, float var2, float[] var3);

   int getDefaultAAMode();

   CharToGlyphMapper getGlyphMapper();

   Map getStrikeMap();

   FontStrike getStrike(float var1, BaseTransform var2);

   FontStrike getStrike(float var1, BaseTransform var2, int var3);

   Object getPeer();

   void setPeer(Object var1);

   boolean isEmbeddedFont();
}
