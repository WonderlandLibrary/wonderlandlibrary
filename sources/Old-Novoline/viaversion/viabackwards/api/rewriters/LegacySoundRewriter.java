package viaversion.viabackwards.api.rewriters;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import viaversion.viabackwards.api.BackwardsProtocol;

@Deprecated
public abstract class LegacySoundRewriter<T extends BackwardsProtocol> extends Rewriter<T> {
    protected final Int2ObjectMap<SoundData> soundRewrites = new Int2ObjectOpenHashMap<>(64);

    protected LegacySoundRewriter(T protocol) {
        super(protocol);
    }

    public SoundData added(int id, int replacement) {
        return added(id, replacement, -1);
    }

    public SoundData added(int id, int replacement, float newPitch) {
        SoundData data = new SoundData(replacement, true, newPitch, true);
        soundRewrites.put(id, data);
        return data;
    }

    public SoundData removed(int id) {
        SoundData data = new SoundData(-1, false, -1, false);
        soundRewrites.put(id, data);
        return data;
    }

    public int handleSounds(int soundId) {
        int newSoundId = soundId;
        SoundData data = soundRewrites.get(soundId);
        if (data != null) return data.getReplacementSound();

        for (Int2ObjectMap.Entry<SoundData> entry : soundRewrites.int2ObjectEntrySet()) {
            if (soundId > entry.getIntKey()) {
                if (entry.getValue().isAdded()) {
                    newSoundId--;
                } else {
                    newSoundId++;
                }
            }
        }
        return newSoundId;
    }

    public boolean hasPitch(int soundId) {
        SoundData data = soundRewrites.get(soundId);
        return data != null && data.isChangePitch();
    }

    public float handlePitch(int soundId) {
        SoundData data = soundRewrites.get(soundId);
        return data != null ? data.getNewPitch() : 1F;
    }

    public static final class SoundData {
        private final int replacementSound;
        private final boolean changePitch;
        private final float newPitch;
        private final boolean added;

        public SoundData(int replacementSound, boolean changePitch, float newPitch, boolean added) {
            this.replacementSound = replacementSound;
            this.changePitch = changePitch;
            this.newPitch = newPitch;
            this.added = added;
        }

        public int getReplacementSound() {
            return replacementSound;
        }

        public boolean isChangePitch() {
            return changePitch;
        }

        public float getNewPitch() {
            return newPitch;
        }

        public boolean isAdded() {
            return added;
        }
    }
}
