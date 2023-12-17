// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderGuardian;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelGuardian;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.monster.EntityGuardian;

public class ModelAdapterGuardian extends ModelAdapter
{
    public ModelAdapterGuardian() {
        super(EntityGuardian.class, "guardian", 0.5f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelGuardian();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelGuardian)) {
            return null;
        }
        final ModelGuardian modelguardian = (ModelGuardian)model;
        if (modelPart.equals("body")) {
            return (ModelRenderer)Reflector.getFieldValue(modelguardian, Reflector.ModelGuardian_body);
        }
        if (modelPart.equals("eye")) {
            return (ModelRenderer)Reflector.getFieldValue(modelguardian, Reflector.ModelGuardian_eye);
        }
        final String s = "spine";
        if (modelPart.startsWith(s)) {
            final ModelRenderer[] amodelrenderer1 = (ModelRenderer[])Reflector.getFieldValue(modelguardian, Reflector.ModelGuardian_spines);
            if (amodelrenderer1 == null) {
                return null;
            }
            final String s2 = modelPart.substring(s.length());
            int j = Config.parseInt(s2, -1);
            return (--j >= 0 && j < amodelrenderer1.length) ? amodelrenderer1[j] : null;
        }
        else {
            final String s3 = "tail";
            if (!modelPart.startsWith(s3)) {
                return null;
            }
            final ModelRenderer[] amodelrenderer2 = (ModelRenderer[])Reflector.getFieldValue(modelguardian, Reflector.ModelGuardian_tail);
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
        return new String[] { "body", "eye", "spine1", "spine2", "spine3", "spine4", "spine5", "spine6", "spine7", "spine8", "spine9", "spine10", "spine11", "spine12", "tail1", "tail2", "tail3" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderGuardian renderguardian = new RenderGuardian(rendermanager);
        renderguardian.mainModel = modelBase;
        renderguardian.shadowSize = shadowSize;
        return renderguardian;
    }
}
