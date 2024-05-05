package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelBlaze
extends ModelBase {
    private ModelRenderer[] blazeSticks = new ModelRenderer[12];
    private ModelRenderer blazeHead;

    public ModelBlaze() {
        for (int i = 0; i < this.blazeSticks.length; ++i) {
            this.blazeSticks[i] = new ModelRenderer((ModelBase)this, 0, 16);
            this.blazeSticks[i].addBox(0.0f, 0.0f, 0.0f, 2, 8, 2);
        }
        this.blazeHead = new ModelRenderer((ModelBase)this, 0, 0);
        this.blazeHead.addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
    }

    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        this.blazeHead.render(scale);
        for (int i = 0; i < this.blazeSticks.length; ++i) {
            this.blazeSticks[i].render(scale);
        }
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        float f = ageInTicks * (float)Math.PI * -0.1f;
        for (int i = 0; i < 4; ++i) {
            this.blazeSticks[i].rotationPointY = -2.0f + MathHelper.cos((float)(((float)(i * 2) + ageInTicks) * 0.25f));
            this.blazeSticks[i].rotationPointX = MathHelper.cos((float)f) * 9.0f;
            this.blazeSticks[i].rotationPointZ = MathHelper.sin((float)f) * 9.0f;
            f += 1.0f;
        }
        f = 0.7853982f + ageInTicks * (float)Math.PI * 0.03f;
        for (int j = 4; j < 8; ++j) {
            this.blazeSticks[j].rotationPointY = 2.0f + MathHelper.cos((float)(((float)(j * 2) + ageInTicks) * 0.25f));
            this.blazeSticks[j].rotationPointX = MathHelper.cos((float)f) * 7.0f;
            this.blazeSticks[j].rotationPointZ = MathHelper.sin((float)f) * 7.0f;
            f += 1.0f;
        }
        f = 0.47123894f + ageInTicks * (float)Math.PI * -0.05f;
        for (int k = 8; k < 12; ++k) {
            this.blazeSticks[k].rotationPointY = 11.0f + MathHelper.cos((float)(((float)k * 1.5f + ageInTicks) * 0.5f));
            this.blazeSticks[k].rotationPointX = MathHelper.cos((float)f) * 5.0f;
            this.blazeSticks[k].rotationPointZ = MathHelper.sin((float)f) * 5.0f;
            f += 1.0f;
        }
        this.blazeHead.rotateAngleY = netHeadYaw / 57.295776f;
        this.blazeHead.rotateAngleX = headPitch / 57.295776f;
    }
}
