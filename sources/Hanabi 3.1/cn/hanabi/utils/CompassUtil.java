package cn.hanabi.utils;

import cn.hanabi.Hanabi;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class CompassUtil {
   public static List degrees = Lists.newArrayList();
   public float innerWidth;
   public float outerWidth;
   public boolean shadow;
   public float scale;
   public int accuracy;

   public CompassUtil(float i, float o, float s, int a, boolean sh) {
      this.innerWidth = i;
      this.outerWidth = o;
      this.scale = s;
      this.accuracy = a;
      this.shadow = sh;
      degrees.add(new Degree("N", 1));
      degrees.add(new Degree("195", 2));
      degrees.add(new Degree("210", 2));
      degrees.add(new Degree("NE", 3));
      degrees.add(new Degree("240", 2));
      degrees.add(new Degree("255", 2));
      degrees.add(new Degree("E", 1));
      degrees.add(new Degree("285", 2));
      degrees.add(new Degree("300", 2));
      degrees.add(new Degree("SE", 3));
      degrees.add(new Degree("330", 2));
      degrees.add(new Degree("345", 2));
      degrees.add(new Degree("S", 1));
      degrees.add(new Degree("15", 2));
      degrees.add(new Degree("30", 2));
      degrees.add(new Degree("SW", 3));
      degrees.add(new Degree("60", 2));
      degrees.add(new Degree("75", 2));
      degrees.add(new Degree("W", 1));
      degrees.add(new Degree("105", 2));
      degrees.add(new Degree("120", 2));
      degrees.add(new Degree("NW", 3));
      degrees.add(new Degree("150", 2));
      degrees.add(new Degree("165", 2));
   }

   public static void draw(ScaledResolution sr) {
      preRender(sr);
      float center = (float)(sr.getScaledWidth() / 2);
      int count = 0;
      float yaaahhrewindTime = Minecraft.getMinecraft().thePlayer.rotationYaw % 360.0F * 2.0F + 1080.0F;
      GL11.glPushMatrix();
      GL11.glEnable(3089);
      RenderUtil.doGlScissor(RenderUtil.width() / 2 - 100, 22, 200, 25);

      try {
         Iterator var4;
         Degree d;
         float location;
         float completeLocation;
         for(var4 = degrees.iterator(); var4.hasNext(); ++count) {
            d = (Degree)var4.next();
            location = center + (float)(count * 30) - yaaahhrewindTime;
            completeLocation = d.type == 1 ? location - (float)(Hanabi.INSTANCE.fontManager.usans28.getStringWidth(d.text) / 2) : (d.type == 2 ? location - (float)(Hanabi.INSTANCE.fontManager.usans28.getStringWidth(d.text) / 2) : location - (float)(Hanabi.INSTANCE.fontManager.usans22.getStringWidth(d.text) / 2));
            int opacity = opacity(sr, completeLocation);
            if (d.type == 1 && opacity != 16777215) {
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               Hanabi.INSTANCE.fontManager.usans28.drawString(d.text, completeLocation, 25.0F, opacity(sr, completeLocation));
            }

            if (d.type == 2 && opacity != 16777215) {
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               Gui.drawRect((int)((double)location - 0.5), 29, (int)((double)location + 0.5), 34, opacity(sr, completeLocation));
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               Hanabi.INSTANCE.fontManager.usans14.drawString(d.text, completeLocation, 37.5F, opacity(sr, completeLocation));
            }

            if (d.type == 3 && opacity != 16777215) {
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               Hanabi.INSTANCE.fontManager.usans22.drawString(d.text, completeLocation, (float)(25 + Hanabi.INSTANCE.fontManager.usans28.FONT_HEIGHT / 2 - Hanabi.INSTANCE.fontManager.usans22.FONT_HEIGHT / 2), opacity(sr, completeLocation));
            }
         }

         for(var4 = degrees.iterator(); var4.hasNext(); ++count) {
            d = (Degree)var4.next();
            location = center + (float)(count * 30) - yaaahhrewindTime;
            completeLocation = d.type == 1 ? location - (float)(Hanabi.INSTANCE.fontManager.usans28.getStringWidth(d.text) / 2) : (d.type == 2 ? location - (float)(Hanabi.INSTANCE.fontManager.usans14.getStringWidth(d.text) / 2) : location - (float)(Hanabi.INSTANCE.fontManager.usans22.getStringWidth(d.text) / 2));
            if (d.type == 1) {
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               Hanabi.INSTANCE.fontManager.usans28.drawString(d.text, completeLocation, 25.0F, opacity(sr, completeLocation));
            }

            if (d.type == 2) {
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               Gui.drawRect((int)((double)location - 0.5), 29, (int)((double)location + 0.5), 34, opacity(sr, completeLocation));
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               Hanabi.INSTANCE.fontManager.usans14.drawString(d.text, completeLocation, 37.5F, opacity(sr, completeLocation));
            }

            if (d.type == 3) {
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               Hanabi.INSTANCE.fontManager.usans22.drawString(d.text, completeLocation, (float)(25 + Hanabi.INSTANCE.fontManager.usans28.FONT_HEIGHT / 2 - Hanabi.INSTANCE.fontManager.usans22.FONT_HEIGHT / 2), opacity(sr, completeLocation));
            }
         }

         for(var4 = degrees.iterator(); var4.hasNext(); ++count) {
            d = (Degree)var4.next();
            location = center + (float)(count * 30) - yaaahhrewindTime;
            completeLocation = d.type == 1 ? location - (float)(Hanabi.INSTANCE.fontManager.usans28.getStringWidth(d.text) / 2) : (d.type == 2 ? location - (float)(Hanabi.INSTANCE.fontManager.usans14.getStringWidth(d.text) / 2) : location - (float)(Hanabi.INSTANCE.fontManager.usans22.getStringWidth(d.text) / 2));
            if (d.type == 1) {
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               Hanabi.INSTANCE.fontManager.usans28.drawString(d.text, completeLocation, 25.0F, opacity(sr, completeLocation));
            }

            if (d.type == 2) {
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               Gui.drawRect((int)((double)location - 0.5), 29, (int)((double)location + 0.5), 34, opacity(sr, completeLocation));
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               Hanabi.INSTANCE.fontManager.usans14.drawString(d.text, completeLocation, 37.5F, opacity(sr, completeLocation));
            }

            if (d.type == 3) {
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               Hanabi.INSTANCE.fontManager.usans22.drawString(d.text, completeLocation, (float)(25 + Hanabi.INSTANCE.fontManager.usans28.FONT_HEIGHT / 2 - Hanabi.INSTANCE.fontManager.usans22.FONT_HEIGHT / 2), opacity(sr, completeLocation));
            }
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }

      GL11.glDisable(3089);
      GL11.glPopMatrix();
   }

   public static void preRender(ScaledResolution sr) {
      GlStateManager.disableAlpha();
      GlStateManager.enableBlend();
   }

   public static int opacity(ScaledResolution sr, float offset) {
      int op = false;
      float offs = 255.0F - Math.abs((float)(sr.getScaledWidth() / 2) - offset) * 1.8F;
      Color c = new Color(255, 255, 255, (int)Math.min(Math.max(0.0F, offs), 255.0F));
      return c.getRGB();
   }

   public static class Degree {
      public String text;
      public int type;

      public Degree(String s, int t) {
         this.text = s;
         this.type = t;
      }
   }
}
