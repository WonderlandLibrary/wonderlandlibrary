// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.block.state.IBlockState;

public class EntityDiggingFX extends EntityFX
{
    private IBlockState sourceState;
    private BlockPos sourcePos;
    
    protected EntityDiggingFX(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final IBlockState state) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.sourceState = state;
        this.setParticleIcon(Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state));
        this.particleGravity = state.getBlock().blockParticleGravity;
        final float particleRed = 0.6f;
        this.particleBlue = particleRed;
        this.particleGreen = particleRed;
        this.particleRed = particleRed;
        this.particleScale /= 2.0f;
    }
    
    public EntityDiggingFX setBlockPos(final BlockPos pos) {
        this.sourcePos = pos;
        if (this.sourceState.getBlock() == Blocks.grass) {
            return this;
        }
        final int i = this.sourceState.getBlock().colorMultiplier(this.worldObj, pos);
        this.particleRed *= (i >> 16 & 0xFF) / 255.0f;
        this.particleGreen *= (i >> 8 & 0xFF) / 255.0f;
        this.particleBlue *= (i & 0xFF) / 255.0f;
        return this;
    }
    
    public EntityDiggingFX func_174845_l() {
        this.sourcePos = new BlockPos(this.posX, this.posY, this.posZ);
        final Block block = this.sourceState.getBlock();
        if (block == Blocks.grass) {
            return this;
        }
        final int i = block.getRenderColor(this.sourceState);
        this.particleRed *= (i >> 16 & 0xFF) / 255.0f;
        this.particleGreen *= (i >> 8 & 0xFF) / 255.0f;
        this.particleBlue *= (i & 0xFF) / 255.0f;
        return this;
    }
    
    @Override
    public int getFXLayer() {
        return 1;
    }
    
    @Override
    public void renderParticle(final WorldRenderer worldRendererIn, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
        float f = (this.particleTextureIndexX + this.particleTextureJitterX / 4.0f) / 16.0f;
        float f2 = f + 0.015609375f;
        float f3 = (this.particleTextureIndexY + this.particleTextureJitterY / 4.0f) / 16.0f;
        float f4 = f3 + 0.015609375f;
        final float f5 = 0.1f * this.particleScale;
        if (this.particleIcon != null) {
            f = this.particleIcon.getInterpolatedU(this.particleTextureJitterX / 4.0f * 16.0f);
            f2 = this.particleIcon.getInterpolatedU((this.particleTextureJitterX + 1.0f) / 4.0f * 16.0f);
            f3 = this.particleIcon.getInterpolatedV(this.particleTextureJitterY / 4.0f * 16.0f);
            f4 = this.particleIcon.getInterpolatedV((this.particleTextureJitterY + 1.0f) / 4.0f * 16.0f);
        }
        final float f6 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - EntityDiggingFX.interpPosX);
        final float f7 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - EntityDiggingFX.interpPosY);
        final float f8 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - EntityDiggingFX.interpPosZ);
        final int i = this.getBrightnessForRender(partialTicks);
        final int j = i >> 16 & 0xFFFF;
        final int k = i & 0xFFFF;
        worldRendererIn.pos(f6 - rotationX * f5 - rotationXY * f5, f7 - rotationZ * f5, f8 - rotationYZ * f5 - rotationXZ * f5).tex(f, f4).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j, k).endVertex();
        worldRendererIn.pos(f6 - rotationX * f5 + rotationXY * f5, f7 + rotationZ * f5, f8 - rotationYZ * f5 + rotationXZ * f5).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j, k).endVertex();
        worldRendererIn.pos(f6 + rotationX * f5 + rotationXY * f5, f7 + rotationZ * f5, f8 + rotationYZ * f5 + rotationXZ * f5).tex(f2, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j, k).endVertex();
        worldRendererIn.pos(f6 + rotationX * f5 - rotationXY * f5, f7 - rotationZ * f5, f8 + rotationYZ * f5 - rotationXZ * f5).tex(f2, f4).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j, k).endVertex();
    }
    
    @Override
    public int getBrightnessForRender(final float partialTicks) {
        final int i = super.getBrightnessForRender(partialTicks);
        int j = 0;
        if (this.worldObj.isBlockLoaded(this.sourcePos)) {
            j = this.worldObj.getCombinedLight(this.sourcePos, 0);
        }
        return (i == 0) ? j : i;
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public EntityFX getEntityFX(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new EntityDiggingFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, Block.getStateById(p_178902_15_[0])).func_174845_l();
        }
    }
}
