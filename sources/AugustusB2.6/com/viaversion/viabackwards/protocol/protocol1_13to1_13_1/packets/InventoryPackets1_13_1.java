// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_13to1_13_1.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viabackwards.protocol.protocol1_13to1_13_1.Protocol1_13To1_13_1;
import com.viaversion.viaversion.rewriter.ItemRewriter;

public class InventoryPackets1_13_1 extends ItemRewriter<Protocol1_13To1_13_1>
{
    public InventoryPackets1_13_1(final Protocol1_13To1_13_1 protocol) {
        super(protocol);
    }
    
    public void registerPackets() {
        this.registerSetCooldown(ClientboundPackets1_13.COOLDOWN);
        this.registerWindowItems(ClientboundPackets1_13.WINDOW_ITEMS, Type.FLAT_ITEM_ARRAY);
        this.registerSetSlot(ClientboundPackets1_13.SET_SLOT, Type.FLAT_ITEM);
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_13.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final String channel = wrapper.passthrough(Type.STRING);
                        if (channel.equals("minecraft:trader_list")) {
                            wrapper.passthrough((Type<Object>)Type.INT);
                            for (int size = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                                final Item input = wrapper.passthrough(Type.FLAT_ITEM);
                                InventoryPackets1_13_1.this.handleItemToClient(input);
                                final Item output = wrapper.passthrough(Type.FLAT_ITEM);
                                InventoryPackets1_13_1.this.handleItemToClient(output);
                                final boolean secondItem = wrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                                if (secondItem) {
                                    final Item second = wrapper.passthrough(Type.FLAT_ITEM);
                                    InventoryPackets1_13_1.this.handleItemToClient(second);
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
        this.registerEntityEquipment(ClientboundPackets1_13.ENTITY_EQUIPMENT, Type.FLAT_ITEM);
        this.registerClickWindow(ServerboundPackets1_13.CLICK_WINDOW, Type.FLAT_ITEM);
        this.registerCreativeInvAction(ServerboundPackets1_13.CREATIVE_INVENTORY_ACTION, Type.FLAT_ITEM);
        this.registerSpawnParticle(ClientboundPackets1_13.SPAWN_PARTICLE, Type.FLAT_ITEM, Type.FLOAT);
    }
}
