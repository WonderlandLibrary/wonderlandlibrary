// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_11to1_10.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
import com.viaversion.viaversion.protocols.protocol1_11to1_10.EntityIdRewriter;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import com.viaversion.viaversion.protocols.protocol1_11to1_10.Protocol1_11To1_10;
import com.viaversion.viaversion.rewriter.ItemRewriter;

public class InventoryPackets extends ItemRewriter<Protocol1_11To1_10>
{
    public InventoryPackets(final Protocol1_11To1_10 protocol) {
        super(protocol);
    }
    
    public void registerPackets() {
        this.registerSetSlot(ClientboundPackets1_9_3.SET_SLOT, Type.ITEM);
        this.registerWindowItems(ClientboundPackets1_9_3.WINDOW_ITEMS, Type.ITEM_ARRAY);
        this.registerEntityEquipment(ClientboundPackets1_9_3.ENTITY_EQUIPMENT, Type.ITEM);
        ((AbstractProtocol<ClientboundPackets1_9_3, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_9_3.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if (wrapper.get(Type.STRING, 0).equalsIgnoreCase("MC|TrList")) {
                            wrapper.passthrough((Type<Object>)Type.INT);
                            for (int size = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                                EntityIdRewriter.toClientItem(wrapper.passthrough(Type.ITEM));
                                EntityIdRewriter.toClientItem(wrapper.passthrough(Type.ITEM));
                                final boolean secondItem = wrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                                if (secondItem) {
                                    EntityIdRewriter.toClientItem(wrapper.passthrough(Type.ITEM));
                                }
                                wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                                wrapper.passthrough((Type<Object>)Type.INT);
                                wrapper.passthrough((Type<Object>)Type.INT);
                            }
                        }
                    }
                });
            }
        });
        this.registerClickWindow(ServerboundPackets1_9_3.CLICK_WINDOW, Type.ITEM);
        this.registerCreativeInvAction(ServerboundPackets1_9_3.CREATIVE_INVENTORY_ACTION, Type.ITEM);
    }
    
    @Override
    public Item handleItemToClient(final Item item) {
        EntityIdRewriter.toClientItem(item);
        return item;
    }
    
    @Override
    public Item handleItemToServer(final Item item) {
        EntityIdRewriter.toServerItem(item);
        if (item == null) {
            return null;
        }
        boolean newItem = item.identifier() >= 218 && item.identifier() <= 234;
        newItem |= (item.identifier() == 449 || item.identifier() == 450);
        if (newItem) {
            item.setIdentifier(1);
            item.setData((short)0);
        }
        return item;
    }
}
