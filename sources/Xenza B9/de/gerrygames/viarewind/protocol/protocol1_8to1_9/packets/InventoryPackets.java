// 
// Decompiled by Procyon v0.6.0
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets;

import com.viaversion.viaversion.api.type.types.ShortType;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.items.ItemRewriter;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.libs.gson.JsonParser;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.Windows;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_8.ServerboundPackets1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ServerboundPackets1_9;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import com.viaversion.viaversion.api.protocol.Protocol;

public class InventoryPackets
{
    public static void register(final Protocol<ClientboundPackets1_9, ClientboundPackets1_8, ServerboundPackets1_9, ServerboundPackets1_8> protocol) {
        protocol.registerClientbound(ClientboundPackets1_9.CLOSE_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.handler(packetWrapper -> {
                    final short windowsId = packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    packetWrapper.user().get(Windows.class).remove(windowsId);
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.OPEN_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.map(Type.COMPONENT);
                this.map(Type.UNSIGNED_BYTE);
                this.handler(packetWrapper -> {
                    final String type = packetWrapper.get(Type.STRING, 0);
                    if (type.equals("EntityHorse")) {
                        packetWrapper.passthrough((Type<Object>)Type.INT);
                    }
                    return;
                });
                this.handler(packetWrapper -> {
                    final short windowId = packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    final String windowType = packetWrapper.get(Type.STRING, 0);
                    packetWrapper.user().get(Windows.class).put(windowId, windowType);
                    return;
                });
                this.handler(packetWrapper -> {
                    final String type2 = packetWrapper.get(Type.STRING, 0);
                    if (type2.equalsIgnoreCase("minecraft:shulker_box")) {
                        final Type<String> string = Type.STRING;
                        final String type3 = "minecraft:container";
                        final int n;
                        final T t;
                        packetWrapper.set(string, n, (String)t);
                    }
                    final String name = packetWrapper.get(Type.COMPONENT, 0).toString();
                    if (name.equalsIgnoreCase("{\"translate\":\"container.shulkerBox\"}")) {
                        packetWrapper.set(Type.COMPONENT, 0, JsonParser.parseString("{\"text\":\"Shulker Box\"}"));
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.WINDOW_ITEMS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.handler(packetWrapper -> {
                    final short windowId = packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    Item[] items = packetWrapper.read(Type.ITEM_ARRAY);
                    for (int i = 0; i < items.length; ++i) {
                        items[i] = ItemRewriter.toClient(items[i]);
                    }
                    if (windowId == 0 && items.length == 46) {
                        final Item[] old = items;
                        items = new Item[45];
                        System.arraycopy(old, 0, items, 0, 45);
                    }
                    else {
                        final String type = packetWrapper.user().get(Windows.class).get(windowId);
                        if (type != null && type.equalsIgnoreCase("minecraft:brewing_stand")) {
                            System.arraycopy(items, 0, packetWrapper.user().get(Windows.class).getBrewingItems(windowId), 0, 4);
                            Windows.updateBrewingStand(packetWrapper.user(), items[4], windowId);
                            final Item[] old2 = items;
                            items = new Item[old2.length - 1];
                            System.arraycopy(old2, 0, items, 0, 4);
                            System.arraycopy(old2, 5, items, 4, old2.length - 5);
                        }
                    }
                    packetWrapper.write(Type.ITEM_ARRAY, items);
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.SET_SLOT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.SHORT);
                this.map(Type.ITEM);
                this.handler(packetWrapper -> {
                    packetWrapper.set(Type.ITEM, 0, ItemRewriter.toClient(packetWrapper.get(Type.ITEM, 0)));
                    final byte windowId = packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0).byteValue();
                    final short slot = packetWrapper.get((Type<Short>)Type.SHORT, 0);
                    if (windowId == 0 && slot == 45) {
                        packetWrapper.cancel();
                    }
                    else {
                        final String type = packetWrapper.user().get(Windows.class).get(windowId);
                        if (type != null) {
                            if (type.equalsIgnoreCase("minecraft:brewing_stand")) {
                                if (slot > 4) {
                                    final ShortType short1 = Type.SHORT;
                                    final short slot2 = (short)(slot - 1);
                                    final int n;
                                    packetWrapper.set(short1, n, slot2);
                                }
                                else if (slot == 4) {
                                    packetWrapper.cancel();
                                    Windows.updateBrewingStand(packetWrapper.user(), packetWrapper.get(Type.ITEM, 0), windowId);
                                }
                                else {
                                    packetWrapper.user().get(Windows.class).getBrewingItems(windowId)[slot] = packetWrapper.get(Type.ITEM, 0);
                                }
                            }
                        }
                    }
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_8.CLOSE_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.handler(packetWrapper -> {
                    final short windowsId = packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    packetWrapper.user().get(Windows.class).remove(windowsId);
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_8.CLICK_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.SHORT);
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Type.BYTE, Type.VAR_INT);
                this.map(Type.ITEM);
                this.handler(packetWrapper -> packetWrapper.set(Type.ITEM, 0, ItemRewriter.toServer(packetWrapper.get(Type.ITEM, 0))));
                this.handler(packetWrapper -> {
                    final short windowId = packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    final Windows windows = packetWrapper.user().get(Windows.class);
                    final String type = windows.get(windowId);
                    if (type != null) {
                        if (type.equalsIgnoreCase("minecraft:brewing_stand")) {
                            final short slot = packetWrapper.get((Type<Short>)Type.SHORT, 0);
                            if (slot > 3) {
                                final ShortType short1 = Type.SHORT;
                                final short slot2 = (short)(slot + 1);
                                final int n;
                                packetWrapper.set(short1, n, slot2);
                            }
                        }
                    }
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_8.CREATIVE_INVENTORY_ACTION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.SHORT);
                this.map(Type.ITEM);
                this.handler(packetWrapper -> packetWrapper.set(Type.ITEM, 0, ItemRewriter.toServer(packetWrapper.get(Type.ITEM, 0))));
            }
        });
    }
}
