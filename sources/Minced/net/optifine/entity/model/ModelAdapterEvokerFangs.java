// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.src.Config;
import net.minecraft.client.renderer.entity.RenderEvokerFangs;
import net.minecraft.client.Minecraft;
import net.optifine.reflect.Reflector;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelEvokerFangs;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.projectile.EntityEvokerFangs;

public class ModelAdapterEvokerFangs extends ModelAdapter
{
    public ModelAdapterEvokerFangs() {
        super(EntityEvokerFangs.class, "evocation_fangs", 0.0f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelEvokerFangs();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelEvokerFangs)) {
            return null;
        }
        final ModelEvokerFangs modelevokerfangs = (ModelEvokerFangs)model;
        if (modelPart.equals("base")) {
            return (ModelRenderer)Reflector.getFieldValue(modelevokerfangs, Reflector.ModelEvokerFangs_ModelRenderers, 0);
        }
        if (modelPart.equals("upper_jaw")) {
            return (ModelRenderer)Reflector.getFieldValue(modelevokerfangs, Reflector.ModelEvokerFangs_ModelRenderers, 1);
        }
        return modelPart.equals("lower_jaw") ? ((ModelRenderer)Reflector.getFieldValue(modelevokerfangs, Reflector.ModelEvokerFangs_ModelRenderers, 2)) : null;
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "base", "upper_jaw", "lower_jaw" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderEvokerFangs renderevokerfangs = new RenderEvokerFangs(rendermanager);
        if (!Reflector.RenderEvokerFangs_model.exists()) {
            Config.warn("Field not found: RenderEvokerFangs.model");
            return null;
        }
        Reflector.setFieldValue(renderevokerfangs, Reflector.RenderEvokerFangs_model, modelBase);
        renderevokerfangs.shadowSize = shadowSize;
        return renderevokerfangs;
    }
}
