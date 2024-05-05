package client.module.impl.movement.flight;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.input.MoveInputEvent;
import client.event.impl.other.MotionEvent;
import client.event.impl.other.MoveEvent;
import client.module.impl.movement.Flight;
import client.util.MoveUtil;
import client.value.Mode;

public class MushFlight extends Mode<Flight> {

    private boolean down;

    public MushFlight(final String name, final Flight parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<MotionEvent> onMotion = event -> {
        if (!MoveUtil.isMoving() || mc.thePlayer.ticksExisted % 3 != 0) event.setCancelled(true);
    };

    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = event -> event.setSneaking(false);

    @EventLink
    public final Listener<MoveEvent> onMove = event -> {
        if (MoveUtil.isMoving() && mc.thePlayer.ticksExisted % 9 == 0) {
            event.setX(-Math.sin(MoveUtil.getDirection()) * 8.6);
            event.setZ(Math.cos(MoveUtil.getDirection()) * 8.6);
        }
        event.setY((mc.gameSettings.keyBindJump.isKeyDown() ? 0.42F : 0) - (mc.gameSettings.keyBindSneak.isKeyDown() ? 0.42F : 0));
        if (event.getY() == 0) {
            event.setY(down ? -0.0626 : 0.0626);
            down = !down;
        }
    };
}
