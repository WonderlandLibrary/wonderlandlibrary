// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_9to1_8.packets;

import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ServerboundPackets1_9;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ItemRewriter;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.InventoryTracker;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import com.viaversion.viaversion.api.protocol.Protocol;

public class InventoryPackets
{
    public static void register(final Protocol protocol) {
        protocol.registerClientbound(ClientboundPackets1_8.WINDOW_PROPERTY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final short windowId = wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                        final short property = wrapper.get((Type<Short>)Type.SHORT, 0);
                        final short value = wrapper.get((Type<Short>)Type.SHORT, 1);
                        final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                        if (inventoryTracker.getInventory() != null && inventoryTracker.getInventory().equalsIgnoreCase("minecraft:enchanting_table") && property > 3 && property < 7) {
                            final short level = (short)(value >> 8);
                            final short enchantID = (short)(value & 0xFF);
                            wrapper.create(wrapper.getId(), new PacketHandler() {
                                @Override
                                public void handle(final PacketWrapper wrapper) throws Exception {
                                    wrapper.write(Type.UNSIGNED_BYTE, windowId);
                                    wrapper.write(Type.SHORT, property);
                                    wrapper.write(Type.SHORT, enchantID);
                                }
                            }).scheduleSend(Protocol1_9To1_8.class);
                            wrapper.set(Type.SHORT, 0, (short)(property + 3));
                            wrapper.set(Type.SHORT, 1, level);
                        }
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_8.OPEN_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.UNSIGNED_BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final String inventory = wrapper.get(Type.STRING, 0);
                        final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                        inventoryTracker.setInventory(inventory);
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final String inventory = wrapper.get(Type.STRING, 0);
                        if (inventory.equals("minecraft:brewing_stand")) {
                            wrapper.set(Type.UNSIGNED_BYTE, 1, (short)(wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 1) + 1));
                        }
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_8.SET_SLOT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.SHORT);
                this.map(Type.ITEM);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Item stack = wrapper.get(Type.ITEM, 0);
                        final boolean showShieldWhenSwordInHand = Via.getConfig().isShowShieldWhenSwordInHand() && Via.getConfig().isShieldBlocking();
                        if (showShieldWhenSwordInHand) {
                            final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                            final EntityTracker1_9 entityTracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                            final short slotID = wrapper.get((Type<Short>)Type.SHORT, 0);
                            final byte windowId = wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0).byteValue();
                            inventoryTracker.setItemId(windowId, slotID, (stack == null) ? 0 : stack.identifier());
                            entityTracker.syncShieldWithSword();
                        }
                        ItemRewriter.toClient(stack);
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                        final short slotID = wrapper.get((Type<Short>)Type.SHORT, 0);
                        if (inventoryTracker.getInventory() != null && inventoryTracker.getInventory().equals("minecraft:brewing_stand") && slotID >= 4) {
                            wrapper.set(Type.SHORT, 0, (short)(slotID + 1));
                        }
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_8.WINDOW_ITEMS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.ITEM_ARRAY);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Item[] stacks = wrapper.get(Type.ITEM_ARRAY, 0);
                        final Short windowId = wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                        final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                        final EntityTracker1_9 entityTracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        final boolean showShieldWhenSwordInHand = Via.getConfig().isShowShieldWhenSwordInHand() && Via.getConfig().isShieldBlocking();
                        for (short i = 0; i < stacks.length; ++i) {
                            final Item stack = stacks[i];
                            if (showShieldWhenSwordInHand) {
                                inventoryTracker.setItemId(windowId, i, (stack == null) ? 0 : stack.identifier());
                            }
                            ItemRewriter.toClient(stack);
                        }
                        if (showShieldWhenSwordInHand) {
                            entityTracker.syncShieldWithSword();
                        }
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                        if (inventoryTracker.getInventory() != null && inventoryTracker.getInventory().equals("minecraft:brewing_stand")) {
                            final Item[] oldStack = wrapper.get(Type.ITEM_ARRAY, 0);
                            final Item[] newStack = new Item[oldStack.length + 1];
                            for (int i = 0; i < newStack.length; ++i) {
                                if (i > 4) {
                                    newStack[i] = oldStack[i - 1];
                                }
                                else if (i != 4) {
                                    newStack[i] = oldStack[i];
                                }
                            }
                            wrapper.set(Type.ITEM_ARRAY, 0, newStack);
                        }
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_8.CLOSE_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                        inventoryTracker.setInventory(null);
                        inventoryTracker.resetInventory(wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0));
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_8.MAP_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) {
                        wrapper.write(Type.BOOLEAN, true);
                    }
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_9.CREATIVE_INVENTORY_ACTION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.SHORT);
                this.map(Type.ITEM);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Item stack = wrapper.get(Type.ITEM, 0);
                        final boolean showShieldWhenSwordInHand = Via.getConfig().isShowShieldWhenSwordInHand() && Via.getConfig().isShieldBlocking();
                        if (showShieldWhenSwordInHand) {
                            final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                            final EntityTracker1_9 entityTracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                            final short slotID = wrapper.get((Type<Short>)Type.SHORT, 0);
                            inventoryTracker.setItemId((short)0, slotID, (stack == null) ? 0 : stack.identifier());
                            entityTracker.syncShieldWithSword();
                        }
                        ItemRewriter.toServer(stack);
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final short slot = wrapper.get((Type<Short>)Type.SHORT, 0);
                        final boolean throwItem = slot == 45;
                        if (throwItem) {
                            wrapper.create(ClientboundPackets1_9.SET_SLOT, new PacketHandler() {
                                @Override
                                public void handle(final PacketWrapper wrapper) throws Exception {
                                    wrapper.write(Type.UNSIGNED_BYTE, (Short)0);
                                    wrapper.write(Type.SHORT, slot);
                                    wrapper.write(Type.ITEM, null);
                                }
                            }).send(Protocol1_9To1_8.class);
                            wrapper.set(Type.SHORT, 0, (Short)(-999));
                        }
                    }
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_9.CLICK_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.SHORT);
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Type.VAR_INT, Type.BYTE);
                this.map(Type.ITEM);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Item stack = wrapper.get(Type.ITEM, 0);
                        if (Via.getConfig().isShowShieldWhenSwordInHand()) {
                            final Short windowId = wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                            final byte mode = wrapper.get((Type<Byte>)Type.BYTE, 1);
                            final short hoverSlot = wrapper.get((Type<Short>)Type.SHORT, 0);
                            final byte button = wrapper.get((Type<Byte>)Type.BYTE, 0);
                            final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                            inventoryTracker.handleWindowClick(wrapper.user(), windowId, mode, hoverSlot, button);
                        }
                        ItemRewriter.toServer(stack);
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final short windowID = wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                        final short slot = wrapper.get((Type<Short>)Type.SHORT, 0);
                        boolean throwItem = slot == 45 && windowID == 0;
                        final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                        if (inventoryTracker.getInventory() != null && inventoryTracker.getInventory().equals("minecraft:brewing_stand")) {
                            if (slot == 4) {
                                throwItem = true;
                            }
                            if (slot > 4) {
                                wrapper.set(Type.SHORT, 0, (short)(slot - 1));
                            }
                        }
                        if (throwItem) {
                            wrapper.create(ClientboundPackets1_9.SET_SLOT, new PacketHandler() {
                                @Override
                                public void handle(final PacketWrapper wrapper) throws Exception {
                                    wrapper.write(Type.UNSIGNED_BYTE, windowID);
                                    wrapper.write(Type.SHORT, slot);
                                    wrapper.write(Type.ITEM, null);
                                }
                            }).scheduleSend(Protocol1_9To1_8.class);
                            wrapper.set(Type.BYTE, 0, (Byte)0);
                            wrapper.set(Type.BYTE, 1, (Byte)0);
                            wrapper.set(Type.SHORT, 0, (Short)(-999));
                        }
                    }
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_9.CLOSE_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                        inventoryTracker.setInventory(null);
                        inventoryTracker.resetInventory(wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0));
                    }
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_9.HELD_ITEM_CHANGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.SHORT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final boolean showShieldWhenSwordInHand = Via.getConfig().isShowShieldWhenSwordInHand() && Via.getConfig().isShieldBlocking();
                        final EntityTracker1_9 entityTracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        if (entityTracker.isBlocking()) {
                            entityTracker.setBlocking(false);
                            if (!showShieldWhenSwordInHand) {
                                entityTracker.setSecondHand(null);
                            }
                        }
                        if (showShieldWhenSwordInHand) {
                            entityTracker.setHeldItemSlot(wrapper.get((Type<Short>)Type.SHORT, 0));
                            entityTracker.syncShieldWithSword();
                        }
                    }
                });
            }
        });
    }
}
