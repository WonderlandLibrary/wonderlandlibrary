/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.utils.misc.sound;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class TipSoundPlayer {
    private final File file;

    public TipSoundPlayer(File file) {
        this.file = file;
    }

    public void asyncPlay(final float volume) {
        Thread thread2 = new Thread(){

            @Override
            public void run() {
                TipSoundPlayer.this.playSound(volume / 100.0f);
            }
        };
        thread2.start();
    }

    public void playSound(float volume) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            FloatControl controller = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = controller.getMaximum() - controller.getMinimum();
            float value = range * volume + controller.getMinimum();
            controller.setValue(value);
            clip.start();
        }
        catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }
}

