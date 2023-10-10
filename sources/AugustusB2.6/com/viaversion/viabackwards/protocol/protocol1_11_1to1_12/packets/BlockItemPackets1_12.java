// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_11_1to1_12.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.LongArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import java.util.Map;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.protocols.protocol1_9_1_2to1_9_3_4.types.Chunk1_9_3_4Type;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.ServerboundPackets1_12;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viabackwards.protocol.protocol1_11_1to1_12.data.MapColorMapping;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.ClientboundPackets1_12;
import com.viaversion.viabackwards.protocol.protocol1_11_1to1_12.Protocol1_11_1To1_12;
import com.viaversion.viabackwards.api.rewriters.LegacyBlockItemRewriter;

public class BlockItemPackets1_12 extends LegacyBlockItemRewriter<Protocol1_11_1To1_12>
{
    public BlockItemPackets1_12(final Protocol1_11_1To1_12 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        ((AbstractProtocol<ClientboundPackets1_12, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_12.MAP_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.BOOLEAN);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        for (int count = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < count * 3; ++i) {
                            wrapper.passthrough((Type<Object>)Type.BYTE);
                        }
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final short columns = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE);
                        if (columns <= 0) {
                            return;
                        }
                        final short rows = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE);
                        wrapper.passthrough((Type<Object>)Type.UNSIGNED_BYTE);
                        wrapper.passthrough((Type<Object>)Type.UNSIGNED_BYTE);
                        final byte[] data = wrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
                        for (int i = 0; i < data.length; ++i) {
                            short color = (short)(data[i] & 0xFF);
                            if (color > 143) {
                                color = (short)MapColorMapping.getNearestOldColor(color);
                                data[i] = (byte)color;
                            }
                        }
                        wrapper.write(Type.BYTE_ARRAY_PRIMITIVE, data);
                    }
                });
            }
        });
        this.registerSetSlot(ClientboundPackets1_12.SET_SLOT, Type.ITEM);
        this.registerWindowItems(ClientboundPackets1_12.WINDOW_ITEMS, Type.ITEM_ARRAY);
        this.registerEntityEquipment(ClientboundPackets1_12.ENTITY_EQUIPMENT, Type.ITEM);
        ((AbstractProtocol<ClientboundPackets1_12, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_12.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if (wrapper.get(Type.STRING, 0).equalsIgnoreCase("MC|TrList")) {
                            wrapper.passthrough((Type<Object>)Type.INT);
                            for (int size = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                                wrapper.write(Type.ITEM, BlockItemPackets1_12.this.handleItemToClient(wrapper.read(Type.ITEM)));
                                wrapper.write(Type.ITEM, BlockItemPackets1_12.this.handleItemToClient(wrapper.read(Type.ITEM)));
                                final boolean secondItem = wrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                                if (secondItem) {
                                    wrapper.write(Type.ITEM, BlockItemPackets1_12.this.handleItemToClient(wrapper.read(Type.ITEM)));
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
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if (wrapper.get((Type<Integer>)Type.VAR_INT, 0) == 1) {
                            wrapper.set(Type.ITEM, 0, null);
                            final PacketWrapper confirm = wrapper.create(ServerboundPackets1_12.WINDOW_CONFIRMATION);
                            confirm.write(Type.UNSIGNED_BYTE, (Short)wrapper.get((Type<T>)Type.UNSIGNED_BYTE, 0));
                            confirm.write(Type.SHORT, (Short)wrapper.get((Type<T>)Type.SHORT, 1));
                            confirm.write(Type.BOOLEAN, false);
                            wrapper.sendToServer(Protocol1_11_1To1_12.class);
                            wrapper.cancel();
                            confirm.sendToServer(Protocol1_11_1To1_12.class);
                            return;
                        }
                        final Item item = wrapper.get(Type.ITEM, 0);
                        BlockItemPackets1_12.this.handleItemToServer(item);
                    }
                });
            }
        });
        this.registerCreativeInvAction(ServerboundPackets1_9_3.CREATIVE_INVENTORY_ACTION, Type.ITEM);
        ((AbstractProtocol<ClientboundPackets1_12, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_12.CHUNK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
                        final Chunk1_9_3_4Type type = new Chunk1_9_3_4Type(clientWorld);
                        final Chunk chunk = wrapper.passthrough((Type<Chunk>)type);
                        LegacyBlockItemRewriter.this.handleChunk(chunk);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_12, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_12.BLOCK_CHANGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int idx = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        wrapper.set(Type.VAR_INT, 0, BlockItemPackets1_12.this.handleBlockID(idx));
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_12, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_12.MULTI_BLOCK_CHANGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.BLOCK_CHANGE_RECORD_ARRAY);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        for (final BlockChangeRecord record : wrapper.get(Type.BLOCK_CHANGE_RECORD_ARRAY, 0)) {
                            record.setBlockId(BlockItemPackets1_12.this.handleBlockID(record.getBlockId()));
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_12, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_12.BLOCK_ENTITY_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.NBT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if (wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0) == 11) {
                            wrapper.cancel();
                        }
                    }
                });
            }
        });
        ((Protocol1_11_1To1_12)this.protocol).getEntityRewriter().filter().handler((event, meta) -> {
            if (meta.metaType().type().equals(Type.ITEM)) {
                meta.setValue(this.handleItemToClient((Item)meta.getValue()));
            }
            return;
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_9_3>)this.protocol).registerServerbound(ServerboundPackets1_9_3.CLIENT_STATUS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if (wrapper.get((Type<Integer>)Type.VAR_INT, 0) == 2) {
                            wrapper.cancel();
                        }
                    }
                });
            }
        });
    }
    
    @Override
    public Item handleItemToClient(final Item item) {
        if (item == null) {
            return null;
        }
        super.handleItemToClient(item);
        if (item.tag() != null) {
            final CompoundTag backupTag = new CompoundTag();
            if (this.handleNbtToClient(item.tag(), backupTag)) {
                item.tag().put("Via|LongArrayTags", backupTag);
            }
        }
        return item;
    }
    
    private boolean handleNbtToClient(final CompoundTag compoundTag, final CompoundTag backupTag) {
        final Iterator<Map.Entry<String, Tag>> iterator = compoundTag.iterator();
        boolean hasLongArrayTag = false;
        while (iterator.hasNext()) {
            final Map.Entry<String, Tag> entry = iterator.next();
            if (entry.getValue() instanceof CompoundTag) {
                final CompoundTag nestedBackupTag = new CompoundTag();
                backupTag.put(entry.getKey(), nestedBackupTag);
                hasLongArrayTag |= this.handleNbtToClient(entry.getValue(), nestedBackupTag);
            }
            else {
                if (!(entry.getValue() instanceof LongArrayTag)) {
                    continue;
                }
                backupTag.put(entry.getKey(), this.fromLongArrayTag(entry.getValue()));
                iterator.remove();
                hasLongArrayTag = true;
            }
        }
        return hasLongArrayTag;
    }
    
    @Override
    public Item handleItemToServer(final Item item) {
        if (item == null) {
            return null;
        }
        super.handleItemToServer(item);
        if (item.tag() != null) {
            final Tag tag = item.tag().remove("Via|LongArrayTags");
            if (tag instanceof CompoundTag) {
                this.handleNbtToServer(item.tag(), (CompoundTag)tag);
            }
        }
        return item;
    }
    
    private void handleNbtToServer(final CompoundTag compoundTag, final CompoundTag backupTag) {
        for (final Map.Entry<String, Tag> entry : backupTag) {
            if (entry.getValue() instanceof CompoundTag) {
                final CompoundTag nestedTag = compoundTag.get(entry.getKey());
                this.handleNbtToServer(nestedTag, entry.getValue());
            }
            else {
                compoundTag.put(entry.getKey(), this.fromIntArrayTag(entry.getValue()));
            }
        }
    }
    
    private IntArrayTag fromLongArrayTag(final LongArrayTag tag) {
        final int[] intArray = new int[tag.length() * 2];
        final long[] longArray = tag.getValue();
        int i = 0;
        for (final long l : longArray) {
            intArray[i++] = (int)(l >> 32);
            intArray[i++] = (int)l;
        }
        return new IntArrayTag(intArray);
    }
    
    private LongArrayTag fromIntArrayTag(final IntArrayTag tag) {
        final long[] longArray = new long[tag.length() / 2];
        final int[] intArray = tag.getValue();
        for (int i = 0, j = 0; i < intArray.length; i += 2, ++j) {
            longArray[j] = ((long)intArray[i] << 32 | ((long)intArray[i + 1] & 0xFFFFFFFFL));
        }
        return new LongArrayTag(longArray);
    }
}
