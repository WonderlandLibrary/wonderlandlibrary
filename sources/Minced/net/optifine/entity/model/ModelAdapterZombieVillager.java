// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderZombieVillager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.monster.EntityZombieVillager;

public class ModelAdapterZombieVillager extends ModelAdapterBiped
{
    public ModelAdapterZombieVillager() {
        super(EntityZombieVillager.class, "zombie_villager", 0.5f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelZombieVillager();
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderZombieVillager renderzombievillager = new RenderZombieVillager(rendermanager);
        renderzombievillager.mainModel = modelBase;
        renderzombievillager.shadowSize = shadowSize;
        return renderzombievillager;
    }
}
