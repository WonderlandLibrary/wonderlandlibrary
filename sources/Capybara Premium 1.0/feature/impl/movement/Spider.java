package fun.expensive.client.feature.impl.movement;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventPreMotion;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.NumberSetting;
import fun.rich.client.utils.math.TimerHelper;
import fun.rich.client.utils.movement.MovementUtils;

public class Spider extends Feature {
    TimerHelper spiderTimer = new TimerHelper();
    public NumberSetting climbSpeed = new NumberSetting("Climb Speed", 1, 0, 5, 0.1F, () -> true);

    public Spider() {
        super("Spider", "Позволяет взбираться вверх по стенам", FeatureCategory.Movement);
        addSettings(climbSpeed);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        setSuffix("" + climbSpeed.getNumberValue());
    }

    @EventTarget
    public void onPreMotion(EventPreMotion eventPreMotion) {
        if (MovementUtils.isMoving() && mc.player.isCollidedHorizontally && spiderTimer.hasReached(climbSpeed.getNumberValue() * 100)) {
            eventPreMotion.setOnGround(true);
            mc.player.jump();
            spiderTimer.reset();
        }
    }
}
