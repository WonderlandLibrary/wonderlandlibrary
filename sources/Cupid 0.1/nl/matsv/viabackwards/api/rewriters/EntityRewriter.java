package nl.matsv.viabackwards.api.rewriters;

import java.util.List;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.entities.storage.EntityData;
import nl.matsv.viabackwards.api.entities.storage.MetaStorage;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_14;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;

public abstract class EntityRewriter<T extends BackwardsProtocol> extends EntityRewriterBase<T> {
  protected EntityRewriter(T protocol) {
    this(protocol, (MetaType)MetaType1_14.OptChat, (MetaType)MetaType1_14.Boolean);
  }
  
  protected EntityRewriter(T protocol, MetaType displayType, MetaType displayVisibilityType) {
    super(protocol, displayType, 2, displayVisibilityType, 3);
  }
  
  public void registerSpawnTrackerWithData(ClientboundPacketType packetType, final EntityType fallingBlockType) {
    this.protocol.registerOutgoing(packetType, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.UUID);
            map((Type)Type.VAR_INT);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.BYTE);
            map(Type.BYTE);
            map(Type.INT);
            handler(wrapper -> {
                  EntityType entityType = EntityRewriter.this.setOldEntityId(wrapper);
                  if (entityType == fallingBlockType) {
                    int blockState = ((Integer)wrapper.get(Type.INT, 0)).intValue();
                    wrapper.set(Type.INT, 0, Integer.valueOf(EntityRewriter.this.protocol.getMappingData().getNewBlockStateId(blockState)));
                  } 
                });
          }
        });
  }
  
  public void registerSpawnTracker(ClientboundPacketType packetType) {
    this.protocol.registerOutgoing(packetType, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.UUID);
            map((Type)Type.VAR_INT);
            handler(wrapper -> EntityRewriter.this.setOldEntityId(wrapper));
          }
        });
  }
  
  private EntityType setOldEntityId(PacketWrapper wrapper) throws Exception {
    int typeId = ((Integer)wrapper.get((Type)Type.VAR_INT, 1)).intValue();
    EntityType entityType = getTypeFromId(typeId);
    addTrackedEntity(wrapper, ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue(), entityType);
    int oldTypeId = getOldEntityId(entityType.getId());
    if (typeId != oldTypeId)
      wrapper.set((Type)Type.VAR_INT, 1, Integer.valueOf(oldTypeId)); 
    return entityType;
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
                  int entityId = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
                  EntityType type = EntityRewriter.this.getEntityType(wrapper.user(), entityId);
                  MetaStorage storage = new MetaStorage((List)wrapper.get(newMetaType, 0));
                  EntityRewriter.this.handleMeta(wrapper.user(), entityId, storage);
                  EntityData entityData = EntityRewriter.this.getEntityData(type);
                  if (entityData != null && entityData.hasBaseMeta())
                    entityData.getDefaultMeta().createMeta(storage); 
                  wrapper.set(newMetaType, 0, storage.getMetaDataList());
                });
          }
        });
  }
  
  protected void registerMetadataRewriter(ClientboundPacketType packetType, Type<List<Metadata>> metaType) {
    registerMetadataRewriter(packetType, (Type<List<Metadata>>)null, metaType);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\api\rewriters\EntityRewriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */