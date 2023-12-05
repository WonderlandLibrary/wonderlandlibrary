package rip.helium.cheat.impl.misc;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import org.lwjgl.input.Keyboard;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.cheat.impl.misc.cheststealer.ChestStealer;
import rip.helium.event.minecraft.PlayerUpdateEvent;
import rip.helium.event.minecraft.SendPacketEvent;
import rip.helium.utils.Action;
import rip.helium.utils.ContainerUtil;
import rip.helium.utils.ItemUtil;
import rip.helium.utils.Stopwatch;
import rip.helium.utils.property.impl.BooleanProperty;
import rip.helium.utils.property.impl.DoubleProperty;
import rip.helium.utils.property.impl.StringsProperty;

import java.util.ArrayList;

/**
 * @author antja03
 */
public class AutoInventory extends Cheat {
    private final ArrayList<Action> clickQueue;
    public boolean cleaning;
    public boolean equipping;
    public boolean swappingSword;
    private final BooleanProperty spoof = new BooleanProperty("Spoof Open", "Make the server think you opened or closed your inventory!", null,
            true);
    private final BooleanProperty onlyInvProperty = new BooleanProperty("Only In Inventory", "Only clean your inventory while it is opened.", () -> !spoof.getValue(),
            false);
    private final StringsProperty automationsProperty = new StringsProperty("Automated Tasks", "What tasks will be automated by the cheat.", null,
            true, false, new String[]{"Clean", "Equip Armor", "Sword Slot"}, new Boolean[]{false, false, false});
    private final DoubleProperty dropDelayProperty = new DoubleProperty("Cleaning Delay", "Delay before each item is dropped. (MS)", () -> automationsProperty.getValue().get("Clean"),
            150, 0, 350, 10, "");
    private final DoubleProperty equipDelayProperty = new DoubleProperty("Equip Delay", "Delay before each item is equipped. (MS)", () -> automationsProperty.getValue().get("Equip Armor"),
            150, 0, 350, 10, "");
    private final DoubleProperty autoSwordSlotProperty = new DoubleProperty("Sword Slot", "The slot your best sword will be moved to.", () -> automationsProperty.getValue().get("Sword Slot"),
            1, 1, 9, 1, "");
    private final Stopwatch dropStopwatch = new Stopwatch();
    private final Stopwatch equipStopwatch = new Stopwatch();
    private boolean guiOpenedByMod;
    private boolean openedinv = false;

    public AutoInventory() {
        super("Inventory Manager", "Auto inventory.", CheatCategory.MISC);
        registerProperties(spoof, onlyInvProperty, automationsProperty, dropDelayProperty, equipDelayProperty, autoSwordSlotProperty);
        clickQueue = new ArrayList<>();
    }

    @Override
    public void onEnable() {
        cleaning = false;
        equipping = false;
        guiOpenedByMod = false;
        clickQueue.clear();
    }

    @Collect
    public void onSendPacket(SendPacketEvent event) {
        if (event.getPacket() instanceof C0DPacketCloseWindow) {
            openedinv = false;
        }
        if (event.getPacket() instanceof C0BPacketEntityAction) {
            C0BPacketEntityAction packet = (C0BPacketEntityAction) event.getPacket();
            if (packet.getAction() == C0BPacketEntityAction.Action.OPEN_INVENTORY) {
                openedinv = true;
            }
        }
    }

    @Collect
    public void onPlayerUpdate(PlayerUpdateEvent playerUpdateEvent) {
        if (Helium.instance.cheatManager.isCheatEnabled("Inventory Cleaner")) return;

        if (!Helium.instance.cheatManager.isCheatEnabled("Scaffold") && playerUpdateEvent.isPre() && !mc.thePlayer.isUsingItem()) {
            ChestStealer cs = (ChestStealer) Helium.instance.cheatManager.getCheatRegistry().get("ChestStealer");
            if (cs.getState() && cs.stealing)
                return;

            if (onlyInvProperty.getValue()
                    && !(getMc().currentScreen instanceof GuiInventory)
                    && !(getMc().currentScreen instanceof GuiContainerCreative))
                return;

            if (!clickQueue.isEmpty()) {
                clickQueue.get(0).execute();
                clickQueue.remove(clickQueue.get(0));
            } else {
                if (!switchSwordSlot()) {
                    swappingSword = false;
                    if (!equipArmor()) {
                        equipping = false;
                        if (!clean()) {
                            cleaning = false;
                        }
                    }
                }
            }

            if (guiOpenedByMod && !cleaning && !equipping) {
                getMc().displayGuiScreen(null);
                guiOpenedByMod = false;
                for (KeyBinding keyBinding : new KeyBinding[]{
                        getMc().gameSettings.keyBindForward, getMc().gameSettings.keyBindBack,
                        getMc().gameSettings.keyBindLeft, getMc().gameSettings.keyBindRight,
                        getMc().gameSettings.keyBindJump, getMc().gameSettings.keyBindSprint}) {
                    KeyBinding.setKeyBindState(keyBinding.getKeyCode(), Keyboard.isKeyDown(keyBinding.getKeyCode()));
                }
            }
        }
    }

    private boolean switchSwordSlot() {
        if (automationsProperty.getValue().get("Sword Slot")) {
            for (int i = 9; i < 45; i++) {
                if (i == 35 + autoSwordSlotProperty.getValue().intValue())
                    continue;

                if (!getPlayer().inventoryContainer.getSlot(i).getHasStack() || !(getPlayer().inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemSword))
                    continue;

                ItemStack stackInSlot = getPlayer().inventoryContainer.getSlot(i).getStack();

                if (!getPlayer().inventoryContainer.getSlot(35 + autoSwordSlotProperty.getValue().intValue()).getHasStack()) {
                    int finalI1 = i;
                    if (!openedinv) {
                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));
                    }

                    clickQueue.add(() -> getMc().playerController.windowClick(getPlayer().inventoryContainer.windowId, finalI1, 0, 0, getPlayer()));
                    clickQueue.add(() -> getMc().playerController.windowClick(getPlayer().inventoryContainer.windowId, 35 + autoSwordSlotProperty.getValue().intValue(), 0, 0, getPlayer()));

                    if (openedinv) {
                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0DPacketCloseWindow(0));
                    }
                    return true;
                } else {
                    ItemStack stackInWantedSlot = getPlayer().inventoryContainer.getSlot(35 + autoSwordSlotProperty.getValue().intValue()).getStack();
                    if (ItemUtil.compareDamage(stackInSlot, stackInWantedSlot) == stackInSlot) {
                        int finalI = i;
                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));

                        clickQueue.add(() -> getMc().playerController.windowClick(getPlayer().inventoryContainer.windowId, finalI, 0, 0, getPlayer()));
                        clickQueue.add(() -> getMc().playerController.windowClick(getPlayer().inventoryContainer.windowId, 35 + autoSwordSlotProperty.getValue().intValue(), 0, 0, getPlayer()));
                        clickQueue.add(() -> getMc().playerController.windowClick(getPlayer().inventoryContainer.windowId, finalI, 0, 0, getPlayer()));

                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0DPacketCloseWindow(0));

                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean equipArmor() {
        if (automationsProperty.getValue().get("Equip Armor")) {

            for (int i = 9; i < 45; i++) {
                if (!getPlayer().inventoryContainer.getSlot(i).getHasStack())
                    continue;

                ItemStack stackInSlot = getPlayer().inventoryContainer.getSlot(i).getStack();

                if (!(stackInSlot.getItem() instanceof ItemArmor))
                    continue;

                if (ContainerUtil.getArmorItemsEquipSlot(stackInSlot, false) == -1)
                    continue;

                if (getPlayer().getEquipmentInSlot(ContainerUtil.getArmorItemsEquipSlot(stackInSlot, true)) == null) {
                    System.out.println("No stack in slot : " + stackInSlot.getUnlocalizedName());
                    if (equipStopwatch.hasPassed(equipDelayProperty.getValue().intValue())) {
                        int finalI = i;
                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));


                        clickQueue.add(() -> getMc().playerController.windowClick(getPlayer().inventoryContainer.windowId, finalI, 0, 0, getPlayer()));
                        clickQueue.add(() -> getMc().playerController.windowClick(getPlayer().inventoryContainer.windowId, ContainerUtil.getArmorItemsEquipSlot(stackInSlot, false), 0, 0, getPlayer()));

                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0DPacketCloseWindow(0));

                        return true;
                    }
                } else {
                    ItemStack stackInEquipmentSlot = getPlayer().getEquipmentInSlot(ContainerUtil.getArmorItemsEquipSlot(stackInSlot, true));
                    if (ItemUtil.compareProtection(stackInSlot, stackInEquipmentSlot) == stackInSlot) {
                        System.out.println("Stack in slot : " + stackInSlot.getUnlocalizedName());
                        if (equipStopwatch.hasPassed(equipDelayProperty.getValue().intValue())) {
                            int finalI1 = i;
                            mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));


                            clickQueue.add(() -> getMc().playerController.windowClick(getPlayer().inventoryContainer.windowId, finalI1, 0, 0, getPlayer()));
                            clickQueue.add(() -> getMc().playerController.windowClick(getPlayer().inventoryContainer.windowId, ContainerUtil.getArmorItemsEquipSlot(stackInSlot, false), 0, 0, getPlayer()));
                            clickQueue.add(() -> getMc().playerController.windowClick(getPlayer().inventoryContainer.windowId, finalI1, 0, 0, getPlayer()));

                            mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0DPacketCloseWindow(0));

                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean clean() {
        if (automationsProperty.getValue().get("Clean")) {
            if (getPlayer() == null)
                return false;

            ArrayList<Integer> uselessItem = new ArrayList<Integer>();
            for (int i = 0; i < 45; i++) {

                if (!getPlayer().inventoryContainer.getSlot(i).getHasStack())
                    continue;

                ItemStack stackInSlot = getPlayer().inventoryContainer.getSlot(i).getStack();

                if (getPlayer().inventory.armorItemInSlot(0) == stackInSlot
                        || getPlayer().inventory.armorItemInSlot(1) == stackInSlot
                        || getPlayer().inventory.armorItemInSlot(2) == stackInSlot
                        || getPlayer().inventory.armorItemInSlot(3) == stackInSlot)
                    continue;

                if (isGarbo(i))
                    uselessItem.add(i);

            }

            if (uselessItem.size() > 0) {
                cleaning = true;
                if (dropStopwatch.hasPassed(dropDelayProperty.getValue().intValue())) {
                    if (!(mc.thePlayer.inventory.currentItem == uselessItem.get(0))) {

                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));

                        getMc().playerController.windowClick(getPlayer().inventoryContainer.windowId, uselessItem.get(0), 1, 4, getPlayer());

                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0DPacketCloseWindow(0));

                    }
                    uselessItem.remove(0);
                    dropStopwatch.reset();
                }
                return true;
            }
        }
        return false;
    }

    private boolean isGarbo(int slot) {
        ItemStack stackInSlot = getPlayer().inventoryContainer.getSlot(slot).getStack();
        if (stackInSlot.getItem() instanceof ItemSword) {
            for (int i = 0; i < 44; i++) {
                if (i == slot) continue;
                if (getPlayer().inventoryContainer.getSlot(i).getHasStack()) {
                    ItemStack stackAtIndex = getPlayer().inventoryContainer.getSlot(i).getStack();
                    if (stackAtIndex.getItem() instanceof ItemSword) {
                        if (ItemUtil.compareDamage(stackInSlot, stackAtIndex) == stackAtIndex) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        if (stackInSlot.getItem() instanceof ItemAxe || stackInSlot.getItem() instanceof ItemBow || stackInSlot.getItem() instanceof ItemFishingRod || stackInSlot.getItem() instanceof ItemPickaxe || Item.getIdFromItem(stackInSlot.getItem()) == 346) {
            for (int i = 44; i > 0; i--) {
                if (i == slot) continue;
                if (getPlayer().inventoryContainer.getSlot(i).getHasStack()) {
                    ItemStack stackAtIndex = getPlayer().inventoryContainer.getSlot(i).getStack();
                    if ((stackAtIndex.getItem() instanceof ItemSword || stackAtIndex.getItem() instanceof ItemAxe || stackAtIndex.getItem() instanceof ItemBow || stackAtIndex.getItem() instanceof ItemFishingRod || stackAtIndex.getItem() instanceof ItemAxe || stackAtIndex.getItem() instanceof ItemPickaxe || Item.getIdFromItem(stackAtIndex.getItem()) == 346)) {
                        if (Item.getIdFromItem(stackAtIndex.getItem()) == Item.getIdFromItem(stackInSlot.getItem())) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        if (stackInSlot.hasDisplayName())
            return false;

        if (isBestArmorItem(stackInSlot))
            return false;

        if (stackInSlot.getItem() instanceof ItemFood)
            return false;

        if (stackInSlot.getItem() instanceof ItemBlock)
            return false;

        if (stackInSlot.getItem() instanceof ItemPotion) {
            return ItemUtil.isPotionNegative(stackInSlot);
        }

        if (stackInSlot.getItem() instanceof ItemTool) {
            return true;
        }

        if (Item.getIdFromItem(stackInSlot.getItem()) == 367)
            return true; // rotten flesh
        if (Item.getIdFromItem(stackInSlot.getItem()) == 259)
            return true; // flint & steel
        if (Item.getIdFromItem(stackInSlot.getItem()) == 262)
            return true; // arrow
        if (Item.getIdFromItem(stackInSlot.getItem()) == 264)
            return true; // diamond
        if (Item.getIdFromItem(stackInSlot.getItem()) == 265)
            return true; // iron
        if (Item.getIdFromItem(stackInSlot.getItem()) == 336)
            return true; // brick
        if (Item.getIdFromItem(stackInSlot.getItem()) == 266)
            return true; // gold ingot
        if (Item.getIdFromItem(stackInSlot.getItem()) == 345)
            return true; // compass
        if (Item.getIdFromItem(stackInSlot.getItem()) == 46)
            return true; // tnt
        if (Item.getIdFromItem(stackInSlot.getItem()) == 261)
            return true; // bow
        if (Item.getIdFromItem(stackInSlot.getItem()) == 262)
            return true; // arrow
        if (Item.getIdFromItem(stackInSlot.getItem()) == 116)
            return true; // enchanting table
        if (Item.getIdFromItem(stackInSlot.getItem()) == 54)
            return true;

        return true;
    }

    private boolean isBestTool(ItemStack itemStack) {
        return false;
    }

    private boolean isBestArmorItem(ItemStack armorStack) {
        if (armorStack.getItem() instanceof ItemArmor) {
            int equipSlot = ContainerUtil.getArmorItemsEquipSlot(armorStack, true);

            if (equipSlot == -1)
                return false;

            if (mc.thePlayer.getEquipmentInSlot(equipSlot) == null) {
                for (int slotNum = 44; slotNum > 0; slotNum--) {
                    if (!mc.thePlayer.inventoryContainer.getSlot(slotNum).getHasStack())
                        continue;

                    ItemStack stackInSlot = mc.thePlayer.inventoryContainer.getSlot(slotNum).getStack();

                    if (!(stackInSlot.getItem() instanceof ItemArmor))
                        continue;

                    if (ContainerUtil.getArmorItemsEquipSlot(stackInSlot, true) == equipSlot
                            && compareArmorItems(armorStack, stackInSlot) == stackInSlot)
                        return false;
                }
            } else {
                return compareArmorItems(armorStack, mc.thePlayer.getEquipmentInSlot(equipSlot)) != mc.thePlayer.getEquipmentInSlot(equipSlot);
            }

            return true;
        }
        return false;
    }

    private ItemStack compareArmorItems(ItemStack item1, ItemStack item2) {
        if (item1 == null || item2 == null)
            return null;

        if (!(item1.getItem() instanceof ItemArmor && item2.getItem() instanceof ItemArmor))
            return null;

        if (ContainerUtil.getArmorItemsEquipSlot(item1, true) != ContainerUtil.getArmorItemsEquipSlot(item2, true))
            return null;

        double item1Protection = ItemUtil.getArmorProtection(item1);
        double item2Protection = ItemUtil.getArmorProtection(item2);

        if (item1Protection != item2Protection) {
            if (item1Protection > item2Protection)
                return item1;
            else
                return item2;
        } else {
            if (item1.getMaxDamage() > item2.getMaxDamage())
                return item2;
            else
                return item1;
        }
    }

}
