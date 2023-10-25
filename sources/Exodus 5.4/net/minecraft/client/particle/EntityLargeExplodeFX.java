/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityLargeExplodeFX
extends EntityFX {
    private int field_70581_a;
    private float field_70582_as;
    private static final VertexFormat field_181549_az;
    private static final ResourceLocation EXPLOSION_TEXTURE;
    private int field_70584_aq;
    private TextureManager theRenderEngine;

    static {
        EXPLOSION_TEXTURE = new ResourceLocation("textures/entity/explosion.png");
        field_181549_az = new VertexFormat().func_181721_a(DefaultVertexFormats.POSITION_3F).func_181721_a(DefaultVertexFormats.TEX_2F).func_181721_a(DefaultVertexFormats.COLOR_4UB).func_181721_a(DefaultVertexFormats.TEX_2S).func_181721_a(DefaultVertexFormats.NORMAL_3B).func_181721_a(DefaultVertexFormats.PADDING_1B);
    }

    @Override
    public int getBrightnessForRender(float f) {
        return 61680;
    }

    @Override
    public int getFXLayer() {
        return 3;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        ++this.field_70581_a;
        if (this.field_70581_a == this.field_70584_aq) {
            this.setDead();
        }
    }

    protected EntityLargeExplodeFX(TextureManager textureManager, World world, double d, double d2, double d3, double d4, double d5, double d6) {
        super(world, d, d2, d3, 0.0, 0.0, 0.0);
        this.theRenderEngine = textureManager;
        this.field_70584_aq = 6 + this.rand.nextInt(4);
        this.particleGreen = this.particleBlue = this.rand.nextFloat() * 0.6f + 0.4f;
        this.particleRed = this.particleBlue;
        this.field_70582_as = 1.0f - (float)d4 * 0.5f;
    }

    @Override
    public void renderParticle(WorldRenderer worldRenderer, Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        int n = (int)(((float)this.field_70581_a + f) * 15.0f / (float)this.field_70584_aq);
        if (n <= 15) {
            this.theRenderEngine.bindTexture(EXPLOSION_TEXTURE);
            float f7 = (float)(n % 4) / 4.0f;
            float f8 = f7 + 0.24975f;
            float f9 = (float)(n / 4) / 4.0f;
            float f10 = f9 + 0.24975f;
            float f11 = 2.0f * this.field_70582_as;
            float f12 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f - interpPosX);
            float f13 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f - interpPosY);
            float f14 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f - interpPosZ);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableLighting();
            RenderHelper.disableStandardItemLighting();
            worldRenderer.begin(7, field_181549_az);
            worldRenderer.pos(f12 - f2 * f11 - f5 * f11, f13 - f3 * f11, f14 - f4 * f11 - f6 * f11).tex(f8, f10).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(0, 240).normal(0.0f, 1.0f, 0.0f).endVertex();
            worldRenderer.pos(f12 - f2 * f11 + f5 * f11, f13 + f3 * f11, f14 - f4 * f11 + f6 * f11).tex(f8, f9).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(0, 240).normal(0.0f, 1.0f, 0.0f).endVertex();
            worldRenderer.pos(f12 + f2 * f11 + f5 * f11, f13 + f3 * f11, f14 + f4 * f11 + f6 * f11).tex(f7, f9).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(0, 240).normal(0.0f, 1.0f, 0.0f).endVertex();
            worldRenderer.pos(f12 + f2 * f11 - f5 * f11, f13 - f3 * f11, f14 + f4 * f11 - f6 * f11).tex(f7, f10).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(0, 240).normal(0.0f, 1.0f, 0.0f).endVertex();
            Tessellator.getInstance().draw();
            GlStateManager.enableLighting();
        }
    }

    public static class Factory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            return new EntityLargeExplodeFX(Minecraft.getMinecraft().getTextureManager(), world, d, d2, d3, d4, d5, d6);
        }
    }
}

