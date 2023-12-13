// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.audio;

import net.minecraft.util.ITickable;

public interface ITickableSound extends ISound, ITickable
{
    boolean isDonePlaying();
}
