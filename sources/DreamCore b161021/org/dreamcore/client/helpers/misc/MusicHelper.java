package org.dreamcore.client.helpers.misc;

import org.dreamcore.client.dreamcore;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class MusicHelper {

    public static void playSound(final String url) {
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(dreamcore.class.getResourceAsStream("/assets/minecraft/dreamcore/songs/" + url));
                clip.open(inputStream);
                clip.start();
            } catch (Exception e) {

            }
        }).start();
    }

}
