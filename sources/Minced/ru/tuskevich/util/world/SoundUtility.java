// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.world;

import net.minecraft.init.SoundEvents;
import net.minecraft.client.Minecraft;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import java.io.InputStream;
import java.io.BufferedInputStream;
import javax.sound.sampled.AudioSystem;

public class SoundUtility
{
    public static float DEFAULT_PITCH;
    
    public static void playSound(final String sound, final float volume) {
        Clip clip;
        InputStream audioSrc;
        BufferedInputStream bufferedIn;
        AudioInputStream inputStream;
        FloatControl gainControl;
        new Thread(() -> {
            try {
                clip = AudioSystem.getClip();
                audioSrc = SoundUtility.class.getResourceAsStream("/assets/minecraft/client/sounds/" + sound);
                bufferedIn = new BufferedInputStream(audioSrc);
                inputStream = AudioSystem.getAudioInputStream(bufferedIn);
                clip.open(inputStream);
                gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume);
                clip.start();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    public static void playSound() {
        Minecraft.getMinecraft();
        Minecraft.player.playSound(SoundEvents.BLOCK_NOTE_PLING, 1.0f, 1.7f);
    }
    
    public static void playSound(final float pitch, final float volume) {
        Minecraft.getMinecraft();
        if (Minecraft.player == null) {
            return;
        }
        Minecraft.getMinecraft();
        Minecraft.player.playSound(SoundEvents.BLOCK_NOTE_PLING, volume, pitch);
    }
    
    public static void playSound(final float pitch) {
        playSound(pitch, 3.0f);
    }
    
    static {
        SoundUtility.DEFAULT_PITCH = 1.7f;
    }
}
