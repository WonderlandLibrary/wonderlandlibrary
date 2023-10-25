/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelDragon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerEnderDragonDeath;
import net.minecraft.client.renderer.entity.layers.LayerEnderDragonEyes;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderDragon
extends RenderLiving<EntityDragon> {
    private static final ResourceLocation enderDragonTextures;
    private static final ResourceLocation enderDragonCrystalBeamTextures;
    protected ModelDragon modelDragon;
    private static final ResourceLocation enderDragonExplodingTextures;

    @Override
    public void doRender(EntityDragon entityDragon, double d, double d2, double d3, float f, float f2) {
        BossStatus.setBossStatus(entityDragon, false);
        super.doRender(entityDragon, d, d2, d3, f, f2);
        if (entityDragon.healingEnderCrystal != null) {
            this.drawRechargeRay(entityDragon, d, d2, d3, f2);
        }
    }

    public RenderDragon(RenderManager renderManager) {
        super(renderManager, new ModelDragon(0.0f), 0.5f);
        this.modelDragon = (ModelDragon)this.mainModel;
        this.addLayer(new LayerEnderDragonEyes(this));
        this.addLayer(new LayerEnderDragonDeath());
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityDragon entityDragon) {
        return enderDragonTextures;
    }

    static {
        enderDragonCrystalBeamTextures = new ResourceLocation("textures/entity/endercrystal/endercrystal_beam.png");
        enderDragonExplodingTextures = new ResourceLocation("textures/entity/enderdragon/dragon_exploding.png");
        enderDragonTextures = new ResourceLocation("textures/entity/enderdragon/dragon.png");
    }

    @Override
    protected void rotateCorpse(EntityDragon entityDragon, float f, float f2, float f3) {
        float f4 = (float)entityDragon.getMovementOffsets(7, f3)[0];
        float f5 = (float)(entityDragon.getMovementOffsets(5, f3)[1] - entityDragon.getMovementOffsets(10, f3)[1]);
        GlStateManager.rotate(-f4, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f5 * 10.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 1.0f);
        if (entityDragon.deathTime > 0) {
            float f6 = ((float)entityDragon.deathTime + f3 - 1.0f) / 20.0f * 1.6f;
            if ((f6 = MathHelper.sqrt_float(f6)) > 1.0f) {
                f6 = 1.0f;
            }
            GlStateManager.rotate(f6 * this.getDeathMaxRotation(entityDragon), 0.0f, 0.0f, 1.0f);
        }
    }

    protected void drawRechargeRay(EntityDragon entityDragon, double d, double d2, double d3, float f) {
        float f2 = (float)entityDragon.healingEnderCrystal.innerRotation + f;
        float f3 = MathHelper.sin(f2 * 0.2f) / 2.0f + 0.5f;
        f3 = (f3 * f3 + f3) * 0.2f;
        float f4 = (float)(entityDragon.healingEnderCrystal.posX - entityDragon.posX - (entityDragon.prevPosX - entityDragon.posX) * (double)(1.0f - f));
        float f5 = (float)((double)f3 + entityDragon.healingEnderCrystal.posY - 1.0 - entityDragon.posY - (entityDragon.prevPosY - entityDragon.posY) * (double)(1.0f - f));
        float f6 = (float)(entityDragon.healingEnderCrystal.posZ - entityDragon.posZ - (entityDragon.prevPosZ - entityDragon.posZ) * (double)(1.0f - f));
        float f7 = MathHelper.sqrt_float(f4 * f4 + f6 * f6);
        float f8 = MathHelper.sqrt_float(f4 * f4 + f5 * f5 + f6 * f6);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)d, (float)d2 + 2.0f, (float)d3);
        GlStateManager.rotate((float)(-Math.atan2(f6, f4)) * 180.0f / (float)Math.PI - 90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate((float)(-Math.atan2(f7, f5)) * 180.0f / (float)Math.PI - 90.0f, 1.0f, 0.0f, 0.0f);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        this.bindTexture(enderDragonCrystalBeamTextures);
        GlStateManager.shadeModel(7425);
        float f9 = 0.0f - ((float)entityDragon.ticksExisted + f) * 0.01f;
        float f10 = MathHelper.sqrt_float(f4 * f4 + f5 * f5 + f6 * f6) / 32.0f - ((float)entityDragon.ticksExisted + f) * 0.01f;
        worldRenderer.begin(5, DefaultVertexFormats.POSITION_TEX_COLOR);
        int n = 8;
        int n2 = 0;
        while (n2 <= 8) {
            float f11 = MathHelper.sin((float)(n2 % 8) * (float)Math.PI * 2.0f / 8.0f) * 0.75f;
            float f12 = MathHelper.cos((float)(n2 % 8) * (float)Math.PI * 2.0f / 8.0f) * 0.75f;
            float f13 = (float)(n2 % 8) * 1.0f / 8.0f;
            worldRenderer.pos(f11 * 0.2f, f12 * 0.2f, 0.0).tex(f13, f10).color(0, 0, 0, 255).endVertex();
            worldRenderer.pos(f11, f12, f8).tex(f13, f9).color(255, 255, 255, 255).endVertex();
            ++n2;
        }
        tessellator.draw();
        GlStateManager.enableCull();
        GlStateManager.shadeModel(7424);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    @Override
    protected void renderModel(EntityDragon entityDragon, float f, float f2, float f3, float f4, float f5, float f6) {
        if (entityDragon.deathTicks > 0) {
            float f7 = (float)entityDragon.deathTicks / 200.0f;
            GlStateManager.depthFunc(515);
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, f7);
            this.bindTexture(enderDragonExplodingTextures);
            this.mainModel.render(entityDragon, f, f2, f3, f4, f5, f6);
            GlStateManager.alphaFunc(516, 0.1f);
            GlStateManager.depthFunc(514);
        }
        this.bindEntityTexture(entityDragon);
        this.mainModel.render(entityDragon, f, f2, f3, f4, f5, f6);
        if (entityDragon.hurtTime > 0) {
            GlStateManager.depthFunc(514);
            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color(1.0f, 0.0f, 0.0f, 0.5f);
            this.mainModel.render(entityDragon, f, f2, f3, f4, f5, f6);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.depthFunc(515);
        }
    }
}

