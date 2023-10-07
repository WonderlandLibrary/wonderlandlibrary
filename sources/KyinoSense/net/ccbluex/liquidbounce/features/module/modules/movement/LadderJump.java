/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;

@ModuleInfo(name="LadderJump", description="Boosts you up when touching a ladder.", category=ModuleCategory.MOVEMENT)
public class LadderJump
extends Module {
    static boolean jumped;

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (LadderJump.mc.field_71439_g.field_70122_E) {
            if (LadderJump.mc.field_71439_g.func_70617_f_()) {
                LadderJump.mc.field_71439_g.field_70181_x = 1.5;
                jumped = true;
            } else {
                jumped = false;
            }
        } else if (!LadderJump.mc.field_71439_g.func_70617_f_() && jumped) {
            LadderJump.mc.field_71439_g.field_70181_x += 0.059;
        }
    }
}

