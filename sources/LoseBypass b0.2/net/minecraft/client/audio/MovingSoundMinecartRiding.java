/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.audio;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class MovingSoundMinecartRiding
extends MovingSound {
    private final EntityPlayer player;
    private final EntityMinecart minecart;

    public MovingSoundMinecartRiding(EntityPlayer playerRiding, EntityMinecart minecart) {
        super(new ResourceLocation("minecraft:minecart.inside"));
        this.player = playerRiding;
        this.minecart = minecart;
        this.attenuationType = ISound.AttenuationType.NONE;
        this.repeat = true;
        this.repeatDelay = 0;
    }

    @Override
    public void update() {
        if (!this.minecart.isDead && this.player.isRiding() && this.player.ridingEntity == this.minecart) {
            float f = MathHelper.sqrt_double(this.minecart.motionX * this.minecart.motionX + this.minecart.motionZ * this.minecart.motionZ);
            if ((double)f >= 0.01) {
                this.volume = 0.0f + MathHelper.clamp_float(f, 0.0f, 1.0f) * 0.75f;
                return;
            }
            this.volume = 0.0f;
            return;
        }
        this.donePlaying = true;
    }
}

