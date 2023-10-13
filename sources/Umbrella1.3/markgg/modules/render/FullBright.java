/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.render;

import markgg.modules.Module;

public class FullBright
extends Module {
    private float old;

    public FullBright() {
        super("FullBright", 0, Module.Category.RENDER);
    }

    @Override
    public void onEnable() {
        this.old = this.mc.gameSettings.gammaSetting;
        this.mc.gameSettings.gammaSetting = 100.0f;
    }

    @Override
    public void onDisable() {
        this.mc.gameSettings.gammaSetting = this.old;
    }
}

