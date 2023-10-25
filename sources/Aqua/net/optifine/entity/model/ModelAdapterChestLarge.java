package net.optifine.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.src.Config;
import net.minecraft.tileentity.TileEntityChest;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorField;

public class ModelAdapterChestLarge
extends ModelAdapter {
    public ModelAdapterChestLarge() {
        super(TileEntityChest.class, "chest_large", 0.0f);
    }

    public ModelBase makeModel() {
        return new ModelLargeChest();
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
        TileEntitySpecialRenderer tileentityspecialrenderer = tileentityrendererdispatcher.getSpecialRendererByClass(TileEntityChest.class);
        if (!(tileentityspecialrenderer instanceof TileEntityChestRenderer)) {
            return null;
        }
        if (tileentityspecialrenderer.getEntityClass() == null) {
            tileentityspecialrenderer = new TileEntityChestRenderer();
            tileentityspecialrenderer.setRendererDispatcher(tileentityrendererdispatcher);
        }
        if (!Reflector.TileEntityChestRenderer_largeChest.exists()) {
            Config.warn((String)"Field not found: TileEntityChestRenderer.largeChest");
            return null;
        }
        Reflector.setFieldValue((Object)tileentityspecialrenderer, (ReflectorField)Reflector.TileEntityChestRenderer_largeChest, (Object)modelBase);
        return tileentityspecialrenderer;
    }
}
