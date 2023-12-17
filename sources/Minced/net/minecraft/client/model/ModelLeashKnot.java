// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelLeashKnot extends ModelBase
{
    public ModelRenderer knotRenderer;
    
    public ModelLeashKnot() {
        this(0, 0, 32, 32);
    }
    
    public ModelLeashKnot(final int p_i46365_1_, final int p_i46365_2_, final int p_i46365_3_, final int p_i46365_4_) {
        this.textureWidth = p_i46365_3_;
        this.textureHeight = p_i46365_4_;
        (this.knotRenderer = new ModelRenderer(this, p_i46365_1_, p_i46365_2_)).addBox(-3.0f, -6.0f, -3.0f, 6, 8, 6, 0.0f);
        this.knotRenderer.setRotationPoint(0.0f, 0.0f, 0.0f);
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.knotRenderer.render(scale);
    }
    
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        this.knotRenderer.rotateAngleY = netHeadYaw * 0.017453292f;
        this.knotRenderer.rotateAngleX = headPitch * 0.017453292f;
    }
}
