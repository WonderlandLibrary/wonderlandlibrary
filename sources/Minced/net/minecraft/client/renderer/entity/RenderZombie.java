// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityZombie;

public class RenderZombie extends RenderBiped<EntityZombie>
{
    private static final ResourceLocation ZOMBIE_TEXTURES;
    
    public RenderZombie(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelZombie(), 0.5f);
        final LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this) {
            @Override
            protected void initArmor() {
                this.modelLeggings = (T)new ModelZombie(0.5f, true);
                this.modelArmor = (T)new ModelZombie(1.0f, true);
            }
        };
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(layerbipedarmor);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityZombie entity) {
        return RenderZombie.ZOMBIE_TEXTURES;
    }
    
    static {
        ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");
    }
}
