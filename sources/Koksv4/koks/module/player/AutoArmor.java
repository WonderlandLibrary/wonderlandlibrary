package koks.module.player;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.utils.RandomUtil;
import koks.api.utils.TimeHelper;
import koks.api.manager.value.annotation.Value;
import koks.event.GuiHandleEvent;
import koks.event.UpdateEvent;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "AutoArmor", description = "You put the armor automatically on", category = Module.Category.PLAYER)
public class AutoArmor extends Module {

    private final List<ItemArmor> helmet;
    private final List<ItemArmor> chest;
    private final List<ItemArmor> legging;
    private final List<ItemArmor> boot;

    private final TimeHelper timeHelper = new TimeHelper();
    private final TimeHelper throwTimer = new TimeHelper();

    @Value(name = "Opened Inventory")
    boolean openedInventory = true;

    @Value(name = "Start Delay", minimum = 0, maximum = 500)
    int startDelay = 250;

    @Value(name = "Throw Delay", minimum = 0, maximum = 500)
    int throwDelay = 100;

    public AutoArmor() {
        helmet = Arrays.asList(Items.leather_helmet, Items.golden_helmet, Items.chainmail_helmet, Items.iron_helmet, Items.diamond_helmet);
        chest = Arrays.asList(Items.leather_chestplate, Items.golden_chestplate, Items.chainmail_chestplate, Items.iron_chestplate, Items.diamond_chestplate);
        legging = Arrays.asList(Items.leather_leggings, Items.golden_leggings, Items.chainmail_leggings, Items.iron_leggings, Items.diamond_leggings);
        boot = Arrays.asList(Items.leather_boots, Items.golden_boots, Items.chainmail_boots, Items.iron_boots, Items.diamond_boots);
    }

    public boolean isFinished() {
        if (getPlayer().inventoryContainer.getInventory().contains(bestHelmet()) && getPlayer().inventoryContainer.getSlot(5).getStack() != bestHelmet())
            return false;
        if (getPlayer().inventoryContainer.getInventory().contains(bestChestplate()) && getPlayer().inventoryContainer.getSlot(6).getStack() != bestChestplate())
            return false;
        if (getPlayer().inventoryContainer.getInventory().contains(bestLeggings()) && getPlayer().inventoryContainer.getSlot(7).getStack() != bestLeggings())
            return false;
        if (getPlayer().inventoryContainer.getInventory().contains(bestBoots()) && getPlayer().inventoryContainer.getSlot(8).getStack() != bestBoots())
            return false;
        return true;
    }

    /*
        5 = Helmet
        6 = ChestPlate
        7 = Leggings
        8 = Boots
     */

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

            for (int i = 5; i < 45; i++) {
                if (getPlayer().inventoryContainer.getSlot(i).getHasStack()) {
                    final ItemStack is = getPlayer().inventoryContainer.getSlot(i).getStack();
                    double random = this.throwDelay == 0 ? 0 : RandomUtil.getInstance().getRandomGaussian(20);
                    if (throwTimer.hasReached((long) (throwDelay + random))) {
                        if (is.getItem() instanceof ItemArmor && isTrashArmor(is)) {
                            getPlayerController().windowClick(getPlayer().inventoryContainer.windowId, i, 1, 4, getPlayer());
                            throwTimer.reset();
                            if (this.throwDelay != 0) {
                                break;
                            }
                        } else if (is.getItem() instanceof ItemArmor && helmet.contains(is.getItem()) && is == bestHelmet() && !getPlayer().inventoryContainer.getSlot(5).getHasStack()) {
                            getPlayerController().windowClick(getPlayer().inventoryContainer.windowId, i, 0, 1, getPlayer());
                            throwTimer.reset();
                            if (this.throwDelay != 0) {
                                break;
                            }
                        } else if (is.getItem() instanceof ItemArmor && chest.contains(is.getItem()) && is == bestChestplate() && !getPlayer().inventoryContainer.getSlot(6).getHasStack()) {
                            getPlayerController().windowClick(getPlayer().inventoryContainer.windowId, i, 0, 1, getPlayer());
                            throwTimer.reset();
                            if (this.throwDelay != 0) {
                                break;
                            }
                        } else if (is.getItem() instanceof ItemArmor && legging.contains(is.getItem()) && is == bestLeggings() && !getPlayer().inventoryContainer.getSlot(7).getHasStack()) {
                            getPlayerController().windowClick(getPlayer().inventoryContainer.windowId, i, 0, 1, getPlayer());
                            throwTimer.reset();
                            if (this.throwDelay != 0) {
                                break;
                            }
                        } else if (is.getItem() instanceof ItemArmor && boot.contains(is.getItem()) && is == bestBoots() && !getPlayer().inventoryContainer.getSlot(8).getHasStack()) {
                            getPlayerController().windowClick(getPlayer().inventoryContainer.windowId, i, 0, 1, getPlayer());
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

    }

    @Override
    public void onDisable() {

    }

    public boolean isTrashArmor(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemArmor && helmet.contains(itemStack.getItem()) && itemStack != bestHelmet() && bestHelmet() != null)
            return true;
        if (itemStack.getItem() instanceof ItemArmor && chest.contains(itemStack.getItem()) && itemStack != bestChestplate() && bestChestplate() != null)
            return true;
        if (itemStack.getItem() instanceof ItemArmor && legging.contains(itemStack.getItem()) && itemStack != bestLeggings() && bestLeggings() != null)
            return true;
        if (itemStack.getItem() instanceof ItemArmor && boot.contains(itemStack.getItem()) && itemStack != bestBoots() && bestBoots() != null)
            return true;
        return false;
    }

    public ItemStack bestHelmet() {
        ItemStack bestArmor = null;
        float armorSkill = -1;

        for (int i = 5; i < 45; i++) {
            if (getPlayer().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = getPlayer().inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemArmor && helmet.contains(is.getItem())) {
                    float armorStrength = getFinalArmorStrength(is);
                    if (armorStrength >= armorSkill) {
                        armorSkill = getFinalArmorStrength(is);
                        bestArmor = is;
                    }
                }
            }
        }
        return bestArmor;
    }

    public ItemStack bestChestplate() {
        ItemStack bestArmor = null;
        float armorSkill = -1;

        for (int i = 5; i < 45; i++) {
            if (getPlayer().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = getPlayer().inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemArmor && chest.contains(is.getItem())) {
                    float armorStrength = getFinalArmorStrength(is);
                    if (armorStrength >= armorSkill) {
                        armorSkill = getFinalArmorStrength(is);
                        bestArmor = is;
                    }
                }
            }
        }

        return bestArmor;
    }

    public ItemStack bestLeggings() {
        ItemStack bestArmor = null;
        float armorSkill = -1;

        for (int i = 5; i < 45; i++) {
            if (getPlayer().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = getPlayer().inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemArmor && legging.contains(is.getItem())) {
                    float armorStrength = getFinalArmorStrength(is);
                    if (armorStrength >= armorSkill) {
                        armorSkill = getFinalArmorStrength(is);
                        bestArmor = is;
                    }
                }
            }
        }

        return bestArmor;
    }

    public ItemStack bestBoots() {
        ItemStack bestArmor = null;
        float armorSkill = -1;

        for (int i = 5; i < 45; i++) {
            if (getPlayer().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = getPlayer().inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemArmor && boot.contains(is.getItem())) {
                    float armorStrength = getFinalArmorStrength(is);
                    if (armorStrength >= armorSkill) {
                        armorSkill = getFinalArmorStrength(is);
                        bestArmor = is;
                    }
                }
            }
        }

        return bestArmor;
    }

    public float getFinalArmorStrength(ItemStack itemStack) {
        float damage = getArmorRating(itemStack);
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, itemStack) * 1.25F;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, itemStack) * 1.20F;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, itemStack) * 1.20F;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, itemStack) * 1.20F;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, itemStack) * 0.33F;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, itemStack) * 0.10F;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) * 0.05F;
        return damage;
    }

    public float getArmorRating(ItemStack itemStack) {
        float rating = 0;

        if (itemStack.getItem() instanceof ItemArmor) {
            final ItemArmor armor = (ItemArmor) itemStack.getItem();
            switch (armor.getArmorMaterial()) {
                case LEATHER:
                    rating = 1;
                    break;
                case GOLD:
                    rating = 2;
                    break;
                case CHAIN:
                    rating = 3;
                    break;
                case IRON:
                    rating = 4;
                    break;
                case DIAMOND:
                    rating = 5;
                    break;
            }
        }
        return rating;
    }
}
