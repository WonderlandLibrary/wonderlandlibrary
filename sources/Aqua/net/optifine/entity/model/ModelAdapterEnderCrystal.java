package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.RenderEnderCrystal;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.src.Config;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorField;
import net.optifine.reflect.ReflectorFields;

public class ModelAdapterEnderCrystal
extends ModelAdapter {
    public ModelAdapterEnderCrystal() {
        this("end_crystal");
    }

    protected ModelAdapterEnderCrystal(String name) {
        super(EntityEnderCrystal.class, name, 0.5f);
    }

    public ModelBase makeModel() {
        return new ModelEnderCrystal(0.0f, true);
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelEnderCrystal)) {
            return null;
        }
        ModelEnderCrystal modelendercrystal = (ModelEnderCrystal)model;
        return modelPart.equals((Object)"cube") ? (ModelRenderer)Reflector.getFieldValue((Object)modelendercrystal, (ReflectorFields)Reflector.ModelEnderCrystal_ModelRenderers, (int)0) : (modelPart.equals((Object)"glass") ? (ModelRenderer)Reflector.getFieldValue((Object)modelendercrystal, (ReflectorFields)Reflector.ModelEnderCrystal_ModelRenderers, (int)1) : (modelPart.equals((Object)"base") ? (ModelRenderer)Reflector.getFieldValue((Object)modelendercrystal, (ReflectorFields)Reflector.ModelEnderCrystal_ModelRenderers, (int)2) : null));
    }

    public String[] getModelRendererNames() {
        return new String[]{"cube", "glass", "base"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        Render render = (Render)rendermanager.getEntityRenderMap().get(EntityEnderCrystal.class);
        if (!(render instanceof RenderEnderCrystal)) {
            Config.warn((String)("Not an instance of RenderEnderCrystal: " + render));
            return null;
        }
        RenderEnderCrystal renderendercrystal = (RenderEnderCrystal)render;
        if (!Reflector.RenderEnderCrystal_modelEnderCrystal.exists()) {
            Config.warn((String)"Field not found: RenderEnderCrystal.modelEnderCrystal");
            return null;
        }
        Reflector.setFieldValue((Object)renderendercrystal, (ReflectorField)Reflector.RenderEnderCrystal_modelEnderCrystal, (Object)modelBase);
        renderendercrystal.shadowSize = shadowSize;
        return renderendercrystal;
    }
}
