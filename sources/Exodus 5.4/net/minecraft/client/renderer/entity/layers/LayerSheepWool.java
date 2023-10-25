/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;

public class LayerSheepWool
implements LayerRenderer<EntitySheep> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
    private final ModelSheep1 sheepModel = new ModelSheep1();
    private final RenderSheep sheepRenderer;

    @Override
    public void doRenderLayer(EntitySheep entitySheep, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        if (!entitySheep.getSheared() && !entitySheep.isInvisible()) {
            this.sheepRenderer.bindTexture(TEXTURE);
            if (entitySheep.hasCustomName() && "jeb_".equals(entitySheep.getCustomNameTag())) {
                int n = 25;
                int n2 = entitySheep.ticksExisted / 25 + entitySheep.getEntityId();
                int n3 = EnumDyeColor.values().length;
                int n4 = n2 % n3;
                int n5 = (n2 + 1) % n3;
                float f8 = ((float)(entitySheep.ticksExisted % 25) + f3) / 25.0f;
                float[] fArray = EntitySheep.func_175513_a(EnumDyeColor.byMetadata(n4));
                float[] fArray2 = EntitySheep.func_175513_a(EnumDyeColor.byMetadata(n5));
                GlStateManager.color(fArray[0] * (1.0f - f8) + fArray2[0] * f8, fArray[1] * (1.0f - f8) + fArray2[1] * f8, fArray[2] * (1.0f - f8) + fArray2[2] * f8);
            } else {
                float[] fArray = EntitySheep.func_175513_a(entitySheep.getFleeceColor());
                GlStateManager.color(fArray[0], fArray[1], fArray[2]);
            }
            this.sheepModel.setModelAttributes(this.sheepRenderer.getMainModel());
            this.sheepModel.setLivingAnimations(entitySheep, f, f2, f3);
            this.sheepModel.render(entitySheep, f, f2, f4, f5, f6, f7);
        }
    }

    public LayerSheepWool(RenderSheep renderSheep) {
        this.sheepRenderer = renderSheep;
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}

