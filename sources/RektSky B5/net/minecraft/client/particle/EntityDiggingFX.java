/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.particle;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityDiggingFX
extends EntityFX {
    private IBlockState field_174847_a;
    private BlockPos field_181019_az;

    protected EntityDiggingFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, IBlockState state) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.field_174847_a = state;
        this.setParticleIcon(Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state));
        this.particleGravity = state.getBlock().blockParticleGravity;
        this.particleBlue = 0.6f;
        this.particleGreen = 0.6f;
        this.particleRed = 0.6f;
        this.particleScale /= 2.0f;
    }

    public EntityDiggingFX func_174846_a(BlockPos pos) {
        this.field_181019_az = pos;
        if (this.field_174847_a.getBlock() == Blocks.grass) {
            return this;
        }
        int i2 = this.field_174847_a.getBlock().colorMultiplier(this.worldObj, pos);
        this.particleRed *= (float)(i2 >> 16 & 0xFF) / 255.0f;
        this.particleGreen *= (float)(i2 >> 8 & 0xFF) / 255.0f;
        this.particleBlue *= (float)(i2 & 0xFF) / 255.0f;
        return this;
    }

    public EntityDiggingFX func_174845_l() {
        this.field_181019_az = new BlockPos(this.posX, this.posY, this.posZ);
        Block block = this.field_174847_a.getBlock();
        if (block == Blocks.grass) {
            return this;
        }
        int i2 = block.getRenderColor(this.field_174847_a);
        this.particleRed *= (float)(i2 >> 16 & 0xFF) / 255.0f;
        this.particleGreen *= (float)(i2 >> 8 & 0xFF) / 255.0f;
        this.particleBlue *= (float)(i2 & 0xFF) / 255.0f;
        return this;
    }

    @Override
    public int getFXLayer() {
        return 1;
    }

    @Override
    public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_) {
        float f2 = ((float)this.particleTextureIndexX + this.particleTextureJitterX / 4.0f) / 16.0f;
        float f1 = f2 + 0.015609375f;
        float f22 = ((float)this.particleTextureIndexY + this.particleTextureJitterY / 4.0f) / 16.0f;
        float f3 = f22 + 0.015609375f;
        float f4 = 0.1f * this.particleScale;
        if (this.particleIcon != null) {
            f2 = this.particleIcon.getInterpolatedU(this.particleTextureJitterX / 4.0f * 16.0f);
            f1 = this.particleIcon.getInterpolatedU((this.particleTextureJitterX + 1.0f) / 4.0f * 16.0f);
            f22 = this.particleIcon.getInterpolatedV(this.particleTextureJitterY / 4.0f * 16.0f);
            f3 = this.particleIcon.getInterpolatedV((this.particleTextureJitterY + 1.0f) / 4.0f * 16.0f);
        }
        float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
        float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
        float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);
        int i2 = this.getBrightnessForRender(partialTicks);
        int j2 = i2 >> 16 & 0xFFFF;
        int k2 = i2 & 0xFFFF;
        worldRendererIn.pos(f5 - p_180434_4_ * f4 - p_180434_7_ * f4, f6 - p_180434_5_ * f4, f7 - p_180434_6_ * f4 - p_180434_8_ * f4).tex(f2, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j2, k2).endVertex();
        worldRendererIn.pos(f5 - p_180434_4_ * f4 + p_180434_7_ * f4, f6 + p_180434_5_ * f4, f7 - p_180434_6_ * f4 + p_180434_8_ * f4).tex(f2, f22).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j2, k2).endVertex();
        worldRendererIn.pos(f5 + p_180434_4_ * f4 + p_180434_7_ * f4, f6 + p_180434_5_ * f4, f7 + p_180434_6_ * f4 + p_180434_8_ * f4).tex(f1, f22).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j2, k2).endVertex();
        worldRendererIn.pos(f5 + p_180434_4_ * f4 - p_180434_7_ * f4, f6 - p_180434_5_ * f4, f7 + p_180434_6_ * f4 - p_180434_8_ * f4).tex(f1, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j2, k2).endVertex();
    }

    @Override
    public int getBrightnessForRender(float partialTicks) {
        int i2 = super.getBrightnessForRender(partialTicks);
        int j2 = 0;
        if (this.worldObj.isBlockLoaded(this.field_181019_az)) {
            j2 = this.worldObj.getCombinedLight(this.field_181019_az, 0);
        }
        return i2 == 0 ? j2 : i2;
    }

    public static class Factory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int ... p_178902_15_) {
            return new EntityDiggingFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, Block.getStateById(p_178902_15_[0])).func_174845_l();
        }
    }
}

