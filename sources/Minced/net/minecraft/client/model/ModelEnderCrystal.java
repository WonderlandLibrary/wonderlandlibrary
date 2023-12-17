// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelEnderCrystal extends ModelBase
{
    private final ModelRenderer cube;
    private final ModelRenderer glass;
    private ModelRenderer base;
    
    public ModelEnderCrystal(final float p_i1170_1_, final boolean renderBase) {
        this.glass = new ModelRenderer(this, "glass");
        this.glass.setTextureOffset(0, 0).addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
        this.cube = new ModelRenderer(this, "cube");
        this.cube.setTextureOffset(32, 0).addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
        if (renderBase) {
            this.base = new ModelRenderer(this, "base");
            this.base.setTextureOffset(0, 16).addBox(-6.0f, 0.0f, -6.0f, 12, 4, 12);
        }
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.translate(0.0f, -0.5f, 0.0f);
        if (this.base != null) {
            this.base.render(scale);
        }
        GlStateManager.rotate(limbSwingAmount, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.8f + ageInTicks, 0.0f);
        GlStateManager.rotate(60.0f, 0.7071f, 0.0f, 0.7071f);
        this.glass.render(scale);
        final float f = 0.875f;
        GlStateManager.scale(0.875f, 0.875f, 0.875f);
        GlStateManager.rotate(60.0f, 0.7071f, 0.0f, 0.7071f);
        GlStateManager.rotate(limbSwingAmount, 0.0f, 1.0f, 0.0f);
        this.glass.render(scale);
        GlStateManager.scale(0.875f, 0.875f, 0.875f);
        GlStateManager.rotate(60.0f, 0.7071f, 0.0f, 0.7071f);
        GlStateManager.rotate(limbSwingAmount, 0.0f, 1.0f, 0.0f);
        this.cube.render(scale);
        GlStateManager.popMatrix();
    }
}
