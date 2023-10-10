
package Reality.Realii.event.events.world;

import Reality.Realii.event.Event;
import Reality.Realii.utils.cheats.player.Helper;
import Reality.Realii.utils.math.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import Reality.Realii.utils.cheats.player.PtichHelper;
import java.util.ArrayList;

public class EventSilentMove
extends Event {

	public boolean silent, advanced;
	public float yaw;

	public EventSilentMove(float yaw) {
		this.yaw = yaw;
	}

	public boolean isSilent() {
		return silent;
	}

	public void setSilent(boolean silent) {
		this.silent = silent;
	}

	public boolean isAdvanced() {
		return advanced;
	}

	public void setAdvanced(boolean advanced) {
		this.advanced = advanced;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	public static double[] getMotion(final double speed, final float strafe, final float forward, final float yaw) {
		final float friction = (float)speed;
		final float f1 = MathHelper.sin(yaw * 3.1415927f / 180.0f);
		final float f2 = MathHelper.cos(yaw * 3.1415927f / 180.0f);
		final double motionX = strafe * friction * f2 - forward * friction * f1;
		final double motionZ = forward * friction * f2 + strafe * friction * f1;
		return new double[] { motionX, motionZ };
	}


	public float[] mySilentStrafe(float strafe, float forward, float yaw, boolean advanced) {
		Minecraft mc = Minecraft.getMinecraft();
		float diff = MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw);
		float newForward = 0.0f;
		float newStrafe = 0.0f;
		if (!advanced) {
			if ((double)diff >= 22.5 && (double)diff < 67.5) {
				newStrafe += strafe;
				newForward += forward;
				newStrafe -= forward;
				newForward += strafe;
			} else if ((double)diff >= 67.5 && (double)diff < 112.5) {
				newStrafe -= forward;
				newForward += strafe;
			} else if ((double)diff >= 112.5 && (double)diff < 157.5) {
				newStrafe -= strafe;
				newForward -= forward;
				newStrafe -= forward;
				newForward += strafe;
			} else if ((double)diff >= 157.5 || (double)diff <= -157.5) {
				newStrafe -= strafe;
				newForward -= forward;
			} else if ((double)diff > -157.5 && (double)diff <= -112.5) {
				newStrafe -= strafe;
				newForward -= forward;
				newStrafe += forward;
				newForward -= strafe;
			} else if ((double)diff > -112.5 && (double)diff <= -67.5) {
				newStrafe += forward;
				newForward -= strafe;
			} else if ((double)diff > -67.5 && (double)diff <= -22.5) {
				newStrafe += strafe;
				newForward += forward;
				newStrafe += forward;
				newForward -= strafe;
			} else {
				newStrafe += strafe;
				newForward += forward;
			}
		} else {
			double[] realMotion = getMotion(0.22, strafe, forward, PtichHelper.realYaw);
			double[] realPos = new double[]{mc.thePlayer.posX, mc.thePlayer.posZ};
			realPos[0] = realPos[0] + realMotion[0];
			realPos[1] = realPos[1] + realMotion[1];
			ArrayList<float[]> possibleForwardStrafe = new ArrayList<float[]>();
			int i = 0;
			boolean b = false;
			while (!b) {
				newForward = 0.0f;
				newStrafe = 0.0f;
				if (i == 0) {
					newStrafe += strafe;
					newForward += forward;
					possibleForwardStrafe.add(new float[]{newForward += strafe, newStrafe -= forward});
				} else if (i == 1) {
					possibleForwardStrafe.add(new float[]{newForward += strafe, newStrafe -= forward});
				} else if (i == 2) {
					newStrafe -= strafe;
					newForward -= forward;
					possibleForwardStrafe.add(new float[]{newForward += strafe, newStrafe -= forward});
				} else if (i == 3) {
					possibleForwardStrafe.add(new float[]{newForward -= forward, newStrafe -= strafe});
				} else if (i == 4) {
					newStrafe -= strafe;
					newForward -= forward;
					possibleForwardStrafe.add(new float[]{newForward -= strafe, newStrafe += forward});
				} else if (i == 5) {
					possibleForwardStrafe.add(new float[]{newForward -= strafe, newStrafe += forward});
				} else if (i == 6) {
					newStrafe += strafe;
					newForward += forward;
					possibleForwardStrafe.add(new float[]{newForward -= strafe, newStrafe += forward});
				} else {
					possibleForwardStrafe.add(new float[]{newForward += forward, newStrafe += strafe});
					b = true;
				}
				++i;
			}
			double distance = 5000.0;
			float[] floats = new float[2];
			for (float[] flo : possibleForwardStrafe) {
				double diffZ;
				if (flo[0] > 1.0f) {
					flo[0] = 1.0f;
				} else if (flo[0] < -1.0f) {
					flo[0] = -1.0f;
				}
				if (flo[1] > 1.0f) {
					flo[1] = 1.0f;
				} else if (flo[1] < -1.0f) {
					flo[1] = -1.0f;
				}
				double[] motion = getMotion(0.22, flo[1], flo[0], mc.thePlayer.rotationYaw);
				motion[0] = motion[0] + mc.thePlayer.posX;
				motion[1] = motion[1] + mc.thePlayer.posZ;
				double diffX = Math.abs(realPos[0] - motion[0]);
				double d0 = diffX * diffX + (diffZ = Math.abs(realPos[1] - motion[1])) * diffZ;
				if (!(d0 < distance)) continue;
				distance = d0;
				floats = flo;
			}
			return new float[]{floats[1], floats[0]};
		}
		return new float[]{newStrafe, newForward};
	}
    
    



}

