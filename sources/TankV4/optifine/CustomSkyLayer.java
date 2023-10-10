package optifine;

import java.util.Properties;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.world.World;

public class CustomSkyLayer {
   public String source = null;
   private int startFadeIn = -1;
   private int endFadeIn = -1;
   private int startFadeOut = -1;
   private int endFadeOut = -1;
   private int blend = 1;
   private boolean rotate = false;
   private float speed = 1.0F;
   private float[] axis;
   private RangeListInt days;
   private int daysLoop;
   public int textureId;
   public static final float[] DEFAULT_AXIS = new float[]{1.0F, 0.0F, 0.0F};

   public CustomSkyLayer(Properties var1, String var2) {
      this.axis = DEFAULT_AXIS;
      this.days = null;
      this.daysLoop = 8;
      this.textureId = -1;
      ConnectedParser var3 = new ConnectedParser("CustomSky");
      this.source = var1.getProperty("source", var2);
      this.startFadeIn = this.parseTime(var1.getProperty("startFadeIn"));
      this.endFadeIn = this.parseTime(var1.getProperty("endFadeIn"));
      this.startFadeOut = this.parseTime(var1.getProperty("startFadeOut"));
      this.endFadeOut = this.parseTime(var1.getProperty("endFadeOut"));
      this.blend = Blender.parseBlend(var1.getProperty("blend"));
      this.rotate = this.parseBoolean(var1.getProperty("rotate"), true);
      this.speed = this.parseFloat(var1.getProperty("speed"), 1.0F);
      this.axis = this.parseAxis(var1.getProperty("axis"), DEFAULT_AXIS);
      this.days = var3.parseRangeListInt(var1.getProperty("days"));
      this.daysLoop = var3.parseInt(var1.getProperty("daysLoop"), 8);
   }

   private int parseTime(String var1) {
      if (var1 == null) {
         return -1;
      } else {
         String[] var2 = Config.tokenize(var1, ":");
         if (var2.length != 2) {
            Config.warn("Invalid time: " + var1);
            return -1;
         } else {
            String var3 = var2[0];
            String var4 = var2[1];
            int var5 = Config.parseInt(var3, -1);
            int var6 = Config.parseInt(var4, -1);
            if (var5 >= 0 && var5 <= 23 && var6 >= 0 && var6 <= 59) {
               var5 -= 6;
               if (var5 < 0) {
                  var5 += 24;
               }

               int var7 = var5 * 1000 + (int)((double)var6 / 60.0D * 1000.0D);
               return var7;
            } else {
               Config.warn("Invalid time: " + var1);
               return -1;
            }
         }
      }
   }

   private boolean parseBoolean(String var1, boolean var2) {
      if (var1 == null) {
         return var2;
      } else if (var1.toLowerCase().equals("true")) {
         return true;
      } else if (var1.toLowerCase().equals("false")) {
         return false;
      } else {
         Config.warn("Unknown boolean: " + var1);
         return var2;
      }
   }

   private float parseFloat(String var1, float var2) {
      if (var1 == null) {
         return var2;
      } else {
         float var3 = Config.parseFloat(var1, Float.MIN_VALUE);
         if (var3 == Float.MIN_VALUE) {
            Config.warn("Invalid value: " + var1);
            return var2;
         } else {
            return var3;
         }
      }
   }

   private float[] parseAxis(String var1, float[] var2) {
      if (var1 == null) {
         return var2;
      } else {
         String[] var3 = Config.tokenize(var1, " ");
         if (var3.length != 3) {
            Config.warn("Invalid axis: " + var1);
            return var2;
         } else {
            float[] var4 = new float[3];

            for(int var5 = 0; var5 < var3.length; ++var5) {
               var4[var5] = Config.parseFloat(var3[var5], Float.MIN_VALUE);
               if (var4[var5] == Float.MIN_VALUE) {
                  Config.warn("Invalid axis: " + var1);
                  return var2;
               }

               if (var4[var5] < -1.0F || var4[var5] > 1.0F) {
                  Config.warn("Invalid axis values: " + var1);
                  return var2;
               }
            }

            float var9 = var4[0];
            float var6 = var4[1];
            float var7 = var4[2];
            if (var9 * var9 + var6 * var6 + var7 * var7 < 1.0E-5F) {
               Config.warn("Invalid axis values: " + var1);
               return var2;
            } else {
               float[] var8 = new float[]{var7, var6, -var9};
               return var8;
            }
         }
      }
   }

   public boolean isValid(String var1) {
      if (this.source == null) {
         Config.warn("No source texture: " + var1);
         return false;
      } else {
         this.source = TextureUtils.fixResourcePath(this.source, TextureUtils.getBasePath(var1));
         if (this.startFadeIn >= 0 && this.endFadeIn >= 0 && this.endFadeOut >= 0) {
            int var2 = this.normalizeTime(this.endFadeIn - this.startFadeIn);
            if (this.startFadeOut < 0) {
               this.startFadeOut = this.normalizeTime(this.endFadeOut - var2);
               int var10001 = this.startFadeOut;
               if (this.startFadeIn <= this.endFadeIn) {
                  this.startFadeOut = this.endFadeIn;
               }
            }

            int var3 = this.normalizeTime(this.startFadeOut - this.endFadeIn);
            int var4 = this.normalizeTime(this.endFadeOut - this.startFadeOut);
            int var5 = this.normalizeTime(this.startFadeIn - this.endFadeOut);
            int var6 = var2 + var3 + var4 + var5;
            if (var6 != 24000) {
               Config.warn("Invalid fadeIn/fadeOut times, sum is not 24h: " + var6);
               return false;
            } else if (this.speed < 0.0F) {
               Config.warn("Invalid speed: " + this.speed);
               return false;
            } else if (this.daysLoop <= 0) {
               Config.warn("Invalid daysLoop: " + this.daysLoop);
               return false;
            } else {
               return true;
            }
         } else {
            Config.warn("Invalid times, required are: startFadeIn, endFadeIn and endFadeOut.");
            return false;
         }
      }
   }

   private int normalizeTime(int var1) {
      while(var1 >= 24000) {
         var1 -= 24000;
      }

      while(var1 < 0) {
         var1 += 24000;
      }

      return var1;
   }

   public void render(int var1, float var2, float var3) {
      float var4 = var3 * this.getFadeBrightness(var1);
      var4 = Config.limit(var4, 0.0F, 1.0F);
      if (var4 >= 1.0E-4F) {
         GlStateManager.bindTexture(this.textureId);
         Blender.setupBlend(this.blend, var4);
         GlStateManager.pushMatrix();
         if (this.rotate) {
            GlStateManager.rotate(var2 * 360.0F * this.speed, this.axis[0], this.axis[1], this.axis[2]);
         }

         Tessellator var5 = Tessellator.getInstance();
         GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
         this.renderSide(var5, 4);
         GlStateManager.pushMatrix();
         GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
         this.renderSide(var5, 1);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
         this.renderSide(var5, 0);
         GlStateManager.popMatrix();
         GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
         this.renderSide(var5, 5);
         GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
         this.renderSide(var5, 2);
         GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
         this.renderSide(var5, 3);
         GlStateManager.popMatrix();
      }

   }

   private float getFadeBrightness(int var1) {
      int var2;
      int var3;
      if (this.startFadeIn <= this.endFadeIn) {
         var2 = this.normalizeTime(this.endFadeIn - this.startFadeIn);
         var3 = this.normalizeTime(var1 - this.startFadeIn);
         return (float)var3 / (float)var2;
      } else if (this.endFadeIn <= this.startFadeOut) {
         return 1.0F;
      } else if (this.startFadeOut <= this.endFadeOut) {
         var2 = this.normalizeTime(this.endFadeOut - this.startFadeOut);
         var3 = this.normalizeTime(var1 - this.startFadeOut);
         return 1.0F - (float)var3 / (float)var2;
      } else {
         return 0.0F;
      }
   }

   private void renderSide(Tessellator var1, int var2) {
      WorldRenderer var3 = var1.getWorldRenderer();
      double var4 = (double)(var2 % 3) / 3.0D;
      double var6 = (double)(var2 / 3) / 2.0D;
      var3.begin(7, DefaultVertexFormats.POSITION_TEX);
      var3.pos(-100.0D, -100.0D, -100.0D).tex(var4, var6).endVertex();
      var3.pos(-100.0D, -100.0D, 100.0D).tex(var4, var6 + 0.5D).endVertex();
      var3.pos(100.0D, -100.0D, 100.0D).tex(var4 + 0.3333333333333333D, var6 + 0.5D).endVertex();
      var3.pos(100.0D, -100.0D, -100.0D).tex(var4 + 0.3333333333333333D, var6).endVertex();
      var1.draw();
   }

   public boolean isActive(World var1, int var2) {
      if (this.endFadeOut <= this.startFadeIn) {
         return false;
      } else {
         if (this.days != null) {
            long var3 = var1.getWorldTime();

            long var5;
            for(var5 = var3 - (long)this.startFadeIn; var5 < 0L; var5 += (long)(24000 * this.daysLoop)) {
            }

            int var7 = (int)(var5 / 24000L);
            int var8 = var7 % this.daysLoop;
            if (!this.days.isInRange(var8)) {
               return false;
            }
         }

         return true;
      }
   }
}
