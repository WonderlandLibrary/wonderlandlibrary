package de.violence.font;

import java.awt.Color;
import java.awt.Font;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class SlickFont extends FontRenderer {
   public static String abc = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
   private UnicodeFont font;
   public int scale;
   public String name;

   public SlickFont(String a1, int a2, boolean bold) {
      super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);
      this.scale = a2;
      this.name = a1;

      try {
         this.font = new UnicodeFont(a1, a2, bold, false);
      } catch (SlickException var6) {
         this.font = new UnicodeFont(new Font("Arial", 0, a2));
      }

      this.font.addAsciiGlyphs();
      this.font.getEffects().add(new ColorEffect(Color.WHITE));

      try {
         this.font.loadGlyphs();
      } catch (SlickException var5) {
         throw new RuntimeException(var5);
      }

      this.FONT_HEIGHT = this.font.getHeight(abc) / 2;
   }

   public SlickFont(Font awtFont) {
      super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);
      this.font = new UnicodeFont(awtFont);
      this.font.addAsciiGlyphs();
      this.font.getEffects().add(new ColorEffect(Color.WHITE));

      try {
         this.font.loadGlyphs();
      } catch (SlickException var3) {
         throw new RuntimeException(var3);
      }

      this.FONT_HEIGHT = this.font.getHeight(abc) / 2;
   }

   public int drawOutlinedString(String string, int x, int y, int color) {
      this.drawString(string, x - 1, y, -1);
      this.drawString(string, x + 1, y, -1);
      this.drawString(string, x, y + 1, -1);
      this.drawString(string, x, y - 1, -1);
      return this.drawString(string, x, y, color);
   }

   public int drawShadowString(String string, int x, int y, int color) {
      this.drawString(StringUtils.stripControlCodes(string), (float)x + 0.5F, (float)y + 0.5F, 0);
      return this.drawString(string, x, y, color);
   }

   public int getCharWidth(char c) {
      return this.getStringWidth(Character.toString(c));
   }

   public int getStringWidth(String string) {
      return this.font.getWidth(string.replace("#", " ")) / 4;
   }

   public int getStringHeight(String string) {
      return this.font.getHeight(string) / 4;
   }

   public void drawCenteredString(String text, int x, int y, int color) {
      this.drawString(text, x - this.getStringWidth(text) / 2, y, color);
   }

   public int drawString(String string, int x, int y, int color) {
      x *= 2;
      y *= 2;
      GL11.glPushMatrix();
      GL11.glScaled(0.25D, 0.25D, 0.25D);
      boolean blend = GL11.glIsEnabled(3042);
      boolean lighting = GL11.glIsEnabled(2896);
      boolean texture = GL11.glIsEnabled(3553);
      if(!blend) {
         GL11.glEnable(3042);
      }

      if(lighting) {
         GL11.glDisable(2896);
      }

      if(texture) {
         GL11.glDisable(3553);
      }

      x *= 2;
      y *= 2;
      this.font.drawString((float)x, (float)y, string, new org.newdawn.slick.Color(color));
      if(texture) {
         GL11.glEnable(3553);
      }

      if(lighting) {
         GL11.glEnable(2896);
      }

      if(!blend) {
         GL11.glDisable(3042);
      }

      GL11.glPopMatrix();
      return x;
   }

   public float drawString(String string, float x, float y, int color) {
      x *= 2.0F;
      y *= 2.0F;
      GL11.glPushMatrix();
      GL11.glScaled(0.25D, 0.25D, 0.25D);
      boolean blend = GL11.glIsEnabled(3042);
      boolean lighting = GL11.glIsEnabled(2896);
      boolean texture = GL11.glIsEnabled(3553);
      if(!blend) {
         GL11.glEnable(3042);
      }

      if(lighting) {
         GL11.glDisable(2896);
      }

      if(texture) {
         GL11.glDisable(3553);
      }

      x *= 2.0F;
      y *= 2.0F;
      this.font.drawString(x, y, string, new org.newdawn.slick.Color(color));
      if(texture) {
         GL11.glEnable(3553);
      }

      if(lighting) {
         GL11.glEnable(2896);
      }

      if(!blend) {
         GL11.glDisable(3042);
      }

      GL11.glPopMatrix();
      return x;
   }
}
