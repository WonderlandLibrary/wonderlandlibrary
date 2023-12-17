// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.BlockFalling;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.world.World;

public class ParticleFallingDust extends Particle
{
    float oSize;
    final float rotSpeed;
    
    protected ParticleFallingDust(final World p_i47135_1_, final double p_i47135_2_, final double p_i47135_4_, final double p_i47135_6_, final float p_i47135_8_, final float p_i47135_9_, final float p_i47135_10_) {
        super(p_i47135_1_, p_i47135_2_, p_i47135_4_, p_i47135_6_, 0.0, 0.0, 0.0);
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.particleRed = p_i47135_8_;
        this.particleGreen = p_i47135_9_;
        this.particleBlue = p_i47135_10_;
        final float f = 0.9f;
        this.particleScale *= 0.75f;
        this.particleScale *= 0.9f;
        this.oSize = this.particleScale;
        this.particleMaxAge = (int)(32.0 / (Math.random() * 0.8 + 0.2));
        this.particleMaxAge *= (int)0.9f;
        this.rotSpeed = ((float)Math.random() - 0.5f) * 0.1f;
        this.particleAngle = (float)Math.random() * 6.2831855f;
    }
    
    @Override
    public void renderParticle(final BufferBuilder buffer, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
        float f = (this.particleAge + partialTicks) / this.particleMaxAge * 32.0f;
        f = MathHelper.clamp(f, 0.0f, 1.0f);
        this.particleScale = this.oSize * f;
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setExpired();
        }
        this.prevParticleAngle = this.particleAngle;
        this.particleAngle += 3.1415927f * this.rotSpeed * 2.0f;
        if (this.onGround) {
            final float n = 0.0f;
            this.particleAngle = n;
            this.prevParticleAngle = n;
        }
        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionY -= 0.003000000026077032;
        this.motionY = Math.max(this.motionY, -0.14000000059604645);
    }
    
    public static class Factory implements IParticleFactory
    {
        @Nullable
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            final IBlockState iblockstate = Block.getStateById(p_178902_15_[0]);
            if (iblockstate.getBlock() != Blocks.AIR && iblockstate.getRenderType() == EnumBlockRenderType.INVISIBLE) {
                return null;
            }
            int i = Minecraft.getMinecraft().getBlockColors().getColor(iblockstate, worldIn, new BlockPos(xCoordIn, yCoordIn, zCoordIn));
            if (iblockstate.getBlock() instanceof BlockFalling) {
                i = ((BlockFalling)iblockstate.getBlock()).getDustColor(iblockstate);
            }
            final float f = (i >> 16 & 0xFF) / 255.0f;
            final float f2 = (i >> 8 & 0xFF) / 255.0f;
            final float f3 = (i & 0xFF) / 255.0f;
            return new ParticleFallingDust(worldIn, xCoordIn, yCoordIn, zCoordIn, f, f2, f3);
        }
    }
}
