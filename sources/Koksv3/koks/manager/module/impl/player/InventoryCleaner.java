package koks.manager.module.impl.player;

import koks.Koks;
import koks.api.util.TimeHelper;
import koks.manager.event.Event;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.api.settings.Setting;
import koks.manager.module.ModuleInfo;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 16:38
 */

@ModuleInfo(name = "InvCleaner", description = "Its sort your inventory for you", category = Module.Category.PLAYER)
public class InventoryCleaner extends Module {

    public List<Item> trashItems;
    public Setting openedInventory = new Setting("Opened Inventory", true, this);
    public Setting startDelay = new Setting("Start Delay", 250.0F, 0.0F, 500.0F, true, this);
    public Setting throwDelay = new Setting("Throw Delay", 100.0F, 0.0F, 150.0F, true, this);
    public Setting preferSword = new Setting("Prefer Sword", true, this);
    public Setting keepTools = new Setting("KeepTools", true, this);
    public Setting swordSlot = new Setting("Weapon Slot", 1.0F, 0.0F, 9.0F, true, this);
    public Setting bowSlot = new Setting("Bow Slot", 2.0F, 0.0F, 9.0F, true, this);
    public Setting pickSlot = new Setting("PickAxe Slot", 0.0F, 0.0F, 9.0F, true, this);
    public Setting axeSlot = new Setting("Axe Slot", 0.0F, 0.0F, 9.0F, true, this);
    public Setting shovelSlot = new Setting("Shovel Slot", 0.0F, 0.0F, 9.0F, true, this);
    private AutoArmor autoArmor;
    private final TimeHelper throwTimer = new TimeHelper();

    public InventoryCleaner() {
        trashItems = Arrays.asList(Items.feather, Items.dye, Items.paper, Items.saddle, Items.string, Items.banner, Items.fishing_rod);
    }

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventUpdate) {

            setInfo(Math.round(throwDelay.getCurrentValue()) + "");
            if (mc.currentScreen instanceof GuiInventory) {
                if (!timeHelper.hasReached((long) startDelay.getCurrentValue())) {
                    throwTimer.reset();
                    return;
                }
            } else {
                timeHelper.reset();
                if (openedInventory.isToggled())
                    return;
            }

            if (autoArmor.isToggled() && !autoArmor.isFinished()) {
                timeHelper.reset();
                throwTimer.reset();
                if (openedInventory.isToggled())
                    return;
            }

            for (int i = 9; i < 45; i++) {
                if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                    ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                    if (throwTimer.hasReached((long) (throwDelay.getCurrentValue() +  randomUtil.getRandomGaussian(20)))) {
                        if (swordSlot.getCurrentValue() != 0 && (is.getItem() instanceof ItemSword || is.getItem() instanceof ItemAxe || is.getItem() instanceof ItemPickaxe) && is == bestWeapon() && mc.thePlayer.inventoryContainer.getInventory().contains(bestWeapon()) && mc.thePlayer.inventoryContainer.getSlot((int) (35 + swordSlot.getCurrentValue())).getStack() != is && !preferSword.isToggled()) {
                            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, (int) (swordSlot.getCurrentValue() - 1), 2, mc.thePlayer);
                            throwTimer.reset();
                            break;
                        } else if (swordSlot.getCurrentValue() != 0 && is.getItem() instanceof ItemSword && is == bestSword() && mc.thePlayer.inventoryContainer.getInventory().contains(bestSword()) && mc.thePlayer.inventoryContainer.getSlot((int) (35 + swordSlot.getCurrentValue())).getStack() != is && preferSword.isToggled()) {
                            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, (int) (swordSlot.getCurrentValue() - 1), 2, mc.thePlayer);
                            throwTimer.reset();
                            break;
                        } else if (bowSlot.getCurrentValue() != 0 && is.getItem() instanceof ItemBow && is == bestBow() && mc.thePlayer.inventoryContainer.getInventory().contains(bestBow()) && mc.thePlayer.inventoryContainer.getSlot((int) (35 + bowSlot.getCurrentValue())).getStack() != is) {
                            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, (int) (bowSlot.getCurrentValue() - 1), 2, mc.thePlayer);
                            throwTimer.reset();
                            break;
                        } else if (pickSlot.getCurrentValue() != 0 && is.getItem() instanceof ItemPickaxe && is == bestPick() && is != bestWeapon() && keepTools.isToggled() && mc.thePlayer.inventoryContainer.getInventory().contains(bestPick()) && mc.thePlayer.inventoryContainer.getSlot((int) (35 + pickSlot.getCurrentValue())).getStack() != is) {
                            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, (int) (pickSlot.getCurrentValue() - 1), 2, mc.thePlayer);
                            throwTimer.reset();
                            break;
                        } else if (axeSlot.getCurrentValue() != 0 && is.getItem() instanceof ItemAxe && is == bestAxe() && is != bestWeapon() && keepTools.isToggled() && mc.thePlayer.inventoryContainer.getInventory().contains(bestAxe()) && mc.thePlayer.inventoryContainer.getSlot((int) (35 + axeSlot.getCurrentValue())).getStack() != is) {
                            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, (int) (axeSlot.getCurrentValue() - 1), 2, mc.thePlayer);
                            throwTimer.reset();
                            break;
                        } else if (shovelSlot.getCurrentValue() != 0 && is.getItem() instanceof ItemSpade && is == bestShovel() && is != bestWeapon() && keepTools.isToggled() && mc.thePlayer.inventoryContainer.getInventory().contains(bestShovel()) && mc.thePlayer.inventoryContainer.getSlot((int) (35 + shovelSlot.getCurrentValue())).getStack() != is) {
                            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, (int) (shovelSlot.getCurrentValue() - 1), 2, mc.thePlayer);
                            throwTimer.reset();
                            break;
                        } else if (trashItems.contains(is.getItem()) || isBadStack(is)) {
                            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 1, 4, mc.thePlayer);
                            throwTimer.reset();
                            break;
                        }
                    }
                }
            }
        }
    }

    public boolean isBadStack(ItemStack is) {
        if ((is.getItem() instanceof ItemSword) && is != bestWeapon() && !preferSword.isToggled())
            return true;
        if (is.getItem() instanceof ItemSword && is != bestSword() && preferSword.isToggled())
            return true;
        if (is.getItem() instanceof ItemBow && is != bestBow())
            return true;
        if (keepTools.isToggled()) {
            if (is.getItem() instanceof ItemAxe && is != bestAxe() && (preferSword.isToggled() || is != bestWeapon()))
                return true;
            if (is.getItem() instanceof ItemPickaxe && is != bestPick() && (preferSword.isToggled() || is != bestWeapon()))
                return true;
            if (is.getItem() instanceof ItemSpade && is != bestShovel())
                return true;
        } else {
            if (is.getItem() instanceof ItemAxe && (preferSword.isToggled() || is != bestWeapon()))
                return true;
            if (is.getItem() instanceof ItemPickaxe && (preferSword.isToggled() || is != bestWeapon()))
                return true;
            if (is.getItem() instanceof ItemSpade)
                return true;
        }
        return false;
    }

    public ItemStack bestWeapon() {
        ItemStack bestWeapon = null;
        float itemDamage = -1;

        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemSword || is.getItem() instanceof ItemAxe || is.getItem() instanceof ItemPickaxe) {
                    float toolDamage = getItemDamage(is);
                    if (toolDamage >= itemDamage) {
                        itemDamage = getItemDamage(is);
                        bestWeapon = is;
                    }
                }
            }
        }

        return bestWeapon;
    }

    public ItemStack bestSword() {
        ItemStack bestSword = null;
        float itemDamage = -1;

        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemSword) {
                    float swordDamage = getItemDamage(is);
                    if (swordDamage >= itemDamage) {
                        itemDamage = getItemDamage(is);
                        bestSword = is;
                    }
                }
            }
        }

        return bestSword;
    }

    public ItemStack bestBow() {
        ItemStack bestBow = null;
        float itemDamage = -1;

        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemBow) {
                    float bowDamage = getBowDamage(is);
                    if (bowDamage >= itemDamage) {
                        itemDamage = getBowDamage(is);
                        bestBow = is;
                    }
                }
            }
        }

        return bestBow;
    }

    public ItemStack bestAxe() {
        ItemStack bestTool = null;
        float itemSkill = -1;

        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemAxe) {
                    float toolSkill = getToolRating(is);
                    if (toolSkill >= itemSkill) {
                        itemSkill = getToolRating(is);
                        bestTool = is;
                    }
                }
            }
        }

        return bestTool;
    }

    public ItemStack bestPick() {
        ItemStack bestTool = null;
        float itemSkill = -1;

        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemPickaxe) {
                    float toolSkill = getToolRating(is);
                    if (toolSkill >= itemSkill) {
                        itemSkill = getToolRating(is);
                        bestTool = is;
                    }
                }
            }
        }

        return bestTool;
    }

    public ItemStack bestShovel() {
        ItemStack bestTool = null;
        float itemSkill = -1;

        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemSpade) {
                    float toolSkill = getToolRating(is);
                    if (toolSkill >= itemSkill) {
                        itemSkill = getToolRating(is);
                        bestTool = is;
                    }
                }
            }
        }

        return bestTool;
    }

    public float getToolRating(ItemStack itemStack) {
        float damage = getToolMaterialRating(itemStack, false);
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack) * 2.00F;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, itemStack) * 0.50F;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemStack) * 0.50F;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) * 0.10F;
        damage += (itemStack.getMaxDamage() - itemStack.getItemDamage()) * 0.000000000001F;
        return damage;
    }

    public float getItemDamage(ItemStack itemStack) {
        float damage = getToolMaterialRating(itemStack, true);
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25F;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) * 0.50F;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) * 0.01F;
        damage += (itemStack.getMaxDamage() - itemStack.getItemDamage()) * 0.000000000001F;

        if (itemStack.getItem() instanceof ItemSword)
            damage += 0.2;
        return damage;
    }

    public float getBowDamage(ItemStack itemStack) {
        float damage = 5;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemStack) * 1.25F;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, itemStack) * 0.75F;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, itemStack) * 0.50F;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) * 0.10F;
        damage += itemStack.getMaxDamage() - itemStack.getItemDamage() * 0.001F;
        return damage;
    }

    public float getToolMaterialRating(ItemStack itemStack, boolean checkForDamage) {
        Item is = itemStack.getItem();
        float rating = 0;

        if (is instanceof ItemSword) {
            switch (((ItemSword) is).getToolMaterialName()) {
                case "WOOD":
                    rating = 4;
                    break;
                case "GOLD":
                    rating = 4;
                    break;
                case "STONE":
                    rating = 5;
                    break;
                case "IRON":
                    rating = 6;
                    break;
                case "EMERALD":
                    rating = 7;
                    break;
            }
        } else if (is instanceof ItemPickaxe) {
            switch (((ItemPickaxe) is).getToolMaterialName()) {
                case "WOOD":
                    rating = 2;
                    break;
                case "GOLD":
                    rating = 2;
                    break;
                case "STONE":
                    rating = 3;
                    break;
                case "IRON":
                    rating = checkForDamage ? 4 : 40;
                    break;
                case "EMERALD":
                    rating = checkForDamage ? 5 : 50;
                    break;
            }
        } else if (is instanceof ItemAxe) {
            switch (((ItemAxe) is).getToolMaterialName()) {
                case "WOOD":
                    rating = 3;
                    break;
                case "GOLD":
                    rating = 3;
                    break;
                case "STONE":
                    rating = 4;
                    break;
                case "IRON":
                    rating = 5;
                    break;
                case "EMERALD":
                    rating = 6;
                    break;
            }
        } else if (is instanceof ItemSpade) {
            switch (((ItemSpade) is).getToolMaterialName()) {
                case "WOOD":
                    rating = 1;
                    break;
                case "GOLD":
                    rating = 1;
                    break;
                case "STONE":
                    rating = 2;
                    break;
                case "IRON":
                    rating = 3;
                    break;
                case "EMERALD":
                    rating = 4;
                    break;
            }
        }

        return rating;
    }

    @Override
    public void onEnable() {
        autoArmor = (AutoArmor) Koks.getKoks().moduleManager.getModule(AutoArmor.class);
    }

    @Override
    public void onDisable() {

    }

}