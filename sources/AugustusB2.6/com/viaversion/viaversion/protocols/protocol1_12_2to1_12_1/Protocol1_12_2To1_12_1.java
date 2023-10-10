// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_12_2to1_12_1;

import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ServerboundPackets1_12_1;
import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;

public class Protocol1_12_2To1_12_1 extends AbstractProtocol<ClientboundPackets1_12_1, ClientboundPackets1_12_1, ServerboundPackets1_12_1, ServerboundPackets1_12_1>
{
    public Protocol1_12_2To1_12_1() {
        super(ClientboundPackets1_12_1.class, ClientboundPackets1_12_1.class, ServerboundPackets1_12_1.class, ServerboundPackets1_12_1.class);
    }
    
    @Override
    protected void registerPackets() {
        ((AbstractProtocol<ClientboundPackets1_12_1, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_12_1.KEEP_ALIVE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT, Type.LONG);
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_12_1>)this).registerServerbound(ServerboundPackets1_12_1.KEEP_ALIVE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.LONG, Type.VAR_INT);
            }
        });
    }
}
