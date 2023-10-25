/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelShulkerBullet;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderShulkerBullet
extends Render<EntityShulkerBullet> {
    private static final ResourceLocation SHULKER_SPARK_TEXTURE = new ResourceLocation("textures/entity/shulker/spark.png");
    private final ModelShulkerBullet model = new ModelShulkerBullet();

    public RenderShulkerBullet(RenderManager manager) {
        super(manager);
    }

    private float rotLerp(float p_188347_1_, float p_188347_2_, float p_188347_3_) {
        float f;
        for (f = p_188347_2_ - p_188347_1_; f < -180.0f; f += 360.0f) {
        }
        while (f >= 180.0f) {
            f -= 360.0f;
        }
        return p_188347_1_ + p_188347_3_ * f;
    }

    @Override
    public void doRender(EntityShulkerBullet entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        float f = this.rotLerp(entity.prevRotationYaw, entity.rotationYaw, partialTicks);
        float f1 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        float f2 = (float)entity.ticksExisted + partialTicks;
        GlStateManager.translate((float)x, (float)y + 0.15f, (float)z);
        GlStateManager.rotate(MathHelper.sin(f2 * 0.1f) * 180.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(MathHelper.cos(f2 * 0.1f) * 180.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(MathHelper.sin(f2 * 0.15f) * 360.0f, 0.0f, 0.0f, 1.0f);
        float f3 = 0.03125f;
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        this.bindEntityTexture(entity);
        this.model.render(entity, 0.0f, 0.0f, 0.0f, f, f1, 0.03125f);
        GlStateManager.enableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.5f);
        GlStateManager.scale(1.5f, 1.5f, 1.5f);
        this.model.render(entity, 0.0f, 0.0f, 0.0f, f, f1, 0.03125f);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityShulkerBullet entity) {
        return SHULKER_SPARK_TEXTURE;
    }
}

