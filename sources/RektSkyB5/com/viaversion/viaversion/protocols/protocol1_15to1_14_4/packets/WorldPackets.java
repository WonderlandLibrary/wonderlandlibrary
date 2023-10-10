/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.protocols.protocol1_15to1_14_4.packets;

import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.types.Chunk1_14Type;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.Protocol1_15To1_14_4;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.types.Chunk1_15Type;
import com.viaversion.viaversion.rewriter.BlockRewriter;

public class WorldPackets {
    public static void register(final Protocol1_15To1_14_4 protocol) {
        BlockRewriter blockRewriter = new BlockRewriter(protocol, Type.POSITION1_14);
        blockRewriter.registerBlockAction(ClientboundPackets1_14.BLOCK_ACTION);
        blockRewriter.registerBlockChange(ClientboundPackets1_14.BLOCK_CHANGE);
        blockRewriter.registerMultiBlockChange(ClientboundPackets1_14.MULTI_BLOCK_CHANGE);
        blockRewriter.registerAcknowledgePlayerDigging(ClientboundPackets1_14.ACKNOWLEDGE_PLAYER_DIGGING);
        protocol.registerClientbound(ClientboundPackets1_14.CHUNK_DATA, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        int i2;
                        Chunk chunk = wrapper.read(new Chunk1_14Type());
                        wrapper.write(new Chunk1_15Type(), chunk);
                        if (chunk.isFullChunk()) {
                            int[] biomeData = chunk.getBiomeData();
                            int[] newBiomeData = new int[1024];
                            if (biomeData != null) {
                                for (i2 = 0; i2 < 4; ++i2) {
                                    for (int j2 = 0; j2 < 4; ++j2) {
                                        int x2 = (j2 << 2) + 2;
                                        int z2 = (i2 << 2) + 2;
                                        int oldIndex = z2 << 4 | x2;
                                        newBiomeData[i2 << 2 | j2] = biomeData[oldIndex];
                                    }
                                }
                                for (i2 = 1; i2 < 64; ++i2) {
                                    System.arraycopy(newBiomeData, 0, newBiomeData, i2 * 16, 16);
                                }
                            }
                            chunk.setBiomeData(newBiomeData);
                        }
                        for (int s2 = 0; s2 < chunk.getSections().length; ++s2) {
                            ChunkSection section = chunk.getSections()[s2];
                            if (section == null) continue;
                            for (i2 = 0; i2 < section.getPaletteSize(); ++i2) {
                                int old = section.getPaletteEntry(i2);
                                int newId = protocol.getMappingData().getNewBlockStateId(old);
                                section.setPaletteEntry(i2, newId);
                            }
                        }
                    }
                });
            }
        });
        blockRewriter.registerEffect(ClientboundPackets1_14.EFFECT, 1010, 2001);
        protocol.registerClientbound(ClientboundPackets1_14.SPAWN_PARTICLE, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.map((Type)Type.FLOAT, Type.DOUBLE);
                this.map((Type)Type.FLOAT, Type.DOUBLE);
                this.map((Type)Type.FLOAT, Type.DOUBLE);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.INT);
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        int id = wrapper.get(Type.INT, 0);
                        if (id == 3 || id == 23) {
                            int data = wrapper.passthrough(Type.VAR_INT);
                            wrapper.set(Type.VAR_INT, 0, protocol.getMappingData().getNewBlockStateId(data));
                        } else if (id == 32) {
                            protocol.getItemRewriter().handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                        }
                    }
                });
            }
        });
    }
}

