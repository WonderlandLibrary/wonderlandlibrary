package vip.radium.module.impl.movement;

import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.event.impl.player.MoveEntityEvent;
import vip.radium.event.impl.player.SprintEvent;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.ModuleManager;
import vip.radium.property.Property;
import vip.radium.utils.MovementUtils;
import vip.radium.utils.Wrapper;

@ModuleInfo(label = "Sprint", category = ModuleCategory.MOVEMENT)
public final class Sprint extends Module {

    private final Property<Boolean> omniProperty = new Property<>("Omni", true);

    private int groundTicks;

    public Sprint() {
        toggle();
    }

    @EventLink
    public final Listener<MoveEntityEvent> onMoveEntityEvent = event -> {
        if (groundTicks > 3 && MovementUtils.isMoving() && !Speed.isSpeeding() &&
                !ModuleManager.getInstance(Flight.class).isEnabled() && omniProperty.getValue())
            MovementUtils.setSpeed(event, MovementUtils.getBaseMoveSpeed());
    };

    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent = event -> {
        if (event.isPre()) {
            if (MovementUtils.isOnGround()) groundTicks++;
            else groundTicks = 0;
        }
    };

    @EventLink
    public final Listener<SprintEvent> onSprintEvent = event -> {
        if (!event.isSprinting()) {
            final boolean canSprint = MovementUtils.canSprint(omniProperty.getValue());
            Wrapper.getPlayer().setSprinting(canSprint);
            event.setSprinting(canSprint);
        }
    };
}
