/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;

public class RenderVillager
extends RenderLiving {
    private static final ResourceLocation villagerTextures = new ResourceLocation("textures/entity/villager/villager.png");
    private static final ResourceLocation farmerVillagerTextures = new ResourceLocation("textures/entity/villager/farmer.png");
    private static final ResourceLocation librarianVillagerTextures = new ResourceLocation("textures/entity/villager/librarian.png");
    private static final ResourceLocation priestVillagerTextures = new ResourceLocation("textures/entity/villager/priest.png");
    private static final ResourceLocation smithVillagerTextures = new ResourceLocation("textures/entity/villager/smith.png");
    private static final ResourceLocation butcherVillagerTextures = new ResourceLocation("textures/entity/villager/butcher.png");
    private static final String __OBFID = "CL_00001032";

    public RenderVillager(RenderManager p_i46132_1_) {
        super(p_i46132_1_, new ModelVillager(0.0f), 0.5f);
        this.addLayer(new LayerCustomHead(this.func_177134_g().villagerHead));
    }

    public ModelVillager func_177134_g() {
        return (ModelVillager)super.getMainModel();
    }

    protected ResourceLocation getEntityTexture(EntityVillager p_110775_1_) {
        switch (p_110775_1_.getProfession()) {
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

    protected void preRenderCallback(EntityVillager p_77041_1_, float p_77041_2_) {
        float var3 = 0.9375f;
        if (p_77041_1_.getGrowingAge() < 0) {
            var3 = (float)((double)var3 * 0.5);
            this.shadowSize = 0.25f;
        } else {
            this.shadowSize = 0.5f;
        }
        GlStateManager.scale(var3, var3, var3);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_) {
        this.preRenderCallback((EntityVillager)p_77041_1_, p_77041_2_);
    }

    @Override
    public ModelBase getMainModel() {
        return this.func_177134_g();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.getEntityTexture((EntityVillager)p_110775_1_);
    }
}

