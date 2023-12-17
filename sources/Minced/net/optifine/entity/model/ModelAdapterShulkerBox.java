// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;
import net.minecraft.client.renderer.tileentity.TileEntityShulkerBoxRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.client.model.ModelBase;
import net.minecraft.tileentity.TileEntityShulkerBox;

public class ModelAdapterShulkerBox extends ModelAdapter
{
    public ModelAdapterShulkerBox() {
        super(TileEntityShulkerBox.class, "shulker_box", 0.0f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelShulker();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelShulker)) {
            return null;
        }
        final ModelShulker modelshulker = (ModelShulker)model;
        if (modelPart.equals("head")) {
            return modelshulker.head;
        }
        if (modelPart.equals("base")) {
            return modelshulker.base;
        }
        return modelPart.equals("lid") ? modelshulker.lid : null;
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "base", "lid", "head" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final TileEntityRendererDispatcher tileentityrendererdispatcher = TileEntityRendererDispatcher.instance;
        TileEntitySpecialRenderer tileentityspecialrenderer = tileentityrendererdispatcher.getRenderer(TileEntityShulkerBox.class);
        if (!(tileentityspecialrenderer instanceof TileEntityShulkerBoxRenderer)) {
            return null;
        }
        if (tileentityspecialrenderer.getEntityClass() == null) {
            tileentityspecialrenderer = new TileEntityShulkerBoxRenderer((ModelShulker)modelBase);
            tileentityspecialrenderer.setRendererDispatcher(tileentityrendererdispatcher);
        }
        if (!Reflector.TileEntityShulkerBoxRenderer_model.exists()) {
            Config.warn("Field not found: TileEntityShulkerBoxRenderer.model");
            return null;
        }
        Reflector.setFieldValue(tileentityspecialrenderer, Reflector.TileEntityShulkerBoxRenderer_model, modelBase);
        return tileentityspecialrenderer;
    }
}
