package team.massacre.api.ui.csgo.component.impl.sub.color;

import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import team.massacre.api.property.ValueChangeListener;
import team.massacre.api.ui.csgo.SkeetUI;
import team.massacre.api.ui.framework.component.ButtonComponent;
import team.massacre.api.ui.framework.component.Component;
import team.massacre.api.ui.framework.component.ExpandableComponent;
import team.massacre.api.ui.framework.component.PredicateComponent;
import team.massacre.utils.RenderingUtils;

public abstract class ColorPickerComponent extends ButtonComponent implements PredicateComponent, ExpandableComponent {
   private static final int MARGIN = 3;
   private static final int SLIDER_THICKNESS = 8;
   private static final float SELECTOR_WIDTH = 1.0F;
   private static final float HALF_WIDTH = 0.5F;
   private static final float OUTLINE_WIDTH = 0.5F;
   private boolean expanded;
   private float hue;
   private float saturation;
   private float brightness;
   private float alpha;
   private boolean colorSelectorDragging;
   private boolean hueSelectorDragging;
   private boolean alphaSelectorDragging;

   public ColorPickerComponent(Component parent, float x, float y, float width, float height) {
      super(parent, x, y, width, height);
      this.addValueChangeListener(this::onValueChange);
   }

   private static void drawCheckeredBackground(float x, float y, float x2, float y2) {
      Gui.drawRect((double)x, (double)y, (double)x2, (double)y2, SkeetUI.getColor(16777215));

      for(boolean offset = false; y < y2; ++y) {
         for(float x1 = x + (float)((offset = !offset) ? 1 : 0); x1 < x2; x1 += 2.0F) {
            if (!(x1 > x2 - 1.0F)) {
               Gui.drawRect((double)x1, (double)y, (double)(x1 + 1.0F), (double)(y + 1.0F), SkeetUI.getColor(8421504));
            }
         }
      }

   }

   public void drawComponent(ScaledResolution lockedResolution, int mouseX, int mouseY) {
      float x = this.getX();
      float y = this.getY();
      float width = this.getWidth();
      float height = this.getHeight();
      int black = SkeetUI.getColor(0);
      Gui.drawRect((double)x - 0.5D, (double)y - 0.5D, (double)(x + width) + 0.5D, (double)(y + height) + 0.5D, black);
      int guiAlpha = (int)SkeetUI.getAlpha();
      int color = this.getColor();
      int colorAlpha = color >> 24 & 255;
      int minAlpha = Math.min(guiAlpha, colorAlpha);
      if (colorAlpha < 255) {
         drawCheckeredBackground(x, y, x + width, y + height);
      }

      int newColor = (new Color(color >> 16 & 255, color >> 8 & 255, color & 255, minAlpha)).getRGB();
      Gui.drawGradientRect(x, y, x + width, y + height, newColor, RenderingUtils.darker(newColor));
      if (this.isExpanded()) {
         GL11.glTranslated(0.0D, 0.0D, 3.0D);
         float expandedX = this.getExpandedX();
         float expandedY = this.getExpandedY();
         float expandedWidth = this.getExpandedWidth();
         float expandedHeight = this.getExpandedHeight();
         Gui.drawRect((double)expandedX, (double)expandedY, (double)(expandedX + expandedWidth), (double)(expandedY + expandedHeight), black);
         Gui.drawRect((double)expandedX + 0.5D, (double)expandedY + 0.5D, (double)(expandedX + expandedWidth) - 0.5D, (double)(expandedY + expandedHeight) - 0.5D, SkeetUI.getColor(3750203));
         Gui.drawRect((double)(expandedX + 1.0F), (double)(expandedY + 1.0F), (double)(expandedX + expandedWidth - 1.0F), (double)(expandedY + expandedHeight - 1.0F), SkeetUI.getColor(2302755));
         float colorPickerSize = expandedWidth - 9.0F - 8.0F;
         float colorPickerLeft = expandedX + 3.0F;
         float colorPickerTop = expandedY + 3.0F;
         float colorPickerRight = colorPickerLeft + colorPickerSize;
         float colorPickerBottom = colorPickerTop + colorPickerSize;
         int selectorWhiteOverlayColor = (new Color(255, 255, 255, Math.min(guiAlpha, 180))).getRGB();
         if ((float)mouseX <= colorPickerLeft || (float)mouseY <= colorPickerTop || (float)mouseX >= colorPickerRight || (float)mouseY >= colorPickerBottom) {
            this.colorSelectorDragging = false;
         }

         Gui.drawRect((double)colorPickerLeft - 0.5D, (double)colorPickerTop - 0.5D, (double)colorPickerRight + 0.5D, (double)colorPickerBottom + 0.5D, SkeetUI.getColor(0));
         this.drawColorPickerRect(colorPickerLeft, colorPickerTop, colorPickerRight, colorPickerBottom);
         float hueSliderLeft = this.saturation * (colorPickerRight - colorPickerLeft);
         float alphaSliderTop = (1.0F - this.brightness) * (colorPickerBottom - colorPickerTop);
         float hueSliderRight;
         float alphaSliderBottom;
         float hueSliderYDif;
         float hueSelectorY;
         if (this.colorSelectorDragging) {
            hueSliderRight = colorPickerRight - colorPickerLeft;
            alphaSliderBottom = (float)mouseX - colorPickerLeft;
            this.saturation = alphaSliderBottom / hueSliderRight;
            hueSliderLeft = alphaSliderBottom;
            hueSliderYDif = colorPickerBottom - colorPickerTop;
            hueSelectorY = (float)mouseY - colorPickerTop;
            this.brightness = 1.0F - hueSelectorY / hueSliderYDif;
            alphaSliderTop = hueSelectorY;
            this.updateColor(Color.HSBtoRGB(this.hue, this.saturation, this.brightness), false);
         }

         hueSliderRight = colorPickerLeft + hueSliderLeft - 0.5F;
         alphaSliderBottom = colorPickerTop + alphaSliderTop - 0.5F;
         hueSliderYDif = colorPickerLeft + hueSliderLeft + 0.5F;
         hueSelectorY = colorPickerTop + alphaSliderTop + 0.5F;
         Gui.drawRect((double)(hueSliderRight - 0.5F), (double)(alphaSliderBottom - 0.5F), (double)hueSliderRight, (double)(hueSelectorY + 0.5F), black);
         Gui.drawRect((double)hueSliderYDif, (double)(alphaSliderBottom - 0.5F), (double)(hueSliderYDif + 0.5F), (double)(hueSelectorY + 0.5F), black);
         Gui.drawRect((double)hueSliderRight, (double)(alphaSliderBottom - 0.5F), (double)hueSliderYDif, (double)alphaSliderBottom, black);
         Gui.drawRect((double)hueSliderRight, (double)hueSelectorY, (double)hueSliderYDif, (double)(hueSelectorY + 0.5F), black);
         Gui.drawRect((double)hueSliderRight, (double)alphaSliderBottom, (double)hueSliderYDif, (double)hueSelectorY, selectorWhiteOverlayColor);
         hueSliderLeft = colorPickerRight + 3.0F;
         hueSliderRight = hueSliderLeft + 8.0F;
         if ((float)mouseX <= hueSliderLeft || (float)mouseY <= colorPickerTop || (float)mouseX >= hueSliderRight || (float)mouseY >= colorPickerBottom) {
            this.hueSelectorDragging = false;
         }

         hueSliderYDif = colorPickerBottom - colorPickerTop;
         hueSelectorY = (1.0F - this.hue) * hueSliderYDif;
         float inc;
         if (this.hueSelectorDragging) {
            inc = (float)mouseY - colorPickerTop;
            this.hue = 1.0F - inc / hueSliderYDif;
            hueSelectorY = inc;
            this.updateColor(Color.HSBtoRGB(this.hue, this.saturation, this.brightness), false);
         }

         Gui.drawRect((double)hueSliderLeft - 0.5D, (double)colorPickerTop - 0.5D, (double)hueSliderRight + 0.5D, (double)colorPickerBottom + 0.5D, black);
         inc = 0.2F;
         float times = 5.0F;
         float hsHeight = colorPickerBottom - colorPickerTop;
         float alphaSelectorX = hsHeight / 5.0F;
         float asLeft = colorPickerTop;

         for(int i = 0; (float)i < 5.0F; ++i) {
            boolean last = (float)i == 4.0F;
            Gui.drawGradientRect(hueSliderLeft, asLeft, hueSliderRight, asLeft + alphaSelectorX, SkeetUI.getColor(Color.HSBtoRGB(1.0F - 0.2F * (float)i, 1.0F, 1.0F)), SkeetUI.getColor(Color.HSBtoRGB(1.0F - 0.2F * (float)(i + 1), 1.0F, 1.0F)));
            if (!last) {
               asLeft += alphaSelectorX;
            }
         }

         float hsTop = colorPickerTop + hueSelectorY - 0.5F;
         float asRight = colorPickerTop + hueSelectorY + 0.5F;
         Gui.drawRect((double)(hueSliderLeft - 0.5F), (double)(hsTop - 0.5F), (double)hueSliderLeft, (double)(asRight + 0.5F), black);
         Gui.drawRect((double)hueSliderRight, (double)(hsTop - 0.5F), (double)(hueSliderRight + 0.5F), (double)(asRight + 0.5F), black);
         Gui.drawRect((double)hueSliderLeft, (double)(hsTop - 0.5F), (double)hueSliderRight, (double)hsTop, black);
         Gui.drawRect((double)hueSliderLeft, (double)asRight, (double)hueSliderRight, (double)(asRight + 0.5F), black);
         Gui.drawRect((double)hueSliderLeft, (double)hsTop, (double)hueSliderRight, (double)asRight, selectorWhiteOverlayColor);
         alphaSliderTop = colorPickerBottom + 3.0F;
         alphaSliderBottom = alphaSliderTop + 8.0F;
         if ((float)mouseX <= colorPickerLeft || (float)mouseY <= alphaSliderTop || (float)mouseX >= colorPickerRight || (float)mouseY >= alphaSliderBottom) {
            this.alphaSelectorDragging = false;
         }

         int color = Color.HSBtoRGB(this.hue, this.saturation, this.brightness);
         int r = color >> 16 & 255;
         int g = color >> 8 & 255;
         int b = color & 255;
         hsHeight = colorPickerRight - colorPickerLeft;
         alphaSelectorX = this.alpha * hsHeight;
         if (this.alphaSelectorDragging) {
            asLeft = (float)mouseX - colorPickerLeft;
            this.alpha = asLeft / hsHeight;
            alphaSelectorX = asLeft;
            this.updateColor((new Color(r, g, b, (int)(this.alpha * 255.0F))).getRGB(), true);
         }

         Gui.drawRect((double)colorPickerLeft - 0.5D, (double)alphaSliderTop - 0.5D, (double)colorPickerRight + 0.5D, (double)alphaSliderBottom + 0.5D, black);
         drawCheckeredBackground(colorPickerLeft, alphaSliderTop, colorPickerRight, alphaSliderBottom);
         RenderingUtils.drawGradientRect((double)colorPickerLeft, (double)alphaSliderTop, (double)colorPickerRight, (double)alphaSliderBottom, true, (new Color(r, g, b, 0)).getRGB(), (new Color(r, g, b, Math.min(guiAlpha, 255))).getRGB());
         asLeft = colorPickerLeft + alphaSelectorX - 0.5F;
         asRight = colorPickerLeft + alphaSelectorX + 0.5F;
         Gui.drawRect((double)(asLeft - 0.5F), (double)alphaSliderTop, (double)(asRight + 0.5F), (double)alphaSliderBottom, black);
         Gui.drawRect((double)asLeft, (double)alphaSliderTop, (double)asRight, (double)alphaSliderBottom, selectorWhiteOverlayColor);
         GL11.glTranslated(0.0D, 0.0D, -3.0D);
      }

   }

   public void onMouseClick(int mouseX, int mouseY, int button) {
      super.onMouseClick(mouseX, mouseY, button);
      if (this.isExpanded() && button == 0) {
         float expandedX = this.getExpandedX();
         float expandedY = this.getExpandedY();
         float expandedWidth = this.getExpandedWidth();
         float expandedHeight = this.getExpandedHeight();
         float colorPickerSize = expandedWidth - 9.0F - 8.0F;
         float colorPickerLeft = expandedX + 3.0F;
         float colorPickerTop = expandedY + 3.0F;
         float colorPickerRight = colorPickerLeft + colorPickerSize;
         float colorPickerBottom = colorPickerTop + colorPickerSize;
         float alphaSliderTop = colorPickerBottom + 3.0F;
         float alphaSliderBottom = alphaSliderTop + 8.0F;
         float hueSliderLeft = colorPickerRight + 3.0F;
         float hueSliderRight = hueSliderLeft + 8.0F;
         this.colorSelectorDragging = !this.colorSelectorDragging && (float)mouseX > colorPickerLeft && (float)mouseY > colorPickerTop && (float)mouseX < colorPickerRight && (float)mouseY < colorPickerBottom;
         this.alphaSelectorDragging = !this.alphaSelectorDragging && (float)mouseX > colorPickerLeft && (float)mouseY > alphaSliderTop && (float)mouseX < colorPickerRight && (float)mouseY < alphaSliderBottom;
         this.hueSelectorDragging = !this.hueSelectorDragging && (float)mouseX > hueSliderLeft && (float)mouseY > colorPickerTop && (float)mouseX < hueSliderRight && (float)mouseY < colorPickerBottom;
      }

   }

   public void onMouseRelease(int button) {
      if (this.colorSelectorDragging) {
         this.colorSelectorDragging = false;
      }

      if (this.alphaSelectorDragging) {
         this.alphaSelectorDragging = false;
      }

      if (this.hueSelectorDragging) {
         this.hueSelectorDragging = false;
      }

   }

   private void updateColor(int hex, boolean hasAlpha) {
      if (hasAlpha) {
         this.setColor(hex);
      } else {
         this.setColor((new Color(hex >> 16 & 255, hex >> 8 & 255, hex & 255, (int)(this.alpha * 255.0F))).getRGB());
      }

   }

   public abstract int getColor();

   public abstract void setColor(int var1);

   public abstract void addValueChangeListener(ValueChangeListener<Integer> var1);

   public void onValueChange(int oldValue, int value) {
      float[] hsb = this.getHSBFromColor(value);
      this.hue = hsb[0];
      this.saturation = hsb[1];
      this.brightness = hsb[2];
      this.alpha = (float)(value >> 24 & 255) / 255.0F;
   }

   private float[] getHSBFromColor(int hex) {
      int r = hex >> 16 & 255;
      int g = hex >> 8 & 255;
      int b = hex & 255;
      return Color.RGBtoHSB(r, g, b, (float[])null);
   }

   private void drawColorPickerRect(float left, float top, float right, float bottom) {
      int hueBasedColor = SkeetUI.getColor(Color.HSBtoRGB(this.hue, 1.0F, 1.0F));
      RenderingUtils.drawGradientRect((double)left, (double)top, (double)right, (double)bottom, true, SkeetUI.getColor(16777215), hueBasedColor);
      Gui.drawGradientRect(left, top, right, bottom, 0, SkeetUI.getColor(0));
   }

   public float getExpandedX() {
      return this.getX() + this.getWidth() - 80.333336F;
   }

   public float getExpandedY() {
      return this.getY() + this.getHeight();
   }

   public boolean isExpanded() {
      return this.expanded;
   }

   public void setExpanded(boolean expanded) {
      this.expanded = expanded;
   }

   public void onPress(int mouseButton) {
      if (mouseButton == 1) {
         this.setExpanded(!this.isExpanded());
      }

   }

   public float getExpandedWidth() {
      float right = this.getX() + this.getWidth();
      return right - this.getExpandedX();
   }

   public float getExpandedHeight() {
      return this.getExpandedWidth();
   }
}
