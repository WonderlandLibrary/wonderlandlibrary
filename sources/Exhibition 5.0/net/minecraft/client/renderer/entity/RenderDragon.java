// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.client.renderer.entity.layers.LayerEnderDragonDeath;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerEnderDragonEyes;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelDragon;
import net.minecraft.util.ResourceLocation;

public class RenderDragon extends RenderLiving
{
    private static final ResourceLocation enderDragonCrystalBeamTextures;
    private static final ResourceLocation enderDragonExplodingTextures;
    private static final ResourceLocation enderDragonTextures;
    protected ModelDragon modelDragon;
    private static final String __OBFID = "CL_00000988";
    
    public RenderDragon(final RenderManager p_i46183_1_) {
        super(p_i46183_1_, new ModelDragon(0.0f), 0.5f);
        this.modelDragon = (ModelDragon)this.mainModel;
        this.addLayer(new LayerEnderDragonEyes(this));
        this.addLayer(new LayerEnderDragonDeath());
    }
    
    protected void func_180575_a(final EntityDragon p_180575_1_, final float p_180575_2_, final float p_180575_3_, final float p_180575_4_) {
        final float var5 = (float)p_180575_1_.getMovementOffsets(7, p_180575_4_)[0];
        final float var6 = (float)(p_180575_1_.getMovementOffsets(5, p_180575_4_)[1] - p_180575_1_.getMovementOffsets(10, p_180575_4_)[1]);
        GlStateManager.rotate(-var5, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(var6 * 10.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 1.0f);
        if (p_180575_1_.deathTime > 0) {
            float var7 = (p_180575_1_.deathTime + p_180575_4_ - 1.0f) / 20.0f * 1.6f;
            var7 = MathHelper.sqrt_float(var7);
            if (var7 > 1.0f) {
                var7 = 1.0f;
            }
            GlStateManager.rotate(var7 * this.getDeathMaxRotation(p_180575_1_), 0.0f, 0.0f, 1.0f);
        }
    }
    
    protected void renderModel(final EntityDragon p_77036_1_, final float p_77036_2_, final float p_77036_3_, final float p_77036_4_, final float p_77036_5_, final float p_77036_6_, final float p_77036_7_) {
        if (p_77036_1_.deathTicks > 0) {
            final float var8 = p_77036_1_.deathTicks / 200.0f;
            GlStateManager.depthFunc(515);
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, var8);
            this.bindTexture(RenderDragon.enderDragonExplodingTextures);
            this.mainModel.render(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
            GlStateManager.alphaFunc(516, 0.1f);
            GlStateManager.depthFunc(514);
        }
        this.bindEntityTexture(p_77036_1_);
        this.mainModel.render(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
        if (p_77036_1_.hurtTime > 0) {
            GlStateManager.depthFunc(514);
            GlStateManager.disableTextures();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color(1.0f, 0.0f, 0.0f, 0.5f);
            this.mainModel.render(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
            GlStateManager.enableTextures();
            GlStateManager.disableBlend();
            GlStateManager.depthFunc(515);
        }
    }
    
    public void doRender(final EntityDragon p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        BossStatus.setBossStatus(p_76986_1_, false);
        super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
        if (p_76986_1_.healingEnderCrystal != null) {
            this.func_180574_a(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_9_);
        }
    }
    
    protected void func_180574_a(final EntityDragon p_180574_1_, final double p_180574_2_, final double p_180574_4_, final double p_180574_6_, final float p_180574_8_) {
        final float var9 = p_180574_1_.healingEnderCrystal.innerRotation + p_180574_8_;
        float var10 = MathHelper.sin(var9 * 0.2f) / 2.0f + 0.5f;
        var10 = (var10 * var10 + var10) * 0.2f;
        final float var11 = (float)(p_180574_1_.healingEnderCrystal.posX - p_180574_1_.posX - (p_180574_1_.prevPosX - p_180574_1_.posX) * (1.0f - p_180574_8_));
        final float var12 = (float)(var10 + p_180574_1_.healingEnderCrystal.posY - 1.0 - p_180574_1_.posY - (p_180574_1_.prevPosY - p_180574_1_.posY) * (1.0f - p_180574_8_));
        final float var13 = (float)(p_180574_1_.healingEnderCrystal.posZ - p_180574_1_.posZ - (p_180574_1_.prevPosZ - p_180574_1_.posZ) * (1.0f - p_180574_8_));
        final float var14 = MathHelper.sqrt_float(var11 * var11 + var13 * var13);
        final float var15 = MathHelper.sqrt_float(var11 * var11 + var12 * var12 + var13 * var13);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_180574_2_, (float)p_180574_4_ + 2.0f, (float)p_180574_6_);
        GlStateManager.rotate((float)(-Math.atan2(var13, var11)) * 180.0f / 3.1415927f - 90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate((float)(-Math.atan2(var14, var12)) * 180.0f / 3.1415927f - 90.0f, 1.0f, 0.0f, 0.0f);
        final Tessellator var16 = Tessellator.getInstance();
        final WorldRenderer var17 = var16.getWorldRenderer();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        this.bindTexture(RenderDragon.enderDragonCrystalBeamTextures);
        GlStateManager.shadeModel(7425);
        final float var18 = 0.0f - (p_180574_1_.ticksExisted + p_180574_8_) * 0.01f;
        final float var19 = MathHelper.sqrt_float(var11 * var11 + var12 * var12 + var13 * var13) / 32.0f - (p_180574_1_.ticksExisted + p_180574_8_) * 0.01f;
        var17.startDrawing(5);
        final byte var20 = 8;
        for (int var21 = 0; var21 <= var20; ++var21) {
            final float var22 = MathHelper.sin(var21 % var20 * 3.1415927f * 2.0f / var20) * 0.75f;
            final float var23 = MathHelper.cos(var21 % var20 * 3.1415927f * 2.0f / var20) * 0.75f;
            final float var24 = var21 % var20 * 1.0f / var20;
            var17.func_178991_c(0);
            var17.addVertexWithUV(var22 * 0.2f, var23 * 0.2f, 0.0, var24, var19);
            var17.func_178991_c(16777215);
            var17.addVertexWithUV(var22, var23, var15, var24, var18);
        }
        var16.draw();
        GlStateManager.enableCull();
        GlStateManager.shadeModel(7424);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
    
    protected ResourceLocation getEntityTexture(final EntityDragon p_110775_1_) {
        return RenderDragon.enderDragonTextures;
    }
    
    @Override
    public void doRender(final EntityLiving p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.doRender((EntityDragon)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    @Override
    protected void rotateCorpse(final EntityLivingBase p_77043_1_, final float p_77043_2_, final float p_77043_3_, final float p_77043_4_) {
        this.func_180575_a((EntityDragon)p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
    }
    
    @Override
    protected void renderModel(final EntityLivingBase p_77036_1_, final float p_77036_2_, final float p_77036_3_, final float p_77036_4_, final float p_77036_5_, final float p_77036_6_, final float p_77036_7_) {
        this.renderModel((EntityDragon)p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
    }
    
    @Override
    public void doRender(final EntityLivingBase p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.doRender((EntityDragon)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.getEntityTexture((EntityDragon)p_110775_1_);
    }
    
    @Override
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.doRender((EntityDragon)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    static {
        enderDragonCrystalBeamTextures = new ResourceLocation("textures/entity/endercrystal/endercrystal_beam.png");
        enderDragonExplodingTextures = new ResourceLocation("textures/entity/enderdragon/dragon_exploding.png");
        enderDragonTextures = new ResourceLocation("textures/entity/enderdragon/dragon.png");
    }
}
