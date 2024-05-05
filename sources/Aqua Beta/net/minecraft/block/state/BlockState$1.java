package net.minecraft.block.state;

import com.google.common.base.Function;
import net.minecraft.block.properties.IProperty;

static final class BlockState.1
implements Function<IProperty, String> {
    BlockState.1() {
    }

    public String apply(IProperty p_apply_1_) {
        return p_apply_1_ == null ? "<NULL>" : p_apply_1_.getName();
    }
}
