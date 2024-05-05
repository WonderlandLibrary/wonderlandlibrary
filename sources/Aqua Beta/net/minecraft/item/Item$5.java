package net.minecraft.item;

import com.google.common.base.Function;
import net.minecraft.block.BlockSand;
import net.minecraft.item.ItemStack;

static final class Item.5
implements Function<ItemStack, String> {
    Item.5() {
    }

    public String apply(ItemStack p_apply_1_) {
        return BlockSand.EnumType.byMetadata((int)p_apply_1_.getMetadata()).getUnlocalizedName();
    }
}
