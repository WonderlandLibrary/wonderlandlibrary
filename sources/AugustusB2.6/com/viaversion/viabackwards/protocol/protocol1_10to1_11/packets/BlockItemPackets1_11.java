// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_10to1_11.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.data.entity.StoredEntityData;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_11Types;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viabackwards.api.data.MappedLegacyBlockItem;
import com.viaversion.viabackwards.protocol.protocol1_10to1_11.storage.WindowTracker;
import com.viaversion.viaversion.protocols.protocol1_11to1_10.EntityIdRewriter;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.protocols.protocol1_9_1_2to1_9_3_4.types.Chunk1_9_3_4Type;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import java.util.Arrays;
import java.util.Optional;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viabackwards.protocol.protocol1_10to1_11.storage.ChestedHorseStorage;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import com.viaversion.viabackwards.api.rewriters.LegacyEnchantmentRewriter;
import com.viaversion.viabackwards.protocol.protocol1_10to1_11.Protocol1_10To1_11;
import com.viaversion.viabackwards.api.rewriters.LegacyBlockItemRewriter;

public class BlockItemPackets1_11 extends LegacyBlockItemRewriter<Protocol1_10To1_11>
{
    private LegacyEnchantmentRewriter enchantmentRewriter;
    
    public BlockItemPackets1_11(final Protocol1_10To1_11 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        ((AbstractProtocol<ClientboundPackets1_9_3, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_9_3.SET_SLOT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.SHORT);
                this.map(Type.ITEM);
                this.handler(BlockItemPackets1_11.this.itemToClientHandler(Type.ITEM));
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if (BlockItemPackets1_11.this.isLlama(wrapper.user())) {
                            final Optional<ChestedHorseStorage> horse = BlockItemPackets1_11.this.getChestedHorse(wrapper.user());
                            if (!horse.isPresent()) {
                                return;
                            }
                            final ChestedHorseStorage storage = horse.get();
                            int currentSlot = wrapper.get((Type<Short>)Type.SHORT, 0);
                            wrapper.set(Type.SHORT, 0, (currentSlot = BlockItemPackets1_11.this.getNewSlotId(storage, currentSlot)).shortValue());
                            wrapper.set(Type.ITEM, 0, BlockItemPackets1_11.this.getNewItem(storage, currentSlot, wrapper.get(Type.ITEM, 0)));
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_9_3, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_9_3.WINDOW_ITEMS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.ITEM_ARRAY);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        Item[] stacks = wrapper.get(Type.ITEM_ARRAY, 0);
                        for (int i = 0; i < stacks.length; ++i) {
                            stacks[i] = BlockItemPackets1_11.this.handleItemToClient(stacks[i]);
                        }
                        if (BlockItemPackets1_11.this.isLlama(wrapper.user())) {
                            final Optional<ChestedHorseStorage> horse = BlockItemPackets1_11.this.getChestedHorse(wrapper.user());
                            if (!horse.isPresent()) {
                                return;
                            }
                            final ChestedHorseStorage storage = horse.get();
                            stacks = Arrays.copyOf(stacks, storage.isChested() ? 53 : 38);
                            for (int j = stacks.length - 1; j >= 0; --j) {
                                stacks[BlockItemPackets1_11.this.getNewSlotId(storage, j)] = stacks[j];
                                stacks[j] = BlockItemPackets1_11.this.getNewItem(storage, j, stacks[j]);
                            }
                            wrapper.set(Type.ITEM_ARRAY, 0, stacks);
                        }
                    }
                });
            }
        });
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
                                wrapper.write(Type.ITEM, BlockItemPackets1_11.this.handleItemToClient(wrapper.read(Type.ITEM)));
                                wrapper.write(Type.ITEM, BlockItemPackets1_11.this.handleItemToClient(wrapper.read(Type.ITEM)));
                                final boolean secondItem = wrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                                if (secondItem) {
                                    wrapper.write(Type.ITEM, BlockItemPackets1_11.this.handleItemToClient(wrapper.read(Type.ITEM)));
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
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_9_3>)this.protocol).registerServerbound(ServerboundPackets1_9_3.CLICK_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.SHORT);
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Type.VAR_INT);
                this.map(Type.ITEM);
                this.handler(BlockItemPackets1_11.this.itemToServerHandler(Type.ITEM));
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if (BlockItemPackets1_11.this.isLlama(wrapper.user())) {
                            final Optional<ChestedHorseStorage> horse = BlockItemPackets1_11.this.getChestedHorse(wrapper.user());
                            if (!horse.isPresent()) {
                                return;
                            }
                            final ChestedHorseStorage storage = horse.get();
                            final int clickSlot = wrapper.get((Type<Short>)Type.SHORT, 0);
                            final int correctSlot = BlockItemPackets1_11.this.getOldSlotId(storage, clickSlot);
                            wrapper.set(Type.SHORT, 0, correctSlot.shortValue());
                        }
                    }
                });
            }
        });
        this.registerCreativeInvAction(ServerboundPackets1_9_3.CREATIVE_INVENTORY_ACTION, Type.ITEM);
        ((AbstractProtocol<ClientboundPackets1_9_3, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_9_3.CHUNK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
                        final Chunk1_9_3_4Type type = new Chunk1_9_3_4Type(clientWorld);
                        final Chunk chunk = wrapper.passthrough((Type<Chunk>)type);
                        LegacyBlockItemRewriter.this.handleChunk(chunk);
                        for (final CompoundTag tag : chunk.getBlockEntities()) {
                            final Tag idTag = tag.get("id");
                            if (!(idTag instanceof StringTag)) {
                                continue;
                            }
                            final String id = (String)idTag.getValue();
                            if (!id.equals("minecraft:sign")) {
                                continue;
                            }
                            ((StringTag)idTag).setValue("Sign");
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_9_3, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_9_3.BLOCK_CHANGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int idx = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        wrapper.set(Type.VAR_INT, 0, BlockItemPackets1_11.this.handleBlockID(idx));
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_9_3, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_9_3.MULTI_BLOCK_CHANGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.BLOCK_CHANGE_RECORD_ARRAY);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        for (final BlockChangeRecord record : wrapper.get(Type.BLOCK_CHANGE_RECORD_ARRAY, 0)) {
                            record.setBlockId(BlockItemPackets1_11.this.handleBlockID(record.getBlockId()));
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_9_3, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_9_3.BLOCK_ENTITY_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.NBT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if (wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0) == 10) {
                            wrapper.cancel();
                        }
                        if (wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0) == 1) {
                            final CompoundTag tag = wrapper.get(Type.NBT, 0);
                            EntityIdRewriter.toClientSpawner(tag, true);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_9_3, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_9_3.OPEN_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.map(Type.COMPONENT);
                this.map(Type.UNSIGNED_BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        int entityId = -1;
                        if (wrapper.get(Type.STRING, 0).equals("EntityHorse")) {
                            entityId = wrapper.passthrough((Type<Integer>)Type.INT);
                        }
                        final String inventory = wrapper.get(Type.STRING, 0);
                        final WindowTracker windowTracker = wrapper.user().get(WindowTracker.class);
                        windowTracker.setInventory(inventory);
                        windowTracker.setEntityId(entityId);
                        if (BlockItemPackets1_11.this.isLlama(wrapper.user())) {
                            wrapper.set(Type.UNSIGNED_BYTE, 1, (Short)17);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_9_3, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_9_3.CLOSE_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final WindowTracker windowTracker = wrapper.user().get(WindowTracker.class);
                        windowTracker.setInventory(null);
                        windowTracker.setEntityId(-1);
                    }
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_9_3>)this.protocol).registerServerbound(ServerboundPackets1_9_3.CLOSE_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final WindowTracker windowTracker = wrapper.user().get(WindowTracker.class);
                        windowTracker.setInventory(null);
                        windowTracker.setEntityId(-1);
                    }
                });
            }
        });
        ((Protocol1_10To1_11)this.protocol).getEntityRewriter().filter().handler((event, meta) -> {
            if (meta.metaType().type().equals(Type.ITEM)) {
                meta.setValue(this.handleItemToClient((Item)meta.getValue()));
            }
        });
    }
    
    @Override
    protected void registerRewrites() {
        final MappedLegacyBlockItem data = this.replacementData.computeIfAbsent(52, s -> new MappedLegacyBlockItem(52, (short)(-1), null, false));
        data.setBlockEntityHandler((b, tag) -> {
            EntityIdRewriter.toClientSpawner(tag, true);
            return tag;
        });
        (this.enchantmentRewriter = new LegacyEnchantmentRewriter(this.nbtTagName)).registerEnchantment(71, "§cCurse of Vanishing");
        this.enchantmentRewriter.registerEnchantment(10, "§cCurse of Binding");
        this.enchantmentRewriter.setHideLevelForEnchants(71, 10);
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
        EntityIdRewriter.toClientItem(item, true);
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
        EntityIdRewriter.toServerItem(item, true);
        if (tag.contains(this.nbtTagName + "|ench")) {
            this.enchantmentRewriter.rewriteEnchantmentsToServer(tag, false);
        }
        if (tag.contains(this.nbtTagName + "|StoredEnchantments")) {
            this.enchantmentRewriter.rewriteEnchantmentsToServer(tag, true);
        }
        return item;
    }
    
    private boolean isLlama(final UserConnection user) {
        final WindowTracker tracker = user.get(WindowTracker.class);
        if (tracker.getInventory() != null && tracker.getInventory().equals("EntityHorse")) {
            final EntityTracker entTracker = user.getEntityTracker(Protocol1_10To1_11.class);
            final StoredEntityData entityData = entTracker.entityData(tracker.getEntityId());
            return entityData != null && entityData.type().is(Entity1_11Types.EntityType.LIAMA);
        }
        return false;
    }
    
    private Optional<ChestedHorseStorage> getChestedHorse(final UserConnection user) {
        final WindowTracker tracker = user.get(WindowTracker.class);
        if (tracker.getInventory() != null && tracker.getInventory().equals("EntityHorse")) {
            final EntityTracker entTracker = user.getEntityTracker(Protocol1_10To1_11.class);
            final StoredEntityData entityData = entTracker.entityData(tracker.getEntityId());
            if (entityData != null) {
                return Optional.of((ChestedHorseStorage)entityData.get((Class<T>)ChestedHorseStorage.class));
            }
        }
        return Optional.empty();
    }
    
    private int getNewSlotId(final ChestedHorseStorage storage, final int slotId) {
        final int totalSlots = storage.isChested() ? 53 : 38;
        final int strength = storage.isChested() ? storage.getLiamaStrength() : 0;
        final int startNonExistingFormula = 2 + 3 * strength;
        final int offsetForm = 15 - 3 * strength;
        if (slotId >= startNonExistingFormula && totalSlots > slotId + offsetForm) {
            return offsetForm + slotId;
        }
        if (slotId == 1) {
            return 0;
        }
        return slotId;
    }
    
    private int getOldSlotId(final ChestedHorseStorage storage, final int slotId) {
        final int strength = storage.isChested() ? storage.getLiamaStrength() : 0;
        final int startNonExistingFormula = 2 + 3 * strength;
        final int endNonExistingFormula = 2 + 3 * (storage.isChested() ? 5 : 0);
        final int offsetForm = endNonExistingFormula - startNonExistingFormula;
        if (slotId == 1 || (slotId >= startNonExistingFormula && slotId < endNonExistingFormula)) {
            return 0;
        }
        if (slotId >= endNonExistingFormula) {
            return slotId - offsetForm;
        }
        if (slotId == 0) {
            return 1;
        }
        return slotId;
    }
    
    private Item getNewItem(final ChestedHorseStorage storage, final int slotId, final Item current) {
        final int strength = storage.isChested() ? storage.getLiamaStrength() : 0;
        final int startNonExistingFormula = 2 + 3 * strength;
        final int endNonExistingFormula = 2 + 3 * (storage.isChested() ? 5 : 0);
        if (slotId >= startNonExistingFormula && slotId < endNonExistingFormula) {
            return new DataItem(166, (byte)1, (short)0, this.getNamedTag("§4SLOT DISABLED"));
        }
        if (slotId == 1) {
            return null;
        }
        return current;
    }
}
