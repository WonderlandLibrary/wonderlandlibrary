package net.minecraft.network.handshake;

import net.minecraft.network.INetHandler;
import net.minecraft.network.handshake.client.C00Handshake;

public abstract interface INetHandlerHandshakeServer
  extends INetHandler
{
  public abstract void processHandshake(C00Handshake paramC00Handshake);
}
