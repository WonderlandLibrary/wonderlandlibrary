/*     */ package org.neverhook.client.ui.newclickgui.settings;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.renderer.BufferBuilder;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.ColorSetting;
/*     */ import org.neverhook.client.ui.newclickgui.FeaturePanel;
/*     */ 
/*     */ public class ColorPickerComponent
/*     */   extends Component
/*     */ {
/*  18 */   public static int heightOffset = 80;
/*  19 */   public static Tessellator tessellator = Tessellator.getInstance();
/*  20 */   public static BufferBuilder buffer = tessellator.getBuffer();
/*     */   
/*     */   private float hue;
/*     */   
/*     */   private float saturation;
/*     */   
/*     */   private float brightness;
/*     */   
/*     */   private float alpha;
/*     */   private boolean colorSelectorDragging;
/*     */   private boolean hueSelectorDragging;
/*     */   private boolean alphaSelectorDragging;
/*     */   
/*     */   public ColorPickerComponent(FeaturePanel featurePanel, ColorSetting setting) {
/*  34 */     this.featurePanel = featurePanel;
/*  35 */     this.setting = (Setting)setting;
/*     */     
/*  37 */     int value = setting.getColorValue();
/*  38 */     float[] hsb = getHSBFromColor(value);
/*  39 */     this.hue = hsb[0];
/*  40 */     this.saturation = hsb[1];
/*  41 */     this.brightness = hsb[2];
/*     */     
/*  43 */     this.alpha = (value >> 24 & 0xFF) / 255.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY) {
/*  49 */     int textColor = 16777215;
/*     */     
/*  51 */     mc.circleregular.drawStringWithOutline(this.setting.getName(), (this.x - 195.0F), (this.y + this.height / 2.0F - (mc.circleregular.getFontHeight() / 4)), textColor);
/*     */ 
/*     */ 
/*     */     
/*  55 */     float cpLeft = this.x + 2.0F;
/*  56 */     float cpTop = this.y + this.height + 2.0F;
/*  57 */     float cpRight = this.x + heightOffset - 2.0F;
/*  58 */     float cpBottom = this.y + this.height + heightOffset - 2.0F;
/*     */     
/*  60 */     if (mouseX <= cpLeft || mouseY <= cpTop || mouseX >= cpRight || mouseY >= cpBottom) {
/*  61 */       this.colorSelectorDragging = false;
/*     */     }
/*  63 */     float colorSelectorX = this.saturation * (cpRight - cpLeft);
/*  64 */     float colorSelectorY = (1.0F - this.brightness) * (cpBottom - cpTop);
/*     */     
/*  66 */     if (this.colorSelectorDragging) {
/*  67 */       float wWidth = cpRight - cpLeft;
/*  68 */       float xDif = mouseX - cpLeft;
/*  69 */       this.saturation = xDif / wWidth;
/*  70 */       colorSelectorX = xDif;
/*     */       
/*  72 */       float hHeight = cpBottom - cpTop;
/*  73 */       float yDif = mouseY - cpTop;
/*  74 */       this.brightness = 1.0F - yDif / hHeight;
/*  75 */       colorSelectorY = yDif;
/*     */       
/*  77 */       updateColor(Color.HSBtoRGB(this.hue, this.saturation, this.brightness), false);
/*     */     } 
/*     */     
/*  80 */     RectHelper.drawRect(cpLeft, cpTop, cpRight, cpBottom, -16777216);
/*     */     
/*  82 */     drawColorPickerRect(cpLeft + 0.5F, cpTop + 0.5F, cpRight - 0.5F, cpBottom - 0.5F);
/*     */     
/*  84 */     float selectorWidth = 2.0F;
/*  85 */     float half = selectorWidth / 2.0F;
/*     */     
/*  87 */     float csRight = cpLeft + colorSelectorX + half;
/*  88 */     float csBottom = cpTop + colorSelectorY + half;
/*  89 */     RenderHelper.drawCircle(csRight, csBottom, 1.4F, true, Color.BLACK);
/*  90 */     RenderHelper.drawCircle(csRight, csBottom, 1.0F, true, Color.WHITE);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  95 */     float sLeft = this.x + heightOffset - 1.0F;
/*  96 */     float sTop = this.y + this.height + 2.0F;
/*  97 */     float sRight = sLeft + 5.0F;
/*  98 */     float sBottom = this.y + this.height + heightOffset - 2.0F;
/*     */     
/* 100 */     if (mouseX <= sLeft || mouseY <= sTop || mouseX >= sRight || mouseY >= sBottom) {
/* 101 */       this.hueSelectorDragging = false;
/*     */     }
/*     */     
/* 104 */     float hueSelectorY = this.hue * (sBottom - sTop);
/*     */     
/* 106 */     if (this.hueSelectorDragging) {
/* 107 */       float hsHeight = sBottom - sTop;
/* 108 */       float yDif = mouseY - sTop;
/* 109 */       this.hue = yDif / hsHeight;
/* 110 */       hueSelectorY = yDif;
/*     */       
/* 112 */       updateColor(Color.HSBtoRGB(this.hue, this.saturation, this.brightness), false);
/*     */     } 
/*     */ 
/*     */     
/* 116 */     RectHelper.drawSmoothRect(sLeft, sTop, sRight, sBottom, -16777216);
/* 117 */     float inc = 0.2F;
/* 118 */     float times = 1.0F / inc;
/* 119 */     float sHeight = sBottom - sTop;
/* 120 */     float sY = sTop + 0.5F;
/* 121 */     float size = sHeight / times;
/*     */ 
/*     */     
/* 124 */     for (int i = 0; i < times; i++) {
/* 125 */       boolean last = (i == times - 1.0F);
/* 126 */       if (last) {
/* 127 */         size--;
/*     */       }
/* 129 */       gui.drawGradientRect(sLeft + 0.5F, sY, sRight - 0.5F, sY + size, Color.HSBtoRGB(inc * i, 1.0F, 1.0F), Color.HSBtoRGB(inc * (i + 1), 1.0F, 1.0F));
/* 130 */       if (!last) {
/* 131 */         sY += size;
/*     */       }
/*     */     } 
/*     */     
/* 135 */     float f1 = 1.5F;
/* 136 */     float f3 = 0.5F;
/* 137 */     float f4 = f1 / 2.0F;
/*     */     
/* 139 */     float f6 = sTop + hueSelectorY - f4;
/* 140 */     float f7 = sTop + hueSelectorY + f4;
/*     */     
/* 142 */     RectHelper.drawRect((sLeft - f3), (f6 - f3), (sRight + f3), (f7 + f3), -16777216);
/*     */     
/* 144 */     RectHelper.drawRect(sLeft, f6, sRight, f7, -1);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     sLeft = this.x + heightOffset + 6.0F;
/* 150 */     sTop = this.y + this.height + 2.0F;
/* 151 */     sRight = sLeft + 5.0F;
/* 152 */     sBottom = this.y + this.height + heightOffset - 2.0F;
/*     */     
/* 154 */     if (mouseX <= sLeft || mouseY <= sTop || mouseX >= sRight || mouseY >= sBottom) {
/* 155 */       this.alphaSelectorDragging = false;
/*     */     }
/*     */     
/* 158 */     int color = Color.HSBtoRGB(this.hue, this.saturation, this.brightness);
/*     */     
/* 160 */     int r = color >> 16 & 0xFF;
/* 161 */     int g = color >> 8 & 0xFF;
/* 162 */     int b = color & 0xFF;
/*     */     
/* 164 */     float alphaSelectorY = this.alpha * (sBottom - sTop);
/*     */     
/* 166 */     if (this.alphaSelectorDragging) {
/* 167 */       float hsHeight = sBottom - sTop;
/* 168 */       float yDif = mouseY - sTop;
/* 169 */       this.alpha = yDif / hsHeight;
/* 170 */       alphaSelectorY = yDif;
/*     */       
/* 172 */       updateColor((new Color(r, g, b, (int)(this.alpha * 255.0F))).getRGB(), true);
/*     */     } 
/*     */ 
/*     */     
/* 176 */     Gui.drawRect(sLeft, sTop, sRight, sBottom, Color.GRAY.getRGB());
/*     */     
/* 178 */     drawCheckeredBackground(sLeft + 0.5F, sTop + 0.5F, sRight - 0.5F, sBottom - 0.5F);
/*     */     
/* 180 */     gui.drawGradientRect(sLeft + 0.5F, sTop + 0.5F, sRight - 0.5F, sBottom - 0.5F, (new Color(r, g, b, 0)).getRGB(), (new Color(r, g, b, 255)).getRGB());
/*     */     
/* 182 */     float selectorHeight = 2.0F;
/* 183 */     float outlineWidth = 0.5F;
/* 184 */     float f2 = selectorHeight / 2.0F;
/*     */     
/* 186 */     float csTop = sTop + alphaSelectorY - f2;
/* 187 */     float f5 = sTop + alphaSelectorY + f2;
/*     */     
/* 189 */     RectHelper.drawRect((sLeft - outlineWidth), (csTop - outlineWidth), (sRight + outlineWidth), (f5 + outlineWidth), -16777216);
/*     */     
/* 191 */     RectHelper.drawRect(sLeft, csTop, sRight, f5, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void drawCheckeredBackground(float x, float y, float right, float bottom) {
/* 196 */     RectHelper.drawRect(x, y, right, bottom, -1);
/*     */     
/* 198 */     for (boolean off = false; y < bottom; y++) {
/* 199 */       float x1; for (x1 = x + ((off = !off) ? true : false); x1 < right; x1 += 2.0F) {
/* 200 */         RectHelper.drawRect(x1, y, (x1 + 1.0F), (y + 1.0F), -8355712);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateColor(int hex, boolean hasAlpha) {
/* 206 */     if (hasAlpha) {
/* 207 */       ((ColorSetting)this.setting).setColorValue(hex);
/*     */     } else {
/* 209 */       ((ColorSetting)this.setting).setColorValue((new Color(hex >> 16 & 0xFF, hex >> 8 & 0xFF, hex & 0xFF, (int)(this.alpha * 255.0F))).getRGB());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseClicked(int mouseX, int mouseY, int button) {
/* 215 */     if (button == 0) {
/* 216 */       float cpLeft = this.x + 2.0F;
/* 217 */       float cpTop = this.y + this.height + 2.0F;
/* 218 */       float cpRight = this.x + heightOffset - 2.0F;
/* 219 */       float cpBottom = this.y + this.height + heightOffset - 2.0F;
/*     */       
/* 221 */       float sLeft = this.x + heightOffset - 1.0F;
/* 222 */       float sTop = this.y + this.height + 2.0F;
/* 223 */       float sRight = sLeft + 5.0F;
/* 224 */       float sBottom = this.y + this.height + heightOffset - 2.0F;
/*     */       
/* 226 */       float asLeft = this.x + heightOffset + 6.0F;
/* 227 */       float asTop = this.y + this.height + 2.0F;
/* 228 */       float asRight = asLeft + 5.0F;
/* 229 */       float asBottom = this.y + this.height + heightOffset - 2.0F;
/*     */       
/* 231 */       this.colorSelectorDragging = (!this.colorSelectorDragging && mouseX > cpLeft && mouseY > cpTop && mouseX < cpRight && mouseY < cpBottom);
/* 232 */       this.hueSelectorDragging = (!this.hueSelectorDragging && mouseX > sLeft && mouseY > sTop && mouseX < sRight && mouseY < sBottom);
/* 233 */       this.alphaSelectorDragging = (!this.alphaSelectorDragging && mouseX > asLeft && mouseY > asTop && mouseX < asRight && mouseY < asBottom);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseReleased(int mouseX, int mouseY, int state) {
/* 239 */     if (this.hueSelectorDragging) {
/* 240 */       this.hueSelectorDragging = false;
/* 241 */     } else if (this.colorSelectorDragging) {
/* 242 */       this.colorSelectorDragging = false;
/* 243 */     } else if (this.alphaSelectorDragging) {
/* 244 */       this.alphaSelectorDragging = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private float[] getHSBFromColor(int hex) {
/* 249 */     int r = hex >> 16 & 0xFF;
/* 250 */     int g = hex >> 8 & 0xFF;
/* 251 */     int b = hex & 0xFF;
/* 252 */     return Color.RGBtoHSB(r, g, b, null);
/*     */   }
/*     */   
/*     */   public void drawColorPickerRect(float left, float top, float right, float bottom) {
/* 256 */     int hueBasedColor = Color.HSBtoRGB(this.hue, 1.0F, 1.0F);
/* 257 */     GlStateManager.disable(3553);
/* 258 */     GlStateManager.enableBlend();
/* 259 */     GlStateManager.shadeModel(7425);
/* 260 */     buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 261 */     buffer.pos(right, top, 0.0D).color(hueBasedColor).endVertex();
/* 262 */     buffer.pos(left, top, 0.0D).color(-1).endVertex();
/* 263 */     buffer.pos(left, bottom, 0.0D).color(-1).endVertex();
/* 264 */     buffer.pos(right, bottom, 0.0D).color(hueBasedColor).endVertex();
/* 265 */     tessellator.draw();
/* 266 */     buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 267 */     buffer.pos(right, top, 0.0D).color(402653184).endVertex();
/* 268 */     buffer.pos(left, top, 0.0D).color(402653184).endVertex();
/* 269 */     buffer.pos(left, bottom, 0.0D).color(-16777216).endVertex();
/* 270 */     buffer.pos(right, bottom, 0.0D).color(-16777216).endVertex();
/* 271 */     tessellator.draw();
/* 272 */     GlStateManager.disableBlend();
/* 273 */     GlStateManager.shadeModel(7425);
/* 274 */     GlStateManager.enable(3553);
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\newclickgui\settings\ColorPickerComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */