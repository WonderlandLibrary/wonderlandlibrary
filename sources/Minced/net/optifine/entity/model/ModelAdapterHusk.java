// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderHusk;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.monster.EntityHusk;

public class ModelAdapterHusk extends ModelAdapterBiped
{
    public ModelAdapterHusk() {
        super(EntityHusk.class, "husk", 0.5f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelZombie();
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderHusk renderhusk = new RenderHusk(rendermanager);
        renderhusk.mainModel = modelBase;
        renderhusk.shadowSize = shadowSize;
        return renderhusk;
    }
}
