package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.util.ResourceLocation;

public class RenderSkeleton
extends RenderBiped<EntitySkeleton> {
    private static final ResourceLocation skeletonTextures = new ResourceLocation("textures/entity/skeleton/skeleton.png");
    private static final ResourceLocation witherSkeletonTextures = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");

    public RenderSkeleton(RenderManager renderManagerIn) {
        super(renderManagerIn, (ModelBiped)new ModelSkeleton(), 0.5f);
        this.addLayer((LayerRenderer)new LayerHeldItem((RendererLivingEntity)this));
        this.addLayer((LayerRenderer)new /* Unavailable Anonymous Inner Class!! */);
    }

    protected void preRenderCallback(EntitySkeleton entitylivingbaseIn, float partialTickTime) {
        if (entitylivingbaseIn.getSkeletonType() == 1) {
            GlStateManager.scale((float)1.2f, (float)1.2f, (float)1.2f);
        }
    }

    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate((float)0.09375f, (float)0.1875f, (float)0.0f);
    }

    protected ResourceLocation getEntityTexture(EntitySkeleton entity) {
        return entity.getSkeletonType() == 1 ? witherSkeletonTextures : skeletonTextures;
    }
}
