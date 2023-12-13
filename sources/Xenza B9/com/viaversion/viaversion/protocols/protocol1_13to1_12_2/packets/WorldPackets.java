// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.packets;

import com.viaversion.viaversion.libs.fastutil.ints.IntOpenHashSet;
import java.util.List;
import com.viaversion.viaversion.api.type.types.Particle;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.ParticleRewriter;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import java.util.Iterator;
import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.types.Chunk1_13Type;
import com.viaversion.viaversion.protocols.protocol1_9_1_2to1_9_3_4.types.Chunk1_9_3_4Type;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.NamedSoundRewriter;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.ConnectionHandler;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.ConnectionData;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.storage.BlockStorage;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.providers.BlockEntityProvider;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.Optional;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.providers.PaintingProvider;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;

public class WorldPackets
{
    private static final IntSet VALID_BIOMES;
    
    public static void register(final Protocol protocol) {
        protocol.registerClientbound(ClientboundPackets1_12_1.SPAWN_PAINTING, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.handler(wrapper -> {
                    final PaintingProvider provider = Via.getManager().getProviders().get(PaintingProvider.class);
                    final String motive = wrapper.read(Type.STRING);
                    final Optional<Integer> id = provider.getIntByIdentifier(motive);
                    if (!id.isPresent() && (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug())) {
                        Via.getPlatform().getLogger().warning("Could not find painting motive: " + motive + " falling back to default (0)");
                    }
                    wrapper.write(Type.VAR_INT, id.orElse(0));
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_12_1.BLOCK_ENTITY_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.NBT);
                this.handler(wrapper -> {
                    final Position position = wrapper.get(Type.POSITION, 0);
                    final short action = wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    final CompoundTag tag = wrapper.get(Type.NBT, 0);
                    final BlockEntityProvider provider = Via.getManager().getProviders().get(BlockEntityProvider.class);
                    final int newId = provider.transform(wrapper.user(), position, tag, true);
                    if (newId != -1) {
                        final BlockStorage storage = wrapper.user().get(BlockStorage.class);
                        final BlockStorage.ReplacementData replacementData = storage.get(position);
                        if (replacementData != null) {
                            replacementData.setReplacement(newId);
                        }
                    }
                    if (action == 5) {
                        wrapper.cancel();
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_12_1.BLOCK_ACTION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.VAR_INT);
                this.handler(wrapper -> {
                    final Position pos = wrapper.get(Type.POSITION, 0);
                    final short action = wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    final short param = wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 1);
                    int blockId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    if (blockId == 25) {
                        blockId = 73;
                    }
                    else if (blockId == 33) {
                        blockId = 99;
                    }
                    else if (blockId == 29) {
                        blockId = 92;
                    }
                    else if (blockId == 54) {
                        blockId = 142;
                    }
                    else if (blockId == 146) {
                        blockId = 305;
                    }
                    else if (blockId == 130) {
                        blockId = 249;
                    }
                    else if (blockId == 138) {
                        blockId = 257;
                    }
                    else if (blockId == 52) {
                        blockId = 140;
                    }
                    else if (blockId == 209) {
                        blockId = 472;
                    }
                    else if (blockId >= 219 && blockId <= 234) {
                        blockId = blockId - 219 + 483;
                    }
                    if (blockId == 73) {
                        final PacketWrapper blockChange = wrapper.create(11);
                        blockChange.write(Type.POSITION, pos);
                        blockChange.write(Type.VAR_INT, 249 + action * 24 * 2 + param * 2);
                        blockChange.send(Protocol1_13To1_12_2.class);
                    }
                    wrapper.set(Type.VAR_INT, 0, blockId);
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_12_1.BLOCK_CHANGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.VAR_INT);
                this.handler(wrapper -> {
                    final Position position = wrapper.get(Type.POSITION, 0);
                    int newId = WorldPackets.toNewId(wrapper.get((Type<Integer>)Type.VAR_INT, 0));
                    final UserConnection userConnection = wrapper.user();
                    if (Via.getConfig().isServersideBlockConnections()) {
                        ConnectionData.updateBlockStorage(userConnection, position.x(), position.y(), position.z(), newId);
                        newId = ConnectionData.connect(userConnection, position, newId);
                    }
                    wrapper.set(Type.VAR_INT, 0, checkStorage(wrapper.user(), position, newId));
                    if (Via.getConfig().isServersideBlockConnections()) {
                        wrapper.send(Protocol1_13To1_12_2.class);
                        wrapper.cancel();
                        ConnectionData.update(userConnection, position);
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_12_1.MULTI_BLOCK_CHANGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.BLOCK_CHANGE_RECORD_ARRAY);
                this.handler(wrapper -> {
                    final int chunkX = wrapper.get((Type<Integer>)Type.INT, 0);
                    final int chunkZ = wrapper.get((Type<Integer>)Type.INT, 1);
                    final UserConnection userConnection = wrapper.user();
                    final BlockChangeRecord[] array;
                    final BlockChangeRecord[] records = array = wrapper.get(Type.BLOCK_CHANGE_RECORD_ARRAY, 0);
                    int i = 0;
                    for (int length = array.length; i < length; ++i) {
                        final BlockChangeRecord record = array[i];
                        final int newBlock = WorldPackets.toNewId(record.getBlockId());
                        final Position position = new Position(record.getSectionX() + chunkX * 16, record.getY(), record.getSectionZ() + chunkZ * 16);
                        if (Via.getConfig().isServersideBlockConnections()) {
                            ConnectionData.updateBlockStorage(userConnection, position.x(), position.y(), position.z(), newBlock);
                        }
                        record.setBlockId(checkStorage(wrapper.user(), position, newBlock));
                    }
                    if (Via.getConfig().isServersideBlockConnections()) {
                        final BlockChangeRecord[] array2;
                        int j = 0;
                        for (int length2 = array2.length; j < length2; ++j) {
                            final BlockChangeRecord record2 = array2[j];
                            final int blockState = record2.getBlockId();
                            final Position position2 = new Position(record2.getSectionX() + chunkX * 16, record2.getY(), record2.getSectionZ() + chunkZ * 16);
                            final ConnectionHandler handler = ConnectionData.getConnectionHandler(blockState);
                            if (handler != null) {
                                final int blockState2 = handler.connect(userConnection, position2, blockState);
                                record2.setBlockId(blockState2);
                            }
                        }
                        wrapper.send(Protocol1_13To1_12_2.class);
                        wrapper.cancel();
                        final BlockChangeRecord[] array3;
                        int k = 0;
                        for (int length3 = array3.length; k < length3; ++k) {
                            final BlockChangeRecord record3 = array3[k];
                            final Position position3 = new Position(record3.getSectionX() + chunkX * 16, record3.getY(), record3.getSectionZ() + chunkZ * 16);
                            ConnectionData.update(userConnection, position3);
                        }
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_12_1.EXPLOSION, new PacketRemapper() {
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
                this.handler(wrapper -> {
                    final UserConnection userConnection = wrapper.user();
                    final int x = (int)Math.floor(wrapper.get((Type<Float>)Type.FLOAT, 0));
                    final int y = (int)Math.floor(wrapper.get((Type<Float>)Type.FLOAT, 1));
                    final int z = (int)Math.floor(wrapper.get((Type<Float>)Type.FLOAT, 2));
                    final int recordCount = wrapper.get((Type<Integer>)Type.INT, 0);
                    final Position[] records = new Position[recordCount];
                    for (int i = 0; i < recordCount; ++i) {
                        final Position position = new Position(x + wrapper.passthrough((Type<Byte>)Type.BYTE), (short)(y + wrapper.passthrough((Type<Byte>)Type.BYTE)), z + wrapper.passthrough((Type<Byte>)Type.BYTE));
                        records[i] = position;
                        ConnectionData.updateBlockStorage(userConnection, position.x(), position.y(), position.z(), 0);
                    }
                    wrapper.send(Protocol1_13To1_12_2.class);
                    wrapper.cancel();
                    for (int j = 0; j < recordCount; ++j) {
                        ConnectionData.update(userConnection, records[j]);
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_12_1.UNLOAD_CHUNK, new PacketRemapper() {
            @Override
            public void registerMap() {
                if (Via.getConfig().isServersideBlockConnections()) {
                    this.handler(wrapper -> {
                        final int x = wrapper.passthrough((Type<Integer>)Type.INT);
                        final int z = wrapper.passthrough((Type<Integer>)Type.INT);
                        ConnectionData.blockConnectionProvider.unloadChunk(wrapper.user(), x, z);
                    });
                }
            }
        });
        protocol.registerClientbound(ClientboundPackets1_12_1.NAMED_SOUND, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(wrapper -> {
                    final String sound = wrapper.get(Type.STRING, 0).replace("minecraft:", "");
                    final String newSoundId = NamedSoundRewriter.getNewId(sound);
                    wrapper.set(Type.STRING, 0, newSoundId);
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_12_1.CHUNK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
                    final BlockStorage storage = wrapper.user().get(BlockStorage.class);
                    final Chunk1_9_3_4Type type = new Chunk1_9_3_4Type(clientWorld);
                    final Chunk1_13Type type1_13 = new Chunk1_13Type(clientWorld);
                    final Chunk chunk = wrapper.read((Type<Chunk>)type);
                    wrapper.write((Type<Chunk>)type1_13, chunk);
                    for (int s = 0; s < chunk.getSections().length; ++s) {
                        final ChunkSection section = chunk.getSections()[s];
                        if (section != null) {
                            final DataPalette blocks = section.palette(PaletteType.BLOCKS);
                            for (int p = 0; p < blocks.size(); ++p) {
                                final int old = blocks.idByIndex(p);
                                final int newId = WorldPackets.toNewId(old);
                                blocks.setIdByIndex(p, newId);
                            }
                            Label_0392_1: {
                                if (chunk.isFullChunk()) {
                                    boolean willSave = false;
                                    int p2 = 0;
                                    while (p2 < blocks.size()) {
                                        if (storage.isWelcome(blocks.idByIndex(p2))) {
                                            willSave = true;
                                            break;
                                        }
                                        else {
                                            ++p2;
                                        }
                                    }
                                    if (!willSave) {
                                        break Label_0392_1;
                                    }
                                }
                                for (int idx = 0; idx < 4096; ++idx) {
                                    final int id = blocks.idAt(idx);
                                    if (storage.isWelcome(id)) {
                                        storage.store(new Position(ChunkSection.xFromIndex(idx) + (chunk.getX() << 4), ChunkSection.yFromIndex(idx) + (s << 4), ChunkSection.zFromIndex(idx) + (chunk.getZ() << 4)), id);
                                    }
                                    else if (!chunk.isFullChunk()) {
                                        storage.remove(new Position(ChunkSection.xFromIndex(idx) + (chunk.getX() << 4), ChunkSection.yFromIndex(idx) + (s << 4), ChunkSection.zFromIndex(idx) + (chunk.getZ() << 4)));
                                    }
                                }
                            }
                            if (Via.getConfig().isServersideBlockConnections()) {
                                if (!(!ConnectionData.needStoreBlocks())) {
                                    if (!chunk.isFullChunk()) {
                                        ConnectionData.blockConnectionProvider.unloadChunkSection(wrapper.user(), chunk.getX(), s, chunk.getZ());
                                    }
                                    boolean willSave2 = false;
                                    int p3 = 0;
                                    while (p3 < blocks.size()) {
                                        if (ConnectionData.isWelcome(blocks.idByIndex(p3))) {
                                            willSave2 = true;
                                            break;
                                        }
                                        else {
                                            ++p3;
                                        }
                                    }
                                    if (!(!willSave2)) {
                                        for (int idx2 = 0; idx2 < 4096; ++idx2) {
                                            final int id2 = blocks.idAt(idx2);
                                            if (ConnectionData.isWelcome(id2)) {
                                                final int globalX = ChunkSection.xFromIndex(idx2) + (chunk.getX() << 4);
                                                final int globalY = ChunkSection.yFromIndex(idx2) + (s << 4);
                                                final int globalZ = ChunkSection.zFromIndex(idx2) + (chunk.getZ() << 4);
                                                ConnectionData.blockConnectionProvider.storeBlock(wrapper.user(), globalX, globalY, globalZ, id2);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (chunk.isBiomeData()) {
                        int latestBiomeWarn = Integer.MIN_VALUE;
                        for (int i = 0; i < 256; ++i) {
                            final int biome = chunk.getBiomeData()[i];
                            if (!WorldPackets.VALID_BIOMES.contains(biome)) {
                                if (biome != 255 && latestBiomeWarn != biome) {
                                    if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
                                        Via.getPlatform().getLogger().warning("Received invalid biome id " + biome);
                                    }
                                    latestBiomeWarn = biome;
                                }
                                chunk.getBiomeData()[i] = 1;
                            }
                        }
                    }
                    final BlockEntityProvider provider = Via.getManager().getProviders().get(BlockEntityProvider.class);
                    final Iterator<CompoundTag> iterator = chunk.getBlockEntities().iterator();
                    while (iterator.hasNext()) {
                        final CompoundTag tag = iterator.next();
                        final int newId2 = provider.transform(wrapper.user(), null, tag, false);
                        if (newId2 != -1) {
                            final int x = tag.get("x").asInt();
                            final int y = tag.get("y").asInt();
                            final int z = tag.get("z").asInt();
                            final Position position = new Position(x, (short)y, z);
                            final BlockStorage.ReplacementData replacementData = storage.get(position);
                            if (replacementData != null) {
                                replacementData.setReplacement(newId2);
                            }
                            chunk.getSections()[y >> 4].setFlatBlock(x & 0xF, y & 0xF, z & 0xF, newId2);
                        }
                        final Tag idTag = tag.get("id");
                        if (idTag instanceof StringTag) {
                            final String id3 = ((StringTag)idTag).getValue();
                            if (id3.equals("minecraft:noteblock") || id3.equals("minecraft:flower_pot")) {
                                iterator.remove();
                            }
                            else {
                                continue;
                            }
                        }
                    }
                    if (Via.getConfig().isServersideBlockConnections()) {
                        ConnectionData.connectBlocks(wrapper.user(), chunk);
                        wrapper.send(Protocol1_13To1_12_2.class);
                        wrapper.cancel();
                        for (int j = 0; j < chunk.getSections().length; ++j) {
                            final ChunkSection section2 = chunk.getSections()[j];
                            if (section2 != null) {
                                ConnectionData.updateChunkSectionNeighbours(wrapper.user(), chunk.getX(), chunk.getZ(), j);
                            }
                        }
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_12_1.SPAWN_PARTICLE, new PacketRemapper() {
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
                this.handler(wrapper -> {
                    final int particleId = wrapper.get((Type<Integer>)Type.INT, 0);
                    int dataCount = 0;
                    if (particleId == 37 || particleId == 38 || particleId == 46) {
                        dataCount = 1;
                    }
                    else if (particleId == 36) {
                        dataCount = 2;
                    }
                    final Integer[] data = new Integer[dataCount];
                    for (int i = 0; i < data.length; ++i) {
                        data[i] = wrapper.read((Type<Integer>)Type.VAR_INT);
                    }
                    final Particle particle = ParticleRewriter.rewriteParticle(particleId, data);
                    if (particle == null || particle.getId() == -1) {
                        wrapper.cancel();
                    }
                    else {
                        if (particle.getId() == 11) {
                            final int count = wrapper.get((Type<Integer>)Type.INT, 1);
                            final float speed = wrapper.get((Type<Float>)Type.FLOAT, 6);
                            if (count == 0) {
                                wrapper.set(Type.INT, 1, 1);
                                wrapper.set(Type.FLOAT, 6, 0.0f);
                                final List<Particle.ParticleData> arguments = particle.getArguments();
                                for (int j = 0; j < 3; ++j) {
                                    float colorValue = wrapper.get((Type<Float>)Type.FLOAT, j + 3) * speed;
                                    if (colorValue == 0.0f && j == 0) {
                                        colorValue = 1.0f;
                                    }
                                    arguments.get(j).setValue(colorValue);
                                    wrapper.set(Type.FLOAT, j + 3, 0.0f);
                                }
                            }
                        }
                        wrapper.set(Type.INT, 0, particle.getId());
                        particle.getArguments().iterator();
                        final Iterator iterator;
                        while (iterator.hasNext()) {
                            final Particle.ParticleData particleData = iterator.next();
                            wrapper.write(particleData.getType(), particleData.getValue());
                        }
                    }
                });
            }
        });
    }
    
    public static int toNewId(int oldId) {
        if (oldId < 0) {
            oldId = 0;
        }
        int newId = Protocol1_13To1_12_2.MAPPINGS.getBlockMappings().getNewId(oldId);
        if (newId != -1) {
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
    
    private static int checkStorage(final UserConnection user, final Position position, final int newId) {
        final BlockStorage storage = user.get(BlockStorage.class);
        if (storage.contains(position)) {
            final BlockStorage.ReplacementData data = storage.get(position);
            if (data.getOriginal() == newId) {
                if (data.getReplacement() != -1) {
                    return data.getReplacement();
                }
            }
            else {
                storage.remove(position);
                if (storage.isWelcome(newId)) {
                    storage.store(position, newId);
                }
            }
        }
        else if (storage.isWelcome(newId)) {
            storage.store(position, newId);
        }
        return newId;
    }
    
    static {
        VALID_BIOMES = new IntOpenHashSet(70, 0.99f);
        for (int i = 0; i < 50; ++i) {
            WorldPackets.VALID_BIOMES.add(i);
        }
        WorldPackets.VALID_BIOMES.add(127);
        for (int i = 129; i <= 134; ++i) {
            WorldPackets.VALID_BIOMES.add(i);
        }
        WorldPackets.VALID_BIOMES.add(140);
        WorldPackets.VALID_BIOMES.add(149);
        WorldPackets.VALID_BIOMES.add(151);
        for (int i = 155; i <= 158; ++i) {
            WorldPackets.VALID_BIOMES.add(i);
        }
        for (int i = 160; i <= 167; ++i) {
            WorldPackets.VALID_BIOMES.add(i);
        }
    }
}
