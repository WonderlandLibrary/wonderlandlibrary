/*
 * Decompiled with CFR 0.150.
 */
package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderZombieVillager;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapterBiped;

public class ModelAdapterZombieVillager
extends ModelAdapterBiped {
    public ModelAdapterZombieVillager() {
        super(EntityZombieVillager.class, "zombie_villager", 0.5f);
    }

    @Override
    public ModelBase makeModel() {
        return new ModelZombieVillager();
    }

    @Override
    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderZombieVillager renderzombievillager = new RenderZombieVillager(rendermanager);
        renderzombievillager.mainModel = modelBase;
        renderzombievillager.shadowSize = shadowSize;
        return renderzombievillager;
    }
}

