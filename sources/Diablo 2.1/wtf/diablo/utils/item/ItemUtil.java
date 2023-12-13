package wtf.diablo.utils.item;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.BlockPos;

public class ItemUtil {
    public static void updateTool(BlockPos pos) {
        Minecraft mc = Minecraft.getMinecraft();
        Block block = mc.theWorld.getBlockState(pos).getBlock();
        float strength = 1.0F;
        int bestItemIndex = -1;

        for(int i = 0; i < 9; ++i) {
            ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];
            if (itemStack != null && itemStack.getStrVsBlock(block) > strength) {
                strength = itemStack.getStrVsBlock(block);
                bestItemIndex = i;
            }
        }

        if (bestItemIndex != -1) {
            mc.thePlayer.inventory.currentItem = bestItemIndex;
        }

    }
    public static int getBestSword(final Entity target) {
        final int originalSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
        int weaponSlot = -1;
        float weaponDamage = 1.0f;
        for (byte slot = 0; slot < 9; ++slot) {
            Minecraft.getMinecraft().thePlayer.inventory.currentItem = slot;
            final ItemStack itemStack = Minecraft.getMinecraft().thePlayer.getHeldItem();
            if (itemStack != null && itemStack.getItem() instanceof ItemSword) {
                float damage = getItemDamage(itemStack);
                if (damage > weaponDamage) {
                    weaponDamage = damage;
                    weaponSlot = slot;
                }
            }
        }
        if (weaponSlot != -1) {
            return weaponSlot;
        }
        return originalSlot;
    }

    public static float getItemDamage(final ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemSword) {
            double damage = 4.0 + ((ItemSword)itemStack.getItem()).getDamageVsEntity();
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25;
            return (float)damage;
        }
        if (itemStack.getItem() instanceof ItemTool) {
            double damage = 1.0 + ((ItemTool)itemStack.getItem()).getToolMaterial().getDamageVsEntity();
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
            return (float)damage;
        }
        return 1.0f;
    }
}
