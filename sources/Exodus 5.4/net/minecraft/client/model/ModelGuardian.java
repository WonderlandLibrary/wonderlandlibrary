/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class ModelGuardian
extends ModelBase {
    private ModelRenderer guardianBody;
    private ModelRenderer guardianEye;
    private ModelRenderer[] guardianTail;
    private ModelRenderer[] guardianSpines;

    public int func_178706_a() {
        return 54;
    }

    public ModelGuardian() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.guardianSpines = new ModelRenderer[12];
        this.guardianBody = new ModelRenderer(this);
        this.guardianBody.setTextureOffset(0, 0).addBox(-6.0f, 10.0f, -8.0f, 12, 12, 16);
        this.guardianBody.setTextureOffset(0, 28).addBox(-8.0f, 10.0f, -6.0f, 2, 12, 12);
        this.guardianBody.setTextureOffset(0, 28).addBox(6.0f, 10.0f, -6.0f, 2, 12, 12, true);
        this.guardianBody.setTextureOffset(16, 40).addBox(-6.0f, 8.0f, -6.0f, 12, 2, 12);
        this.guardianBody.setTextureOffset(16, 40).addBox(-6.0f, 22.0f, -6.0f, 12, 2, 12);
        int n = 0;
        while (n < this.guardianSpines.length) {
            this.guardianSpines[n] = new ModelRenderer(this, 0, 0);
            this.guardianSpines[n].addBox(-1.0f, -4.5f, -1.0f, 2, 9, 2);
            this.guardianBody.addChild(this.guardianSpines[n]);
            ++n;
        }
        this.guardianEye = new ModelRenderer(this, 8, 0);
        this.guardianEye.addBox(-1.0f, 15.0f, 0.0f, 2, 2, 1);
        this.guardianBody.addChild(this.guardianEye);
        this.guardianTail = new ModelRenderer[3];
        this.guardianTail[0] = new ModelRenderer(this, 40, 0);
        this.guardianTail[0].addBox(-2.0f, 14.0f, 7.0f, 4, 4, 8);
        this.guardianTail[1] = new ModelRenderer(this, 0, 54);
        this.guardianTail[1].addBox(0.0f, 14.0f, 0.0f, 3, 3, 7);
        this.guardianTail[2] = new ModelRenderer(this);
        this.guardianTail[2].setTextureOffset(41, 32).addBox(0.0f, 14.0f, 0.0f, 2, 2, 6);
        this.guardianTail[2].setTextureOffset(25, 19).addBox(1.0f, 10.5f, 3.0f, 1, 9, 9);
        this.guardianBody.addChild(this.guardianTail[0]);
        this.guardianTail[0].addChild(this.guardianTail[1]);
        this.guardianTail[1].addChild(this.guardianTail[2]);
    }

    @Override
    public void setRotationAngles(float f, float f2, float f3, float f4, float f5, float f6, Entity entity) {
        EntityGuardian entityGuardian = (EntityGuardian)entity;
        float f7 = f3 - (float)entityGuardian.ticksExisted;
        this.guardianBody.rotateAngleY = f4 / 57.295776f;
        this.guardianBody.rotateAngleX = f5 / 57.295776f;
        float[] fArray = new float[]{1.75f, 0.25f, 0.0f, 0.0f, 0.5f, 0.5f, 0.5f, 0.5f, 1.25f, 0.75f, 0.0f, 0.0f};
        float[] fArray2 = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.25f, 1.75f, 1.25f, 0.75f, 0.0f, 0.0f, 0.0f, 0.0f};
        float[] fArray3 = new float[]{0.0f, 0.0f, 0.25f, 1.75f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.75f, 1.25f};
        float[] fArray4 = new float[]{0.0f, 0.0f, 8.0f, -8.0f, -8.0f, 8.0f, 8.0f, -8.0f, 0.0f, 0.0f, 8.0f, -8.0f};
        float[] fArray5 = new float[]{-8.0f, -8.0f, -8.0f, -8.0f, 0.0f, 0.0f, 0.0f, 0.0f, 8.0f, 8.0f, 8.0f, 8.0f};
        float[] fArray6 = new float[]{8.0f, -8.0f, 0.0f, 0.0f, -8.0f, -8.0f, 8.0f, 8.0f, 8.0f, -8.0f, 0.0f, 0.0f};
        float f8 = (1.0f - entityGuardian.func_175469_o(f7)) * 0.55f;
        int n = 0;
        while (n < 12) {
            this.guardianSpines[n].rotateAngleX = (float)Math.PI * fArray[n];
            this.guardianSpines[n].rotateAngleY = (float)Math.PI * fArray2[n];
            this.guardianSpines[n].rotateAngleZ = (float)Math.PI * fArray3[n];
            this.guardianSpines[n].rotationPointX = fArray4[n] * (1.0f + MathHelper.cos(f3 * 1.5f + (float)n) * 0.01f - f8);
            this.guardianSpines[n].rotationPointY = 16.0f + fArray5[n] * (1.0f + MathHelper.cos(f3 * 1.5f + (float)n) * 0.01f - f8);
            this.guardianSpines[n].rotationPointZ = fArray6[n] * (1.0f + MathHelper.cos(f3 * 1.5f + (float)n) * 0.01f - f8);
            ++n;
        }
        this.guardianEye.rotationPointZ = -8.25f;
        Entity entity2 = Minecraft.getMinecraft().getRenderViewEntity();
        if (entityGuardian.hasTargetedEntity()) {
            entity2 = entityGuardian.getTargetedEntity();
        }
        if (entity2 != null) {
            Vec3 vec3 = entity2.getPositionEyes(0.0f);
            Vec3 vec32 = entity.getPositionEyes(0.0f);
            double d = vec3.yCoord - vec32.yCoord;
            this.guardianEye.rotationPointY = d > 0.0 ? 0.0f : 1.0f;
            Vec3 vec33 = entity.getLook(0.0f);
            vec33 = new Vec3(vec33.xCoord, 0.0, vec33.zCoord);
            Vec3 vec34 = new Vec3(vec32.xCoord - vec3.xCoord, 0.0, vec32.zCoord - vec3.zCoord).normalize().rotateYaw(1.5707964f);
            double d2 = vec33.dotProduct(vec34);
            this.guardianEye.rotationPointX = MathHelper.sqrt_float((float)Math.abs(d2)) * 2.0f * (float)Math.signum(d2);
        }
        this.guardianEye.showModel = true;
        float f9 = entityGuardian.func_175471_a(f7);
        this.guardianTail[0].rotateAngleY = MathHelper.sin(f9) * (float)Math.PI * 0.05f;
        this.guardianTail[1].rotateAngleY = MathHelper.sin(f9) * (float)Math.PI * 0.1f;
        this.guardianTail[1].rotationPointX = -1.5f;
        this.guardianTail[1].rotationPointY = 0.5f;
        this.guardianTail[1].rotationPointZ = 14.0f;
        this.guardianTail[2].rotateAngleY = MathHelper.sin(f9) * (float)Math.PI * 0.15f;
        this.guardianTail[2].rotationPointX = 0.5f;
        this.guardianTail[2].rotationPointY = 0.5f;
        this.guardianTail[2].rotationPointZ = 6.0f;
    }

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        this.setRotationAngles(f, f2, f3, f4, f5, f6, entity);
        this.guardianBody.render(f6);
    }
}

