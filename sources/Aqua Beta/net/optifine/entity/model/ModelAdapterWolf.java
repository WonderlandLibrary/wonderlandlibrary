package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.passive.EntityWolf;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorField;

public class ModelAdapterWolf
extends ModelAdapter {
    public ModelAdapterWolf() {
        super(EntityWolf.class, "wolf", 0.5f);
    }

    public ModelBase makeModel() {
        return new ModelWolf();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelWolf)) {
            return null;
        }
        ModelWolf modelwolf = (ModelWolf)model;
        return modelPart.equals((Object)"head") ? modelwolf.wolfHeadMain : (modelPart.equals((Object)"body") ? modelwolf.wolfBody : (modelPart.equals((Object)"leg1") ? modelwolf.wolfLeg1 : (modelPart.equals((Object)"leg2") ? modelwolf.wolfLeg2 : (modelPart.equals((Object)"leg3") ? modelwolf.wolfLeg3 : (modelPart.equals((Object)"leg4") ? modelwolf.wolfLeg4 : (modelPart.equals((Object)"tail") ? (ModelRenderer)Reflector.getFieldValue((Object)modelwolf, (ReflectorField)Reflector.ModelWolf_tail) : (modelPart.equals((Object)"mane") ? (ModelRenderer)Reflector.getFieldValue((Object)modelwolf, (ReflectorField)Reflector.ModelWolf_mane) : null)))))));
    }

    public String[] getModelRendererNames() {
        return new String[]{"head", "body", "leg1", "leg2", "leg3", "leg4", "tail", "mane"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderWolf renderwolf = new RenderWolf(rendermanager, modelBase, shadowSize);
        return renderwolf;
    }
}
