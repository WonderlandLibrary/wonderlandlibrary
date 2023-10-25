package net.minecraft.client.particle;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class EntityDiggingFX
extends EntityFX {
    private IBlockState sourceState;
    private BlockPos sourcePos;

    protected EntityDiggingFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, IBlockState state) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.sourceState = state;
        this.setParticleIcon(Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state));
        this.particleGravity = state.getBlock().blockParticleGravity;
        this.particleBlue = 0.6f;
        this.particleGreen = 0.6f;
        this.particleRed = 0.6f;
        this.particleScale /= 2.0f;
    }

    public EntityDiggingFX setBlockPos(BlockPos pos) {
        this.sourcePos = pos;
        if (this.sourceState.getBlock() == Blocks.grass) {
            return this;
        }
        int i = this.sourceState.getBlock().colorMultiplier((IBlockAccess)this.worldObj, pos);
        this.particleRed *= (float)(i >> 16 & 0xFF) / 255.0f;
        this.particleGreen *= (float)(i >> 8 & 0xFF) / 255.0f;
        this.particleBlue *= (float)(i & 0xFF) / 255.0f;
        return this;
    }

    public EntityDiggingFX func_174845_l() {
        this.sourcePos = new BlockPos(this.posX, this.posY, this.posZ);
        Block block = this.sourceState.getBlock();
        if (block == Blocks.grass) {
            return this;
        }
        int i = block.getRenderColor(this.sourceState);
        this.particleRed *= (float)(i >> 16 & 0xFF) / 255.0f;
        this.particleGreen *= (float)(i >> 8 & 0xFF) / 255.0f;
        this.particleBlue *= (float)(i & 0xFF) / 255.0f;
        return this;
    }

    public int getFXLayer() {
        return 1;
    }

    public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        float f = ((float)this.particleTextureIndexX + this.particleTextureJitterX / 4.0f) / 16.0f;
        float f1 = f + 0.015609375f;
        float f2 = ((float)this.particleTextureIndexY + this.particleTextureJitterY / 4.0f) / 16.0f;
        float f3 = f2 + 0.015609375f;
        float f4 = 0.1f * this.particleScale;
        if (this.particleIcon != null) {
            f = this.particleIcon.getInterpolatedU((double)(this.particleTextureJitterX / 4.0f * 16.0f));
            f1 = this.particleIcon.getInterpolatedU((double)((this.particleTextureJitterX + 1.0f) / 4.0f * 16.0f));
            f2 = this.particleIcon.getInterpolatedV((double)(this.particleTextureJitterY / 4.0f * 16.0f));
            f3 = this.particleIcon.getInterpolatedV((double)((this.particleTextureJitterY + 1.0f) / 4.0f * 16.0f));
        }
        float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
        float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
        float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);
        int i = this.getBrightnessForRender(partialTicks);
        int j = i >> 16 & 0xFFFF;
        int k = i & 0xFFFF;
        worldRendererIn.pos((double)(f5 - rotationX * f4 - rotationXY * f4), (double)(f6 - rotationZ * f4), (double)(f7 - rotationYZ * f4 - rotationXZ * f4)).tex((double)f, (double)f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j, k).endVertex();
        worldRendererIn.pos((double)(f5 - rotationX * f4 + rotationXY * f4), (double)(f6 + rotationZ * f4), (double)(f7 - rotationYZ * f4 + rotationXZ * f4)).tex((double)f, (double)f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j, k).endVertex();
        worldRendererIn.pos((double)(f5 + rotationX * f4 + rotationXY * f4), (double)(f6 + rotationZ * f4), (double)(f7 + rotationYZ * f4 + rotationXZ * f4)).tex((double)f1, (double)f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j, k).endVertex();
        worldRendererIn.pos((double)(f5 + rotationX * f4 - rotationXY * f4), (double)(f6 - rotationZ * f4), (double)(f7 + rotationYZ * f4 - rotationXZ * f4)).tex((double)f1, (double)f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j, k).endVertex();
    }

    public int getBrightnessForRender(float partialTicks) {
        int i = super.getBrightnessForRender(partialTicks);
        int j = 0;
        if (this.worldObj.isBlockLoaded(this.sourcePos)) {
            j = this.worldObj.getCombinedLight(this.sourcePos, 0);
        }
        return i == 0 ? j : i;
    }
}
