/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.packets;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.types.Particle;
import com.viaversion.viaversion.libs.fastutil.ints.IntOpenHashSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.ConnectionData;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.ConnectionHandler;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.NamedSoundRewriter;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.ParticleRewriter;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.providers.BlockEntityProvider;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.providers.PaintingProvider;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.storage.BlockStorage;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.types.Chunk1_13Type;
import com.viaversion.viaversion.protocols.protocol1_9_1_2to1_9_3_4.types.Chunk1_9_3_4Type;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class WorldPackets {
    private static final IntSet VALID_BIOMES;

    public static void register(Protocol protocol) {
        protocol.registerClientbound(ClientboundPackets1_12_1.SPAWN_PAINTING, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        String motive;
                        PaintingProvider provider = Via.getManager().getProviders().get(PaintingProvider.class);
                        Optional<Integer> id = provider.getIntByIdentifier(motive = wrapper.read(Type.STRING));
                        if (!(id.isPresent() || Via.getConfig().isSuppressConversionWarnings() && !Via.getManager().isDebug())) {
                            Via.getPlatform().getLogger().warning("Could not find painting motive: " + motive + " falling back to default (0)");
                        }
                        wrapper.write(Type.VAR_INT, id.orElse(0));
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_12_1.BLOCK_ENTITY_DATA, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.NBT);
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        BlockStorage storage;
                        BlockStorage.ReplacementData replacementData;
                        Position position = wrapper.get(Type.POSITION, 0);
                        short action = wrapper.get(Type.UNSIGNED_BYTE, 0);
                        CompoundTag tag = wrapper.get(Type.NBT, 0);
                        BlockEntityProvider provider = Via.getManager().getProviders().get(BlockEntityProvider.class);
                        int newId = provider.transform(wrapper.user(), position, tag, true);
                        if (newId != -1 && (replacementData = (storage = wrapper.user().get(BlockStorage.class)).get(position)) != null) {
                            replacementData.setReplacement(newId);
                        }
                        if (action == 5) {
                            wrapper.cancel();
                        }
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_12_1.BLOCK_ACTION, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        Position pos = wrapper.get(Type.POSITION, 0);
                        short action = wrapper.get(Type.UNSIGNED_BYTE, 0);
                        short param = wrapper.get(Type.UNSIGNED_BYTE, 1);
                        int blockId = wrapper.get(Type.VAR_INT, 0);
                        if (blockId == 25) {
                            blockId = 73;
                        } else if (blockId == 33) {
                            blockId = 99;
                        } else if (blockId == 29) {
                            blockId = 92;
                        } else if (blockId == 54) {
                            blockId = 142;
                        } else if (blockId == 146) {
                            blockId = 305;
                        } else if (blockId == 130) {
                            blockId = 249;
                        } else if (blockId == 138) {
                            blockId = 257;
                        } else if (blockId == 52) {
                            blockId = 140;
                        } else if (blockId == 209) {
                            blockId = 472;
                        } else if (blockId >= 219 && blockId <= 234) {
                            blockId = blockId - 219 + 483;
                        }
                        if (blockId == 73) {
                            PacketWrapper blockChange = wrapper.create(11);
                            blockChange.write(Type.POSITION, pos);
                            blockChange.write(Type.VAR_INT, 249 + action * 24 * 2 + param * 2);
                            blockChange.send(Protocol1_13To1_12_2.class);
                        }
                        wrapper.set(Type.VAR_INT, 0, blockId);
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_12_1.BLOCK_CHANGE, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        Position position = wrapper.get(Type.POSITION, 0);
                        int newId = WorldPackets.toNewId(wrapper.get(Type.VAR_INT, 0));
                        UserConnection userConnection = wrapper.user();
                        if (Via.getConfig().isServersideBlockConnections()) {
                            ConnectionData.updateBlockStorage(userConnection, position.x(), position.y(), position.z(), newId);
                            newId = ConnectionData.connect(userConnection, position, newId);
                        }
                        wrapper.set(Type.VAR_INT, 0, WorldPackets.checkStorage(wrapper.user(), position, newId));
                        if (Via.getConfig().isServersideBlockConnections()) {
                            wrapper.send(Protocol1_13To1_12_2.class);
                            wrapper.cancel();
                            ConnectionData.update(userConnection, position);
                        }
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_12_1.MULTI_BLOCK_CHANGE, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.BLOCK_CHANGE_RECORD_ARRAY);
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        Position position;
                        BlockChangeRecord[] records;
                        int chunkX = wrapper.get(Type.INT, 0);
                        int chunkZ = wrapper.get(Type.INT, 1);
                        UserConnection userConnection = wrapper.user();
                        for (BlockChangeRecord record : records = wrapper.get(Type.BLOCK_CHANGE_RECORD_ARRAY, 0)) {
                            int newBlock = WorldPackets.toNewId(record.getBlockId());
                            position = new Position(record.getSectionX() + chunkX * 16, record.getY(), record.getSectionZ() + chunkZ * 16);
                            if (Via.getConfig().isServersideBlockConnections()) {
                                ConnectionData.updateBlockStorage(userConnection, position.x(), position.y(), position.z(), newBlock);
                            }
                            record.setBlockId(WorldPackets.checkStorage(wrapper.user(), position, newBlock));
                        }
                        if (Via.getConfig().isServersideBlockConnections()) {
                            for (BlockChangeRecord record : records) {
                                int blockState = record.getBlockId();
                                position = new Position(record.getSectionX() + chunkX * 16, record.getY(), record.getSectionZ() + chunkZ * 16);
                                ConnectionHandler handler = ConnectionData.getConnectionHandler(blockState);
                                if (handler == null) continue;
                                blockState = handler.connect(userConnection, position, blockState);
                                record.setBlockId(blockState);
                            }
                            wrapper.send(Protocol1_13To1_12_2.class);
                            wrapper.cancel();
                            for (BlockChangeRecord record : records) {
                                Position position2 = new Position(record.getSectionX() + chunkX * 16, record.getY(), record.getSectionZ() + chunkZ * 16);
                                ConnectionData.update(userConnection, position2);
                            }
                        }
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_12_1.EXPLOSION, new PacketRemapper(){

            @Override
            public void registerMap() {
                if (!Via.getConfig().isServersideBlockConnections()) {
                    return;
                }
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.INT);
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        int i2;
                        UserConnection userConnection = wrapper.user();
                        int x2 = (int)Math.floor(wrapper.get(Type.FLOAT, 0).floatValue());
                        int y2 = (int)Math.floor(wrapper.get(Type.FLOAT, 1).floatValue());
                        int z2 = (int)Math.floor(wrapper.get(Type.FLOAT, 2).floatValue());
                        int recordCount = wrapper.get(Type.INT, 0);
                        Position[] records = new Position[recordCount];
                        for (i2 = 0; i2 < recordCount; ++i2) {
                            Position position;
                            records[i2] = position = new Position(x2 + wrapper.passthrough(Type.BYTE), (short)(y2 + wrapper.passthrough(Type.BYTE)), z2 + wrapper.passthrough(Type.BYTE));
                            ConnectionData.updateBlockStorage(userConnection, position.x(), position.y(), position.z(), 0);
                        }
                        wrapper.send(Protocol1_13To1_12_2.class);
                        wrapper.cancel();
                        for (i2 = 0; i2 < recordCount; ++i2) {
                            ConnectionData.update(userConnection, records[i2]);
                        }
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_12_1.UNLOAD_CHUNK, new PacketRemapper(){

            @Override
            public void registerMap() {
                if (Via.getConfig().isServersideBlockConnections()) {
                    this.handler(new PacketHandler(){

                        @Override
                        public void handle(PacketWrapper wrapper) throws Exception {
                            int x2 = wrapper.passthrough(Type.INT);
                            int z2 = wrapper.passthrough(Type.INT);
                            ConnectionData.blockConnectionProvider.unloadChunk(wrapper.user(), x2, z2);
                        }
                    });
                }
            }
        });
        protocol.registerClientbound(ClientboundPackets1_12_1.NAMED_SOUND, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        String sound = wrapper.get(Type.STRING, 0).replace("minecraft:", "");
                        String newSoundId = NamedSoundRewriter.getNewId(sound);
                        wrapper.set(Type.STRING, 0, newSoundId);
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_12_1.CHUNK_DATA, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
                        BlockStorage storage = wrapper.user().get(BlockStorage.class);
                        Chunk1_9_3_4Type type = new Chunk1_9_3_4Type(clientWorld);
                        Chunk1_13Type type1_13 = new Chunk1_13Type(clientWorld);
                        Chunk chunk = wrapper.read(type);
                        wrapper.write(type1_13, chunk);
                        for (int i2 = 0; i2 < chunk.getSections().length; ++i2) {
                            int block;
                            int x2;
                            int z2;
                            int y2;
                            int newId;
                            ChunkSection section = chunk.getSections()[i2];
                            if (section == null) continue;
                            for (int p2 = 0; p2 < section.getPaletteSize(); ++p2) {
                                int old = section.getPaletteEntry(p2);
                                newId = WorldPackets.toNewId(old);
                                section.setPaletteEntry(p2, newId);
                            }
                            boolean willSaveToStorage = false;
                            for (int p3 = 0; p3 < section.getPaletteSize(); ++p3) {
                                newId = section.getPaletteEntry(p3);
                                if (!storage.isWelcome(newId)) continue;
                                willSaveToStorage = true;
                                break;
                            }
                            boolean willSaveConnection = false;
                            if (Via.getConfig().isServersideBlockConnections() && ConnectionData.needStoreBlocks()) {
                                for (int p4 = 0; p4 < section.getPaletteSize(); ++p4) {
                                    int newId2 = section.getPaletteEntry(p4);
                                    if (!ConnectionData.isWelcome(newId2)) continue;
                                    willSaveConnection = true;
                                    break;
                                }
                            }
                            if (willSaveToStorage) {
                                for (y2 = 0; y2 < 16; ++y2) {
                                    for (z2 = 0; z2 < 16; ++z2) {
                                        for (x2 = 0; x2 < 16; ++x2) {
                                            block = section.getFlatBlock(x2, y2, z2);
                                            if (!storage.isWelcome(block)) continue;
                                            storage.store(new Position(x2 + (chunk.getX() << 4), (short)(y2 + (i2 << 4)), z2 + (chunk.getZ() << 4)), block);
                                        }
                                    }
                                }
                            }
                            if (!willSaveConnection) continue;
                            for (y2 = 0; y2 < 16; ++y2) {
                                for (z2 = 0; z2 < 16; ++z2) {
                                    for (x2 = 0; x2 < 16; ++x2) {
                                        block = section.getFlatBlock(x2, y2, z2);
                                        if (!ConnectionData.isWelcome(block)) continue;
                                        ConnectionData.blockConnectionProvider.storeBlock(wrapper.user(), x2 + (chunk.getX() << 4), y2 + (i2 << 4), z2 + (chunk.getZ() << 4), block);
                                    }
                                }
                            }
                        }
                        if (chunk.isBiomeData()) {
                            int latestBiomeWarn = Integer.MIN_VALUE;
                            for (int i3 = 0; i3 < 256; ++i3) {
                                int biome = chunk.getBiomeData()[i3];
                                if (VALID_BIOMES.contains(biome)) continue;
                                if (biome != 255 && latestBiomeWarn != biome) {
                                    if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
                                        Via.getPlatform().getLogger().warning("Received invalid biome id " + biome);
                                    }
                                    latestBiomeWarn = biome;
                                }
                                chunk.getBiomeData()[i3] = 1;
                            }
                        }
                        BlockEntityProvider provider = Via.getManager().getProviders().get(BlockEntityProvider.class);
                        Iterator<CompoundTag> iterator = chunk.getBlockEntities().iterator();
                        while (iterator.hasNext()) {
                            String id;
                            Object idTag;
                            CompoundTag tag = iterator.next();
                            int newId = provider.transform(wrapper.user(), null, tag, false);
                            if (newId != -1) {
                                int z3;
                                int y3;
                                int x3 = ((NumberTag)tag.get("x")).asInt();
                                Position position = new Position(x3, (short)(y3 = ((NumberTag)tag.get("y")).asInt()), z3 = ((NumberTag)tag.get("z")).asInt());
                                BlockStorage.ReplacementData replacementData = storage.get(position);
                                if (replacementData != null) {
                                    replacementData.setReplacement(newId);
                                }
                                chunk.getSections()[y3 >> 4].setFlatBlock(x3 & 0xF, y3 & 0xF, z3 & 0xF, newId);
                            }
                            if (!((idTag = tag.get("id")) instanceof StringTag) || !(id = ((StringTag)idTag).getValue()).equals("minecraft:noteblock") && !id.equals("minecraft:flower_pot")) continue;
                            iterator.remove();
                        }
                        if (Via.getConfig().isServersideBlockConnections()) {
                            ConnectionData.connectBlocks(wrapper.user(), chunk);
                            wrapper.send(Protocol1_13To1_12_2.class);
                            wrapper.cancel();
                            for (int i4 = 0; i4 < chunk.getSections().length; ++i4) {
                                ChunkSection section = chunk.getSections()[i4];
                                if (section == null) continue;
                                ConnectionData.updateChunkSectionNeighbours(wrapper.user(), chunk.getX(), chunk.getZ(), i4);
                            }
                        }
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_12_1.SPAWN_PARTICLE, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.INT);
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        int particleId = wrapper.get(Type.INT, 0);
                        int dataCount = 0;
                        if (particleId == 37 || particleId == 38 || particleId == 46) {
                            dataCount = 1;
                        } else if (particleId == 36) {
                            dataCount = 2;
                        }
                        Integer[] data = new Integer[dataCount];
                        for (int i2 = 0; i2 < data.length; ++i2) {
                            data[i2] = wrapper.read(Type.VAR_INT);
                        }
                        Particle particle = ParticleRewriter.rewriteParticle(particleId, data);
                        if (particle == null || particle.getId() == -1) {
                            wrapper.cancel();
                            return;
                        }
                        if (particle.getId() == 11) {
                            int count = wrapper.get(Type.INT, 1);
                            float speed = wrapper.get(Type.FLOAT, 6).floatValue();
                            if (count == 0) {
                                wrapper.set(Type.INT, 1, 1);
                                wrapper.set(Type.FLOAT, 6, Float.valueOf(0.0f));
                                List<Particle.ParticleData> arguments = particle.getArguments();
                                for (int i3 = 0; i3 < 3; ++i3) {
                                    float colorValue = wrapper.get(Type.FLOAT, i3 + 3).floatValue() * speed;
                                    if (colorValue == 0.0f && i3 == 0) {
                                        colorValue = 1.0f;
                                    }
                                    arguments.get(i3).setValue(Float.valueOf(colorValue));
                                    wrapper.set(Type.FLOAT, i3 + 3, Float.valueOf(0.0f));
                                }
                            }
                        }
                        wrapper.set(Type.INT, 0, particle.getId());
                        for (Particle.ParticleData particleData : particle.getArguments()) {
                            wrapper.write(particleData.getType(), particleData.getValue());
                        }
                    }
                });
            }
        });
    }

    public static int toNewId(int oldId) {
        int newId;
        if (oldId < 0) {
            oldId = 0;
        }
        if ((newId = Protocol1_13To1_12_2.MAPPINGS.getBlockMappings().getNewId(oldId)) != -1) {
            return newId;
        }
        newId = Protocol1_13To1_12_2.MAPPINGS.getBlockMappings().getNewId(oldId & 0xFFFFFFF0);
        if (newId != -1) {
            if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
                Via.getPlatform().getLogger().warning("Missing block " + oldId);
            }
            return newId;
        }
        if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
            Via.getPlatform().getLogger().warning("Missing block completely " + oldId);
        }
        return 1;
    }

    private static int checkStorage(UserConnection user, Position position, int newId) {
        BlockStorage storage = user.get(BlockStorage.class);
        if (storage.contains(position)) {
            BlockStorage.ReplacementData data = storage.get(position);
            if (data.getOriginal() == newId) {
                if (data.getReplacement() != -1) {
                    return data.getReplacement();
                }
            } else {
                storage.remove(position);
                if (storage.isWelcome(newId)) {
                    storage.store(position, newId);
                }
            }
        } else if (storage.isWelcome(newId)) {
            storage.store(position, newId);
        }
        return newId;
    }

    static {
        int i2;
        VALID_BIOMES = new IntOpenHashSet(70, 0.99f);
        for (i2 = 0; i2 < 50; ++i2) {
            VALID_BIOMES.add(i2);
        }
        VALID_BIOMES.add(127);
        for (i2 = 129; i2 <= 134; ++i2) {
            VALID_BIOMES.add(i2);
        }
        VALID_BIOMES.add(140);
        VALID_BIOMES.add(149);
        VALID_BIOMES.add(151);
        for (i2 = 155; i2 <= 158; ++i2) {
            VALID_BIOMES.add(i2);
        }
        for (i2 = 160; i2 <= 167; ++i2) {
            VALID_BIOMES.add(i2);
        }
    }
}

