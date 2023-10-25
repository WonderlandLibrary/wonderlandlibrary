/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class EntityBreakingFX
extends EntityFX {
    protected EntityBreakingFX(World world, double d, double d2, double d3, Item item, int n) {
        super(world, d, d2, d3, 0.0, 0.0, 0.0);
        this.setParticleIcon(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(item, n));
        this.particleBlue = 1.0f;
        this.particleGreen = 1.0f;
        this.particleRed = 1.0f;
        this.particleGravity = Blocks.snow.blockParticleGravity;
        this.particleScale /= 2.0f;
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

    @Override
    public int getFXLayer() {
        return 1;
    }

    protected EntityBreakingFX(World world, double d, double d2, double d3, double d4, double d5, double d6, Item item, int n) {
        this(world, d, d2, d3, item, n);
        this.motionX *= (double)0.1f;
        this.motionY *= (double)0.1f;
        this.motionZ *= (double)0.1f;
        this.motionX += d4;
        this.motionY += d5;
        this.motionZ += d6;
    }

    protected EntityBreakingFX(World world, double d, double d2, double d3, Item item) {
        this(world, d, d2, d3, item, 0);
    }

    public static class Factory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            int n2 = nArray.length > 1 ? nArray[1] : 0;
            return new EntityBreakingFX(world, d, d2, d3, d4, d5, d6, Item.getItemById(nArray[0]), n2);
        }
    }

    public static class SlimeFactory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            return new EntityBreakingFX(world, d, d2, d3, Items.slime_ball);
        }
    }

    public static class SnowballFactory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            return new EntityBreakingFX(world, d, d2, d3, Items.snowball);
        }
    }
}

