// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;

public class MovingSoundMinecartRiding extends MovingSound
{
    private final EntityPlayer player;
    private final EntityMinecart minecart;
    
    public MovingSoundMinecartRiding(final EntityPlayer playerRiding, final EntityMinecart minecart) {
        super(SoundEvents.ENTITY_MINECART_INSIDE, SoundCategory.NEUTRAL);
        this.player = playerRiding;
        this.minecart = minecart;
        this.attenuationType = ISound.AttenuationType.NONE;
        this.repeat = true;
        this.repeatDelay = 0;
    }
    
    @Override
    public void update() {
        if (!this.minecart.isDead && this.player.isRiding() && this.player.getRidingEntity() == this.minecart) {
            final float f = MathHelper.sqrt(this.minecart.motionX * this.minecart.motionX + this.minecart.motionZ * this.minecart.motionZ);
            if (f >= 0.01) {
                this.volume = 0.0f + MathHelper.clamp(f, 0.0f, 1.0f) * 0.75f;
            }
            else {
                this.volume = 0.0f;
            }
        }
        else {
            this.donePlaying = true;
        }
    }
}
