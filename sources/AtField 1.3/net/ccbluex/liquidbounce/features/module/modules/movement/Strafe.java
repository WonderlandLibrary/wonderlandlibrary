/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MovementUtils;

@ModuleInfo(name="Strafe", description="Allows you to freely move in mid air.", category=ModuleCategory.MOVEMENT)
public final class Strafe
extends Module {
    @EventTarget
    public final void onMotion(MotionEvent motionEvent) {
        if (motionEvent.getEventState() == EventState.POST) {
            return;
        }
        MovementUtils.strafe$default(0.0f, 1, null);
    }
}

