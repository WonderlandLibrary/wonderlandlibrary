package net.optifine.entity.model;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRabbit;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderRabbit;
import net.minecraft.entity.passive.EntityRabbit;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorFields;

public class ModelAdapterRabbit
extends ModelAdapter {
    private static Map<String, Integer> mapPartFields = null;

    public ModelAdapterRabbit() {
        super(EntityRabbit.class, "rabbit", 0.3f);
    }

    public ModelBase makeModel() {
        return new ModelRabbit();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelRabbit)) {
            return null;
        }
        ModelRabbit modelrabbit = (ModelRabbit)model;
        Map<String, Integer> map = ModelAdapterRabbit.getMapPartFields();
        if (map.containsKey((Object)modelPart)) {
            int i = (Integer)map.get((Object)modelPart);
            return (ModelRenderer)Reflector.getFieldValue((Object)modelrabbit, (ReflectorFields)Reflector.ModelRabbit_renderers, (int)i);
        }
        return null;
    }

    public String[] getModelRendererNames() {
        return new String[]{"left_foot", "right_foot", "left_thigh", "right_thigh", "body", "left_arm", "right_arm", "head", "right_ear", "left_ear", "tail", "nose"};
    }

    private static Map<String, Integer> getMapPartFields() {
        if (mapPartFields != null) {
            return mapPartFields;
        }
        mapPartFields = new HashMap();
        mapPartFields.put((Object)"left_foot", (Object)0);
        mapPartFields.put((Object)"right_foot", (Object)1);
        mapPartFields.put((Object)"left_thigh", (Object)2);
        mapPartFields.put((Object)"right_thigh", (Object)3);
        mapPartFields.put((Object)"body", (Object)4);
        mapPartFields.put((Object)"left_arm", (Object)5);
        mapPartFields.put((Object)"right_arm", (Object)6);
        mapPartFields.put((Object)"head", (Object)7);
        mapPartFields.put((Object)"right_ear", (Object)8);
        mapPartFields.put((Object)"left_ear", (Object)9);
        mapPartFields.put((Object)"tail", (Object)10);
        mapPartFields.put((Object)"nose", (Object)11);
        return mapPartFields;
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderRabbit renderrabbit = new RenderRabbit(rendermanager, modelBase, shadowSize);
        return renderrabbit;
    }
}
