package best.azura.client.impl.module.impl.movement;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.eventbus.core.EventPriority;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.module.impl.combat.TargetStrafe;
import best.azura.client.impl.module.impl.movement.flight.*;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.util.modes.ModeUtil;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.ModeValue;
import best.azura.client.impl.value.NumberValue;

import java.util.Arrays;

@ModuleInfo(name = "Flight", category = Category.MOVEMENT, description = "Makes you move faster", keyBind = 0)
public class Flight extends Module {
	
	public static final ModeValue mode = new ModeValue("Mode", "Lets you fly.", "");
	public static final NumberValue<Double> speedValue = new NumberValue<>("Speed", "Speed value", 1.7D, 0.1, 0.1, 10D);
	public final NumberValue<Float> bobbing = new NumberValue<>("View bobbing strength", "Strength of the view bobbing.", 0.0F, 1F, 0.F, 100F);
	public static final BooleanValue silentDamage = new BooleanValue("Silent damage", "Sound and hurt cam will not be rendered or played", false);
	
	public Flight() {
		super();
		ModeUtil.registerModuleModes(this, Arrays.asList(
						new FlightMotionImpl(),
						new FlightMushMCImpl(),
						new FlightPacketImpl(),
						new FlightTNTHighJumpImpl(),
						new FlyMorganImpl(),
						new FlightWatchdogNewerImpl(),
						new FlightFuncraftImpl(),
						new FlightNoRulesImpl(),
						new FlightTeamMLGImpl(),
						new FlightVerusFreeCamImpl(),
						new FlightAirJumpImpl(),
						new FlightAirWalkImpl(),
						new FlightRedeDarkImpl(),
						new FlightMinelandImpl(),
						new FlightOldNCPGlideImpl(),
						new FlightAntiACImpl(),
						new FlightVerusDamageImpl(),
						new FlightDamageImpl(),
						new FlightVerusOtherImpl(),
						new FlightHycraftImpl(),
						new FlightSpartanB456Impl(),
						new FlightMinemoraImpl(),
						new FlightMinemenImpl()
				),
				mode, true);
	}
	
	@EventHandler(EventPriority.LOWER)
	public Listener<EventMove> eventMoveListener = e -> {
		setSuffix(mode.getObject());
		double currSpeed = Math.sqrt(e.getX() * e.getX() + e.getZ() * e.getZ());
		if (Client.INSTANCE.getModuleManager().getModule(TargetStrafe.class).isEnabled())
			TargetStrafe.setSpeedForStrafe(e, currSpeed);
	};
	
	@EventHandler(EventPriority.LOWER)
	public Listener<EventMotion> eventMotionListener = e -> {
		if (mc.thePlayer.isMoving() && bobbing.getObject() != 0)
			mc.thePlayer.cameraYaw = bobbing.getObject() * 0.001F;
	};
	
	
	@Override
	public void onEnable() {
		Client.INSTANCE.getEventBus().register(this);
		ModeUtil.onEnable(this);
		if (Client.INSTANCE.getModuleManager().getModule(LongJump.class).isEnabled()) {
			Client.INSTANCE.getModuleManager().getModule(LongJump.class).setEnabled(false);
			Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Flight", "Disabled Long Jump to reduce flags", 1500, Type.INFO));
		}
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		ModeUtil.onDisable(this);
	}
}
