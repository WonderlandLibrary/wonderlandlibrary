package nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.packets;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import nl.matsv.viabackwards.api.rewriters.EntityRewriter;
import nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.Protocol1_15_2To1_16;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.entities.Entity1_15Types;
import us.myles.ViaVersion.api.entities.Entity1_16Types;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_14;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.remapper.ValueTransformer;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.Particle;
import us.myles.ViaVersion.api.type.types.version.Types1_14;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.viaversion.libs.gson.JsonElement;

public class EntityPackets1_16 extends EntityRewriter<Protocol1_15_2To1_16> {
  private final ValueTransformer<String, Integer> dimensionTransformer = new ValueTransformer<String, Integer>(Type.STRING, Type.INT) {
      public Integer transform(PacketWrapper wrapper, String input) throws Exception {
        switch (input) {
          case "minecraft:the_nether":
            return Integer.valueOf(-1);
          default:
            return Integer.valueOf(0);
          case "minecraft:the_end":
            break;
        } 
        return Integer.valueOf(1);
      }
    };
  
  public EntityPackets1_16(Protocol1_15_2To1_16 protocol) {
    super((BackwardsProtocol)protocol);
  }
  
  protected void registerPackets() {
    registerSpawnTrackerWithData((ClientboundPacketType)ClientboundPackets1_16.SPAWN_ENTITY, (EntityType)Entity1_16Types.EntityType.FALLING_BLOCK);
    registerSpawnTracker((ClientboundPacketType)ClientboundPackets1_16.SPAWN_MOB);
    ((Protocol1_15_2To1_16)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.RESPAWN, new PacketRemapper() {
          public void registerMap() {
            map(EntityPackets1_16.this.dimensionTransformer);
            map(Type.STRING, Type.NOTHING);
            map(Type.LONG);
            map(Type.UNSIGNED_BYTE);
            map(Type.BYTE, Type.NOTHING);
            handler(wrapper -> {
                  ClientWorld clientWorld = (ClientWorld)wrapper.user().get(ClientWorld.class);
                  int dimension = ((Integer)wrapper.get(Type.INT, 0)).intValue();
                  if (clientWorld.getEnvironment() != null && dimension == clientWorld.getEnvironment().getId()) {
                    PacketWrapper packet = wrapper.create(ClientboundPackets1_15.RESPAWN.ordinal());
                    packet.write(Type.INT, Integer.valueOf((dimension == 0) ? -1 : 0));
                    packet.write(Type.LONG, Long.valueOf(0L));
                    packet.write(Type.UNSIGNED_BYTE, Short.valueOf((short)0));
                    packet.write(Type.STRING, "default");
                    packet.send(Protocol1_15_2To1_16.class, true, true);
                  } 
                  clientWorld.setEnvironment(dimension);
                  wrapper.write(Type.STRING, "default");
                  wrapper.read(Type.BOOLEAN);
                  if (((Boolean)wrapper.read(Type.BOOLEAN)).booleanValue())
                    wrapper.set(Type.STRING, 0, "flat"); 
                  wrapper.read(Type.BOOLEAN);
                });
          }
        });
    ((Protocol1_15_2To1_16)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.JOIN_GAME, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            map(Type.UNSIGNED_BYTE);
            map(Type.BYTE, Type.NOTHING);
            map(Type.STRING_ARRAY, Type.NOTHING);
            map(Type.NBT, Type.NOTHING);
            map(EntityPackets1_16.this.dimensionTransformer);
            map(Type.STRING, Type.NOTHING);
            map(Type.LONG);
            map(Type.UNSIGNED_BYTE);
            handler(wrapper -> {
                  ClientWorld clientChunks = (ClientWorld)wrapper.user().get(ClientWorld.class);
                  clientChunks.setEnvironment(((Integer)wrapper.get(Type.INT, 1)).intValue());
                  EntityPackets1_16.this.getEntityTracker(wrapper.user()).trackEntityType(((Integer)wrapper.get(Type.INT, 0)).intValue(), (EntityType)Entity1_16Types.EntityType.PLAYER);
                  wrapper.write(Type.STRING, "default");
                  wrapper.passthrough((Type)Type.VAR_INT);
                  wrapper.passthrough(Type.BOOLEAN);
                  wrapper.passthrough(Type.BOOLEAN);
                  wrapper.read(Type.BOOLEAN);
                  if (((Boolean)wrapper.read(Type.BOOLEAN)).booleanValue())
                    wrapper.set(Type.STRING, 0, "flat"); 
                });
          }
        });
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_16.SPAWN_EXPERIENCE_ORB, (EntityType)Entity1_16Types.EntityType.EXPERIENCE_ORB);
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_16.SPAWN_PAINTING, (EntityType)Entity1_16Types.EntityType.PAINTING);
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_16.SPAWN_PLAYER, (EntityType)Entity1_16Types.EntityType.PLAYER);
    registerEntityDestroy((ClientboundPacketType)ClientboundPackets1_16.DESTROY_ENTITIES);
    registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_16.ENTITY_METADATA, Types1_14.METADATA_LIST);
    ((Protocol1_15_2To1_16)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.ENTITY_PROPERTIES, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  wrapper.passthrough((Type)Type.VAR_INT);
                  int size = ((Integer)wrapper.passthrough(Type.INT)).intValue();
                  for (int i = 0; i < size; i++) {
                    String attributeIdentifier = (String)wrapper.read(Type.STRING);
                    String oldKey = (String)((Protocol1_15_2To1_16)EntityPackets1_16.this.protocol).getMappingData().getAttributeMappings().get(attributeIdentifier);
                    wrapper.write(Type.STRING, (oldKey != null) ? oldKey : attributeIdentifier.replace("minecraft:", ""));
                    wrapper.passthrough(Type.DOUBLE);
                    int modifierSize = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
                    for (int j = 0; j < modifierSize; j++) {
                      wrapper.passthrough(Type.UUID);
                      wrapper.passthrough(Type.DOUBLE);
                      wrapper.passthrough(Type.BYTE);
                    } 
                  } 
                });
          }
        });
    ((Protocol1_15_2To1_16)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.PLAYER_INFO, new PacketRemapper() {
          public void registerMap() {
            handler(packetWrapper -> {
                  int action = ((Integer)packetWrapper.passthrough((Type)Type.VAR_INT)).intValue();
                  int playerCount = ((Integer)packetWrapper.passthrough((Type)Type.VAR_INT)).intValue();
                  for (int i = 0; i < playerCount; i++) {
                    packetWrapper.passthrough(Type.UUID);
                    if (action == 0) {
                      packetWrapper.passthrough(Type.STRING);
                      int properties = ((Integer)packetWrapper.passthrough((Type)Type.VAR_INT)).intValue();
                      for (int j = 0; j < properties; j++) {
                        packetWrapper.passthrough(Type.STRING);
                        packetWrapper.passthrough(Type.STRING);
                        if (((Boolean)packetWrapper.passthrough(Type.BOOLEAN)).booleanValue())
                          packetWrapper.passthrough(Type.STRING); 
                      } 
                      packetWrapper.passthrough((Type)Type.VAR_INT);
                      packetWrapper.passthrough((Type)Type.VAR_INT);
                      if (((Boolean)packetWrapper.passthrough(Type.BOOLEAN)).booleanValue())
                        ((Protocol1_15_2To1_16)EntityPackets1_16.this.protocol).getTranslatableRewriter().processText((JsonElement)packetWrapper.passthrough(Type.COMPONENT)); 
                    } else if (action == 1) {
                      packetWrapper.passthrough((Type)Type.VAR_INT);
                    } else if (action == 2) {
                      packetWrapper.passthrough((Type)Type.VAR_INT);
                    } else if (action == 3 && ((Boolean)packetWrapper.passthrough(Type.BOOLEAN)).booleanValue()) {
                      ((Protocol1_15_2To1_16)EntityPackets1_16.this.protocol).getTranslatableRewriter().processText((JsonElement)packetWrapper.passthrough(Type.COMPONENT));
                    } 
                  } 
                });
          }
        });
  }
  
  protected void registerRewrites() {
    registerMetaHandler().handle(e -> {
          Metadata meta = e.getData();
          MetaType type = meta.getMetaType();
          if (type == MetaType1_14.Slot) {
            meta.setValue(((Protocol1_15_2To1_16)this.protocol).getBlockItemPackets().handleItemToClient((Item)meta.getValue()));
          } else if (type == MetaType1_14.BlockID) {
            meta.setValue(Integer.valueOf(((Protocol1_15_2To1_16)this.protocol).getMappingData().getNewBlockStateId(((Integer)meta.getValue()).intValue())));
          } else if (type == MetaType1_14.PARTICLE) {
            rewriteParticle((Particle)meta.getValue());
          } else if (type == MetaType1_14.OptChat) {
            JsonElement text = (JsonElement)meta.getCastedValue();
            if (text != null)
              ((Protocol1_15_2To1_16)this.protocol).getTranslatableRewriter().processText(text); 
          } 
          return meta;
        });
    mapEntityDirect((EntityType)Entity1_16Types.EntityType.ZOMBIFIED_PIGLIN, (EntityType)Entity1_15Types.EntityType.ZOMBIE_PIGMAN);
    mapTypes((EntityType[])Entity1_16Types.EntityType.values(), Entity1_15Types.EntityType.class);
    mapEntity((EntityType)Entity1_16Types.EntityType.HOGLIN, (EntityType)Entity1_16Types.EntityType.COW).jsonName("Hoglin");
    mapEntity((EntityType)Entity1_16Types.EntityType.ZOGLIN, (EntityType)Entity1_16Types.EntityType.COW).jsonName("Zoglin");
    mapEntity((EntityType)Entity1_16Types.EntityType.PIGLIN, (EntityType)Entity1_16Types.EntityType.ZOMBIFIED_PIGLIN).jsonName("Piglin");
    mapEntity((EntityType)Entity1_16Types.EntityType.STRIDER, (EntityType)Entity1_16Types.EntityType.MAGMA_CUBE).jsonName("Strider");
    registerMetaHandler().filter((EntityType)Entity1_16Types.EntityType.ZOGLIN, 16).removed();
    registerMetaHandler().filter((EntityType)Entity1_16Types.EntityType.HOGLIN, 15).removed();
    registerMetaHandler().filter((EntityType)Entity1_16Types.EntityType.PIGLIN, 16).removed();
    registerMetaHandler().filter((EntityType)Entity1_16Types.EntityType.PIGLIN, 17).removed();
    registerMetaHandler().filter((EntityType)Entity1_16Types.EntityType.PIGLIN, 18).removed();
    registerMetaHandler().filter((EntityType)Entity1_16Types.EntityType.STRIDER, 15).handle(meta -> {
          boolean baby = ((Boolean)meta.getData().getCastedValue()).booleanValue();
          meta.getData().setValue(Integer.valueOf(baby ? 1 : 3));
          meta.getData().setMetaType((MetaType)MetaType1_14.VarInt);
          return meta.getData();
        });
    registerMetaHandler().filter((EntityType)Entity1_16Types.EntityType.STRIDER, 16).removed();
    registerMetaHandler().filter((EntityType)Entity1_16Types.EntityType.STRIDER, 17).removed();
    registerMetaHandler().filter((EntityType)Entity1_16Types.EntityType.STRIDER, 18).removed();
    registerMetaHandler().filter((EntityType)Entity1_16Types.EntityType.FISHING_BOBBER, 8).removed();
    registerMetaHandler().filter((EntityType)Entity1_16Types.EntityType.ABSTRACT_ARROW, true, 8).removed();
    registerMetaHandler().filter((EntityType)Entity1_16Types.EntityType.ABSTRACT_ARROW, true).handle(meta -> {
          if (meta.getIndex() >= 8)
            meta.getData().setId(meta.getIndex() + 1); 
          return meta.getData();
        });
  }
  
  protected EntityType getTypeFromId(int typeId) {
    return (EntityType)Entity1_16Types.getTypeFromId(typeId);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_15_2to1_16\packets\EntityPackets1_16.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */