package net.minecraft.client.audio;

import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundManager;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemLogger;

/*
 * Exception performing whole class analysis ignored.
 */
class SoundManager.1
implements Runnable {
    SoundManager.1() {
    }

    public void run() {
        SoundSystemConfig.setLogger((SoundSystemLogger)new /* Unavailable Anonymous Inner Class!! */);
        SoundManager soundManager = SoundManager.this;
        soundManager.getClass();
        SoundManager.access$102((SoundManager)SoundManager.this, (SoundManager.SoundSystemStarterThread)new SoundManager.SoundSystemStarterThread(soundManager, null));
        SoundManager.access$302((SoundManager)SoundManager.this, (boolean)true);
        SoundManager.access$100((SoundManager)SoundManager.this).setMasterVolume(SoundManager.access$400((SoundManager)SoundManager.this).getSoundLevel(SoundCategory.MASTER));
        SoundManager.access$000().info(SoundManager.access$500(), "Sound engine started");
    }
}
