/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.render;

import tk.rektsky.module.Category;
import tk.rektsky.module.Module;

public class FullBright
extends Module {
    float pre = 1.0f;

    public FullBright() {
        super("FullBright", "Turns your gamma up", Category.RENDER);
    }

    @Override
    public void onEnable() {
        this.pre = this.mc.gameSettings.gammaSetting;
        this.mc.gameSettings.gammaSetting = 1000.0f;
    }

    @Override
    public void onDisable() {
        this.mc.gameSettings.gammaSetting = this.pre;
    }
}

