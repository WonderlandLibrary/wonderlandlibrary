package net.optifine.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityEnderChestRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.src.Config;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorField;

public class ModelAdapterEnderChest
extends ModelAdapter {
    public ModelAdapterEnderChest() {
        super(TileEntityEnderChest.class, "ender_chest", 0.0f);
    }

    public ModelBase makeModel() {
        return new ModelChest();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelChest)) {
            return null;
        }
        ModelChest modelchest = (ModelChest)model;
        return modelPart.equals((Object)"lid") ? modelchest.chestLid : (modelPart.equals((Object)"base") ? modelchest.chestBelow : (modelPart.equals((Object)"knob") ? modelchest.chestKnob : null));
    }

    public String[] getModelRendererNames() {
        return new String[]{"lid", "base", "knob"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        TileEntityRendererDispatcher tileentityrendererdispatcher = TileEntityRendererDispatcher.instance;
        TileEntitySpecialRenderer tileentityspecialrenderer = tileentityrendererdispatcher.getSpecialRendererByClass(TileEntityEnderChest.class);
        if (!(tileentityspecialrenderer instanceof TileEntityEnderChestRenderer)) {
            return null;
        }
        if (tileentityspecialrenderer.getEntityClass() == null) {
            tileentityspecialrenderer = new TileEntityEnderChestRenderer();
            tileentityspecialrenderer.setRendererDispatcher(tileentityrendererdispatcher);
        }
        if (!Reflector.TileEntityEnderChestRenderer_modelChest.exists()) {
            Config.warn((String)"Field not found: TileEntityEnderChestRenderer.modelChest");
            return null;
        }
        Reflector.setFieldValue((Object)tileentityspecialrenderer, (ReflectorField)Reflector.TileEntityEnderChestRenderer_modelChest, (Object)modelBase);
        return tileentityspecialrenderer;
    }
}
