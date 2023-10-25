package nl.matsv.viabackwards.protocol.protocol1_9_4to1_10.packets;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import nl.matsv.viabackwards.api.entities.storage.EntityData;
import nl.matsv.viabackwards.api.entities.storage.MetaStorage;
import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import nl.matsv.viabackwards.api.rewriters.LegacyEntityRewriter;
import nl.matsv.viabackwards.protocol.protocol1_9_4to1_10.Protocol1_9_4To1_10;
import nl.matsv.viabackwards.utils.Block;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import us.myles.ViaVersion.api.entities.Entity1_11Types;
import us.myles.ViaVersion.api.entities.Entity1_12Types;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.entities.ObjectType;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_9;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.version.Types1_9;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;

public class EntityPackets1_10 extends LegacyEntityRewriter<Protocol1_9_4To1_10> {
  public EntityPackets1_10(Protocol1_9_4To1_10 protocol) {
    super((BackwardsProtocol)protocol);
  }
  
  protected void registerPackets() {
    ((Protocol1_9_4To1_10)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_ENTITY, new PacketRemapper() {
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
            handler(EntityPackets1_10.this.getObjectTrackerHandler());
            handler(EntityPackets1_10.this.getObjectRewriter(id -> (ObjectType)Entity1_11Types.ObjectType.findById(id.byteValue()).orElse(null)));
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    Optional<Entity1_12Types.ObjectType> type = Entity1_12Types.ObjectType.findById(((Byte)wrapper.get(Type.BYTE, 0)).byteValue());
                    if (type.isPresent() && type.get() == Entity1_12Types.ObjectType.FALLING_BLOCK) {
                      int objectData = ((Integer)wrapper.get(Type.INT, 0)).intValue();
                      int objType = objectData & 0xFFF;
                      int data = objectData >> 12 & 0xF;
                      Block block = ((Protocol1_9_4To1_10)EntityPackets1_10.this.getProtocol()).getBlockItemPackets().handleBlock(objType, data);
                      if (block == null)
                        return; 
                      wrapper.set(Type.INT, 0, Integer.valueOf(block.getId() | block.getData() << 12));
                    } 
                  }
                });
          }
        });
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_EXPERIENCE_ORB, (EntityType)Entity1_10Types.EntityType.EXPERIENCE_ORB);
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_GLOBAL_ENTITY, (EntityType)Entity1_10Types.EntityType.WEATHER);
    ((Protocol1_9_4To1_10)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_MOB, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.UUID);
            map(Type.UNSIGNED_BYTE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.BYTE);
            map(Type.BYTE);
            map(Type.BYTE);
            map((Type)Type.SHORT);
            map((Type)Type.SHORT);
            map((Type)Type.SHORT);
            map(Types1_9.METADATA_LIST);
            handler(EntityPackets1_10.this.getTrackerHandler(Type.UNSIGNED_BYTE, 0));
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int entityId = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    EntityType type = EntityPackets1_10.this.getEntityType(wrapper.user(), entityId);
                    MetaStorage storage = new MetaStorage((List)wrapper.get(Types1_9.METADATA_LIST, 0));
                    EntityPackets1_10.this.handleMeta(wrapper
                        .user(), ((Integer)wrapper
                        .get((Type)Type.VAR_INT, 0)).intValue(), storage);
                    EntityData entityData = EntityPackets1_10.this.getEntityData(type);
                    if (entityData != null) {
                      wrapper.set(Type.UNSIGNED_BYTE, 0, Short.valueOf((short)entityData.getReplacementId()));
                      if (entityData.hasBaseMeta())
                        entityData.getDefaultMeta().createMeta(storage); 
                    } 
                    wrapper.set(Types1_9.METADATA_LIST, 0, storage
                        
                        .getMetaDataList());
                  }
                });
          }
        });
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_PAINTING, (EntityType)Entity1_10Types.EntityType.PAINTING);
    registerJoinGame((ClientboundPacketType)ClientboundPackets1_9_3.JOIN_GAME, (EntityType)Entity1_10Types.EntityType.PLAYER);
    registerRespawn((ClientboundPacketType)ClientboundPackets1_9_3.RESPAWN);
    ((Protocol1_9_4To1_10)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_PLAYER, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.UUID);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.BYTE);
            map(Type.BYTE);
            map(Types1_9.METADATA_LIST);
            handler(EntityPackets1_10.this.getTrackerAndMetaHandler(Types1_9.METADATA_LIST, (EntityType)Entity1_11Types.EntityType.PLAYER));
          }
        });
    registerEntityDestroy((ClientboundPacketType)ClientboundPackets1_9_3.DESTROY_ENTITIES);
    registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_9_3.ENTITY_METADATA, Types1_9.METADATA_LIST);
  }
  
  protected void registerRewrites() {
    mapEntity((EntityType)Entity1_10Types.EntityType.POLAR_BEAR, (EntityType)Entity1_10Types.EntityType.SHEEP).mobName("Polar Bear");
    registerMetaHandler().filter((EntityType)Entity1_10Types.EntityType.POLAR_BEAR, 13).handle(e -> {
          Metadata data = e.getData();
          boolean b = ((Boolean)data.getValue()).booleanValue();
          data.setMetaType((MetaType)MetaType1_9.Byte);
          data.setValue(Byte.valueOf(b ? 14 : 0));
          return data;
        });
    registerMetaHandler().filter((EntityType)Entity1_10Types.EntityType.ZOMBIE, 13).handle(e -> {
          Metadata data = e.getData();
          if (((Integer)data.getValue()).intValue() == 6)
            data.setValue(Integer.valueOf(0)); 
          return data;
        });
    registerMetaHandler().filter((EntityType)Entity1_10Types.EntityType.SKELETON, 12).handle(e -> {
          Metadata data = e.getData();
          if (((Integer)data.getValue()).intValue() == 2)
            data.setValue(Integer.valueOf(0)); 
          return data;
        });
    registerMetaHandler().handle(e -> {
          Metadata data = e.getData();
          if (data.getId() == 5)
            throw RemovedValueException.EX; 
          if (data.getId() >= 5)
            data.setId(data.getId() - 1); 
          return data;
        });
  }
  
  protected EntityType getTypeFromId(int typeId) {
    return (EntityType)Entity1_10Types.getTypeFromId(typeId, false);
  }
  
  protected EntityType getObjectTypeFromId(int typeId) {
    return (EntityType)Entity1_10Types.getTypeFromId(typeId, true);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_9_4to1_10\packets\EntityPackets1_10.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */