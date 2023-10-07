package best.azura.client.impl.module.impl.movement.longjump;

import best.azura.client.api.module.ModeImpl;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.api.value.Value;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.client.impl.events.EventRender2D;
import best.azura.client.impl.module.impl.movement.LongJump;
import best.azura.client.impl.value.NumberValue;
import best.azura.client.util.color.ColorUtil;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.other.ChatUtil;
import best.azura.client.util.player.MovementUtil;
import best.azura.client.util.render.RenderUtil;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.util.Vec3;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class LongJumpWatchdogNewNoDMGImpl implements ModeImpl<LongJump> {

	private double speed;
	private int tick;
	private int stage;
	private Vec3 startPos;

	@Override
	public LongJump getParent() {
		return (LongJump) Client.INSTANCE.getModuleManager().getModule(LongJump.class);
	}

	@Override
	public String getName() {
		return "Watchdog Simple";
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
		if (event instanceof EventMove) {
			final EventMove e = (EventMove) event;
			switch (stage) {
				case 0:
					speed = MovementUtil.getBaseSpeed() * 1.73;
					e.setY(mc.thePlayer.motionY = 0.42f);
					if (mc.thePlayer.isPotionActive(Potion.jump)) {
						e.setY(mc.thePlayer.motionY *= 1.0 + (float) (mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.25F);
					}
					if (mc.thePlayer.isMoving())
						MovementUtil.setSpeed(Math.max(MovementUtil.getBaseSpeed(), speed), e);
					stage++;
					break;
				case 1:
					if (mc.thePlayer.motionY < 0) e.setY(mc.thePlayer.motionY = e.getY() + 0.025);
					if (mc.thePlayer.onGround) getParent().setEnabled(false);
					if (mc.thePlayer.isMoving())
						MovementUtil.setSpeed(Math.max(MovementUtil.getBaseSpeed(), speed -= speed / 60), e);
					else MovementUtil.setSpeed(speed = 0, e);
					break;
			}
		}
	}
}