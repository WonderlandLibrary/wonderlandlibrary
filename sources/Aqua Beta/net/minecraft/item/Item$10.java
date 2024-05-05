package net.minecraft.item;

import com.google.common.base.Function;
import net.minecraft.block.BlockFlower;
import net.minecraft.item.ItemStack;

static final class Item.10
implements Function<ItemStack, String> {
    Item.10() {
    }

    public String apply(ItemStack p_apply_1_) {
        return BlockFlower.EnumFlowerType.getType((BlockFlower.EnumFlowerColor)BlockFlower.EnumFlowerColor.YELLOW, (int)p_apply_1_.getMetadata()).getUnlocalizedName();
    }
}
