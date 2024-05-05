package tech.drainwalk.utility.sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class Sound {
    public synchronized void playSound(String url, float volume) {
        try {
            Clip clip = AudioSystem.getClip();
            InputStream audioSrc = Sound.class.getResourceAsStream("/assets/minecraft/drainwalk/sounds/" + url);
            assert audioSrc != null;
            BufferedInputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(bufferedIn);
            clip.open(inputStream);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-40.0f + (volume / 10.0f) * 3.0f);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
