// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.math.Vec3d;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.Entity;

public class ModelGuardian extends ModelBase
{
    private final ModelRenderer guardianBody;
    private final ModelRenderer guardianEye;
    private final ModelRenderer[] guardianSpines;
    private final ModelRenderer[] guardianTail;
    
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
        for (int i = 0; i < this.guardianSpines.length; ++i) {
            (this.guardianSpines[i] = new ModelRenderer(this, 0, 0)).addBox(-1.0f, -4.5f, -1.0f, 2, 9, 2);
            this.guardianBody.addChild(this.guardianSpines[i]);
        }
        (this.guardianEye = new ModelRenderer(this, 8, 0)).addBox(-1.0f, 15.0f, 0.0f, 2, 2, 1);
        this.guardianBody.addChild(this.guardianEye);
        this.guardianTail = new ModelRenderer[3];
        (this.guardianTail[0] = new ModelRenderer(this, 40, 0)).addBox(-2.0f, 14.0f, 7.0f, 4, 4, 8);
        (this.guardianTail[1] = new ModelRenderer(this, 0, 54)).addBox(0.0f, 14.0f, 0.0f, 3, 3, 7);
        this.guardianTail[2] = new ModelRenderer(this);
        this.guardianTail[2].setTextureOffset(41, 32).addBox(0.0f, 14.0f, 0.0f, 2, 2, 6);
        this.guardianTail[2].setTextureOffset(25, 19).addBox(1.0f, 10.5f, 3.0f, 1, 9, 9);
        this.guardianBody.addChild(this.guardianTail[0]);
        this.guardianTail[0].addChild(this.guardianTail[1]);
        this.guardianTail[1].addChild(this.guardianTail[2]);
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.guardianBody.render(scale);
    }
    
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        final EntityGuardian entityguardian = (EntityGuardian)entityIn;
        final float f = ageInTicks - entityguardian.ticksExisted;
        this.guardianBody.rotateAngleY = netHeadYaw * 0.017453292f;
        this.guardianBody.rotateAngleX = headPitch * 0.017453292f;
        final float[] afloat = { 1.75f, 0.25f, 0.0f, 0.0f, 0.5f, 0.5f, 0.5f, 0.5f, 1.25f, 0.75f, 0.0f, 0.0f };
        final float[] afloat2 = { 0.0f, 0.0f, 0.0f, 0.0f, 0.25f, 1.75f, 1.25f, 0.75f, 0.0f, 0.0f, 0.0f, 0.0f };
        final float[] afloat3 = { 0.0f, 0.0f, 0.25f, 1.75f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.75f, 1.25f };
        final float[] afloat4 = { 0.0f, 0.0f, 8.0f, -8.0f, -8.0f, 8.0f, 8.0f, -8.0f, 0.0f, 0.0f, 8.0f, -8.0f };
        final float[] afloat5 = { -8.0f, -8.0f, -8.0f, -8.0f, 0.0f, 0.0f, 0.0f, 0.0f, 8.0f, 8.0f, 8.0f, 8.0f };
        final float[] afloat6 = { 8.0f, -8.0f, 0.0f, 0.0f, -8.0f, -8.0f, 8.0f, 8.0f, 8.0f, -8.0f, 0.0f, 0.0f };
        final float f2 = (1.0f - entityguardian.getSpikesAnimation(f)) * 0.55f;
        for (int i = 0; i < 12; ++i) {
            this.guardianSpines[i].rotateAngleX = 3.1415927f * afloat[i];
            this.guardianSpines[i].rotateAngleY = 3.1415927f * afloat2[i];
            this.guardianSpines[i].rotateAngleZ = 3.1415927f * afloat3[i];
            this.guardianSpines[i].rotationPointX = afloat4[i] * (1.0f + MathHelper.cos(ageInTicks * 1.5f + i) * 0.01f - f2);
            this.guardianSpines[i].rotationPointY = 16.0f + afloat5[i] * (1.0f + MathHelper.cos(ageInTicks * 1.5f + i) * 0.01f - f2);
            this.guardianSpines[i].rotationPointZ = afloat6[i] * (1.0f + MathHelper.cos(ageInTicks * 1.5f + i) * 0.01f - f2);
        }
        this.guardianEye.rotationPointZ = -8.25f;
        Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
        if (entityguardian.hasTargetedEntity()) {
            entity = entityguardian.getTargetedEntity();
        }
        if (entity != null) {
            final Vec3d vec3d = entity.getPositionEyes(0.0f);
            final Vec3d vec3d2 = entityIn.getPositionEyes(0.0f);
            final double d0 = vec3d.y - vec3d2.y;
            if (d0 > 0.0) {
                this.guardianEye.rotationPointY = 0.0f;
            }
            else {
                this.guardianEye.rotationPointY = 1.0f;
            }
            Vec3d vec3d3 = entityIn.getLook(0.0f);
            vec3d3 = new Vec3d(vec3d3.x, 0.0, vec3d3.z);
            final Vec3d vec3d4 = new Vec3d(vec3d2.x - vec3d.x, 0.0, vec3d2.z - vec3d.z).normalize().rotateYaw(1.5707964f);
            final double d2 = vec3d3.dotProduct(vec3d4);
            this.guardianEye.rotationPointX = MathHelper.sqrt((float)Math.abs(d2)) * 2.0f * (float)Math.signum(d2);
        }
        this.guardianEye.showModel = true;
        final float f3 = entityguardian.getTailAnimation(f);
        this.guardianTail[0].rotateAngleY = MathHelper.sin(f3) * 3.1415927f * 0.05f;
        this.guardianTail[1].rotateAngleY = MathHelper.sin(f3) * 3.1415927f * 0.1f;
        this.guardianTail[1].rotationPointX = -1.5f;
        this.guardianTail[1].rotationPointY = 0.5f;
        this.guardianTail[1].rotationPointZ = 14.0f;
        this.guardianTail[2].rotateAngleY = MathHelper.sin(f3) * 3.1415927f * 0.15f;
        this.guardianTail[2].rotationPointX = 0.5f;
        this.guardianTail[2].rotationPointY = 0.5f;
        this.guardianTail[2].rotationPointZ = 6.0f;
    }
}
