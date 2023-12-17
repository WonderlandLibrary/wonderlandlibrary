// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.init.Items;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.init.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class ParticleBreaking extends Particle
{
    protected ParticleBreaking(final World worldIn, final double posXIn, final double posYIn, final double posZIn, final Item itemIn) {
        this(worldIn, posXIn, posYIn, posZIn, itemIn, 0);
    }
    
    protected ParticleBreaking(final World worldIn, final double posXIn, final double posYIn, final double posZIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final Item itemIn, final int meta) {
        this(worldIn, posXIn, posYIn, posZIn, itemIn, meta);
        this.motionX *= 0.10000000149011612;
        this.motionY *= 0.10000000149011612;
        this.motionZ *= 0.10000000149011612;
        this.motionX += xSpeedIn;
        this.motionY += ySpeedIn;
        this.motionZ += zSpeedIn;
    }
    
    protected ParticleBreaking(final World worldIn, final double posXIn, final double posYIn, final double posZIn, final Item itemIn, final int meta) {
        super(worldIn, posXIn, posYIn, posZIn, 0.0, 0.0, 0.0);
        this.setParticleTexture(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(itemIn, meta));
        this.particleRed = 1.0f;
        this.particleGreen = 1.0f;
        this.particleBlue = 1.0f;
        this.particleGravity = Blocks.SNOW.blockParticleGravity;
        this.particleScale /= 2.0f;
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
        final float f6 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - ParticleBreaking.interpPosX);
        final float f7 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - ParticleBreaking.interpPosY);
        final float f8 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - ParticleBreaking.interpPosZ);
        final int i = this.getBrightnessForRender(partialTicks);
        final int j = i >> 16 & 0xFFFF;
        final int k = i & 0xFFFF;
        buffer.pos(f6 - rotationX * f5 - rotationXY * f5, f7 - rotationZ * f5, f8 - rotationYZ * f5 - rotationXZ * f5).tex(f, f4).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j, k).endVertex();
        buffer.pos(f6 - rotationX * f5 + rotationXY * f5, f7 + rotationZ * f5, f8 - rotationYZ * f5 + rotationXZ * f5).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j, k).endVertex();
        buffer.pos(f6 + rotationX * f5 + rotationXY * f5, f7 + rotationZ * f5, f8 + rotationYZ * f5 + rotationXZ * f5).tex(f2, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j, k).endVertex();
        buffer.pos(f6 + rotationX * f5 - rotationXY * f5, f7 - rotationZ * f5, f8 + rotationYZ * f5 - rotationXZ * f5).tex(f2, f4).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j, k).endVertex();
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            final int i = (p_178902_15_.length > 1) ? p_178902_15_[1] : 0;
            return new ParticleBreaking(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, Item.getItemById(p_178902_15_[0]), i);
        }
    }
    
    public static class SlimeFactory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new ParticleBreaking(worldIn, xCoordIn, yCoordIn, zCoordIn, Items.SLIME_BALL);
        }
    }
    
    public static class SnowballFactory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new ParticleBreaking(worldIn, xCoordIn, yCoordIn, zCoordIn, Items.SNOWBALL);
        }
    }
}
