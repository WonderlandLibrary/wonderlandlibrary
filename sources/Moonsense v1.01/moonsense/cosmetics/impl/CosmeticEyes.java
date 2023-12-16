// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.cosmetics.impl;

import moonsense.cosmetics.physics.EyePhysics;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import moonsense.cosmetics.PhysicsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import java.util.Random;
import net.minecraft.util.ResourceLocation;
import moonsense.cosmetics.model.ModelEyes;
import moonsense.cosmetics.base.CosmeticBase;

public class CosmeticEyes extends CosmeticBase
{
    private final ModelEyes model;
    private static final ResourceLocation TEXTURE;
    private static final float[] headJointSneak;
    private static final float[] headJoint;
    private static final float[] eyeOffset;
    private static final float[] irisColor;
    private static final float[] pupilColor;
    private static final float halfInterpupillaryDistance = 0.125f;
    private static final float eyeScale = 0.75f;
    private static final float modelScale = 0.0625f;
    private Random rand;
    private int[] potionTime;
    
    static {
        TEXTURE = new ResourceLocation("streamlined/cosmetics/eyes.png");
        headJointSneak = new float[] { 0.0f, -0.0625f, 0.0f };
        headJoint = new float[3];
        eyeOffset = new float[] { 0.0f, 0.25f, 0.25f };
        irisColor = new float[] { 0.9f, 0.9f, 0.9f };
        pupilColor = new float[] { 0.0f, 0.0f, 0.0f };
    }
    
    public CosmeticEyes(final RenderPlayer playerRenderer) {
        super(playerRenderer);
        this.rand = new Random();
        this.potionTime = new int[2];
        this.model = new ModelEyes(playerRenderer);
    }
    
    @Override
    public void render(final EntityLivingBase player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (player.isInvisible()) {
            return;
        }
        if (player.getName().equalsIgnoreCase(Minecraft.getMinecraft().session.getUsername())) {
            final EyePhysics physics = PhysicsManager.getInstance().getPhysics((AbstractClientPlayer)player);
            physics.requireUpdate();
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            for (int i = 0; i < 2; ++i) {
                GlStateManager.pushMatrix();
                final float[] joint = this.getSneakOffset((AbstractClientPlayer)player, partialTicks);
                GlStateManager.translate(-joint[0], -joint[1], -joint[2]);
                GlStateManager.rotate(this.getHeadYaw((AbstractClientPlayer)player, partialTicks), 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(this.getHeadPitch((AbstractClientPlayer)player, partialTicks), 1.0f, 0.0f, 0.0f);
                GlStateManager.translate(-(CosmeticEyes.eyeOffset[0] + ((i == 0) ? 0.125f : -0.125f)), -CosmeticEyes.eyeOffset[1], -CosmeticEyes.eyeOffset[2]);
                GlStateManager.scale(0.75f, 0.75f, 0.375f);
                Minecraft.getMinecraft().getTextureManager().bindTexture(CosmeticEyes.TEXTURE);
                GlStateManager.color(CosmeticEyes.irisColor[0], CosmeticEyes.irisColor[1], CosmeticEyes.irisColor[2]);
                this.model.renderIris(0.0625f);
                GlStateManager.color(CosmeticEyes.pupilColor[0], CosmeticEyes.pupilColor[1], CosmeticEyes.pupilColor[2]);
                final float pupilScale = this.getPupilScale((AbstractClientPlayer)player, partialTicks, i);
                GlStateManager.pushMatrix();
                GlStateManager.scale(pupilScale, pupilScale, 1.0f);
                this.model.movePupil(physics.eyes[i].prevDeltaX + (physics.eyes[i].deltaX - physics.eyes[i].prevDeltaX) * partialTicks, physics.eyes[i].prevDeltaY + (physics.eyes[i].deltaY - physics.eyes[i].prevDeltaY) * partialTicks, pupilScale);
                this.model.renderPupil(0.0625f);
                GlStateManager.popMatrix();
                GlStateManager.popMatrix();
            }
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    private float getPupilScale(final AbstractClientPlayer player, final float partialTicks, final int eye) {
        if (!player.getActivePotionEffects().isEmpty()) {
            this.rand.setSeed(Math.abs(player.hashCode()) * 1000);
            if (this.potionTime == null || this.potionTime.length < 2) {
                this.potionTime = new int[2];
            }
            for (int i = 0; i < this.potionTime.length; ++i) {
                this.potionTime[i] = 20 + this.rand.nextInt(20);
            }
            return 0.3f + ((float)Math.sin(Math.toRadians((player.ticksExisted + partialTicks) / this.potionTime[eye] * 360.0f)) + 1.0f) / 2.0f;
        }
        return 1.0f;
    }
    
    private float interpolateRotation(final float prevAngle, final float nextAngle, final float partialTicks) {
        float f;
        for (f = nextAngle - prevAngle; f < -180.0f; f += 360.0f) {}
        while (f >= 180.0f) {
            f -= 360.0f;
        }
        return prevAngle + partialTicks * f;
    }
    
    private float getHeadPitch(final AbstractClientPlayer player, final float partialTicks) {
        return this.interpolateRotation(player.prevRotationPitch, player.rotationPitch, partialTicks);
    }
    
    private float getHeadYaw(final AbstractClientPlayer player, final float partialTicks) {
        return this.interpolateRotation(player.prevRotationYawHead, player.rotationYawHead, partialTicks) - this.interpolateRotation(player.prevRenderYawOffset, player.renderYawOffset, partialTicks);
    }
    
    private float[] getSneakOffset(final AbstractClientPlayer player, final float partialTicks) {
        if (player.isSneaking()) {
            GlStateManager.translate(0.0f, 0.2f, 0.0f);
            return CosmeticEyes.headJointSneak;
        }
        return CosmeticEyes.headJoint;
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
