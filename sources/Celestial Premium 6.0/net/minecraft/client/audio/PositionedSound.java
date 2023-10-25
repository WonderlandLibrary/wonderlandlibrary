/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.audio;

import javax.annotation.Nullable;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public abstract class PositionedSound
implements ISound {
    protected Sound sound;
    @Nullable
    private SoundEventAccessor soundEvent;
    protected SoundCategory category;
    protected ResourceLocation positionedSoundLocation;
    protected float volume = 1.0f;
    protected float pitch = 1.0f;
    protected float xPosF;
    protected float yPosF;
    protected float zPosF;
    protected boolean repeat;
    protected int repeatDelay;
    protected ISound.AttenuationType attenuationType = ISound.AttenuationType.LINEAR;

    protected PositionedSound(SoundEvent soundIn, SoundCategory categoryIn) {
        this(soundIn.getSoundName(), categoryIn);
    }

    protected PositionedSound(ResourceLocation soundId, SoundCategory categoryIn) {
        this.positionedSoundLocation = soundId;
        this.category = categoryIn;
    }

    @Override
    public ResourceLocation getSoundLocation() {
        return this.positionedSoundLocation;
    }

    @Override
    public SoundEventAccessor createAccessor(SoundHandler handler) {
        this.soundEvent = handler.getAccessor(this.positionedSoundLocation);
        this.sound = this.soundEvent == null ? SoundHandler.MISSING_SOUND : this.soundEvent.cloneEntry();
        return this.soundEvent;
    }

    @Override
    public Sound getSound() {
        return this.sound;
    }

    @Override
    public SoundCategory getCategory() {
        return this.category;
    }

    @Override
    public boolean canRepeat() {
        return this.repeat;
    }

    @Override
    public int getRepeatDelay() {
        return this.repeatDelay;
    }

    @Override
    public float getVolume() {
        return this.volume * this.sound.getVolume();
    }

    @Override
    public float getPitch() {
        return this.pitch * this.sound.getPitch();
    }

    @Override
    public float getXPosF() {
        return this.xPosF;
    }

    @Override
    public float getYPosF() {
        return this.yPosF;
    }

    @Override
    public float getZPosF() {
        return this.zPosF;
    }

    @Override
    public ISound.AttenuationType getAttenuationType() {
        return this.attenuationType;
    }
}

