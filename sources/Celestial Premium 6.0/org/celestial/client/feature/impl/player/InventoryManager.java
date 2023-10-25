/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.misc.TimerHelper;
import org.celestial.client.helpers.player.InventoryHelper;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class InventoryManager
extends Feature {
    public static NumberSetting delay1;
    public static BooleanSetting cleaner;
    public static BooleanSetting openinv;
    public static BooleanSetting dropBlocks;
    public static BooleanSetting nomoveswap;
    public static int weaponSlot;
    public static int pickaxeSlot;
    public static int axeSlot;
    public static int shovelSlot;
    public static List<Block> invalidBlocks;
    private final TimerHelper timer = new TimerHelper();

    public InventoryManager() {
        super("InventoryManager", "\u0427\u0438\u0441\u0442\u0438\u0442, \u0441\u043e\u0440\u0442\u0438\u0440\u0443\u0435\u0442 \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u044c \u0437\u0430 \u0432\u0430\u0441", Type.Player);
        delay1 = new NumberSetting("Sort Delay", 1.0f, 0.0f, 10.0f, 0.1f, () -> true);
        cleaner = new BooleanSetting("Cleaner", true, () -> true);
        openinv = new BooleanSetting("Open Inv", true, () -> true);
        nomoveswap = new BooleanSetting("No Moving Swap", false, () -> true);
        dropBlocks = new BooleanSetting("Drop Blocks", false, () -> cleaner.getCurrentValue());
        this.addSettings(delay1, cleaner, dropBlocks, openinv, nomoveswap);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion eventPre) {
        long delay = (long)delay1.getCurrentValue() * 50L;
        if (!(InventoryManager.mc.currentScreen instanceof GuiInventory) && openinv.getCurrentValue()) {
            return;
        }
        if (MovementHelper.isMoving() && nomoveswap.getCurrentValue()) {
            return;
        }
        if (InventoryManager.mc.currentScreen == null || InventoryManager.mc.currentScreen instanceof GuiInventory || InventoryManager.mc.currentScreen instanceof GuiChat) {
            if (this.timer.hasReached(delay) && weaponSlot >= 36) {
                if (!InventoryManager.mc.player.inventoryContainer.getSlot(weaponSlot).getHasStack()) {
                    this.getBestWeapon(weaponSlot);
                } else if (!this.isBestWeapon(InventoryManager.mc.player.inventoryContainer.getSlot(weaponSlot).getStack())) {
                    this.getBestWeapon(weaponSlot);
                }
            }
            if (this.timer.hasReached(delay) && pickaxeSlot >= 36) {
                this.getBestPickaxe();
            }
            if (this.timer.hasReached(delay) && shovelSlot >= 36) {
                this.getBestShovel();
            }
            if (this.timer.hasReached(delay) && axeSlot >= 36) {
                this.getBestAxe();
            }
            if (this.timer.hasReached(delay) && cleaner.getCurrentValue()) {
                for (int i = 9; i < 45; ++i) {
                    ItemStack is;
                    if (!InventoryManager.mc.player.inventoryContainer.getSlot(i).getHasStack() || !this.shouldDrop(is = InventoryManager.mc.player.inventoryContainer.getSlot(i).getStack(), i)) continue;
                    this.drop(i);
                    if (delay == 0L) {
                        InventoryManager.mc.player.closeScreen();
                    }
                    this.timer.reset();
                    if (delay > 0L) break;
                }
            }
        }
    }

    public void swap(int slot1, int hotbarSlot) {
        InventoryManager.mc.playerController.windowClick(InventoryManager.mc.player.inventoryContainer.windowId, slot1, hotbarSlot, ClickType.SWAP, InventoryManager.mc.player);
    }

    public void drop(int slot) {
        InventoryManager.mc.playerController.windowClick(InventoryManager.mc.player.inventoryContainer.windowId, slot, 1, ClickType.THROW, InventoryManager.mc.player);
    }

    public boolean isBestWeapon(ItemStack stack) {
        float damage = this.getDamage(stack);
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!InventoryManager.mc.player.inventoryContainer.getSlot(i).getHasStack() || !(this.getDamage(is = InventoryManager.mc.player.inventoryContainer.getSlot(i).getStack()) > damage) || !(is.getItem() instanceof ItemSword)) continue;
            return false;
        }
        return stack.getItem() instanceof ItemSword;
    }

    public void getBestWeapon(int slot) {
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!InventoryManager.mc.player.inventoryContainer.getSlot(i).getHasStack() || !this.isBestWeapon(is = InventoryManager.mc.player.inventoryContainer.getSlot(i).getStack()) || !(this.getDamage(is) > 0.0f) || !(is.getItem() instanceof ItemSword)) continue;
            this.swap(i, slot - 36);
            this.timer.reset();
            break;
        }
    }

    private float getDamage(ItemStack stack) {
        float damage = 0.0f;
        Item item = stack.getItem();
        if (item instanceof ItemTool) {
            ItemTool tool = (ItemTool)item;
            damage += tool.getDamageVsEntity();
        }
        if (item instanceof ItemSword) {
            ItemSword sword = (ItemSword)item;
            damage += sword.getDamageVsEntity();
        }
        return damage += (float)EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(16)), stack) * 1.25f + (float)EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(20)), stack) * 0.01f;
    }

    public boolean shouldDrop(ItemStack stack, int slot) {
        if (stack.getDisplayName().toLowerCase().contains("/")) {
            return false;
        }
        if (stack.getDisplayName().toLowerCase().contains("\u043f\u0440\u0435\u0434\u043c\u0435\u0442\u044b")) {
            return false;
        }
        if (stack.getDisplayName().toLowerCase().contains("\u00a7k||")) {
            return false;
        }
        if (stack.getDisplayName().toLowerCase().contains("kit")) {
            return false;
        }
        if (stack.getDisplayName().toLowerCase().contains("\u043b\u043e\u0431\u0431\u0438")) {
            return false;
        }
        if (slot == weaponSlot && this.isBestWeapon(InventoryManager.mc.player.inventoryContainer.getSlot(weaponSlot).getStack()) || slot == pickaxeSlot && this.isBestPickaxe(InventoryManager.mc.player.inventoryContainer.getSlot(pickaxeSlot).getStack()) && pickaxeSlot >= 0 || slot == axeSlot && this.isBestAxe(InventoryManager.mc.player.inventoryContainer.getSlot(axeSlot).getStack()) && axeSlot >= 0 || slot == shovelSlot && this.isBestShovel(InventoryManager.mc.player.inventoryContainer.getSlot(shovelSlot).getStack()) && shovelSlot >= 0) {
            return false;
        }
        if (stack.getItem() instanceof ItemArmor) {
            for (int type = 1; type < 5; ++type) {
                ItemStack is;
                if (InventoryManager.mc.player.inventoryContainer.getSlot(4 + type).getHasStack() && InventoryHelper.isBestArmor(is = InventoryManager.mc.player.inventoryContainer.getSlot(4 + type).getStack(), type) || !InventoryHelper.isBestArmor(stack, type)) continue;
                return false;
            }
        }
        if (dropBlocks.getCurrentValue() && stack.getItem() instanceof ItemBlock) {
            return true;
        }
        if (stack.getItem() instanceof ItemPotion && this.isBadPotion(stack)) {
            return true;
        }
        if (stack.getItem() instanceof ItemFood && !(stack.getItem() instanceof ItemAppleGold)) {
            return false;
        }
        if (stack.getItem() instanceof ItemHoe || stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemArmor) {
            return true;
        }
        if (stack.getItem() instanceof ItemBow || stack.getItem().getUnlocalizedName().contains("arrow")) {
            return false;
        }
        return stack.getItem().getUnlocalizedName().contains("tnt") || stack.getItem().getUnlocalizedName().contains("stick") || stack.getItem().getUnlocalizedName().contains("bed") || stack.getItem().getUnlocalizedName().contains("egg") || stack.getItem().getUnlocalizedName().contains("string") || stack.getItem().getUnlocalizedName().contains("cake") || stack.getItem().getUnlocalizedName().contains("mushroom") || stack.getItem().getUnlocalizedName().contains("flint") || stack.getItem().getUnlocalizedName().contains("dyePowder") || stack.getItem().getUnlocalizedName().contains("feather") || stack.getItem().getUnlocalizedName().contains("bucket") || stack.getItem().getUnlocalizedName().contains("chest") && !stack.getDisplayName().toLowerCase().contains("collect") || stack.getItem().getUnlocalizedName().contains("snow") || stack.getItem().getUnlocalizedName().contains("fish") || stack.getItem().getUnlocalizedName().contains("enchant") || stack.getItem().getUnlocalizedName().contains("exp") || stack.getItem().getUnlocalizedName().contains("shears") || stack.getItem().getUnlocalizedName().contains("anvil") || stack.getItem().getUnlocalizedName().contains("torch") || stack.getItem().getUnlocalizedName().contains("seeds") || stack.getItem().getUnlocalizedName().contains("leather") || stack.getItem().getUnlocalizedName().contains("reeds") || stack.getItem().getUnlocalizedName().contains("skull") || stack.getItem().getUnlocalizedName().contains("wool") || stack.getItem().getUnlocalizedName().contains("record") || stack.getItem().getUnlocalizedName().contains("snowball") || stack.getItem() instanceof ItemGlassBottle || stack.getItem().getUnlocalizedName().contains("piston");
    }

    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (!InventoryManager.mc.player.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is = InventoryManager.mc.player.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (!(is.getItem() instanceof ItemBlock) || invalidBlocks.contains(((ItemBlock)item).getBlock())) continue;
            blockCount += is.stackSize;
        }
        return blockCount;
    }

    private void getBestPickaxe() {
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!InventoryManager.mc.player.inventoryContainer.getSlot(i).getHasStack() || !this.isBestPickaxe(is = InventoryManager.mc.player.inventoryContainer.getSlot(i).getStack()) || pickaxeSlot == i || this.isBestWeapon(is)) continue;
            if (!InventoryManager.mc.player.inventoryContainer.getSlot(pickaxeSlot).getHasStack()) {
                this.swap(i, pickaxeSlot - 36);
                this.timer.reset();
                if (!(delay1.getCurrentValue() > 0.0f)) continue;
                return;
            }
            if (this.isBestPickaxe(InventoryManager.mc.player.inventoryContainer.getSlot(pickaxeSlot).getStack())) continue;
            this.swap(i, pickaxeSlot - 36);
            this.timer.reset();
            if (!(delay1.getCurrentValue() > 0.0f)) continue;
            return;
        }
    }

    private void getBestShovel() {
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!InventoryManager.mc.player.inventoryContainer.getSlot(i).getHasStack() || !this.isBestShovel(is = InventoryManager.mc.player.inventoryContainer.getSlot(i).getStack()) || shovelSlot == i || this.isBestWeapon(is)) continue;
            if (!InventoryManager.mc.player.inventoryContainer.getSlot(shovelSlot).getHasStack()) {
                this.swap(i, shovelSlot - 36);
                this.timer.reset();
                if (!(delay1.getCurrentValue() > 0.0f)) continue;
                return;
            }
            if (this.isBestShovel(InventoryManager.mc.player.inventoryContainer.getSlot(shovelSlot).getStack())) continue;
            this.swap(i, shovelSlot - 36);
            this.timer.reset();
            if (!(delay1.getCurrentValue() > 0.0f)) continue;
            return;
        }
    }

    private void getBestAxe() {
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!InventoryManager.mc.player.inventoryContainer.getSlot(i).getHasStack() || !this.isBestAxe(is = InventoryManager.mc.player.inventoryContainer.getSlot(i).getStack()) || axeSlot == i || this.isBestWeapon(is)) continue;
            if (!InventoryManager.mc.player.inventoryContainer.getSlot(axeSlot).getHasStack()) {
                this.swap(i, axeSlot - 36);
                this.timer.reset();
                if (!(delay1.getCurrentValue() > 0.0f)) continue;
                return;
            }
            if (this.isBestAxe(InventoryManager.mc.player.inventoryContainer.getSlot(axeSlot).getStack())) continue;
            this.swap(i, axeSlot - 36);
            this.timer.reset();
            if (!(delay1.getCurrentValue() > 0.0f)) continue;
            return;
        }
    }

    private boolean isBestPickaxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemPickaxe)) {
            return false;
        }
        float value = this.getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!InventoryManager.mc.player.inventoryContainer.getSlot(i).getHasStack() || !(this.getToolEffect(is = InventoryManager.mc.player.inventoryContainer.getSlot(i).getStack()) > value) || !(is.getItem() instanceof ItemPickaxe)) continue;
            return false;
        }
        return true;
    }

    private boolean isBestShovel(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemSpade)) {
            return false;
        }
        float value = this.getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!InventoryManager.mc.player.inventoryContainer.getSlot(i).getHasStack() || !(this.getToolEffect(is = InventoryManager.mc.player.inventoryContainer.getSlot(i).getStack()) > value) || !(is.getItem() instanceof ItemSpade)) continue;
            return false;
        }
        return true;
    }

    private boolean isBestAxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemAxe)) {
            return false;
        }
        float value = this.getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!InventoryManager.mc.player.inventoryContainer.getSlot(i).getHasStack() || !(this.getToolEffect(is = InventoryManager.mc.player.inventoryContainer.getSlot(i).getStack()) > value) || !(is.getItem() instanceof ItemAxe) || this.isBestWeapon(stack)) continue;
            return false;
        }
        return true;
    }

    private float getToolEffect(ItemStack stack) {
        float value;
        Item item = stack.getItem();
        if (!(item instanceof ItemTool)) {
            return 0.0f;
        }
        String name = item.getUnlocalizedName();
        ItemTool tool = (ItemTool)item;
        if (item instanceof ItemPickaxe) {
            value = tool.getStrVsBlock(stack, Blocks.STONE.getDefaultState());
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        } else if (item instanceof ItemSpade) {
            value = tool.getStrVsBlock(stack, Blocks.DIRT.getDefaultState());
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        } else if (item instanceof ItemAxe) {
            value = tool.getStrVsBlock(stack, Blocks.LOG.getDefaultState());
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        } else {
            return 1.0f;
        }
        value = (float)((double)value + (double)EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(32)), stack) * 0.0075);
        value = (float)((double)value + (double)EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(34)), stack) / 100.0);
        return value;
    }

    private boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            for (PotionEffect o : PotionUtils.getEffectsFromStack(stack)) {
                if (o.getPotion() != Potion.getPotionById(19) && o.getPotion() != Potion.getPotionById(7) && o.getPotion() != Potion.getPotionById(2) && o.getPotion() != Potion.getPotionById(18)) continue;
                return true;
            }
        }
        return false;
    }

    static {
        weaponSlot = 36;
        pickaxeSlot = 37;
        axeSlot = 38;
        shovelSlot = 39;
        invalidBlocks = Arrays.asList(Blocks.ENCHANTING_TABLE, Blocks.FURNACE, Blocks.CARPET, Blocks.CRAFTING_TABLE, Blocks.TRAPPED_CHEST, Blocks.CHEST, Blocks.DISPENSER, Blocks.AIR, Blocks.WATER, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.FLOWING_LAVA, Blocks.SAND, Blocks.SNOW_LAYER, Blocks.TORCH, Blocks.ANVIL, Blocks.JUKEBOX, Blocks.STONE_BUTTON, Blocks.WOODEN_BUTTON, Blocks.LEVER, Blocks.NOTEBLOCK, Blocks.STONE_PRESSURE_PLATE, Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, Blocks.WOODEN_PRESSURE_PLATE, Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, Blocks.STONE_SLAB, Blocks.WOODEN_SLAB, Blocks.STONE_SLAB2, Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.YELLOW_FLOWER, Blocks.RED_FLOWER, Blocks.ANVIL, Blocks.GLASS_PANE, Blocks.STAINED_GLASS_PANE, Blocks.IRON_BARS, Blocks.CACTUS, Blocks.LADDER, Blocks.WEB);
    }
}

