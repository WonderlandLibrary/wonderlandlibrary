/*
 * Decompiled with CFR 0.150.
 */
package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderShulker;
import net.minecraft.entity.monster.EntityShulker;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;

public class ModelAdapterShulker
extends ModelAdapter {
    public ModelAdapterShulker() {
        super(EntityShulker.class, "shulker", 0.0f);
    }

    @Override
    public ModelBase makeModel() {
        return new ModelShulker();
    }

    @Override
    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelShulker)) {
            return null;
        }
        ModelShulker modelshulker = (ModelShulker)model;
        if (modelPart.equals("head")) {
            return modelshulker.head;
        }
        if (modelPart.equals("base")) {
            return modelshulker.base;
        }
        return modelPart.equals("lid") ? modelshulker.lid : null;
    }

    @Override
    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderShulker rendershulker = new RenderShulker(rendermanager);
        rendershulker.mainModel = modelBase;
        rendershulker.shadowSize = shadowSize;
        return rendershulker;
    }
}

