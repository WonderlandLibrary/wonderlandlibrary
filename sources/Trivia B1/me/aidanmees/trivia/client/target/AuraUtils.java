package me.aidanmees.trivia.client.target;

import java.util.ArrayList;

import me.aidanmees.trivia.client.settings.ClientSettings;
import me.aidanmees.trivia.client.tools.Helper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class AuraUtils {

	public static ArrayList<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
	public static ArrayList<EntityLivingBase> blackList = new ArrayList<EntityLivingBase>();

	public static boolean hasEntity(Entity en) {
		if (en == null) {
			return false;
		}
		if (!AuraUtils.targets.isEmpty()) {
			for (EntityLivingBase en1 : AuraUtils.targets) {
				if (en1 == null) {
					continue;
				}
				if (en1.isEntityEqual(en)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean blackEntity(Entity en) {
		if (en == null) {
			return false;
		}
		if (!AuraUtils.blackList.isEmpty()) {
			for (EntityLivingBase en1 : AuraUtils.blackList) {
				if (en1 == null) {
					continue;
				}
				if (en1.isEntityEqual(en)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean disableAura = false;
	private static boolean reachExploit = false;
	private static int timercap = 15;
	private static double range = 7;
	private static boolean headsnap = false;
	private static double chargerange = 8.0;
	
	public static boolean getDisableAura() {
		return disableAura;
	}

	public static void setDisableAura(boolean disableAura) {
		AuraUtils.disableAura = disableAura;
	}

	public static void setReachExploit(boolean reachExploit) {
		AuraUtils.reachExploit = reachExploit;
	}

	public static boolean isReachExploit() {
		return reachExploit;
	}

	private static double packetTPRange = 10;

	public static double getPacketTPRange() {
		return packetTPRange;
	}

	public static void setPacketTPRange(double packetTPRange) {
		AuraUtils.packetTPRange = packetTPRange;
	}

	public static double getRange() {
		return range;
	}

	public static boolean getHeadsnap() {
		return headsnap;
	}

	public static int getAPS() {
		return timercap;
	}

	public static void setTimer(int set) {
		timercap = set;
	}

	public static void setRange(double value) {
		range = value;
	}

	public static void setHeadSnap(boolean selected) {
		headsnap = selected;
	}

	public static double getChargeRange() {
		return chargerange;
	}

	public static void setChargeRange(double chargerange) {
		AuraUtils.chargerange = chargerange;
	}

	public static double getSmoothAimSpeed() {
		return ClientSettings.smoothAimSpeed;
	}

	public static void setSmoothAimSpeed(double smoothAimSpeed) {
		ClientSettings.smoothAimSpeed = smoothAimSpeed;
	}

	public static double[] getRotationToEntity(Entity entity) {
        double pX = Helper.player().posX;
        double pY = Helper.player().posY + (Helper.player().getEyeHeight());
        double pZ = Helper.player().posZ;

        double eX = entity.posX;
        double eY = entity.posY + (entity.height/2);
        double eZ = entity.posZ;

        double dX = pX - eX;
        double dY = pY - eY;
        double dZ = pZ - eZ;
        double dH = Math.sqrt(Math.pow(dX, 2) + Math.pow(dZ, 2));

        double yaw = (Math.toDegrees(Math.atan2(dZ, dX)) + 90);
        double pitch = (Math.toDegrees(Math.atan2(dH, dY)));

        return new double[]{yaw, 90 - pitch};
    }


	public static double angleDifference(double a, double b) {
		return ((((a - b) % 360D) + 540D) % 360D) - 180D;
	}


    public boolean isWithingFOV(Entity en, float angle) {
        angle *= 0.5;
        double angleDifference = angleDifference(Helper.player().rotationYaw , Helper.entityUtils().getRotationToEntity(en)[0]);
        return (angleDifference > 0 && angleDifference < angle) || (-angle < angleDifference && angleDifference < 0);
    }
}
