// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.particle;

import net.minecraft.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemDye;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagList;

public class EntityFireworkStarterFX extends EntityFX
{
    private int fireworkAge;
    private final EffectRenderer theEffectRenderer;
    private NBTTagList fireworkExplosions;
    boolean twinkle;
    private static final String __OBFID = "CL_00000906";
    
    public EntityFireworkStarterFX(final World worldIn, final double p_i46355_2_, final double p_i46355_4_, final double p_i46355_6_, final double p_i46355_8_, final double p_i46355_10_, final double p_i46355_12_, final EffectRenderer p_i46355_14_, final NBTTagCompound p_i46355_15_) {
        super(worldIn, p_i46355_2_, p_i46355_4_, p_i46355_6_, 0.0, 0.0, 0.0);
        this.motionX = p_i46355_8_;
        this.motionY = p_i46355_10_;
        this.motionZ = p_i46355_12_;
        this.theEffectRenderer = p_i46355_14_;
        this.particleMaxAge = 8;
        if (p_i46355_15_ != null) {
            this.fireworkExplosions = p_i46355_15_.getTagList("Explosions", 10);
            if (this.fireworkExplosions.tagCount() == 0) {
                this.fireworkExplosions = null;
            }
            else {
                this.particleMaxAge = this.fireworkExplosions.tagCount() * 2 - 1;
                for (int var16 = 0; var16 < this.fireworkExplosions.tagCount(); ++var16) {
                    final NBTTagCompound var17 = this.fireworkExplosions.getCompoundTagAt(var16);
                    if (var17.getBoolean("Flicker")) {
                        this.twinkle = true;
                        this.particleMaxAge += 15;
                        break;
                    }
                }
            }
        }
    }
    
    @Override
    public void func_180434_a(final WorldRenderer p_180434_1_, final Entity p_180434_2_, final float p_180434_3_, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
    }
    
    @Override
    public void onUpdate() {
        if (this.fireworkAge == 0 && this.fireworkExplosions != null) {
            final boolean var1 = this.func_92037_i();
            boolean var2 = false;
            if (this.fireworkExplosions.tagCount() >= 3) {
                var2 = true;
            }
            else {
                for (int var3 = 0; var3 < this.fireworkExplosions.tagCount(); ++var3) {
                    final NBTTagCompound var4 = this.fireworkExplosions.getCompoundTagAt(var3);
                    if (var4.getByte("Type") == 1) {
                        var2 = true;
                        break;
                    }
                }
            }
            final String var5 = "fireworks." + (var2 ? "largeBlast" : "blast") + (var1 ? "_far" : "");
            this.worldObj.playSound(this.posX, this.posY, this.posZ, var5, 20.0f, 0.95f + this.rand.nextFloat() * 0.1f, true);
        }
        if (this.fireworkAge % 2 == 0 && this.fireworkExplosions != null && this.fireworkAge / 2 < this.fireworkExplosions.tagCount()) {
            final int var6 = this.fireworkAge / 2;
            final NBTTagCompound var7 = this.fireworkExplosions.getCompoundTagAt(var6);
            final byte var8 = var7.getByte("Type");
            final boolean var9 = var7.getBoolean("Trail");
            final boolean var10 = var7.getBoolean("Flicker");
            int[] var11 = var7.getIntArray("Colors");
            final int[] var12 = var7.getIntArray("FadeColors");
            if (var11.length == 0) {
                var11 = new int[] { ItemDye.dyeColors[0] };
            }
            if (var8 == 1) {
                this.createBall(0.5, 4, var11, var12, var9, var10);
            }
            else if (var8 == 2) {
                this.createShaped(0.5, new double[][] { { 0.0, 1.0 }, { 0.3455, 0.309 }, { 0.9511, 0.309 }, { 0.3795918367346939, -0.12653061224489795 }, { 0.6122448979591837, -0.8040816326530612 }, { 0.0, -0.35918367346938773 } }, var11, var12, var9, var10, false);
            }
            else if (var8 == 3) {
                this.createShaped(0.5, new double[][] { { 0.0, 0.2 }, { 0.2, 0.2 }, { 0.2, 0.6 }, { 0.6, 0.6 }, { 0.6, 0.2 }, { 0.2, 0.2 }, { 0.2, 0.0 }, { 0.4, 0.0 }, { 0.4, -0.6 }, { 0.2, -0.6 }, { 0.2, -0.4 }, { 0.0, -0.4 } }, var11, var12, var9, var10, true);
            }
            else if (var8 == 4) {
                this.createBurst(var11, var12, var9, var10);
            }
            else {
                this.createBall(0.25, 2, var11, var12, var9, var10);
            }
            final int var13 = var11[0];
            final float var14 = ((var13 & 0xFF0000) >> 16) / 255.0f;
            final float var15 = ((var13 & 0xFF00) >> 8) / 255.0f;
            final float var16 = ((var13 & 0xFF) >> 0) / 255.0f;
            final EntityFireworkOverlayFX var17 = new EntityFireworkOverlayFX(this.worldObj, this.posX, this.posY, this.posZ);
            var17.setRBGColorF(var14, var15, var16);
            this.theEffectRenderer.addEffect(var17);
        }
        ++this.fireworkAge;
        if (this.fireworkAge > this.particleMaxAge) {
            if (this.twinkle) {
                final boolean var1 = this.func_92037_i();
                final String var18 = "fireworks." + (var1 ? "twinkle_far" : "twinkle");
                this.worldObj.playSound(this.posX, this.posY, this.posZ, var18, 20.0f, 0.9f + this.rand.nextFloat() * 0.15f, true);
            }
            this.setDead();
        }
    }
    
    private boolean func_92037_i() {
        final Minecraft var1 = Minecraft.getMinecraft();
        return var1 == null || var1.func_175606_aa() == null || var1.func_175606_aa().getDistanceSq(this.posX, this.posY, this.posZ) >= 256.0;
    }
    
    private void createParticle(final double p_92034_1_, final double p_92034_3_, final double p_92034_5_, final double p_92034_7_, final double p_92034_9_, final double p_92034_11_, final int[] p_92034_13_, final int[] p_92034_14_, final boolean p_92034_15_, final boolean p_92034_16_) {
        final EntityFireworkSparkFX var17 = new EntityFireworkSparkFX(this.worldObj, p_92034_1_, p_92034_3_, p_92034_5_, p_92034_7_, p_92034_9_, p_92034_11_, this.theEffectRenderer);
        var17.setAlphaF(0.99f);
        var17.setTrail(p_92034_15_);
        var17.setTwinkle(p_92034_16_);
        final int var18 = this.rand.nextInt(p_92034_13_.length);
        var17.setColour(p_92034_13_[var18]);
        if (p_92034_14_ != null && p_92034_14_.length > 0) {
            var17.setFadeColour(p_92034_14_[this.rand.nextInt(p_92034_14_.length)]);
        }
        this.theEffectRenderer.addEffect(var17);
    }
    
    private void createBall(final double p_92035_1_, final int p_92035_3_, final int[] p_92035_4_, final int[] p_92035_5_, final boolean p_92035_6_, final boolean p_92035_7_) {
        final double var8 = this.posX;
        final double var9 = this.posY;
        final double var10 = this.posZ;
        for (int var11 = -p_92035_3_; var11 <= p_92035_3_; ++var11) {
            for (int var12 = -p_92035_3_; var12 <= p_92035_3_; ++var12) {
                for (int var13 = -p_92035_3_; var13 <= p_92035_3_; ++var13) {
                    final double var14 = var12 + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5;
                    final double var15 = var11 + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5;
                    final double var16 = var13 + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5;
                    final double var17 = MathHelper.sqrt_double(var14 * var14 + var15 * var15 + var16 * var16) / p_92035_1_ + this.rand.nextGaussian() * 0.05;
                    this.createParticle(var8, var9, var10, var14 / var17, var15 / var17, var16 / var17, p_92035_4_, p_92035_5_, p_92035_6_, p_92035_7_);
                    if (var11 != -p_92035_3_ && var11 != p_92035_3_ && var12 != -p_92035_3_ && var12 != p_92035_3_) {
                        var13 += p_92035_3_ * 2 - 1;
                    }
                }
            }
        }
    }
    
    private void createShaped(final double p_92038_1_, final double[][] p_92038_3_, final int[] p_92038_4_, final int[] p_92038_5_, final boolean p_92038_6_, final boolean p_92038_7_, final boolean p_92038_8_) {
        final double var9 = p_92038_3_[0][0];
        final double var10 = p_92038_3_[0][1];
        this.createParticle(this.posX, this.posY, this.posZ, var9 * p_92038_1_, var10 * p_92038_1_, 0.0, p_92038_4_, p_92038_5_, p_92038_6_, p_92038_7_);
        final float var11 = this.rand.nextFloat() * 3.1415927f;
        final double var12 = p_92038_8_ ? 0.034 : 0.34;
        for (int var13 = 0; var13 < 3; ++var13) {
            final double var14 = var11 + var13 * 3.1415927f * var12;
            double var15 = var9;
            double var16 = var10;
            for (int var17 = 1; var17 < p_92038_3_.length; ++var17) {
                final double var18 = p_92038_3_[var17][0];
                final double var19 = p_92038_3_[var17][1];
                for (double var20 = 0.25; var20 <= 1.0; var20 += 0.25) {
                    double var21 = (var15 + (var18 - var15) * var20) * p_92038_1_;
                    final double var22 = (var16 + (var19 - var16) * var20) * p_92038_1_;
                    final double var23 = var21 * Math.sin(var14);
                    var21 *= Math.cos(var14);
                    for (double var24 = -1.0; var24 <= 1.0; var24 += 2.0) {
                        this.createParticle(this.posX, this.posY, this.posZ, var21 * var24, var22, var23 * var24, p_92038_4_, p_92038_5_, p_92038_6_, p_92038_7_);
                    }
                }
                var15 = var18;
                var16 = var19;
            }
        }
    }
    
    private void createBurst(final int[] p_92036_1_, final int[] p_92036_2_, final boolean p_92036_3_, final boolean p_92036_4_) {
        final double var5 = this.rand.nextGaussian() * 0.05;
        final double var6 = this.rand.nextGaussian() * 0.05;
        for (int var7 = 0; var7 < 70; ++var7) {
            final double var8 = this.motionX * 0.5 + this.rand.nextGaussian() * 0.15 + var5;
            final double var9 = this.motionZ * 0.5 + this.rand.nextGaussian() * 0.15 + var6;
            final double var10 = this.motionY * 0.5 + this.rand.nextDouble() * 0.5;
            this.createParticle(this.posX, this.posY, this.posZ, var8, var10, var9, p_92036_1_, p_92036_2_, p_92036_3_, p_92036_4_);
        }
    }
    
    @Override
    public int getFXLayer() {
        return 0;
    }
}
