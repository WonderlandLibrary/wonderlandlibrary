// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.Entity;

public class ModelShulker extends ModelBase
{
    public final ModelRenderer base;
    public final ModelRenderer lid;
    public ModelRenderer head;
    
    public ModelShulker() {
        this.textureHeight = 64;
        this.textureWidth = 64;
        this.lid = new ModelRenderer(this);
        this.base = new ModelRenderer(this);
        this.head = new ModelRenderer(this);
        this.lid.setTextureOffset(0, 0).addBox(-8.0f, -16.0f, -8.0f, 16, 12, 16);
        this.lid.setRotationPoint(0.0f, 24.0f, 0.0f);
        this.base.setTextureOffset(0, 28).addBox(-8.0f, -8.0f, -8.0f, 16, 8, 16);
        this.base.setRotationPoint(0.0f, 24.0f, 0.0f);
        this.head.setTextureOffset(0, 52).addBox(-3.0f, 0.0f, -3.0f, 6, 6, 6);
        this.head.setRotationPoint(0.0f, 12.0f, 0.0f);
    }
    
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        final EntityShulker entityshulker = (EntityShulker)entityIn;
        final float f = ageInTicks - entityshulker.ticksExisted;
        final float f2 = (0.5f + entityshulker.getClientPeekAmount(f)) * 3.1415927f;
        final float f3 = -1.0f + MathHelper.sin(f2);
        float f4 = 0.0f;
        if (f2 > 3.1415927f) {
            f4 = MathHelper.sin(ageInTicks * 0.1f) * 0.7f;
        }
        this.lid.setRotationPoint(0.0f, 16.0f + MathHelper.sin(f2) * 8.0f + f4, 0.0f);
        if (entityshulker.getClientPeekAmount(f) > 0.3f) {
            this.lid.rotateAngleY = f3 * f3 * f3 * f3 * 3.1415927f * 0.125f;
        }
        else {
            this.lid.rotateAngleY = 0.0f;
        }
        this.head.rotateAngleX = headPitch * 0.017453292f;
        this.head.rotateAngleY = netHeadYaw * 0.017453292f;
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.base.render(scale);
        this.lid.render(scale);
    }
}
