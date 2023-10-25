package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.src.Config;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorField;

public class ModelAdapterBoat
extends ModelAdapter {
    public ModelAdapterBoat() {
        super(EntityBoat.class, "boat", 0.5f);
    }

    public ModelBase makeModel() {
        return new ModelBoat();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelBoat)) {
            return null;
        }
        ModelBoat modelboat = (ModelBoat)model;
        return modelPart.equals((Object)"bottom") ? modelboat.boatSides[0] : (modelPart.equals((Object)"back") ? modelboat.boatSides[1] : (modelPart.equals((Object)"front") ? modelboat.boatSides[2] : (modelPart.equals((Object)"right") ? modelboat.boatSides[3] : (modelPart.equals((Object)"left") ? modelboat.boatSides[4] : null))));
    }

    public String[] getModelRendererNames() {
        return new String[]{"bottom", "back", "front", "right", "left"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderBoat renderboat = new RenderBoat(rendermanager);
        if (!Reflector.RenderBoat_modelBoat.exists()) {
            Config.warn((String)"Field not found: RenderBoat.modelBoat");
            return null;
        }
        Reflector.setFieldValue((Object)renderboat, (ReflectorField)Reflector.RenderBoat_modelBoat, (Object)modelBase);
        renderboat.shadowSize = shadowSize;
        return renderboat;
    }
}
