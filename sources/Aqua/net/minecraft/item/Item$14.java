package net.minecraft.item;

import com.google.common.base.Function;
import net.minecraft.block.BlockWall;
import net.minecraft.item.ItemStack;

static final class Item.14
implements Function<ItemStack, String> {
    Item.14() {
    }

    public String apply(ItemStack p_apply_1_) {
        return BlockWall.EnumType.byMetadata((int)p_apply_1_.getMetadata()).getUnlocalizedName();
    }
}
