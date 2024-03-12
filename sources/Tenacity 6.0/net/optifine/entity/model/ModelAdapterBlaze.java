// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderBlaze;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelBlaze;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.monster.EntityBlaze;

public class ModelAdapterBlaze extends ModelAdapter
{
    public ModelAdapterBlaze() {
        super(EntityBlaze.class, "blaze", 0.5f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelBlaze();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelBlaze)) {
            return null;
        }
        final ModelBlaze modelblaze = (ModelBlaze)model;
        if (modelPart.equals("head")) {
            return (ModelRenderer)Reflector.getFieldValue(modelblaze, Reflector.ModelBlaze_blazeHead);
        }
        final String s = "stick";
        if (!modelPart.startsWith(s)) {
            return null;
        }
        final ModelRenderer[] amodelrenderer = (ModelRenderer[])Reflector.getFieldValue(modelblaze, Reflector.ModelBlaze_blazeSticks);
        if (amodelrenderer == null) {
            return null;
        }
        final String s2 = modelPart.substring(s.length());
        int i = Config.parseInt(s2, -1);
        return (--i >= 0 && i < amodelrenderer.length) ? amodelrenderer[i] : null;
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "head", "stick1", "stick2", "stick3", "stick4", "stick5", "stick6", "stick7", "stick8", "stick9", "stick10", "stick11", "stick12" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderBlaze renderblaze = new RenderBlaze(rendermanager);
        renderblaze.mainModel = modelBase;
        renderblaze.shadowSize = shadowSize;
        return renderblaze;
    }
}
