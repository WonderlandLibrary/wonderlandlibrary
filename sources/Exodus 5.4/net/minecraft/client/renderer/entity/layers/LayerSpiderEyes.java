/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.ResourceLocation;

public class LayerSpiderEyes
implements LayerRenderer<EntitySpider> {
    private final RenderSpider spiderRenderer;
    private static final ResourceLocation SPIDER_EYES = new ResourceLocation("textures/entity/spider_eyes.png");

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    public LayerSpiderEyes(RenderSpider renderSpider) {
        this.spiderRenderer = renderSpider;
    }

    @Override
    public void doRenderLayer(EntitySpider entitySpider, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        this.spiderRenderer.bindTexture(SPIDER_EYES);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc(1, 1);
        if (entitySpider.isInvisible()) {
            GlStateManager.depthMask(false);
        } else {
            GlStateManager.depthMask(true);
        }
        int n = 61680;
        int n2 = n % 65536;
        int n3 = n / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)n2 / 1.0f, (float)n3 / 1.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.spiderRenderer.getMainModel().render(entitySpider, f, f2, f4, f5, f6, f7);
        n = entitySpider.getBrightnessForRender(f3);
        n2 = n % 65536;
        n3 = n / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)n2 / 1.0f, (float)n3 / 1.0f);
        this.spiderRenderer.func_177105_a(entitySpider, f3);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }
}

