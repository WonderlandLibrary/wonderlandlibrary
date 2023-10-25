/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelWither;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderWither;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class LayerWitherAura
implements LayerRenderer<EntityWither> {
    private final ModelWither witherModel = new ModelWither(0.5f);
    private final RenderWither witherRenderer;
    private static final ResourceLocation WITHER_ARMOR = new ResourceLocation("textures/entity/wither/wither_armor.png");

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    public LayerWitherAura(RenderWither renderWither) {
        this.witherRenderer = renderWither;
    }

    @Override
    public void doRenderLayer(EntityWither entityWither, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        if (entityWither.isArmored()) {
            GlStateManager.depthMask(!entityWither.isInvisible());
            this.witherRenderer.bindTexture(WITHER_ARMOR);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f8 = (float)entityWither.ticksExisted + f3;
            float f9 = MathHelper.cos(f8 * 0.02f) * 3.0f;
            float f10 = f8 * 0.01f;
            GlStateManager.translate(f9, f10, 0.0f);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            float f11 = 0.5f;
            GlStateManager.color(f11, f11, f11, 1.0f);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(1, 1);
            this.witherModel.setLivingAnimations(entityWither, f, f2, f3);
            this.witherModel.setModelAttributes(this.witherRenderer.getMainModel());
            this.witherModel.render(entityWither, f, f2, f4, f5, f6, f7);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
        }
    }
}

