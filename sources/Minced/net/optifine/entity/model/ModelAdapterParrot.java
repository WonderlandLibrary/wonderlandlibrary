// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderParrot;
import net.minecraft.client.Minecraft;
import net.optifine.reflect.Reflector;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelParrot;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.passive.EntityParrot;

public class ModelAdapterParrot extends ModelAdapter
{
    public ModelAdapterParrot() {
        super(EntityParrot.class, "parrot", 0.3f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelParrot();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelParrot)) {
            return null;
        }
        final ModelParrot modelparrot = (ModelParrot)model;
        if (modelPart.equals("body")) {
            return (ModelRenderer)Reflector.getFieldValue(modelparrot, Reflector.ModelParrot_ModelRenderers, 0);
        }
        if (modelPart.equals("tail")) {
            return (ModelRenderer)Reflector.getFieldValue(modelparrot, Reflector.ModelParrot_ModelRenderers, 1);
        }
        if (modelPart.equals("left_wing")) {
            return (ModelRenderer)Reflector.getFieldValue(modelparrot, Reflector.ModelParrot_ModelRenderers, 2);
        }
        if (modelPart.equals("right_wing")) {
            return (ModelRenderer)Reflector.getFieldValue(modelparrot, Reflector.ModelParrot_ModelRenderers, 3);
        }
        if (modelPart.equals("head")) {
            return (ModelRenderer)Reflector.getFieldValue(modelparrot, Reflector.ModelParrot_ModelRenderers, 4);
        }
        if (modelPart.equals("left_leg")) {
            return (ModelRenderer)Reflector.getFieldValue(modelparrot, Reflector.ModelParrot_ModelRenderers, 9);
        }
        return modelPart.equals("right_leg") ? ((ModelRenderer)Reflector.getFieldValue(modelparrot, Reflector.ModelParrot_ModelRenderers, 10)) : null;
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "body", "tail", "left_wing", "right_wing", "head", "left_leg", "right_leg" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderParrot renderparrot = new RenderParrot(rendermanager);
        renderparrot.mainModel = modelBase;
        renderparrot.shadowSize = shadowSize;
        return renderparrot;
    }
}
