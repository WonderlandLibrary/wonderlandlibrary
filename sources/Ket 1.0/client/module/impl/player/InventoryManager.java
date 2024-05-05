package client.module.impl.player;

import client.Client;
import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.UpdateEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.util.MoveUtil;
import client.util.liquidbounce.InventoryUtils;
import client.value.impl.BooleanValue;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import static net.minecraft.network.play.client.C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT;

@ModuleInfo(name = "Inventory Manager", description = "", category = Category.PLAYER)
public class InventoryManager extends Module {

    private static final int WEAPON_SLOT = 36, PICKAXE_SLOT = 37, AXE_SLOT = 38, SHOVEL_SLOT = 39, BLOCKS_SLOT = 44;
    private final int maxDelay = 75, minDelay = 50, itemDelay = 0, blockCap = 128;
    private final BooleanValue invOpenValue = new BooleanValue("InvOpen", this, false),
            simulateInventory = new BooleanValue("SimulateInventory", this, true),
            noMoveValue = new BooleanValue("NoMoveClicks", this, false),
            ignoreVehicles = new BooleanValue("IgnoreVehicles", this, false),
            hotbarValue = new BooleanValue("Hotbar", this, true),
            randomSlotValue = new BooleanValue("RandomSlot", this, false),
            sortValue = new BooleanValue("Sort", this, true),
            sortSwords = new BooleanValue("SortSwords", this, true),
            sortFood = new BooleanValue("SortFood", this, true),
            sortArchery = new BooleanValue("SortArchery", this, true),
            keepEmpty = new BooleanValue("KeepEmpty", this, true);

    private long delay, lastClean;

    @EventLink
    public final Listener<UpdateEvent> onUpdate = event -> {
        if (!InventoryUtils.CLICK_TIMER.hasTimePassed(delay) || !(mc.currentScreen instanceof GuiInventory) && invOpenValue.getValue() || noMoveValue.getValue() && MoveUtil.isMoving() || mc.thePlayer.openContainer != null && mc.thePlayer.openContainer.windowId != 0 || Client.INSTANCE.getModuleManager().get(AutoArmor.class).isLocked()) return;
        final boolean b = !(mc.currentScreen instanceof GuiInventory) && simulateInventory.getValue();
        if (b) mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(OPEN_INVENTORY_ACHIEVEMENT));
        if (!mc.thePlayer.inventoryContainer.getSlot(WEAPON_SLOT).getHasStack()) {
            getBestWeapon(WEAPON_SLOT);
        } else {
            if (!isBestWeapon(mc.thePlayer.inventoryContainer.getSlot(WEAPON_SLOT).getStack())) {
                getBestWeapon(WEAPON_SLOT);
            }
        }
        getBestPickaxe(PICKAXE_SLOT);
        getBestShovel(SHOVEL_SLOT);
        getBestAxe(AXE_SLOT);
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (shouldDrop(is, i)) {
                    drop(i);
                    lastClean = System.currentTimeMillis();
                }
            }
        }
        if (b) mc.getNetHandler().addToSendQueue(new C0DPacketCloseWindow());
    };

    public void swap(int slot1, int hotbarSlot) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot1, hotbarSlot, 2, mc.thePlayer);
        lastClean = System.currentTimeMillis();
    }

    public void drop(int slot) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, mc.thePlayer);
        lastClean = System.currentTimeMillis();
    }

    public boolean isBestWeapon(ItemStack stack) {
        float damage = getDamage(stack);
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getDamage(is) > damage && (is.getItem() instanceof ItemSword || !sortSwords.getValue()))
                    return false;
            }
        }
        return stack.getItem() instanceof ItemSword || !sortSwords.getValue();
    }

    public void getBestWeapon(int slot) {
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (isBestWeapon(is) && getDamage(is) > 0 && (is.getItem() instanceof ItemSword || !sortSwords.getValue())) {
                    swap(i, slot - 36);
                    break;
                }
            }
        }
    }

    private float getDamage(ItemStack stack) {
        float damage = 0;
        Item item = stack.getItem();
        if (item instanceof ItemTool) {
            ItemTool tool = (ItemTool) item;
            damage += tool.getToolMaterial().getDamageVsEntity();
        }
        if (item instanceof ItemSword) {
            ItemSword sword = (ItemSword) item;
            damage += sword.getDamageVsEntity();
        }
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f +
                EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
        return damage;
    }

    public boolean shouldDrop(ItemStack stack, int slot) {
        String itemName = stack.getItem().getUnlocalizedName().toLowerCase();
        if (stack.getDisplayName().toLowerCase().contains("(right click)")) {
            return false;
        }
        if (stack.getDisplayName().toLowerCase().contains("profil")) {
            return false;
        }
        if (stack.getDisplayName().toLowerCase().contains("§k||")) {
            return false;
        }
        if ((slot == WEAPON_SLOT && isBestWeapon(mc.thePlayer.inventoryContainer.getSlot(WEAPON_SLOT).getStack())) ||
                (slot == PICKAXE_SLOT && isBestPickaxe(mc.thePlayer.inventoryContainer.getSlot(PICKAXE_SLOT).getStack()) && PICKAXE_SLOT >= 0) ||
                (slot == AXE_SLOT && isBestAxe(mc.thePlayer.inventoryContainer.getSlot(AXE_SLOT).getStack()) && AXE_SLOT >= 0) ||
                (slot == SHOVEL_SLOT && isBestShovel(mc.thePlayer.inventoryContainer.getSlot(SHOVEL_SLOT).getStack()) && SHOVEL_SLOT >= 0)) {
            return false;
        }
        if (stack.getItem() instanceof ItemBlock &&
                (getBlockCount() > blockCap ||
                        InventoryUtils.BLOCK_BLACKLIST.contains(((ItemBlock) stack.getItem()).getBlock()))) {
            return true;
        }

        if (stack.getItem() instanceof ItemPotion) {
            return isBadPotion(stack);
        }

        if (stack.getItem() instanceof ItemArmor)
            return false;

        if (stack.getItem() instanceof ItemFood && sortFood.getValue() && !(stack.getItem() instanceof ItemAppleGold || stack.getItem() instanceof ItemFood)) {
            return true;
        }

        if (stack.getItem() instanceof ItemHoe || stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemSword) {
            return true;
        }

        if ((stack.getItem() instanceof ItemBow || itemName.contains("arrow")) && sortArchery.getValue()) {
            return true;
        }

        if (stack.getItem() instanceof ItemDoublePlant) {
            return true;
        }

        if ((itemName.contains("bowl") ||
                (itemName.contains("bucket") && !itemName.contains("water") &&
                        !itemName.contains("lava") && !itemName.contains("milk")) ||
                (stack.getItem() instanceof ItemGlassBottle && !keepEmpty.getValue())) && !keepEmpty.getValue()) {
            return true;
        }
        return (itemName.contains("tnt")) ||
                (itemName.contains("stick")) ||
                (itemName.contains("egg")) ||
                (itemName.contains("string")) ||
                (itemName.contains("cake")) ||
                (itemName.contains("mushroom") && !itemName.contains("stew")) ||
                (itemName.contains("flint")) ||
                (itemName.contains("dyepowder")) ||
                (itemName.contains("feather")) ||
                (itemName.contains("chest") && !stack.getDisplayName().toLowerCase().contains("collect")) ||
                (itemName.contains("snow")) ||
                (itemName.contains("fish")) ||
                (itemName.contains("enchant")) ||
                (itemName.contains("exp")) ||
                (itemName.contains("shears")) ||
                (itemName.contains("anvil")) ||
                (itemName.contains("torch")) ||
                (itemName.contains("seeds")) ||
                (itemName.contains("beacon")) ||
                (itemName.contains("flower")) ||
                (itemName.contains("leather")) ||
                (itemName.contains("reeds")) ||
                (itemName.contains("skull")) ||
                (itemName.contains("record")) ||
                (itemName.contains("snowball")) ||
                (itemName.contains("slab")) ||
                (itemName.contains("stair")) ||
                (itemName.contains("piston"));
    }

    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock && !InventoryUtils.BLOCK_BLACKLIST.contains(((ItemBlock) item).getBlock())) {
                    blockCount += is.stackSize;
                }
            }
        }
        return blockCount;
    }

    private void getBestPickaxe(int slot) {
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (isBestPickaxe(is) && PICKAXE_SLOT != i) {
                    if (!isBestWeapon(is)) {
                        if (!mc.thePlayer.inventoryContainer.getSlot(PICKAXE_SLOT).getHasStack()) {
                            swap(i, PICKAXE_SLOT - 36);
                        } else if (!isBestPickaxe(mc.thePlayer.inventoryContainer.getSlot(PICKAXE_SLOT).getStack())) {
                            swap(i, PICKAXE_SLOT - 36);
                        }
                    }
                }
            }
        }
    }

    private void getBestShovel(int slot) {
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (isBestShovel(is) && SHOVEL_SLOT != i) {
                    if (!isBestWeapon(is)) {
                        if (!mc.thePlayer.inventoryContainer.getSlot(SHOVEL_SLOT).getHasStack()) {
                            swap(i, SHOVEL_SLOT - 36);
                        } else if (!isBestShovel(mc.thePlayer.inventoryContainer.getSlot(SHOVEL_SLOT).getStack())) {
                            swap(i, SHOVEL_SLOT - 36);
                        }
                    }
                }
            }
        }
    }

    private void getBestAxe(int slot) {
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (isBestAxe(is) && AXE_SLOT != i) {
                    if (!isBestWeapon(is)) {
                        if (!mc.thePlayer.inventoryContainer.getSlot(AXE_SLOT).getHasStack()) {
                            swap(i, AXE_SLOT - 36);
                        } else if (!isBestAxe(mc.thePlayer.inventoryContainer.getSlot(AXE_SLOT).getStack())) {
                            swap(i, AXE_SLOT - 36);
                        }
                    }
                }
            }
        }
    }

    private boolean isBestPickaxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemPickaxe))
            return false;
        float value = getToolEffect(stack);
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getToolEffect(is) > value && is.getItem() instanceof ItemPickaxe) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isBestShovel(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemSpade))
            return false;
        float value = getToolEffect(stack);
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getToolEffect(is) > value && is.getItem() instanceof ItemSpade) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isBestAxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemAxe))
            return false;
        float value = getToolEffect(stack);
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getToolEffect(is) > value && is.getItem() instanceof ItemAxe && !isBestWeapon(stack)) {
                    return false;
                }
            }
        }
        return true;
    }

    private float getToolEffect(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemTool))
            return 0;
        String name = item.getUnlocalizedName();
        ItemTool tool = (ItemTool) item;
        float value;
        if (item instanceof ItemPickaxe) {
            value = tool.getStrVsBlock(stack, Blocks.stone);
            if (name.toLowerCase().contains("gold")) {
                value -= 5;
            }
        } else if (item instanceof ItemSpade) {
            value = tool.getStrVsBlock(stack, Blocks.dirt);
            if (name.toLowerCase().contains("gold")) {
                value -= 5;
            }
        } else if (item instanceof ItemAxe) {
            value = tool.getStrVsBlock(stack, Blocks.log);
            if (name.toLowerCase().contains("gold")) {
                value -= 5;
            }
        } else
            return 1f;
        value += EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075D;
        value += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100d;
        return value;
    }

    private boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion) stack.getItem();
            if (potion.getEffects(stack) == null && !potion.hasEffect(stack)) {
                return true;
            }
            for (final Object o : potion.getEffects(stack)) {
                final PotionEffect effect = (PotionEffect) o;
                if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId()) {
                    return true;
                }
            }
        }
        return false;
    }
}
