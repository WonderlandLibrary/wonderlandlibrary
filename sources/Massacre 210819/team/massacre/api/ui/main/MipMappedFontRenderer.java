package team.massacre.api.ui.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import org.lwjgl.opengl.GL11;
import team.massacre.api.ui.framework.CustomFontRenderer;
import team.massacre.api.ui.framework.StaticallySizedImage;
import team.massacre.utils.OGLUtils;

public final class MipMappedFontRenderer implements CustomFontRenderer {
   private final StaticallySizedImage[] characters = new StaticallySizedImage[256];
   private final float margin = 6.0F;

   public MipMappedFontRenderer(Font font, boolean antiAliasing) {
      BufferedImage tempImage = new BufferedImage(1, 1, 2);
      Graphics2D graphics2D = (Graphics2D)tempImage.getGraphics();
      FontMetrics fontMetrics = graphics2D.getFontMetrics(font);

      for(int i = 11; i < 256; ++i) {
         if (i != 127) {
            char character = (char)i;
            Rectangle2D characterBounds = fontMetrics.getStringBounds(String.valueOf(character), graphics2D);
            double var10002 = characterBounds.getWidth();
            this.getClass();
            BufferedImage characterImage = new BufferedImage((int)StrictMath.ceil(var10002 + (double)(2.0F * 6.0F)), (int)StrictMath.ceil(characterBounds.getHeight()), 2);
            Graphics2D graphics = (Graphics2D)characterImage.getGraphics();
            graphics.setFont(font);
            graphics.setColor(new Color(255, 255, 255, 0));
            graphics.fillRect(0, 0, characterImage.getWidth(), characterImage.getHeight());
            graphics.setColor(Color.WHITE);
            if (antiAliasing) {
               graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
               graphics.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 150);
               graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }

            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            String var10001 = String.valueOf(character);
            this.getClass();
            graphics.drawString(var10001, 6.0F, (float)fontMetrics.getAscent());
            this.characters[i] = new StaticallySizedImage(characterImage, 2);
         }
      }

   }

   private void renderString(String text) {
      int length;
      if (text != null && (length = text.length()) != 0) {
         StaticallySizedImage[] characters = this.characters;
         this.getClass();
         float x = -6.0F;

         for(int i = 0; i < length; ++i) {
            char character = text.charAt(i);
            if (character > '\n' && character != 127 && character < 256) {
               StaticallySizedImage image = characters[character];
               image.draw(x, 0.0F);
               float var10001 = (float)image.getWidth();
               this.getClass();
               x += var10001 - 2.0F * 6.0F;
            }
         }

      }
   }

   public void drawString(String text, float x, float y, int color) {
      GL11.glTranslatef(x, y, 0.0F);
      GL11.glScalef(0.5F, 0.5F, 0.0F);
      GL11.glEnable(3042);
      OGLUtils.color(color);
      this.renderString(text);
      GL11.glDisable(3042);
      GL11.glScalef(2.0F, 2.0F, 0.0F);
      GL11.glTranslatef(-x, -y, 0.0F);
   }

   public void drawStringWithOutline(String text, float x, float y, int outlineColor, int color) {
      GL11.glTranslatef(x, y, 0.0F);
      GL11.glScalef(0.5F, 0.5F, 0.0F);
      GL11.glEnable(3042);
      OGLUtils.color(outlineColor);
      GL11.glTranslatef(1.0F, 0.0F, 0.0F);
      this.renderString(text);
      GL11.glTranslatef(-1.0F, 1.0F, 0.0F);
      this.renderString(text);
      GL11.glTranslatef(0.0F, -2.0F, 0.0F);
      this.renderString(text);
      GL11.glTranslatef(-1.0F, 1.0F, 0.0F);
      this.renderString(text);
      GL11.glTranslatef(1.0F, 0.0F, 0.0F);
      OGLUtils.color(color);
      this.renderString(text);
      GL11.glDisable(3042);
      GL11.glScalef(2.0F, 2.0F, 0.0F);
      GL11.glTranslatef(-x, -y, 0.0F);
   }

   public void drawStringWithShadow(String text, float x, float y, int color) {
      GL11.glTranslatef(x, y, 0.0F);
      GL11.glScalef(0.5F, 0.5F, 0.0F);
      GL11.glEnable(3042);
      GL11.glTranslatef(1.0F, 1.0F, 0.0F);
      OGLUtils.color(4144959 + (color << 24));
      this.renderString(text);
      GL11.glTranslatef(-1.0F, -1.0F, 0.0F);
      OGLUtils.color(color);
      this.renderString(text);
      GL11.glDisable(3042);
      GL11.glScalef(2.0F, 2.0F, 0.0F);
      GL11.glTranslatef(-x, -y, 0.0F);
   }

   public float getWidth(String text) {
      int length;
      if (text != null && (length = text.length()) != 0) {
         float width = 0.0F;
         StaticallySizedImage[] characters = this.characters;

         for(int i = 0; i < length; ++i) {
            char character = text.charAt(i);
            if (character > '\n' && character != 127 && character < 256) {
               float var10001 = (float)characters[character].getWidth();
               this.getClass();
               width += (var10001 - 2.0F * 6.0F) / 2.0F;
            }
         }

         return width;
      } else {
         return 0.0F;
      }
   }

   public float getHeight(String text) {
      int length;
      if (text != null && (length = text.length()) != 0) {
         float height = 0.0F;
         StaticallySizedImage[] characters = this.characters;

         for(int i = 0; i < length; ++i) {
            char character = text.charAt(i);
            if (character > '\n' && character != 127 && character < 256) {
               height = Math.max(height, (float)characters[character].getHeight());
            }
         }

         return height / 2.0F;
      } else {
         return 0.0F;
      }
   }
}
