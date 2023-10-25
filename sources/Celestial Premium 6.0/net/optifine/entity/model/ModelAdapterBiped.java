/*
 * Decompiled with CFR 0.150.
 */
package net.optifine.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.optifine.entity.model.ModelAdapter;

public abstract class ModelAdapterBiped
extends ModelAdapter {
    public ModelAdapterBiped(Class entityClass, String name, float shadowSize) {
        super(entityClass, name, shadowSize);
    }

    @Override
    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelBiped)) {
            return null;
        }
        ModelBiped modelbiped = (ModelBiped)model;
        if (modelPart.equals("head")) {
            return modelbiped.bipedHead;
        }
        if (modelPart.equals("headwear")) {
            return modelbiped.bipedHeadwear;
        }
        if (modelPart.equals("body")) {
            return modelbiped.bipedBody;
        }
        if (modelPart.equals("left_arm")) {
            return modelbiped.bipedLeftArm;
        }
        if (modelPart.equals("right_arm")) {
            return modelbiped.bipedRightArm;
        }
        if (modelPart.equals("left_leg")) {
            return modelbiped.bipedLeftLeg;
        }
        return modelPart.equals("right_leg") ? modelbiped.bipedRightLeg : null;
    }
}

