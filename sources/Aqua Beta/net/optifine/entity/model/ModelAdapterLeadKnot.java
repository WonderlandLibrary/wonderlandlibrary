package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelLeashKnot;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderLeashKnot;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.src.Config;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorField;

public class ModelAdapterLeadKnot
extends ModelAdapter {
    public ModelAdapterLeadKnot() {
        super(EntityLeashKnot.class, "lead_knot", 0.0f);
    }

    public ModelBase makeModel() {
        return new ModelLeashKnot();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelLeashKnot)) {
            return null;
        }
        ModelLeashKnot modelleashknot = (ModelLeashKnot)model;
        return modelPart.equals((Object)"knot") ? modelleashknot.field_110723_a : null;
    }

    public String[] getModelRendererNames() {
        return new String[]{"knot"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderLeashKnot renderleashknot = new RenderLeashKnot(rendermanager);
        if (!Reflector.RenderLeashKnot_leashKnotModel.exists()) {
            Config.warn((String)"Field not found: RenderLeashKnot.leashKnotModel");
            return null;
        }
        Reflector.setFieldValue((Object)renderleashknot, (ReflectorField)Reflector.RenderLeashKnot_leashKnotModel, (Object)modelBase);
        renderleashknot.shadowSize = shadowSize;
        return renderleashknot;
    }
}
