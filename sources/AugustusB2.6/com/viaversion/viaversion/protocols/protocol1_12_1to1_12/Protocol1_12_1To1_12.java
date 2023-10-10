// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_12_1to1_12;

import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.ServerboundPackets1_12;
import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.ClientboundPackets1_12;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;

public class Protocol1_12_1To1_12 extends AbstractProtocol<ClientboundPackets1_12, ClientboundPackets1_12_1, ServerboundPackets1_12, ServerboundPackets1_12_1>
{
    public Protocol1_12_1To1_12() {
        super(ClientboundPackets1_12.class, ClientboundPackets1_12_1.class, ServerboundPackets1_12.class, ServerboundPackets1_12_1.class);
    }
    
    @Override
    protected void registerPackets() {
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_12_1>)this).cancelServerbound(ServerboundPackets1_12_1.CRAFT_RECIPE_REQUEST);
    }
}
