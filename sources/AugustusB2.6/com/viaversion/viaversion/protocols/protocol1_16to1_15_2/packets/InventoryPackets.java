// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_16to1_15_2.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.LongTag;
import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import com.viaversion.viaversion.api.type.types.UUIDIntArrayType;
import java.util.UUID;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.ServerboundPackets1_16;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.data.RecipeRewriter1_14;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.type.types.ShortType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.storage.InventoryTracker1_16;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.Protocol1_16To1_15_2;
import com.viaversion.viaversion.rewriter.ItemRewriter;

public class InventoryPackets extends ItemRewriter<Protocol1_16To1_15_2>
{
    public InventoryPackets(final Protocol1_16To1_15_2 protocol) {
        super(protocol);
    }
    
    public void registerPackets() {
        final PacketWrapper clearPacket;
        final PacketHandler cursorRemapper = wrapper -> {
            clearPacket = wrapper.create(ClientboundPackets1_16.SET_SLOT);
            clearPacket.write(Type.UNSIGNED_BYTE, (Short)(-1));
            clearPacket.write(Type.SHORT, (Short)(-1));
            clearPacket.write(Type.FLAT_VAR_INT_ITEM, null);
            clearPacket.send(Protocol1_16To1_15_2.class);
            return;
        };
        ((AbstractProtocol<ClientboundPackets1_15, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_15.OPEN_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.COMPONENT);
                this.handler(cursorRemapper);
                final InventoryTracker1_16 inventoryTracker;
                final int windowId;
                int windowType;
                this.handler(wrapper -> {
                    inventoryTracker = wrapper.user().get(InventoryTracker1_16.class);
                    windowId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    windowType = wrapper.get((Type<Integer>)Type.VAR_INT, 1);
                    if (windowType >= 20) {
                        wrapper.set(Type.VAR_INT, 1, ++windowType);
                    }
                    inventoryTracker.setInventory((short)windowId);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_15, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_15.CLOSE_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(cursorRemapper);
                final InventoryTracker1_16 inventoryTracker;
                this.handler(wrapper -> {
                    inventoryTracker = wrapper.user().get(InventoryTracker1_16.class);
                    inventoryTracker.setInventory((short)(-1));
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_15, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_15.WINDOW_PROPERTY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                final short property;
                short enchantmentId;
                ShortType short1;
                short enchantmentId2;
                final int n;
                this.handler(wrapper -> {
                    property = wrapper.get((Type<Short>)Type.SHORT, 0);
                    if (property >= 4 && property <= 6) {
                        enchantmentId = wrapper.get((Type<Short>)Type.SHORT, 1);
                        if (enchantmentId >= 11) {
                            short1 = Type.SHORT;
                            enchantmentId2 = (short)(enchantmentId + 1);
                            wrapper.set(short1, n, enchantmentId2);
                        }
                    }
                });
            }
        });
        this.registerSetCooldown(ClientboundPackets1_15.COOLDOWN);
        this.registerWindowItems(ClientboundPackets1_15.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY);
        this.registerTradeList(ClientboundPackets1_15.TRADE_LIST, Type.FLAT_VAR_INT_ITEM);
        this.registerSetSlot(ClientboundPackets1_15.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
        this.registerAdvancements(ClientboundPackets1_15.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        ((AbstractProtocol<ClientboundPackets1_15, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_15.ENTITY_EQUIPMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                final int slot;
                this.handler(wrapper -> {
                    slot = wrapper.read((Type<Integer>)Type.VAR_INT);
                    wrapper.write(Type.BYTE, (byte)slot);
                    InventoryPackets.this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                });
            }
        });
        new RecipeRewriter1_14(this.protocol).registerDefaultHandler(ClientboundPackets1_15.DECLARE_RECIPES);
        this.registerClickWindow(ServerboundPackets1_16.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
        this.registerCreativeInvAction(ServerboundPackets1_16.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_16>)this.protocol).registerServerbound(ServerboundPackets1_16.CLOSE_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                final InventoryTracker1_16 inventoryTracker;
                this.handler(wrapper -> {
                    inventoryTracker = wrapper.user().get(InventoryTracker1_16.class);
                    inventoryTracker.setInventory((short)(-1));
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_16>)this.protocol).registerServerbound(ServerboundPackets1_16.EDIT_BOOK, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> InventoryPackets.this.handleItemToServer(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM)));
            }
        });
        this.registerSpawnParticle(ClientboundPackets1_15.SPAWN_PARTICLE, Type.FLAT_VAR_INT_ITEM, Type.DOUBLE);
    }
    
    @Override
    public Item handleItemToClient(final Item item) {
        if (item == null) {
            return null;
        }
        if (item.identifier() == 771 && item.tag() != null) {
            final CompoundTag tag = item.tag();
            final Tag ownerTag = tag.get("SkullOwner");
            if (ownerTag instanceof CompoundTag) {
                final CompoundTag ownerCompundTag = (CompoundTag)ownerTag;
                final Tag idTag = ownerCompundTag.get("Id");
                if (idTag instanceof StringTag) {
                    final UUID id = UUID.fromString((String)idTag.getValue());
                    ownerCompundTag.put("Id", new IntArrayTag(UUIDIntArrayType.uuidToIntArray(id)));
                }
            }
        }
        oldToNewAttributes(item);
        item.setIdentifier(Protocol1_16To1_15_2.MAPPINGS.getNewItemId(item.identifier()));
        return item;
    }
    
    @Override
    public Item handleItemToServer(final Item item) {
        if (item == null) {
            return null;
        }
        item.setIdentifier(Protocol1_16To1_15_2.MAPPINGS.getOldItemId(item.identifier()));
        if (item.identifier() == 771 && item.tag() != null) {
            final CompoundTag tag = item.tag();
            final Tag ownerTag = tag.get("SkullOwner");
            if (ownerTag instanceof CompoundTag) {
                final CompoundTag ownerCompundTag = (CompoundTag)ownerTag;
                final Tag idTag = ownerCompundTag.get("Id");
                if (idTag instanceof IntArrayTag) {
                    final UUID id = UUIDIntArrayType.uuidFromIntArray((int[])idTag.getValue());
                    ownerCompundTag.put("Id", new StringTag(id.toString()));
                }
            }
        }
        newToOldAttributes(item);
        return item;
    }
    
    public static void oldToNewAttributes(final Item item) {
        if (item.tag() == null) {
            return;
        }
        final ListTag attributes = item.tag().get("AttributeModifiers");
        if (attributes == null) {
            return;
        }
        for (final Tag tag : attributes) {
            final CompoundTag attribute = (CompoundTag)tag;
            rewriteAttributeName(attribute, "AttributeName", false);
            rewriteAttributeName(attribute, "Name", false);
            final Tag leastTag = attribute.get("UUIDLeast");
            if (leastTag != null) {
                final Tag mostTag = attribute.get("UUIDMost");
                final int[] uuidIntArray = UUIDIntArrayType.bitsToIntArray(((NumberTag)leastTag).asLong(), ((NumberTag)mostTag).asLong());
                attribute.put("UUID", new IntArrayTag(uuidIntArray));
            }
        }
    }
    
    public static void newToOldAttributes(final Item item) {
        if (item.tag() == null) {
            return;
        }
        final ListTag attributes = item.tag().get("AttributeModifiers");
        if (attributes == null) {
            return;
        }
        for (final Tag tag : attributes) {
            final CompoundTag attribute = (CompoundTag)tag;
            rewriteAttributeName(attribute, "AttributeName", true);
            rewriteAttributeName(attribute, "Name", true);
            final IntArrayTag uuidTag = attribute.get("UUID");
            if (uuidTag != null && uuidTag.getValue().length == 4) {
                final UUID uuid = UUIDIntArrayType.uuidFromIntArray(uuidTag.getValue());
                attribute.put("UUIDLeast", new LongTag(uuid.getLeastSignificantBits()));
                attribute.put("UUIDMost", new LongTag(uuid.getMostSignificantBits()));
            }
        }
    }
    
    public static void rewriteAttributeName(final CompoundTag compoundTag, final String entryName, final boolean inverse) {
        final StringTag attributeNameTag = compoundTag.get(entryName);
        if (attributeNameTag == null) {
            return;
        }
        String attributeName = attributeNameTag.getValue();
        if (inverse && !attributeName.startsWith("minecraft:")) {
            attributeName = "minecraft:" + attributeName;
        }
        final String mappedAttribute = (inverse ? Protocol1_16To1_15_2.MAPPINGS.getAttributeMappings().inverse() : Protocol1_16To1_15_2.MAPPINGS.getAttributeMappings()).get(attributeName);
        if (mappedAttribute == null) {
            return;
        }
        attributeNameTag.setValue(mappedAttribute);
    }
}
