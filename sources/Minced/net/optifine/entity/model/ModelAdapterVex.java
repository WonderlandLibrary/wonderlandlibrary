// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderVex;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;
import net.minecraft.client.model.ModelVex;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.monster.EntityVex;

public class ModelAdapterVex extends ModelAdapterBiped
{
    public ModelAdapterVex() {
        super(EntityVex.class, "vex", 0.3f);
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelVex)) {
            return null;
        }
        final ModelRenderer modelrenderer = super.getModelRenderer(model, modelPart);
        if (modelrenderer != null) {
            return modelrenderer;
        }
        final ModelVex modelvex = (ModelVex)model;
        if (modelPart.equals("left_wing")) {
            return (ModelRenderer)Reflector.getFieldValue(modelvex, Reflector.ModelVex_leftWing);
        }
        return modelPart.equals("right_wing") ? ((ModelRenderer)Reflector.getFieldValue(modelvex, Reflector.ModelVex_rightWing)) : null;
    }
    
    @Override
    public String[] getModelRendererNames() {
        String[] astring = super.getModelRendererNames();
        astring = (String[])Config.addObjectsToArray(astring, new String[] { "left_wing", "right_wing" });
        return astring;
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelVex();
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderVex rendervex = new RenderVex(rendermanager);
        rendervex.mainModel = modelBase;
        rendervex.shadowSize = shadowSize;
        return rendervex;
    }
}
