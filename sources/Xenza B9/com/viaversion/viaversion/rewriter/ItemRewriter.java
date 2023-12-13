// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.rewriter;

import com.viaversion.viaversion.api.data.ParticleMappings;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.data.Mappings;
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
                this.handler(wrapper -> {
                    final Item[] array;
                    final Item[] items = array = wrapper.get(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT, 0);
                    int i = 0;
                    for (int length = array.length; i < length; ++i) {
                        final Item item = array[i];
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
                this.handler(wrapper -> {
                    final Object val$type = type;
                    byte slot;
                    do {
                        slot = wrapper.passthrough((Type<Byte>)Type.BYTE);
                        ItemRewriter.this.handleItemToClient(wrapper.passthrough((Type<Item>)type));
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
    
    public void registerClickWindow1_17_1(final ServerboundPacketType packetType, final Type<Item> type) {
        this.protocol.registerServerbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.VAR_INT);
                this.map(Type.SHORT);
                this.map(Type.BYTE);
                this.map(Type.VAR_INT);
                this.handler(wrapper -> {
                    final Object val$type = type;
                    for (int length = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < length; ++i) {
                        wrapper.passthrough((Type<Object>)Type.SHORT);
                        ItemRewriter.this.handleItemToServer(wrapper.passthrough((Type<Item>)type));
                    }
                    ItemRewriter.this.handleItemToServer(wrapper.passthrough((Type<Item>)type));
                });
            }
        });
    }
    
    public void registerSetCooldown(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final int itemId = wrapper.read((Type<Integer>)Type.VAR_INT);
                    wrapper.write(Type.VAR_INT, ItemRewriter.this.protocol.getMappingData().getNewItemId(itemId));
                });
            }
        });
    }
    
    public void registerTradeList(final ClientboundPacketType packetType, final Type<Item> type) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final Object val$type = type;
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                    for (int size = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                        ItemRewriter.this.handleItemToClient(wrapper.passthrough((Type<Item>)type));
                        ItemRewriter.this.handleItemToClient(wrapper.passthrough((Type<Item>)type));
                        if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                            ItemRewriter.this.handleItemToClient(wrapper.passthrough((Type<Item>)type));
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
    
    public void registerTradeList1_19(final ClientboundPacketType packetType, final Type<Item> type) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final Object val$type = type;
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                    for (int size = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                        ItemRewriter.this.handleItemToClient(wrapper.passthrough((Type<Item>)type));
                        ItemRewriter.this.handleItemToClient(wrapper.passthrough((Type<Item>)type));
                        ItemRewriter.this.handleItemToClient(wrapper.passthrough((Type<Item>)type));
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
                this.handler(wrapper -> {
                    final Object val$type = type;
                    wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                    for (int size = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                        wrapper.passthrough(Type.STRING);
                        if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                            wrapper.passthrough(Type.STRING);
                        }
                        if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                            wrapper.passthrough(Type.COMPONENT);
                            wrapper.passthrough(Type.COMPONENT);
                            ItemRewriter.this.handleItemToClient(wrapper.passthrough((Type<Item>)type));
                            wrapper.passthrough((Type<Object>)Type.VAR_INT);
                            final int flags = wrapper.passthrough((Type<Integer>)Type.INT);
                            if ((flags & 0x1) != 0x0) {
                                wrapper.passthrough(Type.STRING);
                            }
                            wrapper.passthrough((Type<Object>)Type.FLOAT);
                            wrapper.passthrough((Type<Object>)Type.FLOAT);
                        }
                        wrapper.passthrough(Type.STRING_ARRAY);
                        for (int arrayLength = wrapper.passthrough((Type<Integer>)Type.VAR_INT), array = 0; array < arrayLength; ++array) {
                            wrapper.passthrough(Type.STRING_ARRAY);
                        }
                    }
                });
            }
        });
    }
    
    public void registerWindowPropertyEnchantmentHandler(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.handler(wrapper -> {
                    final short property = wrapper.passthrough((Type<Short>)Type.SHORT);
                    if (property >= 4 && property <= 6) {
                        final Mappings mappings = ItemRewriter.this.protocol.getMappingData().getEnchantmentMappings();
                        final short enchantmentId = (short)mappings.getNewId(wrapper.read((Type<Short>)Type.SHORT));
                        wrapper.write(Type.SHORT, enchantmentId);
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
    
    public void registerSpawnParticle1_19(final ClientboundPacketType packetType, final Type<Item> itemType, final Type<?> coordType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.BOOLEAN);
                this.map(coordType);
                this.map(coordType);
                this.map(coordType);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.INT);
                this.handler(ItemRewriter.this.getSpawnParticleHandler(Type.VAR_INT, itemType));
            }
        });
    }
    
    public PacketHandler getSpawnParticleHandler(final Type<Item> itemType) {
        return this.getSpawnParticleHandler(Type.INT, itemType);
    }
    
    public PacketHandler getSpawnParticleHandler(final Type<Integer> idType, final Type<Item> itemType) {
        return wrapper -> {
            final int id = wrapper.get((Type<Integer>)idType, 0);
            if (id != -1) {
                final ParticleMappings mappings = this.protocol.getMappingData().getParticleMappings();
                if (mappings.isBlockParticle(id)) {
                    final int data = wrapper.read((Type<Integer>)Type.VAR_INT);
                    wrapper.write(Type.VAR_INT, this.protocol.getMappingData().getNewBlockStateId(data));
                }
                else if (mappings.isItemParticle(id)) {
                    this.handleItemToClient(wrapper.passthrough((Type<Item>)itemType));
                }
                final int newId = this.protocol.getMappingData().getNewParticleId(id);
                if (newId != id) {
                    wrapper.set(idType, 0, newId);
                }
            }
        };
    }
    
    public PacketHandler itemArrayHandler(final Type<Item[]> type) {
        return wrapper -> {
            final Item[] array;
            final Item[] items = array = wrapper.get((Type<Item[]>)type, 0);
            int i = 0;
            for (int length = array.length; i < length; ++i) {
                final Item item = array[i];
                this.handleItemToClient(item);
            }
        };
    }
    
    public PacketHandler itemToClientHandler(final Type<Item> type) {
        return wrapper -> this.handleItemToClient(wrapper.get((Type<Item>)type, 0));
    }
    
    public PacketHandler itemToServerHandler(final Type<Item> type) {
        return wrapper -> this.handleItemToServer(wrapper.get((Type<Item>)type, 0));
    }
}
