// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.cosmetics.physics;

import net.minecraft.util.MathHelper;
import java.util.Random;
import net.minecraft.client.entity.AbstractClientPlayer;

public class EyePhysics
{
    private final AbstractClientPlayer player;
    private final Random rand;
    private boolean shouldUpdate;
    private long lastUpdate;
    private double motionX;
    private double motionY;
    private double motionZ;
    public EyeInfo[] eyes;
    
    public EyePhysics(final AbstractClientPlayer player) {
        this.shouldUpdate = true;
        this.player = player;
        this.rand = new Random(Math.abs(player.hashCode()) * 8134);
        this.eyes = new EyeInfo[2];
        for (int i = 0; i < this.eyes.length; ++i) {
            this.eyes[i] = new EyeInfo();
        }
        this.update();
    }
    
    public void update() {
        if (!this.shouldUpdate) {
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
        this.lastUpdate = this.player.worldObj.getWorldTime();
    }
    
    public long getLastUpdate() {
        return this.lastUpdate;
    }
    
    public AbstractClientPlayer getPlayer() {
        return this.player;
    }
    
    public class EyeInfo
    {
        private float rotationYaw;
        private float rotationPitch;
        public float prevDeltaX;
        public float prevDeltaY;
        public float deltaX;
        public float deltaY;
        private float momentumX;
        private float momentumY;
        
        public EyeInfo() {
            final float n = -1.0f;
            this.deltaY = n;
            this.prevDeltaY = n;
        }
        
        public void update(final int eve, final EyePhysics physics, final double motionX, final double motionY, final double motionZ) {
            final float prevRotationYaw = this.rotationYaw;
            final float prevRotationPitch = this.rotationPitch;
            this.rotationYaw = physics.player.prevRotationYaw;
            this.rotationPitch = physics.player.prevRotationPitch;
            this.prevDeltaX = this.deltaX;
            this.prevDeltaY = this.deltaY;
            final float yawDiff = this.rotationYaw - prevRotationYaw;
            final float pitchDiff = this.rotationPitch - prevRotationPitch;
            this.momentumY += (float)(motionY * 1.5 + (motionX + motionZ) * EyePhysics.this.rand.nextGaussian() * 0.75 + pitchDiff / 45.0f + yawDiff / 180.0f);
            this.momentumX -= (float)((motionX + motionZ) * EyePhysics.this.rand.nextGaussian() * 0.4000000059604645 + yawDiff / 45.0f);
            final float momentumLoss = 0.9f;
            final float newDeltaX = this.deltaX + this.momentumX;
            final float newDeltaY = this.deltaY + this.momentumY;
            if (newDeltaX < -1.0f || newDeltaX > 1.0f) {
                final float newMo = this.momentumX * -momentumLoss;
                final float randomFloat = 0.8f + EyePhysics.this.rand.nextFloat() * 0.2f;
                this.momentumX = newMo * randomFloat;
                this.momentumY += newMo * randomFloat * ((EyePhysics.this.rand.nextFloat() > 0.5f) ? 1.0f : -1.0f);
            }
            if (newDeltaY < -1.0f || newDeltaY > 1.0f) {
                final float newMo = this.momentumY * -momentumLoss;
                final float randomFloat = 0.8f + EyePhysics.this.rand.nextFloat() * 0.2f;
                this.momentumX = newMo * randomFloat;
                this.momentumY += newMo * randomFloat * ((EyePhysics.this.rand.nextFloat() > 0.5f) ? 1.0f : -1.0f);
            }
            else {
                this.momentumY -= MathHelper.clamp_float(1.0f + this.deltaY, 0.0f, 0.1999f);
            }
            this.momentumX *= 0.95f;
            this.deltaX *= 0.95f;
            if (Math.abs(this.momentumX) < 0.03f) {
                this.momentumX = 0.0f;
            }
            if (Math.abs(this.deltaX) < 0.03f) {
                this.deltaX = 0.0f;
            }
            final float maxMomentum = 1.3f;
            this.momentumX = MathHelper.clamp_float(this.momentumX, -maxMomentum, maxMomentum);
            this.momentumY = MathHelper.clamp_float(this.momentumY, -maxMomentum, maxMomentum);
            this.deltaX += this.momentumX;
            this.deltaY += this.momentumY;
            this.deltaX = MathHelper.clamp_float(this.deltaX, -1.0f, 1.0f);
            this.deltaY = MathHelper.clamp_float(this.deltaY, -1.0f, 1.0f);
        }
    }
}
