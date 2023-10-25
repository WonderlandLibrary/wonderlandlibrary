/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderEnderCrystal
extends Render<EntityEnderCrystal> {
    private static final ResourceLocation enderCrystalTextures = new ResourceLocation("textures/entity/endercrystal/endercrystal.png");
    private ModelBase modelEnderCrystal = new ModelEnderCrystal(0.0f, true);

    public RenderEnderCrystal(RenderManager renderManager) {
        super(renderManager);
        this.shadowSize = 0.5f;
    }

    @Override
    public void doRender(EntityEnderCrystal entityEnderCrystal, double d, double d2, double d3, float f, float f2) {
        float f3 = (float)entityEnderCrystal.innerRotation + f2;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)d, (float)d2, (float)d3);
        this.bindTexture(enderCrystalTextures);
        float f4 = MathHelper.sin(f3 * 0.2f) / 2.0f + 0.5f;
        f4 = f4 * f4 + f4;
        this.modelEnderCrystal.render(entityEnderCrystal, 0.0f, f3 * 3.0f, f4 * 0.2f, 0.0f, 0.0f, 0.0625f);
        GlStateManager.popMatrix();
        super.doRender(entityEnderCrystal, d, d2, d3, f, f2);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityEnderCrystal entityEnderCrystal) {
        return enderCrystalTextures;
    }
}

