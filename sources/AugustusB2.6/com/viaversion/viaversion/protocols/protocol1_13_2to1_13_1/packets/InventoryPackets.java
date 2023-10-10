// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13_2to1_13_1.packets;

import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.api.protocol.Protocol;

public class InventoryPackets
{
    public static void register(final Protocol protocol) {
        protocol.registerClientbound(ClientboundPackets1_13.SET_SLOT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.SHORT);
                this.map(Type.FLAT_ITEM, Type.FLAT_VAR_INT_ITEM);
            }
        });
        protocol.registerClientbound(ClientboundPackets1_13.WINDOW_ITEMS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.FLAT_ITEM_ARRAY, Type.FLAT_VAR_INT_ITEM_ARRAY);
            }
        });
        protocol.registerClientbound(ClientboundPackets1_13.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final String channel = wrapper.get(Type.STRING, 0);
                        if (channel.equals("minecraft:trader_list") || channel.equals("trader_list")) {
                            wrapper.passthrough((Type<Object>)Type.INT);
                            for (int size = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                                wrapper.write(Type.FLAT_VAR_INT_ITEM, (Item)wrapper.read((Type<T>)Type.FLAT_ITEM));
                                wrapper.write(Type.FLAT_VAR_INT_ITEM, (Item)wrapper.read((Type<T>)Type.FLAT_ITEM));
                                final boolean secondItem = wrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                                if (secondItem) {
                                    wrapper.write(Type.FLAT_VAR_INT_ITEM, (Item)wrapper.read((Type<T>)Type.FLAT_ITEM));
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
        protocol.registerClientbound(ClientboundPackets1_13.ENTITY_EQUIPMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.FLAT_ITEM, Type.FLAT_VAR_INT_ITEM);
            }
        });
        protocol.registerClientbound(ClientboundPackets1_13.DECLARE_RECIPES, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        for (int recipesNo = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < recipesNo; ++i) {
                            wrapper.passthrough(Type.STRING);
                            final String type = wrapper.passthrough(Type.STRING);
                            if (type.equals("crafting_shapeless")) {
                                wrapper.passthrough(Type.STRING);
                                for (int ingredientsNo = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i2 = 0; i2 < ingredientsNo; ++i2) {
                                    wrapper.write(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT, (Item[])(Object)wrapper.read((Type<T>)Type.FLAT_ITEM_ARRAY_VAR_INT));
                                }
                                wrapper.write(Type.FLAT_VAR_INT_ITEM, (Item)wrapper.read((Type<T>)Type.FLAT_ITEM));
                            }
                            else if (type.equals("crafting_shaped")) {
                                final int ingredientsNo = wrapper.passthrough((Type<Integer>)Type.VAR_INT) * wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                                wrapper.passthrough(Type.STRING);
                                for (int i2 = 0; i2 < ingredientsNo; ++i2) {
                                    wrapper.write(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT, (Item[])(Object)wrapper.read((Type<T>)Type.FLAT_ITEM_ARRAY_VAR_INT));
                                }
                                wrapper.write(Type.FLAT_VAR_INT_ITEM, (Item)wrapper.read((Type<T>)Type.FLAT_ITEM));
                            }
                            else if (type.equals("smelting")) {
                                wrapper.passthrough(Type.STRING);
                                wrapper.write(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT, (Item[])(Object)wrapper.read((Type<T>)Type.FLAT_ITEM_ARRAY_VAR_INT));
                                wrapper.write(Type.FLAT_VAR_INT_ITEM, (Item)wrapper.read((Type<T>)Type.FLAT_ITEM));
                                wrapper.passthrough((Type<Object>)Type.FLOAT);
                                wrapper.passthrough((Type<Object>)Type.VAR_INT);
                            }
                        }
                    }
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_13.CLICK_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.SHORT);
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Type.VAR_INT);
                this.map(Type.FLAT_VAR_INT_ITEM, Type.FLAT_ITEM);
            }
        });
        protocol.registerServerbound(ServerboundPackets1_13.CREATIVE_INVENTORY_ACTION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.SHORT);
                this.map(Type.FLAT_VAR_INT_ITEM, Type.FLAT_ITEM);
            }
        });
    }
}
