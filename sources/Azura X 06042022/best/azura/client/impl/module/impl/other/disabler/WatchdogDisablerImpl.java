package best.azura.client.impl.module.impl.other.disabler;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.client.util.math.MathUtil;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.other.Disabler;
import best.azura.client.util.other.ServerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class WatchdogDisablerImpl implements ModeImpl<Disabler> {

	@Override
	public Disabler getParent() {
		return (Disabler) Client.INSTANCE.getModuleManager().getModule(Disabler.class);
	}

	@Override
	public String getName() {
		return "Watchdog";
	}

	@EventHandler
	public final Listener<EventSentPacket> eventSendPacketListener = e -> {
		if (!ServerUtil.isHypixel()) return;
		/*if (e.getPacket() instanceof C03PacketPlayer && mc.thePlayer.ticksExisted <= MathUtil.getRandom_int(5, 20)) {
			final C03PacketPlayer c03 = e.getPacket();
			c03.y += MathUtil.getRandom_double(-MathUtil.getRandom_double(1, 300), MathUtil.getRandom_double(1, 300));
			c03.x += MathUtil.getRandom_double(-MathUtil.getRandom_double(1, 300), MathUtil.getRandom_double(1, 300));
			c03.z += MathUtil.getRandom_double(-MathUtil.getRandom_double(1, 300), MathUtil.getRandom_double(1, 300));
			c03.yaw = MathUtil.getRandom_float(-180, 180);
			c03.pitch = MathUtil.getRandom_float(-90, 90);
			c03.onGround = ThreadLocalRandom.current().nextBoolean();
		}
		if (test.getObject()) return;

		if (e.getPacket() instanceof C0FPacketConfirmTransaction) {
			final C0FPacketConfirmTransaction c0F = e.getPacket();
			e.setPacket(new C0FPacketConfirmTransaction(c0F.getWindowId(), c0F.getUid(), ThreadLocalRandom.current().nextBoolean()));
			c0fAmount++;
		}
		if (e.getPacket() instanceof C00PacketKeepAlive) {
			final C00PacketKeepAlive c00 = e.getPacket();
			e.setPacket(new C00PacketKeepAlive(c00.getKey() + MathUtil.getRandom_int(-MathUtil.getRandom_int(3, 50), MathUtil.getRandom_int(3, 50))));
			c00Amount++;
		}
		if (e.getPacket() instanceof C03PacketPlayer && mc.thePlayer.ticksExisted > 20) {
			final C03PacketPlayer c03 = e.getPacket();
			final boolean c03Correct = (c03.x == 0. && c03.y == 0. && c03.z == 0.) ||
					(c03.x == mc.thePlayer.posX && c03.y == mc.thePlayer.posY && c03.z == mc.thePlayer.posZ);
			final boolean jumping = mc.thePlayer.onGround && mc.thePlayer.motionY > 0;
			if (!c03.rotating && MathUtil.getDifference(mc.thePlayer.posX, mc.thePlayer.lastTickPosX) == 0. &&
					MathUtil.getDifference(mc.thePlayer.posY, mc.thePlayer.lastTickPosY) == 0. &&
					MathUtil.getDifference(mc.thePlayer.posZ, mc.thePlayer.lastTickPosZ) == 0. && c03Correct && !mc.playerController.getIsHittingBlock()
					& !jumping && mc.thePlayer.ticksExisted % 10 != 0) {
				e.setCancelled(true);
				return;
			}
		}
		if (e.getPacket() instanceof C03PacketPlayer) {
			final C03PacketPlayer c03 = e.getPacket();
			final String channel0 = new String(MathUtil.getRandomBytes(3, 8, Byte.MIN_VALUE, Byte.MAX_VALUE)),
					channel0_1 = channel0.substring(0, MathUtil.getRandom_int(1, Math.min(8, channel0.length()))),
					channel1 = new String(MathUtil.getRandomBytes(3, 8, Byte.MIN_VALUE, Byte.MAX_VALUE));
			final ByteBuf buffer = Unpooled.buffer().writeDouble(c03.x).writeDouble(c03.y).writeDouble(c03.z).writeFloat(c03.yaw).writeFloat(c03.pitch);
			mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload(channel0_1 + "|" + channel1,
					new PacketBuffer(buffer)));
		}*/
		if (e.getPacket() instanceof C03PacketPlayer && mc.thePlayer.ticksExisted > 20) {
			final C03PacketPlayer c03 = e.getPacket();
			final boolean c03Correct = (c03.x == 0. && c03.y == 0. && c03.z == 0.) ||
					(c03.x == mc.thePlayer.posX && c03.y == mc.thePlayer.posY && c03.z == mc.thePlayer.posZ);
			final boolean jumping = mc.thePlayer.onGround && mc.thePlayer.motionY > 0;
			if (!c03.rotating && MathUtil.getDifference(mc.thePlayer.posX, mc.thePlayer.lastTickPosX) == 0. &&
					MathUtil.getDifference(mc.thePlayer.posY, mc.thePlayer.lastTickPosY) == 0. &&
					MathUtil.getDifference(mc.thePlayer.posZ, mc.thePlayer.lastTickPosZ) == 0. && c03Correct && !mc.playerController.getIsHittingBlock()
					& !jumping && mc.thePlayer.ticksExisted % 10 != 0) {
				e.setCancelled(true);
			}
		}
		if (mc.thePlayer.ticksExisted < 120 && e.getPacket() instanceof C03PacketPlayer)
			e.setCancelled(true);
	};

	@EventHandler
	public final Listener<EventReceivedPacket> eventReceivedPacketListener = e -> {
		if (e.getPacket() instanceof S08PacketPlayerPosLook) {
			final S08PacketPlayerPosLook s08 = e.getPacket();
			e.setCancelled(true);
			EntityPlayer entityplayer = mc.thePlayer;
			double d0 = s08.getX();
			double d1 = s08.getY();
			double d2 = s08.getZ();
			float f = s08.getYaw();
			float f1 = s08.getPitch();

			if (s08.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X)) d0 += entityplayer.posX;
			else entityplayer.motionX = 0.0D;

			if (s08.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y)) d1 += entityplayer.posY;
			else entityplayer.motionY = 0.0D;

			if (s08.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Z)) d2 += entityplayer.posZ;
			else entityplayer.motionZ = 0.0D;

			if (s08.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X_ROT)) f1 += entityplayer.rotationPitch;

			if (s08.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT)) f += entityplayer.rotationYaw;

			entityplayer.setPositionAndRotation(d0, d1, d2, f, f1);
			mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(s08.x + Math.random() / 100.0, s08.y + Math.random() / 100.0, s08.z + Math.random() / 100.0, false));

			if (!mc.getNetHandler().doneLoadingTerrain)
			{
				mc.thePlayer.prevPosX = mc.thePlayer.posX;
				mc.thePlayer.prevPosY = mc.thePlayer.posY;
				mc.thePlayer.prevPosZ = mc.thePlayer.posZ;
				mc.getNetHandler().doneLoadingTerrain = true;
				mc.displayGuiScreen(null);
			}
			//lastLagBackTime = System.currentTimeMillis();
		}
	};

	@Override
	public void onEnable() {

	}

	@Override
	public void onDisable() {

	}
}