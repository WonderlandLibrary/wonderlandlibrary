/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelEvokerFangs;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.util.ResourceLocation;

public class RenderEvokerFangs
extends Render<EntityEvokerFangs> {
    private static final ResourceLocation field_191329_a = new ResourceLocation("textures/entity/illager/fangs.png");
    private final ModelEvokerFangs field_191330_f = new ModelEvokerFangs();

    public RenderEvokerFangs(RenderManager p_i47208_1_) {
        super(p_i47208_1_);
    }

    @Override
    public void doRender(EntityEvokerFangs entity, double x, double y, double z, float entityYaw, float partialTicks) {
        float f = entity.func_190550_a(partialTicks);
        if (f != 0.0f) {
            float f1 = 2.0f;
            if (f > 0.9f) {
                f1 = (float)((double)f1 * ((1.0 - (double)f) / (double)0.1f));
            }
            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            GlStateManager.enableAlpha();
            this.bindEntityTexture(entity);
            GlStateManager.translate((float)x, (float)y, (float)z);
            GlStateManager.rotate(90.0f - entity.rotationYaw, 0.0f, 1.0f, 0.0f);
            GlStateManager.scale(-f1, -f1, f1);
            float f2 = 0.03125f;
            GlStateManager.translate(0.0f, -0.626f, 0.0f);
            this.field_191330_f.render(entity, f, 0.0f, 0.0f, entity.rotationYaw, entity.rotationPitch, 0.03125f);
            GlStateManager.popMatrix();
            GlStateManager.enableCull();
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityEvokerFangs entity) {
        return field_191329_a;
    }
}

