package com.sun.javafx.font;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.text.GlyphList;
import java.util.HashMap;
import java.util.Map;

public abstract class PrismFontStrike implements FontStrike {
   private DisposerRecord disposer;
   private PrismFontFile fontResource;
   private Map glyphMap = new HashMap();
   private PrismMetrics metrics;
   protected boolean drawShapes = false;
   private float size;
   private BaseTransform transform;
   private int aaMode;
   private FontStrikeDesc desc;
   private int hash;

   protected PrismFontStrike(PrismFontFile var1, float var2, BaseTransform var3, int var4, FontStrikeDesc var5) {
      this.fontResource = var1;
      this.size = var2;
      this.desc = var5;
      PrismFontFactory var6 = PrismFontFactory.getFontFactory();
      boolean var7 = var6.isLCDTextSupported();
      this.aaMode = var7 ? var4 : 0;
      if (var3.isTranslateOrIdentity()) {
         this.transform = BaseTransform.IDENTITY_TRANSFORM;
      } else {
         this.transform = new Affine2D(var3.getMxx(), var3.getMyx(), var3.getMxy(), var3.getMyy(), 0.0, 0.0);
      }

   }

   DisposerRecord getDisposer() {
      if (this.disposer == null) {
         this.disposer = this.createDisposer(this.desc);
      }

      return this.disposer;
   }

   protected abstract DisposerRecord createDisposer(FontStrikeDesc var1);

   public synchronized void clearDesc() {
      this.fontResource.getStrikeMap().remove(this.desc);
   }

   public float getSize() {
      return this.size;
   }

   public Metrics getMetrics() {
      if (this.metrics == null) {
         this.metrics = this.fontResource.getFontMetrics(this.size);
      }

      return this.metrics;
   }

   public PrismFontFile getFontResource() {
      return this.fontResource;
   }

   public boolean drawAsShapes() {
      return this.drawShapes;
   }

   public int getAAMode() {
      return this.aaMode;
   }

   public BaseTransform getTransform() {
      return this.transform;
   }

   public int getQuantizedPosition(Point2D var1) {
      if (this.aaMode == 0) {
         var1.x = (float)Math.round(var1.x);
      } else {
         var1.x = (float)Math.round(3.0 * (double)var1.x) / 3.0F;
      }

      var1.y = (float)Math.round(var1.y);
      return 0;
   }

   public float getCharAdvance(char var1) {
      int var2 = this.fontResource.getGlyphMapper().charToGlyph((int)var1);
      return this.fontResource.getAdvance(var2, this.size);
   }

   public Glyph getGlyph(char var1) {
      int var2 = this.fontResource.getGlyphMapper().charToGlyph((int)var1);
      return this.getGlyph(var2);
   }

   protected abstract Glyph createGlyph(int var1);

   public Glyph getGlyph(int var1) {
      Glyph var2 = (Glyph)this.glyphMap.get(var1);
      if (var2 == null) {
         var2 = this.createGlyph(var1);
         this.glyphMap.put(var1, var2);
      }

      return var2;
   }

   protected abstract Path2D createGlyphOutline(int var1);

   public Shape getOutline(GlyphList var1, BaseTransform var2) {
      Path2D var3 = new Path2D();
      this.getOutline(var1, var2, var3);
      return var3;
   }

   void getOutline(GlyphList var1, BaseTransform var2, Path2D var3) {
      var3.reset();
      if (var1 != null) {
         if (var2 == null) {
            var2 = BaseTransform.IDENTITY_TRANSFORM;
         }

         Affine2D var4 = new Affine2D();

         for(int var5 = 0; var5 < var1.getGlyphCount(); ++var5) {
            int var6 = var1.getGlyphCode(var5);
            if (var6 != 65535) {
               Path2D var7 = this.createGlyphOutline(var6);
               if (var7 != null) {
                  var4.setTransform(var2);
                  var4.translate((double)var1.getPosX(var5), (double)var1.getPosY(var5));
                  var3.append(var7.getPathIterator(var4), false);
               }
            }
         }

      }
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (!(var1 instanceof PrismFontStrike)) {
         return false;
      } else {
         PrismFontStrike var2 = (PrismFontStrike)var1;
         return this.size == var2.size && this.transform.getMxx() == var2.transform.getMxx() && this.transform.getMxy() == var2.transform.getMxy() && this.transform.getMyx() == var2.transform.getMyx() && this.transform.getMyy() == var2.transform.getMyy() && this.fontResource.equals(var2.fontResource);
      }
   }

   public int hashCode() {
      if (this.hash != 0) {
         return this.hash;
      } else {
         this.hash = Float.floatToIntBits(this.size) + Float.floatToIntBits((float)this.transform.getMxx()) + Float.floatToIntBits((float)this.transform.getMyx()) + Float.floatToIntBits((float)this.transform.getMxy()) + Float.floatToIntBits((float)this.transform.getMyy());
         this.hash = 71 * this.hash + this.fontResource.hashCode();
         return this.hash;
      }
   }

   public String toString() {
      return "FontStrike: " + super.toString() + " font resource = " + this.fontResource + " size = " + this.size + " matrix = " + this.transform;
   }
}
