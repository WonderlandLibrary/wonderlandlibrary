package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.BlockFlower;

class BlockFlower.1
implements Predicate<BlockFlower.EnumFlowerType> {
    BlockFlower.1() {
    }

    public boolean apply(BlockFlower.EnumFlowerType p_apply_1_) {
        return p_apply_1_.getBlockType() == BlockFlower.this.getBlockType();
    }
}
