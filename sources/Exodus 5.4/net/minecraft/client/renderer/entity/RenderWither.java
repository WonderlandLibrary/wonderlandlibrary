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
    private static final ResourceLocation witherTextures;
    private static final ResourceLocation invulnerableWitherTextures;

    @Override
    public void doRender(EntityWither entityWither, double d, double d2, double d3, float f, float f2) {
        BossStatus.setBossStatus(entityWither, true);
        super.doRender(entityWither, d, d2, d3, f, f2);
    }

    public RenderWither(RenderManager renderManager) {
        super(renderManager, new ModelWither(0.0f), 1.0f);
        this.addLayer(new LayerWitherAura(this));
    }

    @Override
    protected void preRenderCallback(EntityWither entityWither, float f) {
        float f2 = 2.0f;
        int n = entityWither.getInvulTime();
        if (n > 0) {
            f2 -= ((float)n - f) / 220.0f * 0.5f;
        }
        GlStateManager.scale(f2, f2, f2);
    }

    static {
        invulnerableWitherTextures = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
        witherTextures = new ResourceLocation("textures/entity/wither/wither.png");
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityWither entityWither) {
        int n = entityWither.getInvulTime();
        return n > 0 && (n > 80 || n / 5 % 2 != 1) ? invulnerableWitherTextures : witherTextures;
    }
}

