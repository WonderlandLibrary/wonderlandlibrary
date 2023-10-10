/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.module.modules.combat;

import com.wallhacks.losebypass.systems.module.Module;

@Module.Registration(name="NoHitDelay", description="Removes 1.8 cps limit", category=Module.Category.COMBAT)
public class NoHitDelay
extends Module {
    static NoHitDelay instance;

    public NoHitDelay() {
        instance = this;
    }

    public static boolean enabled() {
        return instance.isEnabled();
    }
}

