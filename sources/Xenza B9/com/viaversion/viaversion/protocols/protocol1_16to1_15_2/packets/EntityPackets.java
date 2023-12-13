// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_16to1_15_2.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.api.minecraft.WorldIdentifiers;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.storage.InventoryTracker1_16;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.ServerboundPackets1_16;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.MappingData;
import java.util.Arrays;
import com.viaversion.viaversion.api.type.types.version.Types1_16;
import com.viaversion.viaversion.api.type.types.version.Types1_14;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.UUID;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_16Types;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.metadata.MetadataRewriter1_16To1_15_2;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.Protocol1_16To1_15_2;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.LongTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.FloatTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;

public class EntityPackets
{
    private static final PacketHandler DIMENSION_HANDLER;
    public static final CompoundTag DIMENSIONS_TAG;
    private static final String[] WORLD_NAMES;
    
    private static CompoundTag createOverworldEntry() {
        final CompoundTag tag = new CompoundTag();
        tag.put("name", new StringTag("minecraft:overworld"));
        tag.put("has_ceiling", new ByteTag((byte)0));
        addSharedOverwaldEntries(tag);
        return tag;
    }
    
    private static CompoundTag createOverworldCavesEntry() {
        final CompoundTag tag = new CompoundTag();
        tag.put("name", new StringTag("minecraft:overworld_caves"));
        tag.put("has_ceiling", new ByteTag((byte)1));
        addSharedOverwaldEntries(tag);
        return tag;
    }
    
    private static void addSharedOverwaldEntries(final CompoundTag tag) {
        tag.put("piglin_safe", new ByteTag((byte)0));
        tag.put("natural", new ByteTag((byte)1));
        tag.put("ambient_light", new FloatTag(0.0f));
        tag.put("infiniburn", new StringTag("minecraft:infiniburn_overworld"));
        tag.put("respawn_anchor_works", new ByteTag((byte)0));
        tag.put("has_skylight", new ByteTag((byte)1));
        tag.put("bed_works", new ByteTag((byte)1));
        tag.put("has_raids", new ByteTag((byte)1));
        tag.put("logical_height", new IntTag(256));
        tag.put("shrunk", new ByteTag((byte)0));
        tag.put("ultrawarm", new ByteTag((byte)0));
    }
    
    private static CompoundTag createNetherEntry() {
        final CompoundTag tag = new CompoundTag();
        tag.put("piglin_safe", new ByteTag((byte)1));
        tag.put("natural", new ByteTag((byte)0));
        tag.put("ambient_light", new FloatTag(0.1f));
        tag.put("infiniburn", new StringTag("minecraft:infiniburn_nether"));
        tag.put("respawn_anchor_works", new ByteTag((byte)1));
        tag.put("has_skylight", new ByteTag((byte)0));
        tag.put("bed_works", new ByteTag((byte)0));
        tag.put("fixed_time", new LongTag(18000L));
        tag.put("has_raids", new ByteTag((byte)0));
        tag.put("name", new StringTag("minecraft:the_nether"));
        tag.put("logical_height", new IntTag(128));
        tag.put("shrunk", new ByteTag((byte)1));
        tag.put("ultrawarm", new ByteTag((byte)1));
        tag.put("has_ceiling", new ByteTag((byte)1));
        return tag;
    }
    
    private static CompoundTag createEndEntry() {
        final CompoundTag tag = new CompoundTag();
        tag.put("piglin_safe", new ByteTag((byte)0));
        tag.put("natural", new ByteTag((byte)0));
        tag.put("ambient_light", new FloatTag(0.0f));
        tag.put("infiniburn", new StringTag("minecraft:infiniburn_end"));
        tag.put("respawn_anchor_works", new ByteTag((byte)0));
        tag.put("has_skylight", new ByteTag((byte)0));
        tag.put("bed_works", new ByteTag((byte)0));
        tag.put("fixed_time", new LongTag(6000L));
        tag.put("has_raids", new ByteTag((byte)1));
        tag.put("name", new StringTag("minecraft:the_end"));
        tag.put("logical_height", new IntTag(256));
        tag.put("shrunk", new ByteTag((byte)0));
        tag.put("ultrawarm", new ByteTag((byte)0));
        tag.put("has_ceiling", new ByteTag((byte)0));
        return tag;
    }
    
    public static void register(final Protocol1_16To1_15_2 protocol) {
        final MetadataRewriter1_16To1_15_2 metadataRewriter = protocol.get(MetadataRewriter1_16To1_15_2.class);
        ((Protocol<ClientboundPackets1_15, ClientboundPackets1_16, S1, S2>)protocol).registerClientbound(ClientboundPackets1_15.SPAWN_GLOBAL_ENTITY, ClientboundPackets1_16.SPAWN_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final int entityId = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    final byte type = wrapper.read((Type<Byte>)Type.BYTE);
                    if (type != 1) {
                        wrapper.cancel();
                    }
                    else {
                        wrapper.user().getEntityTracker(Protocol1_16To1_15_2.class).addEntity(entityId, Entity1_16Types.LIGHTNING_BOLT);
                        wrapper.write(Type.UUID, UUID.randomUUID());
                        wrapper.write(Type.VAR_INT, Entity1_16Types.LIGHTNING_BOLT.getId());
                        wrapper.passthrough((Type<Object>)Type.DOUBLE);
                        wrapper.passthrough((Type<Object>)Type.DOUBLE);
                        wrapper.passthrough((Type<Object>)Type.DOUBLE);
                        wrapper.write(Type.BYTE, (Byte)0);
                        wrapper.write(Type.BYTE, (Byte)0);
                        wrapper.write(Type.INT, 0);
                        wrapper.write(Type.SHORT, (Short)0);
                        wrapper.write(Type.SHORT, (Short)0);
                        wrapper.write(Type.SHORT, (Short)0);
                    }
                });
            }
        });
        metadataRewriter.registerTrackerWithData(ClientboundPackets1_15.SPAWN_ENTITY, Entity1_16Types.FALLING_BLOCK);
        metadataRewriter.registerTracker(ClientboundPackets1_15.SPAWN_MOB);
        metadataRewriter.registerTracker(ClientboundPackets1_15.SPAWN_PLAYER, Entity1_16Types.PLAYER);
        metadataRewriter.registerMetadataRewriter(ClientboundPackets1_15.ENTITY_METADATA, Types1_14.METADATA_LIST, Types1_16.METADATA_LIST);
        metadataRewriter.registerRemoveEntities(ClientboundPackets1_15.DESTROY_ENTITIES);
        ((AbstractProtocol<ClientboundPackets1_15, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_15.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(EntityPackets.DIMENSION_HANDLER);
                this.map(Type.LONG);
                this.map(Type.UNSIGNED_BYTE);
                this.handler(wrapper -> {
                    wrapper.write(Type.BYTE, (Byte)(-1));
                    final String levelType = wrapper.read(Type.STRING);
                    wrapper.write(Type.BOOLEAN, false);
                    wrapper.write(Type.BOOLEAN, levelType.equals("flat"));
                    wrapper.write(Type.BOOLEAN, true);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_15, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_15.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.handler(wrapper -> {
                    wrapper.write(Type.BYTE, (Byte)(-1));
                    wrapper.write(Type.STRING_ARRAY, Arrays.copyOf(EntityPackets.WORLD_NAMES, EntityPackets.WORLD_NAMES.length));
                    wrapper.write(Type.NBT, EntityPackets.DIMENSIONS_TAG.clone());
                    return;
                });
                this.handler(EntityPackets.DIMENSION_HANDLER);
                this.map(Type.LONG);
                this.map(Type.UNSIGNED_BYTE);
                this.handler(wrapper -> {
                    wrapper.user().getEntityTracker(Protocol1_16To1_15_2.class).addEntity(wrapper.get((Type<Integer>)Type.INT, 0), Entity1_16Types.PLAYER);
                    final String type = wrapper.read(Type.STRING);
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                    wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                    wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                    wrapper.write(Type.BOOLEAN, false);
                    wrapper.write(Type.BOOLEAN, type.equals("flat"));
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_15, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_15.ENTITY_PROPERTIES, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final Object val$protocol = protocol;
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                    int actualSize;
                    final int size = actualSize = wrapper.passthrough((Type<Integer>)Type.INT);
                    for (int i = 0; i < size; ++i) {
                        final String key = wrapper.read(Type.STRING);
                        String attributeIdentifier = protocol.getMappingData().getAttributeMappings().get(key);
                        if (attributeIdentifier == null) {
                            attributeIdentifier = "minecraft:" + key;
                            if (!MappingData.isValid1_13Channel(attributeIdentifier)) {
                                if (!Via.getConfig().isSuppressConversionWarnings()) {
                                    Via.getPlatform().getLogger().warning("Invalid attribute: " + key);
                                }
                                --actualSize;
                                wrapper.read((Type<Object>)Type.DOUBLE);
                                for (int modifierSize = wrapper.read((Type<Integer>)Type.VAR_INT), j = 0; j < modifierSize; ++j) {
                                    wrapper.read(Type.UUID);
                                    wrapper.read((Type<Object>)Type.DOUBLE);
                                    wrapper.read((Type<Object>)Type.BYTE);
                                }
                                continue;
                            }
                        }
                        wrapper.write(Type.STRING, attributeIdentifier);
                        wrapper.passthrough((Type<Object>)Type.DOUBLE);
                        for (int modifierSize2 = wrapper.passthrough((Type<Integer>)Type.VAR_INT), k = 0; k < modifierSize2; ++k) {
                            wrapper.passthrough(Type.UUID);
                            wrapper.passthrough((Type<Object>)Type.DOUBLE);
                            wrapper.passthrough((Type<Object>)Type.BYTE);
                        }
                    }
                    if (size != actualSize) {
                        wrapper.set(Type.INT, 0, actualSize);
                    }
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_16>)protocol).registerServerbound(ServerboundPackets1_16.ANIMATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final InventoryTracker1_16 inventoryTracker = wrapper.user().get(InventoryTracker1_16.class);
                    if (inventoryTracker.getInventory() != -1) {
                        wrapper.cancel();
                    }
                });
            }
        });
    }
    
    static {
        DIMENSION_HANDLER = (wrapper -> {
            WorldIdentifiers map = Via.getConfig().get1_16WorldNamesMap();
            final WorldIdentifiers userMap = wrapper.user().get(WorldIdentifiers.class);
            if (userMap != null) {
                map = userMap;
            }
            final int dimension = wrapper.read((Type<Integer>)Type.INT);
            String dimensionName = null;
            String outputName = null;
            switch (dimension) {
                case -1: {
                    dimensionName = "minecraft:the_nether";
                    outputName = map.nether();
                    break;
                }
                case 0: {
                    dimensionName = "minecraft:overworld";
                    outputName = map.overworld();
                    break;
                }
                case 1: {
                    dimensionName = "minecraft:the_end";
                    outputName = map.end();
                    break;
                }
                default: {
                    Via.getPlatform().getLogger().warning("Invalid dimension id: " + dimension);
                    dimensionName = "minecraft:overworld";
                    outputName = map.overworld();
                    break;
                }
            }
            wrapper.write(Type.STRING, dimensionName);
            wrapper.write(Type.STRING, outputName);
            return;
        });
        DIMENSIONS_TAG = new CompoundTag();
        WORLD_NAMES = new String[] { "minecraft:overworld", "minecraft:the_nether", "minecraft:the_end" };
        final ListTag list = new ListTag(CompoundTag.class);
        list.add(createOverworldEntry());
        list.add(createOverworldCavesEntry());
        list.add(createNetherEntry());
        list.add(createEndEntry());
        EntityPackets.DIMENSIONS_TAG.put("dimension", list);
    }
}
