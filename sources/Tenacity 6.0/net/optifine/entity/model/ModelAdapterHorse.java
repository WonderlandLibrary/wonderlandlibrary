// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.client.Minecraft;
import java.util.HashMap;
import net.optifine.reflect.Reflector;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.passive.EntityHorse;
import java.util.Map;

public class ModelAdapterHorse extends ModelAdapter
{
    private static Map<String, Integer> mapPartFields;
    
    public ModelAdapterHorse() {
        super(EntityHorse.class, "horse", 0.75f);
    }
    
    protected ModelAdapterHorse(final Class entityClass, final String name, final float shadowSize) {
        super(entityClass, name, shadowSize);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelHorse();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelHorse)) {
            return null;
        }
        final ModelHorse modelhorse = (ModelHorse)model;
        final Map<String, Integer> map = getMapPartFields();
        if (map.containsKey(modelPart)) {
            final int i = map.get(modelPart);
            return (ModelRenderer)Reflector.getFieldValue(modelhorse, Reflector.ModelHorse_ModelRenderers, i);
        }
        return null;
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "head", "upper_mouth", "lower_mouth", "horse_left_ear", "horse_right_ear", "mule_left_ear", "mule_right_ear", "neck", "horse_face_ropes", "mane", "body", "tail_base", "tail_middle", "tail_tip", "back_left_leg", "back_left_shin", "back_left_hoof", "back_right_leg", "back_right_shin", "back_right_hoof", "front_left_leg", "front_left_shin", "front_left_hoof", "front_right_leg", "front_right_shin", "front_right_hoof", "mule_left_chest", "mule_right_chest", "horse_saddle_bottom", "horse_saddle_front", "horse_saddle_back", "horse_left_saddle_rope", "horse_left_saddle_metal", "horse_right_saddle_rope", "horse_right_saddle_metal", "horse_left_face_metal", "horse_right_face_metal", "horse_left_rein", "horse_right_rein" };
    }
    
    private static Map<String, Integer> getMapPartFields() {
        if (ModelAdapterHorse.mapPartFields != null) {
            return ModelAdapterHorse.mapPartFields;
        }
        (ModelAdapterHorse.mapPartFields = new HashMap<String, Integer>()).put("head", 0);
        ModelAdapterHorse.mapPartFields.put("upper_mouth", 1);
        ModelAdapterHorse.mapPartFields.put("lower_mouth", 2);
        ModelAdapterHorse.mapPartFields.put("horse_left_ear", 3);
        ModelAdapterHorse.mapPartFields.put("horse_right_ear", 4);
        ModelAdapterHorse.mapPartFields.put("mule_left_ear", 5);
        ModelAdapterHorse.mapPartFields.put("mule_right_ear", 6);
        ModelAdapterHorse.mapPartFields.put("neck", 7);
        ModelAdapterHorse.mapPartFields.put("horse_face_ropes", 8);
        ModelAdapterHorse.mapPartFields.put("mane", 9);
        ModelAdapterHorse.mapPartFields.put("body", 10);
        ModelAdapterHorse.mapPartFields.put("tail_base", 11);
        ModelAdapterHorse.mapPartFields.put("tail_middle", 12);
        ModelAdapterHorse.mapPartFields.put("tail_tip", 13);
        ModelAdapterHorse.mapPartFields.put("back_left_leg", 14);
        ModelAdapterHorse.mapPartFields.put("back_left_shin", 15);
        ModelAdapterHorse.mapPartFields.put("back_left_hoof", 16);
        ModelAdapterHorse.mapPartFields.put("back_right_leg", 17);
        ModelAdapterHorse.mapPartFields.put("back_right_shin", 18);
        ModelAdapterHorse.mapPartFields.put("back_right_hoof", 19);
        ModelAdapterHorse.mapPartFields.put("front_left_leg", 20);
        ModelAdapterHorse.mapPartFields.put("front_left_shin", 21);
        ModelAdapterHorse.mapPartFields.put("front_left_hoof", 22);
        ModelAdapterHorse.mapPartFields.put("front_right_leg", 23);
        ModelAdapterHorse.mapPartFields.put("front_right_shin", 24);
        ModelAdapterHorse.mapPartFields.put("front_right_hoof", 25);
        ModelAdapterHorse.mapPartFields.put("mule_left_chest", 26);
        ModelAdapterHorse.mapPartFields.put("mule_right_chest", 27);
        ModelAdapterHorse.mapPartFields.put("horse_saddle_bottom", 28);
        ModelAdapterHorse.mapPartFields.put("horse_saddle_front", 29);
        ModelAdapterHorse.mapPartFields.put("horse_saddle_back", 30);
        ModelAdapterHorse.mapPartFields.put("horse_left_saddle_rope", 31);
        ModelAdapterHorse.mapPartFields.put("horse_left_saddle_metal", 32);
        ModelAdapterHorse.mapPartFields.put("horse_right_saddle_rope", 33);
        ModelAdapterHorse.mapPartFields.put("horse_right_saddle_metal", 34);
        ModelAdapterHorse.mapPartFields.put("horse_left_face_metal", 35);
        ModelAdapterHorse.mapPartFields.put("horse_right_face_metal", 36);
        ModelAdapterHorse.mapPartFields.put("horse_left_rein", 37);
        ModelAdapterHorse.mapPartFields.put("horse_right_rein", 38);
        return ModelAdapterHorse.mapPartFields;
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderHorse renderhorse = new RenderHorse(rendermanager, (ModelHorse)modelBase, shadowSize);
        return renderhorse;
    }
    
    static {
        ModelAdapterHorse.mapPartFields = null;
    }
}
