// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.client.entity.EntityPlayerSP;

public class ElytraSound extends MovingSound
{
    private final EntityPlayerSP player;
    private int time;
    
    public ElytraSound(final EntityPlayerSP p_i47113_1_) {
        super(SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.PLAYERS);
        this.player = p_i47113_1_;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 0.1f;
    }
    
    @Override
    public void update() {
        ++this.time;
        if (!this.player.isDead && (this.time <= 20 || this.player.isElytraFlying())) {
            this.xPosF = (float)this.player.posX;
            this.yPosF = (float)this.player.posY;
            this.zPosF = (float)this.player.posZ;
            final float f = MathHelper.sqrt(this.player.motionX * this.player.motionX + this.player.motionZ * this.player.motionZ + this.player.motionY * this.player.motionY);
            final float f2 = f / 2.0f;
            if (f >= 0.01) {
                this.volume = MathHelper.clamp(f2 * f2, 0.0f, 1.0f);
            }
            else {
                this.volume = 0.0f;
            }
            if (this.time < 20) {
                this.volume = 0.0f;
            }
            else if (this.time < 40) {
                this.volume *= (float)((this.time - 20) / 20.0);
            }
            final float f3 = 0.8f;
            if (this.volume > 0.8f) {
                this.pitch = 1.0f + (this.volume - 0.8f);
            }
            else {
                this.pitch = 1.0f;
            }
        }
        else {
            this.donePlaying = true;
        }
    }
}
