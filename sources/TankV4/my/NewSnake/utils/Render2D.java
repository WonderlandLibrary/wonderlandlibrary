package my.NewSnake.utils;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Render2D {
   private static final Minecraft MC = Minecraft.getMinecraft();

   public final void rect(double var1, double var3, double var5, double var7, Color var9) {
      this.rect(var1, var3, var5, var7, true, var9);
   }

   public final void circle(double var1, double var3, double var5) {
      this.polygon(var1, var3, var5, 360);
   }

   public final void outlineInlinedGradientRect(double var1, double var3, double var5, double var7, double var9, Color var11, Color var12) {
      this.gradient(var1, var3, var5, var9, var11, var12);
      this.gradient(var1, var3 + var7 - var9, var5, var9, var12, var11);
      this.gradientSideways(var1, var3, var9, var7, var11, var12);
      this.gradientSideways(var1 + var5 - var9, var3, var9, var7, var12, var11);
   }

   public final void polygon(double var1, double var3, double var5, int var7) {
      this.polygon(var1, var3, var5, (double)var7, true, (Color)null);
   }

   public final void line(double var1, double var3, double var5, double var7, Color var9) {
      this.line(var1, var3, var5, var7, 0.0D, var9);
   }

   public final void startSmooth() {
      this.enable(2881);
      this.enable(2848);
      this.enable(2832);
   }

   public final void polygonCentered(double var1, double var3, double var5, int var7, boolean var8, Color var9) {
      var1 -= var5 / 2.0D;
      var3 -= var5 / 2.0D;
      this.polygon(var1, var3, var5, (double)var7, var8, var9);
   }

   public final void roundedRectRight(double var1, double var3, double var5, double var7, double var9, Color var11, Color var12) {
      double var13 = var9 / 2.0D;
      var5 -= var13;
      var7 -= var13;
      this.circle(var1 + var5 - var9 / 2.0D, var3, var9, var12);
      this.circle(var1 + var5 - var9 / 2.0D, var3 + var7 - var9 / 2.0D, var9, var12);
      this.gradientSideways(var1, var3, var5, var7 + var13, var11, var12);
      this.rect(var1 + var5, var3 + var13, 5.0D, var7 - var13, var12);
   }

   public final void triangle(double var1, double var3, double var5, boolean var7, Color var8) {
      this.polygon(var1, var3, var5, 3.0D, var7, var8);
   }

   public final void scale(double var1, double var3) {
      GL11.glScaled(var1, var3, 1.0D);
   }

   public final void circle(double var1, double var3, double var5, boolean var7, Color var8) {
      this.polygon(var1, var3, var5, 360.0D, var7, var8);
   }

   public final void rect(double var1, double var3, double var5, double var7) {
      this.rect(var1, var3, var5, var7, true, (Color)null);
   }

   public final void color(double var1, double var3, double var5) {
      this.color(var1, var3, var5, 1.0D);
   }

   public final void polygon(double var1, double var3, double var5, double var7, boolean var9, Color var10) {
      var5 /= 2.0D;
      this.start();
      if (var10 != null) {
         this.color(var10);
      }

      if (!var9) {
         GL11.glLineWidth(1.0F);
      }

      GL11.glEnable(2848);
      this.begin(var9 ? 6 : 3);

      for(double var11 = 0.0D; var11 <= var7; ++var11) {
         double var13 = var11 * 6.283185307179586D / var7;
         this.vertex(var1 + var5 * Math.cos(var13) + var5, var3 + var5 * Math.sin(var13) + var5);
      }

      this.end();
      GL11.glDisable(2848);
      this.stop();
   }

   public final void rotate(double var1, double var3, double var5, double var7) {
      GL11.glRotated(var7, var1, var3, var5);
   }

   public final void roundedRectRightTop(double var1, double var3, double var5, double var7, double var9, Color var11, Color var12) {
      double var13 = var9 / 2.0D;
      var5 -= var13;
      var7 -= var13;
      this.circle(var1 + var5 - var9 / 2.0D, var3, var9, var12);
      this.gradientSideways(var1, var3, var5, var7 + var13, var11, var12);
      this.rect(var1 + var5, var3 + var13, 5.0D, var7, var12);
   }

   public final void rectCentered(double var1, double var3, double var5, double var7, boolean var9) {
      var1 -= var5 / 2.0D;
      var3 -= var7 / 2.0D;
      this.rect(var1, var3, var5, var7, var9, (Color)null);
   }

   public final void imageCentered(ResourceLocation var1, double var2, double var4, double var6, double var8) {
      var2 -= var6 / 2.0D;
      var4 -= var8 / 2.0D;
      this.image(var1, var2, var4, var6, var8);
   }

   public final void translate(double var1, double var3) {
      GL11.glTranslated(var1, var3, 0.0D);
   }

   public final void polygonCentered(double var1, double var3, double var5, int var7, Color var8) {
      var1 -= var5 / 2.0D;
      var3 -= var5 / 2.0D;
      this.polygon(var1, var3, var5, (double)var7, true, var8);
   }

   public final void gradientSideways(double var1, double var3, double var5, double var7, Color var9, Color var10) {
      this.gradientSideways(var1, var3, var5, var7, true, var9, var10);
   }

   public final void circleCentered(double var1, double var3, double var5) {
      var1 -= var5 / 2.0D;
      var3 -= var5 / 2.0D;
      this.polygon(var1, var3, var5, 360);
   }

   public final void circle(double var1, double var3, double var5, boolean var7) {
      this.polygon(var1, var3, var5, 360, var7);
   }

   public final void roundedRectBottom(double var1, double var3, double var5, double var7, double var9, Color var11) {
      double var12 = var9 / 2.0D;
      var5 -= var12;
      var7 -= var12;
      this.circle(var1 + var5 - var9 / 2.0D, var3 + var7 - var9 / 2.0D, var9, var11);
      this.circle(var1, var3 + var7 - var9 / 2.0D, var9, var11);
      this.rect(var1, var3, var5 + var12, var7, var11);
      this.rect(var1 + var12, var3 + var7, var5 - var12, var12, var11);
   }

   public final void gradientCentered(double var1, double var3, double var5, double var7, Color var9, Color var10) {
      var1 -= var5 / 2.0D;
      var3 -= var7 / 2.0D;
      this.gradient(var1, var3, var5, var7, true, var9, var10);
   }

   public final void start() {
      this.enable(3042);
      GL11.glBlendFunc(770, 771);
      this.disable(3553);
      this.disable(2884);
      GlStateManager.disableAlpha();
   }

   public final void gradient(double var1, double var3, double var5, double var7, boolean var9, Color var10, Color var11) {
      this.start();
      GL11.glShadeModel(7425);
      GlStateManager.disableAlpha();
      if (var10 != null) {
         this.color(var10);
      }

      this.begin(var9 ? 7 : 1);
      this.vertex(var1, var3);
      this.vertex(var1 + var5, var3);
      if (var11 != null) {
         this.color(var11);
      }

      this.vertex(var1 + var5, var3 + var7);
      this.vertex(var1, var3 + var7);
      if (!var9) {
         this.vertex(var1, var3);
         this.vertex(var1, var3 + var7);
         this.vertex(var1 + var5, var3);
         this.vertex(var1 + var5, var3 + var7);
      }

      this.end();
      GlStateManager.enableAlpha();
      GL11.glShadeModel(7424);
      this.stop();
   }

   public final void roundedRect(double var1, double var3, double var5, double var7, double var9, Color var11) {
      double var12 = var9 / 2.0D;
      var5 -= var12;
      var7 -= var12;
      this.circle(var1, var3, var9, var11);
      this.circle(var1 + var5 - var9 / 2.0D, var3, var9, var11);
      this.circle(var1 + var5 - var9 / 2.0D, var3 + var7 - var9 / 2.0D, var9, var11);
      this.circle(var1, var3 + var7 - var9 / 2.0D, var9, var11);
      this.rect(var1 + var12, var3 + var12, var5 - var12, var7 - var12, var11);
      this.rect(var1, var3 + var12, var9 / 2.0D, var7 - var12, var11);
      this.rect(var1 + var5, var3 + var12, var9 / 2.0D, var7 - var12, var11);
      this.rect(var1 + var12, var3, var5 - var12, var12, var11);
      this.rect(var1 + var12, var3 + var7, var5 - var12, var12, var11);
   }

   public final void triangle(double var1, double var3, double var5, Color var7) {
      this.polygon(var1, var3, var5, 3, var7);
   }

   public final void circleCentered(double var1, double var3, double var5, Color var7) {
      var1 -= var5 / 2.0D;
      var3 -= var5 / 2.0D;
      this.polygon(var1, var3, var5, 360, var7);
   }

   public final void enable(int var1) {
      GL11.glEnable(var1);
   }

   public final void stop() {
      GlStateManager.enableAlpha();
      this.enable(2884);
      this.enable(3553);
      this.disable(3042);
      this.color(Color.white);
   }

   public final void polygon(double var1, double var3, double var5, int var7, Color var8) {
      this.polygon(var1, var3, var5, (double)var7, true, var8);
   }

   public final void line(double var1, double var3, double var5, double var7, double var9) {
      this.line(var1, var3, var5, var7, var9, (Color)null);
   }

   public final void endSmooth() {
      this.disable(2832);
      this.disable(2848);
      this.disable(2881);
   }

   public final void line(double var1, double var3, double var5, double var7, double var9, Color var11) {
      this.start();
      if (var11 != null) {
         this.color(var11);
      }

      this.lineWidth(var9 <= 1.0D ? 1.0D : var9);
      GL11.glEnable(2848);
      this.begin(1);
      this.vertex(var1, var3);
      this.vertex(var5, var7);
      this.end();
      GL11.glDisable(2848);
      this.stop();
   }

   public final void circle(double var1, double var3, double var5, Color var7) {
      this.polygon(var1, var3, var5, 360, var7);
   }

   public final void scissor(double var1, double var3, double var5, double var7) {
      ScaledResolution var9 = new ScaledResolution(MC);
      double var10 = (double)var9.getScaleFactor();
      var3 = (double)ScaledResolution.getScaledHeight() - var3;
      var1 *= var10;
      var3 *= var10;
      var5 *= var10;
      var7 *= var10;
      GL11.glScissor((int)var1, (int)(var3 - var7), (int)var5, (int)var7);
   }

   public final void end() {
      GL11.glEnd();
   }

   public final void rectCentered(double var1, double var3, double var5, double var7, Color var9) {
      var1 -= var5 / 2.0D;
      var3 -= var7 / 2.0D;
      this.rect(var1, var3, var5, var7, true, var9);
   }

   public final void vertex(double var1, double var3) {
      GL11.glVertex2d(var1, var3);
   }

   public final void polygonCentered(double var1, double var3, double var5, int var7, boolean var8) {
      var1 -= var5 / 2.0D;
      var3 -= var5 / 2.0D;
      this.polygon(var1, var3, var5, (double)var7, var8, (Color)null);
   }

   public final void color(double var1, double var3, double var5, double var7) {
      GL11.glColor4d(var1, var3, var5, var7);
   }

   public final void roundedRectRightBottom(double var1, double var3, double var5, double var7, double var9, Color var11, Color var12) {
      double var13 = var9 / 2.0D;
      var5 -= var13;
      var7 -= var13;
      this.circle(var1 + var5 - var9 / 2.0D, var3 + var7 - var9 / 2.0D, var9, var12);
      this.gradientSideways(var1, var3, var5, var7 + var13, var11, var12);
      this.rect(var1 + var5, var3, 5.0D, var7, var12);
   }

   public final void disable(int var1) {
      GL11.glDisable(var1);
   }

   public final void gradientSidewaysCentered(double var1, double var3, double var5, double var7, Color var9, Color var10) {
      var1 -= var5 / 2.0D;
      var3 -= var7 / 2.0D;
      this.gradientSideways(var1, var3, var5, var7, true, var9, var10);
   }

   public final void pop() {
      GL11.glPopMatrix();
   }

   public final void rect(double var1, double var3, double var5, double var7, boolean var9) {
      this.rect(var1, var3, var5, var7, var9, (Color)null);
   }

   public final void push() {
      GL11.glPushMatrix();
   }

   public final void line(double var1, double var3, double var5, double var7) {
      this.line(var1, var3, var5, var7, 0.0D, (Color)null);
   }

   public final void triangleCentered(double var1, double var3, double var5, Color var7) {
      var1 -= var5 / 2.0D;
      var3 -= var5 / 2.0D;
      this.polygon(var1, var3, var5, 3, var7);
   }

   public final void gradient(double var1, double var3, double var5, double var7, Color var9, Color var10) {
      this.gradient(var1, var3, var5, var7, true, var9, var10);
   }

   public final void triangleCentered(double var1, double var3, double var5, boolean var7) {
      var1 -= var5 / 2.0D;
      var3 -= var5 / 2.0D;
      this.polygon(var1, var3, var5, 3, var7);
   }

   public final void begin(int var1) {
      GL11.glBegin(var1);
   }

   public final void rectCentered(double var1, double var3, double var5, double var7, boolean var9, Color var10) {
      var1 -= var5 / 2.0D;
      var3 -= var7 / 2.0D;
      this.rect(var1, var3, var5, var7, var9, var10);
   }

   public final void circleCentered(double var1, double var3, double var5, boolean var7) {
      var1 -= var5 / 2.0D;
      var3 -= var5 / 2.0D;
      this.polygon(var1, var3, var5, 360, var7);
   }

   public final void triangle(double var1, double var3, double var5, boolean var7) {
      this.polygon(var1, var3, var5, 3, var7);
   }

   public final void polygonCentered(double var1, double var3, double var5, int var7) {
      var1 -= var5 / 2.0D;
      var3 -= var5 / 2.0D;
      this.polygon(var1, var3, var5, (double)var7, true, (Color)null);
   }

   public final void triangleCentered(double var1, double var3, double var5) {
      var1 -= var5 / 2.0D;
      var3 -= var5 / 2.0D;
      this.polygon(var1, var3, var5, 3);
   }

   public final void rectCentered(double var1, double var3, double var5, double var7) {
      var1 -= var5 / 2.0D;
      var3 -= var7 / 2.0D;
      this.rect(var1, var3, var5, var7, true, (Color)null);
   }

   public final void gradientSideways(double var1, double var3, double var5, double var7, boolean var9, Color var10, Color var11) {
      this.start();
      GL11.glShadeModel(7425);
      GlStateManager.disableAlpha();
      if (var10 != null) {
         this.color(var10);
      }

      this.begin(var9 ? 6 : 1);
      this.vertex(var1, var3);
      this.vertex(var1, var3 + var7);
      if (var11 != null) {
         this.color(var11);
      }

      this.vertex(var1 + var5, var3 + var7);
      this.vertex(var1 + var5, var3);
      this.end();
      GlStateManager.enableAlpha();
      GL11.glShadeModel(7424);
      this.stop();
   }

   public final void polygon(double var1, double var3, double var5, int var7, boolean var8) {
      this.polygon(var1, var3, var5, (double)var7, var8, (Color)null);
   }

   public final void image(ResourceLocation var1, double var2, double var4, double var6, double var8) {
      GL11.glTexParameteri(3553, 10241, 9728);
      GL11.glTexParameteri(3553, 10240, 9728);
      this.enable(3042);
      GlStateManager.disableAlpha();
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      MC.getTextureManager().bindTexture(var1);
      Gui.drawModalRectWithCustomSizedTexture((double)((int)var2), (double)((int)var4), 0.0F, 0.0F, (double)((int)var6), (double)((int)var8), (double)((float)var6), (double)((float)var8));
      GlStateManager.enableAlpha();
      this.disable(3042);
   }

   public final void triangle(double var1, double var3, double var5) {
      this.polygon(var1, var3, var5, 3);
   }

   public final void lineWidth(double var1) {
      GL11.glLineWidth((float)var1);
   }

   public final void triangleCentered(double var1, double var3, double var5, boolean var7, Color var8) {
      var1 -= var5 / 2.0D;
      var3 -= var5 / 2.0D;
      this.polygon(var1, var3, var5, 3.0D, var7, var8);
   }

   public final void rect(double var1, double var3, double var5, double var7, boolean var9, Color var10) {
      this.start();
      if (var10 != null) {
         this.color(var10);
      }

      this.begin(var9 ? 6 : 1);
      this.vertex(var1, var3);
      this.vertex(var1 + var5, var3);
      this.vertex(var1 + var5, var3 + var7);
      this.vertex(var1, var3 + var7);
      if (!var9) {
         this.vertex(var1, var3);
         this.vertex(var1, var3 + var7);
         this.vertex(var1 + var5, var3);
         this.vertex(var1 + var5, var3 + var7);
      }

      this.end();
      this.stop();
   }

   public final void roundedRectTop(double var1, double var3, double var5, double var7, double var9, Color var11) {
      double var12 = var9 / 2.0D;
      var5 -= var12;
      var7 -= var12;
      this.circle(var1, var3, var9, var11);
      this.circle(var1 + var5 - var9 / 2.0D, var3, var9, var11);
      this.rect(var1, var3 + var12, var5 + var12, var7, var11);
      this.rect(var1 + var12, var3, var5 - var12, var12, var11);
   }

   public final void circleCentered(double var1, double var3, double var5, boolean var7, Color var8) {
      var1 -= var5 / 2.0D;
      var3 -= var5 / 2.0D;
      this.polygon(var1, var3, var5, 360.0D, var7, var8);
   }

   public final void color(Color var1) {
      if (var1 == null) {
         var1 = Color.white;
      }

      this.color((double)((float)var1.getRed() / 255.0F), (double)((float)var1.getGreen() / 255.0F), (double)((float)var1.getBlue() / 255.0F), (double)((float)var1.getAlpha() / 255.0F));
   }
}
