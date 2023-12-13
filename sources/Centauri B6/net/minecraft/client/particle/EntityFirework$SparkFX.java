package net.minecraft.client.particle;

import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityFirework$SparkFX extends EntityFX {
   private int baseTextureIndex = 160;
   private boolean trail;
   private boolean twinkle;
   private final EffectRenderer field_92047_az;
   private float fadeColourRed;
   private float fadeColourGreen;
   private float fadeColourBlue;
   private boolean hasFadeColour;

   public EntityFirework$SparkFX(World p_i46465_1_, double p_i46465_2_, double p_i46465_4_, double p_i46465_6_, double p_i46465_8_, double p_i46465_10_, double p_i46465_12_, EffectRenderer p_i46465_14_) {
      super(p_i46465_1_, p_i46465_2_, p_i46465_4_, p_i46465_6_);
      this.motionX = p_i46465_8_;
      this.motionY = p_i46465_10_;
      this.motionZ = p_i46465_12_;
      this.field_92047_az = p_i46465_14_;
      this.particleScale *= 0.75F;
      this.particleMaxAge = 48 + this.rand.nextInt(12);
      this.noClip = false;
   }

   public boolean canBePushed() {
      return false;
   }

   public float getBrightness(float partialTicks) {
      return 1.0F;
   }

   public AxisAlignedBB getCollisionBoundingBox() {
      return null;
   }

   public int getBrightnessForRender(float partialTicks) {
      return 15728880;
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if(this.particleAge++ >= this.particleMaxAge) {
         this.setDead();
      }

      if(this.particleAge > this.particleMaxAge / 2) {
         this.setAlphaF(1.0F - ((float)this.particleAge - (float)(this.particleMaxAge / 2)) / (float)this.particleMaxAge);
         if(this.hasFadeColour) {
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
      if(this.onGround) {
         this.motionX *= 0.699999988079071D;
         this.motionZ *= 0.699999988079071D;
      }

      if(this.trail && this.particleAge < this.particleMaxAge / 2 && (this.particleAge + this.particleMaxAge) % 2 == 0) {
         EntityFirework$SparkFX entityfirework$sparkfx = new EntityFirework$SparkFX(this.worldObj, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, this.field_92047_az);
         entityfirework$sparkfx.setAlphaF(0.99F);
         entityfirework$sparkfx.setRBGColorF(this.particleRed, this.particleGreen, this.particleBlue);
         entityfirework$sparkfx.particleAge = entityfirework$sparkfx.particleMaxAge / 2;
         if(this.hasFadeColour) {
            entityfirework$sparkfx.hasFadeColour = true;
            entityfirework$sparkfx.fadeColourRed = this.fadeColourRed;
            entityfirework$sparkfx.fadeColourGreen = this.fadeColourGreen;
            entityfirework$sparkfx.fadeColourBlue = this.fadeColourBlue;
         }

         entityfirework$sparkfx.twinkle = this.twinkle;
         this.field_92047_az.addEffect(entityfirework$sparkfx);
      }

   }

   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_) {
      if(!this.twinkle || this.particleAge < this.particleMaxAge / 3 || (this.particleAge + this.particleMaxAge) / 3 % 2 == 0) {
         super.renderParticle(worldRendererIn, entityIn, partialTicks, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
      }

   }

   public void setFadeColour(int faceColour) {
      this.fadeColourRed = (float)((faceColour & 16711680) >> 16) / 255.0F;
      this.fadeColourGreen = (float)((faceColour & '\uff00') >> 8) / 255.0F;
      this.fadeColourBlue = (float)((faceColour & 255) >> 0) / 255.0F;
      this.hasFadeColour = true;
   }

   public void setTwinkle(boolean twinkleIn) {
      this.twinkle = twinkleIn;
   }

   public void setTrail(boolean trailIn) {
      this.trail = trailIn;
   }

   public void setColour(int colour) {
      float f = (float)((colour & 16711680) >> 16) / 255.0F;
      float f1 = (float)((colour & '\uff00') >> 8) / 255.0F;
      float f2 = (float)((colour & 255) >> 0) / 255.0F;
      float f3 = 1.0F;
      this.setRBGColorF(f * f3, f1 * f3, f2 * f3);
   }
}
