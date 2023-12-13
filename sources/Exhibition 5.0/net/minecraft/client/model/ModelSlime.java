// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelSlime extends ModelBase
{
    ModelRenderer slimeBodies;
    ModelRenderer slimeRightEye;
    ModelRenderer slimeLeftEye;
    ModelRenderer slimeMouth;
    private static final String __OBFID = "CL_00000858";
    
    public ModelSlime(final int p_i1157_1_) {
        (this.slimeBodies = new ModelRenderer(this, 0, p_i1157_1_)).addBox(-4.0f, 16.0f, -4.0f, 8, 8, 8);
        if (p_i1157_1_ > 0) {
            (this.slimeBodies = new ModelRenderer(this, 0, p_i1157_1_)).addBox(-3.0f, 17.0f, -3.0f, 6, 6, 6);
            (this.slimeRightEye = new ModelRenderer(this, 32, 0)).addBox(-3.25f, 18.0f, -3.5f, 2, 2, 2);
            (this.slimeLeftEye = new ModelRenderer(this, 32, 4)).addBox(1.25f, 18.0f, -3.5f, 2, 2, 2);
            (this.slimeMouth = new ModelRenderer(this, 32, 8)).addBox(0.0f, 21.0f, -3.5f, 1, 1, 1);
        }
    }
    
    @Override
    public void render(final Entity p_78088_1_, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        this.slimeBodies.render(p_78088_7_);
        if (this.slimeRightEye != null) {
            this.slimeRightEye.render(p_78088_7_);
            this.slimeLeftEye.render(p_78088_7_);
            this.slimeMouth.render(p_78088_7_);
        }
    }
}
