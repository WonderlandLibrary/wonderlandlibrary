// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.world.IBlockAccess;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;

public class ParticleDigging extends Particle
{
    private final IBlockState sourceState;
    private BlockPos sourcePos;
    
    protected ParticleDigging(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final IBlockState state) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.sourceState = state;
        this.setParticleTexture(Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state));
        this.particleGravity = state.getBlock().blockParticleGravity;
        this.particleRed = 0.6f;
        this.particleGreen = 0.6f;
        this.particleBlue = 0.6f;
        this.particleScale /= 2.0f;
    }
    
    public ParticleDigging setBlockPos(final BlockPos pos) {
        this.sourcePos = pos;
        if (this.sourceState.getBlock() == Blocks.GRASS) {
            return this;
        }
        this.multiplyColor(pos);
        return this;
    }
    
    public ParticleDigging init() {
        this.sourcePos = new BlockPos(this.posX, this.posY, this.posZ);
        final Block block = this.sourceState.getBlock();
        if (block == Blocks.GRASS) {
            return this;
        }
        this.multiplyColor(this.sourcePos);
        return this;
    }
    
    protected void multiplyColor(@Nullable final BlockPos p_187154_1_) {
        final int i = Minecraft.getMinecraft().getBlockColors().colorMultiplier(this.sourceState, this.world, p_187154_1_, 0);
        this.particleRed *= (i >> 16 & 0xFF) / 255.0f;
        this.particleGreen *= (i >> 8 & 0xFF) / 255.0f;
        this.particleBlue *= (i & 0xFF) / 255.0f;
    }
    
    @Override
    public int getFXLayer() {
        return 1;
    }
    
    @Override
    public void renderParticle(final BufferBuilder buffer, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
        float f = (this.particleTextureIndexX + this.particleTextureJitterX / 4.0f) / 16.0f;
        float f2 = f + 0.015609375f;
        float f3 = (this.particleTextureIndexY + this.particleTextureJitterY / 4.0f) / 16.0f;
        float f4 = f3 + 0.015609375f;
        final float f5 = 0.1f * this.particleScale;
        if (this.particleTexture != null) {
            f = this.particleTexture.getInterpolatedU(this.particleTextureJitterX / 4.0f * 16.0f);
            f2 = this.particleTexture.getInterpolatedU((this.particleTextureJitterX + 1.0f) / 4.0f * 16.0f);
            f3 = this.particleTexture.getInterpolatedV(this.particleTextureJitterY / 4.0f * 16.0f);
            f4 = this.particleTexture.getInterpolatedV((this.particleTextureJitterY + 1.0f) / 4.0f * 16.0f);
        }
        final float f6 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - ParticleDigging.interpPosX);
        final float f7 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - ParticleDigging.interpPosY);
        final float f8 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - ParticleDigging.interpPosZ);
        final int i = this.getBrightnessForRender(partialTicks);
        final int j = i >> 16 & 0xFFFF;
        final int k = i & 0xFFFF;
        buffer.pos(f6 - rotationX * f5 - rotationXY * f5, f7 - rotationZ * f5, f8 - rotationYZ * f5 - rotationXZ * f5).tex(f, f4).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j, k).endVertex();
        buffer.pos(f6 - rotationX * f5 + rotationXY * f5, f7 + rotationZ * f5, f8 - rotationYZ * f5 + rotationXZ * f5).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j, k).endVertex();
        buffer.pos(f6 + rotationX * f5 + rotationXY * f5, f7 + rotationZ * f5, f8 + rotationYZ * f5 + rotationXZ * f5).tex(f2, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j, k).endVertex();
        buffer.pos(f6 + rotationX * f5 - rotationXY * f5, f7 - rotationZ * f5, f8 + rotationYZ * f5 - rotationXZ * f5).tex(f2, f4).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j, k).endVertex();
    }
    
    @Override
    public int getBrightnessForRender(final float partialTick) {
        final int i = super.getBrightnessForRender(partialTick);
        int j = 0;
        if (this.world.isBlockLoaded(this.sourcePos)) {
            j = this.world.getCombinedLight(this.sourcePos, 0);
        }
        return (i == 0) ? j : i;
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new ParticleDigging(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, Block.getStateById(p_178902_15_[0])).init();
        }
    }
}
