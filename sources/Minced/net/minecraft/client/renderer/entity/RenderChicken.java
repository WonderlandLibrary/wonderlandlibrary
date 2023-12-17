// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.passive.EntityChicken;

public class RenderChicken extends RenderLiving<EntityChicken>
{
    private static final ResourceLocation CHICKEN_TEXTURES;
    
    public RenderChicken(final RenderManager p_i47211_1_) {
        super(p_i47211_1_, new ModelChicken(), 0.3f);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityChicken entity) {
        return RenderChicken.CHICKEN_TEXTURES;
    }
    
    @Override
    protected float handleRotationFloat(final EntityChicken livingBase, final float partialTicks) {
        final float f = livingBase.oFlap + (livingBase.wingRotation - livingBase.oFlap) * partialTicks;
        final float f2 = livingBase.oFlapSpeed + (livingBase.destPos - livingBase.oFlapSpeed) * partialTicks;
        return (MathHelper.sin(f) + 1.0f) * f2;
    }
    
    static {
        CHICKEN_TEXTURES = new ResourceLocation("textures/entity/chicken.png");
    }
}
