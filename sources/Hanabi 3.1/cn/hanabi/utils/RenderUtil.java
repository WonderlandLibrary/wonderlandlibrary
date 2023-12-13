package cn.hanabi.utils;

import cn.hanabi.Wrapper;
import cn.hanabi.injection.interfaces.IEntityRenderer;
import cn.hanabi.injection.interfaces.IRenderManager;
import cn.hanabi.utils.glu.TessCallback;
import cn.hanabi.utils.glu.VertexData;
import com.mojang.authlib.GameProfile;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.GLUtessellator;

public class RenderUtil {
   public static float delta;

   public static double interpolate(double newPos, double oldPos) {
      return oldPos + (newPos - oldPos) * (double)Wrapper.getTimer().renderPartialTicks;
   }

   public static void pre() {
      GL11.glDisable(2929);
      GL11.glDisable(3553);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
   }

   public static void post() {
      GL11.glDisable(3042);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glColor3d(1.0, 1.0, 1.0);
   }

   public static int width() {
      return (new ScaledResolution(Minecraft.getMinecraft())).getScaledWidth();
   }

   public static int height() {
      return (new ScaledResolution(Minecraft.getMinecraft())).getScaledHeight();
   }

   public static int reAlpha(int color, float alpha) {
      Color c = new Color(color);
      float r = 0.003921569F * (float)c.getRed();
      float g = 0.003921569F * (float)c.getGreen();
      float b = 0.003921569F * (float)c.getBlue();
      return (new Color(r, g, b, alpha)).getRGB();
   }

   public static void drawTracerPointer(float x, float y, float size, float widthDiv, float heightDiv, int color) {
      boolean blend = GL11.glIsEnabled(3042);
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glPushMatrix();
      hexColor(color);
      GL11.glBegin(7);
      GL11.glVertex2d((double)x, (double)y);
      GL11.glVertex2d((double)(x - size / widthDiv), (double)(y + size));
      GL11.glVertex2d((double)x, (double)(y + size / heightDiv));
      GL11.glVertex2d((double)(x + size / widthDiv), (double)(y + size));
      GL11.glVertex2d((double)x, (double)y);
      GL11.glEnd();
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.8F);
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      if (!blend) {
         GL11.glDisable(3042);
      }

      GL11.glDisable(2848);
   }

   public static void hexColor(int hexColor) {
      float red = (float)(hexColor >> 16 & 255) / 255.0F;
      float green = (float)(hexColor >> 8 & 255) / 255.0F;
      float blue = (float)(hexColor & 255) / 255.0F;
      float alpha = (float)(hexColor >> 24 & 255) / 255.0F;
      GL11.glColor4f(red, green, blue, alpha);
   }

   public static int createGermanColor() {
      Random random = new Random();
      return Color.getHSBColor(random.nextFloat(), (float)(random.nextInt(2000) + 4000) / 10000.0F, 1.0F).getRGB();
   }

   public static int getRainbow(int speed, int offset, double rainSpeed, double rainOffset, float saturation, float brightness) {
      float hue = (float)(((double)System.currentTimeMillis() * rainSpeed + (double)offset / rainOffset) % (double)speed * 2.0);
      hue /= (float)speed;
      return Color.getHSBColor(hue, saturation, brightness).getRGB();
   }

   public static Color fade(long offset, float fade) {
      float hue = (float)(System.nanoTime() + offset) / 1.0E10F % 1.0F;
      long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0F, 1.0F)), 16);
      Color c = new Color((int)color);
      return new Color((float)c.getRed() / 255.0F * fade, (float)c.getGreen() / 255.0F * fade, (float)c.getBlue() / 255.0F * fade, (float)c.getAlpha() / 155.0F);
   }

   public static Vec3 to2D(double x, double y, double z) {
      FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
      IntBuffer viewport = BufferUtils.createIntBuffer(16);
      FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
      FloatBuffer projection = BufferUtils.createFloatBuffer(16);
      GL11.glGetFloat(2982, modelView);
      GL11.glGetFloat(2983, projection);
      GL11.glGetInteger(2978, viewport);
      boolean result = GLU.gluProject((float)x, (float)y, (float)z, modelView, projection, viewport, screenCoords);
      return result ? new Vec3((double)screenCoords.get(0), (double)((float)Display.getHeight() - screenCoords.get(1)), (double)screenCoords.get(2)) : null;
   }

   public static void prepareScissorBox(float x, float y, float x2, float y2) {
      ScaledResolution scale = new ScaledResolution(Minecraft.getMinecraft());
      int factor = scale.getScaleFactor();
      GL11.glScissor((int)(x * (float)factor), (int)(((float)scale.getScaledHeight() - y2) * (float)factor), (int)((x2 - x) * (float)factor), (int)((y2 - y) * (float)factor));
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

   public static void drawArc(float x1, float y1, double r, int color, int startPoint, double arc, int linewidth) {
      r *= 2.0;
      x1 *= 2.0F;
      y1 *= 2.0F;
      float f = (float)(color >> 24 & 255) / 255.0F;
      float f1 = (float)(color >> 16 & 255) / 255.0F;
      float f2 = (float)(color >> 8 & 255) / 255.0F;
      float f3 = (float)(color & 255) / 255.0F;
      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glDepthMask(true);
      GL11.glEnable(2848);
      GL11.glHint(3154, 4354);
      GL11.glHint(3155, 4354);
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      GL11.glLineWidth((float)linewidth);
      GL11.glEnable(2848);
      GL11.glColor4f(f1, f2, f3, f);
      GL11.glBegin(3);

      for(int i = startPoint; (double)i <= arc; ++i) {
         double x = Math.sin((double)i * Math.PI / 180.0) * r;
         double y = Math.cos((double)i * Math.PI / 180.0) * r;
         GL11.glVertex2d((double)x1 + x, (double)y1 + y);
      }

      GL11.glEnd();
      GL11.glDisable(2848);
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
      GL11.glDisable(2848);
      GL11.glHint(3154, 4352);
      GL11.glHint(3155, 4352);
   }

   public static void drawGradient(double x, double y, double x2, double y2, int col1, int col2) {
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
      GL11.glVertex2d(x2, y);
      GL11.glVertex2d(x, y);
      GL11.glColor4f(f5, f6, f7, f4);
      GL11.glVertex2d(x, y2);
      GL11.glVertex2d(x2, y2);
      GL11.glEnd();
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glShadeModel(7424);
      GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
   }

   public static int rainbow(int delay) {
      double rainbowState = Math.ceil((double)(System.currentTimeMillis() + (long)delay) / 20.0);
      rainbowState %= 360.0;
      return Color.getHSBColor((float)(rainbowState / 360.0), 0.8F, 0.7F).brighter().getRGB();
   }

   public static Color rainbowEffect(int delay) {
      float hue = (float)(System.nanoTime() + (long)delay) / 2.0E10F % 1.0F;
      Color color = new Color((int)Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0F, 1.0F)), 16));
      return new Color((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
   }

   public static void drawFullscreenImage(ResourceLocation image) {
      ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(3008);
      Minecraft.getMinecraft().getTextureManager().bindTexture(image);
      Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0F, 0.0F, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), (float)scaledResolution.getScaledWidth(), (float)scaledResolution.getScaledHeight());
      GL11.glDepthMask(true);
      GL11.glEnable(2929);
      GL11.glEnable(3008);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void drawPlayerHead(String playerName, int x, int y, int width, int height) {
      Iterator var5 = Minecraft.getMinecraft().theWorld.playerEntities.iterator();

      while(var5.hasNext()) {
         EntityPlayer player = (EntityPlayer)var5.next();
         if (playerName.equalsIgnoreCase(player.getName())) {
            GameProfile profile = new GameProfile(player.getUniqueID(), player.getName());
            NetworkPlayerInfo networkplayerinfo1 = new NetworkPlayerInfo(profile);
            new ScaledResolution(Minecraft.getMinecraft());
            GL11.glDisable(2929);
            GL11.glEnable(3042);
            GL11.glDepthMask(false);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().getTextureManager().bindTexture(networkplayerinfo1.getLocationSkin());
            Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, (float)width, (float)height);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GL11.glEnable(2929);
         }
      }

   }

   public static double getAnimationState(double animation, double finalState, double speed) {
      float add = (float)((double)delta * speed);
      if (animation < finalState) {
         if (animation + (double)add < finalState) {
            animation += (double)add;
         } else {
            animation = finalState;
         }
      } else if (animation - (double)add > finalState) {
         animation -= (double)add;
      } else {
         animation = finalState;
      }

      return animation;
   }

   public static double getAnimationState1(double animation, double finalState, double speed) {
      if (animation != finalState && speed != 0.0) {
         double minSpeed = speed / 6.0;
         double maxSpeed = speed * 2.5;
         double sp33d = Math.pow(Math.abs(finalState - animation), 1.2) * 1.8;
         sp33d = Math.max(minSpeed, Math.min(maxSpeed, sp33d));
         float add = (float)((double)delta * sp33d);
         if (animation < finalState) {
            if (animation + (double)add < finalState) {
               animation += (double)add;
            } else {
               animation = finalState;
            }
         } else if (animation - (double)add > finalState) {
            animation -= (double)add;
         } else {
            animation = finalState;
         }

         return animation;
      } else {
         return animation;
      }
   }

   public static double getAnimationStateSmooth(double target, double current, double speed) {
      boolean larger = target > current;
      if (speed < 0.0) {
         speed = 0.0;
      } else if (speed > 1.0) {
         speed = 1.0;
      }

      if (target == current) {
         return target;
      } else {
         double dif = Math.max(target, current) - Math.min(target, current);
         double factor = dif * speed;
         if (factor < 0.1) {
            factor = 0.1;
         }

         if (larger) {
            if (current + factor > target) {
               current = target;
            } else {
               current += factor;
            }
         } else if (current - factor < target) {
            current = target;
         } else {
            current -= factor;
         }

         return current;
      }
   }

   public static void drawLoadingCircle() {
      float status = (float)((double)System.currentTimeMillis() * 0.1 % 400.0);
      float size = 0.5F;
      ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
      float radius = (float)res.getScaledWidth() / 16.0F;
      drawCircle((float)res.getScaledWidth() / 2.0F, (float)res.getScaledHeight() / 2.0F, radius, new Color(FlatColors.DARK_GREY.c), 5.0F, 0.0F, 1.0F);
      drawCircle((float)res.getScaledWidth() / 2.0F, (float)res.getScaledHeight() / 2.0F, radius, new Color(FlatColors.BLUE.c), 7.0F, status, size);
   }

   public static void drawCircle(int xx, int yy, int radius, Color col) {
      int sections = 70;
      double dAngle = 6.283185307179586 / (double)sections;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glShadeModel(7425);
      GL11.glBegin(2);

      for(int i = 0; i < sections; ++i) {
         float x = (float)((double)radius * Math.cos((double)i * dAngle));
         float y = (float)((double)radius * Math.sin((double)i * dAngle));
         GL11.glColor4f((float)col.getRed() / 255.0F, (float)col.getGreen() / 255.0F, (float)col.getBlue() / 255.0F, (float)col.getAlpha() / 255.0F);
         GL11.glVertex2f((float)xx + x, (float)yy + y);
      }

      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   public static void drawCircle(double xx, double yy, double radius, Color col) {
      int sections = 70;
      double dAngle = 6.283185307179586 / (double)sections;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glShadeModel(7425);
      GL11.glBegin(2);

      for(int i = 0; i < sections; ++i) {
         float x = (float)(radius * Math.cos((double)i * dAngle));
         float y = (float)(radius * Math.sin((double)i * dAngle));
         GL11.glColor4f((float)col.getRed() / 255.0F, (float)col.getGreen() / 255.0F, (float)col.getBlue() / 255.0F, (float)col.getAlpha() / 255.0F);
         GL11.glVertex2f((float)xx + x, (float)yy + y);
      }

      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   public static void drawCircle(double xx, double yy, double radius, int color) {
      int sections = 70;
      double dAngle = 6.283185307179586 / (double)sections;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glLineWidth(1.0F);
      GL11.glShadeModel(7425);
      GL11.glBegin(2);

      for(int i = 0; i < sections; ++i) {
         float x = (float)(radius * Math.cos((double)i * dAngle));
         float y = (float)(radius * Math.sin((double)i * dAngle));
         Color col = new Color(color);
         GL11.glColor4f((float)col.getRed() / 255.0F, (float)col.getGreen() / 255.0F, (float)col.getBlue() / 255.0F, (float)col.getAlpha() / 255.0F);
         GL11.glVertex2f((float)(xx + (double)x), (float)(yy + (double)y));
      }

      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   public static void drawCircle(float xx, float yy, float radius, Color col, float width, float position, float round) {
      int sections = 100;
      double dAngle = (double)(round * 2.0F) * Math.PI / (double)sections;
      float x = 0.0F;
      float y = 0.0F;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glLineWidth(width);
      GL11.glShadeModel(7425);
      GL11.glBegin(2);

      int i;
      for(i = (int)position; (float)i < position + (float)sections; ++i) {
         x = (float)((double)radius * Math.cos((double)i * dAngle));
         y = (float)((double)radius * Math.sin((double)i * dAngle));
         GL11.glColor4f((float)col.getRed() / 255.0F, (float)col.getGreen() / 255.0F, (float)col.getBlue() / 255.0F, (float)col.getAlpha() / 255.0F);
         GL11.glVertex2f(xx + x, yy + y);
      }

      for(i = (int)(position + (float)sections); i > (int)position; --i) {
         x = (float)((double)radius * Math.cos((double)i * dAngle));
         y = (float)((double)radius * Math.sin((double)i * dAngle));
         GL11.glColor4f((float)col.getRed() / 255.0F, (float)col.getGreen() / 255.0F, (float)col.getBlue() / 255.0F, (float)col.getAlpha() / 255.0F);
         GL11.glVertex2f(xx + x, yy + y);
      }

      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   public static void drawFilledCircle(int xx, int yy, float radius, Color col) {
      int sections = 100;
      double dAngle = 6.283185307179586 / (double)sections;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glBegin(6);

      for(int i = 0; i < sections; ++i) {
         float x = (float)((double)radius * Math.sin((double)i * dAngle));
         float y = (float)((double)radius * Math.cos((double)i * dAngle));
         GL11.glColor4f((float)col.getRed() / 255.0F, (float)col.getGreen() / 255.0F, (float)col.getBlue() / 255.0F, (float)col.getAlpha() / 255.0F);
         GL11.glVertex2f((float)xx + x, (float)yy + y);
      }

      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   public static void drawFilledCircle(float xx, float yy, float radius, Color col) {
      int sections = 50;
      double dAngle = 6.283185307179586 / (double)sections;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glBegin(6);

      for(int i = 0; i < sections; ++i) {
         float x = (float)((double)radius * Math.sin((double)i * dAngle));
         float y = (float)((double)radius * Math.cos((double)i * dAngle));
         GL11.glColor4f((float)col.getRed() / 255.0F, (float)col.getGreen() / 255.0F, (float)col.getBlue() / 255.0F, (float)col.getAlpha() / 255.0F);
         GL11.glVertex2f(xx + x, yy + y);
      }

      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   public static void drawFilledCircle(int xx, int yy, float radius, int col) {
      float f = (float)(col >> 24 & 255) / 255.0F;
      float f1 = (float)(col >> 16 & 255) / 255.0F;
      float f2 = (float)(col >> 8 & 255) / 255.0F;
      float f3 = (float)(col & 255) / 255.0F;
      int sections = 50;
      double dAngle = 6.283185307179586 / (double)sections;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glBlendFunc(770, 771);
      GL11.glBegin(6);

      for(int i = 0; i < sections; ++i) {
         float x = (float)((double)radius * Math.sin((double)i * dAngle));
         float y = (float)((double)radius * Math.cos((double)i * dAngle));
         GL11.glColor4f(f1, f2, f3, f);
         GL11.glVertex2f((float)xx + x, (float)yy + y);
      }

      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   public static void drawFilledCircle(float xx, float yy, float radius, int col) {
      float f = (float)(col >> 24 & 255) / 255.0F;
      float f1 = (float)(col >> 16 & 255) / 255.0F;
      float f2 = (float)(col >> 8 & 255) / 255.0F;
      float f3 = (float)(col & 255) / 255.0F;
      int sections = 50;
      double dAngle = 6.283185307179586 / (double)sections;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glBegin(6);

      for(int i = 0; i < sections; ++i) {
         float x = (float)((double)radius * Math.sin((double)i * dAngle));
         float y = (float)((double)radius * Math.cos((double)i * dAngle));
         GL11.glColor4f(f1, f2, f3, f);
         GL11.glVertex2f(xx + x, yy + y);
      }

      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   public static void drawFilledCircle(int xx, int yy, float radius, int col, int xLeft, int yAbove, int xRight, int yUnder) {
      float f = (float)(col >> 24 & 255) / 255.0F;
      float f1 = (float)(col >> 16 & 255) / 255.0F;
      float f2 = (float)(col >> 8 & 255) / 255.0F;
      float f3 = (float)(col & 255) / 255.0F;
      int sections = 50;
      double dAngle = 6.283185307179586 / (double)sections;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glBegin(6);

      for(int i = 0; i < sections; ++i) {
         float x = (float)((double)radius * Math.sin((double)i * dAngle));
         float y = (float)((double)radius * Math.cos((double)i * dAngle));
         float xEnd = (float)xx + x;
         float yEnd = (float)yy + y;
         if (xEnd < (float)xLeft) {
            xEnd = (float)xLeft;
         }

         if (xEnd > (float)xRight) {
            xEnd = (float)xRight;
         }

         if (yEnd < (float)yAbove) {
            yEnd = (float)yAbove;
         }

         if (yEnd > (float)yUnder) {
            yEnd = (float)yUnder;
         }

         GL11.glColor4f(f1, f2, f3, f);
         GL11.glVertex2f(xEnd, yEnd);
      }

      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   public static void drawLine(float x, float y, float x2, float y2, Color color) {
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GL11.glEnable(2848);
      GL11.glLineWidth(1.0F);
      GlStateManager.color((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
      worldrenderer.begin(1, DefaultVertexFormats.POSITION);
      worldrenderer.pos((double)x, (double)y, 0.0).endVertex();
      worldrenderer.pos((double)x2, (double)y2, 0.0).endVertex();
      tessellator.draw();
      GL11.glDisable(2848);
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static String getShaderCode(InputStreamReader file) {
      StringBuilder shaderSource = new StringBuilder();

      try {
         BufferedReader reader = new BufferedReader(file);

         String line;
         while((line = reader.readLine()) != null) {
            shaderSource.append(line).append("\n");
         }

         reader.close();
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      return shaderSource.toString();
   }

   public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
      drawImage(image, x, y, width, height, 1.0F);
   }

   public static void drawImage(ResourceLocation image, float x, float y, float width, float height) {
      drawImage(image, (int)x, (int)y, (int)width, (int)height, 1.0F);
   }

   public static void drawImage(ResourceLocation image, int x, int y, int width, int height, float alpha) {
      new ScaledResolution(Minecraft.getMinecraft());
      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glDepthMask(false);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
      Minecraft.getMinecraft().getTextureManager().bindTexture(image);
      Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, (float)width, (float)height);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
   }

   public static void drawOutlinedRect(int x, int y, int width, int height, int lineSize, Color lineColor, Color backgroundColor) {
      drawRect((float)x, (float)y, (float)width, (float)height, backgroundColor.getRGB());
      drawRect((float)x, (float)y, (float)width, (float)(y + lineSize), lineColor.getRGB());
      drawRect((float)x, (float)(height - lineSize), (float)width, (float)height, lineColor.getRGB());
      drawRect((float)x, (float)(y + lineSize), (float)(x + lineSize), (float)(height - lineSize), lineColor.getRGB());
      drawRect((float)(width - lineSize), (float)(y + lineSize), (float)width, (float)(height - lineSize), lineColor.getRGB());
   }

   public static void drawOutlinedRect(float x, float y, float width, float height, float lineSize, int lineColor, int backgroundColor) {
      drawRect(x, y, width, height, backgroundColor);
      drawRect(x, y, width, y + lineSize, lineColor);
      drawRect(x, height - lineSize, width, height, lineColor);
      drawRect(x, y + lineSize, x + lineSize, height - lineSize, lineColor);
      drawRect(width - lineSize, y + lineSize, width, height - lineSize, lineColor);
   }

   public static void drawImage(ResourceLocation image, int x, int y, int width, int height, Color color) {
      new ScaledResolution(Minecraft.getMinecraft());
      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glDepthMask(false);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getRed() / 255.0F, 1.0F);
      Minecraft.getMinecraft().getTextureManager().bindTexture(image);
      Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, (float)width, (float)height);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
   }

   public static void doGlScissor(int x, int y, int width, int height) {
      Minecraft mc = Minecraft.getMinecraft();
      int scaleFactor = 1;
      int k = mc.gameSettings.guiScale;
      if (k == 0) {
         k = 1000;
      }

      while(scaleFactor < k && mc.displayWidth / (scaleFactor + 1) >= 320 && mc.displayHeight / (scaleFactor + 1) >= 240) {
         ++scaleFactor;
      }

      GL11.glScissor(x * scaleFactor, mc.displayHeight - (y + height) * scaleFactor, width * scaleFactor, height * scaleFactor);
   }

   public static void drawRect(float x1, float y1, float x2, float y2, int color) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glPushMatrix();
      color(color);
      GL11.glBegin(7);
      GL11.glVertex2d((double)x2, (double)y1);
      GL11.glVertex2d((double)x1, (double)y1);
      GL11.glVertex2d((double)x1, (double)y2);
      GL11.glVertex2d((double)x2, (double)y2);
      GL11.glEnd();
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
      Gui.drawRect(0, 0, 0, 0, 0);
   }

   public static void color(int color) {
      float f = (float)(color >> 24 & 255) / 255.0F;
      float f1 = (float)(color >> 16 & 255) / 255.0F;
      float f2 = (float)(color >> 8 & 255) / 255.0F;
      float f3 = (float)(color & 255) / 255.0F;
      GL11.glColor4f(f1, f2, f3, f);
   }

   public static int createShader(String shaderCode, int shaderType) throws Exception {
      int shader = 0;

      try {
         shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
         if (shader == 0) {
            return 0;
         }
      } catch (Exception var4) {
         ARBShaderObjects.glDeleteObjectARB(shader);
         throw var4;
      }

      ARBShaderObjects.glShaderSourceARB(shader, shaderCode);
      ARBShaderObjects.glCompileShaderARB(shader);
      if (ARBShaderObjects.glGetObjectParameteriARB(shader, 35713) == 0) {
         throw new RuntimeException("Error creating shader:");
      } else {
         return shader;
      }
   }

   public static void outlineOne() {
      GL11.glPushAttrib(1048575);
      GL11.glDisable(3008);
      GL11.glDisable(3553);
      GL11.glDisable(2896);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glLineWidth(4.0F);
      GL11.glEnable(2848);
      GL11.glEnable(2960);
      GL11.glClear(1024);
      GL11.glClearStencil(15);
      GL11.glStencilFunc(512, 1, 15);
      GL11.glStencilOp(7681, 7681, 7681);
      GL11.glPolygonMode(1032, 6913);
   }

   public static void outlineTwo() {
      GL11.glStencilFunc(512, 0, 15);
      GL11.glStencilOp(7681, 7681, 7681);
      GL11.glPolygonMode(1032, 6914);
   }

   public static void outlineThree() {
      GL11.glStencilFunc(514, 1, 15);
      GL11.glStencilOp(7680, 7680, 7680);
      GL11.glPolygonMode(1032, 6913);
   }

   public static void outlineFour() {
      GL11.glDepthMask(false);
      GL11.glDisable(2929);
      GL11.glEnable(10754);
      GL11.glPolygonOffset(1.0F, -2000000.0F);
      GL11.glColor4f(0.9529412F, 0.6117647F, 0.07058824F, 1.0F);
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
   }

   public static void outlineFive() {
      GL11.glPolygonOffset(1.0F, 2000000.0F);
      GL11.glDisable(10754);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(2960);
      GL11.glDisable(2848);
      GL11.glHint(3154, 4352);
      GL11.glEnable(3042);
      GL11.glEnable(2896);
      GL11.glEnable(3553);
      GL11.glEnable(3008);
      GL11.glPopAttrib();
   }

   public static void drawOutlinedBlockESP(double x, double y, double z, float red, float green, float blue, float alpha, float lineWidth) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glLineWidth(lineWidth);
      GL11.glColor4f(red, green, blue, alpha);
      drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldRenderer = tessellator.getWorldRenderer();
      worldRenderer.begin(3, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      tessellator.draw();
      worldRenderer.begin(3, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      tessellator.draw();
      worldRenderer.begin(1, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      tessellator.draw();
   }

   public static void drawBoundingBox(AxisAlignedBB aa) {
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldRenderer = tessellator.getWorldRenderer();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      tessellator.draw();
   }

   public static void drawBlockESP(double x, double y, double z, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWidth) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(2896);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glColor4f(red, green, blue, alpha);
      drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
      GL11.glLineWidth(lineWidth);
      GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
      drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2896);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawSolidBlockESP(double x, double y, double z, float red, float green, float blue, float alpha) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glColor4f(red, green, blue, alpha);
      drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawBlockESP(double x, double y, double z, int maincoolor, int borderColor, float alpha, float lineWidth) {
      float red = (float)(maincoolor >> 16 & 255) / 255.0F;
      float green = (float)(maincoolor >> 8 & 255) / 255.0F;
      float blue = (float)(maincoolor & 255) / 255.0F;
      float lineRed = (float)(borderColor >> 16 & 255) / 255.0F;
      float lineGreen = (float)(borderColor >> 8 & 255) / 255.0F;
      float lineBlue = (float)(borderColor & 255) / 255.0F;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glColor4f(red, green, blue, alpha);
      drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
      GL11.glLineWidth(lineWidth);
      GL11.glColor4f(lineRed, lineGreen, lineBlue, alpha);
      drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawCircle(double radius, double x, double lastTickX, double y, double lastTickY, double z, double lastTickZ, Color c) {
      float pTicks = Wrapper.getTimer().renderPartialTicks;
      double posX = lastTickX + (x - lastTickX) * (double)pTicks - ((IRenderManager)Minecraft.getMinecraft().getRenderManager()).getRenderPosX();
      double posY = lastTickY + (y - lastTickY) * (double)pTicks - ((IRenderManager)Minecraft.getMinecraft().getRenderManager()).getRenderPosY();
      double posZ = lastTickZ + (z - lastTickZ) * (double)pTicks - ((IRenderManager)Minecraft.getMinecraft().getRenderManager()).getRenderPosZ();
      GlStateManager.pushMatrix();
      GlStateManager.disableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer wr = tessellator.getWorldRenderer();
      wr.begin(2, DefaultVertexFormats.POSITION_COLOR);

      for(int a = 0; a < 360; ++a) {
         double rad = Math.toRadians((double)a);
         wr.pos(posX + Math.cos(rad) * radius, posY + 0.20000000298023224, posZ + Math.sin(rad) * radius).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
      }

      tessellator.draw();
      GlStateManager.disableBlend();
      GlStateManager.enableTexture2D();
      GlStateManager.popMatrix();
   }

   public static void drawOutlinedEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glColor4f(red, green, blue, alpha);
      drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawSolidEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glColor4f(red, green, blue, alpha);
      drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawEntityESP(double x, double y, double z, double width, double height, int color, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      color(color);
      drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
      GL11.glLineWidth(lineWdith);
      GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
      drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawTracerLine(double x, double y, double z, float red, float green, float blue, float alpha, float lineWdith) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(3042);
      GL11.glLineWidth(lineWdith);
      GL11.glColor4f(red, green, blue, alpha);
      GL11.glBegin(2);
      GL11.glVertex3d(0.0, 0.0 + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0);
      GL11.glVertex3d(x, y, z);
      GL11.glEnd();
      GL11.glDisable(3042);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDisable(2848);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void checkSetupFBO() {
      Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
      if (fbo != null && fbo.depthBuffer > -1) {
         EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
         int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
         EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
         EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
         EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
         EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
         fbo.depthBuffer = -1;
      }

   }

   public static void drawFilledBox(AxisAlignedBB mask) {
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldRenderer = tessellator.getWorldRenderer();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(mask.minX, mask.minY, mask.minZ).endVertex();
      worldRenderer.pos(mask.minX, mask.maxY, mask.minZ);
      worldRenderer.pos(mask.maxX, mask.minY, mask.minZ);
      worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ);
      worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ);
      worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ);
      worldRenderer.pos(mask.minX, mask.minY, mask.maxZ);
      worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ);
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ);
      worldRenderer.pos(mask.maxX, mask.minY, mask.minZ);
      worldRenderer.pos(mask.minX, mask.maxY, mask.minZ);
      worldRenderer.pos(mask.minX, mask.minY, mask.minZ);
      worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ);
      worldRenderer.pos(mask.minX, mask.minY, mask.maxZ);
      worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ);
      worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ);
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(mask.minX, mask.maxY, mask.minZ);
      worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ);
      worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ);
      worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ);
      worldRenderer.pos(mask.minX, mask.maxY, mask.minZ);
      worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ);
      worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ);
      worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ);
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(mask.minX, mask.minY, mask.minZ);
      worldRenderer.pos(mask.maxX, mask.minY, mask.minZ);
      worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ);
      worldRenderer.pos(mask.minX, mask.minY, mask.maxZ);
      worldRenderer.pos(mask.minX, mask.minY, mask.minZ);
      worldRenderer.pos(mask.minX, mask.minY, mask.maxZ);
      worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ);
      worldRenderer.pos(mask.maxX, mask.minY, mask.minZ);
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(mask.minX, mask.minY, mask.minZ);
      worldRenderer.pos(mask.minX, mask.maxY, mask.minZ);
      worldRenderer.pos(mask.minX, mask.minY, mask.maxZ);
      worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ);
      worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ);
      worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ);
      worldRenderer.pos(mask.maxX, mask.minY, mask.minZ);
      worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ);
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ);
      worldRenderer.pos(mask.minX, mask.minY, mask.maxZ);
      worldRenderer.pos(mask.minX, mask.maxY, mask.minZ);
      worldRenderer.pos(mask.minX, mask.minY, mask.minZ);
      worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ);
      worldRenderer.pos(mask.maxX, mask.minY, mask.minZ);
      worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ);
      worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ);
      tessellator.draw();
   }

   public static void drawRoundedRect(float x, float y, float x2, float y2, float round, int color) {
      x = (float)((double)x + (double)(round / 2.0F) + 0.5);
      y = (float)((double)y + (double)(round / 2.0F) + 0.5);
      x2 = (float)((double)x2 - ((double)(round / 2.0F) + 0.5));
      y2 = (float)((double)y2 - ((double)(round / 2.0F) + 0.5));
      drawRect(x, y, x2, y2, color);
      circle(x2 - round / 2.0F, y + round / 2.0F, round, color);
      circle(x + round / 2.0F, y2 - round / 2.0F, round, color);
      circle(x + round / 2.0F, y + round / 2.0F, round, color);
      circle(x2 - round / 2.0F, y2 - round / 2.0F, round, color);
      drawRect(x - round / 2.0F - 0.5F, y + round / 2.0F, x2, y2 - round / 2.0F, color);
      drawRect(x, y + round / 2.0F, x2 + round / 2.0F + 0.5F, y2 - round / 2.0F, color);
      drawRect(x + round / 2.0F, y - round / 2.0F - 0.5F, x2 - round / 2.0F, y2 - round / 2.0F, color);
      drawRect(x + round / 2.0F, y, x2 - round / 2.0F, y2 + round / 2.0F + 0.5F, color);
   }

   public static void drawRoundedRect2(float n, float n2, float n3, float n4, int n5, int n6) {
      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glDepthMask(true);
      GL11.glEnable(2848);
      GL11.glHint(3154, 4354);
      GL11.glHint(3155, 4354);
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      drawVLine(n *= 2.0F, (n2 *= 2.0F) + 1.0F, (n4 *= 2.0F) - 2.0F, n5);
      drawVLine((n3 *= 2.0F) - 1.0F, n2 + 1.0F, n4 - 2.0F, n5);
      drawHLine(n + 2.0F, n3 - 3.0F, n2, n5);
      drawHLine(n + 2.0F, n3 - 3.0F, n4 - 1.0F, n5);
      drawHLine(n + 1.0F, n + 1.0F, n2 + 1.0F, n5);
      drawHLine(n3 - 2.0F, n3 - 2.0F, n2 + 1.0F, n5);
      drawHLine(n3 - 2.0F, n3 - 2.0F, n4 - 2.0F, n5);
      drawHLine(n + 1.0F, n + 1.0F, n4 - 2.0F, n5);
      drawRect(n + 1.0F, n2 + 1.0F, n3 - 1.0F, n4 - 1.0F, n6);
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
      GL11.glDisable(2848);
      GL11.glHint(3154, 4352);
      GL11.glHint(3155, 4352);
   }

   public static void drawHLine(float x, float y, float x1, int y1) {
      if (y < x) {
         float i = x;
         x = y;
         y = i;
      }

      drawRect(x, x1, y + 1.0F, x1 + 1.0F, y1);
   }

   public static void drawVLine(float x, float y, float x1, int y1) {
      if (x1 < y) {
         float i = y;
         y = x1;
         x1 = i;
      }

      drawRect(x, y + 1.0F, x + 1.0F, x1, y1);
   }

   public static void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
      rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      rectangle(x + width, y, x1 - width, y + width, borderColor);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      rectangle(x, y, x + width, y1, borderColor);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      rectangle(x1 - width, y, x1, y1, borderColor);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void drawCircle(float x, float y, float radius, int startPi, int endPi, int c) {
      float f = (float)(c >> 24 & 255) / 255.0F;
      float f1 = (float)(c >> 16 & 255) / 255.0F;
      float f2 = (float)(c >> 8 & 255) / 255.0F;
      float f3 = (float)(c & 255) / 255.0F;
      GL11.glColor4f(f1, f2, f3, f);
      GlStateManager.enableAlpha();
      GlStateManager.enableBlend();
      GL11.glDisable(3553);
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.alphaFunc(516, 0.001F);
      Tessellator tess = Tessellator.getInstance();
      WorldRenderer render = tess.getWorldRenderer();

      for(double i = (double)startPi; i < (double)endPi; ++i) {
         double cs = i * Math.PI / 180.0;
         double ps = (i - 1.0) * Math.PI / 180.0;
         double[] outer = new double[]{Math.cos(cs) * (double)radius, -Math.sin(cs) * (double)radius, Math.cos(ps) * (double)radius, -Math.sin(ps) * (double)radius};
         render.begin(6, DefaultVertexFormats.POSITION);
         render.pos((double)x + outer[2], (double)y + outer[3], 0.0).endVertex();
         render.pos((double)x + outer[0], (double)y + outer[1], 0.0).endVertex();
         render.pos((double)x, (double)y, 0.0).endVertex();
         tess.draw();
      }

      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GlStateManager.disableBlend();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.disableAlpha();
      GL11.glEnable(3553);
   }

   public static void rectangle(double left, double top, double right, double bottom, int color) {
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
      WorldRenderer worldRenderer = tessellator.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(var6, var7, var8, var11);
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(left, bottom, 0.0).endVertex();
      worldRenderer.pos(right, bottom, 0.0).endVertex();
      worldRenderer.pos(right, top, 0.0).endVertex();
      worldRenderer.pos(left, top, 0.0).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void arcEllipse(float x, float y, float start, float end, float w, float h, int color) {
      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
      float temp = 0.0F;
      if (start > end) {
         temp = end;
         end = start;
         start = temp;
      }

      float var11 = (float)(color >> 24 & 255) / 255.0F;
      float var6 = (float)(color >> 16 & 255) / 255.0F;
      float var7 = (float)(color >> 8 & 255) / 255.0F;
      float var8 = (float)(color & 255) / 255.0F;
      Tessellator var9 = Tessellator.getInstance();
      WorldRenderer var10 = var9.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(var6, var7, var8, var11);
      float ldy;
      float ldx;
      float i;
      if (var11 > 0.5F) {
         GL11.glEnable(2848);
         GL11.glLineWidth(2.0F);
         GL11.glBegin(3);

         for(i = end; i >= start; i -= 4.0F) {
            ldx = (float)Math.cos((double)i * Math.PI / 180.0) * w * 1.001F;
            ldy = (float)Math.sin((double)i * Math.PI / 180.0) * h * 1.001F;
            GL11.glVertex2f(x + ldx, y + ldy);
         }

         GL11.glEnd();
         GL11.glDisable(2848);
      }

      GL11.glBegin(6);

      for(i = end; i >= start; i -= 4.0F) {
         ldx = (float)Math.cos((double)i * Math.PI / 180.0) * w;
         ldy = (float)Math.sin((double)i * Math.PI / 180.0) * h;
         GL11.glVertex2f(x + ldx, y + ldy);
      }

      GL11.glEnd();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void circle(float x, float y, float radius, int fill) {
      GL11.glEnable(3042);
      arc(x, y, 0.0F, 360.0F, radius, fill);
      GL11.glDisable(3042);
   }

   public static void circle(float x, float y, float radius, Color fill) {
      arc(x, y, 0.0F, 360.0F, radius, fill);
   }

   public static void arc(float x, float y, float start, float end, float radius, int color) {
      arcEllipse(x, y, start, end, radius, radius, color);
   }

   public static void arc(float x, float y, float start, float end, float radius, Color color) {
      arcEllipse(x, y, start, end, radius, radius, color);
   }

   public static void arcEllipse(float x, float y, float start, float end, float w, float h, Color color) {
      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
      float temp = 0.0F;
      if (start > end) {
         temp = end;
         end = start;
         start = temp;
      }

      Tessellator var9 = Tessellator.getInstance();
      WorldRenderer var10 = var9.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
      float ldy;
      float ldx;
      float i;
      if ((float)color.getAlpha() > 0.5F) {
         GL11.glEnable(2848);
         GL11.glLineWidth(2.0F);
         GL11.glBegin(3);

         for(i = end; i >= start; i -= 4.0F) {
            ldx = (float)Math.cos((double)i * Math.PI / 180.0) * w * 1.001F;
            ldy = (float)Math.sin((double)i * Math.PI / 180.0) * h * 1.001F;
            GL11.glVertex2f(x + ldx, y + ldy);
         }

         GL11.glEnd();
         GL11.glDisable(2848);
      }

      GL11.glBegin(6);

      for(i = end; i >= start; i -= 4.0F) {
         ldx = (float)Math.cos((double)i * Math.PI / 180.0) * w;
         ldy = (float)Math.sin((double)i * Math.PI / 180.0) * h;
         GL11.glVertex2f(x + ldx, y + ldy);
      }

      GL11.glEnd();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void startDrawing() {
      GL11.glEnable(3042);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glDisable(3553);
      GL11.glDisable(2929);
      ((IEntityRenderer)Minecraft.getMinecraft().entityRenderer).runSetupCameraTransform(Wrapper.getTimer().renderPartialTicks, 0);
   }

   public static void stopDrawing() {
      GL11.glDisable(3042);
      GL11.glEnable(3553);
      GL11.glDisable(2848);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
   }

   public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, int col1, int col2) {
      drawRect(x, y, x2, y2, col2);
      float f = (float)(col1 >> 24 & 255) / 255.0F;
      float f2 = (float)(col1 >> 16 & 255) / 255.0F;
      float f3 = (float)(col1 >> 8 & 255) / 255.0F;
      float f4 = (float)(col1 & 255) / 255.0F;
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glPushMatrix();
      GL11.glColor4f(f2, f3, f4, f);
      GL11.glLineWidth(l1);
      GL11.glBegin(1);
      GL11.glVertex2d((double)x, (double)y);
      GL11.glVertex2d((double)x, (double)y2);
      GL11.glVertex2d((double)x2, (double)y2);
      GL11.glVertex2d((double)x2, (double)y);
      GL11.glVertex2d((double)x, (double)y);
      GL11.glVertex2d((double)x2, (double)y);
      GL11.glVertex2d((double)x, (double)y2);
      GL11.glVertex2d((double)x2, (double)y2);
      GL11.glEnd();
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
   }

   public static int withTransparency(int rgb, float alpha) {
      float r = (float)(rgb >> 16 & 255) / 255.0F;
      float g = (float)(rgb >> 8 & 255) / 255.0F;
      float b = (float)(rgb & 255) / 255.0F;
      return (new Color(r, g, b, alpha)).getRGB();
   }

   public static Color blend(Color color1, Color color2, double ratio) {
      float r = (float)ratio;
      float ir = 1.0F - r;
      float[] rgb1 = new float[3];
      float[] rgb2 = new float[3];
      color1.getColorComponents(rgb1);
      color2.getColorComponents(rgb2);
      return new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r + rgb2[1] * ir, rgb1[2] * r + rgb2[2] * ir);
   }

   public static void drawFastRoundedRect(float x0, float y0, float x1, float y1, float radius, int color) {
      int Semicircle = true;
      float f = 5.0F;
      float f2 = (float)(color >> 24 & 255) / 255.0F;
      float f3 = (float)(color >> 16 & 255) / 255.0F;
      float f4 = (float)(color >> 8 & 255) / 255.0F;
      float f5 = (float)(color & 255) / 255.0F;
      GL11.glDisable(2884);
      GL11.glDisable(3553);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      GL11.glColor4f(f3, f4, f5, f2);
      GL11.glBegin(5);
      GL11.glVertex2f(x0 + radius, y0);
      GL11.glVertex2f(x0 + radius, y1);
      GL11.glVertex2f(x1 - radius, y0);
      GL11.glVertex2f(x1 - radius, y1);
      GL11.glEnd();
      GL11.glBegin(5);
      GL11.glVertex2f(x0, y0 + radius);
      GL11.glVertex2f(x0 + radius, y0 + radius);
      GL11.glVertex2f(x0, y1 - radius);
      GL11.glVertex2f(x0 + radius, y1 - radius);
      GL11.glEnd();
      GL11.glBegin(5);
      GL11.glVertex2f(x1, y0 + radius);
      GL11.glVertex2f(x1 - radius, y0 + radius);
      GL11.glVertex2f(x1, y1 - radius);
      GL11.glVertex2f(x1 - radius, y1 - radius);
      GL11.glEnd();
      GL11.glBegin(6);
      float f6 = x1 - radius;
      float f7 = y0 + radius;
      GL11.glVertex2f(f6, f7);
      int j = false;

      float f11;
      int j;
      for(j = 0; j <= 18; ++j) {
         f11 = (float)j * 5.0F;
         GL11.glVertex2f((float)((double)f6 + (double)radius * Math.cos(Math.toRadians((double)f11))), (float)((double)f7 - (double)radius * Math.sin(Math.toRadians((double)f11))));
      }

      GL11.glEnd();
      GL11.glBegin(6);
      f6 = x0 + radius;
      f7 = y0 + radius;
      GL11.glVertex2f(f6, f7);

      for(j = 0; j <= 18; ++j) {
         f11 = (float)j * 5.0F;
         GL11.glVertex2f((float)((double)f6 - (double)radius * Math.cos(Math.toRadians((double)f11))), (float)((double)f7 - (double)radius * Math.sin(Math.toRadians((double)f11))));
      }

      GL11.glEnd();
      GL11.glBegin(6);
      f6 = x0 + radius;
      f7 = y1 - radius;
      GL11.glVertex2f(f6, f7);

      for(j = 0; j <= 18; ++j) {
         f11 = (float)j * 5.0F;
         GL11.glVertex2f((float)((double)f6 - (double)radius * Math.cos(Math.toRadians((double)f11))), (float)((double)f7 + (double)radius * Math.sin(Math.toRadians((double)f11))));
      }

      GL11.glEnd();
      GL11.glBegin(6);
      f6 = x1 - radius;
      f7 = y1 - radius;
      GL11.glVertex2f(f6, f7);

      for(j = 0; j <= 18; ++j) {
         f11 = (float)j * 5.0F;
         GL11.glVertex2f((float)((double)f6 + (double)radius * Math.cos(Math.toRadians((double)f11))), (float)((double)f7 + (double)radius * Math.sin(Math.toRadians((double)f11))));
      }

      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glEnable(2884);
      GL11.glDisable(3042);
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
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
      GL11.glDisable(2848);
      GL11.glShadeModel(7424);
      GL11.glColor4d(255.0, 255.0, 255.0, 255.0);
   }

   public static void drawRoundRect2(float x, float y, float x2, float y2, float round, int color) {
      x += (float)((double)(round / 2.0F) + 0.5);
      y += (float)((double)(round / 2.0F) + 0.5);
      x2 -= (float)((double)(round / 2.0F) + 0.5);
      y2 -= (float)((double)(round / 2.0F) + 0.5);
      Gui.drawRect((int)x, (int)y, (int)x2, (int)y2, color);
      circle(x2 - round / 2.0F, y + round / 2.0F, round, color);
      circle(x + round / 2.0F, y2 - round / 2.0F, round, color);
      circle(x + round / 2.0F, y + round / 2.0F, round, color);
      circle(x2 - round / 2.0F, y2 - round / 2.0F, round, color);
      Gui.drawRect((int)(x - round / 2.0F - 0.5F), (int)(y + round / 2.0F), (int)x2, (int)(y2 - round / 2.0F), color);
      Gui.drawRect((int)x, (int)(y + round / 2.0F), (int)(x2 + round / 2.0F + 0.5F), (int)(y2 - round / 2.0F), color);
      Gui.drawRect((int)(x + round / 2.0F), (int)(y - round / 2.0F - 0.5F), (int)(x2 - round / 2.0F), (int)(y2 - round / 2.0F), color);
      Gui.drawRect((int)(x + round / 2.0F), (int)y, (int)(x2 - round / 2.0F), (int)(y2 + round / 2.0F + 0.5F), color);
      Gui.drawRect(0, 0, 0, 0, 0);
   }

   public static void drawVerticalGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
      float f = (float)(col1 >> 24 & 255) / 255.0F;
      float f1 = (float)(col1 >> 16 & 255) / 255.0F;
      float f2 = (float)(col1 >> 8 & 255) / 255.0F;
      float f3 = (float)(col1 & 255) / 255.0F;
      float f4 = (float)(col2 >> 24 & 255) / 255.0F;
      float f5 = (float)(col2 >> 16 & 255) / 255.0F;
      float f6 = (float)(col2 >> 8 & 255) / 255.0F;
      float f7 = (float)(col2 & 255) / 255.0F;
      GlStateManager.disableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.shadeModel(7425);
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer world = tessellator.getWorldRenderer();
      world.begin(7, DefaultVertexFormats.POSITION_COLOR);
      world.pos(right, top, 0.0).color(f1, f2, f3, f).endVertex();
      world.pos(left, top, 0.0).color(f1, f2, f3, f).endVertex();
      world.pos(left, bottom, 0.0).color(f5, f6, f7, f4).endVertex();
      world.pos(right, bottom, 0.0).color(f5, f6, f7, f4).endVertex();
      tessellator.draw();
      GlStateManager.shadeModel(7424);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.enableTexture2D();
      Gui.drawRect(0, 0, 0, 0, 0);
   }

   public static void drawHorizontalGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
      float f = (float)(col1 >> 24 & 255) / 255.0F;
      float f1 = (float)(col1 >> 16 & 255) / 255.0F;
      float f2 = (float)(col1 >> 8 & 255) / 255.0F;
      float f3 = (float)(col1 & 255) / 255.0F;
      float f4 = (float)(col2 >> 24 & 255) / 255.0F;
      float f5 = (float)(col2 >> 16 & 255) / 255.0F;
      float f6 = (float)(col2 >> 8 & 255) / 255.0F;
      float f7 = (float)(col2 & 255) / 255.0F;
      GlStateManager.disableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.shadeModel(7425);
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer world = tessellator.getWorldRenderer();
      world.begin(7, DefaultVertexFormats.POSITION_COLOR);
      world.pos(left, top, 0.0).color(f1, f2, f3, f).endVertex();
      world.pos(left, bottom, 0.0).color(f1, f2, f3, f).endVertex();
      world.pos(right, bottom, 0.0).color(f5, f6, f7, f4).endVertex();
      world.pos(right, top, 0.0).color(f5, f6, f7, f4).endVertex();
      tessellator.draw();
      GlStateManager.shadeModel(7424);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.enableTexture2D();
      Gui.drawRect(0, 0, 0, 0, 0);
   }

   public static void drawFilledCircle(double x, double y, double r, int c, int id) {
      float f = (float)(c >> 24 & 255) / 255.0F;
      float f1 = (float)(c >> 16 & 255) / 255.0F;
      float f2 = (float)(c >> 8 & 255) / 255.0F;
      float f3 = (float)(c & 255) / 255.0F;
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glColor4f(f1, f2, f3, f);
      GL11.glBegin(9);
      int i;
      double x2;
      double y2;
      if (id == 1) {
         GL11.glVertex2d(x, y);

         for(i = 0; i <= 90; ++i) {
            x2 = Math.sin((double)i * Math.PI / 180.0) * r;
            y2 = Math.cos((double)i * Math.PI / 180.0) * r;
            GL11.glVertex2d(x - x2, y - y2);
         }
      } else if (id == 2) {
         GL11.glVertex2d(x, y);

         for(i = 90; i <= 180; ++i) {
            x2 = Math.sin((double)i * Math.PI / 180.0) * r;
            y2 = Math.cos((double)i * Math.PI / 180.0) * r;
            GL11.glVertex2d(x - x2, y - y2);
         }
      } else if (id == 3) {
         GL11.glVertex2d(x, y);

         for(i = 270; i <= 360; ++i) {
            x2 = Math.sin((double)i * Math.PI / 180.0) * r;
            y2 = Math.cos((double)i * Math.PI / 180.0) * r;
            GL11.glVertex2d(x - x2, y - y2);
         }
      } else if (id == 4) {
         GL11.glVertex2d(x, y);

         for(i = 180; i <= 270; ++i) {
            x2 = Math.sin((double)i * Math.PI / 180.0) * r;
            y2 = Math.cos((double)i * Math.PI / 180.0) * r;
            GL11.glVertex2d(x - x2, y - y2);
         }
      } else {
         for(i = 0; i <= 360; ++i) {
            x2 = Math.sin((double)i * Math.PI / 180.0) * r;
            y2 = Math.cos((double)i * Math.PI / 180.0) * r;
            GL11.glVertex2f((float)(x - x2), (float)(y - y2));
         }
      }

      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      Gui.drawRect(0, 0, 0, 0, 0);
   }

   public static void drawRoundRect(double xPosition, double yPosition, double endX, double endY, int radius, int color) {
      double width = endX - xPosition;
      double height = endY - yPosition;
      Gui.drawRect((int)xPosition + radius, (int)yPosition + radius, (int)(xPosition + width - (double)radius), (int)(yPosition + height - (double)radius), color);
      Gui.drawRect((int)xPosition, (int)yPosition + radius, (int)xPosition + radius, (int)(yPosition + height - (double)radius), color);
      Gui.drawRect((int)(xPosition + width - (double)radius), (int)yPosition + radius, (int)(xPosition + width), (int)(yPosition + height - (double)radius), color);
      Gui.drawRect((int)xPosition + radius, (int)yPosition, (int)(xPosition + width - (double)radius), (int)yPosition + radius, color);
      Gui.drawRect((int)xPosition + radius, (int)(yPosition + height - (double)radius), (int)(xPosition + width - (double)radius), (int)(yPosition + height), color);
      drawFilledCircle(xPosition + (double)radius, yPosition + (double)radius, (double)radius, color, 1);
      drawFilledCircle(xPosition + (double)radius, (double)((int)(yPosition + height - (double)radius)), (double)radius, color, 2);
      drawFilledCircle((double)((int)(xPosition + width - (double)radius)), yPosition + (double)radius, (double)radius, color, 3);
      drawFilledCircle((double)((int)(xPosition + width - (double)radius)), (double)((int)(yPosition + height - (double)radius)), (double)radius, color, 4);
      Gui.drawRect(0, 0, 0, 0, 0);
   }

   public static void startGlScissor(int x, int y, int width, int height) {
      int scaleFactor = 1;
      int k = Minecraft.getMinecraft().gameSettings.guiScale;
      if (k == 0) {
         k = 1000;
      }

      while(scaleFactor < k && Minecraft.getMinecraft().displayWidth / (scaleFactor + 1) >= 320 && Minecraft.getMinecraft().displayHeight / (scaleFactor + 1) >= 240) {
         ++scaleFactor;
      }

      GlStateManager.pushMatrix();
      GL11.glEnable(3089);
      GL11.glScissor(x * scaleFactor, Minecraft.getMinecraft().displayHeight - (y + height) * scaleFactor, width * scaleFactor, height * scaleFactor);
      Gui.drawRect(0, 0, 0, 0, 0);
   }

   public static void stopGlScissor() {
      GL11.glDisable(3089);
      GlStateManager.popMatrix();
      Gui.drawRect(0, 0, 0, 0, 0);
   }

   public static void drawOutlinedRect(float x, float y, float width, float height, float lineSize, int lineColor) {
      drawRect(x, y, width, y + lineSize, lineColor);
      drawRect(x, height - lineSize, width, height, lineColor);
      drawRect(x, y + lineSize, x + lineSize, height - lineSize, lineColor);
      drawRect(width - lineSize, y + lineSize, width, height - lineSize, lineColor);
   }

   public static float smoothAnimation(float ani, float finalState, float speed, float scale) {
      return getAnimationState(ani, finalState, Math.max(10.0F, Math.abs(ani - finalState) * speed) * scale);
   }

   public static void drawLine(int color, double x, double y, double z) {
      float red = (float)(color >> 16 & 255) / 255.0F;
      float green = (float)(color >> 8 & 255) / 255.0F;
      float blue = (float)(color & 255) / 255.0F;
      pre();
      GL11.glColor4f(red, green, blue, 0.5F);
      GL11.glLineWidth(0.12F);
      GL11.glBegin(1);
      GL11.glVertex3d(0.0, (double)Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0);
      GL11.glVertex3d(x + 0.5, y + 0.5, z + 0.5);
      GL11.glEnd();
      post();
   }

   public static float getAnimationState(float animation, float finalState, float speed) {
      float add = delta * speed;
      if (animation < finalState) {
         if (animation + add < finalState) {
            animation += add;
         } else {
            animation = finalState;
         }
      } else if (animation - add > finalState) {
         animation -= add;
      } else {
         animation = finalState;
      }

      return animation;
   }

   public static void drawImage(ResourceLocation image, float x, float y, float width, float height, float alpha) {
      GlStateManager.disableDepth();
      GlStateManager.enableBlend();
      GL11.glDepthMask(false);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
      Minecraft.getMinecraft().getTextureManager().bindTexture(image);
      drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, width, height);
      GL11.glDepthMask(true);
      GlStateManager.disableBlend();
      GlStateManager.enableDepth();
      GlStateManager.resetColor();
   }

   public static void drawImage(ResourceLocation image, float x, float y, float width, float height, int color) {
      GlStateManager.disableDepth();
      GlStateManager.enableBlend();
      GL11.glDepthMask(false);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      float f = (float)(color >> 24 & 255) / 255.0F;
      float f1 = (float)(color >> 16 & 255) / 255.0F;
      float f2 = (float)(color >> 8 & 255) / 255.0F;
      float f3 = (float)(color & 255) / 255.0F;
      GL11.glColor4f(f1, f2, f3, f);
      Minecraft.getMinecraft().getTextureManager().bindTexture(image);
      drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, width, height);
      GL11.glDepthMask(true);
      GlStateManager.disableBlend();
      GlStateManager.enableDepth();
      GlStateManager.resetColor();
   }

   public static void drawModalRectWithCustomSizedTexture(float x, float y, float u, float v, float width, float height, float textureWidth, float textureHeight) {
      float f = 1.0F / textureWidth;
      float f1 = 1.0F / textureHeight;
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
      worldrenderer.pos((double)x, (double)(y + height), 0.0).tex((double)(u * f), (double)((v + height) * f1)).endVertex();
      worldrenderer.pos((double)(x + width), (double)(y + height), 0.0).tex((double)((u + width) * f), (double)((v + height) * f1)).endVertex();
      worldrenderer.pos((double)(x + width), (double)y, 0.0).tex((double)((u + width) * f), (double)(v * f1)).endVertex();
      worldrenderer.pos((double)x, (double)y, 0.0).tex((double)(u * f), (double)(v * f1)).endVertex();
      tessellator.draw();
   }

   public static void drawCircle(float cx, float cy, float r, int num_segments, int c) {
      GL11.glPushMatrix();
      cx *= 2.0F;
      cy *= 2.0F;
      float f = (float)(c >> 24 & 255) / 255.0F;
      float f1 = (float)(c >> 16 & 255) / 255.0F;
      float f2 = (float)(c >> 8 & 255) / 255.0F;
      float f3 = (float)(c & 255) / 255.0F;
      float theta = (float)(6.2831852 / (double)num_segments);
      float p = (float)Math.cos((double)theta);
      float s = (float)Math.sin((double)theta);
      float x = r *= 2.0F;
      float y = 0.0F;
      enableGL2D();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      GL11.glColor4f(f1, f2, f3, f);
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
      GL11.glPopMatrix();
   }

   public static boolean isHovering(int mouseX, int mouseY, float xLeft, float yUp, float xRight, float yBottom) {
      return (float)mouseX > xLeft && (float)mouseX < xRight && (float)mouseY > yUp && (float)mouseY < yBottom;
   }

   public static void doGlScissor(float x, float y, float width, float height) {
      Minecraft mc = Minecraft.getMinecraft();
      int scaleFactor = 1;
      int k = mc.gameSettings.guiScale;
      if (k == 0) {
         k = 1000;
      }

      while(scaleFactor < k && mc.displayWidth / (scaleFactor + 1) >= 320 && mc.displayHeight / (scaleFactor + 1) >= 240) {
         ++scaleFactor;
      }

      GL11.glScissor((int)(x * (float)scaleFactor), (int)((float)mc.displayHeight - (y + height) * (float)scaleFactor), (int)(width * (float)scaleFactor), (int)(height * (float)scaleFactor));
   }

   private void drawLine(double[] color, double x, double y, double z, double playerX, double playerY, double playerZ) {
      GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
      GL11.glLineWidth(5.0F);
      GL11.glBegin(1);
      GL11.glVertex3d(playerX, playerY, playerZ);
      GL11.glVertex3d(x, y, z);
      GL11.glEnd();
      GL11.glDisable(2848);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public void drawCircle(Entity e, double radius, double x, double y, double z, double yOffset, Color c) {
      float pTicks = Wrapper.getTimer().renderPartialTicks;
      double posX = e.lastTickPosX + yOffset + (x - (e.lastTickPosX + yOffset)) * (double)pTicks - ((IRenderManager)Minecraft.getMinecraft().getRenderManager()).getRenderPosX();
      double posY = e.lastTickPosY + yOffset + (y - (e.lastTickPosY + yOffset)) * (double)pTicks - ((IRenderManager)Minecraft.getMinecraft().getRenderManager()).getRenderPosY();
      double posZ = e.lastTickPosZ + yOffset + (z - (e.lastTickPosZ + yOffset)) * (double)pTicks - ((IRenderManager)Minecraft.getMinecraft().getRenderManager()).getRenderPosZ();
      GlStateManager.pushMatrix();
      GlStateManager.disableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer wr = tessellator.getWorldRenderer();
      wr.begin(2, DefaultVertexFormats.POSITION_COLOR);

      for(int a = 0; a < 360; ++a) {
         double rad = Math.toRadians((double)a);
         wr.pos(posX + Math.cos(rad) * radius, posY, posZ + Math.sin(rad) * radius).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
      }

      tessellator.draw();
      GlStateManager.disableBlend();
      GlStateManager.enableTexture2D();
      GlStateManager.popMatrix();
   }

   public void drawCircle(double radius, double x, double y, double z, Color c) {
      float pTicks = Wrapper.getTimer().renderPartialTicks;
      GlStateManager.pushMatrix();
      GlStateManager.disableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer wr = tessellator.getWorldRenderer();
      wr.begin(2, DefaultVertexFormats.POSITION_COLOR);

      for(int a = 0; a < 360; ++a) {
         double rad = Math.toRadians((double)a);
         wr.pos(x + Math.cos(rad) * radius, y + 0.20000000298023224, z + Math.sin(rad) * radius).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
      }

      tessellator.draw();
      GlStateManager.disableBlend();
      GlStateManager.enableTexture2D();
      GlStateManager.popMatrix();
   }

   public void drawCircle(int x, int y, float radius, int color) {
      float alpha = (float)(color >> 24 & 255) / 255.0F;
      float red = (float)(color >> 16 & 255) / 255.0F;
      float green = (float)(color >> 8 & 255) / 255.0F;
      float blue = (float)(color & 255) / 255.0F;
      boolean blend = GL11.glIsEnabled(3042);
      boolean line = GL11.glIsEnabled(2848);
      boolean texture = GL11.glIsEnabled(3553);
      if (!blend) {
         GL11.glEnable(3042);
      }

      if (!line) {
         GL11.glEnable(2848);
      }

      if (texture) {
         GL11.glDisable(3553);
      }

      GL11.glBlendFunc(770, 771);
      GL11.glColor4f(red, green, blue, alpha);
      GL11.glBegin(9);

      for(int i = 0; i <= 360; ++i) {
         GL11.glVertex2d((double)x + Math.sin((double)i * Math.PI / 180.0) * (double)radius, (double)y + Math.cos((double)i * Math.PI / 180.0) * (double)radius);
      }

      GL11.glEnd();
      if (texture) {
         GL11.glEnable(3553);
      }

      if (!line) {
         GL11.glDisable(2848);
      }

      if (!blend) {
         GL11.glDisable(3042);
      }

   }

   public static void setColor(int colorHex) {
      float alpha = (float)(colorHex >> 24 & 255) / 255.0F;
      float red = (float)(colorHex >> 16 & 255) / 255.0F;
      float green = (float)(colorHex >> 8 & 255) / 255.0F;
      float blue = (float)(colorHex & 255) / 255.0F;
      GL11.glColor4f(red, green, blue, alpha == 0.0F ? 1.0F : alpha);
   }

   public static void enableGL3D(float lineWidth) {
      GL11.glDisable(3008);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glEnable(2884);
      GL11.glEnable(2848);
      GL11.glHint(3154, 4354);
      GL11.glHint(3155, 4354);
      GL11.glLineWidth(lineWidth);
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

   public static void drawWolframEntityESP(EntityLivingBase entity, int rgb, double posX, double posY, double posZ) {
      GL11.glPushMatrix();
      GL11.glTranslated(posX, posY, posZ);
      GL11.glRotatef(-entity.rotationYaw, 0.0F, 1.0F, 0.0F);
      setColor(rgb);
      enableGL3D(1.0F);
      Cylinder c = new Cylinder();
      GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
      c.setDrawStyle(100011);
      c.draw(0.5F, 0.5F, entity.height + 0.1F, 18, 1);
      disableGL3D();
      GL11.glPopMatrix();
   }

   public static void drawExeterCrossESP(EntityLivingBase entity, int rgb, double x, double y, double z) {
      AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x - 0.4, y, z - 0.4, x + 0.4, y + 2.0, z + 0.4);
      GlStateManager.pushMatrix();
      GlStateManager.translate(x, y, z);
      GlStateManager.translate(-x, -y, -z);
      enableGL3D(1.0F);
      setColor(rgb);
      drawOutlinedBoundingBox(axisAlignedBB);
      disableGL3D();
      GlStateManager.popMatrix();
   }

   public static void drawAWTShape(Shape shape, double epsilon) {
      PathIterator path = shape.getPathIterator(new AffineTransform());
      Double[] cp = new Double[2];
      GLUtessellator tess = GLU.gluNewTess();
      tess.gluTessCallback(100100, TessCallback.INSTANCE);
      tess.gluTessCallback(100102, TessCallback.INSTANCE);
      tess.gluTessCallback(100101, TessCallback.INSTANCE);
      tess.gluTessCallback(100105, TessCallback.INSTANCE);
      switch (path.getWindingRule()) {
         case 0:
            tess.gluTessProperty(100140, 100130.0);
            break;
         case 1:
            tess.gluTessProperty(100140, 100131.0);
      }

      ArrayList pointsCache = new ArrayList();
      tess.gluTessBeginPolygon((Object)null);

      for(; !path.isDone(); path.next()) {
         double[] segment = new double[6];
         int type = path.currentSegment(segment);
         Double[][] points;
         switch (type) {
            case 0:
               tess.gluTessBeginContour();
               pointsCache.add(new Double[]{segment[0], segment[1]});
               cp[0] = segment[0];
               cp[1] = segment[1];
               break;
            case 1:
               pointsCache.add(new Double[]{segment[0], segment[1]});
               cp[0] = segment[0];
               cp[1] = segment[1];
               break;
            case 2:
               points = MathUtils.getPointsOnCurve(new Double[][]{{cp[0], cp[1]}, {segment[0], segment[1]}, {segment[2], segment[3]}}, 10);
               pointsCache.addAll(Arrays.asList(points));
               cp[0] = segment[2];
               cp[1] = segment[3];
               break;
            case 3:
               points = MathUtils.getPointsOnCurve(new Double[][]{{cp[0], cp[1]}, {segment[0], segment[1]}, {segment[2], segment[3]}, {segment[4], segment[5]}}, 10);
               pointsCache.addAll(Arrays.asList(points));
               cp[0] = segment[4];
               cp[1] = segment[5];
               break;
            case 4:
               points = MathUtils.simplifyPoints((Double[][])pointsCache.toArray(new Double[0][0]), epsilon);
               int var10 = points.length;

               for(int var11 = 0; var11 < var10; ++var11) {
                  Double[] point = points[var11];
                  tessVertex(tess, new double[]{point[0], point[1], 0.0, 0.0, 0.0, 0.0});
               }

               pointsCache.clear();
               tess.gluTessEndContour();
         }
      }

      tess.gluEndPolygon();
      tess.gluDeleteTess();
   }

   public static void tessVertex(GLUtessellator tessellator, double[] coords) {
      tessellator.gluTessVertex(coords, 0, new VertexData(coords));
   }
}
