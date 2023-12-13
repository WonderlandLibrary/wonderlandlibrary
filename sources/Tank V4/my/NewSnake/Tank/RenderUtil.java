package my.NewSnake.Tank;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.vecmath.Vector3d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class RenderUtil {
   private static final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
   private static final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
   private static final Frustum frustrum = new Frustum();
   private static final Minecraft mc = Minecraft.getMinecraft();
   private static final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);

   public static void drawCircle(float var0, float var1, float var2, int var3) {
      float var4 = (float)(var3 >> 24 & 255) / 255.0F;
      float var5 = (float)(var3 >> 16 & 255) / 255.0F;
      float var6 = (float)(var3 >> 8 & 255) / 255.0F;
      float var7 = (float)(var3 & 255) / 255.0F;
      GL11.glPushMatrix();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GL11.glEnable(2848);
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GL11.glColor4f(var5, var6, var7, var4);
      GL11.glBegin(6);

      int var8;
      double var9;
      double var11;
      for(var8 = 0; var8 <= 360; ++var8) {
         var9 = Math.sin((double)var8 * 3.141592653589793D / 180.0D) * (double)(var2 / 2.0F);
         var11 = Math.cos((double)var8 * 3.141592653589793D / 180.0D) * (double)(var2 / 2.0F);
         GL11.glVertex2d((double)(var0 + var2 / 2.0F) + var9, (double)(var1 + var2 / 2.0F) + var11);
      }

      GL11.glEnd();
      GL11.glBegin(2);

      for(var8 = 0; var8 <= 360; ++var8) {
         var9 = Math.sin((double)var8 * 3.141592653589793D / 180.0D) * (double)(var2 / 2.0F);
         var11 = Math.cos((double)var8 * 3.141592653589793D / 180.0D) * (double)(var2 / 2.0F);
         GL11.glVertex2d((double)(var0 + var2 / 2.0F) + var9, (double)(var1 + var2 / 2.0F) + var11);
      }

      GL11.glEnd();
      GL11.glDisable(2848);
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
      GL11.glPopMatrix();
   }

   public static void hexColor(int var0) {
      float var1 = (float)(var0 >> 16 & 255) / 255.0F;
      float var2 = (float)(var0 >> 8 & 255) / 255.0F;
      float var3 = (float)(var0 & 255) / 255.0F;
      float var4 = (float)(var0 >> 24 & 255) / 255.0F;
      GL11.glColor4f(var1, var2, var3, var4);
   }

   public static void drawBorderedRect(double var0, double var2, double var4, double var6, double var8, int var10, int var11) {
      Gui.drawRect(var0, var2, var0 + var4, var2 + var6, var11);
      Gui.drawRect(var0, var2, var0 + var4, var2 + var8, var10);
      Gui.drawRect(var0, var2, var0 + var8, var2 + var6, var10);
      Gui.drawRect(var0 + var4, var2, var0 + var4 - var8, var2 + var6, var10);
      Gui.drawRect(var0, var2 + var6, var0 + var4, var2 + var6 - var8, var10);
   }

   public static void drawCheckMark(float var0, float var1, int var2, int var3) {
      float var4 = (float)(var3 >> 24 & 255) / 255.0F;
      float var5 = (float)(var3 >> 16 & 255) / 255.0F;
      float var6 = (float)(var3 >> 8 & 255) / 255.0F;
      float var7 = (float)(var3 & 255) / 255.0F;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glBlendFunc(770, 771);
      GL11.glLineWidth(1.5F);
      GL11.glBegin(3);
      GL11.glColor4f(var5, var6, var7, var4);
      GL11.glVertex2d((double)(var0 + (float)var2) - 6.5D, (double)(var1 + 3.0F));
      GL11.glVertex2d((double)(var0 + (float)var2) - 11.5D, (double)(var1 + 10.0F));
      GL11.glVertex2d((double)(var0 + (float)var2) - 13.5D, (double)(var1 + 8.0F));
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void drawOutlinedRoundedRect(double var0, double var2, double var4, double var6, double var8, float var10, int var11) {
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      double var12 = var0 + var4;
      double var14 = var2 + var6;
      float var16 = (float)(var11 >> 24 & 255) / 255.0F;
      float var17 = (float)(var11 >> 16 & 255) / 255.0F;
      float var18 = (float)(var11 >> 8 & 255) / 255.0F;
      float var19 = (float)(var11 & 255) / 255.0F;
      GL11.glPushAttrib(0);
      GL11.glScaled(0.5D, 0.5D, 0.5D);
      var0 *= 2.0D;
      var2 *= 2.0D;
      var12 *= 2.0D;
      var14 *= 2.0D;
      GL11.glLineWidth(var10);
      GL11.glDisable(3553);
      GL11.glColor4f(var17, var18, var19, var16);
      GL11.glEnable(2848);
      GL11.glBegin(2);

      int var20;
      for(var20 = 0; var20 <= 90; var20 += 3) {
         GL11.glVertex2d(var0 + var8 + Math.sin((double)var20 * 3.141592653589793D / 180.0D) * var8 * -1.0D, var2 + var8 + Math.cos((double)var20 * 3.141592653589793D / 180.0D) * var8 * -1.0D);
      }

      for(var20 = 90; var20 <= 180; var20 += 3) {
         GL11.glVertex2d(var0 + var8 + Math.sin((double)var20 * 3.141592653589793D / 180.0D) * var8 * -1.0D, var14 - var8 + Math.cos((double)var20 * 3.141592653589793D / 180.0D) * var8 * -1.0D);
      }

      for(var20 = 0; var20 <= 90; var20 += 3) {
         GL11.glVertex2d(var12 - var8 + Math.sin((double)var20 * 3.141592653589793D / 180.0D) * var8, var14 - var8 + Math.cos((double)var20 * 3.141592653589793D / 180.0D) * var8);
      }

      for(var20 = 90; var20 <= 180; var20 += 3) {
         GL11.glVertex2d(var12 - var8 + Math.sin((double)var20 * 3.141592653589793D / 180.0D) * var8, var2 + var8 + Math.cos((double)var20 * 3.141592653589793D / 180.0D) * var8);
      }

      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glScaled(2.0D, 2.0D, 2.0D);
      GL11.glPopAttrib();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawCornerRect(double var0, double var2, double var4, double var6, double var8, int var10, boolean var11, double var12) {
      double var14 = var4 / 4.0D;
      double var16 = var6 / 4.0D;
      drawRect(var0, var2, var14 + (var11 ? var12 : 0.0D), var8, var10);
      drawRect(var0 + var4 - (var14 + (var11 ? var12 : 0.0D)), var2, var14, var8, var10);
      drawRect(var0, var2 + var6 - var8, var14 + (var11 ? var12 : 0.0D), var8, var10);
      drawRect(var0 + var4 - (var14 + (var11 ? var12 : 0.0D)), var2 + var6 - var8, var14, var8, var10);
      drawRect(var0, var2, var8, var16 + (var11 ? var12 : 0.0D), var10);
      drawRect(var0 + var4 - var8, var2, var8, var16 + (var11 ? var12 : 0.0D), var10);
      drawRect(var0, var2 + var6 - (var16 + (var11 ? var12 : 0.0D)), var8, var16, var10);
      drawRect(var0 + var4 - var8, var2 + var6 - (var16 + (var11 ? var12 : 0.0D)), var8, var16, var10);
   }

   public static boolean isInViewFrustrum(Entity var0) {
      return isInViewFrustrum(var0.getEntityBoundingBox()) || var0.ignoreFrustumCheck;
   }

   public static Vector3d project(double var0, double var2, double var4) {
      FloatBuffer var6 = GLAllocation.createDirectFloatBuffer(4);
      GL11.glGetFloat(2982, modelview);
      GL11.glGetFloat(2983, projection);
      GL11.glGetInteger(2978, viewport);
      return GLU.gluProject((float)var0, (float)var2, (float)var4, modelview, projection, viewport, var6) ? new Vector3d((double)(var6.get(0) / (float)getResolution().getScaleFactor()), (double)(((float)Display.getHeight() - var6.get(1)) / (float)getResolution().getScaleFactor()), (double)var6.get(2)) : null;
   }

   public static void drawRoundedRect(double var0, double var2, double var4, double var6, double var8, int var10) {
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      double var11 = var0 + var4;
      double var13 = var2 + var6;
      float var15 = (float)(var10 >> 24 & 255) / 255.0F;
      float var16 = (float)(var10 >> 16 & 255) / 255.0F;
      float var17 = (float)(var10 >> 8 & 255) / 255.0F;
      float var18 = (float)(var10 & 255) / 255.0F;
      GL11.glPushAttrib(0);
      GL11.glScaled(0.5D, 0.5D, 0.5D);
      var0 *= 2.0D;
      var2 *= 2.0D;
      var11 *= 2.0D;
      var13 *= 2.0D;
      GL11.glDisable(3553);
      GL11.glColor4f(var16, var17, var18, var15);
      GL11.glEnable(2848);
      GL11.glBegin(9);

      int var19;
      for(var19 = 0; var19 <= 90; var19 += 3) {
         GL11.glVertex2d(var0 + var8 + Math.sin((double)var19 * 3.141592653589793D / 180.0D) * var8 * -1.0D, var2 + var8 + Math.cos((double)var19 * 3.141592653589793D / 180.0D) * var8 * -1.0D);
      }

      for(var19 = 90; var19 <= 180; var19 += 3) {
         GL11.glVertex2d(var0 + var8 + Math.sin((double)var19 * 3.141592653589793D / 180.0D) * var8 * -1.0D, var13 - var8 + Math.cos((double)var19 * 3.141592653589793D / 180.0D) * var8 * -1.0D);
      }

      for(var19 = 0; var19 <= 90; var19 += 3) {
         GL11.glVertex2d(var11 - var8 + Math.sin((double)var19 * 3.141592653589793D / 180.0D) * var8, var13 - var8 + Math.cos((double)var19 * 3.141592653589793D / 180.0D) * var8);
      }

      for(var19 = 90; var19 <= 180; var19 += 3) {
         GL11.glVertex2d(var11 - var8 + Math.sin((double)var19 * 3.141592653589793D / 180.0D) * var8, var2 + var8 + Math.cos((double)var19 * 3.141592653589793D / 180.0D) * var8);
      }

      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glScaled(2.0D, 2.0D, 2.0D);
      GL11.glPopAttrib();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawBar(float var0, float var1, float var2, float var3, float var4, float var5, int var6) {
      float var7 = (float)(var6 >> 24 & 255) / 255.0F;
      float var8 = (float)(var6 >> 16 & 255) / 255.0F;
      float var9 = (float)(var6 >> 8 & 255) / 255.0F;
      float var10 = (float)(var6 & 255) / 255.0F;
      float var11 = var3 / var4;
      GL11.glColor4f(var8, var9, var10, var7);
      drawBorderedRect((double)var0, (double)var1, (double)var2, (double)var3, 0.5D, -16777216, 0);
      float var12 = var1 + var3 - var11;

      for(int var13 = 0; (float)var13 < var5; ++var13) {
         drawBorderedRect((double)(var0 + 0.25F), (double)var12, (double)(var2 - 0.5F), (double)var11, 0.25D, -16777216, var6);
         var12 -= var11;
      }

   }

   public static float[] getRGBAs(int var0) {
      return new float[]{(float)(var0 >> 16 & 255) / 255.0F, (float)(var0 >> 8 & 255) / 255.0F, (float)(var0 & 255) / 255.0F, (float)(var0 >> 24 & 255) / 255.0F};
   }

   public static void drawRoundedRectWithShadow(double var0, double var2, double var4, double var6, double var8, int var10) {
      drawRoundedRect(var0 + 2.0D, var2 + 1.0D, var4, var6 + 1.0D, var8, (new Color(0)).getRGB());
      drawRoundedRect(var0, var2, var4, var6, var8, var10);
   }

   public static void disable3D() {
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
   }

   public static void drawOutlinedBoundingBox(AxisAlignedBB var0) {
      Tessellator var1 = Tessellator.getInstance();
      WorldRenderer var2 = var1.getWorldRenderer();
      var2.begin(3, DefaultVertexFormats.POSITION);
      var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var1.draw();
      var2.begin(3, DefaultVertexFormats.POSITION);
      var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var1.draw();
      var2.begin(1, DefaultVertexFormats.POSITION);
      var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
      var1.draw();
   }

   public static void enable3D() {
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
   }

   public static void drawBorderedRoundedRect(float var0, float var1, float var2, float var3, float var4, float var5, int var6, int var7) {
      drawRoundedRect((double)var0, (double)var1, (double)var2, (double)var3, (double)var4, var7);
      drawOutlinedRoundedRect((double)var0, (double)var1, (double)var2, (double)var3, (double)var4, var5, var6);
   }

   public static int getRainbow(int var0, int var1, float var2) {
      float var3 = (float)((System.currentTimeMillis() + (long)var1) % (long)var0);
      var3 /= (float)var0;
      return Color.getHSBColor(var3, var2, 1.0F).getRGB();
   }

   public static void prepareScissorBox(ScaledResolution var0, float var1, float var2, float var3, float var4) {
      float var5 = var1 + var3;
      float var6 = var2 + var4;
      int var7 = var0.getScaleFactor();
      GL11.glScissor((int)(var1 * (float)var7), (int)(((float)ScaledResolution.getScaledHeight() - var6) * (float)var7), (int)((var5 - var1) * (float)var7), (int)((var6 - var2) * (float)var7));
   }

   public static void BB(AxisAlignedBB var0, int var1) {
      enable3D();
      color(var1);
      drawBoundingBox(var0);
      disable3D();
   }

   public static boolean isInViewFrustrum(AxisAlignedBB var0) {
      Entity var1 = Minecraft.getMinecraft().getRenderViewEntity();
      frustrum.setPosition(var1.posX, var1.posY, var1.posZ);
      return frustrum.isBoundingBoxInFrustum(var0);
   }

   public static void drawRect(double var0, double var2, double var4, double var6, int var8) {
      float var9 = (float)(var8 >> 24 & 255) / 255.0F;
      float var10 = (float)(var8 >> 16 & 255) / 255.0F;
      float var11 = (float)(var8 >> 8 & 255) / 255.0F;
      float var12 = (float)(var8 & 255) / 255.0F;
      GL11.glColor4f(var10, var11, var12, var9);
      Gui.drawRect(var0, var2, var0 + var4, var2 + var6, var8);
   }

   public static void color(int var0) {
      GL11.glColor4f((float)(var0 >> 16 & 255) / 255.0F, (float)(var0 >> 8 & 255) / 255.0F, (float)(var0 & 255) / 255.0F, (float)(var0 >> 24 & 255) / 255.0F);
   }

   public static void drawTexturedModalRect(int var0, int var1, int var2, int var3, int var4, int var5, float var6) {
      float var7 = 0.00390625F;
      float var8 = 0.00390625F;
      Tessellator var9 = Tessellator.getInstance();
      WorldRenderer var10 = var9.getWorldRenderer();
      var10.begin(7, DefaultVertexFormats.POSITION_TEX);
      var10.pos((double)var0, (double)(var1 + var5), (double)var6).tex((double)((float)var2 * 0.00390625F), (double)((float)(var3 + var5) * 0.00390625F)).endVertex();
      var10.pos((double)(var0 + var4), (double)(var1 + var5), (double)var6).tex((double)((float)(var2 + var4) * 0.00390625F), (double)((float)(var3 + var5) * 0.00390625F)).endVertex();
      var10.pos((double)(var0 + var4), (double)var1, (double)var6).tex((double)((float)(var2 + var4) * 0.00390625F), (double)((float)var3 * 0.00390625F)).endVertex();
      var10.pos((double)var0, (double)var1, (double)var6).tex((double)((float)var2 * 0.00390625F), (double)((float)var3 * 0.00390625F)).endVertex();
      var9.draw();
   }

   public static void drawUnfilledCircle(float var0, float var1, float var2, int var3) {
      float var4 = (float)(var3 >> 24 & 255) / 255.0F;
      float var5 = (float)(var3 >> 16 & 255) / 255.0F;
      float var6 = (float)(var3 >> 8 & 255) / 255.0F;
      float var7 = (float)(var3 & 255) / 255.0F;
      GL11.glPushMatrix();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GL11.glColor4f(var5, var6, var7, var4);
      GL11.glLineWidth(1.0F);
      GL11.glBegin(2);

      for(int var8 = 0; var8 <= 360; ++var8) {
         double var9 = Math.sin((double)var8 * 3.141592653589793D / 180.0D) * (double)(var2 / 2.0F);
         double var11 = Math.cos((double)var8 * 3.141592653589793D / 180.0D) * (double)(var2 / 2.0F);
         GL11.glVertex2d((double)(var0 + var2 / 2.0F) + var9, (double)(var1 + var2 / 2.0F) + var11);
      }

      GL11.glEnd();
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
      GL11.glPopMatrix();
   }

   public static void drawBoundingBox(AxisAlignedBB var0) {
      Tessellator var1 = Tessellator.getInstance();
      WorldRenderer var2 = var1.getWorldRenderer();
      var2.begin(7, DefaultVertexFormats.POSITION_TEX);
      var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
      var1.draw();
      var2.begin(7, DefaultVertexFormats.POSITION_TEX);
      var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
      var1.draw();
      var2.begin(7, DefaultVertexFormats.POSITION_TEX);
      var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
      var1.draw();
      var2.begin(7, DefaultVertexFormats.POSITION_TEX);
      var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
      var1.draw();
      var2.begin(7, DefaultVertexFormats.POSITION_TEX);
      var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
      var1.draw();
      var2.begin(7, DefaultVertexFormats.POSITION_TEX);
      var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
      var1.draw();
   }

   public static double interpolate(double var0, double var2, double var4) {
      return var2 + (var0 - var2) * var4;
   }

   public static void drawBordered(double var0, double var2, double var4, double var6, double var8, int var10, int var11) {
      double var12 = 0.0D;
      if (var8 < 1.0D) {
         var12 = 1.0D;
      }

      drawRect2(var0 + var8, var2 + var8, var4 - var8, var6 - var8, var10);
      drawRect2(var0, var2 + 1.0D - var12, var0 + var8, var6, var11);
      drawRect2(var0, var2, var4 - 1.0D + var12, var2 + var8, var11);
      drawRect2(var4 - var8, var2, var4, var6 - 1.0D + var12, var11);
      drawRect2(var0 + 1.0D - var12, var6 - var8, var4, var6, var11);
   }

   public static void drawArrow(float var0, float var1, boolean var2, int var3) {
      GL11.glPushMatrix();
      GL11.glScaled(1.3D, 1.3D, 1.3D);
      var0 = (float)((double)var0 / 1.3D);
      var1 = (float)((double)var1 / 1.3D);
      GL11.glEnable(2848);
      GL11.glDisable(3553);
      hexColor(var3);
      GL11.glLineWidth(2.0F);
      GL11.glBegin(1);
      GL11.glVertex2d((double)var0, (double)(var1 + (float)(var2 ? 4 : 0)));
      GL11.glVertex2d((double)(var0 + 3.0F), (double)(var1 + (float)(var2 ? 0 : 4)));
      GL11.glEnd();
      GL11.glBegin(1);
      GL11.glVertex2d((double)(var0 + 3.0F), (double)(var1 + (float)(var2 ? 0 : 4)));
      GL11.glVertex2d((double)(var0 + 6.0F), (double)(var1 + (float)(var2 ? 4 : 0)));
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   public static void OutlinedBB(AxisAlignedBB var0, float var1, int var2) {
      enable3D();
      GL11.glLineWidth(var1);
      color(var2);
      drawOutlinedBoundingBox(var0);
      disable3D();
   }

   public static void drawTracerPointer(float var0, float var1, float var2, float var3, float var4, int var5) {
      boolean var6 = GL11.glIsEnabled(3042);
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glPushMatrix();
      hexColor(var5);
      GL11.glBegin(7);
      GL11.glVertex2d((double)var0, (double)var1);
      GL11.glVertex2d((double)(var0 - var2 / var3), (double)(var1 + var2));
      GL11.glVertex2d((double)var0, (double)(var1 + var2 / var4));
      GL11.glVertex2d((double)(var0 + var2 / var3), (double)(var1 + var2));
      GL11.glVertex2d((double)var0, (double)var1);
      GL11.glEnd();
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.8F);
      GL11.glBegin(2);
      GL11.glVertex2d((double)var0, (double)var1);
      GL11.glVertex2d((double)(var0 - var2 / var3), (double)(var1 + var2));
      GL11.glVertex2d((double)var0, (double)(var1 + var2 / var4));
      GL11.glVertex2d((double)(var0 + var2 / var3), (double)(var1 + var2));
      GL11.glVertex2d((double)var0, (double)var1);
      GL11.glEnd();
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      if (!var6) {
         GL11.glDisable(3042);
      }

      GL11.glDisable(2848);
   }

   public static ScaledResolution getResolution() {
      return new ScaledResolution(mc);
   }

   public static void drawImage(ResourceLocation var0, float var1, float var2, int var3, int var4) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      Minecraft.getMinecraft().getTextureManager().bindTexture(var0);
      Gui.drawModalRectWithCustomSizedTexture((double)var1, (double)var2, 0.0F, 0.0F, (double)var3, (double)var4, (double)var3, (double)var4);
   }

   public static void drawRect2(double var0, double var2, double var4, double var6, int var8) {
      float var9 = (float)(var8 >> 24 & 255) / 255.0F;
      float var10 = (float)(var8 >> 16 & 255) / 255.0F;
      float var11 = (float)(var8 >> 8 & 255) / 255.0F;
      float var12 = (float)(var8 & 255) / 255.0F;
      GL11.glColor4f(var10, var11, var12, var9);
      Gui.drawRect(var0, var2, var4, var6, var8);
   }
}
