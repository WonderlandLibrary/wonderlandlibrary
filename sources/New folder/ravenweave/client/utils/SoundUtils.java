package ravenweave.client.utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.HashMap;

public class SoundUtils {

    private static final HashMap<String, AudioInputStream> sounds = new HashMap<String, AudioInputStream>();
    public static Clip clip;

    public static void playSound(String name) {
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(SoundUtils.class.getResource("/assets/ravenweave/sounds/" + name + ".wav")));
            clip.start();
        } catch (Exception e) {
            System.out.println("Error with playing sound.");
            e.printStackTrace();
        }
    }

}
