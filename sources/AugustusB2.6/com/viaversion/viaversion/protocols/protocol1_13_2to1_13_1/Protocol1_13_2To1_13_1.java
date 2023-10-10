// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13_2to1_13_1;

import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_13_2to1_13_1.packets.EntityPackets;
import com.viaversion.viaversion.protocols.protocol1_13_2to1_13_1.packets.WorldPackets;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_13_2to1_13_1.packets.InventoryPackets;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;

public class Protocol1_13_2To1_13_1 extends AbstractProtocol<ClientboundPackets1_13, ClientboundPackets1_13, ServerboundPackets1_13, ServerboundPackets1_13>
{
    public Protocol1_13_2To1_13_1() {
        super(ClientboundPackets1_13.class, ClientboundPackets1_13.class, ServerboundPackets1_13.class, ServerboundPackets1_13.class);
    }
    
    @Override
    protected void registerPackets() {
        InventoryPackets.register(this);
        WorldPackets.register(this);
        EntityPackets.register(this);
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_13>)this).registerServerbound(ServerboundPackets1_13.EDIT_BOOK, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.FLAT_VAR_INT_ITEM, Type.FLAT_ITEM);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_13.ADVANCEMENTS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                        for (int size = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                            wrapper.passthrough(Type.STRING);
                            if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                                wrapper.passthrough(Type.STRING);
                            }
                            if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                                wrapper.passthrough(Type.COMPONENT);
                                wrapper.passthrough(Type.COMPONENT);
                                final Item icon = wrapper.read(Type.FLAT_ITEM);
                                wrapper.write(Type.FLAT_VAR_INT_ITEM, icon);
                                wrapper.passthrough((Type<Object>)Type.VAR_INT);
                                final int flags = wrapper.passthrough((Type<Integer>)Type.INT);
                                if ((flags & 0x1) != 0x0) {
                                    wrapper.passthrough(Type.STRING);
                                }
                                wrapper.passthrough((Type<Object>)Type.FLOAT);
                                wrapper.passthrough((Type<Object>)Type.FLOAT);
                            }
                            wrapper.passthrough(Type.STRING_ARRAY);
                            for (int arrayLength = wrapper.passthrough((Type<Integer>)Type.VAR_INT), array = 0; array < arrayLength; ++array) {
                                wrapper.passthrough(Type.STRING_ARRAY);
                            }
                        }
                    }
                });
            }
        });
    }
}
