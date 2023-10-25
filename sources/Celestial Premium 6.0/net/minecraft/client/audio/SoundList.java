/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.audio;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.audio.Sound;

public class SoundList {
    private final List<Sound> sounds;
    private final boolean replaceExisting;
    private final String subtitle;

    public SoundList(List<Sound> soundsIn, boolean replceIn, String subtitleIn) {
        this.sounds = soundsIn;
        this.replaceExisting = replceIn;
        this.subtitle = subtitleIn;
    }

    public List<Sound> getSounds() {
        return this.sounds;
    }

    public boolean canReplaceExisting() {
        return this.replaceExisting;
    }

    @Nullable
    public String getSubtitle() {
        return this.subtitle;
    }
}

