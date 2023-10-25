package net.minecraft.item;

import com.google.common.base.Function;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.item.ItemStack;

static final class Item.16
implements Function<ItemStack, String> {
    Item.16() {
    }

    public String apply(ItemStack p_apply_1_) {
        return BlockPrismarine.EnumType.byMetadata((int)p_apply_1_.getMetadata()).getUnlocalizedName();
    }
}
