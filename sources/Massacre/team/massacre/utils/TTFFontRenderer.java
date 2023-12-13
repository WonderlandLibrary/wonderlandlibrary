package team.massacre.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Locale;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class TTFFontRenderer {
   private Font font;
   private boolean fractionalMetrics;
   private TTFFontRenderer.CharacterData[] regularData;
   private TTFFontRenderer.CharacterData[] boldData;
   private TTFFontRenderer.CharacterData[] italicsData;
   private int[] colorCodes;
   private byte[] glyphWidth;
   private boolean unicodeFlag;
   private float[] charWidth;
   private int RANDOM_OFFSET;
   public float offsetBold;
   public int FONT_HEIGHT;

   public TTFFontRenderer(Font font) {
      this(font, 256);
   }

   public TTFFontRenderer(Font font, int characterCount) {
      this(font, characterCount, true);
   }

   public TTFFontRenderer(Font font, boolean fractionalMetrics) {
      this(font, 256, fractionalMetrics);
   }

   public TTFFontRenderer(Font font, int characterCount, boolean fractionalMetrics) {
      this.glyphWidth = new byte[65536];
      this.charWidth = new float[256];
      this.offsetBold = 1.0F;
      this.FONT_HEIGHT = 9;
      this.RANDOM_OFFSET = 1;
      this.fractionalMetrics = false;
      this.colorCodes = new int[32];
      this.font = font;
      this.fractionalMetrics = fractionalMetrics;
      this.regularData = this.setup(new TTFFontRenderer.CharacterData[characterCount], 0);
      this.boldData = this.setup(new TTFFontRenderer.CharacterData[characterCount], 1);
      this.italicsData = this.setup(new TTFFontRenderer.CharacterData[characterCount], 2);
   }

   public void setUnicodeFlag(boolean unicodeFlag) {
      this.unicodeFlag = unicodeFlag;
   }

   private TTFFontRenderer.CharacterData[] setup(TTFFontRenderer.CharacterData[] characterData, int type) {
      this.generateColors();
      Font font = this.font.deriveFont(type);
      BufferedImage utilityImage = new BufferedImage(1, 1, 2);
      Graphics2D utilityGraphics = (Graphics2D)utilityImage.getGraphics();
      utilityGraphics.setFont(font);
      FontMetrics fontMetrics = utilityGraphics.getFontMetrics();

      for(int index = 0; index < characterData.length; ++index) {
         char character = (char)index;
         Rectangle2D characterBounds = fontMetrics.getStringBounds(String.valueOf(character), utilityGraphics);
         float width = (float)characterBounds.getWidth() + 8.0F;
         float height = (float)characterBounds.getHeight();
         BufferedImage characterImage = new BufferedImage(MathHelper.ceiling_double_int((double)width), MathHelper.ceiling_double_int((double)height), 2);
         Graphics2D graphics = (Graphics2D)characterImage.getGraphics();
         graphics.setFont(font);
         graphics.setColor(new Color(255, 255, 255, 0));
         graphics.fillRect(0, 0, characterImage.getWidth(), characterImage.getHeight());
         graphics.setColor(Color.WHITE);
         graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
         graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
         graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, this.fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
         graphics.drawString(String.valueOf(character), 4, fontMetrics.getAscent());
         int textureId = GL11.glGenTextures();
         this.createTexture(textureId, characterImage);
         characterData[index] = new TTFFontRenderer.CharacterData(character, (float)characterImage.getWidth(), (float)characterImage.getHeight(), textureId);
      }

      return characterData;
   }

   private void createTexture(int textureId, BufferedImage image) {
      int[] pixels = new int[image.getWidth() * image.getHeight()];
      image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
      ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

      for(int y = 0; y < image.getHeight(); ++y) {
         for(int x = 0; x < image.getWidth(); ++x) {
            int pixel = pixels[y * image.getWidth() + x];
            buffer.put((byte)(pixel >> 16 & 255));
            buffer.put((byte)(pixel >> 8 & 255));
            buffer.put((byte)(pixel & 255));
            buffer.put((byte)(pixel >> 24 & 255));
         }
      }

      buffer.flip();
      GlStateManager.bindTexture(textureId);
      GL11.glTexParameteri(3553, 10241, 9728);
      GL11.glTexParameteri(3553, 10240, 9728);
      GL11.glTexImage2D(3553, 0, 6408, image.getWidth(), image.getHeight(), 0, 6408, 5121, buffer);
      GlStateManager.bindTexture(0);
   }

   public void drawString(String text, float x, float y, int color) {
      this.renderString(text, x, y, color, false);
   }

   public int drawStringInt(String text, float x, float y, int color, boolean dropShadow) {
      int i;
      if (dropShadow) {
         i = this.renderStringInt(text, x + 1.0F, y + 1.0F, color, true);
         i = Math.max(i, this.renderStringInt(text, x, y, color, false));
      } else {
         i = this.renderStringInt(text, x, y, color, false);
      }

      return i;
   }

   public int drawStringWithShadowInt(String text, float x, float y, int color) {
      return this.drawStringInt(text, x, y, color, true);
   }

   public String drawStringWithShadow(String text, float x, float y, int color) {
      GL11.glTranslated(0.5D, 0.5D, 0.0D);
      this.renderString(text, x, y, color, true);
      GL11.glTranslated(-0.5D, -0.5D, 0.0D);
      this.renderString(text, x, y, color, false);
      return text;
   }

   public void drawCenteredString(String text, int x, int y, int color) {
      this.drawStringWithShadow(text, (float)x - this.getWidth(text) / 2.0F, (float)y, color);
   }

   private void renderString(String text, float x, float y, int color, boolean shadow) {
      if (text.length() != 0) {
         GL11.glPushMatrix();
         GlStateManager.scale(0.5D, 0.5D, 1.0D);
         x -= 2.0F;
         y -= 2.0F;
         x += 0.5F;
         y += 0.5F;
         x *= 2.0F;
         y *= 2.0F;
         TTFFontRenderer.CharacterData[] characterData = this.regularData;
         boolean underlined = false;
         boolean strikethrough = false;
         boolean obfuscated = false;
         int length = text.length();
         float multiplier = shadow ? 4.0F : 1.0F;
         float a = (float)(color >> 24 & 255) / 255.0F;
         float r = (float)(color >> 16 & 255) / 255.0F;
         float g = (float)(color >> 8 & 255) / 255.0F;
         float b = (float)(color & 255) / 255.0F;
         GlStateManager.color(r / multiplier, g / multiplier, b / multiplier, a);

         for(int i = 0; i < length; ++i) {
            char character = text.charAt(i);
            char previous = i > 0 ? text.charAt(i - 1) : 46;
            if (previous != 167) {
               if (character == 167 && i < length) {
                  int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                  if (index >= 16) {
                     if (index == 16) {
                        obfuscated = true;
                     } else if (index == 17) {
                        characterData = this.boldData;
                     } else if (index == 18) {
                        strikethrough = true;
                     } else if (index == 19) {
                        underlined = true;
                     } else if (index == 20) {
                        characterData = this.italicsData;
                     } else if (index == 21) {
                        obfuscated = false;
                        strikethrough = false;
                        underlined = false;
                        characterData = this.regularData;
                        GL11.glColor4d(1.0D * (shadow ? 0.25D : 1.0D), 1.0D * (shadow ? 0.25D : 1.0D), 1.0D * (shadow ? 0.25D : 1.0D), (double)a);
                     }
                  } else {
                     obfuscated = false;
                     strikethrough = false;
                     underlined = false;
                     characterData = this.regularData;
                     if (index < 0 || index > 15) {
                        index = 15;
                     }

                     if (shadow) {
                        index += 16;
                     }

                     int textColor = this.colorCodes[index];
                     GL11.glColor4d((double)(textColor >> 16) / 255.0D, (double)(textColor >> 8 & 255) / 255.0D, (double)(textColor & 255) / 255.0D, (double)a);
                  }
               } else if (character <= 255) {
                  if (obfuscated) {
                     character += (char)this.RANDOM_OFFSET;
                  }

                  this.drawChar(character, characterData, x, y);
                  TTFFontRenderer.CharacterData charData = characterData[character];
                  if (strikethrough) {
                     this.drawLine(new Vector2f(0.0F, charData.height / 2.0F), new Vector2f(charData.width, charData.height / 2.0F), 3.0F);
                  }

                  if (underlined) {
                     this.drawLine(new Vector2f(0.0F, charData.height - 15.0F), new Vector2f(charData.width, charData.height - 15.0F), 3.0F);
                  }

                  x += charData.width - 8.0F;
               }
            }
         }

         GL11.glPopMatrix();
         GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
         GlStateManager.bindTexture(0);
      }
   }

   private int renderStringInt(String text, float x, float y, int color, boolean shadow) {
      if (text.length() == 0) {
         return 0;
      } else {
         GL11.glPushMatrix();
         GlStateManager.scale(0.5D, 0.5D, 1.0D);
         x -= 2.0F;
         y -= 2.0F;
         x += 0.5F;
         y += 0.5F;
         x *= 2.0F;
         y *= 2.0F;
         TTFFontRenderer.CharacterData[] characterData = this.regularData;
         boolean underlined = false;
         boolean strikethrough = false;
         boolean obfuscated = false;
         int length = text.length();
         float multiplier = shadow ? 4.0F : 1.0F;
         float a = (float)(color >> 24 & 255) / 255.0F;
         float r = (float)(color >> 16 & 255) / 255.0F;
         float g = (float)(color >> 8 & 255) / 255.0F;
         float b = (float)(color & 255) / 255.0F;
         GlStateManager.color(r / multiplier, g / multiplier, b / multiplier, a);

         for(int i = 0; i < length; ++i) {
            char character = text.charAt(i);
            char previous = i > 0 ? text.charAt(i - 1) : 46;
            if (previous != 167) {
               if (character == 167 && i < length) {
                  int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                  if (index >= 16) {
                     if (index == 16) {
                        obfuscated = true;
                     } else if (index == 17) {
                        characterData = this.boldData;
                     } else if (index == 18) {
                        strikethrough = true;
                     } else if (index == 19) {
                        underlined = true;
                     } else if (index == 20) {
                        characterData = this.italicsData;
                     } else if (index == 21) {
                        obfuscated = false;
                        strikethrough = false;
                        underlined = false;
                        characterData = this.regularData;
                        GL11.glColor4d(1.0D * (shadow ? 0.25D : 1.0D), 1.0D * (shadow ? 0.25D : 1.0D), 1.0D * (shadow ? 0.25D : 1.0D), (double)a);
                     }
                  } else {
                     obfuscated = false;
                     strikethrough = false;
                     underlined = false;
                     characterData = this.regularData;
                     if (index < 0 || index > 15) {
                        index = 15;
                     }

                     if (shadow) {
                        index += 16;
                     }

                     int textColor = this.colorCodes[index];
                     GL11.glColor4d((double)(textColor >> 16) / 255.0D, (double)(textColor >> 8 & 255) / 255.0D, (double)(textColor & 255) / 255.0D, (double)a);
                  }
               } else if (character <= 255) {
                  if (obfuscated) {
                     character += (char)this.RANDOM_OFFSET;
                  }

                  this.drawChar(character, characterData, x, y);
                  TTFFontRenderer.CharacterData charData = characterData[character];
                  if (strikethrough) {
                     this.drawLine(new Vector2f(0.0F, charData.height / 2.0F), new Vector2f(charData.width, charData.height / 2.0F), 3.0F);
                  }

                  if (underlined) {
                     this.drawLine(new Vector2f(0.0F, charData.height - 15.0F), new Vector2f(charData.width, charData.height - 15.0F), 3.0F);
                  }

                  x += charData.width - 8.0F;
               }
            }
         }

         GL11.glPopMatrix();
         GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
         GlStateManager.bindTexture(0);
         return (int)x;
      }
   }

   public float getWidth(String text) {
      float width = 0.0F;
      TTFFontRenderer.CharacterData[] characterData = this.regularData;
      int length = text.length();

      for(int i = 0; i < length; ++i) {
         char character = text.charAt(i);
         char previous = i > 0 ? text.charAt(i - 1) : 46;
         if (previous != 167) {
            if (character == 167 && i < length) {
               int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
               if (index == 17) {
                  characterData = this.boldData;
               } else if (index == 20) {
                  characterData = this.italicsData;
               } else if (index == 21) {
                  characterData = this.regularData;
               }
            } else if (character <= 255) {
               TTFFontRenderer.CharacterData charData = characterData[character];
               width += (charData.width - 8.0F) / 2.0F;
            }
         }
      }

      return width + 2.0F;
   }

   public float getHeight(String text) {
      float height = 0.0F;
      TTFFontRenderer.CharacterData[] characterData = this.regularData;
      int length = text.length();

      for(int i = 0; i < length; ++i) {
         char character = text.charAt(i);
         char previous = i > 0 ? text.charAt(i - 1) : 46;
         if (previous != 167) {
            if (character == 167 && i < length) {
               int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
               if (index == 17) {
                  characterData = this.boldData;
               } else if (index == 20) {
                  characterData = this.italicsData;
               } else if (index == 21) {
                  characterData = this.regularData;
               }
            } else if (character <= 255) {
               TTFFontRenderer.CharacterData charData = characterData[character];
               height = Math.max(height, charData.height);
            }
         }
      }

      return height / 2.0F - 2.0F;
   }

   public int getStringWidth(String p_78256_1_) {
      if (p_78256_1_ == null) {
         return 0;
      } else {
         float var2 = 0.0F;
         boolean var3 = false;

         for(int var4 = 0; var4 < p_78256_1_.length(); ++var4) {
            char var5 = p_78256_1_.charAt(var4);
            float var6 = this.getCharWidthFloat(var5);
            if (var6 < 0.0F && var4 < p_78256_1_.length() - 1) {
               ++var4;
               var5 = p_78256_1_.charAt(var4);
               if (var5 != 'l' && var5 != 'L') {
                  if (var5 == 'r' || var5 == 'R') {
                     var3 = false;
                  }
               } else {
                  var3 = true;
               }

               var6 = 0.0F;
            }

            var2 += var6;
            if (var3 && var6 > 0.0F) {
               var2 += this.unicodeFlag ? 1.0F : this.offsetBold;
            }
         }

         return (int)var2;
      }
   }

   private float getCharWidthFloat(char p_78263_1_) {
      if (p_78263_1_ == 167) {
         return -1.0F;
      } else if (p_78263_1_ == ' ') {
         return this.charWidth[32];
      } else {
         int var2 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".indexOf(p_78263_1_);
         if (p_78263_1_ > 0 && var2 != -1 && !this.unicodeFlag) {
            return this.charWidth[var2];
         } else if (this.glyphWidth[p_78263_1_] != 0) {
            int var3 = this.glyphWidth[p_78263_1_] >>> 4;
            int var4 = this.glyphWidth[p_78263_1_] & 15;
            var3 &= 15;
            ++var4;
            return (float)((var4 - var3) / 2 + 1);
         } else {
            return 0.0F;
         }
      }
   }

   private void drawChar(char character, TTFFontRenderer.CharacterData[] characterData, float x, float y) {
      TTFFontRenderer.CharacterData charData = characterData[character];
      charData.bind();
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glBegin(7);
      GL11.glTexCoord2f(0.0F, 0.0F);
      GL11.glVertex2d((double)x, (double)y);
      GL11.glTexCoord2f(0.0F, 1.0F);
      GL11.glVertex2d((double)x, (double)(y + charData.height));
      GL11.glTexCoord2f(1.0F, 1.0F);
      GL11.glVertex2d((double)(x + charData.width), (double)(y + charData.height));
      GL11.glTexCoord2f(1.0F, 0.0F);
      GL11.glVertex2d((double)(x + charData.width), (double)y);
      GL11.glEnd();
      GL11.glDisable(3042);
      GL11.glPopMatrix();
      GL11.glBindTexture(3553, 0);
   }

   private void drawLine(Vector2f start, Vector2f end, float width) {
      GL11.glDisable(3553);
      GL11.glLineWidth(width);
      GL11.glBegin(1);
      GL11.glVertex2f(start.x, start.y);
      GL11.glVertex2f(end.x, end.y);
      GL11.glEnd();
      GL11.glEnable(3553);
   }

   private void generateColors() {
      for(int i = 0; i < 32; ++i) {
         int thingy = (i >> 3 & 1) * 85;
         int red = (i >> 2 & 1) * 170 + thingy;
         int green = (i >> 1 & 1) * 170 + thingy;
         int blue = (i >> 0 & 1) * 170 + thingy;
         if (i == 6) {
            red += 85;
         }

         if (i >= 16) {
            red /= 4;
            green /= 4;
            blue /= 4;
         }

         this.colorCodes[i] = (red & 255) << 16 | (green & 255) << 8 | blue & 255;
      }

   }

   public Font getFont() {
      return this.font;
   }

   class CharacterData {
      public char character;
      public float width;
      public float height;
      private int textureId;

      public CharacterData(char character, float width, float height, int textureId) {
         this.character = character;
         this.width = width;
         this.height = height;
         this.textureId = textureId;
      }

      public void bind() {
         GL11.glBindTexture(3553, this.textureId);
      }
   }
}
