/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.ResourceLocation
 */
package dev.sakura_starring.util.sound;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Objects;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class SoundFxPlayer {
    public static void playSound(SoundType soundType, float f) {
        new Thread(() -> SoundFxPlayer.lambda$playSound$0(soundType, f)).start();
    }

    private static void lambda$playSound$0(SoundType soundType, float f) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(Objects.requireNonNull(Minecraft.func_71410_x().func_110442_L().func_110536_a(new ResourceLocation("atfield/sound/" + soundType.getName())).func_110527_b())));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            FloatControl floatControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            floatControl.setValue(f);
            clip.start();
        }
        catch (IOException | LineUnavailableException | UnsupportedAudioFileException exception) {
            exception.printStackTrace();
        }
    }

    public static enum SoundType {
        KaedeharaKazuha("kaedehara_kazuha.wav"),
        Enable("enable.wav"),
        Disable("disable.wav");

        final String name;

        String getName() {
            return this.name;
        }

        /*
         * WARNING - Possible parameter corruption
         * WARNING - void declaration
         */
        private SoundType() {
            void var3_1;
            this.name = var3_1;
        }
    }
}

