package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Speed;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.other.ServerUtil;
import best.azura.client.util.player.MovementUtil;
import net.minecraft.potion.Potion;

public class WatchdogLowSpeedImpl implements ModeImpl<Speed> {
	private double speed;
	private int ticks;
	
	@Override
	public Speed getParent() {
		return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
	}
	
	@Override
	public String getName() {
		return "Watchdog Low";
	}
	
	@Override
	public void onEnable() {
		Client.INSTANCE.getEventBus().register(this);
		speed = 0;
		ticks = 0;
	}
	
	@Override
	public void onDisable() {
		Client.INSTANCE.getEventBus().unregister(this);
		speed = 0;
		ticks = 0;
	}
	
	@EventHandler
	public final Listener<EventMove> moveListener = e -> {
		if (!mc.thePlayer.isMoving() || mc.thePlayer.isCollidedHorizontally) speed = 0;
		final double value = mc.thePlayer.isMoving() ? Math.max(MovementUtil.getBaseSpeed(), mc.thePlayer.getSpeed()) : 0,
				yaw = mc.thePlayer.getDirection(),
				x = -Math.sin(yaw) * value,
				z = Math.cos(yaw) * value;
		//e.setX(x);
		//e.setZ(z);
		e.setX(x - (x - e.getX()) * (1.0 - 80F / 100));
		e.setZ(z - (z - e.getZ()) * (1.0 - 80F / 100));
		MovementUtil.setSpeed(mc.thePlayer.isMoving() ? Math.max(MovementUtil.getBaseSpeed(), mc.thePlayer.getSpeed()) : 0, e);
	};
	
	@EventHandler
	public final Listener<EventMotion> motionListener = e -> {
		if (ServerUtil.isHypixel())
			e.yaw = (float) Math.toDegrees(mc.thePlayer.getDirection());
		boolean doLowHop = !this.mc.thePlayer.isPotionActive(Potion.jump) &&
				this.mc.thePlayer.fallDistance <= 0.75F &&
				!this.mc.thePlayer.isCollidedHorizontally;
		if (mc.thePlayer.onGround) {
			if (mc.thePlayer.isMoving()) {
				mc.thePlayer.jump();
				if (!this.mc.thePlayer.isPotionActive(Potion.jump)) mc.thePlayer.motionY = 0.4;
				mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.65);
				if (ticks > 0 && ticks % 2 == 0) {
					getParent();
					mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.72);
				}
				ticks++;
			}
		} else if (mc.thePlayer.isMoving() && doLowHop) {
			final double groundOffset = MathUtil.round(this.mc.thePlayer.posY - (int) this.mc.thePlayer.posY, 3, 0.0001);
			if (groundOffset == MathUtil.round(0.4, 3, 0.0001)) {
				mc.thePlayer.motionY = 0.32;
			} else if (groundOffset == MathUtil.round(0.71, 3, 0.0001)) {
				mc.thePlayer.motionY = 0.04;
			} else if (groundOffset == MathUtil.round(0.75, 3, 0.0001)) {
				mc.thePlayer.motionY = -0.2;
			} else if (groundOffset == MathUtil.round(0.55, 3, 0.0001)) {
				mc.thePlayer.motionY = -0.15;
			} else if (groundOffset == MathUtil.round(0.41, 3, 0.0001)) {
				mc.thePlayer.motionY = -0.2;
			}
		}
		if (!mc.thePlayer.isMoving()) speed = 0;
	};
}