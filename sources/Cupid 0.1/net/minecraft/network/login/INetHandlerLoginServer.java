package net.minecraft.network.login;

import net.minecraft.network.INetHandler;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;

public interface INetHandlerLoginServer extends INetHandler {
  void processLoginStart(C00PacketLoginStart paramC00PacketLoginStart);
  
  void processEncryptionResponse(C01PacketEncryptionResponse paramC01PacketEncryptionResponse);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\network\login\INetHandlerLoginServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */