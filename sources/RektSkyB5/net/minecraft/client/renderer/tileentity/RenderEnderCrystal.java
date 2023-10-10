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

    public RenderEnderCrystal(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.5f;
    }

    @Override
    public void doRender(EntityEnderCrystal entity, double x2, double y2, double z2, float entityYaw, float partialTicks) {
        float f2 = (float)entity.innerRotation + partialTicks;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x2, (float)y2, (float)z2);
        this.bindTexture(enderCrystalTextures);
        float f1 = MathHelper.sin(f2 * 0.2f) / 2.0f + 0.5f;
        f1 = f1 * f1 + f1;
        this.modelEnderCrystal.render(entity, 0.0f, f2 * 3.0f, f1 * 0.2f, 0.0f, 0.0f, 0.0625f);
        GlStateManager.popMatrix();
        super.doRender(entity, x2, y2, z2, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityEnderCrystal entity) {
        return enderCrystalTextures;
    }
}

