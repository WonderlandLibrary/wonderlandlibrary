// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.layers.LayerMooshroomMushroom;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCow;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.passive.EntityMooshroom;

public class RenderMooshroom extends RenderLiving<EntityMooshroom>
{
    private static final ResourceLocation MOOSHROOM_TEXTURES;
    
    public RenderMooshroom(final RenderManager p_i47200_1_) {
        super(p_i47200_1_, new ModelCow(), 0.7f);
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerMooshroomMushroom(this));
    }
    
    @Override
    public ModelCow getMainModel() {
        return (ModelCow)super.getMainModel();
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityMooshroom entity) {
        return RenderMooshroom.MOOSHROOM_TEXTURES;
    }
    
    static {
        MOOSHROOM_TEXTURES = new ResourceLocation("textures/entity/cow/mooshroom.png");
    }
}
