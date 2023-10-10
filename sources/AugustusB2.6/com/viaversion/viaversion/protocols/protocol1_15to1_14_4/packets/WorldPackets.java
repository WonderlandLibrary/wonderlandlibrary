// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_15to1_14_4.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.types.Chunk1_15Type;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.types.Chunk1_14Type;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.BlockRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.Protocol1_15To1_14_4;

public class WorldPackets
{
    public static void register(final Protocol1_15To1_14_4 protocol) {
        final BlockRewriter blockRewriter = new BlockRewriter(protocol, Type.POSITION1_14);
        blockRewriter.registerBlockAction(ClientboundPackets1_14.BLOCK_ACTION);
        blockRewriter.registerBlockChange(ClientboundPackets1_14.BLOCK_CHANGE);
        blockRewriter.registerMultiBlockChange(ClientboundPackets1_14.MULTI_BLOCK_CHANGE);
        blockRewriter.registerAcknowledgePlayerDigging(ClientboundPackets1_14.ACKNOWLEDGE_PLAYER_DIGGING);
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_14.CHUNK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Chunk chunk = wrapper.read((Type<Chunk>)new Chunk1_14Type());
                        wrapper.write(new Chunk1_15Type(), chunk);
                        if (chunk.isFullChunk()) {
                            final int[] biomeData = chunk.getBiomeData();
                            final int[] newBiomeData = new int[1024];
                            if (biomeData != null) {
                                for (int i = 0; i < 4; ++i) {
                                    for (int j = 0; j < 4; ++j) {
                                        final int x = (j << 2) + 2;
                                        final int z = (i << 2) + 2;
                                        final int oldIndex = z << 4 | x;
                                        newBiomeData[i << 2 | j] = biomeData[oldIndex];
                                    }
                                }
                                for (int i = 1; i < 64; ++i) {
                                    System.arraycopy(newBiomeData, 0, newBiomeData, i * 16, 16);
                                }
                            }
                            chunk.setBiomeData(newBiomeData);
                        }
                        for (int s = 0; s < chunk.getSections().length; ++s) {
                            final ChunkSection section = chunk.getSections()[s];
                            if (section != null) {
                                for (int i = 0; i < section.getPaletteSize(); ++i) {
                                    final int old = section.getPaletteEntry(i);
                                    final int newId = protocol.getMappingData().getNewBlockStateId(old);
                                    section.setPaletteEntry(i, newId);
                                }
                            }
                        }
                    }
                });
            }
        });
        blockRewriter.registerEffect(ClientboundPackets1_14.EFFECT, 1010, 2001);
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_14.SPAWN_PARTICLE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.map(Type.FLOAT, Type.DOUBLE);
                this.map(Type.FLOAT, Type.DOUBLE);
                this.map(Type.FLOAT, Type.DOUBLE);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int id = wrapper.get((Type<Integer>)Type.INT, 0);
                        if (id == 3 || id == 23) {
                            final int data = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                            wrapper.set(Type.VAR_INT, 0, protocol.getMappingData().getNewBlockStateId(data));
                        }
                        else if (id == 32) {
                            protocol.getItemRewriter().handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                        }
                    }
                });
            }
        });
    }
}
