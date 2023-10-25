package fun.expensive.client.feature.impl.movement;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;

public class AutoParkour extends Feature {
    public AutoParkour() {
        super("AutoParkour", "Автоматически прыгает с конца блока", FeatureCategory.Movement);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0, -0.5, 0).expand(-0.001, 0, -0.001)).isEmpty() && mc.player.onGround && !mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.player.jump();
        }
    }
}
