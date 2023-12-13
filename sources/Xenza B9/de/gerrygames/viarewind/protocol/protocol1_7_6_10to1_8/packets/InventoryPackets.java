// 
// Decompiled by Procyon v0.6.0
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.ServerboundPackets1_7;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.GameProfileStorage;
import java.util.UUID;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.EntityTracker;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items.ItemRewriter;
import com.viaversion.viaversion.api.minecraft.item.Item;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import de.gerrygames.viarewind.utils.ChatUtil;
import com.viaversion.viaversion.libs.gson.JsonElement;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.Windows;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;

public class InventoryPackets
{
    public static void register(final Protocol1_7_6_10TO1_8 protocol) {
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.OPEN_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(packetWrapper -> {
                    final short windowId = packetWrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE);
                    final String windowType = packetWrapper.read(Type.STRING);
                    final short windowtypeId = (short)Windows.getInventoryType(windowType);
                    packetWrapper.user().get(Windows.class).types.put(windowId, windowtypeId);
                    packetWrapper.write(Type.UNSIGNED_BYTE, windowtypeId);
                    final JsonElement titleComponent = packetWrapper.read(Type.COMPONENT);
                    final String title = ChatUtil.jsonToLegacy(titleComponent);
                    String title2 = ChatUtil.removeUnusedColor(title, '8');
                    if (title2.length() > 32) {
                        title2 = title2.substring(0, 32);
                    }
                    packetWrapper.write(Type.STRING, title2);
                    packetWrapper.passthrough((Type<Object>)Type.UNSIGNED_BYTE);
                    packetWrapper.write(Type.BOOLEAN, true);
                    if (windowtypeId == 11) {
                        packetWrapper.passthrough((Type<Object>)Type.INT);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.CLOSE_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.handler(packetWrapper -> {
                    final short windowsId = packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    packetWrapper.user().get(Windows.class).remove(windowsId);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.SET_SLOT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(packetWrapper -> {
                    final short windowId = packetWrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                    final short windowType = packetWrapper.user().get(Windows.class).get(windowId);
                    packetWrapper.write(Type.UNSIGNED_BYTE, windowId);
                    short slot = packetWrapper.read((Type<Short>)Type.SHORT);
                    if (windowType == 4) {
                        if (slot == 1) {
                            packetWrapper.cancel();
                            return;
                        }
                        else if (slot >= 2) {
                            --slot;
                        }
                    }
                    packetWrapper.write(Type.SHORT, slot);
                    return;
                });
                this.map(Type.ITEM, Types1_7_6_10.COMPRESSED_NBT_ITEM);
                this.handler(packetWrapper -> {
                    final Item item = packetWrapper.get(Types1_7_6_10.COMPRESSED_NBT_ITEM, 0);
                    ItemRewriter.toClient(item);
                    packetWrapper.set(Types1_7_6_10.COMPRESSED_NBT_ITEM, 0, item);
                    return;
                });
                this.handler(packetWrapper -> {
                    final short windowId2 = packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    if (windowId2 == 0) {
                        final short slot2 = packetWrapper.get((Type<Short>)Type.SHORT, 0);
                        if (slot2 >= 5 && slot2 <= 8) {
                            final Item item2 = packetWrapper.get(Types1_7_6_10.COMPRESSED_NBT_ITEM, 0);
                            final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
                            final UUID myId = packetWrapper.user().getProtocolInfo().getUuid();
                            tracker.setPlayerEquipment(myId, item2, 8 - slot2);
                            if (tracker.getGamemode() == 3) {
                                packetWrapper.cancel();
                            }
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.WINDOW_ITEMS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(packetWrapper -> {
                    final short windowId = packetWrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                    final short windowType = packetWrapper.user().get(Windows.class).get(windowId);
                    packetWrapper.write(Type.UNSIGNED_BYTE, windowId);
                    Item[] items = packetWrapper.read(Type.ITEM_ARRAY);
                    if (windowType == 4) {
                        final Item[] old = items;
                        items = new Item[old.length - 1];
                        items[0] = old[0];
                        System.arraycopy(old, 2, items, 1, old.length - 3);
                    }
                    for (int i = 0; i < items.length; ++i) {
                        items[i] = ItemRewriter.toClient(items[i]);
                    }
                    packetWrapper.write(Types1_7_6_10.COMPRESSED_NBT_ITEM_ARRAY, items);
                    return;
                });
                this.handler(packetWrapper -> {
                    final short windowId2 = packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    if (windowId2 == 0) {
                        final Item[] items2 = packetWrapper.get(Types1_7_6_10.COMPRESSED_NBT_ITEM_ARRAY, 0);
                        final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
                        final UUID myId = packetWrapper.user().getProtocolInfo().getUuid();
                        for (int j = 5; j < 9; ++j) {
                            tracker.setPlayerEquipment(myId, items2[j], 8 - j);
                            if (tracker.getGamemode() == 3) {
                                items2[j] = null;
                            }
                        }
                        if (tracker.getGamemode() == 3) {
                            final GameProfileStorage.GameProfile profile = packetWrapper.user().get(GameProfileStorage.class).get(myId);
                            if (profile != null) {
                                items2[5] = profile.getSkull();
                            }
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.WINDOW_PROPERTY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.handler(packetWrapper -> {
                    final short windowId = packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    final Windows windows = packetWrapper.user().get(Windows.class);
                    final short windowType = windows.get(windowId);
                    final short property = packetWrapper.get((Type<Short>)Type.SHORT, 0);
                    final short value = packetWrapper.get((Type<Short>)Type.SHORT, 1);
                    if (windowType != -1) {
                        if (windowType == 2) {
                            final Windows.Furnace furnace = windows.furnace.computeIfAbsent(windowId, x -> new Windows.Furnace());
                            if (property == 0 || property == 1) {
                                if (property == 0) {
                                    furnace.setFuelLeft(value);
                                }
                                else {
                                    furnace.setMaxFuel(value);
                                }
                                if (furnace.getMaxFuel() == 0) {
                                    packetWrapper.cancel();
                                }
                                else {
                                    final short value2 = (short)(200 * furnace.getFuelLeft() / furnace.getMaxFuel());
                                    packetWrapper.set(Type.SHORT, 0, (Short)1);
                                    packetWrapper.set(Type.SHORT, 1, value2);
                                }
                            }
                            else if (property == 2 || property == 3) {
                                if (property == 2) {
                                    furnace.setProgress(value);
                                }
                                else {
                                    furnace.setMaxProgress(value);
                                }
                                if (furnace.getMaxProgress() == 0) {
                                    packetWrapper.cancel();
                                }
                                else {
                                    final short value3 = (short)(200 * furnace.getProgress() / furnace.getMaxProgress());
                                    packetWrapper.set(Type.SHORT, 0, (Short)0);
                                    packetWrapper.set(Type.SHORT, 1, value3);
                                }
                            }
                        }
                        else if (windowType == 4) {
                            if (property > 2) {
                                packetWrapper.cancel();
                            }
                        }
                        else if (windowType == 8) {
                            windows.levelCost = value;
                            windows.anvilId = windowId;
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_7>)protocol).registerServerbound(ServerboundPackets1_7.CLOSE_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.handler(packetWrapper -> {
                    final short windowsId = packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    packetWrapper.user().get(Windows.class).remove(windowsId);
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_7>)protocol).registerServerbound(ServerboundPackets1_7.CLICK_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(packetWrapper -> {
                    final short windowId = packetWrapper.read((Type<Byte>)Type.BYTE);
                    packetWrapper.write(Type.UNSIGNED_BYTE, windowId);
                    final short windowType = packetWrapper.user().get(Windows.class).get(windowId);
                    short slot = packetWrapper.read((Type<Short>)Type.SHORT);
                    if (windowType == 4 && slot > 0) {
                        ++slot;
                    }
                    packetWrapper.write(Type.SHORT, slot);
                    return;
                });
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Type.BYTE);
                this.map(Types1_7_6_10.COMPRESSED_NBT_ITEM, Type.ITEM);
                this.handler(packetWrapper -> {
                    final Item item = packetWrapper.get(Type.ITEM, 0);
                    ItemRewriter.toServer(item);
                    packetWrapper.set(Type.ITEM, 0, item);
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_7>)protocol).registerServerbound(ServerboundPackets1_7.WINDOW_CONFIRMATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Type.BOOLEAN);
                this.handler(packetWrapper -> {
                    final int action = packetWrapper.get((Type<Short>)Type.SHORT, 0);
                    if (action == -89) {
                        packetWrapper.cancel();
                    }
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_7>)protocol).registerServerbound(ServerboundPackets1_7.CREATIVE_INVENTORY_ACTION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.SHORT);
                this.map(Types1_7_6_10.COMPRESSED_NBT_ITEM, Type.ITEM);
                this.handler(packetWrapper -> {
                    final Item item = packetWrapper.get(Type.ITEM, 0);
                    ItemRewriter.toServer(item);
                    packetWrapper.set(Type.ITEM, 0, item);
                });
            }
        });
    }
}
