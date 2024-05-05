package net.minecraft.item;

import com.google.common.base.Function;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.item.ItemStack;

static final class Item.12
implements Function<ItemStack, String> {
    Item.12() {
    }

    public String apply(ItemStack p_apply_1_) {
        return BlockSilverfish.EnumType.byMetadata((int)p_apply_1_.getMetadata()).getUnlocalizedName();
    }
}
