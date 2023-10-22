package cn.hanabi.gui.newclickui.impl;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.font.noway.ttfr.HFontRenderer;
import cn.hanabi.gui.newclickui.ClickUI;
import cn.hanabi.utils.RenderUtil;
import cn.hanabi.utils.TranslateUtil;
import cn.hanabi.value.Value;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class DoubleValue {
   public Value values;
   public float length;
   public float x;
   public float y;
   public String name;
   public boolean premouse;
   public TranslateUtil anima = new TranslateUtil(0.0F, 0.0F);

   public DoubleValue(Value values) {
      this.values = values;
      this.name = values.getValueName().split("_")[1];
   }

   public void draw(float x, float y, float mouseX, float mouseY) {
      this.x = x;
      this.y = y;
      this.length = 30.0F;
      HFontRenderer font = Hanabi.INSTANCE.fontManager.wqy18;
      font.drawString(this.name, x + 20.0F, y + 8.0F, (new Color(255, 255, 255, 255)).getRGB());
      double inc = this.values.getSteps();
      double max = (Double)this.values.getValueMax();
      double min = (Double)this.values.getValueMin();
      double valn = (Double)this.values.getValueState();
      int longValue = 90;
      this.anima.interpolate((float)((double)longValue * (valn - min) / (max - min)), 0.0F, 0.4F);
      RenderUtil.drawRoundRect((double)(x + 130.0F), (double)(y + 11.0F), (double)(x + 220.0F), (double)(y + 13.0F), 1, (new Color(70, 70, 70, 255)).getRGB());
      RenderUtil.drawRoundRect((double)(x + 130.0F), (double)(y + 11.0F), (double)(x + 130.0F + this.anima.getX()), (double)(y + 13.0F), 1, (new Color(255, 255, 255, 255)).getRGB());
      RenderUtil.circle(x + 130.0F + this.anima.getX(), y + 12.0F, 2.0F, (new Color(255, 255, 255, 255)).getRGB());
      if (ClickUI.isHover(mouseX, mouseY, x + 120.0F, y + 2.0F, x + 230.0F, y + this.length - 2.0F)) {
         double v = (double)longValue * (valn - min) / (max - min);
         font.drawCenteredString((Double)this.values.getValueState() + "", (float)((double)(x + 125.0F) + v + 4.0), y, (new Color(255, 255, 255, 255)).getRGB());
      }

   }

   public void handleMouseinRender(float mouseX, float mouseY, int key) {
      if (ClickUI.isHover(mouseX, mouseY, this.x + 120.0F, this.y + 2.0F, this.x + 230.0F, this.y + this.length - 2.0F) && Mouse.isButtonDown(0) && this.premouse) {
         double inc = this.values.getSteps();
         double max = (Double)this.values.getValueMax();
         double min = (Double)this.values.getValueMin();
         double valn = (Double)this.values.getValueState();
         int longValue = 90;
         double valAbs = (double)(mouseX - (this.x + 130.0F));
         double perc = valAbs / ((double)longValue * Math.max(Math.min(valn / max, 0.0), 1.0));
         perc = Math.min(Math.max(0.0, perc), 1.0);
         double valRel = (max - min) * perc;
         double val = min + valRel;
         val = (double)Math.round(val * (1.0 / inc)) / (1.0 / inc);
         this.values.setValueState(val);
      }

   }

   public void handleMouse(float mouseX, float mouseY, int key) {
      if (ClickUI.isHover(mouseX, mouseY, this.x + 120.0F, this.y + 2.0F, this.x + 230.0F, this.y + this.length - 2.0F)) {
         if (key == 0) {
            this.premouse = true;
         }
      } else {
         this.premouse = false;
      }

   }

   public float getLength() {
      return this.length;
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
