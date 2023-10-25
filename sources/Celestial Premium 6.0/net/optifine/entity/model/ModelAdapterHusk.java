/*
 * Decompiled with CFR 0.150.
 */
package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderHusk;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityHusk;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapterBiped;

public class ModelAdapterHusk
extends ModelAdapterBiped {
    public ModelAdapterHusk() {
        super(EntityHusk.class, "husk", 0.5f);
    }

    @Override
    public ModelBase makeModel() {
        return new ModelZombie();
    }

    @Override
    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderHusk renderhusk = new RenderHusk(rendermanager);
        renderhusk.mainModel = modelBase;
        renderhusk.shadowSize = shadowSize;
        return renderhusk;
    }
}

