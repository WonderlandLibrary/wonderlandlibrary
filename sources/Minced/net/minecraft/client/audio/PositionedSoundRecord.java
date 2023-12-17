// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class PositionedSoundRecord extends PositionedSound
{
    public PositionedSoundRecord(final SoundEvent soundIn, final SoundCategory categoryIn, final float volumeIn, final float pitchIn, final BlockPos pos) {
        this(soundIn, categoryIn, volumeIn, pitchIn, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
    }
    
    public static PositionedSoundRecord getMasterRecord(final SoundEvent soundIn, final float pitchIn) {
        return getRecord(soundIn, pitchIn, 0.25f);
    }
    
    public static PositionedSoundRecord getRecord(final SoundEvent soundIn, final float pitchIn, final float volumeIn) {
        return new PositionedSoundRecord(soundIn, SoundCategory.MASTER, volumeIn, pitchIn, false, 0, ISound.AttenuationType.NONE, 0.0f, 0.0f, 0.0f);
    }
    
    public static PositionedSoundRecord getMusicRecord(final SoundEvent soundIn) {
        return new PositionedSoundRecord(soundIn, SoundCategory.MUSIC, 1.0f, 1.0f, false, 0, ISound.AttenuationType.NONE, 0.0f, 0.0f, 0.0f);
    }
    
    public static PositionedSoundRecord getRecordSoundRecord(final SoundEvent soundIn, final float xIn, final float yIn, final float zIn) {
        return new PositionedSoundRecord(soundIn, SoundCategory.RECORDS, 4.0f, 1.0f, false, 0, ISound.AttenuationType.LINEAR, xIn, yIn, zIn);
    }
    
    public PositionedSoundRecord(final SoundEvent soundIn, final SoundCategory categoryIn, final float volumeIn, final float pitchIn, final float xIn, final float yIn, final float zIn) {
        this(soundIn, categoryIn, volumeIn, pitchIn, false, 0, ISound.AttenuationType.LINEAR, xIn, yIn, zIn);
    }
    
    private PositionedSoundRecord(final SoundEvent soundIn, final SoundCategory categoryIn, final float volumeIn, final float pitchIn, final boolean repeatIn, final int repeatDelayIn, final ISound.AttenuationType attenuationTypeIn, final float xIn, final float yIn, final float zIn) {
        this(soundIn.getSoundName(), categoryIn, volumeIn, pitchIn, repeatIn, repeatDelayIn, attenuationTypeIn, xIn, yIn, zIn);
    }
    
    public PositionedSoundRecord(final ResourceLocation soundId, final SoundCategory categoryIn, final float volumeIn, final float pitchIn, final boolean repeatIn, final int repeatDelayIn, final ISound.AttenuationType attenuationTypeIn, final float xIn, final float yIn, final float zIn) {
        super(soundId, categoryIn);
        this.volume = volumeIn;
        this.pitch = pitchIn;
        this.xPosF = xIn;
        this.yPosF = yIn;
        this.zPosF = zIn;
        this.repeat = repeatIn;
        this.repeatDelay = repeatDelayIn;
        this.attenuationType = attenuationTypeIn;
    }
}
