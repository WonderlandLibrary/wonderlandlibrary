/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
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
implements ISoundEventAccessor {
    private final List soundPool = Lists.newArrayList();
    private final Random rnd = new Random();
    private final ResourceLocation soundLocation;
    private final SoundCategory category;
    private double eventPitch;
    private double eventVolume;
    private static final String __OBFID = "CL_00001146";

    public SoundEventAccessorComposite(ResourceLocation soundLocation, double pitch, double volume, SoundCategory category) {
        this.soundLocation = soundLocation;
        this.eventVolume = volume;
        this.eventPitch = pitch;
        this.category = category;
    }

    @Override
    public int getWeight() {
        int var1 = 0;
        for (ISoundEventAccessor var3 : this.soundPool) {
            var1 += var3.getWeight();
        }
        return var1;
    }

    public SoundPoolEntry cloneEntry1() {
        int var1 = this.getWeight();
        if (!this.soundPool.isEmpty() && var1 != 0) {
            ISoundEventAccessor var4;
            int var2 = this.rnd.nextInt(var1);
            Iterator var3 = this.soundPool.iterator();
            do {
                if (var3.hasNext()) continue;
                return SoundHandler.missing_sound;
            } while ((var2 -= (var4 = (ISoundEventAccessor)var3.next()).getWeight()) >= 0);
            SoundPoolEntry var5 = (SoundPoolEntry)var4.cloneEntry();
            var5.setPitch(var5.getPitch() * this.eventPitch);
            var5.setVolume(var5.getVolume() * this.eventVolume);
            return var5;
        }
        return SoundHandler.missing_sound;
    }

    public void addSoundToEventPool(ISoundEventAccessor p_148727_1_) {
        this.soundPool.add(p_148727_1_);
    }

    public ResourceLocation getSoundEventLocation() {
        return this.soundLocation;
    }

    public SoundCategory getSoundCategory() {
        return this.category;
    }

    @Override
    public Object cloneEntry() {
        return this.cloneEntry1();
    }
}

