/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public class SoundPoolEntry {
    private double volume;
    private final boolean streamingSound;
    private final ResourceLocation location;
    private double pitch;

    public void setPitch(double d) {
        this.pitch = d;
    }

    public SoundPoolEntry(ResourceLocation resourceLocation, double d, double d2, boolean bl) {
        this.location = resourceLocation;
        this.pitch = d;
        this.volume = d2;
        this.streamingSound = bl;
    }

    public double getPitch() {
        return this.pitch;
    }

    public ResourceLocation getSoundPoolEntryLocation() {
        return this.location;
    }

    public double getVolume() {
        return this.volume;
    }

    public SoundPoolEntry(SoundPoolEntry soundPoolEntry) {
        this.location = soundPoolEntry.location;
        this.pitch = soundPoolEntry.pitch;
        this.volume = soundPoolEntry.volume;
        this.streamingSound = soundPoolEntry.streamingSound;
    }

    public boolean isStreamingSound() {
        return this.streamingSound;
    }

    public void setVolume(double d) {
        this.volume = d;
    }
}

