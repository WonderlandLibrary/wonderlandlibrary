// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_18to1_17_1.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import java.util.Iterator;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import java.util.BitSet;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.types.Chunk1_18Type;
import com.viaversion.viaversion.util.MathUtil;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk1_18;
import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
import com.viaversion.viaversion.api.minecraft.chunks.DataPaletteImpl;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSectionImpl;
import com.viaversion.viaversion.api.minecraft.blockentity.BlockEntityImpl;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import com.viaversion.viaversion.api.minecraft.blockentity.BlockEntity;
import java.util.ArrayList;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.types.Chunk1_17Type;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.storage.ChunkLightStorage;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.BlockEntityIds;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_17_1to1_17.ClientboundPackets1_17_1;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.Protocol1_18To1_17_1;

public final class WorldPackets
{
    public static void register(final Protocol1_18To1_17_1 protocol) {
        ((AbstractProtocol<ClientboundPackets1_17_1, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_17_1.BLOCK_ENTITY_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION1_14);
                this.handler(wrapper -> {
                    final short id = wrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                    final int newId = BlockEntityIds.newId(id);
                    wrapper.write(Type.VAR_INT, newId);
                    handleSpawners(newId, wrapper.passthrough(Type.NBT));
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_17_1, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_17_1.UPDATE_LIGHT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final int chunkX = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    final int chunkZ = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    if (wrapper.user().get(ChunkLightStorage.class).isLoaded(chunkX, chunkZ)) {
                        if (!Via.getConfig().cache1_17Light()) {
                            return;
                        }
                    }
                    else {
                        wrapper.cancel();
                    }
                    final boolean trustEdges = wrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                    final long[] skyLightMask = wrapper.passthrough(Type.LONG_ARRAY_PRIMITIVE);
                    final long[] blockLightMask = wrapper.passthrough(Type.LONG_ARRAY_PRIMITIVE);
                    final long[] emptySkyLightMask = wrapper.passthrough(Type.LONG_ARRAY_PRIMITIVE);
                    final long[] emptyBlockLightMask = wrapper.passthrough(Type.LONG_ARRAY_PRIMITIVE);
                    final int skyLightLenght = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    final byte[][] skyLight = new byte[skyLightLenght][];
                    for (int i = 0; i < skyLightLenght; ++i) {
                        skyLight[i] = wrapper.passthrough(Type.BYTE_ARRAY_PRIMITIVE);
                    }
                    final int blockLightLength = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    final byte[][] blockLight = new byte[blockLightLength][];
                    for (int j = 0; j < blockLightLength; ++j) {
                        blockLight[j] = wrapper.passthrough(Type.BYTE_ARRAY_PRIMITIVE);
                    }
                    final ChunkLightStorage lightStorage = wrapper.user().get(ChunkLightStorage.class);
                    lightStorage.storeLight(chunkX, chunkZ, new ChunkLightStorage.ChunkLight(trustEdges, skyLightMask, blockLightMask, emptySkyLightMask, emptyBlockLightMask, skyLight, blockLight));
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_17_1, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_17_1.CHUNK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final Object val$protocol = protocol;
                    final EntityTracker tracker = protocol.getEntityRewriter().tracker(wrapper.user());
                    final Chunk oldChunk = wrapper.read((Type<Chunk>)new Chunk1_17Type(tracker.currentWorldSectionHeight()));
                    final ArrayList<BlockEntity> blockEntities = new ArrayList<BlockEntity>(oldChunk.getBlockEntities().size());
                    oldChunk.getBlockEntities().iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final CompoundTag tag = iterator.next();
                        final NumberTag xTag = tag.get("x");
                        final NumberTag yTag = tag.get("y");
                        final NumberTag zTag = tag.get("z");
                        final StringTag idTag = tag.get("id");
                        if (xTag != null && yTag != null && zTag != null) {
                            if (idTag == null) {
                                continue;
                            }
                            else {
                                final String id = idTag.getValue();
                                final int typeId = protocol.getMappingData().blockEntityIds().getInt(id.replace("minecraft:", ""));
                                if (typeId == -1) {
                                    Via.getPlatform().getLogger().warning("Unknown block entity: " + id);
                                }
                                handleSpawners(typeId, tag);
                                final byte packedXZ = (byte)((xTag.asInt() & 0xF) << 4 | (zTag.asInt() & 0xF));
                                blockEntities.add(new BlockEntityImpl(packedXZ, yTag.asShort(), typeId, tag));
                            }
                        }
                    }
                    final int[] biomeData = oldChunk.getBiomeData();
                    final ChunkSection[] sections = oldChunk.getSections();
                    for (int i = 0; i < sections.length; ++i) {
                        ChunkSection section = sections[i];
                        if (section == null) {
                            section = new ChunkSectionImpl();
                            (sections[i] = section).setNonAirBlocksCount(0);
                            final DataPaletteImpl blockPalette = new DataPaletteImpl(4096);
                            blockPalette.addId(0);
                            section.addPalette(PaletteType.BLOCKS, blockPalette);
                        }
                        final DataPaletteImpl biomePalette = new DataPaletteImpl(64);
                        section.addPalette(PaletteType.BIOMES, biomePalette);
                        final int offset = i * 64;
                        for (int biomeIndex = 0, biomeArrayIndex = offset; biomeIndex < 64; ++biomeIndex, ++biomeArrayIndex) {
                            final int biome = biomeData[biomeArrayIndex];
                            biomePalette.setIdAt(biomeIndex, (biome != -1) ? biome : 0);
                        }
                    }
                    final Chunk chunk = new Chunk1_18(oldChunk.getX(), oldChunk.getZ(), sections, oldChunk.getHeightMap(), blockEntities);
                    wrapper.write(new Chunk1_18Type(tracker.currentWorldSectionHeight(), MathUtil.ceilLog2(protocol.getMappingData().getBlockStateMappings().mappedSize()), MathUtil.ceilLog2(tracker.biomesSent())), chunk);
                    final ChunkLightStorage lightStorage = wrapper.user().get(ChunkLightStorage.class);
                    final boolean alreadyLoaded = !lightStorage.addLoadedChunk(chunk.getX(), chunk.getZ());
                    final ChunkLightStorage.ChunkLight light = Via.getConfig().cache1_17Light() ? lightStorage.getLight(chunk.getX(), chunk.getZ()) : lightStorage.removeLight(chunk.getX(), chunk.getZ());
                    if (light == null) {
                        Via.getPlatform().getLogger().warning("No light data found for chunk at " + chunk.getX() + ", " + chunk.getZ() + ". Chunk was already loaded: " + alreadyLoaded);
                        final BitSet emptyLightMask = new BitSet();
                        emptyLightMask.set(0, tracker.currentWorldSectionHeight() + 2);
                        wrapper.write(Type.BOOLEAN, false);
                        wrapper.write(Type.LONG_ARRAY_PRIMITIVE, new long[0]);
                        wrapper.write(Type.LONG_ARRAY_PRIMITIVE, new long[0]);
                        wrapper.write(Type.LONG_ARRAY_PRIMITIVE, emptyLightMask.toLongArray());
                        wrapper.write(Type.LONG_ARRAY_PRIMITIVE, emptyLightMask.toLongArray());
                        wrapper.write(Type.VAR_INT, 0);
                        wrapper.write(Type.VAR_INT, 0);
                    }
                    else {
                        wrapper.write(Type.BOOLEAN, light.trustEdges());
                        wrapper.write(Type.LONG_ARRAY_PRIMITIVE, light.skyLightMask());
                        wrapper.write(Type.LONG_ARRAY_PRIMITIVE, light.blockLightMask());
                        wrapper.write(Type.LONG_ARRAY_PRIMITIVE, light.emptySkyLightMask());
                        wrapper.write(Type.LONG_ARRAY_PRIMITIVE, light.emptyBlockLightMask());
                        wrapper.write(Type.VAR_INT, light.skyLight().length);
                        light.skyLight();
                        final byte[][] array;
                        int j = 0;
                        for (int length = array.length; j < length; ++j) {
                            final byte[] skyLight = array[j];
                            wrapper.write(Type.BYTE_ARRAY_PRIMITIVE, skyLight);
                        }
                        wrapper.write(Type.VAR_INT, light.blockLight().length);
                        light.blockLight();
                        final byte[][] array2;
                        int k = 0;
                        for (int length2 = array2.length; k < length2; ++k) {
                            final byte[] blockLight = array2[k];
                            wrapper.write(Type.BYTE_ARRAY_PRIMITIVE, blockLight);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_17_1, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_17_1.UNLOAD_CHUNK, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final int chunkX = wrapper.passthrough((Type<Integer>)Type.INT);
                    final int chunkZ = wrapper.passthrough((Type<Integer>)Type.INT);
                    wrapper.user().get(ChunkLightStorage.class).clear(chunkX, chunkZ);
                });
            }
        });
    }
    
    private static void handleSpawners(final int typeId, final CompoundTag tag) {
        if (typeId == 8) {
            final Tag entity = tag.get("SpawnData");
            if (entity instanceof CompoundTag) {
                final CompoundTag spawnData = new CompoundTag();
                tag.put("SpawnData", spawnData);
                spawnData.put("entity", entity);
            }
        }
    }
}
