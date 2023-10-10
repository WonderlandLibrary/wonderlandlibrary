// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.api.rewriters;

import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import com.viaversion.viaversion.api.rewriter.RewriterBase;
import com.viaversion.viabackwards.api.BackwardsProtocol;

@Deprecated
public abstract class LegacySoundRewriter<T extends BackwardsProtocol> extends RewriterBase<T>
{
    protected final Int2ObjectMap<SoundData> soundRewrites;
    
    protected LegacySoundRewriter(final T protocol) {
        super(protocol);
        this.soundRewrites = new Int2ObjectOpenHashMap<SoundData>(64);
    }
    
    public SoundData added(final int id, final int replacement) {
        return this.added(id, replacement, -1.0f);
    }
    
    public SoundData added(final int id, final int replacement, final float newPitch) {
        final SoundData data = new SoundData(replacement, true, newPitch, true);
        this.soundRewrites.put(id, data);
        return data;
    }
    
    public SoundData removed(final int id) {
        final SoundData data = new SoundData(-1, false, -1.0f, false);
        this.soundRewrites.put(id, data);
        return data;
    }
    
    public int handleSounds(final int soundId) {
        int newSoundId = soundId;
        final SoundData data = this.soundRewrites.get(soundId);
        if (data != null) {
            return data.getReplacementSound();
        }
        for (final Int2ObjectMap.Entry<SoundData> entry : this.soundRewrites.int2ObjectEntrySet()) {
            if (soundId > entry.getIntKey()) {
                if (entry.getValue().isAdded()) {
                    --newSoundId;
                }
                else {
                    ++newSoundId;
                }
            }
        }
        return newSoundId;
    }
    
    public boolean hasPitch(final int soundId) {
        final SoundData data = this.soundRewrites.get(soundId);
        return data != null && data.isChangePitch();
    }
    
    public float handlePitch(final int soundId) {
        final SoundData data = this.soundRewrites.get(soundId);
        return (data != null) ? data.getNewPitch() : 1.0f;
    }
    
    public static final class SoundData
    {
        private final int replacementSound;
        private final boolean changePitch;
        private final float newPitch;
        private final boolean added;
        
        public SoundData(final int replacementSound, final boolean changePitch, final float newPitch, final boolean added) {
            this.replacementSound = replacementSound;
            this.changePitch = changePitch;
            this.newPitch = newPitch;
            this.added = added;
        }
        
        public int getReplacementSound() {
            return this.replacementSound;
        }
        
        public boolean isChangePitch() {
            return this.changePitch;
        }
        
        public float getNewPitch() {
            return this.newPitch;
        }
        
        public boolean isAdded() {
            return this.added;
        }
    }
}
