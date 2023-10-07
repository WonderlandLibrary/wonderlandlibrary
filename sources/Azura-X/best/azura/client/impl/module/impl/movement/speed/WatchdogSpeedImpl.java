package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.*;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.combat.TargetStrafe;
import best.azura.client.impl.module.impl.movement.Speed;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.other.ServerUtil;
import best.azura.client.util.player.MovementUtil;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.NumberValue;
import best.azura.client.api.value.Value;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.potion.Potion;

import java.util.Arrays;
import java.util.List;

public class WatchdogSpeedImpl implements ModeImpl<Speed> {
	private int ticks;
	private final BooleanValue timerValue = new BooleanValue("Timer Boost", "Boost your speed using timer", true);
	private final NumberValue<Float> timerSpeedValue = new NumberValue<>("Timer Speed", "Change the timer amount", timerValue::getObject, 1.0F, 0.1F, 0.5F, 2.0F);
	private final BooleanValue damageBoostValue = new BooleanValue("Damage Boost", "Boost your speed when receiving damage", false);
	private final NumberValue<Double> strafingStrength = new NumberValue<>("Strafing Strength", "Strength of strafe", 50.0, 0.0, 100.0);
	private final BooleanValue strafeBypass = new BooleanValue("Strafe bypass", "Bypass the watchdog strafe lag-back checks", false);
	private final BooleanValue realStrafeBypass = new BooleanValue("Real Strafe", "Bypass the watchdog strafe lag-back checks in another way", false);
	private final BooleanValue lowHop = new BooleanValue("Low Hop", "Low hop instead of a full bhop", true);
	private final BooleanValue fastFall = new BooleanValue("Fast Fall", "Fall faster", () -> !lowHop.getObject(), false);
	private final BooleanValue slow = new BooleanValue("Slow", "Slow the bhop down in order for it to bypass better", () -> !lowHop.getObject(), true);

	@Override
	public List<Value<?>> getValues() {
		return Arrays.asList(
				timerValue,
				timerSpeedValue,
				damageBoostValue,
				strafeBypass,
				strafingStrength,
				realStrafeBypass,
				lowHop,
				fastFall,
				slow
		);
	}
	
	@Override
	public Speed getParent() {
		return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
	}
	
	@Override
	public String getName() {
		return "Watchdog";
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
	public final Listener<EventStrafe> eventStrafeListener = e -> {
		if (!realStrafeBypass.getObject()) return;
		float speed = (float) Math.max(MovementUtil.getBaseSpeed() * 1.12f, mc.thePlayer.getSpeed() * (mc.thePlayer.moveStrafing != 0 && mc.thePlayer.moveForward != 0 ? 1.12f : 1.15f));
		MovementUtil.setMotionPartialStrafe(e, speed, (float) (0.235 + MovementUtil.getWatchdogUnpatchValues()));
	};

	@EventHandler
	public final Listener<EventMove> moveListener = e -> {
		final double value = mc.thePlayer.isMoving() ? Math.max(MovementUtil.getBaseSpeed(), mc.thePlayer.getSpeed()) : 0,
				yaw = mc.thePlayer.getDirection(),
				x = -Math.sin(yaw) * value,
				z = Math.cos(yaw) * value;
		e.setX(x - (x - e.getX()) * (1.0 - strafingStrength.getObject() / 100));
		e.setZ(z - (z - e.getZ()) * (1.0 - strafingStrength.getObject() / 100));
	};
	@EventHandler
	public final Listener<EventReceivedPacket> eventReceivedPacketListener = e -> {
		if (e.getPacket() instanceof S27PacketExplosion) {
			S27PacketExplosion s = e.getPacket();
			double motion = Math.abs(s.func_149149_c()) + Math.abs(s.func_149147_e());
			if (motion >= 0.3 && damageBoostValue.getObject()) {
				e.setCancelled(true);
				double speed = Math.sqrt(s.func_149149_c() * s.func_149149_c() + s.func_149147_e() * s.func_149147_e());
				mc.thePlayer.setSpeed(mc.thePlayer.getSpeed() + speed * 0.8);
			}
		}
		if (e.getPacket() instanceof S12PacketEntityVelocity) {
			S12PacketEntityVelocity s = e.getPacket();
			final double x = s.getMotionX() / 8000.0D, z = s.getMotionZ() / 8000.0D;
			if (s.getEntityID() != mc.thePlayer.getEntityId()) return;
			double motion = Math.abs(x) + Math.abs(z);
			if (motion >= 0.2 && damageBoostValue.getObject()) {
				e.setCancelled(true);
				double speed = Math.sqrt(x * x + z * z);
				mc.thePlayer.setSpeed(mc.thePlayer.getSpeed() + speed * 0.8);
			}
		}
		if (e.getPacket() instanceof S08PacketPlayerPosLook) {
			Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Disabled", "Disabled due to lag back.", 1000, Type.INFO));
			getParent().setEnabled(false);
		}
	};
	
	@EventHandler
	public final Listener<EventJump> eventJumpListener = e -> e.setSpeed(0);
	
	@EventHandler
	public final Listener<EventMotion> motionListener = e -> {
		boolean doLowHop = !this.mc.thePlayer.isPotionActive(Potion.jump) &&
				this.mc.thePlayer.fallDistance <= 0.75F &&
				!this.mc.thePlayer.isCollidedHorizontally;
		final TargetStrafe targetStrafe = (TargetStrafe) Client.INSTANCE.getModuleManager().getModule(TargetStrafe.class);
		if ((strafeBypass.getObject() ||(targetStrafe.isEnabled() && targetStrafe.isStrafingDo() && realStrafeBypass.getObject())) && ServerUtil.isHypixel())
			e.yaw = (float) Math.toDegrees(mc.thePlayer.getDirection());
		if (timerValue.getObject())
			mc.timer.timerSpeed = timerSpeedValue.getObject();
		if (mc.thePlayer.motionY < 0.0)
			mc.timer.timerSpeed = 1;
		if (!lowHop.getObject()) {
			if (mc.thePlayer.onGround) {
				if (mc.thePlayer.isMoving()) {
					e.y = mc.thePlayer.posY;
					e.onGround = true;
					double prevSpeed = mc.thePlayer.getSpeed();
					mc.thePlayer.jump();
					mc.thePlayer.setSpeed(prevSpeed);

					switch (ticks) {
						case 0:
						case 6:
						case 7:
							mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.65);
							ticks++;
							break;
						case 1:
							mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.77);
							ticks++;
							break;
						case 2:
							mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.61);
							ticks++;
							break;
						case 3:
							mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.8);
							ticks++;
							break;
						case 4:
						case 5:
							mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.7);
							ticks++;
							break;
						case 8:
							mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.62);
							ticks = 0;
							break;
					}

					if (slow.getObject())
						mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() + 0.46 - 0.2873);

					if (mc.thePlayer.isPotionActive(Potion.moveSpeed))
						mc.thePlayer.setSpeed(Math.min(mc.thePlayer.getSpeed() * 1.2, 0.8));
				}
			} else if (mc.thePlayer.isMoving() && !e.isUpdate() && fastFall.getObject()) {
				if (mc.thePlayer.motionY < 0.1 && mc.thePlayer.motionY > -0.25 && mc.thePlayer.fallDistance < 0.1) {
					mc.thePlayer.motionY -= 0.05;
				}
			}
		} else {
			if (mc.thePlayer.onGround) {
				if (mc.thePlayer.isMoving()) {
					mc.thePlayer.jump();
					if (!this.mc.thePlayer.isPotionActive(Potion.jump)) mc.thePlayer.motionY = 0.4;
					mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.6);
					if (ticks > 0 && ticks % 2 == 0) {
						getParent();
						mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.76);
					}
					ticks++;
				}
			} else if (mc.thePlayer.isMoving() && doLowHop) {
				final double groundOffset = MathUtil.round(this.mc.thePlayer.posY - (int) this.mc.thePlayer.posY, 3, 0.0001);
				if (groundOffset == MathUtil.round(0.4, 3, 0.0001)) {
					mc.thePlayer.motionY = 0.31;
				} else if (groundOffset == MathUtil.round(0.71, 3, 0.0001)) {
					mc.thePlayer.motionY = 0.04;
				} else if (groundOffset == MathUtil.round(0.75, 3, 0.0001)) {
					mc.thePlayer.motionY = -0.2;
				} else if (groundOffset == MathUtil.round(0.55, 3, 0.0001)) {
					mc.thePlayer.motionY = -0.14;
				} else if (groundOffset == MathUtil.round(0.41, 3, 0.0001)) {
					mc.thePlayer.motionY = -0.2;
				}
			}
		}
		if (!mc.thePlayer.isMoving()) {
			ticks = 0;
		}
	};
}