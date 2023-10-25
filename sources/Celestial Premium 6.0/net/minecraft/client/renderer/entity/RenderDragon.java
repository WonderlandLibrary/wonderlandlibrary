/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelDragon;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerEnderDragonDeath;
import net.minecraft.client.renderer.entity.layers.LayerEnderDragonEyes;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderDragon
extends RenderLiving<EntityDragon> {
    public static final ResourceLocation ENDERCRYSTAL_BEAM_TEXTURES = new ResourceLocation("textures/entity/endercrystal/endercrystal_beam.png");
    private static final ResourceLocation DRAGON_EXPLODING_TEXTURES = new ResourceLocation("textures/entity/enderdragon/dragon_exploding.png");
    private static final ResourceLocation DRAGON_TEXTURES = new ResourceLocation("textures/entity/enderdragon/dragon.png");

    public RenderDragon(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelDragon(0.0f), 0.5f);
        this.addLayer(new LayerEnderDragonEyes(this));
        this.addLayer(new LayerEnderDragonDeath());
    }

    @Override
    protected void rotateCorpse(EntityDragon entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks) {
        float f = (float)entityLiving.getMovementOffsets(7, partialTicks)[0];
        float f1 = (float)(entityLiving.getMovementOffsets(5, partialTicks)[1] - entityLiving.getMovementOffsets(10, partialTicks)[1]);
        GlStateManager.rotate(-f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f1 * 10.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 1.0f);
        if (entityLiving.deathTime > 0) {
            float f2 = ((float)entityLiving.deathTime + partialTicks - 1.0f) / 20.0f * 1.6f;
            if ((f2 = MathHelper.sqrt(f2)) > 1.0f) {
                f2 = 1.0f;
            }
            GlStateManager.rotate(f2 * this.getDeathMaxRotation(entityLiving), 0.0f, 0.0f, 1.0f);
        }
    }

    @Override
    protected void renderModel(EntityDragon entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        if (entitylivingbaseIn.deathTicks > 0) {
            float f = (float)entitylivingbaseIn.deathTicks / 200.0f;
            GlStateManager.depthFunc(515);
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, f);
            this.bindTexture(DRAGON_EXPLODING_TEXTURES);
            this.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            GlStateManager.alphaFunc(516, 0.1f);
            GlStateManager.depthFunc(514);
        }
        this.bindEntityTexture(entitylivingbaseIn);
        this.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        if (entitylivingbaseIn.hurtTime > 0) {
            GlStateManager.depthFunc(514);
            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(1.0f, 0.0f, 0.0f, 0.5f);
            this.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.depthFunc(515);
        }
    }

    @Override
    public void doRender(EntityDragon entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        if (entity.healingEnderCrystal != null) {
            this.bindTexture(ENDERCRYSTAL_BEAM_TEXTURES);
            float f = MathHelper.sin(((float)entity.healingEnderCrystal.ticksExisted + partialTicks) * 0.2f) / 2.0f + 0.5f;
            f = (f * f + f) * 0.2f;
            RenderDragon.renderCrystalBeams(x, y, z, partialTicks, entity.posX + (entity.prevPosX - entity.posX) * (double)(1.0f - partialTicks), entity.posY + (entity.prevPosY - entity.posY) * (double)(1.0f - partialTicks), entity.posZ + (entity.prevPosZ - entity.posZ) * (double)(1.0f - partialTicks), entity.ticksExisted, entity.healingEnderCrystal.posX, (double)f + entity.healingEnderCrystal.posY, entity.healingEnderCrystal.posZ);
        }
    }

    public static void renderCrystalBeams(double p_188325_0_, double p_188325_2_, double p_188325_4_, float p_188325_6_, double p_188325_7_, double p_188325_9_, double p_188325_11_, int p_188325_13_, double p_188325_14_, double p_188325_16_, double p_188325_18_) {
        float f = (float)(p_188325_14_ - p_188325_7_);
        float f1 = (float)(p_188325_16_ - 1.0 - p_188325_9_);
        float f2 = (float)(p_188325_18_ - p_188325_11_);
        float f3 = MathHelper.sqrt(f * f + f2 * f2);
        float f4 = MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_188325_0_, (float)p_188325_2_ + 2.0f, (float)p_188325_4_);
        GlStateManager.rotate((float)(-Math.atan2(f2, f)) * 57.295776f - 90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate((float)(-Math.atan2(f3, f1)) * 57.295776f - 90.0f, 1.0f, 0.0f, 0.0f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        GlStateManager.shadeModel(7425);
        float f5 = 0.0f - ((float)p_188325_13_ + p_188325_6_) * 0.01f;
        float f6 = MathHelper.sqrt(f * f + f1 * f1 + f2 * f2) / 32.0f - ((float)p_188325_13_ + p_188325_6_) * 0.01f;
        bufferbuilder.begin(5, DefaultVertexFormats.POSITION_TEX_COLOR);
        int i = 8;
        for (int j = 0; j <= 8; ++j) {
            float f7 = MathHelper.sin((float)(j % 8) * ((float)Math.PI * 2) / 8.0f) * 0.75f;
            float f8 = MathHelper.cos((float)(j % 8) * ((float)Math.PI * 2) / 8.0f) * 0.75f;
            float f9 = (float)(j % 8) / 8.0f;
            bufferbuilder.pos(f7 * 0.2f, f8 * 0.2f, 0.0).tex(f9, f5).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(f7, f8, f4).tex(f9, f6).color(255, 255, 255, 255).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableCull();
        GlStateManager.shadeModel(7424);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityDragon entity) {
        return DRAGON_TEXTURES;
    }
}

