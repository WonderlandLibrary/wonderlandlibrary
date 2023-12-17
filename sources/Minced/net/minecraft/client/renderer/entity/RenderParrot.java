// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelParrot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.passive.EntityParrot;

public class RenderParrot extends RenderLiving<EntityParrot>
{
    public static final ResourceLocation[] PARROT_TEXTURES;
    
    public RenderParrot(final RenderManager p_i47375_1_) {
        super(p_i47375_1_, new ModelParrot(), 0.3f);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityParrot entity) {
        return RenderParrot.PARROT_TEXTURES[entity.getVariant()];
    }
    
    public float handleRotationFloat(final EntityParrot livingBase, final float partialTicks) {
        return this.getCustomBob(livingBase, partialTicks);
    }
    
    private float getCustomBob(final EntityParrot parrot, final float p_192861_2_) {
        final float f = parrot.oFlap + (parrot.flap - parrot.oFlap) * p_192861_2_;
        final float f2 = parrot.oFlapSpeed + (parrot.flapSpeed - parrot.oFlapSpeed) * p_192861_2_;
        return (MathHelper.sin(f) + 1.0f) * f2;
    }
    
    static {
        PARROT_TEXTURES = new ResourceLocation[] { new ResourceLocation("textures/entity/parrot/parrot_red_blue.png"), new ResourceLocation("textures/entity/parrot/parrot_blue.png"), new ResourceLocation("textures/entity/parrot/parrot_green.png"), new ResourceLocation("textures/entity/parrot/parrot_yellow_blue.png"), new ResourceLocation("textures/entity/parrot/parrot_grey.png") };
    }
}
