package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C00PacketKeepAlive implements Packet<INetHandlerPlayServer> {
  public int key;
  
  public C00PacketKeepAlive() {}
  
  public C00PacketKeepAlive(int key) {
    this.key = key;
  }
  
  public void processPacket(INetHandlerPlayServer handler) {
    handler.processKeepAlive(this);
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.key = buf.readVarIntFromBuffer();
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeVarIntToBuffer(this.key);
  }
  
  public int getKey() {
    return this.key;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\network\play\client\C00PacketKeepAlive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */