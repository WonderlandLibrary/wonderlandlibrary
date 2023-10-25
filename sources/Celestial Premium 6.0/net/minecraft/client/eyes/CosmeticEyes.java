/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.eyes;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.eyes.EyePhysics;
import net.minecraft.client.eyes.ModelEyes;
import net.minecraft.client.eyes.PhysicsManager;
import net.minecraft.client.eyes.cosmetic.CosmeticBase;
import net.minecraft.client.eyes.cosmetic.CosmeticController;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import org.celestial.client.feature.impl.visuals.CustomModel;

public class CosmeticEyes
extends CosmeticBase {
    private final ModelEyes model;
    private static final ResourceLocation TEXTURE = new ResourceLocation("celestial/eyes.png");
    private static final float[] headJointSneak = new float[]{0.0f, -0.0625f, 0.0f};
    private static final float[] headJoint = new float[3];
    private static final float[] eyeOffset = new float[]{0.0f, 0.25f, 0.25f};
    private static final float[] irisColor = new float[]{0.9f, 0.9f, 0.9f};
    private static final float[] pupilColor = new float[]{0.0f, 0.0f, 0.0f};
    private static final float halfInterpupillaryDistance = 0.125f;
    private static final float modelScale = 0.0625f;
    private Random rand = new Random();
    private int[] potionTime = new int[2];

    public CosmeticEyes(RenderPlayer playerRenderer) {
        super(playerRenderer);
        this.model = new ModelEyes(playerRenderer);
    }

    @Override
    public void render(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!CosmeticController.shouldRenderCosmetic(player)) {
            return;
        }
        EyePhysics physics = PhysicsManager.getInstance().getPhysics(player);
        physics.requireUpdate();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        for (int i = 0; i < 2; ++i) {
            GlStateManager.pushMatrix();
            float[] joint = this.getSneakOffset(player, partialTicks);
            GlStateManager.translate(-joint[0], -joint[1], -joint[2]);
            GlStateManager.rotate(this.getHeadYaw(player, partialTicks), 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(this.getHeadPitch(player, partialTicks), 1.0f, 0.0f, 0.0f);
            GlStateManager.translate(-(eyeOffset[0] + (i == 0 ? 0.125f : -0.125f)), -eyeOffset[1], -eyeOffset[2]);
            GlStateManager.scale(CustomModel.googlyEyesSize.getCurrentValue(), CustomModel.googlyEyesSize.getCurrentValue(), CustomModel.googlyEyesSize.getCurrentValue() * 0.5f);
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
            GlStateManager.color(irisColor[0], irisColor[1], irisColor[2]);
            this.model.renderIris(0.0625f);
            GlStateManager.color(pupilColor[0], pupilColor[1], pupilColor[2]);
            float pupilScale = this.getPupilScale(player, partialTicks, i);
            GlStateManager.pushMatrix();
            GlStateManager.scale(pupilScale, pupilScale, 1.0f);
            this.model.movePupil(physics.eyes[i].prevDeltaX + (physics.eyes[i].deltaX - physics.eyes[i].prevDeltaX) * partialTicks, physics.eyes[i].prevDeltaY + (physics.eyes[i].deltaY - physics.eyes[i].prevDeltaY) * partialTicks, pupilScale);
            this.model.renderPupil(0.0625f);
            GlStateManager.popMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }

    private float getPupilScale(AbstractClientPlayer player, float partialTicks, int eye) {
        if (!player.getActivePotionEffects().isEmpty()) {
            this.rand.setSeed(Math.abs(player.hashCode()) * 1000);
            if (this.potionTime == null || this.potionTime.length < 2) {
                this.potionTime = new int[2];
            }
            for (int i = 0; i < this.potionTime.length; ++i) {
                this.potionTime[i] = 20 + this.rand.nextInt(20);
            }
            return 0.3f + ((float)Math.sin(Math.toRadians(((float)player.ticksExisted + partialTicks) / (float)this.potionTime[eye] * 360.0f)) + 1.0f) / 2.0f;
        }
        return 1.0f;
    }

    private float interpolateRotation(float prevAngle, float nextAngle, float partialTicks) {
        float f;
        for (f = nextAngle - prevAngle; f < -180.0f; f += 360.0f) {
        }
        while (f >= 180.0f) {
            f -= 360.0f;
        }
        return prevAngle + partialTicks * f;
    }

    private float getHeadPitch(AbstractClientPlayer player, float partialTicks) {
        return this.interpolateRotation(player.prevRotationPitch, player.rotationPitch, partialTicks);
    }

    private float getHeadYaw(AbstractClientPlayer player, float partialTicks) {
        return this.interpolateRotation(player.prevRotationYawHead, player.rotationYawHead, partialTicks) - this.interpolateRotation(player.prevRenderYawOffset, player.renderYawOffset, partialTicks);
    }

    private float[] getSneakOffset(AbstractClientPlayer player, float partialTicks) {
        if (player.isSneaking()) {
            GlStateManager.translate(0.0f, 0.2f, 0.0f);
            return headJointSneak;
        }
        return headJoint;
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}

