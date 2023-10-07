// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderMite;
import net.minecraft.util.ResourceLocation;

public class RenderEndermite extends RenderLiving
{
    private static final ResourceLocation field_177108_a;
    private static final String __OBFID = "CL_00002445";
    
    public RenderEndermite(final RenderManager p_i46181_1_) {
        super(p_i46181_1_, new ModelEnderMite(), 0.3f);
    }
    
    protected float func_177107_a(final EntityEndermite p_177107_1_) {
        return 180.0f;
    }
    
    protected ResourceLocation func_177106_b(final EntityEndermite p_177106_1_) {
        return RenderEndermite.field_177108_a;
    }
    
    @Override
    protected float getDeathMaxRotation(final EntityLivingBase p_77037_1_) {
        return this.func_177107_a((EntityEndermite)p_77037_1_);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.func_177106_b((EntityEndermite)p_110775_1_);
    }
    
    static {
        field_177108_a = new ResourceLocation("textures/entity/endermite.png");
    }
}
