package client.module.impl.player;

import client.Client;
import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.UpdateEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.util.MoveUtil;
import client.util.liquidbounce.ArmorPiece;
import client.util.liquidbounce.InventoryUtils;
import client.util.liquidbounce.ItemUtils;
import client.util.liquidbounce.TimeUtils;
import client.value.impl.BooleanValue;
import net.minecraft.block.BlockBush;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;

import java.util.*;
import java.util.stream.Collectors;

import static client.module.impl.player.AutoArmor.ARMOR_COMPARATOR;

@ModuleInfo(name = "Inventory Cleaner", description = "", category = Category.PLAYER)
public class InventoryCleaner extends Module {

    private final int maxDelay = 75, minDelay = 50, itemDelay = 0;
    private final BooleanValue invOpen = new BooleanValue("InvOpen", this, false), simulateInventory = new BooleanValue("SimulateInventory", this, true, () -> !invOpen.getValue()), noMove = new BooleanValue("NoMoveClicks", this, false), ignoreVehicles = new BooleanValue("IgnoreVehicles", this, false), hotbar = new BooleanValue("Hotbar", this, true), randomSlot = new BooleanValue("RandomSlot", this, false), sort = new BooleanValue("Sort", this, true);
    private int delay;

    @EventLink
    public final Listener<UpdateEvent> onUpdate = event -> {
        if (!InventoryUtils.CLICK_TIMER.hasTimePassed(delay) || !(mc.currentScreen instanceof GuiInventory) && invOpen.getValue() || MoveUtil.isMoving() && noMove.getValue() || mc.thePlayer.openContainer != null && mc.thePlayer.openContainer.windowId != 0 || Client.INSTANCE.getModuleManager().get(AutoArmor.class).isLocked()) return;
        if (sort.getValue()) sortHotbar();
        while (InventoryUtils.CLICK_TIMER.hasTimePassed(delay)) {
            final List<Integer> garbageItems = items(9, hotbar.getValue() ? 45 : 36).entrySet().stream().filter(entry -> !isUseful(entry.getValue(), entry.getKey())).map(Map.Entry::getKey).collect(Collectors.toList());
            if (randomSlot.getValue()) Collections.shuffle(garbageItems);
            final Integer garbageItem = garbageItems.stream().findFirst().orElse(null);
            if (garbageItem == null) break;
            final boolean openInventory = !(mc.currentScreen instanceof GuiInventory) && simulateInventory.getValue();
            if (openInventory) mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
            mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, garbageItem, 1, 4, mc.thePlayer);
            if (openInventory) mc.getNetHandler().addToSendQueue(new C0DPacketCloseWindow());
            delay = TimeUtils.randomDelay(minDelay, maxDelay);
        }
    };

    public boolean isUseful(final ItemStack itemStack, final int slot) {
        final Item item = itemStack.getItem();
        if (item instanceof ItemSword || item instanceof ItemTool) {
            if (slot >= 36 && Objects.equals(findBetterItem(slot - 36, mc.thePlayer.inventory.getStackInSlot(slot - 36)), slot - 36)) return true;
            for (int i = 0; i <= 8; i++) if ((type(i).equals("Sword") && item instanceof ItemSword || type(i).equals("Pickaxe") && item instanceof ItemPickaxe || type(i).equals("Axe") && item instanceof ItemAxe) && findBetterItem(i, mc.thePlayer.inventory.getStackInSlot(i)) == null) return true;
            final double damage = (itemStack.getAttributeModifiers().get("generic.attackDamage").stream().findFirst().orElse(null) != null ? itemStack.getAttributeModifiers().get("generic.attackDamage").stream().findFirst().orElse(null).getAmount() : 0.0) + 1.25 * ItemUtils.getEnchantment(itemStack, Enchantment.sharpness);
            return items(0, 45).entrySet().stream().noneMatch(entry -> {
                final ItemStack stack = entry.getValue();
                return stack != itemStack && stack.getClass() == itemStack.getClass() && damage < (stack.getAttributeModifiers().get("generic.attackDamage").stream().findFirst().orElse(null) != null ? stack.getAttributeModifiers().get("generic.attackDamage").stream().findFirst().orElse(null).getAmount() : 0.0) + 1.25 * ItemUtils.getEnchantment(stack, Enchantment.sharpness);
            });
        } else if (item instanceof ItemBow) {
            final double currPower = ItemUtils.getEnchantment(itemStack, Enchantment.power);
            return items().entrySet().stream().noneMatch(entry -> {
                final ItemStack stack = entry.getValue();
                return itemStack != stack && stack.getItem() instanceof ItemBow && currPower < ItemUtils.getEnchantment(stack, Enchantment.power);
            });
        } else if (item instanceof ItemArmor) {
            final ArmorPiece currArmor = new ArmorPiece(itemStack, slot);
            return items().entrySet().stream().noneMatch(entry -> {
                final ItemStack stack = entry.getValue();
                if (stack != itemStack && stack.getItem() instanceof ItemArmor) {
                    final ArmorPiece armor = new ArmorPiece(stack, slot);
                    return armor.getArmorType() == currArmor.getArmorType() && ARMOR_COMPARATOR.compare(currArmor, armor) <= 0;
                } else return false;
            });
        } else if (itemStack.getUnlocalizedName().equals("item.compass")) {
            return items(0, 45).entrySet().stream().noneMatch(entry -> {
                final ItemStack stack = entry.getValue();
                return itemStack != stack && stack.getUnlocalizedName().equals("item.compass");
            });
        } else return item instanceof ItemFood || itemStack.getUnlocalizedName().equals("item.arrow") || item instanceof ItemBlock && !(((ItemBlock) item).getBlock() instanceof BlockBush) || item instanceof ItemBed || itemStack.getUnlocalizedName().equals("item.diamond") || itemStack.getUnlocalizedName().equals("item.ingotIron") || item instanceof ItemPotion || item instanceof ItemEnderPearl || item instanceof ItemEnchantedBook || item instanceof ItemBucket || itemStack.getUnlocalizedName().equals("item.stick") || ignoreVehicles.getValue() && (item instanceof ItemBoat || item instanceof ItemMinecart);
    }

    private void sortHotbar() {
        for (int index = 0; index <= 8; index++) {
            final Integer bestItem = findBetterItem(index, mc.thePlayer.inventory.getStackInSlot(index));
            if (bestItem == null) continue;
            if (bestItem != index) {
                final boolean openInventory = !(mc.currentScreen instanceof GuiInventory) && simulateInventory.getValue();
                if (openInventory) mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                mc.playerController.windowClick(0, bestItem < 9 ? bestItem + 36 : bestItem, index, 2, mc.thePlayer);
                if (openInventory) mc.getNetHandler().addToSendQueue(new C0DPacketCloseWindow());
                delay = TimeUtils.randomDelay(minDelay, maxDelay);
            }
        }
    }

    private Boolean currentTypeChecker(final String type, final Item item) {
        switch (type) {
            case "Sword":
                return item instanceof ItemSword;
            case "Pickaxe":
                return item instanceof ItemPickaxe;
            case "Axe":
                return item instanceof ItemAxe;
            default:
                return null;
        }
    }

    private Integer findBetterItem(final int targetSlot, final ItemStack slotStack) {
        final String type = type(targetSlot);
        switch (type) {
            case "Sword":
            case "Pickaxe":
            case "Axe": {
                int bestWeapon = currentTypeChecker(type, slotStack != null ? slotStack.getItem() : null) ? targetSlot : -1;
                for (int index = 0; index < mc.thePlayer.inventory.mainInventory.length; index++) {
                    final ItemStack itemStack = mc.thePlayer.inventory.mainInventory[index];
                    if (itemStack != null && currentTypeChecker(type, itemStack != null ? itemStack.getItem() : null) && !type(index).equals(type)) {
                        if (bestWeapon == -1) bestWeapon = index; else {
                            final double currDamage = (itemStack.getAttributeModifiers().get("generic.attackDamage").stream().findFirst().orElse(null) != null ? itemStack.getAttributeModifiers().get("generic.attackDamage").stream().findFirst().orElse(null).getAmount() : 0.0) + 1.25 * ItemUtils.getEnchantment(itemStack, Enchantment.sharpness);
                            final ItemStack bestStack = mc.thePlayer.inventory.getStackInSlot(bestWeapon);
                            if (bestStack == null) break;
                            final double bestDamage = (bestStack.getAttributeModifiers().get("generic.attackDamage").stream().findFirst().orElse(null) != null ? bestStack.getAttributeModifiers().get("generic.attackDamage").stream().findFirst().orElse(null).getAmount() : 0.0) + 1.25 * ItemUtils.getEnchantment(bestStack, Enchantment.sharpness);
                            if (bestDamage < currDamage) bestWeapon = index;
                        }
                    }
                }
                return bestWeapon != -1 || bestWeapon == targetSlot ? bestWeapon : null;
            }
            case "Bow": {
                int bestBow = (slotStack != null ? slotStack.getItem() : null) instanceof ItemBow ? targetSlot : -1;
                double bestPower = bestBow != -1 ? ItemUtils.getEnchantment(slotStack, Enchantment.power) : 0;
                for (int index = 0; index < mc.thePlayer.inventory.mainInventory.length; index++) {
                    final ItemStack itemStack = mc.thePlayer.inventory.mainInventory[index];
                    if ((itemStack != null ? itemStack.getItem() : null) instanceof ItemBow && !type(index).equals(type)) {
                        if (bestBow == -1) bestBow = index; else {
                            if (ItemUtils.getEnchantment(itemStack, Enchantment.power) > bestPower) {
                                bestBow = index;
                                bestPower = ItemUtils.getEnchantment(itemStack, Enchantment.power);
                            }
                        }
                    }
                }
                return bestBow != -1 ? bestBow : null;
            }
            case "Food": {
                for (int index = 0; index < mc.thePlayer.inventory.mainInventory.length; index++) {
                    final ItemStack stack = mc.thePlayer.inventory.mainInventory[index];
                    if (stack != null) {
                        final Item item = stack.getItem();
                        if (item instanceof ItemFood && !(item instanceof ItemAppleGold) && !type(index).equals("Food")) return ItemUtils.isStackEmpty(slotStack) || !(slotStack.getItem() instanceof ItemFood) ? index : null;
                    }
                }
                break;
            }
            case "Block": {
                for (int index = 0; index < mc.thePlayer.inventory.mainInventory.length; index++) {
                    final ItemStack stack = mc.thePlayer.inventory.mainInventory[index];
                    if (stack != null) {
                        final Item item = stack.getItem();
                        if (item instanceof ItemBlock && !InventoryUtils.BLOCK_BLACKLIST.contains(((ItemBlock) item).getBlock()) && !type(index).equals("Block")) return ItemUtils.isStackEmpty(slotStack) || !(slotStack.getItem() instanceof ItemBlock) ? index : null;
                    }
                }
                break;
            }
            case "Water": {
                for (int index = 0; index < mc.thePlayer.inventory.mainInventory.length; index++) {
                    final ItemStack stack = mc.thePlayer.inventory.mainInventory[index];
                    if (stack != null) {
                        final Item item = stack.getItem();
                        if (item instanceof ItemBucket && ((ItemBucket) item).getIsFull() == Blocks.flowing_water && !type(index).equals("Water")) return ItemUtils.isStackEmpty(slotStack) || !(slotStack.getItem() instanceof ItemBucket) || ((ItemBucket) slotStack.getItem()).getIsFull() != Blocks.flowing_water ? index : null;
                    }
                }
                break;
            }
            case "Gapple": {
                for (int index = 0; index < mc.thePlayer.inventory.mainInventory.length; index++) {
                    final ItemStack stack = mc.thePlayer.inventory.mainInventory[index];
                    if (stack != null) {
                        final Item item = stack.getItem();
                        if (item instanceof ItemAppleGold && !type(index).equals("Gapple")) return ItemUtils.isStackEmpty(slotStack) || !(slotStack.getItem() instanceof ItemAppleGold) ? index : null;
                    }
                }
                break;
            }
            case "Pearl": {
                for (int index = 0; index < mc.thePlayer.inventory.mainInventory.length; index++) {
                    final ItemStack stack = mc.thePlayer.inventory.mainInventory[index];
                    if (stack != null) {
                        final Item item = stack.getItem();
                        if (item instanceof ItemEnderPearl && !type(index).equals("Pearl")) return ItemUtils.isStackEmpty(slotStack) || !(slotStack.getItem() instanceof ItemEnderPearl) ? index : null;
                    }
                }
                break;
            }
        }
        return null;
    }

    private Map<Integer, ItemStack> items() {
        return items(0, 45);
    }

    private Map<Integer, ItemStack> items(final int start, final int end) {
        final Map<Integer, ItemStack> items = new HashMap<>();
        for (int i = end - 1; i >= start; i--) {
            final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack == null || ItemUtils.isStackEmpty(itemStack) || type(i).equals("Ignore")) continue;
            if (System.currentTimeMillis() - itemStack.getTimeCreated() >= itemDelay) items.put(i, itemStack);
        }
        return items;
    }

    private String type(final int targetSlot) {
        switch (targetSlot) {
            case 0:
                return "Sword";
            case 1:
                return "Bow";
            case 2:
                return "Pickaxe";
            case 3:
                return "Axe";
            case 4:
            case 5:
                return "None";
            case 6:
                return "Food";
            case 7:
            case 8:
                return "Block";
            default:
                return "";
        }
    }
}
