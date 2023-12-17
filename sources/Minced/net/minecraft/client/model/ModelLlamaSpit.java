// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelLlamaSpit extends ModelBase
{
    private final ModelRenderer main;
    
    public ModelLlamaSpit() {
        this(0.0f);
    }
    
    public ModelLlamaSpit(final float p_i47225_1_) {
        this.main = new ModelRenderer(this);
        final int i = 2;
        this.main.setTextureOffset(0, 0).addBox(-4.0f, 0.0f, 0.0f, 2, 2, 2, p_i47225_1_);
        this.main.setTextureOffset(0, 0).addBox(0.0f, -4.0f, 0.0f, 2, 2, 2, p_i47225_1_);
        this.main.setTextureOffset(0, 0).addBox(0.0f, 0.0f, -4.0f, 2, 2, 2, p_i47225_1_);
        this.main.setTextureOffset(0, 0).addBox(0.0f, 0.0f, 0.0f, 2, 2, 2, p_i47225_1_);
        this.main.setTextureOffset(0, 0).addBox(2.0f, 0.0f, 0.0f, 2, 2, 2, p_i47225_1_);
        this.main.setTextureOffset(0, 0).addBox(0.0f, 2.0f, 0.0f, 2, 2, 2, p_i47225_1_);
        this.main.setTextureOffset(0, 0).addBox(0.0f, 0.0f, 2.0f, 2, 2, 2, p_i47225_1_);
        this.main.setRotationPoint(0.0f, 0.0f, 0.0f);
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.main.render(scale);
    }
}
