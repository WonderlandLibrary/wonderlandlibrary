package nl.matsv.viabackwards.protocol.protocol1_14to1_14_1.packets;

import java.util.List;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.entities.storage.MetaStorage;
import nl.matsv.viabackwards.api.rewriters.LegacyEntityRewriter;
import nl.matsv.viabackwards.protocol.protocol1_14to1_14_1.Protocol1_14To1_14_1;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.entities.Entity1_14Types;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.version.Types1_14;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;

public class EntityPackets1_14_1 extends LegacyEntityRewriter<Protocol1_14To1_14_1> {
  public EntityPackets1_14_1(Protocol1_14To1_14_1 protocol) {
    super((BackwardsProtocol)protocol);
  }
  
  protected void registerPackets() {
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_14.SPAWN_EXPERIENCE_ORB, (EntityType)Entity1_14Types.EntityType.EXPERIENCE_ORB);
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_14.SPAWN_GLOBAL_ENTITY, (EntityType)Entity1_14Types.EntityType.LIGHTNING_BOLT);
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_14.SPAWN_PAINTING, (EntityType)Entity1_14Types.EntityType.PAINTING);
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_14.SPAWN_PLAYER, (EntityType)Entity1_14Types.EntityType.PLAYER);
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_14.JOIN_GAME, (EntityType)Entity1_14Types.EntityType.PLAYER, Type.INT);
    registerEntityDestroy((ClientboundPacketType)ClientboundPackets1_14.DESTROY_ENTITIES);
    ((Protocol1_14To1_14_1)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.SPAWN_ENTITY, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.UUID);
            map((Type)Type.VAR_INT);
            handler(EntityPackets1_14_1.this.getTrackerHandler());
          }
        });
    ((Protocol1_14To1_14_1)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.SPAWN_MOB, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.UUID);
            map((Type)Type.VAR_INT);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.BYTE);
            map(Type.BYTE);
            map(Type.BYTE);
            map((Type)Type.SHORT);
            map((Type)Type.SHORT);
            map((Type)Type.SHORT);
            map(Types1_14.METADATA_LIST);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int entityId = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    int type = ((Integer)wrapper.get((Type)Type.VAR_INT, 1)).intValue();
                    EntityPackets1_14_1.this.addTrackedEntity(wrapper, entityId, (EntityType)Entity1_14Types.getTypeFromId(type));
                    MetaStorage storage = new MetaStorage((List)wrapper.get(Types1_14.METADATA_LIST, 0));
                    EntityPackets1_14_1.this.handleMeta(wrapper.user(), entityId, storage);
                  }
                });
          }
        });
    registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_14.ENTITY_METADATA, Types1_14.METADATA_LIST);
  }
  
  protected void registerRewrites() {
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.VILLAGER, 15).removed();
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.VILLAGER, 16).handleIndexChange(15);
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.WANDERING_TRADER, 15).removed();
  }
  
  protected EntityType getTypeFromId(int typeId) {
    return (EntityType)Entity1_14Types.getTypeFromId(typeId);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_14to1_14_1\packets\EntityPackets1_14_1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */