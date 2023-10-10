/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.MISC;

import com.darkmagician6.eventapi.EventTarget;
import java.util.Random;
import me.AveReborn.Value;
import me.AveReborn.events.EventPreMotion;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import me.AveReborn.util.timeUtils.TimeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmor
extends Mod {
    public Value<Double> delay = new Value<Double>("AutoArmor_Delay", 100.0, 0.0, 500.0, 1.0);
    public TimeUtil timer = new TimeUtil();
    double maxValue = -1.0;
    double mv;
    private int num = 5;
    int item = -1;
    private final int[] boots = new int[]{313, 309, 317, 305, 301};
    private final int[] chestplate = new int[]{311, 307, 315, 303, 299};
    private final int[] helmet = new int[]{310, 306, 314, 302, 298};
    private final int[] leggings = new int[]{312, 308, 316, 304, 300};

    public AutoArmor() {
        super("AutoArmor", Category.MISC);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (Minecraft.thePlayer.capabilities.isCreativeMode || Minecraft.thePlayer.openContainer != null && Minecraft.thePlayer.openContainer.windowId != 0) {
            return;
        }
        if (TimeUtil.delay(this.delay.getValueState().intValue() + new Random().nextInt(4))) {
            this.maxValue = -1.0;
            this.item = -1;
            int i2 = 9;
            while (i2 < 45) {
                if (Minecraft.thePlayer.inventoryContainer.getSlot(i2).getStack() != null && this.canEquip(Minecraft.thePlayer.inventoryContainer.getSlot(i2).getStack()) != -1 && this.canEquip(Minecraft.thePlayer.inventoryContainer.getSlot(i2).getStack()) == this.num) {
                    this.change(this.num, i2);
                }
                ++i2;
            }
            if (this.item != -1) {
                if (Minecraft.thePlayer.inventoryContainer.getSlot(this.item).getStack() != null) {
                    this.mc.playerController.windowClick(0, this.num, 0, 1, Minecraft.thePlayer);
                }
                this.mc.playerController.windowClick(0, this.item, 0, 1, Minecraft.thePlayer);
            }
            int n2 = this.num == 8 ? 5 : (this.num = this.num + 1);
            this.num = n2;
            TimeUtil.reset();
        }
    }

    private int canEquip(ItemStack stack) {
        int id2;
        int[] arrn = this.boots;
        int n2 = arrn.length;
        int n3 = 0;
        while (n3 < n2) {
            id2 = arrn[n3];
            stack.getItem();
            if (Item.getIdFromItem(stack.getItem()) == id2) {
                return 8;
            }
            ++n3;
        }
        arrn = this.leggings;
        n2 = arrn.length;
        n3 = 0;
        while (n3 < n2) {
            id2 = arrn[n3];
            stack.getItem();
            if (Item.getIdFromItem(stack.getItem()) == id2) {
                return 7;
            }
            ++n3;
        }
        arrn = this.chestplate;
        n2 = arrn.length;
        n3 = 0;
        while (n3 < n2) {
            id2 = arrn[n3];
            stack.getItem();
            if (Item.getIdFromItem(stack.getItem()) == id2) {
                return 6;
            }
            ++n3;
        }
        arrn = this.helmet;
        n2 = arrn.length;
        n3 = 0;
        while (n3 < n2) {
            id2 = arrn[n3];
            stack.getItem();
            if (Item.getIdFromItem(stack.getItem()) == id2) {
                return 5;
            }
            ++n3;
        }
        return -1;
    }

    private void change(int numy, int i2) {
        double d2 = this.maxValue == -1.0 ? (Minecraft.thePlayer.inventoryContainer.getSlot(numy).getStack() != null ? this.getProtValue(Minecraft.thePlayer.inventoryContainer.getSlot(numy).getStack()) : this.maxValue) : (this.mv = this.maxValue);
        if (this.mv <= this.getProtValue(Minecraft.thePlayer.inventoryContainer.getSlot(i2).getStack())) {
            if (this.mv == this.getProtValue(Minecraft.thePlayer.inventoryContainer.getSlot(i2).getStack())) {
                int currentD = Minecraft.thePlayer.inventoryContainer.getSlot(numy).getStack() != null ? Minecraft.thePlayer.inventoryContainer.getSlot(numy).getStack().getItemDamage() : 999;
                int n2 = Minecraft.thePlayer.inventoryContainer.getSlot(i2).getStack() != null ? Minecraft.thePlayer.inventoryContainer.getSlot(i2).getStack().getItemDamage() : 500;
                int newD = n2;
                if (newD <= currentD && newD != currentD) {
                    this.item = i2;
                    this.maxValue = this.getProtValue(Minecraft.thePlayer.inventoryContainer.getSlot(i2).getStack());
                }
            } else {
                this.item = i2;
                this.maxValue = this.getProtValue(Minecraft.thePlayer.inventoryContainer.getSlot(i2).getStack());
            }
        }
    }

    private double getProtValue(ItemStack stack) {
        if (stack != null) {
            return (double)((ItemArmor)stack.getItem()).damageReduceAmount + (double)((100 - ((ItemArmor)stack.getItem()).damageReduceAmount * 4) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 4) * 0.0075;
        }
        return 0.0;
    }
}

