// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_18to1_17_1.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
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
                final short id;
                final int newId;
                this.handler(wrapper -> {
                    id = wrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                    newId = BlockEntityIds.newId(id);
                    wrapper.write(Type.VAR_INT, newId);
                    handleSpawners(newId, wrapper.passthrough(Type.NBT));
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_17_1, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_17_1.UPDATE_LIGHT, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int chunkX;
                final int chunkZ;
                final boolean trustEdges;
                final long[] skyLightMask;
                final long[] blockLightMask;
                final long[] emptySkyLightMask;
                final long[] emptyBlockLightMask;
                final int skyLightLenght;
                final byte[][] skyLight;
                int i;
                final int blockLightLength;
                final byte[][] blockLight;
                int j;
                final ChunkLightStorage lightStorage;
                this.handler(wrapper -> {
                    chunkX = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    chunkZ = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    if (wrapper.user().get(ChunkLightStorage.class).isLoaded(chunkX, chunkZ)) {
                        if (!Via.getConfig().cache1_17Light()) {
                            return;
                        }
                    }
                    else {
                        wrapper.cancel();
                    }
                    trustEdges = wrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                    skyLightMask = wrapper.passthrough(Type.LONG_ARRAY_PRIMITIVE);
                    blockLightMask = wrapper.passthrough(Type.LONG_ARRAY_PRIMITIVE);
                    emptySkyLightMask = wrapper.passthrough(Type.LONG_ARRAY_PRIMITIVE);
                    emptyBlockLightMask = wrapper.passthrough(Type.LONG_ARRAY_PRIMITIVE);
                    skyLightLenght = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    skyLight = new byte[skyLightLenght][];
                    for (i = 0; i < skyLightLenght; ++i) {
                        skyLight[i] = wrapper.passthrough(Type.BYTE_ARRAY_PRIMITIVE);
                    }
                    blockLightLength = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    blockLight = new byte[blockLightLength][];
                    for (j = 0; j < blockLightLength; ++j) {
                        blockLight[j] = wrapper.passthrough(Type.BYTE_ARRAY_PRIMITIVE);
                    }
                    lightStorage = wrapper.user().get(ChunkLightStorage.class);
                    lightStorage.storeLight(chunkX, chunkZ, new ChunkLightStorage.ChunkLight(trustEdges, skyLightMask, blockLightMask, emptySkyLightMask, emptyBlockLightMask, skyLight, blockLight));
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_17_1, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_17_1.CHUNK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                final Protocol1_18To1_17_1 val$protocol;
                final EntityTracker tracker;
                final Chunk oldChunk;
                final ArrayList<BlockEntity> blockEntities;
                final Iterator<CompoundTag> iterator;
                CompoundTag tag;
                NumberTag xTag;
                NumberTag yTag;
                NumberTag zTag;
                StringTag idTag;
                String id;
                int typeId;
                byte packedXZ;
                final int[] biomeData;
                ChunkSection[] sections;
                int i;
                ChunkSection section;
                DataPaletteImpl blockPalette;
                DataPaletteImpl biomePalette;
                int offset;
                int biomeIndex;
                int biomeArrayIndex;
                int biome;
                final Chunk chunk;
                final ChunkLightStorage lightStorage;
                final boolean alreadyLoaded;
                final ChunkLightStorage.ChunkLight light;
                BitSet emptyLightMask;
                final byte[][] array;
                int length;
                int j = 0;
                byte[] skyLight;
                final byte[][] array2;
                int length2;
                int k = 0;
                byte[] blockLight;
                this.handler(wrapper -> {
                    val$protocol = protocol;
                    tracker = val$protocol.getEntityRewriter().tracker(wrapper.user());
                    oldChunk = wrapper.read((Type<Chunk>)new Chunk1_17Type(tracker.currentWorldSectionHeight()));
                    blockEntities = new ArrayList<BlockEntity>(oldChunk.getBlockEntities().size());
                    oldChunk.getBlockEntities().iterator();
                    while (iterator.hasNext()) {
                        tag = iterator.next();
                        xTag = tag.get("x");
                        yTag = tag.get("y");
                        zTag = tag.get("z");
                        idTag = tag.get("id");
                        if (xTag != null && yTag != null) {
                            if (zTag == null) {
                                continue;
                            }
                            else {
                                if (idTag == null) {
                                    if (tag.get("Chest") instanceof StringTag) {
                                        id = "minecraft:chest";
                                    }
                                    else if (tag.get("EnderChest") instanceof StringTag) {
                                        id = "minecraft:ender_chest";
                                    }
                                    else {
                                        continue;
                                    }
                                }
                                else {
                                    id = idTag.getValue();
                                }
                                typeId = val$protocol.getMappingData().blockEntityIds().getInt(id.replace("minecraft:", ""));
                                if (typeId == -1) {
                                    Via.getPlatform().getLogger().warning("Unknown block entity: " + id);
                                }
                                handleSpawners(typeId, tag);
                                packedXZ = (byte)((xTag.asInt() & 0xF) << 4 | (zTag.asInt() & 0xF));
                                blockEntities.add(new BlockEntityImpl(packedXZ, yTag.asShort(), typeId, tag));
                            }
                        }
                    }
                    biomeData = oldChunk.getBiomeData();
                    for (sections = oldChunk.getSections(), i = 0; i < sections.length; ++i) {
                        section = sections[i];
                        if (section == null) {
                            section = new ChunkSectionImpl();
                            (sections[i] = section).setNonAirBlocksCount(0);
                            blockPalette = new DataPaletteImpl(4096);
                            blockPalette.addId(0);
                            section.addPalette(PaletteType.BLOCKS, blockPalette);
                        }
                        biomePalette = new DataPaletteImpl(64);
                        section.addPalette(PaletteType.BIOMES, biomePalette);
                        offset = i * 64;
                        for (biomeIndex = 0, biomeArrayIndex = offset; biomeIndex < 64; ++biomeIndex, ++biomeArrayIndex) {
                            biome = biomeData[biomeArrayIndex];
                            biomePalette.setIdAt(biomeIndex, (biome != -1) ? biome : 0);
                        }
                    }
                    chunk = new Chunk1_18(oldChunk.getX(), oldChunk.getZ(), sections, oldChunk.getHeightMap(), blockEntities);
                    wrapper.write(new Chunk1_18Type(tracker.currentWorldSectionHeight(), MathUtil.ceilLog2(val$protocol.getMappingData().getBlockStateMappings().mappedSize()), MathUtil.ceilLog2(tracker.biomesSent())), chunk);
                    lightStorage = wrapper.user().get(ChunkLightStorage.class);
                    alreadyLoaded = !lightStorage.addLoadedChunk(chunk.getX(), chunk.getZ());
                    light = (Via.getConfig().cache1_17Light() ? lightStorage.getLight(chunk.getX(), chunk.getZ()) : lightStorage.removeLight(chunk.getX(), chunk.getZ()));
                    if (light == null) {
                        Via.getPlatform().getLogger().warning("No light data found for chunk at " + chunk.getX() + ", " + chunk.getZ() + ". Chunk was already loaded: " + alreadyLoaded);
                        emptyLightMask = new BitSet();
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
                        for (length = array.length; j < length; ++j) {
                            skyLight = array[j];
                            wrapper.write(Type.BYTE_ARRAY_PRIMITIVE, skyLight);
                        }
                        wrapper.write(Type.VAR_INT, light.blockLight().length);
                        light.blockLight();
                        for (length2 = array2.length; k < length2; ++k) {
                            blockLight = array2[k];
                            wrapper.write(Type.BYTE_ARRAY_PRIMITIVE, blockLight);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_17_1, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_17_1.UNLOAD_CHUNK, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int chunkX;
                final int chunkZ;
                this.handler(wrapper -> {
                    chunkX = wrapper.passthrough((Type<Integer>)Type.INT);
                    chunkZ = wrapper.passthrough((Type<Integer>)Type.INT);
                    wrapper.user().get(ChunkLightStorage.class).clear(chunkX, chunkZ);
                });
            }
        });
    }
    
    private static void handleSpawners(final int typeId, final CompoundTag tag) {
        if (typeId == 8) {
            final CompoundTag entity = tag.get("SpawnData");
            if (entity != null) {
                final CompoundTag spawnData = new CompoundTag();
                tag.put("SpawnData", spawnData);
                spawnData.put("entity", entity);
            }
        }
    }
}
