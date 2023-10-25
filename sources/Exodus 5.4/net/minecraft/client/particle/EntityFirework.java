/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemDye;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFirework {

    public static class Factory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            SparkFX sparkFX = new SparkFX(world, d, d2, d3, d4, d5, d6, Minecraft.getMinecraft().effectRenderer);
            sparkFX.setAlphaF(0.99f);
            return sparkFX;
        }
    }

    public static class SparkFX
    extends EntityFX {
        private boolean hasFadeColour;
        private float fadeColourRed;
        private float fadeColourBlue;
        private int baseTextureIndex = 160;
        private boolean twinkle;
        private boolean trail;
        private final EffectRenderer field_92047_az;
        private float fadeColourGreen;

        @Override
        public int getBrightnessForRender(float f) {
            return 0xF000F0;
        }

        @Override
        public float getBrightness(float f) {
            return 1.0f;
        }

        public void setColour(int n) {
            float f = (float)((n & 0xFF0000) >> 16) / 255.0f;
            float f2 = (float)((n & 0xFF00) >> 8) / 255.0f;
            float f3 = (float)((n & 0xFF) >> 0) / 255.0f;
            float f4 = 1.0f;
            this.setRBGColorF(f * f4, f2 * f4, f3 * f4);
        }

        public void setTwinkle(boolean bl) {
            this.twinkle = bl;
        }

        public void setFadeColour(int n) {
            this.fadeColourRed = (float)((n & 0xFF0000) >> 16) / 255.0f;
            this.fadeColourGreen = (float)((n & 0xFF00) >> 8) / 255.0f;
            this.fadeColourBlue = (float)((n & 0xFF) >> 0) / 255.0f;
            this.hasFadeColour = true;
        }

        @Override
        public void renderParticle(WorldRenderer worldRenderer, Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
            if (!this.twinkle || this.particleAge < this.particleMaxAge / 3 || (this.particleAge + this.particleMaxAge) / 3 % 2 == 0) {
                super.renderParticle(worldRenderer, entity, f, f2, f3, f4, f5, f6);
            }
        }

        public SparkFX(World world, double d, double d2, double d3, double d4, double d5, double d6, EffectRenderer effectRenderer) {
            super(world, d, d2, d3);
            this.motionX = d4;
            this.motionY = d5;
            this.motionZ = d6;
            this.field_92047_az = effectRenderer;
            this.particleScale *= 0.75f;
            this.particleMaxAge = 48 + this.rand.nextInt(12);
            this.noClip = false;
        }

        @Override
        public boolean canBePushed() {
            return false;
        }

        public void setTrail(boolean bl) {
            this.trail = bl;
        }

        @Override
        public void onUpdate() {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            if (this.particleAge++ >= this.particleMaxAge) {
                this.setDead();
            }
            if (this.particleAge > this.particleMaxAge / 2) {
                this.setAlphaF(1.0f - ((float)this.particleAge - (float)(this.particleMaxAge / 2)) / (float)this.particleMaxAge);
                if (this.hasFadeColour) {
                    this.particleRed += (this.fadeColourRed - this.particleRed) * 0.2f;
                    this.particleGreen += (this.fadeColourGreen - this.particleGreen) * 0.2f;
                    this.particleBlue += (this.fadeColourBlue - this.particleBlue) * 0.2f;
                }
            }
            this.setParticleTextureIndex(this.baseTextureIndex + (7 - this.particleAge * 8 / this.particleMaxAge));
            this.motionY -= 0.004;
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double)0.91f;
            this.motionY *= (double)0.91f;
            this.motionZ *= (double)0.91f;
            if (this.onGround) {
                this.motionX *= (double)0.7f;
                this.motionZ *= (double)0.7f;
            }
            if (this.trail && this.particleAge < this.particleMaxAge / 2 && (this.particleAge + this.particleMaxAge) % 2 == 0) {
                SparkFX sparkFX = new SparkFX(this.worldObj, this.posX, this.posY, this.posZ, 0.0, 0.0, 0.0, this.field_92047_az);
                sparkFX.setAlphaF(0.99f);
                sparkFX.setRBGColorF(this.particleRed, this.particleGreen, this.particleBlue);
                sparkFX.particleAge = sparkFX.particleMaxAge / 2;
                if (this.hasFadeColour) {
                    sparkFX.hasFadeColour = true;
                    sparkFX.fadeColourRed = this.fadeColourRed;
                    sparkFX.fadeColourGreen = this.fadeColourGreen;
                    sparkFX.fadeColourBlue = this.fadeColourBlue;
                }
                sparkFX.twinkle = this.twinkle;
                this.field_92047_az.addEffect(sparkFX);
            }
        }

        @Override
        public AxisAlignedBB getCollisionBoundingBox() {
            return null;
        }
    }

    public static class OverlayFX
    extends EntityFX {
        protected OverlayFX(World world, double d, double d2, double d3) {
            super(world, d, d2, d3);
            this.particleMaxAge = 4;
        }

        @Override
        public void renderParticle(WorldRenderer worldRenderer, Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
            float f7 = 0.25f;
            float f8 = 0.5f;
            float f9 = 0.125f;
            float f10 = 0.375f;
            float f11 = 7.1f * MathHelper.sin(((float)this.particleAge + f - 1.0f) * 0.25f * (float)Math.PI);
            this.particleAlpha = 0.6f - ((float)this.particleAge + f - 1.0f) * 0.25f * 0.5f;
            float f12 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f - interpPosX);
            float f13 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f - interpPosY);
            float f14 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f - interpPosZ);
            int n = this.getBrightnessForRender(f);
            int n2 = n >> 16 & 0xFFFF;
            int n3 = n & 0xFFFF;
            worldRenderer.pos(f12 - f2 * f11 - f5 * f11, f13 - f3 * f11, f14 - f4 * f11 - f6 * f11).tex(0.5, 0.375).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(n2, n3).endVertex();
            worldRenderer.pos(f12 - f2 * f11 + f5 * f11, f13 + f3 * f11, f14 - f4 * f11 + f6 * f11).tex(0.5, 0.125).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(n2, n3).endVertex();
            worldRenderer.pos(f12 + f2 * f11 + f5 * f11, f13 + f3 * f11, f14 + f4 * f11 + f6 * f11).tex(0.25, 0.125).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(n2, n3).endVertex();
            worldRenderer.pos(f12 + f2 * f11 - f5 * f11, f13 - f3 * f11, f14 + f4 * f11 - f6 * f11).tex(0.25, 0.375).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(n2, n3).endVertex();
        }
    }

    public static class StarterFX
    extends EntityFX {
        private int fireworkAge;
        private final EffectRenderer theEffectRenderer;
        boolean twinkle;
        private NBTTagList fireworkExplosions;

        private void createBurst(int[] nArray, int[] nArray2, boolean bl, boolean bl2) {
            double d = this.rand.nextGaussian() * 0.05;
            double d2 = this.rand.nextGaussian() * 0.05;
            int n = 0;
            while (n < 70) {
                double d3 = this.motionX * 0.5 + this.rand.nextGaussian() * 0.15 + d;
                double d4 = this.motionZ * 0.5 + this.rand.nextGaussian() * 0.15 + d2;
                double d5 = this.motionY * 0.5 + this.rand.nextDouble() * 0.5;
                this.createParticle(this.posX, this.posY, this.posZ, d3, d5, d4, nArray, nArray2, bl, bl2);
                ++n;
            }
        }

        private boolean func_92037_i() {
            Minecraft minecraft = Minecraft.getMinecraft();
            return minecraft == null || minecraft.getRenderViewEntity() == null || minecraft.getRenderViewEntity().getDistanceSq(this.posX, this.posY, this.posZ) >= 256.0;
        }

        @Override
        public void onUpdate() {
            int n;
            if (this.fireworkAge == 0 && this.fireworkExplosions != null) {
                n = this.func_92037_i();
                boolean bl = false;
                if (this.fireworkExplosions.tagCount() >= 3) {
                    bl = true;
                } else {
                    int n2 = 0;
                    while (n2 < this.fireworkExplosions.tagCount()) {
                        NBTTagCompound nBTTagCompound = this.fireworkExplosions.getCompoundTagAt(n2);
                        if (nBTTagCompound.getByte("Type") == 1) {
                            bl = true;
                            break;
                        }
                        ++n2;
                    }
                }
                String string = "fireworks." + (bl ? "largeBlast" : "blast") + (n != 0 ? "_far" : "");
                this.worldObj.playSound(this.posX, this.posY, this.posZ, string, 20.0f, 0.95f + this.rand.nextFloat() * 0.1f, true);
            }
            if (this.fireworkAge % 2 == 0 && this.fireworkExplosions != null && this.fireworkAge / 2 < this.fireworkExplosions.tagCount()) {
                n = this.fireworkAge / 2;
                NBTTagCompound nBTTagCompound = this.fireworkExplosions.getCompoundTagAt(n);
                byte by = nBTTagCompound.getByte("Type");
                boolean bl = nBTTagCompound.getBoolean("Trail");
                boolean bl2 = nBTTagCompound.getBoolean("Flicker");
                int[] nArray = nBTTagCompound.getIntArray("Colors");
                int[] nArray2 = nBTTagCompound.getIntArray("FadeColors");
                if (nArray.length == 0) {
                    nArray = new int[]{ItemDye.dyeColors[0]};
                }
                if (by == 1) {
                    this.createBall(0.5, 4, nArray, nArray2, bl, bl2);
                } else if (by == 2) {
                    this.createShaped(0.5, new double[][]{{0.0, 1.0}, {0.3455, 0.309}, {0.9511, 0.309}, {0.3795918367346939, -0.12653061224489795}, {0.6122448979591837, -0.8040816326530612}, {0.0, -0.35918367346938773}}, nArray, nArray2, bl, bl2, false);
                } else if (by == 3) {
                    this.createShaped(0.5, new double[][]{{0.0, 0.2}, {0.2, 0.2}, {0.2, 0.6}, {0.6, 0.6}, {0.6, 0.2}, {0.2, 0.2}, {0.2, 0.0}, {0.4, 0.0}, {0.4, -0.6}, {0.2, -0.6}, {0.2, -0.4}, {0.0, -0.4}}, nArray, nArray2, bl, bl2, true);
                } else if (by == 4) {
                    this.createBurst(nArray, nArray2, bl, bl2);
                } else {
                    this.createBall(0.25, 2, nArray, nArray2, bl, bl2);
                }
                int n3 = nArray[0];
                float f = (float)((n3 & 0xFF0000) >> 16) / 255.0f;
                float f2 = (float)((n3 & 0xFF00) >> 8) / 255.0f;
                float f3 = (float)((n3 & 0xFF) >> 0) / 255.0f;
                OverlayFX overlayFX = new OverlayFX(this.worldObj, this.posX, this.posY, this.posZ);
                overlayFX.setRBGColorF(f, f2, f3);
                this.theEffectRenderer.addEffect(overlayFX);
            }
            ++this.fireworkAge;
            if (this.fireworkAge > this.particleMaxAge) {
                if (this.twinkle) {
                    n = this.func_92037_i() ? 1 : 0;
                    String string = "fireworks." + (n != 0 ? "twinkle_far" : "twinkle");
                    this.worldObj.playSound(this.posX, this.posY, this.posZ, string, 20.0f, 0.9f + this.rand.nextFloat() * 0.15f, true);
                }
                this.setDead();
            }
        }

        @Override
        public void renderParticle(WorldRenderer worldRenderer, Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        }

        @Override
        public int getFXLayer() {
            return 0;
        }

        private void createParticle(double d, double d2, double d3, double d4, double d5, double d6, int[] nArray, int[] nArray2, boolean bl, boolean bl2) {
            SparkFX sparkFX = new SparkFX(this.worldObj, d, d2, d3, d4, d5, d6, this.theEffectRenderer);
            sparkFX.setAlphaF(0.99f);
            sparkFX.setTrail(bl);
            sparkFX.setTwinkle(bl2);
            int n = this.rand.nextInt(nArray.length);
            sparkFX.setColour(nArray[n]);
            if (nArray2 != null && nArray2.length > 0) {
                sparkFX.setFadeColour(nArray2[this.rand.nextInt(nArray2.length)]);
            }
            this.theEffectRenderer.addEffect(sparkFX);
        }

        public StarterFX(World world, double d, double d2, double d3, double d4, double d5, double d6, EffectRenderer effectRenderer, NBTTagCompound nBTTagCompound) {
            super(world, d, d2, d3, 0.0, 0.0, 0.0);
            this.motionX = d4;
            this.motionY = d5;
            this.motionZ = d6;
            this.theEffectRenderer = effectRenderer;
            this.particleMaxAge = 8;
            if (nBTTagCompound != null) {
                this.fireworkExplosions = nBTTagCompound.getTagList("Explosions", 10);
                if (this.fireworkExplosions.tagCount() == 0) {
                    this.fireworkExplosions = null;
                } else {
                    this.particleMaxAge = this.fireworkExplosions.tagCount() * 2 - 1;
                    int n = 0;
                    while (n < this.fireworkExplosions.tagCount()) {
                        NBTTagCompound nBTTagCompound2 = this.fireworkExplosions.getCompoundTagAt(n);
                        if (nBTTagCompound2.getBoolean("Flicker")) {
                            this.twinkle = true;
                            this.particleMaxAge += 15;
                            break;
                        }
                        ++n;
                    }
                }
            }
        }

        private void createBall(double d, int n, int[] nArray, int[] nArray2, boolean bl, boolean bl2) {
            double d2 = this.posX;
            double d3 = this.posY;
            double d4 = this.posZ;
            int n2 = -n;
            while (n2 <= n) {
                int n3 = -n;
                while (n3 <= n) {
                    int n4 = -n;
                    while (n4 <= n) {
                        double d5 = (double)n3 + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5;
                        double d6 = (double)n2 + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5;
                        double d7 = (double)n4 + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5;
                        double d8 = (double)MathHelper.sqrt_double(d5 * d5 + d6 * d6 + d7 * d7) / d + this.rand.nextGaussian() * 0.05;
                        this.createParticle(d2, d3, d4, d5 / d8, d6 / d8, d7 / d8, nArray, nArray2, bl, bl2);
                        if (n2 != -n && n2 != n && n3 != -n && n3 != n) {
                            n4 += n * 2 - 1;
                        }
                        ++n4;
                    }
                    ++n3;
                }
                ++n2;
            }
        }

        private void createShaped(double d, double[][] dArray, int[] nArray, int[] nArray2, boolean bl, boolean bl2, boolean bl3) {
            double d2 = dArray[0][0];
            double d3 = dArray[0][1];
            this.createParticle(this.posX, this.posY, this.posZ, d2 * d, d3 * d, 0.0, nArray, nArray2, bl, bl2);
            float f = this.rand.nextFloat() * (float)Math.PI;
            double d4 = bl3 ? 0.034 : 0.34;
            int n = 0;
            while (n < 3) {
                double d5 = (double)f + (double)((float)n * (float)Math.PI) * d4;
                double d6 = d2;
                double d7 = d3;
                int n2 = 1;
                while (n2 < dArray.length) {
                    double d8 = dArray[n2][0];
                    double d9 = dArray[n2][1];
                    double d10 = 0.25;
                    while (d10 <= 1.0) {
                        double d11 = (d6 + (d8 - d6) * d10) * d;
                        double d12 = (d7 + (d9 - d7) * d10) * d;
                        double d13 = d11 * Math.sin(d5);
                        d11 *= Math.cos(d5);
                        double d14 = -1.0;
                        while (d14 <= 1.0) {
                            this.createParticle(this.posX, this.posY, this.posZ, d11 * d14, d12, d13 * d14, nArray, nArray2, bl, bl2);
                            d14 += 2.0;
                        }
                        d10 += 0.25;
                    }
                    d6 = d8;
                    d7 = d9;
                    ++n2;
                }
                ++n;
            }
        }
    }
}

