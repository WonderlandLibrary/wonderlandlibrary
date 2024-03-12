// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;
import net.minecraft.client.renderer.tileentity.TileEntityBannerRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelBanner;
import net.minecraft.client.model.ModelBase;
import net.minecraft.tileentity.TileEntityBanner;

public class ModelAdapterBanner extends ModelAdapter
{
    public ModelAdapterBanner() {
        super(TileEntityBanner.class, "banner", 0.0f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelBanner();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelBanner)) {
            return null;
        }
        final ModelBanner modelbanner = (ModelBanner)model;
        return modelPart.equals("slate") ? modelbanner.bannerSlate : (modelPart.equals("stand") ? modelbanner.bannerStand : (modelPart.equals("top") ? modelbanner.bannerTop : null));
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "slate", "stand", "top" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final TileEntityRendererDispatcher tileentityrendererdispatcher = TileEntityRendererDispatcher.instance;
        TileEntitySpecialRenderer tileentityspecialrenderer = tileentityrendererdispatcher.getSpecialRendererByClass(TileEntityBanner.class);
        if (!(tileentityspecialrenderer instanceof TileEntityBannerRenderer)) {
            return null;
        }
        if (tileentityspecialrenderer.getEntityClass() == null) {
            tileentityspecialrenderer = new TileEntityBannerRenderer();
            tileentityspecialrenderer.setRendererDispatcher(tileentityrendererdispatcher);
        }
        if (!Reflector.TileEntityBannerRenderer_bannerModel.exists()) {
            Config.warn("Field not found: TileEntityBannerRenderer.bannerModel");
            return null;
        }
        Reflector.setFieldValue(tileentityspecialrenderer, Reflector.TileEntityBannerRenderer_bannerModel, modelBase);
        return tileentityspecialrenderer;
    }
}
