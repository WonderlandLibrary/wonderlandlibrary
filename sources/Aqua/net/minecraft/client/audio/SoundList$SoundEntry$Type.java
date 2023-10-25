package net.minecraft.client.audio;

public static enum SoundList.SoundEntry.Type {
    FILE("file"),
    SOUND_EVENT("event");

    private final String field_148583_c;

    private SoundList.SoundEntry.Type(String p_i45109_3_) {
        this.field_148583_c = p_i45109_3_;
    }

    public static SoundList.SoundEntry.Type getType(String p_148580_0_) {
        for (SoundList.SoundEntry.Type soundlist$soundentry$type : SoundList.SoundEntry.Type.values()) {
            if (!soundlist$soundentry$type.field_148583_c.equals((Object)p_148580_0_)) continue;
            return soundlist$soundentry$type;
        }
        return null;
    }
}
