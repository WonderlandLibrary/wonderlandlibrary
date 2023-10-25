package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraft.entity.monster.EntitySlime;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorFields;

public class ModelAdapterSlime
extends ModelAdapter {
    public ModelAdapterSlime() {
        super(EntitySlime.class, "slime", 0.25f);
    }

    public ModelBase makeModel() {
        return new ModelSlime(16);
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelSlime)) {
            return null;
        }
        ModelSlime modelslime = (ModelSlime)model;
        return modelPart.equals((Object)"body") ? (ModelRenderer)Reflector.getFieldValue((Object)modelslime, (ReflectorFields)Reflector.ModelSlime_ModelRenderers, (int)0) : (modelPart.equals((Object)"left_eye") ? (ModelRenderer)Reflector.getFieldValue((Object)modelslime, (ReflectorFields)Reflector.ModelSlime_ModelRenderers, (int)1) : (modelPart.equals((Object)"right_eye") ? (ModelRenderer)Reflector.getFieldValue((Object)modelslime, (ReflectorFields)Reflector.ModelSlime_ModelRenderers, (int)2) : (modelPart.equals((Object)"mouth") ? (ModelRenderer)Reflector.getFieldValue((Object)modelslime, (ReflectorFields)Reflector.ModelSlime_ModelRenderers, (int)3) : null)));
    }

    public String[] getModelRendererNames() {
        return new String[]{"body", "left_eye", "right_eye", "mouth"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderSlime renderslime = new RenderSlime(rendermanager, modelBase, shadowSize);
        return renderslime;
    }
}
