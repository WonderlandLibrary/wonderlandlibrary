// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderOcelot;
import net.minecraft.client.Minecraft;
import java.util.HashMap;
import net.optifine.reflect.Reflector;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.passive.EntityOcelot;
import java.util.Map;

public class ModelAdapterOcelot extends ModelAdapter
{
    private static Map<String, Integer> mapPartFields;
    
    public ModelAdapterOcelot() {
        super(EntityOcelot.class, "ocelot", 0.4f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelOcelot();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelOcelot)) {
            return null;
        }
        final ModelOcelot modelocelot = (ModelOcelot)model;
        final Map<String, Integer> map = getMapPartFields();
        if (map.containsKey(modelPart)) {
            final int i = map.get(modelPart);
            return (ModelRenderer)Reflector.getFieldValue(modelocelot, Reflector.ModelOcelot_ModelRenderers, i);
        }
        return null;
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "back_left_leg", "back_right_leg", "front_left_leg", "front_right_leg", "tail", "tail2", "head", "body" };
    }
    
    private static Map<String, Integer> getMapPartFields() {
        if (ModelAdapterOcelot.mapPartFields != null) {
            return ModelAdapterOcelot.mapPartFields;
        }
        (ModelAdapterOcelot.mapPartFields = new HashMap<String, Integer>()).put("back_left_leg", 0);
        ModelAdapterOcelot.mapPartFields.put("back_right_leg", 1);
        ModelAdapterOcelot.mapPartFields.put("front_left_leg", 2);
        ModelAdapterOcelot.mapPartFields.put("front_right_leg", 3);
        ModelAdapterOcelot.mapPartFields.put("tail", 4);
        ModelAdapterOcelot.mapPartFields.put("tail2", 5);
        ModelAdapterOcelot.mapPartFields.put("head", 6);
        ModelAdapterOcelot.mapPartFields.put("body", 7);
        return ModelAdapterOcelot.mapPartFields;
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderOcelot renderocelot = new RenderOcelot(rendermanager, modelBase, shadowSize);
        return renderocelot;
    }
    
    static {
        ModelAdapterOcelot.mapPartFields = null;
    }
}
