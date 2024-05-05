package team.massacre.utils;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public final class RenderingUtils {
   private static final double DOUBLE_PI = 6.283185307179586D;
   private static final Frustum FRUSTUM = new Frustum();
   private static int lastScaledWidth;
   private static int lastScaledHeight;
   private static int lastGuiScale;
   private static ScaledResolution scaledResolution;
   private static int lastWidth;
   private static int lastHeight;

   private RenderingUtils() {
   }

   public static boolean isBBInFrustum(AxisAlignedBB aabb) {
      EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
      FRUSTUM.setPosition(player.posX, player.posY, player.posZ);
      return FRUSTUM.isBoundingBoxInFrustum(aabb);
   }

   public static void drawGradientRect(double left, double top, double right, double bottom, boolean sideways, int startColor, int endColor) {
      GL11.glDisable(3553);
      OGLUtils.enableBlending();
      GL11.glShadeModel(7425);
      GL11.glBegin(7);
      OGLUtils.color(startColor);
      if (sideways) {
         GL11.glVertex2d(left, top);
         GL11.glVertex2d(left, bottom);
         OGLUtils.color(endColor);
         GL11.glVertex2d(right, bottom);
         GL11.glVertex2d(right, top);
      } else {
         GL11.glVertex2d(left, top);
         OGLUtils.color(endColor);
         GL11.glVertex2d(left, bottom);
         GL11.glVertex2d(right, bottom);
         OGLUtils.color(startColor);
         GL11.glVertex2d(right, top);
      }

      GL11.glEnd();
      GL11.glDisable(3042);
      GL11.glShadeModel(7424);
      GL11.glEnable(3553);
   }

   public static ScaledResolution getScaledResolution() {
      int displayWidth = Display.getWidth();
      int displayHeight = Display.getHeight();
      int guiScale = Minecraft.getMinecraft().gameSettings.guiScale;
      if (displayWidth == lastScaledWidth && displayHeight == lastScaledHeight && guiScale == lastGuiScale) {
         return scaledResolution;
      } else {
         lastScaledWidth = displayWidth;
         lastScaledHeight = displayHeight;
         lastGuiScale = guiScale;
         return scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
      }
   }

   public static int blendColors(float progress) {
      int[] colors = new int[]{-16711831, -256, -65536, -8388608};
      return blendColors(colors, progress);
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

   public static int getRainbow(long currentMillis, int speed, int offset) {
      return Color.HSBtoRGB(1.0F - (float)((currentMillis + (long)offset) % (long)speed) / (float)speed, 0.9F, 0.9F);
   }

   public static void drawAndRotateArrow(float x, float y, float size, boolean rotate) {
      GL11.glTranslatef(x, y, 0.0F);
      OGLUtils.enableBlending();
      GL11.glEnable(2848);
      GL11.glHint(3154, 4354);
      GL11.glLineWidth(1.0F);
      GL11.glDisable(3553);
      GL11.glBegin(4);
      if (rotate) {
         GL11.glVertex2f(size, size / 2.0F);
         GL11.glVertex2f(size / 2.0F, 0.0F);
         GL11.glVertex2f(0.0F, size / 2.0F);
      } else {
         GL11.glVertex2f(0.0F, 0.0F);
         GL11.glVertex2f(size / 2.0F, size / 2.0F);
         GL11.glVertex2f(size, 0.0F);
      }

      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glTranslatef(-x, -y, 0.0F);
   }

   public static double progressiveAnimation(double now, double desired, double speed) {
      double dif = Math.abs(now - desired);
      int fps = Minecraft.getDebugFPS();
      if (dif > 0.0D) {
         double animationSpeed = MathUtil.round(Math.min(10.0D, Math.max(0.05D, 144.0D / (double)fps * (dif / 10.0D) * speed)), 0.05D);
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

   public static void drawOutlinedString(TTFFontRenderer fr, String s, float x, float y, int color, int outlineColor) {
      fr.drawString(s, x - 0.5F, y, outlineColor);
      fr.drawString(s, x, y - 0.5F, outlineColor);
      fr.drawString(s, x + 0.5F, y, outlineColor);
      fr.drawString(s, x, y + 0.5F, outlineColor);
      fr.drawString(s, x, y, color);
   }

   public static void drawImage(float x, float y, float width, float height, int color) {
      float f = 1.0F / width;
      float f1 = 1.0F / height;
      GL11.glEnable(3042);
      OGLUtils.color(color);
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
      worldrenderer.pos((double)x, (double)(y + height), 0.0D).tex(0.0D, (double)(height * f1)).endVertex();
      worldrenderer.pos((double)(x + width), (double)(y + height), 0.0D).tex((double)(width * f), (double)(height * f1)).endVertex();
      worldrenderer.pos((double)(x + width), (double)y, 0.0D).tex((double)(width * f), 0.0D).endVertex();
      worldrenderer.pos((double)x, (double)y, 0.0D).tex(0.0D, 0.0D).endVertex();
      tessellator.draw();
      GL11.glDisable(3042);
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

   public static void drawLoop(float x, float y, double radius, int points, float width, int color, boolean filled) {
      GL11.glDisable(3553);
      GL11.glLineWidth(width);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      OGLUtils.color(color);
      int smooth = filled ? 2881 : 2848;
      GL11.glEnable(smooth);
      GL11.glHint(filled ? 3155 : 3154, 4354);
      GL11.glBegin(filled ? 6 : 2);

      for(int i = 0; i < points; ++i) {
         if (filled) {
            double cs = (double)i * 3.141592653589793D / 180.0D;
            double ps = (double)(i - 1) * 3.141592653589793D / 180.0D;
            GL11.glVertex2d((double)x + Math.cos(ps) * radius, (double)y + -Math.sin(ps) * radius);
            GL11.glVertex2d((double)x + Math.cos(cs) * radius, (double)y + -Math.sin(cs) * radius);
            GL11.glVertex2d((double)x, (double)y);
         } else {
            GL11.glVertex2d((double)x + radius * Math.cos((double)i * 6.283185307179586D / (double)points), (double)y + radius * Math.sin((double)i * 6.283185307179586D / (double)points));
         }
      }

      GL11.glEnd();
      GL11.glDisable(smooth);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
      GL11.glEnable(3553);
   }

   public static double interpolate(double old, double now, float partialTicks) {
      return old + (now - old) * (double)partialTicks;
   }

   public static float interpolate(float old, float now, float partialTicks) {
      return old + (now - old) * partialTicks;
   }

   public static void drawGuiBackground(int width, int height) {
      Gui.drawRect(0, 0, width, height, -14144460);
   }
}
