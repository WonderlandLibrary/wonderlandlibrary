package team.massacre.utils;

import java.awt.Color;
import java.awt.Rectangle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class GuiUtils {
   public static final RenderItem RENDER_ITEM;
   private static final Frustum frustrum = new Frustum();
   private static ScaledResolution scaledResolution;
   static int fade;
   static boolean fadeIn;
   static boolean fadeOut;

   public static Color pulseBrightness(Color color, int index, int count) {
      float[] hsb = new float[3];
      Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
      float brightness = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0F + (float)index / (float)count * 2.0F) % 2.0F - 1.0F);
      brightness = 0.5F + 0.5F * brightness;
      return new Color(Color.HSBtoRGB(hsb[0], hsb[1], brightness % 2.0F));
   }

   public static int getRainbow(int speed, int offset, float s) {
      float hue = (float)((System.currentTimeMillis() + (long)offset) % (long)speed);
      hue /= (float)speed;
      return Color.getHSBColor(hue, s, 1.0F).getRGB();
   }

   public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
      float f = (float)(col1 >> 24 & 255) / 255.0F;
      float f1 = (float)(col1 >> 16 & 255) / 255.0F;
      float f2 = (float)(col1 >> 8 & 255) / 255.0F;
      float f3 = (float)(col1 & 255) / 255.0F;
      float f4 = (float)(col2 >> 24 & 255) / 255.0F;
      float f5 = (float)(col2 >> 16 & 255) / 255.0F;
      float f6 = (float)(col2 >> 8 & 255) / 255.0F;
      float f7 = (float)(col2 & 255) / 255.0F;
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glShadeModel(7425);
      GL11.glPushMatrix();
      GL11.glBegin(7);
      GL11.glColor4f(f1, f2, f3, f);
      GL11.glVertex2d(left, top);
      GL11.glVertex2d(left, bottom);
      GL11.glColor4f(f5, f6, f7, f4);
      GL11.glVertex2d(right, bottom);
      GL11.glVertex2d(right, top);
      GL11.glEnd();
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
   }

   public static Color darker(Color color, float factor) {
      factor = MathHelper.clamp_float(factor, 0.001F, 0.999F);
      return new Color(Math.max((int)((float)color.getRed() * factor), 0), Math.max((int)((float)color.getGreen() * factor), 0), Math.max((int)((float)color.getBlue() * factor), 0), color.getAlpha());
   }

   public static Color getGradientOffset(Color color1, Color color2, double offset) {
      double inverse_percent;
      int redPart;
      if (offset > 1.0D) {
         inverse_percent = offset % 1.0D;
         redPart = (int)offset;
         offset = redPart % 2 == 0 ? inverse_percent : 1.0D - inverse_percent;
      }

      inverse_percent = 1.0D - offset;
      redPart = (int)((double)color1.getRed() * inverse_percent + (double)color2.getRed() * offset);
      int greenPart = (int)((double)color1.getGreen() * inverse_percent + (double)color2.getGreen() * offset);
      int bluePart = (int)((double)color1.getBlue() * inverse_percent + (double)color2.getBlue() * offset);
      return new Color(redPart, greenPart, bluePart);
   }

   public static int darker(int color, float factor) {
      int r = (int)((float)(color >> 16 & 255) * factor);
      int g = (int)((float)(color >> 8 & 255) * factor);
      int b = (int)((float)(color & 255) * factor);
      int a = color >> 24 & 255;
      return (r & 255) << 16 | (g & 255) << 8 | b & 255 | (a & 255) << 24;
   }

   public static int darker(int color) {
      return darker(color, 0.6F);
   }

   public static int blendColors(float progress) {
      int[] colors = new int[]{-16711831, -256, -65536, -8388608};
      return blendColors(colors, progress);
   }

   public static int fadeBetween(int startColor, int endColor, float progress) {
      if (progress > 1.0F) {
         progress = 1.0F - progress % 1.0F;
      }

      return fadeTo(startColor, endColor, progress);
   }

   public static int fadeBetween(int startColor, int endColor) {
      return fadeBetween(startColor, endColor, (float)(System.currentTimeMillis() % 2000L) / 1000.0F);
   }

   public static int fadeTo(int startColor, int endColor, float progress) {
      float invert = 1.0F - progress;
      int r = (int)((float)(startColor >> 16 & 255) * invert + (float)(endColor >> 16 & 255) * progress);
      int g = (int)((float)(startColor >> 8 & 255) * invert + (float)(endColor >> 8 & 255) * progress);
      int b = (int)((float)(startColor & 255) * invert + (float)(endColor & 255) * progress);
      int a = (int)((float)(startColor >> 24 & 255) * invert + (float)(endColor >> 24 & 255) * progress);
      return (a & 255) << 24 | (r & 255) << 16 | (g & 255) << 8 | b & 255;
   }

   public static int blendColors(int[] colors, float progress) {
      int size = colors.length;
      if (progress == 1.0F) {
         return colors[0];
      } else if (progress == 0.0F) {
         return colors[size - 1];
      } else {
         float mulProgress = Math.max(0.0F, (1.0F - progress) * (float)(size - 1));
         int index = (int)mulProgress;
         int startCol = colors[index];
         int endColor = colors[index + 1];
         return fadeBetween(startCol, endColor, mulProgress - (float)index);
      }
   }

   public static void enableBlending() {
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
   }

   public static void enableDepth() {
      GL11.glDepthMask(true);
      GL11.glEnable(2929);
   }

   public static void disableDepth() {
      GL11.glDepthMask(false);
      GL11.glDisable(2929);
   }

   public static boolean isInViewFrustrum(Entity entity) {
      return isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
   }

   private static boolean isInViewFrustrum(AxisAlignedBB bb) {
      Entity current = Minecraft.getMinecraft().getRenderViewEntity();
      frustrum.setPosition(current.posX, current.posY, current.posZ);
      return frustrum.isBoundingBoxInFrustum(bb);
   }

   public static double interpolateScale(double current, double old, double scale) {
      return old + (current - old) * scale;
   }

   public static int drawHealth(EntityLivingBase entityLivingBase) {
      float health = entityLivingBase.getHealth();
      float maxHealth = entityLivingBase.getMaxHealth();
      return Color.HSBtoRGB(Math.max(0.0F, Math.min(health, maxHealth) / maxHealth) / 3.0F, 1.0F, 1.0F) | -16777216;
   }

   public static Color drawHealth(float health, float maxHealth) {
      float[] fractions = new float[]{0.0F, 0.5F, 1.0F};
      Color[] colors = new Color[]{new Color(108, 0, 0), new Color(255, 51, 0), Color.GREEN};
      float progress = health / maxHealth;
      return blendColors(fractions, colors, progress).brighter();
   }

   public static Color blendColors(float[] fractions, Color[] colors, float progress) {
      if (fractions.length == colors.length) {
         int[] indices = getFractionIndices(fractions, progress);
         float[] range = new float[]{fractions[indices[0]], fractions[indices[1]]};
         Color[] colorRange = new Color[]{colors[indices[0]], colors[indices[1]]};
         float max = range[1] - range[0];
         float value = progress - range[0];
         float weight = value / max;
         Color color = blend(colorRange[0], colorRange[1], (double)(1.0F - weight));
         return color;
      } else {
         throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
      }
   }

   public static Color blend(Color color1, Color color2, double ratio) {
      float r = (float)ratio;
      float ir = 1.0F - r;
      float[] rgb1 = color1.getColorComponents(new float[3]);
      float[] rgb2 = color2.getColorComponents(new float[3]);
      float red = rgb1[0] * r + rgb2[0] * ir;
      float green = rgb1[1] * r + rgb2[1] * ir;
      float blue = rgb1[2] * r + rgb2[2] * ir;
      if (red < 0.0F) {
         red = 0.0F;
      } else if (red > 255.0F) {
         red = 255.0F;
      }

      if (green < 0.0F) {
         green = 0.0F;
      } else if (green > 255.0F) {
         green = 255.0F;
      }

      if (blue < 0.0F) {
         blue = 0.0F;
      } else if (blue > 255.0F) {
         blue = 255.0F;
      }

      Color color3 = null;

      try {
         color3 = new Color(red, green, blue);
      } catch (IllegalArgumentException var13) {
      }

      return color3;
   }

   public static int[] getFractionIndices(float[] fractions, float progress) {
      int[] range = new int[2];

      int startPoint;
      for(startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
      }

      if (startPoint >= fractions.length) {
         startPoint = fractions.length - 1;
      }

      range[0] = startPoint - 1;
      range[1] = startPoint;
      return range;
   }

   public static double interpolate(double old, double now, float partialTicks) {
      return old + (now - old) * (double)partialTicks;
   }

   public static float interpolate(float old, float now, float partialTicks) {
      return old + (now - old) * partialTicks;
   }

   public static double linearAnimation(double now, double desired, double speed) {
      double dif = Math.abs(now - desired);
      int fps = Minecraft.getDebugFPS();
      if (dif > 0.0D) {
         double animationSpeed = MathUtil.round(Math.min(10.0D, Math.max(0.005D, 144.0D / (double)fps * speed)), 0.005D);
         if (dif != 0.0D && dif < animationSpeed) {
            animationSpeed = dif;
         }

         if (now < desired) {
            return now + animationSpeed;
         }

         if (now > desired) {
            return now - animationSpeed;
         }
      }

      return now;
   }

   public static void prepareScissorBox(ScaledResolution sr, float x, float y, float width, float height) {
      float x2 = x + width;
      float y2 = y + height;
      int factor = sr.getScaleFactor();
      GL11.glScissor((int)(x * (float)factor), (int)(((float)sr.getScaledHeight() - y2) * (float)factor), (int)((x2 - x) * (float)factor), (int)((y2 - y) * (float)factor));
   }

   public static void color(int color) {
      float red = (float)(color & 255) / 255.0F;
      float green = (float)(color >> 8 & 255) / 255.0F;
      float blue = (float)(color >> 16 & 255) / 255.0F;
      float alpha = (float)(color >> 24 & 255) / 255.0F;
      GlStateManager.color(red, green, blue, alpha);
   }

   public static void drawRoundedRectSmooth(double x, double y, double width, double height, double radius, int color) {
      drawRoundedRectFoot(x, y, width - x, height - y, radius, color);
   }

   public static void drawRoundedRect1(double x, double y, double width, double height, double radius, int color) {
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      double x1 = x + width;
      double y1 = y + height;
      float f = (float)(color >> 24 & 255) / 255.0F;
      float f1 = (float)(color >> 16 & 255) / 255.0F;
      float f2 = (float)(color >> 8 & 255) / 255.0F;
      float f3 = (float)(color & 255) / 255.0F;
      GL11.glPushAttrib(0);
      GL11.glScaled(0.5D, 0.5D, 0.5D);
      x *= 2.0D;
      y *= 2.0D;
      x1 *= 2.0D;
      y1 *= 2.0D;
      GL11.glDisable(3553);
      GL11.glColor4f(f1, f2, f3, f);
      GL11.glEnable(2848);
      GL11.glBegin(9);

      int i;
      for(i = 0; i <= 90; i += 3) {
         GL11.glVertex2d(x + radius + Math.sin((double)i * 3.141592653589793D / 180.0D) * radius * -1.0D, y + radius + Math.cos((double)i * 3.141592653589793D / 180.0D) * radius * -1.0D);
      }

      for(i = 90; i <= 180; i += 3) {
         GL11.glVertex2d(x + radius + Math.sin((double)i * 3.141592653589793D / 180.0D) * radius * -1.0D, y1 - radius + Math.cos((double)i * 3.141592653589793D / 180.0D) * radius * -1.0D);
      }

      for(i = 0; i <= 90; i += 3) {
         GL11.glVertex2d(x1 - radius + Math.sin((double)i * 3.141592653589793D / 180.0D) * radius, y1 - radius + Math.cos((double)i * 3.141592653589793D / 180.0D) * radius);
      }

      for(i = 90; i <= 180; i += 3) {
         GL11.glVertex2d(x1 - radius + Math.sin((double)i * 3.141592653589793D / 180.0D) * radius, y + radius + Math.cos((double)i * 3.141592653589793D / 180.0D) * radius);
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

   public static void drawRoundedRectFoot(double x, double y, double width, double height, double radius, int color) {
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      double x1 = x + width;
      double y1 = y + height;
      float f = (float)(color >> 24 & 255) / 255.0F;
      float f1 = (float)(color >> 16 & 255) / 255.0F;
      float f2 = (float)(color >> 8 & 255) / 255.0F;
      float f3 = (float)(color & 255) / 255.0F;
      GL11.glPushAttrib(0);
      GL11.glScaled(0.5D, 0.5D, 0.5D);
      x *= 2.0D;
      y *= 2.0D;
      x1 *= 2.0D;
      y1 *= 2.0D;
      GL11.glDisable(3553);
      GL11.glColor4f(f1, f2, f3, f);
      GL11.glEnable(2848);
      GL11.glBegin(9);

      int i;
      for(i = 0; i <= 90; i += 3) {
         GL11.glVertex2d(x + radius + Math.sin((double)i * 3.141592653589793D / 180.0D) * radius * -1.0D, y + radius + Math.cos((double)i * 3.141592653589793D / 180.0D) * radius * -1.0D);
      }

      for(i = 90; i <= 180; i += 3) {
         GL11.glVertex2d(x + radius + Math.sin((double)i * 3.141592653589793D / 180.0D) * radius * -1.0D, y1 - radius + Math.cos((double)i * 3.141592653589793D / 180.0D) * radius * -1.0D);
      }

      for(i = 0; i <= 90; i += 3) {
         GL11.glVertex2d(x1 - radius + Math.sin((double)i * 3.141592653589793D / 180.0D) * radius, y1 - radius + Math.cos((double)i * 3.141592653589793D / 180.0D) * radius);
      }

      for(i = 90; i <= 180; i += 3) {
         GL11.glVertex2d(x1 - radius + Math.sin((double)i * 3.141592653589793D / 180.0D) * radius, y + radius + Math.cos((double)i * 3.141592653589793D / 180.0D) * radius);
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

   public static void drawCircle(double x, double y, float radius, int color) {
      float alpha = (float)(color >> 24 & 255) / 255.0F;
      float red = (float)(color >> 16 & 255) / 255.0F;
      float green = (float)(color >> 8 & 255) / 255.0F;
      float blue = (float)(color & 255) / 255.0F;
      GL11.glColor4f(red, green, blue, alpha);
      GL11.glBegin(9);

      for(int i = 0; i <= 360; ++i) {
         GL11.glVertex2d(x + Math.sin((double)i * 3.141526D / 180.0D) * (double)radius, y + Math.cos((double)i * 3.141526D / 180.0D) * (double)radius);
      }

      GL11.glEnd();
   }

   public static void drawBorderedCircle(double x, double y, float radius, int outsideC, int insideC) {
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glPushMatrix();
      float scale = 0.1F;
      GL11.glScalef(0.1F, 0.1F, 0.1F);
      drawCircle(x *= 10.0D, y *= 10.0D, radius *= 10.0F, insideC);
      GL11.glScalef(10.0F, 10.0F, 10.0F);
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      GL11.glDisable(2848);
   }

   public static void entityRenderer(Minecraft mc, Entity entity, Runnable runnable) {
      double var10000 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)mc.timer.renderPartialTicks;
      mc.getRenderManager();
      double x = var10000 - RenderManager.renderPosX;
      var10000 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)mc.timer.renderPartialTicks;
      mc.getRenderManager();
      double y = var10000 - RenderManager.renderPosY;
      var10000 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)mc.timer.renderPartialTicks;
      mc.getRenderManager();
      double z = var10000 - RenderManager.renderPosZ;
      GL11.glPushMatrix();
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(2929);
      GlStateManager.translate(x, y, z);
      runnable.run();
      GL11.glEnable(2929);
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
   }

   public static void drawRect2(double x, double y, double width, double height, int color) {
      float f = (float)(color >> 24 & 255) / 255.0F;
      float f1 = (float)(color >> 16 & 255) / 255.0F;
      float f2 = (float)(color >> 8 & 255) / 255.0F;
      float f3 = (float)(color & 255) / 255.0F;
      GL11.glColor4f(f1, f2, f3, f);
      Gui.drawRect(x, y, x + width, y + height, color);
   }

   public static void drawOutlineRect(float drawX, float drawY, float drawWidth, float drawHeight, int color) {
      drawRect((double)drawX, (double)drawY, (double)drawWidth, (double)(drawY + 1.0F), color);
      drawRect((double)drawX, (double)(drawY + 1.0F), (double)(drawX + 1.0F), (double)drawHeight, color);
      drawRect((double)(drawWidth - 1.0F), (double)(drawY + 1.0F), (double)drawWidth, (double)(drawHeight - 1.0F), color);
      drawRect((double)(drawX + 1.0F), (double)(drawHeight - 1.0F), (double)drawWidth, (double)drawHeight, color);
   }

   public static void drawRectangle(double left, double top, double right, double bottom, int color) {
      double j;
      if (left < right) {
         j = left;
         left = right;
         right = j;
      }

      if (top < bottom) {
         j = top;
         top = bottom;
         bottom = j;
      }

      float f3 = (float)(color >> 24 & 255) / 255.0F;
      float f = (float)(color >> 16 & 255) / 255.0F;
      float f1 = (float)(color >> 8 & 255) / 255.0F;
      float f2 = (float)(color & 255) / 255.0F;
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(f, f1, f2, f3);
      worldrenderer.begin(7, DefaultVertexFormats.POSITION);
      worldrenderer.pos(left, bottom, 0.0D).endVertex();
      worldrenderer.pos(right, bottom, 0.0D).endVertex();
      worldrenderer.pos(right, top, 0.0D).endVertex();
      worldrenderer.pos(left, top, 0.0D).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawImg(ResourceLocation loc, double posX, double posY, double width, double height) {
      GlStateManager.pushMatrix();
      GlStateManager.enableAlpha();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
      float f = 1.0F / (float)width;
      float f1 = 1.0F / (float)height;
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
      worldrenderer.pos(posX, posY + height, 0.0D).tex((double)(0.0F * f), (double)((0.0F + (float)height) * f1)).endVertex();
      worldrenderer.pos(posX + width, posY + height, 0.0D).tex((double)((0.0F + (float)width) * f), (double)((0.0F + (float)height) * f1)).endVertex();
      worldrenderer.pos(posX + width, posY, 0.0D).tex((double)((0.0F + (float)width) * f), (double)(0.0F * f1)).endVertex();
      worldrenderer.pos(posX, posY, 0.0D).tex((double)(0.0F * f), (double)(0.0F * f1)).endVertex();
      tessellator.draw();
      GlStateManager.popMatrix();
   }

   public static void draw2DRect(double left, double top, double right, double bottom, int color) {
      double var5;
      if (left < right) {
         var5 = left;
         left = right;
         right = var5;
      }

      if (top < bottom) {
         var5 = top;
         top = bottom;
         bottom = var5;
      }

      float var11 = (float)(color >> 24 & 255) / 255.0F;
      float var6 = (float)(color >> 16 & 255) / 255.0F;
      float var7 = (float)(color >> 8 & 255) / 255.0F;
      float var8 = (float)(color & 255) / 255.0F;
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(var6, var7, var8, var11);
      worldrenderer.begin(7, DefaultVertexFormats.POSITION);
      worldrenderer.pos(left, bottom, 0.0D);
      worldrenderer.pos(right, bottom, 0.0D);
      worldrenderer.pos(right, top, 0.0D);
      worldrenderer.pos(left, top, 0.0D);
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawSpecialGradRect(int left, int top, int right, int bottom, int color1, int color2) {
      enableGL2D();
      GL11.glShadeModel(7425);
      GL11.glBegin(7);
      glColor(color1);
      GL11.glVertex2f((float)left, (float)top);
      GL11.glVertex2f((float)left, (float)bottom);
      glColor(color2);
      GL11.glVertex2f((float)right, (float)bottom);
      GL11.glVertex2f((float)right, (float)top);
      GL11.glEnd();
      GL11.glShadeModel(7424);
      disableGL2D();
      float f3 = (float)((new Color(0, 0, 0, 0)).getRGB() >> 24 & 255) / 255.0F;
      float f = (float)((new Color(0, 0, 0, 0)).getRGB() >> 16 & 255) / 255.0F;
      float f1 = (float)((new Color(0, 0, 0, 0)).getRGB() >> 8 & 255) / 255.0F;
      float f2 = (float)((new Color(0, 0, 0, 0)).getRGB() & 255) / 255.0F;
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(f, f1, f2, f3);
      worldrenderer.begin(7, DefaultVertexFormats.POSITION);
      worldrenderer.pos((double)left, (double)bottom, 0.0D).endVertex();
      worldrenderer.pos((double)right, (double)bottom, 0.0D).endVertex();
      worldrenderer.pos((double)right, (double)top, 0.0D).endVertex();
      worldrenderer.pos((double)left, (double)top, 0.0D).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
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

   public static void disableGL2D() {
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
      GL11.glDisable(2848);
      GL11.glHint(3154, 4352);
      GL11.glHint(3155, 4352);
   }

   public static void drawRect(Rectangle rectangle, int color) {
      drawRect((double)rectangle.x, (double)rectangle.y, (double)(rectangle.x + rectangle.width), (double)(rectangle.y + rectangle.height), color);
   }

   public static void drawRect(double x, double y, double x1, double y1, int color) {
      enableGL2D();
      glColor(color);
      drawRect((float)x, (float)y, (float)x1, (float)y1);
      disableGL2D();
   }

   public static void drawRect(Vector2f pos, Vector2f end, int color) {
      Gui.drawRect((int)pos.getX(), (int)pos.getY(), (int)end.getX(), (int)end.getY(), color);
   }

   public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int internalColor, int borderColor) {
      enableGL2D();
      glColor(internalColor);
      drawRect(x + width, y + width, x1 - width, y1 - width);
      glColor(borderColor);
      drawRect(x + width, y, x1 - width, y + width);
      drawRect(x, y, x + width, y1);
      drawRect(x1 - width, y, x1, y1);
      if (width != 1.069F) {
      }

      drawRect(x + width, y1 - width, x1 - width, y1);
      disableGL2D();
   }

   public static void drawClickguiRect(float x, float y, float x1, float y1, int insideC, int borderC) {
      enableGL2D();
      x *= 2.0F;
      x1 *= 2.0F;
      y *= 2.0F;
      y1 *= 2.0F;
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      drawVLine(x, y, y1, insideC);
      drawVLine(x1 - 1.0F, y, y1, insideC);
      drawHLine(x, x1 - 1.0F, y, insideC);
      drawHLine(x, x1 - 2.0F, y1 - 1.0F, insideC);
      drawRect((double)(x + 1.0F), (double)(y + 1.0F), (double)(x1 - 1.0F), (double)(y1 - 1.0F), insideC);
      drawVLine(x + 1.0F, y, y1, insideC);
      drawVLine(x1 - 2.0F, y, y1, insideC);
      drawHLine(x, x1 - 1.0F, y + 1.0F, insideC);
      drawHLine(x, x1 - 2.0F, y1 - 2.0F, insideC);
      drawVLine(x + 2.0F, y, y1, insideC);
      drawVLine(x1 - 3.0F, y, y1, insideC);
      drawHLine(x, x1 - 4.0F, y + 2.0F, insideC);
      drawHLine(x, x1 - 2.0F, y1 - 3.0F, insideC);
      drawVLine(x + 3.0F, y + 3.0F, y1 - 3.0F, borderC);
      drawVLine(x1 - 4.0F, y + 3.0F, y1 - 3.0F, borderC);
      drawHLine(x + 3.0F, x1 - 4.0F, y + 3.0F, borderC);
      drawHLine(x + 3.0F, x1 - 4.0F, y1 - 4.0F, borderC);
      drawVLine(x + 4.0F, y + 3.0F, y1 - 3.0F, borderC);
      drawVLine(x1 - 5.0F, y + 3.0F, y1 - 3.0F, borderC);
      drawHLine(x + 3.0F, x1 - 4.0F, y + 4.0F, borderC);
      drawHLine(x + 3.0F, x1 - 4.0F, y1 - 5.0F, borderC);
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      disableGL2D();
   }

   public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
      enableGL2D();
      x *= 2.0F;
      x1 *= 2.0F;
      y *= 2.0F;
      y1 *= 2.0F;
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      drawVLine(x, y, y1, borderC);
      drawVLine(x1 - 1.0F, y, y1, borderC);
      drawHLine(x, x1 - 1.0F, y, borderC);
      drawHLine(x, x1 - 2.0F, y1 - 1.0F, borderC);
      drawRect((double)(x + 1.0F), (double)(y + 1.0F), (double)(x1 - 1.0F), (double)(y1 - 1.0F), insideC);
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      disableGL2D();
   }

   public static void drawBorderedRectNoTop(float x, float y, float x1, float y1, float bottomWidth, int insideC, int borderC) {
      enableGL2D();
      x *= 2.0F;
      x1 *= 2.0F;
      y *= 2.0F;
      y1 *= 2.0F;
      bottomWidth *= 2.0F;
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      drawVLine(x, y, y1, borderC);
      drawVLine(x1 - 1.0F, y, y1, borderC);
      drawHLine(x, x + bottomWidth, y1 - 1.0F, borderC);
      drawRect((double)(x + 1.0F), (double)(y + 1.0F), (double)(x1 - 1.0F), (double)(y1 - 1.0F), insideC);
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      disableGL2D();
   }

   public static void drawBorderedRectReliant(float x, float y, float x1, float y1, float lineWidth, int inside, int border) {
      enableGL2D();
      drawRect((double)x, (double)y, (double)x1, (double)y1, inside);
      glColor(border);
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glLineWidth(lineWidth);
      GL11.glBegin(3);
      GL11.glVertex2f(x, y);
      GL11.glVertex2f(x, y1);
      GL11.glVertex2f(x1, y1);
      GL11.glVertex2f(x1, y);
      GL11.glVertex2f(x, y);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      disableGL2D();
   }

   public static void drawGradientBorderedRectReliant(float x, float y, float x1, float y1, float lineWidth, int border, int bottom, int top) {
      enableGL2D();
      drawGradientRect(x, y, x1, y1, top, bottom);
      glColor(border);
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glLineWidth(lineWidth);
      GL11.glBegin(3);
      GL11.glVertex2f(x, y);
      GL11.glVertex2f(x, y1);
      GL11.glVertex2f(x1, y1);
      GL11.glVertex2f(x1, y);
      GL11.glVertex2f(x, y);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
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

   public static void post3D() {
      GL11.glDepthMask(true);
      GL11.glEnable(2929);
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
      enableGL2D();
      drawRect1(x + 0.5F, y, x1 - 0.5F, y + 0.5F, insideC);
      drawRect1(x + 0.5F, y1 - 0.5F, x1 - 0.5F, y1, insideC);
      drawRect1(x, y + 0.5F, x1, y1 - 0.5F, insideC);
      disableGL2D();
   }

   public static void drawRoundedRect(double x, double y, double width, double height, double cornerRadius) {
      int slices = true;
      drawFillRectangle(x + cornerRadius, y, width - 2.0D * cornerRadius, height);
      drawFillRectangle(x, y + cornerRadius, cornerRadius, height - 2.0D * cornerRadius);
      drawFillRectangle(x + width - cornerRadius, y + cornerRadius, cornerRadius, height - 2.0D * cornerRadius);
      drawCirclePart(x + cornerRadius, y + cornerRadius, -3.1415927F, -1.5707964F, (float)cornerRadius, 10);
      drawCirclePart(x + cornerRadius, y + height - cornerRadius, -1.5707964F, 0.0F, (float)cornerRadius, 10);
      drawCirclePart(x + width - cornerRadius, y + cornerRadius, 1.5707964F, 3.1415927F, (float)cornerRadius, 10);
      drawCirclePart(x + width - cornerRadius, y + height - cornerRadius, 0.0F, 1.5707964F, (float)cornerRadius, 10);
      GL11.glDisable(3042);
      GL11.glEnable(3553);
      GlStateManager.disableBlend();
   }

   public static void drawFillRectangle(double x, double y, double width, double height) {
      GlStateManager.enableBlend();
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glBegin(7);
      GL11.glVertex2d(x, y + height);
      GL11.glVertex2d(x + width, y + height);
      GL11.glVertex2d(x + width, y);
      GL11.glVertex2d(x, y);
      GL11.glEnd();
   }

   public static void drawCirclePart(double x, double y, float fromAngle, float toAngle, float radius, int slices) {
      GlStateManager.enableBlend();
      GL11.glBegin(6);
      GL11.glVertex2d(x, y);
      float increment = (toAngle - fromAngle) / (float)slices;

      for(int i = 0; i <= slices; ++i) {
         float angle = fromAngle + (float)i * increment;
         float dX = MathHelper.sin(angle);
         float dY = MathHelper.cos(angle);
         GL11.glVertex2d(x + (double)(dX * radius), y + (double)(dY * radius));
      }

      GL11.glEnd();
   }

   public static void drawBorderedRect(double x, double y, double width, double height, double lineSize, int borderColor, int color) {
      Gui.drawRect(x, y, x + width, y + height, color);
      Gui.drawRect(x, y, x + width, y + lineSize, borderColor);
      Gui.drawRect(x, y, x + lineSize, y + height, borderColor);
      Gui.drawRect(x + width, y, x + width - lineSize, y + height, borderColor);
      Gui.drawRect(x, y + height, x + width, y + height - lineSize, borderColor);
   }

   public static void drawRoundedRect1(double x, double y, double x1, double y1, int borderC, int insideC) {
      enableGL2D();
      drawRect1((float)x + 0.5F, (float)y, (float)(x + x1 - 0.5D), (float)(y + y1 + 0.5D), insideC);
      drawRect1((float)x + 0.5F, (float)y - 0.5F, (float)(x + x1 - 0.5D), (float)(y + y1), insideC);
      drawRect1((float)x, (float)(y + y1 + 0.5D), (float)(x + x1), (float)(y + y1), insideC);
      disableGL2D();
   }

   public static void drawRoundedRect2(float x, float y, float x1, float y1, float borderC, int insideC) {
      enableGL2D();
      drawRect1(x + 0.5F, y, x + x1 - 0.5F, y + y1 + 0.5F, insideC);
      drawRect1(x + 0.5F, y - 0.5F, x + x1 - 0.5F, y + y1, insideC);
      drawRect1(x, y + y1 + 0.5F, x + x1, y + y1, insideC);
      disableGL2D();
   }

   public static void drawRoundedRect2(double x, double y, double width, double height, double radius, int color) {
      drawRoundedRect(x, y, width - x, height - y, radius, color);
   }

   public static void drawRoundedRect(double x, double y, double width, double height, double radius, int color) {
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      double x1 = x + width;
      double y1 = y + height;
      float f = (float)(color >> 24 & 255) / 255.0F;
      float f1 = (float)(color >> 16 & 255) / 255.0F;
      float f2 = (float)(color >> 8 & 255) / 255.0F;
      float f3 = (float)(color & 255) / 255.0F;
      GL11.glPushAttrib(0);
      GL11.glScaled(0.5D, 0.5D, 0.5D);
      x *= 2.0D;
      y *= 2.0D;
      x1 *= 2.0D;
      y1 *= 2.0D;
      GL11.glDisable(3553);
      GL11.glColor4f(f1, f2, f3, f);
      GL11.glEnable(2848);
      GL11.glBegin(9);

      int i;
      for(i = 0; i <= 90; i += 3) {
         GL11.glVertex2d(x + radius + Math.sin((double)i * 3.141592653589793D / 180.0D) * radius * -1.0D, y + radius + Math.cos((double)i * 3.141592653589793D / 180.0D) * radius * -1.0D);
      }

      for(i = 90; i <= 180; i += 3) {
         GL11.glVertex2d(x + radius + Math.sin((double)i * 3.141592653589793D / 180.0D) * radius * -1.0D, y1 - radius + Math.cos((double)i * 3.141592653589793D / 180.0D) * radius * -1.0D);
      }

      for(i = 0; i <= 90; i += 3) {
         GL11.glVertex2d(x1 - radius + Math.sin((double)i * 3.141592653589793D / 180.0D) * radius, y1 - radius + Math.cos((double)i * 3.141592653589793D / 180.0D) * radius);
      }

      for(i = 90; i <= 180; i += 3) {
         GL11.glVertex2d(x1 - radius + Math.sin((double)i * 3.141592653589793D / 180.0D) * radius, y + radius + Math.cos((double)i * 3.141592653589793D / 180.0D) * radius);
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

   public static void drawSpecialRoundedRect(int glFlag, int left, int top, int right, int bottom, int color) {
      enableGL2D();
      drawRect1((float)left + 0.5F, (float)top, (float)right - 0.5F, (float)top + 0.5F, color);
      drawRect1((float)left + 0.5F, (float)bottom - 0.5F, (float)right - 0.5F, (float)bottom, color);
      drawRect1((float)left, (float)top + 0.5F, (float)right, (float)bottom - 0.5F, color);
      disableGL2D();
      float f3 = (float)((new Color(0, 0, 0, 0)).getRGB() >> 24 & 255) / 255.0F;
      float f = (float)((new Color(0, 0, 0, 0)).getRGB() >> 16 & 255) / 255.0F;
      float f1 = (float)((new Color(0, 0, 0, 0)).getRGB() >> 8 & 255) / 255.0F;
      float f2 = (float)((new Color(0, 0, 0, 0)).getRGB() & 255) / 255.0F;
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawRect1(float x, float y, float x1, float y1, int color) {
      enableGL2D();
      glColor(color);
      drawRect(x, y, x1, y1);
      disableGL2D();
   }

   public static void drawRect1(float x, float y, float x1, float y1) {
      GL11.glBegin(7);
      GL11.glVertex2f(x, y1);
      GL11.glVertex2f(x1, y1);
      GL11.glVertex2f(x1, y);
      GL11.glVertex2f(x, y);
      GL11.glEnd();
   }

   public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
      enableGL2D();
      GL11.glShadeModel(7425);
      GL11.glBegin(7);
      glColor(topColor);
      GL11.glVertex2f(x, y1);
      GL11.glVertex2f(x1, y1);
      glColor(bottomColor);
      GL11.glVertex2f(x1, y);
      GL11.glVertex2f(x, y);
      GL11.glEnd();
      GL11.glShadeModel(7424);
      disableGL2D();
   }

   public static void drawGradientRect1(float x, float y, float x1, float y1, int topColor, int bottomColor) {
      enableGL2D();
      GL11.glShadeModel(7425);
      GL11.glBegin(7);
      glColor(topColor);
      GL11.glVertex2f(x, y1);
      GL11.glVertex2f(x1, y1);
      glColor(bottomColor);
      GL11.glVertex2f(x1, y);
      GL11.glVertex2f(x, y);
      GL11.glEnd();
      GL11.glShadeModel(7424);
      disableGL2D();
   }

   public static void drawBorderedRect(Rectangle rectangle, float width, int internalColor, int borderColor) {
      float x = (float)rectangle.x;
      float y = (float)rectangle.y;
      float x2 = (float)(rectangle.x + rectangle.width);
      float y2 = (float)(rectangle.y + rectangle.height);
      enableGL2D();
      glColor(internalColor);
      drawRect(x + width, y + width, x2 - width, y2 - width);
      glColor(borderColor);
      drawRect(x + 1.0F, y, x2 - 1.0F, y + width);
      drawRect(x, y, x + width, y2);
      drawRect(x2 - width, y, x2, y2);
      drawRect(x + 1.0F, y2 - width, x2 - 1.0F, y2);
      disableGL2D();
   }

   public static void drawGradientHRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
      enableGL2D();
      GL11.glShadeModel(7425);
      GL11.glBegin(7);
      glColor(topColor);
      GL11.glVertex2f(x, y);
      GL11.glVertex2f(x, y1);
      glColor(bottomColor);
      GL11.glVertex2f(x1, y1);
      GL11.glVertex2f(x1, y);
      GL11.glEnd();
      GL11.glShadeModel(7424);
      disableGL2D();
   }

   public static void drawSpecialGradRect(int glFlag, int left, int top, int right, int bottom, int color1, int color2) {
      enableGL2D();
      GL11.glShadeModel(7425);
      GL11.glBegin(7);
      glColor(color1);
      GL11.glVertex2f((float)left, (float)top);
      GL11.glVertex2f((float)left, (float)bottom);
      glColor(color2);
      GL11.glVertex2f((float)right, (float)bottom);
      GL11.glVertex2f((float)right, (float)top);
      GL11.glEnd();
      GL11.glShadeModel(7424);
      disableGL2D();
      float f3 = (float)((new Color(0, 0, 0, 0)).getRGB() >> 24 & 255) / 255.0F;
      float f = (float)((new Color(0, 0, 0, 0)).getRGB() >> 16 & 255) / 255.0F;
      float f1 = (float)((new Color(0, 0, 0, 0)).getRGB() >> 8 & 255) / 255.0F;
      float f2 = (float)((new Color(0, 0, 0, 0)).getRGB() & 255) / 255.0F;
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(f, f1, f2, f3);
      worldrenderer.begin(glFlag, DefaultVertexFormats.POSITION);
      worldrenderer.pos((double)left, (double)bottom, 0.0D).endVertex();
      worldrenderer.pos((double)right, (double)bottom, 0.0D).endVertex();
      worldrenderer.pos((double)right, (double)top, 0.0D).endVertex();
      worldrenderer.pos((double)left, (double)top, 0.0D).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawGradientRect(double x, double y, double x2, double y2, int col1, int col2) {
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glShadeModel(7425);
      GL11.glPushMatrix();
      GL11.glBegin(7);
      glColor(col1);
      GL11.glVertex2d(x2, y);
      GL11.glVertex2d(x, y);
      glColor(col2);
      GL11.glVertex2d(x, y2);
      GL11.glVertex2d(x2, y2);
      GL11.glEnd();
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glShadeModel(7424);
   }

   public static void drawGradientBorderedRect(double x, double y, double x2, double y2, float l1, int col1, int col2, int col3) {
      enableGL2D();
      GL11.glPushMatrix();
      glColor(col1);
      GL11.glLineWidth(1.0F);
      GL11.glBegin(1);
      GL11.glVertex2d(x, y);
      GL11.glVertex2d(x, y2);
      GL11.glVertex2d(x2, y2);
      GL11.glVertex2d(x2, y);
      GL11.glVertex2d(x, y);
      GL11.glVertex2d(x2, y);
      GL11.glVertex2d(x, y2);
      GL11.glVertex2d(x2, y2);
      GL11.glEnd();
      GL11.glPopMatrix();
      drawGradientRect(x, y, x2, y2, col2, col3);
      disableGL2D();
   }

   public static void glColor(Color color) {
      GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
   }

   public static void glColor(int hex) {
      float alpha = (float)(hex >> 24 & 255) / 255.0F;
      float red = (float)(hex >> 16 & 255) / 255.0F;
      float green = (float)(hex >> 8 & 255) / 255.0F;
      float blue = (float)(hex & 255) / 255.0F;
      GL11.glColor4f(red, green, blue, alpha);
   }

   public static void glColor(float alpha, int redRGB, int greenRGB, int blueRGB) {
      float red = 0.003921569F * (float)redRGB;
      float green = 0.003921569F * (float)greenRGB;
      float blue = 0.003921569F * (float)blueRGB;
      GL11.glColor4f(red, green, blue, alpha);
   }

   public static void drawStrip(int x, int y, float width, double angle, float points, float radius, int color) {
      float f1 = (float)(color >> 24 & 255) / 255.0F;
      float f2 = (float)(color >> 16 & 255) / 255.0F;
      float f3 = (float)(color >> 8 & 255) / 255.0F;
      float f4 = (float)(color & 255) / 255.0F;
      GL11.glPushMatrix();
      GL11.glTranslated((double)x, (double)y, 0.0D);
      GL11.glColor4f(f2, f3, f4, f1);
      GL11.glLineWidth(width);
      int i;
      float a;
      float xc;
      float yc;
      if (angle > 0.0D) {
         GL11.glBegin(3);

         for(i = 0; (double)i < angle; ++i) {
            a = (float)((double)i * (angle * 3.141592653589793D / (double)points));
            xc = (float)(Math.cos((double)a) * (double)radius);
            yc = (float)(Math.sin((double)a) * (double)radius);
            GL11.glVertex2f(xc, yc);
         }

         GL11.glEnd();
      }

      if (angle < 0.0D) {
         GL11.glBegin(3);

         for(i = 0; (double)i > angle; --i) {
            a = (float)((double)i * (angle * 3.141592653589793D / (double)points));
            xc = (float)(Math.cos((double)a) * (double)(-radius));
            yc = (float)(Math.sin((double)a) * (double)(-radius));
            GL11.glVertex2f(xc, yc);
         }

         GL11.glEnd();
      }

      disableGL2D();
      GL11.glDisable(3479);
      GL11.glPopMatrix();
   }

   public static void drawHLine(float x, float y, float x1, int y1) {
      if (y < x) {
         float var5 = x;
         x = y;
         y = var5;
      }

      drawRect((double)x, (double)x1, (double)(y + 1.0F), (double)(x1 + 1.0F), y1);
   }

   public static void drawVLine(float x, float y, float x1, int y1) {
      if (x1 < y) {
         float var5 = y;
         y = x1;
         x1 = var5;
      }

      drawRect((double)x, (double)(y + 1.0F), (double)(x + 1.0F), (double)x1, y1);
   }

   public static void drawHLine(float x, float y, float x1, int y1, int y2) {
      if (y < x) {
         float var5 = x;
         x = y;
         y = var5;
      }

      drawGradientRect(x, x1, y + 1.0F, x1 + 1.0F, y1, y2);
   }

   public static void drawRect(float x, float y, float x1, float y1, float r, float g, float b, float a) {
      enableGL2D();
      GL11.glColor4f(r, g, b, a);
      drawRect(x, y, x1, y1);
      disableGL2D();
   }

   public static void drawRect(float x, float y, float x1, float y1) {
      GL11.glBegin(7);
      GL11.glVertex2f(x, y1);
      GL11.glVertex2f(x1, y1);
      GL11.glVertex2f(x1, y);
      GL11.glVertex2f(x, y);
      GL11.glEnd();
   }

   public static void drawAnimatedUnfilledCircle(float x, float y, float radius, float lineWidth, int color) {
      float alpha = (float)(color >> 24 & 255) / 255.0F;
      float red = (float)(color >> 16 & 255) / 255.0F;
      float green = (float)(color >> 8 & 255) / 255.0F;
      float blue = (float)(color & 255) / 255.0F;
      GL11.glColor4f(red, green, blue, alpha);
      GL11.glLineWidth(lineWidth);
      GL11.glEnable(2848);
      GL11.glBegin(2);
      fade = 1080;

      for(int i = 0; i <= fade; ++i) {
         GL11.glVertex2d((double)x + Math.sin((double)i * 3.141592653589793D / 270.0D) * (double)radius, (double)y + Math.cos((double)i * 3.141592653589793D / 180.0D) * (double)radius);
      }

      GL11.glEnd();
      GL11.glDisable(2848);
   }

   public static void drawCircle(float cx, float cy, float r, int num_segments, int c) {
      r *= 2.0F;
      cx *= 2.0F;
      cy *= 2.0F;
      float f = (float)(c >> 24 & 255) / 255.0F;
      float f2 = (float)(c >> 16 & 255) / 255.0F;
      float f3 = (float)(c >> 8 & 255) / 255.0F;
      float f4 = (float)(c & 255) / 255.0F;
      float theta = (float)(6.2831852D / (double)num_segments);
      float p = (float)Math.cos((double)theta);
      float s = (float)Math.sin((double)theta);
      float x = r;
      float y = 0.0F;
      enableGL2D();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      GL11.glColor4f(f2, f3, f4, f);
      GL11.glBegin(2);

      for(int ii = 0; ii < num_segments; ++ii) {
         GL11.glVertex2f(x + cx, y + cy);
         float t = x;
         x = p * x - s * y;
         y = s * t + p * y;
      }

      GL11.glEnd();
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      disableGL2D();
   }

   public static void drawFullCircle(double cx, int cy, double r, int c) {
      r *= 2.0D;
      cx *= 2.0D;
      cy *= 2;
      float f = (float)(c >> 24 & 255) / 255.0F;
      float f2 = (float)(c >> 16 & 255) / 255.0F;
      float f3 = (float)(c >> 8 & 255) / 255.0F;
      float f4 = (float)(c & 255) / 255.0F;
      enableGL2D();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      GL11.glColor4f(f2, f3, f4, f);
      GL11.glBegin(6);

      for(int i = 0; i <= 360; ++i) {
         double x = Math.sin((double)i * 3.141592653589793D / 180.0D) * r;
         double y = Math.cos((double)i * 3.141592653589793D / 180.0D) * r;
         GL11.glVertex2d(cx + x, (double)cy + y);
      }

      GL11.glEnd();
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      disableGL2D();
   }

   public static void drawSmallString(String s, int x, int y, int color) {
      GL11.glPushMatrix();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      Minecraft.getMinecraft().fontRendererObj.drawString(s, (float)(x * 2), (float)(y * 2), color);
      GL11.glPopMatrix();
   }

   public static void drawLargeString(String text, int x, int y, int color) {
      x *= 2;
      GL11.glPushMatrix();
      GL11.glScalef(1.5F, 1.5F, 1.5F);
      Minecraft.getMinecraft().fontRendererObj.drawString(text, (float)x, (float)y, color);
      GL11.glPopMatrix();
   }

   public static ScaledResolution getScaledResolution() {
      return scaledResolution;
   }

   static {
      RENDER_ITEM = new RenderItem(Minecraft.getMinecraft().renderEngine, Minecraft.getMinecraft().modelManager);
      fade = 0;
      fadeIn = true;
      fadeOut = false;
   }
}
