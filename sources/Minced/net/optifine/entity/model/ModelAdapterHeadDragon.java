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
import net.minecraft.client.model.ModelDragonHead;
import net.minecraft.client.model.ModelBase;
import net.minecraft.tileentity.TileEntitySkull;

public class ModelAdapterHeadDragon extends ModelAdapter
{
    public ModelAdapterHeadDragon() {
        super(TileEntitySkull.class, "head_dragon", 0.0f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelDragonHead(0.0f);
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelDragonHead)) {
            return null;
        }
        final ModelDragonHead modeldragonhead = (ModelDragonHead)model;
        if (modelPart.equals("head")) {
            return (ModelRenderer)Reflector.getFieldValue(modeldragonhead, Reflector.ModelDragonHead_head);
        }
        return modelPart.equals("jaw") ? ((ModelRenderer)Reflector.getFieldValue(modeldragonhead, Reflector.ModelDragonHead_jaw)) : null;
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "head", "jaw" };
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
        if (!Reflector.TileEntitySkullRenderer_dragonHead.exists()) {
            Config.warn("Field not found: TileEntitySkullRenderer.dragonHead");
            return null;
        }
        Reflector.setFieldValue(tileentityspecialrenderer, Reflector.TileEntitySkullRenderer_dragonHead, modelBase);
        return tileentityspecialrenderer;
    }
}
