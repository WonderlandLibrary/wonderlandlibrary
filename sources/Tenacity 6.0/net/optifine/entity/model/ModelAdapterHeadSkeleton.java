// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.model.ModelBase;
import net.minecraft.tileentity.TileEntitySkull;

public class ModelAdapterHeadSkeleton extends ModelAdapter
{
    public ModelAdapterHeadSkeleton() {
        super(TileEntitySkull.class, "head_skeleton", 0.0f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelSkeletonHead(0, 0, 64, 32);
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelSkeletonHead)) {
            return null;
        }
        final ModelSkeletonHead modelskeletonhead = (ModelSkeletonHead)model;
        return modelPart.equals("head") ? modelskeletonhead.skeletonHead : null;
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "head" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final TileEntityRendererDispatcher tileentityrendererdispatcher = TileEntityRendererDispatcher.instance;
        TileEntitySpecialRenderer tileentityspecialrenderer = tileentityrendererdispatcher.getSpecialRendererByClass(TileEntitySkull.class);
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
