/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityFootStepFX
extends EntityFX {
    private static final ResourceLocation FOOTPRINT_TEXTURE = new ResourceLocation("textures/particle/footprint.png");
    private int footstepAge;
    private TextureManager currentFootSteps;
    private int footstepMaxAge;

    protected EntityFootStepFX(TextureManager textureManager, World world, double d, double d2, double d3) {
        super(world, d, d2, d3, 0.0, 0.0, 0.0);
        this.currentFootSteps = textureManager;
        this.motionZ = 0.0;
        this.motionY = 0.0;
        this.motionX = 0.0;
        this.footstepMaxAge = 200;
    }

    @Override
    public int getFXLayer() {
        return 3;
    }

    @Override
    public void onUpdate() {
        ++this.footstepAge;
        if (this.footstepAge == this.footstepMaxAge) {
            this.setDead();
        }
    }

    @Override
    public void renderParticle(WorldRenderer worldRenderer, Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        float f7;
        float f8 = ((float)this.footstepAge + f) / (float)this.footstepMaxAge;
        if ((f7 = 2.0f - (f8 *= f8) * 2.0f) > 1.0f) {
            f7 = 1.0f;
        }
        f7 *= 0.2f;
        GlStateManager.disableLighting();
        float f9 = 0.125f;
        float f10 = (float)(this.posX - interpPosX);
        float f11 = (float)(this.posY - interpPosY);
        float f12 = (float)(this.posZ - interpPosZ);
        float f13 = this.worldObj.getLightBrightness(new BlockPos(this));
        this.currentFootSteps.bindTexture(FOOTPRINT_TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldRenderer.pos(f10 - 0.125f, f11, f12 + 0.125f).tex(0.0, 1.0).color(f13, f13, f13, f7).endVertex();
        worldRenderer.pos(f10 + 0.125f, f11, f12 + 0.125f).tex(1.0, 1.0).color(f13, f13, f13, f7).endVertex();
        worldRenderer.pos(f10 + 0.125f, f11, f12 - 0.125f).tex(1.0, 0.0).color(f13, f13, f13, f7).endVertex();
        worldRenderer.pos(f10 - 0.125f, f11, f12 - 0.125f).tex(0.0, 0.0).color(f13, f13, f13, f7).endVertex();
        Tessellator.getInstance().draw();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
    }

    public static class Factory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            return new EntityFootStepFX(Minecraft.getMinecraft().getTextureManager(), world, d, d2, d3);
        }
    }
}

