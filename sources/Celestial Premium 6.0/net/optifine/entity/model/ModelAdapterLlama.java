/*
 * Decompiled with CFR 0.150.
 */
package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelLlama;
import net.minecraft.client.renderer.entity.RenderLlama;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityLlama;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapterQuadruped;

public class ModelAdapterLlama
extends ModelAdapterQuadruped {
    public ModelAdapterLlama() {
        super(EntityLlama.class, "llama", 0.7f);
    }

    @Override
    public ModelBase makeModel() {
        return new ModelLlama(0.0f);
    }

    @Override
    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderLlama renderllama = new RenderLlama(rendermanager);
        renderllama.mainModel = modelBase;
        renderllama.shadowSize = shadowSize;
        return renderllama;
    }
}

