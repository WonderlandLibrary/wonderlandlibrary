// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;
import net.minecraft.client.renderer.entity.RenderShulkerBullet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelShulkerBullet;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.projectile.EntityShulkerBullet;

public class ModelAdapterShulkerBullet extends ModelAdapter
{
    public ModelAdapterShulkerBullet() {
        super(EntityShulkerBullet.class, "shulker_bullet", 0.0f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelShulkerBullet();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelShulkerBullet)) {
            return null;
        }
        final ModelShulkerBullet modelshulkerbullet = (ModelShulkerBullet)model;
        return modelPart.equals("bullet") ? modelshulkerbullet.renderer : null;
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "bullet" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderShulkerBullet rendershulkerbullet = new RenderShulkerBullet(rendermanager);
        if (!Reflector.RenderShulkerBullet_model.exists()) {
            Config.warn("Field not found: RenderShulkerBullet.model");
            return null;
        }
        Reflector.setFieldValue(rendershulkerbullet, Reflector.RenderShulkerBullet_model, modelBase);
        rendershulkerbullet.shadowSize = shadowSize;
        return rendershulkerbullet;
    }
}
