package net.minecraft.client.audio;

import net.minecraft.util.ITickable;

public interface ITickableSound extends ITickable, ISound {
   boolean isDonePlaying();
}
