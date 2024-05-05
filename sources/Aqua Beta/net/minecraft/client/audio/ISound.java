package net.minecraft.client.audio;

import net.minecraft.client.audio.ISound;
import net.minecraft.util.ResourceLocation;

public interface ISound {
    public ResourceLocation getSoundLocation();

    public boolean canRepeat();

    public int getRepeatDelay();

    public float getVolume();

    public float getPitch();

    public float getXPosF();

    public float getYPosF();

    public float getZPosF();

    public AttenuationType getAttenuationType();
}
