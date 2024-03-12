// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderBat;
import net.minecraft.client.Minecraft;
import net.optifine.reflect.Reflector;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelBat;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.passive.EntityBat;

public class ModelAdapterBat extends ModelAdapter
{
    public ModelAdapterBat() {
        super(EntityBat.class, "bat", 0.25f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelBat();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelBat)) {
            return null;
        }
        final ModelBat modelbat = (ModelBat)model;
        return (ModelRenderer)(modelPart.equals("head") ? Reflector.getFieldValue(modelbat, Reflector.ModelBat_ModelRenderers, 0) : (modelPart.equals("body") ? Reflector.getFieldValue(modelbat, Reflector.ModelBat_ModelRenderers, 1) : (modelPart.equals("right_wing") ? Reflector.getFieldValue(modelbat, Reflector.ModelBat_ModelRenderers, 2) : (modelPart.equals("left_wing") ? Reflector.getFieldValue(modelbat, Reflector.ModelBat_ModelRenderers, 3) : (modelPart.equals("outer_right_wing") ? Reflector.getFieldValue(modelbat, Reflector.ModelBat_ModelRenderers, 4) : (modelPart.equals("outer_left_wing") ? ((ModelRenderer)Reflector.getFieldValue(modelbat, Reflector.ModelBat_ModelRenderers, 5)) : null))))));
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "head", "body", "right_wing", "left_wing", "outer_right_wing", "outer_left_wing" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderBat renderbat = new RenderBat(rendermanager);
        renderbat.mainModel = modelBase;
        renderbat.shadowSize = shadowSize;
        return renderbat;
    }
}
