package de.tired.base.module.implementation.misc;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.guis.newclickgui.setting.ModeSetting;
import de.tired.base.guis.newclickgui.setting.NumberSetting;
import de.tired.base.guis.newclickgui.setting.impl.BooleanSetting;
import de.tired.util.math.TimerUtil;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.EventPreMotion;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ModuleAnnotation(name = "Manager", category = ModuleCategory.MISC)
public class InvCleaner extends Module {

    private static List<Block> blacklistedBlocks;
    public static int weaponSlot = 36, pickaxeSlot = 37, axeSlot = 38, shovelSlot = 39;
    TimerUtil timer = new TimerUtil();
    ArrayList<Integer> whitelistedItems = new ArrayList<>();



    private BooleanSetting onlySwords = new BooleanSetting("OnlySwordWeapon", this, true);
    private BooleanSetting cleanFood = new BooleanSetting("cleanFood", this, false);
    private BooleanSetting bow = new BooleanSetting("cleanBowsUArrow", this, false);

    private ModeSetting mode = new ModeSetting("Mode", this, new String[]{"Basic", "OpenInv"});
    private BooleanSetting cleanInv = new BooleanSetting("CleanInventory", this, true);
    public NumberSetting delaySetting = new NumberSetting("Delay", this, 1, 0, 10, 1);

    public InvCleaner() {
        blacklistedBlocks = Arrays.asList(
                Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava,
                Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars,
                Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore,
                Blocks.chest, Blocks.trapped_chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt,
                Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore,
                Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate,
                Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.tallgrass, Blocks.tripwire, Blocks.tripwire_hook, Blocks.rail, Blocks.waterlily,
                Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.vine, Blocks.trapdoor, Blocks.yellow_flower, Blocks.ladder, Blocks.furnace,
                Blocks.sand, Blocks.cactus, Blocks.dispenser, Blocks.noteblock, Blocks.dropper, Blocks.crafting_table, Blocks.web, Blocks.pumpkin, Blocks.sapling, Blocks.cobblestone_wall, Blocks.oak_fence);

    }

    @EventTarget
    public void onPre(EventPreMotion eventPreMotion) {
        long delay = delaySetting.getValueLong();

        if (mode.getValue().equalsIgnoreCase("OpenInv") && !(MC.currentScreen instanceof GuiInventory)) {
            return;
        }

        if (MC.currentScreen == null || MC.currentScreen instanceof GuiInventory || MC.currentScreen instanceof GuiChat) {
            if (timer.reachedTime(delay)) {

                if (!MC.thePlayer.inventoryContainer.getSlot(weaponSlot).getHasStack()) {
                    getBestWeapon(weaponSlot);

                } else {
                    if (!isBestWeapon(MC.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack())) {
                        getBestWeapon(weaponSlot);
                    }
                }
            }
            if (timer.reachedTime(delay) && pickaxeSlot >= 36) {
                getBestPickaxe(pickaxeSlot);
            }
            if (timer.reachedTime(delay) && shovelSlot >= 36) {
                getBestShovel(shovelSlot);
            }
            if (timer.reachedTime(delay) && axeSlot >= 36) {
                getBestAxe(axeSlot);
            }
            if (timer.reachedTime(delay) && cleanInv.getValue())
                for (int i = 9; i < 45; i++) {
                    if (MC.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                        ItemStack is = MC.thePlayer.inventoryContainer.getSlot(i).getStack();
                        if (shouldDrop(is, i)) {
                            drop(i);
                            timer.doReset();
                            if (delay > 0)
                                break;
                        }
                    }
                }
        }
    }

    public void shiftClick(int slot) {
        MC.playerController.windowClick(MC.thePlayer.inventoryContainer.windowId, slot, 0, 1, MC.thePlayer);
    }

    public void swap(int slot1, int hotbarSlot) {
        MC.playerController.windowClick(MC.thePlayer.inventoryContainer.windowId, slot1, hotbarSlot, 2, MC.thePlayer);
    }

    public void drop(int slot) {
        MC.playerController.windowClick(MC.thePlayer.inventoryContainer.windowId, slot, 1, 4, MC.thePlayer);
    }

    public boolean isBestWeapon(ItemStack stack) {
        float damage = getDamage(stack);
        for (int i = 9; i < 45; i++) {
            if (MC.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = MC.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getDamage(is) > damage && (is.getItem() instanceof ItemSword || !onlySwords.getValue()))
                    return false;
            }
        }
        if ((stack.getItem() instanceof ItemSword || !onlySwords.getValue())) {
            return true;
        } else {
            return false;
        }

    }

    public void getBestWeapon(int slot) {
        for (int i = 9; i < 45; i++) {
            if (MC.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = MC.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (isBestWeapon(is) && getDamage(is) > 0 && (is.getItem() instanceof ItemSword || !onlySwords.getValue())) {
                    swap(i, slot - 36);
                    timer.doReset();
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
            damage += tool.getDamage();
        }
        if (item instanceof ItemSword) {
            ItemSword sword = (ItemSword) item;
            damage += sword.getAttackDamage();
        }
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f +
                EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
        return damage;
    }

    public boolean shouldDrop(ItemStack stack, int slot) {
        if (stack.getDisplayName().toLowerCase().contains("(right click)")) {
            return false;
        }
        if (stack.getDisplayName().toLowerCase().contains("§k||")) {
            return false;
        }
        if ((slot == weaponSlot && isBestWeapon(MC.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack())) ||
                (slot == pickaxeSlot && isBestPickaxe(MC.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack()) && pickaxeSlot >= 0) ||
                (slot == axeSlot && isBestAxe(MC.thePlayer.inventoryContainer.getSlot(axeSlot).getStack()) && axeSlot >= 0) ||
                (slot == shovelSlot && isBestShovel(MC.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack()) && shovelSlot >= 0)) {
            return false;
        }
        if (stack.getItem() instanceof ItemArmor) {
            for (int type = 1; type < 5; type++) {
                if (MC.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
                    ItemStack is = MC.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
                    if (AutoArmor.isBestArmor(is, type)) {
                        continue;
                    }
                }
                if (AutoArmor.isBestArmor(stack, type)) {
                    return false;
                }
            }
        }
        if (stack.getItem() instanceof ItemBlock && (getBlockCount() > 122 || blacklistedBlocks.contains(((ItemBlock) stack.getItem()).getBlock()))) {
            return true;
        }
        if (stack.getItem() instanceof ItemPotion) {
            if (isBadPotion(stack)) {
                return true;
            }
        }
        if (stack.getItem() instanceof ItemFood && cleanFood.getValue() && !(stack.getItem() instanceof ItemAppleGold)) {
            return true;
        }
        if (stack.getItem() instanceof ItemHoe || stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemArmor) {
            return true;
        }
        if ((stack.getItem() instanceof ItemBow || stack.getItem().getUnlocalizedName().contains("arrow")) && bow.getValue()) {
            return true;
        }

        if (((stack.getItem().getUnlocalizedName().contains("tnt")) ||
                (stack.getItem().getUnlocalizedName().contains("stick")) ||
                (stack.getItem().getUnlocalizedName().contains("egg")) ||
                (stack.getItem().getUnlocalizedName().contains("string")) ||
                (stack.getItem().getUnlocalizedName().contains("cake")) ||
                (stack.getItem().getUnlocalizedName().contains("mushroom")) ||
                (stack.getItem().getUnlocalizedName().contains("flint")) ||
                (stack.getItem().getUnlocalizedName().contains("dyePowder")) ||
                (stack.getItem().getUnlocalizedName().contains("feather")) ||
                (stack.getItem().getUnlocalizedName().contains("bucket")) ||
                (stack.getItem().getUnlocalizedName().contains("chest") && !stack.getDisplayName().toLowerCase().contains("collect")) ||
                (stack.getItem().getUnlocalizedName().contains("snow")) ||
                (stack.getItem().getUnlocalizedName().contains("fish")) ||
                (stack.getItem().getUnlocalizedName().contains("enchant")) ||
                (stack.getItem().getUnlocalizedName().contains("exp")) ||
                (stack.getItem().getUnlocalizedName().contains("shears")) ||
                (stack.getItem().getUnlocalizedName().contains("anvil")) ||
                (stack.getItem().getUnlocalizedName().contains("torch")) ||
                (stack.getItem().getUnlocalizedName().contains("seeds")) ||
                (stack.getItem().getUnlocalizedName().contains("leather")) ||
                (stack.getItem().getUnlocalizedName().contains("reeds")) ||
                (stack.getItem().getUnlocalizedName().contains("skull")) ||
                (stack.getItem().getUnlocalizedName().contains("record")) ||
                (stack.getItem().getUnlocalizedName().contains("snowball")) ||
                (stack.getItem() instanceof ItemGlassBottle) ||
                (stack.getItem().getUnlocalizedName().contains("piston")))) {
            return true;
        }

        return false;
    }

    public ArrayList<Integer> getWhitelistedItem() {
        return whitelistedItems;
    }

    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; i++) {
            if (MC.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = MC.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock && !blacklistedBlocks.contains(((ItemBlock) item).getBlock())) {
                    blockCount += is.stackSize;
                }
            }
        }
        return blockCount;
    }

    private void getBestPickaxe(int slot) {
        for (int i = 9; i < 45; i++) {
            if (MC.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = MC.thePlayer.inventoryContainer.getSlot(i).getStack();

                if (isBestPickaxe(is) && pickaxeSlot != i) {
                    if (!isBestWeapon(is))
                        if (!MC.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getHasStack()) {
                            swap(i, pickaxeSlot - 36);
                            timer.doReset();
                            if (delaySetting.getValueLong() > 0)
                                return;
                        } else if (!isBestPickaxe(MC.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack())) {
                            swap(i, pickaxeSlot - 36);
                            timer.doReset();
                            if (delaySetting.getValueLong() > 0)
                                return;
                        }

                }
            }
        }
    }

    private void getBestShovel(int slot) {
        for (int i = 9; i < 45; i++) {
            if (MC.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = MC.thePlayer.inventoryContainer.getSlot(i).getStack();

                if (isBestShovel(is) && shovelSlot != i) {
                    if (!isBestWeapon(is))
                        if (!MC.thePlayer.inventoryContainer.getSlot(shovelSlot).getHasStack()) {
                            swap(i, shovelSlot - 36);
                            timer.doReset();
                            if (delaySetting.getValueLong() > 0)
                                return;
                        } else if (!isBestShovel(MC.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack())) {
                            swap(i, shovelSlot - 36);
                            timer.doReset();
                            if (delaySetting.getValueLong() > 0)
                                return;
                        }

                }
            }
        }
    }

    private void getBestAxe(int slot) {

        for (int i = 9; i < 45; i++) {
            if (MC.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = MC.thePlayer.inventoryContainer.getSlot(i).getStack();

                if (isBestAxe(is) && axeSlot != i) {
                    if (!isBestWeapon(is))
                        if (!MC.thePlayer.inventoryContainer.getSlot(axeSlot).getHasStack()) {
                            swap(i, axeSlot - 36);
                            timer.doReset();
                            if (delaySetting.getValueLong() > 0)
                                return;
                        } else if (!isBestAxe(MC.thePlayer.inventoryContainer.getSlot(axeSlot).getStack())) {
                            swap(i, axeSlot - 36);
                            timer.doReset();
                            if (delaySetting.getValueLong() > 0)
                                return;
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
            if (MC.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = MC.thePlayer.inventoryContainer.getSlot(i).getStack();
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
            if (MC.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = MC.thePlayer.inventoryContainer.getSlot(i).getStack();
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
            if (MC.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = MC.thePlayer.inventoryContainer.getSlot(i).getStack();
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
        float value = 1;
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
            if (potion.getEffects(stack) == null)
                return true;
            for (final Object o : potion.getEffects(stack)) {
                final PotionEffect effect = (PotionEffect) o;
                if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean invContainsType(int type) {

        for (int i = 9; i < 45; i++) {
            if (MC.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = MC.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if (item instanceof ItemArmor) {
                    ItemArmor armor = (ItemArmor) item;
                    if (type == armor.armorType) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void getBestArmor() {
        for (int type = 1; type < 5; type++) {
            if (MC.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
                ItemStack is = MC.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
                if (AutoArmor.isBestArmor(is, type)) {
                    continue;
                } else {
                    drop(4 + type);
                }
            }
            for (int i = 9; i < 45; i++) {
                if (MC.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                    ItemStack is = MC.thePlayer.inventoryContainer.getSlot(i).getStack();
                    if (AutoArmor.isBestArmor(is, type) && AutoArmor.getProtection(is) > 0) {
                        shiftClick(i);
                        timer.doReset();
                        if (delaySetting.getValueLong() > 0)
                            return;
                    }
                }
            }
        }
    }

    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {

    }
}
