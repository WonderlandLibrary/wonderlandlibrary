// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_17to1_16_4.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord1_16_2;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_17Types;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import java.util.BitSet;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.types.Chunk1_17Type;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.types.Chunk1_16_2Type;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ClientboundPackets1_17;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.BlockRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.Protocol1_17To1_16_4;

public final class WorldPackets
{
    public static void register(final Protocol1_17To1_16_4 protocol) {
        final BlockRewriter blockRewriter = new BlockRewriter(protocol, Type.POSITION1_14);
        blockRewriter.registerBlockAction(ClientboundPackets1_16_2.BLOCK_ACTION);
        blockRewriter.registerBlockChange(ClientboundPackets1_16_2.BLOCK_CHANGE);
        blockRewriter.registerVarLongMultiBlockChange(ClientboundPackets1_16_2.MULTI_BLOCK_CHANGE);
        blockRewriter.registerAcknowledgePlayerDigging(ClientboundPackets1_16_2.ACKNOWLEDGE_PLAYER_DIGGING);
        ((Protocol<ClientboundPackets1_16_2, ClientboundPackets1_17, S1, S2>)protocol).registerClientbound(ClientboundPackets1_16_2.WORLD_BORDER, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int type;
                ClientboundPacketType packetType = null;
                final IllegalArgumentException ex;
                this.handler(wrapper -> {
                    type = wrapper.read((Type<Integer>)Type.VAR_INT);
                    switch (type) {
                        case 0: {
                            packetType = ClientboundPackets1_17.WORLD_BORDER_SIZE;
                            break;
                        }
                        case 1: {
                            packetType = ClientboundPackets1_17.WORLD_BORDER_LERP_SIZE;
                            break;
                        }
                        case 2: {
                            packetType = ClientboundPackets1_17.WORLD_BORDER_CENTER;
                            break;
                        }
                        case 3: {
                            packetType = ClientboundPackets1_17.WORLD_BORDER_INIT;
                            break;
                        }
                        case 4: {
                            packetType = ClientboundPackets1_17.WORLD_BORDER_WARNING_DELAY;
                            break;
                        }
                        case 5: {
                            packetType = ClientboundPackets1_17.WORLD_BORDER_WARNING_DISTANCE;
                            break;
                        }
                        default: {
                            new IllegalArgumentException("Invalid world border type received: " + type);
                            throw ex;
                        }
                    }
                    wrapper.setId(packetType.getId());
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16_2, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_16_2.UPDATE_LIGHT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.BOOLEAN);
                final int skyLightMask;
                final int blockLightMask;
                this.handler(wrapper -> {
                    skyLightMask = wrapper.read((Type<Integer>)Type.VAR_INT);
                    blockLightMask = wrapper.read((Type<Integer>)Type.VAR_INT);
                    wrapper.write(Type.LONG_ARRAY_PRIMITIVE, this.toBitSetLongArray(skyLightMask));
                    wrapper.write(Type.LONG_ARRAY_PRIMITIVE, this.toBitSetLongArray(blockLightMask));
                    wrapper.write(Type.LONG_ARRAY_PRIMITIVE, this.toBitSetLongArray(wrapper.read((Type<Integer>)Type.VAR_INT)));
                    wrapper.write(Type.LONG_ARRAY_PRIMITIVE, this.toBitSetLongArray(wrapper.read((Type<Integer>)Type.VAR_INT)));
                    this.writeLightArrays(wrapper, skyLightMask);
                    this.writeLightArrays(wrapper, blockLightMask);
                });
            }
            
            private void writeLightArrays(final PacketWrapper wrapper, final int bitMask) throws Exception {
                final List<byte[]> light = new ArrayList<byte[]>();
                for (int i = 0; i < 18; ++i) {
                    if (this.isSet(bitMask, i)) {
                        light.add(wrapper.read(Type.BYTE_ARRAY_PRIMITIVE));
                    }
                }
                wrapper.write(Type.VAR_INT, light.size());
                for (final byte[] bytes : light) {
                    wrapper.write(Type.BYTE_ARRAY_PRIMITIVE, bytes);
                }
            }
            
            private long[] toBitSetLongArray(final int bitmask) {
                return new long[] { bitmask };
            }
            
            private boolean isSet(final int mask, final int i) {
                return (mask & 1 << i) != 0x0;
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16_2, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_16_2.CHUNK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: aload_0         /* this */
                //     2: getfield        com/viaversion/viaversion/protocols/protocol1_17to1_16_4/packets/WorldPackets$3.val$protocol:Lcom/viaversion/viaversion/protocols/protocol1_17to1_16_4/Protocol1_17To1_16_4;
                //     5: invokedynamic   BootstrapMethod #0, handle:(Lcom/viaversion/viaversion/protocols/protocol1_17to1_16_4/Protocol1_17To1_16_4;)Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
                //    10: invokevirtual   com/viaversion/viaversion/protocols/protocol1_17to1_16_4/packets/WorldPackets$3.handler:(Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
                //    13: return         
                // 
                // The error that occurred was:
                // 
                // java.lang.IllegalStateException: Could not infer any expression.
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:374)
                //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
                //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
                //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
                //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
                //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
                //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16_2, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_16_2.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.BYTE);
                this.map(Type.STRING_ARRAY);
                this.map(Type.NBT);
                this.map(Type.NBT);
                final CompoundTag dimensionRegistry;
                final ListTag dimensions;
                final Iterator<Tag> iterator;
                Tag dimension;
                CompoundTag dimensionCompound;
                final CompoundTag currentDimensionTag;
                final UserConnection user;
                this.handler(wrapper -> {
                    dimensionRegistry = (CompoundTag)wrapper.get(Type.NBT, 0).get("minecraft:dimension_type");
                    dimensions = dimensionRegistry.get("value");
                    dimensions.iterator();
                    while (iterator.hasNext()) {
                        dimension = iterator.next();
                        dimensionCompound = ((CompoundTag)dimension).get("element");
                        addNewDimensionData(dimensionCompound);
                    }
                    currentDimensionTag = wrapper.get(Type.NBT, 1);
                    addNewDimensionData(currentDimensionTag);
                    user = wrapper.user();
                    user.getEntityTracker(Protocol1_17To1_16_4.class).addEntity(wrapper.get((Type<Integer>)Type.INT, 0), Entity1_17Types.PLAYER);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16_2, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_16_2.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                final CompoundTag dimensionData;
                this.handler(wrapper -> {
                    dimensionData = wrapper.passthrough(Type.NBT);
                    addNewDimensionData(dimensionData);
                });
            }
        });
        blockRewriter.registerEffect(ClientboundPackets1_16_2.EFFECT, 1010, 2001);
    }
    
    private static void writeMultiBlockChangePacket(final PacketWrapper wrapper, final Chunk chunk) throws Exception {
        long chunkPosition = ((long)chunk.getX() & 0x3FFFFFL) << 42;
        chunkPosition |= ((long)chunk.getZ() & 0x3FFFFFL) << 20;
        final ChunkSection[] sections = chunk.getSections();
        for (int chunkY = 0; chunkY < sections.length; ++chunkY) {
            final ChunkSection section = sections[chunkY];
            if (section != null) {
                final PacketWrapper blockChangePacket = wrapper.create(ClientboundPackets1_17.MULTI_BLOCK_CHANGE);
                blockChangePacket.write(Type.LONG, chunkPosition | ((long)chunkY & 0xFFFFFL));
                blockChangePacket.write(Type.BOOLEAN, true);
                final BlockChangeRecord[] blockChangeRecords = new BlockChangeRecord[4096];
                int j = 0;
                for (int x = 0; x < 16; ++x) {
                    for (int y = 0; y < 16; ++y) {
                        for (int z = 0; z < 16; ++z) {
                            final int blockStateId = Protocol1_17To1_16_4.MAPPINGS.getNewBlockStateId(section.getFlatBlock(x, y, z));
                            blockChangeRecords[j++] = new BlockChangeRecord1_16_2(x, y, z, blockStateId);
                        }
                    }
                }
                blockChangePacket.write(Type.VAR_LONG_BLOCK_CHANGE_RECORD_ARRAY, blockChangeRecords);
                blockChangePacket.send(Protocol1_17To1_16_4.class);
            }
        }
    }
    
    private static void addNewDimensionData(final CompoundTag tag) {
        tag.put("min_y", new IntTag(0));
        tag.put("height", new IntTag(256));
    }
}
