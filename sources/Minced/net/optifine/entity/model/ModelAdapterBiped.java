// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelBase;

public abstract class ModelAdapterBiped extends ModelAdapter
{
    public ModelAdapterBiped(final Class entityClass, final String name, final float shadowSize) {
        super(entityClass, name, shadowSize);
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelBiped)) {
            return null;
        }
        final ModelBiped modelbiped = (ModelBiped)model;
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
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "head", "headwear", "body", "left_arm", "right_arm", "left_leg", "right_leg" };
    }
}
