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
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class Barrier
extends EntityFX {
    @Override
    public int getFXLayer() {
        return 1;
    }

    @Override
    public void renderParticle(WorldRenderer worldRenderer, Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        float f7 = this.particleIcon.getMinU();
        float f8 = this.particleIcon.getMaxU();
        float f9 = this.particleIcon.getMinV();
        float f10 = this.particleIcon.getMaxV();
        float f11 = 0.5f;
        float f12 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f - interpPosX);
        float f13 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f - interpPosY);
        float f14 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f - interpPosZ);
        int n = this.getBrightnessForRender(f);
        int n2 = n >> 16 & 0xFFFF;
        int n3 = n & 0xFFFF;
        worldRenderer.pos(f12 - f2 * 0.5f - f5 * 0.5f, f13 - f3 * 0.5f, f14 - f4 * 0.5f - f6 * 0.5f).tex(f8, f10).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(n2, n3).endVertex();
        worldRenderer.pos(f12 - f2 * 0.5f + f5 * 0.5f, f13 + f3 * 0.5f, f14 - f4 * 0.5f + f6 * 0.5f).tex(f8, f9).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(n2, n3).endVertex();
        worldRenderer.pos(f12 + f2 * 0.5f + f5 * 0.5f, f13 + f3 * 0.5f, f14 + f4 * 0.5f + f6 * 0.5f).tex(f7, f9).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(n2, n3).endVertex();
        worldRenderer.pos(f12 + f2 * 0.5f - f5 * 0.5f, f13 - f3 * 0.5f, f14 + f4 * 0.5f - f6 * 0.5f).tex(f7, f10).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(n2, n3).endVertex();
    }

    protected Barrier(World world, double d, double d2, double d3, Item item) {
        super(world, d, d2, d3, 0.0, 0.0, 0.0);
        this.setParticleIcon(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(item));
        this.particleBlue = 1.0f;
        this.particleGreen = 1.0f;
        this.particleRed = 1.0f;
        this.motionZ = 0.0;
        this.motionY = 0.0;
        this.motionX = 0.0;
        this.particleGravity = 0.0f;
        this.particleMaxAge = 80;
    }

    public static class Factory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            return new Barrier(world, d, d2, d3, Item.getItemFromBlock(Blocks.barrier));
        }
    }
}

