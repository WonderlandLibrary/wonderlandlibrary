package net.minecraft.network;

import net.minecraft.util.IChatComponent;

public abstract interface INetHandler
{
  public abstract void onDisconnect(IChatComponent paramIChatComponent);
}
