/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.audio;

import net.minecraft.client.audio.ISoundEventAccessor;
import net.minecraft.client.audio.SoundPoolEntry;

public class SoundEventAccessor
implements ISoundEventAccessor<SoundPoolEntry> {
    private final SoundPoolEntry entry;
    private final int weight;

    @Override
    public SoundPoolEntry cloneEntry() {
        return new SoundPoolEntry(this.entry);
    }

    SoundEventAccessor(SoundPoolEntry soundPoolEntry, int n) {
        this.entry = soundPoolEntry;
        this.weight = n;
    }

    @Override
    public int getWeight() {
        return this.weight;
    }
}

