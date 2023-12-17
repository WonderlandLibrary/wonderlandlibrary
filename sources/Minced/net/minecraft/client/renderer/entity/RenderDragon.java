// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerEnderDragonDeath;
import net.minecraft.client.renderer.entity.layers.LayerEnderDragonEyes;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelDragon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.boss.EntityDragon;

public class RenderDragon extends RenderLiving<EntityDragon>
{
    public static final ResourceLocation ENDERCRYSTAL_BEAM_TEXTURES;
    private static final ResourceLocation DRAGON_EXPLODING_TEXTURES;
    private static final ResourceLocation DRAGON_TEXTURES;
    
    public RenderDragon(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelDragon(0.0f), 0.5f);
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerEnderDragonEyes(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerEnderDragonDeath());
    }
    
    @Override
    protected void applyRotations(final EntityDragon entityLiving, final float ageInTicks, final float rotationYaw, final float partialTicks) {
        final float f = (float)entityLiving.getMovementOffsets(7, partialTicks)[0];
        final float f2 = (float)(entityLiving.getMovementOffsets(5, partialTicks)[1] - entityLiving.getMovementOffsets(10, partialTicks)[1]);
        GlStateManager.rotate(-f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f2 * 10.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 1.0f);
        if (entityLiving.deathTime > 0) {
            float f3 = (entityLiving.deathTime + partialTicks - 1.0f) / 20.0f * 1.6f;
            f3 = MathHelper.sqrt(f3);
            if (f3 > 1.0f) {
                f3 = 1.0f;
            }
            GlStateManager.rotate(f3 * this.getDeathMaxRotation(entityLiving), 0.0f, 0.0f, 1.0f);
        }
    }
    
    @Override
    protected void renderModel(final EntityDragon entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor) {
        if (entitylivingbaseIn.deathTicks > 0) {
            final float f = entitylivingbaseIn.deathTicks / 200.0f;
            GlStateManager.depthFunc(515);
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, f);
            this.bindTexture(RenderDragon.DRAGON_EXPLODING_TEXTURES);
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
    public void doRender(final EntityDragon entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        if (entity.healingEnderCrystal != null) {
            this.bindTexture(RenderDragon.ENDERCRYSTAL_BEAM_TEXTURES);
            float f = MathHelper.sin((entity.healingEnderCrystal.ticksExisted + partialTicks) * 0.2f) / 2.0f + 0.5f;
            f = (f * f + f) * 0.2f;
            renderCrystalBeams(x, y, z, partialTicks, entity.posX + (entity.prevPosX - entity.posX) * (1.0f - partialTicks), entity.posY + (entity.prevPosY - entity.posY) * (1.0f - partialTicks), entity.posZ + (entity.prevPosZ - entity.posZ) * (1.0f - partialTicks), entity.ticksExisted, entity.healingEnderCrystal.posX, f + entity.healingEnderCrystal.posY, entity.healingEnderCrystal.posZ);
        }
    }
    
    public static void renderCrystalBeams(final double p_188325_0_, final double p_188325_2_, final double p_188325_4_, final float p_188325_6_, final double p_188325_7_, final double p_188325_9_, final double p_188325_11_, final int p_188325_13_, final double p_188325_14_, final double p_188325_16_, final double p_188325_18_) {
        final float f = (float)(p_188325_14_ - p_188325_7_);
        final float f2 = (float)(p_188325_16_ - 1.0 - p_188325_9_);
        final float f3 = (float)(p_188325_18_ - p_188325_11_);
        final float f4 = MathHelper.sqrt(f * f + f3 * f3);
        final float f5 = MathHelper.sqrt(f * f + f2 * f2 + f3 * f3);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_188325_0_, (float)p_188325_2_ + 2.0f, (float)p_188325_4_);
        GlStateManager.rotate((float)(-Math.atan2(f3, f)) * 57.295776f - 90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate((float)(-Math.atan2(f4, f2)) * 57.295776f - 90.0f, 1.0f, 0.0f, 0.0f);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        GlStateManager.shadeModel(7425);
        final float f6 = 0.0f - (p_188325_13_ + p_188325_6_) * 0.01f;
        final float f7 = MathHelper.sqrt(f * f + f2 * f2 + f3 * f3) / 32.0f - (p_188325_13_ + p_188325_6_) * 0.01f;
        bufferbuilder.begin(5, DefaultVertexFormats.POSITION_TEX_COLOR);
        final int i = 8;
        for (int j = 0; j <= 8; ++j) {
            final float f8 = MathHelper.sin(j % 8 * 6.2831855f / 8.0f) * 0.75f;
            final float f9 = MathHelper.cos(j % 8 * 6.2831855f / 8.0f) * 0.75f;
            final float f10 = j % 8 / 8.0f;
            bufferbuilder.pos(f8 * 0.2f, f9 * 0.2f, 0.0).tex(f10, f6).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(f8, f9, f5).tex(f10, f7).color(255, 255, 255, 255).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableCull();
        GlStateManager.shadeModel(7424);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityDragon entity) {
        return RenderDragon.DRAGON_TEXTURES;
    }
    
    static {
        ENDERCRYSTAL_BEAM_TEXTURES = new ResourceLocation("textures/entity/endercrystal/endercrystal_beam.png");
        DRAGON_EXPLODING_TEXTURES = new ResourceLocation("textures/entity/enderdragon/dragon_exploding.png");
        DRAGON_TEXTURES = new ResourceLocation("textures/entity/enderdragon/dragon.png");
    }
}
