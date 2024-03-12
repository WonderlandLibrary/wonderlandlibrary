// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.passive.EntitySheep;

public class ModelAdapterSheep extends ModelAdapterQuadruped
{
    public ModelAdapterSheep() {
        super(EntitySheep.class, "sheep", 0.7f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelSheep2();
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        return new RenderSheep(rendermanager, modelBase, shadowSize);
    }
}
