/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;

public class LayerWolfCollar
implements LayerRenderer<EntityWolf> {
    private final RenderWolf wolfRenderer;
    private static final ResourceLocation WOLF_COLLAR = new ResourceLocation("textures/entity/wolf/wolf_collar.png");

    @Override
    public void doRenderLayer(EntityWolf entityWolf, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        if (entityWolf.isTamed() && !entityWolf.isInvisible()) {
            this.wolfRenderer.bindTexture(WOLF_COLLAR);
            EnumDyeColor enumDyeColor = EnumDyeColor.byMetadata(entityWolf.getCollarColor().getMetadata());
            float[] fArray = EntitySheep.func_175513_a(enumDyeColor);
            GlStateManager.color(fArray[0], fArray[1], fArray[2]);
            this.wolfRenderer.getMainModel().render(entityWolf, f, f2, f4, f5, f6, f7);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }

    public LayerWolfCollar(RenderWolf renderWolf) {
        this.wolfRenderer = renderWolf;
    }
}

