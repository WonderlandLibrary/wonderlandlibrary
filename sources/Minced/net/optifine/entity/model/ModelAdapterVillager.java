// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderVillager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.passive.EntityVillager;

public class ModelAdapterVillager extends ModelAdapter
{
    public ModelAdapterVillager() {
        super(EntityVillager.class, "villager", 0.5f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelVillager(0.0f);
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelVillager)) {
            return null;
        }
        final ModelVillager modelvillager = (ModelVillager)model;
        if (modelPart.equals("head")) {
            return modelvillager.villagerHead;
        }
        if (modelPart.equals("body")) {
            return modelvillager.villagerBody;
        }
        if (modelPart.equals("arms")) {
            return modelvillager.villagerArms;
        }
        if (modelPart.equals("left_leg")) {
            return modelvillager.leftVillagerLeg;
        }
        if (modelPart.equals("right_leg")) {
            return modelvillager.rightVillagerLeg;
        }
        return modelPart.equals("nose") ? modelvillager.villagerNose : null;
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "head", "body", "arms", "right_leg", "left_leg", "nose" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderVillager rendervillager = new RenderVillager(rendermanager);
        rendervillager.mainModel = modelBase;
        rendervillager.shadowSize = shadowSize;
        return rendervillager;
    }
}
