/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.ResourceLocation;

public class LayerCreeperCharge
implements LayerRenderer<EntityCreeper> {
    private final RenderCreeper creeperRenderer;
    private final ModelCreeper creeperModel = new ModelCreeper(2.0f);
    private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    public LayerCreeperCharge(RenderCreeper renderCreeper) {
        this.creeperRenderer = renderCreeper;
    }

    @Override
    public void doRenderLayer(EntityCreeper entityCreeper, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        if (entityCreeper.getPowered()) {
            boolean bl = entityCreeper.isInvisible();
            GlStateManager.depthMask(!bl);
            this.creeperRenderer.bindTexture(LIGHTNING_TEXTURE);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f8 = (float)entityCreeper.ticksExisted + f3;
            GlStateManager.translate(f8 * 0.01f, f8 * 0.01f, 0.0f);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            float f9 = 0.5f;
            GlStateManager.color(f9, f9, f9, 1.0f);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(1, 1);
            this.creeperModel.setModelAttributes(this.creeperRenderer.getMainModel());
            this.creeperModel.render(entityCreeper, f, f2, f4, f5, f6, f7);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(bl);
        }
    }
}

