package net.minecraft.item;

import com.google.common.base.Function;
import net.minecraft.block.BlockPlanks;
import net.minecraft.item.ItemStack;

static final class Item.3
implements Function<ItemStack, String> {
    Item.3() {
    }

    public String apply(ItemStack p_apply_1_) {
        return BlockPlanks.EnumType.byMetadata((int)p_apply_1_.getMetadata()).getUnlocalizedName();
    }
}
