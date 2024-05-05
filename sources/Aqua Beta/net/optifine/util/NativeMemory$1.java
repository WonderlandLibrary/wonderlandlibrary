package net.optifine.util;

import java.lang.reflect.Method;
import net.minecraft.src.Config;
import net.optifine.util.LongSupplier;

static final class NativeMemory.1
implements LongSupplier {
    private boolean disabled = false;
    final /* synthetic */ Method val$method_f;
    final /* synthetic */ Object val$o_f;

    NativeMemory.1(Method method, Object object) {
        this.val$method_f = method;
        this.val$o_f = object;
    }

    public long getAsLong() {
        if (this.disabled) {
            return -1L;
        }
        try {
            return (Long)this.val$method_f.invoke(this.val$o_f, new Object[0]);
        }
        catch (Throwable throwable) {
            Config.warn((String)("" + throwable.getClass().getName() + ": " + throwable.getMessage()));
            this.disabled = true;
            return -1L;
        }
    }
}
