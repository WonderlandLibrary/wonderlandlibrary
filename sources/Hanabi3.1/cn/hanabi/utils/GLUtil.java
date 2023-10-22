package cn.hanabi.utils;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class GLUtil {
   private static final Map glCapMap = new HashMap();

   public static void startSmooth() {
      GL11.glEnable(2848);
      GL11.glEnable(2881);
      GL11.glEnable(2832);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glHint(3154, 4354);
      GL11.glHint(3155, 4354);
      GL11.glHint(3153, 4354);
   }

   public static void endSmooth() {
      GL11.glDisable(2848);
      GL11.glDisable(2881);
      GL11.glEnable(2832);
   }

   public static void setColor(Color color) {
      GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
   }

   public static void setGLCap(int cap, boolean flag) {
      glCapMap.put(cap, GL11.glGetBoolean(cap));
      if (flag) {
         GL11.glEnable(cap);
      } else {
         GL11.glDisable(cap);
      }

   }

   public static void revertGLCap(int cap) {
      Boolean origCap = (Boolean)glCapMap.get(cap);
      if (origCap != null) {
         if (origCap) {
            GL11.glEnable(cap);
         } else {
            GL11.glDisable(cap);
         }
      }

   }

   public static void glEnable(int cap) {
      setGLCap(cap, true);
   }

   public static void glDisable(int cap) {
      setGLCap(cap, false);
   }

   public static void revertAllCaps() {
      Iterator var0 = glCapMap.keySet().iterator();

      while(var0.hasNext()) {
         Integer integer = (Integer)var0.next();
         int cap = integer;
         revertGLCap(cap);
      }

   }

   public static void setColor(int rgba) {
      int r = rgba & 255;
      int g = rgba >> 8 & 255;
      int b = rgba >> 16 & 255;
      int a = rgba >> 24 & 255;
      GL11.glColor4b((byte)r, (byte)g, (byte)b, (byte)a);
   }

   public static int toRGBA(Color c) {
      return c.getRed() | c.getGreen() << 8 | c.getBlue() << 16 | c.getAlpha() << 24;
   }

   public static void drawRect(int mode, int left, int top, int right, int bottom, int color) {
      int j;
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
      worldrenderer.begin(mode, DefaultVertexFormats.POSITION);
      worldrenderer.pos((double)left, (double)bottom, 0.0).endVertex();
      worldrenderer.pos((double)right, (double)bottom, 0.0).endVertex();
      worldrenderer.pos((double)right, (double)top, 0.0).endVertex();
      worldrenderer.pos((double)left, (double)top, 0.0).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static int glmClamp(int value, int min, int max) {
      return Math.max(min, Math.min(max, value));
   }

   public static double glmClamp(double value, double min, double max) {
      return Math.max(min, Math.min(max, value));
   }
}
