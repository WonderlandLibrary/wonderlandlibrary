// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelSlime extends ModelBase
{
    ModelRenderer slimeBodies;
    ModelRenderer slimeRightEye;
    ModelRenderer slimeLeftEye;
    ModelRenderer slimeMouth;
    
    public ModelSlime(final int slimeBodyTexOffY) {
        if (slimeBodyTexOffY > 0) {
            (this.slimeBodies = new ModelRenderer(this, 0, slimeBodyTexOffY)).addBox(-3.0f, 17.0f, -3.0f, 6, 6, 6);
            (this.slimeRightEye = new ModelRenderer(this, 32, 0)).addBox(-3.25f, 18.0f, -3.5f, 2, 2, 2);
            (this.slimeLeftEye = new ModelRenderer(this, 32, 4)).addBox(1.25f, 18.0f, -3.5f, 2, 2, 2);
            (this.slimeMouth = new ModelRenderer(this, 32, 8)).addBox(0.0f, 21.0f, -3.5f, 1, 1, 1);
        }
        else {
            (this.slimeBodies = new ModelRenderer(this, 0, slimeBodyTexOffY)).addBox(-4.0f, 16.0f, -4.0f, 8, 8, 8);
        }
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        GlStateManager.translate(0.0f, 0.001f, 0.0f);
        this.slimeBodies.render(scale);
        if (this.slimeRightEye != null) {
            this.slimeRightEye.render(scale);
            this.slimeLeftEye.render(scale);
            this.slimeMouth.render(scale);
        }
    }
}
