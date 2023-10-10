// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_14to1_13_2.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import java.util.Arrays;
import com.viaversion.viaversion.api.minecraft.chunks.NibbleArray;
import com.viaversion.viaversion.api.minecraft.BlockFace;
import com.viaversion.viaversion.util.CompactArrayUtil;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_14Types;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSectionLight;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.storage.EntityTracker1_14;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.LongArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.types.Chunk1_14Type;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.types.Chunk1_13Type;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.BlockRewriter;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.Protocol1_14To1_13_2;

public class WorldPackets
{
    public static final int SERVERSIDE_VIEW_DISTANCE = 64;
    private static final byte[] FULL_LIGHT;
    public static int air;
    public static int voidAir;
    public static int caveAir;
    
    public static void register(final Protocol1_14To1_13_2 protocol) {
        final BlockRewriter blockRewriter = new BlockRewriter(protocol, null);
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_13.BLOCK_BREAK_ANIMATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.POSITION, Type.POSITION1_14);
                this.map(Type.BYTE);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_13.BLOCK_ENTITY_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION, Type.POSITION1_14);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_13.BLOCK_ACTION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION, Type.POSITION1_14);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        wrapper.set(Type.VAR_INT, 0, protocol.getMappingData().getNewBlockId(wrapper.get((Type<Integer>)Type.VAR_INT, 0)));
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_13.BLOCK_CHANGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION, Type.POSITION1_14);
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int id = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        wrapper.set(Type.VAR_INT, 0, protocol.getMappingData().getNewBlockStateId(id));
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_13.SERVER_DIFFICULTY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        wrapper.write(Type.BOOLEAN, false);
                    }
                });
            }
        });
        blockRewriter.registerMultiBlockChange(ClientboundPackets1_13.MULTI_BLOCK_CHANGE);
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_13.EXPLOSION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        for (int i = 0; i < 3; ++i) {
                            float coord = wrapper.get((Type<Float>)Type.FLOAT, i);
                            if (coord < 0.0f) {
                                coord = (float)(int)coord;
                                wrapper.set(Type.FLOAT, i, coord);
                            }
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_13.CHUNK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
                        final Chunk chunk = wrapper.read((Type<Chunk>)new Chunk1_13Type(clientWorld));
                        wrapper.write(new Chunk1_14Type(), chunk);
                        final int[] motionBlocking = new int[256];
                        final int[] worldSurface = new int[256];
                        for (int s = 0; s < chunk.getSections().length; ++s) {
                            final ChunkSection section = chunk.getSections()[s];
                            if (section != null) {
                                boolean hasBlock = false;
                                for (int i = 0; i < section.getPaletteSize(); ++i) {
                                    final int old = section.getPaletteEntry(i);
                                    final int newId = protocol.getMappingData().getNewBlockStateId(old);
                                    if (!hasBlock && newId != WorldPackets.air && newId != WorldPackets.voidAir && newId != WorldPackets.caveAir) {
                                        hasBlock = true;
                                    }
                                    section.setPaletteEntry(i, newId);
                                }
                                if (!hasBlock) {
                                    section.setNonAirBlocksCount(0);
                                }
                                else {
                                    int nonAirBlockCount = 0;
                                    for (int x = 0; x < 16; ++x) {
                                        for (int y = 0; y < 16; ++y) {
                                            for (int z = 0; z < 16; ++z) {
                                                final int id = section.getFlatBlock(x, y, z);
                                                if (id != WorldPackets.air && id != WorldPackets.voidAir && id != WorldPackets.caveAir) {
                                                    ++nonAirBlockCount;
                                                    worldSurface[x + z * 16] = y + s * 16 + 1;
                                                }
                                                if (protocol.getMappingData().getMotionBlocking().contains(id)) {
                                                    motionBlocking[x + z * 16] = y + s * 16 + 1;
                                                }
                                                if (Via.getConfig().isNonFullBlockLightFix() && protocol.getMappingData().getNonFullBlocks().contains(id)) {
                                                    setNonFullLight(chunk, section, s, x, y, z);
                                                }
                                            }
                                        }
                                    }
                                    section.setNonAirBlocksCount(nonAirBlockCount);
                                }
                            }
                        }
                        final CompoundTag heightMap = new CompoundTag();
                        heightMap.put("MOTION_BLOCKING", new LongArrayTag(encodeHeightMap(motionBlocking)));
                        heightMap.put("WORLD_SURFACE", new LongArrayTag(encodeHeightMap(worldSurface)));
                        chunk.setHeightMap(heightMap);
                        final PacketWrapper lightPacket = wrapper.create(ClientboundPackets1_14.UPDATE_LIGHT);
                        lightPacket.write(Type.VAR_INT, chunk.getX());
                        lightPacket.write(Type.VAR_INT, chunk.getZ());
                        int skyLightMask = chunk.isFullChunk() ? 262143 : 0;
                        int blockLightMask = 0;
                        for (int j = 0; j < chunk.getSections().length; ++j) {
                            final ChunkSection sec = chunk.getSections()[j];
                            if (sec != null) {
                                if (!chunk.isFullChunk() && sec.getLight().hasSkyLight()) {
                                    skyLightMask |= 1 << j + 1;
                                }
                                blockLightMask |= 1 << j + 1;
                            }
                        }
                        lightPacket.write(Type.VAR_INT, skyLightMask);
                        lightPacket.write(Type.VAR_INT, blockLightMask);
                        lightPacket.write(Type.VAR_INT, 0);
                        lightPacket.write(Type.VAR_INT, 0);
                        if (chunk.isFullChunk()) {
                            lightPacket.write(Type.BYTE_ARRAY_PRIMITIVE, WorldPackets.FULL_LIGHT);
                        }
                        for (final ChunkSection section2 : chunk.getSections()) {
                            if (section2 == null || !section2.getLight().hasSkyLight()) {
                                if (chunk.isFullChunk()) {
                                    lightPacket.write(Type.BYTE_ARRAY_PRIMITIVE, WorldPackets.FULL_LIGHT);
                                }
                            }
                            else {
                                lightPacket.write(Type.BYTE_ARRAY_PRIMITIVE, section2.getLight().getSkyLight());
                            }
                        }
                        if (chunk.isFullChunk()) {
                            lightPacket.write(Type.BYTE_ARRAY_PRIMITIVE, WorldPackets.FULL_LIGHT);
                        }
                        for (final ChunkSection section2 : chunk.getSections()) {
                            if (section2 != null) {
                                lightPacket.write(Type.BYTE_ARRAY_PRIMITIVE, section2.getLight().getBlockLight());
                            }
                        }
                        final EntityTracker1_14 entityTracker = wrapper.user().getEntityTracker(Protocol1_14To1_13_2.class);
                        final int diffX = Math.abs(entityTracker.getChunkCenterX() - chunk.getX());
                        final int diffZ = Math.abs(entityTracker.getChunkCenterZ() - chunk.getZ());
                        if (entityTracker.isForceSendCenterChunk() || diffX >= 64 || diffZ >= 64) {
                            final PacketWrapper fakePosLook = wrapper.create(ClientboundPackets1_14.UPDATE_VIEW_POSITION);
                            fakePosLook.write(Type.VAR_INT, chunk.getX());
                            fakePosLook.write(Type.VAR_INT, chunk.getZ());
                            fakePosLook.send(Protocol1_14To1_13_2.class);
                            entityTracker.setChunkCenterX(chunk.getX());
                            entityTracker.setChunkCenterZ(chunk.getZ());
                        }
                        lightPacket.send(Protocol1_14To1_13_2.class);
                        for (final ChunkSection section3 : chunk.getSections()) {
                            if (section3 != null) {
                                section3.setLight(null);
                            }
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_13.EFFECT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.POSITION, Type.POSITION1_14);
                this.map(Type.INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int id = wrapper.get((Type<Integer>)Type.INT, 0);
                        final int data = wrapper.get((Type<Integer>)Type.INT, 1);
                        if (id == 1010) {
                            wrapper.set(Type.INT, 1, protocol.getMappingData().getNewItemId(data));
                        }
                        else if (id == 2001) {
                            wrapper.set(Type.INT, 1, protocol.getMappingData().getNewBlockStateId(data));
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_13.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ClientWorld clientChunks = wrapper.user().get(ClientWorld.class);
                        final int dimensionId = wrapper.get((Type<Integer>)Type.INT, 1);
                        clientChunks.setEnvironment(dimensionId);
                        final int entityId = wrapper.get((Type<Integer>)Type.INT, 0);
                        final Entity1_14Types entType = Entity1_14Types.PLAYER;
                        final EntityTracker1_14 tracker = wrapper.user().getEntityTracker(Protocol1_14To1_13_2.class);
                        tracker.addEntity(entityId, entType);
                        tracker.setClientEntityId(entityId);
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final short difficulty = wrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                        final PacketWrapper difficultyPacket = wrapper.create(ClientboundPackets1_14.SERVER_DIFFICULTY);
                        difficultyPacket.write(Type.UNSIGNED_BYTE, difficulty);
                        difficultyPacket.write(Type.BOOLEAN, false);
                        difficultyPacket.scheduleSend(protocol.getClass());
                        wrapper.passthrough((Type<Object>)Type.UNSIGNED_BYTE);
                        wrapper.passthrough(Type.STRING);
                        wrapper.write(Type.VAR_INT, 64);
                    }
                });
                this.handler(wrapper -> {
                    wrapper.send(Protocol1_14To1_13_2.class);
                    wrapper.cancel();
                    sendViewDistancePacket(wrapper.user());
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_13.MAP_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.BOOLEAN);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        wrapper.write(Type.BOOLEAN, false);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_13.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
                        final int dimensionId = wrapper.get((Type<Integer>)Type.INT, 0);
                        clientWorld.setEnvironment(dimensionId);
                        final EntityTracker1_14 entityTracker = wrapper.user().getEntityTracker(Protocol1_14To1_13_2.class);
                        entityTracker.setForceSendCenterChunk(true);
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final short difficulty = wrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                        final PacketWrapper difficultyPacket = wrapper.create(ClientboundPackets1_14.SERVER_DIFFICULTY);
                        difficultyPacket.write(Type.UNSIGNED_BYTE, difficulty);
                        difficultyPacket.write(Type.BOOLEAN, false);
                        difficultyPacket.scheduleSend(protocol.getClass());
                    }
                });
                this.handler(wrapper -> {
                    wrapper.send(Protocol1_14To1_13_2.class);
                    wrapper.cancel();
                    sendViewDistancePacket(wrapper.user());
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_13.SPAWN_POSITION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION, Type.POSITION1_14);
            }
        });
    }
    
    private static void sendViewDistancePacket(final UserConnection connection) throws Exception {
        final PacketWrapper setViewDistance = PacketWrapper.create(ClientboundPackets1_14.UPDATE_VIEW_DISTANCE, null, connection);
        setViewDistance.write(Type.VAR_INT, 64);
        setViewDistance.send(Protocol1_14To1_13_2.class);
    }
    
    private static long[] encodeHeightMap(final int[] heightMap) {
        return CompactArrayUtil.createCompactArray(9, heightMap.length, i -> heightMap[i]);
    }
    
    private static void setNonFullLight(final Chunk chunk, final ChunkSection section, int ySection, final int x, final int y, final int z) {
        int skyLight = 0;
        int blockLight = 0;
        for (final BlockFace blockFace : BlockFace.values()) {
            NibbleArray skyLightArray = section.getLight().getSkyLightNibbleArray();
            NibbleArray blockLightArray = section.getLight().getBlockLightNibbleArray();
            final int neighbourX = x + blockFace.modX();
            int neighbourY = y + blockFace.modY();
            final int neighbourZ = z + blockFace.modZ();
            Label_0363: {
                if (blockFace.modX() != 0) {
                    if (neighbourX == 16) {
                        break Label_0363;
                    }
                    if (neighbourX == -1) {
                        break Label_0363;
                    }
                }
                else if (blockFace.modY() != 0) {
                    if (neighbourY == 16 || neighbourY == -1) {
                        if (neighbourY == 16) {
                            ++ySection;
                            neighbourY = 0;
                        }
                        else {
                            --ySection;
                            neighbourY = 15;
                        }
                        if (ySection == 16) {
                            break Label_0363;
                        }
                        if (ySection == -1) {
                            break Label_0363;
                        }
                        final ChunkSection newSection = chunk.getSections()[ySection];
                        if (newSection == null) {
                            break Label_0363;
                        }
                        skyLightArray = newSection.getLight().getSkyLightNibbleArray();
                        blockLightArray = newSection.getLight().getBlockLightNibbleArray();
                    }
                }
                else if (blockFace.modZ() != 0) {
                    if (neighbourZ == 16) {
                        break Label_0363;
                    }
                    if (neighbourZ == -1) {
                        break Label_0363;
                    }
                }
                if (blockLightArray != null && blockLight != 15) {
                    final int neighbourBlockLight = blockLightArray.get(neighbourX, neighbourY, neighbourZ);
                    if (neighbourBlockLight == 15) {
                        blockLight = 14;
                    }
                    else if (neighbourBlockLight > blockLight) {
                        blockLight = neighbourBlockLight - 1;
                    }
                }
                if (skyLightArray != null && skyLight != 15) {
                    final int neighbourSkyLight = skyLightArray.get(neighbourX, neighbourY, neighbourZ);
                    if (neighbourSkyLight == 15) {
                        if (blockFace.modY() == 1) {
                            skyLight = 15;
                        }
                        else {
                            skyLight = 14;
                        }
                    }
                    else if (neighbourSkyLight > skyLight) {
                        skyLight = neighbourSkyLight - 1;
                    }
                }
            }
        }
        if (skyLight != 0) {
            if (!section.getLight().hasSkyLight()) {
                final byte[] newSkyLight = new byte[2028];
                section.getLight().setSkyLight(newSkyLight);
            }
            section.getLight().getSkyLightNibbleArray().set(x, y, z, skyLight);
        }
        if (blockLight != 0) {
            section.getLight().getBlockLightNibbleArray().set(x, y, z, blockLight);
        }
    }
    
    private static long getChunkIndex(final int x, final int z) {
        return ((long)x & 0x3FFFFFFL) << 38 | ((long)z & 0x3FFFFFFL);
    }
    
    static {
        Arrays.fill(FULL_LIGHT = new byte[2048], (byte)(-1));
    }
}
