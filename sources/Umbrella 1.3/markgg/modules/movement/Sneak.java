/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.movement;

import markgg.modules.Module;

public class Sneak
extends Module {
    public Sneak() {
        super("Sneak", 0, Module.Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        this.mc.gameSettings.keyBindSneak.pressed = true;
    }

    @Override
    public void onDisable() {
        this.mc.gameSettings.keyBindSneak.pressed = false;
    }
}

