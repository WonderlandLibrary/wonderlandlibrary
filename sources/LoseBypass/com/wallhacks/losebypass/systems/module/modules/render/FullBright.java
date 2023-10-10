/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.module.modules.render;

import com.wallhacks.losebypass.systems.module.Module;

@Module.Registration(name="FullBright", description="Puts your game at full brightness", category=Module.Category.RENDER)
public class FullBright
extends Module {
    static FullBright instance;

    public FullBright() {
        instance = this;
    }

    public static boolean enabled() {
        return instance.isEnabled();
    }
}

