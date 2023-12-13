package koks.manager.module.impl.player;

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
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 16:37
 */

@ModuleInfo(name = "AutoArmor", description = "You put the armor automatically on", category = Module.Category.PLAYER)
public class AutoArmor extends Module {

    private final List<ItemArmor> helmet;
    private final List<ItemArmor> chest;
    private final List<ItemArmor> legging;
    private final List<ItemArmor> boot;

    public Setting openedInventory = new Setting("Opened Inventory", true, this);
    public Setting startDelay = new Setting("Start Delay", 250.0F, 0.0F, 500.0F, true, this);
    public Setting throwDelay = new Setting("Equip Delay", 100.0F, 0.0F, 150.0F, true, this);

    private final TimeHelper throwTimer = new TimeHelper();

    public AutoArmor() {
        helmet = Arrays.asList(Items.leather_helmet, Items.golden_helmet, Items.chainmail_helmet, Items.iron_helmet, Items.diamond_helmet);
        chest = Arrays.asList(Items.leather_chestplate, Items.golden_chestplate, Items.chainmail_chestplate, Items.iron_chestplate, Items.diamond_chestplate);
        legging = Arrays.asList(Items.leather_leggings, Items.golden_leggings, Items.chainmail_leggings, Items.iron_leggings, Items.diamond_leggings);
        boot = Arrays.asList(Items.leather_boots, Items.golden_boots, Items.chainmail_boots, Items.iron_boots, Items.diamond_boots);
    }

    public boolean isFinished() {
        if (mc.thePlayer.inventoryContainer.getInventory().contains(bestHelmet()) && mc.thePlayer.inventoryContainer.getSlot(5).getStack() != bestHelmet())
            return false;
        if (mc.thePlayer.inventoryContainer.getInventory().contains(bestChestplate()) && mc.thePlayer.inventoryContainer.getSlot(6).getStack() != bestChestplate())
            return false;
        if (mc.thePlayer.inventoryContainer.getInventory().contains(bestLeggings()) && mc.thePlayer.inventoryContainer.getSlot(7).getStack() != bestLeggings())
            return false;
        if (mc.thePlayer.inventoryContainer.getInventory().contains(bestBoots()) && mc.thePlayer.inventoryContainer.getSlot(8).getStack() != bestBoots())
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

            for (int i = 5; i < 45; i++) {
                if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                    ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                    if (throwTimer.hasReached((long) (throwDelay.getCurrentValue() + randomUtil.getRandomGaussian(20)))) {
                        if (is.getItem() instanceof ItemArmor && isTrashArmor(is)) {
                            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 1, 4, mc.thePlayer);
                            throwTimer.reset();
                            break;
                        } else if (is.getItem() instanceof ItemArmor && helmet.contains(is.getItem()) && is == bestHelmet() && !mc.thePlayer.inventoryContainer.getSlot(5).getHasStack()) {
                            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 0, 1, mc.thePlayer);
                            throwTimer.reset();
                            break;
                        } else if (is.getItem() instanceof ItemArmor && chest.contains(is.getItem()) && is == bestChestplate() && !mc.thePlayer.inventoryContainer.getSlot(6).getHasStack()) {
                            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 0, 1, mc.thePlayer);
                            throwTimer.reset();
                            break;
                        } else if (is.getItem() instanceof ItemArmor && legging.contains(is.getItem()) && is == bestLeggings() && !mc.thePlayer.inventoryContainer.getSlot(7).getHasStack()) {
                            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 0, 1, mc.thePlayer);
                            throwTimer.reset();
                            break;
                        } else if (is.getItem() instanceof ItemArmor && boot.contains(is.getItem()) && is == bestBoots() && !mc.thePlayer.inventoryContainer.getSlot(8).getHasStack()) {
                            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 0, 1, mc.thePlayer);
                            throwTimer.reset();
                            break;
                        }
                    }
                }
            }
        }
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
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
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
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
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
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
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
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
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
            switch (((ItemArmor) itemStack.getItem()).getArmorMaterial()) {
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

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}