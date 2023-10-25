/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelEnderCrystal
extends ModelBase {
    private ModelRenderer cube;
    private ModelRenderer glass = new ModelRenderer(this, "glass");
    private ModelRenderer base;

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.translate(0.0f, -0.5f, 0.0f);
        if (this.base != null) {
            this.base.render(f6);
        }
        GlStateManager.rotate(f2, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.8f + f3, 0.0f);
        GlStateManager.rotate(60.0f, 0.7071f, 0.0f, 0.7071f);
        this.glass.render(f6);
        float f7 = 0.875f;
        GlStateManager.scale(f7, f7, f7);
        GlStateManager.rotate(60.0f, 0.7071f, 0.0f, 0.7071f);
        GlStateManager.rotate(f2, 0.0f, 1.0f, 0.0f);
        this.glass.render(f6);
        GlStateManager.scale(f7, f7, f7);
        GlStateManager.rotate(60.0f, 0.7071f, 0.0f, 0.7071f);
        GlStateManager.rotate(f2, 0.0f, 1.0f, 0.0f);
        this.cube.render(f6);
        GlStateManager.popMatrix();
    }

    public ModelEnderCrystal(float f, boolean bl) {
        this.glass.setTextureOffset(0, 0).addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
        this.cube = new ModelRenderer(this, "cube");
        this.cube.setTextureOffset(32, 0).addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
        if (bl) {
            this.base = new ModelRenderer(this, "base");
            this.base.setTextureOffset(0, 16).addBox(-6.0f, 0.0f, -6.0f, 12, 4, 12);
        }
    }
}

