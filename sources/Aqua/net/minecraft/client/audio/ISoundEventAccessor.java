package net.minecraft.client.audio;

public interface ISoundEventAccessor<T> {
    public int getWeight();

    public T cloneEntry();
}
