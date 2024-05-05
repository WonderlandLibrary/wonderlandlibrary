package net.minecraft.client.audio;

public static enum ISound.AttenuationType {
    NONE(0),
    LINEAR(2);

    private final int type;

    private ISound.AttenuationType(int typeIn) {
        this.type = typeIn;
    }

    public int getTypeInt() {
        return this.type;
    }
}
