/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.client.audio.ISoundEventAccessor;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.util.ResourceLocation;

public class SoundEventAccessorComposite
implements ISoundEventAccessor<SoundPoolEntry> {
    private double eventPitch;
    private final List<ISoundEventAccessor<SoundPoolEntry>> soundPool = Lists.newArrayList();
    private final Random rnd = new Random();
    private double eventVolume;
    private final ResourceLocation soundLocation;
    private final SoundCategory category;

    public SoundEventAccessorComposite(ResourceLocation resourceLocation, double d, double d2, SoundCategory soundCategory) {
        this.soundLocation = resourceLocation;
        this.eventVolume = d2;
        this.eventPitch = d;
        this.category = soundCategory;
    }

    public ResourceLocation getSoundEventLocation() {
        return this.soundLocation;
    }

    @Override
    public SoundPoolEntry cloneEntry() {
        int n = this.getWeight();
        if (!this.soundPool.isEmpty() && n != 0) {
            int n2 = this.rnd.nextInt(n);
            for (ISoundEventAccessor<SoundPoolEntry> iSoundEventAccessor : this.soundPool) {
                if ((n2 -= iSoundEventAccessor.getWeight()) >= 0) continue;
                SoundPoolEntry soundPoolEntry = iSoundEventAccessor.cloneEntry();
                soundPoolEntry.setPitch(soundPoolEntry.getPitch() * this.eventPitch);
                soundPoolEntry.setVolume(soundPoolEntry.getVolume() * this.eventVolume);
                return soundPoolEntry;
            }
            return SoundHandler.missing_sound;
        }
        return SoundHandler.missing_sound;
    }

    public SoundCategory getSoundCategory() {
        return this.category;
    }

    @Override
    public int getWeight() {
        int n = 0;
        for (ISoundEventAccessor<SoundPoolEntry> iSoundEventAccessor : this.soundPool) {
            n += iSoundEventAccessor.getWeight();
        }
        return n;
    }

    public void addSoundToEventPool(ISoundEventAccessor<SoundPoolEntry> iSoundEventAccessor) {
        this.soundPool.add(iSoundEventAccessor);
    }
}

