package nl.matsv.viabackwards.api.rewriters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import nl.matsv.viabackwards.ViaBackwards;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.entities.storage.EntityData;
import nl.matsv.viabackwards.api.entities.storage.EntityObjectData;
import nl.matsv.viabackwards.api.entities.storage.MetaStorage;
import org.jetbrains.annotations.Nullable;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.entities.ObjectType;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_9;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;

public abstract class LegacyEntityRewriter<T extends BackwardsProtocol> extends EntityRewriterBase<T> {
  private final Map<ObjectType, EntityData> objectTypes = new HashMap<>();
  
  protected LegacyEntityRewriter(T protocol) {
    this(protocol, (MetaType)MetaType1_9.String, (MetaType)MetaType1_9.Boolean);
  }
  
  protected LegacyEntityRewriter(T protocol, MetaType displayType, MetaType displayVisibilityType) {
    super(protocol, displayType, 2, displayVisibilityType, 3);
  }
  
  protected EntityObjectData mapObjectType(ObjectType oldObjectType, ObjectType replacement, int data) {
    EntityObjectData entData = new EntityObjectData(oldObjectType.getId(), true, replacement.getId(), data);
    this.objectTypes.put(oldObjectType, entData);
    return entData;
  }
  
  @Nullable
  protected EntityData getObjectData(ObjectType type) {
    return this.objectTypes.get(type);
  }
  
  protected void registerRespawn(ClientboundPacketType packetType) {
    this.protocol.registerOutgoing(packetType, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            handler(wrapper -> {
                  ClientWorld clientWorld = (ClientWorld)wrapper.user().get(ClientWorld.class);
                  clientWorld.setEnvironment(((Integer)wrapper.get(Type.INT, 0)).intValue());
                });
          }
        });
  }
  
  protected void registerJoinGame(ClientboundPacketType packetType, final EntityType playerType) {
    this.protocol.registerOutgoing(packetType, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            map(Type.UNSIGNED_BYTE);
            map(Type.INT);
            handler(wrapper -> {
                  ClientWorld clientChunks = (ClientWorld)wrapper.user().get(ClientWorld.class);
                  clientChunks.setEnvironment(((Integer)wrapper.get(Type.INT, 1)).intValue());
                  LegacyEntityRewriter.this.getEntityTracker(wrapper.user()).trackEntityType(((Integer)wrapper.get(Type.INT, 0)).intValue(), playerType);
                });
          }
        });
  }
  
  protected void registerMetadataRewriter(ClientboundPacketType packetType, final Type<List<Metadata>> oldMetaType, final Type<List<Metadata>> newMetaType) {
    getProtocol().registerOutgoing(packetType, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            if (oldMetaType != null) {
              map(oldMetaType, newMetaType);
            } else {
              map(newMetaType);
            } 
            handler(wrapper -> {
                  List<Metadata> metadata = (List<Metadata>)wrapper.get(newMetaType, 0);
                  wrapper.set(newMetaType, 0, LegacyEntityRewriter.this.handleMeta(wrapper.user(), ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue(), new MetaStorage(metadata)).getMetaDataList());
                });
          }
        });
  }
  
  protected void registerMetadataRewriter(ClientboundPacketType packetType, Type<List<Metadata>> metaType) {
    registerMetadataRewriter(packetType, (Type<List<Metadata>>)null, metaType);
  }
  
  protected PacketHandler getMobSpawnRewriter(Type<List<Metadata>> metaType) {
    return wrapper -> {
        int entityId = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
        EntityType type = getEntityType(wrapper.user(), entityId);
        MetaStorage storage = new MetaStorage((List)wrapper.get(metaType, 0));
        handleMeta(wrapper.user(), entityId, storage);
        EntityData entityData = getEntityData(type);
        if (entityData != null) {
          wrapper.set((Type)Type.VAR_INT, 1, Integer.valueOf(entityData.getReplacementId()));
          if (entityData.hasBaseMeta())
            entityData.getDefaultMeta().createMeta(storage); 
        } 
        wrapper.set(metaType, 0, storage.getMetaDataList());
      };
  }
  
  protected PacketHandler getObjectTrackerHandler() {
    return wrapper -> addTrackedEntity(wrapper, ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue(), getObjectTypeFromId(((Byte)wrapper.get(Type.BYTE, 0)).byteValue()));
  }
  
  protected PacketHandler getTrackerAndMetaHandler(Type<List<Metadata>> metaType, EntityType entityType) {
    return wrapper -> {
        addTrackedEntity(wrapper, ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue(), entityType);
        List<Metadata> metaDataList = handleMeta(wrapper.user(), ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue(), new MetaStorage((List)wrapper.get(metaType, 0))).getMetaDataList();
        wrapper.set(metaType, 0, metaDataList);
      };
  }
  
  protected PacketHandler getObjectRewriter(Function<Byte, ObjectType> objectGetter) {
    return wrapper -> {
        ObjectType type = objectGetter.apply(wrapper.get(Type.BYTE, 0));
        if (type == null) {
          ViaBackwards.getPlatform().getLogger().warning("Could not find Entity Type" + wrapper.get(Type.BYTE, 0));
          return;
        } 
        EntityData data = getObjectData(type);
        if (data != null) {
          wrapper.set(Type.BYTE, 0, Byte.valueOf((byte)data.getReplacementId()));
          if (data.getObjectData() != -1)
            wrapper.set(Type.INT, 0, Integer.valueOf(data.getObjectData())); 
        } 
      };
  }
  
  protected EntityType getObjectTypeFromId(int typeId) {
    return getTypeFromId(typeId);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\api\rewriters\LegacyEntityRewriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */