package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.util.ResourceLocation;

public class RenderCaveSpider
extends RenderSpider<EntityCaveSpider> {
    private static final ResourceLocation caveSpiderTextures = new ResourceLocation("textures/entity/spider/cave_spider.png");

    public RenderCaveSpider(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize *= 0.7f;
    }

    protected void preRenderCallback(EntityCaveSpider entitylivingbaseIn, float partialTickTime) {
        GlStateManager.scale((float)0.7f, (float)0.7f, (float)0.7f);
    }

    protected ResourceLocation getEntityTexture(EntityCaveSpider entity) {
        return caveSpiderTextures;
    }
}
