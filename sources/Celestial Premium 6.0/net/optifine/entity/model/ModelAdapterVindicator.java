/*
 * Decompiled with CFR 0.150.
 */
package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelIllager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderVindicator;
import net.minecraft.entity.monster.EntityVindicator;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapterIllager;

public class ModelAdapterVindicator
extends ModelAdapterIllager {
    public ModelAdapterVindicator() {
        super(EntityVindicator.class, "vindication_illager", 0.5f);
    }

    @Override
    public ModelBase makeModel() {
        return new ModelIllager(0.0f, 0.0f, 64, 64);
    }

    @Override
    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderVindicator rendervindicator = new RenderVindicator(rendermanager);
        rendervindicator.mainModel = modelBase;
        rendervindicator.shadowSize = shadowSize;
        return rendervindicator;
    }
}

