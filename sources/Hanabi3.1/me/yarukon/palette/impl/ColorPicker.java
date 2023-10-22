package me.yarukon.palette.impl;

import cn.hanabi.Hanabi;
import cn.hanabi.utils.RenderUtil;
import java.awt.Color;
import me.yarukon.YRenderUtil;
import me.yarukon.palette.ColorValue;
import me.yarukon.palette.PaletteGui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class ColorPicker {
   public PaletteGui parent;
   public ColorValue colorValue;
   public float x;
   public float y;
   public float width;
   public float height;
   public boolean sbDragging;
   public float sbPosX;
   public float sbPosY;
   public boolean rainbowSpeedDragging;
   public float rainbowSpeedPos = 0.0F;
   public boolean hueDragging;
   public float huePos = 0.0F;
   public boolean alphaDragging;
   public float alphaPos = 0.0F;

   public ColorPicker(PaletteGui parent, ColorValue value) {
      this.parent = parent;
      this.colorValue = value;
   }

   public void render(float x, float y, float width, float height, int mouseX, int mouseY) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      YRenderUtil.drawRect(x, y, width, height, -13684426);
      Hanabi.INSTANCE.fontManager.usans16.drawString(this.colorValue.name, x + 3.0F, y + 3.0F, -1);
      int color = this.colorValue.getColor();
      int hueShit = Color.HSBtoRGB(this.colorValue.hue, 1.0F, 1.0F);
      YRenderUtil.drawRect(x + width - 24.0F, y + 2.0F, 20.0F, 10.0F, -12237499);
      if (this.colorValue.hasAlpha) {
         drawCheckeredBackground(x + width - 24.0F, y + 2.0F, x + width - 4.0F, y + 12.0F);
      }

      YRenderUtil.drawRect(x + width - 24.0F, y + 2.0F, 20.0F, 10.0F, color);
      Hanabi.INSTANCE.fontManager.usans16.drawString("Rainbow", x + 3.0F, y + 16.0F, -1);
      YRenderUtil.drawRect(x + width - 14.0F, y + 16.0F, 10.0F, 10.0F, -12237499);
      if (this.colorValue.rainbow) {
         YRenderUtil.drawRect(x + width - 13.0F, y + 17.0F, 8.0F, 8.0F, -16732281);
      }

      Hanabi.INSTANCE.fontManager.usans16.drawString("Speed: " + (double)Math.round((double)this.colorValue.rainbowSpeed * 10.0) / 10.0, x + 3.0F, y + 29.0F, -1);
      YRenderUtil.drawRect(x + width - 104.0F, y + 32.0F, 100.0F, 3.0F, -12237499);
      this.dealWithRenderPos(0);
      YRenderUtil.drawRect(x + width - 104.0F, y + 32.0F, this.rainbowSpeedPos, 3.0F, -16732281);
      YRenderUtil.drawRect(x + width - 104.0F - 3.0F + this.rainbowSpeedPos, y + 32.0F - 1.5F, 6.0F, 6.0F, -16732281);
      if (this.rainbowSpeedDragging) {
         this.dealWithDragging(mouseX, mouseY, 0);
      }

      this.dealWithRenderPos(1);
      this.dealWithRenderPos(2);
      if (this.sbDragging) {
         this.dealWithDragging(mouseX, mouseY, 1);
         this.dealWithDragging(mouseX, mouseY, 2);
      }

      this.drawGradientRect((double)(x + 4.0F), (double)(y + 44.0F), (double)(x + width - 4.0F), (double)(y + 104.0F), true, -1, hueShit);
      this.drawGradientRect((double)(x + 4.0F), (double)(y + 44.0F), (double)(x + width - 4.0F), (double)(y + 110.5F), false, getColor(0), 0);
      RenderUtil.drawRect(x + 4.0F + this.sbPosX - 2.0F, y + 44.0F + this.sbPosY - 2.0F, x + 4.0F + this.sbPosX + 2.0F, y + 44.0F + this.sbPosY + 2.0F, -16777216);
      RenderUtil.drawRect(x + 4.0F + this.sbPosX - 1.0F, y + 44.0F + this.sbPosY - 1.0F, x + 4.0F + this.sbPosX + 1.0F, y + 44.0F + this.sbPosY + 1.0F, -1);
      float sliderLeft = x + width - 4.0F;
      float sliderRight = x + 4.0F;
      float hueSliderWidth = sliderLeft - sliderRight;
      float hueSelector = hueSliderWidth / 5.0F;
      float asLeft = sliderLeft;

      for(int i = 0; i < 5; ++i) {
         boolean last = i == 4;
         this.drawGradientSideways(asLeft - hueSelector, y + 108.0F, asLeft + hueSelector - hueSelector, y + 120.0F, getColor(Color.HSBtoRGB(1.0F - 0.2F * (float)(i + 1), 1.0F, 1.0F)), getColor(Color.HSBtoRGB(1.0F - 0.2F * (float)i, 1.0F, 1.0F)));
         if (!last) {
            asLeft -= hueSelector;
         }
      }

      this.dealWithRenderPos(3);
      if (this.hueDragging) {
         this.dealWithDragging(mouseX, mouseY, 3);
      }

      YRenderUtil.drawRect(x + 2.0F + (this.huePos < 2.0F ? 2.0F : Math.min(this.huePos, width - 8.0F)), y + 108.0F, 2.0F, 12.0F, -16777216);
      this.dealWithRenderPos(4);
      drawCheckeredBackground(x + 4.0F, y + 124.0F, x + width - 4.0F, y + 124.0F + 12.0F);
      this.drawGradientSideways(x + 4.0F, y + 124.0F, x + width - 4.0F, y + 124.0F + 12.0F, RenderUtil.reAlpha(hueShit, 0.0F), hueShit);
      YRenderUtil.drawRect(x + 2.0F + (this.alphaPos < 2.0F ? 2.0F : Math.min(this.alphaPos, width - 8.0F)), y + 124.0F, 2.0F, 12.0F, -16777216);
      if (!this.colorValue.hasAlpha) {
         YRenderUtil.drawRect(x + 4.0F, y + 124.0F, width - 8.0F, 12.0F, -1153417152);
         Hanabi.INSTANCE.fontManager.usans16.drawCenteredString("No support for alpha", x + 4.0F + (width - 8.0F) / 2.0F, y + 126.0F, -1);
      }

      if (this.alphaDragging && this.colorValue.hasAlpha) {
         this.dealWithDragging(mouseX, mouseY, 4);
      }

   }

   public void dealWithRenderPos(int type) {
      float curVal;
      float minVal;
      float maxVal;
      float barLength;
      switch (type) {
         case 0:
            curVal = this.colorValue.rainbowSpeed;
            minVal = 5.0F;
            maxVal = 15.0F;
            barLength = 100.0F;
            this.rainbowSpeedPos = barLength * (curVal - minVal) / (maxVal - minVal);
            break;
         case 1:
            curVal = this.colorValue.saturation;
            minVal = 0.0F;
            maxVal = 1.0F;
            barLength = this.width - 8.0F;
            this.sbPosX = barLength * (curVal - minVal) / (maxVal - minVal);
            break;
         case 2:
            curVal = this.colorValue.brightness;
            minVal = 0.0F;
            maxVal = 1.0F;
            barLength = 60.0F;
            this.sbPosY = barLength * (curVal - minVal) / (maxVal - minVal);
            break;
         case 3:
            curVal = this.colorValue.hue;
            minVal = 0.0F;
            maxVal = 1.0F;
            barLength = this.width - 8.0F;
            this.huePos = barLength * (curVal - minVal) / (maxVal - minVal);
            break;
         case 4:
            curVal = this.colorValue.alpha;
            minVal = 0.0F;
            maxVal = 1.0F;
            barLength = this.width - 8.0F;
            this.alphaPos = barLength * (curVal - minVal) / (maxVal - minVal);
      }

   }

   public void dealWithDragging(int mouseX, int mouseY, int type) {
      float curVal;
      float minVal;
      float maxVal;
      float step;
      float barLength;
      float valAbs;
      float perc;
      float valRel;
      float val;
      switch (type) {
         case 0:
            curVal = this.colorValue.rainbowSpeed;
            minVal = 5.0F;
            maxVal = 15.0F;
            step = 0.1F;
            barLength = 100.0F;
            valAbs = (float)mouseX - (this.x + this.width - 104.0F);
            perc = valAbs / (barLength * Math.max(Math.min(curVal / maxVal, 0.0F), 1.0F));
            perc = Math.min(Math.max(0.0F, perc), 1.0F);
            valRel = (maxVal - minVal) * perc;
            val = minVal + valRel;
            this.colorValue.rainbowSpeed = (float)Math.round(val * (1.0F / step)) / (1.0F / step);
            break;
         case 1:
            curVal = this.colorValue.saturation;
            minVal = 0.0F;
            maxVal = 1.0F;
            step = 0.01F;
            barLength = this.width - 8.0F;
            valAbs = (float)mouseX - (this.x + 4.0F);
            perc = valAbs / (barLength * Math.max(Math.min(curVal / maxVal, 0.0F), 1.0F));
            perc = Math.min(Math.max(0.0F, perc), 1.0F);
            valRel = (maxVal - minVal) * perc;
            val = minVal + valRel;
            this.colorValue.saturation = (float)Math.round(val * (1.0F / step)) / (1.0F / step);
            break;
         case 2:
            curVal = this.colorValue.brightness;
            minVal = 0.0F;
            maxVal = 1.0F;
            step = 0.01F;
            barLength = 60.0F;
            valAbs = (float)mouseY - (this.y + 44.0F);
            perc = valAbs / (barLength * Math.max(Math.min(curVal / maxVal, 0.0F), 1.0F));
            perc = Math.min(Math.max(0.0F, perc), 1.0F);
            valRel = (maxVal - minVal) * perc;
            val = minVal + valRel;
            this.colorValue.brightness = (float)Math.round(val * (1.0F / step)) / (1.0F / step);
            break;
         case 3:
            curVal = this.colorValue.hue;
            minVal = 0.0F;
            maxVal = 1.0F;
            step = 0.01F;
            barLength = this.width - 8.0F;
            valAbs = (float)mouseX - (this.x + 4.0F);
            perc = valAbs / (barLength * Math.max(Math.min(curVal / maxVal, 0.0F), 1.0F));
            perc = Math.min(Math.max(0.0F, perc), 1.0F);
            valRel = (maxVal - minVal) * perc;
            val = minVal + valRel;
            this.colorValue.hue = (float)Math.round(val * (1.0F / step)) / (1.0F / step);
            break;
         case 4:
            curVal = this.colorValue.alpha;
            minVal = 0.0F;
            maxVal = 1.0F;
            step = 0.01F;
            barLength = this.width - 8.0F;
            valAbs = (float)mouseX - (this.x + 4.0F);
            perc = valAbs / (barLength * Math.max(Math.min(curVal / maxVal, 0.0F), 1.0F));
            perc = Math.min(Math.max(0.0F, perc), 1.0F);
            valRel = (maxVal - minVal) * perc;
            val = minVal + valRel;
            this.colorValue.alpha = (float)Math.round(val * (1.0F / step)) / (1.0F / step);
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (YRenderUtil.isHoveringBound((float)mouseX, (float)mouseY, this.parent.x, this.parent.y, 200.0F, 300.0F) && this.y > this.parent.y + this.parent.draggableHeight && this.y < this.parent.y + this.parent.draggableHeight + 300.0F) {
         if (YRenderUtil.isHoveringBound((float)mouseX, (float)mouseY, this.x + this.width - 104.0F - 3.0F + this.rainbowSpeedPos, this.y + 32.0F - 1.5F, 6.0F, 6.0F) || YRenderUtil.isHoveringBound((float)mouseX, (float)mouseY, this.x + this.width - 104.0F, this.y + 32.0F, 100.0F, 3.0F)) {
            this.rainbowSpeedDragging = true;
         }

         if (YRenderUtil.isHoveringBound((float)mouseX, (float)mouseY, this.x + 4.0F, this.y + 44.0F, this.width - 8.0F, 60.0F)) {
            this.sbDragging = true;
         }

         if (YRenderUtil.isHoveringBound((float)mouseX, (float)mouseY, this.x + 4.0F, this.y + 108.0F, this.width - 8.0F, 12.0F)) {
            this.hueDragging = true;
         }

         if (YRenderUtil.isHoveringBound((float)mouseX, (float)mouseY, this.x + 4.0F, this.y + 124.0F, this.width - 8.0F, 12.0F)) {
            this.alphaDragging = true;
         }

         if (YRenderUtil.isHoveringBound((float)mouseX, (float)mouseY, this.x + this.width - 14.0F, this.y + 16.0F, 10.0F, 10.0F)) {
            this.colorValue.rainbow = !this.colorValue.rainbow;
         }
      }

   }

   public void mouseReleased(int mouseX, int mouseY, int state) {
      this.rainbowSpeedDragging = false;
      this.sbDragging = false;
      this.hueDragging = false;
      this.alphaDragging = false;
   }

   private static void drawCheckeredBackground(float left, float top, float right, float bottom) {
      YRenderUtil.drawRectNormal(left, top, right, bottom, -1);

      for(boolean offset = false; top < bottom; ++top) {
         for(float x1 = left + (float)((offset = !offset) ? 1 : 0); x1 < right; x1 += 2.0F) {
            if (x1 <= right - 1.0F) {
               YRenderUtil.drawRectNormal(x1, top, x1 + 1.0F, top + 1.0F, -8355712);
            }
         }
      }

   }

   public void drawGradientSideways(float left, float top, float right, float bottom, int startColor, int endColor) {
      float f = (float)(startColor >> 24 & 255) / 255.0F;
      float f1 = (float)(startColor >> 16 & 255) / 255.0F;
      float f2 = (float)(startColor >> 8 & 255) / 255.0F;
      float f3 = (float)(startColor & 255) / 255.0F;
      float f4 = (float)(endColor >> 24 & 255) / 255.0F;
      float f5 = (float)(endColor >> 16 & 255) / 255.0F;
      float f6 = (float)(endColor >> 8 & 255) / 255.0F;
      float f7 = (float)(endColor & 255) / 255.0F;
      GlStateManager.disableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.shadeModel(7425);
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
      worldrenderer.pos((double)right, (double)top, 0.0).color(f5, f6, f7, f4).endVertex();
      worldrenderer.pos((double)left, (double)top, 0.0).color(f1, f2, f3, f).endVertex();
      worldrenderer.pos((double)left, (double)bottom, 0.0).color(f1, f2, f3, f).endVertex();
      worldrenderer.pos((double)right, (double)bottom, 0.0).color(f5, f6, f7, f4).endVertex();
      Tessellator.getInstance().draw();
      GlStateManager.shadeModel(7424);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.enableTexture2D();
   }

   public void drawGradientRect(double left, double top, double right, double bottom, boolean sideways, int startColor, int endColor) {
      GL11.glDisable(3553);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glShadeModel(7425);
      GL11.glBegin(7);
      RenderUtil.color(startColor);
      if (sideways) {
         GL11.glVertex2d(left, top);
         GL11.glVertex2d(left, bottom);
         RenderUtil.color(endColor);
         GL11.glVertex2d(right, bottom);
         GL11.glVertex2d(right, top);
      } else {
         GL11.glVertex2d(left, top);
         RenderUtil.color(endColor);
         GL11.glVertex2d(left, bottom);
         GL11.glVertex2d(right, bottom);
         RenderUtil.color(startColor);
         GL11.glVertex2d(right, top);
      }

      GL11.glEnd();
      GL11.glDisable(3042);
      GL11.glShadeModel(7424);
      GL11.glEnable(3553);
   }

   public static int getColor(int color) {
      int r = color >> 16 & 255;
      int g = color >> 8 & 255;
      int b = color & 255;
      int a = 255;
      return (r & 255) << 16 | (g & 255) << 8 | b & 255 | (a & 255) << 24;
   }
}
