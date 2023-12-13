/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class EntityBreakingFX
extends EntityFX {
    private static final String __OBFID = "CL_00000897";

    protected EntityBreakingFX(World worldIn, double p_i1195_2_, double p_i1195_4_, double p_i1195_6_, Item p_i1195_8_) {
        this(worldIn, p_i1195_2_, p_i1195_4_, p_i1195_6_, p_i1195_8_, 0);
    }

    protected EntityBreakingFX(World worldIn, double p_i1197_2_, double p_i1197_4_, double p_i1197_6_, double p_i1197_8_, double p_i1197_10_, double p_i1197_12_, Item p_i1197_14_, int p_i1197_15_) {
        this(worldIn, p_i1197_2_, p_i1197_4_, p_i1197_6_, p_i1197_14_, p_i1197_15_);
        this.motionX *= (double)0.1f;
        this.motionY *= (double)0.1f;
        this.motionZ *= (double)0.1f;
        this.motionX += p_i1197_8_;
        this.motionY += p_i1197_10_;
        this.motionZ += p_i1197_12_;
    }

    protected EntityBreakingFX(World worldIn, double p_i1196_2_, double p_i1196_4_, double p_i1196_6_, Item p_i1196_8_, int p_i1196_9_) {
        super(worldIn, p_i1196_2_, p_i1196_4_, p_i1196_6_, 0.0, 0.0, 0.0);
        this.func_180435_a(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(p_i1196_8_, p_i1196_9_));
        this.particleBlue = 1.0f;
        this.particleGreen = 1.0f;
        this.particleRed = 1.0f;
        this.particleGravity = Blocks.snow.blockParticleGravity;
        this.particleScale /= 2.0f;
    }

    @Override
    public int getFXLayer() {
        return 1;
    }

    @Override
    public void func_180434_a(WorldRenderer p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_) {
        float var9 = ((float)this.particleTextureIndexX + this.particleTextureJitterX / 4.0f) / 16.0f;
        float var10 = var9 + 0.015609375f;
        float var11 = ((float)this.particleTextureIndexY + this.particleTextureJitterY / 4.0f) / 16.0f;
        float var12 = var11 + 0.015609375f;
        float var13 = 0.1f * this.particleScale;
        if (this.particleIcon != null) {
            var9 = this.particleIcon.getInterpolatedU(this.particleTextureJitterX / 4.0f * 16.0f);
            var10 = this.particleIcon.getInterpolatedU((this.particleTextureJitterX + 1.0f) / 4.0f * 16.0f);
            var11 = this.particleIcon.getInterpolatedV(this.particleTextureJitterY / 4.0f * 16.0f);
            var12 = this.particleIcon.getInterpolatedV((this.particleTextureJitterY + 1.0f) / 4.0f * 16.0f);
        }
        float var14 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)p_180434_3_ - interpPosX);
        float var15 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)p_180434_3_ - interpPosY);
        float var16 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)p_180434_3_ - interpPosZ);
        p_180434_1_.func_178986_b(this.particleRed, this.particleGreen, this.particleBlue);
        p_180434_1_.addVertexWithUV(var14 - p_180434_4_ * var13 - p_180434_7_ * var13, var15 - p_180434_5_ * var13, var16 - p_180434_6_ * var13 - p_180434_8_ * var13, var9, var12);
        p_180434_1_.addVertexWithUV(var14 - p_180434_4_ * var13 + p_180434_7_ * var13, var15 + p_180434_5_ * var13, var16 - p_180434_6_ * var13 + p_180434_8_ * var13, var9, var11);
        p_180434_1_.addVertexWithUV(var14 + p_180434_4_ * var13 + p_180434_7_ * var13, var15 + p_180434_5_ * var13, var16 + p_180434_6_ * var13 + p_180434_8_ * var13, var10, var11);
        p_180434_1_.addVertexWithUV(var14 + p_180434_4_ * var13 - p_180434_7_ * var13, var15 - p_180434_5_ * var13, var16 + p_180434_6_ * var13 - p_180434_8_ * var13, var10, var12);
    }

    public static class Factory
    implements IParticleFactory {
        private static final String __OBFID = "CL_00002613";

        @Override
        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_) {
            int var16 = p_178902_15_.length > 1 ? p_178902_15_[1] : 0;
            return new EntityBreakingFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_, Item.getItemById(p_178902_15_[0]), var16);
        }
    }

    public static class SlimeFactory
    implements IParticleFactory {
        private static final String __OBFID = "CL_00002612";

        @Override
        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_) {
            return new EntityBreakingFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, Items.slime_ball);
        }
    }

    public static class SnowballFactory
    implements IParticleFactory {
        private static final String __OBFID = "CL_00002611";

        @Override
        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_) {
            return new EntityBreakingFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, Items.snowball);
        }
    }
}

