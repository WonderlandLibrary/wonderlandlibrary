// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import javax.annotation.Nullable;
import java.util.List;

public class SoundList
{
    private final List<Sound> sounds;
    private final boolean replaceExisting;
    private final String subtitle;
    
    public SoundList(final List<Sound> soundsIn, final boolean replceIn, final String subtitleIn) {
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
