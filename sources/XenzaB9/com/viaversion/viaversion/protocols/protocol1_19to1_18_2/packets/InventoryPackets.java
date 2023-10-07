// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_19to1_18_2.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.provider.AckSequenceProvider;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.data.RecipeRewriter1_16;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.ServerboundPackets1_19;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.ClientboundPackets1_18;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.Protocol1_19To1_18_2;
import com.viaversion.viaversion.rewriter.ItemRewriter;

public final class InventoryPackets extends ItemRewriter<Protocol1_19To1_18_2>
{
    public InventoryPackets(final Protocol1_19To1_18_2 protocol) {
        super(protocol);
    }
    
    public void registerPackets() {
        this.registerSetCooldown(ClientboundPackets1_18.COOLDOWN);
        this.registerWindowItems1_17_1(ClientboundPackets1_18.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT, Type.FLAT_VAR_INT_ITEM);
        this.registerSetSlot1_17_1(ClientboundPackets1_18.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
        this.registerAdvancements(ClientboundPackets1_18.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        this.registerEntityEquipmentArray(ClientboundPackets1_18.ENTITY_EQUIPMENT, Type.FLAT_VAR_INT_ITEM);
        ((AbstractProtocol<ClientboundPackets1_18, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_18.SPAWN_PARTICLE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT, Type.VAR_INT);
                this.map(Type.BOOLEAN);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.INT);
                this.handler(InventoryPackets.this.getSpawnParticleHandler(Type.VAR_INT, Type.FLAT_VAR_INT_ITEM));
            }
        });
        this.registerClickWindow1_17_1(ServerboundPackets1_19.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
        this.registerCreativeInvAction(ServerboundPackets1_19.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
        this.registerWindowPropertyEnchantmentHandler(ClientboundPackets1_18.WINDOW_PROPERTY);
        ((AbstractProtocol<ClientboundPackets1_18, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_18.TRADE_LIST, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(wrapper -> {
                    final int size = wrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                    wrapper.write(Type.VAR_INT, size);
                    for (int i = 0; i < size; ++i) {
                        InventoryPackets.this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                        InventoryPackets.this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                        if (wrapper.read((Type<Boolean>)Type.BOOLEAN)) {
                            InventoryPackets.this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                        }
                        else {
                            wrapper.write(Type.FLAT_VAR_INT_ITEM, null);
                        }
                        wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                        wrapper.passthrough((Type<Object>)Type.INT);
                        wrapper.passthrough((Type<Object>)Type.INT);
                        wrapper.passthrough((Type<Object>)Type.INT);
                        wrapper.passthrough((Type<Object>)Type.INT);
                        wrapper.passthrough((Type<Object>)Type.FLOAT);
                        wrapper.passthrough((Type<Object>)Type.INT);
                    }
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_19>)this.protocol).registerServerbound(ServerboundPackets1_19.PLAYER_DIGGING, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.POSITION1_14);
                this.map(Type.UNSIGNED_BYTE);
                this.handler(InventoryPackets.this.sequenceHandler());
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_19>)this.protocol).registerServerbound(ServerboundPackets1_19.PLAYER_BLOCK_PLACEMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.POSITION1_14);
                this.map(Type.VAR_INT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.BOOLEAN);
                this.handler(InventoryPackets.this.sequenceHandler());
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_19>)this.protocol).registerServerbound(ServerboundPackets1_19.USE_ITEM, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(InventoryPackets.this.sequenceHandler());
            }
        });
        new RecipeRewriter1_16(this.protocol).registerDefaultHandler(ClientboundPackets1_18.DECLARE_RECIPES);
    }
    
    private PacketHandler sequenceHandler() {
        return wrapper -> {
            final int sequence = wrapper.read((Type<Integer>)Type.VAR_INT);
            final AckSequenceProvider provider = Via.getManager().getProviders().get(AckSequenceProvider.class);
            provider.handleSequence(wrapper.user(), sequence);
        };
    }
}
