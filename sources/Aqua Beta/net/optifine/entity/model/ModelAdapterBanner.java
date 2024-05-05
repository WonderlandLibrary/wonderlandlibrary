package net.optifine.entity.model;

import net.minecraft.client.model.ModelBanner;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityBannerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.src.Config;
import net.minecraft.tileentity.TileEntityBanner;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorField;

public class ModelAdapterBanner
extends ModelAdapter {
    public ModelAdapterBanner() {
        super(TileEntityBanner.class, "banner", 0.0f);
    }

    public ModelBase makeModel() {
        return new ModelBanner();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelBanner)) {
            return null;
        }
        ModelBanner modelbanner = (ModelBanner)model;
        return modelPart.equals((Object)"slate") ? modelbanner.bannerSlate : (modelPart.equals((Object)"stand") ? modelbanner.bannerStand : (modelPart.equals((Object)"top") ? modelbanner.bannerTop : null));
    }

    public String[] getModelRendererNames() {
        return new String[]{"slate", "stand", "top"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        TileEntityRendererDispatcher tileentityrendererdispatcher = TileEntityRendererDispatcher.instance;
        TileEntitySpecialRenderer tileentityspecialrenderer = tileentityrendererdispatcher.getSpecialRendererByClass(TileEntityBanner.class);
        if (!(tileentityspecialrenderer instanceof TileEntityBannerRenderer)) {
            return null;
        }
        if (tileentityspecialrenderer.getEntityClass() == null) {
            tileentityspecialrenderer = new TileEntityBannerRenderer();
            tileentityspecialrenderer.setRendererDispatcher(tileentityrendererdispatcher);
        }
        if (!Reflector.TileEntityBannerRenderer_bannerModel.exists()) {
            Config.warn((String)"Field not found: TileEntityBannerRenderer.bannerModel");
            return null;
        }
        Reflector.setFieldValue((Object)tileentityspecialrenderer, (ReflectorField)Reflector.TileEntityBannerRenderer_bannerModel, (Object)modelBase);
        return tileentityspecialrenderer;
    }
}
