/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.misc.sound;

import java.io.File;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.FileUtils;
import net.ccbluex.liquidbounce.utils.misc.sound.TipSoundPlayer;

public final class TipSoundManager {
    private TipSoundPlayer enableSound;
    private TipSoundPlayer disableSound;

    public final TipSoundPlayer getEnableSound() {
        return this.enableSound;
    }

    public final void setEnableSound(TipSoundPlayer tipSoundPlayer) {
        this.enableSound = tipSoundPlayer;
    }

    public final TipSoundPlayer getDisableSound() {
        return this.disableSound;
    }

    public final void setDisableSound(TipSoundPlayer tipSoundPlayer) {
        this.disableSound = tipSoundPlayer;
    }

    public TipSoundManager() {
        File enableSoundFile = new File(LiquidBounce.INSTANCE.getFileManager().soundsDir, "enable.wav");
        File disableSoundFile = new File(LiquidBounce.INSTANCE.getFileManager().soundsDir, "disable.wav");
        if (!enableSoundFile.exists()) {
            FileUtils.unpackFile(enableSoundFile, "assets/minecraft/killer/sound/enable.wav");
        }
        if (!disableSoundFile.exists()) {
            FileUtils.unpackFile(disableSoundFile, "assets/minecraft/killer/sound/disable.wav");
        }
        this.enableSound = new TipSoundPlayer(enableSoundFile);
        this.disableSound = new TipSoundPlayer(disableSoundFile);
    }
}

