// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_16to1_15_2.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import java.util.Map;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import com.viaversion.viaversion.api.type.types.UUIDIntArrayType;
import java.util.UUID;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.api.minecraft.Position;
import java.util.Iterator;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.util.CompactArrayUtil;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.LongArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.types.Chunk1_16Type;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.types.Chunk1_15Type;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.BlockRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.Protocol1_16To1_15_2;

public class WorldPackets
{
    public static void register(final Protocol1_16To1_15_2 protocol) {
        final BlockRewriter blockRewriter = new BlockRewriter(protocol, Type.POSITION1_14);
        blockRewriter.registerBlockAction(ClientboundPackets1_15.BLOCK_ACTION);
        blockRewriter.registerBlockChange(ClientboundPackets1_15.BLOCK_CHANGE);
        blockRewriter.registerMultiBlockChange(ClientboundPackets1_15.MULTI_BLOCK_CHANGE);
        blockRewriter.registerAcknowledgePlayerDigging(ClientboundPackets1_15.ACKNOWLEDGE_PLAYER_DIGGING);
        ((AbstractProtocol<ClientboundPackets1_15, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_15.UPDATE_LIGHT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.handler(wrapper -> wrapper.write(Type.BOOLEAN, true));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_15, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_15.CHUNK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final Object val$protocol = protocol;
                    final Chunk chunk = wrapper.read((Type<Chunk>)new Chunk1_15Type());
                    wrapper.write(new Chunk1_16Type(), chunk);
                    chunk.setIgnoreOldLightData(chunk.isFullChunk());
                    int i = 0;
                    for (int s = 0; s < chunk.getSections().length; ++s) {
                        final ChunkSection section = chunk.getSections()[s];
                        if (section != null) {
                            for (i = 0; i < section.getPaletteSize(); ++i) {
                                final int old = section.getPaletteEntry(i);
                                section.setPaletteEntry(i, protocol.getMappingData().getNewBlockStateId(old));
                            }
                        }
                    }
                    final CompoundTag heightMaps = chunk.getHeightMap();
                    heightMaps.values().iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final Tag heightMapTag = iterator.next();
                        final LongArrayTag heightMap = (LongArrayTag)heightMapTag;
                        final int[] heightMapData = new int[256];
                        CompactArrayUtil.iterateCompactArray(9, heightMapData.length, heightMap.getValue(), (i, v) -> heightMapData[i] = v);
                        heightMap.setValue(CompactArrayUtil.createCompactArrayWithPadding(9, heightMapData.length, i -> heightMapData[i]));
                    }
                    if (chunk.getBlockEntities() != null) {
                        chunk.getBlockEntities().iterator();
                        final Iterator iterator2;
                        while (iterator2.hasNext()) {
                            final CompoundTag blockEntity = iterator2.next();
                            handleBlockEntity(blockEntity);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_15, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_15.BLOCK_ENTITY_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final Position position = wrapper.passthrough(Type.POSITION1_14);
                    final short action = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE);
                    final CompoundTag tag = wrapper.passthrough(Type.NBT);
                    handleBlockEntity(tag);
                });
            }
        });
        blockRewriter.registerEffect(ClientboundPackets1_15.EFFECT, 1010, 2001);
    }
    
    private static void handleBlockEntity(final CompoundTag compoundTag) {
        final StringTag idTag = compoundTag.get("id");
        if (idTag == null) {
            return;
        }
        final String id = idTag.getValue();
        if (id.equals("minecraft:conduit")) {
            final Tag targetUuidTag = compoundTag.remove("target_uuid");
            if (!(targetUuidTag instanceof StringTag)) {
                return;
            }
            final UUID targetUuid = UUID.fromString((String)targetUuidTag.getValue());
            compoundTag.put("Target", new IntArrayTag(UUIDIntArrayType.uuidToIntArray(targetUuid)));
        }
        else if (id.equals("minecraft:skull") && compoundTag.get("Owner") instanceof CompoundTag) {
            final CompoundTag ownerTag = compoundTag.remove("Owner");
            final StringTag ownerUuidTag = ownerTag.remove("Id");
            if (ownerUuidTag != null) {
                final UUID ownerUuid = UUID.fromString(ownerUuidTag.getValue());
                ownerTag.put("Id", new IntArrayTag(UUIDIntArrayType.uuidToIntArray(ownerUuid)));
            }
            final CompoundTag skullOwnerTag = new CompoundTag();
            for (final Map.Entry<String, Tag> entry : ownerTag.entrySet()) {
                skullOwnerTag.put(entry.getKey(), entry.getValue());
            }
            compoundTag.put("SkullOwner", skullOwnerTag);
        }
    }
}
