package net.minecraft.item;

import com.google.common.base.Function;
import net.minecraft.block.BlockFlower;
import net.minecraft.item.ItemStack;

static final class Item.11
implements Function<ItemStack, String> {
    Item.11() {
    }

    public String apply(ItemStack p_apply_1_) {
        return BlockFlower.EnumFlowerType.getType((BlockFlower.EnumFlowerColor)BlockFlower.EnumFlowerColor.RED, (int)p_apply_1_.getMetadata()).getUnlocalizedName();
    }
}
