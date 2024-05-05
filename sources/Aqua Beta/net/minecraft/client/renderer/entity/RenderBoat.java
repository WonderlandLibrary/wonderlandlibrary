package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
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

    public void doRender(EntityBoat entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)((float)x), (float)((float)y + 0.25f), (float)((float)z));
        GlStateManager.rotate((float)(180.0f - entityYaw), (float)0.0f, (float)1.0f, (float)0.0f);
        float f = (float)entity.getTimeSinceHit() - partialTicks;
        float f1 = entity.getDamageTaken() - partialTicks;
        if (f1 < 0.0f) {
            f1 = 0.0f;
        }
        if (f > 0.0f) {
            GlStateManager.rotate((float)(MathHelper.sin((float)f) * f * f1 / 10.0f * (float)entity.getForwardDirection()), (float)1.0f, (float)0.0f, (float)0.0f);
        }
        float f2 = 0.75f;
        GlStateManager.scale((float)f2, (float)f2, (float)f2);
        GlStateManager.scale((float)(1.0f / f2), (float)(1.0f / f2), (float)(1.0f / f2));
        this.bindEntityTexture((Entity)entity);
        GlStateManager.scale((float)-1.0f, (float)-1.0f, (float)1.0f);
        this.modelBoat.render((Entity)entity, 0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        GlStateManager.popMatrix();
        super.doRender((Entity)entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(EntityBoat entity) {
        return boatTextures;
    }
}
