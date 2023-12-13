/*     */ package org.neverhook.client.ui.font;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class MCFontRenderer
/*     */   extends CFont {
/*  12 */   private final int[] colorCode = new int[32];
/*  13 */   protected CFont.CharData[] boldChars = new CFont.CharData[256];
/*  14 */   protected CFont.CharData[] italicChars = new CFont.CharData[256];
/*  15 */   protected CFont.CharData[] boldItalicChars = new CFont.CharData[256];
/*     */   protected DynamicTexture texBold;
/*     */   protected DynamicTexture texItalic;
/*     */   protected DynamicTexture texItalicBold;
/*     */   
/*     */   public MCFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
/*  21 */     super(font, antiAlias, fractionalMetrics);
/*  22 */     setupBoldItalicIDs();
/*     */     
/*  24 */     for (int index = 0; index < 32; index++) {
/*  25 */       int noClue = (index >> 3 & 0x1) * 85;
/*  26 */       int red = (index >> 2 & 0x1) * 170 + noClue;
/*  27 */       int green = (index >> 1 & 0x1) * 170 + noClue;
/*  28 */       int blue = (index & 0x1) * 170 + noClue;
/*     */       
/*  30 */       if (index == 6) {
/*  31 */         red += 85;
/*     */       }
/*     */       
/*  34 */       if (index >= 16) {
/*  35 */         red /= 4;
/*  36 */         green /= 4;
/*  37 */         blue /= 4;
/*     */       } 
/*     */       
/*  40 */       this.colorCode[index] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void drawStringWithOutline(MCFontRenderer fontRenderer, String text, float x, float y, int color) {
/*  45 */     fontRenderer.drawString(text, x - 0.8F, y, Color.BLACK.getRGB());
/*  46 */     fontRenderer.drawString(text, x + 0.8F, y, Color.BLACK.getRGB());
/*  47 */     fontRenderer.drawString(text, x, y - 0.8F, Color.BLACK.getRGB());
/*  48 */     fontRenderer.drawString(text, x, y + 0.8F, Color.BLACK.getRGB());
/*  49 */     fontRenderer.drawString(text, x, y, color);
/*     */   }
/*     */   
/*     */   public static void drawStringWithOutline(FontRenderer fontRenderer, String text, float x, float y, int color) {
/*  53 */     fontRenderer.drawString(text, x - 1.0F, y, Color.BLACK.getRGB());
/*  54 */     fontRenderer.drawString(text, x + 1.0F, y, Color.BLACK.getRGB());
/*  55 */     fontRenderer.drawString(text, x, y - 1.0F, Color.BLACK.getRGB());
/*  56 */     fontRenderer.drawString(text, x, y + 1.0F, Color.BLACK.getRGB());
/*  57 */     fontRenderer.drawString(text, x, y, color);
/*     */   }
/*     */   
/*     */   public static void drawCenteredStringWithOutline(FontRenderer fontRenderer, String text, float x, float y, int color) {
/*  61 */     fontRenderer.drawCenteredString(text, x - 1.0F, y, Color.BLACK.getRGB());
/*  62 */     fontRenderer.drawCenteredString(text, x + 1.0F, y, Color.BLACK.getRGB());
/*  63 */     fontRenderer.drawCenteredString(text, x, y - 1.0F, Color.BLACK.getRGB());
/*  64 */     fontRenderer.drawCenteredString(text, x, y + 1.0F, Color.BLACK.getRGB());
/*  65 */     fontRenderer.drawCenteredString(text, x, y, color);
/*     */   }
/*     */   
/*     */   public static float drawCenteredStringWithShadow(FontRenderer fontRenderer, String text, float x, float y, int color) {
/*  69 */     return fontRenderer.drawString(text, x - (fontRenderer.getStringWidth(text) / 2), y, color);
/*     */   }
/*     */   
/*     */   public void drawCenteredStringWithOutline(MCFontRenderer fontRenderer, String text, float x, float y, int color) {
/*  73 */     drawCenteredString(text, x - 1.0F, y, Color.BLACK.getRGB());
/*  74 */     drawCenteredString(text, x + 1.0F, y, Color.BLACK.getRGB());
/*  75 */     drawCenteredString(text, x, y - 1.0F, Color.BLACK.getRGB());
/*  76 */     drawCenteredString(text, x, y + 1.0F, Color.BLACK.getRGB());
/*  77 */     drawCenteredString(text, x, y, color);
/*     */   }
/*     */   
/*     */   public float drawStringWithShadow(String text, double x, double y, int color) {
/*  81 */     float shadowWidth = drawString(text, x + 0.9D, y + 0.7D, color, true);
/*  82 */     return Math.max(shadowWidth, drawString(text, x, y, color, false));
/*     */   }
/*     */   
/*     */   public float drawString(String text, float x, float y, int color) {
/*  86 */     return drawString(text, x, y, color, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public float drawCenteredString(String text, float x, float y, int color) {
/*  91 */     return drawString(text, x - getStringWidth(text) / 2.0F, y, color);
/*     */   }
/*     */   
/*     */   public float drawCenteredStringWithShadow(String text, float x, float y, int color) {
/*  95 */     return drawString(text, x - (getStringWidth(text) / 2), y, color);
/*     */   }
/*     */   
/*     */   public float drawString(String text, double x, double y, int color, boolean shadow) {
/*  99 */     x--;
/* 100 */     if (color == 553648127) {
/* 101 */       color = 16777215;
/*     */     }
/* 103 */     if ((color & 0xFC000000) == 0) {
/* 104 */       color |= 0xFF000000;
/*     */     }
/* 106 */     if (shadow) {
/* 107 */       color = (color & 0xFCFCFC) >> 2 | color & (new Color(20, 20, 20, 200)).getRGB();
/*     */     }
/* 109 */     CFont.CharData[] currentData = this.charData;
/* 110 */     float alpha = (color >> 24 & 0xFF) / 255.0F;
/* 111 */     boolean bold = false;
/* 112 */     boolean italic = false;
/* 113 */     boolean strikethrough = false;
/* 114 */     boolean underline = false;
/* 115 */     x *= 2.0D;
/* 116 */     y = (y - 3.0D) * 2.0D;
/* 117 */     GL11.glPushMatrix();
/* 118 */     GlStateManager.scale(0.5D, 0.5D, 0.5D);
/* 119 */     GlStateManager.enableBlend();
/* 120 */     GlStateManager.blendFunc(770, 771);
/* 121 */     GlStateManager.color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
/* 122 */     int size = text.length();
/* 123 */     GlStateManager.enableTexture2D();
/* 124 */     GlStateManager.bindTexture(this.tex.getGlTextureId());
/* 125 */     GL11.glBindTexture(3553, this.tex.getGlTextureId());
/* 126 */     int i = 0;
/* 127 */     while (i < size) {
/* 128 */       char character = text.charAt(i);
/* 129 */       if (String.valueOf(character).equals("§")) {
/* 130 */         int colorIndex = 21;
/*     */         try {
/* 132 */           colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
/* 133 */         } catch (Exception e) {
/* 134 */           e.printStackTrace();
/*     */         } 
/* 136 */         if (colorIndex < 16) {
/* 137 */           bold = false;
/* 138 */           italic = false;
/* 139 */           underline = false;
/* 140 */           strikethrough = false;
/* 141 */           GlStateManager.bindTexture(this.tex.getGlTextureId());
/* 142 */           currentData = this.charData;
/* 143 */           if (colorIndex < 0) {
/* 144 */             colorIndex = 15;
/*     */           }
/* 146 */           if (shadow) {
/* 147 */             colorIndex += 16;
/*     */           }
/* 149 */           int colorcode = this.colorCode[colorIndex];
/* 150 */           GlStateManager.color((colorcode >> 16 & 0xFF) / 255.0F, (colorcode >> 8 & 0xFF) / 255.0F, (colorcode & 0xFF) / 255.0F, alpha);
/* 151 */         } else if (colorIndex == 17) {
/* 152 */           bold = true;
/* 153 */           if (italic) {
/* 154 */             GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
/* 155 */             currentData = this.boldItalicChars;
/*     */           } else {
/* 157 */             GlStateManager.bindTexture(this.texBold.getGlTextureId());
/* 158 */             currentData = this.boldChars;
/*     */           } 
/* 160 */         } else if (colorIndex == 18) {
/* 161 */           strikethrough = true;
/* 162 */         } else if (colorIndex == 19) {
/* 163 */           underline = true;
/* 164 */         } else if (colorIndex == 20) {
/* 165 */           italic = true;
/* 166 */           if (bold) {
/* 167 */             GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
/* 168 */             currentData = this.boldItalicChars;
/*     */           } else {
/* 170 */             GlStateManager.bindTexture(this.texItalic.getGlTextureId());
/* 171 */             currentData = this.italicChars;
/*     */           } 
/* 173 */         } else if (colorIndex == 21) {
/* 174 */           bold = false;
/* 175 */           italic = false;
/* 176 */           underline = false;
/* 177 */           strikethrough = false;
/* 178 */           GlStateManager.color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
/* 179 */           GlStateManager.bindTexture(this.tex.getGlTextureId());
/* 180 */           currentData = this.charData;
/*     */         } 
/* 182 */         i++;
/* 183 */       } else if (character < currentData.length) {
/* 184 */         GL11.glBegin(4);
/* 185 */         drawChar(currentData, character, (float)x, (float)y);
/* 186 */         GL11.glEnd();
/* 187 */         if (strikethrough) {
/* 188 */           drawLine(x, y + ((currentData[character]).height / 2.0F), x + (currentData[character]).width - 8.0D, y + ((currentData[character]).height / 2.0F), 1.0F);
/*     */         }
/* 190 */         if (underline) {
/* 191 */           drawLine(x, y + (currentData[character]).height - 2.0D, x + (currentData[character]).width - 8.0D, y + (currentData[character]).height - 2.0D, 1.0F);
/*     */         }
/* 193 */         x += ((currentData[character]).width - 8 + this.charOffset);
/*     */       } 
/* 195 */       i++;
/*     */     } 
/* 197 */     GL11.glPopMatrix();
/* 198 */     return (float)(x / 2.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStringWidth(String text) {
/* 203 */     int width = 0;
/* 204 */     CFont.CharData[] currentData = this.charData;
/* 205 */     boolean bold = false;
/* 206 */     boolean italic = false;
/* 207 */     int size = text.length();
/* 208 */     int i = 0;
/* 209 */     while (i < size) {
/* 210 */       char character = text.charAt(i);
/* 211 */       if (String.valueOf(character).equals("§")) {
/* 212 */         int colorIndex = "0123456789abcdefklmnor".indexOf(character);
/* 213 */         if (colorIndex < 16) {
/* 214 */           bold = false;
/* 215 */           italic = false;
/* 216 */         } else if (colorIndex == 17) {
/* 217 */           bold = true;
/* 218 */           currentData = italic ? this.boldItalicChars : this.boldChars;
/* 219 */         } else if (colorIndex == 20) {
/* 220 */           italic = true;
/* 221 */           currentData = bold ? this.boldItalicChars : this.italicChars;
/* 222 */         } else if (colorIndex == 21) {
/* 223 */           bold = false;
/* 224 */           italic = false;
/* 225 */           currentData = this.charData;
/*     */         } 
/* 227 */         i++;
/* 228 */       } else if (character < currentData.length) {
/* 229 */         width += (currentData[character]).width - 8 + this.charOffset;
/*     */       } 
/* 231 */       i++;
/*     */     } 
/* 233 */     return width / 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFont(Font font) {
/* 238 */     super.setFont(font);
/* 239 */     setupBoldItalicIDs();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAntiAlias(boolean antiAlias) {
/* 244 */     super.setAntiAlias(antiAlias);
/* 245 */     setupBoldItalicIDs();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFractionalMetrics(boolean fractionalMetrics) {
/* 250 */     super.setFractionalMetrics(fractionalMetrics);
/* 251 */     setupBoldItalicIDs();
/*     */   }
/*     */   
/*     */   private void setupBoldItalicIDs() {
/* 255 */     this.texBold = setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
/* 256 */     this.texItalic = setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
/* 257 */     this.texItalicBold = setupTexture(this.font.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
/*     */   }
/*     */   
/*     */   private void drawLine(double x, double y, double x1, double y1, float width) {
/* 261 */     GL11.glDisable(3553);
/* 262 */     GL11.glLineWidth(width);
/* 263 */     GL11.glBegin(1);
/* 264 */     GL11.glVertex2d(x, y);
/* 265 */     GL11.glVertex2d(x1, y1);
/* 266 */     GL11.glEnd();
/* 267 */     GL11.glEnable(3553);
/*     */   }
/*     */   
/*     */   public void drawStringWithOutline(String text, double x, double y, int color) {
/* 271 */     drawString(text, x - 0.5D, y, Color.BLACK.getRGB(), false);
/* 272 */     drawString(text, x + 0.5D, y, Color.BLACK.getRGB(), false);
/* 273 */     drawString(text, x, y - 0.5D, Color.BLACK.getRGB(), false);
/* 274 */     drawString(text, x, y + 0.5D, Color.BLACK.getRGB(), false);
/* 275 */     drawString(text, x, y, color, false);
/*     */   }
/*     */   
/*     */   public void drawCenteredStringWithOutline(String text, float x, float y, int color) {
/* 279 */     drawCenteredString(text, x - 0.5F, y, Color.BLACK.getRGB());
/* 280 */     drawCenteredString(text, x + 0.5F, y, Color.BLACK.getRGB());
/* 281 */     drawCenteredString(text, x, y - 0.5F, Color.BLACK.getRGB());
/* 282 */     drawCenteredString(text, x, y + 0.5F, Color.BLACK.getRGB());
/* 283 */     drawCenteredString(text, x, y, color);
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\font\MCFontRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */