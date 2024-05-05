package net.minecraft.block.state;

import com.google.common.base.Function;
import java.util.Map;
import net.minecraft.block.properties.IProperty;

static final class BlockStateBase.1
implements Function<Map.Entry<IProperty, Comparable>, String> {
    BlockStateBase.1() {
    }

    public String apply(Map.Entry<IProperty, Comparable> p_apply_1_) {
        if (p_apply_1_ == null) {
            return "<NULL>";
        }
        IProperty iproperty = (IProperty)p_apply_1_.getKey();
        return iproperty.getName() + "=" + iproperty.getName((Comparable)p_apply_1_.getValue());
    }
}
