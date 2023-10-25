package net.minecraft.item;

import com.google.common.base.Function;
import net.minecraft.block.BlockStone;
import net.minecraft.item.ItemStack;

static final class Item.1
implements Function<ItemStack, String> {
    Item.1() {
    }

    public String apply(ItemStack p_apply_1_) {
        return BlockStone.EnumType.byMetadata((int)p_apply_1_.getMetadata()).getUnlocalizedName();
    }
}
