// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelVex;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityVex;

public class RenderVex extends RenderBiped<EntityVex>
{
    private static final ResourceLocation VEX_TEXTURE;
    private static final ResourceLocation VEX_CHARGING_TEXTURE;
    private int modelVersion;
    
    public RenderVex(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelVex(), 0.3f);
        this.modelVersion = ((ModelVex)this.mainModel).getModelVersion();
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityVex entity) {
        return entity.isCharging() ? RenderVex.VEX_CHARGING_TEXTURE : RenderVex.VEX_TEXTURE;
    }
    
    @Override
    public void doRender(final EntityVex entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        final int i = ((ModelVex)this.mainModel).getModelVersion();
        if (i != this.modelVersion) {
            this.mainModel = new ModelVex();
            this.modelVersion = i;
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected void preRenderCallback(final EntityVex entitylivingbaseIn, final float partialTickTime) {
        GlStateManager.scale(0.4f, 0.4f, 0.4f);
    }
    
    static {
        VEX_TEXTURE = new ResourceLocation("textures/entity/illager/vex.png");
        VEX_CHARGING_TEXTURE = new ResourceLocation("textures/entity/illager/vex_charging.png");
    }
}
