// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.audio;

public interface ISoundEventAccessor<T>
{
    int getWeight();
    
    T cloneEntry();
}
