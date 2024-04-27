/*
 * Decompiled with CFR 0.152.
 */
package cc.paimon.modules.hyt;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;

@ModuleInfo(name="ScaffoldHelper", description="Grim bypass", category=ModuleCategory.WORLD)
public class ScaffoldHelper
extends Module {
    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        Scaffold scaffold = (Scaffold)LiquidBounce.moduleManager.getModule(Scaffold.class);
        if (ScaffoldHelper.mc2.field_71439_g.field_70122_E) {
            scaffold.setState(false);
        } else {
            scaffold.setState(true);
        }
    }

    @Override
    public void onDisable() {
        Scaffold scaffold = (Scaffold)LiquidBounce.moduleManager.getModule(Scaffold.class);
        scaffold.setState(false);
    }
}

