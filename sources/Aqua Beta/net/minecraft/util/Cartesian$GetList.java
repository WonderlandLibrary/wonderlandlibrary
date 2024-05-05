package net.minecraft.util;

import com.google.common.base.Function;
import java.util.Arrays;
import java.util.List;

static class Cartesian.GetList<T>
implements Function<Object[], List<T>> {
    private Cartesian.GetList() {
    }

    public List<T> apply(Object[] p_apply_1_) {
        return Arrays.asList((Object[])p_apply_1_);
    }
}
