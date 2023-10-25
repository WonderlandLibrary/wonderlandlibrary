package nl.matsv.viabackwards.protocol.protocol1_12_1to1_12_2;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
import us.myles.ViaVersion.protocols.protocol1_12_1to1_12.ServerboundPackets1_12_1;

public class Protocol1_12_1To1_12_2 extends BackwardsProtocol<ClientboundPackets1_12_1, ClientboundPackets1_12_1, ServerboundPackets1_12_1, ServerboundPackets1_12_1> {
  public Protocol1_12_1To1_12_2() {
    super(ClientboundPackets1_12_1.class, ClientboundPackets1_12_1.class, ServerboundPackets1_12_1.class, ServerboundPackets1_12_1.class);
  }
  
  protected void registerPackets() {
    registerOutgoing((ClientboundPacketType)ClientboundPackets1_12_1.KEEP_ALIVE, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    Long keepAlive = (Long)packetWrapper.read(Type.LONG);
                    ((KeepAliveTracker)packetWrapper.user().get(KeepAliveTracker.class)).setKeepAlive(keepAlive.longValue());
                    packetWrapper.write((Type)Type.VAR_INT, Integer.valueOf(keepAlive.hashCode()));
                  }
                });
          }
        });
    registerIncoming((ServerboundPacketType)ServerboundPackets1_12_1.KEEP_ALIVE, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int keepAlive = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                    long realKeepAlive = ((KeepAliveTracker)packetWrapper.user().get(KeepAliveTracker.class)).getKeepAlive();
                    if (keepAlive != Long.hashCode(realKeepAlive)) {
                      packetWrapper.cancel();
                      return;
                    } 
                    packetWrapper.write(Type.LONG, Long.valueOf(realKeepAlive));
                    ((KeepAliveTracker)packetWrapper.user().get(KeepAliveTracker.class)).setKeepAlive(2147483647L);
                  }
                });
          }
        });
  }
  
  public void init(UserConnection userConnection) {
    userConnection.put(new KeepAliveTracker(userConnection));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_12_1to1_12_2\Protocol1_12_1To1_12_2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */