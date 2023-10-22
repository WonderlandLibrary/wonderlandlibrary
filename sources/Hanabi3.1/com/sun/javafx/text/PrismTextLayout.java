package com.sun.javafx.text;

import com.sun.javafx.font.CharToGlyphMapper;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.Translate2D;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.scene.text.TextSpan;
import java.text.Bidi;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;

public class PrismTextLayout implements TextLayout {
   private static final BaseTransform IDENTITY;
   private static final int X_MIN_INDEX = 0;
   private static final int Y_MIN_INDEX = 1;
   private static final int X_MAX_INDEX = 2;
   private static final int Y_MAX_INDEX = 3;
   private static final Hashtable stringCache;
   private static final Object CACHE_SIZE_LOCK;
   private static int cacheSize;
   private static final int MAX_STRING_SIZE = 256;
   private static final int MAX_CACHE_SIZE;
   private char[] text;
   private TextSpan[] spans;
   private PGFont font;
   private FontStrike strike;
   private Integer cacheKey;
   private TextLine[] lines;
   private TextRun[] runs;
   private int runCount;
   private BaseBounds logicalBounds = new RectBounds();
   private RectBounds visualBounds;
   private float layoutWidth;
   private float layoutHeight;
   private float wrapWidth;
   private float spacing;
   private LayoutCache layoutCache;
   private Shape shape;
   private int flags = 262144;

   private void reset() {
      this.layoutCache = null;
      this.runs = null;
      this.flags &= -2048;
      this.relayout();
   }

   private void relayout() {
      this.logicalBounds.makeEmpty();
      this.visualBounds = null;
      this.layoutWidth = this.layoutHeight = 0.0F;
      this.flags &= -1665;
      this.lines = null;
      this.shape = null;
   }

   public boolean setContent(TextSpan[] var1) {
      if (var1 == null && this.spans == null) {
         return false;
      } else {
         if (var1 != null && this.spans != null && var1.length == this.spans.length) {
            int var2;
            for(var2 = 0; var2 < var1.length && var1[var2] == this.spans[var2]; ++var2) {
            }

            if (var2 == var1.length) {
               return false;
            }
         }

         this.reset();
         this.spans = var1;
         this.font = null;
         this.strike = null;
         this.text = null;
         this.cacheKey = null;
         return true;
      }
   }

   public boolean setContent(String var1, Object var2) {
      this.reset();
      this.spans = null;
      this.font = (PGFont)var2;
      this.strike = ((PGFont)var2).getStrike(IDENTITY);
      this.text = var1.toCharArray();
      if (MAX_CACHE_SIZE > 0) {
         int var3 = var1.length();
         if (0 < var3 && var3 <= 256) {
            this.cacheKey = var1.hashCode() * this.strike.hashCode();
         }
      }

      return true;
   }

   public boolean setDirection(int var1) {
      if ((this.flags & 15360) == var1) {
         return false;
      } else {
         this.flags &= -15361;
         this.flags |= var1 & 15360;
         this.reset();
         return true;
      }
   }

   public boolean setBoundsType(int var1) {
      if ((this.flags & 16384) == var1) {
         return false;
      } else {
         this.flags &= -16385;
         this.flags |= var1 & 16384;
         this.reset();
         return true;
      }
   }

   public boolean setAlignment(int var1) {
      int var2 = 262144;
      switch (var1) {
         case 0:
            var2 = 262144;
            break;
         case 1:
            var2 = 524288;
            break;
         case 2:
            var2 = 1048576;
            break;
         case 3:
            var2 = 2097152;
      }

      if ((this.flags & 3932160) == var2) {
         return false;
      } else {
         if (var2 == 2097152 || (this.flags & 2097152) != 0) {
            this.reset();
         }

         this.flags &= -3932161;
         this.flags |= var2;
         this.relayout();
         return true;
      }
   }

   public boolean setWrapWidth(float var1) {
      if (Float.isInfinite(var1)) {
         var1 = 0.0F;
      }

      if (Float.isNaN(var1)) {
         var1 = 0.0F;
      }

      float var2 = this.wrapWidth;
      this.wrapWidth = Math.max(0.0F, var1);
      boolean var3 = true;
      if (this.lines != null && var2 != 0.0F && var1 != 0.0F && (this.flags & 262144) != 0) {
         if (var1 > var2) {
            if ((this.flags & 128) == 0) {
               var3 = false;
            }
         } else if (var1 >= this.layoutWidth) {
            var3 = false;
         }
      }

      if (var3) {
         this.relayout();
      }

      return var3;
   }

   public boolean setLineSpacing(float var1) {
      if (this.spacing == var1) {
         return false;
      } else {
         this.spacing = var1;
         this.relayout();
         return true;
      }
   }

   private void ensureLayout() {
      if (this.lines == null) {
         this.layout();
      }

   }

   public com.sun.javafx.scene.text.TextLine[] getLines() {
      this.ensureLayout();
      return this.lines;
   }

   public GlyphList[] getRuns() {
      this.ensureLayout();
      GlyphList[] var1 = new GlyphList[this.runCount];
      int var2 = 0;

      for(int var3 = 0; var3 < this.lines.length; ++var3) {
         TextRun[] var4 = this.lines[var3].getRuns();
         int var5 = var4.length;
         System.arraycopy(var4, 0, var1, var2, var5);
         var2 += var5;
      }

      return var1;
   }

   public BaseBounds getBounds() {
      this.ensureLayout();
      return this.logicalBounds;
   }

   public BaseBounds getBounds(TextSpan var1, BaseBounds var2) {
      this.ensureLayout();
      float var3 = Float.POSITIVE_INFINITY;
      float var4 = Float.POSITIVE_INFINITY;
      float var5 = Float.NEGATIVE_INFINITY;
      float var6 = Float.NEGATIVE_INFINITY;
      int var7;
      TextLine var8;
      if (var1 != null) {
         for(var7 = 0; var7 < this.lines.length; ++var7) {
            var8 = this.lines[var7];
            TextRun[] var9 = var8.getRuns();

            for(int var10 = 0; var10 < var9.length; ++var10) {
               TextRun var11 = var9[var10];
               TextSpan var12 = var11.getTextSpan();
               if (var12 == var1) {
                  Point2D var13 = var11.getLocation();
                  float var14 = var13.x;
                  if (var11.isLeftBearing()) {
                     var14 += var8.getLeftSideBearing();
                  }

                  float var15 = var13.x + var11.getWidth();
                  if (var11.isRightBearing()) {
                     var15 += var8.getRightSideBearing();
                  }

                  float var16 = var13.y;
                  float var17 = var13.y + var8.getBounds().getHeight() + this.spacing;
                  if (var14 < var3) {
                     var3 = var14;
                  }

                  if (var16 < var4) {
                     var4 = var16;
                  }

                  if (var15 > var5) {
                     var5 = var15;
                  }

                  if (var17 > var6) {
                     var6 = var17;
                  }
               }
            }
         }
      } else {
         var6 = 0.0F;
         var4 = 0.0F;

         for(var7 = 0; var7 < this.lines.length; ++var7) {
            var8 = this.lines[var7];
            RectBounds var20 = var8.getBounds();
            float var21 = var20.getMinX() + var8.getLeftSideBearing();
            if (var21 < var3) {
               var3 = var21;
            }

            float var22 = var20.getMaxX() + var8.getRightSideBearing();
            if (var22 > var5) {
               var5 = var22;
            }

            var6 += var20.getHeight();
         }

         if (this.isMirrored()) {
            float var18 = this.getMirroringWidth();
            float var19 = var3;
            var3 = var18 - var5;
            var5 = var18 - var19;
         }
      }

      return var2.deriveWithNewBounds(var3, var4, 0.0F, var5, var6, 0.0F);
   }

   public PathElement[] getCaretShape(int var1, boolean var2, float var3, float var4) {
      this.ensureLayout();
      int var5 = 0;

      for(int var6 = this.getLineCount(); var5 < var6 - 1; ++var5) {
         TextLine var7 = this.lines[var5];
         int var8 = var7.getStart() + var7.getLength();
         if (var8 > var1) {
            break;
         }
      }

      int var23 = -1;
      byte var24 = 0;
      float var9 = 0.0F;
      float var10 = 0.0F;
      float var11 = 0.0F;
      TextLine var12 = this.lines[var5];
      TextRun[] var13 = var12.getRuns();
      int var14 = var13.length;
      int var15 = -1;

      int var16;
      TextRun var17;
      int var18;
      int var19;
      for(var16 = 0; var16 < var14; ++var16) {
         var17 = var13[var16];
         var18 = var17.getStart();
         var19 = var17.getEnd();
         if (var18 <= var1 && var1 < var19) {
            if (!var17.isLinebreak()) {
               var15 = var16;
            }
            break;
         }
      }

      int var26;
      Point2D var27;
      if (var15 != -1) {
         TextRun var25 = var13[var15];
         var26 = var25.getStart();
         var27 = var25.getLocation();
         var9 = var27.x + var25.getXAtOffset(var1 - var26, var2);
         var10 = var27.y;
         var11 = var12.getBounds().getHeight();
         if (var2) {
            if (var15 > 0 && var1 == var26) {
               var24 = var25.getLevel();
               var23 = var1 - 1;
            }
         } else {
            var19 = var25.getEnd();
            if (var15 + 1 < var13.length && var1 + 1 == var19) {
               var24 = var25.getLevel();
               var23 = var1 + 1;
            }
         }
      } else {
         var16 = 0;
         var15 = 0;

         for(var26 = 0; var26 < var14; ++var26) {
            TextRun var29 = var13[var26];
            if (var29.getStart() >= var16 && !var29.isLinebreak()) {
               var16 = var29.getStart();
               var15 = var26;
            }
         }

         var17 = var13[var15];
         var27 = var17.getLocation();
         var9 = var27.x + (var17.isLeftToRight() ? var17.getWidth() : 0.0F);
         var10 = var27.y;
         var11 = var12.getBounds().getHeight();
      }

      if (this.isMirrored()) {
         var9 = this.getMirroringWidth() - var9;
      }

      var9 += var3;
      var10 += var4;
      if (var23 != -1) {
         for(var16 = 0; var16 < var13.length; ++var16) {
            var17 = var13[var16];
            var18 = var17.getStart();
            var19 = var17.getEnd();
            if (var18 <= var23 && var23 < var19 && (var17.getLevel() & 1) != (var24 & 1)) {
               Point2D var20 = var17.getLocation();
               float var21 = var20.x;
               if (var2) {
                  if ((var24 & 1) != 0) {
                     var21 += var17.getWidth();
                  }
               } else if ((var24 & 1) == 0) {
                  var21 += var17.getWidth();
               }

               if (this.isMirrored()) {
                  var21 = this.getMirroringWidth() - var21;
               }

               var21 += var3;
               PathElement[] var22 = new PathElement[]{new MoveTo((double)var9, (double)var10), new LineTo((double)var9, (double)(var10 + var11 / 2.0F)), new MoveTo((double)var21, (double)(var10 + var11 / 2.0F)), new LineTo((double)var21, (double)(var10 + var11))};
               return var22;
            }
         }
      }

      PathElement[] var28 = new PathElement[]{new MoveTo((double)var9, (double)var10), new LineTo((double)var9, (double)(var10 + var11))};
      return var28;
   }

   public HitInfo getHitInfo(float var1, float var2) {
      this.ensureLayout();
      HitInfo var3 = new HitInfo();
      int var4 = this.getLineIndex(var2);
      if (var4 >= this.getLineCount()) {
         var3.setCharIndex(this.getCharCount());
      } else {
         if (this.isMirrored()) {
            var1 = this.getMirroringWidth() - var1;
         }

         TextLine var5 = this.lines[var4];
         TextRun[] var6 = var5.getRuns();
         RectBounds var7 = var5.getBounds();
         TextRun var8 = null;
         var1 -= var7.getMinX();

         for(int var9 = 0; var9 < var6.length; ++var9) {
            var8 = var6[var9];
            if (var1 < var8.getWidth()) {
               break;
            }

            if (var9 + 1 < var6.length) {
               if (var6[var9 + 1].isLinebreak()) {
                  break;
               }

               var1 -= var8.getWidth();
            }
         }

         if (var8 != null) {
            int[] var10 = new int[1];
            var3.setCharIndex(var8.getStart() + var8.getOffsetAtX(var1, var10));
            var3.setLeading(var10[0] == 0);
         } else {
            var3.setCharIndex(var5.getStart());
            var3.setLeading(true);
         }
      }

      return var3;
   }

   public PathElement[] getRange(int var1, int var2, int var3, float var4, float var5) {
      this.ensureLayout();
      int var6 = this.getLineCount();
      ArrayList var7 = new ArrayList();
      float var8 = 0.0F;

      for(int var9 = 0; var9 < var6; ++var9) {
         TextLine var10 = this.lines[var9];
         RectBounds var11 = var10.getBounds();
         int var12 = var10.getStart();
         if (var12 >= var2) {
            break;
         }

         int var13 = var12 + var10.getLength();
         if (var1 > var13) {
            var8 += var11.getHeight() + this.spacing;
         } else {
            TextRun[] var14 = var10.getRuns();
            int var15 = Math.min(var13, var2) - Math.max(var12, var1);
            int var16 = 0;
            float var17 = -1.0F;
            float var18 = -1.0F;

            for(float var19 = var11.getMinX(); var15 > 0 && var16 < var14.length; ++var16) {
               TextRun var20 = var14[var16];
               int var21 = var20.getStart();
               int var22 = var20.getEnd();
               float var23 = var20.getWidth();
               int var24 = Math.max(var21, Math.min(var1, var22));
               int var25 = Math.max(var21, Math.min(var2, var22));
               int var26 = var25 - var24;
               if (var26 != 0) {
                  boolean var27 = var20.isLeftToRight();
                  float var28;
                  if (var21 > var1) {
                     var28 = var27 ? var19 : var19 + var23;
                  } else {
                     var28 = var19 + var20.getXAtOffset(var1 - var21, true);
                  }

                  float var29;
                  if (var22 < var2) {
                     var29 = var27 ? var19 + var23 : var19;
                  } else {
                     var29 = var19 + var20.getXAtOffset(var2 - var21, true);
                  }

                  float var30;
                  if (var28 > var29) {
                     var30 = var28;
                     var28 = var29;
                     var29 = var30;
                  }

                  var15 -= var26;
                  var30 = 0.0F;
                  float var31 = 0.0F;
                  switch (var3) {
                     case 1:
                        var30 = var8;
                        var31 = var8 + var11.getHeight();
                        break;
                     case 2:
                     case 4:
                        label89: {
                           FontStrike var32 = null;
                           if (this.spans != null) {
                              TextSpan var33 = var20.getTextSpan();
                              PGFont var34 = (PGFont)var33.getFont();
                              if (var34 == null) {
                                 break label89;
                              }

                              var32 = var34.getStrike(IDENTITY);
                           } else {
                              var32 = this.strike;
                           }

                           var30 = var8 - var20.getAscent();
                           Metrics var35 = var32.getMetrics();
                           if (var3 == 2) {
                              var30 += var35.getUnderLineOffset();
                              var31 = var30 + var35.getUnderLineThickness();
                           } else {
                              var30 += var35.getStrikethroughOffset();
                              var31 = var30 + var35.getStrikethroughThickness();
                           }
                        }
                     case 3:
                  }

                  float var36;
                  float var37;
                  float var38;
                  if (var28 != var18) {
                     if (var17 != -1.0F && var18 != -1.0F) {
                        var36 = var17;
                        var37 = var18;
                        if (this.isMirrored()) {
                           var38 = this.getMirroringWidth();
                           var36 = var38 - var17;
                           var37 = var38 - var18;
                        }

                        var7.add(new MoveTo((double)(var4 + var36), (double)(var5 + var30)));
                        var7.add(new LineTo((double)(var4 + var37), (double)(var5 + var30)));
                        var7.add(new LineTo((double)(var4 + var37), (double)(var5 + var31)));
                        var7.add(new LineTo((double)(var4 + var36), (double)(var5 + var31)));
                        var7.add(new LineTo((double)(var4 + var36), (double)(var5 + var30)));
                     }

                     var17 = var28;
                  }

                  var18 = var29;
                  if (var15 == 0) {
                     var36 = var17;
                     var37 = var29;
                     if (this.isMirrored()) {
                        var38 = this.getMirroringWidth();
                        var36 = var38 - var17;
                        var37 = var38 - var29;
                     }

                     var7.add(new MoveTo((double)(var4 + var36), (double)(var5 + var30)));
                     var7.add(new LineTo((double)(var4 + var37), (double)(var5 + var30)));
                     var7.add(new LineTo((double)(var4 + var37), (double)(var5 + var31)));
                     var7.add(new LineTo((double)(var4 + var36), (double)(var5 + var31)));
                     var7.add(new LineTo((double)(var4 + var36), (double)(var5 + var30)));
                  }
               }

               var19 += var23;
            }

            var8 += var11.getHeight() + this.spacing;
         }
      }

      return (PathElement[])var7.toArray(new PathElement[var7.size()]);
   }

   public Shape getShape(int var1, TextSpan var2) {
      this.ensureLayout();
      boolean var3 = (var1 & 1) != 0;
      boolean var4 = (var1 & 2) != 0;
      boolean var5 = (var1 & 4) != 0;
      boolean var6 = (var1 & 8) != 0;
      if (this.shape != null && var3 && !var4 && !var5 && var6) {
         return this.shape;
      } else {
         Path2D var7 = new Path2D();
         Translate2D var8 = new Translate2D(0.0, 0.0);
         float var9 = 0.0F;
         if (var6) {
            var9 = -this.lines[0].getBounds().getMinY();
         }

         for(int var10 = 0; var10 < this.lines.length; ++var10) {
            TextLine var11 = this.lines[var10];
            TextRun[] var12 = var11.getRuns();
            RectBounds var13 = var11.getBounds();
            float var14 = -var13.getMinY();

            for(int var15 = 0; var15 < var12.length; ++var15) {
               TextRun var16 = var12[var15];
               FontStrike var17 = null;
               if (this.spans != null) {
                  TextSpan var18 = var16.getTextSpan();
                  if (var2 != null && var18 != var2) {
                     continue;
                  }

                  PGFont var19 = (PGFont)var18.getFont();
                  if (var19 == null) {
                     continue;
                  }

                  var17 = var19.getStrike(IDENTITY);
               } else {
                  var17 = this.strike;
               }

               Point2D var23 = var16.getLocation();
               float var24 = var23.x;
               float var20 = var23.y + var14 - var9;
               Metrics var21 = null;
               if (var4 || var5) {
                  var21 = var17.getMetrics();
               }

               RoundRectangle2D var22;
               if (var4) {
                  var22 = new RoundRectangle2D();
                  var22.x = var24;
                  var22.y = var20 + var21.getUnderLineOffset();
                  var22.width = var16.getWidth();
                  var22.height = var21.getUnderLineThickness();
                  var7.append((Shape)var22, false);
               }

               if (var5) {
                  var22 = new RoundRectangle2D();
                  var22.x = var24;
                  var22.y = var20 + var21.getStrikethroughOffset();
                  var22.width = var16.getWidth();
                  var22.height = var21.getStrikethroughThickness();
                  var7.append((Shape)var22, false);
               }

               if (var3 && var16.getGlyphCount() > 0) {
                  var8.restoreTransform(1.0, 0.0, 0.0, 1.0, (double)var24, (double)var20);
                  Path2D var25 = (Path2D)var17.getOutline(var16, var8);
                  var7.append((Shape)var25, false);
               }
            }
         }

         if (var3 && !var4 && !var5) {
            this.shape = var7;
         }

         return var7;
      }
   }

   private int getLineIndex(float var1) {
      int var2 = 0;
      float var3 = 0.0F;

      for(int var4 = this.getLineCount(); var2 < var4; ++var2) {
         var3 += this.lines[var2].getBounds().getHeight() + this.spacing;
         if (var2 + 1 == var4) {
            var3 -= this.lines[var2].getLeading();
         }

         if (var3 > var1) {
            break;
         }
      }

      return var2;
   }

   private boolean copyCache() {
      int var1 = this.flags & 3932160;
      int var2 = this.flags & 16384;
      return this.wrapWidth != 0.0F || var1 != 262144 || var2 == 0 || this.isMirrored();
   }

   private void initCache() {
      if (this.cacheKey != null) {
         if (this.layoutCache == null) {
            LayoutCache var1 = (LayoutCache)stringCache.get(this.cacheKey);
            if (var1 != null && var1.font.equals(this.font) && Arrays.equals(var1.text, this.text)) {
               this.layoutCache = var1;
               this.runs = var1.runs;
               this.runCount = var1.runCount;
               this.flags |= var1.analysis;
            }
         }

         if (this.layoutCache != null) {
            if (this.copyCache()) {
               if (this.layoutCache.runs == this.runs) {
                  this.runs = new TextRun[this.runCount];
                  System.arraycopy(this.layoutCache.runs, 0, this.runs, 0, this.runCount);
               }
            } else if (this.layoutCache.lines != null) {
               this.runs = this.layoutCache.runs;
               this.runCount = this.layoutCache.runCount;
               this.flags |= this.layoutCache.analysis;
               this.lines = this.layoutCache.lines;
               this.layoutWidth = this.layoutCache.layoutWidth;
               this.layoutHeight = this.layoutCache.layoutHeight;
               float var2 = this.lines[0].getBounds().getMinY();
               this.logicalBounds = this.logicalBounds.deriveWithNewBounds(0.0F, var2, 0.0F, this.layoutWidth, this.layoutHeight + var2, 0.0F);
            }
         }
      }

   }

   private int getLineCount() {
      return this.lines.length;
   }

   private int getCharCount() {
      if (this.text != null) {
         return this.text.length;
      } else {
         int var1 = 0;

         for(int var2 = 0; var2 < this.lines.length; ++var2) {
            var1 += this.lines[var2].getLength();
         }

         return var1;
      }
   }

   public TextSpan[] getTextSpans() {
      return this.spans;
   }

   public PGFont getFont() {
      return this.font;
   }

   public int getDirection() {
      if ((this.flags & 1024) != 0) {
         return 0;
      } else if ((this.flags & 2048) != 0) {
         return 1;
      } else if ((this.flags & 4096) != 0) {
         return -2;
      } else {
         return (this.flags & 8192) != 0 ? -1 : -2;
      }
   }

   public void addTextRun(TextRun var1) {
      if (this.runCount + 1 > this.runs.length) {
         TextRun[] var2 = new TextRun[this.runs.length + 64];
         System.arraycopy(this.runs, 0, var2, 0, this.runs.length);
         this.runs = var2;
      }

      this.runs[this.runCount++] = var1;
   }

   private void buildRuns(char[] var1) {
      this.runCount = 0;
      if (this.runs == null) {
         int var2 = Math.max(4, Math.min(var1.length / 16, 16));
         this.runs = new TextRun[var2];
      }

      GlyphLayout var4 = GlyphLayout.getInstance();
      this.flags = var4.breakRuns(this, var1, this.flags);
      var4.dispose();

      for(int var3 = this.runCount; var3 < this.runs.length; ++var3) {
         this.runs[var3] = null;
      }

   }

   private void shape(TextRun var1, char[] var2, GlyphLayout var3) {
      FontStrike var4;
      PGFont var5;
      if (this.spans != null) {
         if (this.spans.length == 0) {
            return;
         }

         TextSpan var6 = var1.getTextSpan();
         var5 = (PGFont)var6.getFont();
         if (var5 == null) {
            RectBounds var7 = var6.getBounds();
            var1.setEmbedded(var7, var6.getText().length());
            return;
         }

         var4 = var5.getStrike(IDENTITY);
      } else {
         var5 = this.font;
         var4 = this.strike;
      }

      float var9;
      if (var1.getAscent() == 0.0F) {
         Metrics var16 = var4.getMetrics();
         if ((this.flags & 16384) == 16384) {
            float var18 = var16.getAscent();
            if (var5.getFamilyName().equals("Segoe UI")) {
               var18 = (float)((double)var18 * 0.8);
            }

            var18 = (float)((int)((double)var18 - 0.75));
            float var8 = (float)((int)((double)var16.getDescent() + 0.75));
            var9 = (float)((int)((double)var16.getLineGap() + 0.75));
            float var10 = (float)((int)((double)var16.getCapHeight() + 0.75));
            float var11 = -var18 - var10;
            if (var11 > var8) {
               var8 = var11;
            } else {
               var18 += var11 - var8;
            }

            var1.setMetrics(var18, var8, var9);
         } else {
            var1.setMetrics(var16.getAscent(), var16.getDescent(), var16.getLineGap());
         }
      }

      if (!var1.isTab()) {
         if (!var1.isLinebreak()) {
            if (var1.getGlyphCount() <= 0) {
               if (var1.isComplex()) {
                  var3.layout(var1, var5, var4, var2);
               } else {
                  FontResource var17 = var4.getFontResource();
                  int var20 = var1.getStart();
                  int var19 = var1.getLength();
                  CharToGlyphMapper var21;
                  if (this.layoutCache == null) {
                     var9 = var4.getSize();
                     var21 = var17.getGlyphMapper();
                     int[] var22 = new int[var19];
                     var21.charsToGlyphs(var20, var19, var2, var22);
                     float[] var12 = new float[var19 + 1 << 1];
                     float var13 = 0.0F;

                     for(int var14 = 0; var14 < var19; ++var14) {
                        float var15 = var17.getAdvance(var22[var14], var9);
                        var12[var14 << 1] = var13;
                        var13 += var15;
                     }

                     var12[var19 << 1] = var13;
                     var1.shape(var19, var22, var12, (int[])null);
                  } else {
                     if (!this.layoutCache.valid) {
                        var9 = var4.getSize();
                        var21 = var17.getGlyphMapper();
                        var21.charsToGlyphs(var20, var19, var2, this.layoutCache.glyphs, var20);
                        int var23 = var20 + var19;
                        float var24 = 0.0F;

                        for(int var25 = var20; var25 < var23; ++var25) {
                           float var26 = var17.getAdvance(this.layoutCache.glyphs[var25], var9);
                           this.layoutCache.advances[var25] = var26;
                           var24 += var26;
                        }

                        var1.setWidth(var24);
                     }

                     var1.shape(var19, this.layoutCache.glyphs, this.layoutCache.advances);
                  }
               }

            }
         }
      }
   }

   private TextLine createLine(int var1, int var2, int var3) {
      int var4 = var2 - var1 + 1;
      TextRun[] var5 = new TextRun[var4];
      if (var1 < this.runCount) {
         System.arraycopy(this.runs, var1, var5, 0, var4);
      }

      float var6 = 0.0F;
      float var7 = 0.0F;
      float var8 = 0.0F;
      float var9 = 0.0F;
      int var10 = 0;

      for(int var11 = 0; var11 < var5.length; ++var11) {
         TextRun var12 = var5[var11];
         var6 += var12.getWidth();
         var7 = Math.min(var7, var12.getAscent());
         var8 = Math.max(var8, var12.getDescent());
         var9 = Math.max(var9, var12.getLeading());
         var10 += var12.getLength();
      }

      if (var6 > this.layoutWidth) {
         this.layoutWidth = var6;
      }

      return new TextLine(var3, var10, var5, var6, var7, var8, var9);
   }

   private void reorderLine(TextLine var1) {
      TextRun[] var2 = var1.getRuns();
      int var3 = var2.length;
      if (var3 > 0 && var2[var3 - 1].isLinebreak()) {
         --var3;
      }

      if (var3 >= 2) {
         byte[] var4 = new byte[var3];

         for(int var5 = 0; var5 < var3; ++var5) {
            var4[var5] = var2[var5].getLevel();
         }

         Bidi.reorderVisually(var4, 0, var2, 0, var3);
      }
   }

   private char[] getText() {
      if (this.text == null) {
         int var1 = 0;

         int var2;
         for(var2 = 0; var2 < this.spans.length; ++var2) {
            var1 += this.spans[var2].getText().length();
         }

         this.text = new char[var1];
         var2 = 0;

         for(int var3 = 0; var3 < this.spans.length; ++var3) {
            String var4 = this.spans[var3].getText();
            int var5 = var4.length();
            var4.getChars(0, var5, this.text, var2);
            var2 += var5;
         }
      }

      return this.text;
   }

   private boolean isSimpleLayout() {
      int var1 = this.flags & 3932160;
      boolean var2 = this.wrapWidth > 0.0F && var1 == 2097152;
      byte var3 = 24;
      return (this.flags & var3) == 0 && !var2;
   }

   private boolean isMirrored() {
      boolean var1 = false;
      switch (this.flags & 15360) {
         case 1024:
            var1 = false;
            break;
         case 2048:
            var1 = true;
            break;
         case 4096:
         case 8192:
            var1 = (this.flags & 256) != 0;
      }

      return var1;
   }

   private float getMirroringWidth() {
      return this.wrapWidth != 0.0F ? this.wrapWidth : this.layoutWidth;
   }

   private void reuseRuns() {
      this.runCount = 0;
      int var1 = 0;

      while(var1 < this.runs.length) {
         TextRun var2 = this.runs[var1];
         if (var2 == null) {
            break;
         }

         this.runs[var1] = null;
         ++var1;
         this.runs[this.runCount++] = var2 = var2.unwrap();
         if (var2.isSplit()) {
            var2.merge((TextRun)null);

            while(var1 < this.runs.length) {
               TextRun var3 = this.runs[var1];
               if (var3 == null) {
                  break;
               }

               var2.merge(var3);
               this.runs[var1] = null;
               ++var1;
               if (var3.isSplitLast()) {
                  break;
               }
            }
         }
      }

   }

   private float getTabAdvance() {
      float var1 = 0.0F;
      if (this.spans != null) {
         for(int var2 = 0; var2 < this.spans.length; ++var2) {
            TextSpan var3 = this.spans[var2];
            PGFont var4 = (PGFont)var3.getFont();
            if (var4 != null) {
               FontStrike var5 = var4.getStrike(IDENTITY);
               var1 = var5.getCharAdvance(' ');
               break;
            }
         }
      } else {
         var1 = this.strike.getCharAdvance(' ');
      }

      return 8.0F * var1;
   }

   private void layout() {
      this.initCache();
      if (this.lines == null) {
         char[] var1 = this.getText();
         if ((this.flags & 2) != 0 && this.isSimpleLayout()) {
            this.reuseRuns();
         } else {
            this.buildRuns(var1);
         }

         GlyphLayout var2 = null;
         if ((this.flags & 16) != 0) {
            var2 = GlyphLayout.getInstance();
         }

         float var3 = 0.0F;
         if ((this.flags & 4) != 0) {
            var3 = this.getTabAdvance();
         }

         BreakIterator var4 = null;
         if (this.wrapWidth > 0.0F && (this.flags & 80) != 0) {
            var4 = BreakIterator.getLineInstance();
            var4.setText(new CharArrayIterator(var1));
         }

         int var5 = this.flags & 3932160;
         if (this.isSimpleLayout()) {
            if (this.layoutCache == null) {
               this.layoutCache = new LayoutCache();
               this.layoutCache.glyphs = new int[var1.length];
               this.layoutCache.advances = new float[var1.length];
            }
         } else {
            this.layoutCache = null;
         }

         float var6 = 0.0F;
         int var7 = 0;
         int var8 = 0;
         ArrayList var9 = new ArrayList();

         float var12;
         int var13;
         int var15;
         boolean var18;
         TextRun[] var20;
         for(int var10 = 0; var10 < this.runCount; ++var10) {
            TextRun var11 = this.runs[var10];
            this.shape(var11, var1, var2);
            if (var11.isTab()) {
               var12 = (float)((int)(var6 / var3) + 1) * var3;
               var11.setWidth(var12 - var6);
            }

            var12 = var11.getWidth();
            if (this.wrapWidth > 0.0F && var6 + var12 > this.wrapWidth && !var11.isLinebreak()) {
               var13 = var11.getStart() + var11.getWrapIndex(this.wrapWidth - var6);
               int var14 = var13;
               var15 = var11.getEnd();
               if (var13 + 1 < var15 && var1[var13] == ' ') {
                  var14 = var13 + 1;
               }

               int var16 = var14;
               if (var4 == null) {
                  for(boolean var17 = Character.isWhitespace(var1[var14]); var16 > var8; --var16) {
                     var18 = Character.isWhitespace(var1[var16 - 1]);
                     if (!var17 && var18) {
                        break;
                     }

                     var17 = var18;
                  }
               } else {
                  var16 = !var4.isBoundary(var14) && var1[var14] != '\t' ? var4.preceding(var14) : var14;
               }

               if (var16 < var8) {
                  var16 = var8;
               }

               int var37 = var7;

               TextRun var38;
               for(var38 = null; var37 < this.runCount; ++var37) {
                  var38 = this.runs[var37];
                  if (var38.getEnd() > var16) {
                     break;
                  }
               }

               if (var16 == var8) {
                  var38 = var11;
                  var37 = var10;
                  var16 = var13;
               }

               int var19 = var16 - var38.getStart();
               if (var19 == 0 && var37 != var7) {
                  var10 = var37 - 1;
               } else {
                  var10 = var37;
                  if (var19 == 0) {
                     ++var19;
                  }

                  if (var19 < var38.getLength()) {
                     if (this.runCount >= this.runs.length) {
                        var20 = new TextRun[this.runs.length + 64];
                        System.arraycopy(this.runs, 0, var20, 0, var37 + 1);
                        System.arraycopy(this.runs, var37 + 1, var20, var37 + 2, this.runs.length - var37 - 1);
                        this.runs = var20;
                     } else {
                        System.arraycopy(this.runs, var37 + 1, this.runs, var37 + 2, this.runCount - var37 - 1);
                     }

                     this.runs[var37 + 1] = var38.split(var19);
                     if (var38.isComplex()) {
                        this.shape(var38, var1, var2);
                     }

                     ++this.runCount;
                  }
               }

               if (var10 + 1 < this.runCount && !this.runs[var10 + 1].isLinebreak()) {
                  var11 = this.runs[var10];
                  var11.setSoftbreak();
                  this.flags |= 128;
               }
            }

            var6 += var12;
            if (var11.isBreak()) {
               TextLine var34 = this.createLine(var7, var10, var8);
               var9.add(var34);
               var7 = var10 + 1;
               var8 += var34.getLength();
               var6 = 0.0F;
            }
         }

         if (var2 != null) {
            var2.dispose();
         }

         var9.add(this.createLine(var7, this.runCount - 1, var8));
         this.lines = new TextLine[var9.size()];
         var9.toArray(this.lines);
         float var33 = Math.max(this.wrapWidth, this.layoutWidth);
         float var32 = 0.0F;
         if (this.isMirrored()) {
            var12 = 1.0F;
            if (var5 == 1048576) {
               var12 = 0.0F;
            }
         } else {
            var12 = 0.0F;
            if (var5 == 1048576) {
               var12 = 1.0F;
            }
         }

         if (var5 == 524288) {
            var12 = 0.5F;
         }

         for(var13 = 0; var13 < this.lines.length; ++var13) {
            TextLine var36 = this.lines[var13];
            var15 = var36.getStart();
            RectBounds var39 = var36.getBounds();
            float var40 = (var33 - var39.getWidth()) * var12;
            var36.setAlignment(var40);
            var18 = this.wrapWidth > 0.0F && var5 == 2097152;
            int var21;
            if (var18) {
               TextRun[] var41 = var36.getRuns();
               int var42 = var41.length;
               if (var42 > 0 && var41[var42 - 1].isSoftbreak()) {
                  var21 = var15 + var36.getLength();
                  int var22 = 0;
                  boolean var23 = false;

                  for(int var24 = var21 - 1; var24 >= var15; --var24) {
                     if (!var23 && var1[var24] != ' ') {
                        var23 = true;
                     }

                     if (var23 && var1[var24] == ' ') {
                        ++var22;
                     }
                  }

                  if (var22 != 0) {
                     float var45 = (var33 - var39.getWidth()) / (float)var22;

                     label214:
                     for(int var25 = 0; var25 < var42; ++var25) {
                        TextRun var26 = var41[var25];
                        int var27 = var26.getStart();
                        int var28 = var26.getEnd();

                        for(int var29 = var27; var29 < var28; ++var29) {
                           if (var1[var29] == ' ') {
                              var26.justify(var29 - var27, var45);
                              --var22;
                              if (var22 == 0) {
                                 break label214;
                              }
                           }
                        }
                     }

                     var40 = 0.0F;
                     var36.setAlignment(var40);
                     var36.setWidth(var33);
                  }
               }
            }

            if ((this.flags & 8) != 0) {
               this.reorderLine(var36);
            }

            this.computeSideBearings(var36);
            float var43 = var40;
            var20 = var36.getRuns();

            for(var21 = 0; var21 < var20.length; ++var21) {
               TextRun var44 = var20[var21];
               var44.setLocation(var43, var32);
               var44.setLine(var36);
               var43 += var44.getWidth();
            }

            if (var13 + 1 < this.lines.length) {
               var32 = Math.max(var32, var32 + var39.getHeight() + this.spacing);
            } else {
               var32 += var39.getHeight() - var36.getLeading();
            }
         }

         float var35 = this.lines[0].getBounds().getMinY();
         this.layoutHeight = var32;
         this.logicalBounds = this.logicalBounds.deriveWithNewBounds(0.0F, var35, 0.0F, this.layoutWidth, this.layoutHeight + var35, 0.0F);
         if (this.layoutCache != null) {
            if (this.cacheKey != null && !this.layoutCache.valid && !this.copyCache()) {
               this.layoutCache.font = this.font;
               this.layoutCache.text = this.text;
               this.layoutCache.runs = this.runs;
               this.layoutCache.runCount = this.runCount;
               this.layoutCache.lines = this.lines;
               this.layoutCache.layoutWidth = this.layoutWidth;
               this.layoutCache.layoutHeight = this.layoutHeight;
               this.layoutCache.analysis = this.flags & 2047;
               synchronized(CACHE_SIZE_LOCK) {
                  var15 = var1.length;
                  if (cacheSize + var15 > MAX_CACHE_SIZE) {
                     stringCache.clear();
                     cacheSize = 0;
                  }

                  stringCache.put(this.cacheKey, this.layoutCache);
                  cacheSize += var15;
               }
            }

            this.layoutCache.valid = true;
         }

      }
   }

   public BaseBounds getVisualBounds(int var1) {
      this.ensureLayout();
      if (this.strike == null) {
         return null;
      } else {
         boolean var2 = (var1 & 2) != 0;
         boolean var3 = (this.flags & 512) != 0;
         boolean var4 = (var1 & 4) != 0;
         boolean var5 = (this.flags & 1024) != 0;
         if (this.visualBounds != null && var2 == var3 && var4 == var5) {
            return this.visualBounds;
         } else {
            this.flags &= -1537;
            if (var2) {
               this.flags |= 512;
            }

            if (var4) {
               this.flags |= 1024;
            }

            this.visualBounds = new RectBounds();
            float var6 = Float.POSITIVE_INFINITY;
            float var7 = Float.POSITIVE_INFINITY;
            float var8 = Float.NEGATIVE_INFINITY;
            float var9 = Float.NEGATIVE_INFINITY;
            float[] var10 = new float[4];
            FontResource var11 = this.strike.getFontResource();
            Metrics var12 = this.strike.getMetrics();
            float var13 = this.strike.getSize();

            for(int var14 = 0; var14 < this.lines.length; ++var14) {
               TextLine var15 = this.lines[var14];
               TextRun[] var16 = var15.getRuns();

               for(int var17 = 0; var17 < var16.length; ++var17) {
                  TextRun var18 = var16[var17];
                  Point2D var19 = var18.getLocation();
                  if (!var18.isLinebreak()) {
                     int var20 = var18.getGlyphCount();

                     float var23;
                     float var24;
                     for(int var21 = 0; var21 < var20; ++var21) {
                        int var22 = var18.getGlyphCode(var21);
                        if (var22 != 65535) {
                           var11.getGlyphBoundingBox(var18.getGlyphCode(var21), var13, var10);
                           if (var10[0] != var10[2]) {
                              var23 = var19.x + var18.getPosX(var21);
                              var24 = var19.y + var18.getPosY(var21);
                              float var25 = var23 + var10[0];
                              float var26 = var24 - var10[3];
                              float var27 = var23 + var10[2];
                              float var28 = var24 - var10[1];
                              if (var25 < var6) {
                                 var6 = var25;
                              }

                              if (var26 < var7) {
                                 var7 = var26;
                              }

                              if (var27 > var8) {
                                 var8 = var27;
                              }

                              if (var28 > var9) {
                                 var9 = var28;
                              }
                           }
                        }
                     }

                     float var29;
                     float var30;
                     if (var2) {
                        var29 = var19.x;
                        var30 = var19.y + var12.getUnderLineOffset();
                        var23 = var29 + var18.getWidth();
                        var24 = var30 + var12.getUnderLineThickness();
                        if (var29 < var6) {
                           var6 = var29;
                        }

                        if (var30 < var7) {
                           var7 = var30;
                        }

                        if (var23 > var8) {
                           var8 = var23;
                        }

                        if (var24 > var9) {
                           var9 = var24;
                        }
                     }

                     if (var4) {
                        var29 = var19.x;
                        var30 = var19.y + var12.getStrikethroughOffset();
                        var23 = var29 + var18.getWidth();
                        var24 = var30 + var12.getStrikethroughThickness();
                        if (var29 < var6) {
                           var6 = var29;
                        }

                        if (var30 < var7) {
                           var7 = var30;
                        }

                        if (var23 > var8) {
                           var8 = var23;
                        }

                        if (var24 > var9) {
                           var9 = var24;
                        }
                     }
                  }
               }
            }

            if (var6 < var8 && var7 < var9) {
               this.visualBounds.setBounds(var6, var7, var8, var9);
            }

            return this.visualBounds;
         }
      }
   }

   private void computeSideBearings(TextLine var1) {
      TextRun[] var2 = var1.getRuns();
      if (var2.length != 0) {
         float[] var3 = new float[4];
         FontResource var4 = null;
         float var5 = 0.0F;
         if (this.strike != null) {
            var4 = this.strike.getFontResource();
            var5 = this.strike.getSize();
         }

         float var6 = 0.0F;
         float var7 = 0.0F;

         int var11;
         label70:
         for(int var8 = 0; var8 < var2.length; ++var8) {
            TextRun var9 = var2[var8];
            int var10 = var9.getGlyphCount();

            for(var11 = 0; var11 < var10; ++var11) {
               float var12 = var9.getAdvance(var11);
               if (var12 != 0.0F) {
                  int var13 = var9.getGlyphCode(var11);
                  if (var13 != 65535) {
                     FontResource var14 = var4;
                     if (var4 == null) {
                        TextSpan var15 = var9.getTextSpan();
                        PGFont var16 = (PGFont)var15.getFont();
                        var5 = var16.getSize();
                        var14 = var16.getFontResource();
                     }

                     var14.getGlyphBoundingBox(var13, var5, var3);
                     float var24 = var3[0];
                     var6 = Math.min(0.0F, var24 + var7);
                     var9.setLeftBearing();
                     break label70;
                  }
               }

               var7 += var12;
            }

            if (var10 == 0) {
               var7 += var9.getWidth();
            }
         }

         float var18 = 0.0F;
         var7 = 0.0F;

         label57:
         for(int var19 = var2.length - 1; var19 >= 0; --var19) {
            TextRun var20 = var2[var19];
            var11 = var20.getGlyphCount();

            for(int var21 = var11 - 1; var21 >= 0; --var21) {
               float var22 = var20.getAdvance(var21);
               if (var22 != 0.0F) {
                  int var23 = var20.getGlyphCode(var21);
                  if (var23 != 65535) {
                     FontResource var25 = var4;
                     if (var4 == null) {
                        TextSpan var26 = var20.getTextSpan();
                        PGFont var17 = (PGFont)var26.getFont();
                        var5 = var17.getSize();
                        var25 = var17.getFontResource();
                     }

                     var25.getGlyphBoundingBox(var23, var5, var3);
                     float var27 = var3[2] - var22;
                     var18 = Math.max(0.0F, var27 - var7);
                     var20.setRightBearing();
                     break label57;
                  }
               }

               var7 += var22;
            }

            if (var11 == 0) {
               var7 += var20.getWidth();
            }
         }

         var1.setSideBearings(var6, var18);
      }
   }

   static {
      IDENTITY = BaseTransform.IDENTITY_TRANSFORM;
      stringCache = new Hashtable();
      CACHE_SIZE_LOCK = new Object();
      cacheSize = 0;
      MAX_CACHE_SIZE = PrismFontFactory.cacheLayoutSize;
   }
}
