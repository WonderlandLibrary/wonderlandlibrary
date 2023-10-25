package net.minecraft.client.audio;

import net.minecraft.client.audio.ISoundEventAccessor;
import net.minecraft.client.audio.SoundEventAccessorComposite;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundList;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.util.ResourceLocation;

/*
 * Exception performing whole class analysis ignored.
 */
class SoundHandler.2
implements ISoundEventAccessor<SoundPoolEntry> {
    final ResourceLocation field_148726_a;
    final /* synthetic */ String val$s1;
    final /* synthetic */ SoundList.SoundEntry val$soundlist$soundentry;

    SoundHandler.2(String string, SoundList.SoundEntry soundEntry) {
        this.val$s1 = string;
        this.val$soundlist$soundentry = soundEntry;
        this.field_148726_a = new ResourceLocation(this.val$s1, this.val$soundlist$soundentry.getSoundEntryName());
    }

    public int getWeight() {
        SoundEventAccessorComposite soundeventaccessorcomposite1 = (SoundEventAccessorComposite)SoundHandler.access$000((SoundHandler)SoundHandler.this).getObject((Object)this.field_148726_a);
        return soundeventaccessorcomposite1 == null ? 0 : soundeventaccessorcomposite1.getWeight();
    }

    public SoundPoolEntry cloneEntry() {
        SoundEventAccessorComposite soundeventaccessorcomposite1 = (SoundEventAccessorComposite)SoundHandler.access$000((SoundHandler)SoundHandler.this).getObject((Object)this.field_148726_a);
        return soundeventaccessorcomposite1 == null ? SoundHandler.missing_sound : soundeventaccessorcomposite1.cloneEntry();
    }
}
