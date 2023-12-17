// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderStray;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.monster.EntityStray;

public class ModelAdapterStray extends ModelAdapterBiped
{
    public ModelAdapterStray() {
        super(EntityStray.class, "stray", 0.7f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelSkeleton();
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderStray renderstray = new RenderStray(rendermanager);
        renderstray.mainModel = modelBase;
        renderstray.shadowSize = shadowSize;
        return renderstray;
    }
}
