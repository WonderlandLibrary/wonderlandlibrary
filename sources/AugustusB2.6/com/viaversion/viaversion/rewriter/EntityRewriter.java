// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.rewriter;

import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import com.viaversion.viaversion.api.data.ParticleMappings;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
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
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import java.util.Iterator;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import java.util.Collection;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEventImpl;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;
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
    protected Int2IntMap typeMappings;
    
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
                    catch (Exception e) {
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
        catch (Exception e) {
            this.logException(e, type, metadataList, metadata);
            return false;
        }
    }
    
    @Deprecated
    protected void handleMetadata(final int entityId, final EntityType type, final Metadata metadata, final List<Metadata> metadatas, final UserConnection connection) throws Exception {
    }
    
    @Override
    public int newEntityId(final int id) {
        return (this.typeMappings != null) ? this.typeMappings.getOrDefault(id, id) : id;
    }
    
    public void mapEntityType(final EntityType type, final EntityType mappedType) {
        Preconditions.checkArgument(type.getClass() != mappedType.getClass(), (Object)"EntityTypes should not be of the same class/enum");
        this.mapEntityType(type.getId(), mappedType.getId());
    }
    
    protected void mapEntityType(final int id, final int mappedId) {
        if (this.typeMappings == null) {
            (this.typeMappings = new Int2IntOpenHashMap()).defaultReturnValue(-1);
        }
        this.typeMappings.put(id, mappedId);
    }
    
    public <E extends Enum> void mapTypes(final EntityType[] oldTypes, final Class<E> newTypeClass) {
        if (this.typeMappings == null) {
            (this.typeMappings = new Int2IntOpenHashMap(oldTypes.length, 0.99f)).defaultReturnValue(-1);
        }
        for (final EntityType oldType : oldTypes) {
            try {
                final E newType = java.lang.Enum.valueOf(newTypeClass, oldType.name());
                this.typeMappings.put(oldType.getId(), ((EntityType)newType).getId());
            }
            catch (IllegalArgumentException notFound) {
                if (!this.typeMappings.containsKey(oldType.getId())) {
                    Via.getPlatform().getLogger().warning("Could not find new entity type for " + oldType + "! Old type: " + oldType.getClass().getEnclosingClass().getSimpleName() + ", new type: " + newTypeClass.getEnclosingClass().getSimpleName());
                }
            }
        }
    }
    
    public void registerMetaTypeHandler(final MetaType itemType, final MetaType blockType, final MetaType particleType) {
        int data;
        this.filter().handler((event, meta) -> {
            if (itemType != null && meta.metaType() == itemType) {
                this.protocol.getItemRewriter().handleItemToClient((Item)meta.value());
            }
            else if (blockType != null && meta.metaType() == blockType) {
                data = (int)meta.value();
                meta.setValue(this.protocol.getMappingData().getNewBlockStateId(data));
            }
            else if (particleType != null && meta.metaType() == particleType) {
                this.rewriteParticle((Particle)meta.value());
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
                final EntityType val$fallingBlockType;
                final int entityId;
                final EntityType entityType;
                this.handler(wrapper -> {
                    val$fallingBlockType = fallingBlockType;
                    entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    entityType = EntityRewriter.this.tracker(wrapper.user()).entityType(entityId);
                    if (entityType == val$fallingBlockType) {
                        wrapper.set(Type.INT, 0, EntityRewriter.this.protocol.getMappingData().getNewBlockStateId(wrapper.get((Type<Integer>)Type.INT, 0)));
                    }
                });
            }
        });
    }
    
    public void registerTracker(final ClientboundPacketType packetType, final EntityType entityType, final Type<Integer> intType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                final Type<Integer> val$intType;
                final EntityType val$entityType;
                final int entityId;
                this.handler(wrapper -> {
                    val$intType = intType;
                    val$entityType = entityType;
                    entityId = wrapper.passthrough(val$intType);
                    EntityRewriter.this.tracker(wrapper.user()).addEntity(entityId, val$entityType);
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
                final int[] entityIds;
                final EntityTracker entityTracker;
                final int[] array;
                int length;
                int i = 0;
                int entity;
                this.handler(wrapper -> {
                    entityIds = wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
                    entityTracker = EntityRewriter.this.tracker(wrapper.user());
                    for (length = array.length; i < length; ++i) {
                        entity = array[i];
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
                final int entityId;
                this.handler(wrapper -> {
                    entityId = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
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
                final Type<List<Metadata>> val$newMetaType;
                final int entityId;
                final List<Metadata> metadata;
                this.handler(wrapper -> {
                    val$newMetaType = newMetaType;
                    entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    metadata = wrapper.get(val$newMetaType, 0);
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
    
    public PacketHandler worldDataTrackerHandler(final int nbtIndex) {
        final EntityTracker tracker;
        final CompoundTag registryData;
        final Tag height;
        int blockHeight;
        final Tag minY;
        final String world;
        return wrapper -> {
            tracker = this.tracker(wrapper.user());
            registryData = wrapper.get(Type.NBT, nbtIndex);
            height = registryData.get("height");
            if (height instanceof IntTag) {
                blockHeight = ((IntTag)height).asInt();
                tracker.setCurrentWorldSectionHeight(blockHeight >> 4);
            }
            else {
                Via.getPlatform().getLogger().warning("Height missing in dimension data: " + registryData);
            }
            minY = registryData.get("min_y");
            if (minY instanceof IntTag) {
                tracker.setCurrentMinY(((IntTag)minY).asInt());
            }
            else {
                Via.getPlatform().getLogger().warning("Min Y missing in dimension data: " + registryData);
            }
            world = wrapper.get(Type.STRING, 0);
            if (tracker.currentWorld() != null && !tracker.currentWorld().equals(world)) {
                tracker.clearEntities();
            }
            tracker.setCurrentWorld(world);
        };
    }
    
    public PacketHandler biomeSizeTracker() {
        final CompoundTag registry;
        final CompoundTag biomeRegistry;
        final ListTag biomes;
        return wrapper -> {
            registry = wrapper.get(Type.NBT, 0);
            biomeRegistry = registry.get("minecraft:worldgen/biome");
            biomes = biomeRegistry.get("value");
            this.tracker(wrapper.user()).setBiomesSent(biomes.size());
        };
    }
    
    public PacketHandler trackerAndRewriterHandler(final Type<List<Metadata>> metaType) {
        final int entityId;
        final int type;
        final int newType;
        final EntityType entType;
        return wrapper -> {
            entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
            type = wrapper.get((Type<Integer>)Type.VAR_INT, 1);
            newType = this.newEntityId(type);
            if (newType != type) {
                wrapper.set(Type.VAR_INT, 1, newType);
            }
            entType = this.typeFromId(this.trackMappedType ? newType : type);
            this.tracker(wrapper.user()).addEntity(entityId, entType);
            if (metaType != null) {
                this.handleMetadata(entityId, wrapper.get(metaType, 0), wrapper.user());
            }
        };
    }
    
    public PacketHandler trackerAndRewriterHandler(final Type<List<Metadata>> metaType, final EntityType entityType) {
        final int entityId;
        return wrapper -> {
            entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
            this.tracker(wrapper.user()).addEntity(entityId, entityType);
            if (metaType != null) {
                this.handleMetadata(entityId, wrapper.get(metaType, 0), wrapper.user());
            }
        };
    }
    
    public PacketHandler objectTrackerHandler() {
        final int entityId;
        final byte type;
        final EntityType entType;
        return wrapper -> {
            entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
            type = wrapper.get((Type<Byte>)Type.BYTE, 0);
            entType = this.objectTypeFromId(type);
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
