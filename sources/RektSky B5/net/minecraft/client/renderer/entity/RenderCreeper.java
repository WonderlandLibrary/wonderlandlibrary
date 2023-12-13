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

    public RenderCreeper(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelCreeper(), 0.5f);
        this.addLayer(new LayerCreeperCharge(this));
    }

    @Override
    protected void preRenderCallback(EntityCreeper entitylivingbaseIn, float partialTickTime) {
        float f2 = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
        float f1 = 1.0f + MathHelper.sin(f2 * 100.0f) * f2 * 0.01f;
        f2 = MathHelper.clamp_float(f2, 0.0f, 1.0f);
        f2 *= f2;
        f2 *= f2;
        float f22 = (1.0f + f2 * 0.4f) * f1;
        float f3 = (1.0f + f2 * 0.1f) / f1;
        GlStateManager.scale(f22, f3, f22);
    }

    @Override
    protected int getColorMultiplier(EntityCreeper entitylivingbaseIn, float lightBrightness, float partialTickTime) {
        float f2 = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
        if ((int)(f2 * 10.0f) % 2 == 0) {
            return 0;
        }
        int i2 = (int)(f2 * 0.2f * 255.0f);
        i2 = MathHelper.clamp_int(i2, 0, 255);
        return i2 << 24 | 0xFFFFFF;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityCreeper entity) {
        return creeperTextures;
    }
}

