/*
 * Decompiled with CFR 0.150.
 */
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
import optifine.Reflector;

public class ModelAdapterHorse
extends ModelAdapter {
    private static Map<String, Integer> mapPartFields = null;

    public ModelAdapterHorse() {
        super(EntityHorse.class, "horse", 0.75f);
    }

    protected ModelAdapterHorse(Class entityClass, String name, float shadowSize) {
        super(entityClass, name, shadowSize);
    }

    @Override
    public ModelBase makeModel() {
        return new ModelHorse();
    }

    @Override
    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelHorse)) {
            return null;
        }
        ModelHorse modelhorse = (ModelHorse)model;
        Map<String, Integer> map = ModelAdapterHorse.getMapPartFields();
        if (map.containsKey(modelPart)) {
            int i = map.get(modelPart);
            return (ModelRenderer)Reflector.getFieldValue(modelhorse, Reflector.ModelHorse_ModelRenderers, i);
        }
        return null;
    }

    private static Map<String, Integer> getMapPartFields() {
        if (mapPartFields != null) {
            return mapPartFields;
        }
        mapPartFields = new HashMap<String, Integer>();
        mapPartFields.put("head", 0);
        mapPartFields.put("upper_mouth", 1);
        mapPartFields.put("lower_mouth", 2);
        mapPartFields.put("horse_left_ear", 3);
        mapPartFields.put("horse_right_ear", 4);
        mapPartFields.put("mule_left_ear", 5);
        mapPartFields.put("mule_right_ear", 6);
        mapPartFields.put("neck", 7);
        mapPartFields.put("horse_face_ropes", 8);
        mapPartFields.put("mane", 9);
        mapPartFields.put("body", 10);
        mapPartFields.put("tail_base", 11);
        mapPartFields.put("tail_middle", 12);
        mapPartFields.put("tail_tip", 13);
        mapPartFields.put("back_left_leg", 14);
        mapPartFields.put("back_left_shin", 15);
        mapPartFields.put("back_left_hoof", 16);
        mapPartFields.put("back_right_leg", 17);
        mapPartFields.put("back_right_shin", 18);
        mapPartFields.put("back_right_hoof", 19);
        mapPartFields.put("front_left_leg", 20);
        mapPartFields.put("front_left_shin", 21);
        mapPartFields.put("front_left_hoof", 22);
        mapPartFields.put("front_right_leg", 23);
        mapPartFields.put("front_right_shin", 24);
        mapPartFields.put("front_right_hoof", 25);
        mapPartFields.put("mule_left_chest", 26);
        mapPartFields.put("mule_right_chest", 27);
        mapPartFields.put("horse_saddle_bottom", 28);
        mapPartFields.put("horse_saddle_front", 29);
        mapPartFields.put("horse_saddle_back", 30);
        mapPartFields.put("horse_left_saddle_rope", 31);
        mapPartFields.put("horse_left_saddle_metal", 32);
        mapPartFields.put("horse_right_saddle_rope", 33);
        mapPartFields.put("horse_right_saddle_metal", 34);
        mapPartFields.put("horse_left_face_metal", 35);
        mapPartFields.put("horse_right_face_metal", 36);
        mapPartFields.put("horse_left_rein", 37);
        mapPartFields.put("horse_right_rein", 38);
        return mapPartFields;
    }

    @Override
    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderHorse renderhorse = new RenderHorse(rendermanager);
        renderhorse.mainModel = modelBase;
        renderhorse.shadowSize = shadowSize;
        return renderhorse;
    }
}

