package koks.module.player;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;
import koks.api.utils.RandomUtil;
import koks.api.utils.TimeHelper;
import koks.api.manager.value.annotation.Value;
import koks.event.GuiHandleEvent;
import koks.event.UpdateEvent;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.*;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "InventoryCleaner", description = "Its sort your inventory for you", category = Module.Category.PLAYER)
public class InventoryCleaner extends Module {

    private final List<Item> trashItems;
    private AutoArmor autoArmor;
    private final TimeHelper timeHelper = new TimeHelper();
    private final TimeHelper throwTimer = new TimeHelper();

    @Value(name = "OpenedInventory")
    boolean openedInventory = true;

    @Value(name = "Start Delay", maximum = 500, minimum = 0)
    int startDelay = 250;

    @Value(name = "Throw Delay", maximum = 500, minimum = 0)
    int throwDelay = 100;

    @Value(name = "PreferSword")
    boolean preferSword = true;

    @Value(name = "KeepTools")
    boolean keepTools = true;

    @Value(name = "Weapon Slot", maximum = 9, minimum = 0)
    int swordSlot = 1;

    @Value(name = "Bow Slot", maximum = 9, minimum = 0)
    int bowSlot = 2;

    @Value(name = "PickAxe Slot", maximum = 9, minimum = 0)
    int pickSlot = 0;

    @Value(name = "Axe Slot", maximum = 9, minimum = 0)
    int axeSlot = 0;

    @Value(name = "Shovel Slot", maximum = 9, minimum = 0)
    int shovelSlot = 0;

    public InventoryCleaner() {
        trashItems = Arrays.asList(Items.dye, Items.paper, Items.saddle, Items.string, Items.banner, Items.fishing_rod);
    }

    @Override
    @Event.Info
    public void onEvent(Event event) {
        final RandomUtil randomUtil = RandomUtil.getInstance();

        if (event instanceof GuiHandleEvent) {
            if (mc.currentScreen instanceof GuiInventory) {
                if (!timeHelper.hasReached((long) (startDelay + randomUtil.getRandomGaussian(20)))) {
                    throwTimer.reset();
                    return;
                }
            } else {
                timeHelper.reset();
                if (openedInventory)
                    return;
            }

            if (autoArmor.isToggled() && !autoArmor.isFinished()) {
                timeHelper.reset();
                throwTimer.reset();
                if (openedInventory)
                    return;
            }

            for (int i = 9; i < 45; i++) {
                if (getPlayer().inventoryContainer.getSlot(i).getHasStack()) {
                    final ItemStack is = getPlayer().inventoryContainer.getSlot(i).getStack();
                    double random = this.throwDelay == 0 ? 0 : RandomUtil.getInstance().getRandomGaussian(20);
                    if (throwTimer.hasReached((long) (throwDelay + random))) {
                        if (swordSlot != 0 && (is.getItem() instanceof ItemSword || is.getItem() instanceof ItemAxe || is.getItem() instanceof ItemPickaxe) && is == bestWeapon() && mc.thePlayer.inventoryContainer.getInventory().contains(bestWeapon()) && mc.thePlayer.inventoryContainer.getSlot((int) (35 + swordSlot)).getStack() != is && !preferSword) {
                            getPlayerController().windowClick(mc.thePlayer.inventoryContainer.windowId, i, (int) (swordSlot - 1), 2, mc.thePlayer);
                            throwTimer.reset();
                            if (this.throwDelay != 0) {
                                break;
                            }
                        } else if (swordSlot != 0 && is.getItem() instanceof ItemSword && is == bestSword() && mc.thePlayer.inventoryContainer.getInventory().contains(bestSword()) && mc.thePlayer.inventoryContainer.getSlot((int) (35 + swordSlot)).getStack() != is && preferSword) {
                            getPlayerController().windowClick(mc.thePlayer.inventoryContainer.windowId, i, (int) (swordSlot - 1), 2, mc.thePlayer);
                            throwTimer.reset();
                            if (this.throwDelay != 0) {
                                break;
                            }
                        } else if (bowSlot != 0 && is.getItem() instanceof ItemBow && is == bestBow() && mc.thePlayer.inventoryContainer.getInventory().contains(bestBow()) && mc.thePlayer.inventoryContainer.getSlot((int) (35 + bowSlot)).getStack() != is) {
                            getPlayerController().windowClick(mc.thePlayer.inventoryContainer.windowId, i, (int) (bowSlot - 1), 2, mc.thePlayer);
                            throwTimer.reset();
                            if (this.throwDelay != 0) {
                                break;
                            }
                        } else if (pickSlot != 0 && is.getItem() instanceof ItemPickaxe && is == bestPick() && is != bestWeapon() && keepTools && mc.thePlayer.inventoryContainer.getInventory().contains(bestPick()) && mc.thePlayer.inventoryContainer.getSlot((int) (35 + pickSlot)).getStack() != is) {
                            getPlayerController().windowClick(mc.thePlayer.inventoryContainer.windowId, i, (int) (pickSlot - 1), 2, mc.thePlayer);
                            throwTimer.reset();
                            if (this.throwDelay != 0) {
                                break;
                            }
                        } else if (axeSlot != 0 && is.getItem() instanceof ItemAxe && is == bestAxe() && is != bestWeapon() && keepTools && mc.thePlayer.inventoryContainer.getInventory().contains(bestAxe()) && mc.thePlayer.inventoryContainer.getSlot((int) (35 + axeSlot)).getStack() != is) {
                            getPlayerController().windowClick(mc.thePlayer.inventoryContainer.windowId, i, (int) (axeSlot - 1), 2, mc.thePlayer);
                            throwTimer.reset();
                            if (this.throwDelay != 0) {
                                break;
                            }
                        } else if (shovelSlot != 0 && is.getItem() instanceof ItemSpade && is == bestShovel() && is != bestWeapon() && keepTools && mc.thePlayer.inventoryContainer.getInventory().contains(bestShovel()) && mc.thePlayer.inventoryContainer.getSlot((int) (35 + shovelSlot)).getStack() != is) {
                            getPlayerController().windowClick(mc.thePlayer.inventoryContainer.windowId, i, (int) (shovelSlot - 1), 2, mc.thePlayer);
                            throwTimer.reset();
                            if (this.throwDelay != 0) {
                                break;
                            }
                        } else if (trashItems.contains(is.getItem()) || isBadStack(is)) {
                            getPlayerController().windowClick(mc.thePlayer.inventoryContainer.windowId, i, 1, 4, mc.thePlayer);
                            throwTimer.reset();
                            if (this.throwDelay != 0) {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onEnable() {
        autoArmor = ModuleRegistry.getModule(AutoArmor.class);
    }

    @Override
    public void onDisable() {

    }

    public boolean isBadStack(ItemStack is) {
        if ((is.getItem() instanceof ItemSword) && is != bestWeapon() && !preferSword)
            return true;
        if (is.getItem() instanceof ItemSword && is != bestSword() && preferSword)
            return true;
        if (is.getItem() instanceof ItemBow && is != bestBow())
            return true;
        if (keepTools) {
            if (is.getItem() instanceof ItemAxe && is != bestAxe() && (preferSword || is != bestWeapon()))
                return true;
            if (is.getItem() instanceof ItemPickaxe && is != bestPick() && (preferSword || is != bestWeapon()))
                return true;
            if (is.getItem() instanceof ItemSpade && is != bestShovel())
                return true;
        } else {
            if (is.getItem() instanceof ItemAxe && (preferSword || is != bestWeapon()))
                return true;
            if (is.getItem() instanceof ItemPickaxe && (preferSword || is != bestWeapon()))
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
            if (getPlayer().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = getPlayer().inventoryContainer.getSlot(i).getStack();
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
            if (getPlayer().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = getPlayer().inventoryContainer.getSlot(i).getStack();
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
            if (getPlayer().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = getPlayer().inventoryContainer.getSlot(i).getStack();
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
            if (getPlayer().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = getPlayer().inventoryContainer.getSlot(i).getStack();
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
            if (getPlayer().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = getPlayer().inventoryContainer.getSlot(i).getStack();
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
            if (getPlayer().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = getPlayer().inventoryContainer.getSlot(i).getStack();
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
        final Item is = itemStack.getItem();
        float rating = 0;

        if (is instanceof ItemSword) {
            switch (((ItemSword) is).getToolMaterialName()) {
                case "WOOD", "GOLD" -> rating = 4;
                case "STONE" -> rating = 5;
                case "IRON" -> rating = 6;
                case "EMERALD" -> rating = 7;
            }
        } else if (is instanceof ItemPickaxe) {
            rating = switch (((ItemPickaxe) is).getToolMaterialName()) {
                case "WOOD", "GOLD" -> 2;
                case "STONE" -> 3;
                case "IRON" -> checkForDamage ? 4 : 40;
                case "EMERALD" -> checkForDamage ? 5 : 50;
                default -> rating;
            };
        } else if (is instanceof ItemAxe) {
            rating = switch (((ItemAxe) is).getToolMaterialName()) {
                case "WOOD", "GOLD" -> 3;
                case "STONE" -> 4;
                case "IRON" -> 5;
                case "EMERALD" -> 6;
                default -> rating;
            };
        } else if (is instanceof ItemSpade) {
            rating = switch (((ItemSpade) is).getToolMaterialName()) {
                case "WOOD", "GOLD" -> 1;
                case "STONE" -> 2;
                case "IRON" -> 3;
                case "EMERALD" -> 4;
                default -> rating;
            };
        }

        return rating;
    }
}
