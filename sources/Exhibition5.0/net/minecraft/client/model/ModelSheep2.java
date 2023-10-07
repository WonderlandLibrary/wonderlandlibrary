// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.EntityLivingBase;

public class ModelSheep2 extends ModelQuadruped
{
    private float field_78153_i;
    private static final String __OBFID = "CL_00000853";
    
    public ModelSheep2() {
        super(12, 0.0f);
        (this.head = new ModelRenderer(this, 0, 0)).addBox(-3.0f, -4.0f, -6.0f, 6, 6, 8, 0.0f);
        this.head.setRotationPoint(0.0f, 6.0f, -8.0f);
        (this.body = new ModelRenderer(this, 28, 8)).addBox(-4.0f, -10.0f, -7.0f, 8, 16, 6, 0.0f);
        this.body.setRotationPoint(0.0f, 5.0f, 2.0f);
    }
    
    @Override
    public void setLivingAnimations(final EntityLivingBase p_78086_1_, final float p_78086_2_, final float p_78086_3_, final float p_78086_4_) {
        super.setLivingAnimations(p_78086_1_, p_78086_2_, p_78086_3_, p_78086_4_);
        this.head.rotationPointY = 6.0f + ((EntitySheep)p_78086_1_).getHeadRotationPointY(p_78086_4_) * 9.0f;
        this.field_78153_i = ((EntitySheep)p_78086_1_).getHeadRotationAngleX(p_78086_4_);
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity p_78087_7_) {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
        this.head.rotateAngleX = this.field_78153_i;
    }
}
