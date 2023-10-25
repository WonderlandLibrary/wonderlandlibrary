/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.audio;

import javax.annotation.Nullable;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public interface ISound {
    public ResourceLocation getSoundLocation();

    @Nullable
    public SoundEventAccessor createAccessor(SoundHandler var1);

    public Sound getSound();

    public SoundCategory getCategory();

    public boolean canRepeat();

    public int getRepeatDelay();

    public float getVolume();

    public float getPitch();

    public float getXPosF();

    public float getYPosF();

    public float getZPosF();

    public AttenuationType getAttenuationType();

    public static enum AttenuationType {
        NONE(0),
        LINEAR(2);

        private final int type;

        private AttenuationType(int typeIn) {
            this.type = typeIn;
        }

        public int getTypeInt() {
            return this.type;
        }
    }
}

