// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderEndermite;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelEnderMite;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.monster.EntityEndermite;

public class ModelAdapterEndermite extends ModelAdapter
{
    public ModelAdapterEndermite() {
        super(EntityEndermite.class, "endermite", 0.3f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelEnderMite();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelEnderMite)) {
            return null;
        }
        final ModelEnderMite modelendermite = (ModelEnderMite)model;
        final String s = "body";
        if (!modelPart.startsWith(s)) {
            return null;
        }
        final ModelRenderer[] amodelrenderer = (ModelRenderer[])Reflector.getFieldValue(modelendermite, Reflector.ModelEnderMite_bodyParts);
        if (amodelrenderer == null) {
            return null;
        }
        final String s2 = modelPart.substring(s.length());
        int i = Config.parseInt(s2, -1);
        return (--i >= 0 && i < amodelrenderer.length) ? amodelrenderer[i] : null;
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "body1", "body2", "body3", "body4" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderEndermite renderendermite = new RenderEndermite(rendermanager);
        renderendermite.mainModel = modelBase;
        renderendermite.shadowSize = shadowSize;
        return renderendermite;
    }
}
