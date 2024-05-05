package net.minecraft.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public static class EntityFirework.OverlayFX
extends EntityFX {
    protected EntityFirework.OverlayFX(World p_i46466_1_, double p_i46466_2_, double p_i46466_4_, double p_i46466_6_) {
        super(p_i46466_1_, p_i46466_2_, p_i46466_4_, p_i46466_6_);
        this.particleMaxAge = 4;
    }

    public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        float f = 0.25f;
        float f1 = 0.5f;
        float f2 = 0.125f;
        float f3 = 0.375f;
        float f4 = 7.1f * MathHelper.sin((float)(((float)this.particleAge + partialTicks - 1.0f) * 0.25f * (float)Math.PI));
        this.particleAlpha = 0.6f - ((float)this.particleAge + partialTicks - 1.0f) * 0.25f * 0.5f;
        float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
        float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
        float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);
        int i = this.getBrightnessForRender(partialTicks);
        int j = i >> 16 & 0xFFFF;
        int k = i & 0xFFFF;
        worldRendererIn.pos((double)(f5 - rotationX * f4 - rotationXY * f4), (double)(f6 - rotationZ * f4), (double)(f7 - rotationYZ * f4 - rotationXZ * f4)).tex(0.5, 0.375).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        worldRendererIn.pos((double)(f5 - rotationX * f4 + rotationXY * f4), (double)(f6 + rotationZ * f4), (double)(f7 - rotationYZ * f4 + rotationXZ * f4)).tex(0.5, 0.125).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        worldRendererIn.pos((double)(f5 + rotationX * f4 + rotationXY * f4), (double)(f6 + rotationZ * f4), (double)(f7 + rotationYZ * f4 + rotationXZ * f4)).tex(0.25, 0.125).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        worldRendererIn.pos((double)(f5 + rotationX * f4 - rotationXY * f4), (double)(f6 - rotationZ * f4), (double)(f7 + rotationYZ * f4 - rotationXZ * f4)).tex(0.25, 0.375).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
    }
}
