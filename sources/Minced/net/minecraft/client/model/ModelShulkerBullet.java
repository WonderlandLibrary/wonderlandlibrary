// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelShulkerBullet extends ModelBase
{
    public ModelRenderer renderer;
    
    public ModelShulkerBullet() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.renderer = new ModelRenderer(this);
        this.renderer.setTextureOffset(0, 0).addBox(-4.0f, -4.0f, -1.0f, 8, 8, 2, 0.0f);
        this.renderer.setTextureOffset(0, 10).addBox(-1.0f, -4.0f, -4.0f, 2, 8, 8, 0.0f);
        this.renderer.setTextureOffset(20, 0).addBox(-4.0f, -1.0f, -4.0f, 8, 2, 8, 0.0f);
        this.renderer.setRotationPoint(0.0f, 0.0f, 0.0f);
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.renderer.render(scale);
    }
    
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        this.renderer.rotateAngleY = netHeadYaw * 0.017453292f;
        this.renderer.rotateAngleX = headPitch * 0.017453292f;
    }
}
