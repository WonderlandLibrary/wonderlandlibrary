package client.module.impl.movement;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.MotionEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.util.MoveUtil;
import client.value.impl.BooleanValue;

@ModuleInfo(name = "Sprint", description = "Makes you sprint", category = Category.MOVEMENT)
public class Sprint extends Module {
    private final BooleanValue legit = new BooleanValue("Legit", this, true);
    @EventLink
    public final Listener<MotionEvent> onMotion = event -> {
        if (MoveUtil.isMoving() && (!legit.getValue() || mc.thePlayer.moveForward > 0.0F) && !mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isSneaking() && !mc.thePlayer.isUsingItem()) mc.thePlayer.setSprinting(true);
    };
}
