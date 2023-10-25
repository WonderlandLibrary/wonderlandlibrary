/*
 * Decompiled with CFR 0.150.
 */
package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWitherSkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapterBiped;

public class ModelAdapterWitherSkeleton
extends ModelAdapterBiped {
    public ModelAdapterWitherSkeleton() {
        super(EntityWitherSkeleton.class, "wither_skeleton", 0.7f);
    }

    @Override
    public ModelBase makeModel() {
        return new ModelSkeleton();
    }

    @Override
    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderWitherSkeleton renderwitherskeleton = new RenderWitherSkeleton(rendermanager);
        renderwitherskeleton.mainModel = modelBase;
        renderwitherskeleton.shadowSize = shadowSize;
        return renderwitherskeleton;
    }
}

