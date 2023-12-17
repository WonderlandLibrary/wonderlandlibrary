// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.monster.EntitySpider;

public class ModelAdapterSpider extends ModelAdapter
{
    public ModelAdapterSpider() {
        super(EntitySpider.class, "spider", 1.0f);
    }
    
    protected ModelAdapterSpider(final Class entityClass, final String name, final float shadowSize) {
        super(entityClass, name, shadowSize);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelSpider();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelSpider)) {
            return null;
        }
        final ModelSpider modelspider = (ModelSpider)model;
        if (modelPart.equals("head")) {
            return modelspider.spiderHead;
        }
        if (modelPart.equals("neck")) {
            return modelspider.spiderNeck;
        }
        if (modelPart.equals("body")) {
            return modelspider.spiderBody;
        }
        if (modelPart.equals("leg1")) {
            return modelspider.spiderLeg1;
        }
        if (modelPart.equals("leg2")) {
            return modelspider.spiderLeg2;
        }
        if (modelPart.equals("leg3")) {
            return modelspider.spiderLeg3;
        }
        if (modelPart.equals("leg4")) {
            return modelspider.spiderLeg4;
        }
        if (modelPart.equals("leg5")) {
            return modelspider.spiderLeg5;
        }
        if (modelPart.equals("leg6")) {
            return modelspider.spiderLeg6;
        }
        if (modelPart.equals("leg7")) {
            return modelspider.spiderLeg7;
        }
        return modelPart.equals("leg8") ? modelspider.spiderLeg8 : null;
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "head", "neck", "body", "leg1", "leg2", "leg3", "leg4", "leg5", "leg6", "leg7", "leg8" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderSpider renderspider = new RenderSpider(rendermanager);
        renderspider.mainModel = modelBase;
        renderspider.shadowSize = shadowSize;
        return renderspider;
    }
}
