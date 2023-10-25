/*
 * Decompiled with CFR 0.150.
 */
package net.optifine.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.optifine.entity.model.ModelAdapter;

public abstract class ModelAdapterQuadruped
extends ModelAdapter {
    public ModelAdapterQuadruped(Class entityClass, String name, float shadowSize) {
        super(entityClass, name, shadowSize);
    }

    @Override
    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelQuadruped)) {
            return null;
        }
        ModelQuadruped modelquadruped = (ModelQuadruped)model;
        if (modelPart.equals("head")) {
            return modelquadruped.head;
        }
        if (modelPart.equals("body")) {
            return modelquadruped.body;
        }
        if (modelPart.equals("leg1")) {
            return modelquadruped.leg1;
        }
        if (modelPart.equals("leg2")) {
            return modelquadruped.leg2;
        }
        if (modelPart.equals("leg3")) {
            return modelquadruped.leg3;
        }
        return modelPart.equals("leg4") ? modelquadruped.leg4 : null;
    }
}

