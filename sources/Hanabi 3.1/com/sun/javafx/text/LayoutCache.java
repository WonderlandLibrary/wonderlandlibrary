package com.sun.javafx.text;

import com.sun.javafx.font.PGFont;

class LayoutCache {
   int[] glyphs;
   float[] advances;
   boolean valid;
   int analysis;
   char[] text;
   PGFont font;
   TextRun[] runs;
   int runCount;
   TextLine[] lines;
   float layoutWidth;
   float layoutHeight;
}
