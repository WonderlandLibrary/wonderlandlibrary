package nl.matsv.viabackwards.protocol.protocol1_11_1to1_12;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.data.ShoulderTracker;
import nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.packets.BlockItemPackets1_12;
import nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.packets.ChatPackets1_12;
import nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.packets.EntityPackets1_12;
import nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.packets.SoundPackets1_12;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.ClientboundPackets1_12;
import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.ServerboundPackets1_12;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import us.myles.viaversion.libs.gson.JsonElement;

public class Protocol1_11_1To1_12 extends BackwardsProtocol<ClientboundPackets1_12, ClientboundPackets1_9_3, ServerboundPackets1_12, ServerboundPackets1_9_3> {
  private EntityPackets1_12 entityPackets;
  
  private BlockItemPackets1_12 blockItemPackets;
  
  public Protocol1_11_1To1_12() {
    super(ClientboundPackets1_12.class, ClientboundPackets1_9_3.class, ServerboundPackets1_12.class, ServerboundPackets1_9_3.class);
  }
  
  protected void registerPackets() {
    (this.entityPackets = new EntityPackets1_12(this)).register();
    (this.blockItemPackets = new BlockItemPackets1_12(this)).register();
    (new SoundPackets1_12(this)).register();
    (new ChatPackets1_12(this)).register();
    registerOutgoing((ClientboundPacketType)ClientboundPackets1_12.TITLE, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  int action = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
                  if (action >= 0 && action <= 2) {
                    JsonElement component = (JsonElement)wrapper.read(Type.COMPONENT);
                    wrapper.write(Type.COMPONENT, Protocol1_9To1_8.fixJson(component.toString()));
                  } 
                });
          }
        });
    cancelOutgoing((ClientboundPacketType)ClientboundPackets1_12.ADVANCEMENTS);
    cancelOutgoing((ClientboundPacketType)ClientboundPackets1_12.UNLOCK_RECIPES);
    cancelOutgoing((ClientboundPacketType)ClientboundPackets1_12.SELECT_ADVANCEMENTS_TAB);
  }
  
  public void init(UserConnection user) {
    if (!user.has(ClientWorld.class))
      user.put((StoredObject)new ClientWorld(user)); 
    if (!user.has(EntityTracker.class))
      user.put((StoredObject)new EntityTracker(user)); 
    user.put((StoredObject)new ShoulderTracker(user));
    ((EntityTracker)user.get(EntityTracker.class)).initProtocol(this);
  }
  
  public EntityPackets1_12 getEntityPackets() {
    return this.entityPackets;
  }
  
  public BlockItemPackets1_12 getBlockItemPackets() {
    return this.blockItemPackets;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_11_1to1_12\Protocol1_11_1To1_12.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */