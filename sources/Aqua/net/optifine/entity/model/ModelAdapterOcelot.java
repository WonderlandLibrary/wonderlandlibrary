package net.optifine.entity.model;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderOcelot;
import net.minecraft.entity.passive.EntityOcelot;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorFields;

public class ModelAdapterOcelot
extends ModelAdapter {
    private static Map<String, Integer> mapPartFields = null;

    public ModelAdapterOcelot() {
        super(EntityOcelot.class, "ocelot", 0.4f);
    }

    public ModelBase makeModel() {
        return new ModelOcelot();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelOcelot)) {
            return null;
        }
        ModelOcelot modelocelot = (ModelOcelot)model;
        Map<String, Integer> map = ModelAdapterOcelot.getMapPartFields();
        if (map.containsKey((Object)modelPart)) {
            int i = (Integer)map.get((Object)modelPart);
            return (ModelRenderer)Reflector.getFieldValue((Object)modelocelot, (ReflectorFields)Reflector.ModelOcelot_ModelRenderers, (int)i);
        }
        return null;
    }

    public String[] getModelRendererNames() {
        return new String[]{"back_left_leg", "back_right_leg", "front_left_leg", "front_right_leg", "tail", "tail2", "head", "body"};
    }

    private static Map<String, Integer> getMapPartFields() {
        if (mapPartFields != null) {
            return mapPartFields;
        }
        mapPartFields = new HashMap();
        mapPartFields.put((Object)"back_left_leg", (Object)0);
        mapPartFields.put((Object)"back_right_leg", (Object)1);
        mapPartFields.put((Object)"front_left_leg", (Object)2);
        mapPartFields.put((Object)"front_right_leg", (Object)3);
        mapPartFields.put((Object)"tail", (Object)4);
        mapPartFields.put((Object)"tail2", (Object)5);
        mapPartFields.put((Object)"head", (Object)6);
        mapPartFields.put((Object)"body", (Object)7);
        return mapPartFields;
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderOcelot renderocelot = new RenderOcelot(rendermanager, modelBase, shadowSize);
        return renderocelot;
    }
}
