package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundList;

public class SoundList {
    private final List<SoundEntry> soundList = Lists.newArrayList();
    private boolean replaceExisting;
    private SoundCategory category;

    public List<SoundEntry> getSoundList() {
        return this.soundList;
    }

    public boolean canReplaceExisting() {
        return this.replaceExisting;
    }

    public void setReplaceExisting(boolean p_148572_1_) {
        this.replaceExisting = p_148572_1_;
    }

    public SoundCategory getSoundCategory() {
        return this.category;
    }

    public void setSoundCategory(SoundCategory soundCat) {
        this.category = soundCat;
    }
}
