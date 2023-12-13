// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.packets;

import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.Protocol1_16_2To1_16_1;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord1_16_2;
import java.util.ArrayList;
import java.util.List;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.types.Chunk1_16_2Type;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.types.Chunk1_16Type;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import com.viaversion.viaversion.rewriter.BlockRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;

public class WorldPackets
{
    private static final BlockChangeRecord[] EMPTY_RECORDS;
    
    public static void register(final Protocol protocol) {
        final BlockRewriter blockRewriter = new BlockRewriter(protocol, Type.POSITION1_14);
        blockRewriter.registerBlockAction(ClientboundPackets1_16.BLOCK_ACTION);
        blockRewriter.registerBlockChange(ClientboundPackets1_16.BLOCK_CHANGE);
        blockRewriter.registerAcknowledgePlayerDigging(ClientboundPackets1_16.ACKNOWLEDGE_PLAYER_DIGGING);
        protocol.registerClientbound(ClientboundPackets1_16.CHUNK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final Object val$protocol = protocol;
                    final Chunk chunk = wrapper.read((Type<Chunk>)new Chunk1_16Type());
                    wrapper.write(new Chunk1_16_2Type(), chunk);
                    for (int s = 0; s < chunk.getSections().length; ++s) {
                        final ChunkSection section = chunk.getSections()[s];
                        if (section != null) {
                            for (int i = 0; i < section.getPaletteSize(); ++i) {
                                final int old = section.getPaletteEntry(i);
                                section.setPaletteEntry(i, protocol.getMappingData().getNewBlockStateId(old));
                            }
                        }
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_16.MULTI_BLOCK_CHANGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final Object val$protocol = protocol;
                    wrapper.cancel();
                    final int chunkX = wrapper.read((Type<Integer>)Type.INT);
                    final int chunkZ = wrapper.read((Type<Integer>)Type.INT);
                    final long chunkPosition = 0L;
                    final long chunkPosition2 = chunkPosition | ((long)chunkX & 0x3FFFFFL) << 42;
                    final long chunkPosition3 = chunkPosition2 | ((long)chunkZ & 0x3FFFFFL) << 20;
                    final List<BlockChangeRecord>[] sectionRecords = new List[16];
                    final BlockChangeRecord[] array;
                    final BlockChangeRecord[] blockChangeRecord = array = wrapper.read(Type.BLOCK_CHANGE_RECORD_ARRAY);
                    int i = 0;
                    for (int length = array.length; i < length; ++i) {
                        final BlockChangeRecord record = array[i];
                        final int chunkY = record.getY() >> 4;
                        List<BlockChangeRecord> list = sectionRecords[chunkY];
                        if (list == null) {
                            final Object o;
                            final int n;
                            list = (o[n] = new ArrayList());
                        }
                        final int blockId = protocol.getMappingData().getNewBlockStateId(record.getBlockId());
                        list.add(new BlockChangeRecord1_16_2(record.getSectionX(), record.getSectionY(), record.getSectionZ(), blockId));
                    }
                    for (int chunkY2 = 0; chunkY2 < sectionRecords.length; ++chunkY2) {
                        final List<BlockChangeRecord> sectionRecord = sectionRecords[chunkY2];
                        if (sectionRecord != null) {
                            final PacketWrapper newPacket = wrapper.create(ClientboundPackets1_16_2.MULTI_BLOCK_CHANGE);
                            newPacket.write(Type.LONG, chunkPosition3 | ((long)chunkY2 & 0xFFFFFL));
                            newPacket.write(Type.BOOLEAN, false);
                            newPacket.write(Type.VAR_LONG_BLOCK_CHANGE_RECORD_ARRAY, sectionRecord.toArray(WorldPackets.EMPTY_RECORDS));
                            newPacket.send(Protocol1_16_2To1_16_1.class);
                        }
                    }
                });
            }
        });
        blockRewriter.registerEffect(ClientboundPackets1_16.EFFECT, 1010, 2001);
    }
    
    static {
        EMPTY_RECORDS = new BlockChangeRecord[0];
    }
}
