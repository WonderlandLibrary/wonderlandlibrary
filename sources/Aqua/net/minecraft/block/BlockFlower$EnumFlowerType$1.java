package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.BlockFlower;

static final class BlockFlower.EnumFlowerType.1
implements Predicate<BlockFlower.EnumFlowerType> {
    final /* synthetic */ BlockFlower.EnumFlowerColor val$blockflower$enumflowercolor;

    BlockFlower.EnumFlowerType.1(BlockFlower.EnumFlowerColor enumFlowerColor) {
        this.val$blockflower$enumflowercolor = enumFlowerColor;
    }

    public boolean apply(BlockFlower.EnumFlowerType p_apply_1_) {
        return p_apply_1_.getBlockType() == this.val$blockflower$enumflowercolor;
    }
}
