// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_14to1_13_2.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.google.common.collect.Sets;
import java.util.Iterator;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ChatRewriter;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.DoubleTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import java.util.concurrent.ThreadLocalRandom;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import com.viaversion.viaversion.rewriter.RecipeRewriter;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.RecipeRewriter1_13_2;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.storage.EntityTracker1_14;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.rewriter.ComponentRewriter;
import java.util.Set;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.Protocol1_14To1_13_2;
import com.viaversion.viaversion.rewriter.ItemRewriter;

public class InventoryPackets extends ItemRewriter<Protocol1_14To1_13_2>
{
    private static final String NBT_TAG_NAME;
    private static final Set<String> REMOVED_RECIPE_TYPES;
    private static final ComponentRewriter COMPONENT_REWRITER;
    
    public InventoryPackets(final Protocol1_14To1_13_2 protocol) {
        super(protocol);
    }
    
    public void registerPackets() {
        this.registerSetCooldown(ClientboundPackets1_13.COOLDOWN);
        this.registerAdvancements(ClientboundPackets1_13.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        ((Protocol<ClientboundPackets1_13, ClientboundPackets1_14, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_13.OPEN_WINDOW, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Short windowId = wrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                        final String type = wrapper.read(Type.STRING);
                        final JsonElement title = wrapper.read(Type.COMPONENT);
                        InventoryPackets.COMPONENT_REWRITER.processText(title);
                        final Short slots = wrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                        if (type.equals("EntityHorse")) {
                            wrapper.setId(31);
                            final int entityId = wrapper.read((Type<Integer>)Type.INT);
                            wrapper.write(Type.UNSIGNED_BYTE, windowId);
                            wrapper.write(Type.VAR_INT, (int)slots);
                            wrapper.write(Type.INT, entityId);
                        }
                        else {
                            wrapper.setId(46);
                            wrapper.write(Type.VAR_INT, (int)windowId);
                            int typeId = -1;
                            final String s = type;
                            switch (s) {
                                case "minecraft:crafting_table": {
                                    typeId = 11;
                                    break;
                                }
                                case "minecraft:furnace": {
                                    typeId = 13;
                                    break;
                                }
                                case "minecraft:dropper":
                                case "minecraft:dispenser": {
                                    typeId = 6;
                                    break;
                                }
                                case "minecraft:enchanting_table": {
                                    typeId = 12;
                                    break;
                                }
                                case "minecraft:brewing_stand": {
                                    typeId = 10;
                                    break;
                                }
                                case "minecraft:villager": {
                                    typeId = 18;
                                    break;
                                }
                                case "minecraft:beacon": {
                                    typeId = 8;
                                    break;
                                }
                                case "minecraft:anvil": {
                                    typeId = 7;
                                    break;
                                }
                                case "minecraft:hopper": {
                                    typeId = 15;
                                    break;
                                }
                                case "minecraft:shulker_box": {
                                    typeId = 19;
                                    break;
                                }
                                default: {
                                    if (slots > 0 && slots <= 54) {
                                        typeId = slots / 9 - 1;
                                        break;
                                    }
                                    break;
                                }
                            }
                            if (typeId == -1) {
                                Via.getPlatform().getLogger().warning("Can't open inventory for 1.14 player! Type: " + type + " Size: " + slots);
                            }
                            wrapper.write(Type.VAR_INT, typeId);
                            wrapper.write(Type.COMPONENT, title);
                        }
                    }
                });
            }
        });
        this.registerWindowItems(ClientboundPackets1_13.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY);
        this.registerSetSlot(ClientboundPackets1_13.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_13.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final String channel = wrapper.get(Type.STRING, 0);
                        if (channel.equals("minecraft:trader_list") || channel.equals("trader_list")) {
                            wrapper.setId(39);
                            wrapper.resetReader();
                            wrapper.read(Type.STRING);
                            final int windowId = wrapper.read((Type<Integer>)Type.INT);
                            final EntityTracker1_14 tracker = wrapper.user().getEntityTracker(Protocol1_14To1_13_2.class);
                            tracker.setLatestTradeWindowId(windowId);
                            wrapper.write(Type.VAR_INT, windowId);
                            for (int size = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                                InventoryPackets.this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                                InventoryPackets.this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                                final boolean secondItem = wrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                                if (secondItem) {
                                    InventoryPackets.this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                                }
                                wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                                wrapper.passthrough((Type<Object>)Type.INT);
                                wrapper.passthrough((Type<Object>)Type.INT);
                                wrapper.write(Type.INT, 0);
                                wrapper.write(Type.INT, 0);
                                wrapper.write(Type.FLOAT, 0.0f);
                            }
                            wrapper.write(Type.VAR_INT, 0);
                            wrapper.write(Type.VAR_INT, 0);
                            wrapper.write(Type.BOOLEAN, false);
                        }
                        else if (channel.equals("minecraft:book_open") || channel.equals("book_open")) {
                            final int hand = wrapper.read((Type<Integer>)Type.VAR_INT);
                            wrapper.clearPacket();
                            wrapper.setId(45);
                            wrapper.write(Type.VAR_INT, hand);
                        }
                    }
                });
            }
        });
        this.registerEntityEquipment(ClientboundPackets1_13.ENTITY_EQUIPMENT, Type.FLAT_VAR_INT_ITEM);
        final RecipeRewriter recipeRewriter = new RecipeRewriter1_13_2(this.protocol);
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_13.DECLARE_RECIPES, new PacketRemapper() {
            @Override
            public void registerMap() {
                final RecipeRewriter val$recipeRewriter;
                final int size;
                int deleted;
                int i;
                String id;
                String type;
                this.handler(wrapper -> {
                    val$recipeRewriter = recipeRewriter;
                    size = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    deleted = 0;
                    for (i = 0; i < size; ++i) {
                        id = wrapper.read(Type.STRING);
                        type = wrapper.read(Type.STRING);
                        if (InventoryPackets.REMOVED_RECIPE_TYPES.contains(type)) {
                            ++deleted;
                        }
                        else {
                            wrapper.write(Type.STRING, type);
                            wrapper.write(Type.STRING, id);
                            val$recipeRewriter.handle(wrapper, type);
                        }
                    }
                    wrapper.set(Type.VAR_INT, 0, size - deleted);
                });
            }
        });
        this.registerClickWindow(ServerboundPackets1_14.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_14>)this.protocol).registerServerbound(ServerboundPackets1_14.SELECT_TRADE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final PacketWrapper resyncPacket = wrapper.create(8);
                        final EntityTracker1_14 tracker = wrapper.user().getEntityTracker(Protocol1_14To1_13_2.class);
                        resyncPacket.write(Type.UNSIGNED_BYTE, (short)tracker.getLatestTradeWindowId());
                        resyncPacket.write(Type.SHORT, (Short)(-999));
                        resyncPacket.write(Type.BYTE, (Byte)2);
                        resyncPacket.write(Type.SHORT, (short)ThreadLocalRandom.current().nextInt());
                        resyncPacket.write(Type.VAR_INT, 5);
                        final CompoundTag tag = new CompoundTag();
                        tag.put("force_resync", new DoubleTag(Double.NaN));
                        resyncPacket.write((Type<DataItem>)Type.FLAT_VAR_INT_ITEM, new DataItem(1, (byte)1, (short)0, tag));
                        resyncPacket.scheduleSendToServer(Protocol1_14To1_13_2.class);
                    }
                });
            }
        });
        this.registerCreativeInvAction(ServerboundPackets1_14.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
        this.registerSpawnParticle(ClientboundPackets1_13.SPAWN_PARTICLE, Type.FLAT_VAR_INT_ITEM, Type.FLOAT);
    }
    
    @Override
    public Item handleItemToClient(final Item item) {
        if (item == null) {
            return null;
        }
        item.setIdentifier(Protocol1_14To1_13_2.MAPPINGS.getNewItemId(item.identifier()));
        if (item.tag() == null) {
            return item;
        }
        final Tag displayTag = item.tag().get("display");
        if (displayTag instanceof CompoundTag) {
            final CompoundTag display = (CompoundTag)displayTag;
            final Tag loreTag = display.get("Lore");
            if (loreTag instanceof ListTag) {
                final ListTag lore = (ListTag)loreTag;
                display.put(InventoryPackets.NBT_TAG_NAME + "|Lore", new ListTag(lore.clone().getValue()));
                for (final Tag loreEntry : lore) {
                    if (loreEntry instanceof StringTag) {
                        final String jsonText = ChatRewriter.legacyTextToJsonString(((StringTag)loreEntry).getValue(), true);
                        ((StringTag)loreEntry).setValue(jsonText);
                    }
                }
            }
        }
        return item;
    }
    
    @Override
    public Item handleItemToServer(final Item item) {
        if (item == null) {
            return null;
        }
        item.setIdentifier(Protocol1_14To1_13_2.MAPPINGS.getOldItemId(item.identifier()));
        if (item.tag() == null) {
            return item;
        }
        final Tag displayTag = item.tag().get("display");
        if (displayTag instanceof CompoundTag) {
            final CompoundTag display = (CompoundTag)displayTag;
            final Tag loreTag = display.get("Lore");
            if (loreTag instanceof ListTag) {
                final ListTag lore = (ListTag)loreTag;
                final ListTag savedLore = display.remove(InventoryPackets.NBT_TAG_NAME + "|Lore");
                if (savedLore != null) {
                    display.put("Lore", new ListTag(savedLore.getValue()));
                }
                else {
                    for (final Tag loreEntry : lore) {
                        if (loreEntry instanceof StringTag) {
                            ((StringTag)loreEntry).setValue(ChatRewriter.jsonToLegacyText(((StringTag)loreEntry).getValue()));
                        }
                    }
                }
            }
        }
        return item;
    }
    
    static {
        NBT_TAG_NAME = "ViaVersion|" + Protocol1_14To1_13_2.class.getSimpleName();
        REMOVED_RECIPE_TYPES = Sets.newHashSet("crafting_special_banneraddpattern", "crafting_special_repairitem");
        COMPONENT_REWRITER = new ComponentRewriter() {
            @Override
            protected void handleTranslate(final JsonObject object, final String translate) {
                super.handleTranslate(object, translate);
                if (translate.startsWith("block.") && translate.endsWith(".name")) {
                    object.addProperty("translate", translate.substring(0, translate.length() - 5));
                }
            }
        };
    }
}
