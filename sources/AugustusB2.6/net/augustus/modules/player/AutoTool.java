// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.player;

import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemAxe;
import net.augustus.events.EventClick;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.augustus.utils.BlockUtil;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemTool;
import net.minecraft.util.MovingObjectPosition;
import net.augustus.events.EventPostMouseOver;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.augustus.modules.Module;

public class AutoTool extends Module
{
    private final TimeHelper timeHelper;
    public StringValue mode;
    private int slot;
    
    public AutoTool() {
        super("AutoTool", new Color(237, 237, 0), Categorys.PLAYER);
        this.timeHelper = new TimeHelper();
        this.mode = new StringValue(1, "Mode", this, "Only Tools", new String[] { "Only Tools" });
        this.slot = 0;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if (AutoTool.mc.thePlayer != null && AutoTool.mc.theWorld != null) {
            this.slot = -1;
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @EventTarget
    public void onEventPostMouseOver(final EventPostMouseOver eventPostMouseOver) {
        double lastDamage = 0.0;
        this.slot = -1;
        final MovingObjectPosition objectPosition = AutoTool.mc.objectMouseOver;
        final String mode = this.mode.getSelected();
        if (objectPosition != null) {
            for (int i = 36; i < AutoTool.mc.thePlayer.inventoryContainer.inventorySlots.size(); ++i) {
                final ItemStack itemStack = AutoTool.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (itemStack != null) {
                    final Item item = itemStack.getItem();
                    if (objectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                        final double itemDamage = this.getItemDamage(itemStack);
                        final double currentDamage = this.getItemDamage(AutoTool.mc.thePlayer.getHeldItem());
                        if (itemDamage > 1.0 && itemDamage > currentDamage && itemDamage > lastDamage) {
                            this.slot = i - 36;
                            lastDamage = itemDamage;
                            this.timeHelper.reset();
                        }
                    }
                    if (objectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && mode.equals("Only Tools") && AutoTool.mc.gameSettings.keyBindAttack.isKeyDown() && (item instanceof ItemTool || item instanceof ItemShears)) {
                        final double toolSpeed = this.getToolSpeed(itemStack);
                        final double currentSpeed = this.getToolSpeed(AutoTool.mc.thePlayer.getHeldItem());
                        if (toolSpeed > 1.0 && toolSpeed > currentSpeed && toolSpeed > lastDamage) {
                            this.slot = i - 36;
                            lastDamage = toolSpeed;
                            this.timeHelper.reset();
                        }
                    }
                }
            }
        }
        if (BlockUtil.isScaffoldToggled()) {
            this.slot = -1;
        }
        AutoTool.mc.playerController.syncCurrentPlayItem();
    }
    
    @EventTarget
    public void onEventClick(final EventClick eventClick) {
        if (!BlockUtil.isScaffoldToggled() && this.slot != -1) {
            eventClick.setSlot(this.slot);
        }
    }
    
    private double getToolSpeed(final ItemStack itemStack) {
        double damage = 0.0;
        if (itemStack != null && (itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemShears)) {
            if (itemStack.getItem() instanceof ItemAxe) {
                damage += itemStack.getItem().getStrVsBlock(itemStack, AutoTool.mc.theWorld.getBlockState(AutoTool.mc.objectMouseOver.getBlockPos()).getBlock()) + EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack);
            }
            else if (itemStack.getItem() instanceof ItemPickaxe) {
                damage += itemStack.getItem().getStrVsBlock(itemStack, AutoTool.mc.theWorld.getBlockState(AutoTool.mc.objectMouseOver.getBlockPos()).getBlock()) + EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack);
            }
            else if (itemStack.getItem() instanceof ItemSpade) {
                damage += itemStack.getItem().getStrVsBlock(itemStack, AutoTool.mc.theWorld.getBlockState(AutoTool.mc.objectMouseOver.getBlockPos()).getBlock()) + EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack);
            }
            else if (itemStack.getItem() instanceof ItemShears) {
                System.out.println(itemStack.getItem().getStrVsBlock(itemStack, AutoTool.mc.theWorld.getBlockState(AutoTool.mc.objectMouseOver.getBlockPos()).getBlock()));
                damage += itemStack.getItem().getStrVsBlock(itemStack, AutoTool.mc.theWorld.getBlockState(AutoTool.mc.objectMouseOver.getBlockPos()).getBlock());
            }
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) / 11.0;
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, itemStack) / 11.0;
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) / 33.0;
            damage -= itemStack.getItemDamage() / 10000.0;
            return damage;
        }
        return 0.0;
    }
    
    private double getItemDamage(final ItemStack itemStack) {
        double damage = 0.0;
        if (itemStack == null) {
            if (itemStack.getItem() instanceof ItemTool) {
                damage += ((ItemTool)itemStack.getItem()).getDamageVsEntity();
                damage += itemStack.getItem().getMaxDamage() + EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
                damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) / 11.0;
                damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, itemStack) / 11.0;
                damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) / 11.0;
                damage -= itemStack.getItemDamage() / 10000.0;
            }
        }
        return damage;
    }
    
    public int getSlot() {
        return this.slot;
    }
}
