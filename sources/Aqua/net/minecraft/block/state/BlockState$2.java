package net.minecraft.block.state;

import java.util.Comparator;
import net.minecraft.block.properties.IProperty;

class BlockState.2
implements Comparator<IProperty> {
    BlockState.2() {
    }

    public int compare(IProperty p_compare_1_, IProperty p_compare_2_) {
        return p_compare_1_.getName().compareTo(p_compare_2_.getName());
    }
}
