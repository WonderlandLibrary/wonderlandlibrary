package com.sun.javafx.font;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.text.GlyphList;

public class CompositeStrike implements FontStrike {
   private CompositeFontResource fontResource;
   private float size;
   private int aaMode;
   BaseTransform transform;
   private FontStrike slot0Strike;
   private FontStrike[] strikeSlots;
   private FontStrikeDesc desc;
   DisposerRecord disposer;
   private PrismMetrics metrics;

   public void clearDesc() {
      this.fontResource.getStrikeMap().remove(this.desc);
      if (this.slot0Strike != null) {
         this.slot0Strike.clearDesc();
      }

      if (this.strikeSlots != null) {
         for(int var1 = 1; var1 < this.strikeSlots.length; ++var1) {
            if (this.strikeSlots[var1] != null) {
               this.strikeSlots[var1].clearDesc();
            }
         }
      }

   }

   CompositeStrike(CompositeFontResource var1, float var2, BaseTransform var3, int var4, FontStrikeDesc var5) {
      this.fontResource = var1;
      this.size = var2;
      if (var3.isTranslateOrIdentity()) {
         this.transform = BaseTransform.IDENTITY_TRANSFORM;
      } else {
         this.transform = var3.copy();
      }

      this.desc = var5;
      this.aaMode = var4;
      this.disposer = new CompositeStrikeDisposer(var1, var5);
   }

   public int getAAMode() {
      PrismFontFactory var1 = PrismFontFactory.getFontFactory();
      return var1.isLCDTextSupported() ? this.aaMode : 0;
   }

   public BaseTransform getTransform() {
      return this.transform;
   }

   public FontStrike getStrikeSlot(int var1) {
      FontResource var3;
      if (var1 == 0) {
         if (this.slot0Strike == null) {
            var3 = this.fontResource.getSlotResource(0);
            this.slot0Strike = var3.getStrike(this.size, this.transform, this.getAAMode());
         }

         return this.slot0Strike;
      } else {
         if (this.strikeSlots == null) {
            this.strikeSlots = new FontStrike[this.fontResource.getNumSlots()];
         }

         if (var1 >= this.strikeSlots.length) {
            FontStrike[] var2 = new FontStrike[this.fontResource.getNumSlots()];
            System.arraycopy(this.strikeSlots, 0, var2, 0, this.strikeSlots.length);
            this.strikeSlots = var2;
         }

         if (this.strikeSlots[var1] == null) {
            var3 = this.fontResource.getSlotResource(var1);
            this.strikeSlots[var1] = var3.getStrike(this.size, this.transform, this.getAAMode());
         }

         return this.strikeSlots[var1];
      }
   }

   public FontResource getFontResource() {
      return this.fontResource;
   }

   public int getStrikeSlotForGlyph(int var1) {
      return var1 >>> 24;
   }

   public float getSize() {
      return this.size;
   }

   public boolean drawAsShapes() {
      return this.getStrikeSlot(0).drawAsShapes();
   }

   public Metrics getMetrics() {
      if (this.metrics == null) {
         PrismFontFile var1 = (PrismFontFile)this.fontResource.getSlotResource(0);
         this.metrics = var1.getFontMetrics(this.size);
      }

      return this.metrics;
   }

   public Glyph getGlyph(char var1) {
      int var2 = this.fontResource.getGlyphMapper().charToGlyph(var1);
      return this.getGlyph(var2);
   }

   public Glyph getGlyph(int var1) {
      int var2 = var1 >>> 24;
      int var3 = var1 & 16777215;
      return this.getStrikeSlot(var2).getGlyph(var3);
   }

   public float getCharAdvance(char var1) {
      int var2 = this.fontResource.getGlyphMapper().charToGlyph((int)var1);
      return this.fontResource.getAdvance(var2, this.size);
   }

   public int getQuantizedPosition(Point2D var1) {
      return this.getStrikeSlot(0).getQuantizedPosition(var1);
   }

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
               Glyph var7 = this.getGlyph(var6);
               Shape var8 = var7.getShape();
               if (var8 != null) {
                  var4.setTransform(var2);
                  var4.translate((double)var1.getPosX(var5), (double)var1.getPosY(var5));
                  var3.append(var8.getPathIterator(var4), false);
               }
            }
         }

      }
   }
}
