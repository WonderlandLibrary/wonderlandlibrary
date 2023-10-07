package best.azura.client.impl.module.impl.movement;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventCancelSprintCollision;
import best.azura.client.impl.events.EventChangeSprintState;
import best.azura.client.impl.events.EventJump;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.impl.module.impl.player.Scaffold;
import best.azura.client.impl.value.BooleanValue;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;

@SuppressWarnings("unused")
@ModuleInfo(name = "Sprint", category = Category.MOVEMENT, description = "Makes your Player sprint")
public class Sprint extends Module {

	private final BooleanValue omnidirectional = new BooleanValue("Omnidirectional", "Omnidirectional Sprint", false);
	public static boolean disable = false;

	@EventHandler
	public final Listener<EventUpdate> motionListener = eventPost -> {
		Scaffold scaffold = (Scaffold) Client.INSTANCE.getModuleManager().getModule(Scaffold.class);
		if (!scaffold.sprint.getObject() && mc.thePlayer.isSprinting() && scaffold.isEnabled()) return;
		if (mc.thePlayer.isMovingForward() || (omnidirectional.getObject() && mc.thePlayer.isMoving()))
			mc.thePlayer.setSprinting(true);
		if (disable) mc.thePlayer.setSprinting(false);
		mc.gameSettings.keyBindSprint.pressed = false;
	};

	@EventHandler
	public final Listener<EventCancelSprintCollision> eventCancelSprintCollisionListener = e -> {
		if (!mc.thePlayer.isCollidedHorizontally) e.cancelSprint = false;
	};

	@EventHandler
	public final Listener<EventJump> eventJumpListener = e -> {
		if (omnidirectional.getObject()) e.setYaw((float)
				Math.toDegrees(mc.thePlayer.getDirection()));
	};

	@EventHandler
	public final Listener<EventChangeSprintState> eventChangeSprintStateListener = e -> e.omni = omnidirectional.getObject();

}
