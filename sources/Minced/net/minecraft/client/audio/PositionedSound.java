// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import net.minecraft.util.SoundEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import javax.annotation.Nullable;

public abstract class PositionedSound implements ISound
{
    protected Sound sound;
    @Nullable
    private SoundEventAccessor soundEvent;
    protected SoundCategory category;
    protected ResourceLocation positionedSoundLocation;
    protected float volume;
    protected float pitch;
    protected float xPosF;
    protected float yPosF;
    protected float zPosF;
    protected boolean repeat;
    protected int repeatDelay;
    protected AttenuationType attenuationType;
    
    protected PositionedSound(final SoundEvent soundIn, final SoundCategory categoryIn) {
        this(soundIn.getSoundName(), categoryIn);
    }
    
    protected PositionedSound(final ResourceLocation soundId, final SoundCategory categoryIn) {
        this.volume = 1.0f;
        this.pitch = 1.0f;
        this.attenuationType = AttenuationType.LINEAR;
        this.positionedSoundLocation = soundId;
        this.category = categoryIn;
    }
    
    @Override
    public ResourceLocation getSoundLocation() {
        return this.positionedSoundLocation;
    }
    
    @Override
    public SoundEventAccessor createAccessor(final SoundHandler handler) {
        this.soundEvent = handler.getAccessor(this.positionedSoundLocation);
        if (this.soundEvent == null) {
            this.sound = SoundHandler.MISSING_SOUND;
        }
        else {
            this.sound = this.soundEvent.cloneEntry();
        }
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
    public AttenuationType getAttenuationType() {
        return this.attenuationType;
    }
}
