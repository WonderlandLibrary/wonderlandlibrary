// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.EntityLivingBase;

public class ModelSheep1 extends ModelQuadruped
{
    private float headRotationAngleX;
    private static final String __OBFID = "CL_00000852";
    
    public ModelSheep1() {
        super(12, 0.0f);
        (this.head = new ModelRenderer(this, 0, 0)).addBox(-3.0f, -4.0f, -4.0f, 6, 6, 6, 0.6f);
        this.head.setRotationPoint(0.0f, 6.0f, -8.0f);
        (this.body = new ModelRenderer(this, 28, 8)).addBox(-4.0f, -10.0f, -7.0f, 8, 16, 6, 1.75f);
        this.body.setRotationPoint(0.0f, 5.0f, 2.0f);
        final float var1 = 0.5f;
        (this.leg1 = new ModelRenderer(this, 0, 16)).addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, var1);
        this.leg1.setRotationPoint(-3.0f, 12.0f, 7.0f);
        (this.leg2 = new ModelRenderer(this, 0, 16)).addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, var1);
        this.leg2.setRotationPoint(3.0f, 12.0f, 7.0f);
        (this.leg3 = new ModelRenderer(this, 0, 16)).addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, var1);
        this.leg3.setRotationPoint(-3.0f, 12.0f, -5.0f);
        (this.leg4 = new ModelRenderer(this, 0, 16)).addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, var1);
        this.leg4.setRotationPoint(3.0f, 12.0f, -5.0f);
    }
    
    @Override
    public void setLivingAnimations(final EntityLivingBase p_78086_1_, final float p_78086_2_, final float p_78086_3_, final float p_78086_4_) {
        super.setLivingAnimations(p_78086_1_, p_78086_2_, p_78086_3_, p_78086_4_);
        this.head.rotationPointY = 6.0f + ((EntitySheep)p_78086_1_).getHeadRotationPointY(p_78086_4_) * 9.0f;
        this.headRotationAngleX = ((EntitySheep)p_78086_1_).getHeadRotationAngleX(p_78086_4_);
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity p_78087_7_) {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
        this.head.rotateAngleX = this.headRotationAngleX;
    }
}
