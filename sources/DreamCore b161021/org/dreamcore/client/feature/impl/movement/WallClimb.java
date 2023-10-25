package org.dreamcore.client.feature.impl.movement;

import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventPreMotion;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.misc.TimerHelper;
import org.dreamcore.client.helpers.player.MovementHelper;
import org.dreamcore.client.settings.impl.NumberSetting;

public class WallClimb extends Feature {

    public static NumberSetting climbTicks;
    private final TimerHelper timerHelper = new TimerHelper();

    public WallClimb() {
        super("WallClimb", "Автоматически взберается на стены", Type.Movement);
        climbTicks = new NumberSetting("Climb Ticks", 1, 0, 5, 0.1F, () -> true);
        addSettings(climbTicks);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        this.setSuffix("" + climbTicks.getNumberValue());
        if (MovementHelper.isMoving() && mc.player.isCollidedHorizontally) {
            if (timerHelper.hasReached(climbTicks.getNumberValue() * 100)) {
                event.setOnGround(true);
                mc.player.onGround = true;
                mc.player.isCollidedVertically = true;
                mc.player.isCollidedHorizontally = true;
                mc.player.isAirBorne = true;
                mc.player.jump();
                timerHelper.reset();
            }
        }
    }
}
