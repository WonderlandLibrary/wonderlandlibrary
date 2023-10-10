package org.newdawn.slick;

import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.newdawn.slick.font.Glyph;
import org.newdawn.slick.font.GlyphPage;
import org.newdawn.slick.font.HieroSettings;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.util.ResourceLoader;

public class UnicodeFont implements Font {
   private static final int DISPLAY_LIST_CACHE_SIZE = 200;
   private static final int MAX_GLYPH_CODE = 1114111;
   private static final int PAGE_SIZE = 512;
   private static final int PAGES = 2175;
   private static final SGL GL = Renderer.get();
   private static final UnicodeFont.DisplayList EMPTY_DISPLAY_LIST = new UnicodeFont.DisplayList();
   private static final Comparator heightComparator = new Comparator() {
      public int compare(Object var1, Object var2) {
         return ((Glyph)var1).getHeight() - ((Glyph)var2).getHeight();
      }
   };
   private java.awt.Font font;
   private String ttfFileRef;
   private int ascent;
   private int descent;
   private int leading;
   private int spaceWidth;
   private final Glyph[][] glyphs;
   private final List glyphPages;
   private final List queuedGlyphs;
   private final List effects;
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
   private UnicodeFont.DisplayList eldestDisplayList;
   private final LinkedHashMap displayLists;
   static Class class$java$awt$font$TextAttribute;
   static Class class$java$awt$Font;

   private static java.awt.Font createFont(String var0) throws SlickException {
      try {
         return java.awt.Font.createFont(0, ResourceLoader.getResourceAsStream(var0));
      } catch (FontFormatException var2) {
         throw new SlickException("Invalid font: " + var0, var2);
      } catch (IOException var3) {
         throw new SlickException("Error reading font: " + var0, var3);
      }
   }

   public UnicodeFont(String var1, String var2) throws SlickException {
      this(var1, new HieroSettings(var2));
   }

   public UnicodeFont(String var1, HieroSettings var2) throws SlickException {
      this.glyphs = new Glyph[2175][];
      this.glyphPages = new ArrayList();
      this.queuedGlyphs = new ArrayList(256);
      this.effects = new ArrayList();
      this.glyphPageWidth = 512;
      this.glyphPageHeight = 512;
      this.displayListCaching = true;
      this.baseDisplayListID = -1;
      this.displayLists = new LinkedHashMap(this, 200, 1.0F, true) {
         private final UnicodeFont this$0;

         {
            this.this$0 = var1;
         }

         protected boolean removeEldestEntry(Entry var1) {
            UnicodeFont.DisplayList var2 = (UnicodeFont.DisplayList)var1.getValue();
            if (var2 != null) {
               UnicodeFont.access$002(this.this$0, var2.id);
            }

            return this.size() > 200;
         }
      };
      this.ttfFileRef = var1;
      java.awt.Font var3 = createFont(var1);
      this.initializeFont(var3, var2.getFontSize(), var2.isBold(), var2.isItalic());
      this.loadSettings(var2);
   }

   public UnicodeFont(String var1, int var2, boolean var3, boolean var4) throws SlickException {
      this.glyphs = new Glyph[2175][];
      this.glyphPages = new ArrayList();
      this.queuedGlyphs = new ArrayList(256);
      this.effects = new ArrayList();
      this.glyphPageWidth = 512;
      this.glyphPageHeight = 512;
      this.displayListCaching = true;
      this.baseDisplayListID = -1;
      this.displayLists = new LinkedHashMap(this, 200, 1.0F, true) {
         private final UnicodeFont this$0;

         {
            this.this$0 = var1;
         }

         protected boolean removeEldestEntry(Entry var1) {
            UnicodeFont.DisplayList var2 = (UnicodeFont.DisplayList)var1.getValue();
            if (var2 != null) {
               UnicodeFont.access$002(this.this$0, var2.id);
            }

            return this.size() > 200;
         }
      };
      this.ttfFileRef = var1;
      this.initializeFont(createFont(var1), var2, var3, var4);
   }

   public UnicodeFont(java.awt.Font var1, String var2) throws SlickException {
      this(var1, new HieroSettings(var2));
   }

   public UnicodeFont(java.awt.Font var1, HieroSettings var2) {
      this.glyphs = new Glyph[2175][];
      this.glyphPages = new ArrayList();
      this.queuedGlyphs = new ArrayList(256);
      this.effects = new ArrayList();
      this.glyphPageWidth = 512;
      this.glyphPageHeight = 512;
      this.displayListCaching = true;
      this.baseDisplayListID = -1;
      this.displayLists = new LinkedHashMap(this, 200, 1.0F, true) {
         private final UnicodeFont this$0;

         {
            this.this$0 = var1;
         }

         protected boolean removeEldestEntry(Entry var1) {
            UnicodeFont.DisplayList var2 = (UnicodeFont.DisplayList)var1.getValue();
            if (var2 != null) {
               UnicodeFont.access$002(this.this$0, var2.id);
            }

            return this.size() > 200;
         }
      };
      this.initializeFont(var1, var2.getFontSize(), var2.isBold(), var2.isItalic());
      this.loadSettings(var2);
   }

   public UnicodeFont(java.awt.Font var1) {
      this.glyphs = new Glyph[2175][];
      this.glyphPages = new ArrayList();
      this.queuedGlyphs = new ArrayList(256);
      this.effects = new ArrayList();
      this.glyphPageWidth = 512;
      this.glyphPageHeight = 512;
      this.displayListCaching = true;
      this.baseDisplayListID = -1;
      this.displayLists = new LinkedHashMap(this, 200, 1.0F, true) {
         private final UnicodeFont this$0;

         {
            this.this$0 = var1;
         }

         protected boolean removeEldestEntry(Entry var1) {
            UnicodeFont.DisplayList var2 = (UnicodeFont.DisplayList)var1.getValue();
            if (var2 != null) {
               UnicodeFont.access$002(this.this$0, var2.id);
            }

            return this.size() > 200;
         }
      };
      this.initializeFont(var1, var1.getSize(), var1.isBold(), var1.isItalic());
   }

   public UnicodeFont(java.awt.Font var1, int var2, boolean var3, boolean var4) {
      this.glyphs = new Glyph[2175][];
      this.glyphPages = new ArrayList();
      this.queuedGlyphs = new ArrayList(256);
      this.effects = new ArrayList();
      this.glyphPageWidth = 512;
      this.glyphPageHeight = 512;
      this.displayListCaching = true;
      this.baseDisplayListID = -1;
      this.displayLists = new LinkedHashMap(this, 200, 1.0F, true) {
         private final UnicodeFont this$0;

         {
            this.this$0 = var1;
         }

         protected boolean removeEldestEntry(Entry var1) {
            UnicodeFont.DisplayList var2 = (UnicodeFont.DisplayList)var1.getValue();
            if (var2 != null) {
               UnicodeFont.access$002(this.this$0, var2.id);
            }

            return this.size() > 200;
         }
      };
      this.initializeFont(var1, var2, var3, var4);
   }

   private void initializeFont(java.awt.Font var1, int var2, boolean var3, boolean var4) {
      Map var5 = var1.getAttributes();
      var5.put(TextAttribute.SIZE, new Float((float)var2));
      var5.put(TextAttribute.WEIGHT, var3 ? TextAttribute.WEIGHT_BOLD : TextAttribute.WEIGHT_REGULAR);
      var5.put(TextAttribute.POSTURE, var4 ? TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR);

      try {
         var5.put((class$java$awt$font$TextAttribute == null ? (class$java$awt$font$TextAttribute = class$("java.awt.font.TextAttribute")) : class$java$awt$font$TextAttribute).getDeclaredField("KERNING").get((Object)null), (class$java$awt$font$TextAttribute == null ? (class$java$awt$font$TextAttribute = class$("java.awt.font.TextAttribute")) : class$java$awt$font$TextAttribute).getDeclaredField("KERNING_ON").get((Object)null));
      } catch (Exception var9) {
      }

      this.font = var1.deriveFont(var5);
      FontMetrics var6 = GlyphPage.getScratchGraphics().getFontMetrics(this.font);
      this.ascent = var6.getAscent();
      this.descent = var6.getDescent();
      this.leading = var6.getLeading();
      char[] var7 = " ".toCharArray();
      GlyphVector var8 = this.font.layoutGlyphVector(GlyphPage.renderContext, var7, 0, var7.length, 0);
      this.spaceWidth = var8.getGlyphLogicalBounds(0).getBounds().width;
   }

   private void loadSettings(HieroSettings var1) {
      this.paddingTop = var1.getPaddingTop();
      this.paddingLeft = var1.getPaddingLeft();
      this.paddingBottom = var1.getPaddingBottom();
      this.paddingRight = var1.getPaddingRight();
      this.paddingAdvanceX = var1.getPaddingAdvanceX();
      this.paddingAdvanceY = var1.getPaddingAdvanceY();
      this.glyphPageWidth = var1.getGlyphPageWidth();
      this.glyphPageHeight = var1.getGlyphPageHeight();
      this.effects.addAll(var1.getEffects());
   }

   public void addGlyphs(int var1, int var2) {
      for(int var3 = var1; var3 <= var2; ++var3) {
         this.addGlyphs(new String(Character.toChars(var3)));
      }

   }

   public void addGlyphs(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("text cannot be null.");
      } else {
         char[] var2 = var1.toCharArray();
         GlyphVector var3 = this.font.layoutGlyphVector(GlyphPage.renderContext, var2, 0, var2.length, 0);
         int var4 = 0;

         for(int var5 = var3.getNumGlyphs(); var4 < var5; ++var4) {
            int var6 = var1.codePointAt(var3.getGlyphCharIndex(var4));
            Rectangle var7 = this.getGlyphBounds(var3, var4, var6);
            this.getGlyph(var3.getGlyphCode(var4), var6, var7, var3, var4);
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

   public boolean loadGlyphs(int var1) throws SlickException {
      if (this.queuedGlyphs.isEmpty()) {
         return false;
      } else if (this.effects.isEmpty()) {
         throw new IllegalStateException("The UnicodeFont must have at least one effect before any glyphs can be loaded.");
      } else {
         Iterator var2 = this.queuedGlyphs.iterator();

         while(true) {
            while(var2.hasNext()) {
               Glyph var3 = (Glyph)var2.next();
               int var4 = var3.getCodePoint();
               if (var3.getWidth() != 0 && var4 != 32) {
                  if (var3.isMissing()) {
                     if (this.missingGlyph != null) {
                        if (var3 != this.missingGlyph) {
                           var2.remove();
                        }
                     } else {
                        this.missingGlyph = var3;
                     }
                  }
               } else {
                  var2.remove();
               }
            }

            Collections.sort(this.queuedGlyphs, heightComparator);
            var2 = this.glyphPages.iterator();

            do {
               if (!var2.hasNext()) {
                  do {
                     if (this.queuedGlyphs.isEmpty()) {
                        return true;
                     }

                     GlyphPage var5 = new GlyphPage(this, this.glyphPageWidth, this.glyphPageHeight);
                     this.glyphPages.add(var5);
                     var1 -= var5.loadGlyphs(this.queuedGlyphs, var1);
                  } while(var1 != 0);

                  return true;
               }

               GlyphPage var6 = (GlyphPage)var2.next();
               var1 -= var6.loadGlyphs(this.queuedGlyphs, var1);
            } while(var1 != 0 && !this.queuedGlyphs.isEmpty());

            return true;
         }
      }
   }

   public void clearGlyphs() {
      for(int var1 = 0; var1 < 2175; ++var1) {
         this.glyphs[var1] = null;
      }

      Iterator var5 = this.glyphPages.iterator();

      while(var5.hasNext()) {
         GlyphPage var2 = (GlyphPage)var5.next();

         try {
            var2.getImage().destroy();
         } catch (SlickException var4) {
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

   public UnicodeFont.DisplayList drawDisplayList(float var1, float var2, String var3, Color var4, int var5, int var6) {
      if (var3 == null) {
         throw new IllegalArgumentException("text cannot be null.");
      } else if (var3.length() == 0) {
         return EMPTY_DISPLAY_LIST;
      } else if (var4 == null) {
         throw new IllegalArgumentException("color cannot be null.");
      } else {
         var1 -= (float)this.paddingLeft;
         var2 -= (float)this.paddingTop;
         String var7 = var3.substring(var5, var6);
         var4.bind();
         TextureImpl.bindNone();
         UnicodeFont.DisplayList var8 = null;
         if (this.displayListCaching && this.queuedGlyphs.isEmpty()) {
            if (this.baseDisplayListID == -1) {
               this.baseDisplayListID = GL.glGenLists(200);
               if (this.baseDisplayListID == 0) {
                  this.baseDisplayListID = -1;
                  this.displayListCaching = false;
                  return new UnicodeFont.DisplayList();
               }
            }

            var8 = (UnicodeFont.DisplayList)this.displayLists.get(var7);
            if (var8 != null) {
               if (!var8.invalid) {
                  GL.glTranslatef(var1, var2, 0.0F);
                  GL.glCallList(var8.id);
                  GL.glTranslatef(-var1, -var2, 0.0F);
                  return var8;
               }

               var8.invalid = false;
            } else if (var8 == null) {
               var8 = new UnicodeFont.DisplayList();
               int var9 = this.displayLists.size();
               this.displayLists.put(var7, var8);
               if (var9 < 200) {
                  var8.id = this.baseDisplayListID + var9;
               } else {
                  var8.id = this.eldestDisplayListID;
               }
            }

            this.displayLists.put(var7, var8);
         }

         GL.glTranslatef(var1, var2, 0.0F);
         if (var8 != null) {
            GL.glNewList(var8.id, 4865);
         }

         char[] var26 = var3.substring(0, var6).toCharArray();
         GlyphVector var10 = this.font.layoutGlyphVector(GlyphPage.renderContext, var26, 0, var26.length, 0);
         int var11 = 0;
         int var12 = 0;
         int var13 = 0;
         int var14 = 0;
         int var15 = this.ascent;
         boolean var16 = false;
         Texture var17 = null;
         int var18 = 0;

         for(int var19 = var10.getNumGlyphs(); var18 < var19; ++var18) {
            int var20 = var10.getGlyphCharIndex(var18);
            if (var20 >= var5) {
               if (var20 > var6) {
                  break;
               }

               int var21 = var3.codePointAt(var20);
               Rectangle var22 = this.getGlyphBounds(var10, var18, var21);
               Glyph var23 = this.getGlyph(var10.getGlyphCode(var18), var21, var22, var10, var18);
               if (var16 && var21 != 10) {
                  var14 = -var22.x;
                  var16 = false;
               }

               Image var24 = var23.getImage();
               if (var24 == null && this.missingGlyph != null && var23.isMissing()) {
                  var24 = this.missingGlyph.getImage();
               }

               if (var24 != null) {
                  Texture var25 = var24.getTexture();
                  if (var17 != null && var17 != var25) {
                     GL.glEnd();
                     var17 = null;
                  }

                  if (var17 == null) {
                     var25.bind();
                     GL.glBegin(7);
                     var17 = var25;
                  }

                  var24.drawEmbedded((float)(var22.x + var14), (float)(var22.y + var15), (float)var24.getWidth(), (float)var24.getHeight());
               }

               if (var18 > 0) {
                  var14 += this.paddingRight + this.paddingLeft + this.paddingAdvanceX;
               }

               var11 = Math.max(var11, var22.x + var14 + var22.width);
               var12 = Math.max(var12, this.ascent + var22.y + var22.height);
               if (var21 == 10) {
                  var16 = true;
                  var15 += this.getLineHeight();
                  ++var13;
                  var12 = 0;
               }
            }
         }

         if (var17 != null) {
            GL.glEnd();
         }

         if (var8 != null) {
            GL.glEndList();
            if (!this.queuedGlyphs.isEmpty()) {
               var8.invalid = true;
            }
         }

         GL.glTranslatef(-var1, -var2, 0.0F);
         if (var8 == null) {
            var8 = new UnicodeFont.DisplayList();
         }

         var8.width = (short)var11;
         var8.height = (short)(var13 * this.getLineHeight() + var12);
         return var8;
      }
   }

   public void drawString(float var1, float var2, String var3, Color var4, int var5, int var6) {
      this.drawDisplayList(var1, var2, var3, var4, var5, var6);
   }

   public void drawString(float var1, float var2, String var3) {
      this.drawString(var1, var2, var3, Color.white);
   }

   public void drawString(float var1, float var2, String var3, Color var4) {
      this.drawString(var1, var2, var3, var4, 0, var3.length());
   }

   private Glyph getGlyph(int var1, int var2, Rectangle var3, GlyphVector var4, int var5) {
      if (var1 >= 0 && var1 < 1114111) {
         int var6 = var1 / 512;
         int var7 = var1 & 511;
         Glyph var8 = null;
         Glyph[] var9 = this.glyphs[var6];
         if (var9 != null) {
            var8 = var9[var7];
            if (var8 != null) {
               return var8;
            }
         } else {
            var9 = this.glyphs[var6] = new Glyph[512];
         }

         var8 = var9[var7] = new Glyph(var2, var3, var4, var5, this);
         this.queuedGlyphs.add(var8);
         return var8;
      } else {
         return new Glyph(this, var2, var3, var4, var5, this) {
            private final UnicodeFont this$0;

            {
               this.this$0 = var1;
            }

            public boolean isMissing() {
               return true;
            }
         };
      }
   }

   private Rectangle getGlyphBounds(GlyphVector var1, int var2, int var3) {
      Rectangle var4 = var1.getGlyphPixelBounds(var2, GlyphPage.renderContext, 0.0F, 0.0F);
      if (var3 == 32) {
         var4.width = this.spaceWidth;
      }

      return var4;
   }

   public int getSpaceWidth() {
      return this.spaceWidth;
   }

   public int getWidth(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("text cannot be null.");
      } else if (var1.length() == 0) {
         return 0;
      } else {
         if (this.displayListCaching) {
            UnicodeFont.DisplayList var2 = (UnicodeFont.DisplayList)this.displayLists.get(var1);
            if (var2 != null) {
               return var2.width;
            }
         }

         char[] var12 = var1.toCharArray();
         GlyphVector var3 = this.font.layoutGlyphVector(GlyphPage.renderContext, var12, 0, var12.length, 0);
         int var4 = 0;
         int var5 = 0;
         boolean var6 = false;
         int var7 = 0;

         for(int var8 = var3.getNumGlyphs(); var7 < var8; ++var7) {
            int var9 = var3.getGlyphCharIndex(var7);
            int var10 = var1.codePointAt(var9);
            Rectangle var11 = this.getGlyphBounds(var3, var7, var10);
            if (var6 && var10 != 10) {
               var5 = -var11.x;
            }

            if (var7 > 0) {
               var5 += this.paddingLeft + this.paddingRight + this.paddingAdvanceX;
            }

            var4 = Math.max(var4, var11.x + var5 + var11.width);
            if (var10 == 10) {
               var6 = true;
            }
         }

         return var4;
      }
   }

   public int getHeight(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("text cannot be null.");
      } else if (var1.length() == 0) {
         return 0;
      } else {
         if (this.displayListCaching) {
            UnicodeFont.DisplayList var2 = (UnicodeFont.DisplayList)this.displayLists.get(var1);
            if (var2 != null) {
               return var2.height;
            }
         }

         char[] var11 = var1.toCharArray();
         GlyphVector var3 = this.font.layoutGlyphVector(GlyphPage.renderContext, var11, 0, var11.length, 0);
         int var4 = 0;
         int var5 = 0;
         int var6 = 0;

         for(int var7 = var3.getNumGlyphs(); var6 < var7; ++var6) {
            int var8 = var3.getGlyphCharIndex(var6);
            int var9 = var1.codePointAt(var8);
            if (var9 != 32) {
               Rectangle var10 = this.getGlyphBounds(var3, var6, var9);
               var5 = Math.max(var5, this.ascent + var10.y + var10.height);
               if (var9 == 10) {
                  ++var4;
                  var5 = 0;
               }
            }
         }

         return var4 * this.getLineHeight() + var5;
      }
   }

   public int getYOffset(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("text cannot be null.");
      } else {
         UnicodeFont.DisplayList var2 = null;
         if (this.displayListCaching) {
            var2 = (UnicodeFont.DisplayList)this.displayLists.get(var1);
            if (var2 != null && var2.yOffset != null) {
               return var2.yOffset.intValue();
            }
         }

         int var3 = var1.indexOf(10);
         if (var3 != -1) {
            var1 = var1.substring(0, var3);
         }

         char[] var4 = var1.toCharArray();
         GlyphVector var5 = this.font.layoutGlyphVector(GlyphPage.renderContext, var4, 0, var4.length, 0);
         int var6 = this.ascent + var5.getPixelBounds((FontRenderContext)null, 0.0F, 0.0F).y;
         if (var2 != null) {
            var2.yOffset = new Short((short)var6);
         }

         return var6;
      }
   }

   public java.awt.Font getFont() {
      return this.font;
   }

   public int getPaddingTop() {
      return this.paddingTop;
   }

   public void setPaddingTop(int var1) {
      this.paddingTop = var1;
   }

   public int getPaddingLeft() {
      return this.paddingLeft;
   }

   public void setPaddingLeft(int var1) {
      this.paddingLeft = var1;
   }

   public int getPaddingBottom() {
      return this.paddingBottom;
   }

   public void setPaddingBottom(int var1) {
      this.paddingBottom = var1;
   }

   public int getPaddingRight() {
      return this.paddingRight;
   }

   public void setPaddingRight(int var1) {
      this.paddingRight = var1;
   }

   public int getPaddingAdvanceX() {
      return this.paddingAdvanceX;
   }

   public void setPaddingAdvanceX(int var1) {
      this.paddingAdvanceX = var1;
   }

   public int getPaddingAdvanceY() {
      return this.paddingAdvanceY;
   }

   public void setPaddingAdvanceY(int var1) {
      this.paddingAdvanceY = var1;
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

   public void setGlyphPageWidth(int var1) {
      this.glyphPageWidth = var1;
   }

   public int getGlyphPageHeight() {
      return this.glyphPageHeight;
   }

   public void setGlyphPageHeight(int var1) {
      this.glyphPageHeight = var1;
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

   public void setDisplayListCaching(boolean var1) {
      this.displayListCaching = var1;
   }

   public String getFontFile() {
      if (this.ttfFileRef == null) {
         try {
            Object var1 = Class.forName("sun.font.FontManager").getDeclaredMethod("getFont2D", class$java$awt$Font == null ? (class$java$awt$Font = class$("java.awt.Font")) : class$java$awt$Font).invoke((Object)null, this.font);
            Field var2 = Class.forName("sun.font.PhysicalFont").getDeclaredField("platName");
            var2.setAccessible(true);
            this.ttfFileRef = (String)var2.get(var1);
         } catch (Throwable var3) {
         }

         if (this.ttfFileRef == null) {
            this.ttfFileRef = "";
         }
      }

      return this.ttfFileRef.length() == 0 ? null : this.ttfFileRef;
   }

   static int access$002(UnicodeFont var0, int var1) {
      return var0.eldestDisplayListID = var1;
   }

   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw (new NoClassDefFoundError()).initCause(var2);
      }
   }

   public static class DisplayList {
      boolean invalid;
      int id;
      Short yOffset;
      public short width;
      public short height;
      public Object userData;

      DisplayList() {
      }
   }
}
