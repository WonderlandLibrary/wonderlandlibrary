// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_14_3to1_14_4;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public class Protocol1_14_3To1_14_4 extends BackwardsProtocol<ClientboundPackets1_14, ClientboundPackets1_14, ServerboundPackets1_14, ServerboundPackets1_14>
{
    public Protocol1_14_3To1_14_4() {
        super(ClientboundPackets1_14.class, ClientboundPackets1_14.class, ServerboundPackets1_14.class, ServerboundPackets1_14.class);
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol<ClientboundPackets1_14, ClientboundPackets1_14, S1, S2>)this).registerClientbound(ClientboundPackets1_14.ACKNOWLEDGE_PLAYER_DIGGING, ClientboundPackets1_14.BLOCK_CHANGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION1_14);
                this.map(Type.VAR_INT);
                final int status;
                final boolean allGood;
                this.handler(wrapper -> {
                    status = wrapper.read((Type<Integer>)Type.VAR_INT);
                    allGood = wrapper.read((Type<Boolean>)Type.BOOLEAN);
                    if (allGood && status == 0) {
                        wrapper.cancel();
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_14.TRADE_LIST, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        wrapper.passthrough((Type<Object>)Type.VAR_INT);
                        for (int size = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                            wrapper.passthrough(Type.FLAT_VAR_INT_ITEM);
                            wrapper.passthrough(Type.FLAT_VAR_INT_ITEM);
                            if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                                wrapper.passthrough(Type.FLAT_VAR_INT_ITEM);
                            }
                            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                            wrapper.passthrough((Type<Object>)Type.INT);
                            wrapper.passthrough((Type<Object>)Type.INT);
                            wrapper.passthrough((Type<Object>)Type.INT);
                            wrapper.passthrough((Type<Object>)Type.INT);
                            wrapper.passthrough((Type<Object>)Type.FLOAT);
                            wrapper.read((Type<Object>)Type.INT);
                        }
                    }
                });
            }
        });
    }
}
