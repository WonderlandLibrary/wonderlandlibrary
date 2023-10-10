// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_17to1_17_1;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viabackwards.protocol.protocol1_17to1_17_1.storage.InventoryStateIds;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ServerboundPackets1_17;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ClientboundPackets1_17;
import com.viaversion.viaversion.protocols.protocol1_17_1to1_17.ClientboundPackets1_17_1;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public final class Protocol1_17To1_17_1 extends BackwardsProtocol<ClientboundPackets1_17_1, ClientboundPackets1_17, ServerboundPackets1_17, ServerboundPackets1_17>
{
    private static final int MAX_PAGE_LENGTH = 8192;
    private static final int MAX_TITLE_LENGTH = 128;
    private static final int MAX_PAGES = 200;
    
    public Protocol1_17To1_17_1() {
        super(ClientboundPackets1_17_1.class, ClientboundPackets1_17.class, ServerboundPackets1_17.class, ServerboundPackets1_17.class);
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol<ClientboundPackets1_17_1, ClientboundPackets1_17, S1, S2>)this).registerClientbound(ClientboundPackets1_17_1.REMOVE_ENTITIES, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int[] entityIds;
                final int[] array;
                int length;
                int i = 0;
                int entityId;
                PacketWrapper newPacket;
                this.handler(wrapper -> {
                    entityIds = wrapper.read(Type.VAR_INT_ARRAY_PRIMITIVE);
                    wrapper.cancel();
                    for (length = array.length; i < length; ++i) {
                        entityId = array[i];
                        newPacket = wrapper.create(ClientboundPackets1_17.REMOVE_ENTITY);
                        newPacket.write(Type.VAR_INT, entityId);
                        newPacket.send(Protocol1_17To1_17_1.class);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_17_1, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_17_1.CLOSE_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                final short containerId;
                this.handler(wrapper -> {
                    containerId = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE);
                    wrapper.user().get(InventoryStateIds.class).removeStateId(containerId);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_17_1, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_17_1.SET_SLOT, new PacketRemapper() {
            @Override
            public void registerMap() {
                final short containerId;
                final int stateId;
                this.handler(wrapper -> {
                    containerId = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE);
                    stateId = wrapper.read((Type<Integer>)Type.VAR_INT);
                    wrapper.user().get(InventoryStateIds.class).setStateId(containerId, stateId);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_17_1, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_17_1.WINDOW_ITEMS, new PacketRemapper() {
            @Override
            public void registerMap() {
                final short containerId;
                final int stateId;
                this.handler(wrapper -> {
                    containerId = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE);
                    stateId = wrapper.read((Type<Integer>)Type.VAR_INT);
                    wrapper.user().get(InventoryStateIds.class).setStateId(containerId, stateId);
                    wrapper.write(Type.FLAT_VAR_INT_ITEM_ARRAY, (Item[])(Object)wrapper.read((Type<T>)Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT));
                    wrapper.read(Type.FLAT_VAR_INT_ITEM);
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_17>)this).registerServerbound(ServerboundPackets1_17.CLOSE_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                final short containerId;
                this.handler(wrapper -> {
                    containerId = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE);
                    wrapper.user().get(InventoryStateIds.class).removeStateId(containerId);
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_17>)this).registerServerbound(ServerboundPackets1_17.CLICK_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                final short containerId;
                final int stateId;
                this.handler(wrapper -> {
                    containerId = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE);
                    stateId = wrapper.user().get(InventoryStateIds.class).removeStateId(containerId);
                    wrapper.write(Type.VAR_INT, (stateId == Integer.MAX_VALUE) ? 0 : stateId);
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_17>)this).registerServerbound(ServerboundPackets1_17.EDIT_BOOK, new PacketRemapper() {
            @Override
            public void registerMap() {
                final Item item;
                final boolean signing;
                final CompoundTag tag;
                StringTag titleTag;
                ListTag pagesTag;
                final Iterator<Tag> iterator;
                Tag pageTag;
                String page;
                String title;
                this.handler(wrapper -> {
                    item = wrapper.read(Type.FLAT_VAR_INT_ITEM);
                    signing = wrapper.read((Type<Boolean>)Type.BOOLEAN);
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                    tag = item.tag();
                    titleTag = null;
                    if (tag == null || (pagesTag = tag.get("pages")) == null || (signing && (titleTag = tag.get("title")) == null)) {
                        wrapper.write(Type.VAR_INT, 0);
                        wrapper.write(Type.BOOLEAN, false);
                    }
                    else {
                        if (pagesTag.size() > 200) {
                            pagesTag = new ListTag(pagesTag.getValue().subList(0, 200));
                        }
                        wrapper.write(Type.VAR_INT, pagesTag.size());
                        pagesTag.iterator();
                        while (iterator.hasNext()) {
                            pageTag = iterator.next();
                            page = ((StringTag)pageTag).getValue();
                            if (page.length() > 8192) {
                                page = page.substring(0, 8192);
                            }
                            wrapper.write(Type.STRING, page);
                        }
                        wrapper.write(Type.BOOLEAN, signing);
                        if (signing) {
                            if (titleTag == null) {
                                titleTag = tag.get("title");
                            }
                            title = titleTag.getValue();
                            if (title.length() > 128) {
                                title = title.substring(0, 128);
                            }
                            wrapper.write(Type.STRING, title);
                        }
                    }
                });
            }
        });
    }
    
    @Override
    public void init(final UserConnection connection) {
        connection.put(new InventoryStateIds());
    }
}
