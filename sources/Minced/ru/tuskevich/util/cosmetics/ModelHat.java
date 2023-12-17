// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.cosmetics;

import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.model.ModelRenderer;

class ModelHat extends CosmeticModelBase
{
    private ModelRenderer rim;
    private ModelRenderer pointy;
    private ModelRenderer p1;
    private ModelRenderer p2;
    private ModelRenderer p3;
    private ModelRenderer p4;
    private ModelRenderer p5;
    private ModelRenderer p6;
    
    public ModelHat(final RenderPlayer player) {
        super(player);
        (this.p6 = new ModelRenderer(this.playerModel)).addBox(-19.5f, -15.0f, -4.5f, 7, 3, 6);
        (this.p3 = new ModelRenderer(this.playerModel)).addBox(-19.5f, -17.0f, -4.0f, 7, 2, 6);
        (this.p1 = new ModelRenderer(this.playerModel)).addBox(-20.5f, -16.0f, -4.0f, 9, 10, 6);
        (this.p2 = new ModelRenderer(this.playerModel)).addBox(-20.5f, -6.0f, -4.0f, 3, 4, 6);
        (this.p4 = new ModelRenderer(this.playerModel)).addBox(-14.5f, -6.0f, -4.0f, 3, 4, 6);
        (this.p5 = new ModelRenderer(this.playerModel)).addBox(-19.0f, -15.0f, -2.0f, 6, 8, 6);
    }
    
    public void render2(final Entity entityIn, final float climbSwing, final float limbSwingAmount, final float ageInTicks, final float headYaw, final float headPitch, final float scale) {
        this.p6.rotationPointX = 0.0f;
        this.p6.rotationPointY = 0.0f;
        this.p6.render(scale);
    }
    
    @Override
    public void render(final Entity entityIn, final float climbSwing, final float limbSwingAmount, final float ageInTicks, final float headYaw, final float headPitch, final float scale) {
        this.p3.rotationPointX = 0.0f;
        this.p3.rotationPointY = 0.0f;
        this.p3.render(scale);
        this.p1.rotationPointX = 0.0f;
        this.p1.rotationPointY = 0.0f;
        this.p1.render(scale);
        this.p2.rotationPointX = 0.0f;
        this.p2.rotationPointY = 0.0f;
        this.p2.render(scale);
        this.p4.rotationPointX = 0.0f;
        this.p4.rotationPointY = 0.0f;
        this.p4.render(scale);
        this.p5.rotationPointX = 0.0f;
        this.p5.rotationPointY = 0.0f;
        this.p5.render(scale);
    }
}
