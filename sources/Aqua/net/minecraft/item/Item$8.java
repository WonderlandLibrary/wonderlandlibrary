package net.minecraft.item;

import com.google.common.base.Function;
import net.minecraft.item.ItemStack;

static final class Item.8
implements Function<ItemStack, String> {
    Item.8() {
    }

    public String apply(ItemStack p_apply_1_) {
        return (p_apply_1_.getMetadata() & 1) == 1 ? "wet" : "dry";
    }
}
