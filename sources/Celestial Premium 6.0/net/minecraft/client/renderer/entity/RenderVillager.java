/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;

public class RenderVillager
extends RenderLiving<EntityVillager> {
    private static final ResourceLocation VILLAGER_TEXTURES = new ResourceLocation("textures/entity/villager/villager.png");
    private static final ResourceLocation FARMER_VILLAGER_TEXTURES = new ResourceLocation("textures/entity/villager/farmer.png");
    private static final ResourceLocation LIBRARIAN_VILLAGER_TEXTURES = new ResourceLocation("textures/entity/villager/librarian.png");
    private static final ResourceLocation PRIEST_VILLAGER_TEXTURES = new ResourceLocation("textures/entity/villager/priest.png");
    private static final ResourceLocation SMITH_VILLAGER_TEXTURES = new ResourceLocation("textures/entity/villager/smith.png");
    private static final ResourceLocation BUTCHER_VILLAGER_TEXTURES = new ResourceLocation("textures/entity/villager/butcher.png");

    public RenderVillager(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelVillager(0.0f), 0.5f);
        this.addLayer(new LayerCustomHead(this.getMainModel().villagerHead));
    }

    @Override
    public ModelVillager getMainModel() {
        return (ModelVillager)super.getMainModel();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityVillager entity) {
        switch (entity.getProfession()) {
            case 0: {
                return FARMER_VILLAGER_TEXTURES;
            }
            case 1: {
                return LIBRARIAN_VILLAGER_TEXTURES;
            }
            case 2: {
                return PRIEST_VILLAGER_TEXTURES;
            }
            case 3: {
                return SMITH_VILLAGER_TEXTURES;
            }
            case 4: {
                return BUTCHER_VILLAGER_TEXTURES;
            }
        }
        return VILLAGER_TEXTURES;
    }

    @Override
    protected void preRenderCallback(EntityVillager entitylivingbaseIn, float partialTickTime) {
        float f = 0.9375f;
        if (entitylivingbaseIn.getGrowingAge() < 0) {
            f = (float)((double)f * 0.5);
            this.shadowSize = 0.25f;
        } else {
            this.shadowSize = 0.5f;
        }
        GlStateManager.scale(f, f, f);
    }
}

