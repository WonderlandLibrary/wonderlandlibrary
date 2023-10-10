package my.NewSnake.utils;

import java.awt.Color;
import java.awt.Rectangle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class GuiUtils {
   static boolean fadeIn;
   public static final RenderItem RENDER_ITEM;
   static int fade;
   private static ScaledResolution scaledResolution;
   static boolean fadeOut;

   public static void glColor(int var0) {
      float var1 = (float)(var0 >> 24 & 255) / 255.0F;
      float var2 = (float)(var0 >> 16 & 255) / 255.0F;
      float var3 = (float)(var0 >> 8 & 255) / 255.0F;
      float var4 = (float)(var0 & 255) / 255.0F;
      GL11.glColor4f(var2, var3, var4, var1);
   }

   public static void drawRect(Rectangle var0, int var1) {
      drawRect((float)var0.x, (float)var0.y, (float)(var0.x + var0.width), (float)(var0.y + var0.height), var1);
   }

   public static void drawRoundedRect(float var0, float var1, float var2, float var3, int var4, int var5) {
      enableGL2D();
      drawRect1(var0 + 0.5F, var1, var2 - 0.5F, var1 + 0.5F, var5);
      drawRect1(var0 + 0.5F, var3 - 0.5F, var2 - 0.5F, var3, var5);
      drawRect1(var0, var1 + 0.5F, var2, var3 - 0.5F, var5);
      disableGL2D();
   }

   public static void drawRect(Vector2f var0, Vector2f var1, int var2) {
      Gui.drawRect((double)((int)var0.getX()), (double)((int)var0.getY()), (double)((int)var1.getX()), (double)((int)var1.getY()), var2);
   }

   public static void drawHLine(float var0, float var1, float var2, int var3, int var4) {
      if (var1 < var0) {
         float var5 = var0;
         var0 = var1;
         var1 = var5;
      }

      drawGradientRect(var0, var2, var1 + 1.0F, var2 + 1.0F, var3, var4);
   }

   public static void glColor(float var0, int var1, int var2, int var3) {
      float var4 = 0.003921569F * (float)var1;
      float var5 = 0.003921569F * (float)var2;
      float var6 = 0.003921569F * (float)var3;
      GL11.glColor4f(var4, var5, var6, var0);
   }

   public static void drawBar(float var0, float var1, float var2, float var3, float var4, float var5, int var6) {
      Gui.drawRect((double)var2, (double)var3, (double)(var2 + var4 * (var0 / var1)), (double)(var3 + var5), var6);
   }

   public static void drawVLine(float var0, float var1, float var2, int var3) {
      if (var2 < var1) {
         float var4 = var1;
         var1 = var2;
         var2 = var4;
      }

      drawRect(var0, var1 + 1.0F, var0 + 1.0F, var2, var3);
   }

   public static ScaledResolution getScaledResolution() {
      return scaledResolution;
   }

   public static void drawBorderedRect(float var0, float var1, float var2, float var3, float var4, int var5, int var6) {
      enableGL2D();
      glColor(var5);
      drawRect(var0 + var4, var1 + var4, var2 - var4, var3 - var4);
      glColor(var6);
      drawRect(var0 + var4, var1, var2 - var4, var1 + var4);
      drawRect(var0, var1, var0 + var4, var3);
      drawRect(var2 - var4, var1, var2, var3);
      drawRect(var0 + var4, var3 - var4, var2 - var4, var3);
      disableGL2D();
   }

   public static void draw2DRect(double var0, double var2, double var4, double var6, int var8) {
      double var9;
      if (var0 < var4) {
         var9 = var0;
         var0 = var4;
         var4 = var9;
      }

      if (var2 < var6) {
         var9 = var2;
         var2 = var6;
         var6 = var9;
      }

      float var11 = (float)(var8 >> 24 & 255) / 255.0F;
      float var12 = (float)(var8 >> 16 & 255) / 255.0F;
      float var13 = (float)(var8 >> 8 & 255) / 255.0F;
      float var14 = (float)(var8 & 255) / 255.0F;
      Tessellator var15 = Tessellator.getInstance();
      WorldRenderer var16 = var15.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(var12, var13, var14, var11);
      var16.begin(7, DefaultVertexFormats.POSITION);
      var16.pos(var0, var6, 0.0D);
      var16.pos(var4, var6, 0.0D);
      var16.pos(var4, var2, 0.0D);
      var16.pos(var0, var2, 0.0D);
      var15.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   static {
      RENDER_ITEM = new RenderItem(Minecraft.getMinecraft().renderEngine, Minecraft.getMinecraft().modelManager);
      fade = 0;
      fadeIn = true;
      fadeOut = false;
   }

   public static void drawStrip(int var0, int var1, float var2, double var3, float var5, float var6, int var7) {
      float var8 = (float)(var7 >> 24 & 255) / 255.0F;
      float var9 = (float)(var7 >> 16 & 255) / 255.0F;
      float var10 = (float)(var7 >> 8 & 255) / 255.0F;
      float var11 = (float)(var7 & 255) / 255.0F;
      GL11.glPushMatrix();
      GL11.glTranslated((double)var0, (double)var1, 0.0D);
      GL11.glColor4f(var9, var10, var11, var8);
      GL11.glLineWidth(var2);
      int var12;
      float var13;
      float var14;
      float var15;
      if (var3 > 0.0D) {
         GL11.glBegin(3);

         for(var12 = 0; (double)var12 < var3; ++var12) {
            var13 = (float)((double)var12 * (var3 * 3.141592653589793D / (double)var5));
            var14 = (float)(Math.cos((double)var13) * (double)var6);
            var15 = (float)(Math.sin((double)var13) * (double)var6);
            GL11.glVertex2f(var14, var15);
         }

         GL11.glEnd();
      }

      if (var3 < 0.0D) {
         GL11.glBegin(3);

         for(var12 = 0; (double)var12 > var3; --var12) {
            var13 = (float)((double)var12 * (var3 * 3.141592653589793D / (double)var5));
            var14 = (float)(Math.cos((double)var13) * (double)(-var6));
            var15 = (float)(Math.sin((double)var13) * (double)(-var6));
            GL11.glVertex2f(var14, var15);
         }

         GL11.glEnd();
      }

      disableGL2D();
      GL11.glDisable(3479);
      GL11.glPopMatrix();
   }

   public static void drawBorderedRect(double var0, double var2, double var4, double var6, double var8, int var10, int var11) {
      Gui.drawRect(var0, var2, var0 + var4, var2 + var6, var11);
      Gui.drawRect(var0, var2, var0 + var4, var2 + var8, var10);
      Gui.drawRect(var0, var2, var0 + var8, var2 + var6, var10);
      Gui.drawRect(var0 + var4, var2, var0 + var4 - var8, var2 + var6, var10);
      Gui.drawRect(var0, var2 + var6, var0 + var4, var2 + var6 - var8, var10);
   }

   public static void drawClickguiRect(float var0, float var1, float var2, float var3, int var4, int var5) {
      enableGL2D();
      var0 *= 2.0F;
      var2 *= 2.0F;
      var1 *= 2.0F;
      var3 *= 2.0F;
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      drawVLine(var0, var1, var3, var4);
      drawVLine(var2 - 1.0F, var1, var3, var4);
      drawHLine(var0, var2 - 1.0F, var1, var4);
      drawHLine(var0, var2 - 2.0F, var3 - 1.0F, var4);
      drawRect(var0 + 1.0F, var1 + 1.0F, var2 - 1.0F, var3 - 1.0F, var4);
      drawVLine(var0 + 1.0F, var1, var3, var4);
      drawVLine(var2 - 2.0F, var1, var3, var4);
      drawHLine(var0, var2 - 1.0F, var1 + 1.0F, var4);
      drawHLine(var0, var2 - 2.0F, var3 - 2.0F, var4);
      drawVLine(var0 + 2.0F, var1, var3, var4);
      drawVLine(var2 - 3.0F, var1, var3, var4);
      drawHLine(var0, var2 - 4.0F, var1 + 2.0F, var4);
      drawHLine(var0, var2 - 2.0F, var3 - 3.0F, var4);
      drawVLine(var0 + 3.0F, var1 + 3.0F, var3 - 3.0F, var5);
      drawVLine(var2 - 4.0F, var1 + 3.0F, var3 - 3.0F, var5);
      drawHLine(var0 + 3.0F, var2 - 4.0F, var1 + 3.0F, var5);
      drawHLine(var0 + 3.0F, var2 - 4.0F, var3 - 4.0F, var5);
      drawVLine(var0 + 4.0F, var1 + 3.0F, var3 - 3.0F, var5);
      drawVLine(var2 - 5.0F, var1 + 3.0F, var3 - 3.0F, var5);
      drawHLine(var0 + 3.0F, var2 - 4.0F, var1 + 4.0F, var5);
      drawHLine(var0 + 3.0F, var2 - 4.0F, var3 - 5.0F, var5);
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      disableGL2D();
   }

   public static void drawRect1(float var0, float var1, float var2, float var3) {
      GL11.glBegin(7);
      GL11.glVertex2f(var0, var3);
      GL11.glVertex2f(var2, var3);
      GL11.glVertex2f(var2, var1);
      GL11.glVertex2f(var0, var1);
      GL11.glEnd();
   }

   public static void drawBorderedRect(float var0, float var1, float var2, float var3, int var4, int var5) {
      enableGL2D();
      var0 *= 2.0F;
      var2 *= 2.0F;
      var1 *= 2.0F;
      var3 *= 2.0F;
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      drawVLine(var0, var1, var3, var5);
      drawVLine(var2 - 1.0F, var1, var3, var5);
      drawHLine(var0, var2 - 1.0F, var1, var5);
      drawHLine(var0, var2 - 2.0F, var3 - 1.0F, var5);
      drawRect(var0 + 1.0F, var1 + 1.0F, var2 - 1.0F, var3 - 1.0F, var4);
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      disableGL2D();
   }

   public static void drawRect(float var0, float var1, float var2, float var3) {
      GL11.glBegin(7);
      GL11.glVertex2f(var0, var3);
      GL11.glVertex2f(var2, var3);
      GL11.glVertex2f(var2, var1);
      GL11.glVertex2f(var0, var1);
      GL11.glEnd();
   }

   public static void enableGL2D() {
      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glDepthMask(true);
      GL11.glEnable(2848);
      GL11.glHint(3154, 4354);
      GL11.glHint(3155, 4354);
   }

   public static void drawSpecialGradRect(int var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      enableGL2D();
      GL11.glShadeModel(7425);
      GL11.glBegin(7);
      glColor(var5);
      GL11.glVertex2f((float)var1, (float)var2);
      GL11.glVertex2f((float)var1, (float)var4);
      glColor(var6);
      GL11.glVertex2f((float)var3, (float)var4);
      GL11.glVertex2f((float)var3, (float)var2);
      GL11.glEnd();
      GL11.glShadeModel(7424);
      disableGL2D();
      float var7 = (float)((new Color(0, 0, 0, 0)).getRGB() >> 24 & 255) / 255.0F;
      float var8 = (float)((new Color(0, 0, 0, 0)).getRGB() >> 16 & 255) / 255.0F;
      float var9 = (float)((new Color(0, 0, 0, 0)).getRGB() >> 8 & 255) / 255.0F;
      float var10 = (float)((new Color(0, 0, 0, 0)).getRGB() & 255) / 255.0F;
      Tessellator var11 = Tessellator.getInstance();
      WorldRenderer var12 = var11.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(var8, var9, var10, var7);
      var12.begin(var0, DefaultVertexFormats.POSITION);
      var12.pos((double)var1, (double)var4, 0.0D).endVertex();
      var12.pos((double)var3, (double)var4, 0.0D).endVertex();
      var12.pos((double)var3, (double)var2, 0.0D).endVertex();
      var12.pos((double)var1, (double)var2, 0.0D).endVertex();
      var11.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawOutlineRect(float var0, float var1, float var2, float var3, int var4) {
      drawRect(var0, var1, var2, var1 + 1.0F, var4);
      drawRect(var0, var1 + 1.0F, var0 + 1.0F, var3, var4);
      drawRect(var2 - 1.0F, var1 + 1.0F, var2, var3 - 1.0F, var4);
      drawRect(var0 + 1.0F, var3 - 1.0F, var2, var3, var4);
   }

   public static void drawRect(float var0, float var1, float var2, float var3, int var4) {
      enableGL2D();
      glColor(var4);
      drawRect(var0, var1, var2, var3);
      disableGL2D();
   }

   public static void drawLargeString(String var0, int var1, int var2, int var3) {
      var1 *= 2;
      GL11.glPushMatrix();
      GL11.glScalef(1.5F, 1.5F, 1.5F);
      Minecraft.getMinecraft();
      Minecraft.fontRendererObj.drawString(var0, (double)var1, (double)var2, var3);
      GL11.glPopMatrix();
   }

   public static void drawRect(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      enableGL2D();
      GL11.glColor4f(var4, var5, var6, var7);
      drawRect(var0, var1, var2, var3);
      disableGL2D();
   }

   public static void drawSpecialRoundedRect(int var0, int var1, int var2, int var3, int var4, int var5) {
      enableGL2D();
      drawRect1((float)var1 + 0.5F, (float)var2, (float)var3 - 0.5F, (float)var2 + 0.5F, var5);
      drawRect1((float)var1 + 0.5F, (float)var4 - 0.5F, (float)var3 - 0.5F, (float)var4, var5);
      drawRect1((float)var1, (float)var2 + 0.5F, (float)var3, (float)var4 - 0.5F, var5);
      disableGL2D();
      float var6 = (float)((new Color(0, 0, 0, 0)).getRGB() >> 24 & 255) / 255.0F;
      float var7 = (float)((new Color(0, 0, 0, 0)).getRGB() >> 16 & 255) / 255.0F;
      float var8 = (float)((new Color(0, 0, 0, 0)).getRGB() >> 8 & 255) / 255.0F;
      float var9 = (float)((new Color(0, 0, 0, 0)).getRGB() & 255) / 255.0F;
      Tessellator var10 = Tessellator.getInstance();
      WorldRenderer var11 = var10.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawGradientHRect(float var0, float var1, float var2, float var3, int var4, int var5) {
      enableGL2D();
      GL11.glShadeModel(7425);
      GL11.glBegin(7);
      glColor(var4);
      GL11.glVertex2f(var0, var1);
      GL11.glVertex2f(var0, var3);
      glColor(var5);
      GL11.glVertex2f(var2, var3);
      GL11.glVertex2f(var2, var1);
      GL11.glEnd();
      GL11.glShadeModel(7424);
      disableGL2D();
   }

   public static void drawSmallString(String var0, int var1, int var2, int var3) {
      GL11.glPushMatrix();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      Minecraft.getMinecraft();
      Minecraft.fontRendererObj.drawString(var0, (double)(var1 * 2), (double)(var2 * 2), var3);
      GL11.glPopMatrix();
   }

   public static void drawBorderedRectReliant(float var0, float var1, float var2, float var3, float var4, int var5, int var6) {
      enableGL2D();
      drawRect(var0, var1, var2, var3, var5);
      glColor(var6);
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glLineWidth(var4);
      GL11.glBegin(3);
      GL11.glVertex2f(var0, var1);
      GL11.glVertex2f(var0, var3);
      GL11.glVertex2f(var2, var3);
      GL11.glVertex2f(var2, var1);
      GL11.glVertex2f(var0, var1);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      disableGL2D();
   }

   public static void drawBorderedRect(Rectangle var0, float var1, int var2, int var3) {
      float var4 = (float)var0.x;
      float var5 = (float)var0.y;
      float var6 = (float)(var0.x + var0.width);
      float var7 = (float)(var0.y + var0.height);
      enableGL2D();
      glColor(var2);
      drawRect(var4 + var1, var5 + var1, var6 - var1, var7 - var1);
      glColor(var3);
      drawRect(var4 + 1.0F, var5, var6 - 1.0F, var5 + var1);
      drawRect(var4, var5, var4 + var1, var7);
      drawRect(var6 - var1, var5, var6, var7);
      drawRect(var4 + 1.0F, var7 - var1, var6 - 1.0F, var7);
      disableGL2D();
   }

   public static void drawGradientRect(float var0, float var1, float var2, float var3, int var4, int var5) {
      enableGL2D();
      GL11.glShadeModel(7425);
      GL11.glBegin(7);
      glColor(var4);
      GL11.glVertex2f(var0, var3);
      GL11.glVertex2f(var2, var3);
      glColor(var5);
      GL11.glVertex2f(var2, var1);
      GL11.glVertex2f(var0, var1);
      GL11.glEnd();
      GL11.glShadeModel(7424);
      disableGL2D();
   }

   public static void drawGradientRect(double var0, double var2, double var4, double var6, int var8, int var9) {
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glShadeModel(7425);
      GL11.glPushMatrix();
      GL11.glBegin(7);
      glColor(var8);
      GL11.glVertex2d(var4, var2);
      GL11.glVertex2d(var0, var2);
      glColor(var9);
      GL11.glVertex2d(var0, var6);
      GL11.glVertex2d(var4, var6);
      GL11.glEnd();
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glShadeModel(7424);
   }

   public static void drawCircle(float var0, float var1, float var2, int var3, int var4) {
      var2 *= 2.0F;
      var0 *= 2.0F;
      var1 *= 2.0F;
      float var5 = (float)(var4 >> 24 & 255) / 255.0F;
      float var6 = (float)(var4 >> 16 & 255) / 255.0F;
      float var7 = (float)(var4 >> 8 & 255) / 255.0F;
      float var8 = (float)(var4 & 255) / 255.0F;
      float var9 = (float)(6.2831852D / (double)var3);
      float var10 = (float)Math.cos((double)var9);
      float var11 = (float)Math.sin((double)var9);
      float var12 = var2;
      float var13 = 0.0F;
      enableGL2D();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      GL11.glColor4f(var6, var7, var8, var5);
      GL11.glBegin(2);

      for(int var14 = 0; var14 < var3; ++var14) {
         GL11.glVertex2f(var12 + var0, var13 + var1);
         float var15 = var12;
         var12 = var10 * var12 - var11 * var13;
         var13 = var11 * var15 + var10 * var13;
      }

      GL11.glEnd();
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      disableGL2D();
   }

   public static void pre3D() {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glShadeModel(7425);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDisable(2896);
      GL11.glDepthMask(false);
      GL11.glHint(3154, 4354);
   }

   public static void drawFullCircle(int var0, int var1, double var2, int var4) {
      var2 *= 2.0D;
      var0 *= 2;
      var1 *= 2;
      float var5 = (float)(var4 >> 24 & 255) / 255.0F;
      float var6 = (float)(var4 >> 16 & 255) / 255.0F;
      float var7 = (float)(var4 >> 8 & 255) / 255.0F;
      float var8 = (float)(var4 & 255) / 255.0F;
      enableGL2D();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      GL11.glColor4f(var6, var7, var8, var5);
      GL11.glBegin(6);

      for(int var9 = 0; var9 <= 360; ++var9) {
         double var10 = Math.sin((double)var9 * 3.141592653589793D / 180.0D) * var2;
         double var12 = Math.cos((double)var9 * 3.141592653589793D / 180.0D) * var2;
         GL11.glVertex2d((double)var0 + var10, (double)var1 + var12);
      }

      GL11.glEnd();
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      disableGL2D();
   }

   public static void drawGradientBorderedRectReliant(float var0, float var1, float var2, float var3, float var4, int var5, int var6, int var7) {
      enableGL2D();
      drawGradientRect(var0, var1, var2, var3, var7, var6);
      glColor(var5);
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glLineWidth(var4);
      GL11.glBegin(3);
      GL11.glVertex2f(var0, var1);
      GL11.glVertex2f(var0, var3);
      GL11.glVertex2f(var2, var3);
      GL11.glVertex2f(var2, var1);
      GL11.glVertex2f(var0, var1);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      disableGL2D();
   }

   public static void disableGL2D() {
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
      GL11.glDisable(2848);
      GL11.glHint(3154, 4352);
      GL11.glHint(3155, 4352);
   }

   public static void glColor(Color var0) {
      GL11.glColor4f((float)var0.getRed() / 255.0F, (float)var0.getGreen() / 255.0F, (float)var0.getBlue() / 255.0F, (float)var0.getAlpha() / 255.0F);
   }

   public static void drawRect1(float var0, float var1, float var2, float var3, int var4) {
      enableGL2D();
      glColor(var4);
      drawRect(var0, var1, var2, var3);
      disableGL2D();
   }

   public static void drawGradientRect1(float var0, float var1, float var2, float var3, int var4, int var5) {
      enableGL2D();
      GL11.glShadeModel(7425);
      GL11.glBegin(7);
      glColor(var4);
      GL11.glVertex2f(var0, var3);
      GL11.glVertex2f(var2, var3);
      glColor(var5);
      GL11.glVertex2f(var2, var1);
      GL11.glVertex2f(var0, var1);
      GL11.glEnd();
      GL11.glShadeModel(7424);
      disableGL2D();
   }

   public static void drawSpecialGradRect(int var0, int var1, int var2, int var3, int var4, int var5) {
      enableGL2D();
      GL11.glShadeModel(7425);
      GL11.glBegin(7);
      glColor(var4);
      GL11.glVertex2f((float)var0, (float)var1);
      GL11.glVertex2f((float)var0, (float)var3);
      glColor(var5);
      GL11.glVertex2f((float)var2, (float)var3);
      GL11.glVertex2f((float)var2, (float)var1);
      GL11.glEnd();
      GL11.glShadeModel(7424);
      disableGL2D();
      float var6 = (float)((new Color(0, 0, 0, 0)).getRGB() >> 24 & 255) / 255.0F;
      float var7 = (float)((new Color(0, 0, 0, 0)).getRGB() >> 16 & 255) / 255.0F;
      float var8 = (float)((new Color(0, 0, 0, 0)).getRGB() >> 8 & 255) / 255.0F;
      float var9 = (float)((new Color(0, 0, 0, 0)).getRGB() & 255) / 255.0F;
      Tessellator var10 = Tessellator.getInstance();
      WorldRenderer var11 = var10.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(var7, var8, var9, var6);
      var11.begin(7, DefaultVertexFormats.POSITION);
      var11.pos((double)var0, (double)var3, 0.0D).endVertex();
      var11.pos((double)var2, (double)var3, 0.0D).endVertex();
      var11.pos((double)var2, (double)var1, 0.0D).endVertex();
      var11.pos((double)var0, (double)var1, 0.0D).endVertex();
      var10.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawRect2(double var0, double var2, double var4, double var6, int var8) {
      float var9 = (float)(var8 >> 24 & 255) / 255.0F;
      float var10 = (float)(var8 >> 16 & 255) / 255.0F;
      float var11 = (float)(var8 >> 8 & 255) / 255.0F;
      float var12 = (float)(var8 & 255) / 255.0F;
      GL11.glColor4f(var10, var11, var12, var9);
      Gui.drawRect(var0, var2, var0 + var4, var2 + var6, var8);
   }

   public static void drawAnimatedUnfilledCircle(float var0, float var1, float var2, float var3, int var4) {
      float var5 = (float)(var4 >> 24 & 255) / 255.0F;
      float var6 = (float)(var4 >> 16 & 255) / 255.0F;
      float var7 = (float)(var4 >> 8 & 255) / 255.0F;
      float var8 = (float)(var4 & 255) / 255.0F;
      GL11.glColor4f(var6, var7, var8, var5);
      GL11.glLineWidth(var3);
      GL11.glEnable(2848);
      GL11.glBegin(2);
      fade = 1080;

      for(int var9 = 0; var9 <= fade; ++var9) {
         GL11.glVertex2d((double)var0 + Math.sin((double)var9 * 3.141592653589793D / 270.0D) * (double)var2, (double)var1 + Math.cos((double)var9 * 3.141592653589793D / 180.0D) * (double)var2);
      }

      GL11.glEnd();
      GL11.glDisable(2848);
   }

   public static void drawBorderedRectNoTop(float var0, float var1, float var2, float var3, float var4, int var5, int var6) {
      enableGL2D();
      var0 *= 2.0F;
      var2 *= 2.0F;
      var1 *= 2.0F;
      var3 *= 2.0F;
      var4 *= 2.0F;
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      drawVLine(var0, var1, var3, var6);
      drawVLine(var2 - 1.0F, var1, var3, var6);
      drawHLine(var0, var0 + var4, var3 - 1.0F, var6);
      drawRect(var0 + 1.0F, var1 + 1.0F, var2 - 1.0F, var3 - 1.0F, var5);
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      disableGL2D();
   }

   public static void drawHLine(float var0, float var1, float var2, int var3) {
      if (var1 < var0) {
         float var4 = var0;
         var0 = var1;
         var1 = var4;
      }

      drawRect(var0, var2, var1 + 1.0F, var2 + 1.0F, var3);
   }

   public static void drawGradientBorderedRect(double var0, double var2, double var4, double var6, float var8, int var9, int var10, int var11) {
      enableGL2D();
      GL11.glPushMatrix();
      glColor(var9);
      GL11.glLineWidth(1.0F);
      GL11.glBegin(1);
      GL11.glVertex2d(var0, var2);
      GL11.glVertex2d(var0, var6);
      GL11.glVertex2d(var4, var6);
      GL11.glVertex2d(var4, var2);
      GL11.glVertex2d(var0, var2);
      GL11.glVertex2d(var4, var2);
      GL11.glVertex2d(var0, var6);
      GL11.glVertex2d(var4, var6);
      GL11.glEnd();
      GL11.glPopMatrix();
      drawGradientRect(var0, var2, var4, var6, var10, var11);
      disableGL2D();
   }

   public static void post3D() {
      GL11.glDepthMask(true);
      GL11.glEnable(2929);
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void drawRoundedRect1(double var0, double var2, double var4, double var6, int var8, int var9) {
      enableGL2D();
      drawRect1((float)var0 + 0.5F, (float)var2, (float)(var0 + var4 - 0.5D), (float)(var2 + var6 + 0.5D), var9);
      drawRect1((float)var0 + 0.5F, (float)var2 - 0.5F, (float)(var0 + var4 - 0.5D), (float)(var2 + var6), var9);
      drawRect1((float)var0, (float)(var2 + var6 + 0.5D), (float)(var0 + var4), (float)(var2 + var6), var9);
      disableGL2D();
   }
}
