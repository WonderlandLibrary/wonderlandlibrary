// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWither;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelWither;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.boss.EntityWither;

public class ModelAdapterWither extends ModelAdapter
{
    public ModelAdapterWither() {
        super(EntityWither.class, "wither", 0.5f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelWither(0.0f);
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelWither)) {
            return null;
        }
        final ModelWither modelwither = (ModelWither)model;
        final String s = "body";
        if (modelPart.startsWith(s)) {
            final ModelRenderer[] amodelrenderer1 = (ModelRenderer[])Reflector.getFieldValue(modelwither, Reflector.ModelWither_bodyParts);
            if (amodelrenderer1 == null) {
                return null;
            }
            final String s2 = modelPart.substring(s.length());
            int j = Config.parseInt(s2, -1);
            return (--j >= 0 && j < amodelrenderer1.length) ? amodelrenderer1[j] : null;
        }
        else {
            final String s3 = "head";
            if (!modelPart.startsWith(s3)) {
                return null;
            }
            final ModelRenderer[] amodelrenderer2 = (ModelRenderer[])Reflector.getFieldValue(modelwither, Reflector.ModelWither_heads);
            if (amodelrenderer2 == null) {
                return null;
            }
            final String s4 = modelPart.substring(s3.length());
            int i = Config.parseInt(s4, -1);
            return (--i >= 0 && i < amodelrenderer2.length) ? amodelrenderer2[i] : null;
        }
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "body1", "body2", "body3", "head1", "head2", "head3" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderWither renderwither = new RenderWither(rendermanager);
        renderwither.mainModel = modelBase;
        renderwither.shadowSize = shadowSize;
        return renderwither;
    }
}
