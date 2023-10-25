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
    private BlockPos field_181019_az;
    private IBlockState field_174847_a;

    @Override
    public int getFXLayer() {
        return 1;
    }

    public EntityDiggingFX func_174845_l() {
        this.field_181019_az = new BlockPos(this.posX, this.posY, this.posZ);
        Block block = this.field_174847_a.getBlock();
        if (block == Blocks.grass) {
            return this;
        }
        int n = block.getRenderColor(this.field_174847_a);
        this.particleRed *= (float)(n >> 16 & 0xFF) / 255.0f;
        this.particleGreen *= (float)(n >> 8 & 0xFF) / 255.0f;
        this.particleBlue *= (float)(n & 0xFF) / 255.0f;
        return this;
    }

    public EntityDiggingFX func_174846_a(BlockPos blockPos) {
        this.field_181019_az = blockPos;
        if (this.field_174847_a.getBlock() == Blocks.grass) {
            return this;
        }
        int n = this.field_174847_a.getBlock().colorMultiplier(this.worldObj, blockPos);
        this.particleRed *= (float)(n >> 16 & 0xFF) / 255.0f;
        this.particleGreen *= (float)(n >> 8 & 0xFF) / 255.0f;
        this.particleBlue *= (float)(n & 0xFF) / 255.0f;
        return this;
    }

    protected EntityDiggingFX(World world, double d, double d2, double d3, double d4, double d5, double d6, IBlockState iBlockState) {
        super(world, d, d2, d3, d4, d5, d6);
        this.field_174847_a = iBlockState;
        this.setParticleIcon(Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(iBlockState));
        this.particleGravity = iBlockState.getBlock().blockParticleGravity;
        this.particleBlue = 0.6f;
        this.particleGreen = 0.6f;
        this.particleRed = 0.6f;
        this.particleScale /= 2.0f;
    }

    @Override
    public int getBrightnessForRender(float f) {
        int n = super.getBrightnessForRender(f);
        int n2 = 0;
        if (this.worldObj.isBlockLoaded(this.field_181019_az)) {
            n2 = this.worldObj.getCombinedLight(this.field_181019_az, 0);
        }
        return n == 0 ? n2 : n;
    }

    @Override
    public void renderParticle(WorldRenderer worldRenderer, Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        float f7 = ((float)this.particleTextureIndexX + this.particleTextureJitterX / 4.0f) / 16.0f;
        float f8 = f7 + 0.015609375f;
        float f9 = ((float)this.particleTextureIndexY + this.particleTextureJitterY / 4.0f) / 16.0f;
        float f10 = f9 + 0.015609375f;
        float f11 = 0.1f * this.particleScale;
        if (this.particleIcon != null) {
            f7 = this.particleIcon.getInterpolatedU(this.particleTextureJitterX / 4.0f * 16.0f);
            f8 = this.particleIcon.getInterpolatedU((this.particleTextureJitterX + 1.0f) / 4.0f * 16.0f);
            f9 = this.particleIcon.getInterpolatedV(this.particleTextureJitterY / 4.0f * 16.0f);
            f10 = this.particleIcon.getInterpolatedV((this.particleTextureJitterY + 1.0f) / 4.0f * 16.0f);
        }
        float f12 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f - interpPosX);
        float f13 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f - interpPosY);
        float f14 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f - interpPosZ);
        int n = this.getBrightnessForRender(f);
        int n2 = n >> 16 & 0xFFFF;
        int n3 = n & 0xFFFF;
        worldRenderer.pos(f12 - f2 * f11 - f5 * f11, f13 - f3 * f11, f14 - f4 * f11 - f6 * f11).tex(f7, f10).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(n2, n3).endVertex();
        worldRenderer.pos(f12 - f2 * f11 + f5 * f11, f13 + f3 * f11, f14 - f4 * f11 + f6 * f11).tex(f7, f9).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(n2, n3).endVertex();
        worldRenderer.pos(f12 + f2 * f11 + f5 * f11, f13 + f3 * f11, f14 + f4 * f11 + f6 * f11).tex(f8, f9).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(n2, n3).endVertex();
        worldRenderer.pos(f12 + f2 * f11 - f5 * f11, f13 - f3 * f11, f14 + f4 * f11 - f6 * f11).tex(f8, f10).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(n2, n3).endVertex();
    }

    public static class Factory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            return new EntityDiggingFX(world, d, d2, d3, d4, d5, d6, Block.getStateById(nArray[0])).func_174845_l();
        }
    }
}

