/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.ResourceLocation;

public class LayerEndermanEyes
implements LayerRenderer<EntityEnderman> {
    private static final ResourceLocation field_177203_a = new ResourceLocation("textures/entity/enderman/enderman_eyes.png");
    private final RenderEnderman endermanRenderer;

    @Override
    public void doRenderLayer(EntityEnderman entityEnderman, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        this.endermanRenderer.bindTexture(field_177203_a);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc(1, 1);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(!entityEnderman.isInvisible());
        int n = 61680;
        int n2 = n % 65536;
        int n3 = n / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)n2 / 1.0f, (float)n3 / 1.0f);
        GlStateManager.enableLighting();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.endermanRenderer.getMainModel().render(entityEnderman, f, f2, f4, f5, f6, f7);
        this.endermanRenderer.func_177105_a(entityEnderman, f3);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    public LayerEndermanEyes(RenderEnderman renderEnderman) {
        this.endermanRenderer = renderEnderman;
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}

