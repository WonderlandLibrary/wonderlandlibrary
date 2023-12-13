/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.util.ResourceLocation;

public class RenderWitherSkull
extends Render<EntityWitherSkull> {
    private static final ResourceLocation invulnerableWitherTextures = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
    private static final ResourceLocation witherTextures = new ResourceLocation("textures/entity/wither/wither.png");
    private final ModelSkeletonHead skeletonHeadModel = new ModelSkeletonHead();

    public RenderWitherSkull(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    private float func_82400_a(float p_82400_1_, float p_82400_2_, float p_82400_3_) {
        float f2;
        for (f2 = p_82400_2_ - p_82400_1_; f2 < -180.0f; f2 += 360.0f) {
        }
        while (f2 >= 180.0f) {
            f2 -= 360.0f;
        }
        return p_82400_1_ + p_82400_3_ * f2;
    }

    @Override
    public void doRender(EntityWitherSkull entity, double x2, double y2, double z2, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        float f2 = this.func_82400_a(entity.prevRotationYaw, entity.rotationYaw, partialTicks);
        float f1 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        GlStateManager.translate((float)x2, (float)y2, (float)z2);
        float f22 = 0.0625f;
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        GlStateManager.enableAlpha();
        this.bindEntityTexture(entity);
        this.skeletonHeadModel.render(entity, 0.0f, 0.0f, 0.0f, f2, f1, f22);
        GlStateManager.popMatrix();
        super.doRender(entity, x2, y2, z2, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityWitherSkull entity) {
        return entity.isInvulnerable() ? invulnerableWitherTextures : witherTextures;
    }
}

