// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.passive.EntityBat;

public class RenderBat extends RenderLiving<EntityBat>
{
    private static final ResourceLocation BAT_TEXTURES;
    
    public RenderBat(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelBat(), 0.25f);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityBat entity) {
        return RenderBat.BAT_TEXTURES;
    }
    
    @Override
    protected void preRenderCallback(final EntityBat entitylivingbaseIn, final float partialTickTime) {
        GlStateManager.scale(0.35f, 0.35f, 0.35f);
    }
    
    @Override
    protected void applyRotations(final EntityBat entityLiving, final float ageInTicks, final float rotationYaw, final float partialTicks) {
        if (entityLiving.getIsBatHanging()) {
            GlStateManager.translate(0.0f, -0.1f, 0.0f);
        }
        else {
            GlStateManager.translate(0.0f, MathHelper.cos(ageInTicks * 0.3f) * 0.1f, 0.0f);
        }
        super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
    }
    
    static {
        BAT_TEXTURES = new ResourceLocation("textures/entity/bat.png");
    }
}
