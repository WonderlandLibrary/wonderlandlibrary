/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.helpers.sound;

import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import org.celestial.client.helpers.Helper;

public class SoundHelper
implements Helper {
    public static synchronized void playSound(String url, float volume, boolean stop) {
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                InputStream audioSrc = SoundHelper.class.getResourceAsStream("/assets/minecraft/celestial/sounds/" + url);
                BufferedInputStream bufferedIn = new BufferedInputStream(audioSrc);
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(bufferedIn);
                clip.open(inputStream);
                FloatControl gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume);
                clip.start();
                if (stop) {
                    clip.stop();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}

