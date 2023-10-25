/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.monster.EntitySlime;

public class LayerSlimeGel
implements LayerRenderer<EntitySlime> {
    private final ModelBase slimeModel = new ModelSlime(0);
    private final RenderSlime slimeRenderer;

    @Override
    public void doRenderLayer(EntitySlime entitySlime, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        if (!entitySlime.isInvisible()) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            this.slimeModel.setModelAttributes(this.slimeRenderer.getMainModel());
            this.slimeModel.render(entitySlime, f, f2, f4, f5, f6, f7);
            GlStateManager.disableBlend();
            GlStateManager.disableNormalize();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }

    public LayerSlimeGel(RenderSlime renderSlime) {
        this.slimeRenderer = renderSlime;
    }
}

