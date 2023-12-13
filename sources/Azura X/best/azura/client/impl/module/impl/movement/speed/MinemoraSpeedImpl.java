package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.api.module.ModeImpl;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.api.value.Value;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.*;
import best.azura.client.impl.module.impl.combat.TargetStrafe;
import best.azura.client.impl.module.impl.movement.Speed;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.NumberValue;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.other.ServerUtil;
import best.azura.client.util.player.MovementUtil;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.potion.Potion;

import java.util.Arrays;
import java.util.List;

public class MinemoraSpeedImpl implements ModeImpl<Speed> {
	private int ticks;
	private final BooleanValue lowHop = new BooleanValue("Low Hop", "Low hop instead of a full bhop", true);
	private final BooleanValue fast = new BooleanValue("Fast", "zoom", true);

	@Override
	public List<Value<?>> getValues() {
		return Arrays.asList(lowHop, fast);
	}
	
	@Override
	public Speed getParent() {
		return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
	}
	
	@Override
	public String getName() {
		return "Minemora";
	}
	
	@Override
	public void onEnable() {
		Client.INSTANCE.getEventBus().register(this);
		ticks = 0;
	}
	
	@Override
	public void onDisable() {
		Client.INSTANCE.getEventBus().unregister(this);
		mc.timer.timerSpeed = 1.0f;
		ticks = 0;
	}

	@EventHandler
	public final Listener<EventMotion> motionListener = e -> {
		if (e.isUpdate()) {
			if (mc.thePlayer.isMoving()) {
				mc.thePlayer.setSpeed(mc.thePlayer.getSpeed());
				mc.timer.timerSpeed = 1f;
				if (mc.thePlayer.onGround) {
					mc.timer.timerSpeed = MathUtil.getRandom_float(1.0f, 1.4f);
					final double speed = mc.thePlayer.getSpeed();
					mc.thePlayer.jump();
					mc.thePlayer.setSpeed(speed + 0.2);
					mc.thePlayer.motionY = 0.4;
				} else if (mc.thePlayer.fallDistance < 2.0) {
					if (fast.getObject())
						mc.thePlayer.setSpeed(mc.thePlayer.getSpeed() * 1.02);
					if (lowHop.getObject() && mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
							mc.thePlayer.getEntityBoundingBox().expand(
									-Math.sin(mc.thePlayer.getDirection()), 0, Math.cos(mc.thePlayer.getDirection()))).isEmpty())
						mc.thePlayer.motionY -= 0.0205;
				}
			}
		}
	};
}