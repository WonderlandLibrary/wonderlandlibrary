// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.rewriter;

import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import com.viaversion.viaversion.api.data.ParticleMappings;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.api.data.entity.DimensionData;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.type.types.Particle;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.data.Int2IntMapMappings;
import java.util.Iterator;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import java.util.Collection;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEventImpl;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import com.viaversion.viaversion.api.data.Mappings;
import com.viaversion.viaversion.rewriter.meta.MetaFilter;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.rewriter.RewriterBase;
import com.viaversion.viaversion.api.protocol.Protocol;

public abstract class EntityRewriter<T extends Protocol> extends RewriterBase<T> implements com.viaversion.viaversion.api.rewriter.EntityRewriter<T>
{
    private static final Metadata[] EMPTY_ARRAY;
    protected final List<MetaFilter> metadataFilters;
    protected final boolean trackMappedType;
    protected Mappings typeMappings;
    
    protected EntityRewriter(final T protocol) {
        this(protocol, true);
    }
    
    protected EntityRewriter(final T protocol, final boolean trackMappedType) {
        super(protocol);
        this.metadataFilters = new ArrayList<MetaFilter>();
        this.trackMappedType = trackMappedType;
        protocol.put(this);
    }
    
    public MetaFilter.Builder filter() {
        return new MetaFilter.Builder(this);
    }
    
    public void registerFilter(final MetaFilter filter) {
        Preconditions.checkArgument(!this.metadataFilters.contains(filter));
        this.metadataFilters.add(filter);
    }
    
    @Override
    public void handleMetadata(final int entityId, final List<Metadata> metadataList, final UserConnection connection) {
        final EntityType type = this.tracker(connection).entityType(entityId);
        int i = 0;
        for (final Metadata metadata : metadataList.toArray(EntityRewriter.EMPTY_ARRAY)) {
            if (!this.callOldMetaHandler(entityId, type, metadata, metadataList, connection)) {
                metadataList.remove(i--);
            }
            else {
                MetaHandlerEvent event = null;
                for (final MetaFilter filter : this.metadataFilters) {
                    if (!filter.isFiltered(type, metadata)) {
                        continue;
                    }
                    if (event == null) {
                        event = new MetaHandlerEventImpl(connection, type, entityId, metadata, metadataList);
                    }
                    try {
                        filter.handler().handle(event, metadata);
                    }
                    catch (final Exception e) {
                        this.logException(e, type, metadataList, metadata);
                        metadataList.remove(i--);
                        break;
                    }
                    if (event.cancelled()) {
                        metadataList.remove(i--);
                        break;
                    }
                }
                if (event != null && event.extraMeta() != null) {
                    metadataList.addAll(event.extraMeta());
                }
                ++i;
            }
        }
    }
    
    @Deprecated
    private boolean callOldMetaHandler(final int entityId, final EntityType type, final Metadata metadata, final List<Metadata> metadataList, final UserConnection connection) {
        try {
            this.handleMetadata(entityId, type, metadata, metadataList, connection);
            return true;
        }
        catch (final Exception e) {
            this.logException(e, type, metadataList, metadata);
            return false;
        }
    }
    
    @Deprecated
    protected void handleMetadata(final int entityId, final EntityType type, final Metadata metadata, final List<Metadata> metadatas, final UserConnection connection) throws Exception {
    }
    
    @Override
    public int newEntityId(final int id) {
        return (this.typeMappings != null) ? this.typeMappings.getNewIdOrDefault(id, id) : id;
    }
    
    public void mapEntityType(final EntityType type, final EntityType mappedType) {
        Preconditions.checkArgument(type.getClass() != mappedType.getClass(), (Object)"EntityTypes should not be of the same class/enum");
        this.mapEntityType(type.getId(), mappedType.getId());
    }
    
    protected void mapEntityType(final int id, final int mappedId) {
        if (this.typeMappings == null) {
            this.typeMappings = Int2IntMapMappings.of();
        }
        this.typeMappings.setNewId(id, mappedId);
    }
    
    public <E extends Enum<E> & EntityType> void mapTypes(final EntityType[] oldTypes, final Class<E> newTypeClass) {
        if (this.typeMappings == null) {
            this.typeMappings = Int2IntMapMappings.of();
        }
        for (final EntityType oldType : oldTypes) {
            try {
                final E newType = Enum.valueOf(newTypeClass, oldType.name());
                this.typeMappings.setNewId(oldType.getId(), newType.getId());
            }
            catch (final IllegalArgumentException notFound) {
                if (!this.typeMappings.contains(oldType.getId())) {
                    Via.getPlatform().getLogger().warning("Could not find new entity type for " + oldType + "! Old type: " + oldType.getClass().getEnclosingClass().getSimpleName() + ", new type: " + newTypeClass.getEnclosingClass().getSimpleName());
                }
            }
        }
    }
    
    public void mapTypes() {
        Preconditions.checkArgument(this.typeMappings == null, (Object)"Type mappings have already been set - manual type mappings should be set *after* this");
        Preconditions.checkNotNull(this.protocol.getMappingData().getEntityMappings(), (Object)"Protocol does not have entity mappings");
        this.typeMappings = this.protocol.getMappingData().getEntityMappings().mappings();
    }
    
    public void registerMetaTypeHandler(final MetaType itemType, final MetaType blockType, final MetaType particleType) {
        this.filter().handler((event, meta) -> {
            if (itemType != null && meta.metaType() == itemType) {
                this.protocol.getItemRewriter().handleItemToClient(meta.value());
            }
            else if (blockType != null && meta.metaType() == blockType) {
                final int data = meta.value();
                meta.setValue(this.protocol.getMappingData().getNewBlockStateId(data));
            }
            else if (particleType != null && meta.metaType() == particleType) {
                this.rewriteParticle(meta.value());
            }
        });
    }
    
    public void registerTracker(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.VAR_INT);
                this.handler(EntityRewriter.this.trackerHandler());
            }
        });
    }
    
    public void registerTrackerWithData(final ClientboundPacketType packetType, final EntityType fallingBlockType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.VAR_INT);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.INT);
                this.handler(EntityRewriter.this.trackerHandler());
                this.handler(wrapper -> {
                    final Object val$fallingBlockType = fallingBlockType;
                    final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final EntityType entityType = EntityRewriter.this.tracker(wrapper.user()).entityType(entityId);
                    if (entityType == fallingBlockType) {
                        wrapper.set(Type.INT, 0, EntityRewriter.this.protocol.getMappingData().getNewBlockStateId(wrapper.get((Type<Integer>)Type.INT, 0)));
                    }
                });
            }
        });
    }
    
    public void registerTrackerWithData1_19(final ClientboundPacketType packetType, final EntityType fallingBlockType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.VAR_INT);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.VAR_INT);
                this.handler(EntityRewriter.this.trackerHandler());
                this.handler(wrapper -> {
                    final Object val$fallingBlockType = fallingBlockType;
                    final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final EntityType entityType = EntityRewriter.this.tracker(wrapper.user()).entityType(entityId);
                    if (entityType == fallingBlockType) {
                        wrapper.set(Type.VAR_INT, 2, EntityRewriter.this.protocol.getMappingData().getNewBlockStateId(wrapper.get((Type<Integer>)Type.VAR_INT, 2)));
                    }
                });
            }
        });
    }
    
    public void registerTracker(final ClientboundPacketType packetType, final EntityType entityType, final Type<Integer> intType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final Object val$intType = intType;
                    final Object val$entityType = entityType;
                    final int entityId = wrapper.passthrough((Type<Integer>)intType);
                    EntityRewriter.this.tracker(wrapper.user()).addEntity(entityId, entityType);
                });
            }
        });
    }
    
    public void registerTracker(final ClientboundPacketType packetType, final EntityType entityType) {
        this.registerTracker(packetType, entityType, Type.VAR_INT);
    }
    
    public void registerRemoveEntities(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final int[] entityIds = wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
                    final EntityTracker entityTracker = EntityRewriter.this.tracker(wrapper.user());
                    final int[] array;
                    int i = 0;
                    for (int length = array.length; i < length; ++i) {
                        final int entity = array[i];
                        entityTracker.removeEntity(entity);
                    }
                });
            }
        });
    }
    
    public void registerRemoveEntity(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final int entityId = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    EntityRewriter.this.tracker(wrapper.user()).removeEntity(entityId);
                });
            }
        });
    }
    
    public void registerMetadataRewriter(final ClientboundPacketType packetType, final Type<List<Metadata>> oldMetaType, final Type<List<Metadata>> newMetaType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                if (oldMetaType != null) {
                    this.map(oldMetaType, newMetaType);
                }
                else {
                    this.map(newMetaType);
                }
                this.handler(wrapper -> {
                    final Object val$newMetaType = newMetaType;
                    final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final List<Metadata> metadata = wrapper.get((Type<List<Metadata>>)newMetaType, 0);
                    EntityRewriter.this.handleMetadata(entityId, metadata, wrapper.user());
                });
            }
        });
    }
    
    public void registerMetadataRewriter(final ClientboundPacketType packetType, final Type<List<Metadata>> metaType) {
        this.registerMetadataRewriter(packetType, null, metaType);
    }
    
    public PacketHandler trackerHandler() {
        return this.trackerAndRewriterHandler(null);
    }
    
    public PacketHandler playerTrackerHandler() {
        return wrapper -> {
            final EntityTracker tracker = this.tracker(wrapper.user());
            final int entityId = wrapper.get((Type<Integer>)Type.INT, 0);
            tracker.setClientEntityId(entityId);
            tracker.addEntity(entityId, tracker.playerType());
        };
    }
    
    public PacketHandler worldDataTrackerHandler(final int nbtIndex) {
        return wrapper -> {
            final EntityTracker tracker = this.tracker(wrapper.user());
            final CompoundTag registryData = wrapper.get(Type.NBT, nbtIndex);
            final Tag height = registryData.get("height");
            if (height instanceof IntTag) {
                final int blockHeight = ((IntTag)height).asInt();
                tracker.setCurrentWorldSectionHeight(blockHeight >> 4);
            }
            else {
                Via.getPlatform().getLogger().warning("Height missing in dimension data: " + registryData);
            }
            final Tag minY = registryData.get("min_y");
            if (minY instanceof IntTag) {
                tracker.setCurrentMinY(((IntTag)minY).asInt());
            }
            else {
                Via.getPlatform().getLogger().warning("Min Y missing in dimension data: " + registryData);
            }
            final String world = wrapper.get(Type.STRING, 0);
            if (tracker.currentWorld() != null && !tracker.currentWorld().equals(world)) {
                tracker.clearEntities();
                tracker.trackClientEntity();
            }
            tracker.setCurrentWorld(world);
        };
    }
    
    public PacketHandler worldDataTrackerHandlerByKey() {
        return wrapper -> {
            final EntityTracker tracker = this.tracker(wrapper.user());
            final String dimensionKey = wrapper.get(Type.STRING, 0);
            DimensionData dimensionData = tracker.dimensionData(dimensionKey);
            if (dimensionData == null) {
                Via.getPlatform().getLogger().severe("Dimension data missing for dimension: " + dimensionKey + ", falling back to overworld");
                dimensionData = tracker.dimensionData("minecraft:overworld");
                Preconditions.checkNotNull(dimensionData, (Object)"Overworld data missing");
            }
            tracker.setCurrentWorldSectionHeight(dimensionData.height() >> 4);
            tracker.setCurrentMinY(dimensionData.minY());
            final String world = wrapper.get(Type.STRING, 1);
            if (tracker.currentWorld() != null && !tracker.currentWorld().equals(world)) {
                tracker.clearEntities();
                tracker.trackClientEntity();
            }
            tracker.setCurrentWorld(world);
        };
    }
    
    public PacketHandler biomeSizeTracker() {
        return wrapper -> {
            final CompoundTag registry = wrapper.get(Type.NBT, 0);
            final CompoundTag biomeRegistry = registry.get("minecraft:worldgen/biome");
            final ListTag biomes = biomeRegistry.get("value");
            this.tracker(wrapper.user()).setBiomesSent(biomes.size());
        };
    }
    
    public PacketHandler trackerAndRewriterHandler(final Type<List<Metadata>> metaType) {
        return wrapper -> {
            final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
            final int type = wrapper.get((Type<Integer>)Type.VAR_INT, 1);
            final int newType = this.newEntityId(type);
            if (newType != type) {
                wrapper.set(Type.VAR_INT, 1, newType);
            }
            final EntityType entType = this.typeFromId(this.trackMappedType ? newType : type);
            this.tracker(wrapper.user()).addEntity(entityId, entType);
            if (metaType != null) {
                this.handleMetadata(entityId, wrapper.get((Type<List<Metadata>>)metaType, 0), wrapper.user());
            }
        };
    }
    
    public PacketHandler trackerAndRewriterHandler(final Type<List<Metadata>> metaType, final EntityType entityType) {
        return wrapper -> {
            final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
            this.tracker(wrapper.user()).addEntity(entityId, entityType);
            if (metaType != null) {
                this.handleMetadata(entityId, wrapper.get((Type<List<Metadata>>)metaType, 0), wrapper.user());
            }
        };
    }
    
    public PacketHandler objectTrackerHandler() {
        return wrapper -> {
            final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
            final byte type = wrapper.get((Type<Byte>)Type.BYTE, 0);
            final EntityType entType = this.objectTypeFromId(type);
            this.tracker(wrapper.user()).addEntity(entityId, entType);
        };
    }
    
    @Deprecated
    protected Metadata metaByIndex(final int index, final List<Metadata> metadataList) {
        for (final Metadata metadata : metadataList) {
            if (metadata.id() == index) {
                return metadata;
            }
        }
        return null;
    }
    
    protected void rewriteParticle(final Particle particle) {
        final ParticleMappings mappings = this.protocol.getMappingData().getParticleMappings();
        final int id = particle.getId();
        if (mappings.isBlockParticle(id)) {
            final Particle.ParticleData data = particle.getArguments().get(0);
            data.setValue(this.protocol.getMappingData().getNewBlockStateId(data.get()));
        }
        else if (mappings.isItemParticle(id) && this.protocol.getItemRewriter() != null) {
            final Particle.ParticleData data = particle.getArguments().get(0);
            final Item item = data.get();
            this.protocol.getItemRewriter().handleItemToClient(item);
        }
        particle.setId(this.protocol.getMappingData().getNewParticleId(id));
    }
    
    private void logException(final Exception e, final EntityType type, final List<Metadata> metadataList, final Metadata metadata) {
        if (!Via.getConfig().isSuppressMetadataErrors() || Via.getManager().isDebug()) {
            final Logger logger = Via.getPlatform().getLogger();
            logger.severe("An error occurred in metadata handler " + this.getClass().getSimpleName() + " for " + ((type != null) ? type.name() : "untracked") + " entity type: " + metadata);
            logger.severe(metadataList.stream().sorted(Comparator.comparingInt(Metadata::id)).map((Function<? super Object, ?>)Metadata::toString).collect((Collector<? super Object, ?, String>)Collectors.joining("\n", "Full metadata: ", "")));
            e.printStackTrace();
        }
    }
    
    static {
        EMPTY_ARRAY = new Metadata[0];
    }
}
