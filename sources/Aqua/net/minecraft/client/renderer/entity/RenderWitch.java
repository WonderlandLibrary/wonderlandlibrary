package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelWitch;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItemWitch;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.util.ResourceLocation;

public class RenderWitch
extends RenderLiving<EntityWitch> {
    private static final ResourceLocation witchTextures = new ResourceLocation("textures/entity/witch.png");

    public RenderWitch(RenderManager renderManagerIn) {
        super(renderManagerIn, (ModelBase)new ModelWitch(0.0f), 0.5f);
        this.addLayer((LayerRenderer)new LayerHeldItemWitch(this));
    }

    public void doRender(EntityWitch entity, double x, double y, double z, float entityYaw, float partialTicks) {
        ((ModelWitch)this.mainModel).field_82900_g = entity.getHeldItem() != null;
        super.doRender((EntityLiving)entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(EntityWitch entity) {
        return witchTextures;
    }

    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate((float)0.0f, (float)0.1875f, (float)0.0f);
    }

    protected void preRenderCallback(EntityWitch entitylivingbaseIn, float partialTickTime) {
        float f = 0.9375f;
        GlStateManager.scale((float)f, (float)f, (float)f);
    }
}
