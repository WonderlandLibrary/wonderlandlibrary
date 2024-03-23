package dev.zprestige.prestige.client.module.impl.misc;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.MoveEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.util.impl.InventoryUtil;
import net.minecraft.block.AirBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

public class AutoTool extends Module {
    public int slot;

    public AutoTool() {
        super("Auto Tool", Category.Misc, "Automatically swaps to the best tool possible");
    }

    @EventListener
    public void event(MoveEvent event) {
        if (getMc().crosshairTarget instanceof BlockHitResult blockHitResult && !getMc().world.getBlockState(blockHitResult.getBlockPos()).isAir()) {
            int n = getTool(blockHitResult.getBlockPos());
            if (n != -1 && getMc().options.attackKey.isPressed()) {
                slot = getMc().player.getInventory().selectedSlot;
                InventoryUtil.INSTANCE.setCurrentSlot(n);
            } else {
                InventoryUtil.INSTANCE.setCurrentSlot(slot);
            }
        }
    }

    private int getTool(BlockPos blockPos) {
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = getMc().player.getInventory().getStack(i);
            if (!itemStack.isEmpty() && itemStack.getMaxDamage() - itemStack.getDamage() > 10) {
                float f = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, itemStack);
                float f2 = itemStack.getMiningSpeedMultiplier(getMc().world.getBlockState(blockPos));
                if (getMc().world.getBlockState(blockPos).getBlock() instanceof AirBlock) {
                    return -1;
                }
                if (f + f2 > 1) {
                    return i;
                }
            }
        }
        return -1;
    }
}