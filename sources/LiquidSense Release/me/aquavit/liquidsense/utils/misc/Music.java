package me.aquavit.liquidsense.utils.misc;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.BufferedInputStream;

public class Music {
    public static synchronized void playSound(String url) {
        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    BufferedInputStream buffInputStream = new BufferedInputStream(Music.class.getResourceAsStream("/assets/minecraft/liquidsense/sound/" + url));
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(buffInputStream);

                    clip.open(inputStream);
                    FloatControl gainControl =
                            (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(-17.0f);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }
}
