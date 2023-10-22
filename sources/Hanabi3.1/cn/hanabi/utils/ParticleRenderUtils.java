package cn.hanabi.utils;

import org.lwjgl.opengl.GL11;

public class ParticleRenderUtils {
   public static void connectPoints(float xOne, float yOne, float xTwo, float yTwo) {
      GL11.glPushMatrix();
      GL11.glEnable(2848);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.8F);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(3042);
      GL11.glLineWidth(0.5F);
      GL11.glBegin(1);
      GL11.glVertex2f(xOne, yOne);
      GL11.glVertex2f(xTwo, yTwo);
      GL11.glEnd();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glPopMatrix();
   }

   public static void drawCircle(float x, float y, float radius, int color) {
      float alpha = (float)(color >> 24 & 255) / 255.0F;
      float red = (float)(color >> 16 & 255) / 255.0F;
      float green = (float)(color >> 8 & 255) / 255.0F;
      float blue = (float)(color & 255) / 255.0F;
      GL11.glColor4f(red, green, blue, alpha);
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glPushMatrix();
      GL11.glLineWidth(1.0F);
      GL11.glBegin(9);

      for(int i = 0; i <= 360; ++i) {
         GL11.glVertex2d((double)x + Math.sin((double)i * Math.PI / 180.0) * (double)radius, (double)y + Math.cos((double)i * Math.PI / 180.0) * (double)radius);
      }

      GL11.glEnd();
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      GL11.glDisable(2848);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }
}
