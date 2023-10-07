package best.azura.client.impl.module.impl.movement;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMove;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.module.impl.combat.TargetStrafe;
import best.azura.client.impl.module.impl.movement.longjump.*;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.util.modes.ModeUtil;
import best.azura.client.impl.value.ModeValue;
import best.azura.client.impl.value.NumberValue;

import java.util.Arrays;

@ModuleInfo(name = "Long Jump", category = Category.MOVEMENT, description = "Makes you move faster", keyBind = 0)
public class LongJump extends Module {

	public final ModeValue mode = new ModeValue("Mode", "Lets you jump longer distances than legit.", "");
	public final NumberValue<Double> speedValue = new NumberValue<>("Speed", "Speed value", 1.7D, 0.1, 0.1, 5.);

	public LongJump() {
		super();
		ModeUtil.registerModuleModes(this, Arrays.asList(
						new LongJumpVanillaImpl(), new LongJumpWatchdogBowImpl(), new LongJumpWatchdogNewImpl(), new LongJumpWatchdogNewNoDMGImpl(),
						new LongJumpBlocksMCImpl(), new LongJumpFunCraftImpl(), new LongJumpMineLandImpl(),
						new LongJumpSpartanB453Impl(), new LongJumpSpartanB456Impl(),
						new LongJumpRedeSkyImpl()),
				mode, true);
	}

	@EventHandler
	public Listener<EventMove> eventMoveListener = e -> {
		setSuffix(mode.getObject());
		double currSpeed = Math.sqrt(e.getX() * e.getX() + e.getZ() * e.getZ());
		if (Client.INSTANCE.getModuleManager().getModule(TargetStrafe.class).isEnabled())
			TargetStrafe.setSpeedForStrafe(e, currSpeed);
	};

	@Override
	public void onEnable() {
		if (Client.INSTANCE.getModuleManager().getModule(Flight.class).isEnabled()) {
			Client.INSTANCE.getModuleManager().getModule(Flight.class).setEnabled(false);
			Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Long Jump", "Disabled Flight to reduce flags", 1500, Type.INFO));
		}
		ModeUtil.onEnable(this);
		super.onEnable();
	}

	@Override
	public void onDisable() {
		ModeUtil.onDisable(this);
		super.onDisable();
	}
}
