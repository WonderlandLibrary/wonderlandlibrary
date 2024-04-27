/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.tomk;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.TickEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.FloatValue;

@ModuleInfo(name="MemoryFix", category=ModuleCategory.MISC, description="idk")
public class MemoryFix
extends Module {
    public MSTimer timer;
    private final FloatValue Speed;
    private final FloatValue delay = new FloatValue("Delay", 120.0f, 10.0f, 600.0f);
    private final FloatValue limit = new FloatValue("Limit", 80.0f, 20.0f, 95.0f);

    @EventTarget
    public void onTick(TickEvent tickEvent) {
        long l = Runtime.getRuntime().maxMemory();
        long l2 = Runtime.getRuntime().totalMemory();
        long l3 = Runtime.getRuntime().freeMemory();
        long l4 = l2 - l3;
        float f = l4 * 100L / l;
        if (this.timer.hasTimePassed((long)((double)((Float)this.delay.get()).floatValue() * 1000.0)) && (double)((Float)this.limit.get()).floatValue() <= (double)f) {
            Runtime.getRuntime().gc();
            this.timer.reset();
        }
    }

    @EventTarget
    public void onMotion(MotionEvent motionEvent) {
        if (mc.getGameSettings().getKeyBindForward().getPressed() || mc.getGameSettings().getKeyBindLeft().getPressed() || mc.getGameSettings().getKeyBindRight().getPressed() || mc.getGameSettings().getKeyBindBack().getPressed()) {
            if (mc.getThePlayer().getOnGround()) {
                mc.getThePlayer().setCameraPitch(((Float)this.Speed.get()).floatValue());
            }
        } else {
            mc.getThePlayer().setCameraPitch(0.0f);
        }
    }

    public MemoryFix() {
        this.Speed = new FloatValue("Speed", 0.05f, 0.0f, 1.0f);
        this.timer = new MSTimer();
    }
}

