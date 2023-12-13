/*     */ package org.neverhook.client.ui.clickgui.component.impl;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.BufferBuilder;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.ColorSetting;
/*     */ import org.neverhook.client.ui.clickgui.component.Component;
/*     */ import org.neverhook.client.ui.clickgui.component.ExpandableComponent;
/*     */ import org.neverhook.client.ui.clickgui.component.PropertyComponent;
/*     */ 
/*     */ public class ColorPickerComponent
/*     */   extends ExpandableComponent
/*     */   implements PropertyComponent
/*     */ {
/*     */   private static final int COLOR_PICKER_HEIGHT = 80;
/*  24 */   public static Tessellator tessellator = Tessellator.getInstance();
/*  25 */   public static BufferBuilder buffer = tessellator.getBuffer();
/*     */   
/*     */   private final ColorSetting colorSetting;
/*     */   
/*     */   private float hue;
/*     */   
/*     */   private float saturation;
/*     */   private float brightness;
/*     */   private float alpha;
/*     */   private boolean colorSelectorDragging;
/*     */   private boolean hueSelectorDragging;
/*     */   private boolean alphaSelectorDragging;
/*     */   
/*     */   public ColorPickerComponent(Component parent, ColorSetting colorSetting, int x, int y, int width, int height) {
/*  39 */     super(parent, colorSetting.getName(), x, y, width, height);
/*     */     
/*  41 */     this.colorSetting = colorSetting;
/*     */     
/*  43 */     int value = colorSetting.getColorValue();
/*  44 */     float[] hsb = getHSBFromColor(value);
/*  45 */     this.hue = hsb[0];
/*  46 */     this.saturation = hsb[1];
/*  47 */     this.brightness = hsb[2];
/*     */     
/*  49 */     this.alpha = (value >> 24 & 0xFF) / 255.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
/*  54 */     super.drawComponent(scaledResolution, mouseX, mouseY);
/*     */     
/*  56 */     int x = getX();
/*  57 */     int y = getY();
/*  58 */     int width = getWidth();
/*  59 */     int height = getHeight();
/*     */     
/*  61 */     int textColor = 16777215;
/*     */     
/*  63 */     Gui.drawRect(x, y, (x + width), (y + height), (new Color(20, 20, 20, 160)).getRGB());
/*  64 */     mc.fontRenderer.drawStringWithShadow(getName(), (x + 2), (y + height / 2.0F - 3.0F), textColor);
/*  65 */     float left = (x + width - 13);
/*  66 */     float top = y + height / 2.0F - 2.0F;
/*  67 */     float right = (x + width - 2);
/*  68 */     float bottom = y + height / 2.0F + 2.0F;
/*     */     
/*  70 */     RectHelper.drawRect((left - 0.5F), (top - 0.5F), (right + 0.5F), (bottom + 0.5F), -16777216);
/*     */     
/*  72 */     drawCheckeredBackground(left, top, right, bottom);
/*  73 */     RectHelper.drawRect(left, top, right, bottom, this.colorSetting.getColorValue());
/*     */     
/*  75 */     if (isExpanded()) {
/*     */       
/*  77 */       Gui.drawRect((x + 1), (y + height), (x + width - 1), (y + 
/*  78 */           getHeightWithExpand()), (new Color(20, 20, 20, 160))
/*  79 */           .getRGB());
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  84 */       float cpLeft = (x + 2);
/*  85 */       float cpTop = (y + height + 2);
/*  86 */       float cpRight = (x + 80 - 2);
/*  87 */       float cpBottom = (y + height + 80 - 2);
/*     */       
/*  89 */       if (mouseX <= cpLeft || mouseY <= cpTop || mouseX >= cpRight || mouseY >= cpBottom) {
/*  90 */         this.colorSelectorDragging = false;
/*     */       }
/*  92 */       float colorSelectorX = this.saturation * (cpRight - cpLeft);
/*  93 */       float colorSelectorY = (1.0F - this.brightness) * (cpBottom - cpTop);
/*     */       
/*  95 */       if (this.colorSelectorDragging) {
/*  96 */         float wWidth = cpRight - cpLeft;
/*  97 */         float xDif = mouseX - cpLeft;
/*  98 */         this.saturation = xDif / wWidth;
/*  99 */         colorSelectorX = xDif;
/*     */         
/* 101 */         float hHeight = cpBottom - cpTop;
/* 102 */         float yDif = mouseY - cpTop;
/* 103 */         this.brightness = 1.0F - yDif / hHeight;
/* 104 */         colorSelectorY = yDif;
/*     */         
/* 106 */         updateColor(Color.HSBtoRGB(this.hue, this.saturation, this.brightness), false);
/*     */       } 
/*     */       
/* 109 */       Gui.drawRect(cpLeft, cpTop, cpRight, cpBottom, -16777216);
/* 110 */       drawColorPickerRect(cpLeft + 0.5F, cpTop + 0.5F, cpRight - 0.5F, cpBottom - 0.5F);
/*     */       
/* 112 */       float selectorWidth = 2.0F;
/* 113 */       float outlineWidth = 0.5F;
/* 114 */       float half = selectorWidth / 2.0F;
/*     */       
/* 116 */       float csLeft = cpLeft + colorSelectorX - half;
/* 117 */       float csTop = cpTop + colorSelectorY - half;
/* 118 */       float csRight = cpLeft + colorSelectorX + half;
/* 119 */       float csBottom = cpTop + colorSelectorY + half;
/*     */       
/* 121 */       Gui.drawRect(csLeft - outlineWidth, csTop - outlineWidth, csRight + outlineWidth, csBottom + outlineWidth, -16777216);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 126 */       Gui.drawRect(csLeft, csTop, csRight, csBottom, 
/*     */ 
/*     */ 
/*     */           
/* 130 */           Color.HSBtoRGB(this.hue, this.saturation, this.brightness));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 135 */       float sLeft = (x + 80 - 1);
/* 136 */       float sTop = (y + height + 2);
/* 137 */       float sRight = sLeft + 5.0F;
/* 138 */       float sBottom = (y + height + 80 - 2);
/*     */       
/* 140 */       if (mouseX <= sLeft || mouseY <= sTop || mouseX >= sRight || mouseY >= sBottom) {
/* 141 */         this.hueSelectorDragging = false;
/*     */       }
/* 143 */       float hueSelectorY = this.hue * (sBottom - sTop);
/*     */       
/* 145 */       if (this.hueSelectorDragging) {
/* 146 */         float hsHeight = sBottom - sTop;
/* 147 */         float yDif = mouseY - sTop;
/* 148 */         this.hue = yDif / hsHeight;
/* 149 */         hueSelectorY = yDif;
/*     */         
/* 151 */         updateColor(Color.HSBtoRGB(this.hue, this.saturation, this.brightness), false);
/*     */       } 
/*     */ 
/*     */       
/* 155 */       Gui.drawRect(sLeft, sTop, sRight, sBottom, -16777216);
/* 156 */       float inc = 0.2F;
/* 157 */       float times = 1.0F / inc;
/* 158 */       float sHeight = sBottom - sTop;
/* 159 */       float sY = sTop + 0.5F;
/* 160 */       float size = sHeight / times;
/*     */       
/* 162 */       for (int i = 0; i < times; i++) {
/* 163 */         boolean last = (i == times - 1.0F);
/* 164 */         if (last)
/* 165 */           size--; 
/* 166 */         gui.drawGradientRect(sLeft + 0.5F, sY, sRight - 0.5F, sY + size, 
/*     */             
/* 168 */             Color.HSBtoRGB(inc * i, 1.0F, 1.0F), 
/* 169 */             Color.HSBtoRGB(inc * (i + 1), 1.0F, 1.0F));
/* 170 */         if (!last) {
/* 171 */           sY += size;
/*     */         }
/*     */       } 
/* 174 */       float f6 = 2.0F;
/* 175 */       float f8 = 0.5F;
/* 176 */       float f10 = f6 / 2.0F;
/*     */       
/* 178 */       float f12 = sTop + hueSelectorY - f10;
/* 179 */       float f13 = sTop + hueSelectorY + f10;
/*     */       
/* 181 */       Gui.drawRect(sLeft - f8, f12 - f8, sRight + f8, f13 + f8, -16777216);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 186 */       Gui.drawRect(sLeft, f12, sRight, f13, 
/*     */ 
/*     */ 
/*     */           
/* 190 */           Color.HSBtoRGB(this.hue, 1.0F, 1.0F));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 195 */       sLeft = (x + 80 + 6);
/* 196 */       sTop = (y + height + 2);
/* 197 */       sRight = sLeft + 5.0F;
/* 198 */       sBottom = (y + height + 80 - 2);
/*     */       
/* 200 */       if (mouseX <= sLeft || mouseY <= sTop || mouseX >= sRight || mouseY >= sBottom) {
/* 201 */         this.alphaSelectorDragging = false;
/*     */       }
/* 203 */       int color = Color.HSBtoRGB(this.hue, this.saturation, this.brightness);
/*     */       
/* 205 */       int r = color >> 16 & 0xFF;
/* 206 */       int g = color >> 8 & 0xFF;
/* 207 */       int b = color & 0xFF;
/*     */       
/* 209 */       float alphaSelectorY = this.alpha * (sBottom - sTop);
/*     */       
/* 211 */       if (this.alphaSelectorDragging) {
/* 212 */         float hsHeight = sBottom - sTop;
/* 213 */         float yDif = mouseY - sTop;
/* 214 */         this.alpha = yDif / hsHeight;
/* 215 */         alphaSelectorY = yDif;
/*     */         
/* 217 */         updateColor((new Color(r, g, b, (int)(this.alpha * 255.0F))).getRGB(), true);
/*     */       } 
/*     */ 
/*     */       
/* 221 */       Gui.drawRect(sLeft, sTop, sRight, sBottom, -16777216);
/*     */       
/* 223 */       drawCheckeredBackground(sLeft + 0.5F, sTop + 0.5F, sRight - 0.5F, sBottom - 0.5F);
/*     */       
/* 225 */       gui.drawGradientRect(sLeft + 0.5F, sTop + 0.5F, sRight - 0.5F, sBottom - 0.5F, (new Color(r, g, b, 0))
/*     */           
/* 227 */           .getRGB(), (new Color(r, g, b, 255))
/* 228 */           .getRGB());
/*     */       
/* 230 */       float selectorHeight = 2.0F;
/* 231 */       float f5 = 0.5F;
/* 232 */       float f7 = selectorHeight / 2.0F;
/*     */       
/* 234 */       float f9 = sTop + alphaSelectorY - f7;
/* 235 */       float f11 = sTop + alphaSelectorY + f7;
/*     */       
/* 237 */       float bx = sRight + f5;
/* 238 */       float ay = f9 - f5;
/* 239 */       float by = f11 + f5;
/*     */       
/* 241 */       GL11.glDisable(3553);
/* 242 */       RenderHelper.setColor(-16777216);
/* 243 */       GL11.glBegin(2);
/* 244 */       GL11.glVertex2f(sLeft, ay);
/* 245 */       GL11.glVertex2f(sLeft, by);
/* 246 */       GL11.glVertex2f(bx, by);
/* 247 */       GL11.glVertex2f(bx, ay);
/* 248 */       GL11.glEnd();
/* 249 */       GL11.glEnable(3553);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 254 */       float xOff = 94.0F;
/* 255 */       float f1 = x + xOff + 7.0F;
/* 256 */       float f2 = (y + height + 2);
/* 257 */       float f3 = f1 + width - xOff - 7.0F;
/* 258 */       float f4 = (y + height) + 40.0F + 8.0F;
/*     */       
/* 260 */       RectHelper.drawRect(f1, f2, f3, (f4 + 31.0F), -16777216);
/*     */       
/* 262 */       drawCheckeredBackground(f1 + 0.5F, f2 + 0.5F, f3 - 0.5F, f4 + 30.0F);
/*     */       
/* 264 */       RectHelper.drawRect((f1 + 3.0F), (f2 + 4.0F), (f3 - 3.0F), (f4 + 28.0F), this.colorSetting.getColorValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void drawCheckeredBackground(float x, float y, float right, float bottom) {
/* 270 */     RectHelper.drawRect(x, y, right, bottom, -1);
/*     */     
/* 272 */     for (boolean off = false; y < bottom; y++) {
/* 273 */       float x1; for (x1 = x + ((off = !off) ? true : false); x1 < right; x1 += 2.0F)
/* 274 */         RectHelper.drawRect(x1, y, (x1 + 1.0F), (y + 1.0F), -8355712); 
/*     */     } 
/*     */   }
/*     */   private void updateColor(int hex, boolean hasAlpha) {
/* 278 */     if (hasAlpha) {
/* 279 */       this.colorSetting.setColorValue(hex);
/*     */     } else {
/* 281 */       this.colorSetting.setColorValue((new Color(hex >> 16 & 0xFF, hex >> 8 & 0xFF, hex & 0xFF, (int)(this.alpha * 255.0F)))
/*     */ 
/*     */ 
/*     */           
/* 285 */           .getRGB());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMouseClick(int mouseX, int mouseY, int button) {
/* 291 */     super.onMouseClick(mouseX, mouseY, button);
/*     */     
/* 293 */     if (isExpanded() && 
/* 294 */       button == 0) {
/* 295 */       int x = getX();
/* 296 */       int y = getY();
/*     */       
/* 298 */       float cpLeft = (x + 2);
/* 299 */       float cpTop = (y + getHeight() + 2);
/* 300 */       float cpRight = (x + 80 - 2);
/* 301 */       float cpBottom = (y + getHeight() + 80 - 2);
/*     */       
/* 303 */       float sLeft = (x + 80 - 1);
/* 304 */       float sTop = (y + getHeight() + 2);
/* 305 */       float sRight = sLeft + 5.0F;
/* 306 */       float sBottom = (y + getHeight() + 80 - 2);
/*     */       
/* 308 */       float asLeft = (x + 80 + 6);
/* 309 */       float asTop = (y + getHeight() + 2);
/* 310 */       float asRight = asLeft + 5.0F;
/* 311 */       float asBottom = (y + getHeight() + 80 - 2);
/*     */       
/* 313 */       this.colorSelectorDragging = (!this.colorSelectorDragging && mouseX > cpLeft && mouseY > cpTop && mouseX < cpRight && mouseY < cpBottom);
/*     */       
/* 315 */       this.hueSelectorDragging = (!this.hueSelectorDragging && mouseX > sLeft && mouseY > sTop && mouseX < sRight && mouseY < sBottom);
/*     */       
/* 317 */       this.alphaSelectorDragging = (!this.alphaSelectorDragging && mouseX > asLeft && mouseY > asTop && mouseX < asRight && mouseY < asBottom);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMouseRelease(int button) {
/* 324 */     if (this.hueSelectorDragging) {
/* 325 */       this.hueSelectorDragging = false;
/* 326 */     } else if (this.colorSelectorDragging) {
/* 327 */       this.colorSelectorDragging = false;
/* 328 */     } else if (this.alphaSelectorDragging) {
/* 329 */       this.alphaSelectorDragging = false;
/*     */     } 
/*     */   }
/*     */   private float[] getHSBFromColor(int hex) {
/* 333 */     int r = hex >> 16 & 0xFF;
/* 334 */     int g = hex >> 8 & 0xFF;
/* 335 */     int b = hex & 0xFF;
/* 336 */     return Color.RGBtoHSB(r, g, b, null);
/*     */   }
/*     */   
/*     */   public void drawColorPickerRect(float left, float top, float right, float bottom) {
/* 340 */     int hueBasedColor = Color.HSBtoRGB(this.hue, 1.0F, 1.0F);
/* 341 */     GL11.glDisable(3553);
/* 342 */     GlStateManager.enableBlend();
/* 343 */     GL11.glShadeModel(7425);
/* 344 */     buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 345 */     buffer.pos(right, top, 0.0D).color(hueBasedColor).endVertex();
/* 346 */     buffer.pos(left, top, 0.0D).color(-1).endVertex();
/* 347 */     buffer.pos(left, bottom, 0.0D).color(-1).endVertex();
/* 348 */     buffer.pos(right, bottom, 0.0D).color(hueBasedColor).endVertex();
/* 349 */     tessellator.draw();
/* 350 */     buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 351 */     buffer.pos(right, top, 0.0D).color(402653184).endVertex();
/* 352 */     buffer.pos(left, top, 0.0D).color(402653184).endVertex();
/* 353 */     buffer.pos(left, bottom, 0.0D).color(-16777216).endVertex();
/* 354 */     buffer.pos(right, bottom, 0.0D).color(-16777216).endVertex();
/* 355 */     tessellator.draw();
/* 356 */     GlStateManager.disableBlend();
/* 357 */     GL11.glShadeModel(7425);
/* 358 */     GL11.glEnable(3553);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canExpand() {
/* 363 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeightWithExpand() {
/* 368 */     return getHeight() + 80;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onPress(int mouseX, int mouseY, int button) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Setting getSetting() {
/* 378 */     return (Setting)this.colorSetting;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\clickgui\component\impl\ColorPickerComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */