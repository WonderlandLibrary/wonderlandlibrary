package com.sun.javafx.webkit.prism;

import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.text.TextRun;
import com.sun.webkit.graphics.WCTextRun;

public final class WCTextRunImpl implements WCTextRun {
   private final TextRun run;
   private static float[] POS_AND_ADVANCE = new float[4];

   public WCTextRunImpl(GlyphList var1) {
      this.run = (TextRun)var1;
   }

   public int getGlyphCount() {
      return this.run.getGlyphCount();
   }

   public boolean isLeftToRight() {
      return this.run.isLeftToRight();
   }

   public int getGlyph(int var1) {
      return var1 < this.run.getGlyphCount() ? this.run.getGlyphCode(var1) : 0;
   }

   public float[] getGlyphPosAndAdvance(int var1) {
      POS_AND_ADVANCE[0] = this.run.getPosX(var1);
      POS_AND_ADVANCE[1] = this.run.getPosY(var1);
      POS_AND_ADVANCE[2] = this.run.getAdvance(var1);
      POS_AND_ADVANCE[3] = 0.0F;
      return POS_AND_ADVANCE;
   }

   public int getStart() {
      return this.run.getStart();
   }

   public int getEnd() {
      return this.run.getEnd();
   }

   public int getCharOffset(int var1) {
      return this.run.getCharOffset(var1);
   }
}
