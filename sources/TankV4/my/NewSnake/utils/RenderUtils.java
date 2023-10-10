package my.NewSnake.utils;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public final class RenderUtils {
   private static final Frustum frustrum = new Frustum();
   public static Minecraft mc = Minecraft.getMinecraft();

   public static void drawBorderedRect5(double var0, double var2, double var4, double var6, double var8, int var10, int var11) {
      Gui.drawRect(var0, var2, var0 + var4, var2 + var6, var11);
      Gui.drawRect(var0, var2, var0 + var4, var2 + var8, var10);
      Gui.drawRect(var0, var2, var0 + var8, var2 + var6, var10);
      Gui.drawRect(var0 + var4, var2, var0 + var4 - var8, var2 + var6, var10);
      Gui.drawRect(var0, var2 + var6, var0 + var4, var2 + var6 - var8, var10);
   }

   public static double getDiff(double var0, double var2, float var4, double var5) {
      return var0 + (var2 - var0) * (double)var4 - var5;
   }

   public static void drawRect(double var0, double var2, double var4, double var6, Color var8) {
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

      float var11 = (float)(var8.getRGB() >> 24 & 255) / 255.0F;
      float var12 = (float)(var8.getRGB() >> 16 & 255) / 255.0F;
      float var13 = (float)(var8.getRGB() >> 8 & 255) / 255.0F;
      float var14 = (float)(var8.getRGB() & 255) / 255.0F;
      Tessellator var15 = Tessellator.getInstance();
      WorldRenderer var16 = var15.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(var12, var13, var14, var11);
      var16.begin(7, DefaultVertexFormats.POSITION);
      var16.pos(var0, var6, 0.0D).endVertex();
      var16.pos(var4, var6, 0.0D).endVertex();
      var16.pos(var4, var2, 0.0D).endVertex();
      var16.pos(var0, var2, 0.0D).endVertex();
      var15.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawRect5(double var0, double var2, double var4, double var6, int var8) {
      float var9 = (float)(var8 >> 24 & 255) / 255.0F;
      float var10 = (float)(var8 >> 16 & 255) / 255.0F;
      float var11 = (float)(var8 >> 8 & 255) / 255.0F;
      float var12 = (float)(var8 & 255) / 255.0F;
      GL11.glColor4f(var10, var11, var12, var9);
      Gui.drawRect(var0, var2, var0 + var4, var2 + var6, var8);
   }

   public static void disableGL2D() {
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
      GL11.glDisable(2848);
      GL11.glHint(3154, 4352);
      GL11.glHint(3155, 4352);
   }

   public static void drawEsp(EntityLivingBase var0, float var1, int var2, int var3) {
      if (var0.isEntityAlive()) {
         double var4 = getDiff(var0.lastTickPosX, var0.posX, var1, RenderManager.renderPosX);
         double var6 = getDiff(var0.lastTickPosY, var0.posY, var1, RenderManager.renderPosY);
         double var8 = getDiff(var0.lastTickPosZ, var0.posZ, var1, RenderManager.renderPosZ);
         boundingBox(var0, var4, var6, var8, var2, var3);
      }
   }

   public static void enableGL3D(float var0) {
      GL11.glDisable(3008);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glEnable(2884);
      Minecraft.getMinecraft().entityRenderer.disableLightmap();
      GL11.glEnable(2848);
      GL11.glHint(3154, 4354);
      GL11.glHint(3155, 4354);
      GL11.glLineWidth(var0);
   }

   public static void prepareScissorBox(float var0, float var1, float var2, float var3) {
      ScaledResolution var4 = new ScaledResolution(mc);
      int var5 = var4.getScaleFactor();
      GL11.glScissor((int)(var0 * (float)var5), (int)(((float)ScaledResolution.getScaledHeight() - var3) * (float)var5), (int)((var2 - var0) * (float)var5), (int)((var3 - var1) * (float)var5));
   }

   public static void drawRect4(float var0, float var1, float var2, float var3, int var4) {
      enableGL2D();
      glColor(var4);
      drawRect5(var0, var1, var2, var3);
      disableGL2D();
   }

   public static void rectangleOutlinedGradient(double var0, double var2, double var4, double var6, int[] var8, double var9) {
      rectangleGradient(var0, var2, var4, var2 + var9, new int[]{var8[0], var8[1], var8[0], var8[1]});
      rectangleGradient(var0, var6 - var9, var4, var6, new int[]{var8[2], var8[3], var8[2], var8[3]});
      rectangleGradient(var0, var2 + var9, var0 + var9, var6 - var9, var8);
      rectangleGradient(var4 - var9, var2 + var9, var4, var6 - var9, var8);
   }

   public static void rectangle(double var0, double var2, double var4, double var6, int var8) {
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

      float var15 = (float)(var8 >> 24 & 255) / 255.0F;
      float var10 = (float)(var8 >> 16 & 255) / 255.0F;
      float var11 = (float)(var8 >> 8 & 255) / 255.0F;
      float var12 = (float)(var8 & 255) / 255.0F;
      WorldRenderer var13 = Tessellator.getInstance().getWorldRenderer();
      var13.begin(7, DefaultVertexFormats.POSITION_TEX);
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(var10, var11, var12, var15);
      var13.pos(var0, var6, 0.0D).endVertex();
      var13.pos(var4, var6, 0.0D).endVertex();
      var13.pos(var4, var2, 0.0D).endVertex();
      var13.pos(var0, var2, 0.0D).endVertex();
      Tessellator.getInstance().draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void rectangleBordered(double var0, double var2, double var4, double var6, double var8, int var10, int var11, RenderUtils.Side var12) {
      rectangle(var0 + var8, var2 + var8, var4 - var8, var6 - var8, var10);
      if (!var12.equals(RenderUtils.Side.Top)) {
         rectangle(var0 + var8, var2, var4 - var8, var2 + var8, var11);
      }

      if (!var12.equals(RenderUtils.Side.Left)) {
         rectangle(var0, var2, var0 + var8, var6, var11);
      }

      if (!var12.equals(RenderUtils.Side.Right)) {
         rectangle(var4 - var8, var2, var4, var6, var11);
      }

      if (!var12.equals(RenderUtils.Side.Bottom)) {
         rectangle(var0 + var8, var6 - var8, var4 - var8, var6, var11);
      }

   }

   public static void boundingBox(Entity var0, double var1, double var3, double var5, int var7, int var8) {
      GlStateManager.pushMatrix();
      GL11.glLineWidth(1.0F);
      AxisAlignedBB var9 = var0.getEntityBoundingBox();
      AxisAlignedBB var10 = new AxisAlignedBB(var9.minX - var0.posX + var1, var9.minY - var0.posY + var3, var9.minZ - var0.posZ + var5, var9.maxX - var0.posX + var1, var9.maxY - var0.posY + var3, var9.maxZ - var0.posZ + var5);
      if (var7 != 0) {
         GlStateManager.disableDepth();
         filledBox(var10, var8, true);
         GlStateManager.disableLighting();
         RenderGlobal.renderSkyEnd();
      }

      GlStateManager.popMatrix();
   }

   private static boolean isInViewFrustrum(AxisAlignedBB var0) {
      Entity var1 = Minecraft.getMinecraft().getRenderViewEntity();
      frustrum.setPosition(var1.posX, var1.posY, var1.posZ);
      return frustrum.isBoundingBoxInFrustum(var0);
   }

   public static int blend(int var0, int var1, float var2) {
      Color var3 = new Color(var0);
      Color var4 = new Color(var1);
      float var5 = 1.0F - var2;
      float var6 = (float)var3.getRed() * var2 + (float)var4.getRed() * var5;
      float var7 = (float)var3.getGreen() * var2 + (float)var4.getGreen() * var5;
      float var8 = (float)var3.getBlue() * var2 + (float)var4.getBlue() * var5;

      Color var9;
      try {
         var9 = new Color(var6 / 255.0F, var7 / 255.0F, var8 / 255.0F);
      } catch (Exception var12) {
         var9 = new Color(-1);
      }

      return var9.getRGB();
   }

   public static void drawRect5(float var0, float var1, float var2, float var3) {
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

   public static double interpolate(double var0, double var2, double var4) {
      return var2 + (var0 - var2) * var4;
   }

   public static void rectangleBordered(double var0, double var2, double var4, double var6, double var8, int var10, int var11) {
      rectangle(var0 + var8, var2 + var8, var4 - var8, var6 - var8, var10);
      rectangle(var0 + var8, var2, var4 - var8, var2 + var8, var11);
      rectangle(var0, var2, var0 + var8, var6, var11);
      rectangle(var4 - var8, var2, var4, var6, var11);
      rectangle(var0 + var8, var6 - var8, var4 - var8, var6, var11);
   }

   public static double lerp(double var0, double var2, double var4) {
      return (1.0D - var4) * var0 + var4 * var2;
   }

   public static void rectangleGradient(double var0, double var2, double var4, double var6, int[] var8) {
      float[] var9 = new float[var8.length];
      float[] var10 = new float[var8.length];
      float[] var11 = new float[var8.length];
      float[] var12 = new float[var8.length];

      for(int var13 = 0; var13 < var8.length; ++var13) {
         var9[var13] = (float)(var8[var13] >> 16 & 255) / 255.0F;
         var10[var13] = (float)(var8[var13] >> 8 & 255) / 255.0F;
         var11[var13] = (float)(var8[var13] & 255) / 255.0F;
         var12[var13] = (float)(var8[var13] >> 24 & 255) / 255.0F;
      }

      GlStateManager.disableTexture2D();
      GlStateManager.disableBlend();
      GlStateManager.disableAlpha();
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      GlStateManager.blendFunc(770, 771);
      GlStateManager.shadeModel(7425);
      WorldRenderer var14 = Tessellator.getInstance().getWorldRenderer();
      var14.begin(7, DefaultVertexFormats.POSITION_COLOR);
      var14.color(var9[0], var10[0], var11[0], var12[0]).endVertex();
      var14.pos(var4, var2, 0.0D).endVertex();
      var14.color(var9[1], var10[1], var11[1], var12[1]).endVertex();
      var14.pos(var0, var2, 0.0D).endVertex();
      var14.color(var9[2], var10[2], var11[2], var12[2]).endVertex();
      var14.pos(var0, var6, 0.0D).endVertex();
      var14.color(var9[3], var10[3], var11[3], var12[3]).endVertex();
      var14.pos(var4, var6, 0.0D).endVertex();
      Tessellator.getInstance().draw();
      GlStateManager.shadeModel(7424);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.enableTexture2D();
   }

   public static void rectangleBorderedGradient(double var0, double var2, double var4, double var6, int[] var8, int[] var9, double var10) {
      rectangleOutlinedGradient(var0, var2, var4, var6, var9, var10);
      rectangleGradient(var0 + var10, var2 + var10, var4 - var10, var6 - var10, var8);
   }

   public static boolean isInViewFrustrum(Entity var0) {
      return isInViewFrustrum(var0.getEntityBoundingBox()) || var0.ignoreFrustumCheck;
   }

   public static void filledBox(AxisAlignedBB var0, int var1, boolean var2) {
      GlStateManager.pushMatrix();
      float var3 = (float)(var1 >> 24 & 255) / 255.0F;
      float var4 = (float)(var1 >> 16 & 255) / 255.0F;
      float var5 = (float)(var1 >> 8 & 255) / 255.0F;
      float var6 = (float)(var1 & 255) / 255.0F;
      WorldRenderer var7 = Tessellator.getInstance().getWorldRenderer();
      if (var2) {
         GlStateManager.color(var4, var5, var6, var3);
      }

      boolean var8 = true;
      var7.begin(7, DefaultVertexFormats.POSITION_TEX);
      var7.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var7.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var7.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
      var7.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
      var7.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
      var7.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
      var7.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
      var7.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
      Tessellator.getInstance().draw();
      var7.begin(7, DefaultVertexFormats.POSITION_TEX);
      var7.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
      var7.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
      var7.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var7.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var7.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
      var7.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
      var7.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
      var7.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
      Tessellator.getInstance().draw();
      var7.begin(7, DefaultVertexFormats.POSITION_TEX);
      var7.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var7.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
      var7.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
      var7.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
      var7.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var7.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
      var7.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
      var7.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
      Tessellator.getInstance().draw();
      var7.begin(7, DefaultVertexFormats.POSITION_TEX);
      var7.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var7.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
      var7.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
      var7.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
      var7.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var7.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
      var7.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
      var7.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
      Tessellator.getInstance().draw();
      var7.begin(7, DefaultVertexFormats.POSITION_TEX);
      var7.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var7.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var7.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
      var7.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
      var7.begin(7, DefaultVertexFormats.POSITION_TEX);
      var7.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
      var7.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
      var7.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
      Tessellator.getInstance().draw();
      var7.begin(7, DefaultVertexFormats.POSITION_TEX);
      var7.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
      var7.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
      var7.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var7.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var7.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
      var7.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
      var7.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
      var7.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
      Tessellator.getInstance().draw();
      GlStateManager.depthMask(true);
      GlStateManager.popMatrix();
   }

   public static void disableGL3D() {
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDisable(3042);
      GL11.glEnable(3008);
      GL11.glDepthMask(true);
      GL11.glCullFace(1029);
      GL11.glDisable(2848);
      GL11.glHint(3154, 4352);
      GL11.glHint(3155, 4352);
   }

   public static void glColor(int var0) {
      float var1 = (float)(var0 >> 24 & 255) / 255.0F;
      float var2 = (float)(var0 >> 16 & 255) / 255.0F;
      float var3 = (float)(var0 >> 8 & 255) / 255.0F;
      float var4 = (float)(var0 & 255) / 255.0F;
      GL11.glColor4f(var2, var3, var4, var1);
   }

   public static enum Side {
      Right("Right", 1),
      Top("Top", 0),
      Bottom("Bottom", 2),
      None("None", 4),
      Left("Left", 3);

      private static final RenderUtils.Side[] ENUM$VALUES = new RenderUtils.Side[]{Top, Right, Bottom, Left, None};

      private Side(String var3, int var4) {
      }
   }
}
