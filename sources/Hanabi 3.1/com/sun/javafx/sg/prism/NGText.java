package com.sun.javafx.sg.prism;

import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.text.TextRun;
import com.sun.prism.Graphics;
import com.sun.prism.paint.Color;

public class NGText extends NGShape {
   static final BaseTransform IDENT;
   private GlyphList[] runs;
   private float layoutX;
   private float layoutY;
   private PGFont font;
   private int fontSmoothingType;
   private boolean underline;
   private boolean strikethrough;
   private Object selectionPaint;
   private int selectionStart;
   private int selectionEnd;
   private static double EPSILON;
   private FontStrike fontStrike = null;
   private FontStrike identityStrike = null;
   private double[] strikeMat = new double[4];
   private boolean drawingEffect = false;
   private static int FILL;
   private static int SHAPE_FILL;
   private static int TEXT;
   private static int DECORATION;

   public void setGlyphs(Object[] var1) {
      this.runs = (GlyphList[])((GlyphList[])var1);
      this.geometryChanged();
   }

   public void setLayoutLocation(float var1, float var2) {
      this.layoutX = var1;
      this.layoutY = var2;
      this.geometryChanged();
   }

   public void setFont(Object var1) {
      if (var1 == null || !var1.equals(this.font)) {
         this.font = (PGFont)var1;
         this.fontStrike = null;
         this.identityStrike = null;
         this.geometryChanged();
      }
   }

   public void setFontSmoothingType(int var1) {
      this.fontSmoothingType = var1;
      this.geometryChanged();
   }

   public void setUnderline(boolean var1) {
      this.underline = var1;
      this.geometryChanged();
   }

   public void setStrikethrough(boolean var1) {
      this.strikethrough = var1;
      this.geometryChanged();
   }

   public void setSelection(int var1, int var2, Object var3) {
      this.selectionPaint = var3;
      this.selectionStart = var1;
      this.selectionEnd = var2;
      this.geometryChanged();
   }

   protected BaseBounds computePadding(BaseBounds var1) {
      float var2 = this.fontSmoothingType == 1 ? 2.0F : 1.0F;
      return var1.deriveWithNewBounds(var1.getMinX() - var2, var1.getMinY() - var2, var1.getMinZ(), var1.getMaxX() + var2, var1.getMaxY() + var2, var1.getMaxZ());
   }

   private FontStrike getStrike(BaseTransform var1) {
      int var2 = this.fontSmoothingType;
      if (this.getMode() == NGShape.Mode.STROKE_FILL) {
         var2 = 0;
      }

      if (var1.isIdentity()) {
         if (this.identityStrike == null || var2 != this.identityStrike.getAAMode()) {
            this.identityStrike = this.font.getStrike(IDENT, var2);
         }

         return this.identityStrike;
      } else {
         if (this.fontStrike == null || this.fontStrike.getSize() != this.font.getSize() || var1.getMxy() == 0.0 && this.strikeMat[1] != 0.0 || var1.getMyx() == 0.0 && this.strikeMat[2] != 0.0 || Math.abs(this.strikeMat[0] - var1.getMxx()) > EPSILON || Math.abs(this.strikeMat[1] - var1.getMxy()) > EPSILON || Math.abs(this.strikeMat[2] - var1.getMyx()) > EPSILON || Math.abs(this.strikeMat[3] - var1.getMyy()) > EPSILON || var2 != this.fontStrike.getAAMode()) {
            this.fontStrike = this.font.getStrike(var1, var2);
            this.strikeMat[0] = var1.getMxx();
            this.strikeMat[1] = var1.getMxy();
            this.strikeMat[2] = var1.getMyx();
            this.strikeMat[3] = var1.getMyy();
         }

         return this.fontStrike;
      }
   }

   public Shape getShape() {
      if (this.runs == null) {
         return new Path2D();
      } else {
         FontStrike var1 = this.getStrike(IDENT);
         Path2D var2 = new Path2D();

         for(int var3 = 0; var3 < this.runs.length; ++var3) {
            GlyphList var4 = this.runs[var3];
            Point2D var5 = var4.getLocation();
            float var6 = var5.x - this.layoutX;
            float var7 = var5.y - this.layoutY;
            BaseTransform var8 = BaseTransform.getTranslateInstance((double)var6, (double)var7);
            var2.append(var1.getOutline(var4, var8), false);
            Metrics var9 = null;
            RoundRectangle2D var10;
            if (this.underline) {
               var9 = var1.getMetrics();
               var10 = new RoundRectangle2D();
               var10.x = var6;
               var10.y = var7 + var9.getUnderLineOffset();
               var10.width = var4.getWidth();
               var10.height = var9.getUnderLineThickness();
               var2.append((Shape)var10, false);
            }

            if (this.strikethrough) {
               if (var9 == null) {
                  var9 = var1.getMetrics();
               }

               var10 = new RoundRectangle2D();
               var10.x = var6;
               var10.y = var7 + var9.getStrikethroughOffset();
               var10.width = var4.getWidth();
               var10.height = var9.getStrikethroughThickness();
               var2.append((Shape)var10, false);
            }
         }

         return var2;
      }
   }

   protected void renderEffect(Graphics var1) {
      if (!var1.getTransformNoClone().isTranslateOrIdentity()) {
         this.drawingEffect = true;
      }

      try {
         super.renderEffect(var1);
      } finally {
         this.drawingEffect = false;
      }

   }

   protected void renderContent2D(Graphics var1, boolean var2) {
      if (this.mode != NGShape.Mode.EMPTY) {
         if (this.runs != null && this.runs.length != 0) {
            BaseTransform var3 = var1.getTransformNoClone();
            FontStrike var4 = this.getStrike(var3);
            if (var4.getAAMode() == 1 || this.fillPaint != null && this.fillPaint.isProportional() || this.drawPaint != null && this.drawPaint.isProportional()) {
               BaseBounds var5 = this.getContentBounds(new RectBounds(), IDENT);
               var1.setNodeBounds((RectBounds)var5);
            }

            Color var8 = null;
            if (this.selectionStart != this.selectionEnd && this.selectionPaint instanceof Color) {
               var8 = (Color)this.selectionPaint;
            }

            BaseBounds var6 = null;
            if (this.getClipNode() != null) {
               var6 = this.getClippedBounds(new RectBounds(), IDENT);
            }

            int var7;
            if (this.mode != NGShape.Mode.STROKE) {
               var1.setPaint(this.fillPaint);
               var7 = TEXT;
               var7 |= !var4.drawAsShapes() && !this.drawingEffect ? FILL : SHAPE_FILL;
               this.renderText(var1, var4, var6, var8, var7);
               if (this.underline || this.strikethrough) {
                  var7 = DECORATION | SHAPE_FILL;
                  this.renderText(var1, var4, var6, var8, var7);
               }
            }

            if (this.mode != NGShape.Mode.FILL) {
               var1.setPaint(this.drawPaint);
               var1.setStroke(this.drawStroke);
               var7 = TEXT;
               if (this.underline || this.strikethrough) {
                  var7 |= DECORATION;
               }

               this.renderText(var1, var4, var6, var8, var7);
            }

            var1.setNodeBounds((RectBounds)null);
         }
      }
   }

   private void renderText(Graphics var1, FontStrike var2, BaseBounds var3, Color var4, int var5) {
      for(int var6 = 0; var6 < this.runs.length; ++var6) {
         TextRun var7 = (TextRun)this.runs[var6];
         RectBounds var8 = var7.getLineBounds();
         Point2D var9 = var7.getLocation();
         float var10 = var9.x - this.layoutX;
         float var11 = var9.y - this.layoutY;
         if (var3 != null) {
            if (var11 > var3.getMaxY()) {
               break;
            }

            if (var11 + var8.getHeight() < var3.getMinY() || var10 > var3.getMaxX() || var10 + var7.getWidth() < var3.getMinX()) {
               continue;
            }
         }

         var11 -= var8.getMinY();
         if ((var5 & TEXT) != 0 && var7.getGlyphCount() > 0) {
            if ((var5 & FILL) != 0) {
               int var12 = var7.getStart();
               var1.drawString(var7, var2, var10, var11, var4, this.selectionStart - var12, this.selectionEnd - var12);
            } else {
               BaseTransform var16 = BaseTransform.getTranslateInstance((double)var10, (double)var11);
               if ((var5 & SHAPE_FILL) != 0) {
                  var1.fill(var2.getOutline(var7, var16));
               } else {
                  var1.draw(var2.getOutline(var7, var16));
               }
            }
         }

         if ((var5 & DECORATION) != 0) {
            Metrics var17 = var2.getMetrics();
            float var13;
            float var14;
            float var15;
            if (this.underline) {
               var13 = var11 + var17.getUnderLineOffset();
               var14 = var17.getUnderLineThickness();
               if ((var5 & SHAPE_FILL) != 0) {
                  if (var14 <= 1.0F && var1.getTransformNoClone().isTranslateOrIdentity()) {
                     var15 = (float)var1.getTransformNoClone().getMyt();
                     var13 = (float)Math.round(var13 + var15) - var15;
                  }

                  var1.fillRect(var10, var13, var7.getWidth(), var14);
               } else {
                  var1.drawRect(var10, var13, var7.getWidth(), var14);
               }
            }

            if (this.strikethrough) {
               var13 = var11 + var17.getStrikethroughOffset();
               var14 = var17.getStrikethroughThickness();
               if ((var5 & SHAPE_FILL) != 0) {
                  if (var14 <= 1.0F && var1.getTransformNoClone().isTranslateOrIdentity()) {
                     var15 = (float)var1.getTransformNoClone().getMyt();
                     var13 = (float)Math.round(var13 + var15) - var15;
                  }

                  var1.fillRect(var10, var13, var7.getWidth(), var14);
               } else {
                  var1.drawRect(var10, var13, var7.getWidth(), var14);
               }
            }
         }
      }

   }

   static {
      IDENT = BaseTransform.IDENTITY_TRANSFORM;
      EPSILON = 0.01;
      FILL = 2;
      SHAPE_FILL = 4;
      TEXT = 8;
      DECORATION = 16;
   }
}
