package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelWither;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerWitherAura;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.util.ResourceLocation;

public class RenderWither
extends RenderLiving<EntityWither> {
    private static final ResourceLocation invulnerableWitherTextures = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
    private static final ResourceLocation witherTextures = new ResourceLocation("textures/entity/wither/wither.png");

    public RenderWither(RenderManager renderManagerIn) {
        super(renderManagerIn, (ModelBase)new ModelWither(0.0f), 1.0f);
        this.addLayer((LayerRenderer)new LayerWitherAura(this));
    }

    public void doRender(EntityWither entity, double x, double y, double z, float entityYaw, float partialTicks) {
        BossStatus.setBossStatus((IBossDisplayData)entity, (boolean)true);
        super.doRender((EntityLiving)entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(EntityWither entity) {
        int i = entity.getInvulTime();
        return i > 0 && (i > 80 || i / 5 % 2 != 1) ? invulnerableWitherTextures : witherTextures;
    }

    protected void preRenderCallback(EntityWither entitylivingbaseIn, float partialTickTime) {
        float f = 2.0f;
        int i = entitylivingbaseIn.getInvulTime();
        if (i > 0) {
            f -= ((float)i - partialTickTime) / 220.0f * 0.5f;
        }
        GlStateManager.scale((float)f, (float)f, (float)f);
    }
}
