/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.client.audio.ISoundEventAccessor;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.util.ResourceLocation;

public class SoundEventAccessorComposite
implements ISoundEventAccessor<SoundPoolEntry> {
    private final List<ISoundEventAccessor<SoundPoolEntry>> soundPool = Lists.newArrayList();
    private final Random rnd = new Random();
    private final ResourceLocation soundLocation;
    private final SoundCategory category;
    private double eventPitch;
    private double eventVolume;

    public SoundEventAccessorComposite(ResourceLocation soundLocation, double pitch, double volume, SoundCategory category) {
        this.soundLocation = soundLocation;
        this.eventVolume = volume;
        this.eventPitch = pitch;
        this.category = category;
    }

    @Override
    public int getWeight() {
        int i = 0;
        Iterator<ISoundEventAccessor<SoundPoolEntry>> iterator = this.soundPool.iterator();
        while (iterator.hasNext()) {
            ISoundEventAccessor<SoundPoolEntry> isoundeventaccessor = iterator.next();
            i += isoundeventaccessor.getWeight();
        }
        return i;
    }

    @Override
    public SoundPoolEntry cloneEntry() {
        ISoundEventAccessor<SoundPoolEntry> isoundeventaccessor;
        int i = this.getWeight();
        if (this.soundPool.isEmpty()) return SoundHandler.missing_sound;
        if (i == 0) return SoundHandler.missing_sound;
        int j = this.rnd.nextInt(i);
        Iterator<ISoundEventAccessor<SoundPoolEntry>> iterator = this.soundPool.iterator();
        do {
            if (!iterator.hasNext()) return SoundHandler.missing_sound;
        } while ((j -= (isoundeventaccessor = iterator.next()).getWeight()) >= 0);
        SoundPoolEntry soundpoolentry = isoundeventaccessor.cloneEntry();
        soundpoolentry.setPitch(soundpoolentry.getPitch() * this.eventPitch);
        soundpoolentry.setVolume(soundpoolentry.getVolume() * this.eventVolume);
        return soundpoolentry;
    }

    public void addSoundToEventPool(ISoundEventAccessor<SoundPoolEntry> sound) {
        this.soundPool.add(sound);
    }

    public ResourceLocation getSoundEventLocation() {
        return this.soundLocation;
    }

    public SoundCategory getSoundCategory() {
        return this.category;
    }
}

