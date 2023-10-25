package net.minecraft.client.audio;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.Source;

class SoundManager.SoundSystemStarterThread
extends SoundSystem {
    private SoundManager.SoundSystemStarterThread() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean playing(String p_playing_1_) {
        Object object = SoundSystemConfig.THREAD_SYNC;
        synchronized (object) {
            if (this.soundLibrary == null) {
                return false;
            }
            Source source = (Source)this.soundLibrary.getSources().get((Object)p_playing_1_);
            boolean bl = source == null ? false : source.playing() || source.paused() || source.preLoad;
            return bl;
        }
    }
}
