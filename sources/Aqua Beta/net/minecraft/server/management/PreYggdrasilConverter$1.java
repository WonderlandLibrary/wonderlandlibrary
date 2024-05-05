package net.minecraft.server.management;

import com.google.common.base.Predicate;
import net.minecraft.util.StringUtils;

static final class PreYggdrasilConverter.1
implements Predicate<String> {
    PreYggdrasilConverter.1() {
    }

    public boolean apply(String p_apply_1_) {
        return !StringUtils.isNullOrEmpty((String)p_apply_1_);
    }
}
