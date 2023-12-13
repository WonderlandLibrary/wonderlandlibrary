package cn.hanabi.gui.font.noway.ttfr;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class GlyphCache {
   private static final int TEXTURE_WIDTH = 256;
   private static final int TEXTURE_HEIGHT = 256;
   private static final int STRING_WIDTH = 256;
   private static final int STRING_HEIGHT = 64;
   private static final int GLYPH_BORDER = 1;
   private static Color BACK_COLOR = new Color(255, 255, 255, 0);
   private int fontSize = 18;
   private boolean antiAliasEnabled = false;
   private BufferedImage stringImage;
   private Graphics2D stringGraphics;
   private BufferedImage glyphCacheImage = new BufferedImage(256, 256, 2);
   private Graphics2D glyphCacheGraphics;
   private FontRenderContext fontRenderContext;
   private int[] imageData;
   private IntBuffer imageBuffer;
   private IntBuffer singleIntBuffer;
   private List allFonts;
   private List usedFonts;
   private int textureName;
   private LinkedHashMap fontCache;
   private LinkedHashMap glyphCache;
   private int cachePosX;
   private int cachePosY;
   private int cacheLineHeight;

   public GlyphCache() {
      this.glyphCacheGraphics = this.glyphCacheImage.createGraphics();
      this.fontRenderContext = this.glyphCacheGraphics.getFontRenderContext();
      this.imageData = new int[65536];
      this.imageBuffer = ByteBuffer.allocateDirect(262144).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
      this.singleIntBuffer = GLAllocation.createDirectIntBuffer(1);
      this.allFonts = Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts());
      this.usedFonts = new ArrayList();
      this.fontCache = new LinkedHashMap();
      this.glyphCache = new LinkedHashMap();
      this.cachePosX = 1;
      this.cachePosY = 1;
      this.cacheLineHeight = 0;
      this.glyphCacheGraphics.setBackground(BACK_COLOR);
      this.glyphCacheGraphics.setComposite(AlphaComposite.Src);
      this.allocateGlyphCacheTexture();
      this.allocateStringImage(256, 64);
      GraphicsEnvironment.getLocalGraphicsEnvironment().preferLocaleFonts();
      this.usedFonts.add(new Font("SansSerif", 0, 72));
   }

   void setDefaultFont(String name, int size, boolean antiAlias) {
      this.usedFonts.clear();
      this.usedFonts.add(new Font(name, 0, 72));
      this.fontSize = size;
      this.antiAliasEnabled = antiAlias;
      this.setRenderingHints();
   }

   void setDefaultFont(Font font, int size, boolean antiAlias) {
      this.usedFonts.clear();
      this.usedFonts.add(font);
      this.fontSize = size;
      this.antiAliasEnabled = antiAlias;
      this.setRenderingHints();
   }

   GlyphVector layoutGlyphVector(Font font, char[] text, int start, int limit, int layoutFlags) {
      if (!this.fontCache.containsKey(font)) {
         this.fontCache.put(font, this.fontCache.size());
      }

      return font.layoutGlyphVector(this.fontRenderContext, text, start, limit, layoutFlags);
   }

   Font lookupFont(char[] text, int start, int limit, int style) {
      Iterator iterator = this.usedFonts.iterator();

      Font font;
      do {
         if (!iterator.hasNext()) {
            iterator = this.allFonts.iterator();

            do {
               if (!iterator.hasNext()) {
                  font = (Font)this.usedFonts.get(0);
                  return font.deriveFont(style, (float)this.fontSize);
               }

               font = (Font)iterator.next();
            } while(font.canDisplayUpTo(text, start, limit) == start);

            this.usedFonts.add(font);
            return font.deriveFont(style, (float)this.fontSize);
         }

         font = (Font)iterator.next();
      } while(font.canDisplayUpTo(text, start, limit) == start);

      return font.deriveFont(style, (float)this.fontSize);
   }

   Entry lookupGlyph(Font font, int glyphCode) {
      long fontKey = (long)(Integer)this.fontCache.get(font) << 32;
      return (Entry)this.glyphCache.get(fontKey | (long)glyphCode);
   }

   void cacheGlyphs(Font font, char[] text, int start, int limit, int layoutFlags) {
      GlyphVector vector = this.layoutGlyphVector(font, text, start, limit, layoutFlags);
      Rectangle vectorBounds = null;
      long fontKey = (long)(Integer)this.fontCache.get(font) << 32;
      int numGlyphs = vector.getNumGlyphs();
      Rectangle dirty = null;
      boolean vectorRendered = false;

      for(int index = 0; index < numGlyphs; ++index) {
         int glyphCode = vector.getGlyphCode(index);
         if (!this.glyphCache.containsKey(fontKey | (long)glyphCode)) {
            if (!vectorRendered) {
               vectorRendered = true;

               int width;
               for(width = 0; width < numGlyphs; ++width) {
                  Point2D pos = vector.getGlyphPosition(width);
                  pos.setLocation(pos.getX() + (double)(2 * width), pos.getY());
                  vector.setGlyphPosition(width, pos);
               }

               vectorBounds = vector.getPixelBounds(this.fontRenderContext, 0.0F, 0.0F);
               if (this.stringImage == null || vectorBounds.width > this.stringImage.getWidth() || vectorBounds.height > this.stringImage.getHeight()) {
                  width = Math.max(vectorBounds.width, this.stringImage.getWidth());
                  int height = Math.max(vectorBounds.height, this.stringImage.getHeight());
                  this.allocateStringImage(width, height);
               }

               this.stringGraphics.clearRect(0, 0, vectorBounds.width, vectorBounds.height);
               this.stringGraphics.drawGlyphVector(vector, (float)(-vectorBounds.x), (float)(-vectorBounds.y));
            }

            Rectangle rect = vector.getGlyphPixelBounds(index, (FontRenderContext)null, (float)(-vectorBounds.x), (float)(-vectorBounds.y));
            if (this.cachePosX + rect.width + 1 > 256) {
               this.cachePosX = 1;
               this.cachePosY += this.cacheLineHeight + 1;
               this.cacheLineHeight = 0;
            }

            if (this.cachePosY + rect.height + 1 > 256) {
               this.updateTexture(dirty);
               dirty = null;
               this.allocateGlyphCacheTexture();
               this.cachePosY = this.cachePosX = 1;
               this.cacheLineHeight = 0;
            }

            if (rect.height > this.cacheLineHeight) {
               this.cacheLineHeight = rect.height;
            }

            this.glyphCacheGraphics.drawImage(this.stringImage, this.cachePosX, this.cachePosY, this.cachePosX + rect.width, this.cachePosY + rect.height, rect.x, rect.y, rect.x + rect.width, rect.y + rect.height, (ImageObserver)null);
            rect.setLocation(this.cachePosX, this.cachePosY);
            Entry entry = new Entry();
            entry.textureName = this.textureName;
            entry.width = rect.width;
            entry.height = rect.height;
            entry.u1 = (float)rect.x / 256.0F;
            entry.v1 = (float)rect.y / 256.0F;
            entry.u2 = (float)(rect.x + rect.width) / 256.0F;
            entry.v2 = (float)(rect.y + rect.height) / 256.0F;
            this.glyphCache.put(fontKey | (long)glyphCode, entry);
            if (dirty == null) {
               dirty = new Rectangle(this.cachePosX, this.cachePosY, rect.width, rect.height);
            } else {
               dirty.add(rect);
            }

            this.cachePosX += rect.width + 1;
         }
      }

      this.updateTexture(dirty);
   }

   private void updateTexture(Rectangle dirty) {
      if (dirty != null) {
         this.updateImageBuffer(dirty.x, dirty.y, dirty.width, dirty.height);
         GlStateManager.bindTexture(this.textureName);
         GL11.glTexSubImage2D(3553, 0, dirty.x, dirty.y, dirty.width, dirty.height, 6408, 5121, this.imageBuffer);
      }

   }

   private void allocateStringImage(int width, int height) {
      this.stringImage = new BufferedImage(width, height, 2);
      this.stringGraphics = this.stringImage.createGraphics();
      this.setRenderingHints();
      this.stringGraphics.setBackground(BACK_COLOR);
      this.stringGraphics.setPaint(Color.WHITE);
   }

   private void setRenderingHints() {
      this.stringGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, this.antiAliasEnabled ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
      this.stringGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, this.antiAliasEnabled ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
      this.stringGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
   }

   private void allocateGlyphCacheTexture() {
      this.glyphCacheGraphics.clearRect(0, 0, 256, 256);
      this.singleIntBuffer.clear();
      GL11.glGenTextures(this.singleIntBuffer);
      this.textureName = this.singleIntBuffer.get(0);
      this.updateImageBuffer(0, 0, 256, 256);
      GlStateManager.bindTexture(this.textureName);
      GL11.glTexImage2D(3553, 0, 32828, 256, 256, 0, 6408, 5121, this.imageBuffer);
      GL11.glTexParameteri(3553, 10241, 9728);
      GL11.glTexParameteri(3553, 10240, 9728);
   }

   private void updateImageBuffer(int x, int y, int width, int height) {
      this.glyphCacheImage.getRGB(x, y, width, height, this.imageData, 0, width);

      for(int i = 0; i < width * height; ++i) {
         int color = this.imageData[i];
         this.imageData[i] = color << 8 | color >>> 24;
      }

      this.imageBuffer.clear();
      this.imageBuffer.put(this.imageData);
      this.imageBuffer.flip();
   }

   static class Entry {
      public int textureName;
      public int width;
      public int height;
      public float u1;
      public float v1;
      public float u2;
      public float v2;
   }
}
