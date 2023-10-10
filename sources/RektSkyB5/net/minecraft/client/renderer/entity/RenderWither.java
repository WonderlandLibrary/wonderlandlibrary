/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelWither;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerWitherAura;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.ResourceLocation;

public class RenderWither
extends RenderLiving<EntityWither> {
    private static final ResourceLocation invulnerableWitherTextures = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
    private static final ResourceLocation witherTextures = new ResourceLocation("textures/entity/wither/wither.png");

    public RenderWither(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelWither(0.0f), 1.0f);
        this.addLayer(new LayerWitherAura(this));
    }

    @Override
    public void doRender(EntityWither entity, double x2, double y2, double z2, float entityYaw, float partialTicks) {
        BossStatus.setBossStatus(entity, true);
        super.doRender(entity, x2, y2, z2, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityWither entity) {
        int i2 = entity.getInvulTime();
        return i2 > 0 && (i2 > 80 || i2 / 5 % 2 != 1) ? invulnerableWitherTextures : witherTextures;
    }

    @Override
    protected void preRenderCallback(EntityWither entitylivingbaseIn, float partialTickTime) {
        float f2 = 2.0f;
        int i2 = entitylivingbaseIn.getInvulTime();
        if (i2 > 0) {
            f2 -= ((float)i2 - partialTickTime) / 220.0f * 0.5f;
        }
        GlStateManager.scale(f2, f2, f2);
    }
}

