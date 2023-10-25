/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.audio;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.ResourceLocation;

public class PositionedSoundRecord
extends PositionedSound {
    private PositionedSoundRecord(ResourceLocation resourceLocation, float f, float f2, boolean bl, int n, ISound.AttenuationType attenuationType, float f3, float f4, float f5) {
        super(resourceLocation);
        this.volume = f;
        this.pitch = f2;
        this.xPosF = f3;
        this.yPosF = f4;
        this.zPosF = f5;
        this.repeat = bl;
        this.repeatDelay = n;
        this.attenuationType = attenuationType;
    }

    public static PositionedSoundRecord create(ResourceLocation resourceLocation, float f, float f2, float f3) {
        return new PositionedSoundRecord(resourceLocation, 4.0f, 1.0f, false, 0, ISound.AttenuationType.LINEAR, f, f2, f3);
    }

    public PositionedSoundRecord(ResourceLocation resourceLocation, float f, float f2, float f3, float f4, float f5) {
        this(resourceLocation, f, f2, false, 0, ISound.AttenuationType.LINEAR, f3, f4, f5);
    }

    public static PositionedSoundRecord create(ResourceLocation resourceLocation) {
        return new PositionedSoundRecord(resourceLocation, 1.0f, 1.0f, false, 0, ISound.AttenuationType.NONE, 0.0f, 0.0f, 0.0f);
    }

    public static PositionedSoundRecord create(ResourceLocation resourceLocation, float f) {
        return new PositionedSoundRecord(resourceLocation, 0.25f, f, false, 0, ISound.AttenuationType.NONE, 0.0f, 0.0f, 0.0f);
    }
}

