// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.misc;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import vip.Resolute.Resolute;
import javax.sound.sampled.AudioSystem;

public class PlaySound
{
    public static synchronized void playSound(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Clip clip = AudioSystem.getClip();
                    final AudioInputStream inputStream = AudioSystem.getAudioInputStream(Resolute.class.getResourceAsStream("/assets/minecraft/resolute/" + url));
                    clip.open(inputStream);
                    clip.start();
                }
                catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }
}
