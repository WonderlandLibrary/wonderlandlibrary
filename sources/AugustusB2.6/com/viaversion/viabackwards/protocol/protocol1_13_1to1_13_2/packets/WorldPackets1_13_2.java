// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_13_1to1_13_2.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viabackwards.protocol.protocol1_13_1to1_13_2.Protocol1_13_1To1_13_2;

public class WorldPackets1_13_2
{
    public static void register(final Protocol1_13_1To1_13_2 protocol) {
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_13.SPAWN_PARTICLE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int id = wrapper.get((Type<Integer>)Type.INT, 0);
                        if (id == 27) {
                            wrapper.write(Type.FLAT_ITEM, (Item)wrapper.read((Type<T>)Type.FLAT_VAR_INT_ITEM));
                        }
                    }
                });
            }
        });
    }
}
