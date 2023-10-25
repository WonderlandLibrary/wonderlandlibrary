/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.audio;

import net.minecraft.client.audio.ISound;
import net.minecraft.util.ResourceLocation;

public abstract class PositionedSound
implements ISound {
    protected float yPosF;
    protected float volume = 1.0f;
    protected boolean repeat = false;
    protected final ResourceLocation positionedSoundLocation;
    protected int repeatDelay = 0;
    protected float pitch = 1.0f;
    protected float zPosF;
    protected float xPosF;
    protected ISound.AttenuationType attenuationType = ISound.AttenuationType.LINEAR;

    @Override
    public float getPitch() {
        return this.pitch;
    }

    @Override
    public boolean canRepeat() {
        return this.repeat;
    }

    @Override
    public ISound.AttenuationType getAttenuationType() {
        return this.attenuationType;
    }

    @Override
    public int getRepeatDelay() {
        return this.repeatDelay;
    }

    @Override
    public float getVolume() {
        return this.volume;
    }

    @Override
    public float getXPosF() {
        return this.xPosF;
    }

    @Override
    public ResourceLocation getSoundLocation() {
        return this.positionedSoundLocation;
    }

    protected PositionedSound(ResourceLocation resourceLocation) {
        this.positionedSoundLocation = resourceLocation;
    }

    @Override
    public float getZPosF() {
        return this.zPosF;
    }

    @Override
    public float getYPosF() {
        return this.yPosF;
    }
}

