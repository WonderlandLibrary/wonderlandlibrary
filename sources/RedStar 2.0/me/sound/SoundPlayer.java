package me.sound;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Objects;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer {
    public void playSound(SoundType st, float volume) {
        new Thread(() -> {
            try {
                AudioInputStream as = AudioSystem.getAudioInputStream(new BufferedInputStream(Objects.requireNonNull(this.getClass().getResourceAsStream("/assets/minecraft/pride/sound/" + st.getName()))));
                Clip clip = AudioSystem.getClip();
                clip.open(as);
                clip.start();
                FloatControl gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume);
                clip.start();
            }
            catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static enum SoundType {
        Enter("enter.wav"),
        Notification("notification.wav"),
        Startup("startup.wav"),
        ClickGuiOpen("clickguiopen.wav"),
        Ding("dingsound.wav"),
        Crack("cracksound.wav"),
        EDITION("ingame.wav"),
        VICTORY("victory.wav"),
        BACKDOOL("back.wav"),
        SKEET("skeet.wav"),
        NEKO("neko.wav"),
        SPECIAL("spec.wav");

        final String name;

        private SoundType(String fileName) {
            this.name = fileName;
        }

        String getName() {
            return this.name;
        }
    }
}
