// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.utils.sound;

import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.AudioSystem;
import java.net.URL;
import javax.sound.sampled.Clip;

public class SoundUtil
{
    private static Clip clip;
    public static final URL button;
    public static final URL toggleOnSound;
    public static final URL toggleOffSound;
    public static final URL loginSuccessful;
    public static final URL loginFailed;
    
    public static void play(final URL filePath) {
        try {
            (SoundUtil.clip = AudioSystem.getClip()).open(AudioSystem.getAudioInputStream(filePath));
            final FloatControl floatControl = (FloatControl)SoundUtil.clip.getControl(FloatControl.Type.MASTER_GAIN);
            floatControl.setValue(0.0f);
            SoundUtil.clip.start();
        }
        catch (LineUnavailableException | UnsupportedAudioFileException | IOException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
        }
    }
    
    static {
        button = SoundUtil.class.getClassLoader().getResource("ressources/sounds/buttonClick.wav");
        toggleOnSound = SoundUtil.class.getClassLoader().getResource("ressources/sounds/toggleSound.wav");
        toggleOffSound = SoundUtil.class.getClassLoader().getResource("ressources/sounds/toggleSound2.wav");
        loginSuccessful = SoundUtil.class.getClassLoader().getResource("ressources/sounds/loginSuccessful.wav");
        loginFailed = SoundUtil.class.getClassLoader().getResource("ressources/sounds/loginFailed.wav");
    }
}
