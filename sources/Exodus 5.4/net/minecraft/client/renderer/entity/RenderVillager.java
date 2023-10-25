/*
 * Decompiled with CFR 0.152.
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
    private static final ResourceLocation farmerVillagerTextures;
    private static final ResourceLocation priestVillagerTextures;
    private static final ResourceLocation smithVillagerTextures;
    private static final ResourceLocation butcherVillagerTextures;
    private static final ResourceLocation villagerTextures;
    private static final ResourceLocation librarianVillagerTextures;

    @Override
    protected void preRenderCallback(EntityVillager entityVillager, float f) {
        float f2 = 0.9375f;
        if (entityVillager.getGrowingAge() < 0) {
            f2 = (float)((double)f2 * 0.5);
            this.shadowSize = 0.25f;
        } else {
            this.shadowSize = 0.5f;
        }
        GlStateManager.scale(f2, f2, f2);
    }

    static {
        villagerTextures = new ResourceLocation("textures/entity/villager/villager.png");
        farmerVillagerTextures = new ResourceLocation("textures/entity/villager/farmer.png");
        librarianVillagerTextures = new ResourceLocation("textures/entity/villager/librarian.png");
        priestVillagerTextures = new ResourceLocation("textures/entity/villager/priest.png");
        smithVillagerTextures = new ResourceLocation("textures/entity/villager/smith.png");
        butcherVillagerTextures = new ResourceLocation("textures/entity/villager/butcher.png");
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityVillager entityVillager) {
        switch (entityVillager.getProfession()) {
            case 0: {
                return farmerVillagerTextures;
            }
            case 1: {
                return librarianVillagerTextures;
            }
            case 2: {
                return priestVillagerTextures;
            }
            case 3: {
                return smithVillagerTextures;
            }
            case 4: {
                return butcherVillagerTextures;
            }
        }
        return villagerTextures;
    }

    @Override
    public ModelVillager getMainModel() {
        return (ModelVillager)super.getMainModel();
    }

    public RenderVillager(RenderManager renderManager) {
        super(renderManager, new ModelVillager(0.0f), 0.5f);
        this.addLayer(new LayerCustomHead(this.getMainModel().villagerHead));
    }
}

