// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.rewriter;

import com.viaversion.viaversion.api.data.ParticleMappings;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.rewriter.RewriterBase;
import com.viaversion.viaversion.api.protocol.Protocol;

public abstract class ItemRewriter<T extends Protocol> extends RewriterBase<T> implements com.viaversion.viaversion.api.rewriter.ItemRewriter<T>
{
    protected ItemRewriter(final T protocol) {
        super(protocol);
    }
    
    @Override
    public Item handleItemToClient(final Item item) {
        if (item == null) {
            return null;
        }
        if (this.protocol.getMappingData() != null && this.protocol.getMappingData().getItemMappings() != null) {
            item.setIdentifier(this.protocol.getMappingData().getNewItemId(item.identifier()));
        }
        return item;
    }
    
    @Override
    public Item handleItemToServer(final Item item) {
        if (item == null) {
            return null;
        }
        if (this.protocol.getMappingData() != null && this.protocol.getMappingData().getItemMappings() != null) {
            item.setIdentifier(this.protocol.getMappingData().getOldItemId(item.identifier()));
        }
        return item;
    }
    
    public void registerWindowItems(final ClientboundPacketType packetType, final Type<Item[]> type) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(type);
                this.handler(ItemRewriter.this.itemArrayHandler(type));
            }
        });
    }
    
    public void registerWindowItems1_17_1(final ClientboundPacketType packetType, final Type<Item[]> itemsType, final Type<Item> carriedItemType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.VAR_INT);
                this.map(itemsType);
                this.map(carriedItemType);
                final Item[] array;
                final Item[] items;
                int length;
                int i = 0;
                Item item;
                this.handler(wrapper -> {
                    items = (array = wrapper.get(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT, 0));
                    for (length = array.length; i < length; ++i) {
                        item = array[i];
                        ItemRewriter.this.handleItemToClient(item);
                    }
                    ItemRewriter.this.handleItemToClient(wrapper.get(Type.FLAT_VAR_INT_ITEM, 0));
                });
            }
        });
    }
    
    public void registerSetSlot(final ClientboundPacketType packetType, final Type<Item> type) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.SHORT);
                this.map(type);
                this.handler(ItemRewriter.this.itemToClientHandler(type));
            }
        });
    }
    
    public void registerSetSlot1_17_1(final ClientboundPacketType packetType, final Type<Item> type) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.VAR_INT);
                this.map(Type.SHORT);
                this.map(type);
                this.handler(ItemRewriter.this.itemToClientHandler(type));
            }
        });
    }
    
    public void registerEntityEquipment(final ClientboundPacketType packetType, final Type<Item> type) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(type);
                this.handler(ItemRewriter.this.itemToClientHandler(type));
            }
        });
    }
    
    public void registerEntityEquipmentArray(final ClientboundPacketType packetType, final Type<Item> type) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                final Type<Item> val$type;
                byte slot;
                this.handler(wrapper -> {
                    val$type = type;
                    do {
                        slot = wrapper.passthrough((Type<Byte>)Type.BYTE);
                        ItemRewriter.this.handleItemToClient(wrapper.passthrough(val$type));
                    } while ((slot & 0xFFFFFF80) != 0x0);
                });
            }
        });
    }
    
    public void registerCreativeInvAction(final ServerboundPacketType packetType, final Type<Item> type) {
        this.protocol.registerServerbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.SHORT);
                this.map(type);
                this.handler(ItemRewriter.this.itemToServerHandler(type));
            }
        });
    }
    
    public void registerClickWindow(final ServerboundPacketType packetType, final Type<Item> type) {
        this.protocol.registerServerbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.SHORT);
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Type.VAR_INT);
                this.map(type);
                this.handler(ItemRewriter.this.itemToServerHandler(type));
            }
        });
    }
    
    public void registerClickWindow1_17(final ServerboundPacketType packetType, final Type<Item> type) {
        this.protocol.registerServerbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.SHORT);
                this.map(Type.BYTE);
                this.map(Type.VAR_INT);
                final Type<Item> val$type;
                int length;
                int i;
                this.handler(wrapper -> {
                    val$type = type;
                    for (length = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < length; ++i) {
                        wrapper.passthrough((Type<Object>)Type.SHORT);
                        ItemRewriter.this.handleItemToServer(wrapper.passthrough(val$type));
                    }
                    ItemRewriter.this.handleItemToServer(wrapper.passthrough(val$type));
                });
            }
        });
    }
    
    public void registerClickWindow1_17_1(final ServerboundPacketType packetType, final Type<Item> type) {
        this.protocol.registerServerbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.VAR_INT);
                this.map(Type.SHORT);
                this.map(Type.BYTE);
                this.map(Type.VAR_INT);
                final Type<Item> val$type;
                int length;
                int i;
                this.handler(wrapper -> {
                    val$type = type;
                    for (length = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < length; ++i) {
                        wrapper.passthrough((Type<Object>)Type.SHORT);
                        ItemRewriter.this.handleItemToServer(wrapper.passthrough(val$type));
                    }
                    ItemRewriter.this.handleItemToServer(wrapper.passthrough(val$type));
                });
            }
        });
    }
    
    public void registerSetCooldown(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int itemId;
                this.handler(wrapper -> {
                    itemId = wrapper.read((Type<Integer>)Type.VAR_INT);
                    wrapper.write(Type.VAR_INT, ItemRewriter.this.protocol.getMappingData().getNewItemId(itemId));
                });
            }
        });
    }
    
    public void registerTradeList(final ClientboundPacketType packetType, final Type<Item> type) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                final Type<Item> val$type;
                int size;
                int i;
                this.handler(wrapper -> {
                    val$type = type;
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                    for (size = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                        ItemRewriter.this.handleItemToClient(wrapper.passthrough(val$type));
                        ItemRewriter.this.handleItemToClient(wrapper.passthrough(val$type));
                        if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                            ItemRewriter.this.handleItemToClient(wrapper.passthrough(val$type));
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
    }
    
    public void registerAdvancements(final ClientboundPacketType packetType, final Type<Item> type) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                final Type<Item> val$type;
                int size;
                int i;
                int flags;
                int arrayLength;
                int array;
                this.handler(wrapper -> {
                    val$type = type;
                    wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                    for (size = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                        wrapper.passthrough(Type.STRING);
                        if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                            wrapper.passthrough(Type.STRING);
                        }
                        if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                            wrapper.passthrough(Type.COMPONENT);
                            wrapper.passthrough(Type.COMPONENT);
                            ItemRewriter.this.handleItemToClient(wrapper.passthrough(val$type));
                            wrapper.passthrough((Type<Object>)Type.VAR_INT);
                            flags = wrapper.passthrough((Type<Integer>)Type.INT);
                            if ((flags & 0x1) != 0x0) {
                                wrapper.passthrough(Type.STRING);
                            }
                            wrapper.passthrough((Type<Object>)Type.FLOAT);
                            wrapper.passthrough((Type<Object>)Type.FLOAT);
                        }
                        wrapper.passthrough(Type.STRING_ARRAY);
                        for (arrayLength = wrapper.passthrough((Type<Integer>)Type.VAR_INT), array = 0; array < arrayLength; ++array) {
                            wrapper.passthrough(Type.STRING_ARRAY);
                        }
                    }
                });
            }
        });
    }
    
    public void registerSpawnParticle(final ClientboundPacketType packetType, final Type<Item> itemType, final Type<?> coordType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.map(coordType);
                this.map(coordType);
                this.map(coordType);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.INT);
                this.handler(ItemRewriter.this.getSpawnParticleHandler(itemType));
            }
        });
    }
    
    public PacketHandler getSpawnParticleHandler(final Type<Item> itemType) {
        final int id;
        ParticleMappings mappings;
        int data;
        int newId;
        return wrapper -> {
            id = wrapper.get((Type<Integer>)Type.INT, 0);
            if (id != -1) {
                mappings = this.protocol.getMappingData().getParticleMappings();
                if (mappings.isBlockParticle(id)) {
                    data = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    wrapper.set(Type.VAR_INT, 0, this.protocol.getMappingData().getNewBlockStateId(data));
                }
                else if (mappings.isItemParticle(id)) {
                    this.handleItemToClient(wrapper.passthrough(itemType));
                }
                newId = this.protocol.getMappingData().getNewParticleId(id);
                if (newId != id) {
                    wrapper.set(Type.INT, 0, newId);
                }
            }
        };
    }
    
    public PacketHandler itemArrayHandler(final Type<Item[]> type) {
        final Item[] array;
        final Item[] items;
        int length;
        int i = 0;
        Item item;
        return wrapper -> {
            items = (array = wrapper.get(type, 0));
            for (length = array.length; i < length; ++i) {
                item = array[i];
                this.handleItemToClient(item);
            }
        };
    }
    
    public PacketHandler itemToClientHandler(final Type<Item> type) {
        return wrapper -> this.handleItemToClient(wrapper.get(type, 0));
    }
    
    public PacketHandler itemToServerHandler(final Type<Item> type) {
        return wrapper -> this.handleItemToServer(wrapper.get(type, 0));
    }
}
