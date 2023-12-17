// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import java.util.Random;

public class ModelGhast extends ModelBase
{
    ModelRenderer body;
    ModelRenderer[] tentacles;
    
    public ModelGhast() {
        this.tentacles = new ModelRenderer[9];
        final int i = -16;
        (this.body = new ModelRenderer(this, 0, 0)).addBox(-8.0f, -8.0f, -8.0f, 16, 16, 16);
        final ModelRenderer body = this.body;
        body.rotationPointY += 8.0f;
        final Random random = new Random(1660L);
        for (int j = 0; j < this.tentacles.length; ++j) {
            this.tentacles[j] = new ModelRenderer(this, 0, 0);
            final float f = ((j % 3 - j / 3 % 2 * 0.5f + 0.25f) / 2.0f * 2.0f - 1.0f) * 5.0f;
            final float f2 = (j / 3 / 2.0f * 2.0f - 1.0f) * 5.0f;
            final int k = random.nextInt(7) + 8;
            this.tentacles[j].addBox(-1.0f, 0.0f, -1.0f, 2, k, 2);
            this.tentacles[j].rotationPointX = f;
            this.tentacles[j].rotationPointZ = f2;
            this.tentacles[j].rotationPointY = 15.0f;
        }
    }
    
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        for (int i = 0; i < this.tentacles.length; ++i) {
            this.tentacles[i].rotateAngleX = 0.2f * MathHelper.sin(ageInTicks * 0.3f + i) + 0.4f;
        }
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, 0.6f, 0.0f);
        this.body.render(scale);
        for (final ModelRenderer modelrenderer : this.tentacles) {
            modelrenderer.render(scale);
        }
        GlStateManager.popMatrix();
    }
}
