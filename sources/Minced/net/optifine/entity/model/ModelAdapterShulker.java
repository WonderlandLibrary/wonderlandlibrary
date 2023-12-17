// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderShulker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.monster.EntityShulker;

public class ModelAdapterShulker extends ModelAdapter
{
    public ModelAdapterShulker() {
        super(EntityShulker.class, "shulker", 0.0f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelShulker();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelShulker)) {
            return null;
        }
        final ModelShulker modelshulker = (ModelShulker)model;
        if (modelPart.equals("head")) {
            return modelshulker.head;
        }
        if (modelPart.equals("base")) {
            return modelshulker.base;
        }
        return modelPart.equals("lid") ? modelshulker.lid : null;
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "base", "lid", "head" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderShulker rendershulker = new RenderShulker(rendermanager);
        rendershulker.mainModel = modelBase;
        rendershulker.shadowSize = shadowSize;
        return rendershulker;
    }
}
