package net.minecraft.block;

public static class Block.SoundType {
    public final String soundName;
    public final float volume;
    public final float frequency;

    public Block.SoundType(String name, float volume, float frequency) {
        this.soundName = name;
        this.volume = volume;
        this.frequency = frequency;
    }

    public float getVolume() {
        return this.volume;
    }

    public float getFrequency() {
        return this.frequency;
    }

    public String getBreakSound() {
        return "dig." + this.soundName;
    }

    public String getStepSound() {
        return "step." + this.soundName;
    }

    public String getPlaceSound() {
        return this.getBreakSound();
    }
}
