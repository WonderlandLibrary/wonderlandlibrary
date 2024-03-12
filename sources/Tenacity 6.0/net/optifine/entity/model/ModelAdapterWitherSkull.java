// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;
import net.minecraft.client.renderer.tileentity.RenderWitherSkull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.projectile.EntityWitherSkull;

public class ModelAdapterWitherSkull extends ModelAdapter
{
    public ModelAdapterWitherSkull() {
        super(EntityWitherSkull.class, "wither_skull", 0.0f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelSkeletonHead();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelSkeletonHead)) {
            return null;
        }
        final ModelSkeletonHead modelskeletonhead = (ModelSkeletonHead)model;
        return modelPart.equals("head") ? modelskeletonhead.skeletonHead : null;
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "head" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderWitherSkull renderwitherskull = new RenderWitherSkull(rendermanager);
        if (!Reflector.RenderWitherSkull_model.exists()) {
            Config.warn("Field not found: RenderWitherSkull_model");
            return null;
        }
        Reflector.setFieldValue(renderwitherskull, Reflector.RenderWitherSkull_model, modelBase);
        renderwitherskull.shadowSize = shadowSize;
        return renderwitherskull;
    }
}
