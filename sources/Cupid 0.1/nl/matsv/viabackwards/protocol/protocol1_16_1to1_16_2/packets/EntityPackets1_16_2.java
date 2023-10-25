package nl.matsv.viabackwards.protocol.protocol1_16_1to1_16_2.packets;

import com.google.common.collect.Sets;
import java.util.Set;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import nl.matsv.viabackwards.api.rewriters.EntityRewriter;
import nl.matsv.viabackwards.protocol.protocol1_16_1to1_16_2.Protocol1_16_1To1_16_2;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.entities.Entity1_16Types;
import us.myles.ViaVersion.api.entities.Entity1_16_2Types;
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
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.packets.EntityPackets;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;

public class EntityPackets1_16_2 extends EntityRewriter<Protocol1_16_1To1_16_2> {
  private final Set<String> oldDimensions = Sets.newHashSet((Object[])new String[] { "minecraft:overworld", "minecraft:the_nether", "minecraft:the_end" });
  
  public EntityPackets1_16_2(Protocol1_16_1To1_16_2 protocol) {
    super((BackwardsProtocol)protocol);
  }
  
  protected void registerPackets() {
    registerSpawnTrackerWithData((ClientboundPacketType)ClientboundPackets1_16_2.SPAWN_ENTITY, (EntityType)Entity1_16_2Types.EntityType.FALLING_BLOCK);
    registerSpawnTracker((ClientboundPacketType)ClientboundPackets1_16_2.SPAWN_MOB);
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_16_2.SPAWN_EXPERIENCE_ORB, (EntityType)Entity1_16_2Types.EntityType.EXPERIENCE_ORB);
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_16_2.SPAWN_PAINTING, (EntityType)Entity1_16_2Types.EntityType.PAINTING);
    registerExtraTracker((ClientboundPacketType)ClientboundPackets1_16_2.SPAWN_PLAYER, (EntityType)Entity1_16_2Types.EntityType.PLAYER);
    registerEntityDestroy((ClientboundPacketType)ClientboundPackets1_16_2.DESTROY_ENTITIES);
    registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_16_2.ENTITY_METADATA, Types1_14.METADATA_LIST);
    ((Protocol1_16_1To1_16_2)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16_2.JOIN_GAME, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            handler(wrapper -> {
                  boolean hardcore = ((Boolean)wrapper.read(Type.BOOLEAN)).booleanValue();
                  short gamemode = ((Short)wrapper.read(Type.UNSIGNED_BYTE)).shortValue();
                  if (hardcore)
                    gamemode = (short)(gamemode | 0x8); 
                  wrapper.write(Type.UNSIGNED_BYTE, Short.valueOf(gamemode));
                });
            map(Type.BYTE);
            map(Type.STRING_ARRAY);
            handler(wrapper -> {
                  wrapper.read(Type.NBT);
                  wrapper.write(Type.NBT, EntityPackets.DIMENSIONS_TAG);
                  CompoundTag dimensionData = (CompoundTag)wrapper.read(Type.NBT);
                  wrapper.write(Type.STRING, EntityPackets1_16_2.this.getDimensionFromData(dimensionData));
                });
            map(Type.STRING);
            map(Type.LONG);
            handler(wrapper -> {
                  int maxPlayers = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                  wrapper.write(Type.UNSIGNED_BYTE, Short.valueOf((short)Math.max(maxPlayers, 255)));
                });
            handler(EntityPackets1_16_2.this.getTrackerHandler((EntityType)Entity1_16_2Types.EntityType.PLAYER, Type.INT));
          }
        });
    ((Protocol1_16_1To1_16_2)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16_2.RESPAWN, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  CompoundTag dimensionData = (CompoundTag)wrapper.read(Type.NBT);
                  wrapper.write(Type.STRING, EntityPackets1_16_2.this.getDimensionFromData(dimensionData));
                });
          }
        });
  }
  
  private String getDimensionFromData(CompoundTag dimensionData) {
    StringTag effectsLocation = (StringTag)dimensionData.get("effects");
    return (effectsLocation != null && this.oldDimensions.contains(effectsLocation.getValue())) ? effectsLocation.getValue() : "minecraft:overworld";
  }
  
  protected void registerRewrites() {
    registerMetaHandler().handle(e -> {
          Metadata meta = e.getData();
          MetaType type = meta.getMetaType();
          if (type == MetaType1_14.Slot) {
            meta.setValue(((Protocol1_16_1To1_16_2)this.protocol).getBlockItemPackets().handleItemToClient((Item)meta.getValue()));
          } else if (type == MetaType1_14.BlockID) {
            meta.setValue(Integer.valueOf(((Protocol1_16_1To1_16_2)this.protocol).getMappingData().getNewBlockStateId(((Integer)meta.getValue()).intValue())));
          } else if (type == MetaType1_14.OptChat) {
            JsonElement text = (JsonElement)meta.getCastedValue();
            if (text != null)
              ((Protocol1_16_1To1_16_2)this.protocol).getTranslatableRewriter().processText(text); 
          } else if (type == MetaType1_14.PARTICLE) {
            rewriteParticle((Particle)meta.getValue());
          } 
          return meta;
        });
    mapTypes((EntityType[])Entity1_16_2Types.EntityType.values(), Entity1_16Types.EntityType.class);
    mapEntity((EntityType)Entity1_16_2Types.EntityType.PIGLIN_BRUTE, (EntityType)Entity1_16_2Types.EntityType.PIGLIN).jsonName("Piglin Brute");
    registerMetaHandler().filter((EntityType)Entity1_16_2Types.EntityType.ABSTRACT_PIGLIN, true).handle(meta -> {
          if (meta.getIndex() == 15) {
            meta.getData().setId(16);
          } else if (meta.getIndex() == 16) {
            meta.getData().setId(15);
          } 
          return meta.getData();
        });
  }
  
  protected EntityType getTypeFromId(int typeId) {
    return (EntityType)Entity1_16_2Types.getTypeFromId(typeId);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_16_1to1_16_2\packets\EntityPackets1_16_2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */