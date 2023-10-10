// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_14to1_13_2.packets;

import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import java.util.List;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import java.util.Collections;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.api.protocol.Protocol;

public class PlayerPackets
{
    public static void register(final Protocol protocol) {
        protocol.registerClientbound(ClientboundPackets1_13.OPEN_SIGN_EDITOR, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION, Type.POSITION1_14);
            }
        });
        protocol.registerServerbound(ServerboundPackets1_14.QUERY_BLOCK_NBT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.POSITION1_14, Type.POSITION);
            }
        });
        protocol.registerServerbound(ServerboundPackets1_14.EDIT_BOOK, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Item item = wrapper.passthrough(Type.FLAT_VAR_INT_ITEM);
                        protocol.getItemRewriter().handleItemToServer(item);
                        if (item == null) {
                            return;
                        }
                        final CompoundTag tag = item.tag();
                        if (tag == null) {
                            return;
                        }
                        final Tag pages = tag.get("pages");
                        if (pages == null) {
                            tag.put("pages", new ListTag((List<Tag>)Collections.singletonList(new StringTag())));
                        }
                        if (Via.getConfig().isTruncate1_14Books() && pages instanceof ListTag) {
                            final ListTag listTag = (ListTag)pages;
                            if (listTag.size() > 50) {
                                listTag.setValue(listTag.getValue().subList(0, 50));
                            }
                        }
                    }
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_14.PLAYER_DIGGING, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.POSITION1_14, Type.POSITION);
            }
        });
        protocol.registerServerbound(ServerboundPackets1_14.RECIPE_BOOK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int type = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        if (type == 0) {
                            wrapper.passthrough(Type.STRING);
                        }
                        else if (type == 1) {
                            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                            wrapper.read((Type<Object>)Type.BOOLEAN);
                            wrapper.read((Type<Object>)Type.BOOLEAN);
                            wrapper.read((Type<Object>)Type.BOOLEAN);
                            wrapper.read((Type<Object>)Type.BOOLEAN);
                        }
                    }
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_14.UPDATE_COMMAND_BLOCK, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION1_14, Type.POSITION);
            }
        });
        protocol.registerServerbound(ServerboundPackets1_14.UPDATE_STRUCTURE_BLOCK, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION1_14, Type.POSITION);
            }
        });
        protocol.registerServerbound(ServerboundPackets1_14.UPDATE_SIGN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION1_14, Type.POSITION);
            }
        });
        protocol.registerServerbound(ServerboundPackets1_14.PLAYER_BLOCK_PLACEMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int hand = wrapper.read((Type<Integer>)Type.VAR_INT);
                        final Position position = wrapper.read(Type.POSITION1_14);
                        final int face = wrapper.read((Type<Integer>)Type.VAR_INT);
                        final float x = wrapper.read((Type<Float>)Type.FLOAT);
                        final float y = wrapper.read((Type<Float>)Type.FLOAT);
                        final float z = wrapper.read((Type<Float>)Type.FLOAT);
                        wrapper.read((Type<Object>)Type.BOOLEAN);
                        wrapper.write(Type.POSITION, position);
                        wrapper.write(Type.VAR_INT, face);
                        wrapper.write(Type.VAR_INT, hand);
                        wrapper.write(Type.FLOAT, x);
                        wrapper.write(Type.FLOAT, y);
                        wrapper.write(Type.FLOAT, z);
                    }
                });
            }
        });
    }
}
