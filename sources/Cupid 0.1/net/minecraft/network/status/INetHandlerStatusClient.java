package net.minecraft.network.status;

import net.minecraft.network.INetHandler;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;

public interface INetHandlerStatusClient extends INetHandler {
  void handleServerInfo(S00PacketServerInfo paramS00PacketServerInfo);
  
  void handlePong(S01PacketPong paramS01PacketPong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\network\status\INetHandlerStatusClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */