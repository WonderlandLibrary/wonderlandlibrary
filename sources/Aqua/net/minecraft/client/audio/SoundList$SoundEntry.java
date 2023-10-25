package net.minecraft.client.audio;

import net.minecraft.client.audio.SoundList;

public static class SoundList.SoundEntry {
    private String name;
    private float volume = 1.0f;
    private float pitch = 1.0f;
    private int weight = 1;
    private Type type = Type.FILE;
    private boolean streaming = false;

    public String getSoundEntryName() {
        return this.name;
    }

    public void setSoundEntryName(String nameIn) {
        this.name = nameIn;
    }

    public float getSoundEntryVolume() {
        return this.volume;
    }

    public void setSoundEntryVolume(float volumeIn) {
        this.volume = volumeIn;
    }

    public float getSoundEntryPitch() {
        return this.pitch;
    }

    public void setSoundEntryPitch(float pitchIn) {
        this.pitch = pitchIn;
    }

    public int getSoundEntryWeight() {
        return this.weight;
    }

    public void setSoundEntryWeight(int weightIn) {
        this.weight = weightIn;
    }

    public Type getSoundEntryType() {
        return this.type;
    }

    public void setSoundEntryType(Type typeIn) {
        this.type = typeIn;
    }

    public boolean isStreaming() {
        return this.streaming;
    }

    public void setStreaming(boolean isStreaming) {
        this.streaming = isStreaming;
    }
}
