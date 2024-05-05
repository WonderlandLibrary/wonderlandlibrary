package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelEnderCrystal
extends ModelBase {
    private ModelRenderer cube;
    private ModelRenderer glass = new ModelRenderer((ModelBase)this, "glass");
    private ModelRenderer base;

    public ModelEnderCrystal(float p_i1170_1_, boolean p_i1170_2_) {
        this.glass.setTextureOffset(0, 0).addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
        this.cube = new ModelRenderer((ModelBase)this, "cube");
        this.cube.setTextureOffset(32, 0).addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
        if (p_i1170_2_) {
            this.base = new ModelRenderer((ModelBase)this, "base");
            this.base.setTextureOffset(0, 16).addBox(-6.0f, 0.0f, -6.0f, 12, 4, 12);
        }
    }

    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.scale((float)2.0f, (float)2.0f, (float)2.0f);
        GlStateManager.translate((float)0.0f, (float)-0.5f, (float)0.0f);
        if (this.base != null) {
            this.base.render(scale);
        }
        GlStateManager.rotate((float)p_78088_3_, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.translate((float)0.0f, (float)(0.8f + p_78088_4_), (float)0.0f);
        GlStateManager.rotate((float)60.0f, (float)0.7071f, (float)0.0f, (float)0.7071f);
        this.glass.render(scale);
        float f = 0.875f;
        GlStateManager.scale((float)f, (float)f, (float)f);
        GlStateManager.rotate((float)60.0f, (float)0.7071f, (float)0.0f, (float)0.7071f);
        GlStateManager.rotate((float)p_78088_3_, (float)0.0f, (float)1.0f, (float)0.0f);
        this.glass.render(scale);
        GlStateManager.scale((float)f, (float)f, (float)f);
        GlStateManager.rotate((float)60.0f, (float)0.7071f, (float)0.0f, (float)0.7071f);
        GlStateManager.rotate((float)p_78088_3_, (float)0.0f, (float)1.0f, (float)0.0f);
        this.cube.render(scale);
        GlStateManager.popMatrix();
    }
}
