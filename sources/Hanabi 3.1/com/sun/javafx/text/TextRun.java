package com.sun.javafx.text;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.scene.text.TextSpan;

public class TextRun implements GlyphList {
   int glyphCount;
   int[] gids;
   float[] positions;
   int[] charIndices;
   int start;
   int length;
   float width = -1.0F;
   byte level;
   int script;
   TextSpan span;
   TextLine line;
   Point2D location;
   private float ascent;
   private float descent;
   private float leading;
   int flags = 0;
   int slot = 0;
   static final int FLAGS_TAB = 1;
   static final int FLAGS_LINEBREAK = 2;
   static final int FLAGS_SOFTBREAK = 4;
   static final int FLAGS_NO_LINK_BEFORE = 8;
   static final int FLAGS_NO_LINK_AFTER = 16;
   static final int FLAGS_COMPLEX = 32;
   static final int FLAGS_EMBEDDED = 64;
   static final int FLAGS_SPLIT = 128;
   static final int FLAGS_SPLIT_LAST = 256;
   static final int FLAGS_LEFT_BEARING = 512;
   static final int FLAGS_RIGHT_BEARING = 1024;
   static final int FLAGS_CANONICAL = 2048;
   static final int FLAGS_COMPACT = 4096;
   float cacheWidth = 0.0F;
   int cacheIndex = 0;

   public TextRun(int var1, int var2, byte var3, boolean var4, int var5, TextSpan var6, int var7, boolean var8) {
      this.start = var1;
      this.length = var2;
      this.level = var3;
      this.script = var5;
      this.span = var6;
      this.slot = var7;
      if (var4) {
         this.flags |= 32;
      }

      if (var8) {
         this.flags |= 2048;
      }

   }

   public int getStart() {
      return this.start;
   }

   public int getEnd() {
      return this.start + this.length;
   }

   public int getLength() {
      return this.length;
   }

   public byte getLevel() {
      return this.level;
   }

   public RectBounds getLineBounds() {
      return this.line.getBounds();
   }

   public void setLine(TextLine var1) {
      this.line = var1;
   }

   public int getScript() {
      return this.script;
   }

   public TextSpan getTextSpan() {
      return this.span;
   }

   public int getSlot() {
      return this.slot;
   }

   public boolean isLinebreak() {
      return (this.flags & 2) != 0;
   }

   public boolean isCanonical() {
      return (this.flags & 2048) != 0;
   }

   public boolean isSoftbreak() {
      return (this.flags & 4) != 0;
   }

   public boolean isBreak() {
      return (this.flags & 6) != 0;
   }

   public boolean isTab() {
      return (this.flags & 1) != 0;
   }

   public boolean isEmbedded() {
      return (this.flags & 64) != 0;
   }

   public boolean isNoLinkBefore() {
      return (this.flags & 8) != 0;
   }

   public boolean isNoLinkAfter() {
      return (this.flags & 16) != 0;
   }

   public boolean isSplit() {
      return (this.flags & 128) != 0;
   }

   public boolean isSplitLast() {
      return (this.flags & 256) != 0;
   }

   public boolean isComplex() {
      return (this.flags & 32) != 0;
   }

   public boolean isLeftBearing() {
      return (this.flags & 512) != 0;
   }

   public boolean isRightBearing() {
      return (this.flags & 1024) != 0;
   }

   public boolean isLeftToRight() {
      return (this.level & 1) == 0;
   }

   public void setComplex(boolean var1) {
      if (var1) {
         this.flags |= 32;
      } else {
         this.flags &= -33;
      }

   }

   public float getWidth() {
      if (this.width != -1.0F) {
         return this.width;
      } else if (this.positions == null) {
         return 0.0F;
      } else if ((this.flags & 4096) == 0) {
         return this.positions[this.glyphCount << 1];
      } else {
         this.width = 0.0F;

         for(int var1 = 0; var1 < this.glyphCount; ++var1) {
            this.width += this.positions[this.start + var1];
         }

         return this.width;
      }
   }

   public float getHeight() {
      return -this.ascent + this.descent + this.leading;
   }

   public void setWidth(float var1) {
      this.width = var1;
   }

   public void setMetrics(float var1, float var2, float var3) {
      this.ascent = var1;
      this.descent = var2;
      this.leading = var3;
   }

   public float getAscent() {
      return this.ascent;
   }

   public float getDescent() {
      return this.descent;
   }

   public float getLeading() {
      return this.leading;
   }

   public void setLocation(float var1, float var2) {
      this.location = new Point2D(var1, var2);
   }

   public Point2D getLocation() {
      return this.location;
   }

   public void setTab() {
      this.flags |= 1;
   }

   public void setEmbedded(RectBounds var1, int var2) {
      this.width = var1.getWidth() * (float)var2;
      this.ascent = var1.getMinY();
      this.descent = var1.getHeight() + this.ascent;
      this.length = var2;
      this.flags |= 64;
   }

   public void setLinebreak() {
      this.flags |= 2;
   }

   public void setSoftbreak() {
      this.flags |= 4;
   }

   public void setLeftBearing() {
      this.flags |= 512;
   }

   public void setRightBearing() {
      this.flags |= 1024;
   }

   public int getWrapIndex(float var1) {
      if (this.glyphCount == 0) {
         return 0;
      } else {
         int var2;
         float var3;
         if (this.isLeftToRight()) {
            var2 = 0;
            if ((this.flags & 4096) != 0) {
               for(var3 = 0.0F; var2 < this.glyphCount; ++var2) {
                  var3 += this.positions[this.start + var2];
                  if (var3 > var1) {
                     return this.getCharOffset(var2);
                  }
               }
            } else {
               while(var2 < this.glyphCount) {
                  if (this.positions[var2 + 1 << 1] > var1) {
                     return this.getCharOffset(var2);
                  }

                  ++var2;
               }
            }
         } else {
            var2 = 0;

            for(var3 = this.positions[this.glyphCount << 1]; var3 > var1; ++var2) {
               float var4 = this.positions[var2 + 1 << 1] - this.positions[var2 << 1];
               if (var3 - var4 <= var1) {
                  return this.getCharOffset(var2);
               }

               var3 -= var4;
            }
         }

         return 0;
      }
   }

   public int getGlyphCount() {
      return this.glyphCount;
   }

   public int getGlyphCode(int var1) {
      if (0 <= var1 && var1 < this.glyphCount) {
         return (this.flags & 4096) != 0 ? this.gids[this.start + var1] : this.gids[var1];
      } else {
         return 65535;
      }
   }

   public float getPosX(int var1) {
      if (0 <= var1 && var1 <= this.glyphCount) {
         if ((this.flags & 4096) != 0) {
            if (this.cacheIndex == var1) {
               return this.cacheWidth;
            } else {
               float var2 = 0.0F;
               if (this.cacheIndex + 1 == var1) {
                  var2 = this.cacheWidth + this.positions[this.start + var1 - 1];
               } else {
                  for(int var3 = 0; var3 < var1; ++var3) {
                     var2 += this.positions[this.start + var3];
                  }
               }

               this.cacheIndex = var1;
               this.cacheWidth = var2;
               return var2;
            }
         } else {
            return this.positions[var1 << 1];
         }
      } else {
         return var1 == 0 ? 0.0F : this.getWidth();
      }
   }

   public float getPosY(int var1) {
      if ((this.flags & 4096) != 0) {
         return 0.0F;
      } else {
         return 0 <= var1 && var1 <= this.glyphCount ? this.positions[(var1 << 1) + 1] : 0.0F;
      }
   }

   public float getAdvance(int var1) {
      return (this.flags & 4096) != 0 ? this.positions[this.start + var1] : this.positions[var1 + 1 << 1] - this.positions[var1 << 1];
   }

   public void shape(int var1, int[] var2, float[] var3, int[] var4) {
      this.glyphCount = var1;
      this.gids = var2;
      this.positions = var3;
      this.charIndices = var4;
   }

   public void shape(int var1, int[] var2, float[] var3) {
      this.glyphCount = var1;
      this.gids = var2;
      this.positions = var3;
      this.charIndices = null;
      this.flags |= 4096;
   }

   public float getXAtOffset(int var1, boolean var2) {
      boolean var3 = this.isLeftToRight();
      if (var1 == this.length) {
         return var3 ? this.getWidth() : 0.0F;
      } else if (this.glyphCount > 0) {
         int var4 = this.getGlyphIndex(var1);
         return var3 ? this.getPosX(var4 + (var2 ? 0 : 1)) : this.getPosX(var4 + (var2 ? 1 : 0));
      } else if (this.isTab()) {
         if (var3) {
            return var2 ? 0.0F : this.getWidth();
         } else {
            return var2 ? this.getWidth() : 0.0F;
         }
      } else {
         return 0.0F;
      }
   }

   public int getGlyphAtX(float var1, int[] var2) {
      boolean var3 = this.isLeftToRight();
      float var4 = 0.0F;

      for(int var5 = 0; var5 < this.glyphCount; ++var5) {
         float var6 = this.getAdvance(var5);
         if (var4 + var6 > var1) {
            if (var2 != null) {
               if (var1 - var4 > var6 / 2.0F) {
                  var2[0] = var3 ? 1 : 0;
               } else {
                  var2[0] = var3 ? 0 : 1;
               }
            }

            return var5;
         }

         var4 += var6;
      }

      if (var2 != null) {
         var2[0] = var3 ? 1 : 0;
      }

      return Math.max(0, this.glyphCount - 1);
   }

   public int getOffsetAtX(float var1, int[] var2) {
      if (this.glyphCount > 0) {
         int var3 = this.getGlyphAtX(var1, var2);
         return this.getCharOffset(var3);
      } else {
         if (this.width != -1.0F && this.length > 0 && var2 != null && var1 > this.width / 2.0F) {
            var2[0] = 1;
         }

         return 0;
      }
   }

   private void reset() {
      this.positions = null;
      this.charIndices = null;
      this.gids = null;
      this.width = -1.0F;
      this.ascent = this.descent = this.leading = 0.0F;
      this.glyphCount = 0;
   }

   public TextRun split(int var1) {
      int var2 = this.length - var1;
      this.length = var1;
      boolean var3 = this.isComplex();
      TextRun var4 = new TextRun(this.start + this.length, var2, this.level, var3, this.script, this.span, this.slot, this.isCanonical());
      this.flags |= 16;
      var4.flags |= 8;
      this.flags |= 128;
      this.flags &= -257;
      var4.flags |= 256;
      var4.setMetrics(this.ascent, this.descent, this.leading);
      if (!var3) {
         this.glyphCount = this.length;
         if ((this.flags & 4096) != 0) {
            var4.shape(var2, this.gids, this.positions);
            if (this.width != -1.0F) {
               if (var2 > this.length) {
                  float var5 = this.width;
                  this.width = -1.0F;
                  var4.setWidth(var5 - this.getWidth());
               } else {
                  this.width -= var4.getWidth();
               }
            }
         } else {
            int[] var10 = new int[var2];
            float[] var6 = new float[var2 + 1 << 1];
            System.arraycopy(this.gids, var1, var10, 0, var2);
            float var7 = this.getWidth();
            int var8 = var1 << 1;

            for(int var9 = 2; var9 < var6.length; var9 += 2) {
               var6[var9] = this.positions[var9 + var8] - var7;
            }

            var4.shape(var2, var10, var6, (int[])null);
         }
      } else {
         this.reset();
      }

      return var4;
   }

   public void merge(TextRun var1) {
      if (var1 != null) {
         this.length += var1.length;
         this.glyphCount += var1.glyphCount;
         if (this.width != -1.0F && var1.width != -1.0F) {
            this.width += var1.width;
         } else {
            this.width = -1.0F;
         }
      }

      this.flags &= -129;
      this.flags &= -257;
   }

   public TextRun unwrap() {
      TextRun var1 = new TextRun(this.start, this.length, this.level, this.isComplex(), this.script, this.span, this.slot, this.isCanonical());
      var1.shape(this.glyphCount, this.gids, this.positions);
      var1.setWidth(this.width);
      var1.setMetrics(this.ascent, this.descent, this.leading);
      byte var2 = 28;
      var1.flags = this.flags & ~var2;
      return var1;
   }

   public void justify(int var1, float var2) {
      if (this.positions != null) {
         int var3 = this.getGlyphIndex(var1);
         if (var3 != -1) {
            for(int var4 = var3 + 1; var4 <= this.glyphCount; ++var4) {
               float[] var10000 = this.positions;
               var10000[var4 << 1] += var2;
            }

            this.width = -1.0F;
         }

         this.setComplex(true);
      }

   }

   public int getGlyphIndex(int var1) {
      if (this.charIndices == null) {
         return var1;
      } else {
         for(int var2 = 0; var2 < this.charIndices.length && var2 < this.glyphCount; ++var2) {
            if (this.charIndices[var2] == var1) {
               return var2;
            }
         }

         if (this.isLeftToRight()) {
            if (var1 > 0) {
               return this.getGlyphIndex(var1 - 1);
            }
         } else if (var1 + 1 < this.length) {
            return this.getGlyphIndex(var1 + 1);
         }

         return 0;
      }
   }

   public int getCharOffset(int var1) {
      return this.charIndices == null ? var1 : this.charIndices[var1];
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("TextRun start=");
      var1.append(this.start);
      var1.append(", length=");
      var1.append(this.length);
      var1.append(", script=");
      var1.append(this.script);
      var1.append(", linebreak=");
      var1.append(this.isLinebreak());
      var1.append(", softbreak=");
      var1.append(this.isSoftbreak());
      var1.append(", complex=");
      var1.append(this.isComplex());
      var1.append(", tab=");
      var1.append(this.isTab());
      var1.append(", compact=");
      var1.append((this.flags & 4096) != 0);
      var1.append(", ltr=");
      var1.append(this.isLeftToRight());
      var1.append(", split=");
      var1.append(this.isSplit());
      return var1.toString();
   }
}
