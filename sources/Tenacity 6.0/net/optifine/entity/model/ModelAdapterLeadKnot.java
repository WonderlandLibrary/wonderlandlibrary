// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;
import net.minecraft.client.renderer.entity.RenderLeashKnot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelLeashKnot;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLeashKnot;

public class ModelAdapterLeadKnot extends ModelAdapter
{
    public ModelAdapterLeadKnot() {
        super(EntityLeashKnot.class, "lead_knot", 0.0f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelLeashKnot();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelLeashKnot)) {
            return null;
        }
        final ModelLeashKnot modelleashknot = (ModelLeashKnot)model;
        return modelPart.equals("knot") ? modelleashknot.field_110723_a : null;
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "knot" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderLeashKnot renderleashknot = new RenderLeashKnot(rendermanager);
        if (!Reflector.RenderLeashKnot_leashKnotModel.exists()) {
            Config.warn("Field not found: RenderLeashKnot.leashKnotModel");
            return null;
        }
        Reflector.setFieldValue(renderleashknot, Reflector.RenderLeashKnot_leashKnotModel, modelBase);
        renderleashknot.shadowSize = shadowSize;
        return renderleashknot;
    }
}
