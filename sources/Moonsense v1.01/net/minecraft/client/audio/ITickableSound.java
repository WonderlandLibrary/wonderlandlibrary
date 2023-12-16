// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import net.minecraft.server.gui.IUpdatePlayerListBox;

public interface ITickableSound extends ISound, IUpdatePlayerListBox
{
    boolean isDonePlaying();
}
