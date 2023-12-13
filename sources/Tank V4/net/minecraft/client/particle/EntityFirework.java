package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemDye;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFirework {
   public static class OverlayFX extends EntityFX {
      public void renderParticle(WorldRenderer var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
         float var9 = 0.25F;
         float var10 = 0.5F;
         float var11 = 0.125F;
         float var12 = 0.375F;
         float var13 = 7.1F * MathHelper.sin(((float)this.particleAge + var3 - 1.0F) * 0.25F * 3.1415927F);
         this.particleAlpha = 0.6F - ((float)this.particleAge + var3 - 1.0F) * 0.25F * 0.5F;
         float var14 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)var3 - interpPosX);
         float var15 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)var3 - interpPosY);
         float var16 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)var3 - interpPosZ);
         int var17 = this.getBrightnessForRender(var3);
         int var18 = var17 >> 16 & '\uffff';
         int var19 = var17 & '\uffff';
         var1.pos((double)(var14 - var4 * var13 - var7 * var13), (double)(var15 - var5 * var13), (double)(var16 - var6 * var13 - var8 * var13)).tex(0.5D, 0.375D).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(var18, var19).endVertex();
         var1.pos((double)(var14 - var4 * var13 + var7 * var13), (double)(var15 + var5 * var13), (double)(var16 - var6 * var13 + var8 * var13)).tex(0.5D, 0.125D).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(var18, var19).endVertex();
         var1.pos((double)(var14 + var4 * var13 + var7 * var13), (double)(var15 + var5 * var13), (double)(var16 + var6 * var13 + var8 * var13)).tex(0.25D, 0.125D).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(var18, var19).endVertex();
         var1.pos((double)(var14 + var4 * var13 - var7 * var13), (double)(var15 - var5 * var13), (double)(var16 + var6 * var13 - var8 * var13)).tex(0.25D, 0.375D).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(var18, var19).endVertex();
      }

      protected OverlayFX(World var1, double var2, double var4, double var6) {
         super(var1, var2, var4, var6);
         this.particleMaxAge = 4;
      }
   }

   public static class StarterFX extends EntityFX {
      private final EffectRenderer theEffectRenderer;
      private NBTTagList fireworkExplosions;
      boolean twinkle;
      private int fireworkAge;

      private void createShaped(double var1, double[][] var3, int[] var4, int[] var5, boolean var6, boolean var7, boolean var8) {
         double var9 = var3[0][0];
         double var11 = var3[0][1];
         this.createParticle(this.posX, this.posY, this.posZ, var9 * var1, var11 * var1, 0.0D, var4, var5, var6, var7);
         float var13 = this.rand.nextFloat() * 3.1415927F;
         double var14 = var8 ? 0.034D : 0.34D;

         for(int var16 = 0; var16 < 3; ++var16) {
            double var17 = (double)var13 + (double)((float)var16 * 3.1415927F) * var14;
            double var19 = var9;
            double var21 = var11;

            for(int var23 = 1; var23 < var3.length; ++var23) {
               double var24 = var3[var23][0];
               double var26 = var3[var23][1];

               for(double var28 = 0.25D; var28 <= 1.0D; var28 += 0.25D) {
                  double var30 = (var19 + (var24 - var19) * var28) * var1;
                  double var32 = (var21 + (var26 - var21) * var28) * var1;
                  double var34 = var30 * Math.sin(var17);
                  var30 *= Math.cos(var17);

                  for(double var36 = -1.0D; var36 <= 1.0D; var36 += 2.0D) {
                     this.createParticle(this.posX, this.posY, this.posZ, var30 * var36, var32, var34 * var36, var4, var5, var6, var7);
                  }
               }

               var19 = var24;
               var21 = var26;
            }
         }

      }

      private void createParticle(double var1, double var3, double var5, double var7, double var9, double var11, int[] var13, int[] var14, boolean var15, boolean var16) {
         EntityFirework.SparkFX var17 = new EntityFirework.SparkFX(this.worldObj, var1, var3, var5, var7, var9, var11, this.theEffectRenderer);
         var17.setAlphaF(0.99F);
         var17.setTrail(var15);
         var17.setTwinkle(var16);
         int var18 = this.rand.nextInt(var13.length);
         var17.setColour(var13[var18]);
         if (var14 != null && var14.length > 0) {
            var17.setFadeColour(var14[this.rand.nextInt(var14.length)]);
         }

         this.theEffectRenderer.addEffect(var17);
      }

      public StarterFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12, EffectRenderer var14, NBTTagCompound var15) {
         super(var1, var2, var4, var6, 0.0D, 0.0D, 0.0D);
         this.motionX = var8;
         this.motionY = var10;
         this.motionZ = var12;
         this.theEffectRenderer = var14;
         this.particleMaxAge = 8;
         if (var15 != null) {
            this.fireworkExplosions = var15.getTagList("Explosions", 10);
            if (this.fireworkExplosions.tagCount() == 0) {
               this.fireworkExplosions = null;
            } else {
               this.particleMaxAge = this.fireworkExplosions.tagCount() * 2 - 1;

               for(int var16 = 0; var16 < this.fireworkExplosions.tagCount(); ++var16) {
                  NBTTagCompound var17 = this.fireworkExplosions.getCompoundTagAt(var16);
                  if (var17.getBoolean("Flicker")) {
                     this.twinkle = true;
                     this.particleMaxAge += 15;
                     break;
                  }
               }
            }
         }

      }

      public void onUpdate() {
         boolean var1;
         if (this.fireworkAge == 0 && this.fireworkExplosions != null) {
            var1 = this.func_92037_i();
            boolean var2 = false;
            if (this.fireworkExplosions.tagCount() >= 3) {
               var2 = true;
            } else {
               for(int var3 = 0; var3 < this.fireworkExplosions.tagCount(); ++var3) {
                  NBTTagCompound var4 = this.fireworkExplosions.getCompoundTagAt(var3);
                  if (var4.getByte("Type") == 1) {
                     var2 = true;
                     break;
                  }
               }
            }

            String var15 = "fireworks." + (var2 ? "largeBlast" : "blast") + (var1 ? "_far" : "");
            this.worldObj.playSound(this.posX, this.posY, this.posZ, var15, 20.0F, 0.95F + this.rand.nextFloat() * 0.1F, true);
         }

         if (this.fireworkAge % 2 == 0 && this.fireworkExplosions != null && this.fireworkAge / 2 < this.fireworkExplosions.tagCount()) {
            int var13 = this.fireworkAge / 2;
            NBTTagCompound var14 = this.fireworkExplosions.getCompoundTagAt(var13);
            byte var17 = var14.getByte("Type");
            boolean var18 = var14.getBoolean("Trail");
            boolean var5 = var14.getBoolean("Flicker");
            int[] var6 = var14.getIntArray("Colors");
            int[] var7 = var14.getIntArray("FadeColors");
            if (var6.length == 0) {
               var6 = new int[]{ItemDye.dyeColors[0]};
            }

            if (var17 == 1) {
               this.createBall(0.5D, 4, var6, var7, var18, var5);
            } else if (var17 == 2) {
               this.createShaped(0.5D, new double[][]{{0.0D, 1.0D}, {0.3455D, 0.309D}, {0.9511D, 0.309D}, {0.3795918367346939D, -0.12653061224489795D}, {0.6122448979591837D, -0.8040816326530612D}, {0.0D, -0.35918367346938773D}}, var6, var7, var18, var5, false);
            } else if (var17 == 3) {
               this.createShaped(0.5D, new double[][]{{0.0D, 0.2D}, {0.2D, 0.2D}, {0.2D, 0.6D}, {0.6D, 0.6D}, {0.6D, 0.2D}, {0.2D, 0.2D}, {0.2D, 0.0D}, {0.4D, 0.0D}, {0.4D, -0.6D}, {0.2D, -0.6D}, {0.2D, -0.4D}, {0.0D, -0.4D}}, var6, var7, var18, var5, true);
            } else if (var17 == 4) {
               this.createBurst(var6, var7, var18, var5);
            } else {
               this.createBall(0.25D, 2, var6, var7, var18, var5);
            }

            int var8 = var6[0];
            float var9 = (float)((var8 & 16711680) >> 16) / 255.0F;
            float var10 = (float)((var8 & '\uff00') >> 8) / 255.0F;
            float var11 = (float)((var8 & 255) >> 0) / 255.0F;
            EntityFirework.OverlayFX var12 = new EntityFirework.OverlayFX(this.worldObj, this.posX, this.posY, this.posZ);
            var12.setRBGColorF(var9, var10, var11);
            this.theEffectRenderer.addEffect(var12);
         }

         ++this.fireworkAge;
         if (this.fireworkAge > this.particleMaxAge) {
            if (this.twinkle) {
               var1 = this.func_92037_i();
               String var16 = "fireworks." + (var1 ? "twinkle_far" : "twinkle");
               this.worldObj.playSound(this.posX, this.posY, this.posZ, var16, 20.0F, 0.9F + this.rand.nextFloat() * 0.15F, true);
            }

            this.setDead();
         }

      }

      private void createBurst(int[] var1, int[] var2, boolean var3, boolean var4) {
         double var5 = this.rand.nextGaussian() * 0.05D;
         double var7 = this.rand.nextGaussian() * 0.05D;

         for(int var9 = 0; var9 < 70; ++var9) {
            double var10 = this.motionX * 0.5D + this.rand.nextGaussian() * 0.15D + var5;
            double var12 = this.motionZ * 0.5D + this.rand.nextGaussian() * 0.15D + var7;
            double var14 = this.motionY * 0.5D + this.rand.nextDouble() * 0.5D;
            this.createParticle(this.posX, this.posY, this.posZ, var10, var14, var12, var1, var2, var3, var4);
         }

      }

      public int getFXLayer() {
         return 0;
      }

      private void createBall(double var1, int var3, int[] var4, int[] var5, boolean var6, boolean var7) {
         double var8 = this.posX;
         double var10 = this.posY;
         double var12 = this.posZ;

         for(int var14 = -var3; var14 <= var3; ++var14) {
            for(int var15 = -var3; var15 <= var3; ++var15) {
               for(int var16 = -var3; var16 <= var3; ++var16) {
                  double var17 = (double)var15 + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5D;
                  double var19 = (double)var14 + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5D;
                  double var21 = (double)var16 + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5D;
                  double var23 = (double)MathHelper.sqrt_double(var17 * var17 + var19 * var19 + var21 * var21) / var1 + this.rand.nextGaussian() * 0.05D;
                  this.createParticle(var8, var10, var12, var17 / var23, var19 / var23, var21 / var23, var4, var5, var6, var7);
                  if (var14 != -var3 && var14 != var3 && var15 != -var3 && var15 != var3) {
                     var16 += var3 * 2 - 1;
                  }
               }
            }
         }

      }

      public void renderParticle(WorldRenderer var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      }

      private boolean func_92037_i() {
         Minecraft var1 = Minecraft.getMinecraft();
         return var1 == null || var1.getRenderViewEntity() == null || var1.getRenderViewEntity().getDistanceSq(this.posX, this.posY, this.posZ) >= 256.0D;
      }
   }

   public static class SparkFX extends EntityFX {
      private boolean trail;
      private float fadeColourGreen;
      private boolean hasFadeColour;
      private int baseTextureIndex = 160;
      private final EffectRenderer field_92047_az;
      private float fadeColourBlue;
      private boolean twinkle;
      private float fadeColourRed;

      public int getBrightnessForRender(float var1) {
         return 15728880;
      }

      public void setTwinkle(boolean var1) {
         this.twinkle = var1;
      }

      public void onUpdate() {
         this.prevPosX = this.posX;
         this.prevPosY = this.posY;
         this.prevPosZ = this.posZ;
         if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
         }

         if (this.particleAge > this.particleMaxAge / 2) {
            this.setAlphaF(1.0F - ((float)this.particleAge - (float)(this.particleMaxAge / 2)) / (float)this.particleMaxAge);
            if (this.hasFadeColour) {
               this.particleRed += (this.fadeColourRed - this.particleRed) * 0.2F;
               this.particleGreen += (this.fadeColourGreen - this.particleGreen) * 0.2F;
               this.particleBlue += (this.fadeColourBlue - this.particleBlue) * 0.2F;
            }
         }

         this.setParticleTextureIndex(this.baseTextureIndex + (7 - this.particleAge * 8 / this.particleMaxAge));
         this.motionY -= 0.004D;
         this.moveEntity(this.motionX, this.motionY, this.motionZ);
         this.motionX *= 0.9100000262260437D;
         this.motionY *= 0.9100000262260437D;
         this.motionZ *= 0.9100000262260437D;
         if (this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
         }

         if (this.trail && this.particleAge < this.particleMaxAge / 2 && (this.particleAge + this.particleMaxAge) % 2 == 0) {
            EntityFirework.SparkFX var1 = new EntityFirework.SparkFX(this.worldObj, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, this.field_92047_az);
            var1.setAlphaF(0.99F);
            var1.setRBGColorF(this.particleRed, this.particleGreen, this.particleBlue);
            var1.particleAge = var1.particleMaxAge / 2;
            if (this.hasFadeColour) {
               var1.hasFadeColour = true;
               var1.fadeColourRed = this.fadeColourRed;
               var1.fadeColourGreen = this.fadeColourGreen;
               var1.fadeColourBlue = this.fadeColourBlue;
            }

            var1.twinkle = this.twinkle;
            this.field_92047_az.addEffect(var1);
         }

      }

      public SparkFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12, EffectRenderer var14) {
         super(var1, var2, var4, var6);
         this.motionX = var8;
         this.motionY = var10;
         this.motionZ = var12;
         this.field_92047_az = var14;
         this.particleScale *= 0.75F;
         this.particleMaxAge = 48 + this.rand.nextInt(12);
         this.noClip = false;
      }

      public void renderParticle(WorldRenderer var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
         if (!this.twinkle || this.particleAge < this.particleMaxAge / 3 || (this.particleAge + this.particleMaxAge) / 3 % 2 == 0) {
            super.renderParticle(var1, var2, var3, var4, var5, var6, var7, var8);
         }

      }

      public float getBrightness(float var1) {
         return 1.0F;
      }

      public void setTrail(boolean var1) {
         this.trail = var1;
      }

      public AxisAlignedBB getCollisionBoundingBox() {
         return null;
      }

      public void setFadeColour(int var1) {
         this.fadeColourRed = (float)((var1 & 16711680) >> 16) / 255.0F;
         this.fadeColourGreen = (float)((var1 & '\uff00') >> 8) / 255.0F;
         this.fadeColourBlue = (float)((var1 & 255) >> 0) / 255.0F;
         this.hasFadeColour = true;
      }

      public void setColour(int var1) {
         float var2 = (float)((var1 & 16711680) >> 16) / 255.0F;
         float var3 = (float)((var1 & '\uff00') >> 8) / 255.0F;
         float var4 = (float)((var1 & 255) >> 0) / 255.0F;
         float var5 = 1.0F;
         this.setRBGColorF(var2 * var5, var3 * var5, var4 * var5);
      }

      public boolean canBePushed() {
         return false;
      }
   }

   public static class Factory implements IParticleFactory {
      public EntityFX getEntityFX(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         EntityFirework.SparkFX var16 = new EntityFirework.SparkFX(var2, var3, var5, var7, var9, var11, var13, Minecraft.getMinecraft().effectRenderer);
         var16.setAlphaF(0.99F);
         return var16;
      }
   }
}
