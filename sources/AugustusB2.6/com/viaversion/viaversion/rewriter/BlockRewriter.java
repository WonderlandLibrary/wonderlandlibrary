// 
// Decompiled by Procyon v0.5.36
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
                final int id;
                final int mappedId;
                this.handler(wrapper -> {
                    id = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    mappedId = BlockRewriter.this.protocol.getMappingData().getNewBlockId(id);
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
                final BlockChangeRecord[] array;
                int length;
                int i = 0;
                BlockChangeRecord record;
                this.handler(wrapper -> {
                    array = wrapper.passthrough(Type.BLOCK_CHANGE_RECORD_ARRAY);
                    for (length = array.length; i < length; ++i) {
                        record = array[i];
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
                final BlockChangeRecord[] array;
                int length;
                int i = 0;
                BlockChangeRecord record;
                this.handler(wrapper -> {
                    array = wrapper.passthrough(Type.VAR_LONG_BLOCK_CHANGE_RECORD_ARRAY);
                    for (length = array.length; i < length; ++i) {
                        record = array[i];
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
                final int val$playRecordId;
                final int val$blockBreakId;
                final int id;
                final int data;
                this.handler(wrapper -> {
                    val$playRecordId = playRecordId;
                    val$blockBreakId = blockBreakId;
                    id = wrapper.get((Type<Integer>)Type.INT, 0);
                    data = wrapper.get((Type<Integer>)Type.INT, 1);
                    if (id == val$playRecordId) {
                        wrapper.set(Type.INT, 1, BlockRewriter.this.protocol.getMappingData().getNewItemId(data));
                    }
                    else if (id == val$blockBreakId) {
                        wrapper.set(Type.INT, 1, BlockRewriter.this.protocol.getMappingData().getNewBlockStateId(data));
                    }
                });
            }
        });
    }
}
