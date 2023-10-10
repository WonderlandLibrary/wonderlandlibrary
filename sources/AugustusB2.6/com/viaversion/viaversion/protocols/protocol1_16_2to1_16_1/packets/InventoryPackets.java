// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.ServerboundPackets1_16_2;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.data.RecipeRewriter1_16;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.Protocol1_16_2To1_16_1;
import com.viaversion.viaversion.rewriter.ItemRewriter;

public class InventoryPackets extends ItemRewriter<Protocol1_16_2To1_16_1>
{
    public InventoryPackets(final Protocol1_16_2To1_16_1 protocol) {
        super(protocol);
    }
    
    public void registerPackets() {
        this.registerSetCooldown(ClientboundPackets1_16.COOLDOWN);
        this.registerWindowItems(ClientboundPackets1_16.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY);
        this.registerTradeList(ClientboundPackets1_16.TRADE_LIST, Type.FLAT_VAR_INT_ITEM);
        this.registerSetSlot(ClientboundPackets1_16.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
        this.registerEntityEquipmentArray(ClientboundPackets1_16.ENTITY_EQUIPMENT, Type.FLAT_VAR_INT_ITEM);
        this.registerAdvancements(ClientboundPackets1_16.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        ((AbstractProtocol<ClientboundPackets1_16, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_16.UNLOCK_RECIPES, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                    wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                    wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                    wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                    wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                    wrapper.write(Type.BOOLEAN, false);
                    wrapper.write(Type.BOOLEAN, false);
                    wrapper.write(Type.BOOLEAN, false);
                    wrapper.write(Type.BOOLEAN, false);
                });
            }
        });
        new RecipeRewriter1_16(this.protocol).registerDefaultHandler(ClientboundPackets1_16.DECLARE_RECIPES);
        this.registerClickWindow(ServerboundPackets1_16_2.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
        this.registerCreativeInvAction(ServerboundPackets1_16_2.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_16_2>)this.protocol).registerServerbound(ServerboundPackets1_16_2.EDIT_BOOK, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> InventoryPackets.this.handleItemToServer(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM)));
            }
        });
        this.registerSpawnParticle(ClientboundPackets1_16.SPAWN_PARTICLE, Type.FLAT_VAR_INT_ITEM, Type.DOUBLE);
    }
}
