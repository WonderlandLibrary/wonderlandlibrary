/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.model;

import java.util.Random;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelGhast
extends ModelBase {
    ModelRenderer body;
    ModelRenderer[] tentacles = new ModelRenderer[9];
    private static final String __OBFID = "CL_00000839";

    public ModelGhast() {
        int var1 = -16;
        this.body = new ModelRenderer(this, 0, 0);
        this.body.addBox(-8.0f, -8.0f, -8.0f, 16, 16, 16);
        this.body.rotationPointY += (float)(24 + var1);
        Random var2 = new Random(1660L);
        for (int var3 = 0; var3 < this.tentacles.length; ++var3) {
            this.tentacles[var3] = new ModelRenderer(this, 0, 0);
            float var4 = (((float)(var3 % 3) - (float)(var3 / 3 % 2) * 0.5f + 0.25f) / 2.0f * 2.0f - 1.0f) * 5.0f;
            float var5 = ((float)(var3 / 3) / 2.0f * 2.0f - 1.0f) * 5.0f;
            int var6 = var2.nextInt(7) + 8;
            this.tentacles[var3].addBox(-1.0f, 0.0f, -1.0f, 2, var6, 2);
            this.tentacles[var3].rotationPointX = var4;
            this.tentacles[var3].rotationPointZ = var5;
            this.tentacles[var3].rotationPointY = 31 + var1;
        }
    }

    @Override
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        for (int var8 = 0; var8 < this.tentacles.length; ++var8) {
            this.tentacles[var8].rotateAngleX = 0.2f * MathHelper.sin(p_78087_3_ * 0.3f + (float)var8) + 0.4f;
        }
    }

    @Override
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, 0.6f, 0.0f);
        this.body.render(p_78088_7_);
        for (ModelRenderer var11 : this.tentacles) {
            var11.render(p_78088_7_);
        }
        GlStateManager.popMatrix();
    }
}

