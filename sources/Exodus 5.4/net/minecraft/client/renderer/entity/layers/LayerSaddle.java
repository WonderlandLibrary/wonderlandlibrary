/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.entity.RenderPig;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.ResourceLocation;

public class LayerSaddle
implements LayerRenderer<EntityPig> {
    private final ModelPig pigModel = new ModelPig(0.5f);
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/pig/pig_saddle.png");
    private final RenderPig pigRenderer;

    @Override
    public void doRenderLayer(EntityPig entityPig, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        if (entityPig.getSaddled()) {
            this.pigRenderer.bindTexture(TEXTURE);
            this.pigModel.setModelAttributes(this.pigRenderer.getMainModel());
            this.pigModel.render(entityPig, f, f2, f4, f5, f6, f7);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    public LayerSaddle(RenderPig renderPig) {
        this.pigRenderer = renderPig;
    }
}

