package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderMinecart;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.src.Config;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorField;

public class ModelAdapterMinecart
extends ModelAdapter {
    public ModelAdapterMinecart() {
        super(EntityMinecart.class, "minecart", 0.5f);
    }

    protected ModelAdapterMinecart(Class entityClass, String name, float shadow) {
        super(entityClass, name, shadow);
    }

    public ModelBase makeModel() {
        return new ModelMinecart();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelMinecart)) {
            return null;
        }
        ModelMinecart modelminecart = (ModelMinecart)model;
        return modelPart.equals((Object)"bottom") ? modelminecart.sideModels[0] : (modelPart.equals((Object)"back") ? modelminecart.sideModels[1] : (modelPart.equals((Object)"front") ? modelminecart.sideModels[2] : (modelPart.equals((Object)"right") ? modelminecart.sideModels[3] : (modelPart.equals((Object)"left") ? modelminecart.sideModels[4] : (modelPart.equals((Object)"dirt") ? modelminecart.sideModels[5] : null)))));
    }

    public String[] getModelRendererNames() {
        return new String[]{"bottom", "back", "front", "right", "left", "dirt"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderMinecart renderminecart = new RenderMinecart(rendermanager);
        if (!Reflector.RenderMinecart_modelMinecart.exists()) {
            Config.warn((String)"Field not found: RenderMinecart.modelMinecart");
            return null;
        }
        Reflector.setFieldValue((Object)renderminecart, (ReflectorField)Reflector.RenderMinecart_modelMinecart, (Object)modelBase);
        renderminecart.shadowSize = shadowSize;
        return renderminecart;
    }
}
