package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class EntityBreakingFX
extends EntityFX {
    protected EntityBreakingFX(World worldIn, double posXIn, double posYIn, double posZIn, Item p_i1195_8_) {
        this(worldIn, posXIn, posYIn, posZIn, p_i1195_8_, 0);
    }

    protected EntityBreakingFX(World worldIn, double posXIn, double posYIn, double posZIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, Item p_i1197_14_, int p_i1197_15_) {
        this(worldIn, posXIn, posYIn, posZIn, p_i1197_14_, p_i1197_15_);
        this.motionX *= (double)0.1f;
        this.motionY *= (double)0.1f;
        this.motionZ *= (double)0.1f;
        this.motionX += xSpeedIn;
        this.motionY += ySpeedIn;
        this.motionZ += zSpeedIn;
    }

    protected EntityBreakingFX(World worldIn, double posXIn, double posYIn, double posZIn, Item p_i1196_8_, int p_i1196_9_) {
        super(worldIn, posXIn, posYIn, posZIn, 0.0, 0.0, 0.0);
        this.setParticleIcon(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(p_i1196_8_, p_i1196_9_));
        this.particleBlue = 1.0f;
        this.particleGreen = 1.0f;
        this.particleRed = 1.0f;
        this.particleGravity = Blocks.snow.blockParticleGravity;
        this.particleScale /= 2.0f;
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
}
