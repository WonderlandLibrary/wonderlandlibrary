package nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.packets;

import java.util.ArrayList;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import nl.matsv.viabackwards.api.entities.storage.MetaStorage;
import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import nl.matsv.viabackwards.api.rewriters.EntityRewriter;
import nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.Protocol1_14_4To1_15;
import nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.data.EntityTypeMapping;
import nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.data.ImmediateRespawn;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.entities.Entity1_15Types;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_14;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.Particle;
import us.myles.ViaVersion.api.type.types.version.Types1_14;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;

public class EntityPackets1_15 extends EntityRewriter<Protocol1_14_4To1_15> {
  public EntityPackets1_15(Protocol1_14_4To1_15 protocol) {
    super((BackwardsProtocol)protocol);
  }
  
  protected void registerPackets() {
    ((Protocol1_14_4To1_15)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_15.UPDATE_HEALTH, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  float health = ((Float)wrapper.passthrough((Type)Type.FLOAT)).floatValue();
                  if (health > 0.0F)
                    return; 
                  if (!((ImmediateRespawn)wrapper.user().get(ImmediateRespawn.class)).isImmediateRespawn())
                    return; 
                  PacketWrapper statusPacket = wrapper.create(4);
                  statusPacket.write((Type)Type.VAR_INT, Integer.valueOf(0));
                  statusPacket.sendToServer(Protocol1_14_4To1_15.class);
                });
          }
        });
    ((Protocol1_14_4To1_15)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_15.GAME_EVENT, new PacketRemapper() {
          public void registerMap() {
            map(Type.UNSIGNED_BYTE);
            map((Type)Type.FLOAT);
            handler(wrapper -> {
                  if (((Short)wrapper.get(Type.UNSIGNED_BYTE, 0)).shortValue() == 11)
                    ((ImmediateRespawn)wrapper.user().get(ImmediateRespawn.class)).setImmediateRespawn((((Float)wrapper.get((Type)Type.FLOAT, 0)).floatValue() == 1.0F)); 
                });
          }
        });
    registerSpawnTrackerWithData((ClientboundPacketType)ClientboundPackets1_15.SPAWN_ENTITY, (EntityType)Entity1_15Types.EntityType.FALLING_BLOCK);
    ((Protocol1_14_4To1_15)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_15.SPAWN_MOB, new PacketRemapper() {
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
            create(wrapper -> wrapper.write(Types1_14.METADATA_LIST, new ArrayList()));
            handler(wrapper -> {
                  int type = ((Integer)wrapper.get((Type)Type.VAR_INT, 1)).intValue();
                  Entity1_15Types.EntityType entityType = Entity1_15Types.getTypeFromId(type);
                  EntityPackets1_15.this.addTrackedEntity(wrapper, ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue(), (EntityType)entityType);
                  wrapper.set((Type)Type.VAR_INT, 1, Integer.valueOf(EntityTypeMapping.getOldEntityId(type)));
                });
          }
        });
    ((Protocol1_14_4To1_15)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_15.RESPAWN, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            map(Type.LONG, Type.NOTHING);
          }
        });
    ((Protocol1_14_4To1_15)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_15.JOIN_GAME, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            map(Type.UNSIGNED_BYTE);
            map(Type.INT);
            map(Type.LONG, Type.NOTHING);
            map(Type.UNSIGNED_BYTE);
            map(Type.STRING);
            map((Type)Type.VAR_INT);
            map(Type.BOOLEAN);
            handler(EntityPackets1_15.this.getTrackerHandler((EntityType)Entity1_15Types.EntityType.PLAYER, Type.INT));
            handler(wrapper -> ((ImmediateRespawn)wrapper.user().get(ImmediateRespawn.class)).setImmediateRespawn(((Boolean)wrapper.read(Type.BOOLEAN)).booleanValue()));
          }
        });
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_15.SPAWN_EXPERIENCE_ORB, (EntityType)Entity1_15Types.EntityType.EXPERIENCE_ORB);
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_15.SPAWN_GLOBAL_ENTITY, (EntityType)Entity1_15Types.EntityType.LIGHTNING_BOLT);
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_15.SPAWN_PAINTING, (EntityType)Entity1_15Types.EntityType.PAINTING);
    ((Protocol1_14_4To1_15)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_15.SPAWN_PLAYER, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.UUID);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.BYTE);
            map(Type.BYTE);
            create(wrapper -> wrapper.write(Types1_14.METADATA_LIST, new ArrayList()));
            handler(EntityPackets1_15.this.getTrackerHandler((EntityType)Entity1_15Types.EntityType.PLAYER, (Type)Type.VAR_INT));
          }
        });
    registerEntityDestroy((ClientboundPacketType)ClientboundPackets1_15.DESTROY_ENTITIES);
    registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_15.ENTITY_METADATA, Types1_14.METADATA_LIST);
    ((Protocol1_14_4To1_15)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_15.ENTITY_PROPERTIES, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.INT);
            handler(wrapper -> {
                  int entityId = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
                  EntityType entityType = EntityPackets1_15.this.getEntityType(wrapper.user(), entityId);
                  if (entityType != Entity1_15Types.EntityType.BEE)
                    return; 
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
    registerMetaHandler().handle(e -> {
          Metadata meta = e.getData();
          MetaType type = meta.getMetaType();
          if (type == MetaType1_14.Slot) {
            Item item = (Item)meta.getValue();
            meta.setValue(((Protocol1_14_4To1_15)this.protocol).getBlockItemPackets().handleItemToClient(item));
          } else if (type == MetaType1_14.BlockID) {
            int blockstate = ((Integer)meta.getValue()).intValue();
            meta.setValue(Integer.valueOf(((Protocol1_14_4To1_15)this.protocol).getMappingData().getNewBlockStateId(blockstate)));
          } else if (type == MetaType1_14.PARTICLE) {
            rewriteParticle((Particle)meta.getValue());
          } 
          return meta;
        });
    registerMetaHandler().filter((EntityType)Entity1_15Types.EntityType.LIVINGENTITY, true).handle(e -> {
          int index = e.getIndex();
          if (index == 12)
            throw RemovedValueException.EX; 
          if (index > 12)
            e.getData().setId(index - 1); 
          return e.getData();
        });
    registerMetaHandler().filter((EntityType)Entity1_15Types.EntityType.BEE, 15).removed();
    registerMetaHandler().filter((EntityType)Entity1_15Types.EntityType.BEE, 16).removed();
    mapEntity((EntityType)Entity1_15Types.EntityType.BEE, (EntityType)Entity1_15Types.EntityType.PUFFERFISH).jsonName("Bee").spawnMetadata(storage -> {
          storage.add(new Metadata(14, (MetaType)MetaType1_14.Boolean, Boolean.valueOf(false)));
          storage.add(new Metadata(15, (MetaType)MetaType1_14.VarInt, Integer.valueOf(2)));
        });
    registerMetaHandler().filter((EntityType)Entity1_15Types.EntityType.ENDERMAN, 16).removed();
    registerMetaHandler().filter((EntityType)Entity1_15Types.EntityType.TRIDENT, 10).removed();
    registerMetaHandler().filter((EntityType)Entity1_15Types.EntityType.WOLF).handle(e -> {
          int index = e.getIndex();
          if (index >= 17)
            e.getData().setId(index + 1); 
          return e.getData();
        });
  }
  
  protected EntityType getTypeFromId(int typeId) {
    return (EntityType)Entity1_15Types.getTypeFromId(typeId);
  }
  
  public int getOldEntityId(int newId) {
    return EntityTypeMapping.getOldEntityId(newId);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_14_4to1_15\packets\EntityPackets1_15.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */