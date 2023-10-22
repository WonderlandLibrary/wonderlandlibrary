package com.sun.javafx.scene.text;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Shape;
import javafx.scene.shape.PathElement;

public interface TextLayout {
   int FLAGS_LINES_VALID = 1;
   int FLAGS_ANALYSIS_VALID = 2;
   int FLAGS_HAS_TABS = 4;
   int FLAGS_HAS_BIDI = 8;
   int FLAGS_HAS_COMPLEX = 16;
   int FLAGS_HAS_EMBEDDED = 32;
   int FLAGS_HAS_CJK = 64;
   int FLAGS_WRAPPED = 128;
   int FLAGS_RTL_BASE = 256;
   int FLAGS_CACHED_UNDERLINE = 512;
   int FLAGS_CACHED_STRIKETHROUGH = 1024;
   int FLAGS_LAST = 2048;
   int ANALYSIS_MASK = 2047;
   int ALIGN_LEFT = 262144;
   int ALIGN_CENTER = 524288;
   int ALIGN_RIGHT = 1048576;
   int ALIGN_JUSTIFY = 2097152;
   int ALIGN_MASK = 3932160;
   int DIRECTION_LTR = 1024;
   int DIRECTION_RTL = 2048;
   int DIRECTION_DEFAULT_LTR = 4096;
   int DIRECTION_DEFAULT_RTL = 8192;
   int DIRECTION_MASK = 15360;
   int BOUNDS_CENTER = 16384;
   int BOUNDS_MASK = 16384;
   int TYPE_TEXT = 1;
   int TYPE_UNDERLINE = 2;
   int TYPE_STRIKETHROUGH = 4;
   int TYPE_BASELINE = 8;
   int TYPE_TOP = 16;
   int TYPE_BEARINGS = 32;

   boolean setContent(TextSpan[] var1);

   boolean setContent(String var1, Object var2);

   boolean setAlignment(int var1);

   boolean setWrapWidth(float var1);

   boolean setLineSpacing(float var1);

   boolean setDirection(int var1);

   boolean setBoundsType(int var1);

   BaseBounds getBounds();

   BaseBounds getBounds(TextSpan var1, BaseBounds var2);

   BaseBounds getVisualBounds(int var1);

   TextLine[] getLines();

   GlyphList[] getRuns();

   Shape getShape(int var1, TextSpan var2);

   HitInfo getHitInfo(float var1, float var2);

   PathElement[] getCaretShape(int var1, boolean var2, float var3, float var4);

   PathElement[] getRange(int var1, int var2, int var3, float var4, float var5);
}
