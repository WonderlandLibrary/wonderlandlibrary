package com.github.creeper123123321.viafabric.protocol;

import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.protocol.SimpleProtocol;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.remapper.ValueTransformer;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.packets.State;

public class ViaFabricHostnameProtocol extends SimpleProtocol {
  public static final ViaFabricHostnameProtocol INSTANCE = new ViaFabricHostnameProtocol();
  
  protected void registerPackets() {
    registerIncoming(State.HANDSHAKE, 0, 0, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.STRING, new ValueTransformer<String, String>(Type.STRING) {
                  public String transform(PacketWrapper packetWrapper, String s) {
                    return s;
                  }
                });
          }
        });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\github\creeper123123321\viafabric\protocol\ViaFabricHostnameProtocol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */