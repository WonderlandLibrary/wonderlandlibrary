package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Random;

public class SoundEventAccessorComposite implements ISoundEventAccessor<SoundPoolEntry> {

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

    public int getWeight() {
        int i = 0;

        for (ISoundEventAccessor<SoundPoolEntry> accessor : this.soundPool) {
            i += accessor.getWeight();
        }

        return i;
    }

    public SoundPoolEntry cloneEntry() {
        int i = this.getWeight();

        if (!this.soundPool.isEmpty() && i != 0) {
            int j = this.rnd.nextInt(i);

            for (ISoundEventAccessor<SoundPoolEntry> accessor : this.soundPool) {
                j -= accessor.getWeight();

                if (j < 0) {
                    SoundPoolEntry soundpoolentry = accessor.cloneEntry();
                    soundpoolentry.setPitch(soundpoolentry.getPitch() * this.eventPitch);
                    soundpoolentry.setVolume(soundpoolentry.getVolume() * this.eventVolume);
                    return soundpoolentry;
                }
            }

        }
        return SoundHandler.missing_sound;
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
