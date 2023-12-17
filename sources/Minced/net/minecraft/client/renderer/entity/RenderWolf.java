// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerWolfCollar;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.passive.EntityWolf;

public class RenderWolf extends RenderLiving<EntityWolf>
{
    private static final ResourceLocation WOLF_TEXTURES;
    private static final ResourceLocation TAMED_WOLF_TEXTURES;
    private static final ResourceLocation ANRGY_WOLF_TEXTURES;
    
    public RenderWolf(final RenderManager p_i47187_1_) {
        super(p_i47187_1_, new ModelWolf(), 0.5f);
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerWolfCollar(this));
    }
    
    @Override
    protected float handleRotationFloat(final EntityWolf livingBase, final float partialTicks) {
        return livingBase.getTailRotation();
    }
    
    @Override
    public void doRender(final EntityWolf entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        if (entity.isWolfWet()) {
            final float f = entity.getBrightness() * entity.getShadingWhileWet(partialTicks);
            GlStateManager.color(f, f, f);
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityWolf entity) {
        if (entity.isTamed()) {
            return RenderWolf.TAMED_WOLF_TEXTURES;
        }
        return entity.isAngry() ? RenderWolf.ANRGY_WOLF_TEXTURES : RenderWolf.WOLF_TEXTURES;
    }
    
    static {
        WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf.png");
        TAMED_WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf_tame.png");
        ANRGY_WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf_angry.png");
    }
}
