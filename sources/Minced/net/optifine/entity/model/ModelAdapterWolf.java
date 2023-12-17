// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.client.Minecraft;
import net.optifine.reflect.Reflector;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.passive.EntityWolf;

public class ModelAdapterWolf extends ModelAdapter
{
    public ModelAdapterWolf() {
        super(EntityWolf.class, "wolf", 0.5f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelWolf();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelWolf)) {
            return null;
        }
        final ModelWolf modelwolf = (ModelWolf)model;
        if (modelPart.equals("head")) {
            return modelwolf.wolfHeadMain;
        }
        if (modelPart.equals("body")) {
            return modelwolf.wolfBody;
        }
        if (modelPart.equals("leg1")) {
            return modelwolf.wolfLeg1;
        }
        if (modelPart.equals("leg2")) {
            return modelwolf.wolfLeg2;
        }
        if (modelPart.equals("leg3")) {
            return modelwolf.wolfLeg3;
        }
        if (modelPart.equals("leg4")) {
            return modelwolf.wolfLeg4;
        }
        if (modelPart.equals("tail")) {
            return (ModelRenderer)Reflector.getFieldValue(modelwolf, Reflector.ModelWolf_tail);
        }
        return modelPart.equals("mane") ? ((ModelRenderer)Reflector.getFieldValue(modelwolf, Reflector.ModelWolf_mane)) : null;
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "head", "body", "leg1", "leg2", "leg3", "leg4", "tail", "mane" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderWolf renderwolf = new RenderWolf(rendermanager);
        renderwolf.mainModel = modelBase;
        renderwolf.shadowSize = shadowSize;
        return renderwolf;
    }
}
