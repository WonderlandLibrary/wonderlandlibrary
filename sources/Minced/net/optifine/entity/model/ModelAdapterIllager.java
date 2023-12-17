// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.model.ModelIllager;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelBase;

public abstract class ModelAdapterIllager extends ModelAdapter
{
    public ModelAdapterIllager(final Class entityClass, final String name, final float shadowSize) {
        super(entityClass, name, shadowSize);
    }
    
    public ModelAdapterIllager(final Class entityClass, final String name, final float shadowSize, final String[] aliases) {
        super(entityClass, name, shadowSize, aliases);
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelIllager)) {
            return null;
        }
        final ModelIllager modelillager = (ModelIllager)model;
        if (modelPart.equals("head")) {
            return modelillager.head;
        }
        if (modelPart.equals("body")) {
            return modelillager.body;
        }
        if (modelPart.equals("arms")) {
            return modelillager.arms;
        }
        if (modelPart.equals("left_leg")) {
            return modelillager.leg1;
        }
        if (modelPart.equals("right_leg")) {
            return modelillager.leg0;
        }
        if (modelPart.equals("nose")) {
            return modelillager.nose;
        }
        if (modelPart.equals("left_arm")) {
            return modelillager.leftArm;
        }
        return modelPart.equals("right_arm") ? modelillager.rightArm : null;
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "head", "body", "arms", "right_leg", "left_leg", "nose", "right_arm", "left_arm" };
    }
}
