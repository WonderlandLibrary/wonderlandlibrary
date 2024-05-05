package net.minecraft.client.renderer.block.statemap;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.block.statemap.StateMap;

public static class StateMap.Builder {
    private IProperty<?> name;
    private String suffix;
    private final List<IProperty<?>> ignored = Lists.newArrayList();

    public StateMap.Builder withName(IProperty<?> builderPropertyIn) {
        this.name = builderPropertyIn;
        return this;
    }

    public StateMap.Builder withSuffix(String builderSuffixIn) {
        this.suffix = builderSuffixIn;
        return this;
    }

    public StateMap.Builder ignore(IProperty<?> ... p_178442_1_) {
        Collections.addAll(this.ignored, (Object[])p_178442_1_);
        return this;
    }

    public StateMap build() {
        return new StateMap(this.name, this.suffix, this.ignored, null);
    }
}
