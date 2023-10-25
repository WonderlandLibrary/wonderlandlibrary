package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.packets;

import java.util.Optional;
import nl.matsv.viabackwards.ViaBackwards;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import nl.matsv.viabackwards.api.entities.storage.EntityPositionHandler;
import nl.matsv.viabackwards.api.entities.storage.MetaStorage;
import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import nl.matsv.viabackwards.api.rewriters.LegacyEntityRewriter;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.Protocol1_12_2To1_13;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.data.EntityTypeMapping;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.data.PaintingMapping;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.data.ParticleMapping;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.storage.BackwardsBlockStorage;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.storage.PlayerPositionStorage1_13;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.entities.Entity1_13Types;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_12;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.Particle;
import us.myles.ViaVersion.api.type.types.version.Types1_12;
import us.myles.ViaVersion.api.type.types.version.Types1_13;
import us.myles.ViaVersion.protocols.protocol1_12_1to1_12.ServerboundPackets1_12_1;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ChatRewriter;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;

public class EntityPackets1_13 extends LegacyEntityRewriter<Protocol1_12_2To1_13> {
  public EntityPackets1_13(Protocol1_12_2To1_13 protocol) {
    super((BackwardsProtocol)protocol);
  }
  
  protected void registerPackets() {
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.PLAYER_POSITION, new PacketRemapper() {
          public void registerMap() {
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map(Type.BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    if (!ViaBackwards.getConfig().isFix1_13FacePlayer())
                      return; 
                    PlayerPositionStorage1_13 playerStorage = (PlayerPositionStorage1_13)wrapper.user().get(PlayerPositionStorage1_13.class);
                    byte bitField = ((Byte)wrapper.get(Type.BYTE, 0)).byteValue();
                    playerStorage.setX(toSet(bitField, 0, playerStorage.getX(), ((Double)wrapper.get(Type.DOUBLE, 0)).doubleValue()));
                    playerStorage.setY(toSet(bitField, 1, playerStorage.getY(), ((Double)wrapper.get(Type.DOUBLE, 1)).doubleValue()));
                    playerStorage.setZ(toSet(bitField, 2, playerStorage.getZ(), ((Double)wrapper.get(Type.DOUBLE, 2)).doubleValue()));
                  }
                  
                  private double toSet(int field, int bitIndex, double origin, double packetValue) {
                    return ((field & 1 << bitIndex) != 0) ? (origin + packetValue) : packetValue;
                  }
                });
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SPAWN_ENTITY, new PacketRemapper() {
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
            handler(EntityPackets1_13.this.getObjectTrackerHandler());
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    Optional<Entity1_13Types.ObjectType> optionalType = Entity1_13Types.ObjectType.findById(((Byte)wrapper.get(Type.BYTE, 0)).byteValue());
                    if (!optionalType.isPresent())
                      return; 
                    Entity1_13Types.ObjectType type = optionalType.get();
                    if (type == Entity1_13Types.ObjectType.FALLING_BLOCK) {
                      int blockState = ((Integer)wrapper.get(Type.INT, 0)).intValue();
                      int combined = Protocol1_12_2To1_13.MAPPINGS.getNewBlockStateId(blockState);
                      combined = combined >> 4 & 0xFFF | (combined & 0xF) << 12;
                      wrapper.set(Type.INT, 0, Integer.valueOf(combined));
                    } else if (type == Entity1_13Types.ObjectType.ITEM_FRAME) {
                      int data = ((Integer)wrapper.get(Type.INT, 0)).intValue();
                      switch (data) {
                        case 3:
                          data = 0;
                          break;
                        case 4:
                          data = 1;
                          break;
                        case 5:
                          data = 3;
                          break;
                      } 
                      wrapper.set(Type.INT, 0, Integer.valueOf(data));
                    } else if (type == Entity1_13Types.ObjectType.TRIDENT) {
                      wrapper.set(Type.BYTE, 0, Byte.valueOf((byte)Entity1_13Types.ObjectType.TIPPED_ARROW.getId()));
                    } 
                  }
                });
          }
        });
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_13.SPAWN_EXPERIENCE_ORB, (EntityType)Entity1_13Types.EntityType.EXPERIENCE_ORB);
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_13.SPAWN_GLOBAL_ENTITY, (EntityType)Entity1_13Types.EntityType.LIGHTNING_BOLT);
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SPAWN_MOB, new PacketRemapper() {
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
            map(Types1_13.METADATA_LIST, Types1_12.METADATA_LIST);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int type = ((Integer)wrapper.get((Type)Type.VAR_INT, 1)).intValue();
                    Entity1_13Types.EntityType entityType = Entity1_13Types.getTypeFromId(type, false);
                    EntityPackets1_13.this.addTrackedEntity(wrapper, ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue(), (EntityType)entityType);
                    int oldId = EntityTypeMapping.getOldId(type);
                    if (oldId == -1) {
                      if (!EntityPackets1_13.this.hasData((EntityType)entityType))
                        ViaBackwards.getPlatform().getLogger().warning("Could not find 1.12 entity type for 1.13 entity type " + type + "/" + entityType); 
                    } else {
                      wrapper.set((Type)Type.VAR_INT, 1, Integer.valueOf(oldId));
                    } 
                  }
                });
            handler(EntityPackets1_13.this.getMobSpawnRewriter(Types1_12.METADATA_LIST));
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SPAWN_PLAYER, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.UUID);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.BYTE);
            map(Type.BYTE);
            map(Types1_13.METADATA_LIST, Types1_12.METADATA_LIST);
            handler(EntityPackets1_13.this.getTrackerAndMetaHandler(Types1_12.METADATA_LIST, (EntityType)Entity1_13Types.EntityType.PLAYER));
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SPAWN_PAINTING, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.UUID);
            handler(EntityPackets1_13.this.getTrackerHandler((EntityType)Entity1_13Types.EntityType.PAINTING, (Type)Type.VAR_INT));
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int motive = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                    String title = PaintingMapping.getStringId(motive);
                    wrapper.write(Type.STRING, title);
                  }
                });
          }
        });
    registerJoinGame((ClientboundPacketType)ClientboundPackets1_13.JOIN_GAME, (EntityType)Entity1_13Types.EntityType.PLAYER);
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.RESPAWN, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            handler(EntityPackets1_13.this.getDimensionHandler(0));
            handler(wrapper -> ((BackwardsBlockStorage)wrapper.user().get(BackwardsBlockStorage.class)).clear());
          }
        });
    registerEntityDestroy((ClientboundPacketType)ClientboundPackets1_13.DESTROY_ENTITIES);
    registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_13.ENTITY_METADATA, Types1_13.METADATA_LIST, Types1_12.METADATA_LIST);
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.FACE_PLAYER, null, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    wrapper.cancel();
                    if (!ViaBackwards.getConfig().isFix1_13FacePlayer())
                      return; 
                    int anchor = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                    double x = ((Double)wrapper.read(Type.DOUBLE)).doubleValue();
                    double y = ((Double)wrapper.read(Type.DOUBLE)).doubleValue();
                    double z = ((Double)wrapper.read(Type.DOUBLE)).doubleValue();
                    PlayerPositionStorage1_13 positionStorage = (PlayerPositionStorage1_13)wrapper.user().get(PlayerPositionStorage1_13.class);
                    PacketWrapper positionAndLook = wrapper.create(47);
                    positionAndLook.write(Type.DOUBLE, Double.valueOf(0.0D));
                    positionAndLook.write(Type.DOUBLE, Double.valueOf(0.0D));
                    positionAndLook.write(Type.DOUBLE, Double.valueOf(0.0D));
                    EntityPositionHandler.writeFacingDegrees(positionAndLook, positionStorage.getX(), (anchor == 1) ? (positionStorage
                        .getY() + 1.62D) : positionStorage.getY(), positionStorage
                        .getZ(), x, y, z);
                    positionAndLook.write(Type.BYTE, Byte.valueOf((byte)7));
                    positionAndLook.write((Type)Type.VAR_INT, Integer.valueOf(-1));
                    positionAndLook.send(Protocol1_12_2To1_13.class, true, true);
                  }
                });
          }
        });
    if (ViaBackwards.getConfig().isFix1_13FacePlayer()) {
      PacketRemapper movementRemapper = new PacketRemapper() {
          public void registerMap() {
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            handler(wrapper -> ((PlayerPositionStorage1_13)wrapper.user().get(PlayerPositionStorage1_13.class)).setCoordinates(wrapper, false));
          }
        };
      ((Protocol1_12_2To1_13)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_12_1.PLAYER_POSITION, movementRemapper);
      ((Protocol1_12_2To1_13)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_12_1.PLAYER_POSITION_AND_ROTATION, movementRemapper);
      ((Protocol1_12_2To1_13)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_12_1.VEHICLE_MOVE, movementRemapper);
    } 
  }
  
  protected void registerRewrites() {
    mapEntity((EntityType)Entity1_13Types.EntityType.DROWNED, (EntityType)Entity1_13Types.EntityType.ZOMBIE_VILLAGER).mobName("Drowned");
    mapEntity((EntityType)Entity1_13Types.EntityType.COD, (EntityType)Entity1_13Types.EntityType.SQUID).mobName("Cod");
    mapEntity((EntityType)Entity1_13Types.EntityType.SALMON, (EntityType)Entity1_13Types.EntityType.SQUID).mobName("Salmon");
    mapEntity((EntityType)Entity1_13Types.EntityType.PUFFERFISH, (EntityType)Entity1_13Types.EntityType.SQUID).mobName("Puffer Fish");
    mapEntity((EntityType)Entity1_13Types.EntityType.TROPICAL_FISH, (EntityType)Entity1_13Types.EntityType.SQUID).mobName("Tropical Fish");
    mapEntity((EntityType)Entity1_13Types.EntityType.PHANTOM, (EntityType)Entity1_13Types.EntityType.PARROT).mobName("Phantom").spawnMetadata(storage -> storage.add(new Metadata(15, (MetaType)MetaType1_12.VarInt, Integer.valueOf(3))));
    mapEntity((EntityType)Entity1_13Types.EntityType.DOLPHIN, (EntityType)Entity1_13Types.EntityType.SQUID).mobName("Dolphin");
    mapEntity((EntityType)Entity1_13Types.EntityType.TURTLE, (EntityType)Entity1_13Types.EntityType.OCELOT).mobName("Turtle");
    registerMetaHandler().handle(e -> {
          Metadata meta = e.getData();
          int typeId = meta.getMetaType().getTypeID();
          if (typeId == 5) {
            meta.setMetaType((MetaType)MetaType1_12.String);
            if (meta.getValue() == null)
              meta.setValue(""); 
          } else if (typeId == 6) {
            meta.setMetaType((MetaType)MetaType1_12.Slot);
            Item item = (Item)meta.getValue();
            meta.setValue(((Protocol1_12_2To1_13)this.protocol).getBlockItemPackets().handleItemToClient(item));
          } else if (typeId == 15) {
            meta.setMetaType((MetaType)MetaType1_12.Discontinued);
          } else if (typeId > 5) {
            meta.setMetaType((MetaType)MetaType1_12.byId(typeId - 1));
          } 
          return meta;
        });
    registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.ENTITY, true, 2).handle(e -> {
          Metadata meta = e.getData();
          String value = meta.getValue().toString();
          if (value.isEmpty())
            return meta; 
          meta.setValue(ChatRewriter.jsonTextToLegacy(value));
          return meta;
        });
    registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.ZOMBIE, true, 15).removed();
    registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.ZOMBIE, true).handle(e -> {
          Metadata meta = e.getData();
          if (meta.getId() > 15)
            meta.setId(meta.getId() - 1); 
          return meta;
        });
    registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.TURTLE, 13).removed();
    registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.TURTLE, 14).removed();
    registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.TURTLE, 15).removed();
    registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.TURTLE, 16).removed();
    registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.TURTLE, 17).removed();
    registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.TURTLE, 18).removed();
    registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.ABSTRACT_FISHES, true, 12).removed();
    registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.ABSTRACT_FISHES, true, 13).removed();
    registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.PHANTOM, 12).removed();
    registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.BOAT, 12).removed();
    registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.TRIDENT, 7).removed();
    registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.WOLF, 17).handle(e -> {
          Metadata meta = e.getData();
          meta.setValue(Integer.valueOf(15 - ((Integer)meta.getValue()).intValue()));
          return meta;
        });
    registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.AREA_EFFECT_CLOUD, 9).handle(e -> {
          Metadata meta = e.getData();
          Particle particle = (Particle)meta.getValue();
          ParticleMapping.ParticleData data = ParticleMapping.getMapping(particle.getId());
          int firstArg = 0;
          int secondArg = 0;
          int[] particleArgs = data.rewriteMeta((Protocol1_12_2To1_13)this.protocol, particle.getArguments());
          if (particleArgs != null && particleArgs.length != 0) {
            if (data.getHandler().isBlockHandler() && particleArgs[0] == 0)
              particleArgs[0] = 102; 
            firstArg = particleArgs[0];
            secondArg = (particleArgs.length == 2) ? particleArgs[1] : 0;
          } 
          e.createMeta(new Metadata(9, (MetaType)MetaType1_12.VarInt, Integer.valueOf(data.getHistoryId())));
          e.createMeta(new Metadata(10, (MetaType)MetaType1_12.VarInt, Integer.valueOf(firstArg)));
          e.createMeta(new Metadata(11, (MetaType)MetaType1_12.VarInt, Integer.valueOf(secondArg)));
          throw RemovedValueException.EX;
        });
  }
  
  protected EntityType getTypeFromId(int typeId) {
    return (EntityType)Entity1_13Types.getTypeFromId(typeId, false);
  }
  
  protected EntityType getObjectTypeFromId(int typeId) {
    return (EntityType)Entity1_13Types.getTypeFromId(typeId, true);
  }
  
  public int getOldEntityId(int newId) {
    return EntityTypeMapping.getOldId(newId);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_12_2to1_13\packets\EntityPackets1_13.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */