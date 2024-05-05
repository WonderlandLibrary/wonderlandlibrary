package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelWither;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWither;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.src.Config;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorField;

public class ModelAdapterWither
extends ModelAdapter {
    public ModelAdapterWither() {
        super(EntityWither.class, "wither", 0.5f);
    }

    public ModelBase makeModel() {
        return new ModelWither(0.0f);
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelWither)) {
            return null;
        }
        ModelWither modelwither = (ModelWither)model;
        String s = "body";
        if (modelPart.startsWith(s)) {
            ModelRenderer[] amodelrenderer1 = (ModelRenderer[])Reflector.getFieldValue((Object)modelwither, (ReflectorField)Reflector.ModelWither_bodyParts);
            if (amodelrenderer1 == null) {
                return null;
            }
            String s3 = modelPart.substring(s.length());
            int j = Config.parseInt((String)s3, (int)-1);
            return --j >= 0 && j < amodelrenderer1.length ? amodelrenderer1[j] : null;
        }
        String s1 = "head";
        if (modelPart.startsWith(s1)) {
            ModelRenderer[] amodelrenderer = (ModelRenderer[])Reflector.getFieldValue((Object)modelwither, (ReflectorField)Reflector.ModelWither_heads);
            if (amodelrenderer == null) {
                return null;
            }
            String s2 = modelPart.substring(s1.length());
            int i = Config.parseInt((String)s2, (int)-1);
            return --i >= 0 && i < amodelrenderer.length ? amodelrenderer[i] : null;
        }
        return null;
    }

    public String[] getModelRendererNames() {
        return new String[]{"body1", "body2", "body3", "head1", "head2", "head3"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderWither renderwither = new RenderWither(rendermanager);
        renderwither.mainModel = modelBase;
        renderwither.shadowSize = shadowSize;
        return renderwither;
    }
}
