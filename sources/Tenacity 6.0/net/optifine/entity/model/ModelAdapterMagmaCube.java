// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderMagmaCube;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelMagmaCube;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.monster.EntityMagmaCube;

public class ModelAdapterMagmaCube extends ModelAdapter
{
    public ModelAdapterMagmaCube() {
        super(EntityMagmaCube.class, "magma_cube", 0.5f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelMagmaCube();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelMagmaCube)) {
            return null;
        }
        final ModelMagmaCube modelmagmacube = (ModelMagmaCube)model;
        if (modelPart.equals("core")) {
            return (ModelRenderer)Reflector.getFieldValue(modelmagmacube, Reflector.ModelMagmaCube_core);
        }
        final String s = "segment";
        if (!modelPart.startsWith(s)) {
            return null;
        }
        final ModelRenderer[] amodelrenderer = (ModelRenderer[])Reflector.getFieldValue(modelmagmacube, Reflector.ModelMagmaCube_segments);
        if (amodelrenderer == null) {
            return null;
        }
        final String s2 = modelPart.substring(s.length());
        int i = Config.parseInt(s2, -1);
        return (--i >= 0 && i < amodelrenderer.length) ? amodelrenderer[i] : null;
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "core", "segment1", "segment2", "segment3", "segment4", "segment5", "segment6", "segment7", "segment8" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderMagmaCube rendermagmacube = new RenderMagmaCube(rendermanager);
        rendermagmacube.mainModel = modelBase;
        rendermagmacube.shadowSize = shadowSize;
        return rendermagmacube;
    }
}
