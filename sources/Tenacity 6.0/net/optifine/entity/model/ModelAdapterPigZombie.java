// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPigZombie;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.monster.EntityPigZombie;

public class ModelAdapterPigZombie extends ModelAdapterBiped
{
    public ModelAdapterPigZombie() {
        super(EntityPigZombie.class, "zombie_pigman", 0.5f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelZombie();
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderPigZombie renderpigzombie = new RenderPigZombie(rendermanager);
        Render.setModelBipedMain(renderpigzombie, (ModelBiped)modelBase);
        renderpigzombie.mainModel = modelBase;
        renderpigzombie.shadowSize = shadowSize;
        return renderpigzombie;
    }
}
