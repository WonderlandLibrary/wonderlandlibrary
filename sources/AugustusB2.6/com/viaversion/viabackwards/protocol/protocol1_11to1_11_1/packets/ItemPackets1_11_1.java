// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_11to1_11_1.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import com.viaversion.viabackwards.api.rewriters.LegacyEnchantmentRewriter;
import com.viaversion.viabackwards.protocol.protocol1_11to1_11_1.Protocol1_11To1_11_1;
import com.viaversion.viabackwards.api.rewriters.LegacyBlockItemRewriter;

public class ItemPackets1_11_1 extends LegacyBlockItemRewriter<Protocol1_11To1_11_1>
{
    private LegacyEnchantmentRewriter enchantmentRewriter;
    
    public ItemPackets1_11_1(final Protocol1_11To1_11_1 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
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
                                wrapper.write(Type.ITEM, ItemPackets1_11_1.this.handleItemToClient(wrapper.read(Type.ITEM)));
                                wrapper.write(Type.ITEM, ItemPackets1_11_1.this.handleItemToClient(wrapper.read(Type.ITEM)));
                                final boolean secondItem = wrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                                if (secondItem) {
                                    wrapper.write(Type.ITEM, ItemPackets1_11_1.this.handleItemToClient(wrapper.read(Type.ITEM)));
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
        ((Protocol1_11To1_11_1)this.protocol).getEntityRewriter().filter().handler((event, meta) -> {
            if (meta.metaType().type().equals(Type.ITEM)) {
                meta.setValue(this.handleItemToClient((Item)meta.getValue()));
            }
        });
    }
    
    @Override
    protected void registerRewrites() {
        (this.enchantmentRewriter = new LegacyEnchantmentRewriter(this.nbtTagName)).registerEnchantment(22, "§7Sweeping Edge");
    }
    
    @Override
    public Item handleItemToClient(final Item item) {
        if (item == null) {
            return null;
        }
        super.handleItemToClient(item);
        final CompoundTag tag = item.tag();
        if (tag == null) {
            return item;
        }
        if (tag.get("ench") instanceof ListTag) {
            this.enchantmentRewriter.rewriteEnchantmentsToClient(tag, false);
        }
        if (tag.get("StoredEnchantments") instanceof ListTag) {
            this.enchantmentRewriter.rewriteEnchantmentsToClient(tag, true);
        }
        return item;
    }
    
    @Override
    public Item handleItemToServer(final Item item) {
        if (item == null) {
            return null;
        }
        super.handleItemToServer(item);
        final CompoundTag tag = item.tag();
        if (tag == null) {
            return item;
        }
        if (tag.contains(this.nbtTagName + "|ench")) {
            this.enchantmentRewriter.rewriteEnchantmentsToServer(tag, false);
        }
        if (tag.contains(this.nbtTagName + "|StoredEnchantments")) {
            this.enchantmentRewriter.rewriteEnchantmentsToServer(tag, true);
        }
        return item;
    }
}
