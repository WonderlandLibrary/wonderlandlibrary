package net.optifine.reflect;

import net.optifine.reflect.IResolvable;
import net.optifine.reflect.Reflector;

/*
 * Exception performing whole class analysis ignored.
 */
static final class Reflector.1
implements IResolvable {
    final /* synthetic */ String val$str;

    Reflector.1(String string) {
        this.val$str = string;
    }

    public void resolve() {
        Reflector.access$000().info("[OptiFine] " + this.val$str);
    }
}
