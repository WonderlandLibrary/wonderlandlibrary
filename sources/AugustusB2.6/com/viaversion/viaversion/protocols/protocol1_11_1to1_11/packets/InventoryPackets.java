// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_11_1to1_11.packets;

import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
import com.viaversion.viaversion.protocols.protocol1_11_1to1_11.Protocol1_11_1To1_11;
import com.viaversion.viaversion.rewriter.ItemRewriter;

public class InventoryPackets extends ItemRewriter<Protocol1_11_1To1_11>
{
    public InventoryPackets(final Protocol1_11_1To1_11 protocol) {
        super(protocol);
    }
    
    public void registerPackets() {
        this.registerCreativeInvAction(ServerboundPackets1_9_3.CREATIVE_INVENTORY_ACTION, Type.ITEM);
    }
    
    @Override
    public Item handleItemToServer(final Item item) {
        if (item == null) {
            return null;
        }
        final boolean newItem = item.identifier() == 452;
        if (newItem) {
            item.setIdentifier(1);
            item.setData((short)0);
        }
        return null;
    }
}
