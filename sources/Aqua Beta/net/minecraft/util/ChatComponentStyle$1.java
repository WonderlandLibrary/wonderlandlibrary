package net.minecraft.util;

import com.google.common.base.Function;
import java.util.Iterator;
import net.minecraft.util.IChatComponent;

static final class ChatComponentStyle.1
implements Function<IChatComponent, Iterator<IChatComponent>> {
    ChatComponentStyle.1() {
    }

    public Iterator<IChatComponent> apply(IChatComponent p_apply_1_) {
        return p_apply_1_.iterator();
    }
}
