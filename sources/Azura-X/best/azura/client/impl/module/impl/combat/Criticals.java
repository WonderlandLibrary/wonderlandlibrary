package best.azura.client.impl.module.impl.combat;

import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.other.DelayUtil;
import best.azura.client.util.player.MovementUtil;
import best.azura.client.impl.value.ModeValue;
import best.azura.client.impl.value.NumberValue;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name = "Criticals", category = Category.COMBAT, description = "Make more damage")
public class Criticals extends Module {

	public static final DelayUtil delay = new DelayUtil();
	private final ModeValue mode = new ModeValue("Mode", "Mode for critical bypass", "Watchdog", "Watchdog", "Watchdog 2", "Funcraft", "NCP", "Motion", "Vulcan", "Verus");
	public static final NumberValue<Long> delayValue = new NumberValue<>("Delay", "Delay between critical-packets", 150L, 50L, 0L, 500L);
	public static final NumberValue<Float> motionHeight = new NumberValue<>("Motion height", "Change the height of the motion criticals", 12F, 1F, 12F, 42F);
	private int lastTick, tick;
	private Entity lastTarget;
	private long lastAttackMS;

	@EventHandler
	public final Listener<EventMotion> eventMotionListener = e -> {
		if (System.currentTimeMillis() - lastAttackMS > 1000 || mc.thePlayer.ticksExisted < 3) lastTarget = null;
		if (lastTarget != null) {
			switch (mode.getObject()) {
				case "Watchdog":
					if (mc.thePlayer.onGround) {
						e.y += 0.003 + MovementUtil.getWatchdogUnpatchValues();
						if (mc.thePlayer.ticksExisted % 2 == 0) e.y += 0.004 + MovementUtil.getWatchdogUnpatchValues();
						e.onGround = mc.thePlayer.motionY > 0 || MathUtil.getDifference(mc.thePlayer.posY, mc.thePlayer.lastTickPosY) > 0;
					} else if (mc.thePlayer.motionY > 0) {
						e.y -= 0.0003 + MovementUtil.getWatchdogUnpatchValues();
						if (mc.thePlayer.ticksExisted % 2 == 0) e.y += 0.0004 + MovementUtil.getWatchdogUnpatchValues();
					}
					break;
				case "Watchdog 2":
					if (mc.thePlayer.onGround) {
						e.y += Math.random() / 300;
						e.onGround = mc.thePlayer.motionY > 0 || MathUtil.getDifference(mc.thePlayer.posY, mc.thePlayer.lastTickPosY) > 0;
					}
					break;
			}
		}
	};

	@EventHandler
    public final Listener<EventSentPacket> eventSendPacketListener = eventSendPacket -> {
		setSuffix(mode.getObject());
		if (eventSendPacket.getPacket() instanceof C02PacketUseEntity) {
			C02PacketUseEntity packet = eventSendPacket.getPacket();
			if (packet.getAction() != C02PacketUseEntity.Action.ATTACK) return;
			lastTarget = packet.getEntityFromWorld(mc.theWorld);
			lastAttackMS = System.currentTimeMillis();

			double motionY = MathUtil.getDifference(mc.thePlayer.posY, mc.thePlayer.lastTickPosY);
			final double[] funcraftOffsets = {
					0.0753f,
					0.015555072702198913,
					0.017078707721880448,
					0.024813599859 },
					watchdogOffsets = { 0.10000000298023224 + Math.random() / 551,
							0.14000000432133675 + Math.random() / 1000.0F,
							0.0028000029653298952 + Math.random() / 1000.0F },
					ncpOffsets = {0.41999998688697815, 0.24813599859094576, 0.08307781780646721},
					vulcanOffsets = {0.41999998688697815, 0.41999998688697815 - 0.0784000015258789, 0.08307781780646721};
			if (mc.thePlayer.onGround && delay.hasReached(delayValue.getObject()) && mc.thePlayer.motionY < 0 && motionY <= 0 &&
					!mc.thePlayer.isOnLadder() && !mc.theWorld.isAnyLiquid(mc.thePlayer.getEntityBoundingBox()) && lastTick != mc.thePlayer.ticksExisted) {
				switch (mode.getObject()) {
					case "Funcraft":
					case "FunCraft":
						handleOffsets(funcraftOffsets);
						break;
					case "Watchdog":
						//handleOffsets(watchdogOffsets);
						break;
					case "Vulcan":
						for (double offset : vulcanOffsets) {
							mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
									mc.thePlayer.posY + offset, mc.thePlayer.posZ, offset == 0.41999998688697815 - 0.0784000015258789));
						}
						break;
					case "NCP":
						handleOffsets(ncpOffsets);
						break;
					case "Motion" :
						mc.thePlayer.motionY += motionHeight.getObject() * 0x1.47ae14p-7f;
						break;
				}
				delay.reset();
				lastTick = mc.thePlayer.ticksExisted;
			}
		}

	};

	protected void handleOffsets(final double[] offsets) {
		for (double offset : offsets) {
			mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
					mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
		}
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}
}

