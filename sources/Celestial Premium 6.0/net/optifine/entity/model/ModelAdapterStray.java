/*
 * Decompiled with CFR 0.150.
 */
package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderStray;
import net.minecraft.entity.monster.EntityStray;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapterBiped;

public class ModelAdapterStray
extends ModelAdapterBiped {
    public ModelAdapterStray() {
        super(EntityStray.class, "stray", 0.7f);
    }

    @Override
    public ModelBase makeModel() {
        return new ModelSkeleton();
    }

    @Override
    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderStray renderstray = new RenderStray(rendermanager);
        renderstray.mainModel = modelBase;
        renderstray.shadowSize = shadowSize;
        return renderstray;
    }
}

