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
    private static final ResourceLocation witherTextures;
    private final ModelSkeletonHead skeletonHeadModel = new ModelSkeletonHead();
    private static final ResourceLocation invulnerableWitherTextures;

    private float func_82400_a(float f, float f2, float f3) {
        float f4 = f2 - f;
        while (f4 < -180.0f) {
            f4 += 360.0f;
        }
        while (f4 >= 180.0f) {
            f4 -= 360.0f;
        }
        return f + f3 * f4;
    }

    public RenderWitherSkull(RenderManager renderManager) {
        super(renderManager);
    }

    static {
        invulnerableWitherTextures = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
        witherTextures = new ResourceLocation("textures/entity/wither/wither.png");
    }

    @Override
    public void doRender(EntityWitherSkull entityWitherSkull, double d, double d2, double d3, float f, float f2) {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        float f3 = this.func_82400_a(entityWitherSkull.prevRotationYaw, entityWitherSkull.rotationYaw, f2);
        float f4 = entityWitherSkull.prevRotationPitch + (entityWitherSkull.rotationPitch - entityWitherSkull.prevRotationPitch) * f2;
        GlStateManager.translate((float)d, (float)d2, (float)d3);
        float f5 = 0.0625f;
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        GlStateManager.enableAlpha();
        this.bindEntityTexture(entityWitherSkull);
        this.skeletonHeadModel.render(entityWitherSkull, 0.0f, 0.0f, 0.0f, f3, f4, f5);
        GlStateManager.popMatrix();
        super.doRender(entityWitherSkull, d, d2, d3, f, f2);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityWitherSkull entityWitherSkull) {
        return entityWitherSkull.isInvulnerable() ? invulnerableWitherTextures : witherTextures;
    }
}

