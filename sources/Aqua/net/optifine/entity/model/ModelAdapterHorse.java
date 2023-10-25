package net.optifine.entity.model;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityHorse;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorFields;

public class ModelAdapterHorse
extends ModelAdapter {
    private static Map<String, Integer> mapPartFields = null;

    public ModelAdapterHorse() {
        super(EntityHorse.class, "horse", 0.75f);
    }

    protected ModelAdapterHorse(Class entityClass, String name, float shadowSize) {
        super(entityClass, name, shadowSize);
    }

    public ModelBase makeModel() {
        return new ModelHorse();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelHorse)) {
            return null;
        }
        ModelHorse modelhorse = (ModelHorse)model;
        Map<String, Integer> map = ModelAdapterHorse.getMapPartFields();
        if (map.containsKey((Object)modelPart)) {
            int i = (Integer)map.get((Object)modelPart);
            return (ModelRenderer)Reflector.getFieldValue((Object)modelhorse, (ReflectorFields)Reflector.ModelHorse_ModelRenderers, (int)i);
        }
        return null;
    }

    public String[] getModelRendererNames() {
        return new String[]{"head", "upper_mouth", "lower_mouth", "horse_left_ear", "horse_right_ear", "mule_left_ear", "mule_right_ear", "neck", "horse_face_ropes", "mane", "body", "tail_base", "tail_middle", "tail_tip", "back_left_leg", "back_left_shin", "back_left_hoof", "back_right_leg", "back_right_shin", "back_right_hoof", "front_left_leg", "front_left_shin", "front_left_hoof", "front_right_leg", "front_right_shin", "front_right_hoof", "mule_left_chest", "mule_right_chest", "horse_saddle_bottom", "horse_saddle_front", "horse_saddle_back", "horse_left_saddle_rope", "horse_left_saddle_metal", "horse_right_saddle_rope", "horse_right_saddle_metal", "horse_left_face_metal", "horse_right_face_metal", "horse_left_rein", "horse_right_rein"};
    }

    private static Map<String, Integer> getMapPartFields() {
        if (mapPartFields != null) {
            return mapPartFields;
        }
        mapPartFields = new HashMap();
        mapPartFields.put((Object)"head", (Object)0);
        mapPartFields.put((Object)"upper_mouth", (Object)1);
        mapPartFields.put((Object)"lower_mouth", (Object)2);
        mapPartFields.put((Object)"horse_left_ear", (Object)3);
        mapPartFields.put((Object)"horse_right_ear", (Object)4);
        mapPartFields.put((Object)"mule_left_ear", (Object)5);
        mapPartFields.put((Object)"mule_right_ear", (Object)6);
        mapPartFields.put((Object)"neck", (Object)7);
        mapPartFields.put((Object)"horse_face_ropes", (Object)8);
        mapPartFields.put((Object)"mane", (Object)9);
        mapPartFields.put((Object)"body", (Object)10);
        mapPartFields.put((Object)"tail_base", (Object)11);
        mapPartFields.put((Object)"tail_middle", (Object)12);
        mapPartFields.put((Object)"tail_tip", (Object)13);
        mapPartFields.put((Object)"back_left_leg", (Object)14);
        mapPartFields.put((Object)"back_left_shin", (Object)15);
        mapPartFields.put((Object)"back_left_hoof", (Object)16);
        mapPartFields.put((Object)"back_right_leg", (Object)17);
        mapPartFields.put((Object)"back_right_shin", (Object)18);
        mapPartFields.put((Object)"back_right_hoof", (Object)19);
        mapPartFields.put((Object)"front_left_leg", (Object)20);
        mapPartFields.put((Object)"front_left_shin", (Object)21);
        mapPartFields.put((Object)"front_left_hoof", (Object)22);
        mapPartFields.put((Object)"front_right_leg", (Object)23);
        mapPartFields.put((Object)"front_right_shin", (Object)24);
        mapPartFields.put((Object)"front_right_hoof", (Object)25);
        mapPartFields.put((Object)"mule_left_chest", (Object)26);
        mapPartFields.put((Object)"mule_right_chest", (Object)27);
        mapPartFields.put((Object)"horse_saddle_bottom", (Object)28);
        mapPartFields.put((Object)"horse_saddle_front", (Object)29);
        mapPartFields.put((Object)"horse_saddle_back", (Object)30);
        mapPartFields.put((Object)"horse_left_saddle_rope", (Object)31);
        mapPartFields.put((Object)"horse_left_saddle_metal", (Object)32);
        mapPartFields.put((Object)"horse_right_saddle_rope", (Object)33);
        mapPartFields.put((Object)"horse_right_saddle_metal", (Object)34);
        mapPartFields.put((Object)"horse_left_face_metal", (Object)35);
        mapPartFields.put((Object)"horse_right_face_metal", (Object)36);
        mapPartFields.put((Object)"horse_left_rein", (Object)37);
        mapPartFields.put((Object)"horse_right_rein", (Object)38);
        return mapPartFields;
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderHorse renderhorse = new RenderHorse(rendermanager, (ModelHorse)modelBase, shadowSize);
        return renderhorse;
    }
}
