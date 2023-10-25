package nl.matsv.viabackwards.protocol.protocol1_16_3to1_16_4;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.protocol.protocol1_16_3to1_16_4.storage.PlayerHandStorage;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.ServerboundPackets1_16_2;

public class Protocol1_16_3To1_16_4 extends BackwardsProtocol<ClientboundPackets1_16_2, ClientboundPackets1_16_2, ServerboundPackets1_16_2, ServerboundPackets1_16_2> {
  public Protocol1_16_3To1_16_4() {
    super(ClientboundPackets1_16_2.class, ClientboundPackets1_16_2.class, ServerboundPackets1_16_2.class, ServerboundPackets1_16_2.class);
  }
  
  protected void registerPackets() {
    registerIncoming((ServerboundPacketType)ServerboundPackets1_16_2.EDIT_BOOK, new PacketRemapper() {
          public void registerMap() {
            map(Type.FLAT_VAR_INT_ITEM);
            map(Type.BOOLEAN);
            handler(wrapper -> {
                  int slot = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                  if (slot == 1) {
                    wrapper.write((Type)Type.VAR_INT, Integer.valueOf(40));
                  } else {
                    wrapper.write((Type)Type.VAR_INT, Integer.valueOf(((PlayerHandStorage)wrapper.user().get(PlayerHandStorage.class)).getCurrentHand()));
                  } 
                });
          }
        });
    registerIncoming((ServerboundPacketType)ServerboundPackets1_16_2.HELD_ITEM_CHANGE, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  short slot = ((Short)wrapper.passthrough((Type)Type.SHORT)).shortValue();
                  ((PlayerHandStorage)wrapper.user().get(PlayerHandStorage.class)).setCurrentHand(slot);
                });
          }
        });
  }
  
  public void init(UserConnection user) {
    user.put((StoredObject)new PlayerHandStorage(user));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_16_3to1_16_4\Protocol1_16_3To1_16_4.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */