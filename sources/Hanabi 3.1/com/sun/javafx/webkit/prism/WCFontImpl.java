package com.sun.javafx.webkit.prism;

import com.sun.javafx.font.CharToGlyphMapper;
import com.sun.javafx.font.FontFactory;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.text.TextLayout;
import com.sun.prism.GraphicsPipeline;
import com.sun.webkit.graphics.WCFont;
import com.sun.webkit.graphics.WCTextRun;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

final class WCFontImpl extends WCFont {
   private static final Logger log = Logger.getLogger(WCFontImpl.class.getName());
   private static final HashMap FONT_MAP = new HashMap();
   private final PGFont font;
   private FontStrike strike;

   static WCFont getFont(String var0, boolean var1, boolean var2, float var3) {
      FontFactory var4 = GraphicsPipeline.getPipeline().getFontFactory();
      synchronized(FONT_MAP) {
         if (FONT_MAP.isEmpty()) {
            FONT_MAP.put("serif", "Serif");
            FONT_MAP.put("dialog", "SansSerif");
            FONT_MAP.put("helvetica", "SansSerif");
            FONT_MAP.put("sansserif", "SansSerif");
            FONT_MAP.put("sans-serif", "SansSerif");
            FONT_MAP.put("monospace", "Monospaced");
            FONT_MAP.put("monospaced", "Monospaced");
            FONT_MAP.put("times", "Times New Roman");
            FONT_MAP.put("courier", "Courier New");
            String[] var6 = var4.getFontFamilyNames();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               String var9 = var6[var8];
               FONT_MAP.put(var9.toLowerCase(), var9);
            }
         }
      }

      String var5 = (String)FONT_MAP.get(var0.toLowerCase());
      if (log.isLoggable(Level.FINE)) {
         StringBuilder var12 = new StringBuilder("WCFontImpl.get(");
         var12.append(var0).append(", ").append(var3);
         if (var1) {
            var12.append(", bold");
         }

         if (var2) {
            var12.append(", italic");
         }

         log.fine(var12.append(") = ").append(var5).toString());
      }

      return var5 != null ? new WCFontImpl(var4.createFont(var5, var1, var2, var3)) : null;
   }

   WCFontImpl(PGFont var1) {
      this.font = var1;
   }

   public WCFont deriveFont(float var1) {
      FontFactory var2 = GraphicsPipeline.getPipeline().getFontFactory();
      return new WCFontImpl(var2.deriveFont(this.font, this.font.getFontResource().isBold(), this.font.getFontResource().isItalic(), var1));
   }

   private FontStrike getFontStrike() {
      if (this.strike == null) {
         this.strike = this.font.getStrike(BaseTransform.IDENTITY_TRANSFORM, 1);
      }

      return this.strike;
   }

   public double getGlyphWidth(int var1) {
      return (double)this.getFontStrike().getFontResource().getAdvance(var1, this.font.getSize());
   }

   public float[] getGlyphBoundingBox(int var1) {
      float[] var2 = new float[4];
      var2 = this.getFontStrike().getFontResource().getGlyphBoundingBox(var1, this.font.getSize(), var2);
      return new float[]{var2[0], -var2[3], var2[2], var2[3] - var2[1]};
   }

   public float getXHeight() {
      return this.getFontStrike().getMetrics().getXHeight();
   }

   private static boolean needsTextLayout(int[] var0) {
      int[] var1 = var0;
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         int var4 = var1[var3];
         if (var4 == 0) {
            return true;
         }
      }

      return false;
   }

   public int[] getGlyphCodes(char[] var1) {
      int[] var2 = new int[var1.length];
      CharToGlyphMapper var3 = this.getFontStrike().getFontResource().getGlyphMapper();
      var3.charsToGlyphs(var1.length, var1, var2);
      if (needsTextLayout(var2)) {
         TextUtilities.createLayout(new String(var1), this.getPlatformFont()).getRuns();
         var3.charsToGlyphs(var1.length, var1, var2);
      }

      return var2;
   }

   public float getAscent() {
      float var1 = -this.getFontStrike().getMetrics().getAscent();
      if (log.isLoggable(Level.FINER)) {
         log.log(Level.FINER, "getAscent({0}, {1}) = {2}", new Object[]{this.font.getName(), this.font.getSize(), var1});
      }

      return var1;
   }

   public float getDescent() {
      float var1 = this.getFontStrike().getMetrics().getDescent();
      if (log.isLoggable(Level.FINER)) {
         log.log(Level.FINER, "getDescent({0}, {1}) = {2}", new Object[]{this.font.getName(), this.font.getSize(), var1});
      }

      return var1;
   }

   public float getLineSpacing() {
      float var1 = this.getFontStrike().getMetrics().getLineHeight();
      if (log.isLoggable(Level.FINER)) {
         log.log(Level.FINER, "getLineSpacing({0}, {1}) = {2}", new Object[]{this.font.getName(), this.font.getSize(), var1});
      }

      return var1;
   }

   public float getLineGap() {
      float var1 = this.getFontStrike().getMetrics().getLineGap();
      if (log.isLoggable(Level.FINER)) {
         log.log(Level.FINER, "getLineGap({0}, {1}) = {2}", new Object[]{this.font.getName(), this.font.getSize(), var1});
      }

      return var1;
   }

   public boolean hasUniformLineMetrics() {
      return false;
   }

   public Object getPlatformFont() {
      return this.font;
   }

   public float getCapHeight() {
      return this.getFontStrike().getMetrics().getCapHeight();
   }

   public WCTextRun[] getTextRuns(String var1) {
      if (log.isLoggable(Level.FINE)) {
         log.fine(String.format("str='%s' length=%d", var1, var1.length()));
      }

      TextLayout var2 = TextUtilities.createLayout(var1, this.getPlatformFont());
      return (WCTextRun[])Arrays.stream(var2.getRuns()).map(WCTextRunImpl::new).toArray((var0) -> {
         return new WCTextRunImpl[var0];
      });
   }
}
