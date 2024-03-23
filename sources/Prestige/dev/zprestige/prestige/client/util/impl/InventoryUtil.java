package dev.zprestige.prestige.client.util.impl;

import dev.zprestige.prestige.client.util.MC;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

import java.util.ArrayList;

public class InventoryUtil implements MC {

    public static InventoryUtil INSTANCE = new InventoryUtil();

    public ArrayList<Integer> findItemSlot(Item item, boolean bl) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = bl ? 0 : 9; i < 36; i++) {
            if (getMc().player.getInventory().getStack(i).getItem() == item) {
                arrayList.add(i);
            }
        }
        return arrayList;
    }

    public void setCurrentSlot(int n) {
        getMc().player.getInventory().selectedSlot = n;
    }

    public boolean isHoldingItem(Item item) {
        return getMc().player.getMainHandStack().getItem() == item;
    }

    public Integer findBlockSlot(Block block) {
        for (int i = 0; i < 9; i++) {
            if (getMc().player.getInventory().getStack(i).getItem() == block.asItem()) {
                return i;
            }
        }
        return null;
    }

    public Integer findPotion(int n, int n2, StatusEffect statusEffect) {
        for (int i = n; i < n2; i++) {
            ItemStack itemStack = getMc().player.getInventory().getStack(i);
            if (itemStack.getItem() == Items.SPLASH_POTION && (statusEffect == null || hasStatusEffect(itemStack, statusEffect))) {
                return i;
            }
        }
        return null;
    }

    public boolean hasStatusEffect(ItemStack itemStack, StatusEffect statusEffect) {
        for (StatusEffectInstance statusEffectInstance : PotionUtil.getPotionEffects(itemStack)) {
            if (statusEffectInstance.getEffectType() == statusEffect) {
                return true;
            }
        }
        return false;
    }

    public boolean performSlotAction(int n, int n2, SlotActionType slotActionType) {
        if (getMc().currentScreen instanceof InventoryScreen screen) {
            getMc().interactionManager.clickSlot(screen.getScreenHandler().syncId, n, n2, slotActionType, getMc().player);
            return true;
        }
        return true;
    }

    public boolean isHoldingOffhandItem(Item item) {
        return getMc().player.getOffHandStack().getItem() == item;
    }

    public Integer findItemInHotbar(Item item) {
        for (int i = 0; i < 9; i++) {
            if (getMc().player.getInventory().getStack(i).getItem() == item) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public MinecraftClient getMc() {
        return MinecraftClient.getInstance();
    }
}