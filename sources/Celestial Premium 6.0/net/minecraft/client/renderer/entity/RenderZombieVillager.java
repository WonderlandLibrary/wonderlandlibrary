/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerVillagerArmor;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.util.ResourceLocation;

public class RenderZombieVillager
extends RenderBiped<EntityZombieVillager> {
    private static final ResourceLocation ZOMBIE_VILLAGER_TEXTURES = new ResourceLocation("textures/entity/zombie_villager/zombie_villager.png");
    private static final ResourceLocation ZOMBIE_VILLAGER_FARMER_LOCATION = new ResourceLocation("textures/entity/zombie_villager/zombie_farmer.png");
    private static final ResourceLocation ZOMBIE_VILLAGER_LIBRARIAN_LOC = new ResourceLocation("textures/entity/zombie_villager/zombie_librarian.png");
    private static final ResourceLocation ZOMBIE_VILLAGER_PRIEST_LOCATION = new ResourceLocation("textures/entity/zombie_villager/zombie_priest.png");
    private static final ResourceLocation ZOMBIE_VILLAGER_SMITH_LOCATION = new ResourceLocation("textures/entity/zombie_villager/zombie_smith.png");
    private static final ResourceLocation ZOMBIE_VILLAGER_BUTCHER_LOCATION = new ResourceLocation("textures/entity/zombie_villager/zombie_butcher.png");

    public RenderZombieVillager(RenderManager p_i47186_1_) {
        super(p_i47186_1_, new ModelZombieVillager(), 0.5f);
        this.addLayer(new LayerVillagerArmor(this));
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityZombieVillager entity) {
        switch (entity.func_190736_dl()) {
            case 0: {
                return ZOMBIE_VILLAGER_FARMER_LOCATION;
            }
            case 1: {
                return ZOMBIE_VILLAGER_LIBRARIAN_LOC;
            }
            case 2: {
                return ZOMBIE_VILLAGER_PRIEST_LOCATION;
            }
            case 3: {
                return ZOMBIE_VILLAGER_SMITH_LOCATION;
            }
            case 4: {
                return ZOMBIE_VILLAGER_BUTCHER_LOCATION;
            }
        }
        return ZOMBIE_VILLAGER_TEXTURES;
    }

    @Override
    protected void rotateCorpse(EntityZombieVillager entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks) {
        if (entityLiving.isConverting()) {
            p_77043_3_ += (float)(Math.cos((double)entityLiving.ticksExisted * 3.25) * Math.PI * 0.25);
        }
        super.rotateCorpse(entityLiving, p_77043_2_, p_77043_3_, partialTicks);
    }
}

