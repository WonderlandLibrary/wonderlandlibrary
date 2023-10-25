package nl.matsv.viabackwards.protocol.protocol1_13to1_13_1.packets;

import java.util.List;
import nl.matsv.viabackwards.ViaBackwards;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import nl.matsv.viabackwards.api.entities.storage.MetaStorage;
import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import nl.matsv.viabackwards.api.rewriters.LegacyEntityRewriter;
import nl.matsv.viabackwards.protocol.protocol1_13to1_13_1.Protocol1_13To1_13_1;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.entities.Entity1_13Types;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_13;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.Particle;
import us.myles.ViaVersion.api.type.types.version.Types1_13;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;

public class EntityPackets1_13_1 extends LegacyEntityRewriter<Protocol1_13To1_13_1> {
  public EntityPackets1_13_1(Protocol1_13To1_13_1 protocol) {
    super((BackwardsProtocol)protocol);
  }
  
  protected void registerPackets() {
    ((Protocol1_13To1_13_1)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SPAWN_ENTITY, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.UUID);
            map(Type.BYTE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.BYTE);
            map(Type.BYTE);
            map(Type.INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int entityId = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    byte type = ((Byte)wrapper.get(Type.BYTE, 0)).byteValue();
                    Entity1_13Types.EntityType entType = Entity1_13Types.getTypeFromId(type, true);
                    if (entType == null) {
                      ViaBackwards.getPlatform().getLogger().warning("Could not find 1.13 entity type " + type);
                      return;
                    } 
                    if (entType.is((EntityType)Entity1_13Types.EntityType.FALLING_BLOCK)) {
                      int data = ((Integer)wrapper.get(Type.INT, 0)).intValue();
                      wrapper.set(Type.INT, 0, Integer.valueOf(((Protocol1_13To1_13_1)EntityPackets1_13_1.this.protocol).getMappingData().getNewBlockStateId(data)));
                    } 
                    EntityPackets1_13_1.this.addTrackedEntity(wrapper, entityId, (EntityType)entType);
                  }
                });
          }
        });
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_13.SPAWN_EXPERIENCE_ORB, (EntityType)Entity1_13Types.EntityType.EXPERIENCE_ORB);
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_13.SPAWN_GLOBAL_ENTITY, (EntityType)Entity1_13Types.EntityType.LIGHTNING_BOLT);
    ((Protocol1_13To1_13_1)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SPAWN_MOB, new PacketRemapper() {
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
            map(Types1_13.METADATA_LIST);
            handler(EntityPackets1_13_1.this.getTrackerHandler());
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    MetaStorage storage = new MetaStorage((List)wrapper.get(Types1_13.METADATA_LIST, 0));
                    EntityPackets1_13_1.this.handleMeta(wrapper.user(), ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue(), storage);
                    wrapper.set(Types1_13.METADATA_LIST, 0, storage.getMetaDataList());
                  }
                });
          }
        });
    ((Protocol1_13To1_13_1)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SPAWN_PLAYER, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.UUID);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.BYTE);
            map(Type.BYTE);
            map(Types1_13.METADATA_LIST);
            handler(EntityPackets1_13_1.this.getTrackerAndMetaHandler(Types1_13.METADATA_LIST, (EntityType)Entity1_13Types.EntityType.PLAYER));
          }
        });
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_13.SPAWN_PAINTING, (EntityType)Entity1_13Types.EntityType.PAINTING);
    registerJoinGame((ClientboundPacketType)ClientboundPackets1_13.JOIN_GAME, (EntityType)Entity1_13Types.EntityType.PLAYER);
    registerRespawn((ClientboundPacketType)ClientboundPackets1_13.RESPAWN);
    registerEntityDestroy((ClientboundPacketType)ClientboundPackets1_13.DESTROY_ENTITIES);
    registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_13.ENTITY_METADATA, Types1_13.METADATA_LIST);
  }
  
  protected void registerRewrites() {
    registerMetaHandler().handle(e -> {
          Metadata meta = e.getData();
          if (meta.getMetaType() == MetaType1_13.Slot) {
            InventoryPackets1_13_1.toClient((Item)meta.getValue());
          } else if (meta.getMetaType() == MetaType1_13.BlockID) {
            int data = ((Integer)meta.getValue()).intValue();
            meta.setValue(Integer.valueOf(((Protocol1_13To1_13_1)this.protocol).getMappingData().getNewBlockStateId(data)));
          } else if (meta.getMetaType() == MetaType1_13.PARTICLE) {
            rewriteParticle((Particle)meta.getValue());
          } 
          return meta;
        });
    registerMetaHandler()
      .filter((EntityType)Entity1_13Types.EntityType.ABSTRACT_ARROW, true, 7)
      .removed();
    registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.SPECTRAL_ARROW, 8)
      .handleIndexChange(7);
    registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.TRIDENT, 8)
      .handleIndexChange(7);
    registerMetaHandler()
      .filter((EntityType)Entity1_13Types.EntityType.MINECART_ABSTRACT, true, 9)
      .handle(e -> {
          Metadata meta = e.getData();
          int data = ((Integer)meta.getValue()).intValue();
          meta.setValue(Integer.valueOf(((Protocol1_13To1_13_1)this.protocol).getMappingData().getNewBlockStateId(data)));
          return meta;
        });
  }
  
  protected EntityType getTypeFromId(int typeId) {
    return (EntityType)Entity1_13Types.getTypeFromId(typeId, false);
  }
  
  protected EntityType getObjectTypeFromId(int typeId) {
    return (EntityType)Entity1_13Types.getTypeFromId(typeId, true);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_13to1_13_1\packets\EntityPackets1_13_1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */