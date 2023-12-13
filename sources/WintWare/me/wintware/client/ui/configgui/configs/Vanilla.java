/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.ui.configgui.configs;

import me.wintware.client.Main;
import me.wintware.client.module.Module;
import me.wintware.client.module.combat.Criticals;
import me.wintware.client.module.combat.KillAura;
import me.wintware.client.module.combat.Velocity;
import me.wintware.client.ui.configgui.Config;

public class Vanilla
extends Config {
    public Vanilla() {
        super("Vanilla");
    }

    @Override
    public void loadConfig() {
        try {
            for (Module mod : Main.instance.moduleManager.modules) {
                if (mod instanceof KillAura) {
                    KillAura.range.setValDouble(6.0);
                    KillAura.fov.setValDouble(360.0);
                    KillAura.onlyCrit.setValue(true);
                }
                if (mod instanceof Criticals) {
                    Main.instance.moduleManager.getModuleByClass(Criticals.class).setToggled(true);
                }
                if (!(mod instanceof Velocity)) continue;
                Main.instance.moduleManager.getModuleByClass(Velocity.class).setToggled(true);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

