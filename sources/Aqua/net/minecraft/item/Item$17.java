package net.minecraft.item;

import com.google.common.base.Function;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.item.ItemStack;

static final class Item.17
implements Function<ItemStack, String> {
    Item.17() {
    }

    public String apply(ItemStack p_apply_1_) {
        return BlockRedSandstone.EnumType.byMetadata((int)p_apply_1_.getMetadata()).getUnlocalizedName();
    }
}
