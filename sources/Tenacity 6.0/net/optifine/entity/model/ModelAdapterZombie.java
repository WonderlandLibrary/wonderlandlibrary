// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.monster.EntityZombie;

public class ModelAdapterZombie extends ModelAdapterBiped
{
    public ModelAdapterZombie() {
        super(EntityZombie.class, "zombie", 0.5f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelZombie();
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderZombie renderzombie = new RenderZombie(rendermanager);
        Render.setModelBipedMain(renderzombie, (ModelBiped)modelBase);
        renderzombie.mainModel = modelBase;
        renderzombie.shadowSize = shadowSize;
        return renderzombie;
    }
}
