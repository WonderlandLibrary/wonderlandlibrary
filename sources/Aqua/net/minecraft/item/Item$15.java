package net.minecraft.item;

import com.google.common.base.Function;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.item.ItemStack;

static final class Item.15
implements Function<ItemStack, String> {
    Item.15() {
    }

    public String apply(ItemStack p_apply_1_) {
        return BlockDoublePlant.EnumPlantType.byMetadata((int)p_apply_1_.getMetadata()).getUnlocalizedName();
    }
}
