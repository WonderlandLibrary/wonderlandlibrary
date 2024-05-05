package net.minecraft.client.audio;

import net.minecraft.client.audio.SoundList;

static class SoundHandler.3 {
    static final /* synthetic */ int[] $SwitchMap$net$minecraft$client$audio$SoundList$SoundEntry$Type;

    static {
        $SwitchMap$net$minecraft$client$audio$SoundList$SoundEntry$Type = new int[SoundList.SoundEntry.Type.values().length];
        try {
            SoundHandler.3.$SwitchMap$net$minecraft$client$audio$SoundList$SoundEntry$Type[SoundList.SoundEntry.Type.FILE.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            SoundHandler.3.$SwitchMap$net$minecraft$client$audio$SoundList$SoundEntry$Type[SoundList.SoundEntry.Type.SOUND_EVENT.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
