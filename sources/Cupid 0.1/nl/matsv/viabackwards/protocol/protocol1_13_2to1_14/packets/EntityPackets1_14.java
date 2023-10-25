package nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.packets;

import java.util.function.Supplier;
import nl.matsv.viabackwards.ViaBackwards;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.entities.meta.MetaHandler;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import nl.matsv.viabackwards.api.entities.storage.EntityData;
import nl.matsv.viabackwards.api.entities.storage.EntityPositionHandler;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import nl.matsv.viabackwards.api.rewriters.EntityRewriterBase;
import nl.matsv.viabackwards.api.rewriters.LegacyEntityRewriter;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.Protocol1_13_2To1_14;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.storage.ChunkLightStorage;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.storage.EntityPositionStorage1_14;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.entities.Entity1_13Types;
import us.myles.ViaVersion.api.entities.Entity1_14Types;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.minecraft.VillagerData;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_13_2;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.Particle;
import us.myles.ViaVersion.api.type.types.version.Types1_13_2;
import us.myles.ViaVersion.api.type.types.version.Types1_14;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.viaversion.libs.fastutil.ints.Int2IntMap;

public class EntityPackets1_14 extends LegacyEntityRewriter<Protocol1_13_2To1_14> {
  private EntityPositionHandler positionHandler;
  
  public EntityPackets1_14(Protocol1_13_2To1_14 protocol) {
    super((BackwardsProtocol)protocol, (MetaType)MetaType1_13_2.OptChat, (MetaType)MetaType1_13_2.Boolean);
  }
  
  protected void addTrackedEntity(PacketWrapper wrapper, int entityId, EntityType type) throws Exception {
    super.addTrackedEntity(wrapper, entityId, type);
    if (type == Entity1_14Types.EntityType.PAINTING) {
      Position position = (Position)wrapper.get(Type.POSITION, 0);
      this.positionHandler.cacheEntityPosition(wrapper, position.getX(), position.getY(), position.getZ(), true, false);
    } else if (wrapper.getId() != 37) {
      this.positionHandler.cacheEntityPosition(wrapper, true, false);
    } 
  }
  
  protected void registerPackets() {
    this.positionHandler = new EntityPositionHandler((EntityRewriterBase)this, EntityPositionStorage1_14.class, EntityPositionStorage1_14::new);
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.ENTITY_STATUS, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  int entityId = ((Integer)wrapper.passthrough(Type.INT)).intValue();
                  byte status = ((Byte)wrapper.passthrough(Type.BYTE)).byteValue();
                  if (status != 3)
                    return; 
                  EntityTracker.ProtocolEntityTracker tracker = EntityPackets1_14.this.getEntityTracker(wrapper.user());
                  EntityType entityType = tracker.getEntityType(entityId);
                  if (entityType != Entity1_14Types.EntityType.PLAYER)
                    return; 
                  for (int i = 0; i <= 5; i++) {
                    PacketWrapper equipmentPacket = wrapper.create(66);
                    equipmentPacket.write((Type)Type.VAR_INT, Integer.valueOf(entityId));
                    equipmentPacket.write((Type)Type.VAR_INT, Integer.valueOf(i));
                    equipmentPacket.write(Type.FLAT_VAR_INT_ITEM, null);
                    equipmentPacket.send(Protocol1_13_2To1_14.class, true, true);
                  } 
                });
          }
        });
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.ENTITY_TELEPORT, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            handler(wrapper -> EntityPackets1_14.this.positionHandler.cacheEntityPosition(wrapper, false, false));
          }
        });
    PacketRemapper relativeMoveHandler = new PacketRemapper() {
        public void registerMap() {
          map((Type)Type.VAR_INT);
          map((Type)Type.SHORT);
          map((Type)Type.SHORT);
          map((Type)Type.SHORT);
          handler(new PacketHandler() {
                public void handle(PacketWrapper wrapper) throws Exception {
                  double x = ((Short)wrapper.get((Type)Type.SHORT, 0)).shortValue() / 4096.0D;
                  double y = ((Short)wrapper.get((Type)Type.SHORT, 1)).shortValue() / 4096.0D;
                  double z = ((Short)wrapper.get((Type)Type.SHORT, 2)).shortValue() / 4096.0D;
                  EntityPackets1_14.this.positionHandler.cacheEntityPosition(wrapper, x, y, z, false, true);
                }
              });
        }
      };
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.ENTITY_POSITION, relativeMoveHandler);
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.ENTITY_POSITION_AND_ROTATION, relativeMoveHandler);
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.SPAWN_ENTITY, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.UUID);
            map((Type)Type.VAR_INT, Type.BYTE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.BYTE);
            map(Type.BYTE);
            map(Type.INT);
            map((Type)Type.SHORT);
            map((Type)Type.SHORT);
            map((Type)Type.SHORT);
            handler(EntityPackets1_14.this.getObjectTrackerHandler());
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    Entity1_13Types.ObjectType objectType;
                    int id = ((Byte)wrapper.get(Type.BYTE, 0)).byteValue();
                    int mappedId = EntityPackets1_14.this.getOldEntityId(id);
                    Entity1_13Types.EntityType entityType = Entity1_13Types.getTypeFromId(mappedId, false);
                    if (entityType.isOrHasParent((EntityType)Entity1_13Types.EntityType.MINECART_ABSTRACT)) {
                      objectType = Entity1_13Types.ObjectType.MINECART;
                      int i = 0;
                      switch (entityType) {
                        case CHEST_MINECART:
                          i = 1;
                          break;
                        case FURNACE_MINECART:
                          i = 2;
                          break;
                        case TNT_MINECART:
                          i = 3;
                          break;
                        case SPAWNER_MINECART:
                          i = 4;
                          break;
                        case HOPPER_MINECART:
                          i = 5;
                          break;
                        case COMMAND_BLOCK_MINECART:
                          i = 6;
                          break;
                      } 
                      if (i != 0)
                        wrapper.set(Type.INT, 0, Integer.valueOf(i)); 
                    } else {
                      objectType = Entity1_13Types.ObjectType.fromEntityType(entityType).orElse(null);
                    } 
                    if (objectType == null)
                      return; 
                    wrapper.set(Type.BYTE, 0, Byte.valueOf((byte)objectType.getId()));
                    int data = ((Integer)wrapper.get(Type.INT, 0)).intValue();
                    if (objectType == Entity1_13Types.ObjectType.FALLING_BLOCK) {
                      int blockState = ((Integer)wrapper.get(Type.INT, 0)).intValue();
                      int combined = ((Protocol1_13_2To1_14)EntityPackets1_14.this.protocol).getMappingData().getNewBlockStateId(blockState);
                      wrapper.set(Type.INT, 0, Integer.valueOf(combined));
                    } else if (entityType.isOrHasParent((EntityType)Entity1_13Types.EntityType.ABSTRACT_ARROW)) {
                      wrapper.set(Type.INT, 0, Integer.valueOf(data + 1));
                    } 
                  }
                });
          }
        });
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.SPAWN_MOB, new PacketRemapper() {
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
            map(Types1_14.METADATA_LIST, Types1_13_2.METADATA_LIST);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int type = ((Integer)wrapper.get((Type)Type.VAR_INT, 1)).intValue();
                    Entity1_14Types.EntityType entityType = Entity1_14Types.getTypeFromId(type);
                    EntityPackets1_14.this.addTrackedEntity(wrapper, ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue(), (EntityType)entityType);
                    int oldId = EntityPackets1_14.this.typeMapping.get(type);
                    if (oldId == -1) {
                      EntityData entityData = EntityPackets1_14.this.getEntityData((EntityType)entityType);
                      if (entityData == null) {
                        ViaBackwards.getPlatform().getLogger().warning("Could not find 1.13.2 entity type for 1.14 entity type " + type + "/" + entityType);
                        wrapper.cancel();
                      } else {
                        wrapper.set((Type)Type.VAR_INT, 1, Integer.valueOf(entityData.getReplacementId()));
                      } 
                    } else {
                      wrapper.set((Type)Type.VAR_INT, 1, Integer.valueOf(oldId));
                    } 
                  }
                });
            handler(EntityPackets1_14.this.getMobSpawnRewriter(Types1_13_2.METADATA_LIST));
          }
        });
    ((Protocol1_13_2To1_14)getProtocol()).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.SPAWN_EXPERIENCE_ORB, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            handler(wrapper -> EntityPackets1_14.this.addTrackedEntity(wrapper, ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue(), (EntityType)Entity1_14Types.EntityType.EXPERIENCE_ORB));
          }
        });
    ((Protocol1_13_2To1_14)getProtocol()).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.SPAWN_GLOBAL_ENTITY, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.BYTE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            handler(wrapper -> EntityPackets1_14.this.addTrackedEntity(wrapper, ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue(), (EntityType)Entity1_14Types.EntityType.LIGHTNING_BOLT));
          }
        });
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.SPAWN_PAINTING, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.UUID);
            map((Type)Type.VAR_INT);
            map(Type.POSITION1_14, Type.POSITION);
            map(Type.BYTE);
            handler(wrapper -> EntityPackets1_14.this.addTrackedEntity(wrapper, ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue(), (EntityType)Entity1_14Types.EntityType.PAINTING));
          }
        });
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.SPAWN_PLAYER, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.UUID);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.BYTE);
            map(Type.BYTE);
            map(Types1_14.METADATA_LIST, Types1_13_2.METADATA_LIST);
            handler(EntityPackets1_14.this.getTrackerAndMetaHandler(Types1_13_2.METADATA_LIST, (EntityType)Entity1_14Types.EntityType.PLAYER));
            handler(wrapper -> EntityPackets1_14.this.positionHandler.cacheEntityPosition(wrapper, true, false));
          }
        });
    registerEntityDestroy((ClientboundPacketType)ClientboundPackets1_14.DESTROY_ENTITIES);
    registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_14.ENTITY_METADATA, Types1_14.METADATA_LIST, Types1_13_2.METADATA_LIST);
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.JOIN_GAME, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            map(Type.UNSIGNED_BYTE);
            map(Type.INT);
            handler(EntityPackets1_14.this.getTrackerHandler((EntityType)Entity1_14Types.EntityType.PLAYER, Type.INT));
            handler(EntityPackets1_14.this.getDimensionHandler(1));
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    wrapper.write(Type.UNSIGNED_BYTE, Short.valueOf((short)0));
                    wrapper.passthrough(Type.UNSIGNED_BYTE);
                    wrapper.passthrough(Type.STRING);
                    wrapper.read((Type)Type.VAR_INT);
                  }
                });
          }
        });
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.RESPAWN, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    ClientWorld clientWorld = (ClientWorld)wrapper.user().get(ClientWorld.class);
                    int dimensionId = ((Integer)wrapper.get(Type.INT, 0)).intValue();
                    clientWorld.setEnvironment(dimensionId);
                    wrapper.write(Type.UNSIGNED_BYTE, Short.valueOf((short)0));
                    ((ChunkLightStorage)wrapper.user().get(ChunkLightStorage.class)).clear();
                  }
                });
          }
        });
  }
  
  protected void registerRewrites() {
    mapTypes((EntityType[])Entity1_14Types.EntityType.values(), Entity1_13Types.EntityType.class);
    mapEntity((EntityType)Entity1_14Types.EntityType.CAT, (EntityType)Entity1_14Types.EntityType.OCELOT).jsonName("Cat");
    mapEntity((EntityType)Entity1_14Types.EntityType.TRADER_LLAMA, (EntityType)Entity1_14Types.EntityType.LLAMA).jsonName("Trader Llama");
    mapEntity((EntityType)Entity1_14Types.EntityType.FOX, (EntityType)Entity1_14Types.EntityType.WOLF).jsonName("Fox");
    mapEntity((EntityType)Entity1_14Types.EntityType.PANDA, (EntityType)Entity1_14Types.EntityType.POLAR_BEAR).jsonName("Panda");
    mapEntity((EntityType)Entity1_14Types.EntityType.PILLAGER, (EntityType)Entity1_14Types.EntityType.VILLAGER).jsonName("Pillager");
    mapEntity((EntityType)Entity1_14Types.EntityType.WANDERING_TRADER, (EntityType)Entity1_14Types.EntityType.VILLAGER).jsonName("Wandering Trader");
    mapEntity((EntityType)Entity1_14Types.EntityType.RAVAGER, (EntityType)Entity1_14Types.EntityType.COW).jsonName("Ravager");
    registerMetaHandler().handle(e -> {
          Metadata meta = e.getData();
          int typeId = meta.getMetaType().getTypeID();
          if (typeId <= 15)
            meta.setMetaType((MetaType)MetaType1_13_2.byId(typeId)); 
          MetaType type = meta.getMetaType();
          if (type == MetaType1_13_2.Slot) {
            Item item = (Item)meta.getValue();
            meta.setValue(((Protocol1_13_2To1_14)getProtocol()).getBlockItemPackets().handleItemToClient(item));
          } else if (type == MetaType1_13_2.BlockID) {
            int blockstate = ((Integer)meta.getValue()).intValue();
            meta.setValue(Integer.valueOf(((Protocol1_13_2To1_14)this.protocol).getMappingData().getNewBlockStateId(blockstate)));
          } 
          return meta;
        });
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.PILLAGER, 15).removed();
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.FOX, 15).removed();
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.FOX, 16).removed();
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.FOX, 17).removed();
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.FOX, 18).removed();
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.PANDA, 15).removed();
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.PANDA, 16).removed();
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.PANDA, 17).removed();
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.PANDA, 18).removed();
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.PANDA, 19).removed();
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.PANDA, 20).removed();
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.CAT, 18).removed();
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.CAT, 19).removed();
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.CAT, 20).removed();
    registerMetaHandler().handle(e -> {
          EntityType type = e.getEntity().getType();
          Metadata meta = e.getData();
          if (type.isOrHasParent((EntityType)Entity1_14Types.EntityType.ABSTRACT_ILLAGER_BASE) || type == Entity1_14Types.EntityType.RAVAGER || type == Entity1_14Types.EntityType.WITCH) {
            int index = e.getIndex();
            if (index == 14)
              throw RemovedValueException.EX; 
            if (index > 14)
              meta.setId(index - 1); 
          } 
          return meta;
        });
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.AREA_EFFECT_CLOUD, 10).handle(e -> {
          Metadata meta = e.getData();
          rewriteParticle((Particle)meta.getValue());
          return meta;
        });
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.FIREWORK_ROCKET, 8).handle(e -> {
          Metadata meta = e.getData();
          meta.setMetaType((MetaType)MetaType1_13_2.VarInt);
          Integer value = (Integer)meta.getValue();
          if (value == null)
            meta.setValue(Integer.valueOf(0)); 
          return meta;
        });
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.ABSTRACT_ARROW, true).handle(e -> {
          Metadata meta = e.getData();
          int index = e.getIndex();
          if (index == 9)
            throw RemovedValueException.EX; 
          if (index > 9)
            meta.setId(index - 1); 
          return meta;
        });
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.VILLAGER, 15).removed();
    MetaHandler villagerDataHandler = e -> {
        Metadata meta = e.getData();
        VillagerData villagerData = (VillagerData)meta.getValue();
        meta.setValue(Integer.valueOf(villagerDataToProfession(villagerData)));
        meta.setMetaType((MetaType)MetaType1_13_2.VarInt);
        if (meta.getId() == 16)
          meta.setId(15); 
        return meta;
      };
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.ZOMBIE_VILLAGER, 18).handle(villagerDataHandler);
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.VILLAGER, 16).handle(villagerDataHandler);
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.ABSTRACT_SKELETON, true, 13).handle(e -> {
          byte value = ((Byte)e.getData().getValue()).byteValue();
          if ((value & 0x4) != 0)
            e.createMeta(new Metadata(14, (MetaType)MetaType1_13_2.Boolean, Boolean.valueOf(true))); 
          return e.getData();
        });
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.ZOMBIE, true, 13).handle(e -> {
          byte value = ((Byte)e.getData().getValue()).byteValue();
          if ((value & 0x4) != 0)
            e.createMeta(new Metadata(16, (MetaType)MetaType1_13_2.Boolean, Boolean.valueOf(true))); 
          return e.getData();
        });
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.ZOMBIE, true).handle(e -> {
          Metadata meta = e.getData();
          int index = e.getIndex();
          if (index >= 16)
            meta.setId(index + 1); 
          return meta;
        });
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.LIVINGENTITY, true).handle(e -> {
          Metadata meta = e.getData();
          int index = e.getIndex();
          if (index == 12) {
            Position position = (Position)meta.getValue();
            if (position != null) {
              PacketWrapper wrapper = new PacketWrapper(51, null, e.getUser());
              wrapper.write((Type)Type.VAR_INT, Integer.valueOf(e.getEntity().getEntityId()));
              wrapper.write(Type.POSITION, position);
              try {
                wrapper.send(Protocol1_13_2To1_14.class);
              } catch (Exception ex) {
                ex.printStackTrace();
              } 
            } 
            throw RemovedValueException.EX;
          } 
          if (index > 12)
            meta.setId(index - 1); 
          return meta;
        });
    registerMetaHandler().handle(e -> {
          Metadata meta = e.getData();
          int index = e.getIndex();
          if (index == 6)
            throw RemovedValueException.EX; 
          if (index > 6)
            meta.setId(index - 1); 
          return meta;
        });
    registerMetaHandler().handle(e -> {
          Metadata meta = e.getData();
          int typeId = meta.getMetaType().getTypeID();
          if (typeId > 15) {
            ViaBackwards.getPlatform().getLogger().warning("New 1.14 metadata was not handled: " + meta + " entity: " + e.getEntity().getType());
            return null;
          } 
          return meta;
        });
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.OCELOT, 13).handle(e -> {
          Metadata meta = e.getData();
          meta.setId(15);
          meta.setMetaType((MetaType)MetaType1_13_2.VarInt);
          meta.setValue(Integer.valueOf(0));
          return meta;
        });
    registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.CAT).handle(e -> {
          Metadata meta = e.getData();
          if (meta.getId() == 15) {
            meta.setValue(Integer.valueOf(1));
          } else if (meta.getId() == 13) {
            meta.setValue(Byte.valueOf((byte)(((Byte)meta.getValue()).byteValue() & 0x4)));
          } 
          return meta;
        });
  }
  
  public int villagerDataToProfession(VillagerData data) {
    switch (data.getProfession()) {
      case 1:
      case 10:
      case 13:
      case 14:
        return 3;
      case 2:
      case 8:
        return 4;
      case 3:
      case 9:
        return 1;
      case 4:
        return 2;
      case 5:
      case 6:
      case 7:
      case 12:
        return 0;
    } 
    return 5;
  }
  
  protected EntityType getTypeFromId(int typeId) {
    return (EntityType)Entity1_14Types.getTypeFromId(typeId);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_13_2to1_14\packets\EntityPackets1_14.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */