// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.misc;

import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import java.io.InputStream;
import javax.sound.sampled.AudioSystem;
import java.io.BufferedInputStream;
import net.minecraft.util.ResourceLocation;
import dev.tenacity.utils.Utils;

public class SoundUtils implements Utils
{
    public static void playSound(final ResourceLocation location, final float volume) {
        BufferedInputStream bufferedInputStream;
        AudioInputStream audioIn;
        Clip clip;
        FloatControl gainControl;
        float range;
        float gain;
        final Exception ex;
        Exception e;
        Multithreading.runAsync(() -> {
            try {
                bufferedInputStream = new BufferedInputStream(SoundUtils.mc.getResourceManager().getResource(location).getInputStream());
                audioIn = AudioSystem.getAudioInputStream(bufferedInputStream);
                clip = AudioSystem.getClip();
                clip.open(audioIn);
                gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                range = gainControl.getMaximum() - gainControl.getMinimum();
                gain = range * volume + gainControl.getMinimum();
                gainControl.setValue(gain);
                clip.start();
            }
            catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex2) {
                e = ex;
                e.printStackTrace();
            }
        });
    }
}
