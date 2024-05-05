package best.azura.client.util.player;

import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.events.EventStrafe;
import best.azura.client.util.math.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;

import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
public class MovementUtil {
	
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	public static final double AIR_FRICTION = 0.9800000190734863;
	public static final double GROUND_FRICTION = 0.20000000298023224;
	public static final double WATER_FRICTION = 0.800000011920929;
	public static final double PLAYER_JUMP_HEIGHT = 0.41999998688697815;
	
	public static void damagePlayerHypixel2() {
		double value = ThreadLocalRandom.current().nextDouble(0.04, 0.06);
		for (double d = 0; d <= mc.thePlayer.getMaxFallHeight(); d += value) {
			spoof(0.0001, false);
			spoof(value, false);
		}
		spoof(0.00001, false);
	}
	
	public static void damagePlayerHypixel() {
		damagePlayerHypixel(1);
	}
	
	public static void damagePlayerHypixel(double multiplier) {
		final PotionEffect potion = mc.thePlayer.getActivePotionEffect(Potion.jump);
		int add = mc.thePlayer.getMaxFallHeight() + (potion != null ? potion.getAmplifier() + 1 : 0);
		final ItemStack stack = mc.thePlayer.getCurrentArmor(0);
		if (mc.thePlayer.getCurrentArmor(0) != null)
			add += EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, stack);
		for (double d = 0; d < add * multiplier; d += 0.1) {
			spoof(MathUtil.getRandom_double(5.8E-2, 6.3E-2) + getWatchdogUnpatchValues(), false);
			spoof(MathUtil.getRandom_double(1.0E-20, 9.0E-20) + getWatchdogUnpatchValues(), false);
			spoof(MathUtil.getRandom_double(4.3E-2, 5.3E-2) + getWatchdogUnpatchValues(), false);
			spoof(MathUtil.getRandom_double(1.0E-20, 9.0E-20) + getWatchdogUnpatchValues(), false);
		}
	}
	
	public static double getWatchdogUnpatchValues() {
		double value = 1.0 / System.currentTimeMillis();
		for (int i = 0; i < MathUtil.getRandom_int(4, 20); i++)
			value *= 1.0 / System.currentTimeMillis();
		return value;
	}
	
	public static void vClip(double value) {
		mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + value, mc.thePlayer.posZ);
	}
	
	public static boolean isOverVoid() {
		if (mc.thePlayer.capabilities.isFlying || mc.thePlayer.capabilities.allowFlying) return false;
		/*for (int i = 2; i >= -100; i--)
			if (!mc.theWorld.isAirBlock(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + i, mc.thePlayer.posZ)))
				return false;*/
		for (int i = 2; i >= -100; i--) {
			for (double xx = -mc.thePlayer.getSpeed() * 10; xx <= mc.thePlayer.getSpeed() * 10; xx++) {
				for (double zz = -mc.thePlayer.getSpeed() * 10; zz <= mc.thePlayer.getSpeed() * 10; zz++) {
					double x = (-(Math.sin(mc.thePlayer.getDirection()) * xx));
					double z = ((Math.cos(mc.thePlayer.getDirection()) * xx));
					if (!mc.theWorld.isAirBlock(new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + i, mc.thePlayer.posZ + z)))
						return false;
				}
			}
		}
		return true;
	}
	
	public static void hClip(double value) {
		double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
		double x = -Math.sin(yaw) * value;
		double z = Math.cos(yaw) * value;
		mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
	}
	
	public static void spoof(double x, double y, double z, boolean ground) {
		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z, ground));
	}
	
	public static void spoof(double y, boolean ground) {
		spoof(0, y, 0, ground);
	}
	
	public static void spoof2(double x, double y, double z, float yaw, float pitch, boolean ground) {
		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z, yaw, pitch, ground));
	}
	
	public static void spoof2(double y, boolean ground) {
		spoof2(0, y, 0, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, ground);
	}
	
	public static void setSpeed(boolean teleport, double value) {
		double yaw = mc.thePlayer.getDirection();
		double x = -Math.sin(yaw) * value;
		double z = Math.cos(yaw) * value;
		if (teleport) {
			mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
		} else {
			mc.thePlayer.motionZ = z;
			mc.thePlayer.motionX = x;
		}
	}
	
	public static void setSpeed(double value, EventMove e) {
		double yaw = mc.thePlayer.getDirection();
		double x = -Math.sin(yaw) * value;
		double z = Math.cos(yaw) * value;
		e.setX(x);
		e.setZ(z);
	}
	
	public static void boost(double value) {
		double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
		mc.thePlayer.motionX = -Math.sin(yaw) * value;
		mc.thePlayer.motionZ = Math.cos(yaw) * value;
	}
	
	public static float getBaseSpeed() {
		float baseSpeed = mc.thePlayer.capabilities.getWalkSpeed() * 2.873f;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			final int ampl = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0 + (0.2 * ampl);
		}
		return baseSpeed;
	}
	
	public static float getBaseSpeedFuncraft() {
		float baseSpeed = mc.thePlayer.capabilities.getWalkSpeed() * 2.873f;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			final int ampl = 1 + mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0 + (0.2 * ampl);
		}
		return baseSpeed;
	}

	public static void setMotion(EventStrafe eventStrafe, double speed) {
		mc.thePlayer.motionX = 0;
		mc.thePlayer.motionZ = 0;
		//speed *= eventStrafe.strafe != 0 && eventStrafe.forward != 0 ? 0.91 : 1;
		eventStrafe.friction = (float) speed;
	}

	public static void setMotionPartialStrafe(EventStrafe eventStrafe, float friction, float strafeComponent) {
		float remainder = 1F - strafeComponent;
		if (mc.thePlayer.onGround) {
			setMotion(eventStrafe, friction);
		} else {
			mc.thePlayer.motionX *= strafeComponent;
			mc.thePlayer.motionZ *= strafeComponent;
			eventStrafe.friction = friction * remainder;
		}
	}
	
}
