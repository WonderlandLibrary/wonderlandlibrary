// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.rewriter;

import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.Protocol;

public class BlockRewriter
{
    private final Protocol protocol;
    private final Type<Position> positionType;
    
    public BlockRewriter(final Protocol protocol, final Type<Position> positionType) {
        this.protocol = protocol;
        this.positionType = positionType;
    }
    
    public void registerBlockAction(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(BlockRewriter.this.positionType);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.VAR_INT);
                this.handler(wrapper -> {
                    final int id = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final int mappedId = BlockRewriter.this.protocol.getMappingData().getNewBlockId(id);
                    if (mappedId == -1) {
                        wrapper.cancel();
                    }
                    else {
                        wrapper.set(Type.VAR_INT, 0, mappedId);
                    }
                });
            }
        });
    }
    
    public void registerBlockChange(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(BlockRewriter.this.positionType);
                this.map(Type.VAR_INT);
                this.handler(wrapper -> wrapper.set(Type.VAR_INT, 0, BlockRewriter.this.protocol.getMappingData().getNewBlockStateId(wrapper.get((Type<Integer>)Type.VAR_INT, 0))));
            }
        });
    }
    
    public void registerMultiBlockChange(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.INT);
                this.handler(wrapper -> {
                    final BlockChangeRecord[] array = wrapper.passthrough(Type.BLOCK_CHANGE_RECORD_ARRAY);
                    int i = 0;
                    for (int length = array.length; i < length; ++i) {
                        final BlockChangeRecord record = array[i];
                        record.setBlockId(BlockRewriter.this.protocol.getMappingData().getNewBlockStateId(record.getBlockId()));
                    }
                });
            }
        });
    }
    
    public void registerVarLongMultiBlockChange(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.LONG);
                this.map(Type.BOOLEAN);
                this.handler(wrapper -> {
                    final BlockChangeRecord[] array = wrapper.passthrough(Type.VAR_LONG_BLOCK_CHANGE_RECORD_ARRAY);
                    int i = 0;
                    for (int length = array.length; i < length; ++i) {
                        final BlockChangeRecord record = array[i];
                        record.setBlockId(BlockRewriter.this.protocol.getMappingData().getNewBlockStateId(record.getBlockId()));
                    }
                });
            }
        });
    }
    
    public void registerAcknowledgePlayerDigging(final ClientboundPacketType packetType) {
        this.registerBlockChange(packetType);
    }
    
    public void registerEffect(final ClientboundPacketType packetType, final int playRecordId, final int blockBreakId) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(BlockRewriter.this.positionType);
                this.map(Type.INT);
                this.handler(wrapper -> {
                    final Object val$playRecordId = playRecordId;
                    final Object val$blockBreakId = blockBreakId;
                    final int id = wrapper.get((Type<Integer>)Type.INT, 0);
                    final int data = wrapper.get((Type<Integer>)Type.INT, 1);
                    if (id == playRecordId) {
                        wrapper.set(Type.INT, 1, BlockRewriter.this.protocol.getMappingData().getNewItemId(data));
                    }
                    else if (id == blockBreakId) {
                        wrapper.set(Type.INT, 1, BlockRewriter.this.protocol.getMappingData().getNewBlockStateId(data));
                    }
                });
            }
        });
    }
}
