/*
 * Decompiled with CFR 0.152.
 */
package me.report.liquidware.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;

@ModuleInfo(name="BunnyHop", description="Excellent fast speed for NCP anti-cheat.", category=ModuleCategory.MOVEMENT)
public class BunnyHop
extends Module {
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        BunnyHop.mc.field_71439_g.field_70747_aH = 0.16f;
    }
}

