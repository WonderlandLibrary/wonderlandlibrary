package net.minecraft.client.audio;

import net.minecraft.server.gui.IUpdatePlayerListBox;

public abstract interface ITickableSound
  extends ISound, IUpdatePlayerListBox
{
  public abstract boolean isDonePlaying();
}
