// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.helpers.misc;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import java.io.InputStream;
import java.io.BufferedInputStream;
import javax.sound.sampled.AudioSystem;
import ru.fluger.client.helpers.Helper;

public class SoundHelper implements Helper
{
    public static synchronized void playSound(final String url, final float volume, final boolean stop) {
        Clip clip;
        InputStream audioSrc;
        BufferedInputStream bufferedIn;
        AudioInputStream inputStream;
        FloatControl gainControl;
        new Thread(() -> {
            try {
                clip = AudioSystem.getClip();
                audioSrc = SoundHelper.class.getResourceAsStream("/assets/minecraft/nightmare/sounds/" + url);
                bufferedIn = new BufferedInputStream(audioSrc);
                inputStream = AudioSystem.getAudioInputStream(bufferedIn);
                clip.open(inputStream);
                gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
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
