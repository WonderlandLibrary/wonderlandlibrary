package net.minecraft.util;

import com.google.common.base.Function;
import net.minecraft.util.IChatComponent;

static final class ChatComponentStyle.2
implements Function<IChatComponent, IChatComponent> {
    ChatComponentStyle.2() {
    }

    public IChatComponent apply(IChatComponent p_apply_1_) {
        IChatComponent ichatcomponent = p_apply_1_.createCopy();
        ichatcomponent.setChatStyle(ichatcomponent.getChatStyle().createDeepCopy());
        return ichatcomponent;
    }
}
