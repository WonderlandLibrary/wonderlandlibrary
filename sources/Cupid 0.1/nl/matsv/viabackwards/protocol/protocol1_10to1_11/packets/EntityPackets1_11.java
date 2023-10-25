package nl.matsv.viabackwards.protocol.protocol1_10to1_11.packets;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import nl.matsv.viabackwards.api.entities.storage.EntityData;
import nl.matsv.viabackwards.api.entities.storage.EntityStorage;
import nl.matsv.viabackwards.api.entities.storage.MetaStorage;
import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import nl.matsv.viabackwards.api.rewriters.LegacyEntityRewriter;
import nl.matsv.viabackwards.protocol.protocol1_10to1_11.PotionSplashHandler;
import nl.matsv.viabackwards.protocol.protocol1_10to1_11.Protocol1_10To1_11;
import nl.matsv.viabackwards.protocol.protocol1_10to1_11.storage.ChestedHorseStorage;
import nl.matsv.viabackwards.utils.Block;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.UserConnection;
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

public class EntityPackets1_11 extends LegacyEntityRewriter<Protocol1_10To1_11> {
  public EntityPackets1_11(Protocol1_10To1_11 protocol) {
    super((BackwardsProtocol)protocol);
  }
  
  protected void registerPackets() {
    ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.EFFECT, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            map(Type.POSITION);
            map(Type.INT);
            handler(wrapper -> {
                  int type = ((Integer)wrapper.get(Type.INT, 0)).intValue();
                  if (type == 2002 || type == 2007) {
                    if (type == 2007)
                      wrapper.set(Type.INT, 0, Integer.valueOf(2002)); 
                    int mappedData = PotionSplashHandler.getOldData(((Integer)wrapper.get(Type.INT, 1)).intValue());
                    if (mappedData != -1)
                      wrapper.set(Type.INT, 1, Integer.valueOf(mappedData)); 
                  } 
                });
          }
        });
    ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_ENTITY, new PacketRemapper() {
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
            handler(EntityPackets1_11.this.getObjectTrackerHandler());
            handler(EntityPackets1_11.this.getObjectRewriter(id -> (ObjectType)Entity1_11Types.ObjectType.findById(id.byteValue()).orElse(null)));
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    Optional<Entity1_12Types.ObjectType> type = Entity1_12Types.ObjectType.findById(((Byte)wrapper.get(Type.BYTE, 0)).byteValue());
                    if (type.isPresent() && type.get() == Entity1_12Types.ObjectType.FALLING_BLOCK) {
                      int objectData = ((Integer)wrapper.get(Type.INT, 0)).intValue();
                      int objType = objectData & 0xFFF;
                      int data = objectData >> 12 & 0xF;
                      Block block = ((Protocol1_10To1_11)EntityPackets1_11.this.getProtocol()).getBlockItemPackets().handleBlock(objType, data);
                      if (block == null)
                        return; 
                      wrapper.set(Type.INT, 0, Integer.valueOf(block.getId() | block.getData() << 12));
                    } 
                  }
                });
          }
        });
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_EXPERIENCE_ORB, (EntityType)Entity1_11Types.EntityType.EXPERIENCE_ORB);
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_GLOBAL_ENTITY, (EntityType)Entity1_11Types.EntityType.WEATHER);
    ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_MOB, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.UUID);
            map((Type)Type.VAR_INT, Type.UNSIGNED_BYTE);
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
            handler(EntityPackets1_11.this.getTrackerHandler(Type.UNSIGNED_BYTE, 0));
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int entityId = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    EntityType type = EntityPackets1_11.this.getEntityType(wrapper.user(), entityId);
                    MetaStorage storage = new MetaStorage((List)wrapper.get(Types1_9.METADATA_LIST, 0));
                    EntityPackets1_11.this.handleMeta(wrapper
                        .user(), ((Integer)wrapper
                        .get((Type)Type.VAR_INT, 0)).intValue(), storage);
                    EntityData entityData = EntityPackets1_11.this.getEntityData(type);
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
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_PAINTING, (EntityType)Entity1_11Types.EntityType.PAINTING);
    registerJoinGame((ClientboundPacketType)ClientboundPackets1_9_3.JOIN_GAME, (EntityType)Entity1_11Types.EntityType.PLAYER);
    registerRespawn((ClientboundPacketType)ClientboundPackets1_9_3.RESPAWN);
    ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_PLAYER, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.UUID);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.BYTE);
            map(Type.BYTE);
            map(Types1_9.METADATA_LIST);
            handler(EntityPackets1_11.this.getTrackerAndMetaHandler(Types1_9.METADATA_LIST, (EntityType)Entity1_11Types.EntityType.PLAYER));
          }
        });
    registerEntityDestroy((ClientboundPacketType)ClientboundPackets1_9_3.DESTROY_ENTITIES);
    registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_9_3.ENTITY_METADATA, Types1_9.METADATA_LIST);
    ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.ENTITY_STATUS, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            map(Type.BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    byte b = ((Byte)wrapper.get(Type.BYTE, 0)).byteValue();
                    if (b == 35) {
                      wrapper.clearPacket();
                      wrapper.setId(30);
                      wrapper.write(Type.UNSIGNED_BYTE, Short.valueOf((short)10));
                      wrapper.write((Type)Type.FLOAT, Float.valueOf(0.0F));
                    } 
                  }
                });
          }
        });
  }
  
  protected void registerRewrites() {
    mapEntity((EntityType)Entity1_11Types.EntityType.ELDER_GUARDIAN, (EntityType)Entity1_11Types.EntityType.GUARDIAN);
    mapEntity((EntityType)Entity1_11Types.EntityType.WITHER_SKELETON, (EntityType)Entity1_11Types.EntityType.SKELETON).mobName("Wither Skeleton").spawnMetadata(storage -> storage.add(getSkeletonTypeMeta(1)));
    mapEntity((EntityType)Entity1_11Types.EntityType.STRAY, (EntityType)Entity1_11Types.EntityType.SKELETON).mobName("Stray").spawnMetadata(storage -> storage.add(getSkeletonTypeMeta(2)));
    mapEntity((EntityType)Entity1_11Types.EntityType.HUSK, (EntityType)Entity1_11Types.EntityType.ZOMBIE).mobName("Husk").spawnMetadata(storage -> handleZombieType(storage, 6));
    mapEntity((EntityType)Entity1_11Types.EntityType.ZOMBIE_VILLAGER, (EntityType)Entity1_11Types.EntityType.ZOMBIE).spawnMetadata(storage -> handleZombieType(storage, 1));
    mapEntity((EntityType)Entity1_11Types.EntityType.HORSE, (EntityType)Entity1_11Types.EntityType.HORSE).spawnMetadata(storage -> storage.add(getHorseMetaType(0)));
    mapEntity((EntityType)Entity1_11Types.EntityType.DONKEY, (EntityType)Entity1_11Types.EntityType.HORSE).spawnMetadata(storage -> storage.add(getHorseMetaType(1)));
    mapEntity((EntityType)Entity1_11Types.EntityType.MULE, (EntityType)Entity1_11Types.EntityType.HORSE).spawnMetadata(storage -> storage.add(getHorseMetaType(2)));
    mapEntity((EntityType)Entity1_11Types.EntityType.SKELETON_HORSE, (EntityType)Entity1_11Types.EntityType.HORSE).spawnMetadata(storage -> storage.add(getHorseMetaType(4)));
    mapEntity((EntityType)Entity1_11Types.EntityType.ZOMBIE_HORSE, (EntityType)Entity1_11Types.EntityType.HORSE).spawnMetadata(storage -> storage.add(getHorseMetaType(3)));
    mapEntity((EntityType)Entity1_11Types.EntityType.EVOCATION_FANGS, (EntityType)Entity1_11Types.EntityType.SHULKER);
    mapEntity((EntityType)Entity1_11Types.EntityType.EVOCATION_ILLAGER, (EntityType)Entity1_11Types.EntityType.VILLAGER).mobName("Evoker");
    mapEntity((EntityType)Entity1_11Types.EntityType.VEX, (EntityType)Entity1_11Types.EntityType.BAT).mobName("Vex");
    mapEntity((EntityType)Entity1_11Types.EntityType.VINDICATION_ILLAGER, (EntityType)Entity1_11Types.EntityType.VILLAGER).mobName("Vindicator").spawnMetadata(storage -> storage.add(new Metadata(13, (MetaType)MetaType1_9.VarInt, Integer.valueOf(4))));
    mapEntity((EntityType)Entity1_11Types.EntityType.LIAMA, (EntityType)Entity1_11Types.EntityType.HORSE).mobName("Llama").spawnMetadata(storage -> storage.add(getHorseMetaType(1)));
    mapEntity((EntityType)Entity1_11Types.EntityType.LIAMA_SPIT, (EntityType)Entity1_11Types.EntityType.SNOWBALL);
    mapObjectType((ObjectType)Entity1_11Types.ObjectType.LIAMA_SPIT, (ObjectType)Entity1_11Types.ObjectType.SNOWBALL, -1);
    mapObjectType((ObjectType)Entity1_11Types.ObjectType.EVOCATION_FANGS, (ObjectType)Entity1_11Types.ObjectType.FALLING_BLOCK, 4294);
    registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.GUARDIAN, true, 12).handle(e -> {
          Metadata data = e.getData();
          boolean b = ((Boolean)data.getValue()).booleanValue();
          int bitmask = b ? 2 : 0;
          if (e.getEntity().getType().is((EntityType)Entity1_11Types.EntityType.ELDER_GUARDIAN))
            bitmask |= 0x4; 
          data.setMetaType((MetaType)MetaType1_9.Byte);
          data.setValue(Byte.valueOf((byte)bitmask));
          return data;
        });
    registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.ABSTRACT_SKELETON, true, 12).handleIndexChange(13);
    registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.ZOMBIE, true).handle(e -> {
          Metadata data = e.getData();
          switch (data.getId()) {
            case 13:
              throw RemovedValueException.EX;
            case 14:
              data.setId(15);
              break;
            case 15:
              data.setId(14);
              break;
            case 16:
              data.setId(13);
              data.setValue(Integer.valueOf(1 + ((Integer)data.getValue()).intValue()));
              break;
          } 
          return data;
        });
    registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.EVOCATION_ILLAGER, 12).handle(e -> {
          Metadata data = e.getData();
          data.setId(13);
          data.setMetaType((MetaType)MetaType1_9.VarInt);
          data.setValue(Integer.valueOf(((Byte)data.getValue()).intValue()));
          return data;
        });
    registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.VEX, 12).handle(e -> {
          Metadata data = e.getData();
          data.setValue(Byte.valueOf((byte)0));
          return data;
        });
    registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.VINDICATION_ILLAGER, 12).handle(e -> {
          Metadata data = e.getData();
          data.setId(13);
          data.setMetaType((MetaType)MetaType1_9.VarInt);
          data.setValue(Integer.valueOf((((Number)data.getValue()).intValue() == 1) ? 2 : 4));
          return data;
        });
    registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.ABSTRACT_HORSE, true, 13).handle(e -> {
          Metadata data = e.getData();
          byte b = ((Byte)data.getValue()).byteValue();
          if (e.getEntity().has(ChestedHorseStorage.class) && ((ChestedHorseStorage)e.getEntity().get(ChestedHorseStorage.class)).isChested()) {
            b = (byte)(b | 0x8);
            data.setValue(Byte.valueOf(b));
          } 
          return data;
        });
    registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.CHESTED_HORSE, true).handle(e -> {
          if (!e.getEntity().has(ChestedHorseStorage.class))
            e.getEntity().put((EntityStorage)new ChestedHorseStorage()); 
          return e.getData();
        });
    registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.HORSE, 16).handleIndexChange(17);
    registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.CHESTED_HORSE, true, 15).handle(e -> {
          ChestedHorseStorage storage = (ChestedHorseStorage)e.getEntity().get(ChestedHorseStorage.class);
          boolean b = ((Boolean)e.getData().getValue()).booleanValue();
          storage.setChested(b);
          throw RemovedValueException.EX;
        });
    registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.LIAMA).handle(e -> {
          Metadata data = e.getData();
          ChestedHorseStorage storage = (ChestedHorseStorage)e.getEntity().get(ChestedHorseStorage.class);
          int index = e.getIndex();
          switch (index) {
            case 16:
              storage.setLiamaStrength(((Integer)data.getValue()).intValue());
              throw RemovedValueException.EX;
            case 17:
              storage.setLiamaCarpetColor(((Integer)data.getValue()).intValue());
              throw RemovedValueException.EX;
            case 18:
              storage.setLiamaVariant(((Integer)data.getValue()).intValue());
              throw RemovedValueException.EX;
          } 
          return e.getData();
        });
    registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.ABSTRACT_HORSE, true, 14).handleIndexChange(16);
    registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.VILLAGER, 13).handle(e -> {
          Metadata data = e.getData();
          if (((Integer)data.getValue()).intValue() == 5)
            data.setValue(Integer.valueOf(0)); 
          return data;
        });
    registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.SHULKER, 15).removed();
  }
  
  private Metadata getSkeletonTypeMeta(int type) {
    return new Metadata(12, (MetaType)MetaType1_9.VarInt, Integer.valueOf(type));
  }
  
  private Metadata getZombieTypeMeta(int type) {
    return new Metadata(13, (MetaType)MetaType1_9.VarInt, Integer.valueOf(type));
  }
  
  private void handleZombieType(MetaStorage storage, int type) {
    Metadata meta = storage.get(13);
    if (meta == null)
      storage.add(getZombieTypeMeta(type)); 
  }
  
  private Metadata getHorseMetaType(int type) {
    return new Metadata(14, (MetaType)MetaType1_9.VarInt, Integer.valueOf(type));
  }
  
  protected EntityType getTypeFromId(int typeId) {
    return (EntityType)Entity1_11Types.getTypeFromId(typeId, false);
  }
  
  protected EntityType getObjectTypeFromId(int typeId) {
    return (EntityType)Entity1_11Types.getTypeFromId(typeId, true);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_10to1_11\packets\EntityPackets1_11.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */