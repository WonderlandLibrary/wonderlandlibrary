// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.audio;

import java.util.Iterator;
import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import java.util.Random;
import java.util.List;

public class SoundEventAccessorComposite implements ISoundEventAccessor
{
    private final List soundPool;
    private final Random rnd;
    private final ResourceLocation soundLocation;
    private final SoundCategory category;
    private double eventPitch;
    private double eventVolume;
    private static final String __OBFID = "CL_00001146";
    
    public SoundEventAccessorComposite(final ResourceLocation soundLocation, final double pitch, final double volume, final SoundCategory category) {
        this.soundPool = Lists.newArrayList();
        this.rnd = new Random();
        this.soundLocation = soundLocation;
        this.eventVolume = volume;
        this.eventPitch = pitch;
        this.category = category;
    }
    
    @Override
    public int getWeight() {
        int var1 = 0;
        for (final ISoundEventAccessor var3 : this.soundPool) {
            var1 += var3.getWeight();
        }
        return var1;
    }
    
    @Override
    public SoundPoolEntry cloneEntry() {
        final int var1 = this.getWeight();
        if (!this.soundPool.isEmpty() && var1 != 0) {
            int var2 = this.rnd.nextInt(var1);
            for (final ISoundEventAccessor var4 : this.soundPool) {
                var2 -= var4.getWeight();
                if (var2 < 0) {
                    final SoundPoolEntry var5 = (SoundPoolEntry)var4.cloneEntry();
                    var5.setPitch(var5.getPitch() * this.eventPitch);
                    var5.setVolume(var5.getVolume() * this.eventVolume);
                    return var5;
                }
            }
            return SoundHandler.missing_sound;
        }
        return SoundHandler.missing_sound;
    }
    
    public void addSoundToEventPool(final ISoundEventAccessor p_148727_1_) {
        this.soundPool.add(p_148727_1_);
    }
    
    public ResourceLocation getSoundEventLocation() {
        return this.soundLocation;
    }
    
    public SoundCategory getSoundCategory() {
        return this.category;
    }
}
