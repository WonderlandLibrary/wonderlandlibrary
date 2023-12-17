// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelWither;
import net.minecraft.client.renderer.entity.RenderWither;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.boss.EntityWither;

public class LayerWitherAura implements LayerRenderer<EntityWither>
{
    private static final ResourceLocation WITHER_ARMOR;
    private final RenderWither witherRenderer;
    private final ModelWither witherModel;
    
    public LayerWitherAura(final RenderWither witherRendererIn) {
        this.witherModel = new ModelWither(0.5f);
        this.witherRenderer = witherRendererIn;
    }
    
    @Override
    public void doRenderLayer(final EntityWither entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (entitylivingbaseIn.isArmored()) {
            GlStateManager.depthMask(!entitylivingbaseIn.isInvisible());
            this.witherRenderer.bindTexture(LayerWitherAura.WITHER_ARMOR);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            final float f = entitylivingbaseIn.ticksExisted + partialTicks;
            final float f2 = MathHelper.cos(f * 0.02f) * 3.0f;
            final float f3 = f * 0.01f;
            GlStateManager.translate(f2, f3, 0.0f);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            final float f4 = 0.5f;
            GlStateManager.color(0.5f, 0.5f, 0.5f, 1.0f);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            this.witherModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
            this.witherModel.setModelAttributes(this.witherRenderer.getMainModel());
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            this.witherModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    static {
        WITHER_ARMOR = new ResourceLocation("textures/entity/wither/wither_armor.png");
    }
}
