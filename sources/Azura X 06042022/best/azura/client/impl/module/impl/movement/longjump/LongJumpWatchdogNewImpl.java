package best.azura.client.impl.module.impl.movement.longjump;

import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.*;
import best.azura.client.util.other.ChatUtil;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.LongJump;
import best.azura.client.util.color.ColorUtil;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.player.MovementUtil;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.impl.value.NumberValue;
import best.azura.client.api.value.Value;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.util.Vec3;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class LongJumpWatchdogNewImpl implements ModeImpl<LongJump> {

	private double speed;
	private int tick;
	private int stage;
	private Vec3 startPos;

	private final NumberValue<Integer> jumps = new NumberValue<>("Jumps", "Amount of jumps for damage", 4, 1, 1, 40);
	private final NumberValue<Double> jumpHeightMultiplier = new NumberValue<>("Jump height multiplier", "Multiplier for jump height", 1.0, 0.1, 0.1, 1.0);
	private final NumberValue<Double> speedMultiplier = new NumberValue<>("Speed multiplier", "Multiplier for speed", 1.0, 0.01, 1.0, 1.5);
	private final NumberValue<Double> jumpMotion = new NumberValue<>("Jump Motion", "Jump motion", 0.82, 0.01, 0.42, 1.0);

	@Override
	public List<Value<?>> getValues() {
		return Arrays.asList(jumps, jumpHeightMultiplier, speedMultiplier, jumpMotion);
	}

	@Override
	public LongJump getParent() {
		return (LongJump) Client.INSTANCE.getModuleManager().getModule(LongJump.class);
	}

	@Override
	public String getName() {
		return "Watchdog";
	}

	@Override
	public void onEnable() {
		speed = 0;
		tick = stage = 0;
		startPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
	}

	@Override
	public void onDisable() {
		mc.timer.timerSpeed = 1.0f;
		try {
			final double dffX = MathUtil.getDifference(startPos.xCoord, mc.thePlayer.posX),
					dffZ = MathUtil.getDifference(startPos.zCoord, mc.thePlayer.posZ),
					distXZ = Math.sqrt(dffX * dffX + dffZ * dffZ);
			ChatUtil.sendChat("Distance: " + distXZ);
		} catch (Exception ignored) {
		}
	}

	@EventHandler
	private void onEvent(final Event event) {
		if (event instanceof EventReceivedPacket) {
			final EventReceivedPacket e = (EventReceivedPacket) event;
			if (e.getPacket() instanceof S08PacketPlayerPosLook) {
				getParent().setEnabled(false);
				Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Long Jump", "Disabled due to teleport", 3000, Type.WARNING));
			}
		}
		if (event instanceof EventMotion) {
			final EventMotion e = (EventMotion) event;
			e.sprinting = true;
			if (stage == 0 && e.isPre() && mc.thePlayer.onGround) {
				if (tick < jumps.getObject()) {
					e.onGround = false;
					mc.thePlayer.jump();
					mc.thePlayer.motionY *= jumpHeightMultiplier.getObject();
					tick++;
				}
				if (tick == jumps.getObject()) {
					e.onGround = true;
					stage++;
				}
			}
			if (stage == 0) {
				mc.thePlayer.cameraYaw = mc.thePlayer.cameraPitch = 0;
				mc.thePlayer.posY = mc.thePlayer.lastTickPosY;
			}
		}
		if (event instanceof EventReceivedPacket) {
			final EventReceivedPacket e = (EventReceivedPacket) event;
			if (e.getPacket() instanceof S12PacketEntityVelocity) {
				S12PacketEntityVelocity s = e.getPacket();
				if (s.getEntityID() != mc.thePlayer.getEntityId()) return;
				e.setCancelled(true);
			}
		}
		if (event instanceof EventRender2D) {
			RenderUtil.INSTANCE.scaleFix(1.0);
			RenderUtil.INSTANCE.drawRect(mc.displayWidth / 2.0 - 101, mc.displayHeight / 2.0 + 39, mc.displayWidth / 2.0 + 101, mc.displayHeight / 2.0 + 43, new Color(13, 14, 13));
			RenderUtil.INSTANCE.drawRect(mc.displayWidth / 2.0 - 100, mc.displayHeight / 2.0 + 40, mc.displayWidth / 2.0 - 100 + (200 * (tick / jumps.getObject().doubleValue())), mc.displayHeight / 2.0 + 42, ColorUtil.getLastHudColor());
			RenderUtil.INSTANCE.invertScaleFix(1.0);
			
		}
		if (event instanceof EventMove) {
			final EventMove e = (EventMove) event;
			final boolean speedPot = mc.thePlayer.isPotionActive(Potion.moveSpeed);
			switch (stage) {
				case 0:
					MovementUtil.setSpeed(0, e);
					break;
				case 1:
					speed = MovementUtil.getBaseSpeed() * (speedPot ? 1.55 : 1.4) * (mc.thePlayer.isPotionActive(Potion.jump) ? 1.0 : speedMultiplier.getObject());
					e.setY(mc.thePlayer.motionY = jumpMotion.getObject().floatValue() - 0.01f);
					if (mc.thePlayer.isPotionActive(Potion.jump)) {
						e.setY(mc.thePlayer.motionY *= 1.0 + (float) (mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.25F);
					}
					stage++;
					break;
				case 2:
					e.setY(mc.thePlayer.motionY += MathUtil.getRandom_double(2.0E-8D, 2.0E-4D));
					if (mc.thePlayer.motionY < 0.2) e.setY(mc.thePlayer.motionY = e.getY() + 0.025);
					if (mc.thePlayer.onGround) getParent().setEnabled(false);
					if (mc.thePlayer.isMoving())
						MovementUtil.setSpeed(Math.max(MovementUtil.getBaseSpeed(), speed -= speed / 65), e);
					else MovementUtil.setSpeed(speed = 0, e);
					break;
			}
		}
	}
}