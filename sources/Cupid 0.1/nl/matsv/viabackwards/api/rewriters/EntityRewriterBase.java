package nl.matsv.viabackwards.api.rewriters;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import nl.matsv.viabackwards.ViaBackwards;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerSettings;
import nl.matsv.viabackwards.api.entities.storage.EntityData;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import nl.matsv.viabackwards.api.entities.storage.MetaStorage;
import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import org.jetbrains.annotations.Nullable;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.data.ParticleMappings;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.Particle;
import us.myles.ViaVersion.exception.CancelException;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.viaversion.libs.fastutil.ints.Int2IntMap;
import us.myles.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;

public abstract class EntityRewriterBase<T extends BackwardsProtocol> extends Rewriter<T> {
  private final Map<EntityType, EntityData> entityTypes = new HashMap<>();
  
  private final List<MetaHandlerSettings> metaHandlers = new ArrayList<>();
  
  private final MetaType displayNameMetaType;
  
  private final MetaType displayVisibilityMetaType;
  
  private final int displayNameIndex;
  
  private final int displayVisibilityIndex;
  
  protected Int2IntMap typeMapping;
  
  EntityRewriterBase(T protocol, MetaType displayNameMetaType, int displayNameIndex, MetaType displayVisibilityMetaType, int displayVisibilityIndex) {
    super(protocol);
    this.displayNameMetaType = displayNameMetaType;
    this.displayNameIndex = displayNameIndex;
    this.displayVisibilityMetaType = displayVisibilityMetaType;
    this.displayVisibilityIndex = displayVisibilityIndex;
  }
  
  protected EntityType getEntityType(UserConnection connection, int id) {
    return getEntityTracker(connection).getEntityType(id);
  }
  
  protected void addTrackedEntity(PacketWrapper wrapper, int entityId, EntityType type) throws Exception {
    getEntityTracker(wrapper.user()).trackEntityType(entityId, type);
  }
  
  protected boolean hasData(EntityType type) {
    return this.entityTypes.containsKey(type);
  }
  
  @Nullable
  protected EntityData getEntityData(EntityType type) {
    return this.entityTypes.get(type);
  }
  
  protected EntityData mapEntity(EntityType oldType, EntityType replacement) {
    Preconditions.checkArgument((oldType.getClass() == replacement.getClass()));
    int mappedReplacementId = getOldEntityId(replacement.getId());
    EntityData data = new EntityData(oldType.getId(), mappedReplacementId);
    mapEntityDirect(oldType.getId(), mappedReplacementId);
    this.entityTypes.put(oldType, data);
    return data;
  }
  
  public <T extends Enum<T> & EntityType> void mapTypes(EntityType[] oldTypes, Class<T> newTypeClass) {
    if (this.typeMapping == null) {
      this.typeMapping = (Int2IntMap)new Int2IntOpenHashMap(oldTypes.length, 1.0F);
      this.typeMapping.defaultReturnValue(-1);
    } 
    for (EntityType oldType : oldTypes) {
      try {
        T newType = (T)Enum.valueOf(newTypeClass, oldType.name());
        this.typeMapping.put(oldType.getId(), ((EntityType)newType).getId());
      } catch (IllegalArgumentException illegalArgumentException) {}
    } 
  }
  
  public void mapEntityDirect(EntityType oldType, EntityType newType) {
    Preconditions.checkArgument((oldType.getClass() != newType.getClass()));
    mapEntityDirect(oldType.getId(), newType.getId());
  }
  
  private void mapEntityDirect(int oldType, int newType) {
    if (this.typeMapping == null) {
      this.typeMapping = (Int2IntMap)new Int2IntOpenHashMap();
      this.typeMapping.defaultReturnValue(-1);
    } 
    this.typeMapping.put(oldType, newType);
  }
  
  public MetaHandlerSettings registerMetaHandler() {
    MetaHandlerSettings settings = new MetaHandlerSettings();
    this.metaHandlers.add(settings);
    return settings;
  }
  
  protected MetaStorage handleMeta(UserConnection user, int entityId, MetaStorage storage) throws Exception {
    EntityTracker.StoredEntity storedEntity = getEntityTracker(user).getEntity(entityId);
    if (storedEntity == null) {
      if (!Via.getConfig().isSuppressMetadataErrors())
        ViaBackwards.getPlatform().getLogger().warning("Metadata for entity id: " + entityId + " not sent because the entity doesn't exist. " + storage); 
      throw CancelException.CACHED;
    } 
    EntityType type = storedEntity.getType();
    for (MetaHandlerSettings settings : this.metaHandlers) {
      List<Metadata> newData = new ArrayList<>();
      for (Metadata meta : storage.getMetaDataList()) {
        MetaHandlerEvent event = null;
        try {
          Metadata modifiedMeta = meta;
          if (settings.isGucci(type, meta)) {
            event = new MetaHandlerEvent(user, storedEntity, meta.getId(), meta, storage);
            modifiedMeta = settings.getHandler().handle(event);
            if (event.getExtraData() != null) {
              newData.addAll(event.getExtraData());
              event.clearExtraData();
            } 
          } 
          if (modifiedMeta == null)
            throw RemovedValueException.EX; 
          newData.add(modifiedMeta);
        } catch (RemovedValueException e) {
          if (event != null && event.getExtraData() != null)
            newData.addAll(event.getExtraData()); 
        } catch (Exception e) {
          Logger log = ViaBackwards.getPlatform().getLogger();
          log.warning("Unable to handle metadata " + meta + " for entity type " + type);
          log.warning(storage.getMetaDataList().stream().sorted(Comparator.comparingInt(Metadata::getId))
              .map(Metadata::toString).collect(Collectors.joining("\n", "Full metadata list: ", "")));
          e.printStackTrace();
        } 
      } 
      storage.setMetaDataList(newData);
    } 
    Metadata data = storage.get(this.displayNameIndex);
    if (data != null) {
      EntityData entityData = getEntityData(type);
      if (entityData != null && entityData.getMobName() != null && (data
        .getValue() == null || data.getValue().toString().isEmpty()) && data
        .getMetaType().getTypeID() == this.displayNameMetaType.getTypeID()) {
        data.setValue(entityData.getMobName());
        if (ViaBackwards.getConfig().alwaysShowOriginalMobName()) {
          storage.delete(this.displayVisibilityIndex);
          storage.add(new Metadata(this.displayVisibilityIndex, this.displayVisibilityMetaType, Boolean.valueOf(true)));
        } 
      } 
    } 
    return storage;
  }
  
  protected void registerExtraTracker(ClientboundPacketType packetType, final EntityType entityType, final Type intType) {
    getProtocol().registerOutgoing(packetType, new PacketRemapper() {
          public void registerMap() {
            map(intType);
            handler(wrapper -> EntityRewriterBase.this.addTrackedEntity(wrapper, ((Integer)wrapper.get(intType, 0)).intValue(), entityType));
          }
        });
  }
  
  protected void registerExtraTracker(ClientboundPacketType packetType, EntityType entityType) {
    registerExtraTracker(packetType, entityType, (Type)Type.VAR_INT);
  }
  
  protected void registerEntityDestroy(ClientboundPacketType packetType) {
    getProtocol().registerOutgoing(packetType, new PacketRemapper() {
          public void registerMap() {
            map(Type.VAR_INT_ARRAY_PRIMITIVE);
            handler(wrapper -> {
                  EntityTracker.ProtocolEntityTracker tracker = EntityRewriterBase.this.getEntityTracker(wrapper.user());
                  for (int entity : (int[])wrapper.get(Type.VAR_INT_ARRAY_PRIMITIVE, 0))
                    tracker.removeEntity(entity); 
                });
          }
        });
  }
  
  protected PacketHandler getTrackerHandler(Type intType, int typeIndex) {
    return wrapper -> {
        Number id = (Number)wrapper.get(intType, typeIndex);
        addTrackedEntity(wrapper, ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue(), getTypeFromId(id.intValue()));
      };
  }
  
  protected PacketHandler getTrackerHandler() {
    return getTrackerHandler((Type)Type.VAR_INT, 1);
  }
  
  protected PacketHandler getTrackerHandler(EntityType entityType, Type intType) {
    return wrapper -> addTrackedEntity(wrapper, ((Integer)wrapper.get(intType, 0)).intValue(), entityType);
  }
  
  protected PacketHandler getDimensionHandler(int index) {
    return wrapper -> {
        ClientWorld clientWorld = (ClientWorld)wrapper.user().get(ClientWorld.class);
        int dimensionId = ((Integer)wrapper.get(Type.INT, index)).intValue();
        clientWorld.setEnvironment(dimensionId);
      };
  }
  
  public EntityTracker.ProtocolEntityTracker getEntityTracker(UserConnection user) {
    return ((EntityTracker)user.get(EntityTracker.class)).get((BackwardsProtocol)getProtocol());
  }
  
  protected void rewriteParticle(Particle particle) {
    ParticleMappings mappings = this.protocol.getMappingData().getParticleMappings();
    int id = particle.getId();
    if (id == mappings.getBlockId() || id == mappings.getFallingDustId()) {
      Particle.ParticleData data = particle.getArguments().get(0);
      data.setValue(Integer.valueOf(this.protocol.getMappingData().getNewBlockStateId(((Integer)data.get()).intValue())));
    } else if (id == mappings.getItemId()) {
      Particle.ParticleData data = particle.getArguments().get(0);
      data.setValue(Integer.valueOf(this.protocol.getMappingData().getNewItemId(((Integer)data.get()).intValue())));
    } 
    particle.setId(this.protocol.getMappingData().getNewParticleId(id));
  }
  
  public int getOldEntityId(int newId) {
    return (this.typeMapping != null) ? this.typeMapping.getOrDefault(newId, newId) : newId;
  }
  
  protected abstract EntityType getTypeFromId(int paramInt);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\api\rewriters\EntityRewriterBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */