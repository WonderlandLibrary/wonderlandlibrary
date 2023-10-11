package artemis.util;

import net.minecraft.client.Minecraft;

public enum MoveUtil {

	instance;
				
	public double getPosForSetPosX(double value) {
		double yaw = Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw);
		double x = -Math.sin(yaw) * value;
		return x;
	}

	public double getPosForSetPosZ(double value) {
		double yaw = Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw);
		double z = Math.cos(yaw) * value;
		return z;
	}

}