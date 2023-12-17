// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.src.Config;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.optifine.reflect.Reflector;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelHumanoidHead;
import net.minecraft.client.model.ModelBase;
import net.minecraft.tileentity.TileEntitySkull;

public class ModelAdapterHeadHumanoid extends ModelAdapter
{
    public ModelAdapterHeadHumanoid() {
        super(TileEntitySkull.class, "head_humanoid", 0.0f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelHumanoidHead();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelHumanoidHead)) {
            return null;
        }
        final ModelHumanoidHead modelhumanoidhead = (ModelHumanoidHead)model;
        if (modelPart.equals("head")) {
            return modelhumanoidhead.skeletonHead;
        }
        if (modelPart.equals("head2")) {
            return Reflector.ModelHumanoidHead_head.exists() ? ((ModelRenderer)Reflector.getFieldValue(modelhumanoidhead, Reflector.ModelHumanoidHead_head)) : null;
        }
        return null;
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "head" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final TileEntityRendererDispatcher tileentityrendererdispatcher = TileEntityRendererDispatcher.instance;
        TileEntitySpecialRenderer tileentityspecialrenderer = tileentityrendererdispatcher.getRenderer(TileEntitySkull.class);
        if (!(tileentityspecialrenderer instanceof TileEntitySkullRenderer)) {
            return null;
        }
        if (tileentityspecialrenderer.getEntityClass() == null) {
            tileentityspecialrenderer = new TileEntitySkullRenderer();
            tileentityspecialrenderer.setRendererDispatcher(tileentityrendererdispatcher);
        }
        if (!Reflector.TileEntitySkullRenderer_humanoidHead.exists()) {
            Config.warn("Field not found: TileEntitySkullRenderer.humanoidHead");
            return null;
        }
        Reflector.setFieldValue(tileentityspecialrenderer, Reflector.TileEntitySkullRenderer_humanoidHead, modelBase);
        return tileentityspecialrenderer;
    }
}
