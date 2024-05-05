package tech.atani.client.feature.module.impl.player;

import cn.muyang.nativeobfuscator.Native;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import tech.atani.client.listener.event.minecraft.input.GuiHandleEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.utility.math.random.RandomUtil;
import tech.atani.client.utility.math.time.TimeHelper;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.value.impl.SliderValue;

import java.util.Arrays;
import java.util.List;

@Native
@ModuleData(category = Category.PLAYER, description = "Automatically equips best armor", name = "AutoArmor")
public class AutoArmor extends Module {
    private final CheckBoxValue openInventory = new CheckBoxValue("Open Inventory", "Operate only with open inventory?", this, true);
    private final SliderValue<Long> minStartDelay = new SliderValue<Long>("Minimum Start Delay", "What will be the minimum delay for starting to operate?", this, 250L, 0L, 1000L, 0),
            maxStartDelay = new SliderValue<Long>("Maximum Start Delay", "What will be the maximum delay for starting to operate?", this, 300L, 0L, 1000L, 0),
            minThrowDelay = new SliderValue<Long>("Minimum Throw Delay", "What will be the minimum delay between throwing each item?", this, 250L, 0L, 1000L, 0),
            maxThrowDelay = new SliderValue<Long>("Maximum Throw Delay", "What will be the maximum delay between throwing each item?", this, 300L, 0L, 1000L, 0);

    private final List<ItemArmor> helmet = Arrays.asList(Items.leather_helmet, Items.golden_helmet, Items.chainmail_helmet, Items.iron_helmet, Items.diamond_helmet);
    private final List<ItemArmor> chest = Arrays.asList(Items.leather_chestplate, Items.golden_chestplate, Items.chainmail_chestplate, Items.iron_chestplate, Items.diamond_chestplate);
    private final List<ItemArmor> legging = Arrays.asList(Items.leather_leggings, Items.golden_leggings, Items.chainmail_leggings, Items.iron_leggings, Items.diamond_leggings);
    private final List<ItemArmor> boot = Arrays.asList(Items.leather_boots, Items.golden_boots, Items.chainmail_boots, Items.iron_boots, Items.diamond_boots);

    private final TimeHelper timeHelper = new TimeHelper();
    private final TimeHelper throwTimer = new TimeHelper();

    @Listen
    public void onGui(GuiHandleEvent guiHandleEvent) {
        if (Methods.mc.currentScreen instanceof GuiInventory) {
            if (!timeHelper.hasReached((long) (RandomUtil.randomBetween(this.minStartDelay.getValue(), this.maxStartDelay.getValue())))) {
                throwTimer.reset();
                return;
            }
        } else {
            timeHelper.reset();
            if (openInventory.getValue())
                return;
        }

        for (int i = 5; i < 45; i++) {
            if (getPlayer() != null && getPlayer().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = getPlayer().inventoryContainer.getSlot(i).getStack();
                long throwDelay = (long) RandomUtil.randomBetween(this.minThrowDelay.getValue(), this.maxThrowDelay.getValue());
                if (throwTimer.hasReached((long) (throwDelay))) {
                    if (is.getItem() instanceof ItemArmor && isTrashArmor(is)) {
                        getPlayerController().windowClick(getPlayer().inventoryContainer.windowId, i, 1, 4, getPlayer());
                        throwTimer.reset();
                        if (throwDelay != 0) {
                            break;
                        }
                    } else if (is.getItem() instanceof ItemArmor && helmet.contains(is.getItem()) && is == bestHelmet() && !getPlayer().inventoryContainer.getSlot(5).getHasStack()) {
                        getPlayerController().windowClick(getPlayer().inventoryContainer.windowId, i, 0, 1, getPlayer());
                        throwTimer.reset();
                        if (throwDelay != 0) {
                            break;
                        }
                    } else if (is.getItem() instanceof ItemArmor && chest.contains(is.getItem()) && is == bestChestplate() && !getPlayer().inventoryContainer.getSlot(6).getHasStack()) {
                        getPlayerController().windowClick(getPlayer().inventoryContainer.windowId, i, 0, 1, getPlayer());
                        throwTimer.reset();
                        if (throwDelay != 0) {
                            break;
                        }
                    } else if (is.getItem() instanceof ItemArmor && legging.contains(is.getItem()) && is == bestLeggings() && !getPlayer().inventoryContainer.getSlot(7).getHasStack()) {
                        getPlayerController().windowClick(getPlayer().inventoryContainer.windowId, i, 0, 1, getPlayer());
                        throwTimer.reset();
                        if (throwDelay != 0) {
                            break;
                        }
                    } else if (is.getItem() instanceof ItemArmor && boot.contains(is.getItem()) && is == bestBoots() && !getPlayer().inventoryContainer.getSlot(8).getHasStack()) {
                        getPlayerController().windowClick(getPlayer().inventoryContainer.windowId, i, 0, 1, getPlayer());
                        throwTimer.reset();
                        if (throwDelay != 0) {
                            break;
                        }
                    }
                }
            }
        }
    }

    public boolean isFinished() {
        if (getPlayer().inventoryContainer.getInventory().contains(bestHelmet()) && getPlayer().inventoryContainer.getSlot(5).getStack() != bestHelmet())
            return false;
        if (getPlayer().inventoryContainer.getInventory().contains(bestChestplate()) && getPlayer().inventoryContainer.getSlot(6).getStack() != bestChestplate())
            return false;
        if (getPlayer().inventoryContainer.getInventory().contains(bestLeggings()) && getPlayer().inventoryContainer.getSlot(7).getStack() != bestLeggings())
            return false;
        return !getPlayer().inventoryContainer.getInventory().contains(bestBoots()) || getPlayer().inventoryContainer.getSlot(8).getStack() == bestBoots();
    }

    public boolean isTrashArmor(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemArmor && helmet.contains(itemStack.getItem()) && itemStack != bestHelmet() && bestHelmet() != null)
            return true;
        if (itemStack.getItem() instanceof ItemArmor && chest.contains(itemStack.getItem()) && itemStack != bestChestplate() && bestChestplate() != null)
            return true;
        if (itemStack.getItem() instanceof ItemArmor && legging.contains(itemStack.getItem()) && itemStack != bestLeggings() && bestLeggings() != null)
            return true;
        return itemStack.getItem() instanceof ItemArmor && boot.contains(itemStack.getItem()) && itemStack != bestBoots() && bestBoots() != null;
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

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}
}