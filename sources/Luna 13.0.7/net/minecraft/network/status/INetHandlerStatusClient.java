package net.minecraft.network.status;

import net.minecraft.network.INetHandler;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;

public abstract interface INetHandlerStatusClient
  extends INetHandler
{
  public abstract void handleServerInfo(S00PacketServerInfo paramS00PacketServerInfo);
  
  public abstract void handlePong(S01PacketPong paramS01PacketPong);
}
