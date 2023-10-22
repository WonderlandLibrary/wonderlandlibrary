package me.theresa.fontRenderer.font;

import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import me.theresa.fontRenderer.font.impl.Font;
import me.theresa.fontRenderer.font.opengl.Texture;
import me.theresa.fontRenderer.font.opengl.TextureImpl;
import me.theresa.fontRenderer.font.opengl.renderer.Renderer;
import me.theresa.fontRenderer.font.opengl.renderer.SGL;
import me.theresa.fontRenderer.font.util.HieroSettings;
import me.theresa.fontRenderer.font.util.ResourceLoader;

public class UnicodeFont implements Font {
   private static final int DISPLAY_LIST_CACHE_SIZE = 200;
   private static final int MAX_GLYPH_CODE = 1114111;
   private static final int PAGE_SIZE = 512;
   private static final int PAGES = 2175;
   private static final SGL GL = Renderer.get();
   private static final DisplayList EMPTY_DISPLAY_LIST = new DisplayList();
   private static final Comparator heightComparator = (o1, o2) -> {
      return ((Glyph)o1).getHeight() - ((Glyph)o2).getHeight();
   };
   private final Glyph[][] glyphs;
   private final List glyphPages;
   private final List queuedGlyphs;
   private final List effects;
   private java.awt.Font font;
   private String ttfFileRef;
   private int ascent;
   private int descent;
   private int leading;
   private int spaceWidth;
   private int paddingTop;
   private int paddingLeft;
   private int paddingBottom;
   private int paddingRight;
   private int paddingAdvanceX;
   private int paddingAdvanceY;
   private Glyph missingGlyph;
   private int glyphPageWidth;
   private int glyphPageHeight;
   private boolean displayListCaching;
   private int baseDisplayListID;
   private int eldestDisplayListID;
   private final LinkedHashMap displayLists;
   private DisplayList eldestDisplayList;

   public UnicodeFont(String ttfFileRef, String hieroFileRef) throws SlickException {
      this(ttfFileRef, new HieroSettings(hieroFileRef));
   }

   public UnicodeFont(String ttfFileRef, HieroSettings settings) throws SlickException {
      this.glyphs = new Glyph[2175][];
      this.glyphPages = new ArrayList();
      this.queuedGlyphs = new ArrayList(256);
      this.effects = new ArrayList();
      this.glyphPageWidth = 512;
      this.glyphPageHeight = 512;
      this.displayListCaching = true;
      this.baseDisplayListID = -1;
      this.displayLists = new LinkedHashMap(200, 1.0F, true) {
         protected boolean removeEldestEntry(Map.Entry eldest) {
            DisplayList displayList = (DisplayList)eldest.getValue();
            if (displayList != null) {
               UnicodeFont.this.eldestDisplayListID = displayList.id;
            }

            return this.size() > 200;
         }
      };
      this.ttfFileRef = ttfFileRef;
      java.awt.Font font = createFont(ttfFileRef);
      this.initializeFont(font, settings.getFontSize(), settings.isBold(), settings.isItalic());
      this.loadSettings(settings);
   }

   public UnicodeFont(String ttfFileRef, int size, boolean bold, boolean italic) throws SlickException {
      this.glyphs = new Glyph[2175][];
      this.glyphPages = new ArrayList();
      this.queuedGlyphs = new ArrayList(256);
      this.effects = new ArrayList();
      this.glyphPageWidth = 512;
      this.glyphPageHeight = 512;
      this.displayListCaching = true;
      this.baseDisplayListID = -1;
      this.displayLists = new LinkedHashMap(200, 1.0F, true) {
         protected boolean removeEldestEntry(Map.Entry eldest) {
            DisplayList displayList = (DisplayList)eldest.getValue();
            if (displayList != null) {
               UnicodeFont.this.eldestDisplayListID = displayList.id;
            }

            return this.size() > 200;
         }
      };
      this.ttfFileRef = ttfFileRef;
      this.initializeFont(createFont(ttfFileRef), size, bold, italic);
   }

   public UnicodeFont(java.awt.Font font, String hieroFileRef) throws SlickException {
      this(font, new HieroSettings(hieroFileRef));
   }

   public UnicodeFont(java.awt.Font font, HieroSettings settings) {
      this.glyphs = new Glyph[2175][];
      this.glyphPages = new ArrayList();
      this.queuedGlyphs = new ArrayList(256);
      this.effects = new ArrayList();
      this.glyphPageWidth = 512;
      this.glyphPageHeight = 512;
      this.displayListCaching = true;
      this.baseDisplayListID = -1;
      this.displayLists = new LinkedHashMap(200, 1.0F, true) {
         protected boolean removeEldestEntry(Map.Entry eldest) {
            DisplayList displayList = (DisplayList)eldest.getValue();
            if (displayList != null) {
               UnicodeFont.this.eldestDisplayListID = displayList.id;
            }

            return this.size() > 200;
         }
      };
      this.initializeFont(font, settings.getFontSize(), settings.isBold(), settings.isItalic());
      this.loadSettings(settings);
   }

   public UnicodeFont(java.awt.Font font) {
      this.glyphs = new Glyph[2175][];
      this.glyphPages = new ArrayList();
      this.queuedGlyphs = new ArrayList(256);
      this.effects = new ArrayList();
      this.glyphPageWidth = 512;
      this.glyphPageHeight = 512;
      this.displayListCaching = true;
      this.baseDisplayListID = -1;
      this.displayLists = new LinkedHashMap(200, 1.0F, true) {
         protected boolean removeEldestEntry(Map.Entry eldest) {
            DisplayList displayList = (DisplayList)eldest.getValue();
            if (displayList != null) {
               UnicodeFont.this.eldestDisplayListID = displayList.id;
            }

            return this.size() > 200;
         }
      };
      this.initializeFont(font, font.getSize(), font.isBold(), font.isItalic());
   }

   public UnicodeFont(java.awt.Font font, int size, boolean bold, boolean italic) {
      this.glyphs = new Glyph[2175][];
      this.glyphPages = new ArrayList();
      this.queuedGlyphs = new ArrayList(256);
      this.effects = new ArrayList();
      this.glyphPageWidth = 512;
      this.glyphPageHeight = 512;
      this.displayListCaching = true;
      this.baseDisplayListID = -1;
      this.displayLists = new LinkedHashMap(200, 1.0F, true) {
         protected boolean removeEldestEntry(Map.Entry eldest) {
            DisplayList displayList = (DisplayList)eldest.getValue();
            if (displayList != null) {
               UnicodeFont.this.eldestDisplayListID = displayList.id;
            }

            return this.size() > 200;
         }
      };
      this.initializeFont(font, size, bold, italic);
   }

   private static java.awt.Font createFont(String ttfFileRef) throws SlickException {
      try {
         return java.awt.Font.createFont(0, ResourceLoader.getResourceAsStream(ttfFileRef));
      } catch (FontFormatException var2) {
         throw new SlickException("Invalid font: " + ttfFileRef, var2);
      } catch (IOException var3) {
         throw new SlickException("Error reading font: " + ttfFileRef, var3);
      }
   }

   private void initializeFont(java.awt.Font baseFont, int size, boolean bold, boolean italic) {
      Map attributes = baseFont.getAttributes();
      attributes.put(TextAttribute.SIZE, (float)size);
      attributes.put(TextAttribute.WEIGHT, bold ? TextAttribute.WEIGHT_BOLD : TextAttribute.WEIGHT_REGULAR);
      attributes.put(TextAttribute.POSTURE, italic ? TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR);

      try {
         attributes.put(TextAttribute.class.getDeclaredField("KERNING").get((Object)null), TextAttribute.class.getDeclaredField("KERNING_ON").get((Object)null));
      } catch (Exception var9) {
      }

      this.font = baseFont.deriveFont(attributes);
      FontMetrics metrics = GlyphPage.getScratchGraphics().getFontMetrics(this.font);
      this.ascent = metrics.getAscent();
      this.descent = metrics.getDescent();
      this.leading = metrics.getLeading();
      char[] chars = " ".toCharArray();
      GlyphVector vector = this.font.layoutGlyphVector(GlyphPage.renderContext, chars, 0, chars.length, 0);
      this.spaceWidth = vector.getGlyphLogicalBounds(0).getBounds().width;
   }

   private void loadSettings(HieroSettings settings) {
      this.paddingTop = settings.getPaddingTop();
      this.paddingLeft = settings.getPaddingLeft();
      this.paddingBottom = settings.getPaddingBottom();
      this.paddingRight = settings.getPaddingRight();
      this.paddingAdvanceX = settings.getPaddingAdvanceX();
      this.paddingAdvanceY = settings.getPaddingAdvanceY();
      this.glyphPageWidth = settings.getGlyphPageWidth();
      this.glyphPageHeight = settings.getGlyphPageHeight();
      this.effects.addAll(settings.getEffects());
   }

   public void addGlyphs(int startCodePoint, int endCodePoint) {
      for(int codePoint = startCodePoint; codePoint <= endCodePoint; ++codePoint) {
         this.addGlyphs(new String(Character.toChars(codePoint)));
      }

   }

   public void addGlyphs(String text) {
      if (text == null) {
         throw new IllegalArgumentException("text cannot be null.");
      } else {
         char[] chars = text.toCharArray();
         GlyphVector vector = this.font.layoutGlyphVector(GlyphPage.renderContext, chars, 0, chars.length, 0);
         int i = 0;

         for(int n = vector.getNumGlyphs(); i < n; ++i) {
            int codePoint = text.codePointAt(vector.getGlyphCharIndex(i));
            Rectangle bounds = this.getGlyphBounds(vector, i, codePoint);
            this.getGlyph(vector.getGlyphCode(i), codePoint, bounds, vector, i);
         }

      }
   }

   public void addAsciiGlyphs() {
      this.addGlyphs(32, 255);
   }

   public void addNeheGlyphs() {
      this.addGlyphs(32, 128);
   }

   public boolean loadGlyphs() throws SlickException {
      return this.loadGlyphs(-1);
   }

   public boolean loadGlyphs(int maxGlyphsToLoad) throws SlickException {
      if (this.queuedGlyphs.isEmpty()) {
         return false;
      } else if (this.effects.isEmpty()) {
         throw new IllegalStateException("The UnicodeFont must have at least one effect before any glyphs can be loaded.");
      } else {
         Iterator iter = this.queuedGlyphs.iterator();

         while(true) {
            while(iter.hasNext()) {
               Glyph glyph = (Glyph)iter.next();
               int codePoint = glyph.getCodePoint();
               if (glyph.getWidth() != 0 && codePoint != 32) {
                  if (glyph.isMissing()) {
                     if (this.missingGlyph != null) {
                        if (glyph != this.missingGlyph) {
                           iter.remove();
                        }
                     } else {
                        this.missingGlyph = glyph;
                     }
                  }
               } else {
                  iter.remove();
               }
            }

            this.queuedGlyphs.sort(heightComparator);
            iter = this.glyphPages.iterator();

            do {
               if (!iter.hasNext()) {
                  do {
                     if (this.queuedGlyphs.isEmpty()) {
                        return true;
                     }

                     GlyphPage glyphPage = new GlyphPage(this, this.glyphPageWidth, this.glyphPageHeight);
                     this.glyphPages.add(glyphPage);
                     maxGlyphsToLoad -= glyphPage.loadGlyphs(this.queuedGlyphs, maxGlyphsToLoad);
                  } while(maxGlyphsToLoad != 0);

                  return true;
               }

               Object page = iter.next();
               GlyphPage glyphPage = (GlyphPage)page;
               maxGlyphsToLoad -= glyphPage.loadGlyphs(this.queuedGlyphs, maxGlyphsToLoad);
            } while(maxGlyphsToLoad != 0 && !this.queuedGlyphs.isEmpty());

            return true;
         }
      }
   }

   public void clearGlyphs() {
      for(int i = 0; i < 2175; ++i) {
         this.glyphs[i] = null;
      }

      Iterator var6 = this.glyphPages.iterator();

      while(var6.hasNext()) {
         Object glyphPage = var6.next();
         GlyphPage page = (GlyphPage)glyphPage;

         try {
            page.getImage().destroy();
         } catch (SlickException var5) {
         }
      }

      this.glyphPages.clear();
      if (this.baseDisplayListID != -1) {
         GL.glDeleteLists(this.baseDisplayListID, this.displayLists.size());
         this.baseDisplayListID = -1;
      }

      this.queuedGlyphs.clear();
      this.missingGlyph = null;
   }

   public void destroy() {
      this.clearGlyphs();
   }

   public DisplayList drawDisplayList(float x, float y, String text, Color color, int startIndex, int endIndex) {
      if (text == null) {
         throw new IllegalArgumentException("text cannot be null.");
      } else if (text.length() == 0) {
         return EMPTY_DISPLAY_LIST;
      } else if (color == null) {
         throw new IllegalArgumentException("color cannot be null.");
      } else {
         x -= (float)this.paddingLeft;
         y -= (float)this.paddingTop;
         String displayListKey = text.substring(startIndex, endIndex);
         color.bind();
         TextureImpl.bindNone();
         DisplayList displayList = null;
         if (this.displayListCaching && this.queuedGlyphs.isEmpty()) {
            if (this.baseDisplayListID == -1) {
               this.baseDisplayListID = GL.glGenLists(200);
               if (this.baseDisplayListID == 0) {
                  this.baseDisplayListID = -1;
                  this.displayListCaching = false;
                  return new DisplayList();
               }
            }

            displayList = (DisplayList)this.displayLists.get(displayListKey);
            if (displayList != null) {
               if (!displayList.invalid) {
                  GL.glTranslatef(x, y, 0.0F);
                  GL.glCallList(displayList.id);
                  GL.glTranslatef(-x, -y, 0.0F);
                  return displayList;
               }

               displayList.invalid = false;
            } else if (displayList == null) {
               displayList = new DisplayList();
               int displayListCount = this.displayLists.size();
               this.displayLists.put(displayListKey, displayList);
               if (displayListCount < 200) {
                  displayList.id = this.baseDisplayListID + displayListCount;
               } else {
                  displayList.id = this.eldestDisplayListID;
               }
            }

            this.displayLists.put(displayListKey, displayList);
         }

         GL.glTranslatef(x, y, 0.0F);
         if (displayList != null) {
            GL.glNewList(displayList.id, 4865);
         }

         char[] chars = text.substring(0, endIndex).toCharArray();
         GlyphVector vector = this.font.layoutGlyphVector(GlyphPage.renderContext, chars, 0, chars.length, 0);
         int maxWidth = 0;
         int totalHeight = 0;
         int lines = 0;
         int extraX = 0;
         int extraY = this.ascent;
         boolean startNewLine = false;
         Texture lastBind = null;
         int glyphIndex = 0;

         for(int n = vector.getNumGlyphs(); glyphIndex < n; ++glyphIndex) {
            int charIndex = vector.getGlyphCharIndex(glyphIndex);
            if (charIndex >= startIndex) {
               if (charIndex > endIndex) {
                  break;
               }

               int codePoint = text.codePointAt(charIndex);
               Rectangle bounds = this.getGlyphBounds(vector, glyphIndex, codePoint);
               Glyph glyph = this.getGlyph(vector.getGlyphCode(glyphIndex), codePoint, bounds, vector, glyphIndex);
               if (startNewLine && codePoint != 10) {
                  extraX = -bounds.x;
                  startNewLine = false;
               }

               Image image = glyph.getImage();
               if (image == null && this.missingGlyph != null && glyph.isMissing()) {
                  image = this.missingGlyph.getImage();
               }

               if (image != null) {
                  Texture texture = image.getTexture();
                  if (lastBind != null && lastBind != texture) {
                     GL.glEnd();
                     lastBind = null;
                  }

                  if (lastBind == null) {
                     texture.bind();
                     GL.glBegin(7);
                     lastBind = texture;
                  }

                  image.drawEmbedded((float)(bounds.x + extraX), (float)(bounds.y + extraY), (float)image.getWidth(), (float)image.getHeight());
               }

               if (glyphIndex >= 0) {
                  extraX += this.paddingRight + this.paddingLeft + this.paddingAdvanceX;
               }

               maxWidth = Math.max(maxWidth, bounds.x + extraX + bounds.width);
               totalHeight = Math.max(totalHeight, this.ascent + bounds.y + bounds.height);
               if (codePoint == 10) {
                  startNewLine = true;
                  extraY += this.getLineHeight();
                  ++lines;
                  totalHeight = 0;
               }
            }
         }

         if (lastBind != null) {
            GL.glEnd();
         }

         if (displayList != null) {
            GL.glEndList();
            if (!this.queuedGlyphs.isEmpty()) {
               displayList.invalid = true;
            }
         }

         GL.glTranslatef(-x, -y, 0.0F);
         if (displayList == null) {
            displayList = new DisplayList();
         }

         displayList.width = (short)maxWidth;
         displayList.height = (short)(lines * this.getLineHeight() + totalHeight);
         return displayList;
      }
   }

   public void drawString(float x, float y, String text, Color color, int startIndex, int endIndex) {
      this.drawDisplayList(x, y, text, color, startIndex, endIndex);
   }

   public void drawString(float x, float y, String text) {
      this.drawString(x, y, text, Color.white);
   }

   public void drawString(float x, float y, String text, Color col) {
      this.drawString(x, y, text, col, 0, text.length());
   }

   private Glyph getGlyph(int glyphCode, int codePoint, Rectangle bounds, GlyphVector vector, int index) {
      if (glyphCode >= 0 && glyphCode < 1114111) {
         int pageIndex = glyphCode / 512;
         int glyphIndex = glyphCode & 511;
         Glyph glyph = null;
         Glyph[] page = this.glyphs[pageIndex];
         if (page != null) {
            glyph = page[glyphIndex];
            if (glyph != null) {
               return glyph;
            }
         } else {
            page = this.glyphs[pageIndex] = new Glyph[512];
         }

         glyph = page[glyphIndex] = new Glyph(codePoint, bounds, vector, index, this);
         this.queuedGlyphs.add(glyph);
         return glyph;
      } else {
         return new Glyph(codePoint, bounds, vector, index, this) {
            public boolean isMissing() {
               return true;
            }
         };
      }
   }

   private Rectangle getGlyphBounds(GlyphVector vector, int index, int codePoint) {
      Rectangle bounds = vector.getGlyphPixelBounds(index, GlyphPage.renderContext, 0.0F, 0.0F);
      if (codePoint == 32) {
         bounds.width = this.spaceWidth;
      }

      return bounds;
   }

   public int getSpaceWidth() {
      return this.spaceWidth;
   }

   public int getWidth(String text) {
      if (text == null) {
         throw new IllegalArgumentException("text cannot be null.");
      } else if (text.length() == 0) {
         return 0;
      } else {
         if (this.displayListCaching) {
            DisplayList displayList = (DisplayList)this.displayLists.get(text);
            if (displayList != null) {
               return displayList.width;
            }
         }

         char[] chars = text.toCharArray();
         GlyphVector vector = this.font.layoutGlyphVector(GlyphPage.renderContext, chars, 0, chars.length, 0);
         int width = 0;
         int extraX = 0;
         boolean startNewLine = false;
         int glyphIndex = 0;

         for(int n = vector.getNumGlyphs(); glyphIndex < n; ++glyphIndex) {
            int charIndex = vector.getGlyphCharIndex(glyphIndex);
            int codePoint = text.codePointAt(charIndex);
            Rectangle bounds = this.getGlyphBounds(vector, glyphIndex, codePoint);
            if (startNewLine && codePoint != 10) {
               extraX = -bounds.x;
            }

            if (glyphIndex > 0) {
               extraX += this.paddingLeft + this.paddingRight + this.paddingAdvanceX;
            }

            width = Math.max(width, bounds.x + extraX + bounds.width);
            if (codePoint == 10) {
               startNewLine = true;
            }
         }

         return width;
      }
   }

   public int getHeight(String text) {
      if (text == null) {
         throw new IllegalArgumentException("text cannot be null.");
      } else if (text.length() == 0) {
         return 0;
      } else {
         if (this.displayListCaching) {
            DisplayList displayList = (DisplayList)this.displayLists.get(text);
            if (displayList != null) {
               return displayList.height;
            }
         }

         char[] chars = text.toCharArray();
         GlyphVector vector = this.font.layoutGlyphVector(GlyphPage.renderContext, chars, 0, chars.length, 0);
         int lines = 0;
         int height = 0;
         int i = 0;

         for(int n = vector.getNumGlyphs(); i < n; ++i) {
            int charIndex = vector.getGlyphCharIndex(i);
            int codePoint = text.codePointAt(charIndex);
            if (codePoint != 32) {
               Rectangle bounds = this.getGlyphBounds(vector, i, codePoint);
               height = Math.max(height, this.ascent + bounds.y + bounds.height);
               if (codePoint == 10) {
                  ++lines;
                  height = 0;
               }
            }
         }

         return lines * this.getLineHeight() + height;
      }
   }

   public int getYOffset(String text) {
      if (text == null) {
         throw new IllegalArgumentException("text cannot be null.");
      } else {
         DisplayList displayList = null;
         if (this.displayListCaching) {
            displayList = (DisplayList)this.displayLists.get(text);
            if (displayList != null && displayList.yOffset != null) {
               return displayList.yOffset.intValue();
            }
         }

         int index = text.indexOf(10);
         if (index != -1) {
            text = text.substring(0, index);
         }

         char[] chars = text.toCharArray();
         GlyphVector vector = this.font.layoutGlyphVector(GlyphPage.renderContext, chars, 0, chars.length, 0);
         int yOffset = this.ascent + vector.getPixelBounds((FontRenderContext)null, 0.0F, 0.0F).y;
         if (displayList != null) {
            displayList.yOffset = (short)yOffset;
         }

         return yOffset;
      }
   }

   public java.awt.Font getFont() {
      return this.font;
   }

   public int getPaddingTop() {
      return this.paddingTop;
   }

   public void setPaddingTop(int paddingTop) {
      this.paddingTop = paddingTop;
   }

   public int getPaddingLeft() {
      return this.paddingLeft;
   }

   public void setPaddingLeft(int paddingLeft) {
      this.paddingLeft = paddingLeft;
   }

   public int getPaddingBottom() {
      return this.paddingBottom;
   }

   public void setPaddingBottom(int paddingBottom) {
      this.paddingBottom = paddingBottom;
   }

   public int getPaddingRight() {
      return this.paddingRight;
   }

   public void setPaddingRight(int paddingRight) {
      this.paddingRight = paddingRight;
   }

   public int getPaddingAdvanceX() {
      return this.paddingAdvanceX;
   }

   public void setPaddingAdvanceX(int paddingAdvanceX) {
      this.paddingAdvanceX = paddingAdvanceX;
   }

   public int getPaddingAdvanceY() {
      return this.paddingAdvanceY;
   }

   public void setPaddingAdvanceY(int paddingAdvanceY) {
      this.paddingAdvanceY = paddingAdvanceY;
   }

   public int getLineHeight() {
      return this.descent + this.ascent + this.leading + this.paddingTop + this.paddingBottom + this.paddingAdvanceY;
   }

   public int getAscent() {
      return this.ascent;
   }

   public int getDescent() {
      return this.descent;
   }

   public int getLeading() {
      return this.leading;
   }

   public int getGlyphPageWidth() {
      return this.glyphPageWidth;
   }

   public void setGlyphPageWidth(int glyphPageWidth) {
      this.glyphPageWidth = glyphPageWidth;
   }

   public int getGlyphPageHeight() {
      return this.glyphPageHeight;
   }

   public void setGlyphPageHeight(int glyphPageHeight) {
      this.glyphPageHeight = glyphPageHeight;
   }

   public List getGlyphPages() {
      return this.glyphPages;
   }

   public List getEffects() {
      return this.effects;
   }

   public boolean isCaching() {
      return this.displayListCaching;
   }

   public void setDisplayListCaching(boolean displayListCaching) {
      this.displayListCaching = displayListCaching;
   }

   public String getFontFile() {
      if (this.ttfFileRef == null) {
         try {
            Object font2D = Class.forName("sun.font.FontManager").getDeclaredMethod("getFont2D", java.awt.Font.class).invoke((Object)null, this.font);
            Field platNameField = Class.forName("sun.font.PhysicalFont").getDeclaredField("platName");
            platNameField.setAccessible(true);
            this.ttfFileRef = (String)platNameField.get(font2D);
         } catch (Throwable var3) {
         }

         if (this.ttfFileRef == null) {
            this.ttfFileRef = "";
         }
      }

      return this.ttfFileRef.length() == 0 ? null : this.ttfFileRef;
   }

   public static class DisplayList {
      public short width;
      public short height;
      public Object userData;
      boolean invalid;
      int id;
      Short yOffset;

      DisplayList() {
      }
   }
}
