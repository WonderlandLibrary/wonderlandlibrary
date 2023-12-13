/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.ghost;

import markgg.Client;
import markgg.modules.Module;

public class Panic
extends Module {
    public Panic() {
        super("Panic", 0, Module.Category.GHOST);
    }

    @Override
    public void onEnable() {
        for (Module m : Client.modules) {
            m.onDisable();
            m.toggled = false;
        }
        this.toggled = false;
    }
}

