package rip.helium.cheat.impl.misc.cheststealer;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.block.BlockChest;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.PlayerUpdateEvent;
import rip.helium.utils.ContainerUtil;
import rip.helium.utils.GuiUtil;
import rip.helium.utils.ItemUtil;
import rip.helium.utils.Stopwatch;
import rip.helium.utils.property.impl.BooleanProperty;
import rip.helium.utils.property.impl.DoubleProperty;
import rip.helium.utils.property.impl.ItemsProperty;
import rip.helium.utils.property.impl.StringsProperty;

import java.util.ArrayList;

public class ChestStealer extends Cheat {

    private final ArrayList<BlockPos> lootedChestPositions;
    public boolean stealing;
    private final BooleanProperty prop_ignoreCustomName = new BooleanProperty("Ignore Custom Chest", "Prevents you from stealing items from chests with a custom displayname.",
            null, true);
    private final ItemsProperty prop_blacklistedItems = new ItemsProperty("BL Items", "Items that won't be stolen.",
            null, false, new ArrayList<>());
    private final StringsProperty prop_blacklistedProps = new StringsProperty("BL Properties", "Properties that will prevent an item from being stolen.",
            null, true, false, new String[]{"Harmful Potion", "Worse Sword", "Duplicate Tool", "Worse Armor"}, new Boolean[]{false, false, false, false});
    private final DoubleProperty prop_stealDelay = new DoubleProperty("Steal delay", "The time in between when each item is taken",
            null, 150, 0, 250, 10, "ms");
    private int fuckingdelay;
    private final Stopwatch takeStopwatch;
    private final Stopwatch stopwatch;

    public ChestStealer() {
        super("ChestStealer", "Takes items from chests.", CheatCategory.MISC);
        registerProperties(prop_ignoreCustomName, prop_blacklistedItems, prop_blacklistedProps, prop_stealDelay);
        takeStopwatch = new Stopwatch();
        lootedChestPositions = new ArrayList<>();

        stopwatch = new Stopwatch();
    }

    public void onEnable() {
        fuckingdelay = prop_stealDelay.getValue().intValue();
    }

    @Collect
    public void onPlayerUpdate(PlayerUpdateEvent playerUpdateEvent) {
        if (playerUpdateEvent.isPre()) {
            if (getMc().currentScreen instanceof GuiChest) {
                GuiChest chest = (GuiChest) getMc().currentScreen;
                if (chest.lowerChestInventory.getName().equals("Enchant Item")) {
                    ItemStack stack = chest.lowerChestInventory.getStackInSlot(33);
                    boolean contains = false;
                    for (String s : stack.getTooltip(mc.thePlayer, true)) {
                        if (s.contains("Blessing V")) {
                            contains = true;
                            break;
                        }
                    }

                    if (stopwatch.hasPassed(500)) {
                        if (!contains) {
                            InventoryPlayer inventoryplayer = mc.thePlayer.inventory;
                            if ((inventoryplayer.getItemStack() != null
                                    && inventoryplayer.getItemStack().getDisplayName().toLowerCase().contains("book"))

                                    || (chest.lowerChestInventory.getStackInSlot(13) != null
                                    && chest.lowerChestInventory.getStackInSlot(13).getDisplayName().toLowerCase().contains("book")))
                                ;

                            getMc().playerController.windowClick(chest.inventorySlots.windowId, 13, 0, 0, getPlayer());
                        } else {
                            getMc().playerController.windowClick(chest.inventorySlots.windowId, 33, 0, 0, getPlayer());
                        }
                        stopwatch.reset();
                    }

                    return;
                }

                BlockPos pos = getMc().objectMouseOver.getBlockPos();
                if (pos != null && getWorld().getBlockState(pos).getBlock() instanceof BlockChest) {
                    lootedChestPositions.add(pos);
                    BlockChest blockChest = (BlockChest) getWorld().getBlockState(getMc().objectMouseOver.getBlockPos()).getBlock();
                    for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL.facings()) {
                        if (getWorld().getBlockState(pos.offset(enumfacing)).getBlock() == blockChest) {
                            lootedChestPositions.add(pos.offset(enumfacing));
                        }
                    }
                }

                if (prop_ignoreCustomName.getValue()) {
                    if (!chest.lowerChestInventory.getName().contains("Chest"))
                        return;
                }

                boolean noMoreItems = true;
                for (int index = 0; index < chest.lowerChestInventory.getSizeInventory(); index++) {
                    ItemStack stack = chest.lowerChestInventory.getStackInSlot(index);
                    if (stack == null)
                        continue;

                    if (prop_blacklistedItems.getValue().contains(stack.getItem()))
                        continue;

                    if (prop_blacklistedProps.getValue().get("Harmful Potion")) {
                        if (stack.getItem() instanceof ItemPotion) {
                            if (ItemUtil.isPotionNegative(stack))
                                continue;
                        }
                    }

                    if (prop_blacklistedProps.getValue().get("Worse Sword")) {
                        if (stack.getItem() instanceof ItemSword) {
                            boolean shouldContinue = false;
                            for (int i = 0; i < 44; i++) {
                                if (i == index) continue;
                                if (getPlayer().inventoryContainer.getSlot(i).getHasStack()) {
                                    ItemStack stackAtIndex = getPlayer().inventoryContainer.getSlot(i).getStack();
                                    if (stackAtIndex.getItem() instanceof ItemSword) {
                                        if (ItemUtil.compareDamage(stack, stackAtIndex) == stackAtIndex) {
                                            shouldContinue = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (shouldContinue)
                                continue;
                        }
                    }

                    if (prop_blacklistedProps.getValue().get("Duplicate Tool")) {
                        if (stack.getItem() instanceof ItemAxe || stack.getItem() instanceof ItemBow || stack.getItem() instanceof ItemFishingRod || stack.getItem() instanceof ItemPickaxe || Item.getIdFromItem(stack.getItem()) == 346) {
                            boolean shouldContinue = false;
                            for (int i = 44; i > 0; i--) {
                                if (i == index) continue;
                                if (getPlayer().inventoryContainer.getSlot(i).getHasStack()) {
                                    ItemStack stackAtIndex = getPlayer().inventoryContainer.getSlot(i).getStack();
                                    if ((stackAtIndex.getItem() instanceof ItemSword || stackAtIndex.getItem() instanceof ItemAxe || stackAtIndex.getItem() instanceof ItemBow || stackAtIndex.getItem() instanceof ItemFishingRod || stackAtIndex.getItem() instanceof ItemAxe || stackAtIndex.getItem() instanceof ItemPickaxe || Item.getIdFromItem(stackAtIndex.getItem()) == 346)) {
                                        if (Item.getIdFromItem(stackAtIndex.getItem()) == Item.getIdFromItem(stack.getItem())) {
                                            shouldContinue = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (shouldContinue)
                                continue;
                        }
                    }

                    if (prop_blacklistedProps.getValue().get("Worse Armor")) {
                        if (stack.getItem() instanceof ItemArmor) {
                            int equipmentType = ContainerUtil.getArmorItemsEquipSlot(stack, true);
                            if (equipmentType != -1) {
                                if (getPlayer().getEquipmentInSlot(equipmentType) != null) {
                                    if (ItemUtil.compareProtection(stack, getPlayer().getEquipmentInSlot(equipmentType)) == getPlayer().getEquipmentInSlot(equipmentType)) {
                                        continue;
                                    }
                                } else {
                                    boolean shouldContinue = false;
                                    for (int i = 44; i > 0; i--) {
                                        if (i == index) continue;
                                        if (getPlayer().inventoryContainer.getSlot(i).getHasStack()) {
                                            ItemStack stackAtIndex = getPlayer().inventoryContainer.getSlot(i).getStack();
                                            if (stackAtIndex.getItem() instanceof ItemArmor) {
                                                if (ContainerUtil.getArmorItemsEquipSlot(stackAtIndex, true) == equipmentType) {
                                                    if (ItemUtil.compareProtection(stack, stackAtIndex) == stackAtIndex) {
                                                        shouldContinue = true;
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (shouldContinue)
                                        continue;
                                }
                            }
                        }
                    }

                    noMoreItems = false;

                    if (takeStopwatch.hasPassed(prop_stealDelay.getValue().intValue())) {
                        getMc().playerController.windowClick(chest.inventorySlots.windowId, index, 0, 1, getPlayer());
                        takeStopwatch.reset();
                        stealing = true;
                        fuckingdelay += 25;
                        if (fuckingdelay >= (prop_stealDelay.getValue().intValue() + (prop_stealDelay.getValue().intValue() / 2))) {
                            fuckingdelay = prop_stealDelay.getValue().intValue();
                        }
                        return;
                    }
                }

                if (noMoreItems) {
                    GuiUtil.closeScreenAndReturn();
                    stealing = false;
                }
            }
        }
    }

    public ArrayList<BlockPos> getLootedChestPositions() {
        return lootedChestPositions;
    }

}
