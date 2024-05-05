package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.RenderWitherSkull;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.src.Config;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorField;

public class ModelAdapterWitherSkull
extends ModelAdapter {
    public ModelAdapterWitherSkull() {
        super(EntityWitherSkull.class, "wither_skull", 0.0f);
    }

    public ModelBase makeModel() {
        return new ModelSkeletonHead();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelSkeletonHead)) {
            return null;
        }
        ModelSkeletonHead modelskeletonhead = (ModelSkeletonHead)model;
        return modelPart.equals((Object)"head") ? modelskeletonhead.skeletonHead : null;
    }

    public String[] getModelRendererNames() {
        return new String[]{"head"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderWitherSkull renderwitherskull = new RenderWitherSkull(rendermanager);
        if (!Reflector.RenderWitherSkull_model.exists()) {
            Config.warn((String)"Field not found: RenderWitherSkull_model");
            return null;
        }
        Reflector.setFieldValue((Object)renderwitherskull, (ReflectorField)Reflector.RenderWitherSkull_model, (Object)modelBase);
        renderwitherskull.shadowSize = shadowSize;
        return renderwitherskull;
    }
}
