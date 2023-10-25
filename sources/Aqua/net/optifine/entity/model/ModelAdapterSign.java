package net.optifine.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.src.Config;
import net.minecraft.tileentity.TileEntitySign;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorField;

public class ModelAdapterSign
extends ModelAdapter {
    public ModelAdapterSign() {
        super(TileEntitySign.class, "sign", 0.0f);
    }

    public ModelBase makeModel() {
        return new ModelSign();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelSign)) {
            return null;
        }
        ModelSign modelsign = (ModelSign)model;
        return modelPart.equals((Object)"board") ? modelsign.signBoard : (modelPart.equals((Object)"stick") ? modelsign.signStick : null);
    }

    public String[] getModelRendererNames() {
        return new String[]{"board", "stick"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        TileEntityRendererDispatcher tileentityrendererdispatcher = TileEntityRendererDispatcher.instance;
        TileEntitySpecialRenderer tileentityspecialrenderer = tileentityrendererdispatcher.getSpecialRendererByClass(TileEntitySign.class);
        if (!(tileentityspecialrenderer instanceof TileEntitySignRenderer)) {
            return null;
        }
        if (tileentityspecialrenderer.getEntityClass() == null) {
            tileentityspecialrenderer = new TileEntitySignRenderer();
            tileentityspecialrenderer.setRendererDispatcher(tileentityrendererdispatcher);
        }
        if (!Reflector.TileEntitySignRenderer_model.exists()) {
            Config.warn((String)"Field not found: TileEntitySignRenderer.model");
            return null;
        }
        Reflector.setFieldValue((Object)tileentityspecialrenderer, (ReflectorField)Reflector.TileEntitySignRenderer_model, (Object)modelBase);
        return tileentityspecialrenderer;
    }
}
