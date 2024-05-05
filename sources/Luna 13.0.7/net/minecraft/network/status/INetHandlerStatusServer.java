package net.minecraft.network.status;

import net.minecraft.network.INetHandler;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;

public abstract interface INetHandlerStatusServer
  extends INetHandler
{
  public abstract void processPing(C01PacketPing paramC01PacketPing);
  
  public abstract void processServerQuery(C00PacketServerQuery paramC00PacketServerQuery);
}
