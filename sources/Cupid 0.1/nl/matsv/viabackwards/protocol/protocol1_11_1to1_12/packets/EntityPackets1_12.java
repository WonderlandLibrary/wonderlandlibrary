package nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.packets;

import java.util.Optional;
import java.util.function.Function;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import nl.matsv.viabackwards.api.entities.storage.EntityStorage;
import nl.matsv.viabackwards.api.entities.storage.MetaStorage;
import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import nl.matsv.viabackwards.api.rewriters.LegacyEntityRewriter;
import nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.Protocol1_11_1To1_12;
import nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.data.ParrotStorage;
import nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.data.ShoulderTracker;
import nl.matsv.viabackwards.utils.Block;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.entities.Entity1_12Types;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.entities.ObjectType;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_12;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.version.Types1_12;
import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.ClientboundPackets1_12;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;

public class EntityPackets1_12 extends LegacyEntityRewriter<Protocol1_11_1To1_12> {
  public EntityPackets1_12(Protocol1_11_1To1_12 protocol) {
    super((BackwardsProtocol)protocol);
  }
  
  protected void registerPackets() {
    ((Protocol1_11_1To1_12)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_12.SPAWN_ENTITY, new PacketRemapper() {
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
            handler(EntityPackets1_12.this.getObjectTrackerHandler());
            handler(EntityPackets1_12.this.getObjectRewriter(id -> (ObjectType)Entity1_12Types.ObjectType.findById(id.byteValue()).orElse(null)));
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    Optional<Entity1_12Types.ObjectType> type = Entity1_12Types.ObjectType.findById(((Byte)wrapper.get(Type.BYTE, 0)).byteValue());
                    if (type.isPresent() && type.get() == Entity1_12Types.ObjectType.FALLING_BLOCK) {
                      int objectData = ((Integer)wrapper.get(Type.INT, 0)).intValue();
                      int objType = objectData & 0xFFF;
                      int data = objectData >> 12 & 0xF;
                      Block block = ((Protocol1_11_1To1_12)EntityPackets1_12.this.getProtocol()).getBlockItemPackets().handleBlock(objType, data);
                      if (block == null)
                        return; 
                      wrapper.set(Type.INT, 0, Integer.valueOf(block.getId() | block.getData() << 12));
                    } 
                  }
                });
          }
        });
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_12.SPAWN_EXPERIENCE_ORB, (EntityType)Entity1_12Types.EntityType.EXPERIENCE_ORB);
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_12.SPAWN_GLOBAL_ENTITY, (EntityType)Entity1_12Types.EntityType.WEATHER);
    ((Protocol1_11_1To1_12)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_12.SPAWN_MOB, new PacketRemapper() {
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
            map(Types1_12.METADATA_LIST);
            handler(EntityPackets1_12.this.getTrackerHandler());
            handler(EntityPackets1_12.this.getMobSpawnRewriter(Types1_12.METADATA_LIST));
          }
        });
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_12.SPAWN_PAINTING, (EntityType)Entity1_12Types.EntityType.PAINTING);
    ((Protocol1_11_1To1_12)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_12.SPAWN_PLAYER, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.UUID);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.BYTE);
            map(Type.BYTE);
            map(Types1_12.METADATA_LIST);
            handler(EntityPackets1_12.this.getTrackerAndMetaHandler(Types1_12.METADATA_LIST, (EntityType)Entity1_12Types.EntityType.PLAYER));
          }
        });
    ((Protocol1_11_1To1_12)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_12.JOIN_GAME, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            map(Type.UNSIGNED_BYTE);
            map(Type.INT);
            handler(EntityPackets1_12.this.getTrackerHandler((EntityType)Entity1_12Types.EntityType.PLAYER, Type.INT));
            handler(EntityPackets1_12.this.getDimensionHandler(1));
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    ShoulderTracker tracker = (ShoulderTracker)wrapper.user().get(ShoulderTracker.class);
                    tracker.setEntityId(((Integer)wrapper.get(Type.INT, 0)).intValue());
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    PacketWrapper wrapper = new PacketWrapper(7, null, packetWrapper.user());
                    wrapper.write((Type)Type.VAR_INT, Integer.valueOf(1));
                    wrapper.write(Type.STRING, "achievement.openInventory");
                    wrapper.write((Type)Type.VAR_INT, Integer.valueOf(1));
                    wrapper.send(Protocol1_11_1To1_12.class);
                  }
                });
          }
        });
    registerRespawn((ClientboundPacketType)ClientboundPackets1_12.RESPAWN);
    registerEntityDestroy((ClientboundPacketType)ClientboundPackets1_12.DESTROY_ENTITIES);
    registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_12.ENTITY_METADATA, Types1_12.METADATA_LIST);
    ((Protocol1_11_1To1_12)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_12.ENTITY_PROPERTIES, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.INT);
            handler(wrapper -> {
                  int size = ((Integer)wrapper.get(Type.INT, 0)).intValue();
                  int newSize = size;
                  for (int i = 0; i < size; i++) {
                    String key = (String)wrapper.read(Type.STRING);
                    if (key.equals("generic.flyingSpeed")) {
                      newSize--;
                      wrapper.read(Type.DOUBLE);
                      int modSize = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                      for (int j = 0; j < modSize; j++) {
                        wrapper.read(Type.UUID);
                        wrapper.read(Type.DOUBLE);
                        wrapper.read(Type.BYTE);
                      } 
                    } else {
                      wrapper.write(Type.STRING, key);
                      wrapper.passthrough(Type.DOUBLE);
                      int modSize = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
                      for (int j = 0; j < modSize; j++) {
                        wrapper.passthrough(Type.UUID);
                        wrapper.passthrough(Type.DOUBLE);
                        wrapper.passthrough(Type.BYTE);
                      } 
                    } 
                  } 
                  if (newSize != size)
                    wrapper.set(Type.INT, 0, Integer.valueOf(newSize)); 
                });
          }
        });
  }
  
  protected void registerRewrites() {
    mapEntity((EntityType)Entity1_12Types.EntityType.PARROT, (EntityType)Entity1_12Types.EntityType.BAT).mobName("Parrot").spawnMetadata(storage -> storage.add(new Metadata(12, (MetaType)MetaType1_12.Byte, Byte.valueOf((byte)0))));
    mapEntity((EntityType)Entity1_12Types.EntityType.ILLUSION_ILLAGER, (EntityType)Entity1_12Types.EntityType.EVOCATION_ILLAGER).mobName("Illusioner");
    registerMetaHandler().filter((EntityType)Entity1_12Types.EntityType.EVOCATION_ILLAGER, true, 12).removed();
    registerMetaHandler().filter((EntityType)Entity1_12Types.EntityType.EVOCATION_ILLAGER, true, 13).handleIndexChange(12);
    registerMetaHandler().filter((EntityType)Entity1_12Types.EntityType.ILLUSION_ILLAGER, 0).handle(e -> {
          byte mask = ((Byte)e.getData().getValue()).byteValue();
          if ((mask & 0x20) == 32)
            mask = (byte)(mask & 0xFFFFFFDF); 
          e.getData().setValue(Byte.valueOf(mask));
          return e.getData();
        });
    registerMetaHandler().filter((EntityType)Entity1_12Types.EntityType.PARROT, true).handle(e -> {
          if (!e.getEntity().has(ParrotStorage.class))
            e.getEntity().put((EntityStorage)new ParrotStorage()); 
          return e.getData();
        });
    registerMetaHandler().filter((EntityType)Entity1_12Types.EntityType.PARROT, 12).removed();
    registerMetaHandler().filter((EntityType)Entity1_12Types.EntityType.PARROT, 13).handle(e -> {
          Metadata data = e.getData();
          ParrotStorage storage = (ParrotStorage)e.getEntity().get(ParrotStorage.class);
          boolean isSitting = ((((Byte)data.getValue()).byteValue() & 0x1) == 1);
          boolean isTamed = ((((Byte)data.getValue()).byteValue() & 0x4) == 4);
          if (storage.isTamed() || isTamed);
          storage.setTamed(isTamed);
          if (isSitting) {
            data.setId(12);
            data.setValue(Byte.valueOf((byte)1));
            storage.setSitting(true);
          } else if (storage.isSitting()) {
            data.setId(12);
            data.setValue(Byte.valueOf((byte)0));
            storage.setSitting(false);
          } else {
            throw RemovedValueException.EX;
          } 
          return data;
        });
    registerMetaHandler().filter((EntityType)Entity1_12Types.EntityType.PARROT, 14).removed();
    registerMetaHandler().filter((EntityType)Entity1_12Types.EntityType.PARROT, 15).removed();
    registerMetaHandler().filter((EntityType)Entity1_12Types.EntityType.PLAYER, 15).handle(e -> {
          CompoundTag tag = (CompoundTag)e.getData().getValue();
          ShoulderTracker tracker = (ShoulderTracker)e.getUser().get(ShoulderTracker.class);
          if (tag.isEmpty() && tracker.getLeftShoulder() != null) {
            tracker.setLeftShoulder(null);
            tracker.update();
          } else if (tag.contains("id") && e.getEntity().getEntityId() == tracker.getEntityId()) {
            String id = (String)tag.get("id").getValue();
            if (tracker.getLeftShoulder() == null || !tracker.getLeftShoulder().equals(id)) {
              tracker.setLeftShoulder(id);
              tracker.update();
            } 
          } 
          throw RemovedValueException.EX;
        });
    registerMetaHandler().filter((EntityType)Entity1_12Types.EntityType.PLAYER, 16).handle(e -> {
          CompoundTag tag = (CompoundTag)e.getData().getValue();
          ShoulderTracker tracker = (ShoulderTracker)e.getUser().get(ShoulderTracker.class);
          if (tag.isEmpty() && tracker.getRightShoulder() != null) {
            tracker.setRightShoulder(null);
            tracker.update();
          } else if (tag.contains("id") && e.getEntity().getEntityId() == tracker.getEntityId()) {
            String id = (String)tag.get("id").getValue();
            if (tracker.getRightShoulder() == null || !tracker.getRightShoulder().equals(id)) {
              tracker.setRightShoulder(id);
              tracker.update();
            } 
          } 
          throw RemovedValueException.EX;
        });
  }
  
  protected EntityType getTypeFromId(int typeId) {
    return (EntityType)Entity1_12Types.getTypeFromId(typeId, false);
  }
  
  protected EntityType getObjectTypeFromId(int typeId) {
    return (EntityType)Entity1_12Types.getTypeFromId(typeId, true);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_11_1to1_12\packets\EntityPackets1_12.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */