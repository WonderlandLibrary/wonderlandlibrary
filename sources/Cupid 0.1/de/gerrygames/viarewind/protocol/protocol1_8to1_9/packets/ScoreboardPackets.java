package de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets;

import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.packets.State;

public class ScoreboardPackets {
  public static void register(Protocol protocol) {
    protocol.registerOutgoing(State.PLAY, 56, 61);
    protocol.registerOutgoing(State.PLAY, 63, 59);
    protocol.registerOutgoing(State.PLAY, 65, 62, new PacketRemapper() {
          public void registerMap() {
            map(Type.STRING);
            map(Type.BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    byte mode = ((Byte)packetWrapper.get(Type.BYTE, 0)).byteValue();
                    if (mode == 0 || mode == 2) {
                      packetWrapper.passthrough(Type.STRING);
                      packetWrapper.passthrough(Type.STRING);
                      packetWrapper.passthrough(Type.STRING);
                      packetWrapper.passthrough(Type.BYTE);
                      packetWrapper.passthrough(Type.STRING);
                      packetWrapper.read(Type.STRING);
                      packetWrapper.passthrough(Type.BYTE);
                    } 
                    if (mode == 0 || mode == 3 || mode == 4)
                      packetWrapper.passthrough(Type.STRING_ARRAY); 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 66, 60);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_8to1_9\packets\ScoreboardPackets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */