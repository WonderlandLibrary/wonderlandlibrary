/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderBoat
extends Render<EntityBoat> {
    private static final ResourceLocation boatTextures = new ResourceLocation("textures/entity/boat.png");
    protected ModelBase modelBoat = new ModelBoat();

    public RenderBoat(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.5f;
    }

    @Override
    public void doRender(EntityBoat entity, double x2, double y2, double z2, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x2, (float)y2 + 0.25f, (float)z2);
        GlStateManager.rotate(180.0f - entityYaw, 0.0f, 1.0f, 0.0f);
        float f2 = (float)entity.getTimeSinceHit() - partialTicks;
        float f1 = entity.getDamageTaken() - partialTicks;
        if (f1 < 0.0f) {
            f1 = 0.0f;
        }
        if (f2 > 0.0f) {
            GlStateManager.rotate(MathHelper.sin(f2) * f2 * f1 / 10.0f * (float)entity.getForwardDirection(), 1.0f, 0.0f, 0.0f);
        }
        float f22 = 0.75f;
        GlStateManager.scale(f22, f22, f22);
        GlStateManager.scale(1.0f / f22, 1.0f / f22, 1.0f / f22);
        this.bindEntityTexture(entity);
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        this.modelBoat.render(entity, 0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        GlStateManager.popMatrix();
        super.doRender(entity, x2, y2, z2, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityBoat entity) {
        return boatTextures;
    }
}

