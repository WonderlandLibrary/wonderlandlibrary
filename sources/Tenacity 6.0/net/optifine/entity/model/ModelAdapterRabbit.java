// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderRabbit;
import net.minecraft.client.Minecraft;
import java.util.HashMap;
import net.optifine.reflect.Reflector;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelRabbit;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.passive.EntityRabbit;
import java.util.Map;

public class ModelAdapterRabbit extends ModelAdapter
{
    private static Map<String, Integer> mapPartFields;
    
    public ModelAdapterRabbit() {
        super(EntityRabbit.class, "rabbit", 0.3f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelRabbit();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelRabbit)) {
            return null;
        }
        final ModelRabbit modelrabbit = (ModelRabbit)model;
        final Map<String, Integer> map = getMapPartFields();
        if (map.containsKey(modelPart)) {
            final int i = map.get(modelPart);
            return (ModelRenderer)Reflector.getFieldValue(modelrabbit, Reflector.ModelRabbit_renderers, i);
        }
        return null;
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "left_foot", "right_foot", "left_thigh", "right_thigh", "body", "left_arm", "right_arm", "head", "right_ear", "left_ear", "tail", "nose" };
    }
    
    private static Map<String, Integer> getMapPartFields() {
        if (ModelAdapterRabbit.mapPartFields != null) {
            return ModelAdapterRabbit.mapPartFields;
        }
        (ModelAdapterRabbit.mapPartFields = new HashMap<String, Integer>()).put("left_foot", 0);
        ModelAdapterRabbit.mapPartFields.put("right_foot", 1);
        ModelAdapterRabbit.mapPartFields.put("left_thigh", 2);
        ModelAdapterRabbit.mapPartFields.put("right_thigh", 3);
        ModelAdapterRabbit.mapPartFields.put("body", 4);
        ModelAdapterRabbit.mapPartFields.put("left_arm", 5);
        ModelAdapterRabbit.mapPartFields.put("right_arm", 6);
        ModelAdapterRabbit.mapPartFields.put("head", 7);
        ModelAdapterRabbit.mapPartFields.put("right_ear", 8);
        ModelAdapterRabbit.mapPartFields.put("left_ear", 9);
        ModelAdapterRabbit.mapPartFields.put("tail", 10);
        ModelAdapterRabbit.mapPartFields.put("nose", 11);
        return ModelAdapterRabbit.mapPartFields;
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderRabbit renderrabbit = new RenderRabbit(rendermanager, modelBase, shadowSize);
        return renderrabbit;
    }
    
    static {
        ModelAdapterRabbit.mapPartFields = null;
    }
}
