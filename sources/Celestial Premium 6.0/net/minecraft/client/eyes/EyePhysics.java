/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.eyes;

import java.util.Random;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.eyes.cosmetic.CosmeticController;
import net.minecraft.util.math.MathHelper;

public class EyePhysics {
    private final AbstractClientPlayer player;
    private final Random rand;
    private boolean shouldUpdate = true;
    private long lastUpdate;
    private double motionX;
    private double motionY;
    private double motionZ;
    public EyeInfo[] eyes;

    public EyePhysics(AbstractClientPlayer player) {
        this.player = player;
        this.rand = new Random(Math.abs(player.hashCode()) * 8134);
        this.eyes = new EyeInfo[2];
        for (int i = 0; i < this.eyes.length; ++i) {
            this.eyes[i] = new EyeInfo();
        }
        this.update();
    }

    public void update() {
        if (!this.shouldUpdate || !CosmeticController.shouldRenderCosmetic(this.player)) {
            return;
        }
        this.shouldUpdate = false;
        this.motionX = this.player.posX - this.player.prevPosX;
        this.motionY = this.player.posY - this.player.prevPosY;
        this.motionZ = this.player.posZ - this.player.prevPosZ;
        for (int i = 0; i < this.eyes.length; ++i) {
            this.eyes[i].update(i, this, this.motionX, this.motionY, this.motionZ);
        }
    }

    public void requireUpdate() {
        this.shouldUpdate = true;
        this.lastUpdate = this.player.world.getWorldTime();
    }

    public long getLastUpdate() {
        return this.lastUpdate;
    }

    public AbstractClientPlayer getPlayer() {
        return this.player;
    }

    public class EyeInfo {
        private float rotationYaw;
        private float rotationPitch;
        public float prevDeltaX;
        public float prevDeltaY = -1.0f;
        public float deltaX;
        public float deltaY = -1.0f;
        private float momentumX;
        private float momentumY;

        public void update(int eve, EyePhysics physics, double motionX, double motionY, double motionZ) {
            float randomFloat;
            float newMo;
            float prevRotationYaw = this.rotationYaw;
            float prevRotationPitch = this.rotationPitch;
            this.rotationYaw = ((EyePhysics)physics).player.prevRotationYaw;
            this.rotationPitch = ((EyePhysics)physics).player.prevRotationPitch;
            this.prevDeltaX = this.deltaX;
            this.prevDeltaY = this.deltaY;
            float yawDiff = this.rotationYaw - prevRotationYaw;
            float pitchDiff = this.rotationPitch - prevRotationPitch;
            this.momentumY = (float)((double)this.momentumY + (motionY * 1.5 + (motionX + motionZ) * EyePhysics.this.rand.nextGaussian() * 0.75 + (double)(pitchDiff / 45.0f) + (double)(yawDiff / 180.0f)));
            this.momentumX = (float)((double)this.momentumX - ((motionX + motionZ) * EyePhysics.this.rand.nextGaussian() * (double)0.4f + (double)(yawDiff / 45.0f)));
            float momentumLoss = 0.9f;
            float newDeltaX = this.deltaX + this.momentumX;
            float newDeltaY = this.deltaY + this.momentumY;
            if (newDeltaX < -1.0f || newDeltaX > 1.0f) {
                newMo = this.momentumX * -momentumLoss;
                randomFloat = 0.8f + EyePhysics.this.rand.nextFloat() * 0.2f;
                this.momentumX = newMo * randomFloat;
                this.momentumY += newMo * randomFloat * (EyePhysics.this.rand.nextFloat() > 0.5f ? 1.0f : -1.0f);
            }
            if (newDeltaY < -1.0f || newDeltaY > 1.0f) {
                newMo = this.momentumY * -momentumLoss;
                randomFloat = 0.8f + EyePhysics.this.rand.nextFloat() * 0.2f;
                this.momentumX = newMo * randomFloat;
                this.momentumY += newMo * randomFloat * (EyePhysics.this.rand.nextFloat() > 0.5f ? 1.0f : -1.0f);
            } else {
                this.momentumY -= MathHelper.clamp(1.0f + this.deltaY, 0.0f, 0.1999f);
            }
            this.momentumX *= 0.95f;
            this.deltaX *= 0.95f;
            if (Math.abs(this.momentumX) < 0.03f) {
                this.momentumX = 0.0f;
            }
            if (Math.abs(this.deltaX) < 0.03f) {
                this.deltaX = 0.0f;
            }
            float maxMomentum = 1.3f;
            this.momentumX = MathHelper.clamp(this.momentumX, -maxMomentum, maxMomentum);
            this.momentumY = MathHelper.clamp(this.momentumY, -maxMomentum, maxMomentum);
            this.deltaX += this.momentumX;
            this.deltaY += this.momentumY;
            this.deltaX = MathHelper.clamp(this.deltaX, -1.0f, 1.0f);
            this.deltaY = MathHelper.clamp(this.deltaY, -1.0f, 1.0f);
        }
    }
}

