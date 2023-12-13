package best.azura.client.impl.module.impl.movement;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.module.impl.combat.TargetStrafe;
import best.azura.client.impl.module.impl.movement.speed.*;
import best.azura.client.util.modes.ModeUtil;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.ModeValue;
import best.azura.client.impl.value.NumberValue;

import java.util.Arrays;

@ModuleInfo(name = "Speed", category = Category.MOVEMENT, description = "sped.")
public class Speed extends Module {
	
	public static final ModeValue mode = new ModeValue("Mode", "Change the mode of the Speed", "");
	public static final NumberValue<Float> speedValue = new NumberValue<>("Boost", "Speed value", 1.75F, 0.1F, 0.1F, 10.0F);
	public final BooleanValue noBobValue = new BooleanValue("No Bob", "Removes view-bobbing while using speed", () -> mc.gameSettings.viewBobbing, false);
	
	public Speed() {
		super();
		ModeUtil.registerModuleModes(this, Arrays.asList(
						new WatchdogSpeedImpl(),
						new NCPSpeedImpl(),
						new MineplexSpeedImpl(),
						new VanillaSpeedImpl(),
						new AntiACSpeedImpl(),
						new PacketGroundSpeedImpl(),
						new PacketGround2SpeedImpl(),
						new BridgerLandSpeedImpl(),
						new VerusSpeedImpl(),
						new VerusLowSpeedImpl(),
						new VerusGroundSpeedImpl(),
						new SpartanB439SpeedImpl(),
						new SpartanB439GroundSpeedImpl(),
						new MatrixSpeedImpl(),
						new FuncraftSpeedImpl(),
						new FuncraftGroundSpeedImpl(),
						new FuncraftLowSpeedImpl(),
						new RedeDarkSpeedImpl(),
						new MinelandLowSpeedImpl(),
						new VulcanSpeedImpl(),
						new MercurySpeedImpl(),
						new MinemoraSpeedImpl(),
						new MorganSpeedImpl()),
				mode, true);
	}
	
	@SuppressWarnings("unused")
	@EventHandler
	public final Listener<EventMove> eventMoveListener = e -> {
		double currSpeed = Math.sqrt(e.getX() * e.getX() + e.getZ() * e.getZ());
		if (Client.INSTANCE.getModuleManager().getModule(TargetStrafe.class).isEnabled())
			TargetStrafe.setSpeedForStrafe(e, currSpeed);
	};
	
	@EventHandler
	public Listener<EventMotion> eventMotionListener = eventMotion -> {
		if (eventMotion.isPre()) {
			setSuffix(mode.getObject());
			if (noBobValue.getObject())
				mc.thePlayer.cameraYaw = mc.thePlayer.cameraPitch = 0;
		}
	};
	
	@Override
	public void onEnable() {
		ModeUtil.onEnable(this);
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		ModeUtil.onDisable(this);
		mc.timer.timerSpeed = 1.0f;
		super.onDisable();
	}
}
