// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSectionLight;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.Protocol1_14To1_13_2;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.Environment;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSectionLightImpl;
import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.storage.ChunkLightStorage;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.types.Chunk1_13Type;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.types.Chunk1_14Type;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import com.viaversion.viaversion.rewriter.RecipeRewriter;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.RecipeRewriter1_13_2;
import java.util.List;
import com.viaversion.viaversion.api.type.types.version.Types1_13;
import com.viaversion.viaversion.api.type.types.version.Types1_13_2;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.ArrayList;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_14Types;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.BlockRewriter;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ChatRewriter;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import com.viaversion.viabackwards.api.rewriters.TranslatableRewriter;
import com.viaversion.viabackwards.api.rewriters.EnchantmentRewriter;
import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.Protocol1_13_2To1_14;
import com.viaversion.viabackwards.api.rewriters.ItemRewriter;

public class BlockItemPackets1_14 extends ItemRewriter<Protocol1_13_2To1_14>
{
    private EnchantmentRewriter enchantmentRewriter;
    
    public BlockItemPackets1_14(final Protocol1_13_2To1_14 protocol, final TranslatableRewriter translatableRewriter) {
        super(protocol, translatableRewriter);
    }
    
    @Override
    protected void registerPackets() {
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_13>)this.protocol).registerServerbound(ServerboundPackets1_13.EDIT_BOOK, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> BlockItemPackets1_14.this.handleItemToServer(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM)));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.OPEN_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int windowId = wrapper.read((Type<Integer>)Type.VAR_INT);
                        wrapper.write(Type.UNSIGNED_BYTE, (short)windowId);
                        final int type = wrapper.read((Type<Integer>)Type.VAR_INT);
                        String stringType = null;
                        String containerTitle = null;
                        int slotSize = 0;
                        if (type < 6) {
                            if (type == 2) {
                                containerTitle = "Barrel";
                            }
                            stringType = "minecraft:container";
                            slotSize = (type + 1) * 9;
                        }
                        else {
                            switch (type) {
                                case 11: {
                                    stringType = "minecraft:crafting_table";
                                    break;
                                }
                                case 9:
                                case 13:
                                case 14:
                                case 20: {
                                    if (type == 9) {
                                        containerTitle = "Blast Furnace";
                                    }
                                    else if (type == 20) {
                                        containerTitle = "Smoker";
                                    }
                                    else if (type == 14) {
                                        containerTitle = "Grindstone";
                                    }
                                    stringType = "minecraft:furnace";
                                    slotSize = 3;
                                    break;
                                }
                                case 6: {
                                    stringType = "minecraft:dropper";
                                    slotSize = 9;
                                    break;
                                }
                                case 12: {
                                    stringType = "minecraft:enchanting_table";
                                    break;
                                }
                                case 10: {
                                    stringType = "minecraft:brewing_stand";
                                    slotSize = 5;
                                    break;
                                }
                                case 18: {
                                    stringType = "minecraft:villager";
                                    break;
                                }
                                case 8: {
                                    stringType = "minecraft:beacon";
                                    slotSize = 1;
                                    break;
                                }
                                case 7:
                                case 21: {
                                    if (type == 21) {
                                        containerTitle = "Cartography Table";
                                    }
                                    stringType = "minecraft:anvil";
                                    break;
                                }
                                case 15: {
                                    stringType = "minecraft:hopper";
                                    slotSize = 5;
                                    break;
                                }
                                case 19: {
                                    stringType = "minecraft:shulker_box";
                                    slotSize = 27;
                                    break;
                                }
                            }
                        }
                        if (stringType == null) {
                            ViaBackwards.getPlatform().getLogger().warning("Can't open inventory for 1.13 player! Type: " + type);
                            wrapper.cancel();
                            return;
                        }
                        wrapper.write(Type.STRING, stringType);
                        JsonElement title = wrapper.read(Type.COMPONENT);
                        final JsonObject object;
                        if (containerTitle != null && title.isJsonObject() && (object = title.getAsJsonObject()).has("translate") && (type != 2 || object.getAsJsonPrimitive("translate").getAsString().equals("container.barrel"))) {
                            title = ChatRewriter.legacyTextToJson(containerTitle);
                        }
                        wrapper.write(Type.COMPONENT, title);
                        wrapper.write(Type.UNSIGNED_BYTE, (short)slotSize);
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_14, ClientboundPackets1_13, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.OPEN_HORSE_WINDOW, ClientboundPackets1_13.OPEN_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        wrapper.passthrough((Type<Object>)Type.UNSIGNED_BYTE);
                        wrapper.write(Type.STRING, "EntityHorse");
                        final JsonObject object = new JsonObject();
                        object.addProperty("translate", "minecraft.horse");
                        wrapper.write(Type.COMPONENT, object);
                        wrapper.write(Type.UNSIGNED_BYTE, wrapper.read((Type<Integer>)Type.VAR_INT).shortValue());
                        wrapper.passthrough((Type<Object>)Type.INT);
                    }
                });
            }
        });
        final BlockRewriter blockRewriter = new BlockRewriter(this.protocol, Type.POSITION);
        this.registerSetCooldown(ClientboundPackets1_14.COOLDOWN);
        this.registerWindowItems(ClientboundPackets1_14.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY);
        this.registerSetSlot(ClientboundPackets1_14.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
        this.registerAdvancements(ClientboundPackets1_14.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        ((Protocol<ClientboundPackets1_14, ClientboundPackets1_13, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.TRADE_LIST, ClientboundPackets1_13.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        wrapper.write(Type.STRING, "minecraft:trader_list");
                        final int windowId = wrapper.read((Type<Integer>)Type.VAR_INT);
                        wrapper.write(Type.INT, windowId);
                        for (int size = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                            Item input = wrapper.read(Type.FLAT_VAR_INT_ITEM);
                            input = BlockItemPackets1_14.this.handleItemToClient(input);
                            wrapper.write(Type.FLAT_VAR_INT_ITEM, input);
                            Item output = wrapper.read(Type.FLAT_VAR_INT_ITEM);
                            output = BlockItemPackets1_14.this.handleItemToClient(output);
                            wrapper.write(Type.FLAT_VAR_INT_ITEM, output);
                            final boolean secondItem = wrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                            if (secondItem) {
                                Item second = wrapper.read(Type.FLAT_VAR_INT_ITEM);
                                second = BlockItemPackets1_14.this.handleItemToClient(second);
                                wrapper.write(Type.FLAT_VAR_INT_ITEM, second);
                            }
                            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                            wrapper.passthrough((Type<Object>)Type.INT);
                            wrapper.passthrough((Type<Object>)Type.INT);
                            wrapper.read((Type<Object>)Type.INT);
                            wrapper.read((Type<Object>)Type.INT);
                            wrapper.read((Type<Object>)Type.FLOAT);
                        }
                        wrapper.read((Type<Object>)Type.VAR_INT);
                        wrapper.read((Type<Object>)Type.VAR_INT);
                        wrapper.read((Type<Object>)Type.BOOLEAN);
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_14, ClientboundPackets1_13, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.OPEN_BOOK, ClientboundPackets1_13.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        wrapper.write(Type.STRING, "minecraft:book_open");
                        wrapper.passthrough((Type<Object>)Type.VAR_INT);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.ENTITY_EQUIPMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.FLAT_VAR_INT_ITEM);
                this.handler(BlockItemPackets1_14.this.itemToClientHandler(Type.FLAT_VAR_INT_ITEM));
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        final EntityType entityType = wrapper.user().getEntityTracker(Protocol1_13_2To1_14.class).entityType(entityId);
                        if (entityType == null) {
                            return;
                        }
                        if (entityType.isOrHasParent(Entity1_14Types.ABSTRACT_HORSE)) {
                            wrapper.setId(63);
                            wrapper.resetReader();
                            wrapper.passthrough((Type<Object>)Type.VAR_INT);
                            wrapper.read((Type<Object>)Type.VAR_INT);
                            final Item item = wrapper.read(Type.FLAT_VAR_INT_ITEM);
                            final int armorType = (item == null || item.identifier() == 0) ? 0 : (item.identifier() - 726);
                            if (armorType < 0 || armorType > 3) {
                                ViaBackwards.getPlatform().getLogger().warning("Received invalid horse armor: " + item);
                                wrapper.cancel();
                                return;
                            }
                            final List<Metadata> metadataList = new ArrayList<Metadata>();
                            metadataList.add(new Metadata(16, Types1_13_2.META_TYPES.varIntType, armorType));
                            wrapper.write(Types1_13.METADATA_LIST, metadataList);
                        }
                    }
                });
            }
        });
        final RecipeRewriter recipeHandler = new RecipeRewriter1_13_2(this.protocol);
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.DECLARE_RECIPES, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    private final Set<String> removedTypes = ImmutableSet.of("crafting_special_suspiciousstew", "blasting", "smoking", "campfire_cooking", "stonecutting");
                    
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int size = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                        int deleted = 0;
                        for (int i = 0; i < size; ++i) {
                            String type = wrapper.read(Type.STRING);
                            final String id = wrapper.read(Type.STRING);
                            type = type.replace("minecraft:", "");
                            if (this.removedTypes.contains(type)) {
                                final String s = type;
                                switch (s) {
                                    case "blasting":
                                    case "smoking":
                                    case "campfire_cooking": {
                                        wrapper.read(Type.STRING);
                                        wrapper.read(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
                                        wrapper.read(Type.FLAT_VAR_INT_ITEM);
                                        wrapper.read((Type<Object>)Type.FLOAT);
                                        wrapper.read((Type<Object>)Type.VAR_INT);
                                        break;
                                    }
                                    case "stonecutting": {
                                        wrapper.read(Type.STRING);
                                        wrapper.read(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
                                        wrapper.read(Type.FLAT_VAR_INT_ITEM);
                                        break;
                                    }
                                }
                                ++deleted;
                            }
                            else {
                                wrapper.write(Type.STRING, id);
                                wrapper.write(Type.STRING, type);
                                recipeHandler.handle(wrapper, type);
                            }
                        }
                        wrapper.set(Type.VAR_INT, 0, size - deleted);
                    }
                });
            }
        });
        this.registerClickWindow(ServerboundPackets1_13.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
        this.registerCreativeInvAction(ServerboundPackets1_13.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.BLOCK_BREAK_ANIMATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.POSITION1_14, Type.POSITION);
                this.map(Type.BYTE);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.BLOCK_ENTITY_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION1_14, Type.POSITION);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.BLOCK_ACTION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION1_14, Type.POSITION);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.VAR_INT);
                final int mappedId;
                this.handler(wrapper -> {
                    mappedId = ((Protocol1_13_2To1_14)BlockItemPackets1_14.this.protocol).getMappingData().getNewBlockId(wrapper.get((Type<Integer>)Type.VAR_INT, 0));
                    if (mappedId == -1) {
                        wrapper.cancel();
                    }
                    else {
                        wrapper.set(Type.VAR_INT, 0, mappedId);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.BLOCK_CHANGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION1_14, Type.POSITION);
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int id = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        wrapper.set(Type.VAR_INT, 0, ((Protocol1_13_2To1_14)BlockItemPackets1_14.this.protocol).getMappingData().getNewBlockStateId(id));
                    }
                });
            }
        });
        blockRewriter.registerMultiBlockChange(ClientboundPackets1_14.MULTI_BLOCK_CHANGE);
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.EXPLOSION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        for (int i = 0; i < 3; ++i) {
                            float coord = wrapper.get((Type<Float>)Type.FLOAT, i);
                            if (coord < 0.0f) {
                                coord = (float)Math.floor(coord);
                                wrapper.set(Type.FLOAT, i, coord);
                            }
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.CHUNK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
                        final Chunk chunk = wrapper.read((Type<Chunk>)new Chunk1_14Type());
                        wrapper.write((Type<Chunk>)new Chunk1_13Type(clientWorld), chunk);
                        final ChunkLightStorage.ChunkLight chunkLight = wrapper.user().get(ChunkLightStorage.class).getStoredLight(chunk.getX(), chunk.getZ());
                        for (int i = 0; i < chunk.getSections().length; ++i) {
                            final ChunkSection section = chunk.getSections()[i];
                            if (section != null) {
                                final ChunkSectionLight sectionLight = new ChunkSectionLightImpl();
                                section.setLight(sectionLight);
                                if (chunkLight == null) {
                                    sectionLight.setBlockLight(ChunkLightStorage.FULL_LIGHT);
                                    if (clientWorld.getEnvironment() == Environment.NORMAL) {
                                        sectionLight.setSkyLight(ChunkLightStorage.FULL_LIGHT);
                                    }
                                }
                                else {
                                    final byte[] blockLight = chunkLight.getBlockLight()[i];
                                    sectionLight.setBlockLight((blockLight != null) ? blockLight : ChunkLightStorage.FULL_LIGHT);
                                    if (clientWorld.getEnvironment() == Environment.NORMAL) {
                                        final byte[] skyLight = chunkLight.getSkyLight()[i];
                                        sectionLight.setSkyLight((skyLight != null) ? skyLight : ChunkLightStorage.FULL_LIGHT);
                                    }
                                }
                                if (Via.getConfig().isNonFullBlockLightFix() && section.getNonAirBlocksCount() != 0 && sectionLight.hasBlockLight()) {
                                    for (int x = 0; x < 16; ++x) {
                                        for (int y = 0; y < 16; ++y) {
                                            for (int z = 0; z < 16; ++z) {
                                                final int id = section.getFlatBlock(x, y, z);
                                                if (Protocol1_14To1_13_2.MAPPINGS.getNonFullBlocks().contains(id)) {
                                                    sectionLight.getBlockLightNibbleArray().set(x, y, z, 0);
                                                }
                                            }
                                        }
                                    }
                                }
                                for (int j = 0; j < section.getPaletteSize(); ++j) {
                                    final int old = section.getPaletteEntry(j);
                                    final int newId = ((Protocol1_13_2To1_14)BlockItemPackets1_14.this.protocol).getMappingData().getNewBlockStateId(old);
                                    section.setPaletteEntry(j, newId);
                                }
                            }
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.UNLOAD_CHUNK, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int x = wrapper.passthrough((Type<Integer>)Type.INT);
                        final int z = wrapper.passthrough((Type<Integer>)Type.INT);
                        wrapper.user().get(ChunkLightStorage.class).unloadChunk(x, z);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.EFFECT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.POSITION1_14, Type.POSITION);
                this.map(Type.INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int id = wrapper.get((Type<Integer>)Type.INT, 0);
                        final int data = wrapper.get((Type<Integer>)Type.INT, 1);
                        if (id == 1010) {
                            wrapper.set(Type.INT, 1, ((Protocol1_13_2To1_14)BlockItemPackets1_14.this.protocol).getMappingData().getNewItemId(data));
                        }
                        else if (id == 2001) {
                            wrapper.set(Type.INT, 1, ((Protocol1_13_2To1_14)BlockItemPackets1_14.this.protocol).getMappingData().getNewBlockStateId(data));
                        }
                    }
                });
            }
        });
        this.registerSpawnParticle(ClientboundPackets1_14.SPAWN_PARTICLE, Type.FLAT_VAR_INT_ITEM, Type.FLOAT);
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.MAP_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.BOOLEAN);
                this.map(Type.BOOLEAN, Type.NOTHING);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.SPAWN_POSITION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION1_14, Type.POSITION);
            }
        });
    }
    
    @Override
    protected void registerRewrites() {
        (this.enchantmentRewriter = new EnchantmentRewriter(this, false)).registerEnchantment("minecraft:multishot", "§7Multishot");
        this.enchantmentRewriter.registerEnchantment("minecraft:quick_charge", "§7Quick Charge");
        this.enchantmentRewriter.registerEnchantment("minecraft:piercing", "§7Piercing");
    }
    
    @Override
    public Item handleItemToClient(final Item item) {
        if (item == null) {
            return null;
        }
        super.handleItemToClient(item);
        final CompoundTag tag = item.tag();
        final CompoundTag display;
        if (tag != null && (display = tag.get("display")) != null) {
            final ListTag lore = display.get("Lore");
            if (lore != null) {
                this.saveListTag(display, lore, "Lore");
                for (final Tag loreEntry : lore) {
                    if (!(loreEntry instanceof StringTag)) {
                        continue;
                    }
                    final StringTag loreEntryTag = (StringTag)loreEntry;
                    final String value = loreEntryTag.getValue();
                    if (value == null || value.isEmpty()) {
                        continue;
                    }
                    loreEntryTag.setValue(ChatRewriter.jsonToLegacyText(value));
                }
            }
        }
        this.enchantmentRewriter.handleToClient(item);
        return item;
    }
    
    @Override
    public Item handleItemToServer(final Item item) {
        if (item == null) {
            return null;
        }
        final CompoundTag tag = item.tag();
        final CompoundTag display;
        if (tag != null && (display = tag.get("display")) != null) {
            final ListTag lore = display.get("Lore");
            if (lore != null && !this.hasBackupTag(display, "Lore")) {
                for (final Tag loreEntry : lore) {
                    if (loreEntry instanceof StringTag) {
                        final StringTag loreEntryTag = (StringTag)loreEntry;
                        loreEntryTag.setValue(ChatRewriter.legacyTextToJsonString(loreEntryTag.getValue()));
                    }
                }
            }
        }
        this.enchantmentRewriter.handleToServer(item);
        super.handleItemToServer(item);
        return item;
    }
}
