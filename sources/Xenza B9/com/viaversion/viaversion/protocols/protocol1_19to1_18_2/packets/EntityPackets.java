// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_19to1_18_2.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import java.io.IOException;
import com.viaversion.viaversion.api.minecraft.nbt.BinaryTagIO;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import java.util.Comparator;
import com.google.common.collect.Maps;
import com.viaversion.viaversion.util.Pair;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.ProfileKey;
import java.util.Iterator;
import com.viaversion.viaversion.api.minecraft.GlobalPosition;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.storage.DimensionRegistryStorage;
import java.util.Map;
import com.viaversion.viaversion.data.entity.DimensionDataImpl;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.api.data.entity.DimensionData;
import java.util.HashMap;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.ArrayList;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.ClientboundPackets1_19;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.types.version.Types1_19;
import com.viaversion.viaversion.api.type.types.version.Types1_18;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_19Types;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.ClientboundPackets1_18;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.Protocol1_19To1_18_2;
import com.viaversion.viaversion.rewriter.EntityRewriter;

public final class EntityPackets extends EntityRewriter<Protocol1_19To1_18_2>
{
    private static final String CHAT_REGISTRY_SNBT = "{\n  \"minecraft:chat_type\": {\n    \"type\": \"minecraft:chat_type\",\n    \"value\": [\n      {\n        \"name\": \"minecraft:system\",\n        \"id\": 1,\n        \"element\": {\n          \"chat\": {},\n          \"narration\": {\n            \"priority\": \"system\"\n          }\n        }\n      },\n      {\n        \"name\": \"minecraft:game_info\",\n        \"id\": 2,\n        \"element\": {\n          \"overlay\": {}\n        }\n      }\n    ]\n  }\n}";
    public static final CompoundTag CHAT_REGISTRY;
    
    public EntityPackets(final Protocol1_19To1_18_2 protocol) {
        super(protocol);
    }
    
    public void registerPackets() {
        this.registerTracker(ClientboundPackets1_18.SPAWN_PLAYER, Entity1_19Types.PLAYER);
        this.registerMetadataRewriter(ClientboundPackets1_18.ENTITY_METADATA, Types1_18.METADATA_LIST, Types1_19.METADATA_LIST);
        this.registerRemoveEntities(ClientboundPackets1_18.REMOVE_ENTITIES);
        ((AbstractProtocol<ClientboundPackets1_18, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_18.SPAWN_ENTITY, new PacketRemapper() {
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
                this.handler(wrapper -> {
                    final byte yaw = wrapper.get((Type<Byte>)Type.BYTE, 1);
                    wrapper.write(Type.BYTE, yaw);
                    return;
                });
                this.map(Type.INT, Type.VAR_INT);
                this.handler(EntityPackets.this.trackerHandler());
                this.handler(wrapper -> {
                    final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final EntityType entityType = EntityPackets.this.tracker(wrapper.user()).entityType(entityId);
                    if (entityType == Entity1_19Types.FALLING_BLOCK) {
                        wrapper.set(Type.VAR_INT, 2, ((Protocol1_19To1_18_2)EntityPackets.this.protocol).getMappingData().getNewBlockStateId(wrapper.get((Type<Integer>)Type.VAR_INT, 2)));
                    }
                });
            }
        });
        this.protocol.registerClientbound(ClientboundPackets1_18.SPAWN_PAINTING, ClientboundPackets1_19.SPAWN_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.handler(wrapper -> {
                    wrapper.write(Type.VAR_INT, Entity1_19Types.PAINTING.getId());
                    final int motive = wrapper.read((Type<Integer>)Type.VAR_INT);
                    final Position blockPosition = wrapper.read(Type.POSITION1_14);
                    final byte direction = wrapper.read((Type<Byte>)Type.BYTE);
                    wrapper.write(Type.DOUBLE, blockPosition.x() + 0.5);
                    wrapper.write(Type.DOUBLE, blockPosition.y() + 0.5);
                    wrapper.write(Type.DOUBLE, blockPosition.z() + 0.5);
                    wrapper.write(Type.BYTE, (Byte)0);
                    wrapper.write(Type.BYTE, (Byte)0);
                    wrapper.write(Type.BYTE, (Byte)0);
                    wrapper.write(Type.VAR_INT, to3dId(direction));
                    wrapper.write(Type.SHORT, (Short)0);
                    wrapper.write(Type.SHORT, (Short)0);
                    wrapper.write(Type.SHORT, (Short)0);
                    wrapper.send(Protocol1_19To1_18_2.class);
                    wrapper.cancel();
                    final PacketWrapper metaPacket = wrapper.create(ClientboundPackets1_19.ENTITY_METADATA);
                    metaPacket.write((Type<Object>)Type.VAR_INT, wrapper.get((Type<T>)Type.VAR_INT, 0));
                    final ArrayList<Metadata> metadata = new ArrayList<Metadata>();
                    metadata.add(new Metadata(8, Types1_19.META_TYPES.paintingVariantType, ((Protocol1_19To1_18_2)EntityPackets.this.protocol).getMappingData().getPaintingMappings().getNewIdOrDefault(motive, 0)));
                    metaPacket.write(Types1_19.METADATA_LIST, metadata);
                    metaPacket.send(Protocol1_19To1_18_2.class);
                });
            }
        });
        this.protocol.registerClientbound(ClientboundPackets1_18.SPAWN_MOB, ClientboundPackets1_19.SPAWN_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.VAR_INT);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.handler(wrapper -> {
                    final byte yaw = wrapper.read((Type<Byte>)Type.BYTE);
                    final byte pitch = wrapper.read((Type<Byte>)Type.BYTE);
                    wrapper.write(Type.BYTE, pitch);
                    wrapper.write(Type.BYTE, yaw);
                    return;
                });
                this.map(Type.BYTE);
                this.create(Type.VAR_INT, 0);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.handler(EntityPackets.this.trackerHandler());
            }
        });
        ((AbstractProtocol<ClientboundPackets1_18, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_18.ENTITY_EFFECT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.VAR_INT);
                this.map(Type.BYTE);
                this.create(Type.BOOLEAN, false);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_18, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_18.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.BYTE);
                this.map(Type.STRING_ARRAY);
                this.map(Type.NBT);
                this.handler(wrapper -> {
                    final CompoundTag tag = wrapper.get(Type.NBT, 0);
                    tag.put("minecraft:chat_type", EntityPackets.CHAT_REGISTRY.clone());
                    final ListTag dimensions = tag.get("minecraft:dimension_type").get("value");
                    final HashMap<String, DimensionData> dimensionDataMap = new HashMap<String, DimensionData>(dimensions.size());
                    final HashMap<CompoundTag, String> dimensionsMap = new HashMap<CompoundTag, String>(dimensions.size());
                    dimensions.iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final Tag dimension = iterator.next();
                        final CompoundTag dimensionCompound = (CompoundTag)dimension;
                        final CompoundTag element = dimensionCompound.get("element");
                        final String name = (String)dimensionCompound.get("name").getValue();
                        addMonsterSpawnData(element);
                        dimensionDataMap.put(name, new DimensionDataImpl(element));
                        dimensionsMap.put(element.clone(), name);
                    }
                    EntityPackets.this.tracker(wrapper.user()).setDimensions(dimensionDataMap);
                    final DimensionRegistryStorage registryStorage = wrapper.user().get(DimensionRegistryStorage.class);
                    registryStorage.setDimensions(dimensionsMap);
                    writeDimensionKey(wrapper, registryStorage);
                    return;
                });
                this.map(Type.STRING);
                this.map(Type.LONG);
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.BOOLEAN);
                this.map(Type.BOOLEAN);
                this.map(Type.BOOLEAN);
                this.map(Type.BOOLEAN);
                this.create(Type.OPTIONAL_GLOBAL_POSITION, null);
                this.handler(EntityPackets.this.playerTrackerHandler());
                this.handler(EntityPackets.this.worldDataTrackerHandlerByKey());
                this.handler(EntityPackets.this.biomeSizeTracker());
                this.handler(wrapper -> {
                    final PacketWrapper displayPreviewPacket = wrapper.create(ClientboundPackets1_19.SET_DISPLAY_CHAT_PREVIEW);
                    displayPreviewPacket.write(Type.BOOLEAN, false);
                    displayPreviewPacket.scheduleSend(Protocol1_19To1_18_2.class);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_18, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_18.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> writeDimensionKey(wrapper, wrapper.user().get(DimensionRegistryStorage.class)));
                this.map(Type.STRING);
                this.map(Type.LONG);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.BYTE);
                this.map(Type.BOOLEAN);
                this.map(Type.BOOLEAN);
                this.map(Type.BOOLEAN);
                this.create(Type.OPTIONAL_GLOBAL_POSITION, null);
                this.handler(EntityPackets.this.worldDataTrackerHandlerByKey());
            }
        });
        ((AbstractProtocol<ClientboundPackets1_18, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_18.PLAYER_INFO, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final int action = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    for (int entries = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < entries; ++i) {
                        wrapper.passthrough(Type.UUID);
                        if (action == 0) {
                            wrapper.passthrough(Type.STRING);
                            for (int properties = wrapper.passthrough((Type<Integer>)Type.VAR_INT), j = 0; j < properties; ++j) {
                                wrapper.passthrough(Type.STRING);
                                wrapper.passthrough(Type.STRING);
                                if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                                    wrapper.passthrough(Type.STRING);
                                }
                            }
                            wrapper.passthrough((Type<Object>)Type.VAR_INT);
                            wrapper.passthrough((Type<Object>)Type.VAR_INT);
                            if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                                wrapper.passthrough(Type.COMPONENT);
                            }
                            wrapper.write(Type.OPTIONAL_PROFILE_KEY, null);
                        }
                        else if (action == 1 || action == 2) {
                            wrapper.passthrough((Type<Object>)Type.VAR_INT);
                        }
                        else if (action == 3 && wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                            wrapper.passthrough(Type.COMPONENT);
                        }
                    }
                });
            }
        });
    }
    
    private static void writeDimensionKey(final PacketWrapper wrapper, final DimensionRegistryStorage registryStorage) throws Exception {
        final CompoundTag currentDimension = wrapper.read(Type.NBT);
        addMonsterSpawnData(currentDimension);
        String dimensionKey = registryStorage.dimensionKey(currentDimension);
        if (dimensionKey == null) {
            if (!Via.getConfig().isSuppressConversionWarnings()) {
                Via.getPlatform().getLogger().warning("The server tried to send dimension data from a dimension the client wasn't told about on join. Plugins and mods have to make sure they are not creating new dimension types while players are online, and proxies need to make sure they don't scramble dimension data. Received dimension: " + currentDimension + ". Known dimensions: " + registryStorage.dimensions());
            }
            dimensionKey = registryStorage.dimensions().entrySet().stream().map(it -> new Pair(it, Maps.difference((Map<?, ?>)currentDimension.getValue(), (Map<?, ?>)it.getKey().getValue()).entriesInCommon())).filter(it -> it.value().containsKey("min_y") && it.value().containsKey("height")).max(Comparator.comparingInt(it -> it.value().size())).orElseThrow(() -> {
                new IllegalArgumentException("Dimension not found in registry data from join packet: " + currentDimension);
                return;
            }).key().getValue();
        }
        wrapper.write(Type.STRING, dimensionKey);
    }
    
    private static int to3dId(final int id) {
        switch (id) {
            case -1: {
                return 1;
            }
            case 2: {
                return 2;
            }
            case 0: {
                return 3;
            }
            case 1: {
                return 4;
            }
            case 3: {
                return 5;
            }
            default: {
                throw new IllegalArgumentException("Unknown 2d id: " + id);
            }
        }
    }
    
    private static void addMonsterSpawnData(final CompoundTag dimension) {
        dimension.put("monster_spawn_block_light_limit", new IntTag(0));
        dimension.put("monster_spawn_light_level", new IntTag(11));
    }
    
    @Override
    protected void registerRewrites() {
        this.filter().handler((event, meta) -> meta.setMetaType(Types1_19.META_TYPES.byId(meta.metaType().typeId())));
        this.registerMetaTypeHandler(Types1_19.META_TYPES.itemType, Types1_19.META_TYPES.blockStateType, Types1_19.META_TYPES.particleType);
        this.filter().filterFamily(Entity1_19Types.MINECART_ABSTRACT).index(11).handler((event, meta) -> {
            final int data = (int)meta.getValue();
            meta.setValue(((Protocol1_19To1_18_2)this.protocol).getMappingData().getNewBlockStateId(data));
            return;
        });
        this.filter().type(Entity1_19Types.CAT).index(19).handler((event, meta) -> meta.setMetaType(Types1_19.META_TYPES.catVariantType));
    }
    
    @Override
    public void onMappingDataLoaded() {
        this.mapTypes();
    }
    
    @Override
    public EntityType typeFromId(final int type) {
        return Entity1_19Types.getTypeFromId(type);
    }
    
    static {
        try {
            CHAT_REGISTRY = BinaryTagIO.readString("{\n  \"minecraft:chat_type\": {\n    \"type\": \"minecraft:chat_type\",\n    \"value\": [\n      {\n        \"name\": \"minecraft:system\",\n        \"id\": 1,\n        \"element\": {\n          \"chat\": {},\n          \"narration\": {\n            \"priority\": \"system\"\n          }\n        }\n      },\n      {\n        \"name\": \"minecraft:game_info\",\n        \"id\": 2,\n        \"element\": {\n          \"overlay\": {}\n        }\n      }\n    ]\n  }\n}").get("minecraft:chat_type");
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
