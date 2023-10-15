/*     */ package org.neverhook.client.ui.font;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class CFont {
/*  12 */   private final float imgSize = 512.0F;
/*  13 */   protected CharData[] charData = new CharData[256];
/*     */   protected Font font;
/*     */   protected boolean antiAlias;
/*     */   protected boolean fractionalMetrics;
/*  17 */   protected int fontHeight = -1;
/*  18 */   protected int charOffset = 0;
/*     */   protected DynamicTexture tex;
/*     */   
/*     */   public CFont(Font font, boolean antiAlias, boolean fractionalMetrics) {
/*  22 */     this.font = font;
/*  23 */     this.antiAlias = antiAlias;
/*  24 */     this.fractionalMetrics = fractionalMetrics;
/*  25 */     this.tex = setupTexture(font, antiAlias, fractionalMetrics, this.charData);
/*     */   }
/*     */   
/*     */   protected DynamicTexture setupTexture(Font font, boolean antiAlias, boolean fractionalMetrics, CharData[] chars) {
/*  29 */     BufferedImage img = generateFontImage(font, antiAlias, fractionalMetrics, chars);
/*     */     
/*     */     try {
/*  32 */       return new DynamicTexture(img);
/*  33 */     } catch (Exception e) {
/*  34 */       e.printStackTrace();
/*     */ 
/*     */       
/*  37 */       return null;
/*     */     } 
/*     */   }
/*     */   protected BufferedImage generateFontImage(Font font, boolean antiAlias, boolean fractionalMetrics, CharData[] chars) {
/*  41 */     getClass(); int imgSize = 512;
/*  42 */     BufferedImage bufferedImage = new BufferedImage(imgSize, imgSize, 2);
/*  43 */     Graphics2D g = (Graphics2D)bufferedImage.getGraphics();
/*  44 */     g.setFont(font);
/*  45 */     g.setColor(new Color(255, 255, 255, 0));
/*  46 */     g.fillRect(0, 0, imgSize, imgSize);
/*  47 */     g.setColor(Color.WHITE);
/*  48 */     g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
/*  49 */     g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAlias ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
/*  50 */     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAlias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
/*  51 */     FontMetrics fontMetrics = g.getFontMetrics();
/*  52 */     int charHeight = 0;
/*  53 */     int positionX = 0;
/*  54 */     int positionY = 1;
/*     */     
/*  56 */     for (int i = 0; i < chars.length; i++) {
/*  57 */       char ch = (char)i;
/*  58 */       CharData charData = new CharData();
/*  59 */       Rectangle2D dimensions = fontMetrics.getStringBounds(String.valueOf(ch), g);
/*  60 */       charData.width = (dimensions.getBounds()).width + 8;
/*  61 */       charData.height = (dimensions.getBounds()).height;
/*  62 */       if (positionX + charData.width >= imgSize) {
/*  63 */         positionX = 0;
/*  64 */         positionY += charHeight;
/*  65 */         charHeight = 0;
/*     */       } 
/*     */       
/*  68 */       if (charData.height > charHeight) {
/*  69 */         charHeight = charData.height;
/*     */       }
/*     */       
/*  72 */       charData.storedX = positionX;
/*  73 */       charData.storedY = positionY;
/*  74 */       if (charData.height > this.fontHeight) {
/*  75 */         this.fontHeight = charData.height;
/*     */       }
/*     */       
/*  78 */       chars[i] = charData;
/*  79 */       g.drawString(String.valueOf(ch), positionX + 2, positionY + fontMetrics.getAscent());
/*  80 */       positionX += charData.width;
/*     */     } 
/*     */     
/*  83 */     return bufferedImage;
/*     */   }
/*     */   
/*     */   public void drawChar(CharData[] chars, char c, float x, float y) throws ArrayIndexOutOfBoundsException {
/*     */     try {
/*  88 */       drawQuad(x, y, (chars[c]).width, (chars[c]).height, (chars[c]).storedX, (chars[c]).storedY, (chars[c]).width, (chars[c]).height);
/*  89 */     } catch (Exception e) {
/*  90 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void drawQuad(float x, float y, float width, float height, float srcX, float srcY, float srcWidth, float srcHeight) {
/*  95 */     getClass(); float renderSRCX = srcX / 512.0F;
/*  96 */     getClass(); float renderSRCY = srcY / 512.0F;
/*  97 */     getClass(); float renderSRCWidth = srcWidth / 512.0F;
/*  98 */     getClass(); float renderSRCHeight = srcHeight / 512.0F;
/*  99 */     GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
/* 100 */     GL11.glVertex2d((x + width), y);
/* 101 */     GL11.glTexCoord2f(renderSRCX, renderSRCY);
/* 102 */     GL11.glVertex2d(x, y);
/* 103 */     GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
/* 104 */     GL11.glVertex2d(x, (y + height));
/* 105 */     GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
/* 106 */     GL11.glVertex2d(x, (y + height));
/* 107 */     GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
/* 108 */     GL11.glVertex2d((x + width), (y + height));
/* 109 */     GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
/* 110 */     GL11.glVertex2d((x + width), y);
/*     */   }
/*     */   
/*     */   public int getStringHeight(String text) {
/* 114 */     return getFontHeight();
/*     */   }
/*     */   
/*     */   public int getFontHeight() {
/* 118 */     return (this.fontHeight - 8) / 2;
/*     */   }
/*     */   
/*     */   public int getStringWidth(String text) {
/* 122 */     int width = 0;
/*     */     
/* 124 */     for (char c : text.toCharArray()) {
/* 125 */       if (c < this.charData.length) {
/* 126 */         width += (this.charData[c]).width - 8 + this.charOffset;
/*     */       }
/*     */     } 
/*     */     
/* 130 */     return width / 2;
/*     */   }
/*     */   
/*     */   public boolean isAntiAlias() {
/* 134 */     return this.antiAlias;
/*     */   }
/*     */   
/*     */   public void setAntiAlias(boolean antiAlias) {
/* 138 */     if (this.antiAlias != antiAlias) {
/* 139 */       this.antiAlias = antiAlias;
/* 140 */       this.tex = setupTexture(this.font, antiAlias, this.fractionalMetrics, this.charData);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isFractionalMetrics() {
/* 145 */     return this.fractionalMetrics;
/*     */   }
/*     */   
/*     */   public void setFractionalMetrics(boolean fractionalMetrics) {
/* 149 */     if (this.fractionalMetrics != fractionalMetrics) {
/* 150 */       this.fractionalMetrics = fractionalMetrics;
/* 151 */       this.tex = setupTexture(this.font, this.antiAlias, fractionalMetrics, this.charData);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Font getFont() {
/* 156 */     return this.font;
/*     */   }
/*     */   
/*     */   public void setFont(Font font) {
/* 160 */     this.font = font;
/* 161 */     this.tex = setupTexture(font, this.antiAlias, this.fractionalMetrics, this.charData);
/*     */   }
/*     */   
/*     */   protected static class CharData {
/*     */     public int width;
/*     */     public int height;
/*     */     public int storedX;
/*     */     public int storedY;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\font\CFont.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */