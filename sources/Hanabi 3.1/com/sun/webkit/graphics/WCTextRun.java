package com.sun.webkit.graphics;

public interface WCTextRun {
   boolean isLeftToRight();

   float[] getGlyphPosAndAdvance(int var1);

   int getCharOffset(int var1);

   int getEnd();

   int getGlyph(int var1);

   int getGlyphCount();

   int getStart();
}
