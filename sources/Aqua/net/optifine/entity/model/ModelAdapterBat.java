package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBat;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderBat;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityBat;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorFields;

public class ModelAdapterBat
extends ModelAdapter {
    public ModelAdapterBat() {
        super(EntityBat.class, "bat", 0.25f);
    }

    public ModelBase makeModel() {
        return new ModelBat();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelBat)) {
            return null;
        }
        ModelBat modelbat = (ModelBat)model;
        return modelPart.equals((Object)"head") ? (ModelRenderer)Reflector.getFieldValue((Object)modelbat, (ReflectorFields)Reflector.ModelBat_ModelRenderers, (int)0) : (modelPart.equals((Object)"body") ? (ModelRenderer)Reflector.getFieldValue((Object)modelbat, (ReflectorFields)Reflector.ModelBat_ModelRenderers, (int)1) : (modelPart.equals((Object)"right_wing") ? (ModelRenderer)Reflector.getFieldValue((Object)modelbat, (ReflectorFields)Reflector.ModelBat_ModelRenderers, (int)2) : (modelPart.equals((Object)"left_wing") ? (ModelRenderer)Reflector.getFieldValue((Object)modelbat, (ReflectorFields)Reflector.ModelBat_ModelRenderers, (int)3) : (modelPart.equals((Object)"outer_right_wing") ? (ModelRenderer)Reflector.getFieldValue((Object)modelbat, (ReflectorFields)Reflector.ModelBat_ModelRenderers, (int)4) : (modelPart.equals((Object)"outer_left_wing") ? (ModelRenderer)Reflector.getFieldValue((Object)modelbat, (ReflectorFields)Reflector.ModelBat_ModelRenderers, (int)5) : null)))));
    }

    public String[] getModelRendererNames() {
        return new String[]{"head", "body", "right_wing", "left_wing", "outer_right_wing", "outer_left_wing"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderBat renderbat = new RenderBat(rendermanager);
        renderbat.mainModel = modelBase;
        renderbat.shadowSize = shadowSize;
        return renderbat;
    }
}
