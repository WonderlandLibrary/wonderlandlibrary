// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_17_1to1_17;

import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.types.StringType;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ServerboundPackets1_17;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ClientboundPackets1_17;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;

public final class Protocol1_17_1To1_17 extends AbstractProtocol<ClientboundPackets1_17, ClientboundPackets1_17_1, ServerboundPackets1_17, ServerboundPackets1_17>
{
    private static final StringType PAGE_STRING_TYPE;
    private static final StringType TITLE_STRING_TYPE;
    
    public Protocol1_17_1To1_17() {
        super(ClientboundPackets1_17.class, ClientboundPackets1_17_1.class, ServerboundPackets1_17.class, ServerboundPackets1_17.class);
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol<ClientboundPackets1_17, ClientboundPackets1_17_1, S1, S2>)this).registerClientbound(ClientboundPackets1_17.REMOVE_ENTITY, ClientboundPackets1_17_1.REMOVE_ENTITIES, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int entityId;
                this.handler(wrapper -> {
                    entityId = wrapper.read((Type<Integer>)Type.VAR_INT);
                    wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[] { entityId });
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_17, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_17.SET_SLOT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.create(Type.VAR_INT, 0);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_17, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_17.WINDOW_ITEMS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.create(Type.VAR_INT, 0);
                this.handler(wrapper -> {
                    wrapper.write(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT, (Item[])(Object)wrapper.read((Type<T>)Type.FLAT_VAR_INT_ITEM_ARRAY));
                    wrapper.write(Type.FLAT_VAR_INT_ITEM, null);
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_17>)this).registerServerbound(ServerboundPackets1_17.CLICK_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.read(Type.VAR_INT);
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_17>)this).registerServerbound(ServerboundPackets1_17.EDIT_BOOK, new PacketRemapper() {
            @Override
            public void registerMap() {
                final CompoundTag tag;
                final Item item;
                final int slot;
                final int pages;
                final ListTag pagesTag;
                int i;
                String page;
                String title;
                this.handler(wrapper -> {
                    tag = new CompoundTag();
                    item = new DataItem(942, (byte)1, (short)0, tag);
                    wrapper.write(Type.FLAT_VAR_INT_ITEM, item);
                    slot = wrapper.read((Type<Integer>)Type.VAR_INT);
                    pages = wrapper.read((Type<Integer>)Type.VAR_INT);
                    pagesTag = new ListTag(StringTag.class);
                    for (i = 0; i < pages; ++i) {
                        page = wrapper.read((Type<String>)Protocol1_17_1To1_17.PAGE_STRING_TYPE);
                        pagesTag.add(new StringTag(page));
                    }
                    if (pagesTag.size() == 0) {
                        pagesTag.add(new StringTag(""));
                    }
                    tag.put("pages", pagesTag);
                    if (wrapper.read((Type<Boolean>)Type.BOOLEAN)) {
                        title = wrapper.read((Type<String>)Protocol1_17_1To1_17.TITLE_STRING_TYPE);
                        tag.put("title", new StringTag(title));
                        tag.put("author", new StringTag(wrapper.user().getProtocolInfo().getUsername()));
                        wrapper.write(Type.BOOLEAN, true);
                    }
                    else {
                        wrapper.write(Type.BOOLEAN, false);
                    }
                    wrapper.write(Type.VAR_INT, slot);
                });
            }
        });
    }
    
    static {
        PAGE_STRING_TYPE = new StringType(8192);
        TITLE_STRING_TYPE = new StringType(128);
    }
}
