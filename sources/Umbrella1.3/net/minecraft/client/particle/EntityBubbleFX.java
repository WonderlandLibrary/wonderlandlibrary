/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.particle;

import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityBubbleFX
extends EntityFX {
    private static final String __OBFID = "CL_00000898";

    protected EntityBubbleFX(World worldIn, double p_i1198_2_, double p_i1198_4_, double p_i1198_6_, double p_i1198_8_, double p_i1198_10_, double p_i1198_12_) {
        super(worldIn, p_i1198_2_, p_i1198_4_, p_i1198_6_, p_i1198_8_, p_i1198_10_, p_i1198_12_);
        this.particleRed = 1.0f;
        this.particleGreen = 1.0f;
        this.particleBlue = 1.0f;
        this.setParticleTextureIndex(32);
        this.setSize(0.02f, 0.02f);
        this.particleScale *= this.rand.nextFloat() * 0.6f + 0.2f;
        this.motionX = p_i1198_8_ * (double)0.2f + (Math.random() * 2.0 - 1.0) * (double)0.02f;
        this.motionY = p_i1198_10_ * (double)0.2f + (Math.random() * 2.0 - 1.0) * (double)0.02f;
        this.motionZ = p_i1198_12_ * (double)0.2f + (Math.random() * 2.0 - 1.0) * (double)0.02f;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY += 0.002;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= (double)0.85f;
        this.motionY *= (double)0.85f;
        this.motionZ *= (double)0.85f;
        if (this.worldObj.getBlockState(new BlockPos(this)).getBlock().getMaterial() != Material.water) {
            this.setDead();
        }
        if (this.particleMaxAge-- <= 0) {
            this.setDead();
        }
    }

    public static class Factory
    implements IParticleFactory {
        private static final String __OBFID = "CL_00002610";

        @Override
        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_) {
            return new EntityBubbleFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }
}

