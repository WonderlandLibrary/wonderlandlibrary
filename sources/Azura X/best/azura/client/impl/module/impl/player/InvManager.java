package best.azura.client.impl.module.impl.player;

import best.azura.client.impl.events.EventWorldChange;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.util.other.ChatUtil;
import best.azura.client.util.other.DelayUtil;
import best.azura.client.util.other.ItemUtil;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.other.ServerUtil;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.ModeValue;
import best.azura.client.impl.value.NumberValue;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

@ModuleInfo(name = "Inventory Manager", category = Category.PLAYER, description = "InvCleaner + AutoArmor + InvSorter + ö")
public class InvManager extends Module {

    private final NumberValue<Long> delayValue = new NumberValue<>("Delay", "Delay for interactions", 150L, 0L, 500L);
    private final NumberValue<Integer> toolsSlot = new NumberValue<>("Tools", "Slot for tools", 2, 2, 8);
    private final BooleanValue sort = new BooleanValue("Sort", "Apply inventory sorting", true);
    private final BooleanValue invCleaner = new BooleanValue("Inv. Cleaner", "Automatically clean your inventory (except for armor)", true);
    private final BooleanValue autoArmor = new BooleanValue("Auto Armor", "Automatically equip armor", true);
    private final BooleanValue autoDisable = new BooleanValue("Auto Disable", "Automatically disable when finished", false);
    private final ModeValue invMode = new ModeValue("Inventory Mode", "Change the mode of inventory handling", "Open", "Open", "None", "Spoof");
    private final DelayUtil delay = new DelayUtil();
    private long lastTimeInChest, lastInteractionTime;
    private boolean invOpened;

    @EventHandler
    public final Listener<Event> eventListener = this::onEvent;

    public void onEvent(Event event) {
        if (event instanceof EventReceivedPacket) {
            EventReceivedPacket e = (EventReceivedPacket) event;
            if (e.getPacket() instanceof S32PacketConfirmTransaction) {
                S32PacketConfirmTransaction s32 = e.getPacket();
                if (s32.getActionNumber() > 0 && ServerUtil.isHypixel()) delay.reset();
            }
        }
        if (event instanceof EventSentPacket) {
            EventSentPacket e = (EventSentPacket) event;
            if ((e.getPacket() instanceof C16PacketClientStatus || e.getPacket() instanceof C0DPacketCloseWindow) && invMode.getObject().equals("Spoof") && invOpened) {
                if (e.getPacket() instanceof C0DPacketCloseWindow && ((C0DPacketCloseWindow) e.getPacket()).windowId != mc.thePlayer.inventoryContainer.windowId) return;
                ChatUtil.sendChat("prevented inv opening or closing in order to prevent bans");
                e.setCancelled(true);
            }
        }
        if (event instanceof EventMotion) {
            if (lastInteractionTime == 0 || !invOpened)
                lastInteractionTime = System.currentTimeMillis();
            EventMotion e = (EventMotion) event;
            if(mc.currentScreen instanceof GuiChest || mc.currentScreen instanceof GuiDownloadTerrain) {
                lastTimeInChest = System.currentTimeMillis();
                invOpened = false;
            }
            boolean inv = !(mc.currentScreen instanceof GuiInventory) && invMode.getObject().equals("Open");
            if(!invMode.getObject().equals("Open") && MathUtil.getDifference(lastTimeInChest, System.currentTimeMillis()) < 800) return;
            if (inv) return;
            int[] bestArmorSlots = new int[] {-1, -1, -1, -1};
            float[] bestArmorDamage = new float[] {-1, -1, -1, -1};
            int bestSwordSlot = -1, bestPickaxeSlot = -1, bestAxeSlot = -1, bestFoodSlot = -1, bestBowSlot = -1;
            float bestSwordDamage = -1, bestPickaxeDamage = -1, bestAxeDamage = -1, bestFoodDamage = -1, bestBowDamage = -1;
            if (e.isUpdate()) {
                for (int i = 0; i < 9 * 5; i++) {
                    if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
                    ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                    if (invCleaner.getObject() && ItemUtil.getItemDamage(stack) == -1.0F && delay.hasReached(delayValue.getObject(), true)) {
                        handleInteraction(i, 1, 4);
                        return;
                    }
                    if (stack.getItem() instanceof ItemArmor) {
                        ItemArmor armor = (ItemArmor) stack.getItem();
                        if (bestArmorDamage[armor.armorType] < ItemUtil.getItemDamage(stack)) {
                            bestArmorDamage[armor.armorType] = ItemUtil.getItemDamage(stack);
                            bestArmorSlots[armor.armorType] = i;
                        }
                    } else if (stack.getItem() instanceof ItemSword) {
                        if (bestSwordDamage < ItemUtil.getItemDamage(stack)) {
                            bestSwordDamage = ItemUtil.getItemDamage(stack);
                            bestSwordSlot = i;
                        }
                    } else if (stack.getItem() instanceof ItemPickaxe) {
                        if (bestPickaxeDamage < ItemUtil.getItemDamage(stack)) {
                            bestPickaxeDamage = ItemUtil.getItemDamage(stack);
                            bestPickaxeSlot = i;
                        }
                    } else if (stack.getItem() instanceof ItemAxe) {
                        if (bestAxeDamage < ItemUtil.getItemDamage(stack)) {
                            bestAxeDamage = ItemUtil.getItemDamage(stack);
                            bestAxeSlot = i;
                        }
                    } else if (stack.getItem() instanceof ItemFood && stack.getItem() != Items.golden_apple) {
                        if (bestFoodDamage < ItemUtil.getItemDamage(stack)) {
                            bestFoodDamage = ItemUtil.getItemDamage(stack);
                            bestFoodSlot = i;
                        }
                    } else if (stack.getItem() instanceof ItemBow) {
                        if (bestBowDamage < ItemUtil.getItemDamage(stack)) {
                            bestBowDamage = ItemUtil.getItemDamage(stack);
                            bestBowSlot = i;
                        }
                    }
                }
                if (autoArmor.getObject()) {
                    for (int i = 0; i < 9 * 5; i++) {
                        if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
                        ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                        if (stack.getItem() instanceof ItemArmor) {
                            ItemArmor armor = (ItemArmor) stack.getItem();
                            for (int o = 0; o < 9 * 5; o++) {
                                if (o == i) continue;
                                if (!mc.thePlayer.inventoryContainer.getSlot(o).getHasStack()) continue;
                                ItemStack stack1 = mc.thePlayer.inventoryContainer.getSlot(o).getStack();
                                if (stack1.getItem() instanceof ItemArmor) {
                                    ItemArmor armor1 = (ItemArmor) stack1.getItem();
                                    if (armor.armorType != armor1.armorType) continue;
                                    if (ItemUtil.getItemDamage(stack) >= ItemUtil.getItemDamage(stack1)) {
                                        if (delay.hasReached(delayValue.getObject(), true))
                                            handleInteraction(o, 1, 4);
                                    }
                                }
                            }
                        }
                    }
                    for (int i = 0; i < 4; i++) {
                        if (bestArmorSlots[i] == -1) continue;
                        if (bestArmorSlots[i] > 8 && delay.hasReached(delayValue.getObject(), true))
                            handleInteraction(bestArmorSlots[i], 0, 1);
                    }
                }
                for (int i = 0; i < 9 * 5; i++) {
                    if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                        ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                        if (ItemUtil.getItemDamage(stack) == 0.0F && invCleaner.getObject()) {
                            if (delay.hasReached(delayValue.getObject())) {
                                handleInteraction(i, 1, 4);
                                delay.reset();
                            }
                            return;
                        }
                    }
                }
                for (int i = 0; i < 9 * 5; i++) {
                    if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                        ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                        if (ItemUtil.getItemDamage(stack) == 0.0F) continue;
                        if (stack.getItem() instanceof ItemArmor && autoArmor.getObject()) {
                            if (bestArmorSlots[((ItemArmor) stack.getItem()).armorType] != i && delay.hasReached(delayValue.getObject(), true))
                                handleInteraction(i, 1, 4);
                        } else {
                            if (invCleaner.getObject()) {
                                if (stack.getItem() instanceof ItemSword) {
                                    if (bestSwordSlot != i && delay.hasReached(delayValue.getObject(), true))
                                        handleInteraction(i, 1, 4);
                                } else if (stack.getItem() instanceof ItemBow) {
                                    if (bestBowSlot != i && delay.hasReached(delayValue.getObject(), true))
                                        handleInteraction(i, 1, 4);
                                } else if (stack.getItem() instanceof ItemFood && stack.getItem() != Items.golden_apple) {
                                    if (bestFoodSlot != i && delay.hasReached(delayValue.getObject(), true))
                                        handleInteraction(i, 1, 4);
                                } else if (stack.getItem() instanceof ItemAxe) {
                                    if (bestAxeSlot != i && delay.hasReached(delayValue.getObject(), true))
                                        handleInteraction(i, 1, 4);
                                } else if (stack.getItem() instanceof ItemPickaxe) {
                                    if (bestPickaxeSlot != i && delay.hasReached(delayValue.getObject(), true))
                                        handleInteraction(i, 1, 4);
                                }
                            }
                        }
                    }
                }
                if (sort.getObject()) {
                    if (bestPickaxeSlot != -1 && bestPickaxeSlot != 36 + toolsSlot.getObject() - 1 && delay.hasReached(delayValue.getObject(), true))
                        handleInteraction(bestPickaxeSlot, toolsSlot.getObject() - 1, 2);
                    if (bestAxeSlot != -1 && bestAxeSlot != 36 + toolsSlot.getObject() && delay.hasReached(delayValue.getObject(), true))
                        handleInteraction(bestAxeSlot, toolsSlot.getObject(), 2);
                    if (bestSwordSlot != -1 && bestSwordSlot != 36 && delay.hasReached(delayValue.getObject(), true))
                        handleInteraction(bestSwordSlot, 0, 2);
                }
                if (invMode.getObject().equals("Spoof")) {
                    final long diff = System.currentTimeMillis() - lastInteractionTime;
                    if (diff < delayValue.getObject() + 800 || diff > delayValue.getObject() + 10000) return;
                    close();
                }
                if (autoDisable.getObject()) {
                    final long diff = System.currentTimeMillis() - lastInteractionTime;
                    if (diff < delayValue.getObject() + 800 || diff > delayValue.getObject() + 10000) return;
                    setEnabled(false);
                }
            }
        }
        if (event instanceof EventWorldChange) {
            invOpened = false;
        }
    }

    private void handleInteraction(int slot, int mode, int button) {
        if (invMode.getObject().equals("Spoof") && !invOpened) {
            if (mc.currentScreen instanceof GuiInventory) {
                invOpened = true;
                delay.reset();
                return;
            }
            mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
            invOpened = true;
            ChatUtil.sendChat("opened inv");
            delay.reset();
            return;
        }
        if (!invMode.getObject().equals("Spoof")) invOpened = true;
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, mode, button, mc.thePlayer);
        lastInteractionTime = System.currentTimeMillis();
    }

    private void close() {
        if (mc.currentScreen instanceof GuiInventory) {
            invOpened = false;
            return;
        }
        if (invOpened) {
            ChatUtil.sendChat("closed inv");
            mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0DPacketCloseWindow(mc.thePlayer.inventoryContainer.windowId));
            invOpened = false;
        }
    }

}