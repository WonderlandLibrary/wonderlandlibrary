// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWitherSkeleton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.monster.EntityWitherSkeleton;

public class ModelAdapterWitherSkeleton extends ModelAdapterBiped
{
    public ModelAdapterWitherSkeleton() {
        super(EntityWitherSkeleton.class, "wither_skeleton", 0.7f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelSkeleton();
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderWitherSkeleton renderwitherskeleton = new RenderWitherSkeleton(rendermanager);
        renderwitherskeleton.mainModel = modelBase;
        renderwitherskeleton.shadowSize = shadowSize;
        return renderwitherskeleton;
    }
}
