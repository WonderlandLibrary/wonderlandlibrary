// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.util.SoundEvent;
import net.minecraft.item.ItemDye;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class ParticleFirework
{
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            final Spark particlefirework$spark = new Spark(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, Minecraft.getMinecraft().effectRenderer);
            particlefirework$spark.setAlphaF(0.99f);
            return particlefirework$spark;
        }
    }
    
    public static class Overlay extends Particle
    {
        protected Overlay(final World p_i46466_1_, final double p_i46466_2_, final double p_i46466_4_, final double p_i46466_6_) {
            super(p_i46466_1_, p_i46466_2_, p_i46466_4_, p_i46466_6_);
            this.particleMaxAge = 4;
        }
        
        @Override
        public void renderParticle(final BufferBuilder buffer, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
            final float f = 0.25f;
            final float f2 = 0.5f;
            final float f3 = 0.125f;
            final float f4 = 0.375f;
            final float f5 = 7.1f * MathHelper.sin((this.particleAge + partialTicks - 1.0f) * 0.25f * 3.1415927f);
            this.setAlphaF(0.6f - (this.particleAge + partialTicks - 1.0f) * 0.25f * 0.5f);
            final float f6 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - Overlay.interpPosX);
            final float f7 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - Overlay.interpPosY);
            final float f8 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - Overlay.interpPosZ);
            final int i = this.getBrightnessForRender(partialTicks);
            final int j = i >> 16 & 0xFFFF;
            final int k = i & 0xFFFF;
            buffer.pos(f6 - rotationX * f5 - rotationXY * f5, f7 - rotationZ * f5, f8 - rotationYZ * f5 - rotationXZ * f5).tex(0.5, 0.375).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
            buffer.pos(f6 - rotationX * f5 + rotationXY * f5, f7 + rotationZ * f5, f8 - rotationYZ * f5 + rotationXZ * f5).tex(0.5, 0.125).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
            buffer.pos(f6 + rotationX * f5 + rotationXY * f5, f7 + rotationZ * f5, f8 + rotationYZ * f5 + rotationXZ * f5).tex(0.25, 0.125).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
            buffer.pos(f6 + rotationX * f5 - rotationXY * f5, f7 - rotationZ * f5, f8 + rotationYZ * f5 - rotationXZ * f5).tex(0.25, 0.375).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        }
    }
    
    public static class Spark extends ParticleSimpleAnimated
    {
        private boolean trail;
        private boolean twinkle;
        private final ParticleManager effectRenderer;
        private float fadeColourRed;
        private float fadeColourGreen;
        private float fadeColourBlue;
        private boolean hasFadeColour;
        
        public Spark(final World p_i46465_1_, final double p_i46465_2_, final double p_i46465_4_, final double p_i46465_6_, final double p_i46465_8_, final double p_i46465_10_, final double p_i46465_12_, final ParticleManager p_i46465_14_) {
            super(p_i46465_1_, p_i46465_2_, p_i46465_4_, p_i46465_6_, 160, 8, -0.004f);
            this.motionX = p_i46465_8_;
            this.motionY = p_i46465_10_;
            this.motionZ = p_i46465_12_;
            this.effectRenderer = p_i46465_14_;
            this.particleScale *= 0.75f;
            this.particleMaxAge = 48 + this.rand.nextInt(12);
        }
        
        public void setTrail(final boolean trailIn) {
            this.trail = trailIn;
        }
        
        public void setTwinkle(final boolean twinkleIn) {
            this.twinkle = twinkleIn;
        }
        
        @Override
        public boolean shouldDisableDepth() {
            return true;
        }
        
        @Override
        public void renderParticle(final BufferBuilder buffer, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
            if (!this.twinkle || this.particleAge < this.particleMaxAge / 3 || (this.particleAge + this.particleMaxAge) / 3 % 2 == 0) {
                super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
            }
        }
        
        @Override
        public void onUpdate() {
            super.onUpdate();
            if (this.trail && this.particleAge < this.particleMaxAge / 2 && (this.particleAge + this.particleMaxAge) % 2 == 0) {
                final Spark particlefirework$spark = new Spark(this.world, this.posX, this.posY, this.posZ, 0.0, 0.0, 0.0, this.effectRenderer);
                particlefirework$spark.setAlphaF(0.99f);
                particlefirework$spark.setRBGColorF(this.particleRed, this.particleGreen, this.particleBlue);
                particlefirework$spark.particleAge = particlefirework$spark.particleMaxAge / 2;
                if (this.hasFadeColour) {
                    particlefirework$spark.hasFadeColour = true;
                    particlefirework$spark.fadeColourRed = this.fadeColourRed;
                    particlefirework$spark.fadeColourGreen = this.fadeColourGreen;
                    particlefirework$spark.fadeColourBlue = this.fadeColourBlue;
                }
                particlefirework$spark.twinkle = this.twinkle;
                this.effectRenderer.addEffect(particlefirework$spark);
            }
        }
    }
    
    public static class Starter extends Particle
    {
        private int fireworkAge;
        private final ParticleManager manager;
        private NBTTagList fireworkExplosions;
        boolean twinkle;
        
        public Starter(final World p_i46464_1_, final double p_i46464_2_, final double p_i46464_4_, final double p_i46464_6_, final double p_i46464_8_, final double p_i46464_10_, final double p_i46464_12_, final ParticleManager p_i46464_14_, @Nullable final NBTTagCompound p_i46464_15_) {
            super(p_i46464_1_, p_i46464_2_, p_i46464_4_, p_i46464_6_, 0.0, 0.0, 0.0);
            this.motionX = p_i46464_8_;
            this.motionY = p_i46464_10_;
            this.motionZ = p_i46464_12_;
            this.manager = p_i46464_14_;
            this.particleMaxAge = 8;
            if (p_i46464_15_ != null) {
                this.fireworkExplosions = p_i46464_15_.getTagList("Explosions", 10);
                if (this.fireworkExplosions.isEmpty()) {
                    this.fireworkExplosions = null;
                }
                else {
                    this.particleMaxAge = this.fireworkExplosions.tagCount() * 2 - 1;
                    for (int i = 0; i < this.fireworkExplosions.tagCount(); ++i) {
                        final NBTTagCompound nbttagcompound = this.fireworkExplosions.getCompoundTagAt(i);
                        if (nbttagcompound.getBoolean("Flicker")) {
                            this.twinkle = true;
                            this.particleMaxAge += 15;
                            break;
                        }
                    }
                }
            }
        }
        
        @Override
        public void renderParticle(final BufferBuilder buffer, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
        }
        
        @Override
        public void onUpdate() {
            if (this.fireworkAge == 0 && this.fireworkExplosions != null) {
                final boolean flag = this.isFarFromCamera();
                boolean flag2 = false;
                if (this.fireworkExplosions.tagCount() >= 3) {
                    flag2 = true;
                }
                else {
                    for (int i = 0; i < this.fireworkExplosions.tagCount(); ++i) {
                        final NBTTagCompound nbttagcompound = this.fireworkExplosions.getCompoundTagAt(i);
                        if (nbttagcompound.getByte("Type") == 1) {
                            flag2 = true;
                            break;
                        }
                    }
                }
                SoundEvent soundevent1;
                if (flag2) {
                    soundevent1 = (flag ? SoundEvents.ENTITY_FIREWORK_LARGE_BLAST_FAR : SoundEvents.ENTITY_FIREWORK_LARGE_BLAST);
                }
                else {
                    soundevent1 = (flag ? SoundEvents.ENTITY_FIREWORK_BLAST_FAR : SoundEvents.ENTITY_FIREWORK_BLAST);
                }
                this.world.playSound(this.posX, this.posY, this.posZ, soundevent1, SoundCategory.AMBIENT, 20.0f, 0.95f + this.rand.nextFloat() * 0.1f, true);
            }
            if (this.fireworkAge % 2 == 0 && this.fireworkExplosions != null && this.fireworkAge / 2 < this.fireworkExplosions.tagCount()) {
                final int k = this.fireworkAge / 2;
                final NBTTagCompound nbttagcompound2 = this.fireworkExplosions.getCompoundTagAt(k);
                final int l = nbttagcompound2.getByte("Type");
                final boolean flag3 = nbttagcompound2.getBoolean("Trail");
                final boolean flag4 = nbttagcompound2.getBoolean("Flicker");
                int[] aint = nbttagcompound2.getIntArray("Colors");
                final int[] aint2 = nbttagcompound2.getIntArray("FadeColors");
                if (aint.length == 0) {
                    aint = new int[] { ItemDye.DYE_COLORS[0] };
                }
                if (l == 1) {
                    this.createBall(0.5, 4, aint, aint2, flag3, flag4);
                }
                else if (l == 2) {
                    this.createShaped(0.5, new double[][] { { 0.0, 1.0 }, { 0.3455, 0.309 }, { 0.9511, 0.309 }, { 0.3795918367346939, -0.12653061224489795 }, { 0.6122448979591837, -0.8040816326530612 }, { 0.0, -0.35918367346938773 } }, aint, aint2, flag3, flag4, false);
                }
                else if (l == 3) {
                    this.createShaped(0.5, new double[][] { { 0.0, 0.2 }, { 0.2, 0.2 }, { 0.2, 0.6 }, { 0.6, 0.6 }, { 0.6, 0.2 }, { 0.2, 0.2 }, { 0.2, 0.0 }, { 0.4, 0.0 }, { 0.4, -0.6 }, { 0.2, -0.6 }, { 0.2, -0.4 }, { 0.0, -0.4 } }, aint, aint2, flag3, flag4, true);
                }
                else if (l == 4) {
                    this.createBurst(aint, aint2, flag3, flag4);
                }
                else {
                    this.createBall(0.25, 2, aint, aint2, flag3, flag4);
                }
                final int j = aint[0];
                final float f = ((j & 0xFF0000) >> 16) / 255.0f;
                final float f2 = ((j & 0xFF00) >> 8) / 255.0f;
                final float f3 = ((j & 0xFF) >> 0) / 255.0f;
                final Overlay particlefirework$overlay = new Overlay(this.world, this.posX, this.posY, this.posZ);
                particlefirework$overlay.setRBGColorF(f, f2, f3);
                this.manager.addEffect(particlefirework$overlay);
            }
            ++this.fireworkAge;
            if (this.fireworkAge > this.particleMaxAge) {
                if (this.twinkle) {
                    final boolean flag5 = this.isFarFromCamera();
                    final SoundEvent soundevent2 = flag5 ? SoundEvents.ENTITY_FIREWORK_TWINKLE_FAR : SoundEvents.ENTITY_FIREWORK_TWINKLE;
                    this.world.playSound(this.posX, this.posY, this.posZ, soundevent2, SoundCategory.AMBIENT, 20.0f, 0.9f + this.rand.nextFloat() * 0.15f, true);
                }
                this.setExpired();
            }
        }
        
        private boolean isFarFromCamera() {
            final Minecraft minecraft = Minecraft.getMinecraft();
            return minecraft == null || minecraft.getRenderViewEntity() == null || minecraft.getRenderViewEntity().getDistanceSq(this.posX, this.posY, this.posZ) >= 256.0;
        }
        
        private void createParticle(final double p_92034_1_, final double p_92034_3_, final double p_92034_5_, final double p_92034_7_, final double p_92034_9_, final double p_92034_11_, final int[] p_92034_13_, final int[] p_92034_14_, final boolean p_92034_15_, final boolean p_92034_16_) {
            final Spark particlefirework$spark = new Spark(this.world, p_92034_1_, p_92034_3_, p_92034_5_, p_92034_7_, p_92034_9_, p_92034_11_, this.manager);
            particlefirework$spark.setAlphaF(0.99f);
            particlefirework$spark.setTrail(p_92034_15_);
            particlefirework$spark.setTwinkle(p_92034_16_);
            final int i = this.rand.nextInt(p_92034_13_.length);
            particlefirework$spark.setColor(p_92034_13_[i]);
            if (p_92034_14_ != null && p_92034_14_.length > 0) {
                particlefirework$spark.setColorFade(p_92034_14_[this.rand.nextInt(p_92034_14_.length)]);
            }
            this.manager.addEffect(particlefirework$spark);
        }
        
        private void createBall(final double speed, final int size, final int[] colours, final int[] fadeColours, final boolean trail, final boolean twinkleIn) {
            final double d0 = this.posX;
            final double d2 = this.posY;
            final double d3 = this.posZ;
            for (int i = -size; i <= size; ++i) {
                for (int j = -size; j <= size; ++j) {
                    for (int k = -size; k <= size; ++k) {
                        final double d4 = j + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5;
                        final double d5 = i + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5;
                        final double d6 = k + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5;
                        final double d7 = MathHelper.sqrt(d4 * d4 + d5 * d5 + d6 * d6) / speed + this.rand.nextGaussian() * 0.05;
                        this.createParticle(d0, d2, d3, d4 / d7, d5 / d7, d6 / d7, colours, fadeColours, trail, twinkleIn);
                        if (i != -size && i != size && j != -size && j != size) {
                            k += size * 2 - 1;
                        }
                    }
                }
            }
        }
        
        private void createShaped(final double speed, final double[][] shape, final int[] colours, final int[] fadeColours, final boolean trail, final boolean twinkleIn, final boolean p_92038_8_) {
            final double d0 = shape[0][0];
            final double d2 = shape[0][1];
            this.createParticle(this.posX, this.posY, this.posZ, d0 * speed, d2 * speed, 0.0, colours, fadeColours, trail, twinkleIn);
            final float f = this.rand.nextFloat() * 3.1415927f;
            final double d3 = p_92038_8_ ? 0.034 : 0.34;
            for (int i = 0; i < 3; ++i) {
                final double d4 = f + i * 3.1415927f * d3;
                double d5 = d0;
                double d6 = d2;
                for (int j = 1; j < shape.length; ++j) {
                    final double d7 = shape[j][0];
                    final double d8 = shape[j][1];
                    for (double d9 = 0.25; d9 <= 1.0; d9 += 0.25) {
                        double d10 = (d5 + (d7 - d5) * d9) * speed;
                        final double d11 = (d6 + (d8 - d6) * d9) * speed;
                        final double d12 = d10 * Math.sin(d4);
                        d10 *= Math.cos(d4);
                        for (double d13 = -1.0; d13 <= 1.0; d13 += 2.0) {
                            this.createParticle(this.posX, this.posY, this.posZ, d10 * d13, d11, d12 * d13, colours, fadeColours, trail, twinkleIn);
                        }
                    }
                    d5 = d7;
                    d6 = d8;
                }
            }
        }
        
        private void createBurst(final int[] colours, final int[] fadeColours, final boolean trail, final boolean twinkleIn) {
            final double d0 = this.rand.nextGaussian() * 0.05;
            final double d2 = this.rand.nextGaussian() * 0.05;
            for (int i = 0; i < 70; ++i) {
                final double d3 = this.motionX * 0.5 + this.rand.nextGaussian() * 0.15 + d0;
                final double d4 = this.motionZ * 0.5 + this.rand.nextGaussian() * 0.15 + d2;
                final double d5 = this.motionY * 0.5 + this.rand.nextDouble() * 0.5;
                this.createParticle(this.posX, this.posY, this.posZ, d3, d5, d4, colours, fadeColours, trail, twinkleIn);
            }
        }
        
        @Override
        public int getFXLayer() {
            return 0;
        }
    }
}
