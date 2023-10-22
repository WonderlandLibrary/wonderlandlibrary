package cn.hanabi.gui.font.noway.ttfr;

import java.awt.Font;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.lang.ref.WeakReference;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.WeakHashMap;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class HStringCache {
   private static final int BASELINE_OFFSET = 7;
   private static final int UNDERLINE_OFFSET = 1;
   private static final int UNDERLINE_THICKNESS = 2;
   private static final int STRIKETHROUGH_OFFSET = -6;
   private static final int STRIKETHROUGH_THICKNESS = 2;
   private GlyphCache glyphCache = new GlyphCache();
   private int[] colorTable;
   private WeakHashMap stringCache = new WeakHashMap();
   private WeakHashMap weakRefCache = new WeakHashMap();
   private Key lookupKey = new Key();
   private Glyph[][] digitGlyphs = new Glyph[4][];
   private boolean digitGlyphsReady = false;
   private boolean antiAliasEnabled = false;
   private Thread mainThread = Thread.currentThread();

   public HStringCache(int[] colors) {
      this.colorTable = colors;
      this.cacheDightGlyphs();
   }

   public void setDefaultFont(String fontName, int fontSize, boolean antiAlias) {
      this.glyphCache.setDefaultFont(fontName, fontSize, antiAlias);
      this.antiAliasEnabled = antiAlias;
      this.weakRefCache.clear();
      this.stringCache.clear();
      this.cacheDightGlyphs();
   }

   public void setDefaultFont(Font font, int fontSize, boolean antiAlias) {
      this.glyphCache.setDefaultFont(font, fontSize, antiAlias);
      this.antiAliasEnabled = antiAlias;
      this.weakRefCache.clear();
      this.stringCache.clear();
      this.cacheDightGlyphs();
   }

   private void cacheDightGlyphs() {
      this.digitGlyphsReady = false;
      this.digitGlyphs[0] = this.cacheString("0123456789").glyphs;
      this.digitGlyphs[1] = this.cacheString("§l0123456789").glyphs;
      this.digitGlyphs[2] = this.cacheString("§o0123456789").glyphs;
      this.digitGlyphs[3] = this.cacheString("§l§o0123456789").glyphs;
      this.digitGlyphsReady = true;
   }

   public int getStringHeight(String str) {
      if (str != null && !str.isEmpty()) {
         Entry entry = this.cacheString(str);
         int fontHeight = 0;
         int glyphIndex = 0;

         for(int colorIndex = false; glyphIndex < entry.glyphs.length; ++glyphIndex) {
            Glyph glyph = entry.glyphs[glyphIndex];
            GlyphCache.Entry texture = glyph.texture;
            char c = str.charAt(glyph.stringIndex);
            if (c >= '0' && c <= '9') {
               texture = this.digitGlyphs[0][c - 48].texture;
            }

            if (texture.height > fontHeight) {
               fontHeight = texture.height;
            }
         }

         return fontHeight;
      } else {
         return 0;
      }
   }

   public int renderString(String str, float startX, float startY, int initialColor, boolean shadowFlag) {
      if (!shadowFlag) {
         this.renderString2(str, startX - 0.01F, startY - 0.01F, initialColor, false);
      }

      return this.renderString2(str, startX, startY, initialColor, false);
   }

   public int renderString2(String str, float startX, float startY, int initialColor, boolean shadowFlag) {
      if (str != null && !str.isEmpty()) {
         GL11.glTexEnvi(8960, 8704, 8448);
         Entry entry = this.cacheString(str);
         startY += 7.0F;
         int color = initialColor;
         GlStateManager.color((float)(initialColor >> 16 & 255) / 255.0F, (float)(initialColor >> 8 & 255) / 255.0F, (float)(initialColor & 255) / 255.0F);
         if (this.antiAliasEnabled) {
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
         }

         Tessellator tessellator = Tessellator.getInstance();
         WorldRenderer worldRenderer = tessellator.getWorldRenderer();
         worldRenderer.begin(7, DefaultVertexFormats.POSITION);
         GlStateManager.color((float)(initialColor >> 16 & 255) / 255.0F, (float)(initialColor >> 8 & 255) / 255.0F, (float)(initialColor & 255) / 255.0F);
         int fontStyle = 0;
         int glyphIndex = 0;

         int glyphIndex;
         int glyphSpace;
         float y2;
         float x2;
         float y1;
         for(glyphIndex = 0; glyphIndex < entry.glyphs.length; ++glyphIndex) {
            while(glyphIndex < entry.colors.length && entry.glyphs[glyphIndex].stringIndex >= entry.colors[glyphIndex].stringIndex) {
               color = this.applyColorCode(entry.colors[glyphIndex].colorCode, initialColor, shadowFlag);
               fontStyle = entry.colors[glyphIndex].fontStyle;
               ++glyphIndex;
            }

            Glyph glyph = entry.glyphs[glyphIndex];
            GlyphCache.Entry texture = glyph.texture;
            glyphSpace = glyph.x;
            char c = str.charAt(glyph.stringIndex);
            if (c >= '0' && c <= '9') {
               int oldWidth = texture.width;
               texture = this.digitGlyphs[fontStyle][c - 48].texture;
               int newWidth = texture.width;
               glyphSpace += oldWidth - newWidth >> 1;
            }

            GlStateManager.enableTexture2D();
            tessellator.draw();
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            GlStateManager.bindTexture(texture.textureName);
            x2 = startX + (float)glyphSpace / 2.0F;
            y1 = startX + (float)(glyphSpace + texture.width) / 2.0F;
            y2 = startY + (float)glyph.y / 2.0F;
            float y2 = startY + (float)(glyph.y + texture.height) / 2.0F;
            int a = color >> 24 & 255;
            int r = color >> 16 & 255;
            int g = color >> 8 & 255;
            int b = color & 255;
            worldRenderer.pos((double)x2, (double)y2, 0.0).tex((double)texture.u1, (double)texture.v1).color(r, g, b, a).endVertex();
            worldRenderer.pos((double)x2, (double)y2, 0.0).tex((double)texture.u1, (double)texture.v2).color(r, g, b, a).endVertex();
            worldRenderer.pos((double)y1, (double)y2, 0.0).tex((double)texture.u2, (double)texture.v2).color(r, g, b, a).endVertex();
            worldRenderer.pos((double)y1, (double)y2, 0.0).tex((double)texture.u2, (double)texture.v1).color(r, g, b, a).endVertex();
         }

         tessellator.draw();
         if (entry.specialRender) {
            int renderStyle = 0;
            GlStateManager.disableTexture2D();
            worldRenderer.begin(7, DefaultVertexFormats.POSITION);
            GlStateManager.color((float)(initialColor >> 16 & 255) / 255.0F, (float)(initialColor >> 8 & 255) / 255.0F, (float)(initialColor & 255) / 255.0F);
            glyphIndex = 0;

            for(int colorIndex = 0; glyphIndex < entry.glyphs.length; ++glyphIndex) {
               while(colorIndex < entry.colors.length && entry.glyphs[glyphIndex].stringIndex >= entry.colors[colorIndex].stringIndex) {
                  this.applyColorCode(entry.colors[colorIndex].colorCode, initialColor, shadowFlag);
                  renderStyle = entry.colors[colorIndex].renderStyle;
                  ++colorIndex;
               }

               Glyph glyph = entry.glyphs[glyphIndex];
               glyphSpace = glyph.advance - glyph.texture.width;
               float x1;
               if ((renderStyle & 1) != 0) {
                  x1 = startX + (float)(glyph.x - glyphSpace) / 2.0F;
                  x2 = startX + (float)(glyph.x + glyph.advance) / 2.0F;
                  y1 = startY + 0.5F;
                  y2 = startY + 1.5F;
                  worldRenderer.pos((double)x1, (double)y1, 0.0).endVertex();
                  worldRenderer.pos((double)x1, (double)y2, 0.0).endVertex();
                  worldRenderer.pos((double)x2, (double)y2, 0.0).endVertex();
                  worldRenderer.pos((double)x2, (double)y1, 0.0).endVertex();
               }

               if ((renderStyle & 2) != 0) {
                  x1 = startX + (float)(glyph.x - glyphSpace) / 2.0F;
                  x2 = startX + (float)(glyph.x + glyph.advance) / 2.0F;
                  y1 = startY + -3.0F;
                  y2 = startY + -2.0F;
                  worldRenderer.pos((double)x1, (double)y1, 0.0).endVertex();
                  worldRenderer.pos((double)x1, (double)y2, 0.0).endVertex();
                  worldRenderer.pos((double)x2, (double)y2, 0.0).endVertex();
                  worldRenderer.pos((double)x2, (double)y1, 0.0).endVertex();
               }
            }

            tessellator.draw();
            GlStateManager.enableTexture2D();
         }

         return entry.advance / 2;
      } else {
         return 0;
      }
   }

   public int getStringWidth(String str) {
      if (str != null && !str.isEmpty()) {
         Entry entry = this.cacheString(str);
         return entry.advance / 2;
      } else {
         return 0;
      }
   }

   private int sizeString(String str, int width, boolean breakAtSpaces) {
      if (str != null && !str.isEmpty()) {
         width += width;
         Glyph[] glyphs = this.cacheString(str).glyphs;
         int wsIndex = -1;
         int advance = 0;

         int index;
         for(index = 0; index < glyphs.length && advance <= width; ++index) {
            if (breakAtSpaces) {
               char c = str.charAt(glyphs[index].stringIndex);
               if (c == ' ') {
                  wsIndex = index;
               } else if (c == '\n') {
                  wsIndex = index;
                  break;
               }
            }

            advance += glyphs[index].advance;
         }

         if (index < glyphs.length && wsIndex != -1 && wsIndex < index) {
            index = wsIndex;
         }

         return index < glyphs.length ? glyphs[index].stringIndex : str.length();
      } else {
         return 0;
      }
   }

   public int sizeStringToWidth(String str, int width) {
      return this.sizeString(str, width, true);
   }

   public String trimStringToWidth(String str, int width, boolean reverse) {
      int length = this.sizeString(str, width, false);
      str = str.substring(0, length);
      if (reverse) {
         str = (new StringBuilder(str)).reverse().toString();
      }

      return str;
   }

   private int applyColorCode(int colorCode, int color, boolean shadowFlag) {
      if (colorCode != -1) {
         colorCode = shadowFlag ? colorCode + 16 : colorCode;
         color = this.colorTable[colorCode] & 16777215 | color & -16777216;
      }

      Tessellator.getInstance().getWorldRenderer().color(color >> 16 & 255, color >> 8 & 255, color & 255, color >> 24 & 255);
      return color;
   }

   private Entry cacheString(String str) {
      Entry entry = null;
      if (this.mainThread == Thread.currentThread()) {
         this.lookupKey.str = str;
         entry = (Entry)this.stringCache.get(this.lookupKey);
      }

      if (entry == null) {
         char[] text = str.toCharArray();
         entry = new Entry();
         int length = this.stripColorCodes(entry, str, text);
         List glyphList = new ArrayList();
         entry.advance = this.layoutBidiString(glyphList, text, 0, length, entry.colors);
         entry.glyphs = new Glyph[glyphList.size()];
         entry.glyphs = (Glyph[])glyphList.toArray(entry.glyphs);
         Arrays.sort(entry.glyphs);
         int colorIndex = 0;
         int shift = 0;

         for(int glyphIndex = 0; glyphIndex < entry.glyphs.length; ++glyphIndex) {
            Glyph glyph;
            for(glyph = entry.glyphs[glyphIndex]; colorIndex < entry.colors.length && glyph.stringIndex + shift >= entry.colors[colorIndex].stringIndex; ++colorIndex) {
               shift += 2;
            }

            glyph.stringIndex += shift;
         }

         if (this.mainThread == Thread.currentThread()) {
            Key key = new Key();
            key.str = new String(str);
            entry.keyRef = new WeakReference(key);
            this.stringCache.put(key, entry);
         }
      }

      if (this.mainThread == Thread.currentThread()) {
         Key oldKey = (Key)entry.keyRef.get();
         if (oldKey != null) {
            this.weakRefCache.put(str, oldKey);
         }

         this.lookupKey.str = null;
      }

      return entry;
   }

   private int stripColorCodes(Entry cacheEntry, String str, char[] text) {
      List colorList = new ArrayList();
      int start = 0;
      int shift = 0;
      byte fontStyle = 0;
      byte renderStyle = 0;

      int next;
      for(byte colorCode = -1; (next = str.indexOf(167, start)) != -1 && next + 1 < str.length(); shift += 2) {
         System.arraycopy(text, next - shift + 2, text, next - shift, text.length - next - 2);
         int code = "0123456789abcdefklmnor".indexOf(Character.toLowerCase(str.charAt(next + 1)));
         switch (code) {
            case 16:
               break;
            case 17:
               fontStyle = (byte)(fontStyle | 1);
               break;
            case 18:
               renderStyle = (byte)(renderStyle | 2);
               cacheEntry.specialRender = true;
               break;
            case 19:
               renderStyle = (byte)(renderStyle | 1);
               cacheEntry.specialRender = true;
               break;
            case 20:
               fontStyle = (byte)(fontStyle | 2);
               break;
            case 21:
               fontStyle = 0;
               renderStyle = 0;
               colorCode = -1;
               break;
            default:
               if (code >= 0 && code <= 15) {
                  colorCode = (byte)code;
                  fontStyle = 0;
                  renderStyle = 0;
               }
         }

         ColorCode entry = new ColorCode();
         entry.stringIndex = next;
         entry.stripIndex = next - shift;
         entry.colorCode = colorCode;
         entry.fontStyle = fontStyle;
         entry.renderStyle = renderStyle;
         colorList.add(entry);
         start = next + 2;
      }

      cacheEntry.colors = new ColorCode[colorList.size()];
      cacheEntry.colors = (ColorCode[])colorList.toArray(cacheEntry.colors);
      return text.length - shift;
   }

   private int layoutBidiString(List glyphList, char[] text, int start, int limit, ColorCode[] colors) {
      int advance = 0;
      if (!Bidi.requiresBidi(text, start, limit)) {
         return this.layoutStyle(glyphList, text, start, limit, 0, advance, colors);
      } else {
         Bidi bidi = new Bidi(text, start, (byte[])null, 0, limit - start, -2);
         if (bidi.isRightToLeft()) {
            return this.layoutStyle(glyphList, text, start, limit, 1, advance, colors);
         } else {
            int runCount = bidi.getRunCount();
            byte[] levels = new byte[runCount];
            Integer[] ranges = new Integer[runCount];

            int visualIndex;
            for(visualIndex = 0; visualIndex < runCount; ++visualIndex) {
               levels[visualIndex] = (byte)bidi.getRunLevel(visualIndex);
               ranges[visualIndex] = new Integer(visualIndex);
            }

            Bidi.reorderVisually(levels, 0, ranges, 0, runCount);

            for(visualIndex = 0; visualIndex < runCount; ++visualIndex) {
               int logicalIndex = ranges[visualIndex];
               int layoutFlag = (bidi.getRunLevel(logicalIndex) & 1) == 1 ? 1 : 0;
               advance = this.layoutStyle(glyphList, text, start + bidi.getRunStart(logicalIndex), start + bidi.getRunLimit(logicalIndex), layoutFlag, advance, colors);
            }

            return advance;
         }
      }
   }

   private int layoutStyle(List glyphList, char[] text, int start, int limit, int layoutFlags, int advance, ColorCode[] colors) {
      int currentFontStyle = 0;
      int colorIndex = Arrays.binarySearch(colors, start);
      if (colorIndex < 0) {
         colorIndex = -colorIndex - 2;
      }

      while(start < limit) {
         int next;
         for(next = limit; colorIndex >= 0 && colorIndex < colors.length - 1 && colors[colorIndex].stripIndex == colors[colorIndex + 1].stripIndex; ++colorIndex) {
         }

         if (colorIndex >= 0 && colorIndex < colors.length) {
            currentFontStyle = colors[colorIndex].fontStyle;
         }

         while(true) {
            ++colorIndex;
            if (colorIndex >= colors.length) {
               break;
            }

            if (colors[colorIndex].fontStyle != currentFontStyle) {
               next = colors[colorIndex].stripIndex;
               break;
            }
         }

         advance = this.layoutString(glyphList, text, start, next, layoutFlags, advance, currentFontStyle);
         start = next;
      }

      return advance;
   }

   private int layoutString(List glyphList, char[] text, int start, int limit, int layoutFlags, int advance, int style) {
      if (this.digitGlyphsReady) {
         for(int index = start; index < limit; ++index) {
            if (text[index] >= '0' && text[index] <= '9') {
               text[index] = '0';
            }
         }
      }

      while(start < limit) {
         Font font = this.glyphCache.lookupFont(text, start, limit, style);
         int next = font.canDisplayUpTo(text, start, limit);
         if (next == -1) {
            next = limit;
         }

         if (next == start) {
            ++next;
         }

         advance = this.layoutFont(glyphList, text, start, next, layoutFlags, advance, font);
         start = next;
      }

      return advance;
   }

   private int layoutFont(List glyphList, char[] text, int start, int limit, int layoutFlags, int advance, Font font) {
      if (this.mainThread == Thread.currentThread()) {
         this.glyphCache.cacheGlyphs(font, text, start, limit, layoutFlags);
      }

      GlyphVector vector = this.glyphCache.layoutGlyphVector(font, text, start, limit, layoutFlags);
      Glyph glyph = null;
      int numGlyphs = vector.getNumGlyphs();

      for(int index = 0; index < numGlyphs; ++index) {
         Point position = vector.getGlyphPixelBounds(index, (FontRenderContext)null, (float)advance, 0.0F).getLocation();
         if (glyph != null) {
            glyph.advance = position.x - glyph.x;
         }

         glyph = new Glyph();
         glyph.stringIndex = start + vector.getGlyphCharIndex(index);
         glyph.texture = this.glyphCache.lookupGlyph(font, vector.getGlyphCode(index));
         glyph.x = position.x;
         glyph.y = position.y;
         glyphList.add(glyph);
      }

      advance += (int)vector.getGlyphPosition(numGlyphs).getX();
      if (glyph != null) {
         glyph.advance = advance - glyph.x;
      }

      return advance;
   }

   private static class Glyph implements Comparable {
      public int stringIndex;
      public GlyphCache.Entry texture;
      public int x;
      public int y;
      public int advance;

      private Glyph() {
      }

      public int compareTo(Glyph o) {
         return this.stringIndex == o.stringIndex ? 0 : (this.stringIndex < o.stringIndex ? -1 : 1);
      }

      // $FF: synthetic method
      Glyph(Object x0) {
         this();
      }
   }

   private static class ColorCode implements Comparable {
      public static final byte UNDERLINE = 1;
      public static final byte STRIKETHROUGH = 2;
      public int stringIndex;
      public int stripIndex;
      public byte colorCode;
      public byte fontStyle;
      public byte renderStyle;

      private ColorCode() {
      }

      public int compareTo(Integer i) {
         return this.stringIndex == i ? 0 : (this.stringIndex < i ? -1 : 1);
      }

      // $FF: synthetic method
      ColorCode(Object x0) {
         this();
      }
   }

   private static class Entry {
      public WeakReference keyRef;
      public int advance;
      public Glyph[] glyphs;
      public ColorCode[] colors;
      public boolean specialRender;

      private Entry() {
      }

      // $FF: synthetic method
      Entry(Object x0) {
         this();
      }
   }

   private static class Key {
      public String str;

      private Key() {
      }

      public int hashCode() {
         int code = 0;
         int length = this.str.length();
         boolean colorCode = false;

         for(int index = 0; index < length; ++index) {
            char c = this.str.charAt(index);
            if (c >= '0' && c <= '9' && !colorCode) {
               c = '0';
            }

            code = code * 31 + c;
            colorCode = c == 167;
         }

         return code;
      }

      public boolean equals(Object o) {
         if (o == null) {
            return false;
         } else {
            String other = o.toString();
            int length = this.str.length();
            if (length != other.length()) {
               return false;
            } else {
               boolean colorCode = false;

               for(int index = 0; index < length; ++index) {
                  char c1 = this.str.charAt(index);
                  char c2 = other.charAt(index);
                  if (c1 != c2 && (c1 < '0' || c1 > '9' || c2 < '0' || c2 > '9' || colorCode)) {
                     return false;
                  }

                  colorCode = c1 == 167;
               }

               return true;
            }
         }
      }

      public String toString() {
         return this.str;
      }

      // $FF: synthetic method
      Key(Object x0) {
         this();
      }
   }
}
