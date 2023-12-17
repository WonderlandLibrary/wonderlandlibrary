// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;
import net.minecraft.client.renderer.tileentity.TileEntityBedRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelBed;
import net.minecraft.client.model.ModelBase;
import net.minecraft.tileentity.TileEntityBed;

public class ModelAdapterBed extends ModelAdapter
{
    public ModelAdapterBed() {
        super(TileEntityBed.class, "bed", 0.0f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelBed();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelBed)) {
            return null;
        }
        final ModelBed modelbed = (ModelBed)model;
        if (modelPart.equals("head")) {
            return modelbed.headPiece;
        }
        if (modelPart.equals("foot")) {
            return modelbed.footPiece;
        }
        if (modelPart.equals("leg1")) {
            return modelbed.legs[0];
        }
        if (modelPart.equals("leg2")) {
            return modelbed.legs[1];
        }
        if (modelPart.equals("leg3")) {
            return modelbed.legs[2];
        }
        return modelPart.equals("leg4") ? modelbed.legs[3] : null;
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "head", "foot", "leg1", "leg2", "leg3", "leg4" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final TileEntityRendererDispatcher tileentityrendererdispatcher = TileEntityRendererDispatcher.instance;
        TileEntitySpecialRenderer tileentityspecialrenderer = tileentityrendererdispatcher.getRenderer(TileEntityBed.class);
        if (!(tileentityspecialrenderer instanceof TileEntityBedRenderer)) {
            return null;
        }
        if (tileentityspecialrenderer.getEntityClass() == null) {
            tileentityspecialrenderer = new TileEntityBedRenderer();
            tileentityspecialrenderer.setRendererDispatcher(tileentityrendererdispatcher);
        }
        if (!Reflector.TileEntityBedRenderer_model.exists()) {
            Config.warn("Field not found: TileEntityBedRenderer.model");
            return null;
        }
        Reflector.setFieldValue(tileentityspecialrenderer, Reflector.TileEntityBedRenderer_model, modelBase);
        return tileentityspecialrenderer;
    }
}
