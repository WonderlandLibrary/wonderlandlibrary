// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_12to1_12_1;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.ServerboundPackets1_12;
import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ServerboundPackets1_12_1;
import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.ClientboundPackets1_12;
import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public class Protocol1_12To1_12_1 extends BackwardsProtocol<ClientboundPackets1_12_1, ClientboundPackets1_12, ServerboundPackets1_12_1, ServerboundPackets1_12>
{
    public Protocol1_12To1_12_1() {
        super(ClientboundPackets1_12_1.class, ClientboundPackets1_12.class, ServerboundPackets1_12_1.class, ServerboundPackets1_12.class);
    }
    
    @Override
    protected void registerPackets() {
        ((AbstractProtocol<ClientboundPackets1_12_1, C2, S1, S2>)this).cancelClientbound(ClientboundPackets1_12_1.CRAFT_RECIPE_RESPONSE);
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_12>)this).cancelServerbound(ServerboundPackets1_12.PREPARE_CRAFTING_GRID);
    }
}
