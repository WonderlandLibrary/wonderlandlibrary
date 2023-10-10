// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.player;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.Slot;
import java.io.IOException;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import net.lenni0451.eventapi.reflection.EventTarget;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import net.augustus.utils.RandomUtil;
import net.minecraft.client.gui.inventory.GuiChest;
import net.augustus.events.EventEarlyTick;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.utils.TimeHelper;
import net.augustus.modules.Module;

public class ChestStealer extends Module
{
    private final TimeHelper delayTimeHelper;
    private final TimeHelper startDelayTimeHelper;
    public DoubleValue startDelay;
    public DoubleValue delay;
    public BooleanValue random;
    public BooleanValue intelligent;
    public BooleanValue autoClose;
    public BooleanValue spamClick;
    private int lastItemPos;
    
    public ChestStealer() {
        super("ChestStealer", new Color(196, 184, 249, 255), Categorys.PLAYER);
        this.delayTimeHelper = new TimeHelper();
        this.startDelayTimeHelper = new TimeHelper();
        this.startDelay = new DoubleValue(1, "StartDelay", this, 200.0, 0.0, 1000.0, 0);
        this.delay = new DoubleValue(2, "Delay", this, 100.0, 0.0, 400.0, 0);
        this.random = new BooleanValue(3, "Random", this, false);
        this.intelligent = new BooleanValue(4, "Intelligent", this, true);
        this.autoClose = new BooleanValue(4, "AutoClose", this, true);
        this.spamClick = new BooleanValue(5, "SpamClick", this, true);
        this.lastItemPos = Integer.MIN_VALUE;
    }
    
    @EventTarget
    public void onEventTick(final EventEarlyTick eventEarlyTick) {
        if (ChestStealer.mc.currentScreen instanceof GuiChest) {
            if (this.startDelayTimeHelper.reached((long)(this.startDelay.getValue() + RandomUtil.nextLong(-35L, 35L)))) {
                final ArrayList<Integer> itemPos = new ArrayList<Integer>();
                final GuiChest chest = (GuiChest)ChestStealer.mc.currentScreen;
                for (int i = 0; i < chest.inventorySlots.inventorySlots.size() - 36; ++i) {
                    final ItemStack itemStack = chest.inventorySlots.getSlot(i).getStack();
                    if (itemStack != null) {
                        if (this.intelligent.getBoolean()) {
                            if (this.isBestChestItem(itemStack) && this.isBestItem(itemStack)) {
                                itemPos.add(i);
                            }
                        }
                        else {
                            itemPos.add(i);
                        }
                    }
                }
                if (this.random.getBoolean()) {
                    Collections.shuffle(itemPos);
                }
                if (this.delayTimeHelper.reached((long)(this.delay.getValue() + RandomUtil.nextLong(-35L, 55L)))) {
                    boolean b = false;
                    for (final Integer integer : itemPos) {
                        this.stealItem(integer);
                        this.lastItemPos = integer;
                        b = true;
                        if (this.delay.getValue() != 0.0) {
                            break;
                        }
                    }
                    if (!b && this.autoClose.getBoolean()) {
                        this.startDelayTimeHelper.reset();
                        ChestStealer.mc.thePlayer.closeScreen();
                    }
                }
                else if (this.lastItemPos != Integer.MIN_VALUE && this.spamClick.getBoolean()) {
                    ChestStealer.mc.playerController.windowClick(chest.inventorySlots.windowId, this.lastItemPos, 0, 1, ChestStealer.mc.thePlayer);
                    System.out.println("GG");
                }
            }
        }
        else {
            this.startDelayTimeHelper.reset();
            this.lastItemPos = Integer.MIN_VALUE;
        }
    }
    
    public boolean isBestChestItem(final ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemBow || itemStack.getItem() instanceof ItemArmor || itemStack.getItem() instanceof ItemAxe || itemStack.getItem() instanceof ItemPickaxe || itemStack.getItem() instanceof ItemSpade || itemStack.getItem() instanceof ItemFishingRod) {
            final ItemStack bestItem = null;
            final GuiChest chest = (GuiChest)ChestStealer.mc.currentScreen;
            for (int i = 0; i < chest.inventorySlots.inventorySlots.size() - 36; ++i) {
                final ItemStack chestItem = chest.inventorySlots.getSlot(i).getStack();
                if (chestItem != null) {
                    if (itemStack.getItem() instanceof ItemSword && chestItem.getItem() instanceof ItemSword) {
                        if (this.getDamageSword(itemStack) < this.getDamageSword(chestItem)) {
                            return false;
                        }
                    }
                    else if (itemStack.getItem() instanceof ItemBow && chestItem.getItem() instanceof ItemBow) {
                        if (this.getDamageBow(itemStack) < this.getDamageBow(chestItem)) {
                            return false;
                        }
                    }
                    else if (itemStack.getItem() instanceof ItemArmor && chestItem.getItem() instanceof ItemArmor) {
                        if (((ItemArmor)itemStack.getItem()).armorType == ((ItemArmor)chestItem.getItem()).armorType && this.getDamageReduceAmount(itemStack) < this.getDamageReduceAmount(chestItem)) {
                            return false;
                        }
                    }
                    else if (itemStack.getItem() instanceof ItemFishingRod && chestItem.getItem() instanceof ItemFishingRod) {
                        if (this.getBestRod(itemStack) < this.getBestRod(chestItem)) {
                            return false;
                        }
                    }
                    else if (itemStack.getItem() instanceof ItemTool && chestItem.getItem() instanceof ItemTool && this.getToolSpeed(itemStack) < this.getToolSpeed(chestItem)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    public boolean isBestItem(final ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemBow || itemStack.getItem() instanceof ItemArmor || itemStack.getItem() instanceof ItemAxe || itemStack.getItem() instanceof ItemPickaxe || itemStack.getItem() instanceof ItemSpade || itemStack.getItem() instanceof ItemFishingRod) {
            for (int i = 0; i < ChestStealer.mc.thePlayer.inventoryContainer.inventorySlots.size(); ++i) {
                final ItemStack inventoryStack = ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (inventoryStack != null) {
                    if (itemStack.getItem() instanceof ItemSword && inventoryStack.getItem() instanceof ItemSword) {
                        if (this.getDamageSword(itemStack) <= this.getDamageSword(inventoryStack)) {
                            return false;
                        }
                    }
                    else if (itemStack.getItem() instanceof ItemBow && inventoryStack.getItem() instanceof ItemBow) {
                        if (this.getDamageBow(itemStack) <= this.getDamageBow(inventoryStack)) {
                            return false;
                        }
                    }
                    else if (itemStack.getItem() instanceof ItemArmor && inventoryStack.getItem() instanceof ItemArmor) {
                        if (((ItemArmor)itemStack.getItem()).armorType == ((ItemArmor)inventoryStack.getItem()).armorType && this.getDamageReduceAmount(itemStack) <= this.getDamageReduceAmount(inventoryStack)) {
                            return false;
                        }
                    }
                    else if (itemStack.getItem() instanceof ItemFishingRod && inventoryStack.getItem() instanceof ItemFishingRod) {
                        if (this.getBestRod(itemStack) <= this.getBestRod(inventoryStack)) {
                            return false;
                        }
                    }
                    else if (itemStack.getItem() instanceof ItemTool && inventoryStack.getItem() instanceof ItemTool && this.getToolSpeed(itemStack) <= this.getToolSpeed(inventoryStack)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    private void stealItem(final int slot) {
        final GuiChest chest = (GuiChest)ChestStealer.mc.currentScreen;
        final Slot slot2 = chest.inventorySlots.getSlot(slot);
        try {
            chest.forceShift = true;
            chest.mouseClicked(slot2.xDisplayPosition + 2 + chest.guiLeft, slot2.yDisplayPosition + 2 + chest.guiTop, 0);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.delayTimeHelper.reset();
    }
    
    private double getDamageSword(final ItemStack itemStack) {
        double damage = 0.0;
        if (itemStack.getItem() instanceof ItemSword) {
            damage += ((ItemSword)itemStack.getItem()).getAttackDamage() + EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) / 11.0;
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, itemStack) / 11.0;
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) / 11.0;
            damage -= itemStack.getItemDamage() / 10000.0;
        }
        return damage;
    }
    
    private double getDamageBow(final ItemStack itemStack) {
        double damage = 0.0;
        if (itemStack.getItem() instanceof ItemBow) {
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, itemStack) / 11.0;
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, itemStack) / 8.0;
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemStack) / 8.0;
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, itemStack) / 11.0;
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) / 11.0;
            damage -= itemStack.getItemDamage() / 10000.0;
        }
        return damage;
    }
    
    private double getToolSpeed(final ItemStack itemStack) {
        double damage = 0.0;
        if (itemStack.getItem() instanceof ItemTool) {
            if (itemStack.getItem() instanceof ItemAxe) {
                damage += itemStack.getItem().getStrVsBlock(itemStack, new Block(Material.wood, MapColor.woodColor)) + EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack);
            }
            else if (itemStack.getItem() instanceof ItemPickaxe) {
                damage += itemStack.getItem().getStrVsBlock(itemStack, new Block(Material.rock, MapColor.stoneColor)) + EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack);
            }
            else if (itemStack.getItem() instanceof ItemSpade) {
                damage += itemStack.getItem().getStrVsBlock(itemStack, new Block(Material.sand, MapColor.sandColor)) + EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack);
            }
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) / 11.0;
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, itemStack) / 11.0;
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) / 33.0;
            damage -= itemStack.getItemDamage() / 10000.0;
        }
        return damage;
    }
    
    private double getDamageReduceAmount(final ItemStack itemStack) {
        double damageReduceAmount = 0.0;
        if (itemStack.getItem() instanceof ItemArmor) {
            damageReduceAmount += ((ItemArmor)itemStack.getItem()).damageReduceAmount + (6 + EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, itemStack) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, itemStack)) / 3.0f;
            damageReduceAmount += EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, itemStack) / 11.0;
            damageReduceAmount += EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, itemStack) / 11.0;
            damageReduceAmount += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, itemStack) / 11.0;
            damageReduceAmount += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) / 11.0;
            damageReduceAmount += EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, itemStack) / 11.0;
            damageReduceAmount += EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, itemStack) / 11.0;
            if (((ItemArmor)itemStack.getItem()).armorType == 0 && ((ItemArmor)itemStack.getItem()).getArmorMaterial() == ItemArmor.ArmorMaterial.GOLD) {
                damageReduceAmount -= 0.01;
            }
            damageReduceAmount -= itemStack.getItemDamage() / 10000.0;
        }
        return damageReduceAmount;
    }
    
    private double getBestRod(final ItemStack itemStack) {
        double damage = 0.0;
        if (itemStack.getItem() instanceof ItemFishingRod) {
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.lure.effectId, itemStack) / 11.0;
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) / 11.0;
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.luckOfTheSea.effectId, itemStack) / 33.0;
            damage -= itemStack.getItemDamage() / 10000.0;
        }
        return damage;
    }
}
