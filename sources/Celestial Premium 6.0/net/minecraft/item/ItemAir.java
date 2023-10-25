/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemAir
extends Item {
    private final Block field_190904_a;

    public ItemAir(Block p_i47264_1_) {
        this.field_190904_a = p_i47264_1_;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.field_190904_a.getUnlocalizedName();
    }

    @Override
    public String getUnlocalizedName() {
        return this.field_190904_a.getUnlocalizedName();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        this.field_190904_a.func_190948_a(stack, playerIn, tooltip, advanced);
    }
}

