// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.passive.EntityChicken;

public class ModelAdapterChicken extends ModelAdapter
{
    public ModelAdapterChicken() {
        super(EntityChicken.class, "chicken", 0.3f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelChicken();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelChicken)) {
            return null;
        }
        final ModelChicken modelchicken = (ModelChicken)model;
        if (modelPart.equals("head")) {
            return modelchicken.head;
        }
        if (modelPart.equals("body")) {
            return modelchicken.body;
        }
        if (modelPart.equals("right_leg")) {
            return modelchicken.rightLeg;
        }
        if (modelPart.equals("left_leg")) {
            return modelchicken.leftLeg;
        }
        if (modelPart.equals("right_wing")) {
            return modelchicken.rightWing;
        }
        if (modelPart.equals("left_wing")) {
            return modelchicken.leftWing;
        }
        if (modelPart.equals("bill")) {
            return modelchicken.bill;
        }
        return modelPart.equals("chin") ? modelchicken.chin : null;
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "head", "body", "right_leg", "left_leg", "right_wing", "left_wing", "bill", "chin" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderChicken renderchicken = new RenderChicken(rendermanager);
        renderchicken.mainModel = modelBase;
        renderchicken.shadowSize = shadowSize;
        return renderchicken;
    }
}
