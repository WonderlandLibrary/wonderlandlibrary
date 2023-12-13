/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.ui.configgui.configs;

import me.wintware.client.Main;
import me.wintware.client.module.Module;
import me.wintware.client.module.combat.Criticals;
import me.wintware.client.module.combat.KillAura;
import me.wintware.client.module.combat.TargetStrafe;
import me.wintware.client.module.combat.Velocity;
import me.wintware.client.ui.configgui.Config;

public class WellMore
extends Config {
    public WellMore() {
        super("WellMore");
    }

    @Override
    public void loadConfig() {
        try {
            for (Module mod : Main.instance.moduleManager.modules) {
                if (mod instanceof KillAura) {
                    KillAura.range.setValDouble(4.0);
                    KillAura.fov.setValDouble(360.0);
                    KillAura.onlyCrit.setValue(true);
                }
                if (mod instanceof Criticals) {
                    Main.instance.moduleManager.getModuleByClass(Criticals.class).setToggled(false);
                }
                if (mod instanceof TargetStrafe) {
                    TargetStrafe.range.setValDouble(3.2);
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

