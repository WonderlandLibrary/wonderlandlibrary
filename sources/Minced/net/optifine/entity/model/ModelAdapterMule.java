// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderAbstractHorse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.passive.EntityMule;

public class ModelAdapterMule extends ModelAdapterHorse
{
    public ModelAdapterMule() {
        super(EntityMule.class, "mule", 0.75f);
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderAbstractHorse renderabstracthorse = new RenderAbstractHorse(rendermanager);
        renderabstracthorse.mainModel = modelBase;
        renderabstracthorse.shadowSize = shadowSize;
        return renderabstracthorse;
    }
}
