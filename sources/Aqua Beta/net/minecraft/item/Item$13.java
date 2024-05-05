package net.minecraft.item;

import com.google.common.base.Function;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.item.ItemStack;

static final class Item.13
implements Function<ItemStack, String> {
    Item.13() {
    }

    public String apply(ItemStack p_apply_1_) {
        return BlockStoneBrick.EnumType.byMetadata((int)p_apply_1_.getMetadata()).getUnlocalizedName();
    }
}
