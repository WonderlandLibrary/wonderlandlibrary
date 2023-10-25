package de.gerrygames.viarewind.utils;

import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.protocol.Protocol;

public interface ServerSender {
  void sendToServer(PacketWrapper paramPacketWrapper, Class<? extends Protocol> paramClass, boolean paramBoolean1, boolean paramBoolean2) throws Exception;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewin\\utils\ServerSender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */