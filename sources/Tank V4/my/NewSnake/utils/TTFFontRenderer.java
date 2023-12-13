package my.NewSnake.utils;

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
   private TTFFontRenderer.CharacterData[] italicsData;
   private int[] colorCodes;
   private static final int MARGIN = 4;
   private static int RANDOM_OFFSET = 1;
   private static final char COLOR_INVOKER = 167;
   private Font font;
   private TTFFontRenderer.CharacterData[] regularData;
   private TTFFontRenderer.CharacterData[] boldData;
   private boolean fractionalMetrics;

   public void drawStringWithShadow(String var1, float var2, float var3, int var4) {
      GL11.glTranslated(0.5D, 0.5D, 0.0D);
      this.renderString(var1, var2, var3, var4, true);
      GL11.glTranslated(-0.5D, -0.5D, 0.0D);
      this.renderString(var1, var2, var3, var4, false);
   }

   private void drawChar(char var1, TTFFontRenderer.CharacterData[] var2, double var3, double var5) {
      TTFFontRenderer.CharacterData var7 = var2[var1];
      var7.bind();
      GL11.glBegin(7);
      GL11.glTexCoord2f(0.0F, 0.0F);
      GL11.glVertex2d(var3, var5);
      GL11.glTexCoord2f(0.0F, 1.0F);
      GL11.glVertex2d(var3, var5 + (double)var7.height);
      GL11.glTexCoord2f(1.0F, 1.0F);
      GL11.glVertex2d(var3 + (double)var7.width, var5 + (double)var7.height);
      GL11.glTexCoord2f(1.0F, 0.0F);
      GL11.glVertex2d(var3 + (double)var7.width, var5);
      GL11.glEnd();
   }

   public void drawCenteredString(String var1, float var2, float var3, int var4) {
      float var5 = this.getWidth(var1) / 2.0F;
      this.renderString(var1, var2 - var5, var3, var4, false);
   }

   private void renderString(String var1, double var2, double var4, int var6, boolean var7) {
      if (var1 != "" && var1.length() != 0) {
         GL11.glPushMatrix();
         GlStateManager.scale(0.5D, 0.5D, 1.0D);
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(770, 771);
         var2 -= 2.0D;
         var4 -= 2.0D;
         var2 += 0.5D;
         var4 += 0.5D;
         var2 *= 2.0D;
         var4 *= 2.0D;
         TTFFontRenderer.CharacterData[] var8 = this.regularData;
         boolean var9 = false;
         boolean var10 = false;
         boolean var11 = false;
         int var12 = var1.length();
         double var13 = 255.0D * (double)(var7 ? 4 : 1);
         Color var15 = new Color(var6);
         GL11.glColor4d((double)var15.getRed() / var13, (double)var15.getGreen() / var13, (double)var15.getBlue() / var13, (double)var15.getAlpha());

         for(int var16 = 0; var16 < var12; ++var16) {
            char var18 = var1.charAt(var16);
            char var17 = var16 > 0 ? var1.charAt(var16 - 1) : 46;
            if (var17 != 167) {
               if (var18 == 167 && var16 < var12) {
                  int var22 = "0123456789abcdefklmnor".indexOf(var1.toLowerCase(Locale.ENGLISH).charAt(var16 + 1));
                  if (var22 >= 16) {
                     if (var22 == 16) {
                        var11 = true;
                     } else if (var22 == 17) {
                        var8 = this.boldData;
                     } else if (var22 == 18) {
                        var10 = true;
                     } else if (var22 == 19) {
                        var9 = true;
                     } else if (var22 == 20) {
                        var8 = this.italicsData;
                     } else if (var22 == 21) {
                        var11 = false;
                        var10 = false;
                        var9 = false;
                        var8 = this.regularData;
                        GL11.glColor4d(1.0D * (var7 ? 0.25D : 1.0D), 1.0D * (var7 ? 0.25D : 1.0D), 1.0D * (var7 ? 0.25D : 1.0D), (double)(var6 >> 24 & 255) / 255.0D);
                     }
                  } else {
                     var11 = false;
                     var10 = false;
                     var9 = false;
                     var8 = this.regularData;
                     if (var22 < 0 || var22 > 15) {
                        var22 = 15;
                     }

                     if (var7) {
                        var22 += 16;
                     }

                     int var21 = this.colorCodes[var22];
                     GL11.glColor4d((double)(var21 >> 16) / 255.0D, (double)(var21 >> 8 & 255) / 255.0D, (double)(var21 & 255) / 255.0D, (double)(var6 >> 24 & 255) / 255.0D);
                  }
               } else if (var18 <= 255) {
                  if (var11) {
                     var18 = (char)(var18 + RANDOM_OFFSET);
                  }

                  this.drawChar(var18, var8, var2, var4);
                  TTFFontRenderer.CharacterData var20 = var8[var18];
                  if (var10) {
                     this.drawLine(new Vector2f(0.0F, var20.height / 2.0F), new Vector2f(var20.width, var20.height / 2.0F), 3.0F);
                  }

                  if (var9) {
                     this.drawLine(new Vector2f(0.0F, var20.height - 15.0F), new Vector2f(var20.width, var20.height - 15.0F), 3.0F);
                  }

                  var2 += (double)(var20.width - 8.0F);
               }
            }
         }

         GL11.glPopMatrix();
         GlStateManager.disableBlend();
         GlStateManager.bindTexture(0);
         GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
      }
   }

   private void renderString(String var1, float var2, float var3, int var4, boolean var5) {
      if (var1 != "" && var1.length() != 0) {
         GL11.glPushMatrix();
         GlStateManager.scale(0.5D, 0.5D, 1.0D);
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(770, 771);
         var2 -= 2.0F;
         var3 -= 2.0F;
         var2 += 0.5F;
         var3 += 0.5F;
         var2 *= 2.0F;
         var3 *= 2.0F;
         TTFFontRenderer.CharacterData[] var6 = this.regularData;
         boolean var7 = false;
         boolean var8 = false;
         boolean var9 = false;
         int var10 = var1.length();
         double var11 = 255.0D * (double)(var5 ? 4 : 1);
         Color var13 = new Color(var4);
         GL11.glColor4d((double)var13.getRed() / var11, (double)var13.getGreen() / var11, (double)var13.getBlue() / var11, (double)var13.getAlpha());

         for(int var14 = 0; var14 < var10; ++var14) {
            char var16 = var1.charAt(var14);
            char var15 = var14 > 0 ? var1.charAt(var14 - 1) : 46;
            if (var15 != 167) {
               if (var16 == 167 && var14 < var10) {
                  int var21 = "0123456789abcdefklmnor".indexOf(var1.toLowerCase(Locale.ENGLISH).charAt(var14 + 1));
                  if (var21 >= 16) {
                     if (var21 == 16) {
                        var9 = true;
                     } else if (var21 == 17) {
                        var6 = this.boldData;
                     } else if (var21 == 18) {
                        var8 = true;
                     } else if (var21 == 19) {
                        var7 = true;
                     } else if (var21 == 20) {
                        var6 = this.italicsData;
                     } else if (var21 == 21) {
                        var9 = false;
                        var8 = false;
                        var7 = false;
                        var6 = this.regularData;
                        GL11.glColor4d(1.0D * (var5 ? 0.25D : 1.0D), 1.0D * (var5 ? 0.25D : 1.0D), 1.0D * (var5 ? 0.25D : 1.0D), (double)(var4 >> 24 & 255) / 255.0D);
                     }
                  } else {
                     var9 = false;
                     var8 = false;
                     var7 = false;
                     var6 = this.regularData;
                     if (var21 < 0 || var21 > 15) {
                        var21 = 15;
                     }

                     if (var5) {
                        var21 += 16;
                     }

                     int var19 = this.colorCodes[var21];
                     GL11.glColor4d((double)(var19 >> 16) / 255.0D, (double)(var19 >> 8 & 255) / 255.0D, (double)(var19 & 255) / 255.0D, (double)(var4 >> 24 & 255) / 255.0D);
                  }
               } else if (var16 <= 255) {
                  if (var9) {
                     var16 = (char)(var16 + RANDOM_OFFSET);
                  }

                  this.drawChar(var16, var6, var2, var3);
                  TTFFontRenderer.CharacterData var18 = var6[var16];
                  if (var8) {
                     this.drawLine(new Vector2f(0.0F, var18.height / 2.0F), new Vector2f(var18.width, var18.height / 2.0F), 3.0F);
                  }

                  if (var7) {
                     this.drawLine(new Vector2f(0.0F, var18.height - 15.0F), new Vector2f(var18.width, var18.height - 15.0F), 3.0F);
                  }

                  var2 += var18.width - 8.0F;
               }
            }
         }

         GL11.glPopMatrix();
         GlStateManager.disableBlend();
         GlStateManager.bindTexture(0);
         GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
      }
   }

   private void drawChar(char var1, TTFFontRenderer.CharacterData[] var2, float var3, float var4) {
      TTFFontRenderer.CharacterData var5 = var2[var1];
      var5.bind();
      GL11.glBegin(7);
      GL11.glTexCoord2f(0.0F, 0.0F);
      GL11.glVertex2d((double)var3, (double)var4);
      GL11.glTexCoord2f(0.0F, 1.0F);
      GL11.glVertex2d((double)var3, (double)(var4 + var5.height));
      GL11.glTexCoord2f(1.0F, 1.0F);
      GL11.glVertex2d((double)(var3 + var5.width), (double)(var4 + var5.height));
      GL11.glTexCoord2f(1.0F, 0.0F);
      GL11.glVertex2d((double)(var3 + var5.width), (double)var4);
      GL11.glEnd();
   }

   private void createTexture(int var1, BufferedImage var2) {
      int[] var3 = new int[var2.getWidth() * var2.getHeight()];
      var2.getRGB(0, 0, var2.getWidth(), var2.getHeight(), var3, 0, var2.getWidth());
      ByteBuffer var4 = BufferUtils.createByteBuffer(var2.getWidth() * var2.getHeight() * 4);

      for(int var5 = 0; var5 < var2.getHeight(); ++var5) {
         for(int var6 = 0; var6 < var2.getWidth(); ++var6) {
            int var7 = var3[var5 * var2.getWidth() + var6];
            var4.put((byte)(var7 >> 16 & 255));
            var4.put((byte)(var7 >> 8 & 255));
            var4.put((byte)(var7 & 255));
            var4.put((byte)(var7 >> 24 & 255));
         }
      }

      var4.flip();
      GlStateManager.bindTexture(var1);
      GL11.glTexParameteri(3553, 10241, 9728);
      GL11.glTexParameteri(3553, 10240, 9728);
      GL11.glTexImage2D(3553, 0, 6408, var2.getWidth(), var2.getHeight(), 0, 6408, 5121, (ByteBuffer)var4);
   }

   public TTFFontRenderer(Font var1, int var2) {
      this(var1, var2, true);
   }

   public void drawString(String var1, float var2, float var3, int var4) {
      this.renderString(var1, var2, var3, var4, false);
   }

   private TTFFontRenderer.CharacterData[] setup(TTFFontRenderer.CharacterData[] var1, int var2) {
      this.generateColors();
      Font var3 = this.font.deriveFont(var2);
      BufferedImage var4 = new BufferedImage(1, 1, 2);
      Graphics2D var5 = (Graphics2D)var4.getGraphics();
      var5.setFont(var3);
      FontMetrics var6 = var5.getFontMetrics();

      for(int var7 = 0; var7 < var1.length; ++var7) {
         char var8 = (char)var7;
         Rectangle2D var9 = var6.getStringBounds("" + var8, var5);
         float var10 = (float)var9.getWidth() + 8.0F;
         float var11 = (float)var9.getHeight();
         BufferedImage var12 = new BufferedImage(MathHelper.ceiling_double_int((double)var10), MathHelper.ceiling_double_int((double)var11), 2);
         Graphics2D var13 = (Graphics2D)var12.getGraphics();
         var13.setFont(var3);
         var13.setColor(new Color(255, 255, 255, 0));
         var13.fillRect(0, 0, var12.getWidth(), var12.getHeight());
         var13.setColor(Color.WHITE);
         var13.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
         var13.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         var13.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
         var13.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, this.fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
         var13.drawString("" + var8, 4, var6.getAscent());
         int var14 = GL11.glGenTextures();
         this.createTexture(var14, var12);
         var1[var7] = new TTFFontRenderer.CharacterData(this, var8, (float)var12.getWidth(), (float)var12.getHeight(), var14);
      }

      return var1;
   }

   public TTFFontRenderer(Font var1) {
      this(var1, 256);
   }

   private void drawLine(Vector2f var1, Vector2f var2, float var3) {
      GL11.glDisable(3553);
      GL11.glLineWidth(var3);
      GL11.glBegin(1);
      GL11.glVertex2f(var1.x, var1.y);
      GL11.glVertex2f(var2.x, var2.y);
      GL11.glEnd();
      GL11.glEnable(3553);
   }

   public float getHeight(String var1) {
      float var2 = 0.0F;
      TTFFontRenderer.CharacterData[] var3 = this.regularData;
      int var4 = var1.length();

      for(int var5 = 0; var5 < var4; ++var5) {
         char var7 = var1.charAt(var5);
         char var6 = var5 > 0 ? var1.charAt(var5 - 1) : 46;
         if (var6 != 167) {
            if (var7 == 167 && var5 < var4) {
               int var10 = "0123456789abcdefklmnor".indexOf(var1.toLowerCase(Locale.ENGLISH).charAt(var5 + 1));
               if (var10 == 17) {
                  var3 = this.boldData;
               } else if (var10 == 20) {
                  var3 = this.italicsData;
               } else if (var10 == 21) {
                  var3 = this.regularData;
               }
            } else if (var7 <= 255) {
               TTFFontRenderer.CharacterData var9 = var3[var7];
               var2 = Math.max(var2, var9.height);
            }
         }
      }

      return var2 / 2.0F - 2.0F;
   }

   public void drawString(String var1, double var2, double var4, int var6) {
      this.renderString(var1, var2, var4, var6, false);
   }

   private void generateColors() {
      for(int var1 = 0; var1 < 32; ++var1) {
         int var2 = (var1 >> 3 & 1) * 85;
         int var3 = (var1 >> 2 & 1) * 170 + var2;
         int var4 = (var1 >> 1 & 1) * 170 + var2;
         int var5 = (var1 >> 0 & 1) * 170 + var2;
         if (var1 == 6) {
            var3 += 85;
         }

         if (var1 >= 16) {
            var3 /= 4;
            var4 /= 4;
            var5 /= 4;
         }

         this.colorCodes[var1] = (var3 & 255) << 16 | (var4 & 255) << 8 | var5 & 255;
      }

   }

   public TTFFontRenderer(Font var1, int var2, boolean var3) {
      this.fractionalMetrics = false;
      this.colorCodes = new int[32];
      this.font = var1;
      this.fractionalMetrics = var3;
      this.regularData = this.setup(new TTFFontRenderer.CharacterData[var2], 0);
      this.boldData = this.setup(new TTFFontRenderer.CharacterData[var2], 1);
      this.italicsData = this.setup(new TTFFontRenderer.CharacterData[var2], 2);
   }

   public float getWidth(String var1) {
      float var2 = 0.0F;
      TTFFontRenderer.CharacterData[] var3 = this.regularData;
      int var4 = var1.length();

      for(int var5 = 0; var5 < var4; ++var5) {
         char var7 = var1.charAt(var5);
         char var6 = var5 > 0 ? var1.charAt(var5 - 1) : 46;
         if (var6 != 167) {
            if (var7 == 167 && var5 < var4) {
               int var10 = "0123456789abcdefklmnor".indexOf(var1.toLowerCase(Locale.ENGLISH).charAt(var5 + 1));
               if (var10 == 17) {
                  var3 = this.boldData;
               } else if (var10 == 20) {
                  var3 = this.italicsData;
               } else if (var10 == 21) {
                  var3 = this.regularData;
               }
            } else if (var7 <= 255) {
               TTFFontRenderer.CharacterData var9 = var3[var7];
               var2 += (var9.width - 8.0F) / 2.0F;
            }
         }
      }

      return var2 + 2.0F;
   }

   public TTFFontRenderer(Font var1, boolean var2) {
      this(var1, 256, var2);
   }

   class CharacterData {
      private int textureId;
      public char character;
      public float width;
      final TTFFontRenderer this$0;
      public float height;

      public CharacterData(TTFFontRenderer var1, char var2, float var3, float var4, int var5) {
         this.this$0 = var1;
         this.character = var2;
         this.width = var3;
         this.height = var4;
         this.textureId = var5;
      }

      public void bind() {
         GL11.glBindTexture(3553, this.textureId);
      }
   }
}
