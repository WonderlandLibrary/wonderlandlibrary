package net.minecraft.item;

import com.google.common.base.Function;
import net.minecraft.block.BlockSandStone;
import net.minecraft.item.ItemStack;

static final class Item.9
implements Function<ItemStack, String> {
    Item.9() {
    }

    public String apply(ItemStack p_apply_1_) {
        return BlockSandStone.EnumType.byMetadata((int)p_apply_1_.getMetadata()).getUnlocalizedName();
    }
}
