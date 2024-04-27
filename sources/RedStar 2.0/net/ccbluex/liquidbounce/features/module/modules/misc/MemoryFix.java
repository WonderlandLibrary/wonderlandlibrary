package net.ccbluex.liquidbounce.features.module.modules.misc;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.TickEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.FloatValue;

@ModuleInfo(name="MemoryFix", description="idk", category=ModuleCategory.MISC)
public class MemoryFix
extends Module {
    private final FloatValue delay = new FloatValue("Delay", 120.0f, 10.0f, 600.0f);
    private final FloatValue limit = new FloatValue("Limit", 80.0f, 20.0f, 95.0f);
    private final FloatValue Speed = new FloatValue("Speed", 0.05f, 0.0f, 1.0f);
    public MSTimer timer = new MSTimer();

    @EventTarget
    public void onTick(TickEvent event) {
        long maxMem = Runtime.getRuntime().maxMemory();
        long totalMem = Runtime.getRuntime().totalMemory();
        long freeMem = Runtime.getRuntime().freeMemory();
        long usedMem = totalMem - freeMem;
        float pct = usedMem * 100L / maxMem;
        if (this.timer.hasReached((double)((Float)this.delay.get()).floatValue() * 1000.0) && (double)((Float)this.limit.get()).floatValue() <= (double)pct) {
            Runtime.getRuntime().gc();
            this.timer.resetTwo();
        }
    }

    @EventTarget
    public void onMotion(MotionEvent event) {
        if (mc.getGameSettings().getKeyBindForward().getPressed() || mc.getGameSettings().getKeyBindLeft().getPressed() || mc.getGameSettings().getKeyBindRight().getPressed() || mc.getGameSettings().getKeyBindBack().getPressed()) {
            if (mc.getThePlayer().getOnGround()) {
                mc.getThePlayer().setCameraPitch(((Float)this.Speed.get()).floatValue());
            }
        } else {
            mc.getThePlayer().setCameraPitch(0.0f);
        }
    }
}
