package net.minecraft.client.resources;

import com.google.common.base.Function;
import net.minecraft.client.resources.IResourcePack;

class SimpleReloadableResourceManager.1
implements Function<IResourcePack, String> {
    SimpleReloadableResourceManager.1() {
    }

    public String apply(IResourcePack p_apply_1_) {
        return p_apply_1_.getPackName();
    }
}
