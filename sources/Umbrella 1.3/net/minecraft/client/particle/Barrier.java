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
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class Barrier
extends EntityFX {
    private static final String __OBFID = "CL_00002615";

    protected Barrier(World worldIn, double p_i46286_2_, double p_i46286_4_, double p_i46286_6_, Item p_i46286_8_) {
        super(worldIn, p_i46286_2_, p_i46286_4_, p_i46286_6_, 0.0, 0.0, 0.0);
        this.func_180435_a(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(p_i46286_8_));
        this.particleBlue = 1.0f;
        this.particleGreen = 1.0f;
        this.particleRed = 1.0f;
        this.motionZ = 0.0;
        this.motionY = 0.0;
        this.motionX = 0.0;
        this.particleGravity = 0.0f;
        this.particleMaxAge = 80;
    }

    @Override
    public int getFXLayer() {
        return 1;
    }

    @Override
    public void func_180434_a(WorldRenderer p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_) {
        float var9 = this.particleIcon.getMinU();
        float var10 = this.particleIcon.getMaxU();
        float var11 = this.particleIcon.getMinV();
        float var12 = this.particleIcon.getMaxV();
        float var13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)p_180434_3_ - interpPosX);
        float var14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)p_180434_3_ - interpPosY);
        float var15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)p_180434_3_ - interpPosZ);
        p_180434_1_.func_178986_b(this.particleRed, this.particleGreen, this.particleBlue);
        float var16 = 0.5f;
        p_180434_1_.addVertexWithUV(var13 - p_180434_4_ * var16 - p_180434_7_ * var16, var14 - p_180434_5_ * var16, var15 - p_180434_6_ * var16 - p_180434_8_ * var16, var10, var12);
        p_180434_1_.addVertexWithUV(var13 - p_180434_4_ * var16 + p_180434_7_ * var16, var14 + p_180434_5_ * var16, var15 - p_180434_6_ * var16 + p_180434_8_ * var16, var10, var11);
        p_180434_1_.addVertexWithUV(var13 + p_180434_4_ * var16 + p_180434_7_ * var16, var14 + p_180434_5_ * var16, var15 + p_180434_6_ * var16 + p_180434_8_ * var16, var9, var11);
        p_180434_1_.addVertexWithUV(var13 + p_180434_4_ * var16 - p_180434_7_ * var16, var14 - p_180434_5_ * var16, var15 + p_180434_6_ * var16 - p_180434_8_ * var16, var9, var12);
    }

    public static class Factory
    implements IParticleFactory {
        private static final String __OBFID = "CL_00002614";

        @Override
        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_) {
            return new Barrier(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, Item.getItemFromBlock(Blocks.barrier));
        }
    }
}

