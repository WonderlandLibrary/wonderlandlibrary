/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.audio;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;

public class ElytraSound
extends MovingSound {
    private final EntityPlayerSP player;
    private int time;

    public ElytraSound(EntityPlayerSP p_i47113_1_) {
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
            float f = MathHelper.sqrt(this.player.motionX * this.player.motionX + this.player.motionZ * this.player.motionZ + this.player.motionY * this.player.motionY);
            float f1 = f / 2.0f;
            this.volume = (double)f >= 0.01 ? MathHelper.clamp(f1 * f1, 0.0f, 1.0f) : 0.0f;
            if (this.time < 20) {
                this.volume = 0.0f;
            } else if (this.time < 40) {
                this.volume = (float)((double)this.volume * ((double)(this.time - 20) / 20.0));
            }
            float f2 = 0.8f;
            this.pitch = this.volume > 0.8f ? 1.0f + (this.volume - 0.8f) : 1.0f;
        } else {
            this.donePlaying = true;
        }
    }
}

