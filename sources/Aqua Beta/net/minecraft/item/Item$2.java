package net.minecraft.item;

import com.google.common.base.Function;
import net.minecraft.block.BlockDirt;
import net.minecraft.item.ItemStack;

static final class Item.2
implements Function<ItemStack, String> {
    Item.2() {
    }

    public String apply(ItemStack p_apply_1_) {
        return BlockDirt.DirtType.byMetadata((int)p_apply_1_.getMetadata()).getUnlocalizedName();
    }
}
