package dev.rise.ui.mainmenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class MainMenuMusic {
    private final Minecraft mc = Minecraft.getMinecraft();
    public ISound currentMusic;
    public boolean shouldKeepPlaying = true;

    public void update() {
        if (currentMusic == null && shouldKeepPlaying) {
            startPlaying();
        }
    }

    public void startPlaying() {
        currentMusic = new PositionedSoundRecord(new ResourceLocation("rise.mainmenu"), 0.25f, 1, true, 1, ISound.AttenuationType.LINEAR, 0, 0, 0);
        mc.getSoundHandler().playSound(currentMusic);
    }

    public void stopPlaying() {
        if (currentMusic != null) {
            mc.getSoundHandler().stopSounds();
            currentMusic = null;
        }
    }
}
