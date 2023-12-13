// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.EnumAction;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;

public class RenderPlayer extends RendererLivingEntity
{
    private boolean field_177140_a;
    private static final String __OBFID = "CL_00001020";
    
    public RenderPlayer(final RenderManager p_i46102_1_) {
        this(p_i46102_1_, false);
    }
    
    public RenderPlayer(final RenderManager p_i46103_1_, final boolean p_i46103_2_) {
        super(p_i46103_1_, new ModelPlayer(0.0f, p_i46103_2_), 0.5f);
        this.field_177140_a = p_i46103_2_;
        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerArrow(this));
        this.addLayer(new LayerDeadmau5Head(this));
        this.addLayer(new LayerCape(this));
        this.addLayer(new LayerCustomHead(this.func_177136_g().bipedHead));
    }
    
    public ModelPlayer func_177136_g() {
        return (ModelPlayer)super.getMainModel();
    }
    
    public void func_180596_a(final AbstractClientPlayer p_180596_1_, final double p_180596_2_, final double p_180596_4_, final double p_180596_6_, final float p_180596_8_, final float p_180596_9_) {
        if (!p_180596_1_.func_175144_cb() || this.renderManager.livingPlayer == p_180596_1_) {
            double var10 = p_180596_4_;
            if (p_180596_1_.isSneaking() && !(p_180596_1_ instanceof EntityPlayerSP)) {
                var10 = p_180596_4_ - 0.125;
            }
            this.func_177137_d(p_180596_1_);
            super.doRender(p_180596_1_, p_180596_2_, var10, p_180596_6_, p_180596_8_, p_180596_9_);
        }
    }
    
    private void func_177137_d(final AbstractClientPlayer p_177137_1_) {
        final ModelPlayer var2 = this.func_177136_g();
        if (p_177137_1_.func_175149_v()) {
            var2.func_178719_a(false);
            var2.bipedHead.showModel = true;
            var2.bipedHeadwear.showModel = true;
        }
        else {
            final ItemStack var3 = p_177137_1_.inventory.getCurrentItem();
            var2.func_178719_a(true);
            var2.bipedHeadwear.showModel = p_177137_1_.func_175148_a(EnumPlayerModelParts.HAT);
            var2.field_178730_v.showModel = p_177137_1_.func_175148_a(EnumPlayerModelParts.JACKET);
            var2.field_178733_c.showModel = p_177137_1_.func_175148_a(EnumPlayerModelParts.LEFT_PANTS_LEG);
            var2.field_178731_d.showModel = p_177137_1_.func_175148_a(EnumPlayerModelParts.RIGHT_PANTS_LEG);
            var2.field_178734_a.showModel = p_177137_1_.func_175148_a(EnumPlayerModelParts.LEFT_SLEEVE);
            var2.field_178732_b.showModel = p_177137_1_.func_175148_a(EnumPlayerModelParts.RIGHT_SLEEVE);
            var2.heldItemLeft = 0;
            var2.aimedBow = false;
            var2.isSneak = p_177137_1_.isSneaking();
            if (var3 == null) {
                var2.heldItemRight = 0;
            }
            else {
                var2.heldItemRight = 1;
                if (p_177137_1_.getItemInUseCount() > 0) {
                    final EnumAction var4 = var3.getItemUseAction();
                    if (var4 == EnumAction.BLOCK) {
                        var2.heldItemRight = 3;
                    }
                    else if (var4 == EnumAction.BOW) {
                        var2.aimedBow = true;
                    }
                }
            }
        }
    }
    
    protected ResourceLocation func_180594_a(final AbstractClientPlayer p_180594_1_) {
        return p_180594_1_.getLocationSkin();
    }
    
    @Override
    public void func_82422_c() {
        GlStateManager.translate(0.0f, 0.1875f, 0.0f);
    }
    
    protected void preRenderCallback(final AbstractClientPlayer p_77041_1_, final float p_77041_2_) {
        final float var3 = 0.9375f;
        GlStateManager.scale(var3, var3, var3);
    }
    
    protected void renderOffsetLivingLabel(final AbstractClientPlayer p_96449_1_, final double p_96449_2_, double p_96449_4_, final double p_96449_6_, final String p_96449_8_, final float p_96449_9_, final double p_96449_10_) {
        if (p_96449_10_ < 100.0) {
            final Scoreboard var12 = p_96449_1_.getWorldScoreboard();
            final ScoreObjective var13 = var12.getObjectiveInDisplaySlot(2);
            if (var13 != null) {
                final Score var14 = var12.getValueFromObjective(p_96449_1_.getName(), var13);
                this.renderLivingLabel(p_96449_1_, var14.getScorePoints() + " " + var13.getDisplayName(), p_96449_2_, p_96449_4_, p_96449_6_, 64);
                p_96449_4_ += this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15f * p_96449_9_;
            }
        }
        super.func_177069_a(p_96449_1_, p_96449_2_, p_96449_4_, p_96449_6_, p_96449_8_, p_96449_9_, p_96449_10_);
    }
    
    public void func_177138_b(final AbstractClientPlayer p_177138_1_) {
        final float var2 = 1.0f;
        GlStateManager.color(var2, var2, var2);
        final ModelPlayer var3 = this.func_177136_g();
        this.func_177137_d(p_177138_1_);
        var3.swingProgress = 0.0f;
        var3.isSneak = false;
        var3.setRotationAngles(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f, p_177138_1_);
        var3.func_178725_a();
    }
    
    public void func_177139_c(final AbstractClientPlayer p_177139_1_) {
        final float var2 = 1.0f;
        GlStateManager.color(var2, var2, var2);
        final ModelPlayer var3 = this.func_177136_g();
        this.func_177137_d(p_177139_1_);
        var3.isSneak = false;
        var3.setRotationAngles(var3.swingProgress = 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f, p_177139_1_);
        var3.func_178726_b();
    }
    
    protected void renderLivingAt(final AbstractClientPlayer p_77039_1_, final double p_77039_2_, final double p_77039_4_, final double p_77039_6_) {
        if (p_77039_1_.isEntityAlive() && p_77039_1_.isPlayerSleeping()) {
            super.renderLivingAt(p_77039_1_, p_77039_2_ + p_77039_1_.field_71079_bU, p_77039_4_ + p_77039_1_.field_71082_cx, p_77039_6_ + p_77039_1_.field_71089_bV);
        }
        else {
            super.renderLivingAt(p_77039_1_, p_77039_2_, p_77039_4_, p_77039_6_);
        }
    }
    
    protected void func_180595_a(final AbstractClientPlayer p_180595_1_, final float p_180595_2_, final float p_180595_3_, final float p_180595_4_) {
        if (p_180595_1_.isEntityAlive() && p_180595_1_.isPlayerSleeping()) {
            GlStateManager.rotate(p_180595_1_.getBedOrientationInDegrees(), 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(this.getDeathMaxRotation(p_180595_1_), 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(270.0f, 0.0f, 1.0f, 0.0f);
        }
        else {
            super.rotateCorpse(p_180595_1_, p_180595_2_, p_180595_3_, p_180595_4_);
        }
    }
    
    @Override
    protected void preRenderCallback(final EntityLivingBase p_77041_1_, final float p_77041_2_) {
        this.preRenderCallback((AbstractClientPlayer)p_77041_1_, p_77041_2_);
    }
    
    @Override
    protected void rotateCorpse(final EntityLivingBase p_77043_1_, final float p_77043_2_, final float p_77043_3_, final float p_77043_4_) {
        this.func_180595_a((AbstractClientPlayer)p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
    }
    
    @Override
    protected void renderLivingAt(final EntityLivingBase p_77039_1_, final double p_77039_2_, final double p_77039_4_, final double p_77039_6_) {
        this.renderLivingAt((AbstractClientPlayer)p_77039_1_, p_77039_2_, p_77039_4_, p_77039_6_);
    }
    
    @Override
    public void doRender(final EntityLivingBase p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.func_180596_a((AbstractClientPlayer)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    @Override
    public ModelBase getMainModel() {
        return this.func_177136_g();
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.func_180594_a((AbstractClientPlayer)p_110775_1_);
    }
    
    @Override
    protected void func_177069_a(final Entity p_177069_1_, final double p_177069_2_, final double p_177069_4_, final double p_177069_6_, final String p_177069_8_, final float p_177069_9_, final double p_177069_10_) {
        this.renderOffsetLivingLabel((AbstractClientPlayer)p_177069_1_, p_177069_2_, p_177069_4_, p_177069_6_, p_177069_8_, p_177069_9_, p_177069_10_);
    }
    
    @Override
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.func_180596_a((AbstractClientPlayer)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}
