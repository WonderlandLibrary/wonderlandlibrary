/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerCreeperCharge;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderCreeper
extends RenderLiving<EntityCreeper> {
    private static final ResourceLocation creeperTextures = new ResourceLocation("textures/entity/creeper/creeper.png");

    public RenderCreeper(RenderManager renderManager) {
        super(renderManager, new ModelCreeper(), 0.5f);
        this.addLayer(new LayerCreeperCharge(this));
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityCreeper entityCreeper) {
        return creeperTextures;
    }

    @Override
    protected void preRenderCallback(EntityCreeper entityCreeper, float f) {
        float f2 = entityCreeper.getCreeperFlashIntensity(f);
        float f3 = 1.0f + MathHelper.sin(f2 * 100.0f) * f2 * 0.01f;
        f2 = MathHelper.clamp_float(f2, 0.0f, 1.0f);
        f2 *= f2;
        f2 *= f2;
        float f4 = (1.0f + f2 * 0.4f) * f3;
        float f5 = (1.0f + f2 * 0.1f) / f3;
        GlStateManager.scale(f4, f5, f4);
    }

    @Override
    protected int getColorMultiplier(EntityCreeper entityCreeper, float f, float f2) {
        float f3 = entityCreeper.getCreeperFlashIntensity(f2);
        if ((int)(f3 * 10.0f) % 2 == 0) {
            return 0;
        }
        int n = (int)(f3 * 0.2f * 255.0f);
        n = MathHelper.clamp_int(n, 0, 255);
        return n << 24 | 0xFFFFFF;
    }
}

